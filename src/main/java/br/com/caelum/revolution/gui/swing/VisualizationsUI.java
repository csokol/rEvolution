package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsVisualization;
import br.com.caelum.revolution.config.MapConfig;
import br.com.caelum.revolution.config.TwoConfigs;
import br.com.caelum.revolution.persistence.HibernatePersistence;
import br.com.caelum.revolution.scanner.ClassScan;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;

public class VisualizationsUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JToolBar toolBar;
	private JPanel contentPane;
	private JTabbedPane panels;
	private Config config;
	private HibernatePersistence persistence;
	private final ClassScan scan;
	private final StringsToDataArrayConverter configConverter;
	private JTextField width;
	private JTextField height;

	public VisualizationsUI(Config config, ClassScan scan, StringsToDataArrayConverter configConverter) {
		
		super("rEvolution");
		this.config = config;
		this.scan = scan;
		this.configConverter = configConverter;
		
		contentPane = new JPanel(new BorderLayout());
		this.setContentPane(contentPane);

		
		loadPersistence();

		createMenus();
		createToolBar();
		createPanels();

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private void loadPersistence() {
		persistence = new HibernatePersistence(config);
		persistence.initMechanism();
		persistence.openSession();
	}

	private void createPanels() {

		panels = new JTabbedPane();
		try {
			Set<String> tabs = scan.findAll(IsVisualization.class);
			
			for(String clazzName : tabs) {
				Class<?> clazz = Class.forName(clazzName);
				IsVisualization visualizationConfigs = (IsVisualization) clazz.getAnnotation(IsVisualization.class);
				panels.add(visualizationConfigs.name(), createPanel(clazz, visualizationConfigs.configs()));
			}

			contentPane.add(panels, BorderLayout.CENTER);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private JPanel createPanel(final Class<?> clazz, String[] configs) {
		final JPanel panel = new JPanel(new BorderLayout());
		final JButton visualize = new JButton("Generate");
		final JButton export = new JButton("Export");
		
		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(visualize);
		buttons.add(export);
		panel.add(buttons, BorderLayout.SOUTH);
		
		String[][] data = configConverter.transformConfigNamesToJTableDataObject(configs);
		final JTable configTable = new JTable(data, new String[] {"Property", "Value"});
		panel.add(configTable, BorderLayout.NORTH);

		export.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				Config extendedCfgs = createConfigBasedOn(configTable);
				
					JFileChooser fc = new JFileChooser();
					int result = fc.showSaveDialog(null);
					
					if(result == JFileChooser.APPROVE_OPTION) {
						exportVisualization(fc.getSelectedFile(), clazz, panel, extendedCfgs);
						JOptionPane.showMessageDialog(null, "Visualization exported successfully!");
					}
			}
		});
		
		visualize.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				Config extendedCfgs = createConfigBasedOn(configTable);
				
				try {
					exportVisualization(new File("temp"), clazz, panel, extendedCfgs);
					BufferedImage image = ImageIO.read(new File("temp"));

					
					JLabel picture = new JLabel(new ImageIcon(image));
					Component scrolledPicture = new JScrollPane(picture);
					panel.add(scrolledPicture, BorderLayout.CENTER);
					
					panel.revalidate();
					panel.repaint();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return panel;
		
	}
	
	private void exportVisualization(File file, final Class<?> clazz, final JPanel panel, Config extendedCfgs) {
		
		try {
			OutputStream pout = new FileOutputStream(file);
	
			SpecificVisualizationFactory factory = (SpecificVisualizationFactory)clazz.newInstance(); 
			Visualization visualization = factory.build(new TwoConfigs(config, extendedCfgs));
			visualization.setSession(persistence.getSession());
			visualization.exportTo(pout, Integer.valueOf(width.getText()), Integer.valueOf(height.getText()));
			
			Component c = ((BorderLayout)panel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
			if(c!=null) panel.remove(c);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private MapConfig createConfigBasedOn(JTable configTable) {
		Map<String, String> tempConfigs = new HashMap<String, String>();
		
		for(int i = 0; i < configTable.getRowCount(); i++) {
			tempConfigs.put((String)configTable.getValueAt(i, 0), (String)configTable.getValueAt(i, 1));
		}
		
		return new MapConfig(tempConfigs);
	}

	
	private void createToolBar() {
		toolBar = new JToolBar();

		
		toolBar.add(new JLabel("Width:"));
		width = new JTextField();
		width.setText("1000");
		toolBar.add(width);
		
		toolBar.add(new JLabel("Height:"));
		height = new JTextField();
		height.setText("1000");
		toolBar.add(height);
		
		contentPane.add(toolBar, BorderLayout.PAGE_START);
	}

	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu revolutionMenu = new JMenu("rEvolution");
		revolutionMenu.setMnemonic(KeyEvent.VK_R);
		
		JMenuItem about = new JMenuItem("About");
		revolutionMenu.add(about);

		menuBar.add(revolutionMenu);
		
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "rEvolution\nCreated By Mauricio Aniche (mauricioaniche@gmail.com)\nhttp://www.github.com/mauricioaniche/rEvolution");
			}
		});

		this.setJMenuBar(menuBar);
	}

}

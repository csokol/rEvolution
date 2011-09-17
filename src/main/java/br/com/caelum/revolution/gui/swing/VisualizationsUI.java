package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

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
		panel.add(visualize, BorderLayout.SOUTH);
		
		String[][] data = configConverter.transformConfigNamesToJTableDataObject(configs);
		final JTable configTable = new JTable(data, new String[] {"Property", "Value"});
		panel.add(configTable, BorderLayout.NORTH);

		visualize.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				Config extendedCfgs = createConfigBasedOn(configTable);
				
				try {
					OutputStream pout = new FileOutputStream(new File("temp.jpg"));

					SpecificVisualizationFactory factory = (SpecificVisualizationFactory)clazz.newInstance(); 
					Visualization visualization = factory.build(new TwoConfigs(config, extendedCfgs));
					visualization.setSession(persistence.getSession());
					visualization.exportTo(pout, 1000, 1000);
					
					BufferedImage image = ImageIO.read(new File("temp.jpg"));
					JLabel picture = new JLabel(new ImageIcon(image));
					panel.add(picture, BorderLayout.CENTER);
					
					panel.revalidate();
					panel.repaint();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

		});
		return panel;
		
	}

	private MapConfig createConfigBasedOn(JTable configTable) {
		Map<String, String> tempConfigs = new HashMap<String, String>();
		
		for(int i = 0; i < configTable.getRowCount(); i++) {
			tempConfigs.put((String)configTable.getValueAt(i, 0), (String)configTable.getValueAt(i, 1));
		}
		
		return new MapConfig(tempConfigs);
	}

	
	private void createToolBar() {
		toolBar = new JToolBar("Bla");

		toolBar.add(createButton("v1", "tooltip", "name1"));
		toolBar.add(createButton("v2", "tooltip", "name2"));
		toolBar.add(createButton("v3", "tooltip", "name3"));
		contentPane.add(toolBar, BorderLayout.PAGE_START);
	}

	protected JButton createButton(String actionCommand, String toolTipText,
			String altText) {
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		// button.addActionListener(this);

		button.setText(altText);

		return button;
	}

	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu rEvolutionMenu = new JMenu("rEvolution");
		rEvolutionMenu.setMnemonic(KeyEvent.VK_R);
		JMenuItem newProjectItem = new JMenuItem("New", KeyEvent.VK_N);
		newProjectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		rEvolutionMenu.add(newProjectItem);

		JMenuItem openProjectItem = new JMenuItem("Open", KeyEvent.VK_O);
		openProjectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		rEvolutionMenu.add(openProjectItem);

		JMenu aboutMenu = new JMenu("About");
		aboutMenu.setMnemonic(KeyEvent.VK_A);

		menuBar.add(rEvolutionMenu);
		menuBar.add(aboutMenu);

		this.setJMenuBar(menuBar);
	}

}

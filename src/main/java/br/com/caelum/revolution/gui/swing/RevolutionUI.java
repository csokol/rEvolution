package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.PropertiesConfig;
import br.com.caelum.revolution.persistence.HibernatePersistence;

public class RevolutionUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JToolBar toolBar;
	private JPanel contentPane;
	private JTabbedPane panels;
	private Config config;
	private HibernatePersistence persistence;

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, FileNotFoundException, IOException {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());
		new RevolutionUI(
				"/Users/mauricioaniche/dev/workspace/rEvolution/dist/caelumweb.properties")
				.setVisible(true);

	}

	public RevolutionUI(String configFile) throws FileNotFoundException,
			IOException {
		super("rEvolution");

		contentPane = new JPanel(new BorderLayout());
		this.setContentPane(contentPane);

		loadConfigFrom(configFile);
		loadPersistence();

		createMenus();
		createToolBar();
		createPanels();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(700, 450));
		pack();
	}

	private void loadPersistence() {
		persistence = new HibernatePersistence(config);
		persistence.initMechanism();
		persistence.openSession();
	}

	private void loadConfigFrom(String configFile) throws IOException,
			FileNotFoundException {
		this.config = new PropertiesConfig(new FileInputStream(configFile));
	}

	private void createPanels() {

		panels = new JTabbedPane();
		try {
			Set<String> tabs = getAllVisualizations();
			
			for(String clazzName : tabs) {
				Class<?> clazz = Class.forName(clazzName);
				VisualizationHasUI tab = (VisualizationHasUI) clazz.newInstance();
				panels.add(tab.tabName(), tab.panel(config, persistence.getSession()));
			}

			contentPane.add(panels, BorderLayout.CENTER);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Set<String> getAllVisualizations() throws IOException {
		URL url = ClasspathUrlFinder.findClassBase(RevolutionUI.class);
		AnnotationDB db = new AnnotationDB();
		db.scanArchives(url);

		Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
		Set<String> tabs = annotationIndex.get(UX.class.getName());
		return tabs;
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

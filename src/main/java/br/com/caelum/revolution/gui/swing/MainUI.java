package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import br.com.caelum.revolution.config.DatabaseConfigTemplate;
import br.com.caelum.revolution.config.MapConfig;
import br.com.caelum.revolution.scanner.ClassScan;

import com.thoughtworks.xstream.XStream;

public class MainUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField host;
	private JTextField user;
	private JTextField password;
	private JButton crawl;
	private JButton visualize;
	private JTextField schema;
	private DatabaseConfigTemplate databaseConfig;
	private List<SavedConfig> savedConfigs;
	private JPanel configsPanel;

	public MainUI() {
		super("rEvolution");
		setLayout(new BorderLayout());
		databaseConfig = new DatabaseConfigTemplate();

		loadSavedConfigs();
		createHeader();
		createSavedConnections();
		createConfigTextBoxes();
		createButtons();
		setActionsToButtons();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(500, 300));
		pack();
	}

	@SuppressWarnings("unchecked")
	private void loadSavedConfigs() {

		try {
			XStream xs = new XStream();
			this.savedConfigs = new ArrayList<SavedConfig>(
					(List<SavedConfig>) xs.fromXML(new FileInputStream(
							new File("saved-configs"))));
		} catch (Exception e) {
			this.savedConfigs = new ArrayList<SavedConfig>();
		}

	}

	private void createSavedConnections() {
		if (configsPanel != null)
			this.remove(configsPanel);
		configsPanel = new JPanel(new BorderLayout());

		final JTree tree = new JTree(createNodesBasedOnConfig());
		JScrollPane treeView = new JScrollPane(tree);
		configsPanel.add(treeView, BorderLayout.CENTER);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		getSavedConfigWhenUserSelectsInOnThe(tree);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		
		JButton save = new JButton("Save New Connection");
		configsPanel.add(save, BorderLayout.SOUTH);
		saveNewConfigWhenClickAtButton(save);
		buttons.add(save);

		JButton delete = new JButton("Delete Connection");
		deleteConfigWhenClickAtButton(tree, delete);
		buttons.add(delete);

		configsPanel.add(buttons, BorderLayout.SOUTH);
		add(configsPanel, BorderLayout.WEST);
		
		validate();
		repaint();
	}

	private void deleteConfigWhenClickAtButton(final JTree tree, JButton delete) {
		delete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				if (node == null)
					return;

				Object nodeInfo = node.getUserObject();
				if (node.isLeaf()) {
					savedConfigs.remove(nodeInfo);
					saveConfigsIntoFile();
					createSavedConnections();
				}
			}
		});
	}

	private DefaultMutableTreeNode createNodesBasedOnConfig() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"Saved Connections");
		for (SavedConfig config : savedConfigs) {
			top.add(new DefaultMutableTreeNode(config));
		}
		return top;
	}

	private void saveNewConfigWhenClickAtButton(JButton save) {
		save.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				String name = JOptionPane.showInputDialog(null,
						"What is the name of this connection?");
				savedConfigs.add(new SavedConfig(name, host.getText(), schema
						.getText(), user.getText(), password.getText()));

				saveConfigsIntoFile();
				createSavedConnections();

			}

		});
	}

	private void saveConfigsIntoFile() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("saved-configs"));
			XStream xs = new XStream();
			bw.write(xs.toXML(savedConfigs));
			bw.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void getSavedConfigWhenUserSelectsInOnThe(final JTree tree) {
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent evt) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				if (node == null)
					return;

				Object nodeInfo = node.getUserObject();
				if (node.isLeaf()) {
					SavedConfig cfg = (SavedConfig) nodeInfo;
					host.setText(cfg.getHost());
					schema.setText(cfg.getSchema());
					user.setText(cfg.getUser());
					password.setText(cfg.getPassword());
				}
			}
		});
	}

	private void createHeader() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Select the database you want to connect."));
		add(panel, BorderLayout.NORTH);
	}

	private void setActionsToButtons() {
		visualize.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				MapConfig config = databaseConfig.configBasedOnInput(
						host.getText(), schema.getText(), user.getText(),
						password.getText(), false);

				new VisualizationsUI(config, new ClassScan(),
						new StringsToDataArrayConverter()).setVisible(true);
				setVisible(false);
			}

		});

		crawl.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				MapConfig config = databaseConfig.configBasedOnInput(
						host.getText(), schema.getText(), user.getText(),
						password.getText(), true);

				new CrawlerUI(config, new ClassScan(),
						new StringsToDataArrayConverter()).setVisible(true);
				setVisible(false);
			}
		});

	}

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());
		new MainUI().setVisible(true);
	}

	private void createButtons() {
		JPanel panel = new JPanel();

		crawl = new JButton("Yes, crawl the repo!");
		visualize = new JButton("No, only visualize it!");

		panel.add(crawl);
		panel.add(visualize);

		add(panel, BorderLayout.SOUTH);

	}

	private void createConfigTextBoxes() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("Host"));
		this.host = new JTextField();
		panel.add(host);

		panel.add(new JLabel("Schema"));
		this.schema = new JTextField();
		panel.add(schema);

		panel.add(new JLabel("User"));
		this.user = new JTextField();
		panel.add(user);

		panel.add(new JLabel("Password"));
		this.password = new JTextField();
		panel.add(password);

		add(panel, BorderLayout.CENTER);

	}
}

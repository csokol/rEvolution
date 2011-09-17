package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.caelum.revolution.config.MapConfig;
import br.com.caelum.revolution.scanner.ClassScan;

public class ConfigUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField host;
	private JTextField user;
	private JTextField password;
	private JButton crawl;
	private JButton visualize;
	private JTextField schema;

	public ConfigUI() {
		super("rEvolution");
		setLayout(new BorderLayout());

		createConfigTextBoxes();
		createButtons();
		setActionsToButtons();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(500, 200));
		pack();
	}

	// TODO: extract to class
	private MapConfig configBasedOnInput() {
		Map<String, String> cfgs = new HashMap<String, String>();

		cfgs.put("driver_class", "com.mysql.jdbc.Driver");
		cfgs.put("connection_string", "jdbc:mysql://" + host.getText()
				+ "/" + schema.getText()
				+ "?useUnicode=true&characterEncoding=UTF-8");
		cfgs.put("dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
		cfgs.put("db_user", user.getText());
		cfgs.put("db_pwd", password.getText());
		cfgs.put("create_tables", "false");

		MapConfig config = new MapConfig(cfgs);
		return config;
	}
	
	private void setActionsToButtons() {
		visualize.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				MapConfig config = configBasedOnInput();
				
				new VisualizationsUI(config, new ClassScan())
						.setVisible(true);
				setVisible(false);
			}

		});

		crawl.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				Map<String, String> cfgs = new HashMap<String, String>();

				cfgs.put("driver_class", "com.mysql.jdbc.Driver");
				cfgs.put("connection_string", "jdbc:mysql://" + host.getText()
						+ "/" + schema.getText()
						+ "?useUnicode=true&characterEncoding=UTF-8");
				cfgs.put("dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
				cfgs.put("db_user", user.getText());
				cfgs.put("db_pwd", password.getText());
				cfgs.put("create_tables", "true");

				new CrawlerUI(new MapConfig(cfgs), new ClassScan())
						.setVisible(true);
				setVisible(false);
			}
		});

	}

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());
		new ConfigUI().setVisible(true);
	}

	private void createButtons() {
		JPanel panel = new JPanel();

		crawl = new JButton("Crawl the repository");
		visualize = new JButton("Visualize the repository");

		panel.add(crawl);
		panel.add(visualize);

		add(panel, BorderLayout.SOUTH);

	}

	private void createConfigTextBoxes() {
		JPanel panel = new JPanel(new GridLayout(4, 2));

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

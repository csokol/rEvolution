package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.caelum.revolution.config.DatabaseConfigTemplate;
import br.com.caelum.revolution.config.MapConfig;
import br.com.caelum.revolution.scanner.ClassScan;

public class MainUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField host;
	private JTextField user;
	private JTextField password;
	private JButton crawl;
	private JButton visualize;
	private JTextField schema;
	private DatabaseConfigTemplate databaseConfig;

	public MainUI() {
		super("rEvolution");
		setLayout(new BorderLayout());
		databaseConfig = new DatabaseConfigTemplate();

		createConfigTextBoxes();
		createButtons();
		setActionsToButtons();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(500, 200));
		pack();
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

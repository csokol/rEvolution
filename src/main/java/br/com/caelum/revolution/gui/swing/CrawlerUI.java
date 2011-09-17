package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsSCM;
import br.com.caelum.revolution.scanner.ClassScan;

public class CrawlerUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JComboBox scm;
	private JTable scmTable;
	private final Config config;
	private final ClassScan scan;
	private Map<String, Class<?>> scms;

	public CrawlerUI(Config config, ClassScan scan) {
		super("rEvolution");
		this.config = config;
		this.scan = scan;
		scms = new HashMap<String, Class<?>>();

		setLayout(new BorderLayout());

		createTabs();

		createButtons();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(700, 450));
		pack();
	}

	private void createButtons() {
		JButton crawl = new JButton("Crawl!");
		add(crawl, BorderLayout.SOUTH);
	}

	private void createTabs() {
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("SCM", createSCM());

		add(tabs, BorderLayout.CENTER);

	}

	private JPanel createSCM() {
		List<String> names = new ArrayList<String>();
		names.add("Choose one...");

		Set<String> scmClasses = scan.findAll(IsSCM.class);
		try {
			for (String clazzName : scmClasses) {
				Class<?> clazz = Class.forName(clazzName);
				IsSCM scmDetails = clazz.getAnnotation(IsSCM.class);
				names.add(scmDetails.name());
				scms.put(scmDetails.name(), clazz);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JPanel header = new JPanel();
		header.add(new JLabel("Source Code Repository"));
		this.scm = new JComboBox(names.toArray());
		header.add(scm);
		panel.add(header, BorderLayout.NORTH);

		scm.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				JComboBox cb = (JComboBox)ae.getSource();
				if (cb.getSelectedIndex() > 0) {
					String selectedScm = (String) cb.getSelectedItem();
					IsSCM scmDetails = scms.get(selectedScm).getAnnotation(
							IsSCM.class);
					String[][] data = new String[scmDetails.configs().length][2];
					for (int i = 0; i < scmDetails.configs().length; i++) {
						data[i][0] = scmDetails.configs()[i];
					}
					scmTable = new JTable(data, new String[] { "Property",
							"Value" });
					panel.add(scmTable, BorderLayout.CENTER);
					panel.revalidate();
					panel.repaint();
				}
			}
		});

		return panel;
	}
}

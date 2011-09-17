package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import br.com.caelum.revolution.analyzers.AnalyzerRunner;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.IsBuild;
import br.com.caelum.revolution.config.IsChangeSet;
import br.com.caelum.revolution.config.IsSCM;
import br.com.caelum.revolution.config.IsTool;
import br.com.caelum.revolution.config.MapConfig;
import br.com.caelum.revolution.config.TwoConfigs;
import br.com.caelum.revolution.gui.commandline.AnalyzerFactory;
import br.com.caelum.revolution.scanner.ClassScan;

public class CrawlerUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private final Config config;
	private final ClassScan scan;

	private JTable scmTable;
	private JComboBox scm;
	private Map<String, Class<?>> allSCMs;

	private JTable changeSetsTable;
	private JComboBox changeSets;
	private Map<String, Class<?>> allChangeSets;

	private JTable buildsTable;
	private JComboBox builds;
	private Map<String, Class<?>> allBuilds;

	private Map<Class<?>, JPanel> allTools;

	private final StringsToDataArrayConverter configConverter;

	public CrawlerUI(Config config, ClassScan scan,
			StringsToDataArrayConverter configConverter) {
		super("rEvolution");
		this.config = config;
		this.scan = scan;
		this.configConverter = configConverter;
		allSCMs = new HashMap<String, Class<?>>();
		allChangeSets = new HashMap<String, Class<?>>();
		allBuilds = new HashMap<String, Class<?>>();
		allTools = new HashMap<Class<?>, JPanel>();

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
		
		crawl.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				Map<String, String> cfgs = new HashMap<String, String>();
				
				putSCMOn(cfgs);
				putChangeSetsOn(cfgs);
				putBuildOn(cfgs);
				putToolsOn(cfgs);
				
				AnalyzerRunner analyzerRunner = new AnalyzerFactory().basedOn(new TwoConfigs(config, new MapConfig(cfgs)));
				analyzerRunner.start();
			}

			private void putToolsOn(Map<String, String> cfgs) {
				
				int i = 1;
				for(Map.Entry<Class<?>, JPanel> e : allTools.entrySet()){
					JPanel panel = e.getValue();
					JCheckBox ok = (JCheckBox) ((BorderLayout)panel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
					
					cfgs.put("tools.1", e.getKey().getName());
					
					if(ok.isSelected()) {
						JTable table = (JTable) ((BorderLayout)panel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
						getConfigsFromTable("tools.1.", cfgs, table);
						i++;
					}
				}
			}

			private void putChangeSetsOn(Map<String, String> cfgs) {
				String changeSetName = (String) changeSets.getSelectedItem();
				Class<?> changeSetClazz = allChangeSets.get(changeSetName);
				cfgs.put("changesets", changeSetClazz.getName());
				getConfigsFromTable(cfgs, changeSetsTable);
			}
			
			private void putBuildOn(Map<String, String> cfgs) {
				String buildName = (String) builds.getSelectedItem();
				Class<?> buildClazz = allBuilds.get(buildName);
				cfgs.put("build", buildClazz.getName());
				getConfigsFromTable(cfgs, buildsTable);
			}
			
			private void putSCMOn(Map<String, String> cfgs) {
				String scmName = (String) scm.getSelectedItem();
				Class<?> scmClazz = allSCMs.get(scmName);
				cfgs.put("scm", scmClazz.getName());
				getConfigsFromTable(cfgs, scmTable);
			}

		});
	}

	private void getConfigsFromTable(String prefix, Map<String, String> cfgs, JTable table) {
		for(int i = 0; i < table.getRowCount(); i++){
			cfgs.put(prefix + (String)table.getValueAt(i, 0),(String)table.getValueAt(i, 1));
		}
	}
	
	private void getConfigsFromTable(Map<String, String> cfgs, JTable table) {
		getConfigsFromTable("", cfgs, table);
	}

	private void createTabs() {
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("SCM", createSCM());
		tabs.addTab("Changesets", createChangesets());
		tabs.addTab("Builds", createBuilds());
		tabs.addTab("Tools", createTools());

		add(tabs, BorderLayout.CENTER);

	}

	private Component createChangesets() {
		List<String> names = new ArrayList<String>();
		names.add("Choose one...");

		Set<String> changeSetClasses = scan.findAll(IsChangeSet.class);
		try {
			for (String clazzName : changeSetClasses) {
				Class<?> clazz = Class.forName(clazzName);
				IsChangeSet changeSetDetails = clazz.getAnnotation(IsChangeSet.class);
				names.add(changeSetDetails.name());
				allChangeSets.put(changeSetDetails.name(), clazz);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JPanel header = new JPanel();
		header.add(new JLabel("Changesets"));
		this.changeSets = new JComboBox(names.toArray());
		header.add(changeSets);
		panel.add(header, BorderLayout.NORTH);
		
		changeSetsTable = new JTable();
		panel.add(changeSetsTable, BorderLayout.CENTER);

		changeSets.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				JComboBox cb = (JComboBox) ae.getSource();
				if (cb.getSelectedIndex() > 0) {
					String selectedChangeSet = (String) cb.getSelectedItem();
					IsChangeSet changeSetDetails = allChangeSets.get(selectedChangeSet).getAnnotation(IsChangeSet.class);
					
					replaceWithNewChangeSetConfigs(panel, changeSetDetails);
				}
			}

			private void replaceWithNewChangeSetConfigs(final JPanel panel,
					IsChangeSet changeSetDetails) {
				Object[][] data = configConverter.transformConfigNamesToJTableDataObject(changeSetDetails.configs());
				
				panel.remove( ((BorderLayout)panel.getLayout()).getLayoutComponent(BorderLayout.CENTER) );
				changeSetsTable = new JTable(data, new String[] { "Property","Value" });
				panel.add(changeSetsTable, BorderLayout.CENTER);
				panel.revalidate();
				panel.repaint();
			}
		});

		return panel;
	}

	private Component createBuilds() {
		List<String> names = new ArrayList<String>();
		names.add("Choose one...");

		Set<String> buildClasses = scan.findAll(IsBuild.class);
		try {
			for (String clazzName : buildClasses) {
				Class<?> clazz = Class.forName(clazzName);
				IsBuild buildDetails = clazz.getAnnotation(IsBuild.class);
				names.add(buildDetails.name());
				allBuilds.put(buildDetails.name(), clazz);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JPanel header = new JPanel();
		header.add(new JLabel("Builds"));
		this.builds = new JComboBox(names.toArray());
		header.add(builds);
		panel.add(header, BorderLayout.NORTH);
		
		buildsTable = new JTable();
		panel.add(buildsTable, BorderLayout.CENTER);

		builds.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				JComboBox cb = (JComboBox) ae.getSource();
				if (cb.getSelectedIndex() > 0) {
					String selectedBuild = (String) cb.getSelectedItem();
					IsBuild buildDetails = (IsBuild) allBuilds.get(selectedBuild).getAnnotation(IsBuild.class);
					
					replaceWithNewBuildConfigs(panel, buildDetails);
				}
			}

			private void replaceWithNewBuildConfigs(final JPanel panel,
					IsBuild changeSetDetails) {
				Object[][] data = configConverter.transformConfigNamesToJTableDataObject(changeSetDetails.configs());
				
				panel.remove( ((BorderLayout)panel.getLayout()).getLayoutComponent(BorderLayout.CENTER) );
				buildsTable = new JTable(data, new String[] { "Property","Value" });
				panel.add(buildsTable, BorderLayout.CENTER);
				panel.revalidate();
				panel.repaint();
			}
		});

		return panel;
	}
	
	private Component createTools() {
		JPanel panel = new JPanel(new BorderLayout());

		JTabbedPane toolsPanel = new JTabbedPane();

		Set<String> toolClasses = scan.findAll(IsTool.class);
		try {
			for (String clazzName : toolClasses) {
				Class<?> clazz = Class.forName(clazzName);
				IsTool toolDetails = clazz.getAnnotation(IsTool.class);

				JPanel toolConfigPanel = new JPanel(new BorderLayout());
				toolsPanel.add(toolDetails.name(), toolConfigPanel);

				Object[][] data = configConverter.transformConfigNamesToJTableDataObject(toolDetails.configs());
				JTable table = new JTable(data, new String[] { "Property","Value" });

				allTools.put(clazz, toolConfigPanel);
				toolConfigPanel.add(new JCheckBox("Run this tool"), BorderLayout.NORTH);
				toolConfigPanel.add(table, BorderLayout.CENTER);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		panel.add(toolsPanel, BorderLayout.CENTER);
		return panel;
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
				allSCMs.put(scmDetails.name(), clazz);
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

		scmTable = new JTable();
		panel.add(scmTable, BorderLayout.CENTER);

		
		scm.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				JComboBox cb = (JComboBox) ae.getSource();
				if (cb.getSelectedIndex() > 0) {
					String selectedScm = (String) cb.getSelectedItem();
					IsSCM scmDetails = allSCMs.get(selectedScm).getAnnotation(IsSCM.class);
					
					replaceWithNewSCMConfigs(panel, scmDetails);
				}
			}

			private void replaceWithNewSCMConfigs(final JPanel panel, IsSCM scmDetails) {
				String[][] data = configConverter.transformConfigNamesToJTableDataObject(scmDetails.configs());
				
				panel.remove( ((BorderLayout)panel.getLayout()).getLayoutComponent(BorderLayout.CENTER) );
				scmTable = new JTable(data, new String[] { "Property","Value" });
				panel.add(scmTable, BorderLayout.CENTER);
				panel.revalidate();
				panel.repaint();
			}
		});

		return panel;
	}
}

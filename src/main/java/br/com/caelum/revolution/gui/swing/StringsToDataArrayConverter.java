package br.com.caelum.revolution.gui.swing;

public class StringsToDataArrayConverter {

	public String[][] transformConfigNamesToJTableDataObject(String[] configs) {
		String[][] data = new String[configs.length][2];
		for(int i = 0; i < configs.length; i++) {
			data[i][0] = configs[i];
		}
		return data;
	}
}

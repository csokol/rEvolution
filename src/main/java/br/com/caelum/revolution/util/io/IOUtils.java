package br.com.caelum.revolution.util.io;

import java.io.File;

public class IOUtils {

	public boolean removeDirectory(File directory) {

		// System.out.println("removeDirectory " + directory);

		if (directory == null)
			return false;
		if (!directory.exists())
			return true;
		if (!directory.isDirectory())
			return false;

		String[] list = directory.list();

		// Some JVMs return null for File.list() when the
		// directory is empty.
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File entry = new File(directory, list[i]);

				// System.out.println("\tremoving entry " + entry);

				if (entry.isDirectory()) {
					if (!removeDirectory(entry))
						return false;
				} else {
					if (!entry.delete())
						return false;
				}
			}
		}

		return directory.delete();
	}

}

package br.com.caelum.revolution.gui.swing;

public class SavedConfig {

	private final String host;
	private final String schema;
	private final String user;
	private final String password;
	private final String name;
	
	public SavedConfig(String name, String host, String schema, String user, String password) {
		this.name = name;
		this.host = host;
		this.schema = schema;
		this.user = user;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public String getHost() {
		return host;
	}
	public String getSchema() {
		return schema;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}

	public String toString() {
		return name;
	}
	
}

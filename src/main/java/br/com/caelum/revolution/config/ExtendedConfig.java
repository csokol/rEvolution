package br.com.caelum.revolution.config;

import java.util.Map;

public class ExtendedConfig implements Config {

	private final Config cfg;
	private final Map<String, String> extendedCfgs;

	public ExtendedConfig(Config cfg, Map<String, String> extendedCfgs) {
		this.cfg = cfg;
		this.extendedCfgs = extendedCfgs;
	}
	
	public String asString(String key) {
		if(cfg.contains(key)) return cfg.asString(key);
		
		String value = extendedCfgs.get(key);
		if(value == null) throw new ConfigNotFoundException("config not found: " + key);
		
		return value;
	}

	public boolean contains(String key) {
		return cfg.contains(key) || extendedCfgs.containsKey(key);
	}

	public int asInt(String key) {
		return Integer.parseInt(asString(key));
	}

}

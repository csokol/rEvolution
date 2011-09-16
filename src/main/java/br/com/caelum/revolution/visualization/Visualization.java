package br.com.caelum.revolution.visualization;

import java.io.OutputStream;

import org.hibernate.Session;

public interface Visualization {
	void exportTo(OutputStream os, int width, int height);
	void setSession(Session session);
}

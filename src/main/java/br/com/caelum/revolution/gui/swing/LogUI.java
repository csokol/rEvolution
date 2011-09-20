package br.com.caelum.revolution.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LogUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea logArea;
	private JScrollPane scroll;

	public LogUI() {
		super("Working...");
		
		setLayout(new BorderLayout());
		
		createTextArea();
		setLogToIt();
		
		setPreferredSize(new Dimension(550, 200));
		pack();
	}

	private void setLogToIt() {
		TextAreaAppender appender = new TextAreaAppender();
		TextAreaAppender.setTextArea(logArea);
		appender.setThreshold(Level.INFO);
		
		Logger.getRootLogger().addAppender(appender);
	}

	private void createTextArea() {
		logArea = new JTextArea();
		logArea.append("rEvolution is about to start...");
		
		scroll = new JScrollPane(logArea);
		
		add(scroll, BorderLayout.CENTER);
	}
	
}
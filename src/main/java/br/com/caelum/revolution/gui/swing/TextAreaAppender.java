package br.com.caelum.revolution.gui.swing;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaAppender extends WriterAppender {
	private static JTextArea logArea;

	public void append(final LoggingEvent event) {
		if (logArea == null)
			return;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				logArea.append(event.getMessage().toString() + "\n");
			}
		});
	}

	public void close() {

	}

	public boolean requiresLayout() {
		return false;
	}

	public static void setTextArea(JTextArea logArea) {
		TextAreaAppender.logArea = logArea;
		
	}
}

package br.com.caelum.revolution.visualization.statistic;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.ExtendedConfig;
import br.com.caelum.revolution.gui.swing.UX;
import br.com.caelum.revolution.gui.swing.VisualizationHasUI;
import br.com.caelum.revolution.visualization.SpecificVisualizationFactory;
import br.com.caelum.revolution.visualization.Visualization;
import br.com.caelum.revolution.visualization.common.MapToDataSetConverter;
import br.com.caelum.revolution.visualization.common.PieChart;
import br.com.caelum.revolution.visualization.common.ThresholdedGroupedDataVisualization;

@UX
public class CommitsPerDeveloperFactory implements SpecificVisualizationFactory, VisualizationHasUI {

	public Visualization build(Config config) {
		// new File(config.asString("file")), 1500, 1500,
		return new ThresholdedGroupedDataVisualization<BigInteger>(
				new PieChart("Commits per Developer", new MapToDataSetConverter()), 
				config.asInt("threshold"),
				"select a.name, count(1) qty from author a inner join commit c on c.author_id = a.id group by a.name order by qty desc");
	}
	
	public JPanel panel(final Config config, final Session session) {
		final JPanel panel = new JPanel(new BorderLayout());
		final JTextField txtThreshold = new JTextField();
		final JButton button = new JButton("Generate");

		panel.add(txtThreshold, BorderLayout.NORTH);
		panel.add(button, BorderLayout.SOUTH);
		
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				Map<String, String> extendedCfgs = new HashMap<String, String>();
				extendedCfgs.put("threshold", txtThreshold.getText());
				
				try {
					OutputStream pout = new FileOutputStream(new File("temp"));

					Visualization visualization = build(new ExtendedConfig(config, extendedCfgs));
					visualization.setSession(session);
					visualization.exportTo(pout, 1000, 1000);
					
					BufferedImage image = ImageIO.read(new File("temp"));
					JLabel picture = new JLabel(new ImageIcon(image));
					panel.add(picture, BorderLayout.CENTER);
					
					panel.revalidate();
					panel.repaint();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return panel;
		
	}

	public String tabName() {
		return "Committers";
	}

}

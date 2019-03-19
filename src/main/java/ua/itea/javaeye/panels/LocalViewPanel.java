/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.itea.javaeye.panels;

import javax.swing.JLabel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import ua.itea.javaeye.utils.JavaEyeUtils;

/**
 *
 * @author yevgen
 */
public class LocalViewPanel extends ViewPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7966377808938755807L;
	private final Webcam webcam;
	private final WebcamPanel webcamPanel;
	private final JLabel webcamFPSLabel = new JLabel();
	private final JLabel webcamResolutionLabel = new JLabel();

	public LocalViewPanel(Webcam webcam) {
		this.webcam = webcam;

		webcamPanel = new WebcamPanel(this.webcam);
		webcamPanel.setDoubleBuffered(true);
		setView(webcamPanel);
		viewTitle = "Local cam view";
	}

	@Override
	public void run() {
		super.run();
		System.out.println("LocalView runs on " + Thread.currentThread().getName());

		webcamFPSLabel.setText(String.format("%4.2f", webcam.getFPS()));
		webcamResolutionLabel
				.setText((int) webcam.getViewSize().getWidth() + "x" + (int) webcam.getViewSize().getHeight());

		info.add(new JLabel(" Webcam: "));
		info.add(new JLabel(webcam.getName()));
		info.add(new JLabel(" Webcam FPS: "));
		info.add(webcamFPSLabel);
		info.add(new JLabel(" View resolution:"));
		info.add(webcamResolutionLabel);
		info.add(new JLabel(" Local IP: "));
		info.add(new JLabel(JavaEyeUtils.localAddress.getHostAddress()));
		info.add(new JLabel(" Codec: "));
		info.add(new JLabel("H.264"));

		while (true) {
			try {
				Thread.sleep(500);
				webcamFPSLabel.setText(String.format("%4.2f", webcam.getFPS()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

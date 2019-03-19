/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.itea.javaeye.panels;

import java.awt.image.BufferedImage;

/**
 *
 * @author yevgen
 */
public class RemoteViewPanel extends ViewPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7928999741730995326L;

	private final VideoPanel videoPanel;

	public RemoteViewPanel() {
		this.videoPanel = new VideoPanel();

		setView(videoPanel);
		viewTitle = "Remote cam view";
	}

	public void updateImage(BufferedImage image) {
		videoPanel.updateImage(image);
	}

	public void close() {
		this.dispose();
		videoPanel.close();
	}

	@Override
	public void run() {
		super.run();
		System.out.println("RemoteView runs on " + Thread.currentThread().getName());

	}

}

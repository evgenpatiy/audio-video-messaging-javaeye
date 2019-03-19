/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.itea.javaeye.panels;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import ua.itea.javaeye.utils.JavaEyeUtils;
import ua.itea.javaeye.utils.Session;

/**
 *
 * @author yevgen
 */
public class RemoteViewPanel extends ViewPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7928999741730995326L;

	private final VideoPanel videoPanel;
	private Session session = null;

	public RemoteViewPanel() {
		this.videoPanel = new VideoPanel();

		setView(videoPanel);
		viewTitle = "Remote cam view";

	}

	public void setSession(Session session) {
		this.session = session;
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
		createViewPanel();
		showInfoPanel();
	}

	@Override
	public void showInfoPanel() {
		System.out.println("RemoteView runs on " + Thread.currentThread().getName());

		info.add(new JLabel(" Remote name: "));
		info.add(new JLabel(session.getRemoteName()));
		info.add(new JLabel(" Remote IP: "));
		info.add(new JLabel(session.getRemoteAddress().getHostAddress()));
		info.add(new JLabel(" View resolution:"));
		info.add(new JLabel((int) JavaEyeUtils.dimension.getWidth() + "x" + (int) JavaEyeUtils.dimension.getHeight()));
		info.add(new JLabel(" Codec: "));
		info.add(new JLabel("H.264"));
	}

}

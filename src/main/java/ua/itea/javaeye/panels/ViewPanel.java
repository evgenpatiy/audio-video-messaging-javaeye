/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.itea.javaeye.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ua.itea.javaeye.utils.Session;

/**
 *
 * @author yevgen
 */
public class ViewPanel extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6357612281585500243L;
	protected Session session;
	protected String viewTitle;
	protected JPanel view = new JPanel();
	protected JPanel info = new JPanel();
	protected Dimension videoDimension;

	protected void setSession(Session session) {
		this.videoDimension = new Dimension(320, 240);
		this.session = session;
	}

	public void setView(JPanel view) {
		this.view = view;
	}

	@Override
	public void run() {
		setPreferredSize(new Dimension(320, 480));
		view.setPreferredSize(videoDimension);
		setTitle(viewTitle);
		// setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new GridLayout(2, 0));

		view.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		info.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Information "));

		info.setLayout(new GridLayout(0, 2));

		add(view);
		add(info);

		URL iconURL = getClass().getResource("/img/eye.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());

		pack();
		setVisible(true);
	}

}

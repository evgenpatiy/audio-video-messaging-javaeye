/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.itea.javaeye.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.github.sarxos.webcam.Webcam;

import ua.itea.javaeye.agent.StreamClientAgent;
import ua.itea.javaeye.agent.StreamServerAgent;
import ua.itea.javaeye.handler.StreamFrameListener;
import ua.itea.javaeye.panels.LocalViewPanel;
import ua.itea.javaeye.panels.RemoteViewPanel;
import ua.itea.javaeye.utils.DbWorker;
import ua.itea.javaeye.utils.JavaEyeUtils;
import ua.itea.javaeye.utils.Session;

/**
 *
 * @author yevgen
 */
public class SessionList extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2307756360605280092L;
	private final Webcam webcam;
	private final JavaEyeUtils util = new JavaEyeUtils();
	private DbWorker db = new DbWorker("javaeye.db");

	protected LocalViewPanel localCam;
	protected RemoteViewPanel remoteCam;

	private final JPanel sessionButtonsPanel = new JPanel();
	private final JPanel bottomButtonsPanel = new JPanel();
	private final JButton sessionButton = new JButton();
	private final JButton addSessionButton = new JButton("Session");
	private final JLabel eyeLabel = new JLabel();

	SessionList(Webcam webcam) {
		this.webcam = webcam;

		this.localCam = new LocalViewPanel(webcam);
		this.remoteCam = new RemoteViewPanel();
	}

	@Override
	public void run() {
		setResizable(false);

		sessionButtonsPanel.setLayout(new GridLayout(10, 1));

		URL iconURL = getClass().getResource("/img/session.png");
		ImageIcon icon = new ImageIcon(iconURL);

		if (!db.isDbExists()) {
			db.createDb();
		} else {
			ArrayList<Session> sessionList = db.getSessionsList();
			for (Session session : sessionList) {
				session.setText(session.getRemoteName());
				session.setHorizontalAlignment(SwingConstants.LEFT);
				session.setIcon(icon);
				session.addActionListener(event -> {
					System.out.println(session);
				});
				sessionButtonsPanel.add(session);
			}
		}

		sessionButton.addActionListener(event -> {
			(new Thread(localCam)).start();
			(new Thread(remoteCam)).start();

			StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL(),
					JavaEyeUtils.dimension);
			// clientAgent.connect(new InetSocketAddress(remoteAddress.getText(), 20000));
		});

		System.out.println("SessionList runs on " + Thread.currentThread().getName());
		setPreferredSize(new Dimension(200, 480));
		setLayout(new BorderLayout());
		setTitle("Session list");

		bottomButtonsPanel.setLayout(new FlowLayout());
		iconURL = getClass().getResource("/img/plus.png");
		icon = new ImageIcon(iconURL);
		addSessionButton.setIcon(icon);
		addSessionButton.addActionListener(event -> {
			(new Thread(new AddSession(sessionButtonsPanel))).start();
		});
		bottomButtonsPanel.add(addSessionButton);

		iconURL = getClass().getResource("/img/eye.png");
		icon = new ImageIcon(iconURL);
		eyeLabel.setIcon(icon);
		bottomButtonsPanel.add(eyeLabel);

		add(sessionButtonsPanel, BorderLayout.CENTER);
		add(bottomButtonsPanel, BorderLayout.SOUTH);

		setIconImage(icon.getImage());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);

		// Start stream server!

		StreamServerAgent serverAgent = new StreamServerAgent(webcam, util.dimension);
		InetSocketAddress myAddress = new InetSocketAddress(JavaEyeUtils.localAddress, 20000);
		serverAgent.start(myAddress);
		System.out.println("Stream server runs on port " + myAddress);
	}

	protected class StreamFrameListenerIMPL implements StreamFrameListener {
		private volatile long count = 0;

		@Override
		public void onFrameReceived(BufferedImage image) {
			remoteCam.updateImage(image);
		}

	}
}

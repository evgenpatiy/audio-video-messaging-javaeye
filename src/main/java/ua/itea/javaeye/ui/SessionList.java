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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

import ua.itea.javaeye.handler.StreamFrameListener;
import ua.itea.javaeye.panels.LocalViewPanel;
import ua.itea.javaeye.panels.RemoteViewPanel;
import ua.itea.javaeye.stream.VideoStreamClient;
import ua.itea.javaeye.utils.DbWorker;
import ua.itea.javaeye.utils.JavaEyeUtils;
import ua.itea.javaeye.utils.Session;
import ua.itea.javaeye.utils.SessionWindow;

/**
 *
 * @author yevgen
 */
public class SessionList extends JFrame implements Runnable {

	private static final long serialVersionUID = 2307756360605280092L;
	private final Webcam webcam;
	private final LocalViewPanel localCam;
	private final RemoteViewPanel remoteCam;
	private final VideoStreamClient videoClient;
	private DbWorker db;
	private JPanel sessionButtonsPanel = new JPanel();

	SessionList(Webcam webcam) {
		this.webcam = webcam;
		this.localCam = new LocalViewPanel(webcam);
		this.remoteCam = new RemoteViewPanel();
		this.db = new DbWorker(JavaEyeUtils.DBNAME);
		this.videoClient = new VideoStreamClient(new StreamFrameListenerIMPL(), JavaEyeUtils.dimension);
	}

	private void updateSessionList() {
		ArrayList<Session> sessionList = db.getSessionsList();
		sessionButtonsPanel.removeAll();
		for (Session session : sessionList) {
			session.sessionButton.addActionListener(event -> new SessionWorker().runSession(session));
			session.editSessionButton.addActionListener(event -> new SessionWorker().editSession(session));
			session.deleteSessionButton.addActionListener(event -> new SessionWorker().deleteSession(session));

			sessionButtonsPanel.add(session);
		}
		sessionButtonsPanel.revalidate();
		sessionButtonsPanel.repaint();
	}

	@Override
	public void run() {

		JPanel bottomButtonsPanel = new JPanel();
		JButton addSessionButton = new JButton("Session");

		sessionButtonsPanel.setLayout(new GridLayout(10, 1));

		if (!db.isDbExists()) {
			db.createDb();
		} else {
			updateSessionList();
		}

		setPreferredSize(new Dimension(200, 500));
		setLayout(new BorderLayout());
		setTitle("Session list");

		URL iconURL = getClass().getResource("/img/plus.png");
		ImageIcon icon = new ImageIcon(iconURL);
		addSessionButton.setIcon(icon);
		addSessionButton.addActionListener(event -> {
			new SessionWorker().addSession();
		});

		iconURL = getClass().getResource("/img/eye.png");
		icon = new ImageIcon(iconURL);

		bottomButtonsPanel.setLayout(new FlowLayout());
		bottomButtonsPanel.add(addSessionButton);

		add(sessionButtonsPanel, BorderLayout.CENTER);
		add(bottomButtonsPanel, BorderLayout.SOUTH);

		setIconImage(icon.getImage());

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}

	private class SessionWorker extends SessionWindow {
		private static final long serialVersionUID = 5392077596526562854L;

		public void runSession(Session runSession) {
			remoteCam.setSession(runSession);
			(new Thread(localCam)).start();
			(new Thread(remoteCam)).start();
			videoClient.connect(new InetSocketAddress(runSession.getRemoteAddress(), JavaEyeUtils.streamServerPort));
		}

		public void addSession() {
			setWindowTitle("New session");
			setOkButton("Add");
			createAddEditWindow();

			okButton.addActionListener(event -> {
				Session session = new Session();
				if (getNameTextField().getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Session couldn't be empty", "Session name error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!JavaEyeUtils.validIP(getAddressTextField().getText())) {
					JOptionPane.showMessageDialog(null, "Provide correct remote IP", "Session address error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					session.setLocalAddress(JavaEyeUtils.localAddress);
					session.setRemoteName(getNameTextField().getText());
					try {
						session.setRemoteAddress(InetAddress.getByName(getAddressTextField().getText()));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}

					dispose();
					db.addSession(session);
					updateSessionList();
				}
			});
		}

		public void editSession(Session editSession) {
			setWindowTitle("Edit session");
			setOkButton("Update");

			getNameTextField().setText(editSession.getRemoteName());
			getAddressTextField().setText(editSession.getRemoteAddress().getHostAddress());
			createAddEditWindow();

			okButton.addActionListener(event -> {
				Session session = new Session();
				session.setId(editSession.getId());

				if (getNameTextField().getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Session couldn't be empty", "Session name error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!JavaEyeUtils.validIP(getAddressTextField().getText())) {
					JOptionPane.showMessageDialog(null, "Provide correct remote IP", "Session address error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					session.setLocalAddress(JavaEyeUtils.localAddress);
					session.setRemoteName(getNameTextField().getText());
					try {
						session.setRemoteAddress(InetAddress.getByName(getAddressTextField().getText()));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}

					dispose();
					System.out.println("edit from class " + session);
					db.editSession(session);
					updateSessionList();
				}
			});
		}

		public void deleteSession(Session deleteSession) {
			if (JOptionPane.showConfirmDialog(null,
					"You're about to delete session " + deleteSession.getRemoteName() + "!", "Warning",
					JOptionPane.YES_NO_OPTION) == 0) {
				db.deleteSession(deleteSession);
				updateSessionList();
			}
		}
	}

	private class StreamFrameListenerIMPL implements StreamFrameListener {
		private volatile long count = 0;

		@Override
		public void onFrameReceived(BufferedImage image) {
			remoteCam.updateImage(image);
		}
	}
}

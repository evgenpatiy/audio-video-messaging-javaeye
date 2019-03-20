package ua.itea.javaeye.utils;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Session extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8614461107737396240L;
	private InetAddress localAddress;
	private InetAddress remoteAddress;
	private String remoteName;

	public JButton sessionButton = new JButton();
	public JButton editSessionButton = new JButton();
	public JButton deleteSessionButton = new JButton();
	private JPanel rightButtonsPanel = new JPanel();

	public Session() {
		setLayout(new BorderLayout());
		rightButtonsPanel.setLayout(new GridLayout(2, 1));

		URL iconURL = getClass().getResource("/img/edit.png");
		ImageIcon icon = new ImageIcon(iconURL);
		editSessionButton.setIcon(icon);
		editSessionButton.setToolTipText("Edit session");
		rightButtonsPanel.add(editSessionButton);

		iconURL = getClass().getResource("/img/delete.png");
		icon = new ImageIcon(iconURL);
		deleteSessionButton.setIcon(icon);
		deleteSessionButton.setToolTipText("Delete session");
		rightButtonsPanel.add(deleteSessionButton);

		iconURL = getClass().getResource("/img/eye.png");
		icon = new ImageIcon(iconURL);
		sessionButton.setIcon(icon);
		sessionButton.setToolTipText("Establish connect");
		sessionButton.setHorizontalAlignment(SwingConstants.LEFT);

		add(sessionButton, BorderLayout.CENTER);
		add(rightButtonsPanel, BorderLayout.EAST);
		this.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	public InetAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(InetAddress localAddress) {
		this.localAddress = localAddress;
	}

	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(InetAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getRemoteName() {
		return remoteName;
	}

	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
		sessionButton.setText(remoteName);
	}

	@Override
	public String toString() {
		return "Session [localAddress=" + localAddress + ", remoteAddress=" + remoteAddress + ", remoteName="
				+ remoteName + "]";
	}
}

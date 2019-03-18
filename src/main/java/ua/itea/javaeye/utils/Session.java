package ua.itea.javaeye.utils;

import java.net.InetAddress;

import javax.swing.JButton;

public class Session extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8614461107737396240L;
	private InetAddress localAddress;
	private InetAddress remoteAddress;
	private String remoteName;

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
	}

	@Override
	public String toString() {
		return "Session [localAddress=" + localAddress + ", remoteAddress=" + remoteAddress + ", remoteName="
				+ remoteName + "]";
	}

}

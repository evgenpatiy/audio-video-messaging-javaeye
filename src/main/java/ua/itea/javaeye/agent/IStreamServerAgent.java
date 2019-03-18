package ua.itea.javaeye.agent;

import java.net.SocketAddress;

public interface IStreamServerAgent {
	public void start(SocketAddress streamAddress);
	public void stop();
}

package ua.itea.javaeye.stream;

import java.net.SocketAddress;

public interface IVideoStreamClient {
	public void connect(SocketAddress streamServerAddress);
	public void stop();
}

package ua.itea.javaeye.stream;

import java.net.SocketAddress;

public interface IVideoStreamServer {
	public void start(SocketAddress streamAddress);
	public void stop();
}

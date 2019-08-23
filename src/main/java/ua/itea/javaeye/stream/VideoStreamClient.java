package ua.itea.javaeye.stream;

import java.awt.Dimension;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import lombok.extern.slf4j.Slf4j;
import ua.itea.javaeye.channel.StreamClientChannelPipelineFactory;
import ua.itea.javaeye.handler.StreamClientListener;
import ua.itea.javaeye.handler.StreamFrameListener;

@Slf4j
public class VideoStreamClient implements IVideoStreamClient {
    protected final ClientBootstrap clientBootstrap;
    protected final StreamClientListener streamClientListener;
    protected final StreamFrameListener streamFrameListener;
    protected final Dimension dimension;
    protected Channel clientChannel;

    public VideoStreamClient(StreamFrameListener streamFrameListener, Dimension dimension) {
        super();
        this.dimension = dimension;
        this.clientBootstrap = new ClientBootstrap();
        this.clientBootstrap.setFactory(
                new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        this.streamFrameListener = streamFrameListener;
        this.streamClientListener = new StreamClientListenerIMPL();
        this.clientBootstrap.setPipelineFactory(
                new StreamClientChannelPipelineFactory(streamClientListener, streamFrameListener, dimension));
    }

    @Override
    public void connect(SocketAddress streamServerAddress) {
        log.info("going to connect to stream server " + streamServerAddress);
        clientBootstrap.connect(streamServerAddress);
    }

    @Override
    public void stop() {
        clientChannel.close();
        clientBootstrap.releaseExternalResources();
    }

    protected class StreamClientListenerIMPL implements StreamClientListener {

        @Override
        public void onConnected(Channel channel) {
            log.info("stream connected to server at " + channel);
            clientChannel = channel;
        }

        @Override
        public void onDisconnected(Channel channel) {
            log.info("stream disconnected to server at " + channel);
        }

        @Override
        public void onException(Channel channel, Throwable t) {
            log.error("exception at " + channel + ", exception " + t);
        }

    }

}

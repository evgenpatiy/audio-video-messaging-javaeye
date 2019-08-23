package ua.itea.javaeye.channel;

import java.awt.Dimension;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import ua.itea.javaeye.handler.H264Decoder;
import ua.itea.javaeye.handler.StreamClientHandler;
import ua.itea.javaeye.handler.StreamClientListener;
import ua.itea.javaeye.handler.StreamFrameListener;

public class StreamClientChannelPipelineFactory implements ChannelPipelineFactory {
    protected final StreamClientListener streamClientListener;
    protected final StreamFrameListener streamFrameListener;
    protected final Dimension dimension;

    public StreamClientChannelPipelineFactory(StreamClientListener streamClientListener,
            StreamFrameListener streamFrameListener, Dimension dimension) {
        super();
        this.streamClientListener = streamClientListener;
        this.streamFrameListener = streamFrameListener;
        this.dimension = dimension;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("stream client handler", new StreamClientHandler(streamClientListener));
        pipeline.addLast("frame decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast("stream handler", new H264Decoder(streamFrameListener, dimension, false, false));
        return pipeline;
    }

}

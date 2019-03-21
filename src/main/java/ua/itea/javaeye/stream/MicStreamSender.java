package ua.itea.javaeye.stream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import ua.itea.javaeye.utils.JavaEyeUtils;

public class MicStreamSender implements Runnable {
	public byte[] buffer;
	private int port = JavaEyeUtils.SOUND_PORT;
	private AudioInputStream ais;
	private TargetDataLine line;
	private DatagramPacket dgp;
	private InetAddress remoteAddress;
	private int rate = JavaEyeUtils.SAMPLE_RATE;
	private int channels = JavaEyeUtils.CHANNELS;
	private int sampleSize = JavaEyeUtils.SAMPLE_SIZE;
	private boolean bigEndian = false;
	private volatile boolean stop = false;

	public MicStreamSender(InetAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	@Override
	public void run() {
		System.out.println("send sound stream to " + remoteAddress.getHostAddress());

		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate,
				bigEndian);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
			System.out.println("Line matching " + info + " not supported.");
			return;
		}

		try {
			line = (TargetDataLine) AudioSystem.getLine(info);

			int buffsize = line.getBufferSize() / 5;
			buffsize += 512;

			line.open(format);
			line.start();

			int numBytesRead;
			byte[] data = new byte[4096];

			DatagramSocket socket = new DatagramSocket();
			while (true) {
				if (stop) {
					System.out.println("interrupt sound stream to " + remoteAddress.getHostAddress());
					socket.close();
					break;
				} else {
					// Read the next chunk of data from the TargetDataLine.
					numBytesRead = line.read(data, 0, data.length);
					// Save this chunk of data.
					dgp = new DatagramPacket(data, data.length, remoteAddress, port);

					socket.send(dgp);
				}
			}

		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO: handle exception
		} catch (SocketException e) {
			// TODO: handle exception
		} catch (IOException e2) {
			// TODO: handle exception
		}

	}
}

package ua.itea.javaeye.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import ua.itea.javaeye.utils.JavaEyeUtils;

public class ServerStreamReceiver implements Runnable {

	private AudioInputStream ais;
	private AudioFormat format;
	private boolean status = true;
	private int port = JavaEyeUtils.SOUND_PORT;
	private int sampleRate = JavaEyeUtils.SAMPLE_RATE;
	private int channels = JavaEyeUtils.CHANNELS;
	private int sampleSize = JavaEyeUtils.SAMPLE_SIZE;

	private DataLine.Info dataLineInfo;
	private SourceDataLine sourceDataLine;

	public ServerStreamReceiver() {
		(new Thread(this)).start();
	}

	public void toSpeaker(byte soundbytes[]) {
		try {
			sourceDataLine.write(soundbytes, 0, soundbytes.length);
		} catch (Exception e) {
			System.out.println("Not working in speakers...");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Sound receiver listening port: " + port);

		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Formula for lag = (byte_size/sample_rate)*2 Byte size 9728 will produce ~
		 * 0.45 seconds of lag. Voice slightly broken. Byte size 1400 will produce ~
		 * 0.06 seconds of lag. Voice extremely broken. Byte size 4000 will produce ~
		 * 0.18 seconds of lag. Voice slightly more broken then 9728.
		 */

		byte[] receiveData = new byte[4096];

		format = new AudioFormat(sampleRate, sampleSize, channels, true, false);
		dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
		try {
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sourceDataLine.open(format);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sourceDataLine.start();

		// FloatControl volumeControl = (FloatControl)
		// sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
		// volumeControl.setValue(1.00f);

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());

		while (status == true) {
			try {
				serverSocket.receive(receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ais = new AudioInputStream(bais, format, receivePacket.getLength());
			toSpeaker(receivePacket.getData());
		}

		sourceDataLine.drain();
		sourceDataLine.close();
	}
}
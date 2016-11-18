package com.kwsoft.kehuhua.audiorecorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Mp3Recorder {

	private static final String TAG = Mp3Recorder.class.getSimpleName();

	static {
		System.loadLibrary("mp3lame");
	}

	private static final int DEFAULT_SAMPLING_RATE = 11025;
	private static final int FRAME_COUNT = 160;
	/* Encoded bit rate. MP3 file will be encoded with bit rate 16kbps */
	private static final int BIT_RATE = 20;
	// use these settings,the audio file is about 2k a seccond;
	private AudioRecord audioRecord = null;
	private int bufferSize;
	private File mp3File;
	private RingBuffer ringBuffer;
	private byte[] buffer;
	private String outputFile;

	private FileOutputStream os = null;
	private DataEncodeThread encodeThread;
	private int samplingRate;
	private int channelConfig;
	private PCMFormat audioFormat;
	private boolean isRecording = false;

	public Mp3Recorder(int samplingRate, int channelConfig) {
		this.samplingRate = samplingRate;
		this.channelConfig = channelConfig;
		this.audioFormat = PCMFormat.PCM_16BIT;
	}

	/**
	 * Default constructor. Setup recorder with default sampling rate 1 channel,
	 * 16 bits pcm
	 */
	public Mp3Recorder() {
		this(DEFAULT_SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO);
	}

	/**
	 * Start recording. Create an encoding thread. Start record from this
	 * thread.
	 * 
	 * @throws IOException
	 */
	public void startRecording() throws IOException {
		if (isRecording){
			return;
		}
		Log.d(TAG, "Start recording");
		Log.d(TAG, "BufferSize = " + bufferSize);
		// Initialize audioRecord if it's null.
		if (audioRecord == null) {
			initAudioRecorder();
		}
		audioRecord.startRecording();

		new Thread() {
			@Override
			public void run() {
				isRecording = true;
				while (isRecording) {
					int bytes = audioRecord.read(buffer, 0, bufferSize);
					if (bytes > 0) {
						ringBuffer.write(buffer, bytes);
					}
				}

				// release and finalize audioRecord
				try {
					audioRecord.stop();
					audioRecord.release();
					audioRecord = null;

					// stop the encoding thread and try to wait
					// until the thread finishes its job
					Message msg = Message.obtain(encodeThread.getHandler(),
							DataEncodeThread.PROCESS_STOP);
					msg.sendToTarget();

//					Log.d(TAG, "waiting for encoding thread");
					encodeThread.join();
//					Log.d(TAG, "done encoding thread");
				} catch (InterruptedException e) {
					Log.d(TAG, "Faile to join encode thread");
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		}.start();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void stopRecording() throws IOException {
		Log.d(TAG, "stop recording");
		isRecording = false;
	}

	/**
	 * Initialize audio recorder
	 */
	private void initAudioRecorder() throws IOException {
		int bytesPerFrame = audioFormat.getBytesPerFrame();
		/* Get number of samples. Calculate the buffer size (round up to the
		   factor of given frame size) */
		int frameSize = AudioRecord.getMinBufferSize(samplingRate, channelConfig,
				audioFormat.getAudioFormat())
				/ bytesPerFrame;
		if (frameSize % FRAME_COUNT != 0) {
			frameSize = frameSize + (FRAME_COUNT - frameSize % FRAME_COUNT);
			Log.d(TAG, "Frame size: " + frameSize);
		}

		bufferSize = frameSize * bytesPerFrame;

		/* Setup audio recorder */
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, samplingRate, channelConfig,
				audioFormat.getAudioFormat(), bufferSize);

		// Setup RingBuffer. Currently is 10 times size of hardware buffer
		// Initialize buffer to hold data
		ringBuffer = new RingBuffer(10 * bufferSize);
		buffer = new byte[bufferSize];

		// Initialize lame buffer
		SimpleLame.init(samplingRate, 1, samplingRate, BIT_RATE);

		// Initialize the place to put mp3 file
		mp3File = new File(outputFile);
		os = new FileOutputStream(mp3File);

		// Create and run thread used to encode data
		// The thread will 
		encodeThread = new DataEncodeThread(ringBuffer, os, bufferSize);
		encodeThread.start();
		audioRecord.setRecordPositionUpdateListener(encodeThread, encodeThread.getHandler());
		audioRecord.setPositionNotificationPeriod(FRAME_COUNT);
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
}
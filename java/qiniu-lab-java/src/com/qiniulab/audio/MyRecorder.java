package com.qiniulab.audio;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class MyRecorder extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// 七牛的相关设置，如果是客户端的应用，服务器端必须有一个获取凭证的API接口，不可以把ak，sk放在客户端代码里面
	// 这里为了方便演示，所以写在这里。
	// access key and secret key
	String ak = "";
	String sk = "";
	String bucket = "if-pbl";

	// 定义录音格式
	AudioFormat af = null;
	// 定义目标数据行,可以从中读取音频数据,该 TargetDataLine 接口提供从目标数据行的缓冲区读取所捕获数据的方法。
	TargetDataLine td = null;
	// 定义源数据行,源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传递给混频器。
	SourceDataLine sd = null;
	// 定义字节数组输入输出流
	ByteArrayInputStream bais = null;
	ByteArrayOutputStream baos = null;
	// 定义音频输入流
	AudioInputStream ais = null;
	// 定义停止录音的标志，来控制录音线程的运行
	boolean stopflag = false;

	// 定义所需要的组件
	JPanel jp1, jp3;
	JLabel jl1 = null;
	JButton captureBtn, stopBtn;

	public static void main(String[] args) {
		// 创造一个实例
		MyRecorder mr = new MyRecorder();
		mr.setVisible(true);
	}

	// 构造函数
	public MyRecorder() {
		// 组件初始化
		jp1 = new JPanel();
		jp3 = new JPanel();

		// 定义字体
		Font myFont = new Font("华文新魏", Font.BOLD, 30);
		jl1 = new JLabel("边录音边上传功能演示");
		jl1.setFont(myFont);
		jp1.add(jl1);

		captureBtn = new JButton("开始录音");
		// 对开始录音按钮进行注册监听
		captureBtn.addActionListener(this);
		captureBtn.setActionCommand("captureBtn");
		// 对停止录音进行注册监听
		stopBtn = new JButton("停止录音");
		stopBtn.addActionListener(this);
		stopBtn.setActionCommand("stopBtn");

		this.add(jp1, BorderLayout.NORTH);
		this.add(jp3, BorderLayout.CENTER);
		jp3.setLayout(null);
		jp3.setLayout(new GridLayout(1, 4, 10, 10));
		jp3.add(captureBtn);
		jp3.add(stopBtn);
		// 设置按钮的属性
		captureBtn.setEnabled(true);
		stopBtn.setEnabled(false);
		// 设置窗口的属性
		this.setSize(400, 300);
		this.setTitle("录音机");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("captureBtn")) {
			// 点击开始录音按钮后的动作
			// 停止按钮可以启动
			captureBtn.setEnabled(false);
			stopBtn.setEnabled(true);

			// 调用录音的方法
			capture();
		} else if (e.getActionCommand().equals("stopBtn")) {
			// 点击停止录音按钮的动作
			captureBtn.setEnabled(true);
			stopBtn.setEnabled(false);
			// 调用停止录音的方法
			stop();
		}

	}

	// 开始录音
	public void capture() {
		try {
			// af为AudioFormat也就是音频格式
			af = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
			td = (TargetDataLine) (AudioSystem.getLine(info));
			// 打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。
			td.open(af);
			// 允许某一数据行执行数据 I/O
			td.start();

			// 创建播放录音的线程
			Record record = new Record();
			Thread t1 = new Thread(record);
			t1.start();

		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
			return;
		}
	}

	// 停止录音
	public void stop() {
		stopflag = true;
	}

	// 设置AudioFormat的参数
	public AudioFormat getAudioFormat() {
		// 下面注释部分是另外一种音频格式，两者都可以
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float rate = 8000f;
		int sampleSize = 16;
		String signedString = "signed";
		boolean bigEndian = true;
		int channels = 1;
		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
		// //采样率是每秒播放和录制的样本数
		// float sampleRate = 16000.0F;
		// // 采样率8000,11025,16000,22050,44100
		// //sampleSizeInBits表示每个具有此格式的声音样本中的位数
		// int sampleSizeInBits = 16;
		// // 8,16
		// int channels = 1;
		// // 单声道为1，立体声为2
		// boolean signed = true;
		// // true,false
		// boolean bigEndian = true;
		// // true,false
		// return new AudioFormat(sampleRate, sampleSizeInBits, channels,
		// signed,bigEndian);
	}

	// 录音类，因为要用到MyRecord类中的变量，所以将其做成内部类
	class Record implements Runnable {
		// 定义存放录音的字节数组,作为缓冲区
		// 这个缓冲区不能太大，因为读取音频的read是阻塞的。
		private final int BUFFER_SIZE = 100;
		private final int INTERVAL_MILLIS = 5 * 1000;// 5s
		private byte buffer[] = new byte[BUFFER_SIZE];
		private Auth auth;
		private UploadManager uploadManager;

		private java.util.List<String> chunkNames;

		public Record() {
			this.chunkNames = new ArrayList<String>();
			this.auth = Auth.create(ak, sk);
			this.uploadManager = new UploadManager();
		}

		public boolean upload(byte[] rawData, String key) throws IOException {
			AudioFormat af = getAudioFormat();
			// 处理原始数据
			ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
			AudioInputStream ais = new AudioInputStream(bais, af, rawData.length / af.getFrameSize());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, baos);

			System.out.println("uploading " + key);
			// mp3 data
			byte[] mp3Data = baos.toByteArray();
			String uptoken = this.auth.uploadToken(bucket, key);
			Response resp = this.uploadManager.put(mp3Data, key, uptoken);
			return resp.isOK();
		}

		// 将字节数组包装到流里，最终存入到baos中
		// 重写run函数
		public void run() {
			baos = new ByteArrayOutputStream();
			try {
				stopflag = false;

				System.out.println("recording...");
				long startMillis = System.currentTimeMillis();
				while (!stopflag) {
					// System.out.println("read chunk...");
					// 当停止录音没按下时，该线程一直执行
					// 从数据行的输入缓冲区读取音频数据。
					// 要读取bts.length长度的字节,cnt 是实际读取的字节数
					int cnt = td.read(buffer, 0, buffer.length);
					if (cnt > 0) {
						baos.write(buffer, 0, cnt);
					}

					long endMillis = System.currentTimeMillis();
					long delta = endMillis - startMillis;
					// System.out.println("delta: " + delta);
					if (delta >= INTERVAL_MILLIS) {
						// 上传这一段的音频文件
						String key = "audio/" + System.currentTimeMillis() + ".mp3";

						// 传入的原始数据在upload里面再进行处理
						byte[] rawData = baos.toByteArray();
						baos.reset();
						boolean result = upload(rawData, key);
						if (result) {
							this.chunkNames.add(key);
						}
						startMillis = endMillis;
					}
				}

				// 上传最后一片
				int cnt = td.read(buffer, 0, buffer.length);
				if (cnt > 0) {
					baos.write(buffer, 0, cnt);
				}

				// 上传这一段的音频文件
				String key = "audio/" + System.currentTimeMillis() + ".mp3";

				// 传入的原始数据在upload里面再进行处理
				byte[] rawData = baos.toByteArray();
				baos.reset();
				boolean result = upload(rawData, key);
				if (result) {
					this.chunkNames.add(key);
				}

				System.out.println("------------------------------");
				System.out.println("record stops");
				// @TODO 搜集所有的音频文件列表，并使用avconcat接口合成
				// 这个过程通过调用业务服务器的API，然后业务服务器去调用七牛接口实现，具体参考avconcat接口。
				// {@link
				// http://developer.qiniu.com/docs/v6/api/reference/fop/av/avconcat.html}

				for (String s : chunkNames) {
					System.out.println(s);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭打开的字节数组流
					if (baos != null) {
						baos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					td.drain();
					td.close();
				}
			}
		}
	}
}
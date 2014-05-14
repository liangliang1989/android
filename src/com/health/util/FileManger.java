package com.health.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.bluetooth.BluetoothAdapter;
import android.os.Environment;

public class FileManger {
	// �ļ�Ŀ¼
	private static final String DIR = "ayitong";
	private static final String ID_FILENAME = "userid.txt";
	// ԭʼ�����ļ���
	public static final String DATA_FILENAME = "data.txt";

	public static final String ABS_PATH = getSaveFilePath();
	// �����Ѷ���λ���ļ�
	private static final String SEEK_FILENAME = getDateString() + "seek.txt";
	private static FileManger instance = null;

	private FileManger() {
		
	}

	public static FileManger getInstance() {
		if (instance == null)
			instance = new FileManger();
		return instance;
	}

	public static String getDateString() {
		DateFormat format = new SimpleDateFormat("yy-MM-dd");
		return format.format(new Date()).toString();
	}

	private static String getSaveFilePath() {
		String basePath;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			basePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		else
			basePath = Environment.getDataDirectory().getAbsolutePath();
		String savePath = new File(basePath, DIR).getAbsolutePath();
		createDir(savePath);
		return savePath;
	}

	private static void createDir(String path) {
		File file = new File(path);
		if (file.exists())
			return;
		file.mkdirs();
	}

	public boolean saveFile(String fileName, String text) {
		String path = getSaveFilePath();
		File file = new File(path, fileName);
		try {
			PrintWriter out = new PrintWriter(file);
			out.print(text);
			out.flush();
			out.close();
			return true;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	}

	public boolean append(String data) {
		return append(DATA_FILENAME, data);
	}

	public boolean append(String fileName, String data) {
		String path = getSaveFilePath();
		File file = new File(path, fileName);	
		try {
			FileOutputStream fileOutStream = new FileOutputStream(file, true);// true��ʾ׷��
			byte[] buff = data.getBytes();
			fileOutStream.write(buff);
			fileOutStream.flush();
			fileOutStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * ��ȡ�ļ�
	 * 
	 * @param fileName
	 * @return�ļ����ݣ����ļ�������ʱ����null
	 * @throws IOException
	 */
	public String readFile(String fileName) {
		String path = getSaveFilePath();
		File file = new File(path, fileName);
		if (!file.exists())
			return null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * ��ȡ�豸id����������ַ��id
	 * 
	 * @return
	 */
	public static String getId() {
		String id = FileManger.getInstance().readFile(ID_FILENAME);
		if (id == null) {
			String mac;
			try {
				BluetoothAdapter adpter = BluetoothAdapter.getDefaultAdapter();
				mac = adpter.getAddress();
				mac = mac.replaceAll(":", "");
			} catch (Exception e) {
				e.printStackTrace();
				mac = "1234567890";
			}
			id = mac.substring(0, mac.length() / 2)
					+ mac.substring(mac.length() / 2, mac.length());
			Long v = Long.valueOf(id, 16);
			FileManger.getInstance().saveFile(ID_FILENAME, v.toString());

		}
		return id;
	}

	/***
	 * ��ȡ�ϴζ��ļ���λ��
	 * 
	 * @return
	 */
	public static long getPosition() {
		String seek = FileManger.getInstance().readFile(SEEK_FILENAME);
		if (seek == null)
			return 0;
		return Long.valueOf(seek);
	}

	/***
	 * ������ļ���λ��
	 * 
	 * @param postion
	 */
	public static void savePosition(long postion) {
		FileManger.getInstance().saveFile(SEEK_FILENAME,
				String.valueOf(postion));
	}
}

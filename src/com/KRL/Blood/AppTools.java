package com.KRL.Blood;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppTools {
	/***
	 * ������ת����byte ����
	 * 
	 * @param data
	 *            ��ת������
	 * @param count
	 *            ת���ĳ���
	 * @return ת��֮�������
	 */
	public static synchronized byte[] String2Byte(String data, int count) {
		byte[] tempdata = new byte[count];
		byte[] temp;
		try {
			temp = data.getBytes("GBK");
			if (tempdata.length > temp.length)
				System.arraycopy(temp, 0, tempdata, 0, temp.length);
			else
				System.arraycopy(temp, 0, tempdata, 0, tempdata.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempdata;
	}

	public static synchronized byte[] String2Byte(String data) {
		try {
			return data.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * ���ַ�����ת�����ַ���
	 * 
	 * @param data
	 *            ��ת������
	 * @return ת��֮����ַ���
	 */
	public static synchronized String byte2String(byte[] data) {
		String temp = "";
		int count = 0;
		for (count = 0; count < data.length;) {
			if (data[count] != 0x00) {
				count++;
			} else
				break;
		}
		try {
			temp = new String(data, 0, count, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	/***
	 * ���ַ�����ת�����ַ���
	 * 
	 * @param data
	 *            ��ת������
	 * @return ת��֮����ַ���
	 */
	public static synchronized String byte2String(byte[] data, int start,
			int len) {
		String temp = "";
		int count = 0;
		for (count = 0; count < len;) {
			if (data[start + count] != 0x00) {
				count++;
			} else
				break;
		}
		try {

			temp = new String(data, start, count, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	/***
	 * ����ȡ���ֽڳ���ת����INT
	 * 
	 * @param b0
	 *            ��1�ֽ�
	 * @param b1
	 *            ��2�ֽ�
	 * @param b2��1�ֽ�
	 * @param b3��2�ֽ�
	 */
	public static synchronized int byte2Int(byte b0, byte b1, byte b2, byte b3) {
		int length = 0;
		length = (b3 + 0x100) % 0x100;
		length = length << 8;
		length += (b2 + 0x100) % 0x100;
		length = length << 8;
		length += (b1 + 0x100) % 0x100;
		length = length << 8;
		length += (b0 + 0x100) % 0x100;
		return length;
	}

	/***
	 * ����ȡ���ֽڳ���ת����INT
	 * 
	 * @param b0
	 *            ��1�ֽ�
	 * @param b1
	 *            ��2�ֽ�
	 * @param b2��1�ֽ�
	 * @param b3��2�ֽ�
	 */
	public static synchronized int byte2Int(byte[] data, int start, int count) {
		int length = 0;
		while (count-- > 0) {
			length = length << 8;
			length += ((data[start + count] + 0x100) % 0x100);
		}
		return length;
	}

	/***
	 * ����ȡ���ֽڳ���ת����INT
	 * 
	 * @param b0
	 *            ���ֽ�
	 * @param b1
	 *            ���ֽ�
	 */
	public static synchronized int byte2Int(byte b0, byte b1) {
		int length = 0;
		length += (b1 + 0x100) % 0x100;
		length = length << 8;
		length += (b0 + 0x100) % 0x100;
		return length;
	}

	public static synchronized short command2short(byte[] command) {
		short length = 0;
		length += (command[1] + 0x100) % 0x100;
		length = (short) (length << 8);
		length += (command[0] + 0x100) % 0x100;
		return length;
	}

	/***
	 * 4�ֽ� int ת����byte[];
	 * 
	 * @param data
	 * @return
	 */
	public static synchronized byte[] int2Byte(int data) {
		byte[] tempbyte = new byte[4];
		tempbyte[0] = (byte) (data & 0x000000FF);
		tempbyte[1] = (byte) ((data >> 8) & 0x000000FF);
		tempbyte[2] = (byte) ((data >> 16) & 0x000000FF);
		tempbyte[3] = (byte) ((data >> 24) & 0x000000FF);
		return tempbyte;
	}

	/***
	 * float ת���� byte����
	 * 
	 * @param data
	 *            ��ת��������
	 * @return С��ģʽ��BYTE���� null ת��ʧ��
	 */
	public static synchronized byte[] float2Byte(float data) {
		ByteArrayOutputStream mByteOuts = new ByteArrayOutputStream();
		DataOutputStream outs = new DataOutputStream(mByteOuts);
		try {
			outs.writeFloat(data);
			byte[] temp = mByteOuts.toByteArray();
			byte[] tempbyte = new byte[4];
			tempbyte[0] = temp[3];
			tempbyte[1] = temp[2];
			tempbyte[2] = temp[1];
			tempbyte[3] = temp[0];
			return tempbyte;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/***
	 * ��UTCʱ��ת����yyyy-MM-dd HH:mm:ss ��Ӧ��32�ֽ�����
	 * 
	 * @param utc
	 *            ��ת��ʱ��
	 * @param format
	 *            ��ת����ʽ
	 * @return ת��֮����ֽ�
	 */
	public static synchronized byte[] utc2TimeByte(long utc, String format) {
		Date mDate = new Date(utc);
		SimpleDateFormat mFormat = new SimpleDateFormat(format);
		return String2Byte(mFormat.format(mDate), 32);
	}

	/***
	 * ��UTCʱ��ת����yyyy-MM-dd HH:mm:ss ��Ӧ��32�ֽ�����
	 * 
	 * @param utc
	 *            ��ת��ʱ��
	 * @return ת��֮����ֽ�
	 */
	public static synchronized byte[] utc2TimeByte(long utc) {
		Date mDate = new Date(utc);
		SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String2Byte(mFormat.format(mDate), 32);
	}

	/***
	 * ��UTCʱ��ת����yyyy-MM-dd HH:mm:ss �ַ���
	 * 
	 * @param utc
	 *            ��ת��ʱ��
	 * @return ת��֮����ֽ�
	 */
	public static synchronized String utc2TimeString(long utc) {
		Date mDate = new Date(utc);
		SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return mFormat.format(mDate);
	}

	/***
	 * ��UTCʱ��ת����yyyy-MM-dd HH:mm:ss �ַ���
	 * 
	 * @param utc
	 *            ��ת��ʱ��
	 * @param format
	 *            ��ת����ʽ
	 * @return ת��֮����ֽ�
	 */
	public static synchronized String utc2TimeString(long utc, String format) {
		Date mDate = new Date(utc);
		SimpleDateFormat mFormat = new SimpleDateFormat(format);
		return mFormat.format(mDate);
	}

	/***
	 * ���ַ�����ʽʱ��ת����utc
	 * 
	 * @param time
	 * @param formate
	 * @return
	 */
	public static synchronized long String2Utc(String time, String formate) {
		SimpleDateFormat tformate = new SimpleDateFormat(formate);
		tformate.setTimeZone(TimeZone.getDefault());
		long utc = 0L;
		try {
			Date date = tformate.parse(time);
			utc = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return utc;
	}

	/***
	 * ��BYTE����ת����long ��λ��ǰ
	 * 
	 * @param data
	 *            ת��֮�������
	 * @return
	 */
	public static synchronized long byte2long(byte[] data) {
		long temp = 0L;
		int length = data.length;
		for (int i = 0; i < length; i++) {
			temp = temp << 8;
			temp += ((data[length - 1 - i] + 0x100) % 0x100);
		}
		return temp;
	}

	/***
	 * ��BYTE����ת����long ��λ��ǰ
	 * 
	 * @param data
	 *            ת��֮�������
	 * @return
	 */
	public static synchronized long byte2long(byte[] data, int start, int count) {
		long temp = 0L;
		// int length = data.length;
		for (int i = 0; i < count; i++) {
			temp = temp << 8;
			temp += ((data[start + count - 1 - i] + 0x100) % 0x100);
		}
		return temp;
	}

	/***
	 * ��BYTE����ת����int ��λ��ǰ
	 * 
	 * @param data
	 *            ת��֮�������
	 * @return
	 */
	public static synchronized int byte2Int(byte[] data) {
		int temp = 0;
		int length = data.length;
		for (int i = 0; i < length; i++) {
			temp = temp << 8;
			temp += ((data[length - 1 - i] + 0x100) % 0x100);
		}
		return temp;
	}

	/***
	 * byte ת���� float
	 * 
	 * @param data
	 * @return
	 */
	public static synchronized float byte2Float(byte[] data) {
		return Float.intBitsToFloat(byte2Int(data));
	}

	/***
	 * byte ת���� float
	 * 
	 * @param data
	 * @return
	 */
	public static synchronized float byte2Float(byte[] data, int start,
			int index) {
		// return Float.intBitsToFloat(byte2Int(data));
		return Float.intBitsToFloat(byte2Int(data, start, index));
	}

	/***
	 * ��long��ת����byte���� ��λ��ǰ
	 * 
	 * @param data
	 *            ת��֮���byte����
	 * @return
	 */
	public static synchronized byte[] long2byte(long data) {
		byte[] temp = new byte[8];
		temp[7] = (byte) (data >> 56);
		temp[6] = (byte) (data >> 48);
		temp[5] = (byte) (data >> 40);
		temp[4] = (byte) (data >> 32);
		temp[3] = (byte) (data >> 24);
		temp[2] = (byte) (data >> 16);
		temp[1] = (byte) (data >> 8);
		temp[0] = (byte) data;
		return temp;
	}

	/***
	 * ��long��ת����byte���� ��λ��ǰ
	 * 
	 * @param data
	 *            ת��֮���byte����
	 * @return
	 */
	public static synchronized byte[] short2byte(short data) {
		byte[] temp = new byte[2];
		temp[1] = (byte) (data >> 8);
		temp[0] = (byte) data;
		return temp;
	}

	/***
	 * �����ļ����ڵ�Ŀ¼
	 * 
	 * @param path
	 *            �ļ�·��
	 * @return true �ɹ� false ʧ��
	 */
	public static synchronized boolean createFolder(String path) {
		String pathfolder = path;
		int tempindex = pathfolder.length();
		for (int i = 0; i < pathfolder.length(); i++) {
			if (pathfolder.charAt(tempindex - 1) == '/')
				break;
			else
				tempindex--;
		}
		char[] olddata = pathfolder.toCharArray();
		char[] newdata = new char[tempindex];
		System.arraycopy(olddata, 0, newdata, 0, tempindex);
		pathfolder = new String(newdata);
		File mFolder = new File(pathfolder);
		if (!mFolder.exists())
			return mFolder.mkdirs();
		else
			return true;
	}

	/***
	 * �����������״̬
	 * 
	 * @return
	 */
	public static synchronized boolean cheackNetConnection(Context context) {
		ConnectivityManager ConManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ��ȡWIFI��������״̬
		NetworkInfo.State wifi_state = ConManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState(); //
		if (wifi_state == NetworkInfo.State.CONNECTED) {
			return true;
		} else {
			// �ж�3G��������״̬
			NetworkInfo.State mobile_state = ConManager.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState();
			if (mobile_state == NetworkInfo.State.CONNECTED) {
				return true;
			} else {
				return false;
			}
		}
	}

	/***
	 * BCD��ʽ����ת����ʮ������
	 * 
	 * @param data
	 *            BCD����
	 * @return ʮ����������
	 */
	public static synchronized int bcd2Hex(int data) {
		return (data / 16) * 10 + data % 16;
	}

	/***
	 * HEXת����BCD
	 * 
	 * @param data
	 *            ��ת��������
	 * @return ����byte����
	 */
	public static synchronized byte hex2Bcd(byte data) {
		return (byte) ((data / 10) * 16 + data % 10);
	}

	/***
	 * ������ת����utc ʱ��
	 * 
	 * @param year
	 *            ��
	 * @param mongth
	 *            ��
	 * @param day
	 *            ��
	 * @param hour
	 *            Сʱ
	 * @param minute
	 *            ����
	 * @param sec
	 *            ��
	 * @return utc ʱ��
	 */
	public static synchronized long dateToUtc(int year, int month, int day,
			int hour, int minute, int sec) {
		Calendar calendar = new GregorianCalendar(year, month, day, hour,
				minute, sec);
		return calendar.getTimeInMillis();
	}

	// ���㵽�ֽ�ת��
	public static synchronized byte[] doubleToByte(double d) {
		byte[] b = new byte[8];
		long l = Double.doubleToLongBits(d);
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;

		}
		return b;
	}

	// �ֽڵ�����ת��
	public static synchronized double byte2Double(byte[] b, int start, int count) {
		long l;

		l = b[start + 0];
		l &= 0xff;
		l |= ((long) b[start + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[start + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[start + 3] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[start + 4] << 32);
		l &= 0xffffffffffl;

		l |= ((long) b[start + 5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[start + 6] << 48);

		l |= ((long) b[start + 7] << 56);
		return Double.longBitsToDouble(l);
	}

	/***
	 * ��ȡ����汾
	 * 
	 * @return ����汾��
	 */
	public static synchronized int getSoftVersion(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionCode;
	}

	public static synchronized int getTextHeight(Paint paint, String txt) {
		FontMetrics fm = paint.getFontMetrics();
		return (int) (fm.descent - fm.ascent);
	}
}

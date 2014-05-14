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
	 * 将汉字转换成byte 数组
	 * 
	 * @param data
	 *            待转换数组
	 * @param count
	 *            转换的长度
	 * @return 转换之后的数组
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
	 * 将字符数组转换成字符串
	 * 
	 * @param data
	 *            待转换数组
	 * @return 转换之后的字符串
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
	 * 将字符数组转换成字符串
	 * 
	 * @param data
	 *            待转换数组
	 * @return 转换之后的字符串
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
	 * 将获取的字节长度转换成INT
	 * 
	 * @param b0
	 *            低1字节
	 * @param b1
	 *            低2字节
	 * @param b2高1字节
	 * @param b3高2字节
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
	 * 将获取的字节长度转换成INT
	 * 
	 * @param b0
	 *            低1字节
	 * @param b1
	 *            低2字节
	 * @param b2高1字节
	 * @param b3高2字节
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
	 * 将获取的字节长度转换成INT
	 * 
	 * @param b0
	 *            低字节
	 * @param b1
	 *            高字节
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
	 * 4字节 int 转换成byte[];
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
	 * float 转换成 byte数组
	 * 
	 * @param data
	 *            待转换的数据
	 * @return 小端模式的BYTE数组 null 转换失败
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
	 * 将UTC时间转换成yyyy-MM-dd HH:mm:ss 对应的32字节数据
	 * 
	 * @param utc
	 *            待转换时间
	 * @param format
	 *            待转换格式
	 * @return 转换之后的字节
	 */
	public static synchronized byte[] utc2TimeByte(long utc, String format) {
		Date mDate = new Date(utc);
		SimpleDateFormat mFormat = new SimpleDateFormat(format);
		return String2Byte(mFormat.format(mDate), 32);
	}

	/***
	 * 将UTC时间转换成yyyy-MM-dd HH:mm:ss 对应的32字节数据
	 * 
	 * @param utc
	 *            待转换时间
	 * @return 转换之后的字节
	 */
	public static synchronized byte[] utc2TimeByte(long utc) {
		Date mDate = new Date(utc);
		SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String2Byte(mFormat.format(mDate), 32);
	}

	/***
	 * 将UTC时间转换成yyyy-MM-dd HH:mm:ss 字符串
	 * 
	 * @param utc
	 *            待转换时间
	 * @return 转换之后的字节
	 */
	public static synchronized String utc2TimeString(long utc) {
		Date mDate = new Date(utc);
		SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return mFormat.format(mDate);
	}

	/***
	 * 将UTC时间转换成yyyy-MM-dd HH:mm:ss 字符串
	 * 
	 * @param utc
	 *            待转换时间
	 * @param format
	 *            待转换格式
	 * @return 转换之后的字节
	 */
	public static synchronized String utc2TimeString(long utc, String format) {
		Date mDate = new Date(utc);
		SimpleDateFormat mFormat = new SimpleDateFormat(format);
		return mFormat.format(mDate);
	}

	/***
	 * 将字符串格式时间转化成utc
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
	 * 将BYTE数组转换成long 低位在前
	 * 
	 * @param data
	 *            转换之后的数据
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
	 * 将BYTE数组转换成long 低位在前
	 * 
	 * @param data
	 *            转换之后的数据
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
	 * 将BYTE数组转换成int 低位在前
	 * 
	 * @param data
	 *            转换之后的数据
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
	 * byte 转换成 float
	 * 
	 * @param data
	 * @return
	 */
	public static synchronized float byte2Float(byte[] data) {
		return Float.intBitsToFloat(byte2Int(data));
	}

	/***
	 * byte 转换成 float
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
	 * 将long型转换成byte数组 低位在前
	 * 
	 * @param data
	 *            转换之后的byte数组
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
	 * 将long型转换成byte数组 低位在前
	 * 
	 * @param data
	 *            转换之后的byte数组
	 * @return
	 */
	public static synchronized byte[] short2byte(short data) {
		byte[] temp = new byte[2];
		temp[1] = (byte) (data >> 8);
		temp[0] = (byte) data;
		return temp;
	}

	/***
	 * 创建文件所在的目录
	 * 
	 * @param path
	 *            文件路径
	 * @return true 成功 false 失败
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
	 * 检查网络连接状态
	 * 
	 * @return
	 */
	public static synchronized boolean cheackNetConnection(Context context) {
		ConnectivityManager ConManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取WIFI网络连接状态
		NetworkInfo.State wifi_state = ConManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState(); //
		if (wifi_state == NetworkInfo.State.CONNECTED) {
			return true;
		} else {
			// 判断3G网络连接状态
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
	 * BCD格式数据转换成十六进制
	 * 
	 * @param data
	 *            BCD数据
	 * @return 十六进制数据
	 */
	public static synchronized int bcd2Hex(int data) {
		return (data / 16) * 10 + data % 16;
	}

	/***
	 * HEX转换成BCD
	 * 
	 * @param data
	 *            待转换的数据
	 * @return 返回byte数据
	 */
	public static synchronized byte hex2Bcd(byte data) {
		return (byte) ((data / 10) * 16 + data % 10);
	}

	/***
	 * 将日期转换成utc 时间
	 * 
	 * @param year
	 *            年
	 * @param mongth
	 *            月
	 * @param day
	 *            日
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param sec
	 *            秒
	 * @return utc 时间
	 */
	public static synchronized long dateToUtc(int year, int month, int day,
			int hour, int minute, int sec) {
		Calendar calendar = new GregorianCalendar(year, month, day, hour,
				minute, sec);
		return calendar.getTimeInMillis();
	}

	// 浮点到字节转换
	public static synchronized byte[] doubleToByte(double d) {
		byte[] b = new byte[8];
		long l = Double.doubleToLongBits(d);
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;

		}
		return b;
	}

	// 字节到浮点转换
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
	 * 获取软件版本
	 * 
	 * @return 软件版本号
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

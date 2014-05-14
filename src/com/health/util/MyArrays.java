package com.health.util;

import java.lang.reflect.Array;

import android.util.Log;

public class MyArrays<T> {
	private static final String TAG = "MyArrays";

	private MyArrays() {// �����࣬�ⲿ����ʵ����
	}

	/**
	 * ��data��[start,end)��������俪���ָ��Ʒ���
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static <T> T[] copyOfRange(T[] data, int start, int end,
			Class<T> type) {
		@SuppressWarnings("unchecked")
		T[] buffer = (T[]) Array.newInstance(type, end - start);
		for (int i = start; i < end; i++) {
			buffer[i] = data[i - start];
		}
		return buffer;
	}

	/**
	 * ��data��[start,end)��������俪���ָ��Ʒ���
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static byte[] copyOfRange(byte[] data, int start, int end) {
		// Log.i(TAG, "start:" + start + ", end:" +
		// end);
		byte[] buffer = new byte[end - start];
		for (int i = start; i < end; i++) {
			buffer[i - start] = data[i];
		}
		return buffer;
	}

	/**
	 * ��data��[0,length)���ָ��Ʒ��أ����length����data���ȣ�
	 * �����Ĳ�����Ϊ0
	 * 
	 * @param data
	 * @param length
	 * @return
	 */
	public static byte[] copyOf(byte[] data, int length) {
		byte[] buffer = new byte[length];
		if (length < data.length)// ����[0,length)����
			return copyOfRange(data, 0, length);
		int i;
		for (i = 0; i < data.length; i++)
			buffer[i] = data[i];
		for (; i < length; i++)
			// �����Ĳ�������Ϊ0
			buffer[i] = 0;
		return buffer;

	}

}

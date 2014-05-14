package com.health.device;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.util.SparseArray;

import com.health.util.MyArrays;

/**
 * �ٽ��豸
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2013-10-25 ����9:17:13
 */
public class BeneCheck {
	private static final String TAG = "BeneCheck";
	// ͬ������
	public static final byte[] ECHO = { '$', 'P', 'C', 'L', 0x01, 0x00, 0x00,
			0x00, 0x00, 0x00, (byte) 0xE0 };
	// ��ѯѪ�Ǽ�¼����������
	public static final byte[] QUERY_GLU_NUM = { '$', 'P', 'C', 'L', 0x40,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x1F };
	// ��ѯ�����¼����������
	public static final byte[] QUERY_UA_NUM = { '$', 'P', 'C', 'L', 0x50, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x2F };
	// ��ѯ�ܵ��̴���¼����������
	public static final byte[] QUERY_CHOL_NUM = { '$', 'P', 'C', 'L', 0x60,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x3F };
	// ���Ѫ�Ǽ�¼����
	public static final byte[] CLEAR_GLU_NUM = { '$', 'P', 'C', 'L', 0x20,
			0x00, 0x01, 0x00, 0x00, 0x00, 0x00 };
	// ��������¼����
	public static final byte[] CLEAR_UA_NUM = { '$', 'P', 'C', 'L', 0x20, 0x00,
			0x02, 0x00, 0x00, 0x00, 0x01 };
	// ����ܵ��̴���¼����
	public static final byte[] CLEAR_CHOL_NUM = { '$', 'P', 'C', 'L', 0x20,
			0x00, 0x04, 0x00, 0x00, 0x00, 0x03 };
	// ��ѯѪ�Ǽ�¼����
	public static final byte TOKEN_GLU_NUM = 0x40;
	// ��ѯѪ�Ǽ�¼
	public static final byte TOKEN_GLU_RECORD = 0x41;
	// ��ѯ�����¼����
	public static final byte TOKEN_UA_NUM = 0x50;
	// ��ѯ�����¼
	public static final byte TOKEN_UA_RECORD = 0x51;
	// ��ѯ�ܵ��̴���¼����
	public static final byte TOKEN_CHOL_NUM = 0x60;
	// ��ѯ�ܵ��̴���¼
	public static final byte TOKEN_CHOL_RECORD = 0x61;
	// ����ͷ�����������ݷָ�
	private static final byte[] HEAD = { '$', 'P' };

	/**
	 * ��ȡ�����
	 * 
	 * @param data
	 */
	public static byte getCheckSum(byte[] data) {
		int sum = 0;
		for (int i = 1; i < data.length; i++) {
			sum += 0xff & data[i];
		}
		return (byte) (sum & 0xff);
	}

	/**
	 * ����ʱ���У���
	 * 
	 * @param data
	 * @return
	 */
	public static boolean check(byte[] data) {
		byte checkSum = getCheckSum(MyArrays.copyOfRange(data, 0,
				data.length - 1));
		boolean result = checkSum == data[data.length - 1];
		Log.i(TAG, "check:" + Arrays.toString(data) + "->" + result);
		return result;
	}

	/**
	 * ��ȡ���ݱ�־
	 * 
	 * @param data
	 */
	private static byte getToken(byte[] data) {
		return data[4];
	}

	/**
	 * �Ӳ�ѯ�����������л�ȡ������ֵ
	 * 
	 * @param data
	 * @return
	 */
	public static int getNum(byte[] data) {
		// $PCL 0x40 0x00 0x00 0x00 0x02 0x00 N_REC
		// checksum
		return (data[10] & 0xff) + ((data[11] & 0xff) << 8);
	}

	/**
	 * ��ȡ��ѯ���¼�¼������
	 * 
	 * @param token
	 * @param num
	 * @return
	 */
	public static byte[] getLatestRecordCommand(int token, int num) {
		// $PCL 0x41 0x00 0x00 0x00 0x04 0x00 R_INDEX
		// checksum
		// $PCL 0x51 0x00 0x00 0x00 0x04 0x00 R_INDEX
		// checksum
		// $PCL 0x61 0x00 0x00 0x00 0x04 0x00 R_INDEX
		// checksum
		if (num <= 0)
			return null;
		byte[] command = { '$', 'P', 'C', 'L', 0x61, 0x00, 0x00, 0x00, 0x04,
				0x00, 0x1, 0x0, 0x0, 0x0, 0x0 };
		command[4] = (byte) (token + 1);
		command[10] = (byte) ((num) % 10);
		command[11] = (byte) ((num) / 10);
		command[12] = (byte) ((num) % 10);
		command[13] = (byte) ((num) / 10);
		command[14] = getCheckSum(MyArrays.copyOfRange(command, 0,
				command.length - 1));
		return command;
	}

	/**
	 * �ӷ��������л�ȡ������¼
	 * 
	 * @param readBuf
	 * @return
	 */
	public static Record getRecord(byte[] data) {
		// ��¼��data����9���ֽڣ�index�����ֽڣ�������ʱ�ָ�һ���ֽ�,����ֵ�����ֽ�
		// $PCL 0x41 0x00 0x00 0x00 0x09 0x00 RECORD
		// checksum
		String date = String.format("%04d-%02d-%02d %02d:%02d:00",
				2000 + data[12], data[13], data[14], data[15], data[16]);
		int ivalue = (data[17] & 0xff) + ((data[18] & 0xff) << 8);
		byte token = getToken(data);
		double value = 0;
		DecimalFormat df=new DecimalFormat("0.00");
		switch(token)
		{
			case TOKEN_GLU_RECORD:
			{
				value=Double.parseDouble(df.format(ivalue*1.11/20.0));
				break;
			}
			case TOKEN_UA_RECORD:
			{
				value=Double.parseDouble(df.format(ivalue/16.67));
				break;
			}
			case TOKEN_CHOL_RECORD:
			{
				value=Double.parseDouble(df.format(ivalue/38.63));
				break;
			}
			
		}
		if (token == TOKEN_GLU_RECORD || token == TOKEN_UA_RECORD
				|| token == TOKEN_CHOL_RECORD)
			System.out.println(Arrays.toString(data));
		return new Record(date, value);
	}

	/**
	 * �ٽ�Ѫ���豸���ݼ�¼�ṹ��
	 * 
	 * @author jiqunpeng
	 * 
	 *         ����ʱ�䣺2013-10-26 ����10:06:01
	 */
	public static class Record {
		public final String date;
		public final double value;

		public Record(String date, double value2) {
			this.date = date;
			this.value = value2;
		}
	}

	/**
	 * ���������ݰ����ͻ���
	 * 
	 * @param buffer
	 * @return
	 */
	public static SparseArray<List<byte[]>> getLegalPatternsFromBuffer(
			byte[] buffer) {
		List<byte[]> datas = HealthDevice.splitBufferData(buffer, HEAD);
		SparseArray<List<byte[]>> map = new SparseArray<List<byte[]>>();
		for (byte[] data : datas) {
			if (check(data) == true) {
				int token = getToken(data);
				if (map.get(token) == null) {
					List<byte[]> pattern = new ArrayList<byte[]>();
					pattern.add(data);
					map.put(token, pattern);
				} else {
					map.get(token).add(data);
				}
			}
		}
		return map;
	}

}

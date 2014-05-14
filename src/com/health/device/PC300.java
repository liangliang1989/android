package com.health.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.health.util.FileManger;
import com.health.util.L;

import android.util.Log;
import android.util.SparseArray;

public class PC300 {
	// ���ڼ���У��͵ı�
	public static final byte[] CRC_TABLE = { 0, 94, -68, -30, 97, 63, -35,
			-125, -62, -100, 126, 32, -93, -3, 31, 65, -99, -61, 33, 127, -4,
			-94, 64, 30, 95, 1, -29, -67, 62, 96, -126, -36, 35, 125, -97, -63,
			66, 28, -2, -96, -31, -65, 93, 3, -128, -34, 60, 98, -66, -32, 2,
			92, -33, -127, 99, 61, 124, 34, -64, -98, 29, 67, -95, -1, 70, 24,
			-6, -92, 39, 121, -101, -59, -124, -38, 56, 102, -27, -69, 89, 7,
			-37, -123, 103, 57, -70, -28, 6, 88, 25, 71, -91, -5, 120, 38, -60,
			-102, 101, 59, -39, -121, 4, 90, -72, -26, -89, -7, 27, 69, -58,
			-104, 122, 36, -8, -90, 68, 26, -103, -57, 37, 123, 58, 100, -122,
			-40, 91, 5, -25, -71, -116, -46, 48, 110, -19, -77, 81, 15, 78, 16,
			-14, -84, 47, 113, -109, -51, 17, 79, -83, -13, 112, 46, -52, -110,
			-45, -115, 111, 49, -78, -20, 14, 80, -81, -15, 19, 77, -50, -112,
			114, 44, 109, 51, -47, -113, 12, 82, -80, -18, 50, 108, -114, -48,
			83, 13, -17, -79, -16, -82, 76, 18, -111, -49, 45, 115, -54, -108,
			118, 40, -85, -11, 23, 73, 8, 86, -76, -22, 105, 55, -43, -117, 87,
			9, -21, -75, 54, 104, -118, -44, -107, -53, 41, 119, -12, -86, 72,
			22, -23, -73, 85, 11, -120, -42, 52, 106, 43, 117, -105, -55, 74,
			20, -10, -88, 116, 42, -56, -106, 21, 75, -87, -9, -74, -24, 10,
			84, -41, -119, 107, 53 };

	public static final byte[] COMMAND_BP_START = { -86, 85, 64, 2, 1, 41 };// ��ʼ����Ѫѹ����
	public static final byte[] COMMAND_BP_STOP = { -86, 85, 64, 2, 2, -53 };// ��ʼ����Ѫѹ����
	public static final byte[] COMMAND_BETTERY = { (byte) 0xaa, 0x55, 0x51,
			0x02, 0x01, -56 };// ��ص�������
	public static final byte[] COMMAND_TEMP_START = { (byte) 0xaa, 0x55, 0x70,
			0x03, 0x01, (byte) 0x40, (byte) 0x1A };// ���¿�ʼ��������
	public static final byte[] COMMAND_ECG_START = { (byte) 0xaa, 0x55, 0x30,
			0x02, 0x01, -58 };// �ĵ翪ʼ��������
	public static final byte[] COMMAND_ECG_STOP = { (byte) 0xaa, 0x55, 0x30,
			0x02, 0x02, 36 };// �ĵ�ֹͣ��������

	public static final byte TOKEN_BP_CURRENT = 0x42;// ��ǰѪѹ����
	public static final byte TOKEN_BP_RESULT = 0x43;// Ѫѹ�����������
	public static final byte TOKEN_BO_WAVE = 0x52;// Ѫ������ͼ����
	public static final byte TOKEN_POWER_OFF = (byte) 0xD0;// �����ǹػ�����
	public static final byte TOKEN_BO_PAKAGE = (byte) 0x53;// �ϴ��������ݰ�����
	public static final byte TOKEN_TEMP = (byte) 0x72;// �ϴ���������
	public static final byte TOKEN_ECG_WAVE = (byte) 0x32;// �ĵ粨��ͼ����
	public static final byte TOKEN_ECG_RESULT = (byte) 0x33;// �ĵ�����������

	public static final int PROBE_OFF = 0x02;// ̽������
	// Ѫѹ��������
	public static final int ERROR_RESULT = -1;// �����������
	public static final int ILLEGAL_PULSE = 0;// ����������Ч������
	public static final int BAD_BOUND = 1;// ����û�а��
	public static final int ERROR_VALUE = 2;// ������ֵ�������
	// public static final int PRESSS_TOO_HIGH = 3;//
	// ����ѹ������295mmHg .���볬ѹ����
	// public static final int MOVED_TOO_MANY = 4;//
	// ��Ԥ���ࣨ�������ƶ���˵���ȣ�
	private static final int MIN_DATA_SIZE = 6;// ��С�ֽ���

	private static final byte[] HEAD = { (byte) 0xaa, 0x55 };

	private static final String TAG = "PC300.this";

	public static final String[] ECG_RESULT = { "����δ���쳣", "�������������Կ���ע����Ϣ",
			"������������������ע����Ϣ", "������������������������ѯҽ��", "�������������Ի���ע����Ϣ",
			"������������������ע����Ϣ", "��������ż������������������ѯҽ��", "���������������ڲ���������ѯҽ��",
			"�������������Կ����ż������������������ѯҽ��", "�������������Ի�����ż������������������ѯҽ��",
			"�������������Ի������������ڲ���������ѯҽ��", "������Ư�������²���", "������������������в���Ư������ѯҽ��",
			"�������������������в���Ư������ѯҽ��", "��������ż�������������̰��в���Ư������ѯҽ��",
			"���������������ڲ�������в���Ư������ѯҽ��", "�źŽϲ������²���" };

	/**
	 * �����ֽ����е�У���
	 * 
	 * @param data
	 * @return
	 */
	public static byte getCRC(byte[] data) {
		byte crc = 0;
		for (int j = 0; j < data.length; ++j) {
			crc = CRC_TABLE[(0xFF & (crc ^ data[j]))];
		}
		return crc;
	}

	/**
	 * �������ݵ�У����Ƿ���ȷ
	 * 
	 * @param data
	 * @return
	 */
	public static boolean check(byte[] data) {
		if (data.length < MIN_DATA_SIZE)
			return false;
		byte[] content = new byte[data.length - 1];
		for (int i = 0; i < content.length; i++)
			content[i] = data[i];
		byte crc = getCRC(content);
		boolean result = crc == data[data.length - 1];
		// Log.i(TAG, "check:" + Arrays.toString(data)
		// + "->" + result);
		return result;

	}

	private void printDataWithCrc(byte[] data, String dataName) {
		byte crc = getCRC(data);
		System.out.print(dataName + " = {");
		for (byte d : data) {
			System.out.print(d + ",");
		}
		System.out.println(crc + "}");
	}

	/**
	 * ��ȡ���ݵ�����
	 * 
	 * @param data
	 * @return
	 */
	public byte getToken(byte[] data) {
		byte token = (byte) (data[2] & 0xff);
		Log.i(TAG + " token", new Byte(token).toString());
		return token;
		// return (byte) (data[2] & 0xff);// ��3���ֽڱ�ʾ����
	}

	/**
	 * 
	 * ��ȡ��ǰѪѹֵ
	 * 
	 * @param data
	 * @return
	 */
	public int getCurrentBp(byte[] data) {
		// ��ͷ��2�� ���ƣ�1�� ���ȣ�1�� ���ͣ�1�� ���ݣ�2�� У��� ��1��
		return ((data[5] & 0xf) << 8) + (data[6] & 0xff);
	}

	/**
	 * ����Ѫѹ�������,���������жϽ��������[0]���[1]������ѹ������ѹ������
	 * 
	 * @param data
	 * @return
	 */
	public int[] getResultBp(byte[] data) {
		if (data.length == 11) {// ������������
			// ��ͷ��2�� ���ƣ�1�� ���ȣ�1�� ���ͣ�1�� ���ݣ�5) У��ͣ�1��
			// -86, 85, 67, 7, 1, 0, 87, 0, 64, 68, -27
			int pulseTag = (0xff & data[5]) >> 7;// ���������1��ʾ���ʲ��룬0��ʾ����
			int sys = ((0x7f & data[5]) << 8) + (data[6] & 0xff);// ����ѹ,��5���ֽڵĺ�7Ϊ���6���ֽ�
			// int map = 0xff&data[7];//ƽ��ѹ
			int dia = 0xff & data[8];// ����ѹ
			int pulse = 0xff & data[9];// ����
			int[] bpResult = { pulseTag, sys, dia, pulse };
			return bpResult;
		} else {// �����������
			int error = 0x01 & data[5];
			int[] errorResult = { ERROR_RESULT, error };
			return errorResult;
		}
	}

	/**
	 * ��ȡѪ������ͼ�����ݣ�һ������һ�����������
	 * 
	 * @param data
	 * @return
	 */
	public int[] getBoWave(byte[] data) {
		if (data[4] == 0x01) {
			int[] value = { data[5] & 0x7f, data[6] & 0x7f };
			return value;
		}
		return new int[] {};
	}

	/**
	 * Ѫ������\����\̽ͷ״̬
	 * 
	 * @param data
	 * @return
	 */
	public int[] getSpO2(byte[] data) {
		return new int[] { 0xff & data[5], (data[7] << 8) + data[6],
				data[9] & PROBE_OFF };
	}

	/**
	 * ��ȡ����
	 * 
	 * @param bs
	 * @return
	 */
	public Float getTemp(byte[] data) {
		int fDegree = ((data[6] & 0xff) << 8) | (data[7] & 0xff);
		return (float) (30.00 + (1.0 * fDegree / 100));
	}

	// -86, 85, 83, 7, 1, 98, 59, 0, 85, 0, -107,

	/**
	 * ����ָ��Ķ������ݵ�У��ͣ�����һ�����͵������ж���������� ֻ���������У������ȷ��һ��
	 * ���������һ��map�У���Ϊ���ݵ����ƣ�ֵΪһ������������
	 * 
	 * @param datas
	 * @return
	 */
	public SparseArray<byte[]> checkCrcAndRetainSinglePattern(List<byte[]> datas) {
		SparseArray<byte[]> map = new SparseArray<byte[]>();
		for (byte[] data : datas) {
			if (check(data) == true) {
				int token = getToken(data);
				map.put(token, data);
			}
		}
		return map;
	}

	/**
	 * �������Ķ������ݷָ����ÿ���������µĺϷ������ݣ����ж���Ѫѹ��ǰֵ�����ݣ�
	 * ֻ�������һ���Ϸ��ĵ�ǰѪѹ����
	 * 
	 * @param buffer
	 * @return
	 */
	public SparseArray<byte[]> getSingleLegalPatternFromBuffer(byte[] buffer) {
		return checkCrcAndRetainSinglePattern(HealthDevice.splitBufferData(
				buffer, HEAD));
	}

	/**
	 * ���������ݰ����ͻ���
	 * 
	 * @param buffer
	 * @return
	 */
	public SparseArray<List<byte[]>> getLegalPatternsFromBuffer(byte[] buffer) {
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

	public static void main(String[] args) {
		PC300 testCrc = new PC300();
		testCrc.printDataWithCrc(COMMAND_BP_START, "COMMAND_BP_START");
		testCrc.printDataWithCrc(COMMAND_BP_STOP, "COMMAND_BP_STOP");
		testCrc.printDataWithCrc(COMMAND_BETTERY, "COMMAND_BETTERY");
		testCrc.printDataWithCrc(COMMAND_TEMP_START, "COMMAND_YEMP");
	}

	/***
	 * ��ȡ�ĵ�Ĳ�������
	 * 
	 * @param each
	 * @return
	 */
	public EcgFrame getEcgFram(byte[] data) {
		if (data.length != 59)// ÿһ֡�����ݳ�Ϊ59
			return null;
		int framNum = data[5] & 0xff;
		int[] ecg = new int[25];
		int index = 0;
		for (int i = 7; i < 56; i += 2)
			ecg[index++] = data[i] & 0xff;
		// data[56]Ϊʵʱ����ֵ����ʱ����
		boolean flag = (data[57] & 0x80) == 1 ? true : false;
		return new EcgFrame(framNum, ecg, flag);
	}

	/***
	 * �ĵ����ݵ�֡
	 * 
	 * @author jiqunpeng
	 * 
	 *         ����ʱ�䣺2014-3-17 ����8:45:26
	 */
	public class EcgFrame implements Comparable<EcgFrame> {
		private int framNum;// ֡��
		private int[] ecg;// �ĵ�ֵ
		private boolean isDeviceOff;// ��������
		private long time;// ����ʱ��

		public EcgFrame(int framNum, int[] ecg, boolean isDeviceOff) {
			super();
			this.framNum = framNum;
			this.ecg = ecg;
			this.isDeviceOff = isDeviceOff;
			this.time = System.currentTimeMillis();
		}

		public int getFramNum() {
			return framNum;
		}

		public int[] getEcg() {
			return ecg;
		}

		public boolean isDeviceOff() {
			return isDeviceOff;
		}
		public long getTime() {
			return time;
		}

		@Override
		public int compareTo(EcgFrame other) {
			return framNum - other.framNum;
		}

	}

	// �����ĵ������֡
	public static List<EcgFrame> ecgFrames = new ArrayList<EcgFrame>();

	/***
	 * ���һ��֡
	 * 
	 * @param frame
	 */
	public void addEcgFrameAndSort(EcgFrame frame) {
		// saveEcgFrams(frame);
		L.i("addEcgFrameAndSort",
				frame.getFramNum() + ":" + Arrays.toString(frame.ecg));
		ecgFrames.add(frame);
		Collections.sort(ecgFrames);
	}

	/***
	 * ����һ��֡�����±�
	 * 
	 * @param frameNum
	 * @return
	 */
	private int findEcgFrameIndex(int frameNum) {
		for (int i = 0; i < ecgFrames.size(); i++)
			if (ecgFrames.get(i).getFramNum() == frameNum)
				return i;
		return -1;
	}

	public EcgFrame getNextFrame(int curFrameNum) {
		int index = findEcgFrameIndex(curFrameNum);
		if (index != -1) {
			if (index < ecgFrames.size() - 1
					&& ecgFrames.get(index + 1).getFramNum() == curFrameNum + 1)
				return ecgFrames.get(index + 1);
		}
		return null;
	}

	/**
	 * �ĵ�����������������ֵ
	 * 
	 * @param bs
	 * @return
	 */
	public int[] getEcgResult(byte[] data) {
		return new int[] { data[5] & 0xff, data[7] & 0xff };
	}

	public void saveEcgFrams(EcgFrame frame) {
		StringBuilder sb = new StringBuilder();
		sb.append(frame.framNum);
		sb.append(":");
		int[] ecg = frame.getEcg();
		for (int j = 0; j < ecg.length; j++) {
			sb.append(ecg[j]);
		}
		sb.append("\n");
		FileManger.getInstance().append(sb.toString());
	}
}

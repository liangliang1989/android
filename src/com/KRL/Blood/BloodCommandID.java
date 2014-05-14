package com.KRL.Blood;

public class BloodCommandID
{
	public class Phone2Device
	{
		/**��ʼ���β���*/
		public final static byte START_SINGLE_TEST = 0x10;
		/**ֹͣ���β���*/
		public final static byte STOP_TEST = 0x1F; // ֹͣ���β���
		/**�����ն˱��*/
		public final static byte SET_TERMNUMBER = 0x14;
		/**��ѯ�ն˱��*/
		public final static byte GET_TERMNUMBER = 0x15;
		/**�����ʷ��������*/
		public final static byte CLEAR_TERMDATA = 0x17;
		/**��ȡ�Զ�����Ѫѹ����*/
		public final static byte READ_AUTOTEST_BLOODPRESSURE = 0x19;
		/**��ʼ����������/������*/
		public final static byte START_TEST = 0x1B;
		/**ֹͣ�Զ�����*/
		public final static byte STOP_AUTOTEST = 0x1C;// ֹͣ�Զ�����
		/**��ȡ������Ϣ*/
		public final static byte GET_BATTERYVALUE = 0x21;
	}
	
	public class Device2Phone
	{
		public final static byte GET_TERMNUMBER = 0x15;
		/**ʵʱѹ������*/
		public final static byte POSTBACKPRESSURE = 0x12;
		/**����Ѫѹ��������*/
		public final static byte POSTBACKBLOODPRESSURERESULT = 0x13;
		/**�����ն˱��*/
		public final static byte POSTBACKTERMNUBER = 0x16;
		/**��ȡ�����ʷ�������ݽ��*/
		public final static byte POSTBACKLEARTERMDATARESULT = 0x18;
		/**�����Զ�����Ѫѹ����*/
		public final static byte POSTBACKREADHISDATA = 0x1A;
		/**�洢�ռ�����*/
		public final static byte OUTOFMEMORY = 0x1D;
		/**���յ�ָ��ظ�(���յ�������Ϣ)*/
		public final static byte RECEIVESTRUCESUCESS = 0x1E;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.KRL.Blood;

public class BloodCommandID
{
	public class Phone2Device
	{
		/**开始单次测量*/
		public final static byte START_SINGLE_TEST = 0x10;
		/**停止单次测量*/
		public final static byte STOP_TEST = 0x1F; // 停止单次测量
		/**设置终端编号*/
		public final static byte SET_TERMNUMBER = 0x14;
		/**查询终端编号*/
		public final static byte GET_TERMNUMBER = 0x15;
		/**清除历史测量数据*/
		public final static byte CLEAR_TERMDATA = 0x17;
		/**获取自动测量血压数据*/
		public final static byte READ_AUTOTEST_BLOODPRESSURE = 0x19;
		/**开始测量（单次/连续）*/
		public final static byte START_TEST = 0x1B;
		/**停止自动测量*/
		public final static byte STOP_AUTOTEST = 0x1C;// 停止自动测量
		/**获取电量信息*/
		public final static byte GET_BATTERYVALUE = 0x21;
	}
	
	public class Device2Phone
	{
		public final static byte GET_TERMNUMBER = 0x15;
		/**实时压力数据*/
		public final static byte POSTBACKPRESSURE = 0x12;
		/**返回血压测量数据*/
		public final static byte POSTBACKBLOODPRESSURERESULT = 0x13;
		/**返回终端编号*/
		public final static byte POSTBACKTERMNUBER = 0x16;
		/**获取清除历史测量数据结果*/
		public final static byte POSTBACKLEARTERMDATARESULT = 0x18;
		/**返回自动测量血压数据*/
		public final static byte POSTBACKREADHISDATA = 0x1A;
		/**存储空间已满*/
		public final static byte OUTOFMEMORY = 0x1D;
		/**接收到指令回复(接收到电量信息)*/
		public final static byte RECEIVESTRUCESUCESS = 0x1E;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

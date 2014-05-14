package com.KRL.Data;

import java.io.IOException;
import java.io.RandomAccessFile;
import com.KRL.Tools.Tools;
// ��������
public class DataDescription
{
	public static final int		BUFFER_SIZE			= 512;
	public static final int		LeadIndex_I			= 0;
	public static final int		LeadIndex_II		= 1;
	public static final int		LeadIndex_III		= 2;
	public static final int		LeadIndex_aVR		= 3;
	public static final int		LeadIndex_aVL		= 4;
	public static final int		LeadIndex_aVF		= 5;
	public static final int		LeadIndex_V1		= 6;
	public static final int		LeadIndex_V2		= 7;
	public static final int		LeadIndex_V3		= 8;
	public static final int		LeadIndex_V4		= 9;
	public static final int		LeadIndex_V5		= 10;
	public static final int		LeadIndex_V6		= 11;
	public static final int		LeadIndex_X			= 0;
	public static final int		LeadIndex_Y			= 1;
	public static final int		LeadIndex_Z			= 2;
	public static final int		LeadIndex_XY		= 3;
	public static final int		LeadIndex_XZ		= 4;
	public static final int		LeadIndex_ZY		= 5;
	public static final int		MAX_ECG_LEAD_COUNT	= 12;
	public static final int		MAX_VCG_LEAD_COUNT	= 6;
	// 1298����ͨ�ĵ�����
	public static final int		SAMPLE_FREQUENCY	= 1000;
	// �Ŵ���
	// 1298����ͨ�ĵ�����
	public static final float	ECG_SCALE			= 0.50895f;
	// �ĵ������Ŵ���
	public static final float	VCG_SCALE			= 500.0f;
	//
	// �������ݽ���ϵ��
	public static final float	VCG_ADJUST_X		= 1.432f;
	public static final float	VCG_ADJUST_Y		= 1.184f;
	public static final float	VCG_ADJUST_Z		= 1.808f;
	// ��������
	// �����ĵ�����
	public static final byte	TYPE_ECG			= 0;
	// �ĵ���������
	public static final byte	TYPE_VCG			= 1;
	// �����ĵ����ݷ�Χ
	public static final float	VOLTAGELOW_ECG		= (-2500.0f);
	public static final float	VOLTAGEHIGH_ECG		= 2500.0f;
	public static final int		DIGVOLTAGELOW_ECG	= (-0x007fffff);
	public static final int		DIGVOLTAGEHIGH_ECG	= 0x007fffff;
	// �����ĵ����ݷ�Χ
	public static final float	VOLTAGELOW_VCG		= 0.0f;
	public static final float	VOLTAGEHIGH_VCG		= 3300.0f;
	public static final int		DIGVOLTAGELOW_VCG	= 0x0000;
	public static final int		DIGVOLTAGEHIGH_VCG	= 0x0fff;
	// ������ռ���ڿռ�Ĵ�С
	public static final int		MAX_BUFFER			= 512;								// bytes
	byte []						wVersion			= new byte[] { 0, 1 };				// �汾��,ĿǰΪ1
	public int					nSamplingFrequency	= SAMPLE_FREQUENCY;				// ����Ƶ��
	public int					nLeadCount			= MAX_ECG_LEAD_COUNT;				// ������
	public byte					tType				= 0;								// ��������
																						// 0��ͨ�ĵ�
																						// 1�����ĵ�
	public float				fVoltageLow			= VOLTAGELOW_ECG;					// ��͵�ѹֵ
	public float				fVoltageHigh		= VOLTAGEHIGH_ECG;					// ��ߵ�ѹֵ
	public int					nDigvoltageLow		= DIGVOLTAGELOW_ECG;				// �����ֵ��ѹֵ
	public int					nDigvoltageHigh		= DIGVOLTAGEHIGH_ECG;				// �����ֵ��ѹֵ
	public float				fScale				= ECG_SCALE;						// �Ŵ���
	public boolean				bBP					= false;							// �Ƿ��������ź�
																						// �ɼ�����ʱ�������˲�ʹ�ܣ����ڼ���ʱ����Ҫ���ɼ�ѡ������˲�����
																						// 0x01(���ߵ���) | 0x02(50Hz����) | 0x04(10ƽ��)
	public byte					tFilterType;
	public boolean []			bLeadEnable			= new boolean[MAX_ECG_LEAD_COUNT];

	//
	//
	public double GetDataValuePerMv()
	{
		return 1.0 / GetMvPerSample();
	}

	public double GetMvPerSample()
	{
		return ((fVoltageHigh - fVoltageLow) / fScale) / (double) (nDigvoltageHigh - nDigvoltageLow);
	}

	public boolean store(RandomAccessFile file) throws IOException
	{
		file.write(wVersion);
		file.write(Tools.intToBytes(nSamplingFrequency));
		file.write(Tools.intToBytes(nLeadCount));
		file.writeByte(tType);
		file.write(Tools.floatToBytes(fVoltageLow));
		file.write(Tools.floatToBytes(fVoltageHigh));
		file.write(Tools.intToBytes(nDigvoltageLow));
		file.write(Tools.intToBytes(nDigvoltageHigh));
		file.write(Tools.floatToBytes(fScale));
		file.writeBoolean(bBP);
		file.writeByte(tFilterType);
		for (int i = 0; i < MAX_ECG_LEAD_COUNT; i++)
		{
			file.writeBoolean(bLeadEnable[i]);
		}
		file.write(new byte[BUFFER_SIZE - 45]);
		return true;
	}

	public boolean load(RandomAccessFile file) throws IOException
	{
		file.read(wVersion, 0, 2);
		nSamplingFrequency = file.readInt();
		nLeadCount = file.readInt();
		tType = file.readByte();
		fVoltageLow = file.readFloat();
		fVoltageHigh = file.readFloat();
		nDigvoltageLow = file.readInt();
		nDigvoltageHigh = file.readInt();
		fScale = file.readFloat();
		bBP = file.readBoolean();
		tFilterType = file.readByte();
		for (int i = 0; i < MAX_ECG_LEAD_COUNT; i++)
		{
			bLeadEnable[i] = file.readBoolean();
		}
		return true;
	}
}

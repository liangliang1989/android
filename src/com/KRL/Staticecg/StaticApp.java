package com.KRL.Staticecg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.KRL.Data.DataBaseAdapter;
import com.KRL.Data.DataManager;
public class StaticApp extends Application
{
	private static StaticApp	mInstance		= null;
	private DataBaseAdapter		mDBAdapter		= null;
	public static final String	MSERVERIP		= "ecg.koanruler.com";		//"192.168.1.24";//
	public static final int		MSERVERPORT		= 8585;
	public static final int		MSAMPLINGTIME	= 60;						//�ɼ�ʱ��
	public static final int		MPAGENUMBER		= 20;						//ÿһҳ��ʾ�Ĳ��˸���
	public String				mTableName		= null;
	public String				mModulePath		= "";						// ��������Ŀ¼
	public String				mDataPath		= "/mnt/sdcard/staticecg/"; // ���ݴ洢Ŀ¼
	public int					mCenterID		= 0;
	public int					mCheckstationId	= 0;
	public DataManager			mDataMager		= new DataManager();
//	public Main_Activity		mMain			= null;
	// /////////////////////////////////////
	public String				mServerIP		= "ecg.koanruler.com";		//"ctc2.koanruler.com";	//"192.168.1.24";//
	public int					mServerPort		= 8585;
	public int					mSamplingTime	= 60;						//�ɼ�ʱ��
	public int					mPageNumber		= 20;						//ÿһҳ��ʾ�Ĳ��˸���
	public String				mAccount		= "";
	public String				mPassword		= "";
	public boolean				mAutoUpload		= false;
	public boolean				mSavePassword	= false;
	public boolean				mLogin			= false;
	public String				mMAC			= "";
	public int					mLastID			= 0;
	public int					mFirstID		= 0;

	public static StaticApp getinstance()
	{
		return mInstance;
	}

	public void load()
	{
		Properties p = loadConfig();
		String text = (String) p.get(Staticecg.CONFIG_ACCOUNT);
		if (text != null)
		{
			mAccount = text;
		}
		text = (String) p.get(Staticecg.CONFIG_PASSWORD);
		if (text != null)
		{
			mPassword = text;
		}
		text = (String) p.get(Staticecg.CONFIG_SAVEPASSWORD);
		if (text != null)
		{
			mSavePassword = Boolean.valueOf(text);;
		}
		text = (String) p.get(Staticecg.CONFIG_IP);
		if (text != null)
		{
			mServerIP = text;
		}
		text = (String) p.get(Staticecg.CONFIG_PORT);
		if (text != null && text.length() > 0)
		{
			mServerPort = Integer.valueOf(text);
		}
		text = (String) p.get(Staticecg.CONFIG_SAMPLINGGTIME);
		if (text != null && text.length() > 0)
		{
			mSamplingTime = Integer.valueOf(text);
		}
		text = (String) p.get(Staticecg.CONFIG_PAGENUMBER);
		if (text != null && text.length() > 0)
		{
			mPageNumber = Integer.valueOf(text);
		}
		text = (String) p.get(Staticecg.CONFIG_AUTOUPLOAD);
		if (text != null && text.length() > 0)
		{
			mAutoUpload = Boolean.valueOf(text);
		}
	}

	public void save()
	{
		Properties p = loadConfig();
		p.put(Staticecg.CONFIG_ACCOUNT, mAccount);
		p.put(Staticecg.CONFIG_PASSWORD, mPassword);
		p.put(Staticecg.CONFIG_SAVEPASSWORD, String.valueOf(mSavePassword));
		p.put(Staticecg.CONFIG_IP, mServerIP);
		p.put(Staticecg.CONFIG_PORT, String.valueOf(mServerPort));
		p.put(Staticecg.CONFIG_SAMPLINGGTIME, String.valueOf(mSamplingTime));
		p.put(Staticecg.CONFIG_PAGENUMBER, String.valueOf(mPageNumber));
		p.put(Staticecg.CONFIG_AUTOUPLOAD, String.valueOf(mAutoUpload));
		saveConfig(p);
	}

	public void init()
	{
		if (mDBAdapter == null)
		{
			this.mDBAdapter = new DataBaseAdapter(this);
			if (mTableName != null)
			{
				mDBAdapter.open(mTableName);
			}
		}
	}

	public void finit()
	{
		mDBAdapter.close();
		mDBAdapter = null;
	}

	public DataBaseAdapter getDB()
	{
		return mDBAdapter;
	}

	@Override
	public void onCreate()
	{
		// TODO �Զ����ɵķ������
		super.onCreate();
		init();
		mInstance = this;
	}

	@Override
	public void onTerminate()
	{
		// TODO �Զ����ɵķ������
		Log.v("DataBaseAdapter", "onTerminate");
		this.mDBAdapter.close();
		super.onTerminate();
	}

	public Properties loadConfig()
	{
		Properties btProper = new Properties();
		FileInputStream instream = null;
		try
		{
			instream = openFileInput(Staticecg.CONFIGFILE);
			btProper.load(instream);
			instream.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return btProper;
	}

	public void saveConfig(Properties btProper)
	{
		FileOutputStream outstream = null;
		try
		{
			outstream = openFileOutput(Staticecg.CONFIGFILE, Context.MODE_WORLD_WRITEABLE);
			btProper.store(outstream, "");
		}
		catch (FileNotFoundException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
}

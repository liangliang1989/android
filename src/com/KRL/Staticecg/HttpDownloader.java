package com.KRL.Staticecg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.util.Log;
public class HttpDownloader
{
	private static final String	TAG			= "HttpDownloader";
	private static final int	BUFFERSIZE	= 2048;

	public boolean downfile(String loadpath, String surl)
	{
		File file = new File(loadpath);
		if (file.exists())
		{
			return true;
		}
		HttpURLConnection urlConn = null;
		URL url = null;
		OutputStream fileoutput = null;
		try
		{
			url = new URL(surl);
		}
		catch (MalformedURLException e2)
		{
			// TODO �Զ����ɵ� catch ��
			e2.printStackTrace();
			Log.e(TAG, e2.toString());
			return false;
		}
		try
		{
			urlConn = (HttpURLConnection) url.openConnection();
		}
		catch (IOException e1)
		{
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
			Log.e(TAG, e1.toString());
			return false;
		}
		InputStream inputstream = null;
		try
		{
			inputstream = urlConn.getInputStream();
		}
		catch (IOException e1)
		{
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
			Log.e(TAG, e1.toString());
			return false;
		}
		try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			Log.e(TAG, e.toString());
			return false;
		}
		try
		{
			fileoutput = new FileOutputStream(file);
		}
		catch (FileNotFoundException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			Log.e(TAG, e.toString());
			return false;
		}
		byte [] buffer = new byte[BUFFERSIZE];
		int len = 0;
		try
		{
			while ((len = inputstream.read(buffer, 0, BUFFERSIZE)) != -1)
			{
				fileoutput.write(buffer, 0, len);
			}
			fileoutput.flush();
		}
		catch (IOException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			Log.e(TAG, e.toString());
			return false;
		}
		finally
		{
			try
			{
				fileoutput.close();
			}
			catch (IOException e)
			{
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				Log.e(TAG, e.toString());
				return false;
			}
		}
		return true;
	}
}

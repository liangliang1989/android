package com.KRL.Staticecg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.KRL.Network.MESSAGE;
import com.KRL.Network.NetworkPacket;
import com.KRL.Network.NetworkPacketFactory;
public class FileUploadThread extends Thread
{
	private static final String	TAG				= "FileUploadThread";
	private OutputStream		mOutputStream	= null;
	private Handler				mHandler		= null;
	private boolean				mCancel			= false;
	private static final int	BUFFER_SIZE		= 1024;
	private UploadFileDesc		mFileDesc		= null;

	public FileUploadThread(OutputStream outputstream, Handler handler, UploadFileDesc fileDesc)
	{
		mOutputStream = outputstream;
		mHandler = handler;
		mFileDesc = fileDesc;
	}

	@Override
	public void run()
	{
		boolean bSuccess = false;
		byte [] buffer = new byte[BUFFER_SIZE];
		byte [] data = null;
		FileInputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(mFileDesc.filePath);
			if (inputStream != null)
				inputStream.skip(mFileDesc.mStartPos);
		}
		catch (FileNotFoundException e1)
		{
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
			Log.v(TAG, "文件:" + mFileDesc.filePath + " 未找到");
		}
		catch (SocketTimeoutException e)
		{
			e.printStackTrace();
			Log.v(TAG, "文件:" + mFileDesc.filePath + "发送超时");
		}
		catch (IOException e1)
		{
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
			Log.v(TAG, "读取文件:" + mFileDesc.filePath + " 失败");
		}
		int count = 0;
		byte [] sendbuffer = new byte[BUFFER_SIZE + NetworkPacket.HEADER_SIZE];
		int [] length = new int[1];
		int flag = 0;
		while (!mCancel && inputStream != null)
		{
			try
			{
				int bytes = inputStream.read(buffer);
				count += bytes;
				if (bytes != BUFFER_SIZE)
				{
					NetworkPacketFactory.make_upload_file_data(buffer, bytes, sendbuffer, length);
					mOutputStream.write(sendbuffer, 0, length[0]);
					mOutputStream.flush();
					data = NetworkPacketFactory.make_upload_file_end(mFileDesc.toBytes());
					mOutputStream.write(data);
					mOutputStream.flush();
					mCancel = true;
					bSuccess = true;
					//Log.d(TAG,"文件读取完成");
				}
				else
				{
					NetworkPacketFactory.make_upload_file_data(buffer, bytes, sendbuffer, length);
					mOutputStream.write(sendbuffer, 0, length[0]);
					mOutputStream.flush();
				}
				//Log.d(TAG,"文件读取:"+count);
			}
			catch (SocketTimeoutException e)
			{
				e.printStackTrace();
				//Log.v(TAG, "文件:" + mFileDesc.filePath + "发送超时");
			}
			catch (IOException e)
			{
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				//Log.e(TAG,"文件读取发生错误");
				break;
			}
		}
		sendbuffer = null;
		length = null;
		if (bSuccess == true && mHandler != null)
		{
			Message msg = mHandler.obtainMessage(MESSAGE.MESSAGE_UPLOAD_FILE);
			Bundle extra = new Bundle();
			extra.putInt(MESSAGE.MESSAGE_KEY_DATA, mFileDesc.nDataID);
			msg.setData(extra);
			mHandler.sendMessage(msg);
		}
	}

	public void cancel()
	{
		mCancel = true;
	}
}

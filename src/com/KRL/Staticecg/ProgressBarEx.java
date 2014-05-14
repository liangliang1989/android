package com.KRL.Staticecg;

import com.KRL.Network.MESSAGE;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
public class ProgressBarEx extends ProgressBar
{
	public ProgressBarEx(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		filter.addAction(MESSAGE.MESSAGE_KEY_PROGRESS);
		this.setMax(100);
	}

	public int			mID			= -1;
	Context				mContext	= null;
	IntentFilter		filter		= new IntentFilter();
	BroadcastReceiver	receiver	= new BroadcastReceiver()
									{
										@Override
										public void onReceive(Context context, Intent intent)
										{
											// TODO 自动生成的方法存根
											Bundle data = intent.getExtras();
											int id = data.getInt(MESSAGE.MESSAGE_KEY_ID);
											int progress = data.getInt(MESSAGE.MESSAGE_KEY_PROGRESS);
											int ctrl = data.getInt(MESSAGE.MESSAGE_KEY_CTRL);
											if (id >= 0 && id == mID)
											{
												if (ctrl > 0)
												{
													setVisibility(VISIBLE);
												}
												else
												{
													setVisibility(GONE);
												}
												setProgress(progress);
											}
										}
									};

	@Override
	protected void onAttachedToWindow()
	{
		// TODO 自动生成的方法存根
		super.onAttachedToWindow();
		mContext.registerReceiver(receiver, filter);
	}

	@Override
	protected void onDetachedFromWindow()
	{
		// TODO 自动生成的方法存根
		super.onDetachedFromWindow();
		mContext.unregisterReceiver(receiver);
	}
}

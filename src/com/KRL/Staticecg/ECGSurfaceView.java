package com.KRL.Staticecg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.KRL.Data.DataDescription;
import com.KRL.Data.DataSegment;

public class ECGSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {
	private static final String TAG = "ECGSurfaceView";
	boolean mbLoop = false;
	SurfaceHolder mSurfaceHolder = null;
	int nMillStep = 50; // 30ms刷新一次
	Paint mPaint = null;
	float mperMM = 0.0f;
	float mcx = 0.0f;
	float mcy = 0.0f;
	int mTopOffset = 0;
	int mLeftOffset = 0;
	int[][] mBuffer = null;
	Bitmap mGrid = null;
	String[] mLead = new String[] { "I", "II", "III", "aVR", "aVL", "aVF",
			"V1", "V2", "V3", "V4", "V5", "V6" };
	public int nLastPos = 0;
	public int nStartPos = 0;
	public int nEndPos = 0;
	public DataSegment mDataSegment = null;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	public int mScreenWidthInCx = 0;
	public int nWidth = 0;
	public int nHeight = 0;

	public void init_canvas_param(DisplayMetrics metrics, DataDescription desc,
			int[][] buffer) {
		Log.e(TAG, "init_canvas_param");
		mperMM = (float) (metrics.densityDpi * 10 / 254);
		mcx = mperMM * 25.0f / desc.nSamplingFrequency;
		mcy = (float) (mperMM * 5.0f * desc.GetMvPerSample());
		nLastPos = 0;
		nStartPos = 0;
		nEndPos = 0;
		mBuffer = buffer;
		Canvas canvas = this.mSurfaceHolder.lockCanvas();
		if (canvas != null) {
			if (null != mGrid) {
				Paint p = new Paint();
				p.setColor(Color.WHITE);
				canvas.drawBitmap(mGrid, 0, 0, p);
			}
			this.mSurfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	public ECGSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.e(TAG, "ECGSurfaceView");
		// TODO 自动生成的构造函数存根
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLUE);
		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		this.setFocusable(true);
		mbLoop = true;
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while (mbLoop) {
			try {
				Thread.sleep(nMillStep);
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			}
			synchronized (this.mSurfaceHolder) {
				this.paint();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO 自动生成的方法存根
		Log.e(TAG, "surfaceChanged");
		drawBackground();
		new Thread(this).start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO 自动生成的方法存根
		Log.e(TAG, "surfaceCreated");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO 自动生成的方法存根
		Log.e(TAG, "surfaceDestroyed");
		mbLoop = false;
	}

	private void drawBackground() {
		nWidth = getWidth();
		nHeight = getHeight();
		mGrid = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ARGB_8888);
		Canvas cv = new Canvas();
		cv.setBitmap(mGrid);
		cv.drawColor(Color.WHITE);
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.argb(30, 255, 100, 0));
		float fgridwidth = mperMM * 5.0f;
		int nRow = (int) (nHeight / fgridwidth);
		int nCol = (int) (nWidth / fgridwidth);
		mScreenHeight = (int) (nRow * fgridwidth);
		mScreenWidth = (int) (nCol * fgridwidth);
		mScreenWidthInCx = (int) (mScreenWidth / mcx);
		mTopOffset = (int) ((nHeight - mScreenHeight) / 2.0f);
		mLeftOffset = (int) ((nWidth - mScreenWidth) / 2.0f);
		int bottom = mScreenHeight + mTopOffset;
		int right = mScreenWidth + mLeftOffset;
		int tmp = 0, i = 0;
		i = 0;
		while (true) {
			tmp = (int) (i * mperMM) + mTopOffset;
			if (tmp > bottom) {
				break;
			}
			cv.drawLine(mLeftOffset, tmp, right, tmp, p);
			i++;
		}
		i = 0;
		while (true) {
			tmp = (int) (i * mperMM) + mLeftOffset;
			if (tmp > right) {
				break;
			}
			cv.drawLine(tmp, mTopOffset, tmp, bottom, p);
			i++;
		}
		p.setColor(Color.argb(80, 255, 100, 0));
		i = 0;
		while (true) {
			tmp = (int) (i * fgridwidth) + mTopOffset;
			if (tmp > bottom) {
				break;
			}
			cv.drawLine(mLeftOffset, tmp, right, tmp, p);
			i++;
		}
		i = 0;
		while (true) {
			tmp = (int) (i * fgridwidth) + mLeftOffset;
			if (tmp > right) {
				break;
			}
			cv.drawLine(tmp, mTopOffset, tmp, bottom, p);
			i++;
		}

		Rect showRect = new Rect(0, 0, nWidth, nHeight);
		Canvas canvas = mSurfaceHolder.lockCanvas(showRect);
		if (null != canvas) {
			// p.setColor(Color.argb(150, 200, 0, 0));
			p.setColor(Color.WHITE);
			canvas.drawBitmap(mGrid, showRect, showRect, p);
		}
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}

	public void paint() {
		// Log.e(TAG, "paint 1");
		if (this.mSurfaceHolder == null || mDataSegment == null
				|| mBuffer == null) {
			return;
		}
		DataDescription desc = mDataSegment.mDesc;
		float nPerHeight = mScreenHeight / desc.nLeadCount;

		int remain = Math.min(mScreenWidthInCx - nLastPos, nEndPos - nStartPos);
		if (remain <= 0)
			return;
		// TODO:
		Rect dst = new Rect((int) (nLastPos * mcx + mLeftOffset), 0,
				(int) ((nLastPos + remain + 50) * mcx) + mLeftOffset, nHeight);
		Canvas canvas = this.mSurfaceHolder.lockCanvas(dst);
		if (canvas == null)
			return;
		canvas.drawBitmap(mGrid, dst, dst, mPaint);
		// 画心电图
		int tX = 0, tY = 0, bX = 0, bY = 0;
		int curY = 0, i = 0, j = 0;
		for (i = DataDescription.LeadIndex_I; i <= DataDescription.LeadIndex_V6; i++) {
			curY = (int) (nPerHeight * i + nPerHeight / 2.0f + mTopOffset);
			for (j = nStartPos; (j - nStartPos) < remain && j < nEndPos - 1; j++) {
				tX = ((int) ((nLastPos + j - nStartPos) * mcx) + mLeftOffset);
				bX = ((int) ((nLastPos + j - nStartPos + 1) * mcx) + mLeftOffset);
				tY = (int) (curY - mBuffer[i][j % mScreenWidthInCx] * mcy)
						+ mTopOffset;
				bY = (int) (curY - mBuffer[i][(j + 1) % mScreenWidthInCx] * mcy)
						+ mTopOffset;
				canvas.drawLine(tX, tY, bX, bY, mPaint);
			}
		}
		nLastPos += (j - nStartPos);
		if (nLastPos >= mScreenWidthInCx) {
			nLastPos = 0;
		}
		nStartPos = j;
		this.mSurfaceHolder.unlockCanvasAndPost(canvas);
	}
}

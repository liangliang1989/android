package com.health.myhealth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.database.Tables;
import com.health.util.Constants;
import com.health.util.MyProgressDialog;
import com.health.util.TimeHelper;
import com.health.web.WebService;

public class CurveRecord extends BaseActivity {
	private static final int OK = 0;
	private static final int NET_ERROR = 1;
	private static final int NO_DATA = 2;
	private static final Integer PAGE_COUNT = 10;
	private static final int GRAPH_WIDTH = 800;// ����ͼ���
	private static final int DAYS = 7;// һ����ʾʱ�䳤��
	private static final String TAG = "CurveRecord";
	private double screenNum = 1.0;// ����ռ�ݵ���Ļ����,�������ٸ������ܿ���������ʾ������
	private Context context;
	private Button bpButton;// Ѫѹ
	private Button pulseButton;// ����
	private Button boButton;// Ѫ��
	private Button tempButton;// ����
	private Button gluButton;// Ѫ��
	private Button uaButton;// ����
	private Button cholButton;// �ܵ��̴�
	private Button urineButton;// �򳣹�
	private Button tableShowButton;// ���
	private Button curveShowButton;// ����
	private Button homeButton;// ��������
	private Button backButton;// ����
	private LinearLayout curveLayout;// ����ͼ

	private Button lastButton = null;// �ϴε���㰴ť
	private int pageIndex = 0;// Ĭ��Ϊ��һҳ
	private boolean hasMore = false;// ���и���������ʾ
	private String cPath = WebService.PATH_QUERY_BP;// ��ǰ��Ŀ�ĺ�׺
	private String cToken = Tables.SBP;;// ��ǰ��Ŀ
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(2);
	private GraphicalView lineView;
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	private DataDenerator[] generators = new DataDenerator[8];// ������ȡ���ݵĹ���
	private MyProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.curve_record);
		parseBundle();
		context = this;
		initView();
		setButtonListener();
		// ��ǰѡ������չʾ
		tableShowButton.setSelected(false);
		curveShowButton.setSelected(true);
		setSelected(cToken);
		mDialog = new MyProgressDialog(context);
		curveLayout.addView(lineView());
		DataDenerator generator = getGenerator(cToken);
		if (null != generator)
			exec.execute(generator);
		setLineViewListener();
	}

	/***
	 * ���ó�ʼʱ��ѡ��button
	 * 
	 * @param token
	 */
	private void setSelected(String token) {
		Button cButton;
		if (Tables.SBP.equals(token))
			cButton = bpButton;
		else if (Tables.PULSE.equals(token))
			cButton = pulseButton;
		else if (Tables.SBP.equals(token))
			cButton = bpButton;
		else if (Tables.BO.equals(token))
			cButton = boButton;
		else if (Tables.TEMP.equals(token))
			cButton = tempButton;
		else if (Tables.GLU.equals(token))
			cButton = gluButton;
		else if (Tables.UA.equals(token))
			cButton = uaButton;
		else if (Tables.CHOL.equals(token))
			cButton = cholButton;
		else
			cButton = urineButton;
		setSelected(cButton);
	}

	private DataDenerator getGenerator(String token) {
		if (token.equals(Tables.SBP)) {
			if (generators[0] == null)
				generators[0] = new DataDenerator("Ѫѹ", "Ѫѹ����ͼ", "Ѫѹֵ��mmHg)",
						40, 200);
			return generators[0];
		} else if (token.equals(Tables.PULSE)) {
			if (generators[1] == null)
				generators[1] = new DataDenerator("����", "��������ͼ", "���ʣ���/���ӣ�",
						30, 140);
			return generators[1];
		} else if (token.equals(Tables.BO)) {
			if (generators[2] == null)
				generators[2] = new DataDenerator("Ѫ��", "Ѫ������ͼ", "Ѫ��������%��", 40,
						120);
			return generators[2];
		} else if (token.equals(Tables.TEMP)) {
			if (generators[3] == null)
				generators[3] = new DataDenerator("����", "��������ͼ", "���£����϶ȣ�", 28,
						43);
			return generators[3];
		} else if (token.equals(Tables.GLU)) {
			if (generators[4] == null)
				generators[4] = new DataDenerator("Ѫ��", "Ѫ������ͼ", "Ѫ�ǣ�mg/dL��",
						0, 50);
			return generators[4];
		} else if (token.equals(Tables.UA)) {
			if (generators[5] == null)
				generators[5] = new DataDenerator("����", "��������ͼ", "���ᣨmg/dL��",
						0, 2);
			return generators[5];
		} else if (token.equals(Tables.CHOL)) {
			if (generators[6] == null)
				generators[6] = new DataDenerator("�ܵ��̴�", "�ܵ��̴�����ͼ",
						"�ܵ��̴���mg/dL��", 0, 15);
			return generators[6];
		}
		return generators[0];

	}

	/***
	 * ����bundle������
	 * 
	 * @param savedInstanceState
	 */
	private void parseBundle() {
		Bundle state = getIntent().getExtras();
		if (state != null) {
			String path = state.getString(TableRecord.SELECTED_PATH);
			if (null != path)
				cPath = path;
			String token = state.getString(TableRecord.SELECTED_TOKEN);
			if (null != token)
				cToken = token;
		}
	}

	/***
	 * ���ü�����
	 */
	private void setButtonListener() {
		OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == curveShowButton) {

				} else if (v == tableShowButton) {
					jump2table();
				} else if (v == homeButton) {
					CurveRecord.this.setResult(RESULT_OK);
					CurveRecord.this.finish();
				} else if (v == backButton) {
					CurveRecord.this.finish();
				} else {
					String path = WebService.PATH_QUERY_BP;
					String token = Tables.SBP;
					if (v == bpButton) {
						path = WebService.PATH_QUERY_BP;
						token = Tables.SBP;// ��������ѹ������ѹ
					} else if (v == pulseButton) {
						path = WebService.PATH_QUERY_PULSE;
						token = Tables.PULSE;
					} else if (v == boButton) {
						path = WebService.PATH_QUERY_BO;
						token = Tables.BO;
					} else if (v == tempButton) {
						path = WebService.PATH_QUERY_TEMP;
						token = Tables.TEMP;
					} else if (v == gluButton) {
						path = WebService.PATH_QUERY_GLU;
						token = Tables.GLU;
					} else if (v == uaButton) {
						path = WebService.PATH_QUERY_UA;
						token = Tables.UA;
					} else if (v == cholButton) {
						path = WebService.PATH_QUERY_CHOL;
						token = Tables.CHOL;
					} else if (v == urineButton) {
						path = WebService.PATH_QUERY_URINE;
						token = Tables.LEU;// �����򳣹�11��
						Intent intent = new Intent(CurveRecord.this,
								UrineChart.class);
						startActivity(intent);
						CurveRecord.this.finish();
					}
					// �����µİ��o
					if (!cToken.equals(token)) {
						cToken = token;
						cPath = path;
						pageIndex = 0;
						moved = 0;// ������������
						setSelected((Button) v);
						// ����İ�ť��ͬ,��֮ǰ���������
						clearDataset();
						setXAxis(mRenderer);
						DataDenerator generator = getGenerator(cToken);
						if (null != generator)
							exec.execute(generator);
					}

				}
			}

			/***
			 * ��ת�������ʾ
			 */
			private void jump2table() {
				Intent intent = new Intent(CurveRecord.this, TableRecord.class);
				Bundle bundle = new Bundle();
				bundle.putString(TableRecord.SELECTED_TOKEN, cToken);
				bundle.putString(TableRecord.SELECTED_PATH, cPath);
				intent.putExtras(bundle);
				startActivity(intent);
				CurveRecord.this.finish();
			}
		};
		bpButton.setOnClickListener(listener);
		pulseButton.setOnClickListener(listener);
		boButton.setOnClickListener(listener);
		tempButton.setOnClickListener(listener);
		gluButton.setOnClickListener(listener);
		uaButton.setOnClickListener(listener);
		cholButton.setOnClickListener(listener);
		urineButton.setOnClickListener(listener);
		tableShowButton.setOnClickListener(listener);
		curveShowButton.setOnClickListener(listener);
		homeButton.setOnClickListener(listener);
		backButton.setOnClickListener(listener);
	}

	private class DataDenerator implements Runnable {

		private String seriesTitle;
		private String charTitle;
		private String yTitle;
		private int yMin;
		private int yMax;

		public DataDenerator(String seriesTitle, String charTitle,
				String yTitle, int yMin, int yMax) {
			this.seriesTitle = seriesTitle;
			this.charTitle = charTitle;
			this.yTitle = yTitle;
			this.yMin = yMin;
			this.yMax = yMax;
		}

		@Override
		public void run() {
			try {
				showCurveLine(seriesTitle, charTitle, yTitle, yMin, yMax);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/***
	 * �����ݼ����
	 */
	private void clearDataset() {
		for (XYSeries series : mDataset.getSeries()) {
			series.clear();
		}
	}

	private double moved = 0;

	private void setLineViewListener() {
		lineView.setOnTouchListener(new OnTouchListener() {
			long begin = 0;
			long end = 0;
			String TAG = "setOnTouchListener";
			Toast toast = new Toast(getApplicationContext());

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				SeriesSelection seriesSelection = lineView
						.getCurrentSeriesAndPoint();
				if (seriesSelection != null) {
					Log.i(TAG, seriesSelection.getXValue() + "seriesSelection");
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.showmsg,
							(ViewGroup) findViewById(R.id.showmsg));
					TextView valueTV = (TextView) view
							.findViewById(R.id.measure_value);
					TextView timeTV = (TextView) view
							.findViewById(R.id.measure_time);
					String time = TimeHelper
							.getStringTime((long) seriesSelection.getXValue());
					String value = String.valueOf((float) seriesSelection
							.getValue());
					valueTV.setText("����ֵ:" + value);
					timeTV.setText("����ʱ��:" + time);
					toast.setView(view);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP | Gravity.LEFT,
							(int) (event.getX()), (int) (event.getY()));
					toast.show();

				}

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					begin = (int) event.getX();
					break;
				case MotionEvent.ACTION_MOVE:
					end = (int) event.getX();
					break;
				case MotionEvent.ACTION_UP:
					moved += end - begin;
					Log.i(TAG, " moved: " + moved / GRAPH_WIDTH + " screenNum:"
							+ screenNum);
					// �ƶ���λ�ó�������ʾ�ķ�Χ
					// û��Ҫ��ʾ������,����Ҫ��̬����
					if (hasMore && moved / GRAPH_WIDTH >= screenNum - 1) {
						pageIndex++;
						DataDenerator generator = getGenerator(cToken);
						if (null != generator)
							exec.execute(generator);
					}
				}
				return false;
			}
		});
	}

	/***
	 * ��ʾ����ͼ
	 * 
	 * @param seriesTitle
	 * @param charTitle
	 * @param yTitle
	 * @param yMin
	 * @param yMax
	 * @throws IOException
	 * @throws JSONException
	 */
	public void showCurveLine(String seriesTitle, String charTitle,
			String yTitle, int yMin, int yMax) throws Exception {
		JSONObject paras = getQueueParas(pageIndex);
		TreeMap<Double, double[]> data = DataService.getSingleDataForCurve(
				paras, cPath, cToken);
		XYSeries[] xySeries = mDataset.getSeries();
		mRenderer.setChartTitle(charTitle);
		mRenderer.setYTitle(yTitle);
		mRenderer.setYAxisMin(yMin);// ����y����Сֵ
		mRenderer.setYAxisMax(yMax);
		setHasMore(data.size());
		if (cToken.equals(Tables.SBP))
			addBpData(xySeries, data);
		else
			addSinlgeData(xySeries, seriesTitle, data);
		if (data.size() > 0)
			setXAxis(mRenderer, data.lastKey());
		if (xySeries[0].getItemCount() == 0)// û������ʱ�����һ�������ݣ���֤������ʾ
			xySeries[0].add((double) System.currentTimeMillis(), -100);
		else
			updateCurveLength(xySeries[0]);
		lineView.repaint();// �ػ�
	}

	private void setXAxis(XYMultipleSeriesRenderer mRenderer, Double lastKey) {
		mRenderer.setXAxisMin(lastKey - TimeHelper.MILLSEC_DAY * (DAYS - 1));
		mRenderer.setXAxisMax(lastKey + DAYS);

	}

	/***
	 * ��ӵ�������
	 * 
	 * @param xySeries
	 * @param seriesTitle
	 * @param data
	 */
	private void addSinlgeData(XYSeries[] xySeries, String seriesTitle,
			TreeMap<Double, double[]> data) {
		xySeries[0].setTitle(seriesTitle);
		xySeries[1].setTitle("");
		double[][] lastData = getPoints(xySeries[0]);

		clearDataset();

		for (int i = 0; i < lastData[0].length; i++) {
			data.put(lastData[0][i], new double[] { lastData[1][i] });
		}
		for (Map.Entry<Double, double[]> entry : data.entrySet()) {
			double key = entry.getKey();
			double[] value = entry.getValue();
			xySeries[0].add(key, value[0]);
		}
	}

	/***
	 * ���Ѫѹ����
	 * 
	 * @param xySeries
	 * @param data
	 */
	private void addBpData(XYSeries[] xySeries, TreeMap<Double, double[]> data) {
		xySeries[0].setTitle("��ѹ");
		xySeries[1].setTitle("��ѹ");
		// ���ݴ�֮ǰ���ص�����
		double[][] lastHigh = getPoints(xySeries[0]);
		double[][] lastLow = getPoints(xySeries[1]);
		clearDataset();
		for (int i = 0; i < lastHigh[0].length; i++) {
			data.put(lastHigh[0][i], new double[] { lastHigh[1][i],
					lastLow[1][i] });
		}
		for (Map.Entry<Double, double[]> entry : data.entrySet()) {
			double key = entry.getKey();
			double[] value = entry.getValue();
			xySeries[0].add(key, value[0]);
			xySeries[1].add(key, value[1]);
		}
	}

	/***
	 * ��ȡ��ʾ�ĵ�����
	 * 
	 * @param xySeries
	 * @return
	 */
	private double[][] getPoints(XYSeries xySeries) {
		int num = xySeries.getItemCount();
		double[][] data = new double[2][num];
		for (int i = 0; i < num; i++) {
			data[0][i] = xySeries.getX(i);
			data[1][i] = xySeries.getY(i);
		}
		return data;
	}

	/***
	 * �����Ƿ�����һҳ
	 * 
	 * @param dataNum
	 */
	private void setHasMore(int dataNum) {
		// ������һҳ����
		if (dataNum == PAGE_COUNT)
			hasMore = true;
		else
			// �յ������ݸ���������ҳ���С��˵���Ѿ�û�пɼ��ص�����
			hasMore = false;
	}

	/***
	 * �������߳��ȣ�����Ϊ��һ���㵽���һ�������ռ������
	 * 
	 * @param start
	 * @param end
	 */
	private void updateCurveLength(XYSeries xySeries) {
		int count = xySeries.getItemCount();
		if (count == 0)
			return;
		double start = xySeries.getX(0);
		double end = xySeries.getX(count - 1);
		String t1 = TimeHelper.getStringTime((long) start);
		String t2 = TimeHelper.getStringTime((long) end);
		Log.i(TAG, "count:" + count + "start:" + t1 + " end:" + t2);
		screenNum = ((end - start) / (TimeHelper.MILLSEC_DAY * DAYS));
	}

	/***
	 * ��ȡ��ѯ���ݵĲ������������еĲ�ѯ����������ͬ�ģ�ֻ�в�ѯ·����ͬ
	 * 
	 * @param pageIndex
	 * @return
	 * @throws JSONException
	 */
	private JSONObject getQueueParas(Integer pageIndex) throws JSONException {
		JSONObject para = new JSONObject();
		para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
		para.put(WebService.CARDNO, BaseActivity.getUser().getCardNo());
		JSONObject data = new JSONObject();
		String startTime = TimeHelper.getBeforeTime(365);
		String endTime = TimeHelper.getCurrentTime();
		data.put(WebService.ENDTIME, endTime);
		data.put(WebService.STARTTIME, startTime);
		data.put(WebService.PAGECOUNT, PAGE_COUNT.toString());
		data.put(WebService.PAGEINDEX, pageIndex.toString());
		para.put(WebService.DATA, data);
		return para;
	}

	/**
	 * ��Ѫ������ͼ
	 * 
	 * @return
	 */
	public View lineView() {
		XYSeriesRenderer xRenderer = new XYSeriesRenderer();// (������һ���߶���)
		XYSeriesRenderer yRenderer = new XYSeriesRenderer();// (������һ���߶���)
		TimeSeries ySeries = new TimeSeries("");
		TimeSeries xSeries = new TimeSeries("");
		mDataset.addSeries(xSeries);
		mDataset.addSeries(ySeries);
		// ����ͼ���X��ĵ�ǰ����
		mRenderer.setOrientation(Orientation.HORIZONTAL);
		mRenderer.setAxisTitleTextSize(15);// ����������ı���С
		mRenderer.setChartTitleTextSize(15);// ����ͼ��������ֵĴ�С
		// mRenderer.setLabelsTextSize(18);// ���ñ�ǩ�����ִ�С
		mRenderer.setYLabels(10);// y����ʾ�ĵ���
		mRenderer.setXLabels(10);// x����ʾ�ĵ���
		mRenderer.setYLabelsColor(1, Color.BLACK);
		mRenderer.setLegendTextSize(20);// ����ͼ���ı���С
		mRenderer.setPointSize(5f);// ���õ�Ĵ�С
		mRenderer.setShowGrid(true);// ��ʾ����
		mRenderer.setShowLabels(true);
		mRenderer.setShowLegend(true);
		mRenderer.setMargins(new int[] { 1, 15, 1, 1 });// ������ͼλ��
		mRenderer.setMarginsColor(Color.WHITE);// ��������Ϊ͸��
		mRenderer.setPanEnabled(true, false);// ���ò��������ƶ�����
		mRenderer.setSelectableBuffer(10);
		setXAxis(mRenderer);
		xRenderer.setColor(Color.BLUE);// ������ɫ
		xRenderer.setPointStyle(PointStyle.CIRCLE);// ���õ����ʽ
		xRenderer.setFillPoints(true);// ���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
		xRenderer.setLineWidth(2);// �����߿�
		mRenderer.addSeriesRenderer(xRenderer);
		yRenderer.setColor(Color.GREEN);// ������ɫ
		yRenderer.setPointStyle(PointStyle.CIRCLE);// ���õ����ʽ
		yRenderer.setFillPoints(true);// ���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
		yRenderer.setLineWidth(2);// �����߿�
		mRenderer.addSeriesRenderer(yRenderer);
		lineView = ChartFactory.getTimeChartView(this, mDataset, mRenderer,
				"yy/MM/dd HH:mm");
		lineView.setBackgroundColor(Color.WHITE);
		return lineView;
	}

	/***
	 * ���õ�ǰ��ʾ��ΧΪ���10��
	 * 
	 * @param mRenderer
	 */
	private void setXAxis(XYMultipleSeriesRenderer mRenderer) {
		long current = TimeHelper.getCurrentDate().getTime();
		mRenderer.setXAxisMin(current - TimeHelper.MILLSEC_DAY * DAYS);
		mRenderer.setXAxisMax(current);
	}

	/**
	 * ����ѡ�а�ť,ȡ���ϴΰ�ť��ѡ��
	 * 
	 * @param button
	 */
	private void setSelected(Button button) {
		if (lastButton != null)
			lastButton.setSelected(false);
		lastButton = button;
		lastButton.setSelected(true);
	}

	/***
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		bpButton = (Button) this.findViewById(R.id.show_bp);
		pulseButton = (Button) this.findViewById(R.id.show_pulse);
		boButton = (Button) this.findViewById(R.id.show_bo);
		tempButton = (Button) this.findViewById(R.id.show_temp);
		gluButton = (Button) this.findViewById(R.id.show_glu);
		uaButton = (Button) this.findViewById(R.id.show_ua);
		cholButton = (Button) this.findViewById(R.id.show_chol);
		urineButton = (Button) this.findViewById(R.id.show_urine);
		tableShowButton = (Button) this.findViewById(R.id.table_show);
		curveShowButton = (Button) this.findViewById(R.id.curve_show);
		homeButton = (Button) this.findViewById(R.id.to_home_button);
		backButton = (Button) this.findViewById(R.id.return_button);
		curveLayout = (LinearLayout) this.findViewById(R.id.show_curve_layout);
	}
}

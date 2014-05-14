package com.health.myhealth;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import cn.younext.R;

import com.health.database.Tables;
import com.health.util.TimeHelper;
import com.health.web.WebService;

public class DataService {

	private static final TreeMap<Double, double[]> EMPTY = new TreeMap<Double, double[]>() {

		private static final long serialVersionUID = 4717608862679765422L;

		{

			put((double) System.currentTimeMillis(),
					new double[] { -100, -100 });
		}
	};

	/**
	 * ��ȡѪѹ����������ʾ����
	 * 
	 * @param timeIndex
	 * @param cardNo
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	// public static double[][]
	// getBpDataForCurve(Map<String, String> paras)
	// throws IOException {
	// try {
	//
	// JSONObject result =
	// WebService.query(WebService.PATH_QUERY_BP,
	// paras);
	// int statusCode =
	// result.getInt(WebService.STATUS_CODE);
	// if (statusCode == WebService.OK) {
	// JSONArray datas =
	// result.getJSONArray(WebService.DATA);
	// double[][] bpData = new
	// double[3][datas.length()];
	// for (int i = 0; i < datas.length(); i++) {
	// JSONObject data = datas.getJSONObject(i);
	// String checkTime =
	// data.getString(WebService.CHECKTIME);
	// long time;
	// try {
	// time = TimeHelper.parseTime(checkTime);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// continue;// ���ʱ���������������������
	// }
	// bpData[0][i] = time;
	// bpData[1][i] = data.getInt(Tables.SBP);
	// bpData[2][i] = data.getInt(Tables.DBP);
	// }
	// // ����
	// bpData = sort(bpData);
	// return bpData;
	// } else
	// return EMPTY;
	// } catch (JSONException e) {
	// e.printStackTrace();
	// return EMPTY;
	// }
	//
	// }

	/***
	 * �����ݰ�ʱ������,����������ݼ��ػ����
	 * 
	 * @param bpData
	 * @return
	 */
	public static double[][] sort(double[][] data) {
		int len = data[0].length;
		for (int i = 0; i < len; i++) {
			for (int j = i; j < len; j++) {
				if (data[0][i] > data[0][j]) {
					swap(data, i, j);
				}
			}
		}

		return data;
	}

	private static void swap(double[][] data, int i, int j) {
		for (int k = 0; k < data.length; k++) {
			double temp = data[k][i];
			data[k][i] = data[k][j];
			data[k][j] = temp;
		}
	}

	/***
	 * ��ȡֻ��һ�����͵�����,��Ѫѹ���ʺ���Һ֮��,���������������� ������ʾ����
	 * 
	 * @param paras
	 * @param path
	 * @param tokenҪ��ȡ�����ݵı�־
	 * @return
	 * @throws IOException
	 */
	public static TreeMap<Double, double[]> getSingleDataForCurve(
			JSONObject paras, String path, String token) throws Exception {
		try {
			JSONObject result = WebService.query(path, paras);
			int statusCode = result.getInt(WebService.STATUS_CODE);
			if (statusCode == WebService.OK) {
				JSONArray datas = result.getJSONArray(WebService.DATA);
				TreeMap<Double, double[]> sortMap = new TreeMap<Double, double[]>();

				for (int i = 0; i < datas.length(); i++) {
					JSONObject data = datas.getJSONObject(i);
					String checkTime = data.getString(WebService.CHECKTIME);
					long time;
					try {
						time = TimeHelper.parseTime(checkTime);
					} catch (ParseException e) {
						e.printStackTrace();
						continue;// ���ʱ���������������������
					}
					double[] values;
					if (token.equals(Tables.SBP))
						values = new double[2];
					else
						values = new double[1];
					values[0] = data.getDouble(token);
					if (token.equals(Tables.SBP))
						values[1] = data.getDouble(Tables.DBP);
					sortMap.put((double) time, values);
				}
				return sortMap;
			} else
				return EMPTY;
		} catch (JSONException e) {
			e.printStackTrace();
			return EMPTY;
		}
	}

	public static List<String[]> getDataForTable(Context context,
			JSONObject paras, String path, String token) throws Exception {
		JSONObject result = WebService.query(path, paras);
		int statusCode = result.getInt(WebService.STATUS_CODE);
		List<String[]> records = new ArrayList<String[]>();
		if (statusCode == WebService.OK) {
			JSONArray datas = result.getJSONArray(WebService.DATA);
			for (int i = 0; i < datas.length(); i++) {
				JSONObject data = datas.getJSONObject(i);
				String[] record = new String[3];
				String checkTime = data.getString(WebService.CHECKTIME);
				record[0] = checkTime;
				if (token.equals(Tables.SBP)) {
					record[1] = "����ѹ:" + data.getString(Tables.SBP) + " / ����ѹ:"
							+ data.getString(Tables.DBP);
					record[2] = "/";// ���������ʱΪ��
				} else if (token.equals(Tables.LEU)) {
					String urine = getUrineData(context, data);
					if (null == urine)
						continue;
					else
						record[1] = urine;
					record[2] = "/";// ���������ʱΪ��
				} else if (token.equals(Tables.ECG_KRL)) {
					record[1] = "/";
					record[2] = data.getString(Tables.ECG_PDF);
				} else {
					record[1] = data.getString(token);
					record[2] = "/";// ���������ʱΪ��
				}

				records.add(record);
			}
		}
		return records;
	}

	/***
	 * �������򳣹�11��
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	private static String getUrineData(Context context, JSONObject data) {
		String[] itemName = { context.getString(R.string.leu),
				context.getString(R.string.bld),
				context.getString(R.string.ph),
				context.getString(R.string.pro),
				context.getString(R.string.ubg),
				context.getString(R.string.nit),
				context.getString(R.string.sg),
				context.getString(R.string.ket),
				context.getString(R.string.bil),
				context.getString(R.string.glu), context.getString(R.string.vc) };
		String[] itemTag = { Tables.LEU, Tables.BLD, Tables.PH, Tables.PRO,
				Tables.UBG, Tables.NIT, Tables.SG, Tables.KET, Tables.BIL,
				Tables.UGLU, Tables.VC };
		try {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < itemName.length; i++) {
				builder.append(itemName[i]);
				builder.append(":");
				builder.append(data.getString(itemTag[i]));
				if (i == 5)
					builder.append("\n");
				else
					builder.append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
			return builder.toString();
		} catch (Exception e) {
			return null;
		}

	}

	public static Map<Long, String> getUrineRecordsForChart(Context context,
			JSONObject parameters, String path, String token) {
		try {
			Map<Long, String> urineRecordsMap = new TreeMap<Long, String>();
			JSONObject result = WebService.query(path, parameters);
			int statusCode = result.getInt(WebService.STATUS_CODE);
			if (statusCode == WebService.OK) {
				JSONArray datas = result.getJSONArray(WebService.DATA);
				for (int i = 0; i < datas.length(); i++) {
					JSONObject data = datas.getJSONObject(i);
					String checkTime = data.getString(WebService.CHECKTIME);
					long time;
					try {
						time = TimeHelper.parseTime(checkTime);
					} catch (ParseException e) {
						e.printStackTrace();
						continue;// ���ʱ���������������������
					}
					String record = getUrineRecord(data);
					if (!urineRecordsMap.containsKey(time)) {
						urineRecordsMap.put(time, record);
					}
				}
				return urineRecordsMap;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getUrineRecord(JSONObject data) {
		String[] itemTag = { Tables.LEU, Tables.BLD, Tables.PH, Tables.PRO,
				Tables.UBG, Tables.NIT, Tables.SG, Tables.KET, Tables.BIL,
				Tables.UGLU, Tables.VC };
		try {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < itemTag.length; i++) {
				builder.append(itemTag[i]);
				builder.append(":");
				builder.append(data.getString(itemTag[i]).trim());
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
			return builder.toString();
		} catch (Exception e) {
			return null;
		}
	}

}

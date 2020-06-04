package com.haotang.pet.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.ComplaintOrderHistoryActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.RecentOrdersAdapter;
import com.haotang.pet.entity.OrdersBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * Title:FeedbackFragment
 * </p>
 * <p>
 * Description:投诉订单界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午3:54:30
 */
@SuppressLint("NewApi")
public class ComplaintOrderFragment extends Fragment implements OnClickListener {
	private RelativeLayout rl_complaintorder_lsts;
	private TextView tv_complaintorder_qbdd;
	private ListView lv_complaintorder_tsdd;
	private Button btn_complaintorder_frag_lxkf;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	private Calendar calendar;
	private List<OrdersBean> orders = new ArrayList<OrdersBean>();
	private ImageView iv_no_recentorder_default;

	public ComplaintOrderFragment() {
		super();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initData();
		View view = initView(inflater, container);
		setLinster();
		setView();
		getData();
		return view;
	}

	private void getData() {
		pDialog.showDialog();
		CommUtil.choose(getContext(), chooseHandler);
	}

	private AsyncHttpResponseHandler chooseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			processData(new String(responseBody));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	// 解析json数据
	private void processData(String result) {
		pDialog.closeDialog();
		try {
			JSONObject jobj = new JSONObject(result);
			if (jobj != null) {
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						if (jdata.has("orders") && !jdata.isNull("orders")) {
							JSONArray jarrorders = jdata.getJSONArray("orders");
							if (jarrorders.length() > 0) {
								lv_complaintorder_tsdd
										.setVisibility(View.VISIBLE);
								iv_no_recentorder_default
										.setVisibility(View.GONE);
								orders.clear();
								for (int i = 0; i < jarrorders.length(); i++) {
									orders.add(OrdersBean
											.json2Entity(jarrorders
													.getJSONObject(i)));
								}
								lv_complaintorder_tsdd
										.setAdapter(new RecentOrdersAdapter<OrdersBean>(
												getActivity(), orders));
							} else {
								lv_complaintorder_tsdd
										.setVisibility(View.GONE);
								iv_no_recentorder_default
										.setVisibility(View.VISIBLE);
							}
						} else {
							lv_complaintorder_tsdd.setVisibility(View.GONE);
							iv_no_recentorder_default
									.setVisibility(View.VISIBLE);
						}
					} else {
						lv_complaintorder_tsdd.setVisibility(View.GONE);
						iv_no_recentorder_default.setVisibility(View.VISIBLE);
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						ToastUtil.showToastShortCenter(getContext(),
								jobj.getString("msg"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setView() {
		SpannableString ss = new SpannableString("全部订单");
		// 用下划线标记文本
		ss.setSpan(new UnderlineSpan(), 0, ss.length(),
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		tv_complaintorder_qbdd.setText(ss);
	}

	private void setLinster() {
		rl_complaintorder_lsts.setOnClickListener(this);
		tv_complaintorder_qbdd.setOnClickListener(this);
		btn_complaintorder_frag_lxkf.setOnClickListener(this);
		lv_complaintorder_tsdd
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					}
				});
	}

	private void initData() {
		pDialog = new MProgressDialog(getContext());
		spUtil = SharedPreferenceUtil.getInstance(getContext());
		calendar = Calendar.getInstance();
	}

	private View initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.complaintorder_frag, container,
				false);
		rl_complaintorder_lsts = (RelativeLayout) view
				.findViewById(R.id.rl_complaintorder_lsts);
		tv_complaintorder_qbdd = (TextView) view
				.findViewById(R.id.tv_complaintorder_qbdd);
		lv_complaintorder_tsdd = (ListView) view
				.findViewById(R.id.lv_complaintorder_tsdd);
		btn_complaintorder_frag_lxkf = (Button) view
				.findViewById(R.id.btn_complaintorder_frag_lxkf);
		iv_no_recentorder_default = (ImageView) view
				.findViewById(R.id.iv_no_recentorder_default);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_complaintorder_lsts:
			startActivity(new Intent(getContext(),
					ComplaintOrderHistoryActivity.class));
			break;
		case R.id.tv_complaintorder_qbdd:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.mainactivity");
			//到洗美订单列表页
			intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT);
			getActivity().sendBroadcast(intent);
			getActivity().finish();
			break;
		case R.id.btn_complaintorder_frag_lxkf:
			calendar.setTimeInMillis(System.currentTimeMillis());
			int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
			if (currentHour >= 9 && currentHour <= 22) {
				showPhoneDialog(spUtil.getString("customerPhone", ""));
			} else {
				ToastUtil.showToastShortCenter(getContext(), "人工客服的工作时间为9：00-22:00");
			}
			break;
		default:
			break;
		}
	}

	private void showPhoneDialog(final String phone) {
		MDialog mDialog = new MDialog.Builder(getContext())
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage(phone)
				.setCancelStr("取消").setOKStr("呼叫")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Global.cellPhone(getContext(), phone);
					}
				}).build();
		mDialog.show();
	}
}

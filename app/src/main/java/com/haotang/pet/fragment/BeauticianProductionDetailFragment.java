package com.haotang.pet.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.BaseFragment;
import com.haotang.pet.BeauticianProductuonActivity;
import com.haotang.pet.R;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class BeauticianProductionDetailFragment extends BaseFragment implements View.OnClickListener{
	private Activity mActivity;
	private TextView tvDes;
	private ImageView ivImage;
	private String mPath;
	private String mDes;
	private String mSmallImage;
	private MProgressDialog pDialog;
	private LinearLayout layout_dianzan;
	private ImageView img_dianzan;
	private TextView textview_dianzannums;
	private int userPraise;
	private int praiseCount;
	private int id;
	private int position;

	public void setData(String path,String des,String smallimage,int userPraise,int praiseCount,int id,int position){
		this.mPath = path;
		this.mDes = des;
		this.mSmallImage = smallimage;
		this.userPraise = userPraise;
		this.praiseCount = praiseCount;
		this.id = id;
		this.position = position;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mActivity = activity;
		pDialog = new MProgressDialog(mActivity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		findView(view);
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.beauticianproductiondetailfragment, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setViw();
	}
	
	private void findView(View view) {
		tvDes = (TextView) view.findViewById(R.id.tv_beauticianproduction_detail);
		ivImage = (ImageView) view.findViewById(R.id.iv_beauticianproduction_detail);
		layout_dianzan = (LinearLayout) view.findViewById(R.id.layout_dianzan);
		img_dianzan = (ImageView) view.findViewById(R.id.img_dianzan);
		textview_dianzannums = (TextView) view.findViewById(R.id.textview_dianzannums);

		layout_dianzan.setOnClickListener(this);
		img_dianzan.setOnClickListener(this);
	}

	private void setViw() {
		if(mDes!=null&&!"".equals(mDes.trim())){
			tvDes.setVisibility(View.VISIBLE);
			tvDes.setText(mDes);
		}else{
			tvDes.setVisibility(View.GONE);
			tvDes.setText("");
		}
		if(mPath!=null&&!"".equals(mPath.trim())){
			pDialog.showDialog();
			GlideUtil.loadImg(mActivity,mPath, ivImage,R.drawable.icon_production_default);
			pDialog.closeDialog();
		}
		if (userPraise==1){
			img_dianzan.setBackgroundResource(R.drawable.good_checked);
		}else {
			img_dianzan.setBackgroundResource(R.drawable.good);
		}
		textview_dianzannums.setText(praiseCount+"");
	}

	public void praiseAdd(int relateId){
		CommUtil.praiseAdd(mContext,1,relateId,handler);
	}
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			try {
				mPDialog.closeDialog();
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				String msg = object.getString("msg");
				if (code==0){
//					BeauticianProductionDetailActivity.beauticianProductionDetailActivity.setListData(position);
					BeauticianProductuonActivity.beauticianProductuonActivity.setListData(position);
					img_dianzan.setBackgroundResource(R.drawable.good_checked);
					int showpraiseCount = praiseCount+=1;
					textview_dianzannums.setText(showpraiseCount+"");
					ToastUtil.showToastShortCenter(mContext,"点赞成功");
				}else {
					ToastUtil.showToastShortCenter(mContext,msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			mPDialog.closeDialog();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_dianzan:
			case R.id.layout_dianzan:
				praiseAdd(id);
				break;
		}
	}
}

package com.haotang.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.haotang.pet.R;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ScreenUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class BaseFragment extends Fragment {
	public Context mContext;
    public boolean  isVisible;
	public Activity mActivity;
	public SharedPreferenceUtil spUtil;
	public MProgressDialog mPDialog;
    @Override
    @Deprecated
    public void onAttach(Context activity) {
    	// TODO Auto-generated method stub
    	super.onAttach(activity);
    	mContext = activity;
		mActivity = getActivity();
    }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			Window window = mActivity.getWindow();
//			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//			window.setStatusBarColor(Color.TRANSPARENT);
//
//		}else {
//			mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
		super.onCreate(savedInstanceState);
		spUtil = SharedPreferenceUtil.getInstance(mActivity);
		mPDialog = new MProgressDialog(mContext);
		if (ImageLoaderUtil.instance==null){
			ImageLoaderUtil.instance = ImageLoader.getInstance();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		FragmentManager fm = getChildFragmentManager();
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (fm.getFragments() == null || index < 0
					|| index >= fm.getFragments().size()) {
				Utils.mLogError("==-->TAG  "+"Activity result fragment index out of range: 0x"
						+ Integer.toHexString(requestCode));
				return;
			}
			Fragment frag = fm.getFragments().get(index);
			if (frag == null) {
				Utils.mLogError("==-->TAG  "+"Activity result no fragment exists for index: 0x"
						+ Integer.toHexString(requestCode));
			} else {
				handleResult(frag, requestCode, resultCode, data);
			}
			return;
		}
	}
	
	/**
	 * 递归调用，对所有子Fragement生效
	 * 
	 * @param frag
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void handleResult(Fragment frag, int requestCode, int resultCode,
			Intent data) {
		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (Fragment f : frags) {
				if (f != null)
					handleResult(f, requestCode, resultCode, data);
			}
		}
	}

	protected View setEmptyViewBaseFoster(int flag, String msg, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview_foster, null);
		ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		if (flag == 1) {
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		return emptyView;
	}

	protected View setEmptyViewBase(int flag, String msg, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		if (flag == 1) {
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		return emptyView;
	}

	protected View setEmptyViewBase(int flag, String msg, int resId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
		ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		if (flag == 1) {
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		iv_emptyview_img.setImageResource(resId);
		return emptyView;
	}

	protected View setEmptyViewBase(int flag, String msg, int resId, int corlorId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
		ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		LinearLayout ll_emptyview_img = (LinearLayout) emptyView.findViewById(R.id.ll_emptyview_img);
		ll_emptyview_img.setBackgroundColor(getResources().getColor(corlorId));
		if (flag == 1) {
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		iv_emptyview_img.setImageResource(resId);
		return emptyView;
	}

	protected View setEmptyViewBase(int flag, String msg, String msgTwo, View.OnClickListener OnClickListener, @ColorInt int color, int topDp) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(getContext(), R.layout.recycler_emptyview, null);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		TextView tv_emptyview_desctwo = emptyView.findViewById(R.id.tv_emptyview_desctow);
		LinearLayout ll_emptyview = emptyView.findViewById(R.id.ll_emptyview_img);
		ImageView ivEmpty = emptyView.findViewById(R.id.iv_emptyview_img);
		ll_emptyview.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivEmpty.getLayoutParams();
		layoutParams.topMargin = ScreenUtil.dip2px(getContext(),topDp);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		if (flag == 1) {
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		Utils.setText(tv_emptyview_desctwo, msgTwo, "", View.GONE, View.VISIBLE);
		ll_emptyview.setBackgroundColor(color);
		return emptyView;
	}

	protected View setEmptyViewBase(int flag, String msg, String btnMsg, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		if (flag == 1) {
			btn_emptyview.setText(btnMsg);
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		return emptyView;
	}

	protected View setEmptyViewBase(int flag, String msg, String btnMsg, int resId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
		ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		if (flag == 1) {
			btn_emptyview.setText(btnMsg);
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		iv_emptyview_img.setImageResource(resId);
		return emptyView;
	}

	protected View setEmptyViewBase(int flag, String msg, String btnMsg, int resId, int corlorId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
		ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		LinearLayout ll_emptyview_img = (LinearLayout) emptyView.findViewById(R.id.ll_emptyview_img);
		ll_emptyview_img.setBackgroundColor(getResources().getColor(corlorId));
		if (flag == 1) {
			btn_emptyview.setText(btnMsg);
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		iv_emptyview_img.setImageResource(resId);
		return emptyView;
	}

	protected View setEmptyViewBase(int flag, String msg, int resId, float width, float height, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
		View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
		ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
		TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
		Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
		if (flag == 1) {
			btn_emptyview.setVisibility(View.VISIBLE);
			btn_emptyview.setOnClickListener(OnClickListener);
		} else if (flag == 2) {
			btn_emptyview.setVisibility(View.GONE);
		}
		Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
		iv_emptyview_img.setImageResource(resId);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_emptyview_img.getLayoutParams();
		params.height = DensityUtil.dp2px(mActivity, height);
		params.width = DensityUtil.dp2px(mActivity, width);
		return emptyView;
	}
}

package com.loveplusplus.demo.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.BaseFragment;
import com.haotang.pet.BeauticianProductuonActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Production;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class ImageDetailFragment extends BaseFragment {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    private File PetCircle;
    private int type = -1;
    private ArrayList<Production> mList;
    private TextView tvDes;
    private String mDes;
    private LinearLayout layout_dianzan;
    private ImageView img_dianzan;
    private TextView textview_dianzannums;
    private int userPraise;
    private int praiseCount;
    private int id;
    private int position;
    private LinearLayout layoutBottomClick;
    private boolean is_uri;

    public static ImageDetailFragment newInstance(String imageUrl, boolean is_uri) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putBoolean("is_uri", is_uri);
        f.setArguments(args);
        return f;
    }

    public static ImageDetailFragment newInstance(String imageUrl, int type, ArrayList<Production> mList, int position, boolean is_uri) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putInt("type", type);
        args.putBoolean("is_uri", is_uri);
        args.putInt("position", position);
        args.putSerializable("mList", mList);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        is_uri = getArguments() != null ? getArguments().getBoolean("is_uri", false) : false;
        type = getArguments() != null ? getArguments().getInt("type", -1) : -1;
        position = getArguments() != null ? getArguments().getInt("position", 0) : 0;
        mList = getArguments() != null ? (ArrayList<Production>) getArguments().getSerializable("mList") : null;
        if (type == 0) {
            id = mList.get(position).id;
            mDes = mList.get(position).des;
            userPraise = mList.get(position).userPraise;
            praiseCount = mList.get(position).praiseCount;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment,
                container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);
        progressBar = (ProgressBar) v.findViewById(R.id.loading);

        tvDes = (TextView) v.findViewById(R.id.tv_beauticianproduction_detail);
        layout_dianzan = (LinearLayout) v.findViewById(R.id.layout_dianzan);
        img_dianzan = (ImageView) v.findViewById(R.id.img_dianzan);
        textview_dianzannums = (TextView) v.findViewById(R.id.textview_dianzannums);
        layoutBottomClick = (LinearLayout) v.findViewById(R.id.layoutBottomClick);


        PetCircle = new File(Environment.getExternalStorageDirectory(), "PetCircle");
        if (!PetCircle.exists()) {
            PetCircle.mkdirs();
        }
        //点击关闭activity
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                try {
                    setBackgroundAlpha(0.6f);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.slide_in_from_top_short,R.anim.slide_out_to_bottom);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //长按保存点击事件
        mAttacher.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String fileName = "Pet_Circle_"
                        + String.valueOf(System.currentTimeMillis() + ".jpg");
                Utils.savePic(getContext(), mImageView, PetCircle, fileName);
                return true;
            }
        });
        if (type == 0) {
            layoutBottomClick.setVisibility(View.VISIBLE);
            layout_dianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    praiseAdd(id);
                }
            });
            img_dianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    praiseAdd(id);
                }
            });
        } else {
            layoutBottomClick.setVisibility(View.GONE);
        }
        return v;
    }

    protected void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private void setViw() {
        if (mDes != null && !"".equals(mDes.trim())) {
            tvDes.setVisibility(View.VISIBLE);
            tvDes.setText(mDes);
        } else {
            tvDes.setVisibility(View.GONE);
            tvDes.setText("");
        }
        if (userPraise == 1) {
            img_dianzan.setBackgroundResource(R.drawable.good_checked);
        } else {
            img_dianzan.setBackgroundResource(R.drawable.good);
        }
        textview_dianzannums.setText(praiseCount + "");
    }

    public void praiseAdd(int relateId) {
        CommUtil.praiseAdd(mContext, 1, relateId, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                String msg = object.getString("msg");
                if (code == 0) {
//					BeauticianProductionDetailActivity.beauticianProductionDetailActivity.setListData(position);
                    BeauticianProductuonActivity.beauticianProductuonActivity.setListData(position);
                    img_dianzan.setBackgroundResource(R.drawable.good_checked);
                    int showpraiseCount = praiseCount += 1;
                    textview_dianzannums.setText(showpraiseCount + "");
                    ToastUtil.showToastShortCenter(mContext, "点赞成功");
                }else {
                    ToastUtil.showToastShortCenter(mContext, msg);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (type == 0) {
            setViw();
        }
        if (is_uri) {
            mImageView.setImageURI(Uri.parse(mImageUrl));
        } else {

            ImageLoaderUtil.loadImg(mImageUrl, mImageView,
                    R.drawable.icon_production_default,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                    message = "下载错误";
                                    break;
                                case DECODING_ERROR:
                                    message = "图片无法显示";
                                    break;
                                case NETWORK_DENIED:
                                    message = "网络有问题，无法下载";
                                    break;
                                case OUT_OF_MEMORY:
                                    message = "图片太大无法显示";
                                    break;
                                case UNKNOWN:
                                    message = "未知的错误";
                                    break;
                            }
                            try {
                                Toast.makeText(getActivity(), message,
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            progressBar.setVisibility(View.GONE);
                            mAttacher.update();
                        }
                    });
        }
    }
}

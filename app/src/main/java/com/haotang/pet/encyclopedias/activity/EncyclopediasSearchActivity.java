package com.haotang.pet.encyclopedias.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.MallAdapter.StockAdapter;
import com.haotang.pet.encyclopedias.bean.EncyHostList;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.view.ClearEditText;
import com.haotang.pet.view.FluidLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/3 0003.
 */

public class EncyclopediasSearchActivity extends SuperActivity {
    @BindView(R.id.input_word_search)
    ClearEditText input_word_search;
    @BindView(R.id.textview_cancle)
    TextView textview_cancle;
    @BindView(R.id.fluid_layout_one)
    FluidLayout fluid_layout_one;
    private List<EncyHostList> hotList = new ArrayList<>();
    private int gravity = Gravity.TOP;
    private StockAdapter stockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ency_search);
        ButterKnife.bind(this);
        getData();
        initListener();
    }

    private void initListener() {
        input_word_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input_word_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH||actionId==EditorInfo.IME_ACTION_UNSPECIFIED){
                    if (!TextUtils.isEmpty(input_word_search.getText())){
                        encyclopediaAddSearchLog(0,input_word_search.getText().toString());
                        goNext(EncyclopediasSearchResultActivity.class,input_word_search.getText().toString());
                        return true;
                    }else {
                        ToastUtil.showToastShortBottom(mContext,"请输入感兴趣的内容");
                    }
                }
                return false;
            }
        });
    }
    private void encyclopediaAddSearchLog(int source,String content){
        CommUtil.encyclopediaAddSearchLog(mContext,source,content,addSearchLogHandler);
    }
    private AsyncHttpResponseHandler addSearchLogHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private void getData() {
        CommUtil.encyclopediaHotSearchList(mContext, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                hotList.add(EncyHostList.j2Entity(array.getJSONObject(i)));
                            }
                        }
                        if (hotList.size() > 0) {
                            genTaghot(fluid_layout_one, true, hotList);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void genTaghot(FluidLayout fluid_layout, boolean hasBg, final List<EncyHostList> tags) {
        fluid_layout.removeAllViews();
        fluid_layout.setGravity(gravity);
        for (int i = 0; i < tags.size(); i++) {
            TextView tv = new TextView(this);
            tv.setBackgroundResource(R.drawable.text_bg);
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setText(tags.get(i).searchContent);
            tv.setTextSize(13);
            tv.setPadding(30, 10, 30, 10);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 20, 20, 20);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
                    int hotId = 0;
                    String content = null;
                    for (int i = 0; i < tags.size(); i++) {
                        EncyHostList mallHot = tags.get(i);
                        if (mallHot.searchContent.equals(view.getText().toString())) {
                            hotId = mallHot.id;
                            content = mallHot.searchContent;
                        }
                    }
//                    encyclopediaAddSearchLog(1,input_word_search.getText().toString());
//                    Intent intent = new Intent(mContext,EncyclopediasSearchResultActivity.class);
//                    intent.putExtra("keyWords",content);
//                    startActivity(intent);
                    goNext(EncyclopediasSearchResultActivity.class,content);
                }
            });
            fluid_layout.addView(tv, params);
        }
    }
    private void goNext(Class cls,String keywords){
        Intent intent = new Intent(mContext,cls);
        intent.putExtra("keyWords",keywords);
        startActivityForResult(intent, Global.ENCYSEARCH_TO_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null){
            return;
        }
        if (resultCode ==Global.RESULT_OK){
            if (requestCode==Global.ENCYSEARCH_TO_RESULT){
                String keyWords =  data.getStringExtra("keyWords");
                input_word_search.setText(keyWords);
                input_word_search.setSelection(input_word_search.getText().length());
            }
        }
    }

    @OnClick({R.id.textview_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textview_cancle:
                finishWithAnimation();
                break;
        }
    }
}

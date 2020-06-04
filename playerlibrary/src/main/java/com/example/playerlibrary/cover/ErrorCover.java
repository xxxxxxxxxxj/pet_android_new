package com.example.playerlibrary.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.playerlibrary.R;
import com.example.playerlibrary.assist.DataInter;
import com.example.playerlibrary.config.PConst;
import com.example.playerlibrary.config.PlayerLibrary;
import com.example.playerlibrary.event.BundlePool;
import com.example.playerlibrary.event.EventKey;
import com.example.playerlibrary.event.OnPlayerEventListener;
import com.example.playerlibrary.receiver.BaseCover;
import com.example.playerlibrary.utils.NetworkUtils;


/**
 * Created by Taurus on 2018/4/20.
 */

public class ErrorCover extends BaseCover {

    final int STATUS_ERROR = -1;
    final int STATUS_UNDEFINE = 0;
    final int STATUS_MOBILE = 1;
    final int STATUS_NETWORK_ERROR = 2;

    int mStatus = STATUS_UNDEFINE;

    TextView mInfo;
    TextView mRetry;

    private boolean mErrorShow;

    private int mCurrPosition;


    public ErrorCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();

        mInfo = findViewById(R.id.tv_error_info);
        mRetry = findViewById(R.id.tv_retry);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStatus();
            }
        });
    }

    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        handleStatusUI(NetworkUtils.getNetworkState(getContext()));
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
    }

    private void handleStatus(){
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_DATA, mCurrPosition);
        switch (mStatus){
            case STATUS_ERROR:
                setErrorState(false);
                requestRetry(bundle);
                break;
            case STATUS_MOBILE:
                PlayerLibrary.ignoreMobile = true;
                setErrorState(false);
                requestResume(bundle);
                break;
            case STATUS_NETWORK_ERROR:
                setErrorState(false);
                requestRetry(bundle);
                break;
        }
    }

    @Override
    public void onProducerData(String key, Object data) {
        super.onProducerData(key, data);
        if(DataInter.Key.KEY_NETWORK_STATE.equals(key)){
            int networkState = (int) data;
            if(networkState== PConst.NETWORK_STATE_WIFI && mErrorShow){
                Bundle bundle = BundlePool.obtain();
                bundle.putInt(EventKey.INT_DATA, mCurrPosition);
                requestRetry(bundle);
            }
            handleStatusUI(networkState);
        }
    }

    private void handleStatusUI(int networkState) {
        if(!getGroupValue().getBoolean(DataInter.Key.KEY_NETWORK_RESOURCE))
            return;
        if(networkState < 0){
            mStatus = STATUS_NETWORK_ERROR;
            setErrorInfo("无网络！");
            setHandleInfo("重试");
            setErrorState(true);
        }else{
            if(networkState== PConst.NETWORK_STATE_WIFI){
                if(mErrorShow){
                    setErrorState(false);
                }
            }else{
                if(PlayerLibrary.ignoreMobile)
                    return;
                mStatus = STATUS_MOBILE;
                setErrorInfo("您正在使用移动网络！");
                setHandleInfo("继续");
                setErrorState(true);
            }
        }
    }

    private void setErrorInfo(String text){
        mInfo.setText(text);
    }

    private void setHandleInfo(String text){
        mRetry.setText(text);
    }

    private void setErrorState(boolean state){
        mErrorShow = state;
        setCoverVisibility(state?View.VISIBLE:View.GONE);
        if(!state){
            mStatus = STATUS_UNDEFINE;
        }else{
            notifyReceiverEvent(DataInter.Event.EVENT_CODE_ERROR_SHOW, null);
        }
        getGroupValue().putBoolean(DataInter.Key.KEY_ERROR_SHOW, state);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                mCurrPosition = 0;
                handleStatusUI(NetworkUtils.getNetworkState(getContext()));
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE:
                mCurrPosition = bundle.getInt(EventKey.INT_ARG1);
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        mStatus = STATUS_ERROR;
        if(!mErrorShow){
            setErrorInfo("出错了！");
            setHandleInfo("重试");
            setErrorState(true);
        }
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_error_cover, null);
    }

    @Override
    public int getCoverLevel() {
        return levelHigh(0);
    }
}

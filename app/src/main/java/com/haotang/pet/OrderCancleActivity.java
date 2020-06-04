package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CancleOrderNewAdapter;
import com.haotang.pet.entity.RefreshOrderEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 取消订单新版
 *
 * @author Administrator
 */
public class OrderCancleActivity extends SuperActivity {
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.imgIcon)
    ImageView imgIcon;
    @BindView(R.id.textViewTop)
    TextView textViewTop;
    @BindView(R.id.imgDefaultIcon)
    ImageView imgDefaultIcon;
    @BindView(R.id.textViewNoticeServiceing)
    TextView textViewNoticeServiceing;
    @BindView(R.id.layoutWorking)
    LinearLayout layoutWorking;
    @BindView(R.id.textViewChooseReason)
    TextView textViewChooseReason;
    @BindView(R.id.imgRightReason)
    ImageView imgRightReason;
    @BindView(R.id.layoutCancleOrderReason)
    LinearLayout layoutCancleOrderReason;
    @BindView(R.id.editTextLinkMan)
    EditText editTextLinkMan;
    @BindView(R.id.layoutLinkMan)
    LinearLayout layoutLinkMan;
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;
    @BindView(R.id.layoutPhone)
    LinearLayout layoutPhone;
    @BindView(R.id.layoutNotice)
    LinearLayout layoutNotice;
    @BindView(R.id.listViewChooseItem)
    MListview listViewChooseItem;
    @BindView(R.id.layoutItemResaon)
    LinearLayout layoutItemResaon;
    @BindView(R.id.relayoutReason)
    RelativeLayout relayoutReason;
    public static OrderCancleActivity orderCancleActivity;
    private SharedPreferenceUtil spUtil;
    public CancleOrderNewAdapter cancleOrderNewAdapter;
    @BindView(R.id.textViewLeftBotton)
    TextView textViewLeftBotton;
    @BindView(R.id.textViewRightBotton)
    TextView textViewRightBotton;
    @BindView(R.id.layoutSetBackBackground)
    LinearLayout layoutSetBackBackground;
    @BindView(R.id.layoutOutSide)
    LinearLayout layoutOutSide;
    private ArrayList<String> listReasons = new ArrayList<>();
    private boolean isCancleEnable = false;
    private int orderId;
    private String officialTelephone;
    private String linkman = " ";
    private String telephone = " ";
    private String rulePage;
    private List<String> listContent = new ArrayList<>();
    private List<String> listTips = new ArrayList<>();
    private int couponId = 0;
    private String chooseId;
    private int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtil = SharedPreferenceUtil.getInstance(OrderCancleActivity.this);
        setContentView(R.layout.activity_order_cancle_new);
        ButterKnife.bind(this);
        MApplication.listAppoint.add(this);
        orderCancleActivity = this;
        getIntentData();
        setView();
        initListener();
        orderCancelRemind();
    }

    private void orderCancelRemind() {
        CommUtil.orderCancelRemind(mContext, orderId, orderCancleDetailHandler);
    }

    private AsyncHttpResponseHandler orderCancleDetailHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jsonObj = new JSONObject(new String(responseBody));
                int code = jsonObj.getInt("code");
                if (code == 0) {
                    if (jsonObj.has("data") && !jsonObj.isNull("data")) {
                        JSONObject json = jsonObj.getJSONObject("data");
                        if (json.has("buttons") && !json.isNull("buttons")) {
                            JSONArray abutton = json.getJSONArray("buttons");
                            if (abutton.length() > 0) {
                                JSONObject obutton = abutton.getJSONObject(0);
                                if (obutton.has("type") && !obutton.isNull("type")) {//如果需要联系客服 这个就是400电话
                                    String type = obutton.getString("type");
                                    officialTelephone = type;
                                }
                                if (obutton.has("text") && !obutton.isNull("text")) {
                                    textViewRightBotton.setText(obutton.getString("text"));
                                }
                            }
                        }
                        if (json.has("rulePage") && !json.isNull("rulePage")) {
                            rulePage = json.getString("rulePage");
                        }
                        if (json.has("cancellable") && !json.isNull("cancellable")) {
                            isCancleEnable = json.getBoolean("cancellable");
                            if (!isCancleEnable) {
                                layoutItemResaon.setVisibility(View.GONE);
                                layoutCancleOrderReason.setVisibility(View.GONE);
                                layoutLinkMan.setVisibility(View.GONE);
                                layoutPhone.setVisibility(View.GONE);
                                layoutWorking.setVisibility(View.VISIBLE);
                                layoutSetBackBackground.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                                layoutSetBackBackground.setClickable(true);
                            } else {
                                layoutCancleOrderReason.setVisibility(View.VISIBLE);
                                layoutLinkMan.setVisibility(View.VISIBLE);
                                layoutPhone.setVisibility(View.VISIBLE);
                                layoutWorking.setVisibility(View.GONE);

                                layoutSetBackBackground.setBackgroundResource(R.drawable.left_right_banyuan_button_huise);
                                layoutSetBackBackground.setClickable(false);
                            }
                        }
                        if (json.has("content") && !json.isNull("content")) {
                            JSONArray aContent = json.getJSONArray("content");
                            if (aContent.length() > 0) {
                                for (int i = 0; i < aContent.length(); i++) {
                                    String content = aContent.getString(i);

                                    listContent.add(content);
                                }
                            }
                        }
                        try {
                            textViewTop.setText(listContent.get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!isCancleEnable) {
                            try {
                                textViewNoticeServiceing.setText(listContent.get(1));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (json.has("tips") && !json.isNull("tips")) {
                            JSONArray aTips = json.getJSONArray("tips");
                            if (aTips.length() > 0) {
                                for (int i = 0; i < aTips.length(); i++) {
                                    String tips = aTips.getString(i);
                                    if (tips.contains("@")) {
                                        tips = tips.replace("@", "");
                                    }
                                    listTips.add(tips);
                                }
                            }
                        }
                        if (listTips.size() > 0) {
                            setLayoutText(listTips);
                        }
                        if (json.has("reasons") && !json.isNull("reasons")) {
                            JSONArray aReasons = json.getJSONArray("reasons");
                            if (aReasons.length() > 0) {
                                for (int i = 0; i < aReasons.length(); i++) {
                                    String reasons = aReasons.getString(i);
                                    listReasons.add(reasons);
                                }
                            }
                        }
                        if (listReasons.size() > 0) {
                            cancleOrderNewAdapter.notifyDataSetChanged();
                        }
                        if (json.has("linkman") && !json.isNull("linkman")) {
                            linkman = json.getString("linkman");
                            editTextLinkMan.setText(linkman);
                            editTextLinkMan.setSelection(linkman.length());
                        } else {
                            linkman = spUtil.getString("userName", "");
                        }
                        if (json.has("telephone") && !json.isNull("telephone")) {
                            telephone = json.getString("telephone");
                            editTextPhone.setText(telephone);
                            editTextPhone.setSelection(telephone.length());
                        } else {
                            telephone = spUtil.getString("cellphone", "");
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

    private void initListener() {
        listViewChooseItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chooseReason = (String) parent.getItemAtPosition(position);
                chooseId = (position + 1) + "";
                textViewChooseReason.setText(chooseReason + "");
                layoutItemResaon.setVisibility(View.GONE);
                cancleOrderNewAdapter.setPos(position);
                setButtonColor();
            }
        });
        editTextLinkMan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setButtonColor();
            }
        });
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setButtonColor();
            }
        });
    }

    private void setView() {
        tvTitlebarTitle.setText("申请退款");
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("退款规则");
        tvTitlebarOther.setTextColor(Color.parseColor("#FFFFFF"));

        cancleOrderNewAdapter = new CancleOrderNewAdapter(mContext, listReasons);
        listViewChooseItem.setAdapter(cancleOrderNewAdapter);
    }

    private void getIntentData() {
        type = getIntent().getIntExtra("type", -1);
        orderId = getIntent().getIntExtra("orderid", -1);
        couponId = getIntent().getIntExtra("couponId", -1);
        getIntent().getStringExtra("linkman");
    }

    private void setLayout() {
        if (isCancleEnable) {
            if (layoutItemResaon.getVisibility() == View.VISIBLE) {
                imgRightReason.setBackgroundResource(R.drawable.icon_arrow_down_beau);
                layoutItemResaon.setVisibility(View.GONE);
            } else {
                imgRightReason.setBackgroundResource(R.drawable.icon_arrow_up_beau);
                layoutItemResaon.setVisibility(View.VISIBLE);
            }

            layoutCancleOrderReason.setVisibility(View.VISIBLE);
            layoutLinkMan.setVisibility(View.VISIBLE);
            layoutPhone.setVisibility(View.VISIBLE);
            layoutWorking.setVisibility(View.GONE);
        } else {
            layoutItemResaon.setVisibility(View.GONE);
            layoutCancleOrderReason.setVisibility(View.GONE);
            layoutLinkMan.setVisibility(View.GONE);
            layoutPhone.setVisibility(View.GONE);
            layoutWorking.setVisibility(View.VISIBLE);
        }
    }

    private void setLayoutText(List<String> listStr) {
        layoutNotice.removeAllViews();
        StringBuilder sp = new StringBuilder();
        StringBuilder sp1 = new StringBuilder();
        TextView textview = new TextView(mContext);
        textview.setTextColor(Color.parseColor("#666666"));
        textview.setLineSpacing(2, 1);
        TextView textview1 = new TextView(mContext);
        textview1.setTextColor(Color.parseColor("#666666"));
        textview1.setLineSpacing(2, 1);
        for (int i = 0; i < 1; i++) {
            sp1.append(listStr.get(i));
        }
        for (int i = 1; i < listStr.size(); i++) {
            sp.append(listStr.get(i) + "\n");
        }
        textview.setText(sp.toString());
        textview1.setText(sp1.toString());
        layoutNotice.addView(textview1);
        layoutNotice.addView(textview);
    }

    // 取消订单
    private AsyncHttpResponseHandler cancelOrder = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            Utils.mLogError("==-->取消订单信息 : " + new String(responseBody));
            String msg = "";
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    EventBus.getDefault().post(new RefreshOrderEvent(true));
                    if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
                        msg = jsonObject.getString("msg");
                        ToastUtil.showToastShortCenter(mContext, msg);
                    }
                    finishWithAnimation();
                } else {
                    if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
                        msg = jsonObject.getString("msg");
                        ToastUtil.showToastShortCenter(mContext, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortCenter(mContext, "网络连接失败");
        }

    };

    //直接取消订单 洗美特色服务
    private void cancleOrder() {
        CommUtil.cancelOrder(mContext, orderId, chooseId, linkman, telephone, cancelOrder);
    }

    //寄养取消
    private void hotelCancel() {
        CommUtil.hotelCancel(mContext, orderId, chooseId, linkman, telephone, cancelOrder);
    }

    private void setButtonColor() {
        if (isCancleEnable) {
            if (!TextUtils.isEmpty(textViewChooseReason.getText())/*&&!TextUtils.isEmpty(editTextLinkMan.getText())&&!TextUtils.isEmpty(editTextPhone.getText())*/) {
                layoutSetBackBackground.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
                layoutSetBackBackground.setClickable(true);
            } else {
                layoutSetBackBackground.setBackgroundResource(R.drawable.left_right_banyuan_button_huise);
                layoutSetBackBackground.setClickable(false);
            }
        } else {
            layoutSetBackBackground.setBackgroundResource(R.drawable.bg_red_jianbian_icon);
            layoutSetBackBackground.setClickable(true);
        }

    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_title, R.id.ib_titlebar_other, R.id.tv_titlebar_other,
            R.id.rl_titlebar, R.id.imgIcon, R.id.textViewTop, R.id.imgDefaultIcon, R.id.textViewNoticeServiceing,
            R.id.layoutWorking, R.id.textViewChooseReason, R.id.imgRightReason,
            R.id.layoutCancleOrderReason, R.id.editTextLinkMan, R.id.layoutLinkMan,
            R.id.editTextPhone, R.id.layoutPhone, R.id.layoutNotice,
            R.id.layoutItemResaon
            , R.id.textViewRightBotton
            , R.id.layoutSetBackBackground
            , R.id.layoutOutSide
            , R.id.relayoutReason})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.tv_titlebar_title:
                break;
            case R.id.ib_titlebar_other:
                break;
            case R.id.tv_titlebar_other:
                //跳转 h5 退款 规则
                Intent intent = new Intent(mContext, ShopH5DetailActivity.class);
                intent.putExtra("previous", 20145);
                intent.putExtra("rulePage", rulePage);
                startActivity(intent);
                break;
            case R.id.rl_titlebar:
                break;
            case R.id.imgIcon:
                break;
            case R.id.textViewTop:
                break;
            case R.id.imgDefaultIcon:
                break;
            case R.id.textViewNoticeServiceing:
                break;
            case R.id.layoutWorking:
                break;
            case R.id.imgRightReason:
                break;
            case R.id.textViewChooseReason:
            case R.id.relayoutReason:
            case R.id.layoutCancleOrderReason:
                setLayout();
                break;
            case R.id.editTextLinkMan:
                break;
            case R.id.layoutLinkMan:
                break;
            case R.id.editTextPhone:
                break;
            case R.id.layoutPhone:
                break;
            case R.id.layoutNotice:
                break;
            case R.id.layoutItemResaon:
                break;
            case R.id.layoutOutSide:
            case R.id.textViewRightBotton:
            case R.id.layoutSetBackBackground:
                if (isCancleEnable) {
                    if (TextUtils.isEmpty(textViewChooseReason.getText())) {
                        ToastUtil.showToastShortCenter(mContext, "请选择退款原因");
                        return;
                    }
                    if (couponId <= 0) {
                        mPDialog.showDialog();
                        if (type == 1) {
                            cancleOrder();
                        } else if (type == 2) {
                            hotelCancel();
                        }
                    } else {
                        MDialog mDialog = new MDialog.Builder(mContext)
                                .setTitle("确定申请退款?").setType(MDialog.DIALOGTYPE_CONFIRM)
                                .setMessage("退款后优惠券不退还～").setCancelStr("取消")
                                .setOKStr("确定")
                                .positiveListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        mPDialog.showDialog();
                                        if (type == 1) {
                                            cancleOrder();
                                        } else if (type == 2) {
                                            hotelCancel();
                                        }
                                    }
                                }).build();
                        mDialog.show();
                    }
                } else {
                    //这里联系客服
                    Utils.telePhoneBroadcast(mContext, officialTelephone);
                }
                break;
        }
    }
}

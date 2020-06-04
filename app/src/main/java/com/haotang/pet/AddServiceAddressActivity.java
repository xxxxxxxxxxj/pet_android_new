package com.haotang.pet;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.entity.event.DeleteAddressSuccessEvent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddServiceAddressActivity extends SuperActivity {

    private ImageButton ib_titlebar_back;
    private ImageView img_people;
    private TextView tv_titlebar_title, textView_icon_addService_area_name, tv_titlebar_other;
    private RelativeLayout layout_choose_service_address;
    private EditText editText_icon_addService_detail_name;
    private Button btn_addService_sure;
    private EditText edit_username;
    private EditText edit_phone;
    private Button bt_petadd_submit;
    /**
     * 广播接收器
     */
    private MyReceiver receiver;
    double lat;
    double lng;
    private int previous;
    private int addressId;
    private SharedPreferenceUtil spUtil;
    private CommAddr commAddr;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//				String str = (String) msg.obj;
//				Toast.makeText(AddServiceAddressActivity.this, "点击连接网络", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    String str = (String) msg.obj;
                    Toast.makeText(AddServiceAddressActivity.this, str, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_address);
        spUtil = SharedPreferenceUtil.getInstance(this);
        previous = getIntent().getIntExtra("previous", 0);
        commAddr = (CommAddr) getIntent().getSerializableExtra("commAddr");
        initView();
        //编辑地址
        if (commAddr != null) {
            tv_titlebar_title.setText("编辑地址");
            bt_petadd_submit.setVisibility(View.VISIBLE);
            edit_username.setText(commAddr.linkman);
            edit_phone.setText(commAddr.telephone);
            textView_icon_addService_area_name.setText(commAddr.address);
            editText_icon_addService_detail_name.setText(commAddr.supplement);
            addressId = commAddr.Customer_AddressId;
        }
        initReceiver();
        initListener();
    }

    private void initListener() {
        editText_icon_addService_detail_name.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText_icon_addService_detail_name.setCursorVisible(true);
                return false;
            }
        });
        ib_titlebar_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        //服务器交互
        btn_addService_sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //这里处理获取到的小区名称以及详细地址，发送到服务器
                if (textView_icon_addService_area_name.getText().equals("") || textView_icon_addService_area_name.getText().equals(null)) {
                    ToastUtil.showToastShort(AddServiceAddressActivity.this, "请输入您的小区名");
                } else {
                    if (!"".equals(spUtil.getString("cellphone", ""))) {
                        CommUtil.addServiceAddress(mContext, addressId, textView_icon_addService_area_name.getText().toString(), lat, lng
                                , editText_icon_addService_detail_name.getText().toString(), edit_username.getText().toString(), edit_phone.getText().toString(),
                                addService);

                    } else {
//						CommUtil.searchTradeArea(lat, lng, searchTradeArea);//未登陆状态下判断是否在服务区，已登陆后台处理
                        //以下模块迁移至服务区判断模块
                        pushDataUnLogin();


                        //以下是给预约模块的
                        Intent data = new Intent();
                        data.putExtra("addr", textView_icon_addService_area_name.getText().toString() + editText_icon_addService_detail_name.getText().toString());
                        data.putExtra("addr_lat", lat);
                        data.putExtra("addr_lng", lng);
                        setResult(Global.RESULT_OK, data);
                        finishWithAnimation();
                    }

                }
            }


        });
        //保存地址到服务器
        tv_titlebar_other.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView_icon_addService_area_name.getText().equals("") || textView_icon_addService_area_name.getText().equals(null)) {
                    ToastUtil.showToastShort(AddServiceAddressActivity.this, "请输入您的小区名");
                    return;
                }
                if (!Utils.isStrNull(Utils.checkEditText(editText_icon_addService_detail_name))) {
                    ToastUtil.showToastShort(AddServiceAddressActivity.this, "请输入详细地址");
                    return;
                }
                if (!"".equals(spUtil.getString("cellphone", ""))) {
                    CommUtil.addServiceAddress(mContext, addressId, textView_icon_addService_area_name.getText().toString(), lat, lng
                            , editText_icon_addService_detail_name.getText().toString(), edit_username.getText().toString(), edit_phone.getText().toString(),
                            addService);
                }
            }
        });

        layout_choose_service_address.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //获取小区名称
                JumpToNext(ServiceAddressSearch.class);
            }
        });
        //删除地址
        bt_petadd_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CommUtil.deleteServiceAddress(mContext, commAddr.Customer_AddressId, deleteServiceAddress);
            }
        });
    }

    private AsyncHttpResponseHandler deleteServiceAddress = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();

            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    String msg = jsonObject.getString("msg");
                    ToastUtil.showDeleteToast(mContext);
                    //通知删除地址成功
                    EventBus.getDefault().post(new DeleteAddressSuccessEvent());
                    finishWithAnimation();
                } else {
                    // 服务器返回字段输出
                    String msg = jsonObject.getString("msg");
                    ToastUtil.showToastShort(AddServiceAddressActivity.this, msg);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            mPDialog.closeDialog();
        }
    };

    private void initReceiver() {
        // 广播事件**********************************************************************
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.AddServiceAddressActivity");
        // 注册广播接收器
        registerReceiver(receiver, filter);
    }

    private void initView() {
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        img_people = (ImageView) findViewById(R.id.img_people);
        textView_icon_addService_area_name = (TextView) findViewById(R.id.textView_icon_addService_area_name);
        layout_choose_service_address = (RelativeLayout) findViewById(R.id.layout_choose_service_address);
        editText_icon_addService_detail_name = (EditText) findViewById(R.id.editText_icon_addService_detail_name);
        editText_icon_addService_detail_name.setCursorVisible(false);
        btn_addService_sure = (Button) findViewById(R.id.btn_addService_sure);
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);
        bt_petadd_submit = findViewById(R.id.bt_petadd_submit);

        tv_titlebar_title.setText("添加地址");
        tv_titlebar_other.setVisibility(View.VISIBLE);
        tv_titlebar_other.setText("保存");
        btn_addService_sure.setVisibility(View.GONE);
        img_people.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

                startActivityForResult(intent, 1000);
            }
        });
    }

    private AsyncHttpResponseHandler searchTradeArea = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            // TODO Auto-generated method stub
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        if (object.has("result") && !object.isNull("result")) {
                            boolean isOk = object.getBoolean("result");
                            if (isOk) {
                                Utils.mLogError("==-->isOk");
                                Intent data = new Intent();
                                data.putExtra("addr", textView_icon_addService_area_name.getText().toString() +
                                        editText_icon_addService_detail_name.getText().toString());
                                data.putExtra("addr_lat", lat);
                                data.putExtra("addr_lng", lng);
                                setResult(Global.RESULT_OK, data);
                                finishWithAnimation();
                            } else {
                                Utils.mLogError("==-->isNot");
                                ToastUtil.showToastShortCenter(AddServiceAddressActivity.this, "很抱歉您选择的地址尚未开通此项服务");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub

        }

    };

    private AsyncHttpResponseHandler addService = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            // TODO Auto-generated method stub
            Utils.mLogError("添加服务地址" + new String(responseBody));

            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code;
                if (jsonObject.has("code") && !jsonObject.isNull("code")) {
                    code = jsonObject.getInt("code");
                    if (code == 0 && jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject object = jsonObject.getJSONObject("data");
//						PushData(object);//加急不要了
                        Intent data = new Intent();
                        data.putExtra("addr", textView_icon_addService_area_name.getText().toString());
                        data.putExtra("supplement", editText_icon_addService_detail_name.getText().toString());
                        if (object.has("telephone") && !object.isNull("telephone")) {
                            data.putExtra("telephone", object.getString("telephone"));
                        }
                        if (object.has("linkman") && !object.isNull("linkman")) {
                            data.putExtra("linkman", object.getString("linkman"));
                        }
                        data.putExtra("addr_lat", lat);
                        data.putExtra("addr_lng", lng);
                        if (object.has("id") && !object.isNull("id")) {
                            data.putExtra("addr_id", object.getInt("id"));
                        }
                        setResult(Global.RESULT_OK, data);
                        if (CommonAddressActivity.commonAddressActivity != null && previous > 0) {
                            CommonAddressActivity.commonAddressActivity.setResult(data);
                        }
                        finishWithAnimation();
                    } else {
                        String msg;
                        if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
                            msg = jsonObject.getString("msg");
                            ToastUtil.showToastShort(AddServiceAddressActivity.this, msg);
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub

        }

    };

    private void PushData(JSONObject jsonObject) throws JSONException {
        //以下是给加急模块的
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("addr", textView_icon_addService_area_name.getText().toString() +
                editText_icon_addService_detail_name.getText().toString());
        bundle.putDouble("addr_lng", lng);
        bundle.putDouble("addr_lat", lat);
        bundle.putInt("addr_id", jsonObject.getInt("id"));
        bundle.putInt("index", 0);
        intent.setAction("android.intent.action.UrgentFragment");
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    private void pushDataUnLogin() {
        //以下是给加急模块的
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("addr", textView_icon_addService_area_name.getText().toString() +
                editText_icon_addService_detail_name.getText().toString());
        bundle.putDouble("addr_lng", lng);
        bundle.putDouble("addr_lat", lat);
        bundle.putInt("index", 0);
        intent.setAction("android.intent.action.UrgentFragment");
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int index = bundle.getInt("index");
            if (index == 0) {
                String selfName = bundle.getString("SelfName");
                textView_icon_addService_area_name.setText(selfName);
                lat = Double.parseDouble(bundle.getString("lat"));
                lng = Double.parseDouble(bundle.getString("lng"));
//				Toast.makeText(context, " lat := "+lat  +"  lng:=  "+lng, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void JumpToNext(Class clazz) {
        Intent intent = new Intent(AddServiceAddressActivity.this, clazz);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("AddServiceAddressActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this);          //统计时长

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("AddServiceAddressActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

    /**
     * 读取联系人信息
     *
     * @param uri
     */
    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            contact[1] = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i("contacts", contact[0]);
            Log.i("contactsUsername", contact[1]);
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    /**
     * 去掉手机号内除数字外的所有字符
     *
     * @param phoneNum 手机号
     * @return
     */
    private String formatPhoneNum(String phoneNum) {
        String regex = "(\\+86)|[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.replaceAll("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    String[] contact = getPhoneContacts(uri);
                    if (contact != null) {
                        String name = contact[0];//姓名
                        String number = contact[1];//手机号
                        Utils.mLogError("==--> " + name + " number " + number);
                        edit_username.setText(name);
                        edit_phone.setText(number.replace(" ", ""));
                    }
                }
            }
        }

    }
}

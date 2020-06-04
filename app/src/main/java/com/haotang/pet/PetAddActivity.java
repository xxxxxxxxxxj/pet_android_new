package com.haotang.pet;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.flyco.roundview.RoundTextView;
import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.CertiOrder;
import com.haotang.pet.entity.RefreshPetEvent;
import com.haotang.pet.luban.Luban;
import com.haotang.pet.luban.OnCompressListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogDefault;
import com.haotang.pet.view.AlertDialogNavAndPost;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PetAddActivity extends SuperActivity {
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_CAMER = 2;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.iv_petadd_img)
    ImageView ivPetaddImg;
    @BindView(R.id.et_petadd_petname)
    EditText etPetaddPetname;
    @BindView(R.id.iv_petadd_petsex_gg)
    ImageView ivPetaddPetsexGg;
    @BindView(R.id.ll_petadd_petsex_gg)
    LinearLayout llPetaddPetsexGg;
    @BindView(R.id.iv_petadd_petsex_mm)
    ImageView ivPetaddPetsexMm;
    @BindView(R.id.ll_petadd_petsex_mm)
    LinearLayout llPetaddPetsexMm;
    @BindView(R.id.tv_petadd_petkindname)
    TextView tvPetaddPetkindname;
    @BindView(R.id.ll_petadd_petpz)
    LinearLayout llPetaddPetpz;
    @BindView(R.id.tv_petadd_petsr)
    TextView tvPetaddPetsr;
    @BindView(R.id.ll_petadd_petsr)
    LinearLayout llPetaddPetsr;
    @BindView(R.id.et_petadd_petms)
    EditText etPetaddPetms;
    @BindView(R.id.iv_petadd_petjg_x35)
    ImageView ivPetaddPetjgX35;
    @BindView(R.id.ll_petadd_petjg_x35)
    LinearLayout llPetaddPetjgX35;
    @BindView(R.id.iv_petadd_petjg_d35)
    ImageView ivPetaddPetjgD35;
    @BindView(R.id.ll_petadd_petjg_d35)
    LinearLayout llPetaddPetjgD35;
    @BindView(R.id.iv_petadd_petbz_yes)
    ImageView ivPetaddPetbzYes;
    @BindView(R.id.ll_petadd_petbz_yes)
    LinearLayout llPetaddPetbzYes;
    @BindView(R.id.iv_petadd_petbz_no)
    ImageView ivPetaddPetbzNo;
    @BindView(R.id.ll_petadd_petbz_no)
    LinearLayout llPetaddPetbzNo;
    @BindView(R.id.sv_petadd_main)
    ScrollView svPetaddMain;
    @BindView(R.id.ll_pet_add)
    LinearLayout llPetAdd;
    @BindView(R.id.tv_petadd_petjg_x35)
    TextView tv_petadd_petjg_x35;
    @BindView(R.id.bt_petadd_submit)
    Button bt_petadd_submit;
    private String TEMPCERTIFICATIONNAME;
    private String photoPath;
    private String bmpStr;
    private String birthday;
    private int customerpetid;
    private int oldsex = -1;
    private int oldhight = -1;
    private String remark;
    private int petid;
    private int oldpetid;
    private String nickname;
    private CertiOrder certiOrder;
    private int sexFlag = -1;
    private int heightFlag = -1;
    private boolean isYm;
    private boolean isJy;
    private String avatar;
    private String petName;
    private String mscolor;
    private static final String[] months =
            {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private String[] years = new String[20];
    private String[] days = null;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private String birthdayStr;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private Uri mUri;
    private int pickIntent;
    private int isBindExtraItemCard;
    private String isBindExtraItemCardTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        customerpetid = getIntent().getIntExtra("customerpetid", 0);
    }

    private void findView() {
        setContentView(R.layout.pet_add);
        ButterKnife.bind(this);
    }

    private void setView() {
        try {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DATE);
            selectedYear = years.length - 1;
            selectedMonth = month;
            selectedDay = date - 1;
            ivPetaddImg.bringToFront();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_petadd_petjg_x35.setText(" <35cm");
        if (customerpetid > 0) {
            mPDialog.showDialog();
            CommUtil.queryCustomerPetById(this, spUtil.getString("cellphone", ""),
                    Global.getCurrentVersion(this), Global.getIMEI(this), customerpetid,
                    queryHandler);
        } else {
            etPetaddPetname.setFocusable(true);
            etPetaddPetname.setFocusableInTouchMode(true);
            etPetaddPetname.requestFocus();
            bt_petadd_submit.setVisibility(View.GONE);
            tvTitlebarTitle.setText("添加宠物");
            btTitlebarOther.setVisibility(View.VISIBLE);
            btTitlebarOther.setText("保存");
        }
    }

    private void setEn(boolean bool) {
        ivPetaddImg.setEnabled(bool);
        etPetaddPetname.setEnabled(bool);
        etPetaddPetms.setEnabled(bool);
        llPetaddPetpz.setEnabled(bool);
        llPetaddPetsexGg.setEnabled(bool);
        llPetaddPetsexMm.setEnabled(bool);
        llPetaddPetsr.setEnabled(bool);
        llPetaddPetjgX35.setEnabled(bool);
        llPetaddPetjgD35.setEnabled(bool);
        llPetaddPetbzYes.setEnabled(bool);
        llPetaddPetbzNo.setEnabled(bool);
    }

    private void goBack() {
        if (isChanged()) {
            new AlertDialogNavAndPost(PetAddActivity.this).builder().setTitle("是否确定退出？")
                    .setMsg("离开页面后填写的内容会消失哦~")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else {
            finish();
        }
    }

    @OnClick({R.id.ib_titlebar_back, R.id.bt_titlebar_other, R.id.iv_petadd_img, R.id.ll_petadd_petsex_gg,
            R.id.ll_petadd_petsex_mm, R.id.ll_petadd_petpz, R.id.ll_petadd_petsr, R.id.ll_petadd_petjg_x35,
            R.id.ll_petadd_petjg_d35, R.id.ll_petadd_petbz_yes, R.id.ll_petadd_petbz_no, R.id.ll_pet_add
            , R.id.bt_petadd_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                goBack();
                break;
            case R.id.bt_titlebar_other:
                saveData();
                break;
            case R.id.iv_petadd_img:
                Utils.goneJP(PetAddActivity.this);
                popPhoto();
                break;
            case R.id.ll_petadd_petsex_gg:
                sexFlag = 1;
                setSex();
                break;
            case R.id.ll_petadd_petsex_mm:
                sexFlag = 0;
                setSex();
                break;
            case R.id.ll_petadd_petpz:
                startActivityForResult(new Intent(this, ChoosePetActivityNew.class).
                        putExtra("previous", Global.ADDPET_TO_PETLIST), Global.ADDPET_TO_PETLIST);
                break;
            case R.id.ll_petadd_petsr:
                Utils.goneJP(PetAddActivity.this);
                showSrDialog();
                break;
            case R.id.ll_petadd_petjg_x35:
                heightFlag = 0;
                setHeight();
                break;
            case R.id.ll_petadd_petjg_d35:
                heightFlag = 1;
                setHeight();
                break;
            case R.id.ll_petadd_petbz_yes:
                setYm();
                break;
            case R.id.ll_petadd_petbz_no:
                setJy();
                break;
            case R.id.ll_pet_add:
                Utils.goneJP(PetAddActivity.this);
                break;
            case R.id.bt_petadd_submit:
                if (isBindExtraItemCard == 1) {
                    String[] split = isBindExtraItemCardTip.split("@@");
                    SpannableString ss = new SpannableString(isBindExtraItemCardTip.replace("@@", ""));
                    int startIndex = split[0].length();
                    int endIndex = startIndex + split[1].length();
                    ss.setSpan(
                            new ForegroundColorSpan(mContext.getResources().getColor(
                                    R.color.aD0021B)), startIndex, endIndex,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("是否确认删除？").setMsg(ss).isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("确认删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPDialog.showDialog();
                            CommUtil.deleteCustomerPet(PetAddActivity.this, spUtil.getString("cellphone", ""),
                                    Global.getCurrentVersion(PetAddActivity.this), Global.getIMEI(PetAddActivity.this),
                                    customerpetid, deleteHandler);
                        }
                    }).show();
                } else {
                    new AlertDialogDefault(mContext).builder()
                            .setTitle("是否确认删除？").setMsg("主人确定不要我了么？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("确认删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPDialog.showDialog();
                            CommUtil.deleteCustomerPet(PetAddActivity.this, spUtil.getString("cellphone", ""),
                                    Global.getCurrentVersion(PetAddActivity.this), Global.getIMEI(PetAddActivity.this),
                                    customerpetid, deleteHandler);
                        }
                    }).show();
                }
                break;
        }
    }

    private void popPhoto() {
        ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.photo_bottom_dialog, null);
        TextView tv_photo_bottomdia_camer = (TextView) customView.findViewById(R.id.tv_photo_bottomdia_camer);
        TextView tv_photo_bottomdia_picture = (TextView) customView.findViewById(R.id.tv_photo_bottomdia_picture);
        RoundTextView tv_photo_bottomdia_cancel = (RoundTextView) customView.findViewById(R.id.tv_photo_bottomdia_cancel);
        ImageView iv_photobottom_bg = (ImageView) customView.findViewById(R.id.iv_photobottom_bg);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_photobottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        tv_photo_bottomdia_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        tv_photo_bottomdia_camer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                    pickIntent = 1;
                    if (!checkPermission()) { //没有或没有全部授权
                        requestPermissions(); //请求权限
                    } else {
                        goCamera();
                    }
                } else {
                    goCamera();
                }
            }
        });
        tv_photo_bottomdia_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                    pickIntent = 2;
                    if (!checkPermission()) { //没有或没有全部授权
                        requestPermissions(); //请求权限
                    } else {
                        goPick();
                    }
                } else {
                    goPick();
                }
            }
        });
    }

    private void goPick() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, SELECT_PICTURE);
    }

    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        TEMPCERTIFICATIONNAME = System.currentTimeMillis()
                + "_pro.jpg";
        File file = new File(Utils
                .getPetPath(PetAddActivity.this),
                TEMPCERTIFICATIONNAME);
        if (Build.VERSION.SDK_INT >= 24) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(PetAddActivity.this, "com.haotang.pet.fileProvider", file);
        } else {
            mUri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, SELECT_CAMER);
    }

    private void showSrDialog() {
        try {
            ViewGroup customView = (ViewGroup) View.inflate(PetAddActivity.this, R.layout.birthday_bottom_dialog, null);
            TextView tv_birthday_bottomdia_wc = (TextView) customView.findViewById(R.id.tv_birthday_bottomdia_wc);
            final WheelView wv_birthday_year = (WheelView) customView.findViewById(R.id.wv_birthday_year);
            final WheelView wv_birthday_month = (WheelView) customView.findViewById(R.id.wv_birthday_month);
            final WheelView wv_birthday_day = (WheelView) customView.findViewById(R.id.wv_birthday_day);
            ImageView iv_birthday_bottom_bg = (ImageView) customView.findViewById(R.id.iv_birthday_bottom_bg);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            for (int i = (calendar.get(Calendar.YEAR) - 19); i <= calendar.get(Calendar.YEAR); i++) {
                years[i - (calendar.get(Calendar.YEAR) - 19)] = i + "年";
            }

            wv_birthday_year.setCurrentItem(selectedYear);
            wv_birthday_year.setLineSpacingMultiplier(2.0f);
            wv_birthday_year.setTextSize(18 * getResources().getDisplayMetrics().density / 3);
            wv_birthday_year.setTextColorCenter(getResources().getColor(R.color.a333333));
            wv_birthday_year.setTextColorOut(getResources().getColor(R.color.a999999));
            wv_birthday_year.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(years)));
            wv_birthday_year.setCyclic(false);//循环滚动
            wv_birthday_year.setDividerColor(getResources().getColor(R.color.a979797));

            wv_birthday_month.setCurrentItem(selectedMonth);
            wv_birthday_month.setLineSpacingMultiplier(2.0f);
            wv_birthday_month.setTextSize(18 * getResources().getDisplayMetrics().density / 3);
            wv_birthday_month.setTextColorCenter(getResources().getColor(R.color.a333333));
            wv_birthday_month.setTextColorOut(getResources().getColor(R.color.a999999));
            wv_birthday_month.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(months)));
            wv_birthday_month.setCyclic(false);//循环滚动
            wv_birthday_month.setDividerColor(getResources().getColor(R.color.a979797));

            int countDay = Utils.getMonthLastDay(Integer.parseInt(years[selectedYear].replace("年", "")),
                    Integer.parseInt(months[selectedMonth].replace("月", "")));
            days = new String[countDay];
            for (int i = 0; i < days.length; i++) {
                days[i] = (i + 1) + "日";
            }

            wv_birthday_day.setCurrentItem(selectedDay);
            wv_birthday_day.setLineSpacingMultiplier(2.0f);
            wv_birthday_day.setTextSize(18 * getResources().getDisplayMetrics().density / 3);
            wv_birthday_day.setTextColorCenter(getResources().getColor(R.color.a333333));
            wv_birthday_day.setTextColorOut(getResources().getColor(R.color.a999999));
            wv_birthday_day.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(days)));
            wv_birthday_day.setCyclic(false);//循环滚动
            wv_birthday_day.setDividerColor(getResources().getColor(R.color.a979797));
            final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            pWinBottomDialog.setFocusable(true);// 取得焦点
            //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
            pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
            //点击外部消失
            pWinBottomDialog.setOutsideTouchable(true);
            //设置可以点击
            pWinBottomDialog.setTouchable(true);
            //进入退出的动画
            pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
            pWinBottomDialog.setWidth(Utils.getDisplayMetrics((Activity) PetAddActivity.this)[0]);
            pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
            wv_birthday_year.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    int countDay = Utils.getMonthLastDay(Integer.parseInt(years[index].replace("年", "")),
                            Integer.parseInt(months[wv_birthday_month.getCurrentItem()].replace("月", "")));
                    days = new String[countDay];
                    for (int i = 0; i < days.length; i++) {
                        days[i] = (i + 1) + "日";
                    }
                    wv_birthday_day.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(days)));
                    wv_birthday_day.setCurrentItem(0);
                }
            });
            wv_birthday_month.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    int countDay = Utils.getMonthLastDay(Integer.parseInt(years[wv_birthday_year.getCurrentItem()].replace("年", "")),
                            Integer.parseInt(months[index].replace("月", "")));
                    days = new String[countDay];
                    for (int i = 0; i < days.length; i++) {
                        days[i] = (i + 1) + "日";
                    }
                    wv_birthday_day.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(days)));
                    wv_birthday_day.setCurrentItem(0);
                }
            });
            iv_birthday_bottom_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinBottomDialog.dismiss();
                }
            });
            tv_birthday_bottomdia_wc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWinBottomDialog.dismiss();
                    selectedYear = wv_birthday_year.getCurrentItem();
                    selectedMonth = wv_birthday_month.getCurrentItem();
                    selectedDay = wv_birthday_day.getCurrentItem();
                    tvPetaddPetsr.setText(years[selectedYear].replace("年", "-") + months[selectedMonth].replace("月", "-")
                            + days[selectedDay].replace("日", ""));
                    birthday = years[selectedYear].replace("年", "-") + months[selectedMonth].replace("月", "-")
                            + days[selectedDay].replace("日", "") + " 00:00:00";
                    Log.e("TAG", "birthday = " + birthday);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //检查权限
    private boolean checkPermission() {
        //是否有权限
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        boolean haveWritePermission = ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return haveCameraPermission && haveWritePermission;

    }

    // 请求所需权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    // 请求权限后会在这里回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:

                boolean allowAllPermission = false;

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//被拒绝授权
                        allowAllPermission = false;
                        break;
                    }
                    allowAllPermission = true;
                }

                if (allowAllPermission) {
                    if (pickIntent == 1) {
                        goCamera();
                    } else if (pickIntent == 2) {
                        goPick();
                    }
                } else {
                    Toast.makeText(mContext, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void setSex() {
        if (sexFlag == 0) {//MM
            ivPetaddPetsexGg.setImageResource(R.drawable.icon_petadd_sex_no);
            ivPetaddPetsexMm.setImageResource(R.drawable.icon_petadd_mm_yes);
        } else if (sexFlag == 1) {//GG
            ivPetaddPetsexGg.setImageResource(R.drawable.icon_petadd_gg_yes);
            ivPetaddPetsexMm.setImageResource(R.drawable.icon_petadd_sex_no);
        }
    }

    private void setHeight() {
        if (heightFlag == 0) {//<35
            ivPetaddPetjgX35.setImageResource(R.drawable.icon_petadd_select);
            ivPetaddPetjgD35.setImageResource(R.drawable.icon_petadd_unselect);
        } else if (heightFlag == 1) {//>35
            ivPetaddPetjgX35.setImageResource(R.drawable.icon_petadd_unselect);
            ivPetaddPetjgD35.setImageResource(R.drawable.icon_petadd_select);
        }
    }

    private void setYm() {
        if (isYm) {//已打疫苗
            ivPetaddPetbzYes.setImageResource(R.drawable.icon_petadd_unselect);
        } else {//未打疫苗
            ivPetaddPetbzYes.setImageResource(R.drawable.icon_petadd_select);
        }
        isYm = !isYm;
    }

    private void setJy() {
        if (isJy) {//已绝育
            ivPetaddPetbzNo.setImageResource(R.drawable.icon_petadd_unselect);
        } else {//未绝育
            ivPetaddPetbzNo.setImageResource(R.drawable.icon_petadd_select);
        }
        isJy = !isJy;
    }

    private void sendBroadcastToMainUpdataUserinfo() {
        Utils.mLogError("去更新主界面");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.mainactivity");
        intent.putExtra("previous", Global.DELETEPET_TO_UPDATEUSERINFO);
        sendBroadcast(intent);
    }

    private void saveData() {
        if (etPetaddPetname.getText().toString().trim().length() <= 0) {
            ToastUtil.showToastShortCenter(this, "还没有填写宝贝的名字呢");
            return;
        }
        if (sexFlag < 0) {
            ToastUtil.showToastShortCenter(this, "还没有选择宝贝的性别呢");
            return;
        }
        if (tvPetaddPetkindname.getText().toString().trim().length() <= 0) {
            ToastUtil.showToastShortCenter(this, "还没有选择宝贝的品种呢");
            return;
        }
        StringBuffer sb = new StringBuffer();
        if (isYm)
            sb.append("ym=1");
        if (isJy) {
            sb.append(";spayed=1");
        }
        mPDialog.showDialog();
        CommUtil.newCustomerPet(this, spUtil.getString("cellphone", ""), Global
                        .getCurrentVersion(this), Global.getIMEI(this), customerpetid, petid,
                tvPetaddPetkindname.getText().toString().trim(), etPetaddPetname.getText()
                        .toString().trim(), sexFlag, birthday, 0, heightFlag, sb
                        .toString(), bmpStr, etPetaddPetms.getText()
                        .toString(), saveHandler);
    }

    private AsyncHttpResponseHandler saveHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    EventBus.getDefault().post(new RefreshPetEvent());
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("id") && !jdata.isNull("id")) {
                            customerpetid = jdata.getInt("id");
                            ToastUtil.showToastShortCenter(
                                    PetAddActivity.this, "保存成功");
                            //这段代码不确定还用不用不用刻意删掉了
                            Intent data = new Intent();
                            data.putExtra("editflag", 1);
                            data.putExtra("customerpetid", customerpetid);
                            setResult(Global.RESULT_OK, data);
                            finish();
                        }
                    }
                    setResult(Global.RESULT_OK);
                    PetAddActivity.this.finish();
                } else {
                    ToastUtil.showToastShortCenter(getApplicationContext(),
                            jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortCenter(getApplicationContext(),
                        "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortCenter(getApplicationContext(),
                    "请求失败");
        }
    };

    private AsyncHttpResponseHandler deleteHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    EventBus.getDefault().post(new RefreshPetEvent());
                    sendBroadcastToMainUpdataUserinfo();
                    ToastUtil.showToastShortCenter(getApplicationContext(),
                            "删除成功");
                    if (PetDetailActivity.act != null)
                        PetDetailActivity.act.finish();
                    finish();
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastShort(PetAddActivity.this,
                                jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortCenter(getApplicationContext(),
                        "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortCenter(getApplicationContext(),
                    "请求失败");
        }
    };

    private AsyncHttpResponseHandler queryHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("isBindExtraItemCard") && !jdata.isNull("isBindExtraItemCard")) {
                            isBindExtraItemCard = jdata.getInt("isBindExtraItemCard");
                        }
                        if (jdata.has("isBindExtraItemCardTip") && !jdata.isNull("isBindExtraItemCardTip")) {
                            isBindExtraItemCardTip = jdata.getString("isBindExtraItemCardTip");
                        }
                        if (jdata.has("pet") && !jdata.isNull("pet")) {
                            JSONObject jpet = jdata.getJSONObject("pet");
                            if (jpet.has("certiOrder") && !jpet.isNull("certiOrder")) {
                                JSONObject jsonObject = jpet.getJSONObject("certiOrder");
                                certiOrder = CertiOrder.json2Entity(jsonObject);
                            }
                            if (jpet.has("nickName") && !jpet.isNull("nickName")) {
                                nickname = jpet.getString("nickName").trim();
                            }
                            if (jpet.has("petName") && !jpet.isNull("petName")) {
                                petName = jpet.getString("petName");
                            }
                            if (jpet.has("color") && !jpet.isNull("color")) {
                                mscolor = jpet.getString("color");
                            }
                            if (jpet.has("sex") && !jpet.isNull("sex")) {
                                sexFlag = jpet.getInt("sex");
                                oldsex = sexFlag;
                            }
                            if (jpet.has("height") && !jpet.isNull("height")) {
                                heightFlag = jpet.getInt("height");
                                oldhight = heightFlag;
                            }
                            if (jpet.has("remark") && !jpet.isNull("remark")) {
                                remark = jpet.getString("remark");
                            }
                            if (jpet.has("petId") && !jpet.isNull("petId")) {
                                petid = jpet.getInt("petId");
                                oldpetid = petid;
                            }
                            if (jpet.has("birthday") && !jpet.isNull("birthday")) {
                                birthdayStr = jpet.getString("birthday").split(" ")[0];
                            }
                            if (jpet.has("avatar") && !jpet.isNull("avatar")) {
                                avatar = jpet.getString("avatar");
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastShortCenter(getApplicationContext(),
                            jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastShortCenter(getApplicationContext(),
                        "数据异常");
            }
            GlideUtil.loadCircleImg(PetAddActivity.this, avatar, ivPetaddImg,
                    R.drawable.user_icon_unnet_circle);
            if (Utils.isStrNull(remark)) {
                if (remark.contains("ym=1")) {
                    isYm = true;
                    ivPetaddPetbzYes.setImageResource(R.drawable.icon_petadd_select);
                } else {
                    isYm = false;
                    ivPetaddPetbzYes.setImageResource(R.drawable.icon_petadd_unselect);
                }
                if (remark.contains("spayed=1")) {
                    isJy = true;
                    ivPetaddPetbzNo.setImageResource(R.drawable.icon_petadd_select);
                } else {
                    isJy = false;
                    ivPetaddPetbzNo.setImageResource(R.drawable.icon_petadd_unselect);
                }
            } else {
                isYm = false;
                ivPetaddPetbzYes.setImageResource(R.drawable.icon_petadd_unselect);
                isJy = false;
                ivPetaddPetbzNo.setImageResource(R.drawable.icon_petadd_unselect);
            }
            Utils.setText(tvPetaddPetsr, birthdayStr, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(etPetaddPetms, mscolor, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvPetaddPetkindname, petName, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvTitlebarTitle, nickname, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(etPetaddPetname, nickname, "", View.VISIBLE, View.VISIBLE);
            if (!Utils.isStrNull(nickname)) {
                etPetaddPetname.setFocusable(true);
                etPetaddPetname.setFocusableInTouchMode(true);
                etPetaddPetname.requestFocus();
            } else if (!Utils.isStrNull(mscolor)) {
                etPetaddPetms.setFocusable(true);
                etPetaddPetms.setFocusableInTouchMode(true);
                etPetaddPetms.requestFocus();
            }
            if (heightFlag >= 0) {
                setHeight();
            }
            if (sexFlag >= 0) {
                setSex();
            }
            if (certiOrder != null) {
                int status = certiOrder.status;
                if (status == 0 || status == 6 || status == 7) {// 可删除，不可编辑
                    bt_petadd_submit.setVisibility(View.VISIBLE);
                    btTitlebarOther.setVisibility(View.GONE);
                    setEn(false);
                } else {// 不可删除，也不可编辑
                    bt_petadd_submit.setVisibility(View.GONE);
                    btTitlebarOther.setVisibility(View.GONE);
                    setEn(false);
                }
            } else {// 可删除，可编辑
                btTitlebarOther.setVisibility(View.VISIBLE);
                btTitlebarOther.setText("保存");
                bt_petadd_submit.setVisibility(View.VISIBLE);
                setEn(true);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastShortCenter(getApplicationContext(),
                    "请求失败");
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || resultCode == Global.RESULT_OK) {
            switch (requestCode) {
                // 从相册选择
                case SELECT_PICTURE:
                    if (data == null) {
                        ToastUtil.showToastShortCenter(PetAddActivity.this,
                                "您选择的照片不存在，请重新选择");
                        return;
                    }
                    try {
                        Uri originalUri = data.getData(); // 获得图片的uri
                        if (!TextUtils.isEmpty(originalUri.getAuthority())) {
                            // 这里开始的第二部分，获取图片的路径：
                            String[] proj = {MediaStore.Images.Media.DATA};
                            // Cursor cursor = managedQuery(originalUri, proj, null,
                            // null, null);
                            Cursor cursor = getContentResolver().query(originalUri,
                                    proj, null, null, null);
                            // 获得用户选择的图片的索引值
                            int column_index = cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            // 最后根据索引值获取图片路径
                            photoPath = cursor.getString(column_index);
                            cursor.close();
                            // 华为及其他手机系统的图片Url获取
                            compressWithLs(new File(photoPath));
                        } else {
                            // 小米系统走的方法
                            compressWithLs(new File(originalUri.getPath()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                // 拍照添加图片
                case SELECT_CAMER:
                    File file = new File(Utils.getPetPath(PetAddActivity.this),
                            TEMPCERTIFICATIONNAME);
                    if (file != null && file.length() > 0) {
                        photoPath = file.getAbsolutePath();
                        compressWithLs(new File(photoPath));
                    } else {
                        Toast.makeText(PetAddActivity.this, "您选择的照片不存在，请重新选择",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Global.ADDPET_TO_PETLIST:
                    petid = data.getIntExtra("petid", 0);
                    tvPetaddPetkindname.setText(data.getStringExtra("petname"));
                    break;
            }
        }
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(File file) {
        Luban.get(this).load(file).putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        Bitmap getxtsldraw = Utils.getxtsldraw(
                                PetAddActivity.this, file.getAbsolutePath());
                        Glide.with(PetAddActivity.this).load(file)
                                .placeholder(R.drawable.user_icon_unnet_circle) // 占位图
                                .error(R.drawable.user_icon_unnet_circle) // 出错的占位图
                                .transform(new CircleCrop())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(ivPetaddImg);
                        bmpStr = Global.encodeWithBase64(getxtsldraw);// 这句话将图片转码后发送给服务器
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    private boolean isChanged() {
        if (Utils.isStrNull(bmpStr))
            return true;
        if ((nickname != null && !etPetaddPetname.getText().toString().trim()
                .equals(nickname))
                || (nickname == null && etPetaddPetname.getText().toString().trim()
                .length() > 0))
            return true;
        if (petid != oldpetid)
            return true;
        if (oldsex != sexFlag)
            return true;
        if (Utils.isStrNull(birthday))
            return true;
        if (heightFlag != oldhight)
            return true;
        StringBuffer sb = new StringBuffer();
        if (isYm)
            sb.append("ym=1");
        if (isJy) {
            sb.append(";spayed=1");
        }
        if ((remark != null && !sb.toString().trim().equals(remark))
                || (remark == null && sb.toString().trim().length() > 0))
            return true;
        return false;
    }
}

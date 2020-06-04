package com.haotang.pet;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BannerBathLoopAdapter;
import com.haotang.pet.adapter.PetCirCleInsideAdapter;
import com.haotang.pet.entity.LoginSuccessEvent;
import com.haotang.pet.entity.PetCircle;
import com.haotang.pet.entity.PetCircleBanner;
import com.haotang.pet.entity.PetCircleInside;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MarketDialogEvent;
import com.haotang.pet.view.UserNameAlertDialog;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.hintview.ColorPointHintView;
import com.melink.bqmmsdk.widget.BQMMMessageText;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 宠圈内
 *
 * @author Administrator
 */
public class PetCircleInsideActivity extends SuperActivity implements OnClickListener {

    public static PetCircleInsideActivity insideActivity;
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    private TextView tv_titlebar_other;
    private PullToRefreshListView pet_circle_inside_listview;
    private View header;
    private ImageView imageView_pet_inside;
    private TextView textview_pet_circle_name;
    private TextView textview_pet_circle_detail;
    private TextView textview_pet_circle_follow_num;
    private TextView textview_pet_circle_post_num;
    private Button button_pet_inside_or;
    private RollPagerView rpv_servicedetail_pet;
    private LinearLayout layout_pet_circle_top_one;
    private TextView textview_top_tag_one;
    private BQMMMessageText textview_top_detail;
    private ImageView imageview_top_right;
    private LinearLayout layout_pet_circle_second;
    private TextView textview_second_tag_one;
    private BQMMMessageText textview_second_detail;
    private ImageView imageview_second_right;
    private static ArrayList<PetCircleInside> list = new ArrayList<PetCircleInside>();
    private static PetCirCleInsideAdapter insideAdapter;
    private ImageView imageview_area_leader;
    private ImageView imageview_shop_leader;
    private LinearLayout layout_one_left;
    private LinearLayout layout_one_right;
    private View left_and_right_line;
    private LinearLayout layout_huodong_one;
    private LinearLayout layout_huodong_two;

    private ArrayList<String> listBanner = new ArrayList<String>();
    private RollPagerView rpvBanner;
    private BannerBathLoopAdapter adapterBanner;
    private static int page = 1;
    private int groupId = 7;
    private TextView textview_petcircle_jl;
    private TextView textview_petcircle_dz;
    private PetCircle petCircle = null;
    private int PostInfoIdOne;
    private int PostInfoIdTwo;
    private String homepageJl;
    private String realNameJL;
    private String homepageDZ;
    private String realNameDZ;

    private ArrayList<PetCircleBanner> petCircleList = new ArrayList<PetCircleBanner>();
    private boolean isTwoBanner = false;
    private int isFollowGroup = -1;
    private LinearLayout show_no_data;
    private boolean isJoinCircleDialog = false;
    private int index = 0;
    private View view_line_isshow;
    private LinearLayout layout_circle_master;
    private static ArrayList<PetCircleInside> listLast = new ArrayList<PetCircleInside>();
    private static double pageSize = 10;
    private long timestamp = 0;
    //	private MyReceiver receiver;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_circle_inside);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        insideActivity = this;
        list.clear();
        petCircleList.clear();
        listBanner.clear();
        listLast.clear();
        page = 1;
        MApplication.listAppoint.add(insideActivity);
        getIntentData();
        initView();
//		initReceiver();
        setData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MarketDialogEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null && event.getEventType() == MarketDialogEvent.PETHOUSEPOST) {
            try {
                if (!spUtil.getBoolean("GOTOMARKET_DIALOG_TRUE", false)) {//先判断是否点击跳转到应用市场
                    //再判断是否点击暂时没有
                    if (spUtil.getBoolean("GOTOMARKET_DIALOG_FALSE", false)) {
                        //再判断距离上一次点击暂时没有是否大于10天且判断打开次数是否是5次
                        String gotomarket_dialog_time = spUtil.getString("GOTOMARKET_DIALOG_TIME", "");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String format = df.format(new Date());// new Date()为获取当前系统时间
                        if (Utils.daysBetween(gotomarket_dialog_time, format) >= 10) {
                            //弹出
                            Utils.goMarketDialog(PetCircleInsideActivity.this);
                        }
                    } else {
                        //弹出
                        Utils.goMarketDialog(PetCircleInsideActivity.this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //	private void initReceiver() {
//		// 广播事件**********************************************************************
//		receiver = new MyReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction("android.intent.action.PetCircleInsideActivity");
//		// 注册广播接收器
//		registerReceiver(receiver, filter);
//	}
    private void getIntentData() {
        petCircle = (PetCircle) getIntent().getSerializableExtra("petCircle");
        groupId = petCircle.PostGroupId;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setData() {
        pet_circle_inside_listview.getRefreshableView().addHeaderView(header);
        insideAdapter = new PetCirCleInsideAdapter<PetCircleInside>(this, list);
        pet_circle_inside_listview.setMode(Mode.BOTH);
        pet_circle_inside_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshBase refreshView) {
                PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                if (mode == Mode.PULL_FROM_START) {
                    // 下拉刷新
                    page = 1;
                    list.clear();
                    insideAdapter.notifyDataSetChanged();
                    getData();
                } else {
                    try {
                        if (listLast.size() > 0 && listLast.size() < pageSize) {
                            page = page - 1;
                            list.removeAll(listLast);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    getData();
                }
            }
        });
        pet_circle_inside_listview.setAdapter(insideAdapter);

        adapterBanner = new BannerBathLoopAdapter(this, rpvBanner, listBanner);
        rpvBanner.setAdapter(adapterBanner);


//		pet_circle_inside_listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				PetCircleInside inside = (PetCircleInside) parent.getItemAtPosition(position);
//				Intent intent = new Intent(mContext, PetCircleInsideDetailActivity.class);
//				intent.putExtra("postId", inside.PostInfoId);
//				startActivity(intent);
//			}
//		});
        getData();
    }

    private void getData() {
        // TODO Auto-generated method stub
        mPDialog.showDialog();
        CommUtil.queryPostInfoList(spUtil.getString("cellphone", ""), Global.getIMEI(this), this, groupId, page, timestamp, queryPostInfoList);
    }

    private AsyncHttpResponseHandler queryPostInfoList = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("==-->帖子列表 " + new String(responseBody));
            pet_circle_inside_listview.onRefreshComplete();
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    JSONObject objectData = object.getJSONObject("data");
                    if (objectData.has("postGroup") && !objectData.isNull("postGroup")) {
                        JSONObject objectPostGroup = objectData.getJSONObject("postGroup");
                        if (objectPostGroup.has("groupName") && !objectPostGroup.isNull("groupName")) {
                            String groupName = objectPostGroup.getString("groupName");
                            textview_pet_circle_name.setText(groupName);
                            tv_titlebar_title.setText(groupName);
                        }
                        if (objectPostGroup.has("pic") && !objectPostGroup.isNull("pic")) {
                            String pic = objectPostGroup.getString("pic");
                            GlideUtil.loadRoundImg(PetCircleInsideActivity.this,pic,imageView_pet_inside,R.drawable.icon_self,5);
                        }
                        if (objectPostGroup.has("extendParam") && !objectPostGroup.isNull("extendParam")) {
                            JSONObject objectExtend = objectPostGroup.getJSONObject("extendParam");
                            if (objectExtend.has("isFollowGroup") && !objectExtend.isNull("isFollowGroup")) {
                                isFollowGroup = objectExtend.getInt("isFollowGroup");
                                if (isFollowGroup == 1) {
                                    //如果没加入执行点击然后已加入
                                    button_pet_inside_or.setText("已加入");
                                    button_pet_inside_or.setTextColor(getResources().getColor(R.color.aBBBBBB));
                                    button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_huise_back);
                                }
                            } else {
                                isFollowGroup = 2;
                                button_pet_inside_or.setText("+ 加入");
                                button_pet_inside_or.setTextColor(getResources().getColor(R.color.aD1494F));
                                button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_appoiment_ok);
                            }

                            if (objectExtend.has("followCount") && !objectExtend.isNull("followCount")) {
                                int followCount = objectExtend.getInt("followCount");
                                textview_pet_circle_follow_num.setText(followCount + "");
                            }
                            if (objectExtend.has("postInfoCount") && !objectExtend.isNull("postInfoCount")) {
                                int postInfoCount = objectExtend.getInt("postInfoCount");
                                textview_pet_circle_post_num.setText(postInfoCount + "");
                            }
                        }
//						if (objectPostGroup.has("isFollowGroup")&&!objectPostGroup.isNull("isFollowGroup")) {
//							isFollowGroup = objectPostGroup.getInt("isFollowGroup");
//							if (isFollowGroup==1) {
//								//如果没加入执行点击然后已加入
//								button_pet_inside_or.setText("已加入");
//								button_pet_inside_or.setTextColor(getResources().getColor(R.color.aBBBBBB));
//								button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_huise_back);
//							}
//						}else {
//							isFollowGroup = 2;
//							button_pet_inside_or.setText("+ 加入");
//							button_pet_inside_or.setTextColor(getResources().getColor(R.color.aD1494F));
//							button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_appoiment_ok);
//						}
                        if (Utils.checkLogin(insideActivity)) {
                            if (!isLogin) {
                                isLogin = true;
                                //有没登录的登陆后通知
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.PetCircleFragment");
                                intent.putExtra("index", 1);
                                sendBroadcast(intent);
                            }
                        }
//						if (objectPostGroup.has("followCount")&&!objectPostGroup.isNull("followCount")) {
//							int followCount = objectPostGroup.getInt("followCount");
//							textview_pet_circle_follow_num.setText(followCount+"");
//						}
//						if (objectPostGroup.has("postInfoCount")&&!objectPostGroup.isNull("postInfoCount")) {
//							int postInfoCount = objectPostGroup.getInt("postInfoCount");
//							textview_pet_circle_post_num.setText(postInfoCount+"");
//						}
                    }

                    if (objectData.has("groupJl") && !objectData.isNull("groupJl")) {
                        JSONObject objectJL = objectData.getJSONObject("groupJl");
                        if (objectJL.has("realName") && !objectJL.isNull("realName")) {
                            realNameJL = objectJL.getString("realName");
                            textview_petcircle_jl.setText(realNameJL);
                        }
                        if (objectJL.has("headImg") && !objectJL.isNull("headImg")) {
                            String headImg = objectJL.getString("headImg");
                            GlideUtil.loadRoundImg(PetCircleInsideActivity.this,headImg,imageview_area_leader,R.drawable.icon_self,5);
                        }
                        if (objectJL.has("profile") && !objectJL.isNull("profile")) {
                            String profile = objectJL.getString("profile");
                        }
                        if (objectJL.has("homepage") && !objectJL.isNull("homepage")) {
                            homepageJl = objectJL.getString("homepage");
                        }
                        if (objectJL.has("groupId") && !objectJL.isNull("groupId")) {
                            int groupIdJL = objectJL.getInt("groupId");
                        }
                    }

                    if (objectData.has("groupDz") && !objectData.isNull("groupDz")) {
                        JSONObject objectDz = objectData.getJSONObject("groupDz");
                        if (objectDz.has("realName") && !objectDz.isNull("realName")) {
                            realNameDZ = objectDz.getString("realName");
                            textview_petcircle_dz.setText(realNameDZ);
                        }
                        if (objectDz.has("headImg") && !objectDz.isNull("headImg")) {
                            String headImg = objectDz.getString("headImg");
                            GlideUtil.loadRoundImg(PetCircleInsideActivity.this,headImg,imageview_shop_leader,R.drawable.icon_self,5);
                        }
                        if (objectDz.has("profile") && !objectDz.isNull("profile")) {
                            String profile = objectDz.getString("profile");
                        }
                        if (objectDz.has("homepage") && !objectDz.isNull("homepage")) {
                            homepageDZ = objectDz.getString("homepage");
                        }
                        if (objectDz.has("groupId") && !objectDz.isNull("groupId")) {
                            int groupIdJL = objectDz.getInt("groupId");
                        }
                    }
                    boolean isGroupDZ = showLayout(objectData);
                    if (objectData.has("topList") && !objectData.isNull("topList")) {
                        JSONArray arrayTopList = objectData.getJSONArray("topList");
                        if (arrayTopList.length() > 0) {
                            layout_huodong_one.setVisibility(View.VISIBLE);
                            layout_huodong_two.setVisibility(View.VISIBLE);
                            view_line_isshow.setVisibility(View.VISIBLE);
                            try {
                                JSONObject objectOne = arrayTopList.getJSONObject(0);
                                if (objectOne.has("content") && !objectOne.isNull("content")) {
                                    String content = objectOne.getString("content");
                                    textview_top_detail.setText(content);
                                    textview_top_tag_one.setBackgroundDrawable(Utils.getDW("FF3A1E"));
                                }
                                if (objectOne.has("id") && !objectOne.isNull("id")) {
                                    PostInfoIdOne = objectOne.getInt("id");
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                layout_huodong_one.setVisibility(View.GONE);
                            }
                            try {
                                JSONObject objectTwo = arrayTopList.getJSONObject(1);
                                if (objectTwo.has("content") && !objectTwo.isNull("content")) {
                                    String content = objectTwo.getString("content");
                                    textview_second_detail.setText(content);
                                    textview_second_tag_one.setBackgroundDrawable(Utils.getDW("FF3A1E"));
                                }
                                if (objectTwo.has("id") && !objectTwo.isNull("id")) {
                                    PostInfoIdTwo = objectTwo.getInt("id");
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Utils.mLogError("==-->固定返回两条，如果只返回一条这个就挂了~");
                                layout_huodong_two.setVisibility(View.GONE);
                            }
                        } else {
                            layout_pet_circle_top_one.setVisibility(View.GONE);
                            layout_pet_circle_second.setVisibility(View.GONE);
                            view_line_isshow.setVisibility(View.GONE);
                        }
                    } else {
                        layout_pet_circle_top_one.setVisibility(View.GONE);
                        layout_pet_circle_second.setVisibility(View.GONE);
                        view_line_isshow.setVisibility(View.GONE);
                    }
                    if (!isTwoBanner) {
                        isTwoBanner = true;
                        if (objectData.has("groupBanners") && !objectData.isNull("groupBanners")) {
                            listBanner.clear();
                            JSONArray array = objectData.getJSONArray("groupBanners");
                            for (int i = 0; i < array.length(); i++) {
                                PetCircleBanner circleBanner = new PetCircleBanner();
                                JSONObject jsonObject = array.getJSONObject(i);
                                if (jsonObject.has("imgUrl") && !jsonObject.isNull("imgUrl")) {
                                    listBanner.add(jsonObject.getString("imgUrl"));
                                    circleBanner.imgUrl = jsonObject.getString("imgUrl");
                                }
                                if (jsonObject.has("imgLink") && !jsonObject.isNull("imgLink")) {
                                    circleBanner.imgLink = jsonObject.getString("imgLink");
                                }
                                if (jsonObject.has("point") && !jsonObject.isNull("point")) {
                                    circleBanner.point = jsonObject.getInt("point");
                                }
                                if (jsonObject.has("backup") && !jsonObject.isNull("backup")) {
                                    circleBanner.backup = jsonObject.getString("backup");
                                }
                                petCircleList.add(circleBanner);
                            }
                            if (listBanner.size() > 1) {
                                rpvBanner.setHintView(new ColorPointHintView(mContext, Color.parseColor("#FE8A3F"), Color.parseColor("#FFE2D0")));
                            } else {
                                rpvBanner.setHintView(null);
                            }
                            adapterBanner.setPetCircleBanner(petCircleList);
                            adapterBanner.notifyDataSetChanged();
                        }
                    }
                    if (objectData.has("pageSize") && !objectData.isNull("pageSize")) {
                        pageSize = objectData.getDouble("pageSize");
                    }
                    if (objectData.has("timestamp") && !objectData.isNull("timestamp")) {
                        timestamp = objectData.getLong("timestamp");
                    }
                    if (objectData.has("list") && !objectData.isNull("list")) {
                        JSONArray array = objectData.getJSONArray("list");
                        if (array.length() > 0) {
                            page++;
                            listLast.clear();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject objectEvery = array.getJSONObject(i);
                            PetCircleInside inside = new PetCircleInside();
                            if (objectEvery.has("content") && !objectEvery.isNull("content")) {
                                inside.content = objectEvery.getString("content");
                            }
                            if (objectEvery.has("created") && !objectEvery.isNull("created")) {
                                inside.created = objectEvery.getString("created");
                            }
                            if (objectEvery.has("extendParam") && !objectEvery.isNull("extendParam")) {
                                JSONObject objExtend = objectEvery.getJSONObject("extendParam");
                                if (objExtend.has("imgs") && !objExtend.isNull("imgs")) {
                                    JSONArray imgArray = objExtend.getJSONArray("imgs");
                                    if (imgArray.length() > 0) {
                                        for (int j = 0; j < imgArray.length(); j++) {
                                            inside.list.add(imgArray.getString(j));
                                        }
                                    }
                                }
                                if (objExtend.has("smallImgs") && !objExtend.isNull("smallImgs")) {
                                    JSONArray smallImgsArray = objExtend.getJSONArray("smallImgs");
                                    if (smallImgsArray.length() > 0) {
                                        for (int j = 0; j < smallImgsArray.length(); j++) {
                                            inside.smallImgsList.add(smallImgsArray.getString(j));
                                        }
                                    }
                                }
                                if (objExtend.has("postUserInfo") && !objExtend.isNull("postUserInfo")) {
                                    JSONObject objectPostUserInfo = objExtend.getJSONObject("postUserInfo");
                                    if (objectPostUserInfo.has("duty") && !objectPostUserInfo.isNull("duty")) {
                                        inside.duty = objectPostUserInfo.getInt("duty");
                                    }
                                    if (objectPostUserInfo.has("userName") && !objectPostUserInfo.isNull("userName")) {
                                        inside.userName = objectPostUserInfo.getString("userName");
                                    }
                                    if (objectPostUserInfo.has("avatar") && !objectPostUserInfo.isNull("avatar")) {
                                        inside.avatar = objectPostUserInfo.getString("avatar");
                                    } else {
                                        inside.avatar = null;
                                    }
                                    if (objectPostUserInfo.has("userMemberLevel") && !objectPostUserInfo.isNull("userMemberLevel")) {
                                        JSONObject objectUserMemberLevel = objectPostUserInfo.getJSONObject("userMemberLevel");
                                        if (objectUserMemberLevel.has("memberIcon") && !objectUserMemberLevel.isNull("memberIcon")) {
                                            inside.memberIcon = objectUserMemberLevel.getString("memberIcon");
                                        } else {
                                            inside.memberIcon = null;
                                        }
                                    } else {
                                        inside.memberIcon = null;
                                    }

                                }
                                if (objExtend.has("commentAmount") && !objExtend.isNull("commentAmount")) {
                                    inside.commentAmount = objExtend.getInt("commentAmount");
                                }
                            }
                            if (objectData.has("shareUrl") && !objectData.isNull("shareUrl")) {
                                inside.shareurl = objectData.getString("shareUrl");
                            }

                            if (objectEvery.has("userId") && !objectEvery.isNull("userId")) {
                                inside.userId = objectEvery.getInt("userId");
                            }
                            if (objectEvery.has("duty") && !objectEvery.isNull("duty")) {
                                inside.duty = objectEvery.getInt("duty");
                            }
//							if (objectEvery.has("PostInfoId")&&!objectEvery.isNull("PostInfoId")) {
//								inside.PostInfoId = objectEvery.getInt("PostInfoId");
//							}
                            if (objectEvery.has("id") && !objectEvery.isNull("id")) {
                                inside.PostInfoId = objectEvery.getInt("id");
                            }
                            if (objectEvery.has("groupId") && !objectEvery.isNull("groupId")) {
                                inside.groupId = objectEvery.getInt("groupId");
                            }
                            if (objectEvery.has("title") && !objectEvery.isNull("title")) {
                                inside.title = objectEvery.getString("title");
                            }

//							if (objectEvery.has("imgs")&&!objectEvery.isNull("imgs")) {
//								JSONArray imgArray = objectEvery.getJSONArray("imgs");
//								if (imgArray.length()>0) {
//									for (int j = 0; j < imgArray.length(); j++) {
//										inside.list.add(imgArray.getString(j));
//									}
//								}
//							}
//							if (objectEvery.has("smallImgs")&&!objectEvery.isNull("smallImgs")) {
//								JSONArray smallImgsArray = objectEvery.getJSONArray("smallImgs");
//								if (smallImgsArray.length()>0) {
//									for (int j = 0; j < smallImgsArray.length(); j++) {
//										inside.smallImgsList.add(smallImgsArray.getString(j));
//									}
//								}
//							}
//							if (objectEvery.has("postUserInfo")&&!objectEvery.isNull("postUserInfo")) {
//								JSONObject objectPostUserInfo = objectEvery.getJSONObject("postUserInfo");
//								if (objectPostUserInfo.has("userName")&&!objectPostUserInfo.isNull("userName")) {
//									inside.userName = objectPostUserInfo.getString("userName");
//								}
//								if (objectPostUserInfo.has("avatar")&&!objectPostUserInfo.isNull("avatar")) {
//									inside.avatar = objectPostUserInfo.getString("avatar");
//								}else{
//									inside.avatar = null;
//								}
//								if (objectPostUserInfo.has("userMemberLevel")&&!objectPostUserInfo.isNull("userMemberLevel")) {
//									JSONObject objectUserMemberLevel = objectPostUserInfo.getJSONObject("userMemberLevel");
//									if (objectUserMemberLevel.has("memberIcon")&&!objectUserMemberLevel.isNull("memberIcon")) {
//										inside.memberIcon = objectUserMemberLevel.getString("memberIcon");
//									}else {
//										inside.memberIcon = null;
//									}
//								}else {
//									inside.memberIcon = null;
//								}
//
//							}
                            listLast.add(inside);
                            list.add(inside);
                        }
                        if (list.size() > 0) {
                            show_no_data.setVisibility(View.GONE);
                        } else {
                            show_no_data.setVisibility(View.VISIBLE);
                        }
                        insideAdapter.notifyDataSetChanged();
                    } else {
                        if (list.size() > 0) {
                            show_no_data.setVisibility(View.GONE);
                        } else {
                            show_no_data.setVisibility(View.VISIBLE);
                        }
                        insideAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                try {
                    pet_circle_inside_listview.onRefreshComplete();
                    mPDialog.closeDialog();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

        private boolean showLayout(JSONObject objectData) {
            boolean isGroupJL = false;
            boolean isGroupDZ = false;
            if (objectData.has("groupJl") && !objectData.isNull("groupJl")) {
                isGroupJL = true;
            } else {
                isGroupJL = false;
            }
            if (objectData.has("groupDz") && !objectData.isNull("groupDz")) {
                isGroupDZ = true;
            } else {
                isGroupDZ = false;
            }
            if (isGroupDZ || isGroupJL) {
                layout_circle_master.setVisibility(View.VISIBLE);
            } else if (!isGroupDZ && !isGroupJL) {
                layout_circle_master.setVisibility(View.GONE);
            }
            if (isGroupJL && isGroupDZ) {
                layout_one_left.setVisibility(View.VISIBLE);
                left_and_right_line.setVisibility(View.VISIBLE);
                layout_one_right.setVisibility(View.VISIBLE);
            } else if (isGroupJL && !isGroupDZ) {
                layout_one_left.setVisibility(View.VISIBLE);
                left_and_right_line.setVisibility(View.GONE);
                layout_one_right.setVisibility(View.GONE);
            } else if (!isGroupJL && isGroupDZ) {
                layout_one_left.setVisibility(View.GONE);
                left_and_right_line.setVisibility(View.GONE);
                layout_one_right.setVisibility(View.VISIBLE);
            }
            return isGroupDZ;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            try {
                pet_circle_inside_listview.onRefreshComplete();
                mPDialog.closeDialog();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    };

    private void initView() {
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);
        tv_titlebar_title.setText("宠圈");
        tv_titlebar_other.setText("发帖");
        tv_titlebar_other.setVisibility(View.VISIBLE);
        tv_titlebar_other.setTextColor(getResources().getColor(R.color.orange));
        pet_circle_inside_listview = (PullToRefreshListView) findViewById(R.id.pet_circle_inside_listview);

        header = LayoutInflater.from(mContext).inflate(R.layout.header_pet_circle_inside, null);
        view_line_isshow = (View) header.findViewById(R.id.view_line_isshow);
        layout_circle_master = (LinearLayout) header.findViewById(R.id.layout_circle_master);
        show_no_data = (LinearLayout) header.findViewById(R.id.show_no_data);
        imageView_pet_inside = (ImageView) header.findViewById(R.id.imageView_pet_inside);
        textview_pet_circle_name = (TextView) header.findViewById(R.id.textview_pet_circle_name);
        textview_pet_circle_detail = (TextView) header.findViewById(R.id.textview_pet_circle_detail);
        textview_pet_circle_follow_num = (TextView) header.findViewById(R.id.textview_pet_circle_follow_num);
        textview_pet_circle_post_num = (TextView) header.findViewById(R.id.textview_pet_circle_post_num);
        textview_petcircle_jl = (TextView) header.findViewById(R.id.textview_petcircle_jl);
        textview_petcircle_dz = (TextView) header.findViewById(R.id.textview_petcircle_dz);
        button_pet_inside_or = (Button) header.findViewById(R.id.button_pet_inside_or);
        rpv_servicedetail_pet = (RollPagerView) header.findViewById(R.id.rpv_servicedetail_pet);
        layout_pet_circle_top_one = (LinearLayout) header.findViewById(R.id.layout_pet_circle_top_one);
        textview_top_tag_one = (TextView) header.findViewById(R.id.textview_top_tag_one);
        textview_top_detail = (BQMMMessageText) header.findViewById(R.id.textview_top_detail);
        imageview_top_right = (ImageView) header.findViewById(R.id.imageview_top_right);
        layout_one_left = (LinearLayout) header.findViewById(R.id.layout_one_left);
        layout_one_right = (LinearLayout) header.findViewById(R.id.layout_one_right);
        left_and_right_line = (View) header.findViewById(R.id.left_and_right_line);
        layout_huodong_one = (LinearLayout) header.findViewById(R.id.layout_huodong_one);
        layout_huodong_two = (LinearLayout) header.findViewById(R.id.layout_huodong_two);
        rpvBanner = (RollPagerView) header.findViewById(R.id.rpv_servicedetail_pet);

        layout_pet_circle_second = (LinearLayout) header.findViewById(R.id.layout_pet_circle_second);
        textview_second_tag_one = (TextView) header.findViewById(R.id.textview_second_tag_one);
        textview_second_detail = (BQMMMessageText) header.findViewById(R.id.textview_second_detail);
        imageview_second_right = (ImageView) header.findViewById(R.id.imageview_second_right);
        imageview_area_leader = (ImageView) header.findViewById(R.id.imageview_area_leader);
        imageview_shop_leader = (ImageView) header.findViewById(R.id.imageview_shop_leader);


        ib_titlebar_back.setOnClickListener(this);
        tv_titlebar_other.setOnClickListener(this);
        imageView_pet_inside.setOnClickListener(this);
        button_pet_inside_or.setOnClickListener(this);
        layout_one_left.setOnClickListener(this);
        layout_one_right.setOnClickListener(this);
        layout_huodong_one.setOnClickListener(this);
        layout_huodong_two.setOnClickListener(this);
        tv_titlebar_title.setOnClickListener(this);
        textview_top_detail.setOnClickListener(this);
        textview_second_detail.setOnClickListener(this);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            // 下拉刷新
            page = 1;
            list.clear();
            insideAdapter.notifyDataSetChanged();
            getData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Global.RESULT_OK) {
            if (requestCode == Global.PET_CIRCLE_TO_POST) {
                // 下拉刷新
                page = 1;
                list.clear();
                insideAdapter.notifyDataSetChanged();
                getData();
            } else if (requestCode == Global.PETCIRCLELIST_TO_IMAGE) {
                index = 1;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.tv_titlebar_other://发帖
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_PetCircle_Post);
                if (isFollowGroup != 1) {
                    if (Utils.checkLogin(insideActivity)) {
                        showBackDialog();
                    } else {
                        intent = new Intent(mContext, LoginNewActivity.class);
                        intent.putExtra("previous", Global.PRE_RECHARGEPAGE_ZF);
                        startActivityForResult(intent, Global.PET_CIRCLE_TO_POST);
                    }
                    return;
                }
                joinCircleAndPost();
                break;
            case R.id.imageView_pet_inside://进入个人中心
//			ToastUtil.showToastShortCenter(mContext, "个人中心");
                break;
            case R.id.button_pet_inside_or://加入圈子
                isJoinCircleDialog = false;
                if (Utils.checkLogin(insideActivity)) {
                    mPDialog.showDialog();
                    CommUtil.followGroup(SharedPreferenceUtil.getInstance(mContext).getString("cellphone", ""), mContext, groupId, followGroup);
                } else {
                    intent = new Intent(mContext, LoginNewActivity.class);
                    intent.putExtra("previous", Global.PRE_RECHARGEPAGE_ZF);
                    startActivityForResult(intent, Global.PET_CIRCLE_TO_POST);
                }
                break;
            case R.id.layout_one_left://区域经理模块
                intent = new Intent(mContext, ADActivity.class);
                intent.putExtra("previous", Global.PET_CIRCLE_TO_H5);
                if (!homepageJl.startsWith("http://") && !homepageJl.startsWith("https://")) {
                    intent.putExtra("url", "http://" + homepageJl);
                } else {
                    intent.putExtra("url", homepageJl);
                }
                startActivity(intent);
                break;
            case R.id.layout_one_right://店长模块
                intent = new Intent(mContext, ADActivity.class);
                intent.putExtra("previous", Global.PET_CIRCLE_TO_H5);
                if (!homepageDZ.startsWith("http://") && !homepageJl.startsWith("https://")) {
                    intent.putExtra("url", "http://" + homepageDZ);
                } else {
                    intent.putExtra("url", homepageDZ);
                }
                startActivity(intent);
                break;
            case R.id.textview_top_detail:
            case R.id.layout_huodong_one://置顶one
                intent = new Intent(mContext, PetCircleInsideDetailActivity.class);
                intent.putExtra("postId", PostInfoIdOne);
                startActivity(intent);
                break;
            case R.id.textview_second_detail:
            case R.id.layout_huodong_two://置顶two
                intent = new Intent(mContext, PetCircleInsideDetailActivity.class);
                intent.putExtra("postId", PostInfoIdTwo);
                startActivity(intent);
                break;
        }
    }

    private void joinCircleAndPost() {
        Intent intent;
        if (Utils.checkLogin(insideActivity)) {
            String userName = spUtil.getString("username", "");
            if (userName.equals("") || TextUtils.isEmpty(userName)) {
                tqDialog();
            } else {
                intent = new Intent(mContext, PetCircleInsidePostActivity.class);
                intent.putExtra("groupId", groupId);
                startActivityForResult(intent, Global.PET_CIRCLE_TO_POST);
            }
        } else {
            intent = new Intent(mContext, LoginNewActivity.class);
            intent.putExtra("previous", Global.PRE_RECHARGEPAGE_ZF);
            startActivityForResult(intent, Global.PET_CIRCLE_TO_POST);
        }
    }

    private AsyncHttpResponseHandler followGroup = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("==-->加入圈子:" + new String(responseBody));
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject objectData = object.getJSONObject("data");
                        if (objectData.has("isFollow") && !objectData.isNull("isFollow")) {
                            int isFollow = objectData.getInt("isFollow");
                            if (isFollow == 1) {
                                isFollowGroup = 1;
                                ToastUtil.showToastShortCenter(insideActivity, getResources().getString(R.string.pet_circle_join_success));
                                //如果没加入执行点击然后已加入
                                button_pet_inside_or.setText("已加入");
                                button_pet_inside_or.setTextColor(getResources().getColor(R.color.aBBBBBB));
                                button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_huise_back);
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.PetCircleFragment");
                                intent.putExtra("index", 1);
                                sendBroadcast(intent);
                                if (isJoinCircleDialog) {
                                    isJoinCircleDialog = false;
                                    joinCircleAndPost();
                                }
                            } else if (isFollow == 2) {
                                isFollowGroup = 2;
                                ToastUtil.showToastShortCenter(insideActivity, getResources().getString(R.string.pet_circle_out_success));
                                button_pet_inside_or.setText("+ 加入");
                                button_pet_inside_or.setTextColor(getResources().getColor(R.color.aD1494F));
                                button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_appoiment_ok);
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.PetCircleFragment");
                                intent.putExtra("index", 1);
                                sendBroadcast(intent);
                            } else {
                                ToastUtil.showToastShortCenter(mContext, "加入圈子失败");
                            }
                        }
                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
                try {
                    mPDialog.closeDialog();
                } catch (NotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            try {
                mPDialog.closeDialog();
            } catch (NotFoundException e1) {
                e1.printStackTrace();
            }
        }

    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		if (index!=1) {
//			// 下拉刷新
//			page = 1;
//			list.clear();
//			insideAdapter.notifyDataSetChanged();
//			getData();
//		}
        int imageHeight = 0;
        int screenWidth = 0;
        screenWidth = Utils.getDisplayMetrics((Activity) mContext)[0];
        imageHeight = (int) (screenWidth / 3.75);
        rpvBanner.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, imageHeight));

    }

    //	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		try {
//			unregisterReceiver(receiver);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        petCircleList.clear();
        listBanner.clear();
        listLast.clear();
        page = 1;
        page = 1;
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    public static boolean isMainActivityTop() {
        ActivityManager manager = (ActivityManager) insideActivity.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(PetCircleInsideActivity.class.getName());
    }

    public static ActivityManager backMan() {
        ActivityManager manager = (ActivityManager) insideActivity.getSystemService(Context.ACTIVITY_SERVICE);
        return manager;

    }

    private void showBackDialog() {
        MDialog mDialog = new MDialog.Builder(insideActivity)
                .setTitle("提示")
                .setType(MDialog.DIALOGTYPE_CONFIRM)
                .setMessage(getResources().getString(R.string.join_circle_can_postorder))
                .setCancelStr("取消")
                .setOKStr("加入")
                .positiveListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (Utils.checkLogin(insideActivity)) {
                            isJoinCircleDialog = true;
                            mPDialog.showDialog();
                            CommUtil.followGroup(SharedPreferenceUtil.getInstance(mContext).getString("cellphone", ""), mContext, groupId, followGroup);
                        } else {
                            Intent intent = new Intent(mContext, LoginNewActivity.class);
//					mContext.startActivity(intent);
                            intent.putExtra("previous", Global.PRE_RECHARGEPAGE_ZF);
                            startActivityForResult(intent, Global.PET_CIRCLE_TO_POST);
                        }

                    }
                }).build();
        mDialog.show();
    }

    private void tqDialog() {
        new UserNameAlertDialog(mContext).builder().setTitle("没昵称我  \"蓝瘦\"")
                .setTextViewHint("请填写昵称").setCloseButton(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setComplaintsButton("保	 存", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.mLogError("==-->点击了保存 0 cellphone:= " + spUtil.getString("cellphone", "0") + " userid:= " + spUtil.getInt("userid", 0) + " getUserName:= " + UserNameAlertDialog.getUserName());
                CommUtil.updateUser(spUtil.getString("cellphone", "0"),
                        Global.getIMEI(mContext), mContext,
                        spUtil.getInt("userid", 0),
                        UserNameAlertDialog.getUserName(), null,
                        updateUser);
            }
        }).show();
    }

    private AsyncHttpResponseHandler updateUser = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            // TODO Auto-generated method stub
            Utils.mLogError("==-->点击了保存 1");
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    ToastUtil.showToastShortCenter(mContext, "创建成功");
                    spUtil.saveString("username", UserNameAlertDialog.getUserName());
                } else {
                    String msg = jsonObject.getString("msg");
                    ToastUtil.showToastShortCenter(mContext, msg);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Utils.mLogError("==-->点击了保存 2 " + e.getMessage());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub

        }

    };

    public static void deltePost(int removePosition) {
        page = 1;
        listLast.clear();
        if (removePosition != -1) {
            list.remove(removePosition);
            ToastUtil.showToastShortCenter(insideActivity, "删除帖子成功");
        }
        double lastSize = list.size() % pageSize;
        double countPage = 0;
        if (lastSize == 0) {//说明该翻页了算出来的页数+1
            countPage = Math.ceil(list.size() / pageSize);
            page = (int) (countPage + 1);
            page += 1;//之前算最后一页翻页判断page -1 这里都得先＋1，记上怕后边忘了
        } else {//上取整就是页数
            countPage = Math.ceil(list.size() / pageSize);
            page = (int) countPage;
            page += 1;
        }
        Utils.mLogError("==-->lastSize 0:= " + lastSize + " countPage:= " + countPage + "  list:= " + list.size());
        for (int i = (int) (list.size() - lastSize); i < list.size(); i++) {
            listLast.add(list.get(i));
        }
        Utils.mLogError("==-->lastSize 1:= " + listLast.size());
        Utils.mLogError("==-->lastSize 2:= " + page);
        insideAdapter.setNotif();
    }

    public static void EvaCountUp(int removePosition) {
        list.get(removePosition).commentAmount = list.get(removePosition).commentAmount += 1;
        insideAdapter.setNotif();
    }
//	private class MyReceiver extends BroadcastReceiver {
//		
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			int indexTag = intent.getIntExtra("indexTag", 0);
//			if (indexTag!=0) {
//				switch (indexTag) {
//				case 1:
//					listLast.clear();
//					Utils.mLogError("==-->indexTag 1");
//					int removePosition = intent.getIntExtra("position", -1);
//					if (removePosition!=-1) {
//						list.remove(removePosition);
//					}
//					double lastSize  = list.size() % pageSize;
//					double countPage = 0 ;
//					if (lastSize > 0) {
//						countPage = Math.ceil(list.size() / pageSize);
//					}
//					Utils.mLogError("==-->lastSize 0:= "+lastSize+" countPage:= "+countPage+"  list:= "+list.size());
//					for (int i = list.size(); i > (int)lastSize;i--) {
//						listLast.add(list.get(i));
//					}
//					Utils.mLogError("==-->lastSize 1:= "+listLast.size());
//					page = (int) countPage;
//					Utils.mLogError("==-->lastSize 2:= "+page);
//					insideAdapter.notifyDataSetChanged();
//					break;
//				}
//			}
//			
//		}
//		
//	}

}

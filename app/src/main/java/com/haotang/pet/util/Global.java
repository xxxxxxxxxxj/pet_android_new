package com.haotang.pet.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import com.haotang.pet.net.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;

public class Global {
    public static final String[] ORDERKEY =
            {"bath", "beauty", "featured", "hotel", "goods", "timeCard", "recharge", "certi", "care", "travel", "seckill", "scard", "mall", "icard"};
    public static final int BOOKINGSERVICEREQUESTCODE_ADDR = 102;
    public static final int ORDERDETAILREQUESTCODE_COUPON = 105;
    public static final int ORDERDETAILREQUESTCODE_NOTE = 106;
    public static final int SERVICEREQUESTCODE_BEAUTICIAN = 110;
    public static final int RESULT_OK = 1000;
    public static final int MY_TO_ADDPET = 1005;
    public static final int ADDPET_TO_PETLIST = 1006;
    public static final int MY_TO_LOGIN = 1008;
    public static final int PETINFO_TO_EDITPET = 1013;
    public static final int ALI_SDK_CHECK_FLAG = 1015;
    public static final int ALI_SDK_PAY_FLAG = 1016;
    public static final int APPOINTMENT_TO_ADDPET = 1017;
    public static final int BATH_TO_ORDERPAY = 1019;
    public static final int PETADD_BACK_PETINFO_BACK_MY = 1022;
    public static final int DELETEPET_TO_UPDATEUSERINFO = 1024;
    public static final int H5_TO_LOGIN = 1025;
    public static final int SERVICEFEATURE_TO_PETLIST = 1027;
    public static final int SERVICEFEATURE_TO_PETLIST_1 = 1028;
    public static final int BEAUTICIAN_TO_TIME = 1029;
    public static final int AD_TO_LOGIN = 1030;
    public static final int BEAUTICIAN_TO_APPOINTMENT = 1032;
    public static final int PRE_MAINFRAGMENT_TO_SERVICEACTIVITY = 2001;
    public static final int PRE_MAINFRAGMENT_TO_BOOKINGSERVICEACTIVITY = 2002;
    public static final int PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY = 2011;
    public static final int PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY = 2012;
    public static final int PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY = 2013;
    public static final int PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY = 2014;
    public static final int PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT = 2015;
    public static final int PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT = 2016;
    public static final int PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY = 2017;
    public static final int PRE_LOGINOUT_TO_BACK_MAINACTIVITY = 2018;
    public static final int PRE_PUSH_TO_LOGIN = 2019;
    public static final int PRE_PUSH_TO_ORDER = 2020;
    public static final int PRE_PUSH_TO_NOSTARTAPP_LOGIN = 2022;
    public static final int PRE_PUSH_TO_ORDER_ORDERDETAILHASOPEN = 2023;
    public static final int PRE_PUSH_TO_NOSTARTAPP_COUPON = 2024;
    public static final int PRE_EVALUATEOVER_BACK_MAIN = 2025;
    public static final int PRE_LOGOUT_TO_MAINACTIVITY = 2026;
    public static final int PRE_BEAUTICIANPRODUCTIONLIST_TO_PRODUCTIONDETAIL = 2028;
    public static final int PRE_BEAUTICIANDETAIL_TO_PRODUCTIONDETAIL = 2029;
    public static final int PRE_RECHARGEPAGE_ZF = 2030;//支付宝
    public static final int PRE_RECHARGEPAGE_WX = 2031;//微信
    public static final int PRE_ORDERDETAIL_TO_UPGRADESERVICE = 2034;
    public static final int PRE_PUSH_TO_EVALUATE = 2035;
    public static final int PRE_ORDER_LIST_TO_MAINACTIVITY = 2036;
    public static final int PRE_ORDERDETAIL_TO_RECHARGE = 2037;
    public static final int PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT = 2039;
    public static final int FOSTERCARE_APPOINTMENT_CHANGEPET = 2041;
    public static final int FOSTERCARE_TO_LOGIN = 2045;
    public static final int ORDER_EVAOVER_TO_ORDERDETAILUPDATE_AND_POPSHOW = 2048;//评价跳转评价完成 评价成功后返回订单详情，并确认是否弹窗发送红包
    public static final int ORDER_TOPAYDETAIL_TOGIVE_MONEY = 2049;//待付款到收银台支付
    public static final int ORDER_OTHER_STATUS_TO_SHOPDETAIL = 2050;//订单详情到店铺/美容师详情
    public static final int ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY = 2051;//办理狗证到收银台支付
    public static final int BINDPHONE_TOLOGIN = 2052;//绑定微信到登录页
    public static final int FLASHACTIVITY_TOLOGIN = 2053;//闪屏页到登录页
    public static final int MAIN_TO_BEAUTICIANLIST = 3000;
    public static final int ORDER_CHANGE_CUSTOMEORPHONE = 3002;
    public static final int ORDER_WAIT_TO_PAY_DETAIL_CHANGE_CUSTOMER = 3003;
    public static final int SWIM_APPOINMENT = 4000;//pre
    public static final int SWIM_DETAIL_ADD_PET = 4004;
    public static final int SWIM_DETAIL_TO_ORDERDETAIL = 4005;
    public static final int SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET = 4007;
    public static final int SWIM_APPOINMENT_TO_WEBVIEW = 4009;
    public static final int SWIM_DETAIL_TO_WEBVIEW = 4010;
    public static final int SWIM_DETAIL_CLICK_ONE_TO_CHANGE_PET = 4011;
    public static final int MAIN_TO_SWIM_DETAIL = 4012;
    public static final int URGENT_TO_ORDERDETAIL = 5000;
    public static final int AD_TO_LOGIN_TO_ORDER = 5003;
    public static final int PET_CIRCLE_TO_H5 = 5555;
    public static final int PET_CIRCLE_TO_POST = 5556;
    public static final int PETCIRCLEINSIDEDETAIL_TO_LOGIN_BACK = 5557;
    public static final int PETCIRCLEINSIDEDETAIL_TO_DIALOG_SHOW = 5558;
    public static final int PETCIRCLELIST_TO_IMAGE = 5559;
    public static final int PAYSUCCESS_TO_MYFRAGMENT = 6001;
    public static final int PUSH_TO_ORDER_ORDER_BEAU_ACCEPT = 7001;
    public static final int PUSH_TO_ORDER_ORDER_NO_BEAU_ACCEPT = 7002;
    public static final int TRAIN_TO_CHOOSE_PET_PRE = 7501;
    public static final int MAIN_TO_TRAIN_DETAIL = 7503;
    public static final int TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET = 7504;
    public static final int CARDS_TO_MAINACTIVITY = 7600;
    public static final int CARDSDETAIL_TO_ORDERPAY = 7700;
    public static final int CARD_CHOOSE_PET = 7702;
    public static final int CARD_NOTPET_CHOOSE_PET = 7703;
    public static final int GROWTH_TO_USERMEMBERFRAGMENT = 8001;
    public static final int UPDATE_TO_ORDERPAY = 8200;
    public static final String MD5_STR = "haotang_jishubu01";
    public static final int EXIT_USER_CODE = 100003;
    public static int WXPAYCODE = -1;
    public static final String APP_ID = "wx965fadef9e22bcb6";//微信appid
    public static final int LIVEDELAYEDGONE = 7006;
    public static final int LOGIN_TO_POSTSELECTIONFRAGMENT = 7008;
    public static final int LOGIN_TO_USERLISTACTIVITY = 7010;
    public static final int MIPCA_TO_ORDERPAY = 7011;
    public static final int MYFRAGMENT_INVITESHARE = 7012;
    public static final int USERMEMBERFRAGMENT_LOGIN = 7013;
    public static final String OfficialWebsite = "http://www.haotang365.com.cn/";
    public static final int RequestCode_UserMember = 7014;
    public static final int SERVICEFEATURE_TO_PETLIST_SHOPLIST = 7016;
    public static final int ORDERDETAIL_TO_BUY_CARD = 7100;
    public static final int UPDATEORDERDETAIL_TO_BUY_CARD = 7101;
    public static final int SERVICE_NEW_TO_LOGIN = 7501;
    public static final int SERVICE_NEW_TO_BUYCARD = 7502;
    public static final int SERVICE_NEW_TO_CHOOSE_SHOPLIST = 7503;
    public static final int SERVICE_NEW_TO_ADD_PET = 7504;
    public static final int SERVICE_NEW_TO_ADD_ADDRESS = 7506;
    public static final int SERVICE_NEW_TO_ADD_ADDRESS_REQUESTCODE = 7507;
    public static final int SERVICE_NEW_TO_CHOOSEADDRESS = 7509;
    public static final int SERVICE_NEW_TO_CHANGE_LINKMAN = 7510;
    public static final int CARDSDETAIL_NEW_TO_BUT_CARD = 7610;
    public static final int BATHBEAY_NEW_TO_ORDERPAY = 7613;
    public static final int APPOINTNEW_TO_BEAUDETAI = 7618;
    public static final int MYCAN_TO_LOGIN = 7622;
    public static final int ORDERDETAILREQUESTCODE_HOMECOUPON = 7631;
    public static final int MALL_ADDRCKGOODSADDRESS = 7700;
    public static final int MALL_SELF_ADDRESS_CLICK = 7701;
    public static final int MALL_SELF_ADDRESS_BOTTOM_CLICK = 7702;
    public static final int MALL_ORDER_ADDRESS = 7703;
    public static final int MALL_ORDER_CHANGE_ADDRESS = 7704;
    public static final int MALL_SEARCH_TORESULT = 7706;
    public static final int ADACTIVITY_SHARE = 7707;
    public static final int COMMODITYDETAIL_LOGIN = 7708;
    public static final int PAYSUCCESSNEWTO_MALLMAINACTIVITY = 7714;
    public static final int ORDERPAY_TO_SETUPPAYPWD = 7715;
    public static final int ORDERPAY_TO_STARTFINGER = 7716;
    public static final int PAYSETTING_TO_SETUPPAYPWD = 7717;
    public static final int SETUPPAYPWD_TO_PAYPWDPROTECTION = 7719;
    public static final int ORDERPAY_TO_SETUPPAYPWD_NEXT = 7720;
    public static final int SHOPLISTADDR_TO_LOGIN = 7721;
    public static final int PAYSUCCESSNEW_TO_ORDER_DETAIL = 7722;
    public static final int ENCYSEARCH_TO_RESULT = 7726;
    public static final int AD_LOGIN_MAIN = 7727;
    public static final int AD_LOGIN_SHOP = 7728;
    public static final int PAY_SUCCESS = 106017;
    public static final int COMMONADDRESS_ADDADDRESS = 8000;
    public static final int COMMONADDRESS_EDIT_ADDADDRESS = 8001;
    public static final int APPOINTMENT_TO_ITEMLIST = 7731;
    public static final int APPOINTMENT_TO_SWITCHSERVICE = 7732;
    public static final int SWITCHSERVICE_TO_ITEMDETAIL = 7733;
    public static final int AVAILABLEWORKERLIST_TO_WORKERDETAIL = 7734;
    public static final int UNAVAILABLEWORKERLIST_TO_WORKERDETAIL = 7735;
    public static final int ITEMLIST_TO_OVERTIME = 7736;
    public static final int ITEMLIST_TO_ITEMDETAIL = 7737;
    public static final int ITEMDETAIL_TO_OVERTIME = 7738;
    public static final int SWITCHSERVICE_TO_OVERTIME = 7739;
    public static final int SHOP_DETAIL_TO_BEAUDETAIL = 7740;
    public static final int MY_CUSTOMERPET = 7741;
    public static final int SWIM_TO_CUSTOMERPET = 7744;
    public static final int PRE_PUSH_TO_EVALUATE_XIMEI = 7755;
    public static final int WEBVIEW_TO_ADDRESS = 7757;
    public static final int FOSORDERCONFIRM_TO_MYCARD = 7758;
    public static final int FOSNEW_TO_PAYSUCCESS = 7759;
    public static final int UPDATENEW_TO_PAYSUCCESS = 7760;
    public static final int MALLCONFIRM_TO_MYCARD = 7762;
    public static final int BUYCARD_TO_CHOOSEPET = 7763;
    public static final int PAYPWD_TO_VERIFCODE = 7764;//设置支付密码
    public static final int REPLACEPHONE_TO_VERIFCODE = 7765;//替换手机号
    public static final int SETREPLACEPHONE_TO_VERIFCODE = 7766;//重新设置手机号
    public static final String BaiDuMapPackageName = "com.baidu.BaiduMap";
    public static final String GaoDeMapPackageName = "com.autonavi.minimap";
    public static final int NO_SET_PASSWORD = 0;//没有设置密码
    public static final int ALREADY_SET_PASSWORD = 1;//已经设置密码

    public static final int FIRST_SET_PASSWORD = 0;//初次设置密码
    public static final int FORGET_PASSWORD = 1;//忘记密码
    public static final int UPDATE_PASSWORD = 2;//修改密码
    public static final int VERIFY_PASSWORD = 3;//校验设置密码

    public static final int SOURCE_MALLSEARCH = 1;//从商城搜索
    public static final int SOURCE_MALLCLASSONE = 2;//从商城首页专题一进入
    public static final int SOURCE_MALLCLASSTwo = 3;//从商城首页专题二进入
    public static final int SOURCE_MALLCLASSThree = 4;//从商城首页专题三进入
    public static final int SOURCE_MALLCLASSIFY = 5;//从商城分类进入


    public static int ANIM_COVER_FROM_LEFT() {
        return 0;
    }

    public static int ANIM_COVER_FROM_RIGHT() {
        return 1;
    }

    public static String ANIM_DIRECTION() {
        return "anim_direction";
    }

    /**
     * 获取IMEI码
     *
     * @param mContext
     * @return
     */
    public static String getIMEI(Context mContext) {
        return GetDeviceId.readDeviceID(mContext);
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phone
     */
    public static void cellPhone(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public static String getCurrentVersion(Context context) {
        String curVersion = "0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);// getPackageName()是你当前类的包名，0代表是获取版本信息
            curVersion = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curVersion;
    }

    public static String encodeWithBase64(Bitmap bitmap) {
        if (bitmap == null)
            return "";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);//在保证图片尽量不失真的情况下，减少图片上传,下载所需要的流量
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static void savePushID(Context context, String cid) {
        SharedPreferenceUtil.getInstance(context).saveString("cid", cid);
    }

    public static String getPushID(Context context) {
        return SharedPreferenceUtil.getInstance(context).getString("cid", "");
    }

    /**
     * 统计事件id
     *
     * @author Administrator
     */
    public static final class UmengEventID {
        public static final String click_HomePage_BathSelect = "click_HomePage_BathSelect";//首页洗澡入口;//1
        public static final String click_HomePage_BeautySelect = "click_HomePage_BeautySelect";//首页美容入口;//1
        public static final String click_HomePage_FosterCareSelect = "click_HomePage_FosterCareSelect";//首页寄养入口;//1
        public static final String click_HomePage_CharacteristicSelect = "click_HomePage_CharacteristicSelect";//首页特色服务入口;//0
        public static final String click_HomePage_DogcardSelect = "click_HomePage_DogcardSelect";//首页犬证入口;//1
        public static final String click_HomePage_Headlines = "click_HomePage_Headlines";//首页宠物家头条;//1
        public static final String click_HomePage_PopularList = "click_HomePage_PopularList";//热门美容师-进入列表页;//1
        public static final String click_HomePage_PopularSelect = "click_HomePage_PopularSelect";//热门美容师-进入推荐美容师主页;//1
        public static final String click_HomePage_PetRingList = "click_HomePage_PetRingList";//什么值得看-进入宠圈;//1
        public static final String click_HomePage_PetRingDetails = "click_HomePage_PetRingDetails";//什么值得看-点击进入帖子详情;//1
        public static final String click_HomePage_CharacteristicDetails = "click_HomePage_CharacteristicDetails";//特色服务-进入服务预约页;//1
        public static final String click_HomePage_Hospital = "click_HomePage_Hospital";//推荐医院-进入医院详情;//1
        public static final String click_HomePage_Encyclopedia = "click_HomePage_Encyclopedia";//宠物百科-进入百科详情;//1
        public static final String click_Order_AddRemarks = "click_Order_AddRemarks";//添加备注;//1
        public static final String click_Order_SelectCoupon = "click_Order_SelectCoupon";//选择优惠券列表入口;//1
        public static final String click_My_MyBalance = "click_My_MyBalance";//我的余额;//1
        public static final String click_My_DogCard = "click_My_DogCard";//犬证办理;//1
        public static final String click_My_FosterLive = "click_My_FosterLive";//寄养直播;//1
        public static final String click_My_MyCoupon = "click_My_MyCoupon";//我的优惠券;//1
        public static final String click_My_InvitationCourtesy = "click_My_InvitationCourtesy";//邀请有礼;//1
        public static final String click_My_CommonAddress = "click_My_CommonAddress";//常用地址;//1
        public static final String click_My_MyEvaluation = "click_My_MyEvaluation";//我的评价;//1
        public static final String click_PetHome_DogCard = "click_PetHome_DogCard";//宠物主页犬证办理;//1
        public static final String click_PetHome_EditPet = "click_PetHome_EditPet";//编辑资料;//1
        public static final String click_PetHome_BathSelect = "click_PetHome_BathSelect";//宠物主页洗澡入口;//1
        public static final String click_PetHome_BeautySelect = "click_PetHome_BeautySelect";//宠物主页美容入口;//1
        public static final String click_PetHome_FosterSelect = "click_PetHome_FosterSelect";//宠物主页寄养入口;//1
        public static final String click_Select_Whole = "click_Select_Whole";//宠圈精选全部标签;//1
        public static final String click_Select_Selected = "click_Select_Selected";//宠圈精选关注标签;//1
        public static final String click_Select_Video = "click_Select_Video";//宠圈精选视频标签;//1
        public static final String click_Select_Homepage = "click_Select_Homepage";//用户个人主页入口;//1
        public static final String click_Select_Details = "click_Select_Details";//宠圈精选帖子详情;//1
        public static final String click_Select_Comment = "click_Select_Comment";//宠圈精选评论;//1
        public static final String click_Select_Share = "click_Select_Share";//宠圈精选分享;//1
        public static final String click_PetCircle = "click_PetCircle";//宠圈圈子入口;//1
        public static final String click_PetCircle_Details = "click_PetCircle_Details";//宠圈圈子帖子详情;//1
        public static final String click_PetCircle_Post = "click_PetCircle_Post";//宠圈圈子发帖;//1
        public static final String click_PetCircle_Comment = "click_PetCircle_Comment";//评论-圈子内;//1
        public static final String click_PetCircle_Share = "click_PetCircle_Share";//分享-圈子内;//1
        public static final String click_CancelOrder = "click_CancelOrder";//取消订单;//1
        public static final String click_ModifyOrder = "click_ModifyOrder";//修改订单;//1
        public static final String click_HomePage_Banner1 = "click_HomePage_Banner1";//首页banner1;//1
        public static final String click_HomePage_Recommend = "click_HomePage_Recommend";//个性化推荐;//1
        public static final String click_HomePage_PresentDynamics = "click_HomePage_PresentDynamics";//此刻动态;//1
        public static final String click_HomePage_Banner2 = "click_HomePage_Banner2";//首页banner2;//1
        public static final String click_Return = "click_Return";//返回修改按钮;//1
        public static final String click_HomePage_nav1 = "click_HomePage_navigation1";//底部导航点击
        public static final String click_HomePage_nav2 = "click_HomePage_navigation2";//底部导航点击
        public static final String click_HomePage_nav3 = "click_HomePage_navigation3";//底部导航点击
        public static final String click_HomePage_nav4 = "click_HomePage_navigation4";//底部导航点击
        public static final String click_HomePage_icon1 = "click_HomePage_top_icon1";//首页顶部icon1点击
        public static final String click_HomePage_icon2 = "click_HomePage_top_icon2";//首页顶部icon2点击
        public static final String click_HomePage_icon3 = "click_HomePage_top_icon3";//首页顶部icon3点击
        public static final String click_HomePage_icon4 = "click_HomePage_top_icon4";//首页顶部icon4点击
        public static final String click_HomePage_icon5 = "click_HomePage_top_icon5";//首页顶部icon5点击
        public static final String click_HomePage_icon6 = "click_HomePage_top_icon6";//首页顶部icon6点击
        public static final String click_HomePage_icon7 = "click_HomePage_top_icon7";//首页顶部icon7点击
        public static final String click_HomePage_icon8 = "click_HomePage_top_icon8";//首页顶部icon8点击
        public static final String click_HomePage_MiddleIcon1 = "click_HomePage_between_icon1";//首页中部图标点击
        public static final String click_HomePage_MiddleIcon2 = "click_HomePage_between_icon2";
        public static final String click_HomePage_MiddleIcon3 = "click_HomePage_between_icon3";
        public static final String click_HomePage_MiddleIcon4 = "click_HomePage_between_icon4";
        public static final String click_MallFrag_icon1 = "click_ShoppingMall_icon1";//商城顶部icon点击
        public static final String click_MallFrag_icon2 = "click_ShoppingMall_icon2";//商城顶部icon点击
        public static final String click_MallFrag_icon3 = "click_ShoppingMall_icon3";//商城顶部icon点击
        public static final String click_MallFrag_icon4 = "click_ShoppingMall_icon4";//商城顶部icon点击
        public static final String click_MallFrag_icon5 = "click_ShoppingMall_icon5";//商城顶部icon点击
        public static final String click_MallFrag_Search = "click_ShoppingMall_search";//商城点击搜索
        public static final String click_MallFrag_ChangePet = "click_ShoppingMall_switch";//商城切换宠物类别
        public static final String click_MallFrag_BannerOne = "click_ShoppingMall_banner1";//商城首页banner1点击
        public static final String click_MallFrag_BannerTwo = "click_ShoppingMall_banner2";//商城首页banner2点击
        public static final String click_MallFrag_BannerThree = "click_ShoppingMall_banner3";//商城首页banner3点击
        public static final String click_MallFrag_BannerFour = "click_ShoppingMall_banner4";//商城首页banner4点击
        public static final String click_HomePage_FosterCare = "click_HomePage_Foster";//首页点击寄养
        public static final String click_MallPage_HomePop = "click_ShoppingMall_Popup";//点击商城弹框
        public static final String click_HomePage_HomePop = "click_HomePage_Popup";//点击首页弹框
        public static final String click_MallSearch_ToCart = "click_ShoppingMall_details_addtocart2";//商品详情点击加入购物车搜索来源
        public static final String click_MallSearch_ToBuy = "click_ShoppingMall_details_Popup_Immediatepurchase2";//商品详情弹窗点击立即购买搜索来源
        public static final String click_MallSearch_ToAddCart = "click_ShoppingMall_details_Popup_addtocart2";//商品详情弹窗点击加入购物车搜索来源
        public static final String click_MallSearch_ToPay = "click_ShoppingMall_Topay2";//商城确认订单点击去付款搜索来源
        public static final String click_MallSearch_PaySuccess = "click_MallSearch_ToPay";//商城支付成功页搜索来源
        public static final String click_MallFrag_ClassOne = "click_ShoppingMall_Thematiccommodities1";//首页商品专题一
        public static final String click_ClassOne_ToCart = "click_ShoppingMall_details_addtocart3";//商品详情点击加入购物车专题商品1来源
        public static final String click_ClassOne_AddCart = "click_ShoppingMall_details_Popup_addtocart3";//商品详情弹窗点击加入购物车专题商品1来源
        public static final String click_ClassOne_ToBuy = "click_ShoppingMall_details_Popup_Immediatepurchase3";//商品详情弹窗点击立即购买专题商品1来源
        public static final String click_ClassOne_ToPay = "click_ShoppingMall_Topay3";//商城确认订单点击去付款专题商品1来源
        public static final String click_ClassOne_PaySuccess = "click_ShoppingMall_SuccessfulPayment3";//商城支付成功页专题商品1来源
        public static final String click_MallFrag_ClassTwo = "click_ShoppingMall_Thematiccommodities2";//首页商品专题二
        public static final String click_ClassTwo_ToCart = "click_ShoppingMall_details_addtocart4";//商品详情点击加入购物车专题商品2来源
        public static final String click_ClassTwo_AddCart = "click_ShoppingMall_details_Popup_addtocart4";//商品详情弹窗点击加入购物车专题商品2来源
        public static final String click_ClassTwo_ToBuy = "click_ShoppingMall_details_Popup_Immediatepurchase4";//商品详情弹窗点击立即购买专题商品2来源
        public static final String click_ClassTwo_ToPay = "click_ShoppingMall_Topay4";//商城确认订单点击去付款专题商品2来源
        public static final String click_ClassTwo_PaySuccess = "click_ShoppingMall_SuccessfulPayment4";//商城支付成功页专题商品2来源
        public static final String click_MallFrag_ClassThree = "click_ShoppingMall_Thematiccommodities3";//首页商品专题三
        public static final String click_ClassThree_ToCart = "click_ShoppingMall_details_addtocart5";//商品详情点击加入购物车专题商品3来源
        public static final String click_ClassThree_AddCart = "click_ShoppingMall_details_Popup_addtocart5";//商品详情弹窗点击加入购物车专题商品3来源
        public static final String click_ClassThree_ToBuy = "click_ShoppingMall_details_Popup_Immediatepurchase5";//商品详情弹窗点击加入购物车专题商品3来源
        public static final String click_ClassThree_ToPay = "click_ShoppingMall_Topay5";//商城确认订单点击去付款专题商品3来源
        public static final String click_ClassThree_PaySuccess = "click_ShoppingMall_SuccessfulPayment5";//商城支付成功页专题商品3来源
        public static final String click_Classify_ToDetail = "click_ShoppingMall_Classificationlist1";//商城分类列表点击商品
        public static final String click_Classify_ToCart = "click_ShoppingMall_details_addtocart1";//商品详情点击加入购物车分类来源
        public static final String click_Classify_AddCart = "click_ShoppingMall_details_Popup_addtocart1";//商品详情弹窗点击加入购物车分类来源
        public static final String click_Classify_ToBuy = "click_ShoppingMall_details_Popup_Immediatepurchase1";//商品详情弹窗点击立即购买分类来源
        public static final String click_Classify_ToPay = "click_ShoppingMall_Topay1";//商城确认订单点击去付款分类来源
        public static final String click_Classify_PaySuccess = "click_ShoppingMall_SuccessfulPayment1";//商城支付成功页分类来源
    }

    /**
     * 服务器统计事件id
     *
     * @author Administrator
     */
    public static final class ServerEventID {
        //个人中心界面
        public static final String choose_myself_page = "97";//个人中心页面id
        public static final String click_myinvite_self = "99";//我的邀请有礼事件id
        public static final String click_mydogcard_self = "98";//我的犬证事件id
        //宠物家首页
        public static final String choose_main_page = "75";//宠物家首页
        public static final String click_viparea_main = "87";//宠物家首页vip专区事件id
        public static final String click_mymoney_main = "86";//宠物家首页我的余额事件id
        public static final String click_mydummy_main = "85";//宠物家首页我的罐头币事件id
        public static final String click_mypetnotice_main = "84";//宠物家首页宠物提醒去看看事件id
        public static final String click_nowmoment_main = "83";//宠物家首页此刻动态去看看事件id
        public static final String click_one_main = "96";//宠物家首页九宫格1事件id
        public static final String click_two_main = "95";//宠物家首页九宫格2事件id
        public static final String click_thr_main = "94";//宠物家首页九宫格3事件id
        public static final String click_four_main = "93";//宠物家首页九宫格4事件id
        public static final String click_five_main = "92";//宠物家首页九宫格5事件id
        public static final String click_six_main = "91";//宠物家首页九宫格6事件id
        public static final String click_seven_main = "90";//宠物家首页九宫格7事件id
        public static final String click_eight_main = "89";//宠物家首页九宫格8事件id
        public static final String click_nine_main = "88";//宠物家首页九宫格9事件id
        public static final String click_vipprivilege_main = "82";//宠物家首页vip特权专区事件id
        public static final String click_middleoperateone_main = "81";//宠物家首页中部运营位1事件id  左边
        public static final String click_middleoperatetwo_main = "80";//宠物家首页中部运营位2事件id  右边
        public static final String click_petcircle_main = "79";//宠物家首页宠圈事件id
        public static final String click_petselect_main = "78";//宠物家首页宠圈精选事件id
        public static final String click_recommendhospital_main = "77";//宠物家首页推荐医院事件id
        public static final String click_petencyclopedias_main = "76";//宠物家首页宠物百科事件id
        //商城界面
        public static final String choose_shopmall_page = "103";//宠物家商城界面
        public static final String click_shoppingcart_shopmall = "104";//宠物家商城购物车
        public static final String click_search_shopmall = "105";//宠物家商城搜索框
        //宠圈
        public static final String choose_petcircle_page = "106";//宠物家宠圈界面
        public static final String click_petselected_post = "110";//宠物家精选发帖按钮
        public static final String click_petcircle_tab = "111";//宠物家宠圈tab顶部
        //支付界面
        public static final String choose_pay_page = "107";//宠物家支付界面
        public static final String click_balanceisnulltorecharge_pay = "109";//宠物家余额不足去充值
        public static final String goto_service_detail_page = "179";//进入服务详情页
        public static final String click_service_detail_back = "180";//服务详情点击返回
        public static final String click_goshop = "181";//点击到店
        public static final String click_gohome = "182";//点击上门
        public static final String click_appointment_back = "183";//预约服务点击返回
        public static final String click_goto_order = "184";//点击去下单
        public static final String click_orderpay_back = "187";//收银台点击返回
        public static final String click_comfirm_pay = "188";//点击确认支付
        public static final String click_default_worker = "191";//点击默认推荐美容师
        public static final String typeid_149 = "149";//typeid
        //打赏
        public static final String typeid_216 = "216";
        public static final String click_gratuity_servicedone = "220";
        public static final String click_gratuity_waiteservice = "221";
        public static final String click_gratuity_overevaluate = "222";
        public static final String click_gratuity_inservice = "224";
    }

    public static void ServerEvent(Activity context, String typeId, String activityId) {
        CommUtil.logcountAdd(context, typeId, activityId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}

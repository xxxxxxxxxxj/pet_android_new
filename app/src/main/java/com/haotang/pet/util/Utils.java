package com.haotang.pet.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;
import com.haotang.pet.ADActivity;
import com.haotang.pet.CashbackAmountActivity;
import com.haotang.pet.CharacteristicServiceActivity;
import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.FosterHomeActivity;
import com.haotang.pet.GiftCardDetailActivity;
import com.haotang.pet.GiftCardListActivity;
import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.MainAcPage;
import com.haotang.pet.MainActivity;
import com.haotang.pet.MainToBeauList;
import com.haotang.pet.MemberUpgradeActivity;
import com.haotang.pet.MyCanActivity;
import com.haotang.pet.MyCardActivity;
import com.haotang.pet.MyCouponNewActivity;
import com.haotang.pet.NewbieTaskActivity;
import com.haotang.pet.PetAddActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServiceNewActivity;
import com.haotang.pet.encyclopedias.activity.EncyclopediasActivity;
import com.haotang.pet.encyclopedias.activity.EncyclopediasDetail;
import com.haotang.pet.entity.ActivityPage;
import com.haotang.pet.entity.MDate;
import com.haotang.pet.entity.ShareInfo;
import com.haotang.pet.entity.StyleAndColorBean;
import com.haotang.pet.mall.MallToListActivity;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.AlertDialogNavAndPostVertical;
import com.haotang.pet.view.CardDescDialog;
import com.haotang.pet.view.GlideImageLoader;
import com.haotang.pet.view.WeChatQrCodeDialog;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.melink.baseframe.utils.StringUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import com.ta.utdid2.android.utils.StringUtils;

public class Utils {
    public static boolean isLog = false;
    private static String log_tag = "ht";

    public static void mLogError(String msg) {
        if (isLog)
            Log.e(log_tag, msg);
    }

    public static void mLog(String msg) {
        if (isLog)
            Log.i(log_tag, msg);
    }

    public static void mLog_d(String msg) {
        if (isLog)
            Log.d(log_tag, msg);
    }

    public static void mLog_v(String msg) {
        if (isLog)
            Log.v(log_tag, msg);
    }

    public static void mLog_w(String msg) {
        if (isLog)
            Log.w(log_tag, msg);
    }

    /**
     * 是否WIFI联网
     *
     * @param context
     * @return
     */
    public static boolean checkWIFI(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (null != networkInfo) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType())
                return true;
        }
        return false;
        // String type = networkInfo.getTypeName();
        // return "WIFI".equalsIgnoreCase(type);
    }

    /**
     * 是否网络可用
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.isAvailable()) {
            return true;
        }
        return false;
        // String type = networkInfo.getTypeName();
        // return "WIFI".equalsIgnoreCase(type);
    }

    /**
     * 获取手机屏幕的宽高
     *
     * @param activity
     * @return
     */
    public static int[] getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 把毫秒为单位的时间格式化为yyyyMMddHHmmss
     *
     * @param time
     * @return
     */
    public static String formatDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date(time));
        if (date == null || "".equalsIgnoreCase(date))
            date = "00000000000000";
        return date;
    }

    /**
     * 把毫秒为单位的时间格式化为0000-00-00 00:00:00
     *
     * @param time
     * @return
     */
    public static String formatDateAll(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(time));
        if (date == null || "".equalsIgnoreCase(date))
            date = "0000-00-00 00:00:00";
        return date;
    }

    /**
     * 把以毫秒为单位的时间格式化为0000-00-00
     *
     * @param time
     * @return
     */
    public static String formatDateShort(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(time));
        if (date == null || "".equalsIgnoreCase(date))
            date = "0000-00-00";
        return date;
    }

    /**
     * 使double类型保留points位小数
     *
     * @param num
     * @return
     */
    public static double formatDouble(double num, int points) {
        BigDecimal bigDecimal = new BigDecimal(new Double(num).toString());
        return bigDecimal.setScale(points, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * @param num
     * @return
     */
    public static String formatDouble(double num, String pon) {
        DecimalFormat df = new DecimalFormat(pon);
        return df.format(num);
    }

    /**
     * double类型转换为int类型
     *
     * @param num
     * @return
     */
    public static int formatDouble(double num) {
        return Integer.parseInt(new java.text.DecimalFormat("0").format(num));
    }

    /**
     * 生成一个小于Max的随机数
     *
     * @param max
     * @return
     */
    public static int getRandom(int max) {
        Random rd = new Random();
        return rd.nextInt(max);
    }

    /**
     * 把数据写入sd卡
     *
     * @param log      数据
     * @param filename 文件名
     * @param time     时间，系统 时间，单位毫秒
     */
    public static void writeToSDCard(String log, String filename, long time) {

        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        File dir = new File(path, "haotang");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File guiji = new File(dir, filename);
        try {
            FileWriter fw = new FileWriter(guiji, true);
            fw.append(formatDate(time) + " ::");
            fw.append(log + "\r\n");
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getHaoTangSdcardPath() {
        if (!FileSizeUtil.externalMemoryAvailable()) {
            return "";
        }

        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        File dir = new File(path, "haotang");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 把数据写入sd卡
     */
    public static boolean saveDataToSdcard(String filename, byte[] data) {
        boolean flag = false;
        String filePath = getHaoTangSdcardPath();

        if (TextUtils.isEmpty(filePath)) {
            return flag;
        }

        File file = new File(filePath, filename);

        if (file.exists()) {
            deleteFile(file);
            mLogError("ah  deleteFile Success ==  "+file.getAbsolutePath());
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
        return flag;
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        deleteFile(file);
    }

    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

    /**
     * 获取应用的文件夹
     *
     * @return
     */
    public static String getPetPath(Context context) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), ".pet");
        } else {
            file = context.getCacheDir();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 计算球面上两个点的距离
     *
     * @param lat1
     * @param lat2
     * @param lon1
     * @param lon2
     * @return
     */
    public static double getDistatce(double lat1, double lat2, double lon1,
                                     double lon2) {
        double R = 6371;
        double distance = 0.0;
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1 * Math.PI / 180)
                * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
        return distance;
    }

    /**
     * 格式化double类型
     *
     * @param pattern 如#0.00为保留两位小数
     * @param decimal
     * @return
     */
    // 格式化里程和费用
    public static String formatDecimal(String pattern, double decimal) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(pattern);
        return df.format(decimal);
    }

    /**
     * 为double类型保留两位小数
     *
     * @param decimal
     * @return
     */
    // 格式化里程和费用
    public static String formatDecimal(double decimal) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("#0.00");
        return df.format(decimal);

    }

    /**
     * double类型如果小数点后为零显示整数否则保留 返回String
     *
     * @param num
     * @return
     */
    public static String doubleTrans(double num) {
        String number1 = String.format("%.2f", num);//只保留小数点后6位
        double number2 = Double.parseDouble(number1);//類型轉換
        if (Math.round(number2) - number2 == 0) {
            return String.valueOf((long) number2);
        }
        return String.valueOf(number2);
    }

    /**
     * 把毫秒为单位的时间格式化为时分秒
     *
     * @param time
     * @return
     */
    // 格式化时间格式
    public static String formatTime(long time) {
        long totalss = time / 1000;
        long timess = totalss % 60;
        long totalmm = totalss / 60;
        long timemm = totalmm % 60;
        long totalhh = totalmm / 60;
        long timehh = totalhh % 60;
        StringBuffer sb = new StringBuffer();
        if (timehh < 10) {
            sb.append("0");
        }
        sb.append(timehh);
        sb.append(":");
        if (timemm < 10) {
            sb.append("0");
        }
        sb.append(timemm);
        sb.append(":");
        if (timess < 10) {
            sb.append("0");
        }
        sb.append(timess);
        return sb.toString();
    }

    /**
     * 把毫秒为单位的时间格式化为时分秒
     *
     * @param time
     * @return
     */
    // 格式化时间格式
    public static String formatTimeFM(long time) {
        long totalss = time / 1000;
        long timess = totalss % 60;
        long totalmm = totalss / 60;
        long timemm = totalmm % 60;
        long totalhh = totalmm / 60;
        long timehh = totalhh % 60;
        StringBuffer sb = new StringBuffer();
        if (timehh < 10) {
            // sb.append("0");
        }
        // sb.append(timehh);
        // sb.append(":");
        if (timemm < 10) {
            sb.append("0");
        }
        sb.append(timemm);
        sb.append(":");
        if (timess < 10) {
            sb.append("0");
        }
        sb.append(timess);
        return sb.toString();
    }

    /**
     * 格式化以分钟为单位的时间，转化为小时：分钟
     *
     * @param minutestr
     * @return
     */
    public static String formatMinutesToHour(String minutestr) {
        int minutes = Integer.parseInt(minutestr);
        int minute = minutes % 60;
        int hour = minutes / 60;
        if (0 == hour) {
            return minute + "分钟";
        } else {
            return hour + "小时" + minute + "分钟";
        }
    }

    // 格式化时间格式
    public static long TimeToMinutes(long time) {
        long totalss = time / 1000;
        long totalmm = totalss / 60;
        // if(totalss % 60 > 0){
        // totalmm += 1;
        // }
        return totalmm;
    }

    /**
     * 转换文件大小格式
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        // if (0l == fileS)
        // return "";
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS <= 0) {
            fileSizeString = "0B";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 创建并获取文件夹的路径
     *
     * @param context
     * @return
     */
    public static String getDir(Context context) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile(), "pet");
        } else {
            file = context.getCacheDir();
        }
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    /**
     * 创建精度条对话框
     *
     * @param ctx
     * @param info
     * @return
     */
    public static ProgressDialog createProcessDialog(Context ctx, String info) {
        ProgressDialog processDialog = new ProgressDialog(ctx);
        processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processDialog.setTitle("");
        processDialog.setMessage(info);
        processDialog.setIcon(android.R.drawable.ic_dialog_map);
        processDialog.setIndeterminate(false);
        processDialog.setCancelable(true);
        processDialog.setCanceledOnTouchOutside(false);
        processDialog.show();
        return processDialog;

    }

    /**
     * 吐司
     *
     * @param context
     * @param str
     */
    public static void showTaost(Context context, String str) {
        // Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        // toast.setGravity(Gravity.CENTER, 0, 0);
        // toast.show();
        ToastUtil.showToastShort(context, str);
    }

    /**
     * 电话验证
     *
     * @param
     * @return
     */
    public static boolean checkPhone(Context context, EditText phone_et) {
        String phone = phone_et.getText().toString().trim().replace(" ", "");
        boolean isAvail = isNumeric(phone);
        if ("".equals(phone.trim())) {
            showTaost(context, "请输入您的手机号码");
            phone_et.requestFocus();
            phone_et.setFocusableInTouchMode(true);
            return false;
        }
        if (!isAvail) {
            showTaost(context, "请输入正确的手机号码");
            phone_et.requestFocus();
            phone_et.setFocusableInTouchMode(true);
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        str.trim().replace(" ", "");
        if (str.length() != 11) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPhone1(Context context, EditText phone_et) {
        String phone = phone_et.getText().toString().trim().replace(" ", "");
        boolean isAvail = isNumeric(phone);
        if ("".equals(phone.trim())) {
            phone_et.requestFocus();
            phone_et.setFocusableInTouchMode(true);
            return false;
        }
        if (!isAvail) {
            phone_et.requestFocus();
            phone_et.setFocusableInTouchMode(true);
            return false;
        }
        return true;
    }

    /**
     * 邮箱验证
     *
     * @param context
     * @param email_et
     * @return
     */
    public static boolean checkEmail(Context context, EditText email_et) {
        String emailPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        String email = email_et.getText().toString().trim();
        boolean isAvail = startCheck(emailPattern, email);

        if (!isAvail) {
            showTaost(context, "邮箱格式不正确，请重新输入");
            email_et.requestFocus();
            email_et.setFocusableInTouchMode(true);
            return false;
        }
        if ("".equals(email.trim())) {
            showTaost(context, "邮箱为空，请重新输入");
            email_et.requestFocus();
            email_et.setFocusableInTouchMode(true);
            return false;
        }
        if (email.length() > 50) {
            showTaost(context, "邮箱长度不能大于50，请重新输入");
            email_et.requestFocus();
            email_et.setFocusableInTouchMode(true);
            return false;
        }
        return true;
    }

    /**
     * 正则规则验证
     *
     * @param reg
     * @param string
     * @return
     */
    public static boolean startCheck(String reg, String string) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * app是否正在运行
     *
     * @param context
     * @return
     */
    public static boolean isAppAvailable(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskinfos = am.getRunningTasks(20);
        boolean result = false;
        for (int i = 0; i < taskinfos.size(); i++) {
            if (context.getPackageName().equals(
                    taskinfos.get(i).topActivity.getPackageName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static String getIPAddress(Context context) {
        WifiManager manager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            return getWifiAddress(context);
        }
        return getLocalIPAddress();

    }

    // 获取wifi地址
    public static String getWifiAddress(Context context) {
        WifiManager manager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (!manager.isWifiEnabled()) {
            manager.setWifiEnabled(true);
        }
        WifiInfo info = manager.getConnectionInfo();
        int ip = info.getIpAddress();
        return ipToString(ip);
    }

    public static String ipToString(int ip) {

        return (ip & 0xFF) + "." +

                ((ip >> 8) & 0xFF) + "." +

                ((ip >> 16) & 0xFF) + "." +

                (ip >> 24 & 0xFF);
    }

    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces(); networkInterfaces
                         .hasMoreElements(); ) {
                NetworkInterface networkInterface = networkInterfaces
                        .nextElement();
                for (Enumeration<InetAddress> addresses = networkInterface
                        .getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress()
                            && (address instanceof Inet4Address)) {
                        return address.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public static Bitmap getbitmap(String imageFile, int length) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile, opts);
        int ins = computeSampleSize(opts, -1, length);
        opts.inSampleSize = ins;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inJustDecodeBounds = false;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
            return bmp;
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static String creatfile(Context pContext, Bitmap bm, String filename) {
        if (bm == null) {
            return "";
        }
        File f = new File(getSDCardPath(pContext) + "/" + filename + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(f);
            if (bm.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static String getSDCardPath(Context pContext) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.jiuyi160_c";
            File PathDir = new File(path);
            if (!PathDir.exists()) {
                PathDir.mkdirs();
            }
            return path;
        } else {
            return pContext.getCacheDir().getAbsolutePath();
        }
    }

    @SuppressLint({"NewApi", "InlinedApi"})
    public static Bitmap getxtsldraw(Context c, String file) {
        File f = new File(file);
        Bitmap drawable = null;
        if (f.length() / 1024 < 100) {
            drawable = BitmapFactory.decodeFile(file);
        } else {
            Cursor cursor = c.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + " like ?",
                    new String[]{"%" + file}, null);
            if (cursor == null || cursor.getCount() == 0) {
                drawable = getbitmap(file, 720 * 1280);
            } else {
                if (cursor.moveToFirst()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    String videoId = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media._ID));
                    long videoIdLong = Long.parseLong(videoId);
                    drawable = MediaStore.Images.Thumbnails.getThumbnail(
                            c.getContentResolver(), videoIdLong,
                            Thumbnails.MINI_KIND, options);
                } else {
                    // drawable = BitmapFactory.decodeResource(c.getResources(),
                    // R.drawable.ic_doctor);
                }
            }
        }
        int degree = 0;
        ExifInterface exifInterface;
        try {
            exifInterface = new android.media.ExifInterface(file);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
            if (degree != 0 && drawable != null) {
                Matrix m = new Matrix();
                m.setRotate(degree, (float) drawable.getWidth() / 2,
                        (float) drawable.getHeight() / 2);
                drawable = Bitmap.createBitmap(drawable, 0, 0,
                        drawable.getWidth(), drawable.getHeight(), m, true);
            }
        } catch (java.lang.OutOfMemoryError e) {
            // Toast.makeText(c, "牌照出错，请重新牌照", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static String getDataString(String data) {
        String n = data.replace("年", "-");
        String y = n.replace("月", "-");
        String r = y.replace("日", " ");
        String s = r.replace("时", ":");
        String f = s.replace("分", ":");
        String overCreateDate = f + "00";
        return overCreateDate;

    }

    // 设置弹窗背景
    public static void setAttribute(Activity activity, PopupWindow pWin) {
        ColorDrawable cd = new ColorDrawable(0x000000);
        pWin.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().setAttributes(lp);
    }

    // 弹窗背景还原
    public static void onDismiss(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
    }

    // 改变时间样式
    public static String ChangeTime(String dateOld) {
        String n = dateOld.replace("年", "-");
        String y = n.replace("月", "-");
        String r = y.replace("日", " ");
        String s = r.replace("时", ":");
        String f = s.replace("分", "");
        return f;
    }

    public static void telePhoneBroadcast(Context activity, String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }

    // 设置圆角背景
    public static Drawable getDW(String locColor) {
        int strokeWidth = 1; // 3dp 边框宽度
        int roundRadius = 8; // 8dp 圆角半径
        int strokeColor = Color.parseColor("#" + locColor);// 边框颜色
        int fillColor = Color.parseColor("#" + locColor);// 内部填充颜色
        GradientDrawable gd = new GradientDrawable();// 创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    /**
     * 设置字体大小
     *
     * @param str   需要设置的字符串。
     * @param color (用getResource的方式获取自定义color。) 颜色。
     *              <p>
     *              return SpannableString 设置了颜色的字符串。
     */
    public static SpannableString getColorSpan(String str, int color,
                                               int colorEndIndex) {

        SpannableString spanString = new SpannableString(str);

        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, 0, colorEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;

    }

    public static Bitmap reduceBitmapQuality(String sourceBitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 2;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(sourceBitmap, options);
    }

    /**
     * 图片变色1
     *
     * @param bitmap
     * @return
     */
    public static Bitmap grey(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        // ColorMatrix colorMatrix = new ColorMatrix(new float[]{
        // 0.33F, 0.59F, 0.11F, 0, 0,
        // 0.33F, 0.59F, 0.11F, 0, 0,
        // 0.33F, 0.59F, 0.11F, 0, 0,
        // 0, 0, 0, 1, 0,
        // });
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{0.5f, 0, 0, 0,
                50, 0, 0.5f, 0, 0, 50, 0, 0, 0.5f, 0, 50, 0, 0, 0, 1f, 0, 0, 0,
                0, 0, 1

        });
        // colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }

    /** */
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmp 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmp) {
        if (bmp != null) {
            int width, height;
            Paint paint = new Paint();
            height = bmp.getHeight();
            width = bmp.getWidth();
            Bitmap bm = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0f);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(bmp, 0, 0, paint);
            return bm;
        } else {
            return bmp;
        }
    }

    /**
     * 图片变色2
     *
     * @param bitmap
     * @param newColor
     * @return
     */
    public static Bitmap changeImageColor(Bitmap bitmap, int newColor) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap grayImg = null;
        try {
            grayImg = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(grayImg);
            Paint paint = new Paint();
            ColorMatrix colorMatrix = new ColorMatrix();
            float[] colorArray = {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0,
                    0, 0, 0, 1, 0};
            colorMatrix.setSaturation(0);
            // colorMatrix.set(colorArray);
            ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                    colorMatrix);
            paint.setColorFilter(colorMatrixFilter);
            // paint.setColorFilter(new LightingColorFilter(Color.BLUE,
            // newColor));//通过color控制图片颜色
            canvas.drawBitmap(bitmap, 0, 0, paint);
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grayImg;
    }

    /**
     * 从给定路径加载图片
     */
    public static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }

    /**
     * 从给定的路径加载图片，并指定是否自动旋转方向
     */
    public static Bitmap loadBitmap(String path, boolean adjustOritation) {
        if (!adjustOritation) {
            return loadBitmap(path);
        } else {
            Bitmap bm = loadBitmap(path);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();

            }
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
            }
            return bm;
        }
    }

    /**
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {//超过100天会出问题
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        StringBuffer sb = new StringBuffer();
        if (days < 10) {
            sb.append("0");
        }
        sb.append(days);
        sb.append(":");
        if (hours < 10) {
            sb.append("0");
        }
        sb.append(hours);
        sb.append(":");
        if (minutes < 10) {
            sb.append("0");
        }
        sb.append(minutes);
        sb.append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        return sb.toString();
    }

    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "年" + mMonth + "月" + mDay + "日" + "/星期" + mWay;
    }

    public static void ActivityPage(Activity mActivity, List<ActivityPage> bannerList,int page) {//1首页2商城3支付成功
        Intent intent = new Intent(mActivity, MainAcPage.class);
        intent.putExtra("bannerList", (Serializable) bannerList);
        intent.putExtra("page",page);
        mActivity.startActivity(intent);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startdate 较小的时间
     * @param enddate   较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(String startdate, String enddate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(enddate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 128) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Utils.mLogError("byte"+baos.toByteArray().length/1024);
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static double extractNUm(String dist) {
        double distNum = 0;
        try {
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(dist);
            distNum = Double.parseDouble(m.replaceAll("").trim());
        } catch (Exception e) {
            Log.e("TAG", "提取数字异常e = " + e.toString());
            e.printStackTrace();
        }
        return distNum;
    }

    //java获取当前月的天数
    public static int getDayOfMonth() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }

    public static int dayofweek(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            return calendar.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static List<MDate> getTodayBefore(int length, String firstDay) {
        List<MDate> list = new ArrayList<MDate>();
        Log.e("TAG", "firstDay = " + firstDay);
        Log.e("TAG", "length = " + length);
        Log.e("TAG", "getDayOfMonth() = " + getDayOfMonth());
        if (length < getDayOfMonth()) {
            Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
            int year = aCalendar.get(Calendar.YEAR);//年份
            int month = aCalendar.get(Calendar.MONTH) + 1;//月份
            int day = aCalendar.getActualMaximum(Calendar.DATE);
            for (int i = 1; i < Integer.parseInt(firstDay); i++) {
                String aDate = String.valueOf(year) + "-" + (month < 10 ? ("0" + month) : month) + "-" + (i < 10 ? ("0" + i) : i);
                list.add(new MDate(aDate, String.valueOf(i), "", "", 0, 0, dayofweek(aDate), -1, false, false, false));
            }
        }
        return list;
    }

    public static String getCurrentMonthFirstDay() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH) + 1;//月份
        return String.valueOf(year) + "-" + (month < 10 ? ("0" + month) : month) + "-01";
    }

    public static void Exit(Context activity, int code) {
        if (code == Global.EXIT_USER_CODE) {
            removeLoginData(activity);
        }
    }

    public static void removeLoginData(Context activity) {
        SharedPreferenceUtil.getInstance(activity).removeData("petname");
        SharedPreferenceUtil.getInstance(activity).removeData("serviceloc");
        SharedPreferenceUtil.getInstance(activity).removeData("myPetMaximum");
        SharedPreferenceUtil.getInstance(activity).removeData("shopCartMemberLevelId");
        SharedPreferenceUtil.getInstance(activity).removeData("upRegionId");
        SharedPreferenceUtil.getInstance(activity).removeData("upShopName");
        SharedPreferenceUtil.getInstance(activity).removeData("shopid");
        SharedPreferenceUtil.getInstance(activity).removeData("invitecode");
        SharedPreferenceUtil.getInstance(activity).removeData("cellphone");
        SharedPreferenceUtil.getInstance(activity).removeData("payway");
        SharedPreferenceUtil.getInstance(activity).removeData("username");
        SharedPreferenceUtil.getInstance(activity).removeData("userimage");
        SharedPreferenceUtil.getInstance(activity).removeData("userid");
        SharedPreferenceUtil.getInstance(activity).removeData("areaid");
        SharedPreferenceUtil.getInstance(activity).removeData("areaname");
        SharedPreferenceUtil.getInstance(activity).removeData("petid");
        SharedPreferenceUtil.getInstance(activity).removeData("isCerti");
        SharedPreferenceUtil.getInstance(activity).removeData("petkind");
        SharedPreferenceUtil.getInstance(activity).removeData("petimage");
        SharedPreferenceUtil.getInstance(activity).removeData("customerpetid");
        SharedPreferenceUtil.getInstance(activity).removeData("customerpetname");
        SharedPreferenceUtil.getInstance(activity).removeData("mypetImage");
        SharedPreferenceUtil.getInstance(activity).removeData("addressid");
        SharedPreferenceUtil.getInstance(activity).removeData("lat");
        SharedPreferenceUtil.getInstance(activity).removeData("lng");
        SharedPreferenceUtil.getInstance(activity).removeData("address");
    }

    public static void showAdDialog(final Activity mActivity, final List<ActivityPage> localBannerList) {
        final View view = View
                .inflate(mActivity, R.layout.pw_ad, null);
        Banner show_main_img = (Banner) view
                .findViewById(R.id.show_main_img);
        ImageView imageView_close = (ImageView) view
                .findViewById(R.id.imageView_close);
        final PopupWindow pWin = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWin.setFocusable(true);
        pWin.setWidth(Utils.getDisplayMetrics(mActivity)[0]);
        pWin.showAtLocation(view, Gravity.CENTER, 0, 0);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < localBannerList.size(); i++) {
            list.add(localBannerList.get(i).getActivityPic());
        }
        show_main_img.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if (localBannerList.size() > position) {
                            ActivityPage activityPage = localBannerList.get(position);
                            if (activityPage != null) {
                                Utils.goService(mActivity, activityPage.getPoint(), activityPage.getBackup());
                                pWin.dismiss();
                            }
                        }
                    }
                })
                .start();
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                pWin.dismiss();
            }
        });
    }

    public static void popPhoto(Context context, View.OnClickListener onClickListenerCamer,
                                View.OnClickListener onClickListenerPicture) {
        ViewGroup customView = (ViewGroup) View.inflate(context, R.layout.photo_bottom_dialog, null);
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
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics((Activity) context)[0]);
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
        tv_photo_bottomdia_camer.setOnClickListener(onClickListenerCamer);
        tv_photo_bottomdia_picture.setOnClickListener(onClickListenerPicture);
    }

    public static void share(final Activity context, final String shareImg, final String shareTitle,
                             final String shareTxt, final String shareUrl, final String channel, final int type,String path) {
        if (Utils.isStrNull(shareImg) && Utils.isStrNull(shareTitle) && Utils.isStrNull(shareTxt) && Utils.isStrNull(shareUrl)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "shareImg = " + shareImg);
                    Log.e("TAG", "shareTitle = " + shareTitle);
                    Log.e("TAG", "shareTxt = " + shareTxt);
                    Log.e("TAG", "shareUrl = " + shareUrl);
                    Log.e("TAG", "channel = " + channel);
                    Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(shareImg);
                    Log.e("TAG", "returnBitmap = " + returnBitmap);
                    Message msg = new Message();
                    msg.obj = new ShareInfo(context, shareImg, shareTitle, shareTxt, shareUrl, channel, returnBitmap, type,path);
                    msg.what = Global.ADACTIVITY_SHARE;
                    handler.sendMessage(msg);
                }
            }).start();
        } else {
            ToastUtil.showToastShortBottom(context, "分享信息不正确");
        }
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Global.ADACTIVITY_SHARE) {
                ShareInfo shareInfo = (ShareInfo) msg.obj;
                if (shareInfo != null) {
                    Log.e("TAG", "(Bitmap) msg.obj = " + (ShareInfo) msg.obj);
                    showShare(shareInfo.getContext(), shareInfo.getBitmap(), shareInfo.getShareImg(),
                            shareInfo.getShareTitle(), shareInfo.getShareTxt(), shareInfo.getShareUrl(),
                            shareInfo.getChannel(), shareInfo.getType(),shareInfo.getPath());
                }
            }
        }
    };

    private static void showShare(final Activity context, final Bitmap bitmap, final String shareImg,
                                  final String shareTitle, final String shareTxt, final String shareUrl,
                                  String channel, int type,String path) {
        if (bitmap != null) {
            if (type > 0) {
                if (type == 1) {
                    setWXShareContent(1, context, bitmap, shareImg, shareTitle, shareTxt, shareUrl);
                } else if (type == 2) {
                    setWXShareContent(2, context, bitmap, shareImg, shareTitle, shareTxt, shareUrl);
                } else if (type == 3) {
                    setWXShareContent(3, context, bitmap, shareImg, shareTitle, shareTxt, shareUrl);
                }
            } else {
                final View view = View.inflate(context, R.layout.sharedialog, null);
                RelativeLayout rlParent = (RelativeLayout) view
                        .findViewById(R.id.rl_sharedialog_parent);
                LinearLayout ll_sharedialog_wxfriend = (LinearLayout) view
                        .findViewById(R.id.ll_sharedialog_wxfriend);
                LinearLayout ll_sharedialog_wxpyq = (LinearLayout) view
                        .findViewById(R.id.ll_sharedialog_wxpyq);
                LinearLayout ll_sharedialog_qqfriend = (LinearLayout) view
                        .findViewById(R.id.ll_sharedialog_qqfriend);
                Button btn_sharedialog_cancel = (Button) view
                        .findViewById(R.id.btn_sharedialog_cancel);
                if (Utils.isStrNull(channel) && channel.contains("1")) {
                    ll_sharedialog_wxfriend.setVisibility(View.VISIBLE);
                } else {
                    ll_sharedialog_wxfriend.setVisibility(View.GONE);
                }
                if (Utils.isStrNull(channel) && channel.contains("2")) {
                    ll_sharedialog_wxpyq.setVisibility(View.VISIBLE);
                } else {
                    ll_sharedialog_wxpyq.setVisibility(View.GONE);
                }
                if (Utils.isStrNull(channel) && channel.contains("3")) {
                    ll_sharedialog_qqfriend.setVisibility(View.VISIBLE);
                } else {
                    ll_sharedialog_qqfriend.setVisibility(View.GONE);
                }
                final PopupWindow pWin = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, true);
                pWin.setFocusable(true);
                pWin.setClippingEnabled(false);
                pWin.setWidth(Utils.getDisplayMetrics(context)[0]);
                pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                rlParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        pWin.dismiss();
                    }
                });
                ll_sharedialog_wxfriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {// 微信好友
                        pWin.dismiss();
                        //判断是否要分享到小程序
                        if (TextUtils.isEmpty(path)){
                            setWXShareContent(1, context, bitmap, shareImg, shareTitle, shareTxt, shareUrl);
                        }
                        else {
                            //分享小程序
                            shareMiniProgram(context,shareUrl,bitmap,path,shareTitle,shareTxt);
                        }
                    }
                });
                ll_sharedialog_wxpyq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {// 微信朋友圈
                        pWin.dismiss();
                        setWXShareContent(2, context, bitmap, shareImg, shareTitle, shareTxt, shareUrl);
                    }
                });
                ll_sharedialog_qqfriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {// QQ好友
                        pWin.dismiss();
                        setWXShareContent(3, context, bitmap, shareImg, shareTitle, shareTxt, shareUrl);
                    }
                });
                btn_sharedialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {// 取消
                        pWin.dismiss();
                    }
                });
            }
        } else {
            ToastUtil.showToastShortBottom(context, "分享信息不正确");
        }
    }

    private static void setWXShareContent(int type, final Activity context, final Bitmap bitmap, final String shareImg,
                                          final String shareTitle, final String shareTxt, final String shareUrl) {
        boolean weixinAvilible = Utils.isWeixinAvilible(context);
        if (type == 1 || type == 2) {// 微信
            if (weixinAvilible) {
                IWXAPI api = WXAPIFactory.createWXAPI(context, Global.APP_ID);
                WXWebpageObject wxwebpage = new WXWebpageObject();
                wxwebpage.webpageUrl = completeUrl(context, shareUrl);
                WXMediaMessage wxmedia = new WXMediaMessage(wxwebpage);
                wxmedia.title = shareTitle;
                wxmedia.description = shareTxt;
                Bitmap createBitmapThumbnail = Utils.createBitmapThumbnail(bitmap);
                if (createBitmapThumbnail == null) {
                    createBitmapThumbnail = bitmap;
                }
                wxmedia.setThumbImage(createBitmapThumbnail);
                wxmedia.thumbData = Util_WX.bmpToByteArray(
                        createBitmapThumbnail, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = wxmedia;
                if (type == 1) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                api.sendReq(req);
            } else {
                ToastUtil.showToastShortBottom(context, "微信不可用");
            }
        } else if (type == 3) {// qq
            UmengShareUtils umengShareUtils = new UmengShareUtils(context, shareTxt, completeUrl(context, shareUrl),
                    shareTitle, shareImg);
            umengShareUtils.mController.getConfig().closeToast();
            umengShareUtils.mController.postShare(context, SHARE_MEDIA.QQ, null);
        }
    }

    public static void shareToWXImage(Context context, String imgURl) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, Global.APP_ID);
        WXMediaMessage msg = new WXMediaMessage();
        Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(imgURl);
        Bitmap createBitmapThumbnail = Utils.createBitmapThumbnail(returnBitmap);
        if (createBitmapThumbnail == null) {
            createBitmapThumbnail = returnBitmap;
        }
        msg.mediaObject = new WXImageObject(returnBitmap);
        msg.setThumbImage(createBitmapThumbnail);
        msg.thumbData = Util_WX.bmpToByteArray(
                createBitmapThumbnail, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    public static void shareMiniProgram(Context context,String webpageUrl,Bitmap bitmap,String path,String title,String description){
        IWXAPI api = WXAPIFactory.createWXAPI(context, Global.APP_ID);
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = webpageUrl; // 兼容低版本的网页链接
        if (CommUtil.getEnvironmental() == 3){
            miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        }
        else {
            miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW;// 正式版:0，测试版:1，体验版:2
        }
        miniProgramObj.userName = "gh_398784131ce1";     // 小程序原始id
        Utils.mLogError("web"+webpageUrl+"path"+path+"title"+title+"description"+description);
        miniProgramObj.path = path;            //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = title;                    // 小程序消息title
        msg.description = description;               // 小程序消息desc

        Bitmap createBitmapThumbnail = Utils.createBitmapThumbnail(bitmap,200,200);
        if (createBitmapThumbnail == null) {
            createBitmapThumbnail = bitmap;
        }
        msg.thumbData = Util_WX.bmpToByteArray(
                createBitmapThumbnail, true);

//        Bitmap compressImage = Utils.compressImage(bitmap);
//        if (compressImage == null){
//            compressImage = bitmap;
//        }
//        msg.thumbData = Util_WX.bmpToByteArray(compressImage,true);                      // 小程序消息封面图片，小于128k

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("miniProgram");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
        api.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public static String completeUrl(Activity context, String url) {
        if (url != null && !TextUtils.isEmpty(url)) {
            if (!url.startsWith("http:")
                    && !url.startsWith("https:")) {
                url = CommUtil.getStaticUrl() + url;
            }
            if (url.contains("?")) {
                url = url
                        + "&system="
                        + CommUtil.getSource()
                        + "_"
                        + Global.getCurrentVersion(context)
                        + "&imei="
                        + Global.getIMEI(context)
                        + "&cellPhone="
                        + SharedPreferenceUtil.getInstance(context).getString(
                        "cellphone", "") + "&phoneModel="
                        + android.os.Build.BRAND + " " + android.os.Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + android.os.Build.VERSION.RELEASE + "&time="
                        + System.currentTimeMillis();
            } else {
                url = url
                        + "?system="
                        + CommUtil.getSource()
                        + "_"
                        + Global.getCurrentVersion(context)
                        + "&imei="
                        + Global.getIMEI(context)
                        + "&cellPhone="
                        + SharedPreferenceUtil.getInstance(context).getString(
                        "cellphone", "") + "&phoneModel="
                        + android.os.Build.BRAND + " " + android.os.Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + android.os.Build.VERSION.RELEASE + "&time="
                        + System.currentTimeMillis();
            }
        }
        return url;
    }

    public static void setCardDesc(Context mContext, String title, CharSequence msg, String okStr, View.OnClickListener positiveListener) {
        CardDescDialog cardDescDialog = new CardDescDialog.Builder(mContext)
                .setTitle(title)
                .setType(MDialog.DIALOGTYPE_ALERT)
                .setMsg(msg)
                .positiveListener(positiveListener)
                .setCancelable(true).setOKStr(okStr)
                .build();
        cardDescDialog.show();
    }

    public static void setCardDesc(Context mContext, String title, CharSequence msg, String okStr, int gravity, View.OnClickListener positiveListener) {
        CardDescDialog cardDescDialog = new CardDescDialog.Builder(mContext)
                .setTitle(title)
                .setType(MDialog.DIALOGTYPE_ALERT)
                .setMsg(msg)
                .setGravity(gravity)
                .positiveListener(positiveListener)
                .setCancelable(true).setOKStr(okStr)
                .build();
        cardDescDialog.show();
    }

    /**
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author sunran   判断当前时间在时间区间内
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取对应格式的时间
     */
    public static Date getFormatDate(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static enum JSON_TYPE {
        /**
         * JSONObject
         */
        JSON_TYPE_OBJECT,
        /**
         * JSONArray
         */
        JSON_TYPE_ARRAY,
        /**
         * 不是JSON格式的字符串
         */
        JSON_TYPE_ERROR
    }

    public static JSON_TYPE getJSONType(String str) {
        if (TextUtils.isEmpty(str)) {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }

        final char[] strChar = str.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

        if (firstChar == '{') {
            return JSON_TYPE.JSON_TYPE_OBJECT;
        } else if (firstChar == '[') {
            return JSON_TYPE.JSON_TYPE_ARRAY;
        } else {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
    }

    public static Spanned getTextAndColor(String NameBase, String PriceBase) {
        return Html.fromHtml("<font size=\"3\" color=\"#757575\">" + NameBase
                + "</font><font size=\"3\" color=\"#FF8C00\">" + "<b>"
                + PriceBase + "</b>" + "</font>");
    }

    public static Spanned getTextAndColorComments(String NameBase,
                                                  String PriceBase) {
        return Html.fromHtml("<font size=\"3\" color=\"#757575\">" + NameBase
                + "</font><font size=\"3\" color=\"#FF8C00\">" + PriceBase
                + "</font>");
    }

    public static Spanned getTextAndColorCommentsBeau(String firstColor,
                                                      String NameBase, String secondColor, String PriceBase) {
        return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
                + NameBase + "</font><font size=\"3\" color=\"" + secondColor
                + "\">" + PriceBase + "</font>");
    }

    public static Spanned getTextAndColorCommentsBeau(String firstColor,
                                                      String NameBase) {
        return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
                + NameBase + "</font>");
    }

    public static Spanned getTextAndColorThr(String firstColor,
                                             String NameBase, String secondColor, String PriceBase,
                                             String thrColor, String showStr) {
        return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
                + NameBase + "</font><font size=\"3\" color=\"" + secondColor
                + "\">" + PriceBase + "</font>" + "<font size=\"3\" color=\""
                + thrColor + "\">" + showStr + "</font>");
    }

    public static Spanned getTextAndColorFour(String firstColor,
                                              String NameBase, String secondColor, String PriceBase,
                                              String thrColor, String showStr, String fourColor, String fourStr) {
        return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
                + NameBase + "</font><font size=\"3\" color=\"" + secondColor
                + "\">" + PriceBase + "</font>" + "<font size=\"3\" color=\""
                + thrColor + "\">" + showStr + "</font>"
                + "<font size=\"3\" color=\"" + fourColor + "\">" + fourStr
                + "</font>");

    }

    public static Spanned getTextAndColorFive(String firstColor,
                                              String NameBase, String secondColor, String PriceBase,
                                              String thrColor, String showStr, String fourColor, String fourStr, String fiveColor, String fiveStr) {
        return Html.fromHtml("<font size=\"3\" color=\"" + firstColor + "\">"
                + NameBase + "</font><font size=\"3\" color=\"" + secondColor
                + "\">" + PriceBase + "</font>" + "<font size=\"3\" color=\""
                + thrColor + "\">" + showStr + "</font>"
                + "<font size=\"3\" color=\"" + fourColor + "\">" + fourStr
                + "</font>"
                + "<font size=\"3\" color=\"" + fiveColor + "\">" + fiveStr
                + "</font>"
        );

    }

    public static Bitmap createBitmapThumbnail(Bitmap bitMap) {
        return createBitmapThumbnail(bitMap,99,99);
    }

    public static Bitmap createBitmapThumbnail(Bitmap bitMap,int widthValue,int heightValue) {
        Bitmap newBitMap = null;
        if (bitMap != null) {
            int width = bitMap.getWidth();
            int height = bitMap.getHeight();
            // 计算缩放比例
            float scaleWidth = ((float) widthValue) / width;
            float scaleHeight = ((float) heightValue) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
                    matrix, true);
        }
        return newBitMap;
    }

    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     * <p>
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     * <p>
     * B.本地路径:url="file://mnt/sdcard/photo/image.png";
     * <p>
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param imageUrl
     * @return
     */
    public static Bitmap GetLocalOrNetBitmap(String imageUrl) {
        if (!imageUrl.startsWith("drawable://")
                && !imageUrl.startsWith("file://")
                && !imageUrl.startsWith("http://")
                && !imageUrl.startsWith("https://")) {
            imageUrl = CommUtil.getStaticUrl() + imageUrl;
        }
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(imageUrl).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public final static int IO_BUFFER_SIZE = 1024;

    public static void MyRecycle(Bitmap bmp) {
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }
    }

    public static void saveImageToGallery(Context context,Bitmap bmp) {
        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirName = "erweima16";
        File appDir = new File(root , dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(timeStamp));
        String fileName = sd + ".jpg";

        //获取文件
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            ToastUtil.showToastShortBottom(context,"保存成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setStringText(TextView tv, String str) {
        if (str != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.INVISIBLE);
        }
    }

    public static void setStringTextGone(TextView tv, String str) {
        if (str != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    /**
     * 查询当前日期前(后)x天的日期
     *
     * @param millis 当前日期毫秒数
     * @param day    天数（如果day数为负数,说明是此日期前的天数）
     * @return long 毫秒数只显示到天，时间全为0
     * @throws ParseException
     */
    public static long beforDateNum(long millis, int day) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        Date newDate = sdf.parse(sdf.format(date));
        return newDate.getTime();
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 比较两个日期的大小 DATE1 > DATE2 返回1 DATE1 < DATE2 返回-1 DATE1 == DATE2 返回0
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static SpannableString getTextShow(Context mContext, String str,
                                              int oneStart, int oneEnd, int TwoStart, int TwoEnd) {
        SpannableString styledText = new SpannableString(str);
        // styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style0),
        // oneStart,oneEnd,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style1),
                TwoStart, TwoEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styledText;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    // 判断微信是否可用
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param filepath 照片路径
     * @return角度
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            File imageFile = new File(filepath);
            exif = new ExifInterface(imageFile.getAbsolutePath());
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    public static Bitmap toturn(Bitmap img, int rto) {
        Matrix matrix = new Matrix();
        matrix.postRotate(+rto); // 翻转rto度
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    public static boolean isStrNull(String str) {
        boolean bool = false;
        if (str != null && !TextUtils.isEmpty(str)) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    public static boolean isObjNull(Object obj) {
        boolean bool = false;
        if (obj != null) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    // 跳转到应用市场评价
    public static void goMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id="
                    + context.getPackageName());
            Intent intentwx = new Intent(Intent.ACTION_VIEW, uri);
            intentwx.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentwx);
        } catch (Exception e) {
            ToastUtil.showToastShortBottom(context, "您没有安装应用市场");
        }
    }

    /**
     * 将 BD-09 坐标转换成 GCJ-02 坐标 GoogleMap和高德map用的是同一个坐标系GCJ-02
     */
    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    public static double[] bd_decrypt(double bd_lat, double bd_lon) {
        double gg_lat = 0.0;
        double gg_lon = 0.0;
        double location[] = new double[2];
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        gg_lon = z * Math.cos(theta);
        gg_lat = z * Math.sin(theta);
        location[0] = gg_lat;
        location[1] = gg_lon;
        return location;
    }

    public static String ToSBC(String input) { // 全角
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    public static String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /*
     * 直接\u9999表示的很少，基本都会用转义字符即\\u9999,这样一来打印出来的就不是我们需要的字符，而是Unicode码\u9999
     * 因此必须解析字符串得到想要的字符。
     */
    public static String unicode2string(String s) {
        List<String> list = new ArrayList<String>();
        String zz = "\\\\u[0-9,a-z,A-Z]{4}";

        // 正则表达式用法参考API
        Pattern pattern = Pattern.compile(zz);
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            list.add(m.group());
        }
        for (int i = 0, j = 2; i < list.size(); i++) {
            String st = list.get(i).substring(j, j + 4);

            // 将得到的数值按照16进制解析为十进制整数，再強转为字符
            char ch = (char) Integer.parseInt(st, 16);
            // 用得到的字符替换编码表达式
            s = s.replace(list.get(i), String.valueOf(ch));
        }
        return s;
    }

    /*
     * 将字符转为Unicode码表示
     */
    public static String string2unicode(String s) {
        int in;
        String st = "";
        for (int i = 0; i < s.length(); i++) {
            in = s.codePointAt(i);
            st = st + "\\u" + Integer.toHexString(in).toUpperCase();
        }
        return st;
    }

    private void setEmojiToTextView(TextView myTextView) {
        int unicodeJoy = 0x1F602;
        String emojiString = getEmojiStringByUnicode(unicodeJoy);
        myTextView.setText(emojiString);
    }

    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    // 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Context context,
                                             final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean hasPet(Context mContext) {
        if (SharedPreferenceUtil.getInstance(mContext).getInt("petid", -1) > 0)
            return true;
        return false;
    }

    public static boolean checkLogin(Context mContext) {
        boolean bool;
        String cellphone = SharedPreferenceUtil.getInstance(mContext)
                .getString("cellphone", "");
        int userid = SharedPreferenceUtil.getInstance(mContext).getInt(
                "userid", 0);
        if (cellphone != null && !cellphone.isEmpty() && userid > 0) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    public static int getMyPetMaximum(Context mContext) {
        int myPetMaximum = SharedPreferenceUtil.getInstance(mContext).getInt("myPetMaximum", -1);
        return myPetMaximum;
    }

    public static String insertStringInParticularPosition(String src,
                                                          String dec, int position) {
        StringBuffer stringBuffer = new StringBuffer(src);
        return stringBuffer.insert(position, dec).toString();
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static File saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(),
                "PetTemp");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "Pet_Temp_camera"
                + String.valueOf(System.currentTimeMillis() + ".png");
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void goneJP(Context context) {
        try {
            ((InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(((Activity) context)
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setText(TextView tv, CharSequence str, String defaultStr,
                               int visibilt, int defaultVisibilt) {
        if (str != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
            tv.setVisibility(visibilt);
        } else {
            tv.setText(defaultStr);
            tv.setVisibility(defaultVisibilt);
        }
    }

    public static void setTextInteger(TextView tv, String str, String defaultStr,
                                      int visibilt, int defaultVisibilt) {
        if (str != null && !TextUtils.isEmpty(str)) {
            tv.setText("充" + (int) Double.parseDouble(str));
            tv.setVisibility(visibilt);
        } else {
            tv.setText(defaultStr);
            tv.setVisibility(defaultVisibilt);
        }
    }

    public static JSONObject getNetData(Context context, byte[] responseBody) {
        JSONObject objectData = null;
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            int code = object.getInt("code");
            if (code == 0) {
                if (object.has("data") && !object.isNull("data")) {
                    objectData = object.getJSONObject("data");
                }
            } else {
                if (object.has("msg") && !object.isNull("msg")) {
                    String msg = object.getString("msg");
                    ToastUtil.showToastShortCenter(context, msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objectData;
    }

    public static Drawable loadImageFromNetwork(String imageUrl) {
        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
            imageUrl = CommUtil.getStaticUrl() + imageUrl;
        }
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }
        return drawable;
    }

    /**
     * RxJava方式保存图片到本地
     *
     * @param context
     * @param mImageView
     * @param petCircle
     * @param fileName
     */
    public static void savePic(final Context context,
                               final ImageView mImageView, final File petCircle,
                               final String fileName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(
                new String[]{context.getResources().getString(
                        R.string.save_picture)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveImageView(getViewBitmap(mImageView), petCircle,
                                context, fileName);
                    }
                });
        builder.show();
    }

    private static class SaveObservable implements
            ObservableOnSubscribe<String> {
        private Bitmap drawingCache = null;
        private File petCircle;
        private String fileName;

        public SaveObservable(Bitmap drawingCache, File petCircle,
                              String fileName) {
            this.drawingCache = drawingCache;
            this.petCircle = petCircle;
            this.fileName = fileName;
        }

        @Override
        public void subscribe(ObservableEmitter<String> subscriber) throws Exception {
            if (drawingCache == null) {
                subscriber.onError(new NullPointerException(
                        "imageview的bitmap获取为null,请确认imageview显示图片了"));
            } else {
                try {
                    File imageFile = new File(this.petCircle, this.fileName);
                    FileOutputStream outStream;
                    outStream = new FileOutputStream(imageFile);
                    drawingCache.compress(Bitmap.CompressFormat.JPEG, 100,
                            outStream);
                    subscriber.onNext(Environment.getExternalStorageDirectory()
                            .getPath());
                    subscriber.onComplete();
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }
    }

    private static class SaveSubscriber implements Observer<String> {
        private Context context;

        public SaveSubscriber(Context context) {
            this.context = context;
        }

        @Override
        public void onComplete() {
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {
            Log.i(getClass().getSimpleName(), e.toString());
            Toast.makeText(context, "保存失败——> " + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(String s) {/*
         * Toast.makeText(context, "保存路径为：-->  " +
         * s, Toast.LENGTH_SHORT) .show();
         */
        }
    }

    public static void saveImageView(Bitmap drawingCache, File petCircle,
                                     Context context, String fileName) {
        Observable
                .create(new SaveObservable(drawingCache, petCircle, fileName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SaveSubscriber(context));
    }

    /**
     * 某些机型直接获取会为null,在这里处理一下防止国内某些机型返回null
     */
    public static Bitmap getViewBitmap(View view) {
        if (view == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 计算时间差
     *
     * @param oldTime
     * @param newTime
     * @return
     */
    public static int TimeCha(String oldTime, String newTime) {
        int days = 0;
        try {
            SimpleDateFormat simpleFormat = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm");// 如2016-08-10 20:40
            long from = simpleFormat.parse(oldTime).getTime();
            long to = simpleFormat.parse(newTime).getTime();
            days = (int) ((to - from) / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            Log.e("TAG", "E = " + e.toString());
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /*
     * 获取时间差
     */
    public static String getTimesToNow(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String now = format.format(new Date());
        String returnText = null;
        try {
            long from = format.parse(date).getTime();
            long to = format.parse(now).getTime();
            int days = (int) ((to - from) / (1000 * 60 * 60 * 24));
            if (days == 0) {// 一天以内，以分钟或者小时显示
                int hours = (int) ((to - from) / (1000 * 60 * 60));
                if (hours == 0) {
                    int minutes = (int) ((to - from) / (1000 * 60));
                    if (minutes == 0) {
                        returnText = "刚刚";
                    } else {
                        returnText = minutes + "分钟前";
                    }
                } else {
                    returnText = hours + "小时前";
                }
            } else if (days == 1) {
                returnText = "昨天";
            } else {
                returnText = days + "天前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnText;
    }

    public static void imageBrower(Context context, int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    public static void imageBrower(Context context, int position, String[] urls, boolean isUri) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.IS_URI, isUri);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    @SuppressLint("NewApi")
    public static void hideBottomUIMenu(Activity context) {
        // 隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower
            // api
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            // for new api versions.
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 判断应用市场是否存在的方法
     *
     * @param context
     * @param packageName
     *
     * 主流应用商店对应的包名
     * com.android.vending    -----Google Play
     * com.tencent.android.qqdownloader     -----应用宝
     * com.qihoo.appstore    -----360手机助手
     * com.baidu.appsearch    -----百度手机助
     * com.xiaomi.market    -----小米应用商店
     * com.wandoujia.phoenix2    -----豌豆荚
     * com.huawei.appmarket    -----华为应用市场
     * com.taobao.appcenter    -----淘宝手机助手
     * com.hiapk.marketpho    -----安卓市场
     * cn.goapk.market        -----安智市场
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> pName = new ArrayList<String>();
        // 从pinfo中将包名字取出
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pf = pinfo.get(i).packageName;
                pName.add(pf);
            }
        }
        // 判断pName中是否有目标程序的包名，有true，没有false
        return pName.contains(packageName);
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面
     */
    public static void launchAppDetail(Context mContext, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) {
                return;
            }

            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setListHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();

        if (adapter == null)
            return;

        int listHeight = 0;
        // Log.i(TAG, "====adapter=====" + adapter.getCount());
        for (int i = 0; i < adapter.getCount(); i++) {

            View listItem = adapter.getView(i, null, listView);
            // View view=adapter.getView(i, convertView, listView);
            listItem.measure(0, 0);

            listHeight += listItem.getMeasuredHeight();
            /*
             * Log.i("==========", "=======listview每一个item高度======" +
             * listItem.getMeasuredHeight());
             */
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = listHeight
                + (listView.getDividerHeight() * ((listView.getCount() - 1)));
        /*
         * Log.i("==========", "=======listview每一个item相间的高度======" +
         * listView.getDividerHeight());
         */
        // Log.i("==========", "=======listview总高度======" + params.height);

        listView.setLayoutParams(params);
    }

    public static int getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
            Utils.mLogError("数据" + i + "  " + String.valueOf(totalHeight));
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        Utils.mLogError("listview总高度=" + params.height);
        listView.setLayoutParams(params);
        listView.requestLayout();
        return totalHeight;
    }

    public static boolean goService(Activity mActivity, int point, String backup) {
        SharedPreferenceUtil spUtil = SharedPreferenceUtil.getInstance(mActivity);
        boolean bool = false;
        switch (point) {
            case 1:// 洗澡
                bool = true;
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_BathSelect);
                spUtil.removeData("charactservice");
                mActivity.startActivity(new Intent(mActivity, ServiceNewActivity.class)
                        .putExtra("serviceType", 1));
                break;
            case 2:// 美容
                bool = true;
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_BeautySelect);
                spUtil.removeData("charactservice");
                mActivity.startActivity(new Intent(mActivity, ServiceNewActivity.class)
                        .putExtra("serviceType", 2));
                break;
            case 3:// 寄养
                bool = true;
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_FosterCareSelect);
                mActivity.startActivity(new Intent(mActivity, FosterHomeActivity.class));
                break;
            case 6:// 特色服务
                bool = true;
                UmengStatistics.UmengEventStatistics(mActivity,
                        Global.UmengEventID.click_HomePage_CharacteristicSelect);
                mActivity.startActivity(new Intent(mActivity,
                        CharacteristicServiceActivity.class));
                break;
            case 7:// 犬证h5
                if (Utils.isStrNull(backup)) {
                    bool = true;
                    UmengStatistics.UmengEventStatistics(mActivity,
                            Global.UmengEventID.click_HomePage_DogcardSelect);
                    spUtil.removeData("charactservice");
                    mActivity.startActivity(new Intent(mActivity, ADActivity.class).putExtra(
                            "url", backup));
                } else {
                    bool = false;
                }
                break;
            case 8:// 服务卡h5
                Log.e("TAG", "backup = " + backup);
                if (Utils.isStrNull(backup)) {
                    bool = true;
                    mActivity.startActivity(new Intent(mActivity, ADActivity.class).putExtra(
                            "url", backup));
                } else {
                    bool = false;
                }
                break;
            case 9:// 特色服务详情页
                if (Utils.isStrNull(backup)) {
                    bool = true;
                    Intent intent = new Intent(mActivity, CharacteristicServiceActivity.class);
                    intent.putExtra("serviceType", 3);
                    intent.putExtra("typeId", Integer.parseInt(backup));// 从特色服务跳转到门店列表专用，为了计算价格
                    mActivity.startActivity(intent);
                } else {
                    bool = false;
                }
                break;
            case 10:// 跳转到添加宠物界面
                bool = true;
                if (Utils.checkLogin(mActivity)) {
                    mActivity.startActivity(new Intent(mActivity, PetAddActivity.class));
                } else {
                    mActivity.startActivity(new Intent(mActivity, LoginNewActivity.class));
                }
                break;
            case 12:// 邀请有礼页面
                Log.e("TAG", "backup = " + backup);
                if (Utils.isStrNull(backup)) {
                    bool = true;
                    mActivity.startActivity(new Intent(mActivity, ADActivity.class).putExtra(
                            "url", backup));
                } else {
                    bool = false;
                }
                break;
            case 13:// 美容师列表
                bool = true;
                mActivity.startActivity(new Intent(mActivity, MainToBeauList.class));
                break;
            case 15://充值卡详情

                break;
            case 17://VIP会员页面
                bool = true;
                if (Utils.checkLogin(mActivity)) {
                    mActivity.startActivity(new Intent(mActivity, MemberUpgradeActivity.class));
                } else {
                    mActivity.startActivity(new Intent(mActivity, LoginNewActivity.class));
                }
                break;
            case 18://我的罐头币页面
                bool = true;
                mActivity.startActivity(new Intent(mActivity, MyCanActivity.class));
                break;
            case 19://商城一级分类落地页
                bool = true;
                mActivity.startActivity(new Intent(mActivity, MallToListActivity.class).putExtra("classificationId", Integer.parseInt(backup)));
                break;
            case 20://商城商品详情页
                bool = true;
                mActivity.startActivity(new Intent(mActivity, CommodityDetailActivity.class).putExtra("commodityId", Integer.parseInt(backup)));
                break;
            case 21://商城首页
                bool = true;
                Intent intent = new Intent();
                intent.setAction("android.intent.action.mainactivity");
                intent.putExtra("previous",
                        Global.PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY);
                mActivity.sendBroadcast(intent);
                for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                    MApplication.listAppoint.get(i).finish();
                }
                for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                    MApplication.listAppoint1.get(i).finish();
                }
                if (mActivity instanceof MainActivity) {

                } else {
                    mActivity.finish();
                }
                break;
            case 22://新手任务页
                bool = true;
                mActivity.startActivity(new Intent(mActivity, NewbieTaskActivity.class).putExtra("activityId", Integer.parseInt(backup)));
                break;
            case 23:// 百科列表
                bool = true;
                mActivity.startActivity(new Intent(mActivity, EncyclopediasActivity.class));
                break;
            case 24://百科详情
                bool = true;
                mActivity.startActivity(new Intent(mActivity, EncyclopediasDetail.class).putExtra("infoId", Integer.parseInt(backup)));
                break;
            case 25://购买E卡界面
                bool = true;
                mActivity.startActivity(new Intent(mActivity, GiftCardListActivity.class).putExtra("cityId", Utils.isStrNull(backup) ? Integer.parseInt(backup) : ""));
                break;
            case 26://E卡详情页
                bool = true;
                mActivity.startActivity(new Intent(mActivity, GiftCardDetailActivity.class).putExtra("cardTemplateId", Integer.parseInt(backup)));
                break;
            case 27://我的E卡列表页
                bool = true;
                if (Utils.checkLogin(mActivity)) {
                    mActivity.startActivity(new Intent(mActivity, MyCardActivity.class));
                } else {
                    mActivity.startActivity(new Intent(mActivity, LoginNewActivity.class));
                }
                break;
            case 29://返现主页
                bool = true;
                if (Utils.checkLogin(mActivity)) {
                    mActivity.startActivity(new Intent(mActivity, CashbackAmountActivity.class));
                } else {
                    mActivity.startActivity(new Intent(mActivity, LoginNewActivity.class));
                }
                break;
            case 30://我的优惠券列表页
                bool = true;
                if (Utils.checkLogin(mActivity)) {
                    mActivity.startActivity(new Intent(mActivity, MyCouponNewActivity.class));
                } else {
                    mActivity.startActivity(new Intent(mActivity, LoginNewActivity.class));
                }
                break;
            default:
                bool = false;
                break;
        }
        return bool;
    }

    public static void showBackDialog(final Context mContext, String title, int isShowTitle, String MessageStr, String cancleText, String ok, final String url) {
        MDialog mDialog = new MDialog.Builder(mContext)
                .setTitle(title)
                .setTitleShow(isShowTitle)//1 显示  其他不显示
                .setType(MDialog.DIALOGTYPE_CONFIRM)
                .setMessage(MessageStr)
                .setCancelStr(cancleText)
                .setOKStr(ok)
                .setTitleTextColor(R.color.a333333)
                .positiveListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (!TextUtils.isEmpty(url)) {
                            Intent intentUrl = new Intent(mContext, ADActivity.class);
                            intentUrl.putExtra("url", url);
                            mContext.startActivity(intentUrl);
                        }
                    }
                }).build();
        mDialog.show();
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    /*//直接拨打电话
    public static void callPhone(String phoneNumber, Context context) {
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }*/

    //跳转到拨号界面
    public static void callToPhone(Context context) {
        context.startActivity(new Intent(Intent.ACTION_CALL_BUTTON));
    }

    //跳转到拨号界面，同时传递电话号码
    public static void callToPhone(String phoneNumber, Context context) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    public static int getLeftMargin(Context mContext, int textSp, TextView textView, String str) {
        TextPaint newPaint = new TextPaint();
        float textSize = mContext.getResources().getDisplayMetrics().scaledDensity * textSp;
        newPaint.setTextSize(textSize);
        float newPaintWidth = newPaint.measureText(str.trim()) + textView.getPaddingLeft() + textView.getPaddingRight();
        return (int) Math.ceil(newPaintWidth);
    }

    public static int setListViewHeightBasedOnChildren(ListView listView) {
        int height = 0;
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return height;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        Log.e("TAG", "params.height = " + params.height);
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        return height;
    }

    public static boolean isDoubleEndWithZero(double price) {
        boolean bool = false;
        try {
            String str = String.valueOf(price);
            String[] split = str.split("\\.");
            if (Integer.parseInt(split[1]) > 0) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }

    public static void setDoubleStr(TextView tv, double price, String doubleBefore, String doubleAfter) {
        String text = "";
        if (isDoubleEndWithZero(price)) {
            text = doubleBefore + Utils.formatDouble(price) + doubleAfter;
        } else {
            text = doubleBefore + price + doubleAfter;
        }
        tv.setText(text);
    }

    public static String getDoubleStr(double price, String doubleBefore, String doubleAfter) {
        String text = "";
        if (isDoubleEndWithZero(price)) {
            text = doubleBefore + Utils.formatDouble(price) + doubleAfter;
        } else {
            text = doubleBefore + price + doubleAfter;
        }
        return text;
    }

    public static void setPhoneDialog(final Context context, final String cellPhone, String msg, String cancelStr, String oKStr, int cancelTextColorId, int oKTextColorId, int msgTextColorId) {
        MDialog mDialog = new MDialog.Builder(context)
                .setType(MDialog.DIALOGTYPE_CONFIRM).setTitle("呼叫电话")
                .setMessage(msg).setCancelStr(cancelStr)
                .setOKStr(oKStr)
                .setTitleTextColor(msgTextColorId)
                .setCancelTextColor(cancelTextColorId).setOKTextColor(oKTextColorId).setMsgTextColor(msgTextColorId)
                .positiveListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 确认拨打电话
                        Utils.telePhoneBroadcast(context, cellPhone);
                    }
                }).build();
        mDialog.show();
    }

    public static void setWeChatQrCodeDialog(final Context context, final String weChatNum, String qrCodeStr) {
        WeChatQrCodeDialog mDialog = new WeChatQrCodeDialog(context).builder()
                .setWeChatCode(weChatNum)
                .setCopyWeChatCodeClick(weChatNum)
                .setWeChatQrCode(qrCodeStr)
                .setWeChatQrCodeLongClick(qrCodeStr);
        mDialog.show();
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources resources = context.getResources();
        return resources.getDisplayMetrics();
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * (getDisplayMetrics(context).densityDpi / 160f);
    }

    public static int convertDpToPixelSize(float dp, Context context) {
        float pixels = convertDpToPixel(dp, context);
        final int res = (int) (pixels + 0.5f);
        if (res != 0) {
            return res;
        } else if (pixels == 0) {
            return 0;
        } else if (pixels > 0) {
            return 1;
        }
        return -1;
    }

    public static void setTextAndStyle(Activity mActivity, String str, int startLength, int endLength, int style, TextView textView) {
        if (isStrNull(str)) {
            SpannableString ss = new SpannableString(str);
            ss.setSpan(new TextAppearanceSpan(mActivity, style),
                    startLength, endLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(ss);
        }
    }

    public static void setTextAndColor(Context mActivity, String str, int startLength, int endLength, int color, TextView textView) {
        if (isStrNull(str)) {
            SpannableString ss = new SpannableString(str);
            ss.setSpan(
                    new ForegroundColorSpan(mActivity.getResources().getColor(color)), startLength, endLength,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(ss);
        }
    }

    public static void setTextStyleAndColor(Context mActivity, String str, int color, int style, TextView textView) {
        if (isStrNull(str)) {
            SpannableString ss = new SpannableString(removeIndexStr(removeIndexStr(str, "#"), "&"));
            String[] split = removeIndexStr(str, "&").split("#");
            if (split.length > 0) {//改变颜色
                StringBuffer sb = new StringBuffer();
                sb.setLength(0);
                List<Integer> xList = new ArrayList<Integer>();
                xList.clear();
                List<StyleAndColorBean> colorList = new ArrayList<StyleAndColorBean>();
                colorList.clear();
                for (int i = 0; i < split.length; i++) {
                    sb.append(split[i]);
                    int index = sb.toString().length();
                    Log.e("TAG", "index = " + index);
                    Log.e("TAG", "sb.toString() = " + sb.toString());
                    xList.add(index);
                }
                Log.e("TAG", "xList.size() = " + xList.size());
                if (xList.size() % 2 != 0) {
                    xList.remove(xList.size() - 1);
                }
                if (xList.size() > 0 && xList.size() % 2 == 0) {
                    for (int i = 0; i < xList.size(); i++) {
                        if (i % 2 != 0) {
                            colorList.add(new StyleAndColorBean(xList.get(i - 1), xList.get(i)));
                        }
                    }
                    for (int i = 0; i < colorList.size(); i++) {
                        Log.e("TAG", "#" + i + " = " + colorList.get(i).getStartIndex());
                        Log.e("TAG", "#" + i + " = " + colorList.get(i).getEndIndex());
                        ss.setSpan(
                                new ForegroundColorSpan(mActivity.getResources().getColor(color)),
                                colorList.get(i).getStartIndex(), colorList.get(i).getEndIndex(),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
            String[] split1 = removeIndexStr(str, "#").split("&");
            if (split1.length > 0) {//改变大小和颜色
                StringBuffer sb = new StringBuffer();
                sb.setLength(0);
                List<Integer> aList = new ArrayList<Integer>();
                aList.clear();
                List<StyleAndColorBean> styleAndColorList = new ArrayList<StyleAndColorBean>();
                styleAndColorList.clear();
                for (int i = 0; i < split1.length; i++) {
                    sb.append(split1[i]);
                    int index = sb.toString().length();
                    aList.add(index);
                }
                if (aList.size() % 2 != 0) {
                    aList.remove(aList.size() - 1);
                }
                if (aList.size() > 0 && aList.size() % 2 == 0) {
                    for (int i = 0; i < aList.size(); i++) {
                        if (i % 2 != 0) {
                            styleAndColorList.add(new StyleAndColorBean(aList.get(i - 1), aList.get(i)));
                        }
                    }
                    for (int i = 0; i < styleAndColorList.size(); i++) {
                        Log.e("TAG", "&" + i + " = " + styleAndColorList.get(i).getStartIndex());
                        Log.e("TAG", "&" + i + " = " + styleAndColorList.get(i).getEndIndex());
                        ss.setSpan(new TextAppearanceSpan(mActivity, style),
                                styleAndColorList.get(i).getStartIndex(), styleAndColorList.get(i).getEndIndex(),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        ss.setSpan(
                                new ForegroundColorSpan(mActivity.getResources().getColor(color)),
                                styleAndColorList.get(i).getStartIndex(), styleAndColorList.get(i).getEndIndex(),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
            textView.setText(ss);
        } else {
            textView.setText("");
        }
    }

    public static int getStartIndex(String str, String indexStr) {
        int startIndex = 0;
        if (isStrNull(str) && str.contains(indexStr)) {
            startIndex = str.indexOf(indexStr);
        }
        return startIndex;
    }

    public static int getLastIndex(String str, String indexStr) {
        int lastIndex = 0;
        if (isStrNull(str) && str.contains(indexStr)) {
            lastIndex = str.lastIndexOf(indexStr) - 1;
        }
        return lastIndex;
    }

    public static String removeIndexStr(String str, String indexStr) {
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        if (isStrNull(str)) {
            String[] split = str.split(indexStr);
            if (split != null && split.length > 0) {
                for (int j = 0; j < split.length; j++) {
                    sb.append(split[j]);
                }
            } else {
                sb.append(str);
            }
        } else {
            sb.append("");
        }
        return sb.toString();
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    //向指定的文件中写入指定的数据
    public static void writeFileData(Context context, String filename, String content) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND);//获得FileOutputStream
            //将要写入的字符串转换为byte数组
            byte[] bytes = content.getBytes();
            fos.write(bytes);//将byte数组写入文件
            fos.close();//关闭文件输出流
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "writeFileData e = " + e.toString());
        }
    }

    //打开指定文件，读取其数据，返回字符串对象
    public static String readFileData(Context context, String fileName) {
        String result = "";
        try {
            FileInputStream fis = context.openFileInput(fileName);
            //获取文件长度
            int lenght = fis.available();
            byte[] buffer = new byte[lenght];
            fis.read(buffer);
            //将byte数组转换成指定格式的字符串
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "readFileData e = " + e.toString());
        }
        return result;
    }

    public static final int STAGGER_IO_BUFFER_SIZE = 8 * 1024;

    private Utils() {
    }

    ;

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (hasHttpConnectionBug()) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap
     * @return size in bytes
     */
    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @SuppressLint("NewApi")
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @SuppressLint("NewApi")
    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @SuppressLint("NewApi")
    public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * Get the memory class of this device (approx. per-app memory limit)
     *
     * @param context
     * @return
     */
    public static int getMemoryClass(Context context) {
        return ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    /**
     * Check if OS version has a http URLConnection bug. See here for more information:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     *
     * @return
     */
    public static boolean hasHttpConnectionBug() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
    }

    /**
     * Check if OS version has built-in external cache dir method.
     *
     * @return
     */
    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Check if ActionBar is available.
     *
     * @return
     */
    public static boolean hasActionBar() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    //截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public static String[] extractAmountMsg(String ptCasinoMsg) {
        String returnAmounts[] = new String[4];
        ptCasinoMsg = ptCasinoMsg.replace(" | ", " ");
        String[] amounts = ptCasinoMsg.split(" ");
        for (int i = 0; i < amounts.length; i++) {
            Pattern p = Pattern.compile("(\\d+\\.\\d+)");
            Matcher m = p.matcher(amounts[i]);
            if (m.find()) {
                returnAmounts[i] = m.group(1);
            } else {
                p = Pattern.compile("(\\d+)");
                m = p.matcher(amounts[i]);
                if (m.find()) {
                    returnAmounts[i] = m.group(1);
                }
            }
        }
        return returnAmounts;
    }

    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view  控件view
     * @param event 焦点位置
     * @return 是否隐藏
     */
    public static void hideKeyboard(MotionEvent event, View view,
                                    Activity activity) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> List<List<T>> partList(List<T> source, int n) {

        if (source == null) {
            return null;
        }

        if (n == 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<List<T>>();
        // 集合长度
        int size = source.size();
        // 余数
        int remaider = size % n;
        System.out.println("余数:" + remaider);
        // 商
        int number = size / n;
        System.out.println("商:" + number);

        for (int i = 0; i < number; i++) {
            List<T> value = source.subList(i * n, (i + 1) * n);
            result.add(value);
        }

        if (remaider > 0) {
            List<T> subList = source.subList(size - remaider, size);
            result.add(subList);
        }
        return result;
    }

    public static void goMarketDialog(final Context context) {
        new AlertDialogNavAndPostVertical(context).builder().setTitle("喵呜~用的怎么样?").setMsg("如果还行,就为我点个赞吧!")
                .setNegativeButton("再逛一会", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        Log.e("TAG", df.format(new Date()));// new Date()为获取当前系统时间
                        SharedPreferenceUtil.getInstance(context).saveBoolean("GOTOMARKET_DIALOG_FALSE", true);
                        SharedPreferenceUtil.getInstance(context).saveString("GOTOMARKET_DIALOG_TIME", df.format(new Date()));
                        SharedPreferenceUtil.getInstance(context).removeData("GOTOMARKET_DIALOG_TRUE");
                    }
                }).setPositiveButton("我要吐槽", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil.getInstance(context).saveBoolean("GOTOMARKET_DIALOG_TRUE", true);
                SharedPreferenceUtil.getInstance(context).removeData("GOTOMARKET_DIALOG_FALSE");
                SharedPreferenceUtil.getInstance(context).removeData("GOTOMARKET_DIALOG_TIME");
                goMarket(context);
            }
        }).setPositiveButton1("五星鼓励", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil.getInstance(context).saveBoolean("GOTOMARKET_DIALOG_TRUE", true);
                SharedPreferenceUtil.getInstance(context).removeData("GOTOMARKET_DIALOG_FALSE");
                SharedPreferenceUtil.getInstance(context).removeData("GOTOMARKET_DIALOG_TIME");
                goMarket(context);
            }
        }).show();
    }

    public static void goToMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            goToMarket.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
            context.startActivity(goToMarket);
        } catch (Exception e) {
            Log.e("TAG", "e = " + e.toString());
            e.printStackTrace();
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap sourceBitmap, int radius) {

        try {

            Bitmap targetBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);

            // 得到画布
            Canvas canvas = new Canvas(targetBitmap);

            // 创建画笔
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            // 值越大角度越明显
            float roundPx = radius;
            float roundPy = radius;

            Rect rect = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
            RectF rectF = new RectF(rect);

            // 绘制
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPy, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(sourceBitmap, rect, rect, paint);

            return targetBitmap;

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void goImageDetail(Context mContext, int position, String[] urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */

    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    /**
     * Bitmap保存成File
     *
     * @param bitmap input bitmap
     * @param name   output file's name
     * @return String output file's path
     */

    public static String bitmap2File(Bitmap bitmap, String path) {
        File f = new File(path);
        if (f.exists()) f.delete();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            return null;
        }
        return f.getAbsolutePath();
    }

    public static int getWidthPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.widthPixels;
        }
        return 0;
    }

    /**
     * 获取系统剪贴板内容
     */
    public static String getClipContent(Activity mActivity) {
        ClipboardManager manager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    /**
     * 清空剪贴板内容
     */
    public static void clearClipboard(Activity mActivity) {
        ClipboardManager manager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            try {
                manager.setPrimaryClip(manager.getPrimaryClip());
                manager.setText(null);
            } catch (Exception e) {
                Log.e("TAG", "清空剪贴板内容异常e = " + e.toString());
            }
        }
    }

    /**
     * 判断Activity是否Destroy
     *
     * @param mActivity
     * @return
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

}

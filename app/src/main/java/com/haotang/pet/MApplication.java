package com.haotang.pet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.VcPlayerLog;
import com.baidu.mapapi.SDKInitializer;
import com.example.playerlibrary.AlivcPlayer.AlivcPlayer;
import com.example.playerlibrary.config.PlayerConfig;
import com.example.playerlibrary.config.PlayerLibrary;
import com.example.playerlibrary.entity.DecoderPlan;
import com.haotang.pet.util.ChannelUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.TimeUtils;
import com.haotang.pet.util.Utils;
import com.hss01248.dialog.StyledDialog;
import com.melink.bqmmsdk.sdk.BQMM;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pet.baseapi.BaseApiApp;
import com.taobao.sophix.SophixManager;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * <p>
 * Title:MApplication
 * </p>
 * <p>
 * Description:主干
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2016-8-15 下午9:02:59
 */
@SuppressLint("NewApi")
public class MApplication extends BaseApiApp {
	public static ArrayList<Activity> listAppoint;
	public static ArrayList<Activity> listAppoint1;
	private static MApplication instance;
	public static final int PLAN_ID_ALI = 1;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//查询补丁
		if ((TimeUtils.getHotfixTime(this)) && isMainProcess()){
			SophixManager.getInstance().queryAndLoadNewPatch();
		}

		StyledDialog.init(getApplicationContext());
		instance = this;
		SDKInitializer.initialize(getApplicationContext());
		initImageLoader(getApplicationContext());
		listAppoint = new ArrayList<Activity>();
		listAppoint1 = new ArrayList<Activity>();
		JPushInterface.setDebugMode(Utils.isLog); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		String cid = JPushInterface.getRegistrationID(getApplicationContext());
		UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,"");
		Utils.mLogError("cid = " + cid);
		if (!cid.isEmpty()) {
			Utils.mLogError("cid = " + cid);
			Global.savePushID(getApplicationContext(), cid);
		}
		// 初始化BQMM
		initBQMM();
		// 清理缓存
		//removeCache();
		ChannelUtil.getChannel(getApplicationContext());

		//查看log
		VcPlayerLog.enableLog();

		//初始化播放器
		AliVcMediaPlayer.init(getApplicationContext());

		PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_ALI, AlivcPlayer.class.getName(), "AlivcPlayer"));
		PlayerConfig.setDefaultPlanId(PLAN_ID_ALI);

		//use default NetworkEventProducer.
		PlayerConfig.setUseDefaultNetworkEventProducer(true);

		PlayerLibrary.init(this);
	}

	private void initBQMM() {
		/**
		 * BQMM集成 首先从AndroidManifest.xml中取得appId和appSecret，然后对BQMM SDK进行初始化
		 */
		BQMM.getInstance().initConfig(getApplicationContext(),"53d3c378e7784e83b71dce4f9d705445", "4e036be994e942c48fd28e33ff1fe955");
	}

	public static MApplication getInstance() {
		return instance;
	}

	// 初始化ImageLoader
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(720, 1280)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.discCacheExtraOptions(720, 1280, null)
				// Can slow ImageLoader, use it carefully (Better don't use
				// it)/设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				//.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.memoryCache(new WeakMemoryCache())
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				// .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																				// (5
																				// s),
																				// readTimeout
																				// (30
																				// s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}
	/**
	 * 获取当前进程名
	 */
	private String getCurrentProcessName() {
		int pid = android.os.Process.myPid();
		String processName = "";
		ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
			if (process.pid == pid) {
				processName = process.processName;
			}
		}
		return processName;
	}
	/**
	 * 包名判断是否为主进程
	 *
	 * @param
	 * @return
	 */
	public boolean isMainProcess() {
		return getApplicationContext().getPackageName().equals(getCurrentProcessName());
	}
}

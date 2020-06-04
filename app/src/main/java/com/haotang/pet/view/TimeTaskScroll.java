package com.haotang.pet.view;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.haotang.pet.adapter.CommodityMarqueeAdapter;
import com.haotang.pet.entity.CommodityMarquee;

import java.util.List;
import java.util.TimerTask;

public class TimeTaskScroll extends TimerTask {
	
	private ListView listView;
	
	public TimeTaskScroll(Activity context, ListView listView, List<CommodityMarquee> list){
		this.listView = listView;
		listView.setAdapter(new CommodityMarqueeAdapter(context, list));
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			listView.smoothScrollBy(1, 0);
		};
	};

	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		handler.sendMessage(msg);
	}

}

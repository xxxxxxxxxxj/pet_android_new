package com.haotang.pet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.collection.ArrayMap;

import com.haotang.pet.R;

import java.util.List;

@SuppressLint({"UseSparseArrays", "NewApi"})
public class SearchAddressResultAdapter extends BaseAdapter {

    private Context context;
    private List<ArrayMap<String, String>> list = null;
    public int selectIndex = 0;
    private String str;
    private ListView listview;
    private ArrayMap<Integer, Object> map;

    public SearchAddressResultAdapter(Context context, List<ArrayMap<String, String>> list, String str/*ListView listview*/) {
        this.context = context;
        this.list = list;
        this.str = str;
        map = new ArrayMap<Integer, Object>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        // TODO 当前复用会出现bug，因数据不多，暂时采用非优化方式解决，后期调试
//		ViewHolder mHolder = null;
//		selectIndex = position;
//		if (view==null) {
//			mHolder = new ViewHolder();
        view = LayoutInflater.from(context).inflate(R.layout.item_searchaddress_result, null);
        TextView textView_service_search_item_unchoose_name = (TextView) view.findViewById(R.id.tv_address_name);
        TextView textView_service_search_item_unchoose_address = (TextView) view.findViewById(R.id.tv_address_loc);
        ImageView iv_address_selected = view.findViewById(R.id.iv_address_selected);
        TextView textView_service_search_address_far = (TextView) view.findViewById(R.id.tv_address_far);
        textView_service_search_item_unchoose_name.setText(list.get(position).get("name"));
        textView_service_search_item_unchoose_address.setText(list.get(position).get("address"));
        if (list.get(position).get("isSecled").equals("yes")){
            iv_address_selected.setVisibility(View.VISIBLE);
        }else {
            iv_address_selected.setVisibility(View.GONE);
        }
        if (Integer.valueOf(list.get(position).get("distance"))==0){
            textView_service_search_address_far.setVisibility(View.GONE);
        }else {
            textView_service_search_address_far.setText(list.get(position).get("distance")+"km");
        }
//        textView_service_search_address_far.setText(list.get(position).get("direction"));
//			view.setTag(mHolder);
//		}else {
//			mHolder = (ViewHolder) view.getTag();
//		}	

//		mHolder.textView_service_search_item_unchoose_name.setText(list.get(position).get("name"));
//		mHolder.textView_service_search_item_unchoose_address.setText(list.get(position).get("address"));
//		if (position==0&&str.equals(list.get(0).get("name"))) {
//			mHolder.textView_service_search_item_unchoose_name.setText("[当前]"+list.get(0).get("name"));
//			mHolder.textView_service_search_item_unchoose_name.setTextColor(Color.parseColor("#FF9942"));
//			mHolder.imageView_add_service_show_item.setBackgroundResource(R.drawable.add_serviceaddress_give_service);
//		}
        return view;
    }


    public class ViewHolder {
        public TextView textView_service_search_item_unchoose_name;
        public TextView textView_service_search_item_unchoose_address;
        public LinearLayout layout_service_search_item_unchoose;
        ImageView imageView_add_service_show_item;
    }


}

package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.WorkerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/7/3
 * @Description:
 */
public class GratuityPriceAdapter extends RecyclerView.Adapter<GratuityPriceAdapter.MyViewHolder> {

    private Context context;

    public GratuityPriceAdapter(Context context) {
        this.context = context;
    }

    private List<WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean> priceList = new ArrayList<>();

    public void setPriceList(List<WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean> priceList) {
        this.priceList = priceList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_gratuity_price,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_price.setText(formatDouble(priceList.get(position).getAmount()));
        if (priceList.get(position).isCheck()){
            holder.rl_itembg.setBackgroundResource(R.drawable.bg_gratuity_checked);
            listener.onItemClick(priceList.get(position));
        }else {
            holder.rl_itembg.setBackgroundResource(R.drawable.bg_gratuity_uncheck);
        }
        holder.rl_itembg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < priceList.size(); i++) {
                    if (position==i){
                        priceList.get(i).setCheck(true);
                    }else {
                        priceList.get(i).setCheck(false);
                    }
                    if (priceList.get(position).isCheck()){
                        holder.rl_itembg.setBackgroundResource(R.drawable.bg_gratuity_checked);
                    }else {
                        holder.rl_itembg.setBackgroundResource(R.drawable.bg_gratuity_uncheck);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_price;
        private RelativeLayout rl_itembg;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_price = (TextView) itemView.findViewById(R.id.tv_gratuity_price);
            rl_itembg = (RelativeLayout) itemView.findViewById(R.id.rl_itembg);
        }
    }
    private ItemClickListener listener;
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    public interface ItemClickListener{
        void onItemClick(WorkerInfo.DataBean.GratuityInfoBeanX.GratuityInfoBean gratuityInfoBean);
    }

    public static String formatDouble(double num) {
        String number1 = String.format("%.6f", num);//只保留小数点后6位
        double number2 = Double.parseDouble(number1);//類型轉換
        if(Math.round(number2)-number2 == 0){
            return String.valueOf((long)number2);
        }
        return String.valueOf(number2);
    }

}

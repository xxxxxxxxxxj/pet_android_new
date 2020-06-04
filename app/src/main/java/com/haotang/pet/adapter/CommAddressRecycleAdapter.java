package com.haotang.pet.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.AddServiceAddressActivity;
import com.haotang.pet.CommonAddressActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.util.Global;

import java.util.List;

/**
 * Created by Administrator on 2018/9/17 0017.
 */

public class CommAddressRecycleAdapter extends RecyclerView.Adapter<CommAddressRecycleAdapter.MyViewHolder> {
    private int addressId;
    public Context mContext;
    public List<CommAddr> mDatas;


    public CommAddressRecycleAdapter(Context mContext, List<CommAddr> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommAddressRecycleAdapter.MyViewHolder viewHolder = null;
        viewHolder = new CommAddressRecycleAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycle_address, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CommAddr commAddr = mDatas.get(position);
        holder.textview_username.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(commAddr.linkman)) {
            holder.textview_username.setVisibility(View.VISIBLE);
            holder.textview_username.setText(commAddr.linkman);
        }
        holder.textview_username_phone.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(commAddr.telephone)) {
            holder.textview_username_phone.setVisibility(View.VISIBLE);
            holder.textview_username_phone.setText(commAddr.telephone);
        }
        holder.address_detail.setText(commAddr.address + "" + commAddr.supplement);
//        Utils.mLogError("接受到的ID "+addressId+" Customer： "+commAddr.Customer_AddressId+" "+(addressId == commAddr.Customer_AddressId));
        if (addressId == commAddr.Customer_AddressId) {
            holder.img_choose.setImageResource(R.drawable.icon_petadd_select);
        } else {
            holder.img_choose.setImageResource(R.drawable.icon_petadd_unselect);
        }
        if (addressId == 0) {
            holder.img_choose.setVisibility(View.GONE);
        }
        if (addressId==0){
            if (commAddr.isShowDel){
                holder.rlDelAddr.setVisibility(View.VISIBLE);
            }else {
                holder.rlDelAddr.setVisibility(View.GONE);
            }
        }
        holder.layout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonAddressActivity.commonAddressActivity.goNextAddress(AddServiceAddressActivity.class, Global.COMMONADDRESS_EDIT_ADDADDRESS, commAddr);
            }
        });
        holder.layout_choose_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressId != 0) {
                    mClickLiner.click(v, position);
                }
            }
        });
        holder.layout_choose_address.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (addressId == 0) {
                    longListener.onItemLongClick(position);
                }
                return false;
            }
        });
        holder.rlDelAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goneDelListener.onItemGoneDelClick(position);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delListener.onItemDelClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_choose;
        private TextView textview_username;
        private TextView textview_username_phone;
        private TextView address_detail;
        private LinearLayout layout_edit;
        private LinearLayout layout_choose_address;
        private ImageView btnDelete;
        private RelativeLayout rlDelAddr;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_choose = (ImageView) itemView.findViewById(R.id.img_choose);
            textview_username = (TextView) itemView.findViewById(R.id.textview_username);
            textview_username_phone = (TextView) itemView.findViewById(R.id.textview_username_phone);
            address_detail = (TextView) itemView.findViewById(R.id.address_detail);
            layout_edit = (LinearLayout) itemView.findViewById(R.id.layout_edit);
            layout_choose_address = (LinearLayout) itemView.findViewById(R.id.layout_choose_address);
            btnDelete = (ImageView) itemView.findViewById(R.id.iv_item_myaddress_del);
            rlDelAddr = itemView.findViewById(R.id.rl_item_myaddress_del);
        }
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public interface OnItemClickRecyleView {
        void click(View v, int position);
    }

    public CommAddressRecycleAdapter.OnItemClickRecyleView mClickLiner = null;

    public void setOnItemClickRecyleView(CommAddressRecycleAdapter.OnItemClickRecyleView mClickLiner) {
        this.mClickLiner = mClickLiner;
    }
    //长按
    private ItemLongClickListener longListener;
    public void setLongClickListener(ItemLongClickListener longListener) {
        this.longListener = longListener;
    }
    public interface ItemLongClickListener{
        void onItemLongClick(int position);
    }

    //删除
    private DelClickListener delListener;
    public void setDelListener(DelClickListener delListener) {
        this.delListener = delListener;
    }
    public interface DelClickListener{
        void onItemDelClick(int position);
    }
    //点击消失
    private GoneDelClickListener goneDelListener;
    public void setGoneDelListener(GoneDelClickListener goneDelListener) {
        this.goneDelListener = goneDelListener;
    }
    public interface GoneDelClickListener{
        void onItemGoneDelClick(int position);
    }

}

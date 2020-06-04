package com.haotang.pet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.pet.GiftCardDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.GiftListAdapter;
import com.haotang.pet.entity.GiftCardList;
import com.haotang.pet.view.NoScollFullLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/10/21
 * @Description:
 */
public class GiftCardListFragment extends Fragment {

    private RecyclerView rvGiftCardList;
    private List<GiftCardList.DataBean.ServiceCardTemplateListBean.TemplatesBean> list = new ArrayList<GiftCardList.DataBean.ServiceCardTemplateListBean.TemplatesBean>();
    private List<GiftCardList.DataBean.ServiceCardTemplateListBean> tempList = new ArrayList<GiftCardList.DataBean.ServiceCardTemplateListBean>();
    private GiftListAdapter giftListAdapter;
    private double lat;
    private double lng;
    private int position;
    private List<GiftCardList.DataBean.ServiceCardTemplateListBean> serviceCardTemplateList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giftcard_list, container, false);
        rvGiftCardList =view.findViewById(R.id.rv_giftcard_list);
        /*if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }*/
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        lng = bundle.getDouble("lng");
        lat = bundle.getDouble("lat");
        position = bundle.getInt("position");
        list = (List<GiftCardList.DataBean.ServiceCardTemplateListBean.TemplatesBean>) bundle.getSerializable("list");
        rvGiftCardList.bringToFront();
        rvGiftCardList.setHasFixedSize(true);
        rvGiftCardList.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(getContext());
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvGiftCardList.setLayoutManager(noScollFullLinearLayoutManager);
        giftListAdapter = new GiftListAdapter(R.layout.item_giftcard_list_item, list);
        rvGiftCardList.setAdapter(giftListAdapter);
        setListener();
        if (list!=null&&list.size()>0){
            giftListAdapter.notifyDataSetChanged();
        }
    }


    private void setListener() {
        giftListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!list.isEmpty() && list.size() > 0) {
                    Intent intent = new Intent(getActivity(), GiftCardDetailActivity.class);
                    intent.putExtra("cardTemplateId", list.get(position).getServiceCardTemplateId());
                    startActivity(intent);
                }
            }
        });
        giftListAdapter.setListener(new GiftListAdapter.TimeEndListener() {
            @Override
            public void onTimeEnd(int position) {
                if (list.get(position).getSaleTimeStart() > 0) {
                    list.get(position).setSaleTimeStart(0);
                    list.get(position).setIsAllSale("距离结束");
                    giftListAdapter.notifyItemChanged(position);
                } else if (list.get(position).getSaleTimeStart() == 0) {
                    list.get(position).setIsShowTime(1);
                    list.get(position).setIsAllSale("已结束");
                    giftListAdapter.notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

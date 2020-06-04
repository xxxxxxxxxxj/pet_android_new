package com.haotang.pet.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haotang.pet.R;
import com.haotang.pet.adapter.ShopAreaAdapter;
import com.haotang.pet.adapter.ShopListAdapter;
import com.haotang.pet.entity.ShopListBean;
import com.haotang.pet.view.GridSpacingItemDecoration;
import com.haotang.pet.view.NoScollFullGridLayoutManager;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/10/23
 * @Description:门店列表
 */
public class ShopListFragment extends Fragment {

    private RecyclerView rv_shop_list;
    private RecyclerView mgv_shoplist_area;
    private List<ShopListBean.DataBean.RegionsBean.RegionMapBean> regionMap;
    private List<ShopListBean.DataBean.RegionsBean.RegionMapBean> region = new ArrayList<>();
    private ShopAreaAdapter shopAreaAdapter;
    private List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> shops = new ArrayList<>();
    private ShopListAdapter shopListAdapter;
    private int allShopNum;
    private int areaClickPosition;
    private double addr_lat;
    private double addr_lng;
    private int previous;
    private StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
        rv_shop_list = view.findViewById(R.id.listView_shop_list);
        mgv_shoplist_area = view.findViewById(R.id.rv_shoplist_area);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        regionMap = (List<ShopListBean.DataBean.RegionsBean.RegionMapBean>) bundle.getSerializable("list");
        addr_lat = bundle.getDouble("addr_lat");
        addr_lng = bundle.getDouble("addr_lng");
        previous = bundle.getInt("previous");
        if (regionMap != null && regionMap.size() > 0) {
            List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> lacalShops = new ArrayList<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean>();
            lacalShops.clear();
            for (int i = 0; i < regionMap.size(); i++) {
                ShopListBean.DataBean.RegionsBean.RegionMapBean regionMapBean = regionMap.get(i);
                if (regionMapBean != null) {
                    List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> shops = regionMapBean.getShops();
                    if (shops != null && shops.size() > 0) {
                        regionMapBean.setShopNum(shops.size());
                        for (int j = 0; j < shops.size(); j++) {
                            ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean shopsBean = shops.get(j);
                            if (shopsBean != null) {
                                shopsBean.setRegion(regionMapBean.getRegion());
                            }
                            lacalShops.add(shopsBean);
                        }
                    }
                }
            }
            region.clear();
            for (int i = 0; i < regionMap.size(); i++) {
                List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> shopsBeans = regionMap.get(i).getShops();
                for (int j = 0; j < shopsBeans.size(); j++) {
                    shopsBeans.get(j).setClassId(i);
                    shopsBeans.get(j).setRegion(regionMap.get(i).getRegion());
                    allShopNum++;
                }
                region.add(new ShopListBean.DataBean.RegionsBean.RegionMapBean(regionMap.get(i).getRegion(), regionMap.get(i).getAreacode(), regionMap.get(i).getSelected(), regionMap.get(i).getShops().size(), shopsBeans));
            }
            region.add(0, new ShopListBean.DataBean.RegionsBean.RegionMapBean("全部", 0, 0, allShopNum, lacalShops));
            for (int i = 0; i < region.size(); i++) {
                if (region.get(i).getSelected() == 1) {
                    areaClickPosition = i;
                    break;
                }
            }
            region.get(areaClickPosition).setSelected(1);
            //区域
            mgv_shoplist_area.setHasFixedSize(true);
            mgv_shoplist_area.setNestedScrollingEnabled(false);
            NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                    NoScollFullGridLayoutManager(mgv_shoplist_area, getActivity(), 4, GridLayoutManager.VERTICAL, false);
            noScollFullGridLayoutManager.setScrollEnabled(false);
            mgv_shoplist_area.setLayoutManager(noScollFullGridLayoutManager);
            mgv_shoplist_area.addItemDecoration(new GridSpacingItemDecoration(4,
                    getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                    getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                    true));
            shopAreaAdapter = new ShopAreaAdapter(getContext(), region);
            mgv_shoplist_area.setAdapter(shopAreaAdapter);
            //店铺
            shops.clear();
            shops.addAll(region.get(areaClickPosition).getShops());
            //区域悬停用HeaderId
            for(int j=0;j<shops.size();j++){
                if (j == 0) {
                    shops.get(j).setHeaderId(j);
                } else {
                    if (shops.get(j - 1).getClassId() == shops.get(j).getClassId()) {
                        shops.get(j).setHeaderId(shops.get(j - 1).getHeaderId());
                    } else {
                        shops.get(j).setHeaderId(j);
                    }
                }
            }
            rv_shop_list.setLayoutManager(new LinearLayoutManager(getActivity()));
            shopListAdapter = new ShopListAdapter(getActivity(), shops,addr_lat,addr_lng,previous);
            rv_shop_list.setAdapter(shopListAdapter);
            stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(shopListAdapter);
            rv_shop_list.addItemDecoration(stickyRecyclerHeadersDecoration);
            setListener();
        }
    }

    private void setListener() {
        shopAreaAdapter.setListener(new ShopAreaAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (region != null && region.size() > 0) {
                    for (int i = 0; i < region.size(); i++) {
                        ShopListBean.DataBean.RegionsBean.RegionMapBean regionMapBean = region.get(i);
                        if (regionMapBean != null) {
                            if (i == position) {
                                regionMapBean.setSelected(1);
                            } else {
                                regionMapBean.setSelected(0);
                            }
                        }
                    }
                    shopAreaAdapter.notifyDataSetChanged();
                    shops.clear();
                    shops.addAll(region.get(position).getShops());
                    //区域悬停用HeaderId
                    for(int j=0;j<shops.size();j++){
                        if (j == 0) {
                            shops.get(j).setHeaderId(j);
                        } else {
                            if (shops.get(j - 1).getClassId() == shops.get(j).getClassId()) {
                                shops.get(j).setHeaderId(shops.get(j - 1).getHeaderId());
                            } else {
                                shops.get(j).setHeaderId(j);
                            }
                        }
                    }
                    shopListAdapter.notifyDataSetChanged();
                    stickyRecyclerHeadersDecoration.invalidateHeaders();
                }
            }
        });
    }
}

package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.ADActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServicePlayVideoActivity;
import com.haotang.pet.encyclopedias.bean.EncyContentBean;
import com.haotang.pet.entity.PetCircleBanner;
import com.haotang.pet.entity.ServiceTypeBanner;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.adapter.LoopPagerAdapter;

import java.util.ArrayList;

public class BannerBathLoopAdapter extends LoopPagerAdapter {
    private ArrayList<String> list = null;
    private int index = -1;
    private ArrayList<PetCircleBanner> petCircleList = null;
    private ArrayList<String> ConvideoList;
    private ArrayList<ServiceTypeBanner> listBanner = null;
    private final Activity context;
    private EncyContentBean encyContentBean = null;
    public boolean isYuanJiao = false;
    public BannerBathLoopAdapter(Activity context,RollPagerView viewPager, ArrayList<String> list) {
        super(viewPager);
        this.list = list;
        this.context = context;
    }
    public void setYuanJiao(boolean isYuanJiao) {
        this.isYuanJiao = isYuanJiao;
    }
    public void setIsSwim(int index) {
        this.index = index;
    }

    public void setPetCircleBanner(ArrayList<PetCircleBanner> petCircleList) {
        this.petCircleList = petCircleList;
    }

    public void setServiceType(ArrayList<ServiceTypeBanner> listBanner) {
        this.listBanner = listBanner;
    }
    public void setEncyBanner(EncyContentBean encyContentBean){
        this.encyContentBean = encyContentBean;
    }
    @Override
    public View getView(ViewGroup container, final int position) {
        final ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        if (list != null) {
            if (list.get(position).contains("video")) {
                String[] videos = list.get(position).split("\\|\\|");
                GlideUtil.loadImg(container.getContext(), videos[1], view, R.drawable.servicefeatureloading);
            } else {
                if (isYuanJiao){
                    view.setBackgroundColor(container.getContext().getResources().getColor(R.color.white));
                    GlideUtil.loadRoundImg(container.getContext(),list.get(position),view,R.drawable.servicefeatureloading,5);
                }else {
                    GlideUtil.loadImg(container.getContext(), list.get(position), view, R.drawable.servicefeatureloading);
                }
            }
        }
        if (index != 1) {
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (petCircleList != null) {
                        if (petCircleList.get(position).point > 0) {
                            Utils.goService(context, petCircleList.get(position).point, petCircleList.get(position).backup);
                        } else {
                            goNextPetCircle(view.getContext(), position);
                        }
                    }else if (encyContentBean !=null){
                        if (encyContentBean.point>0){
                            Utils.goService(context, encyContentBean.point, encyContentBean.backup);
                        }else {
                            goNextPetCircle(view.getContext(), position);
                        }
                    } else if (listBanner != null) {
                        goNextPetCircle(view.getContext(), position);
                    } else {
                        if (list != null) {
                            if (list.get(position).contains("video")) {
                                goNextServicePlayVideo(view.getContext(), position);
                            } else {
                                goNext(view.getContext(), position);
                            }
                        }
                    }
//        			if (petCircleList==null) {
//        				if (list.get(position).contains("video")) {
//        					goNextServicePlayVideo(view.getContext(), position);
////        					ToastUtil.showToastShortCenter(view.getContext(), "这个是视频");
//						}else {
//							goNext(view.getContext(), position);
//						}
//					}else {
//						goNextPetCircle(view.getContext(), position);
//					}
                }
            });
        }
        return view;
    }

    private void goNext(Context context, int index) {
        String[] pics;
        if (list.size() <= 0) {
            return;
        } else {
            ConvideoList = new ArrayList<String>();
            ConvideoList.clear();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).startsWith("video")) {
                    ConvideoList.add(list.get(i));
                }
            }
            if (ConvideoList.size() > 0) {
                pics = new String[ConvideoList.size()];
                for (int i = 0; i < ConvideoList.size(); i++) {
                    pics[i] = ConvideoList.get(i);
                }
            } else {
                pics = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).contains("video")) {
                        pics[i] = list.get(i);
                    }
                }
            }

        }
        Utils.imageBrower(context, index, pics);
    }

    private void goNextPetCircle(Context context, int index) {
        Intent intent = new Intent(context, ADActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        ((Activity) context).getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("index", index);
        intent.putExtra("previous", Global.PET_CIRCLE_TO_H5);
        try {
            if (petCircleList != null) {
                if (!TextUtils.isEmpty(petCircleList.get(index).imgLink) && petCircleList.get(index).imgLink != null) {
                    intent.putExtra("url", petCircleList.get(index).imgLink);
                    context.startActivity(intent);
                }
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            if (listBanner != null) {
                if (!TextUtils.isEmpty(listBanner.get(index).url) && listBanner.get(index).url != null) {
                    intent.putExtra("url", listBanner.get(index).url);
                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void goNextServicePlayVideo(Context context, int index) {
        Intent intent = new Intent(context, ServicePlayVideoActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        ((Activity) context).getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        intent.putExtra("videourl", list.get(index));
        context.startActivity(intent);
    }

    @Override
    protected int getRealCount() {
        // TODO Auto-generated method stub
        if (list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }

}

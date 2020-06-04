package com.haotang.pet.entity.mallEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航条件例如 品牌、适用对象 使用阶段 宠物体型 之类
 * Created by Administrator on 2017/8/29.
 */

public class NavigationCondition {
    public String NavigationName;//导航条件name
    public String NavigationOldName;//导航条件name
    public int id;//例如 品牌的id
    public int sort;//
    public int isDel;//
    public String created;//
    public int isOpen=0;//表示下方gridView是否开启 0 未开启 1 开启
    public String bottonChooseStr=null;//底部品牌选中放到顶部列表 如果这个字段不为null 就展示这个字段 否则展示 NavigationName
    public String publicAttribute;//publicAttribute=1,2,3
    public List<NavigationCondition.NavigationOpenDetail> openDetailList = new ArrayList<>();

    public class NavigationOpenDetail{
        public String NavigationOpenDetailName;//例如打开品牌里边所对应的每一个name  对应name
        public int id;//每个名字对应的id  对应 id
        public int isChoose=0;//0 非选中 1选中
        public int typeId;//
        public int sort;//
        public int isDel;//
        public String created;//

        /**
         * "id": 1,
         "name": "皇家",
         "typeId": 1,
         "sort": 0,
         "isDel": 0,
         "created": 1504094461000
         */
    }
}

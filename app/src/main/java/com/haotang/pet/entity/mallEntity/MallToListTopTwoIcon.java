package com.haotang.pet.entity.mallEntity;

import com.haotang.pet.entity.Addr;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class MallToListTopTwoIcon {//这个算是二级分类
    public int id;
    public String  title;
    public int pid;
    public int level;
    public int sort;
    public int status;
    public int isDel;
    public String  created;
    public String  updateTime;
    public List<MallToListThr> ThrList = new ArrayList<>();
    public static MallToListTopTwoIcon json2Entity(JSONObject object1){
        MallToListTopTwoIcon mallToListTopTwoIcon = new MallToListTopTwoIcon();
        try{
            if (object1.has("id")&&!object1.isNull("id")){
                mallToListTopTwoIcon.id = object1.getInt("id");
            }
            if (object1.has("title")&&!object1.isNull("title")){
                mallToListTopTwoIcon.title = object1.getString("title");
            }
            if (object1.has("pid")&&!object1.isNull("pid")){
                mallToListTopTwoIcon.pid = object1.getInt("pid");
            }
            if (object1.has("level")&&!object1.isNull("level")){
                mallToListTopTwoIcon.level = object1.getInt("level");
            }
            if (object1.has("sort")&&!object1.isNull("sort")){
                mallToListTopTwoIcon.sort = object1.getInt("sort");
            }
            if (object1.has("status")&&!object1.isNull("status")){
                mallToListTopTwoIcon.status = object1.getInt("status");
            }
            if (object1.has("isDel")&&!object1.isNull("isDel")){
                mallToListTopTwoIcon.isDel = object1.getInt("isDel");
            }
            if (object1.has("created")&&!object1.isNull("created")){
                mallToListTopTwoIcon.created = object1.getString("created");
            }
            if (object1.has("updateTime")&&!object1.isNull("updateTime")){
                mallToListTopTwoIcon.updateTime = object1.getString("updateTime");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mallToListTopTwoIcon;
    }
    public class MallToListThr{//这个算是三级分类
        public int id;
        public String title;
        public int pid;
        public int level;
        public int sort;
        public int status;
        public int isDel;
        public String created;
        public String updateTime;
        public MallToListThr json2Entity(JSONObject object1){
            MallToListTopTwoIcon.MallToListThr mallToListThr = new MallToListTopTwoIcon().new MallToListThr();
            try{
                if (object1.has("id")&&!object1.isNull("id")){
                    mallToListThr.id = object1.getInt("id");
                }
                if (object1.has("title")&&!object1.isNull("title")){
                    mallToListThr.title = object1.getString("title");
                }
                if (object1.has("pid")&&!object1.isNull("pid")){
                    mallToListThr.pid = object1.getInt("pid");
                }
                if (object1.has("level")&&!object1.isNull("level")){
                    mallToListThr.level = object1.getInt("level");
                }
                if (object1.has("sort")&&!object1.isNull("sort")){
                    mallToListThr.sort = object1.getInt("sort");
                }
                if (object1.has("status")&&!object1.isNull("status")){
                    mallToListThr.status = object1.getInt("status");
                }
                if (object1.has("isDel")&&!object1.isNull("isDel")){
                    mallToListThr.isDel = object1.getInt("isDel");
                }
                if (object1.has("created")&&!object1.isNull("created")){
                    mallToListThr.created = object1.getString("created");
                }
                if (object1.has("updateTime")&&!object1.isNull("updateTime")){
                    mallToListThr.updateTime = object1.getString("updateTime");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return mallToListThr;
        }
    }
}

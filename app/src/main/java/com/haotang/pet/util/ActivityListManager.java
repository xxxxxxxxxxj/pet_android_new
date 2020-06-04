package com.haotang.pet.util;

import android.app.Activity;
import android.os.Process;
import android.util.Log;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/5/23 14:24
 */
public class ActivityListManager {
    private List<Activity> mActivityList;
    private Activity mCurrentActivity;

    public ActivityListManager() {
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    public Activity getCurrentActivity() {
        return this.mCurrentActivity != null?this.mCurrentActivity:null;
    }

    public Activity getTopActivity() {
        if(this.mActivityList == null) {
            Log.w("TAG","mActivityList == null when getTopActivity()");
            return null;
        } else {
            return this.mActivityList.size() > 0?(Activity)this.mActivityList.get(this.mActivityList.size() - 1):null;
        }
    }

    public List<Activity> getActivityList() {
        if(this.mActivityList == null) {
            this.mActivityList = new LinkedList();
        }

        return this.mActivityList;
    }

    public void addActivity(Activity activity) {
        Class var2 = ActivityListManager.class;
        synchronized(ActivityListManager.class) {
            List activities = this.getActivityList();
            if(!activities.contains(activity)) {
                activities.add(activity);
            }

        }
    }

    public void removeActivity(Activity activity) {
        if(this.mActivityList == null) {
            Log.w("TAG","mActivityList == null when removeActivity(Activity)");
        } else {
            Class var2 = ActivityListManager.class;
            synchronized(ActivityListManager.class) {
                if(this.mActivityList.contains(activity)) {
                    this.mActivityList.remove(activity);
                }

            }
        }
    }

    public Activity removeActivity(int location) {
        if(this.mActivityList == null) {
            Log.w("TAG","mActivityList == null when removeActivity(int)");
            return null;
        } else {
            Class var2 = ActivityListManager.class;
            synchronized(ActivityListManager.class) {
                return location > 0 && location < this.mActivityList.size()?(Activity)this.mActivityList.remove(location):null;
            }
        }
    }

    public void killActivity(Class<?> activityClass) {
        if(this.mActivityList == null) {
            Log.w("TAG","mActivityList == null when killActivity(Class)");
        } else {
            Class var2 = ActivityListManager.class;
            synchronized(ActivityListManager.class) {
                Iterator iterator = this.getActivityList().iterator();

                while(iterator.hasNext()) {
                    Activity next = (Activity)iterator.next();
                    if(next.getClass().equals(activityClass)) {
                        iterator.remove();
                        next.finish();
                    }
                }

            }
        }
    }

    public boolean activityInstanceIsLive(Activity activity) {
        if(this.mActivityList == null) {
            Log.w("TAG","mActivityList == null when activityInstanceIsLive(Activity)");
            return false;
        } else {
            return this.mActivityList.contains(activity);
        }
    }

    public boolean activityClassIsLive(Class<?> activityClass) {
        if(this.mActivityList == null) {
            Log.w("TAG","mActivityList == null when activityClassIsLive(Class)");
            return false;
        } else {
            Iterator var2 = this.mActivityList.iterator();

            Activity activity;
            do {
                if(!var2.hasNext()) {
                    return false;
                }

                activity = (Activity)var2.next();
            } while(!activity.getClass().equals(activityClass));

            return true;
        }
    }

    public Activity findActivity(Class<?> activityClass) {
        if(this.mActivityList == null) {
            Log.w("TAG","mActivityList == null when findActivity(Class)");
            return null;
        } else {
            Iterator var2 = this.mActivityList.iterator();

            Activity activity;
            do {
                if(!var2.hasNext()) {
                    return null;
                }

                activity = (Activity)var2.next();
            } while(!activity.getClass().equals(activityClass));

            return activity;
        }
    }

    public void killAll() {
        Class var1 = ActivityListManager.class;
        synchronized(ActivityListManager.class) {
            Iterator iterator = this.getActivityList().iterator();

            while(iterator.hasNext()) {
                Activity next = (Activity)iterator.next();
                iterator.remove();
                next.finish();
            }

        }
    }

    public void killAllExclude(Class... excludeActivityClasses) {
        List excludeList = Arrays.asList(excludeActivityClasses);
        Class var3 = ActivityListManager.class;
        synchronized(ActivityListManager.class) {
            Iterator iterator = this.getActivityList().iterator();

            while(iterator.hasNext()) {
                Activity next = (Activity)iterator.next();
                if(!excludeList.contains(next.getClass())) {
                    iterator.remove();
                    next.finish();
                }
            }

        }
    }

    public void killAllExclude(String... excludeActivityName) {
        List excludeList = Arrays.asList(excludeActivityName);
        Class var3 = ActivityListManager.class;
        synchronized(ActivityListManager.class) {
            Iterator iterator = this.getActivityList().iterator();

            while(iterator.hasNext()) {
                Activity next = (Activity)iterator.next();
                if(!excludeList.contains(next.getClass().getName())) {
                    iterator.remove();
                    next.finish();
                }
            }

        }
    }

    public void exitApp() {
        try {
            this.killAll();
            Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }
}

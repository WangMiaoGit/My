package com.ricoh.wm.my.collector;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录打开的activity 随时退出程序
 * Created by 2017063001 on 2018/3/8.
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public  static void finishAllActivity(){
        for (Activity activity: activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}

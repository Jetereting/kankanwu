package eiyou.us.kankanwu.utils;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Au on 2016/3/19.
 *
 * //用户相关
 * userId 用于存放用户ID int
 * userQqName 用户QQ昵称
 * userQqId 用户QQid
 * userAvatar 用户头像
 * userPhone 用户手机号
 * userStatus 用户状态 int
 * schoolId 用户学校  int
 *
 * //注册
 * registerPhone 注册手机号
 *
 * //发布兼职
 * publishTitle 兼职标题
 * publishDescribe 兼职描述
 * publishSalary 兼职薪资
 * publishType 兼职类型
 * publishSalaryType 薪资类型
 * publishSex 性别要求
 * publishNeed 需求人数
 * publishQq QQ
 * publishPhone 手机号
 * publishWorkDate 工作时间
 *
 * //筛选
 * filterBlacklist 是否看黑命单的
 * filterSex 性别
 * filterNeed 需求人数
 * filterType 兼职类型
 * filterTypeDetail 兼职类型详情
 * filterSql 筛选sql
 *
 * //图片
 * imgWelcomeUrl 欢迎页图片url
 *
 * //activity
 *  fromActivity 从。。Activity来
 *
 *  //webView
 *  webViewNoAdJs 去广告的js
 */
public class SP {
    public static void cleanAll(Context context){
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().commit();
    }
    public static void put(Context context,String key,String value){
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }
    public static void put(Context context,String key,int value){
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }
    public static void put(Context context,String key,boolean value){
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }
    public static void put(Context context,String key,Set<String> value){
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putStringSet(key, value).commit();
    }
    public static int getInt(Context context,String key){
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt(key, 0);
    }
    public static boolean getBoolean(Context context,String key){
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean(key, false);
    }
    public static String  getString(Context context,String key){
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString(key, "");
    }
    public static Set<String> getSetString(Context context, String key){
        Set<String> typeDetail=new HashSet<>();
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getStringSet(key,typeDetail);
    }

}

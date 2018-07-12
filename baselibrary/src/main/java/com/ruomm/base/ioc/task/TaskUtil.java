package com.ruomm.base.ioc.task;

import android.text.TextUtils;

import com.ruomm.baseconfig.BaseConfig;

import org.bouncycastle.jce.provider.symmetric.ARC4;

import java.lang.reflect.Constructor;

public class TaskUtil {
    public static  <T> T getTask(String taskName){
        {
            T t=null;
            if(TextUtils.isEmpty(taskName))
            {
                return null;
            }
            try {
                Class<?> onwClass = TaskUtil.class.getClassLoader().loadClass(taskName);
                Constructor<?> constructor = onwClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object object = constructor.newInstance();
                t = (T) object;

            }
            catch (Exception ex) {
                ex.printStackTrace();
                t=null;
            }
            return t;
        }
    }
}

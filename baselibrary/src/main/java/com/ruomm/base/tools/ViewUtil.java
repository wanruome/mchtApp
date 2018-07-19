package com.ruomm.base.tools;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

public class ViewUtil {
    public static int getEditTextMaxLength(EditText et) {
        int length = 0;
        try {
            InputFilter[] inputFilters = et.getFilters();
            for (InputFilter filter : inputFilters) {
                Class<?> c = filter.getClass();
                if (c.getName().equals("android.text.InputFilter$LengthFilter")) {
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            length=0;
        }
        return length;
    }
    public static void setTextParamNotEmpty(TextView textView,int color){
        String text=textView.getText().toString();
        SpannableStringBuilder sb=new SpannableStringBuilder();
        sb.append(textView.getText());
        SpannableString letterNeed=new SpannableString("*");
        SpanStringUtil.setForecolor(letterNeed,color);
        sb.append(letterNeed);
        textView.setText(sb);

    }
    /**
     * 添加桌面图标快捷方式
     * @param activity Activity对象
     * @param name 快捷方式名称
     * @param icon 快捷方式图标
     * @param actionIntent 快捷方式图标点击动作
     */
    public static void addShortcut(Activity activity, String name, Bitmap icon, Intent actionIntent,int shortCutRequestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            //  创建快捷方式的intent广播
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            // 添加快捷名称
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            //  快捷图标是允许重复(不一定有效)
            shortcut.putExtra("duplicate", false);
            // 快捷图标
            // 使用资源id方式
//            Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, R.mipmap.icon);
//            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
            // 使用Bitmap对象模式
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
            // 添加携带的下次启动要用的Intent信息
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
            // 发送广播
            activity.sendBroadcast(shortcut);
        } else {
            ShortcutManager shortcutManager = (ShortcutManager) activity.getSystemService(Context.SHORTCUT_SERVICE);
            if (null == shortcutManager) {
                // 创建快捷方式失败
                Log.e("MainActivity", "Create shortcut failed");
                return;
            }
            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(activity, name)
                    .setShortLabel(name)
                    .setIcon(Icon.createWithBitmap(icon))
                    .setIntent(actionIntent)
                    .setLongLabel(name)
                    .build();

            shortcutManager.requestPinShortcut(shortcutInfo, PendingIntent.getActivity(activity,
                    shortCutRequestCode, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT).getIntentSender());
        }
    }

}

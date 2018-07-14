package com.ruomm.base.tools;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
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
}

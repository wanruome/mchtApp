package com.ruomm.base.tools;
/**
 * Span文字样式设定类
 */
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;

public class SpanStringUtil {
	public static void setSpan(SpannableString spanString, Object what) {
		spanString.setSpan(what, 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	//设置前景色
	public static void setForecolor(SpannableString spanString, int colorvalue) {
		ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(colorvalue);
		spanString.setSpan(foregroundColorSpan, 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	//设置文字大小
	public static void setTextsize(SpannableString spanString, int textsize) {
		int size = textsize;
		if (size <= 0) {
			size = 28;
		}
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(size);
		spanString.setSpan(span, 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	//设置前景色和文字大小
	public static void setTextSizeAndForceColor(SpannableString spanString, int textsize, int colorvalue) {
		setTextsize(spanString, textsize);
		setForecolor(spanString, colorvalue);
	}
	//设置文字删除线
	public static void setStrikethrough(SpannableString spanString) {
		spanString.setSpan(new StrikethroughSpan(), 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
}

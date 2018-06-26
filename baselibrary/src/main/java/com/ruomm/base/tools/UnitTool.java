package com.ruomm.base.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class UnitTool {

	// /**
	// *
	// * @Title: getWindowScreenWidth
	// * @Description: 获取屏幕宽度
	// * @param @param context
	// * @param @return
	// * @return int
	// * @throws
	// */
	// public static int getWindowScreenWidth(Context context) {
	// return ((Activity) context).getWindowManager().getDefaultDisplay()
	// .getWidth();
	// }
	//
	// /**
	// *
	// * @Title: getWindowScreenHeight
	// * @Description: 获取屏幕高度
	// * @param @param context
	// * @param @return
	// * @return int
	// * @throws
	// */
	// public static int getWindowScreenHeight(Context context) {
	// return ((Activity) context).getWindowManager().getDefaultDisplay()
	// .getHeight();
	// }
	//
	// /**
	// * 根据手机的分辨率�?dp 的单�?转成�?px(像素)
	// */
	// public static int dip2px(Context context, float dpValue) {
	// final float scale = context.getResources().getDisplayMetrics().density;
	// return (int) (dpValue * scale + 0.5f);
	// }
	//
	// /**
	// * 根据手机的分辨率�?px(像素) 的单�?转成�?dp
	// */
	// public static int px2dip(Context context, float pxValue) {
	// final float scale = context.getResources().getDisplayMetrics().density;
	// return (int) (pxValue / scale + 0.5f);
	// }
	//
	// /**
	// * 将sp值转换为px值，保证文字大小不变
	// *
	// * @param spValue
	// * @param fontScale
	// * （DisplayMetrics类中属性scaledDensity）
	// * @return
	// */
	// public static int sp2px(Context context, float spValue) {
	// final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	// return (int) (spValue * fontScale + 0.5f);
	// }
	//
	//
	// // 通过文件名判断是什么类型的文件
	// public static boolean checkEndsWithInStringArray(String checkItsEnd,
	// String[] fileEndings) {
	// for (String aEnd : fileEndings) {
	// if (checkItsEnd.endsWith(aEnd))
	// return true;
	// }
	// return false;
	// }
	//
	//
	// // 取最大值格式，如果超过某个最大值就显示为max+
	// public static String getMaxFormat(int num, int max) {
	// if (num <= max)
	// return "" + num;
	// return max + "+";
	// }
	// /**
	// * 获取状态栏的高度的方法
	// * @param context
	// * @return
	// */
	// public static int getZTHeight(Context context){
	// return((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	// /*Rect frame = new Rect();
	// ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	// return frame.top;*/
	// }

	/**
	 * edit by zg
	 * 
	 * @Title: binSearchT
	 * @Description: 查找集合列表中第一个与字母相匹配的位置
	 * @param pus
	 *            目标集合
	 * @param s
	 *            要匹配的字母
	 * @param methodName
	 *            集合中获取拼音的方法名
	 * @return int
	 * @throws
	 */
	public synchronized static <T> int binSearchT(List<T> pus, String s, String methodName) {
		if (ArrayUtil.isListEmpty(pus)) {
			return -1;
		}
		Method method = null;
		for (int i = 0; i < pus.size(); i++) {
			T t = pus.get(i);
			try {
				if (null == method) {
					method = t.getClass().getMethod(methodName);
					// method = t.getClass().getMethod(methodName, null);
				}
				// String pinyin = (String) method.invoke(t, null);
				String pinyin = (String) method.invoke(t);
				if (!StringUtils.isEmpty(pinyin)) {
					if (s.equalsIgnoreCase(pinyin.substring(0, 1))) { // 不区分大小写
						return i;
					}
				}

			}
			catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

}

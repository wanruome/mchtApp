package com.ruomm.base.tools;

/**   
*    
* 项目名称：工具类   
* 
* 类名称：ArrayUtils   
* 
* 类描述：   数组工具类，可用于数组常用操作
* 
* 创建人：王龙能
* 
* 创建时间：2014-3-11 下午2:04:13   
* 
* 修改人：王龙能  
* 
* 修改时间：2014-3-11 下午2:04:13   
* 
* 修改备注：   
* 
* @version    
*    
*/
public class ArrayUtils {

	/**
     * 为空或它的长度为0
     * 
     * @param <V>
     * @param sourceArray
     * @return
     */
    public static <V> boolean isEmpty(V[] sourceArray) {
        return (sourceArray == null || sourceArray.length == 0);
    }

    /**
     * 获得目标元素的最后一个元素，在第一个匹配的目标元素前后
     * <ul>
     * <li>如果数组为空，返回defaultvalue </li>
     * <li>如果目标元素是不存在的数组，返回defaultvalue</li>
     * <li>如果目标元素存在于阵列及其指数是不是0，返回最后一个元素</li>
     * <li>如果目标元素存在于阵列及其指数为0，如果是真的iscircle返回数组的最后一个，其他的</li>
     * </ul>
     * 
     * @param <V>
     * @param sourceArray
     * @param value value of target element
     * @param defaultValue default return value
     * @param isCircle whether is circle
     * @return
     */
    public static <V> V getLast(V[] sourceArray, V value, V defaultValue, boolean isCircle) {
        if (isEmpty(sourceArray)) {
            return defaultValue;
        }

        int currentPosition = -1;
        for (int i = 0; i < sourceArray.length; i++) {
            if (ObjectUtils.isEquals(value, sourceArray[i])) {
                currentPosition = i;
                break;
            }
        }
        if (currentPosition == -1) {
            return defaultValue;
        }

        if (currentPosition == 0) {
            return isCircle ? sourceArray[sourceArray.length - 1] : defaultValue;
        }
        return sourceArray[currentPosition - 1];
    }

    /**
     * g获得目标元素的下一个元素，后的第一个匹配的目标元素前后
     * <ul>
     * <li>如果数组为空，返回defaultvalue</li>
     * <li>如果目标元素是不存在的数组，返回defaultvalue</li>
     * <li>如果目标元素存在于阵列而不是最后一个数组，返回下一个元素</li>
     * <li>i如果目标元素存在于阵列和最后一个数组，如果iscircle返回数组中的第一个是真，否则返回defaultvalue</li>
     * </ul>
     * 
     * @param <V>
     * @param sourceArray
     * @param value value of target element
     * @param defaultValue default return value
     * @param isCircle whether is circle
     * @return
     */
    public static <V> V getNext(V[] sourceArray, V value, V defaultValue, boolean isCircle) {
        if (isEmpty(sourceArray)) {
            return defaultValue;
        }

        int currentPosition = -1;
        for (int i = 0; i < sourceArray.length; i++) {
            if (ObjectUtils.isEquals(value, sourceArray[i])) {
                currentPosition = i;
                break;
            }
        }
        if (currentPosition == -1) {
            return defaultValue;
        }

        if (currentPosition == sourceArray.length - 1) {
            return isCircle ? sourceArray[0] : defaultValue;
        }
        return sourceArray[currentPosition + 1];
    }

    /**
     * @see {@link ArrayUtils#getLast(Object[], Object, Object, boolean)} defaultValue is null
     */
    public static <V> V getLast(V[] sourceArray, V value, boolean isCircle) {
        return getLast(sourceArray, value, null, isCircle);
    }

    /**
     * @see {@link ArrayUtils#getNext(Object[], Object, Object, boolean)} defaultValue is null
     */
    public static <V> V getNext(V[] sourceArray, V value, boolean isCircle) {
        return getNext(sourceArray, value, null, isCircle);
    }

    /**
     * @see {@link ArrayUtils#getLast(Object[], Object, Object, boolean)} Object is Long
     */
    public static long getLast(long[] sourceArray, long value, long defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Long[] array = ObjectUtils.transformLongArray(sourceArray);
        return getLast(array, value, defaultValue, isCircle);

    }

    /**
     * @see {@link ArrayUtils#getNext(Object[], Object, Object, boolean)} Object is Long
     */
    public static long getNext(long[] sourceArray, long value, long defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Long[] array = ObjectUtils.transformLongArray(sourceArray);
        return getNext(array, value, defaultValue, isCircle);
    }

    /**
     * @see {@link ArrayUtils#getLast(Object[], Object, Object, boolean)} Object is Integer
     */
    public static int getLast(int[] sourceArray, int value, int defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Integer[] array = ObjectUtils.transformIntArray(sourceArray);
        return getLast(array, value, defaultValue, isCircle);

    }

    /**
     * @see {@link ArrayUtils#getNext(Object[], Object, Object, boolean)} Object is Integer
     */
    public static int getNext(int[] sourceArray, int value, int defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Integer[] array = ObjectUtils.transformIntArray(sourceArray);
        return getNext(array, value, defaultValue, isCircle);
    }
}

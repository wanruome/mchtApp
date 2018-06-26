package com.ruomm.base.view.dragsortgridview;

import java.util.Arrays;

import android.annotation.SuppressLint;

public class ReorderArray {

	private int[] mArray;
	private int[] mCacheArray;
	private onMoveListener mOnMoveListener;

	public ReorderArray(int capacity) {
		mArray = new int[capacity];
		for (int i = 0; i < capacity; i++) {
			mArray[i] = i;
		}
	}

	public int getCount() {
		return mArray.length;
	}

	private final void set(int position, int value) {
		final int length = mCacheArray.length;
		if (position < 0 || position > length - 1) {
			return;
		}
		this.mCacheArray[position] = value;
	}

	protected final void move(int fromPos, int toPos) {
		if (mOnMoveListener != null) {
			mOnMoveListener.onMove(fromPos, toPos);
		}
		int value = get(fromPos);
		set(toPos, value);
	}

	public final int get(int position) {
		final int length = mArray.length;
		if (position < 0 || position > length - 1) {
			return -1;
		}
		return mArray[position];
	}

	public final int indexOf(int value) {
		final int[] array = mArray;
		int index = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				index = i;
				break;
			}
		}
		return index;
	}

	public final void reOrder(int fromPos, int toPos) {

		if (fromPos == toPos) {
			return;
		}

		copyArray();

		final int fromValue = get(fromPos);

		onReOrder(fromPos, toPos);

		set(toPos, fromValue);

		applyArray();
	}

	/**
	 * you should only use move() to tell GridView how it's element move
	 */
	public void onReOrder(int fromPos, int toPos) {

		// ����Ԫ���Ƶ�ǰ�棬����������Ԫ�������
		if (fromPos > toPos) {
			for (int i = fromPos; i > toPos; i--) {
				move(i - 1, i);
			}
		}
		else {// ǰ��Ԫ���������棬����������Ԫ����ǰ��
			for (int i = fromPos; i < toPos; i++) {
				move(i + 1, i);
			}
		}

	}

	@SuppressLint("NewApi")
	private void copyArray() {
		mCacheArray = Arrays.copyOf(mArray, mArray.length);
	}

	private void applyArray() {
		this.mArray = mCacheArray;
	}

	@Override
	public String toString() {
		final int[] array = mArray;
		final int length = array.length;
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < length; i++) {
			sb.append(array[i]);
			if (i < length - 1) {
				sb.append(",");
			}
			else {
				sb.append("]");
			}
		}
		return sb.toString();
	}

	public void setOnMoveListener(onMoveListener listener) {
		this.mOnMoveListener = listener;
	}

	public interface onMoveListener {
		public void onMove(int fromPos, int toPos);
	}
}

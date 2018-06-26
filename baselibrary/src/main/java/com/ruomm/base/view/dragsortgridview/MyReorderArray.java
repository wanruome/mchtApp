package com.ruomm.base.view.dragsortgridview;

public class MyReorderArray extends ReorderArray {

	public MyReorderArray(int capacity) {
		super(capacity);
	}

	@Override
	public void onReOrder(int fromPos, int toPos) {
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

}

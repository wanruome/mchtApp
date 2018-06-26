package com.ruomm.base.view.dragsortgridview;

import com.ruomm.base.view.dragsortgridview.DragSortGridView.OnReorderContentListener;

import android.widget.BaseAdapter;

public abstract class DragSortAdapter extends BaseAdapter implements OnReorderContentListener {

	public abstract ReorderArray getReorderArray();

	public abstract int getDragStartPositon();

	public abstract int getDragEndPostion();

}

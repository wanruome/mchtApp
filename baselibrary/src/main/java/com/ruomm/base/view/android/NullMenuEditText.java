package com.ruomm.base.view.android;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.ruomm.base.tools.ToastUtil;

public class NullMenuEditText extends AppCompatEditText{

    public NullMenuEditText(Context context) {
       super(context, null);
        setLongClickable(false);
        setTextIsSelectable(false);
//        setFocusable(true);

    }

    public NullMenuEditText(Context context, AttributeSet attrs) {
        super(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
        setLongClickable(false);
        setTextIsSelectable(false);
//        setFocusable(true);

    }

    public NullMenuEditText(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        setLongClickable(false);
        setTextIsSelectable(false);
//        setFocusable(true);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean flag=false;
        if(id==android.R.id.selectAll){
            flag=true;
        }
        else if(id==android.R.id.undo){
            flag=true;
        }
        else if(id==android.R.id.redo){
            flag=true;
        }
        else if(id==android.R.id.cut){
            flag=true;
        }
        else  if(id==android.R.id.copy){
            flag=true;
        }
        else  if(id==android.R.id.paste){
            flag=true;
        }
        else  if(id==android.R.id.shareText){
            flag=true;
        }
        else  if(id==android.R.id.pasteAsPlainText){
            flag=true;
        }
        else  if(id==android.R.id.replaceText){
            flag=true;
        }
        else  if(id==android.R.id.textAssist){
            flag=true;
        }
        else  if(id==android.R.id.autofill){
            flag=true;
        }
        if(flag)
        {
            ToastUtil.makeShortToast(getContext(),"安全输入框不能进行编辑");
            return flag;
        }
        else {
            return false;
        }
    }
}
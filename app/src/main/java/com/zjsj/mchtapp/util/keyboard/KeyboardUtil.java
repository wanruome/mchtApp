package com.zjsj.mchtapp.util.keyboard;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.ruomm.base.tools.DisplayUtil;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.module.login.LoginActivity;

import java.lang.reflect.Method;

public class KeyboardUtil {

    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private Keyboard k3;// 符号键盘
    private boolean isUpper = false;// 是否大写
    // 字母键盘
    private static final int BOARD_LETTER=-11;
    // 数字键盘
    private static final int BOARD_NUMBER=-12;
    // 符号键盘
    private static final int BOARD_SYMBOL=-13;
    private static final int BOARD_SHIFT=-14;
    private static final int ACTION_DELETE=-15;
    private static final int ACTION_DONE=-16;
    private static final int ACTION_LETF=-17;
    private static final int ACTION_RIGHT=-18;
    //€
    private static final int MONEY_RMB=-21;
    private static final int MONEY_OY=-22;

    private static final int MONEY_YB=-23;


    private ViewGroup rootView;
    private View keyboradContainer;

    private View kv_img_hidden;
    private View kv_lyt_container;
    private KeyboardView keyboardView;
    private LayoutInflater  mLayoutInflater;
    private EditText mEditText;
    private Activity mActivity;



    public KeyboardUtil(Activity activity, EditText edit) {
        this.mEditText = edit;
        mActivity=activity;
        k1 = new Keyboard(activity, R.xml.key_letter);
        k2 = new Keyboard(activity,R.xml.key_number);
        k3 = new Keyboard(activity,R.xml.key_symbol);
        mLayoutInflater=LayoutInflater.from(activity);
//        keyboardView = new KeyboardView(activity, null);
        rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content));
        keyboradContainer=mLayoutInflater.inflate(R.layout.lyt_keyboard,null);

//        int height= DisplayUtil.getNavigationBarHeight(activity);
//        if(height>0)
//        {
//           keyboradContainer.setPadding(0,0,0,height);
//        }
        kv_img_hidden=keyboradContainer.findViewById(R.id.kv_img_hidden);
        kv_lyt_container=keyboradContainer.findViewById(R.id.kv_lyt_container);
        keyboardView = keyboradContainer.findViewById(R.id.kv_lyt_keyboard);
        int height= DisplayUtil.getNavigationBarHeight(activity);
        if(height>0)
        {
            kv_lyt_container.setPadding(0,mActivity.getResources().getDimensionPixelSize(R.dimen.dpx006),0,height);
            kv_lyt_container.setPadding(0,mActivity.getResources().getDimensionPixelSize(R.dimen.dpx006),0,mActivity.getResources().getDimensionPixelSize(R.dimen.dpx032));
        }
        else {
            kv_lyt_container.setPadding(0,mActivity.getResources().getDimensionPixelSize(R.dimen.dpx006),0,mActivity.getResources().getDimensionPixelSize(R.dimen.dpx080));
        }
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
        kv_img_hidden.setOnClickListener(kvOnClickListener);
        mEditText.setOnTouchListener(kvOnTouchListener);
        mEditText.setOnFocusChangeListener(kvOnFocusChangeListener);
    }
    private View.OnClickListener kvOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard();
        }
    };
    private View.OnFocusChangeListener kvOnFocusChangeListener=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    hideKeyboard();
                }
        }
    };
    private View.OnTouchListener kvOnTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(isShow)
            {
                return false;
            }
            int eventAction=event.getAction();
            if(eventAction==MotionEvent.ACTION_UP) {

                int sdk_int = android.os.Build.VERSION.SDK_INT;
                if (sdk_int <= 10) {
                    mEditText.setInputType(InputType.TYPE_NULL);
                } else {
                    mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    try {
                        Class<EditText> cls = EditText.class;
                        Method setSoftInputShownOnFocus;
                        setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                        setSoftInputShownOnFocus.setAccessible(true);
                        setSoftInputShownOnFocus.invoke(mEditText, false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                showKeyboard();
                return false;
            }
            else{
                return false;
            }
        }
    };

    OnKeyboardActionListener onKeyboardActionListener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            if (primaryCode ==ACTION_DONE) {// 完成
                hideKeyboard();
            } else if (primaryCode == ACTION_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == BOARD_LETTER) {// 大小写切换

                keyboardView.setKeyboard(k1);
            }
            else if(primaryCode==BOARD_SHIFT)
            {
                isUpper = !isUpper;
                k1.setShifted(isUpper);
                keyboardView.invalidateAllKeys();
            }
            else if (primaryCode == BOARD_NUMBER) {// 符号键盘
                keyboardView.setKeyboard(k2);
            }
            else if (primaryCode == BOARD_SYMBOL) {// 符号键盘
                keyboardView.setKeyboard(k3);
            } else if (primaryCode == ACTION_LETF) { //向左
                if (start > 0) {
                    mEditText.setSelection(start - 1);
                }
            } else if (primaryCode == ACTION_RIGHT) { // 向右
                if (start < mEditText.length()) {
                    mEditText.setSelection(start + 1);
                }
            }
            else if(primaryCode==MONEY_RMB)
            {
                editable.insert(start,"¥");
            }
            else if(primaryCode==MONEY_OY)
            {
                editable.insert(start,"€");
            }
            else if(primaryCode==MONEY_YB)
            {
                editable.insert(start,"£");
            }
            else {
                String str = Character.toString((char) primaryCode);
                if (isWord(str)) {
                    if (isUpper) {
                        str = str.toUpperCase();
                    } else {
                        str = str.toLowerCase();
                    }
                }
                editable.insert(start, str);

            }
        }
    };

    private boolean isShow = false;

    public void showKeyboard() {
        if (!isShow) {
            rootView.addView(keyboradContainer);
            isShow = true;
            DisplayUtil.closeSoftInputView(mActivity);
        }
    }

    public void hideKeyboard() {
        if (rootView != null && keyboardView != null && isShow) {
            isShow = false;
            rootView.removeView(keyboradContainer);

        }
//        mInstance = null;
    }
    private void destoryKeyBoard(){
        hideKeyboard();
        this.keyboradContainer=null;
        this.keyboardView=null;
        this.mEditText.setOnTouchListener(null);
        this.mEditText.setOnFocusChangeListener(null);
        this.mActivity=null;
        this.mEditText=null;
    }

    private boolean isWord(String str) {
        return str.matches("[a-zA-Z]");
    }
//
//    private static KeyboardUtil mInstance;
//
//    public static KeyboardUtil shared(Activity activity, EditText edit) {
//        if (mInstance == null) {
//            mInstance = new KeyboardUtil(activity, edit);
//        }
//        mInstance.mEditText = edit;
//        return mInstance;
//    }
}

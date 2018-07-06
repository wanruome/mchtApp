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
import com.ruomm.base.tools.StringUtils;
import com.ruomm.baseconfig.debug.MLog;
import com.zjsj.mchtapp.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeyboardUtil {
    public static enum  KEYMODE{
        LETTER_UPPER,LETTER_LOWER,NUMBER,SYMBOL
    };
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
    private boolean isLetterEnable=true;
    private boolean isNumberEnable=true;
    private boolean isSymbolEnable=true;
    private boolean isNumberRandom=true;
    private boolean isShow = false;
    private boolean isFirstShow=true;
    private List<String> encryptLst=new ArrayList<>();
    private String encryptStr=null;
    private KeyboardSafeInterface keyboardSafeInterface=null;
    public KeyboardUtil(Activity activity, EditText edit) {
        this.mEditText = edit;
        mActivity=activity;
        k1 = new Keyboard(activity, R.xml.key_letter);
        k2 = new Keyboard(activity,R.xml.key_number);
        k3 = new Keyboard(activity,R.xml.key_symbol);
        mLayoutInflater=LayoutInflater.from(activity);
        rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content));
        keyboradContainer=mLayoutInflater.inflate(R.layout.lyt_keyboard,null);
        kv_img_hidden=keyboradContainer.findViewById(R.id.kv_img_hidden);
        kv_lyt_container=keyboradContainer.findViewById(R.id.kv_lyt_container);
        keyboardView = keyboradContainer.findViewById(R.id.kv_lyt_keyboard);
        int height= DisplayUtil.getNavigationBarHeight(activity);
        if(height>0)
        {
            kv_lyt_container.setPadding(0,mActivity.getResources().getDimensionPixelSize(R.dimen.dpx006),0,height);
        }
        else {
            kv_lyt_container.setPadding(0,mActivity.getResources().getDimensionPixelSize(R.dimen.dpx006),0,mActivity.getResources().getDimensionPixelSize(R.dimen.dpx032));
        }

        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
        kv_img_hidden.setOnClickListener(kvOnClickListener);
        mEditText.setOnTouchListener(kvOnTouchListener);
        mEditText.setOnFocusChangeListener(kvOnFocusChangeListener);
        mEditText.setLongClickable(false);
        mEditText.setTextIsSelectable(false);
    }
        //    Keyboard.Key k1Number=k1.getKeys().get(28);
        //    Keyboard.Key k1Symbol=k1.getKeys().get(29);
        //    Keyboard.Key k2Leeter=k2.getKeys().get(9);
        //    Keyboard.Key k2Symbol=k2.getKeys().get(10);
        //    Keyboard.Key k3Leeter=k3.getKeys().get(30);
        //    Keyboard.Key k3Number=k3.getKeys().get(31);
    public KeyboardUtil setLetterEnable(boolean isLetterEnable){
        this.isLetterEnable=isLetterEnable;
        Keyboard.Key k2Leeter=k2.getKeys().get(9);
        Keyboard.Key k3Leeter=k3.getKeys().get(30);
        if( this.isLetterEnable) {
            k2Leeter.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_letter_normal);
            k3Leeter.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_letter_normal);
        }
        else{
            k2Leeter.icon= mActivity.getResources().getDrawable(R.drawable.keyboard_letter_disable);
            k3Leeter.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_letter_disable);
        }
        return this;
    }
    public KeyboardUtil setNumberEnable(boolean isNumberEnable){
        this.isNumberEnable=isNumberEnable;
        Keyboard.Key k1Number=k1.getKeys().get(28);
        Keyboard.Key k3Number=k3.getKeys().get(31);
        if(this.isNumberEnable) {
            k1Number.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_number_normal);
            k3Number.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_number_normal);
        }
        else{
            k1Number.icon= mActivity.getResources().getDrawable(R.drawable.keyboard_number_disable);
            k3Number.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_number_disable);
        }
        return this;
    }
    public KeyboardUtil setSymbolEnable(boolean isSymbolEnable){
        this.isSymbolEnable=isSymbolEnable;
        Keyboard.Key k1Symbol=k1.getKeys().get(29);
        Keyboard.Key k2Symbol=k2.getKeys().get(10);
        if( this.isSymbolEnable) {
            k1Symbol.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_symbol_normal);
            k2Symbol.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_symbol_normal);
        }
        else{
            k1Symbol.icon= mActivity.getResources().getDrawable(R.drawable.keyboard_symbol_disable);
            k2Symbol.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_symbol_disable);
        }
        return this;
    }
    public KeyboardUtil setNumberRandom(boolean isNumberRandom){
        this.isNumberRandom=isNumberRandom;
        return this;
    }
    public KeyboardUtil setSafeInterFace(KeyboardSafeInterface keyboardSafeInterface)
    {
        this.keyboardSafeInterface=keyboardSafeInterface;
        return this;
    }
    public KeyboardUtil bulider(KEYMODE keymode)
    {
        KEYMODE keymodeReal=getBuliderMode(keymode);
        if(null==keymodeReal) {
            setKeyboardLetter(isUpper);
        }
        else if(keymodeReal==KEYMODE.LETTER_LOWER){
            setKeyboardLetter(false);
        }
        else if(keymodeReal==KEYMODE.LETTER_UPPER){
            setKeyboardLetter(true);
        }
        else if(keymodeReal==KEYMODE.SYMBOL)
        {
            setKeyboardSymbol();
        }
        else
        {
            setKeyboardNumber();
        }
        return this;
    }
   private KEYMODE getBuliderMode(KEYMODE keymode){
        if(null==keymode) {
            if(isLetterEnable)
            {
                return KEYMODE.LETTER_LOWER;
            }
            else if(isNumberEnable)
            {
                return KEYMODE.NUMBER;
            }
            else if(isSymbolEnable){
                return KEYMODE.SYMBOL;
            }
            else {
                return null;
            }
        }
        else if(keymode==KEYMODE.LETTER_LOWER||keymode==KEYMODE.LETTER_UPPER){
            if(isLetterEnable)
            {
                return keymode;
            }
            else if(isNumberEnable)
            {
                return KEYMODE.NUMBER;
            }
            else if(isSymbolEnable){
                return KEYMODE.SYMBOL;
            }
            else {
                return null;
            }
        }
        else if(keymode==KEYMODE.SYMBOL)
        {
            if(isSymbolEnable)
            {
                return keymode;
            }
            else if(isLetterEnable)
            {
                return KEYMODE.LETTER_LOWER;
            }
            else if(isNumberEnable)
            {
                return KEYMODE.NUMBER;
            }
            else {
                return null;
            }
        }
        else
        {
            if(isNumberEnable)
            {
                return keymode;
            }
            else if(isLetterEnable)
            {
                return KEYMODE.LETTER_LOWER;
            }
            else if(isSymbolEnable)
            {
                return KEYMODE.SYMBOL;
            }
            return null;
        }

    }
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
//            if(eventAction==MotionEvent.ACTION_DOWN)
//            {
//                int temp = mEditText.getOffsetForPosition(event.getX(),event.getY());
//                mEditText.setSelection(temp);
//                mEditText.requestFocus();
//            }
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
//            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            if (primaryCode ==ACTION_DONE) {// 完成
                hideKeyboard();
            } else if (primaryCode == ACTION_DELETE) {// 回退
                Editable editable = mEditText.getText();
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editTextDelete(start);
                    }
                }
            } else if (primaryCode == BOARD_LETTER) {
                if(isLetterEnable) {
                    setKeyboardLetter(isUpper);
                }
            }
            // 大小写切换
            else if(primaryCode==BOARD_SHIFT)
            {
                changeLetterState(!isUpper);

            }
            else if (primaryCode == BOARD_NUMBER) {// 符号键盘
                if(isNumberEnable) {
                   setKeyboardNumber();
                }
            }
            else if (primaryCode == BOARD_SYMBOL) {// 符号键盘
                if(isSymbolEnable) {
                  setKeyboardSymbol();
                }
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
                editTextInsert(start,"¥");
            }
            else if(primaryCode==MONEY_OY)
            {
                editTextInsert(start,"€");
            }
            else if(primaryCode==MONEY_YB)
            {
                editTextInsert(start,"£");
            }
            else {
                String str = Character.toString((char) primaryCode);
//                if (isWord(str)) {
//                    if (isUpper) {
//                        str = str.toUpperCase();
//                    } else {
//                        str = str.toLowerCase();
//                    }
//                }
                editTextInsert(start, str);

            }
        }
    };
    private void editTextInsert(int start,String str)
    {
        if(null!=keyboardSafeInterface) {
            if(keyboardSafeInterface.isEncryptByChar()) {
                try {
                    Editable editable = mEditText.getText();
                    if (editable.length() != encryptLst.size()) {
                        mEditText.setText(null);
                        encryptLst.clear();
                        return;
                    }
                    editable.insert(start, keyboardSafeInterface.getShowStr(str));
                    encryptLst.add(start, keyboardSafeInterface.getEncryptStr(str));
                    MLog.i(encryptLst);
                } catch (Exception e) {
                    e.printStackTrace();
                    mEditText.setText(null);
                    encryptLst.clear();
                }
            }
            else{
                try {
                    String data=keyboardSafeInterface.getDecryptStr(encryptStr);
                    StringBuilder sbTemp=StringUtils.isEmpty(data)?new StringBuilder():new StringBuilder(data) ;
                    Editable editable = mEditText.getText();
                    if(editable.length()!= sbTemp.length())
                    {
                        mEditText.setText(null);
                        encryptStr=null;
                        return;
                    }
                    sbTemp.insert(start,str);
                    editable.insert(start, keyboardSafeInterface.getShowStr(str));
                    encryptStr=keyboardSafeInterface.getEncryptStr(sbTemp.toString());
                    MLog.i(encryptStr);
                }catch (Exception e)
                {
                    Editable editable = mEditText.getText();
                    mEditText.setText(null);
                    encryptLst.clear();
                    e.printStackTrace();
                }


            }
        }
        else{
            Editable editable = mEditText.getText();
            editable.insert(start, str);
        }

    }
    private void editTextDelete(int start)
    {
        if(null!=keyboardSafeInterface) {
            if(keyboardSafeInterface.isEncryptByChar()) {
                try {
                    Editable editable = mEditText.getText();
                    if (editable.length() != encryptLst.size()) {
                        mEditText.setText(null);
                        encryptLst.clear();
                        return;
                    }
                    editable.delete(start - 1, start);
                    encryptLst.remove(start - 1);
                    MLog.i(encryptLst);
                } catch (Exception e) {
                    e.printStackTrace();
                    Editable editable = mEditText.getText();
                    mEditText.setText(null);
                    encryptLst.clear();
                }
            }
            else
            {
                try {
                    String data=keyboardSafeInterface.getDecryptStr(encryptStr);
                    StringBuilder sbTemp=StringUtils.isEmpty(data)?new StringBuilder():new StringBuilder(data) ;
                    Editable editable = mEditText.getText();
                    if(editable.length()!= sbTemp.length())
                    {
                        mEditText.setText(null);
                        encryptStr=null;
                        return;
                    }
                    editable.delete(start - 1, start);
                    sbTemp.delete(start-1,start);
                    encryptStr=keyboardSafeInterface.getEncryptStr(sbTemp.toString());
                    MLog.i(encryptStr);
                }catch (Exception e)
                {
                    Editable editable = mEditText.getText();
                    mEditText.setText(null);
                    encryptLst.clear();
                    e.printStackTrace();
                }

            }
        }
        else {
            Editable editable = mEditText.getText();
            editable.delete(start - 1, start);
        }
    }


    public void showKeyboard() {
        if (!isShow) {

            rootView.addView(keyboradContainer);
            isShow = true;
            DisplayUtil.closeSoftInputView(mActivity);
            if(!isFirstShow&&isNumberRandom&&keyboardView.getKeyboard()==k2)
            {
                setBoardNumberRandom();
                keyboardView.invalidateAllKeys();
            }
            isFirstShow=false;
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
    private void setKeyboardLetter(boolean upperStatus){
        changeLetterState(upperStatus);
        keyboardView.setKeyboard(k1);
    }
    private void setKeyboardNumber()
    {
        if(isNumberRandom)
        {
            setBoardNumberRandom();
        }
        keyboardView.setKeyboard(k2);
    }
    private void setKeyboardSymbol(){
        keyboardView.setKeyboard(k3);
    }
    private void setBoardNumberRandom()
    {
        Random random = new Random();
        List<Integer> lst = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lst.add(i);
        }
        List<Integer> lstResult = new ArrayList<>();
        for (int i = 10; i > 0; i--) {
            int index = random.nextInt(i);
            Integer value = lst.get(index);
            lst.remove(value);
            lstResult.add(value);
        }
        List<Keyboard.Key> k2Keys=k2.getKeys();
        List<Keyboard.Key> k2NumberKeys=new ArrayList<>();
        for(Keyboard.Key tmp:k2Keys)
        {
            int keyCode=tmp.codes[0];
            if(keyCode>=48&&keyCode<=57)
            {
                k2NumberKeys.add(tmp);
            }
        }
        for(int i=0;i<10;i++)
        {
            int value=lstResult.get(i);
            Keyboard.Key key=k2NumberKeys.get(i);
            key.label=value+"";
            key.codes[0]=value+48;
        }
    }
    private View.OnClickListener kvOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard();
        }
    };
    private void changeLetterState(boolean upperStatus) {
        if(isUpper==upperStatus)
        {
            return;
        }
        isUpper=upperStatus;
        List<Keyboard.Key> keys = k1.getKeys();
        if (isUpper) {
            for (Keyboard.Key key : keys) {
                int keyCode = key.codes[0];
                if (keyCode >= 97 & keyCode <= 122) {
                    key.codes[0] = keyCode - 32;
                }
                else if (key.codes[0] == BOARD_SHIFT) {
                    key.label = null;
                    key.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_shift_big);
                }
            }
        }
        else{
            for (Keyboard.Key key : keys) {
                int keyCode = key.codes[0];
                if (keyCode >= 65 & keyCode <= 90) {
                    key.codes[0] = keyCode + 32;
                }
                else if (key.codes[0] == BOARD_SHIFT) {
                    key.label = null;
                    key.icon = mActivity.getResources().getDrawable(R.drawable.keyboard_shift_little);
                }
            }
        }
        k1.setShifted(isUpper);
        keyboardView.invalidateAllKeys();
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

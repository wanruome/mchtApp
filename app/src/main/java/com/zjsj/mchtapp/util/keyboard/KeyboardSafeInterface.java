package com.zjsj.mchtapp.util.keyboard;

public interface KeyboardSafeInterface {
    public boolean isEncryptByChar();
    public String  getShowStr(String str);
    public String getEncryptStr(String str);
    public String getDecryptStr(String encryptStr);
}

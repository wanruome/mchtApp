package com.ruomm.base.tools.rootcheck;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class RootCheckUtil {
    private static final String LOG_TAG=RootCheckUtil.class.getSimpleName();
    //    查看系统是否测试版
    public static boolean checkDeviceDebuggable(){
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            Log.i(LOG_TAG,"buildTags="+buildTags);
            return true;
        }
        return false;
    }
    //    检查是否存在Superuser.apk
    public static boolean checkSuperuserApk(){
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                Log.i(LOG_TAG,"/system/app/Superuser.apk exist");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //    检查su命令
    public static boolean checkRootPathSU()
    {
        File f=null;
        final String kSuSearchPaths[]={"/system/bin/","/system/xbin/","/system/sbin/","/sbin/","/vendor/bin/"};
        try{
            for(int i=0;i<kSuSearchPaths.length;i++)
            {
                f=new File(kSuSearchPaths[i]+"su");
                if(f!=null&&f.exists())
                {
                    Log.i(LOG_TAG,"find su in : "+kSuSearchPaths[i]);
                    return true;
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    //    使用which命令查看是否存在su.
    //  which是linux下的一个命令，可以在系统PATH变量指定的路径中搜索某个系统命令的位置并且返回第一个搜索结果。
    // 这里，我们就用它来查找su。
    public static boolean checkRootWhichSU() {
        String[] strCmd = new String[] {"/system/xbin/which","su"};
        ArrayList<String> execResult = executeCommand(strCmd);
        if (execResult != null){
            Log.i(LOG_TAG,"execResult="+execResult.toString());
            return true;
        }else{
            Log.i(LOG_TAG,"execResult=null");
            return false;
        }
    }
    private static ArrayList<String> executeCommand(String[] shellCmd){
        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;
        try {
            Log.i(LOG_TAG,"to shell exec which for find su :");
            localProcess = Runtime.getRuntime().exec(shellCmd);
        } catch (Exception e) {
            return null;
        }
        BufferedWriter out =null;
        BufferedReader in = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
            while ((line = in.readLine()) != null) {
                Log.i(LOG_TAG,"–> Line received: " + line);
                fullResponse.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(null!=in)
                {
                    in.close();
                }
                if(null!=out)
                {
                    out.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        Log.i(LOG_TAG,"–> Full response was: " + fullResponse);
        return fullResponse;
    }
    //执行SU，会调用su授权不推荐。
    public static synchronized boolean checkGetRootAuth()
    {
        Process process = null;
        DataOutputStream os = null;
        try
        {
            Log.i(LOG_TAG,"to exec su");
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            Log.i(LOG_TAG, "exitValue="+exitValue);
            if (exitValue == 0)
            {
                return true;
            } else
            {
                return false;
            }
        } catch (Exception e)
        {
            Log.i(LOG_TAG, "Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        } finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                process.destroy();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
//    执行busybox
    public static synchronized boolean checkBusybox()
    {
        try
        {
            Log.i(LOG_TAG,"to exec busybox df");
            String[] strCmd = new String[] {"busybox","df"};
            ArrayList<String> execResult = executeCommand(strCmd);
            if (execResult != null){
                Log.i(LOG_TAG,"execResult="+execResult.toString());
                return true;
            }else{
                Log.i(LOG_TAG,"execResult=null");
                return false;
            }
        } catch (Exception e)
        {
            Log.i(LOG_TAG, "Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        }
    }
//    访问/data目录，查看读写权限
    public static synchronized boolean checkAccessRootData()
    {
        try
        {
            Log.i(LOG_TAG,"to write /data");
            String fileContent = "test_ok";
            Boolean writeFlag = writeFile("/data/su_test",fileContent);
            if (writeFlag){
                Log.i(LOG_TAG,"write ok");
            }else{
                Log.i(LOG_TAG,"write failed");
            }

            Log.i(LOG_TAG,"to read /data");
            String strRead = readFile("/data/su_test");
            Log.i(LOG_TAG,"strRead="+strRead);
            if(fileContent.equals(strRead)){
                return true;
            }else {
                return false;
            }
        } catch (Exception e)
        {
            Log.i(LOG_TAG, "Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        }
    }
    public static Boolean writeFile(String fileName,String message){
        try{
            FileOutputStream fout = new FileOutputStream(fileName);
            byte [] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //读文件
    public static String readFile(String fileName){
        File file = new File(fileName);
        try {
            FileInputStream fis= new FileInputStream(file);
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            while((len=fis.read(bytes))>0){
                bos.write(bytes, 0, len);
            }
            String result = new String(bos.toByteArray());
            Log.i(LOG_TAG, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean isDeviceRooted() {
        if (checkDeviceDebuggable()){return true;}//check buildTags
        if (checkSuperuserApk()){return true;}//Superuser.apk
        if (checkRootPathSU()){return true;}//find su in some path
        if (checkRootWhichSU()){return true;}//find su use 'which'
        if (checkBusybox()){return true;}//find su use 'which'
        if (checkAccessRootData()){return true;}//find su use 'which'
//        if (checkGetRootAuth()){return true;}//exec su

        return false;
    }
}

package com.ruomm.base.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**   
*    
* 项目名称：工具类   
* 
* 类名称：ShellUtils   
* 
* 类描述：   Android Shell工具类，可用于检查系统root权限，并在shell或root用户下执行shell命令。
* 
* 创建人：王龙能
* 
* 创建时间：2014-3-13 上午10:55:40   
* 
* 修改人：王龙能  
* 
* 修改时间：2014-3-13 上午10:55:40   
* 
* 修改备注：   
* 
* @version    
*    
*/
public class ShellUtils {

    public static final String COMMAND_SU       = "su";
    public static final String COMMAND_SH       = "sh";
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    /**
     * 检查是否有root权限
     * 
     * @return
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * 执行shell命令，默认的返回结果
     * 
     * @param command command
     * @param isRoot 否需要以root运行 
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[] { command }, isRoot, true);
    }

    /**
     * 执行shell命令，默认的返回结果
     * 
     * @param commands 命令列表
     * @param isRoot 是否需要以root运行 
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, true);
    }

    /**
     * 执行shell命令，默认的返回结果
     * 
     * @param commands 命令阵列 
     * @param isRoot 是否需要以root运行
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /**
     * 执行shell命令 
     * 
     * @param command command
     * @param isRoot t是否需要以root运行 
     * @param isNeedResultMsg 是否需要结果
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[] { command }, isRoot, isNeedResultMsg);
    }

    /**
     * 执行shell命令 
     * 
     * @param commands 命令列表 
     * @param isRoot 是否需要以root运行 
     * @param isNeedResultMsg是否需要结果
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, isNeedResultMsg);
    }

    /**
     * execute 执行shell命令 
     * 
     * @param commands 命令阵列 
     * @param isRoot 是否需要以root运行
     * @param isNeedResultMsg 是否需要结果
     * @return <ul>
     * <li>如果isNeedResultMsg是假的 {@link CommandResult#successMsg} 为空 {@link CommandResult#errorMsg} 是空</li>
     * <li>如果{@link CommandResult#result} 结果是-1，但也许有些excepiton。</li>
     * </ul>
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                // donnot使用os.writeBytes（条命令），避免中国的charset错误
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            //get命令的结果
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
            : errorMsg.toString());
    }

    /**
     * 命令的结果 
     * <ul>
     * <li>{@link CommandResult#result}表示命令的结果，0表示正常，否则表示错误，相同的：599在Linux的shell</li>
     * <li>{@link CommandResult#successMsg} 表示命令的结果成功消息</li>
     * <li>{@link CommandResult#errorMsg} 表示的命令结果的错误信息</li>
     * </ul>
     */
    public static class CommandResult {

        /** 命令的结果 **/
        public int    result;
        /** 命令结果成功消息 **/
        public String successMsg;
        /** 命令结果的错误讯息 **/
        public String errorMsg;

        public CommandResult(int result){
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg){
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}


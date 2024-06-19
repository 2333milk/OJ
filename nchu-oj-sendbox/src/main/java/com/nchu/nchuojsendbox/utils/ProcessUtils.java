package com.nchu.nchuojsendbox.utils;

import cn.hutool.core.date.StopWatch;
import com.nchu.nchuojsendbox.model.ExecuteMessage;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/***
 * 进程工具类
 */
public class ProcessUtils {
    //时间限制
    private static final long TIME_OUT = 5000L;
    /**
     * 执行进程并返回信息
     * @param cmdStr
     * @return
     */
    public static ExecuteMessage runProcess(String cmdStr) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Process compileProcess = Runtime.getRuntime().exec(cmdStr);
            //超时控制
            new Thread(()->{
                try {
                    Thread.sleep(TIME_OUT);
                    compileProcess.destroy();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            //等待程序执行结束，获取错误码
            int execValue = compileProcess.waitFor();
            executeMessage.setExitValue(execValue);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            String compileOutputLine;
            List<String> outputList = new ArrayList<>();
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                outputList.add(compileOutputLine);
            }
            executeMessage.setMessage(StringUtils.join(outputList,"\n"));
            //异常退出
            if (execValue != 0) {
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                String errorCompileOutputLine;
                List<String> errorOutput = new ArrayList<>();
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorOutput.add(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(StringUtils.join(errorOutput,"\n"));
            }
            compileProcess.destroy();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        if(executeMessage.getTime()>=TIME_OUT){
            executeMessage.setExitValue(1);
            executeMessage.setErrorMessage("程序超时");
        }
        return executeMessage;
    }
    /**
     * 执行交互式进程并返回信息
     * @param cmdStr
     * @return
     */
    public static ExecuteMessage runInteractProcess(String cmdStr,String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            Process compileProcess = Runtime.getRuntime().exec(cmdStr);

            //向控制台输入
            OutputStream outputStream = compileProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(args);
            outputStreamWriter.flush();

            //等待程序执行结束，获取错误码
            int execValue = compileProcess.waitFor();
            executeMessage.setExitValue(execValue);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            String compileOutputLine;
            StringBuilder compileOutputStrBuilder = new StringBuilder();
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStrBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStrBuilder.toString());
            //异常退出
            if (execValue != 0) {
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                String errorCompileOutputLine;
                StringBuilder errorCompileOutputStrBuilder = new StringBuilder();
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorCompileOutputStrBuilder.append(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(errorCompileOutputStrBuilder.toString());
            }
            outputStreamWriter.close();
            outputStream.close();
            compileProcess.destroy();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return executeMessage;
    }
}

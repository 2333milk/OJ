package com.jueye.nchuojbackendjudgeservice.judge.codeSendbox;

import com.jueye.nchuojbackendjudgeservice.judge.impl.ExampleCodeSandbox;
import com.jueye.nchuojbackendjudgeservice.judge.impl.RemoteCodeSandbox;
import com.jueye.nchuojbackendjudgeservice.judge.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂
 */
public class CodeSendboxFactory {

    /**
     * 创建代码沙箱实例
     * @param type 沙箱类型
     * @return
     */
    public static CodeSendbox newInstance(String type){
        switch (type){
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return  new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}

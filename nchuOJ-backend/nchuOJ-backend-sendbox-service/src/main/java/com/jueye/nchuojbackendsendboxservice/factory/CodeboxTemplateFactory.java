package com.jueye.nchuojbackendsendboxservice.factory;

import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendsendboxservice.service.CodeSendbox;
import com.jueye.nchuojbackendsendboxservice.service.impl.CCodeSendboxTemplate;
import com.jueye.nchuojbackendsendboxservice.service.impl.JavaNativeCodeSendbox;
import com.jueye.nchuojbackendsendboxservice.service.impl.PythonCodeSendboxTemplate;
import org.springframework.stereotype.Component;

@Component
public class CodeboxTemplateFactory {

    public CodeSendbox getCodeSendbox(String language){
        if(language.equals("java")){
            return JavaNativeCodeSendbox.getInstance();
        }else if(language.equals("c")){
            return CCodeSendboxTemplate.getInstance();
        }else if(language.equals("python")){
            return PythonCodeSendboxTemplate.getInstance();
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的编程语言");
    }
}

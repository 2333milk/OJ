package com.nchu.nchuojsendbox.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.nchu.nchuojsendbox.CodeSendbox;
import com.nchu.nchuojsendbox.model.ExcuteCodeRequest;
import com.nchu.nchuojsendbox.model.ExcuteCodeResponse;
import com.nchu.nchuojsendbox.model.ExecuteMessage;
import com.nchu.nchuojsendbox.model.JudgeInfo;
import com.nchu.nchuojsendbox.utils.ProcessUtils;
import jdk.nashorn.internal.codegen.Compiler;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * java原生实现
 */
@Component
public class JavaNativeCodeSendbox extends JavaCodeSendboxTemplate {

}

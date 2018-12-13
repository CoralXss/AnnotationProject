package com.coral.compiler.router;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * Created by xss on 2017/10/20.
 *
 * 几个问题：
 * 1. 为什么需要提供 java 版本？
 * 2. 注解处理器处理流程。以下几个方法如何串联在一起的？
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RouterAnnotationProcessor extends AbstractProcessor {

    /**
     * 描述：获取 processingEnvironment 对象，该对象主要作用：1）获取到生成代码的文件对象；2）debug输出对象；3）提供一些相关工具类。
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

    }

    /**
     * 描述：返回支持的 java 版本，一般返回当前所支持的最新 java 版本即可。（问题：为什么需要java版本？版本兼容问题吗？）
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    /**
     * 描述：需要处理的所有注解，返回值会被 process() 方法接收。
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    /**
     * 描述：扫描所欲被注解的元素，处理并最后生成文件。
     * 返回值 true 代表本次处理的注解都被处理，不希望下一个注解处理器继续处理。否则下一个注解处理器会继续处理。
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return true;
    }
}

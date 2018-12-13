package com.coral.compiler;

import com.coral.annotations.bindview.AutoCreate;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by xss on 2017/9/30.
 *
 * @AutoService 注解作用：告诉编译期该注解处理器的存在，在编译时自动在 META-INF/services下生成 javax.annotation.processing.Processor 文件；
 *   所以，如果不添加该注解，则可以手动创建该目录，并添加自定义的 Processor。
 *
 *   - 上述配置文件保存的是所有声明的注解处理器。当外部程序装载这个模块时，能通该模块 jar 包下的 META-INF/services 下找到具体的注解处理器的实现类名，
 *   并加载实例化，完成模块的注入。
 *
 * 注解处理器处理步骤：编译器找到所有注解处理器（问题一：从META-INF/services下寻找？这个目录的作用是什么？自定义插件也会有resources/META-INF目录。）
 *        -> 执行一个注解处理器 -> 遍历被注解元素 -> 生成类文件（问题二：自动生成的代码运行时会用到，所以最终是如何被打包到apk中？）
 *
 * 问题四：Android Studio中如何定义编译流程？gradle同步、rebuild project、make project、执行命令 gradle build分辨有什么区别？
 *
 * 自动生成的代码位于：build/generated/source/apt/debug目录下
 *
 * 问题五：构建过程中生成的 build/目录下的各个目录主要作用。
 *
 * 问题六：gradle 编译时遇到 dependency{} 闭包下的 annotationProcessor 选项时，如何处理这个声明？
 *
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TestProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(AutoCreate.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // 生成类： public final class HelloWorld
        TypeSpec.Builder helloWorldClassBuilder = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        MethodSpec getMsgMethod = MethodSpec.methodBuilder("getMsg")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S", "Hello Java Poet!")
                .build();

        // 生成方法： public static void main(String[] args)
        // 方法体：System.out.println("Hello, JavaPoet!")
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();


        TypeSpec helloWorld = helloWorldClassBuilder
                .addMethod(getMsgMethod)
                .addMethod(main)         // 将 main() 方法添加到该类中
                .build();

        // 生成一个类文件
        JavaFile javaFile = JavaFile.builder("com.songwenju.aptproject", helloWorld)
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

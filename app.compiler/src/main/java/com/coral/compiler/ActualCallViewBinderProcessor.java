package com.coral.compiler;

import com.coral.annotations.bindview.BindView;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by xss on 2017/9/29.
 * desc: 注解处理器 - 根据注解在编译期间自动生成 java 代码
 * 3. 实现根据注解生成代码
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ActualCallViewBinderProcessor extends AbstractProcessor {

    private Filer filer;  // 文件相关辅助类，该接口支持 通过注释处理器创建新文件
    private Messager messager;     // 日志相关辅助类，提供注释处理器用来报告错误消息、警告和其他通知的方式
    private Elements elementUtils; // 元素相关辅助类，用来对程序元素进行操作的实用工具方法
    private Map<String, AnnotatedClass> annotatedClassMap;

    /**
     * 每一个注解处理器类都必须有一个空的构造函数，init() 方法会被注解处理工具调用，
     * 并输入 @param processingEnvironment 参数。
     * ProcessingEnvironment 提供了很多有用的工具类 Elements Types 和 Filter .
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);  // JavacProcessingEnvironment

        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        annotatedClassMap = new TreeMap<>();
    }

    /**
     * 相当于每个处理器的主函数 main() ，在此处写扫描、评估 和处理注解的代码，以及生成 Java 代码
     * @param set
     * @param roundEnvironment 该参数可以查询出包含特定注解的被注解元素
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        annotatedClassMap.clear();

        try {
            note("--begin to process...");
            processBindView(roundEnvironment);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error(e.getMessage());
        }

        for (AnnotatedClass annotatedClass: annotatedClassMap.values()) {
            try {
                note("--begin to generate...");
                annotatedClass.generateFile().writeTo(filer);
                note("--end generate...");
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }

        return true;
    }

    private void processBindView(RoundEnvironment roundEnv) throws IllegalArgumentException {
        // 获取 使用给定注释类型 BindView 注释的元素
        for (Element element: roundEnv.getElementsAnnotatedWith(BindView.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);

            BinderViewField binderViewField = new BinderViewField(element);
            annotatedClass.addField(binderViewField);
        }
        note("--end process...");
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        // TypeElement 接口表示一个类 或 接口程序元素
        // 以下方法 返回封装此元素的 最里层元素
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        // 返回此类型元素的 完全限定名称
        String fullName = typeElement.getQualifiedName().toString();

        AnnotatedClass annotatedClass = annotatedClassMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, elementUtils);
            annotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;

    }

    private void error(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void note(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, "log: " + msg);
    }

    /**
     * 返回值为 字符串集合，包含本处理器想要处理的注解类型的合法全称。
     * 也即是，在此处 定义 你的注解处理器 应用于哪些注解上。
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());

        return types;
    }

    /**
     * 用来指定使用的 Java 版本
     * @return
     */
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latestSupported();
//    }

//    private void printMsg(String msg) {
//        System.out.println("log: " + msg);
//    }
}

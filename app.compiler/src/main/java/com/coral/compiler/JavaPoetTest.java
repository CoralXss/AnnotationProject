package com.coral.compiler;

import com.coral.annotations.router.RouterPage;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.lang.model.element.Modifier;

/**
 * Created by xss on 2017/10/20.
 */

@RouterPage(uris = "")
public class JavaPoetTest extends Test {
    private String member;
    private TestProcessor processor;
    protected String protectedField;
    public String publicField;

    public JavaPoetTest() {

    }

    @Deprecated
    public static void generateClass() {
        // 根据输入的 xxx.class 确定需要import的类，所以使用到了什么类，需要直接指明类的类型，不能直接字符串拼接类
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
//                .addStatement("System.out.println($S)", "Hello, JavaPoet!")
                .build();

        TypeSpec clazz = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.coral.helloworld", clazz).build();
        try {
            // 显示输出到控制台（注解处理器中则使用 辅助文件工具 Filer）
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JavaPoetTest.generateClass();

        Class clazz = JavaPoetTest.class;
        clazz.isAnnotationPresent(RouterPage.class);

        print("getName", clazz.getName());   // com.coral.compiler.JavaPoetTest
        print("getCanonicalName", clazz.getCanonicalName());  // com.coral.compiler.JavaPoetTest
        print("getSimpleName", clazz.getSimpleName());  // JavaPoetTest

        print("", clazz.getClass().toString());          // class java.lang.Class
        print("", clazz.getClassLoader().toString());    // sun.misc.Launcher$AppClassLoader@266474c2

//        print("", clazz.getDeclaringClass().toString());  // npe

        Class[] declaredClasses = clazz.getDeclaredClasses();
        for (Class c: declaredClasses) {
            print("getDeclaringClass", c.toString()); // JavaPoetTest$InnerAbstractClass  JavaPoetTest$InnerClass
        }

        // 有直接父类返回直接父类，没有继承则返回 Object
        print("", clazz.getGenericSuperclass().toString());// class java.lang.Object

        // 返回内部类的上层类-外部类，没有外部类返回null
//        print("", clazz.getEnclosingClass().toString());
        // 返回 class 所在包名
        print("", clazz.getPackage().toString());          // package com.coral.compiler

        print("", clazz.getSuperclass().toString());       // class java.lang.Object

        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation a: annotations) {
            print("", a.toString());
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            /**
             * clazz.getDeclaredFields():
             *  private java.lang.String com.coral.compiler.JavaPoetTest.member
             *  private com.coral.compiler.TestProcessor com.coral.compiler.JavaPoetTest.processor
             *  返回 public protected default private field，但是不包括继承的字段
             *
             * clazz.getFields():
             *  返回所有 public 字段，包括父类/接口中继承的public字段
             */
            print("field", field.toString());
        }

        Method[] methods = clazz.getDeclaredMethods();


        InnerClass.print();  // class com.coral.compiler.JavaPoetTest

        new InnerAbstractClass() {

            @Override
            public void printMsg() {
                // class com.coral.compiler.JavaPoetTest
                System.out.print(this.getClass().getEnclosingMethod());
            }
        };

    }

    public static class InnerClass {

        public void testEnclosingMethod() {

        }

        public static void print() {
            System.out.print(InnerClass.class.getEnclosingClass().toString());
//            System.out.print("EnclosingMethod" + InnerClass.class.getEnclosingMethod().toString());
        }


    }

    public static abstract class InnerAbstractClass {
        public abstract void printMsg();
    }
}

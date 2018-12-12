package com.coral.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by xss on 2017/9/29.
 * 2. 定义一个被注解类 AnnotatedClass，用于保存哪些被注解的对象
 */

public class AnnotatedClass {

    private TypeElement typeElement;
    private Elements elements;
    private ArrayList<BinderViewField> fields;

    AnnotatedClass(TypeElement typeElement, Elements elements) {
        this.typeElement = typeElement;
        this.elements = elements;
        this.fields = new ArrayList<>();
    }

    void addField(BinderViewField field) {
        fields.add(field);
    }

    JavaFile generateFile() {   // javapoet 生成代码
        // generate Method
        /**
         * 生成方法: bindView(MainActivity host,
         *                   Object source,
         *                   ViewFinder finder)
         */
        MethodSpec.Builder bindViewMethodBuilder = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(typeElement.asType()), "host") // MainActivity
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.PROVIDER, "finder");

        /**
         * 生成方法体：变量@BindView注释的所有成员变量，目前只有 tv_msg
         *           host.tv_msg = (TextView)(finder.findView(source, R.id.tv_msg));
         */
        for (BinderViewField field: fields) {
            // find views
            bindViewMethodBuilder.addStatement(
                    "host.$N = ($T)(finder.findView(source, $L))",
                    field.getFieldName(),                // tv_msg
                    ClassName.get(field.getFieldType()), // TextView
                    field.getResId());                   // R.id.tv_msg
        }

        MethodSpec.Builder unBindViewMethodBuilder = MethodSpec.methodBuilder("unBindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(typeElement.asType()), "host");

        for (BinderViewField field: fields) {
            unBindViewMethodBuilder.addStatement("" +
                    "host.$N = null",
                    field.getFieldName());
        }

        // generate class
        TypeSpec injectClass = TypeSpec.classBuilder(
                typeElement.getSimpleName() + "$$ViewBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(typeElement.asType())))
                .addMethod(bindViewMethodBuilder.build())
                .addMethod(unBindViewMethodBuilder.build())
                .build();
        String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, injectClass).build();
    }
}

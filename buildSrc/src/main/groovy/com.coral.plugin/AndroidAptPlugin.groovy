package com.coral.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.tasks.compile.GroovyCompile

/**
 * Created by xss on 2018/12/13.
 */
class AndroidAptPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def variants = null

        if (project.plugins.findPlugin("com.android.application") || project.plugins.findPlugin("android") ||
                project.plugins.findPlugin("com.android.test")) {
            variants = "applicationVariants"
        } else if (project.plugins.findPlugin("com.android.library") || project.plugins.findPlugin("android-library")) {
            variants = "libraryVariants"
        } else {
            throw new ProjectConfigurationException("The android or android-library plugin must be applied to the project.", null)
        }

        println("==============>>")
        println("plugins: " + project.plugins)

        println("==============>>")
        println("configurations: " + project.configurations)

        // 创建apt配置选项，继承自 compile 、provided
        def aptConfiguration = project.configurations.create('apt')
                .extendsFrom(project.configurations.compile, project.configurations.provided)

        // 该回调表示所有的模块都配置完成，可以准备执行 task 了。
        project.afterEvaluate {
            project.android[variants].all { variant ->

                println("==============>>")
                println("variant: " + variant)  // debug 和 release 两种
                // 如果 dependencies 中没有添加 apt 选项，就是空的
                if (aptConfiguration.empty) {
                    println("No apt dependencies for configuration ${aptConfiguration.name}")
                    return
                }

                // 注解处理器自动生成代码输出目录
                def aptOutputDir = project.file(new File(project.buildDir, "generated/source/apt"))
                def aptOutput = new File(aptOutputDir, variant.dirName)
                println("==============>>")
                // /Users/xss/Documents/AndroidStudioProjects/AnnotationProject/app/build/generated/source/apt/debug
                println("aptOutput: " + aptOutput)

                // true
                printOut("has javaCompiler: " + variant.hasProperty('javaCompiler'))
                def javaCompile = variant.hasProperty('javaCompiler') ? variant.javaCompiler : variant.javaCompile
                // variant.javaCompiler: task ':app:compileDebugJavaWithJavac'
                printOut("variant.javaCompiler: " + variant.javaCompiler)
                // variant.javaCompile: task ':app:compileDebugJavaWithJavac'
                printOut("variant.javaCompile: " + variant.javaCompile)
                variant.addJavaSourceFoldersToModel(aptOutput)

                // 处理路径：所有jar包、aar、module、project路径
                printOut("aptConfiguration: " + aptConfiguration)   // aptConfiguration: configuration ':app:apt'
                printOut("javaCompile.classpath: " + javaCompile.classpath)  // javaCompile.classpath: file collection
                def processorPath = (aptConfiguration + javaCompile.classpath).asPath
                printOut("processorPath: " + processorPath)
                def taskDependency = aptConfiguration.buildDependencies
                //
                printOut("taskDependency: " + taskDependency)
                // 编译依赖 apt 所在构建任务
                if (taskDependency) {
                    javaCompile.dependsOn += taskDependency
                }

                /**
                 * 以下为 javac 比那一起的标准选项配置
                 * javac 用法：javac <选项> <源文件>
                 * 选项包括：
                 * -d <目录> 指定存放生成的类文件位置
                 * -s <目录> 指定存放生成的源文件位置
                 * -processor <class1>[, <class2>, <class3>...] 要运行的注解处理程序的名称；绕过默认的搜索进程
                 * - processorpath <路径> 指定查找注解处理器程序的位置
                 */
                javaCompile.options.compilerArgs += ['-s', aptOutput]
                javaCompile.options.compilerArgs += ['-processorpath', processorPath]

                javaCompile.doFirst {
                    aptOutput.mkdirs()
                }

                def dependency = javaCompile.finalizedBy
                printOut("dependency: " + dependency)
                def dependencies = dependency.getDependencies(javaCompile)
                printOut("dependencies.length: " + dependencies)  // []

                for (def dep: dependencies) {
                    printOut("dep: " + dep)
                    if (dep instanceof GroovyCompile) {
                        printOut("dep groovyOptions: " + dep.groovyOptions.hasProperty("javaAnnotationProcessing"))
                        if (dep.groovyOptions.hasProperty("javaAnnotationProcessing")) {
                            dep.options.compilerArgs += javaCompile.options.compilerArgs
                            dep.groovyOptions.javaAnnotationProcessing = true
                        }
                    }
                }
            }
        }
    }

    static void printOut(def msg) {
        println("==============>>")
        println(msg)
    }

    static void configureVariant(def project, def aptConfiguration, def aptExtension) {

    }
}

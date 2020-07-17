/*
 * Copyright 2020 Mohammed Khalid Hamid.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dependencies

import com.android.build.gradle.*
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.kotlin.dsl.*
import org.gradle.testing.jacoco.plugins.JacocoPlugin
/**
 * This my custom plugin for Android projects
 * The setups testing and base configuration
 */
open class KhalidAndroidPlugin : Plugin<Project>, Utility() {
    /**
     * Determines if a Project is the 'library' module
     */
    val Project.isLibrary get() = name == "app"
    val Project.configDir get() = "$rootDir/quality"
    private var lintExclusionRules = arrayListOf("ObsoleteLintCustomCheck", // ButterKnife will fix this in v9.0
        "IconExpectedSize",
        "InvalidPackage", // Firestore uses GRPC which makes lint mad
        "NewerVersionAvailable", "GradleDependency", // For reproducible builds
        "SelectableText", "SyntheticAccessor")
    override fun apply(target: Project) {
        ext = target.extensions.create<KPluginExtensions>("KPlugin")
        target.applyPlugins((target.name == "app"))
        System.out.println("name "+ target.name)
        //TODO: unable to use extension property in apply function
        target.configureAndroid()
        target.configureQuality()
        System.out.println("ext  ..after "+ ext.compileSDK)
        target.afterEvaluate {
            System.out.println("afterEvaluate")
            target.extensions.getByType(KPluginExtensions::class.java).run {
                val jacocoOptions = this.jacoco
                if (jacocoOptions.isEnabled) {
                    // Setup jacoco tasks to generate coverage report for this module.
                    target.plugins.apply(JacocoPlugin::class.java)
                    target.plugins.all {
                        when (this) {
                            is LibraryPlugin -> {
                                target.extensions.getByType(LibraryExtension::class.java).run {
                                    configureJacoco(target, libraryVariants, jacocoOptions)
                                }
                            }
                            is AppPlugin -> {
                                target.extensions.getByType(AppExtension::class.java).run {
                                    configureJacoco(target, applicationVariants, jacocoOptions)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun Project.configureKotlin(){
        configure<BaseExtension>{
            compileSdkVersion(ext.compileSDK)
        }
    }

    fun Project.configureAndroid() {
        configure<BaseExtension>{
            System.out.println(" compileSDK "+ ext.compileSDK)
            compileSdkVersion(ext.compileSDK)
            buildToolsVersion(ext.buildTools)
            defaultConfig {
                System.out.println(" min sdk "+ ext.minSDK)
                System.out.println(" targetSDK "+ ext.targetSDK)
                System.out.println(" testRunner  "+ ext.testRunner)
                System.out.println(" lintExclusionRules "+ ext.lintExclusionRules)
                System.out.println(" isLibraryModule "+ ext.isLibraryModule)
                System.out.println(" lintExclusionRules "+ ext.lintExclusionRules.toString())
                minSdkVersion("21")
                multiDexEnabled = true
                targetSdkVersion(ext.targetSDK)
                versionName = ext.versionName
                versionCode = ext.versionCode
                //dataBinding.isEnabledForTests = true
                vectorDrawables.useSupportLibrary = true
                testInstrumentationRunner = ext.testRunner

                javaCompileOptions {
                    annotationProcessorOptions {
                        arguments = mapOf("room.schemaLocation" to "$projectDir/schemas\".toString()")
                        arguments = mapOf("room.incremental" to "true")
                        arguments = mapOf("room.expandProjection" to "true")
                        arguments = mapOf("androidx.room.RoomProcessor" to "true")
                        arguments = mapOf("dagger.gradle.incremental" to "true")
                    }
                }
            }





            lintOptions {
                disable(
                    "ObsoleteLintCustomCheck", // ButterKnife will fix this in v9.0
                    "IconExpectedSize",
                    "InvalidPackage", // Firestore uses GRPC which makes lint mad
                    "NewerVersionAvailable", "GradleDependency", // For reproducible builds
                    "SelectableText", "SyntheticAccessor" // We almost never care about this
                )
                isCheckAllWarnings = true
                isWarningsAsErrors = true
                isAbortOnError = true
                baselineFile = getLintBaseline()
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = true
                    if(ext.isLibraryModule){
                        consumerProguardFile("proguard-android.txt")
                    }else{
                        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
                    }
                }
                getByName("debug"){
                    isDebuggable = true
                }
            }

            packagingOptions {
                exclude("LICENSE.txt")
                exclude("META-INF/rxjava.properties")
                exclude("META-INF/DEPENDENCIES")
                exclude("META-INF/LICENSE")
                exclude("META-INF/LICENSE.txt")
                exclude("META-INF/license.txt")
                exclude("META-INF/NOTICE")
                exclude("META-INF/NOTICE.txt")
                exclude("META-INF/notice.txt")
                exclude("META-INF/ASL2.0")
            }

            testOptions.unitTests.isReturnDefaultValues = true
            testOptions.unitTests.isIncludeAndroidResources = true

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            dependencies {
                unitTest()
                UITest()
            }
        }
    }

    fun Project.configureQuality() {
        apply(plugin = "checkstyle")

        configure<CheckstyleExtension> { toolVersion = "8.10.1" }
        tasks.named("check").configure { dependsOn("checkstyle") }

        tasks.register<Checkstyle>("checkstyle") {
            var path = ext.checkstylePath
            if(path.length <= 0){
                path = "${project.configDir}/checkstyle.xml"
            }
            configFile = file(path)
            source("src")
            include("**/*.java")
            exclude("**/gen/**")
            classpath = files()
        }
    }

    internal fun Project.configurePlugins() {
        plugins.apply("com.android.library")
        plugins.apply("org.gradle.maven-publish") // or anything else, that you would like to load
    }



}
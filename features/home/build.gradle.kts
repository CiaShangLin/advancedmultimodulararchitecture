import deps.DependenciesVersions
import deps.androidx
import deps.dataModule
import deps.domainModule
import deps.hilt
import deps.navigatorModule
import deps.presentationModule
import deps.retrofit
import deps.room
import deps.testDebugDeps
import deps.testDeps
import deps.testImplDeps
import plugs.SharedLibraryGradlePlugin

plugins {
    id(plugs.BuildPlugins.ANDROID_LIBRARY)
}

apply<SharedLibraryGradlePlugin>()

android {
    namespace = "com.shang.home"

    composeOptions {
        kotlinCompilerExtensionVersion = DependenciesVersions.KOTLIN_COMPILER
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    androidx()
    retrofit()
    dataModule()
    domainModule()
    presentationModule()
    navigatorModule()
    hilt()
    room()
    testDeps()
    testImplDeps()
    testDebugDeps()
}

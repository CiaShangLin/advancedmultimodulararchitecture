import deps.androidx
import deps.dataModule
import deps.domainModule
import deps.hilt
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
    namespace = "com.shang.login"
}

dependencies {
    domainModule()
    dataModule()
    androidx()
    retrofit()
    hilt()
    room()
    testDeps()
    testImplDeps()
    testDebugDeps()
}

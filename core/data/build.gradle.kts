import deps.chucker
import deps.dataStore
import deps.domainModule
import deps.hilt
import deps.kotlinx
import deps.okHttp
import deps.protodatastoreModule
import deps.retrofit
import deps.testDebugDeps
import deps.testDeps
import deps.testImplDeps
import plugs.SharedLibraryGradlePlugin

plugins {
    id(plugs.BuildPlugins.ANDROID_LIBRARY)
}

apply<SharedLibraryGradlePlugin>()

android {
    namespace = "com.shang.data"
}

dependencies {
    domainModule()
    protodatastoreModule()
    okHttp()
    retrofit()
    hilt()
    chucker()
    dataStore()
    kotlinx()
    testDeps()
    testImplDeps()
    testDebugDeps()
}

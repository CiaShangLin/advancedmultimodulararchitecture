import deps.dataStore
import deps.hilt
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
    protodatastoreModule()
    okHttp()
    retrofit()
    hilt()
    dataStore()
    testDeps()
    testImplDeps()
    testDebugDeps()
}

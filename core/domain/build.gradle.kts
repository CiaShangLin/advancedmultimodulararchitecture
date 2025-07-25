import deps.kotlinx
import deps.testDebugDeps
import deps.testDeps
import deps.testImplDeps
import plugs.SharedLibraryGradlePlugin

plugins {
    id(plugs.BuildPlugins.ANDROID_LIBRARY)
}

apply<SharedLibraryGradlePlugin>()

android {
    namespace = "com.shang.domain"
}

dependencies {
    kotlinx()
    testDeps()
    testImplDeps()
    testDebugDeps()
}

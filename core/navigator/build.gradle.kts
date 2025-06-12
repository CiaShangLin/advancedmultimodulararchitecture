import deps.androidx
import deps.hilt
import plugs.SharedLibraryGradlePlugin
import deps.testDebugDeps
import deps.testDeps
import deps.testImplDeps

plugins {
    id(plugs.BuildPlugins.ANDROID_LIBRARY)
}

apply<SharedLibraryGradlePlugin>()

android {
    namespace = "com.shang.navigator"
}

dependencies {
    androidx()
    hilt()
    testDeps()
    testImplDeps()
    testDebugDeps()
}

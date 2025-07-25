import deps.Dependencies.protoBufArtifact
import deps.hilt
import deps.protoDataStore
import deps.testDebugDeps
import deps.testDeps
import deps.testImplDeps
import plugs.SharedLibraryGradlePlugin

plugins {
    id(plugs.BuildPlugins.ANDROID_LIBRARY)
    id(plugs.BuildPlugins.GOOGLE_PROTOBUF)
}

apply<SharedLibraryGradlePlugin>()

android {
    namespace = "com.shang.protodatastore"

    protobuf {
        protoc {
            artifact = protoBufArtifact
        }
        generateProtoTasks {
            all().forEach { task ->
                task.plugins {
                    create("kotlin").apply {
                        option("lite")
                    }
                }
                task.plugins {
                    create("java").apply {
                        option("lite")
                    }
                }
            }
        }
    }
}

dependencies {
    protoDataStore()
    hilt()
    testDeps()
    testImplDeps()
    testDebugDeps()
}

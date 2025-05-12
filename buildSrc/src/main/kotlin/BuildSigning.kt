import com.android.build.api.dsl.ApkSigningConfig
import org.gradle.api.NamedDomainObjectContainer
import java.io.File

sealed class BuildSigning(val name: String) {
    abstract fun create(namedDomainObjectContainer: NamedDomainObjectContainer<out ApkSigningConfig>)


    object Debug : BuildSigning(SigningTypes.DEBUG) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<out ApkSigningConfig>) {
            namedDomainObjectContainer.getByName(name) {
                storeFile = File("")
                storePassword = ""
                keyAlias = ""
                keyPassword = ""
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }

    object Release : BuildSigning(SigningTypes.RELEASE) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<out ApkSigningConfig>) {
            namedDomainObjectContainer.create(name) {
                storeFile = File("")
                storePassword = ""
                keyAlias = ""
                keyPassword = ""
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }

    object ReleaseExternalQA : BuildSigning(SigningTypes.RELEASE_EXTERNAL_QA) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<out ApkSigningConfig>) {
            namedDomainObjectContainer.create(name) {
                storeFile = File("")
                storePassword = ""
                keyAlias = ""
                keyPassword = ""
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }
}
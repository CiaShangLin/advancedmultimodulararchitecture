package build

import extensions.buildConfigBooleanField
import extensions.buildConfigIntField
import extensions.buildConfigStringField
import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.LibraryBuildType
import extensions.getLocalProperty
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

sealed class BuildCreator(val name: String) {

    abstract fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationBuildType>): ApplicationBuildType
    abstract fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryBuildType>): LibraryBuildType

    class Debug(private val project: Project) : BuildCreator(BuildTypes.DEBUG) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationBuildType>): ApplicationBuildType {
            return namedDomainObjectContainer.getByName(name) {
                isMinifyEnabled = Build.Debug.isMinifyEnabled
                enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage
                isDebuggable = Build.Debug.isDebuggable
                versionNameSuffix = Build.Debug.versionNameSuffix
                applicationIdSuffix = Build.Debug.applicationIdSuffix

                buildConfigStringField(
                    BuildVariables.BASE_URL,
                    project.getLocalProperty("dev.debug_endpoint")
                )
                buildConfigIntField(
                    BuildVariables.DB_VERSION,
                    project.getLocalProperty("dev.db_version")
                )
                buildConfigBooleanField(
                    BuildVariables.CAN_CLEAR_CACHE,
                    project.getLocalProperty("dev.clear_cache")
                )
                buildConfigStringField(
                    BuildVariables.MAP_KEY,
                    project.getLocalProperty("dev.map_key")
                )
                buildConfigStringField(
                    BuildVariables.PIN_CERTIFICATE,
                    project.getLocalProperty("dev.pin_certificate")
                )
            }
        }

        override fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryBuildType>): LibraryBuildType {
            return namedDomainObjectContainer.getByName(name) {
                isMinifyEnabled = Build.Debug.isMinifyEnabled
                enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage

                buildConfigStringField(
                    BuildVariables.BASE_URL,
                    project.getLocalProperty("dev.debug_endpoint")
                )
                buildConfigIntField(
                    BuildVariables.DB_VERSION,
                    project.getLocalProperty("dev.db_version")
                )
                buildConfigBooleanField(
                    BuildVariables.CAN_CLEAR_CACHE,
                    project.getLocalProperty("dev.clear_cache")
                )
                buildConfigStringField(
                    BuildVariables.MAP_KEY,
                    project.getLocalProperty("dev.map_key")
                )
                buildConfigStringField(
                    BuildVariables.PIN_CERTIFICATE,
                    project.getLocalProperty("dev.pin_certificate")
                )
            }
        }
    }

    class Release(private val project: Project) : BuildCreator(BuildTypes.RELEASE) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationBuildType>): ApplicationBuildType {
            return namedDomainObjectContainer.getByName(name) {
                isMinifyEnabled = Build.Release.isMinifyEnabled
                enableUnitTestCoverage = Build.Release.enableUnitTestCoverage
                isDebuggable = Build.Release.isDebuggable

                buildConfigStringField(
                    BuildVariables.BASE_URL,
                    project.getLocalProperty("dev.prod_endpoint")
                )
                buildConfigIntField(
                    BuildVariables.DB_VERSION,
                    project.getLocalProperty("dev.db_version")
                )
                buildConfigBooleanField(
                    BuildVariables.CAN_CLEAR_CACHE,
                    project.getLocalProperty("dev.clear_cache")
                )
                buildConfigStringField(
                    BuildVariables.MAP_KEY,
                    project.getLocalProperty("release.map_key")
                )
                buildConfigStringField(
                    BuildVariables.PIN_CERTIFICATE,
                    project.getLocalProperty("release.pin_certificate")
                )
            }
        }

        override fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryBuildType>): LibraryBuildType {
            return namedDomainObjectContainer.getByName(name) {
                isMinifyEnabled = Build.Release.isMinifyEnabled
                enableUnitTestCoverage = Build.Release.enableUnitTestCoverage


                buildConfigStringField(
                    BuildVariables.BASE_URL,
                    project.getLocalProperty("dev.prod_endpoint")
                )
                buildConfigIntField(
                    BuildVariables.DB_VERSION,
                    project.getLocalProperty("dev.db_version")
                )
                buildConfigBooleanField(
                    BuildVariables.CAN_CLEAR_CACHE,
                    project.getLocalProperty("dev.clear_cache")
                )
                buildConfigStringField(
                    BuildVariables.MAP_KEY,
                    project.getLocalProperty("release.map_key")
                )
                buildConfigStringField(
                    BuildVariables.PIN_CERTIFICATE,
                    project.getLocalProperty("release.pin_certificate")
                )
            }
        }
    }

    class ReleaseExternalQA(private val project: Project) : BuildCreator(BuildTypes.RELEASE_EXTERNAL_QA) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationBuildType>): ApplicationBuildType {
            return namedDomainObjectContainer.create(name) {
                isMinifyEnabled = Build.ReleaseExternalQa.isMinifyEnabled
                enableUnitTestCoverage = Build.ReleaseExternalQa.enableUnitTestCoverage
                isDebuggable = Build.ReleaseExternalQa.isDebuggable
                versionNameSuffix = Build.ReleaseExternalQa.versionNameSuffix
                applicationIdSuffix = Build.ReleaseExternalQa.applicationIdSuffix


                buildConfigStringField(
                    BuildVariables.BASE_URL,
                    project.getLocalProperty("dev.qa_endpoint")
                )
                buildConfigIntField(
                    BuildVariables.DB_VERSION,
                    project.getLocalProperty("dev.db_version")
                )
                buildConfigBooleanField(
                    BuildVariables.CAN_CLEAR_CACHE,
                    project.getLocalProperty("dev.clear_cache")
                )
                buildConfigStringField(
                    BuildVariables.MAP_KEY,
                    project.getLocalProperty("dev.map_key")
                )
                buildConfigStringField(
                    BuildVariables.PIN_CERTIFICATE,
                    project.getLocalProperty("release.pin_certificate")
                )
            }
        }

        override fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryBuildType>): LibraryBuildType {
            return namedDomainObjectContainer.create(name) {
                isMinifyEnabled = Build.ReleaseExternalQa.isMinifyEnabled
                enableUnitTestCoverage = Build.ReleaseExternalQa.enableUnitTestCoverage


                buildConfigStringField(
                    BuildVariables.BASE_URL,
                    project.getLocalProperty("dev.qa_endpoint")
                )
                buildConfigIntField(
                    BuildVariables.DB_VERSION,
                    project.getLocalProperty("dev.db_version")
                )
                buildConfigBooleanField(
                    BuildVariables.CAN_CLEAR_CACHE,
                    project.getLocalProperty("dev.clear_cache")
                )
                buildConfigStringField(
                    BuildVariables.MAP_KEY,
                    project.getLocalProperty("dev.map_key")
                )
                buildConfigStringField(
                    BuildVariables.PIN_CERTIFICATE,
                    project.getLocalProperty("release.pin_certificate")
                )
            }
        }
    }
}
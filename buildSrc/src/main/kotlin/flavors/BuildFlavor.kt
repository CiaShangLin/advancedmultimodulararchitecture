package flavors

import build.BuildDimensions
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.LibraryProductFlavor
import org.gradle.api.NamedDomainObjectContainer

sealed class BuildFlavor(val name: String) {
    abstract fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationProductFlavor>): ApplicationProductFlavor
    abstract fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryProductFlavor>): LibraryProductFlavor


    object Google : BuildFlavor(FlavorType.GOOGLE){
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationProductFlavor>): ApplicationProductFlavor {
            return namedDomainObjectContainer.create(name){
                dimension = BuildDimensions.STORE
                applicationIdSuffix = ".${name}"
                versionNameSuffix = "-${name}"
            }
        }

        override fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryProductFlavor>): LibraryProductFlavor {
            return namedDomainObjectContainer.create(name){
                dimension = BuildDimensions.STORE
            }
        }
    }

    object Huawei : BuildFlavor(FlavorType.HUAWEI){
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationProductFlavor>): ApplicationProductFlavor {
            return namedDomainObjectContainer.create(name){
                dimension = BuildDimensions.STORE
                applicationIdSuffix = ".${name}"
                versionNameSuffix = "-${name}"
            }
        }

        override fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryProductFlavor>): LibraryProductFlavor {
            return namedDomainObjectContainer.create(name){
                dimension = BuildDimensions.STORE
            }
        }
    }

    object Driver : BuildFlavor(FlavorType.DRIVER){
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationProductFlavor>): ApplicationProductFlavor {
            return namedDomainObjectContainer.create(name){
                dimension = BuildDimensions.APP
                applicationIdSuffix = ".${name}"
                versionNameSuffix = "-${name}"
            }
        }

        override fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryProductFlavor>): LibraryProductFlavor {
            return namedDomainObjectContainer.create(name){
                dimension = BuildDimensions.APP
            }
        }
    }

    object Client : BuildFlavor(FlavorType.CLIENT){
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationProductFlavor>): ApplicationProductFlavor {
            return namedDomainObjectContainer.create(name){
                dimension = BuildDimensions.APP
                applicationIdSuffix = ".${name}"
                versionNameSuffix = "-${name}"
            }
        }

        override fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryProductFlavor>): LibraryProductFlavor {
            return namedDomainObjectContainer.create(name){
                dimension = BuildDimensions.APP
            }
        }
    }
}
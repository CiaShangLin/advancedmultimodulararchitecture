import com.android.build.api.dsl.ApkSigningConfig
import org.gradle.api.NamedDomainObjectContainer

sealed class BuildSigning(val name:String) {
    abstract fun create(namedDomainObjectContainer:NamedDomainObjectContainer<out ApkSigningConfig>)
}
# Advanced Android Course:MVVM, Multi-Flavored Builds, Data Store, Advanced Gradle, Custome Plugins, Networking

網址: https://www.udemy.com/course/mastering-multi-modular-clean-architecture-in-android/learn/lecture/45680117#overview

[https://github.com/minafarideleia/AdvancedMultiModularArchitecture](https://github.com/minafarideleia/AdvancedMultiModularArchitecture)

# 第一章

介紹課程，適合怎麼樣的人，課程章節會學到什麼，Github連結

# 第二章

### Lect6

創立一個buildSrc資料夾，新增build.gradle.kts檔案，然後設定kotlin-dsl跟repositories，編譯後就會跑出build和.gradle的文件。

```jsx
plugins {
    `kotlin-dsl`
}
repositories{
    mavenCentral()
}
```

### Lect7

透過kotlin文件去設定gradle參數，整體來說還不錯，很像透過kotlin去設定，透過檔案名稱去區隔。

這樣子的優點是等到其他module也可以直接使用還清楚

例如

```jsx
object BuildConfig {
    const val APP_ID = "com.minafarid.advancedmultimodulararchitecture"
    const val COMPILE_SDK_VERSION = 34
    const val MIN_SDK_VERSION = 24
    const val TARGET_SDK_VERSION = 34
}
```

![image.png](image.png)

### Lect8

繼續新增Dependencies.kt現在連gradle引入都抽成kotlin下去處理，他這裡Version跟implementation是分成兩個class下去處理，原先是用新一點toml引入的，現在改成kotlin不知道有沒有比較好維護?不過可以確定的是gradle要是kts才能使用，所以相對toml的支援度比較好。

```jsx
object Dependencies {
    const val ANDROIDX_CORE = "androidx.core:core-ktx:${DependenciesVersions.CORE_KTX}"
    const val ANDROIDX_LIFECYCLE_RUNTIME_KTX =
        "androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersions.LIFE_CYCLE_RUNTIME_KTX}"
    const val ANDROIDX_ACTIVITY_COMPOSE =
        "androidx.activity:activity-compose:${DependenciesVersions.ACTIVITY_COMPOSE}"
    const val ANDROIDX_UI = "androidx.compose.ui:ui:${DependenciesVersions.COMPOSE_UI}"
    const val ANDROIDX_UI_GRAPHICS = "androidx.compose.ui:ui-graphics:${DependenciesVersions.COMPOSE_UI}"
    const val ANDROIDX_UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview:${DependenciesVersions.COMPOSE_UI}"
    const val ANDROIDX_MATERIAL3= "androidx.compose.material3:material3:${DependenciesVersions.MATERIAL_3}"
}

object DependenciesVersions {
    const val CORE_KTX = "1.10.1"
    const val LIFE_CYCLE_RUNTIME_KTX = "2.6.1"
    const val KOTLIN = "1.9.0"
    const val JUNIT = "4.13.2"
    const val JUNIT_VERSION = "1.1.5"
    const val ESPRESSO_CORE = "3.5.1"
    const val ACTIVITY_COMPOSE = "1.8.0"
    const val COMPOSE_UI = "1.6.8"
    const val MATERIAL_3 = "1.2.1"
}

 implementation(Dependencies.ANDROIDX_CORE)
 implementation(Dependencies.ANDROIDX_LIFECYCLE_RUNTIME_KTX)
 implementation(Dependencies.ANDROIDX_ACTIVITY_COMPOSE)
 implementation(Dependencies.ANDROIDX_UI)
 implementation(Dependencies.ANDROIDX_UI_GRAPHICS)
 implementation(Dependencies.ANDROIDX_UI_TOOLING_PREVIEW)
 implementation(Dependencies.ANDROIDX_MATERIAL3)
```

### Lect9

持續分割出來，這一章是把測試單獨獨立出來

```kotlin
object TestDependencies {
    const val ANDROIDX_JUNIT = "androidx.test.ext:junit:${DependenciesVersions.JUNIT}"
    const val ANDROIDX_ESPRESSO_CORE =
        "androidx.test.espresso:espresso-core:${DependenciesVersions.ESPRESSO_CORE}"
    const val ANDROIDX_COMPOSE_UI_TEST =
        "androidx.compose.ui:ui-test-junit4:${DependenciesVersions.COMPOSE_UI}"
    const val ANDROIDX_COMPOSE_UI_TEST_MANIFEST ="androidx.compose.ui:ui-test-manifest:${DependenciesVersions.COMPOSE_UI}"
}
```

# 第三章

### Lect10

接來是把打包的維度切出來，果然沒在用Flavor跟Dimensions真的是比較沒感覺，不知道切出來會是怎麼樣？

作者有舉例Uber透過FlavorType在一個專案中去打包出駕駛用和客戶用

```kotlin
object FlavorType {
    const val GOOGLE = "google"
    const val HUAWEI = "huawei"
    const val DRIVER = "driver"
    const val CLIENT = "client"
}

object BuildTypes {
    const val DEBUG = "debug"
    const val RELEASE = "release"
    const val RELEASE_EXTERNAL_QA = "releaseExternalQA"
}

object BuildDimensions {
    const val APP = "app"
    const val STORE = "store"
}
```

### Lect11

在自己寫的buildSrc去引入kotlin
這裡的寫法是簡化寫法

還要把原先的alias註解掉不然會編譯失敗

以前是用classpath，之後新的是alias，現在他用kotlin()+api是為了曝露出去，對應的就是app層的gradle引入plugins的東西

```kotlin
repositories{
    google()
    mavenCentral()
}

dependencies{
    api(kotlin("gradle-plugin:1.9.0"))
    implementation("com.android.tools.build:gradle:8.1.4")
}

plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

plugins {
    id(BuildPlugins.KOTLIN_ANDROID)
    id(BuildPlugins.ANDROID_APPLICATION)
}
```

### Lect12

現在要用來設定BuildType，沒想到可以用sealed class去設定

```kotlin
sealed class Build {
    open val isMinifyEnabled = false
    open val isDebuggable = false
    open val applicationIdSuffix = ""
    open val versionNameSuffix = ""
    open val enableUnitTestCoverage = false

    object Debug : Build() {
        override val isMinifyEnabled = false
        override val isDebuggable = true
        override val applicationIdSuffix = ".debug"
        override val versionNameSuffix = "-debug"
        override val enableUnitTestCoverage = true
    }

    object ReleaseExternalQA : Build() {
        override val isMinifyEnabled = false
        override val isDebuggable = false
        override val applicationIdSuffix = ".QA"
        override val versionNameSuffix = "-releaseExternalQA"
        override val enableUnitTestCoverage = true
    }

    object Release : Build() {
        override val isMinifyEnabled = true
        override val isDebuggable = false
        override val enableUnitTestCoverage = true
    }
}
```

### Lect13

沒想到連BuildTypes也能用Kotlin處理

用getByName是代表原先已經有這個variants了

用create是代表原先沒有這個

```jsx
 buildTypes {
        getByName(BuildTypes.RELEASE) {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = Build.Release.isMinifyEnabled
            enableUnitTestCoverage = Build.Release.enableUnitTestCoverage
            isDebuggable = Build.Release.isDebuggable
        }

        getByName(BuildTypes.DEBUG) {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = Build.Debug.isMinifyEnabled
            enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage
            isDebuggable = Build.Debug.isDebuggable
            versionNameSuffix = Build.Debug.versionNameSuffix
            applicationIdSuffix = Build.Debug.applicationIdSuffix
        }

        create(BuildTypes.RELEASE_EXTERNAL_QA) {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = Build.ReleaseExternalQA.isMinifyEnabled
            enableUnitTestCoverage = Build.ReleaseExternalQA.enableUnitTestCoverage
            isDebuggable = Build.ReleaseExternalQA.isDebuggable
            versionNameSuffix = Build.ReleaseExternalQA.versionNameSuffix
            applicationIdSuffix = Build.ReleaseExternalQA.applicationIdSuffix
        }
    }
```

### Lect14

BuildFlavor自己創立了create分別是application層跟library層，要等到下一章才會實作。

```jsx
sealed class BuildFlavor(val name: String) {
    abstract fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationProductFlavor>): ApplicationProductFlavor
    abstract fun createLibrary(namedDomainObjectContainer: NamedDomainObjectContainer<LibraryProductFlavor>): LibraryProductFlavor
}
```

Cladue產生

```
android {
    // 首先定義維度，順序很重要（優先級從高到低）
    flavorDimensions("version", "market")
    
    productFlavors {
        // 版本維度的 flavors
        free {
            dimension = "version"
            applicationIdSuffix = ".free"
            buildConfigField("boolean", "IS_PREMIUM", "false")
        }
        
        premium {
            dimension = "version"
            applicationIdSuffix = ".premium"
            buildConfigField("boolean", "IS_PREMIUM", "true")
        }
        
        // 市場維度的 flavors
        china {
            dimension = "market"
            buildConfigField("String", "API_ENDPOINT", "\"https://api.example.cn\"")
            buildConfigField("boolean", "USE_CHINA_SERVICES", "true")
        }
        
        global {
            dimension = "market"
            buildConfigField("String", "API_ENDPOINT", "\"https://api.example.com\"")
            buildConfigField("boolean", "USE_CHINA_SERVICES", "false")
        }
    }
}
```

### 實際效果

這個配置會生成以下四種構建變體：

1. **freeChina**: 免費版 + 中國市場特定配置
2. **freeGlobal**: 免費版 + 全球市場特定配置
3. **premiumChina**: 付費版 + 中國市場特定配置
4. **premiumGlobal**: 付費版 + 全球市場特定配置

### Lect15

根據前面添加的flavorDimensions有這有順序優先

所以變成flavorDimensions+FlavorType+buildTypes

做到現在感覺有點亂了

```jsx
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
```

![擷取.PNG](%E6%93%B7%E5%8F%96.png)

### Lect16

接上的方法依樣是使用productFlavors，他算是用上了Kotlin的DSL作用域。

```kotlin
productFlavors{
        BuildFlavor.Google.create(this)
        BuildFlavor.Huawei.create(this)
        BuildFlavor.Driver.create(this)
        BuildFlavor.Client.create(this)
}    
```

創立對應的資料夾，放入對應的strings.xml之後，目前在main的ui就可以讀取到，另外資料夾上面有一個綠色的方框代表是當前的BuildVariant。

![image.png](image%201.png)

透過applicationIdSuffix的設定選擇不同的BuildVariant後可以同時安裝相同的APP，因為他們的applicationId不同。例如:

com.minafarid.advancedmultimodulararchitecture.client.google.debug

com.minafarid.advancedmultimodulararchitecture.driver.huawei.debug

透過一樣路徑的資料夾和依樣名稱的class，這邊可以自動判別打包引入使用的對象，如果切換到其他Flavor但是沒有這個class，AS會自動報錯編譯不過，這個真的牛B跟之前想像的不太依樣，以為都是引入Module的形式，這個沒看過還真的是不知道。

```kotlin
Greeting(
	name = stringResource(R.string.app_name),
	data = DataProvider.userName,
	data2 = Shang.data,
	mapId = MapProvider.mapId,
	modifier = Modifier.padding(innerPadding)
)
```

作用域的猜想應該跟我猜的依樣，以這個專案的例子是三個維度，如果要添加程式碼or資源檔案，只要其中一個有添加在同一個維度的也要有相同的檔案。只是目前還不確定有沒有前後順序的差異，他還可以做到維度的組合，例如:clientGoogle，clientHuawei，drvierGoogle，driverHuawei，變成四個都要去添加

- buildTypes : debug，release，releaseQA
- flavorDimensions : APP，STORE
- productFlavors : Client，Driver

![image.png](image%202.png)

### Lect17

要來添加signingConfigs，不同的Type也需要不同的簽名嗎?

我看過APP進入頁面是可以選擇客戶or司機的，不知道像uber這樣是分兩個APP出來還是一個APP不同頁面?

開始對於用kts去設定Gradle有感覺了，主要就是作用域的設定添加，透過kotlin的extension使用.()的方法去操作裡面的作用域

```kotlin
sealed class BuildSigning(val name:String) {
    abstract fun create(namedDomainObjectContainer:NamedDomainObjectContainer<out ApkSigningConfig>)
}
```

### Lect18

實作了BuildSigning道理和buildTypes一樣，只是這裡目前還不知道為什麼只有Debug需要getByName而Release卻不需要。

signingConfigs的順序要在buildTypes不然他會找不到。

```kotlin
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
```

### Lect19

寫了一個dev_credentials.properties來放簽名要用的參數，比較常在py做這件事情，之前放google map api key也是這裡。

沒想到連拿取getLocalProperty也能寫成函數

```kotlin
private const val LOCAL_PROPERTIES_FILE_NAME = "dev_credentials"

fun Project.getLocalProperty(propertyName: String): String {
    val localProperties = Properties().apply {
        val localPropertiesFile = rootProject.file(LOCAL_PROPERTIES_FILE_NAME)
        if (localPropertiesFile.exists()) {
            load(FileInputStream(localPropertiesFile))
        }
    }
    return localProperties.getProperty(propertyName)
}
```

### Lect20

Debug的部分是使用預設的debug.keystore，原來他是放在.android資料夾的。

Release的部分就是去讀取dev_credentials.properties設定。

這裡不知道為什麼不乾脆把project在create的時候傳入？

在mac上直接跳轉到android資料夾的方法

cd ~/.android/

open .

```kotlin
class Debug(private val project: Project) : BuildSigning(SigningTypes.DEBUG) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<out ApkSigningConfig>) {
            namedDomainObjectContainer.getByName(name) {
                storeFile = File(project.rootProject.rootDir,"debug.keystore")
                storePassword = "android"
                keyAlias = "androiddebugkey"
                keyPassword = "android"
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }
    
class Release(private val project: Project) : BuildSigning(SigningTypes.RELEASE) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<out ApkSigningConfig>) {
            namedDomainObjectContainer.create(name) {
                storeFile = File(project.getLocalProperty("release_key.store"))
                storePassword = project.getLocalProperty("release_store.password")
                keyAlias = project.getLocalProperty("release_key.alias")
                keyPassword = project.getLocalProperty("release_key.password")
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }
```

### Lect21

實際打包一下debug的看看apk長什麼樣子而已

### Lect22

連buildType都要抽成kotlin有點煩，最後使用還要用apply添加其他的，應該也是可以傳入只是有點麻煩。

最後說要額外添加buildConfig = true，這個不是BuildConfig跟這個打包有什麼關係？

```kotlin
sealed class BuildCreator(val name: String) {

    abstract fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationBuildType>): ApplicationBuildType

    object Debug : BuildCreator(BuildTypes.DEBUG) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationBuildType>): ApplicationBuildType {
            return namedDomainObjectContainer.getByName(name) {
                isMinifyEnabled = Build.Debug.isMinifyEnabled
                enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage
                isDebuggable = Build.Debug.isDebuggable
                versionNameSuffix = Build.Debug.versionNameSuffix
                applicationIdSuffix = Build.Debug.applicationIdSuffix
            }
        }
    }

    object Release : BuildCreator(BuildTypes.RELEASE) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationBuildType>): ApplicationBuildType {
            return namedDomainObjectContainer.getByName(name) {
                isMinifyEnabled = Build.Release.isMinifyEnabled
                enableUnitTestCoverage = Build.Release.enableUnitTestCoverage
                isDebuggable = Build.Release.isDebuggable
            }
        }
    }

    object ReleaseExternalQA : BuildCreator(BuildTypes.RELEASE_EXTERNAL_QA) {
        override fun create(namedDomainObjectContainer: NamedDomainObjectContainer<ApplicationBuildType>): ApplicationBuildType {
            return namedDomainObjectContainer.create(name) {
                isMinifyEnabled = Build.ReleaseExternalQa.isMinifyEnabled
                enableUnitTestCoverage = Build.ReleaseExternalQa.enableUnitTestCoverage
                isDebuggable = Build.ReleaseExternalQa.isDebuggable
                versionNameSuffix = Build.ReleaseExternalQa.versionNameSuffix
                applicationIdSuffix = Build.ReleaseExternalQa.applicationIdSuffix
            }
        }
    }
}
```

### Lect23

怪不得要導入project進去，設定BuildConfig的方法跟以前一樣，只是寫的位置不同

還有自己去擴展ApplicationBuildType.buildConfigStringField

```kotlin
fun ApplicationBuildType.buildConfigStringField(name: String, value: String) {
    this.buildConfigField("String", name, value)
}

buildConfigStringField(
    BuildVariables.BASE_URL,
    project.getLocalProperty("dev.qa_endpoint")
)
```

### Lect24

創立jks檔案測試release的flavor能不能啟動

### Lect25

只是幫BuildSrc的class分資料夾而已，mac的as怪怪的

# 第四章

### Lect26

開始要來添加module features了，這裡的module指的不是像lib那種，而是一個一個功能的模組區塊，例如：Login登入,Home首頁。

一開始是添加一個java kotlin的空module，然後再添加android module，現在有問題是如果保留原先的gradle會導致編譯不過，所以我都刪除，因為到時候應該是一起使用buildSrc的才對。

### Lect27

導入Room,Hilt之類的其他Lib，還有plugins例如:kapt。

### Lect28

buildSrc需要添加

gradlePluginPortal()

implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")

Login Module還是需要添加自己的build.gradle但是只是部分引入的改成buildSrc的，應該之後再調整。

```
plugins {
    `kotlin-dsl`
}

repositories{
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies{
    api(kotlin("gradle-plugin:1.9.0"))
    implementation("com.android.tools.build:gradle:8.1.4")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
}
```

### gradlePluginPortal()

`gradlePluginPortal()` 是一個 Maven 倉庫，它包含了 Gradle 插件的官方倉庫。添加這個倉庫後，你的構建腳本就可以訪問和解析從 Gradle 插件門戶網站發布的插件。

在你的腳本中添加這個倉庫的原因：

1. 確保可以訪問標準的 Gradle 插件
2. 許多 Kotlin 相關的插件和依賴項都存放在這個倉庫中
3. 當你需要使用 `kotlin-gradle-plugin` 或其他 Gradle 插件時，這個倉庫是必需的

### implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")

這行代碼添加了 Kotlin Gradle 插件作為依賴項。為什麼需要這個：

1. **插件依賴性**：你的構建腳本使用了 `kotlin-dsl` 插件，這需要 Kotlin Gradle 插件的支持。
2. **版本一致性**：注意到你已經在 API 依賴中有 `api(kotlin("gradle-plugin:1.9.0"))`，添加實現依賴確保了版本一致性，並提供了對插件完整功能的訪問。
3. **構建自定義插件**：當你在編寫自己的 Gradle 插件時，特別是如果這些插件需要處理 Kotlin 項目，就需要 Kotlin Gradle 插件的支持。
4. **完整功能訪問**：通過 `implementation` 而不僅僅是 `api` 來依賴這個插件，你可以在自己的插件中使用 Kotlin Gradle 插件的所有功能。

### Lect29

連添加依賴都使用kotlin去做一個整合有點稍微多於偏累，但是可以知道為什麼想樣這樣做，可以有效方便組合這個Lib的相關使用，缺點是如果只有一個就會顯得多餘。

自己去擴展DependencyHandler，缺點是如果不是kts就無法這樣玩。

```kotlin
fun DependencyHandler.implementation(dependency: String) {
    add("implementation", dependency)
}

fun DependencyHandler.retrofit() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitConverterGson)
    implementation(Dependencies.retrofitKotlinCoroutinesAdapter)
}
```

### Lect30

實際去引入上面整合的，但是遇到kapt問題，最後是把JDK調整到11，AS setting gradle的JDK也調到17才解決，真的是黑人問號。

```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlinOptions {
    jvmTarget = "11"
}
```

### Lect31

在Login Module測試一下Lib有沒有引入成功而已。

# 第五章

### Lect32

Module的Gradle有想到應該會去接原先buildSrc寫的，但是沒想到目前連Gradle設置都打算用kts處理。

沒想到可以自己去創立類似泛用的Gradle架構

```kotlin
class SharedLibraryGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.addConfigurations()
        project.addAndroidConfigurations()
        project.addKotlinOptions()
    }

    private fun Project.addConfigurations() {
        plugins.apply(BuildPlugins.KOTLIN_ANDROID)
        plugins.apply(BuildPlugins.KAPT)
    }

    private fun Project.addAndroidConfigurations() {
				extensions.getByType(LibraryExtension::class.java).apply{
            compileSdk = BuildConfig.COMPILE_SDK_VERSION
            defaultConfig.apply {
                minSdk = BuildConfig.MIN_SDK_VERSION
                testInstrumentationRunner=TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
            }
            flavorDimensions.add(BuildDimensions.APP)
            flavorDimensions.add(BuildDimensions.STORE)

            productFlavors {
                BuildFlavor.Google.createLibrary(this)
                BuildFlavor.Huawei.createLibrary(this)
                BuildFlavor.Driver.createLibrary(this)
                BuildFlavor.Client.createLibrary(this)
            }

            buildFeatures {
                compose = true
                buildConfig = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }
    }

    private fun Project.addKotlinOptions() {
        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}
```

### Lect33

Kapt引入調整

```kotlin
 const val KAPT = "org.jetbrains.kotlin.kapt"
 id(plugs.BuildPlugins.KAPT)
 等價於
 kotlin("kapt")
```

最後接入方法，相當簡化用apply滿直觀的就是接在後面繼續寫的概念

```kotlin
apply<SharedLibraryGradlePlugin>()
```

### Lect34

測試能不能RUN跟Flavor有沒有。

### Lect35

SharedLibraryPlugin補上簽名區塊，新增BuildCreator的createLibrary，設定Library打包相關條件，一陣子沒看就有點忘記了。

沒想到連Library模組也要分Flavor，到時候打包整合的時候不知道會是怎麼樣的。

```kotlin
signingConfigs {
                BuildSigning.Debug(project).create(this)
                BuildSigning.Release(project).create(this)
                BuildSigning.ReleaseExternalQA(project).create(this)
            }

            buildTypes {
                BuildCreator.Debug(project).createLibrary(this).apply {
                    signingConfig = signingConfigs.getByName(SigningTypes.DEBUG)
                }
                BuildCreator.Release(project).createLibrary(this).apply {
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                    signingConfig = signingConfigs.getByName(SigningTypes.RELEASE)
                }
                BuildCreator.ReleaseExternalQA(project).createLibrary(this).apply {
                    signingConfig = signingConfigs.getByName(SigningTypes.RELEASE_EXTERNAL_QA)
                }
            }
```

### Lect36

手動添加KTLINT，這個裡面的名字是自己取的，對應自己創建的gradle

這個之後可以多研究，看看要不要添加到自己的專案裡面

```kotlin
const val KTLINT = "ktlint-settings"
id(plugs.BuildPlugins.KTLINT)
```

```kotlin
import com.android.build.gradle.internal.tasks.factory.dependsOn

// 建立一個名為 ktlint 的自定義配置（Configuration）
// 用於管理 ktlint 相關的依賴項
val ktlint: Configuration by configurations.creating

dependencies {
    // 添加 Pinterest 的 ktlint 工具作為依賴
    // 版本為 0.49.0
    ktlint("com.pinterest:ktlint:0.49.0") {
        attributes {
            // 設定 bundling 屬性為 EXTERNAL
            // 表示這是一個外部的依賴，不會被打包到最終的 APK 中
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

// 註冊一個名為 "ktlintFormat" 的 JavaExec 任務
// ktlintFormat 會檢查並格式化 Kotlin 程式碼風格，嘗試自動修復一些違規問題
tasks.register<JavaExec>("ktlintFormat") {
    // 將此任務歸類到驗證群組（VERIFICATION_GROUP）
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    
    // 設定任務的描述
    description = "Check Kotlin code style and format"
    
    // 設定執行時的 classpath，使用前面定義的 ktlint 配置
    classpath = ktlint
    
    // 指定要執行的主類別
    mainClass.set("com.pinterest.ktlint.Main")
    
    // 添加 JVM 參數以解決模組系統的存取限制
    // 這是因為較新版本的 Java 對反射存取有更嚴格的限制
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    
    // 設定 ktlint 的命令列參數
    // 詳細用法可參考：https://pinterest.github.io/ktlint/install/cli/#command-line-usage
    args(
        "-F",                    // --format: 啟用自動格式化模式
        "**/src/**/*.kt",        // 檢查所有 src 目錄下的 .kt 檔案
        "**.kts",               // 檢查所有 .kts 檔案（Kotlin 腳本檔案）
        "!**/build/**",         // 排除 build 目錄下的檔案
    )
}

// 設定任務依賴關係
tasks {
    // 讓 "preBuild" 任務依賴於 "ktlintFormat" 任務
    // 這意味著在執行 preBuild 之前，會先執行 ktlintFormat
    // 確保在建置專案前，程式碼風格已經被檢查和格式化
    named("preBuild").dependsOn("ktlintFormat")
}
```

### Lect37

自己添加了一個.editorconfig的檔案去設定ktlint，他會再編譯的時候去做，例如:自動排版,自動檢查格式，像是預設import使用到*，就會被擋住，錯誤訊息會看到kotlinFormat Failed，但是不會直接跳到那一行有點麻煩，如果要使用的話可能還是要去了解基礎的設定，然後透過AI去編寫。

```kotlin
[*.{kt,kts}]
ktlint_function_signature_body_expression_wrapping=always
# disabled_rules=no-wildcard-imports
ktlint_standard= enabled

# Disable all rules from the `standard` rule set provided by KtLint
# ktlint_standard_final-newline = enabled # Enables the `final-newline` rule in the `standard` rule set provided by KtLint
# ktlint_experimental = enabled # Enable rules marked as experimental for all rule sets that are enabled
# ktlint_standard_some-experimental-rule = disabled # Disables the (experimental) `some-experimental-rule` in the `standard` rule set provided by KtLint
# ktlint_custom-rule-set = enabled # Enable all rules in the `custom-rule-set` rule set (not provided by KtLint)
# ktlint_custom-rule-set_custom-rule = disabled # Disables the `custom-rule` rule in the `custom-rule-set` rule set (not provided by KtLint)
```

### Lect38

添加了spotless gradle跟上面依樣，不過這個是負責處理java和kotlin以外的。這個也可以研究一下。

### Lect39

導入spotless plugin和實現。

```kotlin
implementation("com.diffplug.spotless:spotless-plugin-gradle:6.22.0")

import com.diffplug.gradle.spotless.SpotlessPlugin

apply<SpotlessPlugin>()

@Suppress("INACCESSIBLE_TYPE")
configure<com.diffplug.gradle.spotless.SpotlessExtension> {

    format("xml") {
        target("**/*.xml")
        prettier(mapOf("prettier" to "2.7.1", "@prettier/plugin-xml" to "2.2.0"))
            .config(
                mapOf(
                    "parser" to "xml",
                    "tabWidth" to 4,
                    "printWidth" to 80,
                    "useTabs" to false,
                    "semi" to true,
                    "singleQuote" to false,
                    "attributeSortOrder" to arrayOf("name", "id", "type"),
                    "selfClosingTags" to arrayOf("br", "img")
                )
            )
        indentWithSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlin {
        target(
            fileTree(
                mapOf(
                    "dir" to ".",
                    "include" to listOf("**/*.kt"),
                    "exclude" to listOf("**/build/**", "**/buildSrc/**", "**/.*")
                )
            )
        )
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
        ktlint("0.49.0")
            .userData(mapOf("android" to "true", "max_line_length" to "120"))
            .editorConfigOverride(mapOf("indent_size" to 2))
    }

    java {
        target(
            fileTree(
                mapOf(
                    "dir" to ".",
                    "include" to listOf("**/*.java"),
                    "exclude" to listOf("**/build/**", "**/buildSrc/**", "**/.*")
                )
            )
        )

        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
        // eclipse()
        // The eclipse() method in the spotless plugin is used to apply the Eclipse Code Formatter to Java files.
        // While it's originally associated with Eclipse IDE, it doesn't mean that it only works with Eclipse projects.
        // The Eclipse formatter configuration can be used by other IDEs, including Android Studio.
        googleJavaFormat()
        //method is used as the formatter for Java files.
        // googleJavaFormat is a formatter that follows the Google Java Style Guide.
    }

    kotlinGradle {
        target(
            fileTree(
                mapOf(
                    "dir" to ".",
                    "include" to listOf("**/*.gradle.kts", "*.gradle.kts"),
                    "exclude" to listOf("**/build/**")
                )
            )
        )
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
        ktlint("0.49.0")
            .userData(mapOf("android" to "true"))
            .editorConfigOverride(mapOf("indent_size" to 2))
    }
}

tasks.named("preBuild") {
    dependsOn("spotlessCheck")
}
tasks.named("preBuild") {
    dependsOn("spotlessApply")
}
```

### Lect40

這次導入detekt靜態分析工具，之前好像有看過這個，其實跟Lint很像。

### Lect41

修復Lect40導入detekt編譯不過問題，直接把FunctionMaxLength，FunctionNaming關閉，好像沒辦法去判斷是不是@Compose方法下去做，因為Compose方法開頭會是大寫，跟FunctionNaming有衝突。

最後在inputs.file(file("${rootProject.projectDir}/detekt/detekt-baseline.xml"))導入這邊沒看懂baseline。

### Lect42

講了一下baseline沒有重點，baseline就是會出現有問題的地方。

### Lect43

透過project.name去區分產生對應的檔案報告，如果有錯誤第一次編譯會不通過，第二次就會成功，因為baseline會添加錯誤的地方，類似於忽略清單。

### Lect44

添加了一個可以提示那些Lib要升級的plugin，因為我們改用kts抽出來寫，所以沒辦法像是以前那樣IDE提示顏色，這個Gradle可以輸出HTML還不錯。

### Lect45

添加了忽略掉非穩定版本的判斷，可以自己呼叫task去檢查版本更新。

### Lect46

這次導入Dokka，這個是用來產出文件用的，之前有看過但是沒有特別研究，高機率是我們的專案也跑不起來。

### Lect47

啟動Dokka看看輸出怎麼樣

### Lect48

設定Dokka根據不同feature輸出不同的doc

# 第六章

### Lect49

新增了一個core module，只是一個空殼子，之後又添加了data,domain.presentation module，導入功能module都是java&Kotlin的，跟Now Android越來越像了。

### Lect50

添加新的module的detekt baseline設置和引入module provider而已

### Lect51

正式引入hilt，只是為什麼SharedPlugin這裡沒有導入hilt的plugin?因為不是每個都有用到hilt，但是有用到的不用引入?

另外引入了JavaPoet應該是之後測試要用

# 第七章

### Lect52

在data module添加http intercept為何?然後app層導入core module裡面的內容?不是應該是login module去導入這些data module比較合理嗎?然後app層有導入Login module再去呼叫不是比較合理嗎?

不過在data module gradle去導入okhttp,retrofit的時候直接呼叫方法就好,真的是滿方便的。

### Lect53

實作了header intercept的操作，連hilt都還沒用到。

### Lect54

寫了一下hilt好久沒寫都快忘了,不知道為什麼NetworkModule是放在data裡面？

```jsx
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providerHeaderIntercept():Interceptor{
        return HeaderIntercept()
    }
}
```

### Lect55

新增了OkhttpLoggingIntercept一樣是透過hilt去注入，只是不知道為什麼編譯的時候出現kotlin版本問題,需要透過升級hilt才能解決。

`logging.redactHeader()` 方法的作用是將指定的 HTTP 標頭（header）標記為敏感資訊，這樣在記錄日誌時，這些標頭的值會被遮蔽或隱藏，而不會以明文形式出現在日誌檔案中。

### Lect56

創立了一個OkhttpClient的provider,用到三個比較沒看到過的方法

```jsx
OkHttpClient.Builder()
   .addInterceptor(interceptor)         // 添加攔截器，用於日誌記錄、認證或修改請求
   .retryOnConnectionFailure(true)      // 連線失敗時自動重試
   .followRedirects(false)              // 不自動跟隨 HTTP 重定向(3xx)，需手動處理
   .followSslRedirects(false)           // 不允許從 HTTPS 重定向到 HTTP，提高安全性
   .readTimeout(60, TimeUnit.SECONDS)   // 讀取伺服器回應資料的超時時間：60秒
   .writeTimeout(60, TimeUnit.SECONDS)  // 寫入請求資料到伺服器的超時時間：60秒
   .connectTimeout(60, TimeUnit.SECONDS)// 建立 TCP 連線的超時時間：60秒
   .build()                             // 建構並返回 OkHttpClient 實例
```

### Lect57

果用介面是用上了Named去區分,主要是完成了這個方法

```jsx
@Provides
    @Singleton
    @Named("HeaderIntercept")
    fun providerHeaderIntercept(
        @Named("clientId") clientId: String,
        @Named("accessToken") accessToken: () -> String?,
        @Named("language") language: () -> Locale
    ): Interceptor {
        return HeaderIntercept(clientId, accessToken, language)
    }
```

### Lect58

新增了一個介面,目前看不出用意,還有添加了PIN_CERTIFICATE應該是之後添加證書相關的

```jsx
interface OkhttpClientProviderInterface {

    fun getOkHttpClient(pin:String):OkHttpClient.Builder

    fun cancelAllRequest()
}
```

### Lect59

添加provider介面是因為debug跟release用的okhttpClient裡面添加的SSL不一樣,就跟我的FloatingGA是同一個道理。

這個debug和release的資料夾是放在core的data裡面,是直接選擇debug和release,沒有選擇其他的flavor的維度

```jsx
class OkHttpClientProvider : OkhttpClientProviderInterface{
    private var dispatcher = Dispatcher()

    override fun getOkHttpClient(pin: String): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String,
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String,
                ) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.dispatcher(dispatcher)
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun cancelAllRequest() {
        dispatcher.cancelAll()
    }
}
```

### Lect60

在release的去實現另外的OkhttpClient，去添加對應的設定，不像我們的產品Host是動態的，像她們這種固定的通常應該會添加一些證書之類的。

憑證釘選 (Certificate Pinning):建立憑證釘選器，將特定的憑證指紋綁定到 `*.yourdomain.com` 域名，這是一種安全機制，防止中間人攻擊。

```jsx
class OkHttpClientProvider : OkhttpClientProviderInterface{
    private var dispatcher = Dispatcher()

    override fun getOkHttpClient(pin: String): OkHttpClient.Builder {
        val certificatePinner = CertificatePinner.Builder().add("*.yourdomain.com", pin).build()

        val builder = OkHttpClient.Builder()
        builder.dispatcher(dispatcher)
        builder.certificatePinner(certificatePinner)
        return builder
    }

    override fun cancelAllRequest() {
        dispatcher.cancelAll()
    }
}
```

### Lect61

最後OkhttpClient的注入改透過Provider的介面去拿取，Library要用BuildConfig一樣去要需告有夠麻煩。

```kotlin
   @Provides
    @Singleton
    @Named("OkhttpCallFactory")
    fun providerOkhttpCallFactory(
        @Named("OkhttpLoggerIntercept") OkhttpLoggerIntercept: HttpLoggingInterceptor,
        @Named("HeaderIntercept") headerIntercept:HeaderIntercept,
        okhttpClientProvider: OkhttpClientProviderInterface
    ): Call.Factory {
        return okhttpClientProvider.getOkHttpClient(BuildConfig.PIN_CERTIFICATE)
            .addInterceptor(OkhttpLoggerIntercept)
            .addInterceptor(headerIntercept)
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }
```

### Lect62

把Hilt用到的Named抽成常數，然後分個資料夾而已。

### Lect63

把intercept相關的獨立抽成一個hilt module而已

### Lect64

新增了Retrofit的hilt

```kotlin
@Provides
@Singleton
fun providerRetrofit(okhttpClient:OkHttpClient): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl("")
            .client(okhttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())

        return builder .build()
}
```

### Lect65

添加了一個Retrofit的ServiceFactory，應該是之後要注入用的，畢竟沒辦法直接去修改Retrofit的create方法，所以應該是自己擴展一個出來。

# 第八章

### Lect66

開始實作LoginModule，這裡的資料夾也是跟core一樣,new出對應的data,domain,presentation,不知道是不是都這樣？可能要去看一下Now Android看看。

另外噴出一個Target Api 31通知問題，好像是hilt compose引入問題，最後額外引入workKtx才解決，莫名其妙。

### Lect67

實作了一個LoginService，但是POST這邊這樣傳入url，這樣retrofit設置BaseUrl要幹嘛？

```jsx
const val BASE_URL = "https://mydomain.com"
const val EMAIL = "email"

interface LoginService {

    @POST("${BASE_URL}/Auth/login")
    fun login(
        @Body body: LoginRequestBody
    ): Deferred<Response<LoginResponse>>

    @POST("${BASE_URL}/Auth/ForgetPassword")
    fun forgetPassword(
        @Query(EMAIL) email: String
    ): Deferred<Response<Unit>>
}
```

### Lect68

在Login Module也創立了一個NetworkModule作di,要拿取ServiceFactory所以引入了core:data module,目前到這裡就開始有點感覺了,模組要怎麼切？如何引入？要切多細？

```jsx
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideLoginServiceFactory(serviceFactory: ServiceFactory): LoginService {
        return serviceFactory.createService(LoginService::class.java)
    }
}
```

# 第九章

### Lect69

在data module實作了一個檢查網路狀態的功能,這邊會需要AndroidMinifest.xml去註冊網路權限,這邊就可以使用註解去強制讓使用者知道。

```jsx
/**
 * [NetworkMonitorImp] 實作了 [NetworkMonitorInterface]，用於監控網路連線狀態。
 *
 * @param context Android 的 Context 物件，用於取得系統服務。
 */
class NetworkMonitorImp(context: Context) : NetworkMonitorInterface {

  /**
   * 取得系統的 ConnectivityManager，用於查詢網路連線資訊。
   */
  private val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  /**
   * 檢查目前是否有網路連線。
   *
   * 需要 [Manifest.permission.ACCESS_NETWORK_STATE] 權限。
   *
   * @return 如果有任何一種網路連線 (Wi-Fi, 行動網路, 乙太網路)，則返回 true，否則返回 false。
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  override fun hasConnectivity(): Boolean {
    // 取得目前活動網路的 NetworkCapabilities。
    return connectivityManager.getNetworkCapabilities(
      connectivityManager.activeNetwork,
    )?.let { networkCapabilities ->
      // 檢查 NetworkCapabilities 是否包含任何一種我們關心的傳輸類型。
      listOf(
        NetworkCapabilities.TRANSPORT_WIFI,
        NetworkCapabilities.TRANSPORT_CELLULAR,
        NetworkCapabilities.TRANSPORT_ETHERNET,
      ).any {
        networkCapabilities.hasTransport(it)
      }
    } ?: false // 如果沒有活動網路，則視為沒有連線。
  }
}
```

### Lect70

在data module新增了一個NetworkDataSource，我猜是Response的封裝層。

目前看起來data module反而不是我想的那樣,是專門放一些bean類之類的

```jsx
class NetworkDataSource<SERVICE>(
    val service: SERVICE,
    val gson: Gson,
    val networkMonitor: NetworkMonitorInterface,
    val userIdProvider: () -> String
) {

}
```

### Lect71

又在data module新增了ErrorResponse和ErrorMessage,開始有點看不懂,這個感覺跟UI有點像,萬一後端給你的不同,這樣擴充性可能要額外考量

```jsx
data class ErrorResponse(
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMessage")
    val errorMessage: String,
    @SerializedName("errorFieldList")
    val errorFieldList: List<String>
)

data class ErrorMessage(val code: Int, val message: String)
```

### Lect72

在data module果然設計了一個通用的response封裝,只是這次他叫做UseCase滿有趣的

```jsx
sealed class OutCome<T> {
    abstract fun isSuccess(): Boolean

    open val errorMessage: ErrorMessage? = null

    abstract suspend fun accept(useCase: UseCase<T>)

    class Success<T>(val data: T) : OutCome<T>() {
        override fun isSuccess(): Boolean = true
        override suspend fun accept(useCase: UseCase<T>) {
            useCase.onSuccess(this)
        }
    }
}

interface UseCase<R> {
  suspend fun onSuccess(success: OutCome.Success<R>)
  suspend fun onEmpty()
  suspend fun onError(errorMessage: ErrorMessage)
}
```

### Lect73

完成了OutCome補上Error跟Empty，理論上應該可以再補個Loading或是其他的,順便幫他優化了一下,不用T的直接用Nothing處理。

其實我們的專案也可以用,只是要自己去擴展Response Adapter,目前看起來應該是沒辦法

```jsx
sealed class OutCome<T> {
    abstract fun isSuccess(): Boolean

    open val errorMessage: ErrorMessage? = null

    abstract suspend fun accept(useCase: UseCase<T>)

    class Success<T>(val data: T) : OutCome<T>() {
        override fun isSuccess(): Boolean = true
        override suspend fun accept(useCase: UseCase<T>) {
            useCase.onSuccess(this)
        }
    }

    class Error(private val _errorMessage: ErrorMessage) : OutCome<Nothing>() {

        override val errorMessage: ErrorMessage = _errorMessage

        override fun isSuccess(): Boolean = false

        override suspend fun accept(useCase: UseCase<Nothing>) {
            useCase.onError(_errorMessage)
        }
    }

    class Empty : OutCome<Nothing>() {
        override fun isSuccess(): Boolean = true
        override suspend fun accept(useCase: UseCase<Nothing>) {
            useCase.onEmpty()
        }
    }

    companion object {
        fun <T> success(data: T) = Success<T>(data)
        fun error(errorMessage: ErrorMessage) = Error(errorMessage)
        fun empty() = Empty()
    }
}
```

### Lect74

額外寫了一個extensions擴展OutCome方法,do系列比較好懂,類似於鏈式調用需要的在呼叫,不需要使用when每個都寫,後面的map跟merge就比較花了,merge有點沒看懂？居然不是整合到Flow去使用？

map跟rx一樣就是轉換資料型別

merge跟rx一樣是合併其他api然後回傳一個

只是不懂為什麼這邊不是由Flow處理？

```jsx
// Executes the given onSuccess action if the OutCome is successful and the coroutine is active.
suspend fun <T> OutCome<T>.doOnSuccess(onSuccess: suspend (T) -> Unit): OutCome<T> {
    if (this is OutCome.Success<T>) {
        if (coroutineContext.isActive) {
            onSuccess(this.data)
        }
    }
    return this
}

// Executes the given onEmpty action if the OutCome is empty and the coroutine is active.
suspend fun <T> OutCome<T>.doOnEmpty(onEmpty: suspend () -> Unit): OutCome<T> {
    if (this is OutCome.Empty) {
        if (coroutineContext.isActive) {
            onEmpty()
        }
    }
    return this
}

// Executes the given onError action if the OutCome is not successful.
suspend fun <T> OutCome<T>.doOnError(onError: () -> Unit): OutCome<T> {
    if (!this.isSuccess() && coroutineContext.isActive) {
        onError()
    }
    return this
}

// Transforms the successful OutCome into another type using the provided mapper function.
suspend fun <T, R> OutCome<T>.map(mapper: suspend (T) -> R): OutCome<R> {
    return when (this) {
        is OutCome.Success<T> -> {
            OutCome.success(mapper(this.data))
        }

        is OutCome.Error<T> -> {
            OutCome.error(this.errorMessage)
        }

        is OutCome.Empty<T> -> {
            OutCome.empty()
        }
    }
}

// Merges the current OutCome with another OutCome produced by a lazy function, using a merger function.
suspend fun <F, S, R> OutCome<F>.merge(
    lazy: suspend () -> OutCome<S>,
    merger: (F?, S?) -> R
): OutCome<R> {
    return when (this) {
        is OutCome.Success<F> -> {
            when (val second = lazy()) {
                is OutCome.Success<S> -> {
                    OutCome.success(merger(this.data, second.data))
                }

                is OutCome.Empty<S> -> {
                    OutCome.success(merger(this.data, null))
                }

                is OutCome.Error<S> -> {
                    OutCome.error(second.errorMessage)
                }
            }
        }

        is OutCome.Error<F> -> {
            OutCome.error(this.errorMessage)
        }

        is OutCome.Empty<F> -> {
            when (val second = lazy()) {
                is OutCome.Success<S> -> {
                    OutCome.success(merger(null, second.data))
                }

                is OutCome.Empty<S> -> {
                    OutCome.empty()
                }

                is OutCome.Error<S> -> {
                    OutCome.error(second.errorMessage)
                }
            }
        }
    }
}
```

### Lect75

自定義了網路Api錯誤回傳Code,只是不知道為什麼是放在Datasource，感覺怪怪的

```jsx
interface DataSource {
    companion object{

        const val SUCCESS = 200
        const val SEE_OTHERS = 300
        const val CREATED = 201
        const val BAD_REQUEST = 400
        const val UNAUTHORISED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val CONFLICT = 409

        const val UNKNOWN = -1
        const val NO_INTERNET = -2
        const val TIMEOUT = -3
        const val SSL_PINNING = -4
    }
}
```

### Lect76

NetworkDataSource封裝了一層API Request預處理,這一段花操作我都看暈了,看來就是okhttp intercept不靠譜,還是要靠自己封裝一層比較合理,做在call Adapter可能也不好,連重新定向都有寫,可惜我們專案太小。

```jsx
class NetworkDataSource<SERVICE>(
    val service: SERVICE,
    val gson: Gson,
    val networkMonitor: NetworkMonitorInterface,
    val userIdProvider: () -> String,
) {
    suspend fun <R, T> performRequest(
        request: suspend (SERVICE).(String) -> Response<R>,
        onSuccess: suspend (R, Headers) -> OutCome<T> = { _, _ -> OutCome.empty<T>() },
        onRedirect: suspend (String, Int) -> OutCome<T> = { _, _ -> OutCome.empty<T>() },
        onEmpty: suspend ()-> OutCome<T> = { OutCome.empty<T>() },
        onError: suspend (ErrorResponse,Int) -> OutCome<T> = { errorResponse,code -> OutCome.error(errorResponse.toDomain()) },
    ): OutCome<T> {

    }
}
```

### Lect77

開始實作performRequest回傳部分，先把錯誤的部分完成了

```kotlin
return if (networkMonitor.hasConnectivity()) {
    try {

    } catch (e: Exception) {
        val code = when (e) {
            is SocketTimeoutException -> DataSource.TIMEOUT
            is UnknownHostException -> DataSource.UNKNOWN
            is SSLProtocolException, SSLHandshakeException -> DataSource.SSL_PINNING
            else -> DataSource.UNKNOWN
        }
        onError(getDefaultErrorResponse(), code)
    }
} else {
    onError(getDefaultErrorResponse(), DataSource.NO_INTERNET)
}
```

### Lect78,Lect79

終於完成了，沒看懂的是都判斷isSuccess了還要補coroutine的isActive判斷有點怪,在OutCome裡面不就有做了嗎？body判斷是Unit也是滿怪的第一次看到,然後觸發重新定向這邊可以看後面怎麼接,感覺有點太多if else了有點醜,比較沒想到的是SERVICE泛型接一個.(String)後轉換成Response<R>這個操作我還真沒想到過。

```kotlin
class NetworkDataSource<SERVICE>(
    val service: SERVICE,
    val gson: Gson,
    val networkMonitor: NetworkMonitorInterface,
    val userIdProvider: () -> String,
) {

    suspend fun <R, T> performRequest(
        request: suspend (SERVICE).(String) -> Response<R>,
        onSuccess: suspend (R, Headers) -> OutCome<T> = { _, _ -> OutCome.empty<T>() },
        onRedirect: suspend (String, Int) -> OutCome<T> = { _, _ -> OutCome.empty<T>() },
        onEmpty: suspend () -> OutCome<T> = { OutCome.empty<T>() },
        onError: suspend (ErrorResponse, Int) -> OutCome<T> = { errorResponse, code ->
            OutCome.error(
                errorResponse.toDomain(code)
            )
        },
    ): OutCome<T> {
        return if (networkMonitor.hasConnectivity()) {
            try {
                val response = service.request(userIdProvider())
                val responseCode = response.code()
                val errorBodyString = response.errorBody()?.string()
                if (response.isSuccessful || responseCode==DataSource.SEE_OTHERS){
                    val body = response.body()
                    if(body!=null && body != Unit){
                        if(coroutineContext.isActive){
                            onSuccess(body, response.headers())
                        }else{
                            onEmpty()
                        }
                    }else{
                        val location = response.headers()[HEADER_LOCATION]
                        if(location!=null){
                            onRedirect(location, responseCode)
                        }else{
                            onEmpty()
                        }
                    }
                }else if(errorBodyString.isNullOrEmpty()){
                    onError(getDefaultErrorResponse(), responseCode)
                }else{
                    onError(getErrorResponse(gson,errorBodyString), responseCode)
                }
            } catch (e: Exception) {
                val code = when (e) {
                    is SocketTimeoutException -> DataSource.TIMEOUT
                    is UnknownHostException -> DataSource.UNKNOWN
                    is SSLProtocolException, is SSLHandshakeException -> DataSource.SSL_PINNING
                    else -> DataSource.UNKNOWN
                }
                onError(getDefaultErrorResponse(), code)
            }
        } else {
            onError(getDefaultErrorResponse(), DataSource.NO_INTERNET)
        }
    }
}
```

# 第十章

### Lect80

開始實作Login Module層了，新增了LoginRemote介面處理Api,之後應該會有Room的處理,只是有UserResponse了還new了一個新的User,有點不知道為什麼。

### Lect81

處理LoginRemote的DI相關,沒想到連Config Provider這種都可以用DI去處理,繞了有點多層,context居然是去app層提供？也是滿奇怪的,我記得之前直接給context的註解好像就可以了。

Config Module有點類似於想要在Hilt使用常數,或是靜態方法的概念。

```kotlin
@Provides
  @Singleton
  fun provideNetworkDataSource(
    loginService: LoginService,
    gson: Gson,
    networkMonitorInterface: NetworkMonitorInterface,
    @Named(USER_ID_TAG) userIdProvider: () -> String,
  ): NetworkDataSource<LoginService> {
    return NetworkDataSource(
      loginService,
      gson,
      networkMonitorInterface,
      userIdProvider,
    )
  }

  @Provides
  @Singleton
  fun provideLoginRemoteImplementer(networkDataSource: NetworkDataSource<LoginService>): LoginRemote {
    return LoginRemoteImp(networkDataSource)
  }
  
  @Module
@InstallIn(SingletonComponent::class)
class AppModule {

  @Provides
  @Singleton
  fun providerContext(@ApplicationContext context: Context): Context {
    return context
  }
}

@Module
@InstallIn(SingletonComponent::class)
class ConfigModule {

  @Provides
  @Singleton
  @Named(USER_ID_TAG)
  fun provideUserId(): () -> String? {
    return { "" } // todo get user id from user prefs later
  }

  @Provides
  @Singleton
  @Named(LANGUAGE_TAG)
  fun provideLanguage(): () -> Locale {
    return { Locale.ENGLISH } // todo get locale from user prefs later, move me to config module
  }

  @Provides
  @Singleton
  @Named(ACCESS_TOKEN_TAG)
  fun provideAccessToken(): () -> String? {
    return { "" } // todo get access token from user prefs later, move me to config module
  }

  @Provides
  @Singleton
  @Named(CLIENT_ID_TAG)
  fun provideClientId(): String {
    return "" // todo get client id from user prefs later, move me to config module
  }
}

```

### Lect82

實作LoginRomoteImp,真的是串了太多層說真的不看影片直接去接我還真的不會,爾且onSuccess的Header還是很奇怪,onError感覺也是重複做事。

```kotlin
class LoginRemoteImp(private val networkDataService: NetworkDataSource<LoginService>) : LoginRemote {
  override fun login(loginRequestBody: LoginRequestBody): OutCome<UserResponse> {
    return networkDataService.performRequest(
        request = { login(loginRequestBody).await()},
        onSuccess = { response, headers -> OutCome.success(response, headers)},
        onError = { errorResponse, code -> OutCome.error(errorResponse.toDomain(code))}
    )
  }
}

```

### Lect83

沒有想到資料轉換用到的Mapper層，所以變成說在Domain層還要多一個Mapper，應該是因為資料可能是從API or DB來，所以這個Mapper才有意義，這裡還傳入了CoroutineDispatcher他說是要設置成CPU線程真的滿細的，因為這個轉換通常不耗時間，就算放在UI線層可能也沒感覺。

今天剛好看到一張架構圖，剛好是說現代模組化區分的模組區塊，跟之前想的不一樣

persentation→domain→data

viewModel→UserCase→Repository→DataSource→Mapper→Api or DB

```kotlin
class LoginMapperImp(private val defaultCoroutineDispatcher: CoroutineDispatcher) :
    LoginMapperInterface {
    override suspend fun toDomain(userResponse: UserResponse): User {
        return withContext(defaultCoroutineDispatcher) {
            User(
                id = userResponse.id.orEmpty(),
                fullName = userResponse.fullName.orEmpty(),
                email = userResponse.email.orEmpty(),
                photo = userResponse.photo.orEmpty(),
            )
        }
    }
}
```

### Lect84

Cortouine Dispaters也寫了一個DI Module提供給其他如注入。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {

    @Provides
    @Singleton
    @Named(DISPATCHER_MAIN_TAG)
    fun provideDispatcherMain(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @Singleton
    @Named(DISPATCHER_DEFAULT_TAG)
    fun provideDispatcherDefault(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @Singleton
    @Named(DISPATCHER_IO_TAG)
    fun provideDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
```

### Lect85

結果她也遇到上面編譯的PendingIntent問題，結果依樣是引入WorkKtx處理，笑死。

# 第十一章

### Lect86

引入DataStore和kotlinSerilaizations和kotlinCollections

這個函式庫 `kotlinx-collections-immutable` 是 JetBrains 開發的 Kotlin 不可變集合函式庫，主要功能包括：

### 主要用途

提供高效能的不可變（immutable）資料結構，包括 List、Set、Map 等集合類型。

### 核心特色

**持久化資料結構**：當你修改集合時，不會改變原本的集合，而是建立一個新的集合實例，但底層會共享大部分資料以提高效能。

**記憶體效率**：透過結構共享（structural sharing）技術，避免不必要的資料複製。

**執行緒安全**：由於集合是不可變的，天然具備執行緒安全特性。

### 主要集合型別

- `PersistentList` - 不可變列表
- `PersistentSet` - 不可變集合
- `PersistentMap` - 不可變映射

### 使用場景

- 函數式程式設計
- 需要執行緒安全的資料結構
- 狀態管理（如 Redux pattern）
- 需要頻繁建立集合副本的場景

### Lect87

新增了DataStore Module，引入DataStore依賴，但是不知道為什麼data module也引入了DataStore的依賴?名字都有點像引入的時候真的會錯亂。

### Lect88

在DataStore Module裡面新增了AppSettings，目前還不清楚用途跟範圍。

```kotlin
@Serializable
data class AppSettings(
    val language: Language = Language.ENGLISH,
    val lastKnownLocation: PersistentList<Location> = persistentListOf(),
) {
}

@Serializable
data class Location(val lat:Double,val long:Double)

enum class Language {
    ENGLISH,
    ARABIC,
    SPANISH,
}
```

### Lect89

添加了一個Serializer這個是DataStoue的不是kotlin的，沒怎麼用過DataStore不是很熟，不過他是要直接寫入Json的樣子。

```kotlin
class AppSettingsSerializer:Serializer<AppSettings> {
    override val defaultValue: AppSettings
        get() = AppSettings()

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            Json.decodeFromString(
                deserializer = AppSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        }catch (e: SerializationException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppSettings.serializer(),
                value = t
            ).toByteArray()
        )
    }
}
```

### Lect90

引入了const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlin.plugin.serialization”

AppSettings.serializer()方法就自動有了，有夠方便

不像以前那些還要自己實作一些方法

### Lect91

在Main測試AppSetting DataStore能不能正常接上。

### Lect92

開始畫UI測試AppSetting DataStore了，DataStore沒再用真的陌生。

```kotlin
@Composable
fun SettingScreen(appSettingsDataStore: DataStore<AppSettings>, modifier: Modifier) {
    val scope = rememberCoroutineScope()
    val appSetting by appSettingsDataStore.data.collectAsState(initial = AppSettings())

    Column(modifier = modifier) {
        Text(text = "language : ${appSetting.language}")
        Text(text = "last location")
        appSetting.lastKnownLocation.forEach {
            Text("${it.lat} ${it.long}")
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        val newLocation = Location(37.123, 122.908)
        Language.values().forEach { language ->
            DropdownMenuItem(text = { Text(language.name) }, onClick = {
                scope.launch {
                    appSettingsDataStore.updateData { currentSetting ->
                        currentSetting.copy(
                            language = language,
                            lastKnownLocation = currentSetting.lastKnownLocation.add(newLocation),
                        )
                    }
                }
            })
        }
    }
}

```

### Lect93

客製化了一個Serializer，因為PersistentList是第三方的他不知道如何解析，應該是預設類型沒有。

這是一個 **Kotlinx Serialization 的自定義序列化器**，專門處理 `PersistentList<Location>` 型別的序列化和反序列化。

**為什麼需要這個序列化器？**

`PersistentList` 是第三方函式庫的自定義集合型別，Kotlinx Serialization 框架預設不知道如何處理它，所以需要告訴框架：

- 如何將 `PersistentList<Location>` 轉換成 JSON
- 如何將 JSON 轉換回 `PersistentList<Location>`

```kotlin
class PersistentListLocationSerializer : KSerializer<PersistentList<Location>> {
    private val delegateSerializer = ListSerializer(Location.serializer())

    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor(
        "PersistentList",
        SerialKind.CONTEXTUAL, // indicates that this is a contextual (custom) serializer
        listSerialDescriptor(Location.serializer().descriptor),
    )

    override fun deserialize(decoder: Decoder): PersistentList<Location> {
        return delegateSerializer.deserialize(decoder).toPersistentList()
    }

    override fun serialize(encoder: Encoder, value: PersistentList<Location>) {
        delegateSerializer.serialize(encoder, value.toList())
    }
}

```

# 第十二章

### Lect94

這一張介紹**Preferences DataStore和Proto DataStore的差異。**

如果是從**SharedPreferences**遷移那使用**Preferences DataStore就可以了，如果是需要類型轉換就適合用Proto DataStore**

**Preferences DataStore**

**特點：**

- 基於鍵值對（key-value）的存儲方式
- 使用起來類似於 SharedPreferences，但更現代化
- 支援基本數據類型：String、Int、Boolean、Float、Long、Set<String>
- API 簡單直觀，學習成本低

**優點：**

- 遷移成本低，容易從 SharedPreferences 轉換
- 不需要定義額外的數據結構
- 適合存儲簡單的設置和偏好

**缺點：**

- 類型安全性較弱
- 無法進行複雜的數據驗證
- 不支援複雜的嵌套數據結構

**Proto DataStore**

**特點：**

- 基於 Protocol Buffers（protobuf）的存儲方式
- 需要預先定義 .proto 文件來描述數據結構
- 支援複雜的數據類型和嵌套結構
- 提供強類型安全保證

**優點：**

- 類型安全性強，編譯時檢查
- 支援複雜的數據結構和嵌套對象
- 數據驗證和向後兼容性更好
- 性能更優，序列化效率高
- 支援數據遷移和版本控制

**缺點：**

- 學習成本較高，需要了解 Protocol Buffers
- 需要額外定義 .proto 文件
- 設置相對複雜

### Lect95

新建了一個protodataModule，沒想到這個也要一個Module，想說他應該跟data Module放在一起就不好了嗎?

引入了proto data需要用的Library。

### Lect96

添加了proto dataStore需要用到的plugin設置

```kotlin
  id(plugs.BuildPlugins.GOOGLE_PROTOBUF)
  
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
```

### Lect97

添加proto的schema，有點麻煩，原本以為會有plugin可以產生之類的。

```kotlin
syntax ="proto3";

option java_package = "com.shang.protodatastore";
option java_multiple_files = true;

message Session{
  string accessToken = 1;
  string refreshToken = 2;
  string userId = 3;
}

message Preferences{
  string language = 1;
  bool isAppLockEnabled = 2;
  int32 notificationCount = 3;
  int64 moneyBalance = 4;
}
```

### Lect98

還是需要自己去編寫一個SessionSerializer，有趣的是上面的data.proto寫完之後去編譯就會自動產生Session.java的檔案，裡面有一些預設的方法可以使用了。

不過整體還說還是很麻煩，如果是像ApiConfig這種大型的不知道能不能?雖然應該不會這樣做才對。

```kotlin
object SessionSerializer: Serializer<Session> {
    override val defaultValue: Session
        get() = Session.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Session {
        return withContext(Dispatchers.IO){
            try {
                Session.parseFrom(input)
            }catch (e: InvalidProtocolBufferException){
                e.printStackTrace()
                defaultValue
            }
        }
    }

    override suspend fun writeTo(t: Session, output: OutputStream) {
        withContext(Dispatchers.IO) {
            t.writeTo(output)
        }
    }
}

```

### Lect99

新增了另外一個PreferencesSerializer。

### Lect100

新增了一個Factory File來放extension的方法，之後拿DataStore就相對簡化了，他的檔案名稱是.pb結尾，**Preferences DataStore**則是用json當作檔名**。**

```kotlin
val Context.sessionDataStore : DataStore<Session> by dataStore(
    fileName = "session.pb",
    serializer = SessionSerializer
)

val Context.preferencesDataStore : DataStore<Preferences> by dataStore(
    fileName = "preferences.pb",
    serializer = PreferencesSerializer
)
```

### Lect101

原來新增Manager資料夾是為了處理getter和setter，真的是有夠麻煩，應該是因為Session是透過plugin產出的原因

```kotlin

class PreferencesDataStoreImp(private val preferencesDataStore: DataStore<Preferences>) :
    PreferencesDataStoreInterface {
    override suspend fun setLanguage(language: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setIsAppLockEnabled(isAppLockEnabled: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setNotificationCount(notificationCount: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun setMoneyBalance(moneyBalance: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getLanguage(): String {
        TODO("Not yet implemented")
    }

    override fun getLanguageFlow(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun isAppLockEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAppLockEnabledFlow(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotificationCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getNotificationCountFlow(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getMoneyBalance(): Int {
        TODO("Not yet implemented")
    }

    override fun getMoneyBalanceFlow(): Flow<Int> {
        TODO("Not yet implemented")
    }
} 
```

### Lect102

完成了PreferencesDataStoreImp這樣子封裝起來之後要用就簡單多了，還可以透過Hilt注入。

```kotlin
class PreferencesDataStoreImp(private val preferencesDataStore: DataStore<Preferences>) :
    PreferencesDataStoreInterface {
    override suspend fun setLanguage(language: String) {
        preferencesDataStore.updateData {
            it.toBuilder().setLanguage(language).build()
        }
    }

    override suspend fun setIsAppLockEnabled(isAppLockEnabled: Boolean) {
        preferencesDataStore.updateData {
            it.toBuilder().setIsAppLockEnabled(isAppLockEnabled).build()
        }
    }

    override suspend fun setNotificationCount(notificationCount: Int) {
        preferencesDataStore.updateData {
            it.toBuilder().setNotificationCount(notificationCount).build()
        }
    }

    override suspend fun setMoneyBalance(moneyBalance: Long) {
        preferencesDataStore.updateData {
            it.toBuilder().setMoneyBalance(moneyBalance).build()
        }
    }

    override suspend fun getLanguage(): String {
        return preferencesDataStore.data.first().language
    }

    override fun getLanguageFlow(): Flow<String> {
        return preferencesDataStore.data.map {
            it.language
        }
    }

    override suspend fun isAppLockEnabled(): Boolean {
        return preferencesDataStore.data.first().isAppLockEnabled
    }

    override fun isAppLockEnabledFlow(): Flow<Boolean> {
        return preferencesDataStore.data.map {
            it.isAppLockEnabled
        }
    }

    override suspend fun getNotificationCount(): Int {
        return preferencesDataStore.data.first().notificationCount
    }

    override fun getNotificationCountFlow(): Flow<Int> {
        return preferencesDataStore.data.map {
            it.notificationCount
        }
    }

    override suspend fun getMoneyBalance(): Long {
        return preferencesDataStore.data.first().moneyBalance
    }

    override fun getMoneyBalanceFlow(): Flow<Long> {
        return preferencesDataStore.data.map {
            it.moneyBalance
        }
    }
}

```

### Lect103,Lect104

樣添加了SessionDataStoreImp這裡多一個可以直接拿到Session的方法，他這幾張也太水了吧，合併成一張不就好了

```kotlin
class SessionDataStoreImp(private val sessionDataStore: DataStore<Session>) :
    SessionDataStoreInterface {
    override suspend fun setAccessToken(accessToken: String) {
        sessionDataStore.updateData { currentSessionData ->
            currentSessionData.toBuilder().setAccessToken(accessToken).build()
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        sessionDataStore.updateData { currentSessionData ->
            currentSessionData.toBuilder().setRefreshToken(refreshToken).build()
        }
    }

    override suspend fun setUserIdToken(userId: String) {
        sessionDataStore.updateData { currentSessionData ->
            currentSessionData.toBuilder().setUserId(userId).build()
        }
    }

    override suspend fun setSession(accessToken: String, refreshToken: String, userId: String) {
        sessionDataStore.updateData { currentSessionData ->
            currentSessionData.toBuilder()
                .setUserId(userId)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken).build()
        }
    }

    override suspend fun getAccessToken(): String {
        return sessionDataStore.data.first().accessToken
    }

    override fun getAccessTokenFlow(): Flow<String> {
        return sessionDataStore.data.map { session ->
            session.accessToken
        }
    }

    override suspend fun getRefreshToken(): String {
        return sessionDataStore.data.first().refreshToken
    }

    override fun getRefreshTokenFlow(): Flow<String> {
        return sessionDataStore.data.map { session ->
            session.refreshToken
        }
    }

    override suspend fun getUserId(): String {
        return sessionDataStore.data.first().userId
    }

    override fun getUserIdFlow(): Flow<String> {
        return sessionDataStore.data.map { session ->
            session.userId
        }
    }

    /*
getSession(): Session: This function returns a single instance of Session synchronously using the first() operator.
 It waits for the first emitted value from the sessionDataStore.data flow and returns it immediately.
 This is useful when you want to retrieve the current session data and use it immediately in your code.
    */
    override suspend fun getSession(): Session {
        return sessionDataStore.data.first()
    }

    /*
 getSessions(): Flow<Session>: This function returns a flow of Session instances.
  It doesn't retrieve the session data immediately but instead provides a stream of session data over time.
  This is useful when you want to observe changes to the session data continuously.
  You can collect or observe this flow in your code and react to any changes to the session data as they occur.
 */
    override fun getSessionFlow(): Flow<Session> {
        return sessionDataStore.data.map { session ->
            session
        }
    }

}
```

### Lect105

實作了DataStore Manager的DI Module，透過context去擴展真的不錯，是我就直接傳context進去new了。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Session> {
        return context.sessionDataStore
    }

    @Provides
    @Singleton
    fun provideSessionDataManager(sessionDataStore: DataStore<Session>): SessionDataStoreInterface {
        return SessionDataStoreImp(sessionDataStore)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.preferencesDataStore
    }

    @Provides
    @Singleton
    fun providePreferencesDataManager(preferencesDataStore: DataStore<Preferences>): PreferencesDataStoreInterface {
        return PreferencesDataStoreImp(preferencesDataStore)
    }
}
```

### Lect106

測試上面有的有沒有問題

```kotlin
@Inject
lateinit var sessionDataStoreInterface: SessionDataStoreInterface

@Inject
lateinit var preferencesDataStoreInterface: PreferencesDataStoreInterface

val accessTokenFlow by sessionDataStoreInterface.getAccessTokenFlow().collectAsState("")
    val accessTokenValue = remember { mutableStateOf("") }

LaunchedEffect(Unit) {
    scope.launch {
        accessTokenValue.value = sessionDataStoreInterface.getAccessToken()
    }
}
    
Spacer(modifier = Modifier.height(16.dp))
        Text(text = "accessTokenFlow : $accessTokenFlow")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "accessTokenValue : ${accessTokenValue.value}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                val newAccessToken = "newAccessToken : ${System.currentTimeMillis()}"
                sessionDataStoreInterface.setAccessToken(newAccessToken)
                accessTokenValue.value = (newAccessToken)
            }
        }) {
            Text("insert")
        }    
```

### Lect107

完成Data Module裡面的ConfigModule，裡面的參數果然是透過上面的去堤共，只是變成Data Module要去引入ProtoDataModule有點奇怪。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class ConfigModule {

    @Provides
    @Singleton
    @Named(USER_ID_TAG)
    fun provideUserId(sessionDataStoreInterface: SessionDataStoreInterface): () -> String? {
        val userId = runBlocking {
            sessionDataStoreInterface.getUserId()
        }
        return { userId }
    }

    @Provides
    @Singleton
    @Named(LANGUAGE_TAG)
    fun provideLanguage(preferencesDataStoreInterface: PreferencesDataStoreInterface): () -> Locale {
        val language = runBlocking {
            preferencesDataStoreInterface.getLanguage()
        }
        return {
            if (language.isNotEmpty()) {
                Locale(language)
            } else {
                Locale.ENGLISH
            }
        }
    }

    @Provides
    @Singleton
    @Named(ACCESS_TOKEN_TAG)
    fun provideAccessToken(sessionDataStoreInterface: SessionDataStoreInterface): () -> String? {
        val accessToken = runBlocking {
            sessionDataStoreInterface.getAccessToken()
        }
        return { accessToken }
    }

    @Provides
    @Singleton
    @Named(CLIENT_ID_TAG)
    fun provideClientId(): String {
        return UUID.randomUUID().toString()
    }
}
```

# 第十三章

### Lect108

介紹一下Chucker這個攔截API的專案，還以為是要安裝他的APP，看來是要用導入的，像是金絲雀依樣同時安裝另一個衍生APP去看。

### Lect109

新增了ChuckerInterceptror，和引入了他的Lib在安裝時可以安裝衍生APP。

```kotlin
@Provides
    @Singleton
    @Named(CHUCKER_INTERCEPTOR_TAG)
    fun provideChuckerInterceptor(@ApplicationContext context: Context): Interceptor {
        return ChuckerInterceptor.Builder(context)
            // The previously created Collector
            .collector(
                ChuckerCollector(
                    context = context,
                    showNotification = true,
                    retentionPeriod = RetentionManager.Period.ONE_HOUR,
                ),
            )
            // The max body content length in bytes, after this responses will be truncated.
            .maxContentLength(250_000L)
            // List of headers to replace with ** in the Chucker UI
            .redactHeaders(AUTHORIZATION_HEADER)
            // Read the whole response body even when the client does not consume the response completely.
            // This is useful in case of parsing errors or when the response body
            // is closed before being read like in Retrofit with Void and Unit types.
            .alwaysReadResponseBody(true)
            // Use decoder when processing request and response bodies. When multiple decoders are installed they
            // are applied in an order they were added.
            // Controls Android shortcut creation.
            .createShortcut(true)
            .build()
    }
```

### Lect110

新增了一個SessionService目前還不知道要幹嘛，可能是要自動刷新token吧，像我們的APP比較特別，他會被登出，一般來說被登出應該就兩種，第一種是類似通知接收到登出訊息，另一種就是token變化。

```kotlin
const val BASE_URL="https://example.com/"
const val REFRESH_TOKEN = "refreshToken"
interface SessionService {

    @GET("${BASE_URL}/Auth/GetSession")
    fun getToken(
        @Header(REFRESH_TOKEN) refreshToken: String,
    ):Deferred<Response<TokenResponse>>

    @GET("${BASE_URL}/Auth/DeleteSession")
    fun logout(
        @Header(REFRESH_TOKEN) refreshToken: String,
    ):Deferred<Response<Unit>>

}
```

### Lect111

添加了一個驗證token的攔截器，一般來說判斷token有沒有合格應該要像他這樣寫比較合理，只不過萬一Api回來是掛掉的可能也不會觸發到這裡，runBlocking也可以添加coroutineDispatcher不知道有什麼差異?指定了coroutineDispatcher是代表裡面的操作在什麼線程下處理，但是runBlocking還是會阻塞AuthenticationIntercept的線程直到value回傳。

```kotlin
class AuthenticationIntercept @Inject constructor(
    private val sessionDataInterface: SessionDataStoreInterface,
    private val sessionService: SessionService,
    private val coroutineDispatcher: CoroutineDispatcher
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = runBlocking(coroutineDispatcher){
            sessionDataInterface.getAccessToken()
        }
        val authenticationRequest = request.newBuilder()
            .header(AUTHORIZATION_HEADER,"Bearer $accessToken")
            .build()
        val response = chain.proceed(authenticationRequest)
        if (response.code!=DataSource.UNAUTHORISED){
            return response
        }
        return response
    }
}
```

### Lect112

完成了AuthenticationIntercept，如果他不是回給401他也不會做，所以這個後端還要配合好，只是不知道為什麼getUpdatedToken這個方法是做在這裡，這裡比較特別的是還用到mutex防止同時多隻API 401觸發，在Intercept處理suspend這個我就比較沒有經驗了，主要還是要看每個系統的token驗證是如何。

```kotlin
class AuthenticationIntercept @Inject constructor(
    private val sessionDataInterface: SessionDataStoreInterface,
    private val sessionService: SessionService,
    private val coroutineDispatcher: CoroutineDispatcher,
) :
    Interceptor {

    private val mutex = Mutex()
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = runBlocking(coroutineDispatcher) {
            sessionDataInterface.getAccessToken()
        }
        val authenticationRequest = request.newBuilder()
            .header(AUTHORIZATION_HEADER, "Bearer $accessToken")
            .build()
        val response = chain.proceed(authenticationRequest)
        if (response.code != DataSource.UNAUTHORISED) {
            return response
        }

        val tokenResponse = runBlocking {
            mutex.withLock {
                val tokenResponse = getUpdatedToken().await()
                tokenResponse.body().also {
                    sessionDataInterface.setAccessToken(it?.accessToken ?: "")
                    sessionDataInterface.setRefreshToken(it?.refreshToken ?: "")
                }
            }
        }

        return if (tokenResponse?.accessToken != null) {
            response.close()

            // retry the original request with the new token
            val authenticatedRequest =
                request.newBuilder()
                    .header(AUTHORIZATION_HEADER, "Bearer ${tokenResponse.accessToken}").build()

            val response = chain.proceed(authenticatedRequest)

            response
        } else {
            response
        }
    }

    private suspend fun getUpdatedToken(): Deferred<retrofit2.Response<TokenResponse>> {
        val refreshToken = sessionDataInterface.getRefreshToken()
        return withContext(coroutineDispatcher) {
            sessionService.getToken(refreshToken)
        }
    }
}
```

### Lect113

添加AuthenticationIntercept的注入DI。

### Lect114

HeaderIntercept移除原先添加accessToken的方法，因為在AuthenticationIntercept有添加了，Bearer抽成常數。

### Lect115

新增了ConnectivityInterceptor處理網路問題，那幹嘛不一開始就弄。

只是他這裡沒有添加@Inject我幫他加了，有沒有添加都可以編譯的過，看來我對Hilt還不夠熟

```kotlin
class ConnectivityInterceptor @Inject constructor(private val networkMonitorInterface: NetworkMonitorInterface) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (networkMonitorInterface.hasConnectivity()) {
            return chain.proceed(chain.request())
        } else {
            throw NoConnectivityException
        }
    }
}

object NoConnectivityException : IOException()
```

# 第十四章

### Lect116

在Data Module裡面新增了Mapper，然後把之前ErrorResponse.toDomain移動到這裡，雖然知道有Mapper層是合理的，但是為什麼要把他從ErrorHandler移動過來?然後在Data Module裡面引入了Domain Module這樣是合理的嗎?理論上這個三最基礎的層不是應該不會互相引入嗎?他把ErrorMessage移動到了domain層。

### 1. Domain (領域層)

- **地位與職責：** 這是你應用程式的**核心**，負責定義所有的**業務邏輯、實體 (Entities) 和用例 (Use Cases)**。它包含了應用程式「是什麼」以及「如何運作」的抽象概念。
- **依賴方向：** **不依賴任何其他層。** 領域層是獨立且純粹的，不應包含任何 Android 框架、資料庫或網路相關的程式碼。這使其成為應用程式中最穩定、最容易測試的部分。
- **總結：** 領域層提供業務規則和抽象介面，供其他層實現或調用。

### 2. Data (資料層)

- **地位與職責：** 負責處理應用程式的**資料來源**，包括從網路 API 獲取資料、與本地資料庫交互、檔案讀寫等。它將外部資料轉換為領域層定義的實體，並將領域層的實體持久化。
- **依賴方向：** **單向引入 Domain 層。** 資料層需要引入領域層，以便實現領域層定義的**抽象儲存庫介面 (Repository Interfaces)**，並將從外部獲取的 DTO (Data Transfer Objects) 轉換為領域層的實體。
- **總結：** 資料層是領域層資料操作抽象的具體實現者，確保資料的獲取和持久化符合業務模型。

### 3. Presentation (展示層)

- **地位與職責：** 負責應用程式的**使用者介面 (UI)** 和使用者交互邏輯，例如 Activity、Fragment、ViewModel 等。它負責將領域層處理後的資料展示給使用者，並接收使用者的輸入。
- **依賴方向：** **單向引入 Domain 層。** 展示層需要引入領域層，以調用領域層定義的**用例 (Use Cases)** 來執行特定的業務操作，並獲取處理後的結果來更新 UI。
- **總結：** 展示層是業務邏輯的消費者和使用者介面的呈現者，將領域層的數據和功能可視化。

### Lect117

他把原先的data module的result和usecase移動到domain層，害我現在有點錯亂，然後又新增了一個AsyncUseCase的抽象層目前看不懂實際觸發如何?

```kotlin
abstract class AsyncUseCase<I, R> : UseCase<R> {

    private lateinit var success: suspend (R) -> Unit
    private lateinit var error: suspend (ErrorMessage) -> Unit
    private lateinit var empty: suspend () -> Unit

    suspend fun execute(
        input: I,
        onSuccess: suspend (R) -> Unit = {},
        onError: suspend (ErrorMessage) -> Unit = {},
        onEmpty: suspend () -> Unit = {}
    ) {
        this.success = onSuccess
        this.error = onError
        this.empty = onEmpty
        run(input).accept(this)
    }

    abstract fun run(input: I): OutCome<R>

    override suspend fun onSuccess(success: OutCome.Success<R>) {
        success(success.data)
    }

    override suspend fun onError(errorMessage: ErrorMessage) {
        error(errorMessage)
    }

    override suspend fun onEmpty() {
        empty()
    }
}
```

### Lect118

實作了一個LoginUseCase體會一下現在的架構，在裡面多了一個Input的類型，變成說LoginRemote那邊只能傳入String這邊感覺是可以整合的，現在Login變成Input→LoginRequestBody，多了一層input轉換。

```kotlin
class LoginUseCase(private val loginRemote: LoginRemote) :
    AsyncUseCase<LoginUseCase.Input, User>() {

    data class Input(val username: String, val password: String)

    override suspend fun run(input: Input): OutCome<User> {
        return loginRemote.login(input.username, input.password)
    }
}
```

# 第十五章

### Lect119

在Login Module裡面new了一個presentation層，新增Actvitiy跟VewModel，終於到了最後一層了。

### Lect120

在Login Module導入Compose要用的Library。

### Lect121

Login Module畫了一個LoginScreen的UI，好一陣子沒畫還真不習慣。

### Lect122

測試LoginScreen有沒有正常顯示。

### Lect123

添加LoginError不是很重要，把APP層的預設啟動關閉，自動會找到Login的。

### Lect124

添加了一個protocol kt，他的命名方法比較接近功能的樣子，像是我可能就會叫做Event之類的。

```kotlin
sealed class LoginInput {
    data class UserNameUpdated(val username: String) : LoginInput()
    data class PasswordUpdated(val password: String) : LoginInput()
    data object LoginButtonClicked : LoginInput()
    data object RegisterButtonClicked : LoginInput()
}

sealed class LoginOutput {
    data object NavigateToMain : LoginOutput()
    data object NavigateToRegister : LoginOutput()
    data class ShowError(val errorMessage: ErrorMessage) : LoginOutput()
}

```

### Lect125

新增了一個LoginViewState有一點接近MVI但是這個用boolean決定要不顯示UI的方法比較適合Compose。

```kotlin
data class LoginViewState(
    val userName: String = "",
    val password: String = "",
    val isLoginButtonEnabled: Boolean = false,
    val userNameError: LoginError = LoginError.NoEntry,
    val passwordError: LoginError = LoginError.NoEntry,
) {
    fun showPasswordError() =
        passwordError != LoginError.NoError && passwordError != LoginError.NoEntry

    fun showUsernameError() =
        userNameError != LoginError.NoError && userNameError != LoginError.NoEntry
}
```

### Lect126

寫了一個LoginValidator，通常我會寫在Repository裡面，只是他用Validator也沒考慮用個介面?爾且他的方法名稱取得有點怪。

```kotlin

const val USERNAME_LENGTH = 5
const val PASSWORD_MAX_LENGTH = 11
const val PASSWORD_MIN_LENGTH = 6

object LoginValidator {

    fun userNameError(username: String): LoginError {
        return when {
            username.isEmpty() -> LoginError.NoEntry
            !isValidUserNameLength(username) -> LoginError.InCorrectUsernameLength
            !username.isAlphanumeric() -> LoginError.InCorrectUserName
            else -> LoginError.NoError
        }
    }

    fun passwordError(password: String): LoginError {
        return when {
            password.isEmpty() -> LoginError.NoEntry
            !isValidPasswordLength(password) -> LoginError.InCorrectPasswordLength
            !password.isAlphaNumericWithSpecialCharacters() -> LoginError.InCorrectPassword
            else -> LoginError.NoError
        }
    }

    private fun String.isAlphaNumericWithSpecialCharacters(): Boolean {
        val containsLowerCase = any { it.isLowerCase() }
        val containsUpperCase = any { it.isUpperCase() }
        val containsSpecialCharacters = any { !it.isLetterOrDigit() }
        val containsDigits = any { it.isDigit() }
        return containsDigits && containsLowerCase && containsUpperCase && containsSpecialCharacters
    }

    private fun isValidPasswordLength(password: String) =
        password.count() in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH

    private fun isValidUserNameLength(username: String) =
        username.count() > USERNAME_LENGTH

    private fun String.isAlphanumeric() = matches(Regex("^[a-zA-Z0-9]*$"))
}
```

### Lect127

開始實作LoginViewModel，為什麼VewState不是選用StateFlow?而viewOutput是選用channel?有點沒看懂這個操作。AI說用Channel是為了處理一次性事件，不會因為畫面旋轉就重新觸發，所以其實用SharedFlow也是可以。

```kotlin
class LoginViewModel : ViewModel() {
    private val _loginViewState = LoginViewState()

    private val _viewOutput : Channel<LoginOutput> = Channel()
    val viewOutput = _viewOutput.receiveAsFlow()

    fun loginInput(loginInput: LoginInput){
        when(loginInput){
            LoginInput.LoginButtonClicked -> login()
            is LoginInput.PasswordUpdated -> TODO()
            LoginInput.RegisterButtonClicked -> sendOutput { LoginOutput.NavigateToRegister }
            is LoginInput.UserNameUpdated -> TODO()
        }
    }

    private fun sendOutput(action:()->LoginOutput){
        viewModelScope.launch {
            _viewOutput.send(action())
        }
    }

    fun login() {

    }
}
```

### Lect128

繼續完成LoginViewModel ，他這個刷新狀態的方法也太麻煩了吧，不過我覺得LoginViewState這個做法還不錯，用data class去定義他的狀態，之前可能會用enum或是sealed會顯得比較複雜，由ViewModel來持有這個UI狀態，只是我覺得他的這些判斷操作應該可以簡化。

```kotlin
class LoginViewModel : ViewModel() {
    private var _loginViewState = LoginViewState()

    private val _viewOutput : Channel<LoginOutput> = Channel()
    val viewOutput = _viewOutput.receiveAsFlow()

    fun loginInput(loginInput: LoginInput){
        when(loginInput){
            LoginInput.LoginButtonClicked -> login()
            is LoginInput.PasswordUpdated -> updateState { copy(password = loginInput.password) }
            LoginInput.RegisterButtonClicked -> sendOutput { LoginOutput.NavigateToRegister }
            is LoginInput.UserNameUpdated -> updateState { copy(userName = loginInput.username) }
        }
    }

    private fun updateState(updateState: LoginViewState.() -> LoginViewState){
        _loginViewState = _loginViewState.updateState()
        validate()
    }

    private fun validate() {
        val userNameError: LoginError = LoginValidator.userNameError(_loginViewState.userName)
        val passwordError: LoginError = LoginValidator.passwordError(_loginViewState.password)
        val isLoginButtonEnabled: Boolean = LoginValidator.canDoLogin(userNameError, passwordError)

        _loginViewState = _loginViewState.copy(
            isLoginButtonEnabled = isLoginButtonEnabled,
            userNameError = userNameError,
            passwordError = passwordError
        )
    }

    private fun sendOutput(action:()->LoginOutput){
        viewModelScope.launch {
            _viewOutput.send(action())
        }
    }

    fun login() {

    }
}
```

### Lect129

ViewModel接上Screen，但是感覺他畫UI功力怪怪的。

### Lect130

loginViewModel也能當LaunchedEffect狀態?這道沒想過，現在LaunchedEffect做collect不用掛起了嗎?

```kotlin
    LaunchedEffect(loginViewModel) {
        loginViewModel.viewOutput.collect{
            when(it){
                LoginOutput.NavigateToMain -> TODO()
                LoginOutput.NavigateToRegister -> TODO()
                is LoginOutput.ShowError -> TODO()
            }
        }
    }
```

### Lect131

LoginViewModel注入UseCase使用

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() 

fun login() {
        viewModelScope.launch {
            loginUseCase.execute(
                input = LoginUseCase.Input(
                    username = _loginViewState.userName,
                    password = _loginViewState.password
                ),
                onSuccess = {

                },
                onError = {
                    
                }
            )
        }
    }
```

### Lect132

添加了GsonConverterFactory，準備Mock Api測試，越來越水。

### Lect133

介紹了[https://app.mockfly.dev/](https://app.mockfly.dev/)這個mock api網頁，還不錯ㄟ，可以自訂回傳狀態，以後寫測試可以用這個。

### Lect134

測試了一下API有沒有回應，結果這邊遇到DI問題，原來是他在某個intercept的sessionService刪除了，但是我沒刪除到導致編譯不過，害我找了老半天只好問AI，這就是DI的小缺點很難找到對應錯誤的地點。

# 第十六章

### Lect135

Demo了一下上面寫的登入測試而已,跳出錯誤POP窗。

### Lect136

Demo了一下上面寫的登入測試而已,跳出Loading窗。

### Lect137

Demo了一下上面寫的登入測試而已,跳出Empty窗。

### Lect138

core的presentation層導入androidx和domainModule，所以真的跟架構圖一樣,是presentation依賴domain,domain依賴data。

### Lect139

core的presentation層新增了一個看起來像是狀態渲染的UI封裝，看來是共用的UI會放在core的presentation裡面。

`sealed class StateRenderer<out S, O> {}`

### Lect140

寫了這個通用Base UI，看了我都懷疑自己會不會寫程式了，除非設計上是相當通用，不然如果來一堆奇怪UI不知道這個架構會怎麼樣，會這樣子寫高機率也是因為Compose上可以配合，這個如果用XML應該會有點混亂。

```kotlin
sealed class StateRenderer<out S, O> {

    class ScreenContent<S, O>(val viewState: S) : StateRenderer<S, O>()

    data class LoadingPopup<S, O>(val viewState: S, @StringRes val loadingMessage: Int = R.string.loading) : StateRenderer<S, O>()

    data class FullScreenLoading<S, O>(val viewState: S, @StringRes val loadingMessage: Int = R.string.loading) : StateRenderer<S, O>()

    data class ErrorPopup<S, O>(val viewState: S, val errorMessage: ErrorMessage) : StateRenderer<S, O>()

    data class FullScreenError<S, O>(val viewState: S, val errorMessage: ErrorMessage) : StateRenderer<S, O>()

    data class Empty<S, O>(val viewState: S, @StringRes val emptyMessage: Int = R.string.no_data) : StateRenderer<S, O>()

    data class Success<S, O>(val output: O) : StateRenderer<S, O>()
}

```

### Lect141

這個通用的StateRenderer就放在presentation層就滿合理的，只是這一串東西我目前是沒看到，現在看到傳lambda Unit進去還是很不適應。

```kotlin
sealed class StateRenderer<out S, O> {

    class ScreenContent<S, O>(val viewState: S) : StateRenderer<S, O>()

    data class LoadingPopup<S, O>(val viewState: S, @StringRes val loadingMessage: Int = R.string.loading) : StateRenderer<S, O>()

    data class LoadingFullScreen<S, O>(val viewState: S, @StringRes val loadingMessage: Int = R.string.loading) : StateRenderer<S, O>()

    data class ErrorPopup<S, O>(val viewState: S, val errorMessage: ErrorMessage) : StateRenderer<S, O>()

    data class ErrorFullScreen<S, O>(val viewState: S, val errorMessage: ErrorMessage) : StateRenderer<S, O>()

    data class Empty<S, O>(val viewState: S, @StringRes val emptyMessage: Int = R.string.no_data) : StateRenderer<S, O>()

    data class Success<S, O>(val output: O) : StateRenderer<S, O>()

    @Composable
    fun onUiState(action: @Composable (S) -> Unit): StateRenderer<S, O> {
        if (this is ScreenContent) {
            action(viewState)
        }
        return this
    }

    @Composable
    fun onLoadingState(action: @Composable (S) -> Unit): StateRenderer<S, O> {
        if (this is LoadingPopup) {
            action(viewState)
        } else if (this is LoadingFullScreen) {
            action(viewState)
        }
        return this
    }

    fun onSuccess(action: (O) -> Unit): StateRenderer<S, O> {
        if (this is Success) {
            action(output)
        }
        return this
    }

    @Composable
    fun onErrorState(action: @Composable (S) -> Unit): StateRenderer<S, O> {
        if (this is ErrorPopup) {
            action(viewState)
        } else if (this is ErrorFullScreen) {
            action(viewState)
        }
        return this
    }

    fun onEmpty(action: () -> Unit): StateRenderer<S, O> {
        if (this is Empty) {
            action()
        }
        return this
    }
}

```

### Lect142

新增了一個靜態方法，目前是有看沒有懂，感覺到時候還會傳lambda然後callback可以創立compose ui的感覺。

```kotlin
    companion object {
        @Composable
        fun <S, O> of(
            retryAction: () -> Unit = {},
            stateRenderer: StateRenderer<S, O>,
            block: @Composable StateRenderer<S, O>.() -> Unit
        ): StateRenderer<S, O> {
            stateRenderer.block() // show this first before doing any thing

            when (stateRenderer) {
                is Empty -> renderEmpty(stateRenderer.emptyMessage)
                is ErrorFullScreen -> renderErorrFullScreen(stateRenderer.errorMessage, retryAction)
                is ErrorPopup -> renderErorrPopup(stateRenderer.errorMessage, retryAction)
                is LoadingFullScreen -> renderLoadingFullScreen(stateRenderer.loadingMessage)
                is LoadingPopup -> renderLoadingPopup(stateRenderer.loadingMessage)
                else -> {}
            }
            return stateRenderer
        }
    }
```

### Lect143

新增Loading和Error的Compose UI。

### Lect144

新增Empty的Compose UI。

### Lect145

新增LoadingPopup,ErrorPopup的Compose UI。

### Lect146

做了login接上狀態，看起來有夠麻煩啦，這樣寫真的有比較好嗎。

```kotlin
    fun login() {
        viewModelScope.launch {
            val newStateRenderer = StateRenderer.LoadingPopup<LoginViewState,User>(loginViewState)
            _stateRenderStateFlow.value = newStateRenderer
            loginUseCase.execute(
                input = LoginUseCase.Input(
                    username = loginViewState.userName,
                    password = loginViewState.password,
                ),
                onSuccess = {
                    val newStateRenderer = StateRenderer.Success<LoginViewState,User>(it)
                    _stateRenderStateFlow.value = newStateRenderer
                },
                onError = {
                    val newStateRenderer = StateRenderer.ErrorPopup<LoginViewState,User>(loginViewState,it)
                    _stateRenderStateFlow.value = newStateRenderer
                },
            )
        }
    }
```

### Lect147

接上了StateRenderer.of這個方法，這裡的UiState是要刷新當前UI的畫面，而不是要顯示對應的pop窗，pop窗封裝在of裡面了，那看來是of這個方法需要新增可以傳入compose方法的才合理，他的VM裡面有LoginState又有這個StateRender，我認為有點太複雜了，不如透過一個Base Class去繼承擴長狀態加資料。

```kotlin

    StateRenderer.of(stateRenderer = statRenderer, retryAction = { loginViewModel.login() }) {
        onUiState { updatedState ->
            ScreeUiContent(updatedState, loginViewModel)
        }
        onLoadingState { updatedState ->
            ScreeUiContent(updatedState, loginViewModel)
        }
        onSuccessState {
            println(it.fullName)
        }
        onEmptyState {
        }
        onErrorState { updatedState ->
            ScreeUiContent(updatedState, loginViewModel)
        }
    }
```

### Lect148

在驗證的時候還要在new一個state給flow刷新也是怪，這邊主要是測試API回應之後實際UI的情況。

```kotlin
private fun validate() {
        val userNameError: LoginError = LoginValidator.userNameError(loginViewState.userName)
        val passwordError: LoginError = LoginValidator.passwordError(loginViewState.password)
        val isLoginButtonEnabled: Boolean = LoginValidator.canDoLogin(userNameError, passwordError)

        loginViewState = loginViewState.copy(
            isLoginButtonEnabled = isLoginButtonEnabled,
            userNameError = userNameError,
            passwordError = passwordError,
        )

        val stateRenderer = StateRenderer.ScreenContent<LoginViewState, User>(loginViewState)
        _stateRenderStateFlow.value = stateRenderer
    }
```

### Lect149

測試了一下checker差點都忘記還有這個東西了，要打開通知才會看到，他不是額外安裝一個APP出來，這個可以考慮引入到我們自己的APP。

# 第十七章

### **Lect150**

引入了navigation和google json相關的

### **Lect151**

新增了Natigator Module。

### **Lect152**

設定了導航通用的事件的樣子，目前看不出來使用情境，原先我想說他應該會放在presentation module裡面畢竟跟UI相關。

```kotlin
sealed class NavigatorEvent {
    data object NavigateUp : NavigatorEvent()

    data object NavigateBack : NavigatorEvent()

    class Directions(val direction: String, val builder: NavOptionsBuilder.() -> Unit) :
        NavigatorEvent()
    class Navigate(val route: String, val builder: NavOptionsBuilder.() -> Unit) :
        NavigatorEvent()
}

interface AppNavigator {

    fun navigateUp(): Boolean
    fun popBackStack()
    fun navigate(
        route: String,
        builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }
    ): Boolean

    val destinationsFlow: Flow<NavigatorEvent>
}
```

### **Lect153**

實做了AppNavigator這裡依樣是使用Channel做一個資料傳送，目前猜測這個應該會放在BaseActivity之類的去做。

```kotlin
class AppNavigatorImp : AppNavigator {
    private val _navigateEvents = Channel<NavigatorEvent>()

    override fun navigateUp(): Boolean {
        return _navigateEvents.trySend(NavigatorEvent.NavigateUp).isSuccess
    }

    override fun popBackStack() {
        _navigateEvents.trySend(NavigatorEvent.PopupStack)
    }

    override fun navigate(destination: String, builder: NavOptionsBuilder.() -> Unit): Boolean {
        return _navigateEvents.trySend(NavigatorEvent.Directions(destination, builder)).isSuccess
    }

    override val destinationsFlow: Flow<NavigatorEvent>
        get() = _navigateEvents.receiveAsFlow()
}
```

### **Lect154**

寫了一個DI NavigatorModule，結果是要注入到ViewModel裡面，沒看懂怎麼樣用。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class NavigatorModule {

    @Provides
    @Singleton
    fun provideNavigator(): AppNavigator {
        return AppNavigatorImp()
    }
}

@HiltViewModel
class AppNavigatorViewModel @Inject constructor(
    private val appNavigator: AppNavigator
) : ViewModel() , AppNavigator by appNavigator {

}
```

### **Lect155**

寫了一個NavigationDestination目前看起來還不錯，感覺以後可以參考沿用，上面說注入ViewModel是為了關注點方離為了測試。

```kotlin
interface NavigationDestination {
    fun destination(): String
    val arguments: List<NamedNavArgument> get() = emptyList()
    val deepLink: List<NavDeepLink> get() = emptyList()
}
```

### **Lect156**

新增了Screens，route url之所以要帶入參數是因為這樣才拿得到的樣子，不確定新版Compose Navigator有沒有變化，因為最近又出了一個Navigator3的版本。

```kotlin
sealed class Screens(val route: String) {

    data object LoginScreenRoute : Screens(LOGIN_ROUTE)
    data object SignUpScreenRoute : Screens(SIGNUP_ROUTE)
    data object HomeScreenRoute :
        Screens(route = "$HOME_ROUTE/${USER_PARAM}/${USER_FULL_NAME}/${USER_AGE}")
}

const val USER_PARAM = "user"
const val USER_AGE = "user_age"
const val USER_FULL_NAME = "user_full_name"
const val HOME_ROUTE = "HomeScreenRoute"

class HomeDestination : NavigationDestination {

    fun createHome(user: String, fullName: String, age: Int): String {
        return "${HOME_ROUTE}/${user}/${fullName}/${age}"
    }

    override fun destination(): String = HOME_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(USER_PARAM) {
                type = StringType
                defaultValue = "default_user"
            },
            navArgument(USER_AGE) {
                type = IntType
                defaultValue = 18
            },
            navArgument(USER_FULL_NAME) {
                type = StringType
                defaultValue = "Default User"
            }
        )

    override val deepLink: List<NavDeepLink> get() = listOf()
}
```

### **Lect157**

新增了Signup Module，新增了SignupScreen UI。

### **Lect158**

實作Home Module，然後把Login Module的User移動到了Domain Module去，有點難區分什麼該放哪裡。

### **Lect159**

畫了HomeScreen的UI，看來navigator的用法我之前是看到新版的樣子。

```kotlin

@Composable
fun HomeScreen(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val user = backStackEntry?.arguments?.getString(USER_PARAM)
    val fullName = backStackEntry?.arguments?.getString(USER_FULL_NAME)
    val age = backStackEntry?.arguments?.getInt(USER_AGE)
    val userObject = user?.toUser()

    Scaffold(
        content = { pad ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Email:${userObject?.email}")
                Spacer(modifier = Modifier.padding(16.dp))
                Text(text = "FullName:$fullName")
                Spacer(modifier = Modifier.padding(16.dp))
                Text(text = "Age:$age")
                Spacer(modifier = Modifier.padding(16.dp))
                Button(onClick = { }) {
                    Text(text = "Load Home Info")
                }
            }
        }
    )
}
```

### **Lect160**

調整了LoginScreen引入了navigator module，結果他在LoginScreen注入AppNavigator，那這樣之前寫的AppNavigator是為了什麼?他在UI這個部分感覺真的是怪怪的，不太靠普的感覺。

### **Lect161**

新增了一個RouteActivity，來設置NavHost用

### **Lect162**

實作NavHost裡面要跳轉的功能，為了這個還封裝一個方法感覺太多餘了

```kotlin
private val composableDestinations: Map<NavigationDestination, @Composable (AppNavigator, NavHostController) -> Unit> =
    mapOf(
        SignUpDestination() to { _, _ -> SignUpScreen() },
        HomeDestination to { _, navHostController -> HomeScreen(navHostController) },
        LoginDestination() to { appNavigator, _ -> LoginScreen(appNavigator = appNavigator) },
    )

fun NavGraphBuilder.addComposableDestinations(
    appNavigator: AppNavigator,
    navHostController: NavHostController,
) {
    composableDestinations.forEach { entry ->
        val destination = entry.key
        composable(
            route = destination.destination(),
            arguments = destination.arguments,
            deepLinks = destination.deepLink,
        ) {
            entry.value(appNavigator, navHostController)
        }
    }
}

```

### **Lect163**

接上navigator跳轉，那個參數定義好麻煩，不確定是不是因為deeplink才需要這樣，還需要特別先編碼過。

```kotlin
sealed class Screens(val route: String) {

    data object LoginScreenRoute : Screens(LOGIN_ROUTE)
    data object SignUpScreenRoute : Screens(SIGNUP_ROUTE)
    data object HomeScreenRoute :
        Screens(route = "$HOME_ROUTE/{$USER_PARAM}/{$USER_FULL_NAME}/{$USER_AGE}")
}

 val encodedUserJson = URLEncoder.encode(user.toJson(), StandardCharsets.UTF_8.toString())
            appNavigator.navigate(
                HomeDestination.createHome(
                    user = encodedUserJson,
                    age = 36,
                    fullName = user.fullName,
                ),
            )
```

### **Lect164**

最後創了一堆Module水時間馬的。

---

# 觀看心得

可以學到什麼

每個章節大約講什麼

適合觀看的人

缺點

個人心得
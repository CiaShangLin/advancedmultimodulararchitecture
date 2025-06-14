package deps

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project
import test.TestDependencies


fun DependencyHandler.room() {
    implementation(Dependencies.roomKtx)
    implementation(Dependencies.roomRuntime)
    kapt(Dependencies.roomCompiler)
    annotationProcessor(Dependencies.roomCompiler)
}


fun DependencyHandler.retrofit() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitConverterGson)
    implementation(Dependencies.retrofitKotlinCoroutinesAdapter)
}

fun DependencyHandler.okHttp() {
    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpLoggingInterceptor)
}

fun DependencyHandler.hilt() {
    implementation(Dependencies.hiltAndroid)
    implementation(Dependencies.hiltCompose)
    implementation(Dependencies.hiltNavigation)
    kapt(Dependencies.hiltCompiler)
    kapt(Dependencies.hiltAgp)
    kapt(Dependencies.hiltCompilerKapt)
}

fun DependencyHandler.androidx() {
    implementation(Dependencies.ANDROIDX_CORE)
    implementation(Dependencies.ANDROIDX_LIFECYCLE_RUNTIME_KTX)
    implementation(Dependencies.ANDROIDX_ACTIVITY_COMPOSE)
    implementation(Dependencies.ANDROIDX_UI)
    implementation(Dependencies.ANDROIDX_UI_GRAPHICS)
    implementation(Dependencies.ANDROIDX_UI_TOOLING_PREVIEW)
    implementation(Dependencies.ANDROIDX_MATERIAL3)
    implementation(Dependencies.workKtx)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.ANDROIDX_ACTIVITY)
    implementation(Dependencies.COMPOSE_MATERIAL)
    implementation(Dependencies.COMPOSE_COMPILER)
    implementation(Dependencies.COMPOSE_RUNTIME)
    implementation(Dependencies.navigation)
    implementation(Dependencies.navigationCompose)
    implementation(Dependencies.navigationFragmentKtx)
    implementation(Dependencies.googleJson)
}

fun DependencyHandler.dataStore() {
    implementation(Dependencies.datastore)
    implementation(Dependencies.kotlinCollections)
}

fun DependencyHandler.kotlinx() {
    implementation(Dependencies.kotlinSerilaizations)
}

fun DependencyHandler.protoDataStore() {
    implementation(Dependencies.datastore)
    implementation(Dependencies.protoBufJavaLite)
    implementation(Dependencies.protoBufKotlinLite)
}

fun DependencyHandler.chucker() {
    debugImplementation(Dependencies.chuckerDebug)
    releaseImplementation(Dependencies.chuckerRelease)
}


fun DependencyHandler.testDeps() {
    testImplementation(TestDependencies.ANDROIDX_JUNIT)
}

fun DependencyHandler.testImplDeps() {
    androidTestImplementation(TestDependencies.ANDROIDX_JUNIT)
    androidTestImplementation(TestDependencies.ANDROIDX_ESPRESSO_CORE)
    androidTestImplementation(TestDependencies.ANDROIDX_COMPOSE_UI_TEST)
}

fun DependencyHandler.testDebugDeps() {
    debugImplementation(Dependencies.ANDROIDX_UI_TOOLING_PREVIEW)
    debugImplementation(TestDependencies.ANDROIDX_COMPOSE_UI_TEST_MANIFEST)
}

fun DependencyHandler.loginModule() {
    moduleImplementation(project(":features:login"))
}

fun DependencyHandler.homeModule() {
    moduleImplementation(project(":features:home"))
}

fun DependencyHandler.signupModule() {
    moduleImplementation(project(":features:signup"))
}

fun DependencyHandler.dataModule() {
    moduleImplementation(project(":core:data"))
}

fun DependencyHandler.domainModule() {
    moduleImplementation(project(":core:domain"))
}

fun DependencyHandler.presentationModule() {
    moduleImplementation(project(":core:presentation"))
}

fun DependencyHandler.datastoreModule() {
    moduleImplementation(project(":core:datastore"))
}

fun DependencyHandler.protodatastoreModule() {
    moduleImplementation(project(":core:protodatastore"))
}

fun DependencyHandler.navigatorModule() {
    moduleImplementation(project(":core:navigator"))
}






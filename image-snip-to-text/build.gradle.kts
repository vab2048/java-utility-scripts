plugins {
    java
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

testing {
    suites {
        val integrationTest by registering(JvmTestSuite::class)
    }
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.bundles.apache.commons)
    implementation(libs.guava)
    implementation(libs.tess4j)
}
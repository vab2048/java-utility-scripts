// Global configuration for all subprojects...
subprojects {

    // Specifically for the case where the subproject is a Java project...
    plugins.withType<JavaPlugin> {

        // Ensure our standard toolchain version.
        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    repositories {
        mavenCentral()
    }



}
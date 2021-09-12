buildscript {
    val composeVersion = System.getenv("COMPOSE_TEMPLATE_COMPOSE_VERSION") ?: "1.0.0-alpha4-build328"
    val kotlinVersion = "1.5.30"

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

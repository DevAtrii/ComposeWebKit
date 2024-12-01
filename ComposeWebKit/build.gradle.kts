plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")

}

android {
    namespace = "dev.atrii.composewebkit"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
}
afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.CodeWithAthari"
                artifactId = "composewebkit"
                version = "1.0.0"

                // Include all the artifacts
                from(components["release"])

                // Include additional artifacts
                artifact("$buildDir/outputs/aar/${project.name}-release.aar")

                // Optional: Include sources
                artifact(tasks.register("androidSourcesJar", Jar::class) {
                    archiveClassifier.set("sources")
                    from(android.sourceSets.getByName("main").java.srcDirs)
                })

                pom {
                    name.set("ComposeWebKit")
                    description.set("Compose WebView with awesome syntax")
                    url.set("https://github.com/CodeWithAthari/ComposeWebKit")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("CodeWithAthari")
                            name.set("Athar Gul")
                            email.set("atriidev+code@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/CodeWithAthari/ComposeWebKit.git")
                        developerConnection.set("scm:git:ssh://github.com/CodeWithAthari/ComposeWebKit.git")
                        url.set("https://github.com/CodeWithAthari/ComposeWebKit")
                    }
                }
            }
        }
    }
}
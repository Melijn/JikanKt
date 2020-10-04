import org.jetbrains.dokka.gradle.DokkaTask


plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.10"
    maven
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://kotlin.bintray.com/ktor")
    maven("https://kotlin.bintray.com/kotlinx")
}

group = "com.github.GSculerlor"
version = "1.3.2"

val ktorVersion: String by project
val gsonVersion: String by project
val coroutinesVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")

    //Ktor
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")

    //Gson
    implementation("com.google.code.gson:gson:$gsonVersion")

    //kotlin-logging
    api("org.slf4j:slf4j-api:1.7.25")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
    }
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            moduleName.set("Dokka Gradle Example")
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
            }
        }
    }
}
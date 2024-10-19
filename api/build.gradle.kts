import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.graalvm.buildtools.native") version "0.10.3"
}

group = "dev.vrba.discord"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.mapstruct:mapstruct:1.6.2")
    implementation("com.discord4j:discord4j-core:3.2.6")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.uploadcare:uploadcare:3.5.3") {
        exclude("commons-logging", "commons-logging")
        constraints {
            implementation("commons-io:commons-io:2.17.0") {
                because("CVE-2024-47554")
            }
        }
    }
}

tasks.withType<BootBuildImage> {
    // Used for local development on the M2 Macbook
    if (System.getProperty("os.arch") == "aarch64") {
        builder = "dashaun/builder:tiny"
    }

    imageName = "ghcr.io/jirkavrba/worldle/api"
    environment.put("BP_NATIVE_IMAGE", "true")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    war
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.spring") version "1.5.20"
    kotlin("plugin.jpa") version "1.5.20"

}

group = "tech.sharply.metch"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    /**
     * Don't know why the last /tech is needed, but I got to this solution after many tries and fails.
     * Also, packages owned by organizations cannot be public for now for whatever reason.
     * Packages owned by organizations, even if they are public, can only be installed by the organization's member.
     * More digging to do!
     * Packages owned by users, on the other hand can be public, but haven't tried it.
     */
    maven("https://maven.pkg.github.com/Sharply-Tech/tech/") {
        credentials {
            username = "cosmyn9708@gmail.com"
            password = System.getenv("GITHUB_PACKAGES_REPOSITORY_TOKEN")
        }
    }
    maven("https://maven.pkg.github.com/cosminstn/tech") {
        credentials {
            username = "cosmyn9708@gmail.com"
            password = System.getenv("GITHUB_PACKAGES_REPOSITORY_TOKEN")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.session:spring-session-core")

    implementation("tech.sharply.metch:metch-orderbook:0.5.1")
    implementation("tech.sharply:spring-disruptor-mediatr:0.2.0")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

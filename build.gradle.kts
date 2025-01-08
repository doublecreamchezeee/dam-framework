plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    `java-library`
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.example"
            artifactId = "dam-framework"
            version = "1.0.0"
        }
    }
    repositories {
        mavenLocal()
    }
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.postgresql:postgresql:42.6.0") // PostgreSQL driver
    implementation("mysql:mysql-connector-java:8.0.33") // MySQL driver
    runtimeOnly("org.neo4j:neo4j-jdbc-full-bundle:6.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.mongodb:mongo-java-driver:3.12.10")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    enabled = false;
}

tasks.jar {
    archiveBaseName.set("dam-framework")
    archiveVersion.set("1.0.0")
    manifest {
        attributes(
            "Implementation-Title" to "Data Access Management Framework",
            "Implementation-Version" to version
        )
    }
}

tasks.bootJar {
    enabled = false
}

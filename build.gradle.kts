plugins {
	java
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "demo"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.2.3")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	implementation("io.github.resilience4j:resilience4j-circuitbreaker:1.7.0")

	implementation("org.springframework.boot:spring-boot-starter-validation")

	compileOnly("org.projectlombok:lombok:1.18.30")
	testCompileOnly("org.projectlombok:lombok:1.18.30")
	implementation("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val openApiGenerate by tasks.registering {
	group = "openapi"
	description = "Generates OpenAPI spec files"
	dependsOn(tasks.compileJava)
	dependsOn(tasks.processResources)
}

tasks.named("build") {
	dependsOn(openApiGenerate)
}

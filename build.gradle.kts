plugins {
    kotlin("multiplatform") version "2.3.21"
    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

val githubRef = System.getenv("GITHUB_REF")
val signingKey = System.getenv("SIGNING_KEY")
val signingPassword = System.getenv("SIGNING_PW")

group = "io.github.murzagalin"
version = githubRef?.split('/')?.last() ?: "0.1.0-SNAPSHOT"

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Multiplatform expressions evaluator")
            description.set("Kotlin multiplatform runtime infix expressions evaluator.")
            url.set("https://github.com/murzagalin/multiplatform-expressions-evaluator")

            licenses {
                license {
                    name.set("Apache 2.0 license")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0")
                }
            }
            developers {
                developer {
                    id.set("murzagalin")
                    name.set("Azamat Murzagalin")
                    email.set("azamat.em@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/murzagalin/multiplatform-expressions-evaluator")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {

    jvm {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js {
        browser()
        nodejs()
    }
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
    }

    // Apple targets
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    tvosArm64()
    tvosSimulatorArm64()

    // Desktop/Server targets
    linuxX64()
    mingwX64()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

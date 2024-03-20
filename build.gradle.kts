plugins {
    kotlin("multiplatform") version "1.8.0"
    id("maven-publish")
    id("signing")
}

val githubRef = System.getenv("GITHUB_REF")
val nexusUsername = System.getenv("NEXUS_USER")
val nexusPassword = System.getenv("NEXUS_PW")
val signingKey = System.getenv("SIGNING_KEY")
val signingPassword = System.getenv("SIGNING_PW")

group = "io.github.murzagalin"
version = githubRef?.split('/')?.last() ?: "0.1.0-SNAPSHOT"

publishing {
    repositories {
        maven {
            name = "sonatype"
            setUrl(
                if (version.toString().contains("SNAPSHOT"))
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                else
                    "https://s01.oss.sonatype.org/content/repositories/releases/"
            )

            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }

    publications.withType<MavenPublication> {
        //artifact(javadocJar.get())

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

    targets {
        jvm {
            compilations.all {
                kotlinOptions.jvmTarget = "1.8"
            }
            testRuns["test"].executionTask.configure {
                useJUnit()
            }
        }
        js(BOTH) {
            browser {
                commonWebpackConfig {
                    cssSupport {
                        enabled = true
                    }
                }
            }
            nodejs()
        }

        macosArm64()
        iosX64()
        iosArm64()
        iosArm32()
        iosSimulatorArm64()
        watchosArm32()
        watchosArm64()
        watchosX86()
        watchosX64()
        watchosSimulatorArm64()
        tvosArm64()
        tvosX64()
        tvosSimulatorArm64()
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

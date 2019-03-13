val projectGroup: String by project
group = projectGroup

val projectVersion: String by project
version = projectVersion

val projectArtifact: String by project

plugins {
    // Apply the java plugin to add support for Java
    `java-library`
    // The Maven plugin adds support for deploying artifacts to Maven repositories.
    maven
    // The signing plugin adds the ability to digitally sign built files and artifacts.
    // These digital signatures can then be used to prove who built the artifact the signature is attached to
    // as well as other information such as when the signature was generated.
    signing
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


// 根据我们在gradle.properties中声明的版本名称,来分辨是Release版本还是 snapshots版本
val isReleaseBuild = !projectVersion.endsWith("SNAPSHOT")

// 声明变量记录maven库地址
val mavenRepositoryUrl =
// 判断是发布到正式库,还是snapshots库
        if (isReleaseBuild) {
            println("RELEASE BUILD")
            val RELEASE_REPOSITORY_URL: String by project
            // 下面的库地址指向的是我们私有仓库的Releases 仓库
            if (hasProperty("RELEASE_REPOSITORY_URL")) RELEASE_REPOSITORY_URL
            else "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
        } else {
            println("SNAPSHOTS BUILD")
            val SNAPSHOT_REPOSITORY_URL: String by project
            // 下面的库地址指向的是我们私有仓库的snapshots 仓库
            if (hasProperty("SNAPSHOT_REPOSITORY_URL")) SNAPSHOT_REPOSITORY_URL
            else "https://oss.sonatype.org/content/repositories/snapshots"
        }


// 进行数字签名
signing {
    isRequired = isReleaseBuild && gradle.taskGraph.hasTask("uploadArchives")
    sign(configurations.archives.get())
}

tasks.javadoc {
    options {
        encoding = "UTF-8"
        charset("UTF-8")
        version = true
    }
}

// 生成sources.jar
val sourcesJar by tasks.registering(Jar::class) {
    group = "jar"
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    group = "jar"
    dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
    archiveClassifier.set("javadoc")
    from(tasks["javadoc"])
}

// 产生相关配置文件的任务
artifacts {
    add("archives", javadocJar)
    add("archives", sourcesJar)
}

// In this section you declare where to find the dependencies of your project
repositories {
    mavenLocal()
    mavenCentral()
    maven("http://maven.aliyun.com/nexus/content/groups/public/")
}

// In this section you declare the dependencies for your production and test code
dependencies {
    // The production code uses the SLF4J logging API at compile time
    "compile"("org.slf4j:slf4j-api:1.7.26")

    //poi
    "compile"("org.apache.poi:poi-ooxml:3.17")

    // junit5
    val jupiterVersion = "5.4.0"
    testCompile("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testCompile("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
    testCompile("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")

    //servlet
    "testCompile"("javax.servlet:javax.servlet-api:4.0.1")

    // log4j2
    "testCompile"("org.apache.logging.log4j:log4j-slf4j-impl:2.11.2")
    "testCompile"("org.apache.logging.log4j:log4j-jcl:2.11.2")
}

afterEvaluate {
    val uploadArchives: Upload by tasks
    uploadArchives.apply {
        repositories {
            withConvention(MavenRepositoryHandlerConvention::class) {
                mavenDeployer {
                    // Sign Maven POM
                    beforeDeployment {
                        signing.signPom(this)
                    }

                    withGroovyBuilder {
                        "repository"("url" to mavenRepositoryUrl) {
                            val sonatypeUsername: String by project
                            val sonatypePassword: String by project
                            "authentication"("userName" to sonatypeUsername, "password" to sonatypePassword)
                        }
                    }

                    // Maven POM generation
                    pom.project {
                        withGroovyBuilder {
                            "name"(projectArtifact)

                            "groupId"(projectGroup)
                            "artifactId"(projectArtifact)
                            "version"(projectVersion)

                            val mavenPackaing: String by project
                            "packaging"(mavenPackaing)

                            val projectUrl: String by project
                            "url"(projectUrl)

                            val projectDescription: String by project
                            "description"(projectDescription)

                            "scm" {
                                val scmUrl: String by project
                                "url"(scmUrl)

                                val scmConnection: String by project
                                "connection"(scmConnection)

                                val scmDeveloperConnection: String by project
                                "developerConnection"(scmDeveloperConnection)
                            }

                            "licenses" {
                                "license" {
                                    val licenseName: String by project
                                    "name"(licenseName)

                                    val licenseUrl: String by project
                                    "url"(licenseUrl)

                                    val licenseDistribution: String by project
                                    "distribution"(licenseDistribution)
                                }
                            }

                            "developers" {
                                "developer" {
                                    val developerId: String by project
                                    "id"(developerId)

                                    val developerName: String by project
                                    "name"(developerName)
                                }
                            }

                            "issueManagement" {
                                val issueSystem: String by project
                                "system"(issueSystem)
                                val issueUrl: String by project
                                "url"(issueUrl)
                            }
                        }
                    }
                }
            }
        }
    }
}

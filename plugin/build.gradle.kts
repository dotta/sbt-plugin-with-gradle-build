plugins {
    scala
    `maven-publish`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("org.scala-sbt:sbt:1.5.0")
    // sbt 1.5.0 is compiled with Scala 2.12.13. See https://github.com/sbt/sbt/blob/7c266e80b6fbd11f4a19456a6efc78e5dc984e3f/project/Dependencies.scala#L7
    implementation("org.scala-lang:scala-library:2.12.13")
}

tasks.jar {
  manifest {
    archiveBaseName.set("sbt-hello-world")
  }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.dotta"
            artifactId = "sbt-hello-world_2.12_1.0"
            version = "0.0.1-SNAPSHOT"
            from(components["java"])
            pom {
                properties.set(mapOf(
                    "scalaVersion" to "2.12",
                    "sbtVersion" to "1.0"
                ))
                setPackaging("jar")
            }
        }
    }
}
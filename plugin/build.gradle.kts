plugins {
    scala
    `ivy-publish`
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

publishing {
    repositories {
        ivy {
            // Configure the Ivy repository
            url = uri("${System.getProperty("user.home")}/.m2/repository")
            patternLayout {
                artifact("[organisation]/[module]_2.12_1.0/[revision]/[artifact]-[revision](-[classifier])(.[ext])")
                ivy("[organisation]/[module]_2.12_1.0/[revision]/ivy-[revision].xml")
                setM2compatible(true) // this converts the "." into "/" from the set "organisation"
            }
        }
    }
    publications {
         create<IvyPublication>("ivy") {
            organisation = "com.github.dotta"
            module = "sbt-hello-world"
            revision = project.version as String
            from(components["java"])
            // A .pom file doesn't even need to be pushed for sbt resolve the published plugin
            //artifact(module + "-" + revision + ".pom")
        }
    }
}

// As a non-standard layout format is used for publishing, the Gradle metadata can't be generated.
tasks.withType<GenerateModuleMetadata> {
    enabled = false
}
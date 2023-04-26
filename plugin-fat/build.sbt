
def commonSettings = Seq(
  version := "0.0.1-SNAPSHOT",
  organization := "com.github.dotta",
  scalaVersion := "2.12.13",
  pluginCrossBuild / sbtVersion := {
    scalaBinaryVersion.value match {
      case "2.12" => "1.5.0" // set minimum sbt version
    }
  },
)

lazy val `sbt-hello-world-thin` = project
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    skip in publish := true,
    resolvers += Resolver.mavenLocal,
    assemblyPackageScala / assembleArtifact := false,
    libraryDependencies += {
      val sbtV = (pluginCrossBuild / sbtBinaryVersion).value
      val scalaV = (update / scalaBinaryVersion).value
      Defaults.sbtPluginExtra("com.github.dotta" % "sbt-hello-world-internal" % "0.0.1-SNAPSHOT", sbtV, scalaV)
    }
  )

lazy val `sbt-hello-world-fat` = project
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    name := "sbt-hello-world",
    packageBin in Compile := (assembly in (`sbt-hello-world-thin`, Compile)).value,
    publishMavenStyle := true,
  )
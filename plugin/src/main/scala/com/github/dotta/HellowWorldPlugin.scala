package com.gradle.enterprise.sbt

import sbt._
import sbt.Keys._

object HelloWorldPlugin extends AutoPlugin {
    override def requires: Plugins = plugins.JvmPlugin
    override def trigger: PluginTrigger = allRequirements

    object autoImport {
        val helloWorld = taskKey[Unit]("Prints an hello world message")
    }

    import autoImport._

    override lazy val projectSettings = Seq(
      helloWorld := {
        val log = streams.value.log
        log.info("Hello, World!")
      }
    )
}
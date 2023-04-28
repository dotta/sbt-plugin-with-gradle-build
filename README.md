# sbt plugin with Gradle build

An hello-world sbt plugin with a Gradle build.

## See it in action

Run the following command to publish in the local M2 cache the `sbt-hello-world` sbt plugin:

    ./gradlew clean publish

The `playground` folder contains an sbt build to demonstrate how to consume and use the `sbt-hello-world` sbt plugin:

    cd playground
    sbt
    [...]
    sbt:playground> helloWorld
    [info] Hello, World!

## How can I help?

There are many aspects that can be improved in this sample project. Contributions are welcomed!

## Repository structure

This repository contains two separate projects:

1. In the base directory, a Gradle build to compile and publish an `sbt-hello-world` sbt plugin into the local M2 cache.
2. In the `playground` directory, an sbt build that consumes the `sbt-hello-world` sbt plugin.

## Challenge

The Scala community [recommends](https://www.scala-lang.org/blog/2023/04/20/sbt-plugins-community-repository.html#:~:text=Historically%2C%20sbt%20plugins%20used%20to,release%20notes%20of%20sbt%201.5.) to publish sbt plugins to Maven central. sbt enables to easily publish a plugin to Maven central, however the published artifacts are _not_ respecting the expected [Maven layout format](https://maven.apache.org/repository/layout.html). This issue is captured in [sbt/sbt#3410](https://github.com/sbt/sbt/issues/3410). Its impact is limited if sbt is used to publish a plugin because the same wrong layout format is used by sbt during resolution. That falls apart if Gradle is used to publish an sbt plugin to Maven central, as the standard Maven layout is strictly followed with no option to change it by the `maven-publish` Gradle plugin.

A [fix](https://github.com/sbt/sbt/pull/7096) for [sbt/sbt#3410](https://github.com/sbt/sbt/issues/3410) has been shipped in sbt 1.9.0-M1, enabling sbt to resolves an sbt plugin that has been published following the standard Maven layout format. But requiring sbt 1.9.0+ would put a hard cliff to the adoption of a sbt plugin, making this solution not very compelling.

A good solution should make it a non-concern for users how a sbt plugin has been built and publish. It's important that the published sbt plugin can be added to an sbt build and resolved by sbt as usual and following the sbt idioms. Next are a few alternatives that respect this requirement:

1. Define a Gradle custom task that copies and rename the `.jar` and `.pom` produced by the `maven-publish` plugin to match the artifacts' name [expected by sbt](https://github.com/sbt/librarymanagement/blob/develop/core/src/main/scala/sbt/librarymanagement/ResolverExtra.scala#L387-L388).
2. Publish with Ivy (using the `ivy-publish` Gradle plugin) into the local M2 folder, using the (wrong) [Maven-like format layout](https://github.com/sbt/librarymanagement/blob/develop/core/src/main/scala/sbt/librarymanagement/ResolverExtra.scala#L387-L388) that's used by sbt.
3. Extend or fork the `maven-publish` Gradle plugin to enable the publishing of a sbt plugin so that it abides to the sbt Maven-like format layout.

### Define a Gradle custom task

I've explored this option but I wasn't able to make it work. Being new to Gradle makes it hard for me to say whether this is possible to achieve or not (but it feels possible).

### Publish with Ivy

This is the solution implemented in this repository. The Gradle build file inside the `plugin` directory defines the layout format that matches the one used by sbt. Interestingly, a `.pom` file doesn't even need to be included for sbt to properly resolve the published sbt plugin. But it'd be easy to do so and in the Gradle build it's left as a comment how this could be achieved. [These](https://repo1.maven.org/maven2/com/github/sbt/sbt-release_2.12_1.0/1.0.14/) `sbt-release` artifacts in Maven central serve as further evidence that no `.pom` is needed when publishing a sbt plugin to Maven central.

### Extend or fork the `maven-publish`

This solution hasn't been explored. In principle, there should be no reason why this wouldn't work.

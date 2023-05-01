# sbt plugin with Gradle build

An hello-world sbt plugin with a Gradle build.

## See it in action

Run the below command to publish to the local M2 cache the `sbt-hello-world` sbt plugin defined in this repository (in the `plugin` folder):

    ./gradlew clean publish

The `playground` folder contains an sbt build that demonstrates how to consume an `sbt-hello-world` sbt plugin. Below is a prompt showcasing how to interact with it:

    cd playground
    sbt
    [...]
    sbt:playground> helloWorld
    [info] Hello, World!

## How can I help?

There are many aspects that can be improved in this sample project. Contributions are welcomed!

## Repository structure

This repository contains two separate projects (with different builds):

1. In the base folder, a Gradle build to compile and publish an `sbt-hello-world` sbt plugin into the local M2 cache.
2. In the `playground` folder, an sbt build that consumes the `sbt-hello-world` sbt plugin.

## Challenge

### Publishing

The Scala community [recommendation](https://www.scala-lang.org/blog/2023/04/20/sbt-plugins-community-repository.html#:~:text=Historically%2C%20sbt%20plugins%20used%20to,release%20notes%20of%20sbt%201.5.) is to publish sbt plugins to Maven central.

While sbt enables to easily publish a plugin to Maven central, the published artifacts are _not_ respecting the expected [Maven layout format](https://maven.apache.org/repository/layout.html). This issue is captured in [sbt/sbt#3410](https://github.com/sbt/sbt/issues/3410), but it has no (or very limited) impact if sbt is used to publish an sbt plugin. This because _the same_ wrong layout format is used by sbt during resolution. However, the situation changes if Gradle is used to publish an sbt plugin to Maven central, as the _expected_ standard Maven layout is *strictly* followed (and there is no option to tweak it with the `maven-publish` Gradle plugin).

A [fix](https://github.com/sbt/sbt/pull/7096) for [sbt/sbt#3410](https://github.com/sbt/sbt/issues/3410) has been included in sbt 1.9.0-M1, enabling sbt to resolves an sbt plugin that's published following the expected Maven layout format. But requiring sbt 1.9.0+ would put a hard cliff to the adoption of any sbt plugin, making this solution not really viable for now.

A good solution should check that the published plugin can be declared and resolved by sbt following the standard sbt idioms and support a good range of sbt versions (ideally going back at least to sbt 1.5.0). Next are the considered alternatives that would respect these requirements:

1. Define a Gradle custom task that copies and rename the `.jar` and `.pom` produced by the `maven-publish` plugin to match the artifacts' name [expected by sbt](https://github.com/sbt/librarymanagement/blob/develop/core/src/main/scala/sbt/librarymanagement/ResolverExtra.scala#L387-L388).
2. Publish with Ivy (using the `ivy-publish` Gradle plugin) into the local M2 folder, using the sbt (wrong) [Maven-like format layout](https://github.com/sbt/librarymanagement/blob/develop/core/src/main/scala/sbt/librarymanagement/ResolverExtra.scala#L387-L388).
3. Extend or fork the `maven-publish` Gradle plugin to enable the publishing of an sbt plugin to follow the sbt (wrong) Maven-like format layout.

#### Define a Gradle custom task

I've explored this option but I wasn't able to make it work. Being new to Gradle makes it hard for me to state whether this is possible to achieve or not, but it feels possible. Though, my perception is that there would be quite a bit of boilerplate to maintain (or automate) in the build. Overall, this solution feels error-prone and complex.

#### Publish with Ivy

This is the solution implemented in this repository. The Gradle build file inside the `plugin` folder defines the layout format that matches the one used by sbt. Interestingly, a `.pom` file doesn't even need to be published, as sbt can properly resolve the published sbt plugin without it. But if a `.pom` would need to be published, it'd be easy to do so (see the commented code in the `plugin` Gradle build file). [These](https://repo1.maven.org/maven2/com/github/sbt/sbt-release_2.12_1.0/1.0.14/) `sbt-release` artifacts in Maven central serve as further evidence that no `.pom` is required when publishing an sbt plugin to Maven central.

#### Extend or fork the `maven-publish` Gradle plugin

This solution hasn't been explored. In principle, there should be no reason why this wouldn't work.

### sbt auto-plugin

A common practice is to create sbt plugins as `AutoPlugin` (refer to the [sbt documentation](https://www.scala-sbt.org/1.x/docs/Plugins.html#Creating+an+auto+plugin) for details). If sbt is used as build tool for the sbt plugin, an `sbt.autoplugins` file with the fully-qualified path to the module's name extending `AutoPlugin` is automatically included in the plugin's JAR under the `sbt/` folder. This enables sbt to discover the auto-plugin when it's added to an sbt build.

In this repository, the `sbt.autoplugins` file has been hardcoded and placed in the `src/main/resources` folder of the `plugin` project. Of course, it'd be cleaner if it was generated at build time via a custom-defined Gradle task.

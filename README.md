# Sbt plugin with Gradle build

A simple hello-world Sbt plugin with a Gradle build.

Run the following command to publish the `sbt-hello-world-internal` sbt plugin:

    ./gradlew clean publishToMavenLocal

Because older versions of Sbt (prior to 1.9.0-M1) are unable to retrieve sbt plugins that are published using the standard Maven layout (because [the fix](https://github.com/sbt/sbt/pull/7096) for [sbt/sbt#3410](https://github.com/sbt/sbt/issues/3410) was shipped with 1.9.0-M1), the `sbt-hello-world-internal` can be used only with Sbt 1.9.0-M1+, which isn't ideal. The `plugin-fat` project enables to workaround this limitation by re-packaging and publishing the content of `sbt-hello-world-internal` as `sbt-hello-world`.

    cd plugin-fat
    sbt
    [...]
    sbt:plugin-fat> publishM2

The `playground` folder contains an Sbt build to demonstrate how to consume and use the `sbt-hello-world` sbt plugin:

    cd playground
    sbt
    [...]
    sbt:playground> helloWorld
    [info] Hello, World!

Note that the playground project uses sbt 1.5.0.

There are many aspects that can be improved in this sample project and contributions are welcomed ;-)
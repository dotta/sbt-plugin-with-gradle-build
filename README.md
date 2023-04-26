# Sbt plugin with Gradle build

A simple hello-world Sbt plugin with a Gradle build.

Run the following command to publish the `sbt-hello-world` sbt plugin:

    ./gradlew clean publishToMavenLocal

The `playground` folder contains an Sbt build to demonstrate how to consume and use the `sbt-hello-world` sbt plugin:

    cd playground
    sbt
    [...]
    sbt:playground> helloWorld
    [info] Hello, World!

It's important to note that sbt 1.9.0-M1 or later must be used for the playground Sbt build to properly fetch the `sbt-hello-world` sbt plugin. This because [the fix](https://github.com/sbt/sbt/pull/7096) for [sbt/sbt#3410](https://github.com/sbt/sbt/issues/3410) was shipped with 1.9.0-M1.

There are many aspects that can be improved in this sample project and contributions are welcomed ;-)
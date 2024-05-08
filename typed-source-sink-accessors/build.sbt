import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.14",
  resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
  resolvers ++= Resolver.sonatypeOssRepos("releases"),
  autoAPIMappings := true,
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  // Macros paradise is integrated into 2.13 but requires a scalacOption
  scalacOptions += "-Ymacro-annotations",
  scalacOptions += "-language:experimental.macros"
)

lazy val macros = (project in file("macros"))
  .settings(name := "example-macros")
  .settings(commonSettings: _*)

lazy val root = (project in file("."))
  .settings(
    name := "typed-source-sink-accessors",
    libraryDependencies += munit % Test
  )
  .settings(commonSettings: _*)
  .dependsOn(macros)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

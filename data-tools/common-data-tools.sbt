enablePlugins(ScalaJSPlugin)
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

resolvers += "Apache" at "https://repo.maven.apache.org/maven2"

lazy val commonSettings = Seq(
  organization := "com.lkroll.common",
  version := "1.3.1-SNAPSHOT",
  scalaVersion := "3.0.0-M2",
  scalacOptions ++= Seq("-source:3.0-migration", "-deprecation", "-feature", "-unchecked", "-language:strictEquality"),
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % "3.2.3" % "test" //).withDottyCompat(scalaVersion.value)
  ),
  bintrayOrganization := Some("lkrollcom"),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "Common Data Tools Root",
    skip in publish := true
  )
  .aggregate(dataToolsJVM, dataToolsJS)

lazy val dataTools = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Full) in file("."))
  .settings(
    commonSettings,
    name := "Common Data Tools",
    libraryDependencies += "org.scala-lang" %% "scala3-tasty-inspector" % scalaVersion.value
  )
  .jvmSettings(
    // Add JVM-specific settings here
    parallelExecution in Test := false,
    logBuffered in Test := false
  )
  .jsSettings(
    // Add JS-specific settings here
  )

lazy val dataToolsJVM = dataTools.jvm
lazy val dataToolsJS = dataTools.js

enablePlugins(ScalaJSPlugin)
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

ThisBuild / organization := "com.lkroll"

ThisBuild / version := "1.3.3"

ThisBuild / scalaVersion := "2.13.5"
ThisBuild / crossScalaVersions := Seq("2.12.13", "2.13.5")

ThisBuild / licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

ThisBuild / homepage :=Some(url("https://github.com/Bathtor/common-utils"))
ThisBuild / scmInfo := Some(
                ScmInfo(url("https://github.com/Bathtor/common-utils"),
                            "git@github.com:Bathtor/common-utils.git"))
ThisBuild / developers := List(Developer(id = "lkroll",
                             name = "Lars Kroll",
                             email = "bathtor@googlemail.com",
                             url = url("https://github.com/Bathtor")))
publishMavenStyle := true

// Add sonatype repository settings
sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / publishTo := sonatypePublishToBundle.value

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
  	"org.scalatest" %%% "scalatest" % "3.2.5" % "test"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-language:implicitConversions",
    //"-Xfatal-warnings"
  )
)

lazy val root = (project in file(".")).settings(
	commonSettings,
	name := "Common Data Tools Root",
	publish / skip := true,
).aggregate(dataToolsJVM, dataToolsJS)

lazy val dataTools = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Full) in file(".")).
  settings(
	  commonSettings,
	  name := "Common Data Tools",
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ).
  jvmSettings(
    // Add JVM-specific settings here
    parallelExecution in Test := false,
    logBuffered in Test := false
  ).
  jsSettings(
    // Add JS-specific settings here
  )

lazy val dataToolsJVM = dataTools.jvm
lazy val dataToolsJS = dataTools.js

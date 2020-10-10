enablePlugins(ScalaJSPlugin)
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
//scalacOptions in ThisBuild ++= Seq("-Ymacro-debug-verbose")

resolvers += "Apache" at "https://repo.maven.apache.org/maven2"

lazy val commonSettings = Seq(
  organization := "com.lkroll.common",
  version := "1.3.1",
  scalaVersion := "2.12.11",
  libraryDependencies ++= Seq(
  	"org.scalatest" %%% "scalatest" % "3.2.0" % "test"),
  bintrayOrganization := Some("lkrollcom"),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
)

lazy val root = (project in file(".")).settings(
	commonSettings,
	name := "Common Data Tools Root",
	skip in publish := true,
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

import sbt._
import sbt.Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys

object CsiBuild extends Build {

  EclipseKeys.skipParents in ThisBuild := false

  val sprayV = "1.3.3"
  val akkaV = "2.3.9"

  lazy val commonutils = Project(
    id = "common-utils-scala",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Common Utils Scala",
      organization := "com.larskroll",
      version := "1.2",
      scalaVersion := "2.11.8",
      publishMavenStyle := true,
      publishTo := Some(Resolver.sftp("SICS Snapshot Repository", "kompics.i.sics.se", "/home/maven/snapshotrepository")),
      //scalacOptions += "-Ydependent-method-types",
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:postfixOps", "-language:implicitConversions"),
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      resolvers += "Typesafe Snapshots" at "http://repo.akka.io/snapshots/",
      resolvers += "spray repo" at "http://repo.spray.io",
      resolvers += "spray nightly repo" at "http://nightlies.spray.io",
      resolvers += "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases/",
      resolvers += "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
      resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      resolvers += {
        import java.io.File
        val privateKeyFile: File = new File(sys.env("HOME") + "/.ssh/id_rsa")
        Resolver.ssh("scala-sh", "kompics.i.sics.se") as("root", privateKeyFile) withPermissions("0644")
      },
      //libraryDependencies += "org.slf4j" % "slf4j-simple" % "[1.7,)",
      libraryDependencies += "io.spray" %% "spray-can" % sprayV % "provided",
      libraryDependencies += "io.spray" %% "spray-routing" % sprayV % "provided",
      libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaV % "provided",
      libraryDependencies += "org.javatuples" % "javatuples" % "1.2" % "provided",
      libraryDependencies += "com.google.guava" % "guava" % "18.0" % "provided"
    )
  )
}

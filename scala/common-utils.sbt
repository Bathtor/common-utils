name := "Common Utils Scala"

organization := "com.larskroll"

version := "2.0"

scalaVersion := "2.12.4"

//crossScalaVersions := Seq("2.11.11", "2.12.4")

scalacOptions ++= Seq("-deprecation","-feature","-language:implicitConversions")

// DEPENDENCIES

lazy val deps =
  new {
    //val sprayV = "1.3.3"
  	val akkaV = "2.5.8"

    //val sprayCan		= "io.spray"						%% "spray-can"						% sprayV
    //val sprayRouting	= "io.spray"						%% "spray-routing"					% sprayV
    val akkaActor		= "com.typesafe.akka"				%% "akka-actor"						% akkaV
    val javaTuples		= "org.javatuples"					%  "javatuples"						% "1.2"
    val guava			= "com.google.guava"				%  "guava"							% "18.0"
    val scalatest		= "org.scalatest"					%% "scalatest"						% "3.0.4"
    val jline           = "org.jline"                  		%  "jline"                          % "3.5.1"
    val fastparse		= "com.lihaoyi"						%% "fastparse"						% "1.0.0"
    val log4j			= "log4j"							%  "log4j"							% "1.2.17"
    val scalalogging	= "com.typesafe.scala-logging" 		%% "scala-logging" 					% "3.7.2"
  }

resolvers += Resolver.mavenLocal
resolvers += "Kompics Releases" at "http://kompics.sics.se/maven/repository/"
resolvers += "Kompics Snapshots" at "http://kompics.sics.se/maven/snapshotrepository/"

libraryDependencies ++= Seq(
	//deps.sprayCan % "provided",
	//deps.sprayRouting % "provided",
	deps.akkaActor % "provided",
	deps.javaTuples % "provided",
	deps.guava % "provided",
	deps.jline % "provided",
	deps.fastparse % "provided",
	deps.log4j % "provided",
	deps.scalalogging % "provided",
	deps.scalatest % "test")

parallelExecution in Test := false

publishMavenStyle := true
publishTo := {
	val kompics = "kompics.i.sics.se"
	val keyFile = Path.userHome / ".ssh" / "id_rsa"
	if (version.value.trim.endsWith("SNAPSHOT"))
		Some(Resolver.sftp("SICS Snapshot Repository", kompics, "/home/maven/snapshotrepository") as("root", keyFile))
	else
		Some(Resolver.sftp("SICS Release Repository", kompics, "/home/maven/repository") as("root", keyFile))
}

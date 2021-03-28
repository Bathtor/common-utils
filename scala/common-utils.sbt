name := "Common Utils Scala"

organization := "com.lkroll"

version := "2.2.0"

crossScalaVersions := Seq("2.12.13", "2.13.5")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/Bathtor/common-utils"))
scmInfo := Some(
                ScmInfo(url("https://github.com/Bathtor/common-utils"),
                            "git@github.com:Bathtor/common-utils.git"))
developers := List(Developer(id = "lkroll",
                             name = "Lars Kroll",
                             email = "bathtor@googlemail.com",
                             url = url("https://github.com/Bathtor")))
publishMavenStyle := true

// Add sonatype repository settings
sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
publishTo := sonatypePublishToBundle.value

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:implicitConversions",
 // "-Xfatal-warnings"
  )

// DEPENDENCIES

lazy val deps =
  new {
    val akkaActor     = "com.typesafe.akka"				    %% "akka-actor"						% "2.6.1"
    val javaTuples		= "org.javatuples"					    %  "javatuples"						% "1.2"
    val guava			    = "com.google.guava"				    %  "guava"							  % "30.1-jre"
    val scalatest		  = "org.scalatest"					      %% "scalatest"						% "3.2.5"
    val jline         = "org.jline"                   %  "jline"                % "3.19.0"
    val fastparse		  = "com.lihaoyi"						      %% "fastparse"						% "2.3.2"
    val log4j			    = "log4j"							          %  "log4j"							  % "1.2.+"
    val scalalogging	= "com.typesafe.scala-logging" 	%% "scala-logging" 	      % "3.9.+"
  }

libraryDependencies ++= Seq(
	deps.akkaActor % "provided",
	deps.javaTuples % "provided",
	deps.guava % "provided",
	deps.jline % "provided",
	deps.fastparse % "provided",
	deps.log4j % "provided",
	deps.scalalogging % "provided",
	deps.scalatest % "test")

parallelExecution in Test := false

name := "Common Utils Scala"

organization := "com.larskroll"

version := "2.1.1"

scalaVersion := "3.0.0-M2"
crossScalaVersions := Seq("2.12.10", "2.13.1", "3.0.0-M2") 

scalacOptions ++= { 
      if (isDotty.value) Seq(
        "-source:3.0-migration",
        "-deprecation",
        "-feature",
        "-unchecked",
        "-language:implicitConversions",
    	"-language:strictEquality")
      else Seq(
        "-deprecation",
        "-feature",
        "-unchecked",
        "-language:implicitConversions")
    }

// DEPENDENCIES

lazy val deps =
  new {
  	val akkaV = "2.6.1"

    val akkaActor     = "com.typesafe.akka"				    %% "akka-actor"						% akkaV
    val javaTuples		= "org.javatuples"					    %  "javatuples"						% "1.2"
    val guava			    = "com.google.guava"				    %  "guava"							  % "27.1-jre"
    val scalatest		  = "org.scalatest"					      %% "scalatest"						% "3.1.0"
    val jline         = "org.jline"                   %  "jline"                % "3.13.2"
    val fastparse		  = "com.lihaoyi"						      %% "fastparse"						% "2.1.3"
    val log4j			    = "log4j"							          %  "log4j"							  % "1.2.+"
    val scalalogging	= "com.typesafe.scala-logging" 	%% "scala-logging" 	      % "3.9.+"
  }

libraryDependencies ++= Seq(
	//deps.sprayCan % "provided",
	//deps.sprayRouting % "provided",
	(deps.akkaActor % "provided").withDottyCompat(scalaVersion.value),
	deps.javaTuples % "provided",
	deps.guava % "provided",
	deps.jline % "provided",
	(deps.fastparse % "provided").withDottyCompat(scalaVersion.value),
	deps.log4j % "provided",
	(deps.scalalogging % "provided").withDottyCompat(scalaVersion.value),
	(deps.scalatest % "test").withDottyCompat(scalaVersion.value)
)

parallelExecution in Test := false

bintrayOrganization := Some("lkrollcom")
bintrayRepository := "maven"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
bintrayPackageLabels := Seq("utils")

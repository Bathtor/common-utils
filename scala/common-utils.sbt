name := "Common Utils Scala"

organization := "com.larskroll"

version := "2.1.0"

scalaVersion := "2.13.1"
crossScalaVersions := Seq("2.12.10", "2.13.1") 

scalacOptions ++= Seq("-deprecation","-feature","-language:implicitConversions")

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
	deps.akkaActor % "provided",
	deps.javaTuples % "provided",
	deps.guava % "provided",
	deps.jline % "provided",
	deps.fastparse % "provided",
	deps.log4j % "provided",
	deps.scalalogging % "provided",
	deps.scalatest % "test")

parallelExecution in Test := false

bintrayOrganization := Some("lkrollcom")
bintrayRepository := "maven"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
bintrayPackageLabels := Seq("utils")

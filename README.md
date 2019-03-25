CommonUtils
============

[![Build Status](https://travis-ci.com/Bathtor/common-utils.svg?branch=master)](https://travis-ci.com/Bathtor/common-utils)

This is just a quick collection of things that I see coming up over and over again in my projects.

### Dependencies
All artifacts are deployed on [Bintray](https://dl.bintray.com/lkrollcom/maven). 
To the necessary resolvers add the following code to your `pom.xml` or `build.sbt` file.
- Java:
```xml
<repository>
  <snapshots>
    <enabled>false</enabled>
  </snapshots>
  <id>bintray-lkrollcom-maven</id>
  <name>bintray</name>
  <url>https://dl.bintray.com/lkrollcom/maven</url>
</repository>
```
- Scala:
```scala
resolvers += Resolver.bintrayRepo("lkrollcom", "maven")
```

The individual dependencies with their latest version are the following:

- Java: [ ![Download](https://api.bintray.com/packages/lkrollcom/maven/common-utils/images/download.svg) ](https://bintray.com/lkrollcom/maven/common-utils/_latestVersion)
```xml
<dependency>
    <groupId>com.larskroll</groupId>
    <artifactId>common-utils</artifactId>
    <version>${PUT LATEST VERSION HERE}</version>
</dependency>
```

- Scala: [ ![Download](https://api.bintray.com/packages/lkrollcom/maven/common-utils-scala/images/download.svg) ](https://bintray.com/lkrollcom/maven/common-utils-scala/_latestVersion)
```scala
libraryDependencies += "com.larskroll" %% "common-utils-scala" % "PUT LATEST VERSION HERE"
```

- Data Tools: [ ![Download](https://api.bintray.com/packages/lkrollcom/maven/common-data-tools/images/download.svg) ](https://bintray.com/lkrollcom/maven/common-data-tools/_latestVersion)
JVM-only:
```scala
libraryDependencies += "com.larskroll" %% "common-data-tools" % "PUT LATEST VERSION HERE"
```
Scala.js or mixed:
```scala
libraryDependencies += "com.larskroll" %%% "common-data-tools" % "PUT LATEST VERSION HERE"
```

CommonUtils
============

[![CI](https://github.com/Bathtor/common-utils/actions/workflows/ci.yml/badge.svg)](https://github.com/Bathtor/common-utils/actions)

This is just a quick collection of things that I see coming up over and over again in my projects.

### Dependencies

#### Resolvers
All artifacts are deployed on [Maven Central](https://search.maven.org/search?q=com.lkroll), so no special resolver should be necessary in general.

#### Artefacts
The individual dependencies with their latest version are the following:

##### Java
[![Maven Central](https://img.shields.io/maven-central/v/com.lkroll/common-utils)](https://search.maven.org/artifact/com.lkroll/common-utils)
```xml
<dependency>
    <groupId>com.lkroll</groupId>
    <artifactId>common-utils</artifactId>
    <version>${PUT LATEST VERSION HERE}</version>
</dependency>
```

##### Scala
[![Maven Central](https://img.shields.io/maven-central/v/com.lkroll/common-utils-scala_2.13)](https://search.maven.org/artifact/com.lkroll/common-utils-scala_2.13)
```scala
libraryDependencies += "com.lkroll" %% "common-utils-scala" % "PUT LATEST VERSION HERE"
```

##### Data Tools
[![Maven Central](https://img.shields.io/maven-central/v/com.lkroll/common-data-tools_2.13)](https://search.maven.org/artifact/com.lkroll/common-data-tools_2.13)

- JVM-only:
```scala
libraryDependencies += "com.lkroll" %% "common-data-tools" % "PUT LATEST VERSION HERE"
```

- Scala.js or mixed:
```scala
libraryDependencies += "com.lkroll" %%% "common-data-tools" % "PUT LATEST VERSION HERE"
```

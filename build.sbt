seq(conscriptSettings :_*)

organization := "org.smartdox"

name := "smartdoxprocessor"

version := "0.3.0-RC2-SNAPSHOT"

scalaVersion := "2.9.2"

// crossScalaVersions := Seq("2.9.2", "2.9.1")

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

// resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

resolvers += "Asami Maven Repository" at "http://www.asamioffice.com/maven"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.4"

libraryDependencies += "org.goldenport" %% "goldenport" % "0.4.0"

libraryDependencies += "org.goldenport" %% "goldenport-scalaz-lib" % "0.2.0"

libraryDependencies += "org.apache.poi" % "poi" % "3.8"

libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.8"

libraryDependencies += "org.smartdox" %% "smartdox" % "0.3.0"

libraryDependencies += "org.xmlsmartdoc" % "smartdoc" % "2.0-beta2"

libraryDependencies += "org.simplemodeling" %% "simplemodeler" % "0.4.0-RC"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

libraryDependencies += "org.goldenport" %% "goldenport-scalatest-lib" % "0.2.0" % "test"

// onejar
// seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

// mainClass in oneJar := Some("org.smartdox.processor.Main")

//
publishTo := Some(Resolver.file("asamioffice", file("target/maven-repository")))

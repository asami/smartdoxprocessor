seq(conscriptSettings :_*)

organization := "org.smartdox"

name := "smartdoxprocessor"

version := "0.2.2-SNAPSHOT"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

// resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

resolvers += "Asami Maven Repository" at "http://www.asamioffice.com/maven"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.3"

libraryDependencies += "org.goldenport" %% "goldenport" % "0.3.1"

libraryDependencies += "org.goldenport" %% "scalazlib" % "0.1.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "org.smartdox" %% "smartdox" % "0.2.2-SNAPSHOT"

libraryDependencies += "org.xmlsmartdoc" % "smartdoc" % "2.0-beta2"

libraryDependencies += "org.simplemodeling" %% "simplemodeler" % "0.3.1"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

// onejar
seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

mainClass in oneJar := Some("org.smartdox.processor.Main")

//
publishTo := Some(Resolver.file("asamioffice", file("target/maven-repository")))

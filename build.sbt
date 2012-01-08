seq(conscriptSettings :_*)

organization := "org.smartdox"

name := "smartdoxprocessor"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

resolvers += "Asami Maven Repository" at "http://www.asamioffice.com/maven"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.3"

libraryDependencies += "org.goldenport" %% "goldenport" % "0.2.4-SNAPSHOT"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "org.xmlsmartdoc" % "smartdoc" % "2.0-SNAPSHOT"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

// for gp

//libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.9.1"

//libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided"

//libraryDependencies += "commons-fileupload" % "commons-fileupload" % "1.2.2" % "provided"

//libraryDependencies += "commons-io" % "commons-io" % "1.3.2" % "provided"

//libraryDependencies += "org.apache.poi" % "poi" % "3.8-beta4" % "provided"

//libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.8-beta4" % "provided"

// onejar

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

mainClass in oneJar := Some("org.smartdox.processor.Main")

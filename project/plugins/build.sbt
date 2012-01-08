
resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse" % "1.5.0")

addSbtPlugin("net.databinder" % "conscript-plugin" % "0.3.3")

// onejar

resolvers += "retronym-releases" at "http://retronym.github.com/repo/releases"

resolvers += "retronym-snapshots" at "http://retronym.github.com/repo/snapshots"

addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.6")

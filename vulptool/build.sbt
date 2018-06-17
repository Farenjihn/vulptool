name := "vulptool"

version := "1.0"

lazy val `vulptool` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"
libraryDependencies += "org.mariadb.jdbc" % "mariadb-java-client" % "2.2.3"
libraryDependencies += "com.github.t3hnar" %% "scala-bcrypt" % "3.1"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
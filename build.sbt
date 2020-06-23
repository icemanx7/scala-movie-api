enablePlugins(JavaAppPackaging)

name := "movie-webserver"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.slick"  %% "slick"                % "3.3.0",
  "com.typesafe.slick"  %% "slick-hikaricp"       % "3.3.0",
  "org.slf4j"           %  "slf4j-nop"            % "1.6.4",
  "org.xerial"          %  "sqlite-jdbc"          % "3.7.2"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % "10.1.8",
  "com.typesafe.akka" %% "akka-stream" % "2.5.19" // or whatever the latest version is
)

libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"

libraryDependencies ++= Seq(
  "com.pauldijou" %% "jwt-spray-json" % "2.1.0"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.26",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.12"
)

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.10.0" % "test")
scalacOptions in Test ++= Seq("-Yrangepos")


name := "movie-webserver"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.slick"  %% "slick"                % "3.3.0",
  "com.typesafe.slick"  %% "slick-hikaricp"       % "3.3.0",
  "org.slf4j"           %  "slf4j-nop"            % "1.6.4",
  "org.xerial"          %  "sqlite-jdbc"          % "3.7.2"
)

val http4sVersion = "0.20.0-M6"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "com.typesafe.slick" %% "slick" % "3.3.0"
  
)

libraryDependencies ++= Seq(
  "org.xerial" % "sqlite-jdbc" % "3.18.0",
  "io.getquill" %% "quill-jdbc" % "3.1.0"
)

scalacOptions ++= Seq("-Ypartial-unification")
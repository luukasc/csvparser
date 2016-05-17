name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  	jdbc,
  	cache,
  	ws,
  	"org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  	"jp.t2v" %% "play2-auth"        % "0.14.2",
	"jp.t2v" %% "play2-auth-social" % "0.14.2", // for social login
	"jp.t2v" %% "play2-auth-test"   % "0.14.2" % "test",
	play.sbt.Play.autoImport.cache
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Eclipse fix
EclipseKeys.createSrc := EclipseCreateSrc.All




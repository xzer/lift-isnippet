import sbt._
import Keys._

object HelloBuild extends Build {

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := "net.xzer",
    version      := "0.1",
    scalaVersion := "2.9.1"
  ) ++ Seq(com.github.siasia.WebPlugin.webSettings: _*)

	val dependencies = Seq(libraryDependencies ++= {
       val liftVersion = "2.4"
       Seq(
               "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
               "net.liftweb" %% "lift-mapper" % liftVersion % "compile",
               "org.mortbay.jetty" % "jetty" % "6.1.26" % "test",
               "junit" % "junit" % "4.7" % "test",
               "ch.qos.logback" % "logback-classic" % "0.9.26",
               "org.scala-tools.testing" %% "specs" % "1.6.9" % "test",
               "com.h2database" % "h2" % "1.2.147",
			   "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"
       )
	})

    lazy val root = Project(id = "isnippet",
                            base = file(".")) aggregate(core, example)

    lazy val core = Project(id = "isnippet-core",
                            base = file("core"),
						    settings = buildSettings ++ dependencies)
	
    lazy val example = Project(id = "isnippet-example",
                               base = file("example"),
							   settings = buildSettings ++ dependencies) dependsOn(core)
}
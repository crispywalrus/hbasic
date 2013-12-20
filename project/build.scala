import sbt._
import Keys._
import com.github.retronym.SbtOneJar

object HBasic extends Build {

  lazy val root = Project("hbasic",file(".")) settings(coreSettings : _*)

  lazy val commonSettings: Seq[Setting[_]] = Seq(
    organization := "com.crispywalrus",
    version := "0.0.1",
    scalaVersion := "2.10.3",
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
      "-explaintypes"
    ),
    exportJars := true
  ) ++ SbtOneJar.oneJarSettings

  lazy val coreSettings = commonSettings ++ Seq(
    name := "hbasic",

    resolvers ++= Seq(
      "maven" at "http://repo1.maven.org/maven2",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Local Maven" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
    ),

    ivyXML :=
      <dependencies>
        <exclude org="org.jboss.netty"/>
        <exclude module="log4j"/>
        <exclude module="slf4j-log4j12"/>
        <exclude module="commons-logging"/>
      </dependencies>
    ,

    libraryDependencies ++= Seq(
      "org.clapper" %% "grizzled-slf4j" % "1.0.2",
      "com.rabbitmq" % "amqp-client" % "3.2.2",
      "com.typesafe.akka" %% "akka-actor" % "2.2.3",
      "org.scalatest" %% "scalatest" % "2.0" % "test",
      "junit" % "junit" % "4.11" % "test",
      "org.mockito" % "mockito-all" % "1.9.0" % "test"
    ),

    parallelExecution in Test := false,
    publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath+"/.m2/repository"))),

    publishMavenStyle := true
  )

}
                                                          

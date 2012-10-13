import sbt._
import Keys._
import com.github.retronym.SbtOneJar

object HBasic extends Build {

  lazy val root = Project("hbasic",file(".")) settings(coreSettings : _*)

  lazy val commonSettings: Seq[Setting[_]] = Seq(
    organization := "com.crispywalrus",
    version := "0.0.1",
    scalaVersion := "2.9.2",
    scalacOptions ++= Seq("-deprecation","-unchecked","-explaintypes"),
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
      "net.databinder" %% "unfiltered-netty-server" % "0.6.3",
      "net.databinder" %% "dispatch-nio" % "0.8.8",
      "org.scalaz" %% "scalaz-core" % "6.0.4",
      "commons-codec" % "commons-codec" % "1.6",
      "org.slf4j" % "slf4j-api" % "1.6.6",
      "org.slf4j" % "log4j-over-slf4j" % "1.6.6",
      "org.clapper" %% "grizzled-slf4j" % "0.6.9",
      "org.codehaus.jackson" % "jackson-core-asl" % "1.9.9",
      "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.9",
      "com.basho.riak" % "riak-client" % "1.0.5",
      "com.lambdaworks" % "lettuce" % "2.1.0",
      "com.rabbitmq" % "amqp-client" % "2.8.6",
      "com.stackmob" %% "scaliak" % "0.1.b-SNAPSHOT",
      "org.hbase" % "asynchbase" % "1.3.2",
      "com.netflix.curator" % "curator-x-discovery" % "1.1.16",
      "com.typesafe.akka" % "akka-actor" % "2.0.3",
      "com.twitter" % "util-core" % "5.3.6",
      "com.twitter" % "util-eval" % "5.3.6",
      "org.scribe" % "scribe" % "1.3.2",
      "org.specs2" %% "specs2" % "1.12.1" % "test",
      "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
      "junit" % "junit" % "4.10" % "test",
      "org.mockito" % "mockito-all" % "1.9.0" % "test",
      "com.netflix.curator" % "curator-test" % "1.1.16" % "test"
    ),

    parallelExecution in Test := false,
    publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath+"/.m2/repository"))),

    publishMavenStyle := true
  )

}
                                                          

import sbt._

object Dependencies {

  val sparkVersion        = "2.2.0"
  val logbackVersion      = "1.2.3"
  val scalaLoggingVersion = "3.7.2"

  lazy val spark = Seq(
    "org.apache.spark" %% "spark-sql"    % sparkVersion,
    "org.apache.spark" %% "spark-mllib"  % sparkVersion
  )

  lazy val logging = Seq(
    "ch.qos.logback"              % "logback-classic" % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging"   % scalaLoggingVersion
  )
}

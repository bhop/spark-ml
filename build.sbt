import Dependencies._

lazy val commonSettings = Seq(
  version       := "0.1.0",
  scalaVersion  := "2.11.11",
  libraryDependencies
    ++= spark
    ++ logging
)

lazy val root = project.in(file("."))
  .settings(commonSettings)
  .aggregate(naiveBayes)

lazy val common = project.in(file("common"))
    .settings(commonSettings)

lazy val naiveBayes = project.in(file("naive-bayes"))
  .settings(commonSettings)
  .settings(name := "spark-ml-naive-bayes")
  .dependsOn(common)
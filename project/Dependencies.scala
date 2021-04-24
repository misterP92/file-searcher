import sbt._

object Dependencies {
  val specs2Core = "org.specs2" %% "specs2-core" % "4.10.5" % Test
  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.7" % Test
  val scalaMock = "org.scalamock" %% "scalamock" % "4.4.0" % Test
}

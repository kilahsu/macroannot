import scala.language.postfixOps

import sbt._
import Keys._


object TestBuild extends Build {

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    version       := "14.5",
    organization  := "com.companyname",
    scalaVersion  := "2.10.3"
  )

  lazy val top = Project(
    id       = "top",
    base     = file("."),
    settings = sharedSettings
  ) aggregate(tests)

  lazy val tests = Project(
    id       = "tests",
    base     = file("tests"),
    settings = testsProjectSettings
  ) 

  lazy val sharedSettings =
    buildSettings ++
    Seq(
      libraryDependencies ++= Dependencies.shared
    )

  lazy val testsProjectSettings =
    sharedSettings ++ Seq(
      name := "tests",
      libraryDependencies ++= Dependencies.test,
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full)
    )
}

object Dependencies {

  private val akkaVersion = "2.3.1"

  object Compile {
    val quasiquotes     = "org.scalamacros"            %% "quasiquotes"                   % "2.0.0"
  }
  import Compile._

  object Test {
    val scalaTest       = "org.scalatest"              %% "scalatest"                     % "2.0" % "test"
  }
  import Test._

  val shared          = Seq(quasiquotes)
  val test            = Seq(scalaTest)
}


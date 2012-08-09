import sbt._
import Keys._

// for more details, see http://jmhofer.johoop.de/?p=292
// put your Scalatron.jar into lib/

object Build extends Build {
  val botDirectory = SettingKey[File]("bot-directory")
  val play = TaskKey[Unit]("play")

  val bot = Project(
    id = "mybot", 
    base = file("."), 
    settings = Project.defaultSettings ++ botSettings)
      
  val botSettings = Seq[Setting[_]](
      organization := "de.johoop",
      name := "scalatron-bot",
      version := "1.0.0-SNAPSHOT",

      scalaVersion := "2.9.1",
      scalacOptions ++= Seq("-deprecation", "-unchecked"),

      javaOptions += "-Xmx1g",

      libraryDependencies ++= Seq(
        "org.specs2" %% "specs2" % "1.8.2" % "test",
        "org.pegdown" % "pegdown" % "1.0.2" % "test",
        "junit" % "junit" % "4.7" % "test"),
      
      testOptions := Seq(
      	Tests.Filter(_ == "de.johoop.scalatron.BotSpec"), 
      	Tests.Argument("html", "console")),

      testOptions <+= crossTarget map { ct =>
        Tests.Setup { () => 
          System.setProperty("specs2.outDir", new File(ct, "specs2").getAbsolutePath)
        }
      },

      botDirectory := file("bots"),

      play <<= (botDirectory, name, javaOptions, unmanagedClasspath in Compile, Keys.`package` in Compile) map { (bots, name, javaOptions, ucp, botJar) =>
        IO createDirectory (bots / name)
        IO copyFile (botJar, bots / name / "ScalatronBot.jar")

        val cmd = "java %s -cp %s scalatron.main.Main -plugins %s" format (
            javaOptions mkString " ",
            Seq(ucp.files.head, botJar).absString,
            bots.absolutePath)
        cmd run
      }
    )
}

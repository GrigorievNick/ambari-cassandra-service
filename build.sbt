import com.typesafe.sbt.packager.MappingsHelper._

name := "ambari-cassandra-service"

version := "3.9"

scalaVersion := "2.12.1"

val buildVersion = version + "-build-" =sys.env.getOrElse("BUILD_NUMBER", "local")

val metaInfoConfig: String = "metainfo.xml"

lazy val `ambari-cassandra-service` = project
  .in(file("."))
  .enablePlugins(UniversalPlugin)
  .settings(
    topLevelDirectory := Some("CASSANDRA"),
    mappings in(Universal, packageBin) ++= directory("configuration"),
    mappings in(Universal, packageBin) ++= directory("package"),
    mappings in(Universal, packageBin) += {
      val content =
        IO.read(file(metaInfoConfig)).replaceAll("<version>.*</version>", s"<version>$buildVersion</version>")
      val templateFile = file(target.value.getAbsolutePath + "/" + metaInfoConfig)
      IO.write(templateFile, content)
      templateFile
    } -> metaInfoConfig
  )
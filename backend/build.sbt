import sbt.Keys.{initialize, scalacOptions}

lazy val akkaHttpVersion = "10.0.11"
lazy val akkaVersion    = "2.5.9"

parallelExecution in Test := false

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.12.4"
    )),

    name := "be",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"                   %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka"                   %% "akka-http-xml"            % akkaHttpVersion,
      "com.typesafe.akka"                   %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka"                   %% "akka-stream"              % akkaVersion,
      "com.typesafe.akka"                   %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "org.scalatest"                       %% "scalatest"                % "3.0.5"         % Test,

      "com.softwaremill.akka-http-session"  %% "core"                     % "0.5.4",
      "com.softwaremill.akka-http-session"  %% "jwt"                      % "0.5.4",
      "ch.megard"                           %% "akka-http-cors"           % "0.2.2",

      "com.github.t3hnar"                   %% "scala-bcrypt"             % "3.1",

      "org.scalikejdbc"                     %% "scalikejdbc"              % "3.2.1",
      "org.scalikejdbc"                     %% "scalikejdbc-syntax-support-macro"      % "3.2.1",
      "org.scalikejdbc"                     %% "scalikejdbc-config"       % "3.2.1",
      "org.scalikejdbc"                     %% "scalikejdbc-test"         % "3.2.1",
      "org.postgresql"                      % "postgresql"                % "42.2.1",

      "org.sangria-graphql"                 %% "sangria"                  % "1.3.3",
      "org.sangria-graphql"                 %% "sangria-spray-json"       % "1.0.1",

      "org.slf4j"                           % "slf4j-api"                 % "1.7.25",
      "ch.qos.logback"                      % "logback-classic"           % "1.2.3",
      "com.typesafe.scala-logging"          %% "scala-logging"            % "3.7.2"
    ),

    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    scalacOptions := Seq("-target:jvm-1.8"),

    initialize := {
    val _ = initialize.value
    if (sys.props("java.specification.version") != "1.8")
      sys.error("Java 8 is required for this project.")
    }
  )
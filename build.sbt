val Http4sVersion = "0.21.25"
val CirceVersion = "0.13.0"
val MunitVersion = "0.7.27"
val LogbackVersion = "1.2.5"
val MunitCatsEffectVersion = "1.0.5"
val CirceGenericExVersion = "0.14.1"
val CirceConfigVersion = "0.8.0"

val PureConfigVersion = "0.12.3"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    organization := "com.example",
    name := "first",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "io.circe" %% "circe-generic-extras" % CirceGenericExVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-config" % CirceConfigVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect-2" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "org.scalameta"   %% "svm-subs"            % "20.2.0",
      "org.tpolecat" %% "doobie-core" % "0.12.1",
      "org.flywaydb" % "flyway-core" % "7.13.0",
      "com.github.pureconfig" %% "pureconfig"             % PureConfigVersion,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % PureConfigVersion,



      // And add any of these as needed

      "org.tpolecat" %% "doobie-h2"        % "0.12.1",          // H2 driver 1.4.200 + type mappings.
      "org.tpolecat" %% "doobie-hikari"    % "0.12.1",          // HikariCP transactor.
      "org.tpolecat" %% "doobie-postgres"  % "0.12.1",          // Postgres driver 42.2.19 + type mappings.
      "org.tpolecat" %% "doobie-quill"     % "0.12.1",          // Support for Quill 3.6.1
      "org.tpolecat" %% "doobie-specs2"    % "0.12.1" % "test", // Specs2 support for typechecking statements.
      "org.tpolecat" %% "doobie-scalatest" % "0.12.1" % "test",
      "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",// ScalaTest support for typechecking statements.
      "com.github.t3hnar" %% "scala-bcrypt" % "4.1",

    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )

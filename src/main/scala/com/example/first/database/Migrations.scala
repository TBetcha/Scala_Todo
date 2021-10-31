package com.example.first.database

import cats.effect.Sync
import org.flywaydb.core.api.FlywayException
import org.flywaydb.core.api.output.MigrateResult

object Migrations {
  def migrate[F[_]: Sync](): fs2.Stream[F, MigrateResult] = {
    import org.flywaydb.core.Flyway
    // Create the Flyway instance and point it to the database
    val flyway: Flyway = Flyway.configure.dataSource(
      "jdbc:postgresql://localhost:5432/firstscala",
      "postgres",
      ""
    ).load

    // Start the migration
    fs2.Stream.eval {
      try {
        Sync[F].delay(flyway.migrate())
      } catch {
        case e: FlywayException => Sync[F].raiseError(e)
      }
    }
  }
}

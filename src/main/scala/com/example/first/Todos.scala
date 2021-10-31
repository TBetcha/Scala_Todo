package com.example.first

import java.util.UUID

import cats.effect.Sync
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

object Todos  {

  case class Todo(id: Option[Long] = None, name: String, completed: Boolean, userId: UUID)
  case class ErrorMessage(message: String)

  object Todo {
    implicit val todoDecoder: Decoder[Todo] = deriveDecoder[Todo]
    implicit val todoEncoder: Encoder[Todo] = deriveEncoder[Todo]

    implicit def todoEntityDecoder[F[_]: Sync]: EntityDecoder[F, Todo] = jsonOf

//    implicit def todoEntityEncoder[F[_]: Sync]: EntityEncoder[F, Todo] = jsonEncoderOf
  }
  object ErrorMessage {
    implicit val todoDecoder: Decoder[ErrorMessage] = deriveDecoder[ErrorMessage]
    implicit val todoEncoder: Encoder[ErrorMessage] = deriveEncoder[ErrorMessage]

    implicit def todoEntityDecoder[F[_]: Sync]: EntityDecoder[F, ErrorMessage] = jsonOf

//   implicit def todoEntityEncoder[F[_]: Sync]: EntityEncoder[F, ErrorMessage] = jsonEncoderOf
  }
}

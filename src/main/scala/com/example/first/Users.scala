package com.example.first

import java.util.UUID

import cats.effect.Sync
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

object Users {

  case class User(id: Option[UUID] = None, email:String, password: String )
  case class ErrorMessage(message: String)

  object User {
    implicit val userDecoder: Decoder[User] = deriveDecoder[User]
    implicit val userEncoder: Encoder[User] = deriveEncoder[User]

    implicit def userEntityDecoder[F[_]: Sync]: EntityDecoder[F, User] = jsonOf
  }

}

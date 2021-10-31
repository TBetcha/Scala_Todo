package com.example.first.repository

import java.util.UUID

import cats.effect.IO
import com.example.first.Users
import com.example.first.Users.User
import com.github.t3hnar.bcrypt
import doobie.implicits._
import doobie.util.Read
import doobie.util.meta.Meta
import doobie.util.transactor.Transactor


class UserRepository(transactor: Transactor[IO]) {

  implicit val uuidMeta: Meta[UUID] = Meta[String].imap[UUID](UUID.fromString)(_.toString)

  def checkPassword(pw: String, hashPw: String): String = {
    val password = bcrypt.BCryptStrOps(pw)
    val res = if (password.isBcrypted(hashPw)) {
      "Success"
    } else {
      "Invalid Credentials"
    }
    res
  }

  def hashPassword(pw: String): String = {
    val passw = bcrypt.BCryptStrOps(pw)
    passw.bcrypt(pw)
  }

  def createUser(user: Users.User)(implicit read: Read[UUID]): IO[User] = {
    sql"INSERT INTO firstscala_users (email, password) values (${user.email}, hashPassword(${user.password}))".update
      .withUniqueGeneratedKeys[UUID]("id")
      .transact(transactor).map { id => user.copy(id = Some(id)) }
  }

  def userLogin(user: Users.User): IO[Option[User]] = {
    sql""" SELECT
           *
         FROM firstscala_users
         WHERE 1=1
         AND email = ${user.email} """.query[User].option.transact(transactor)
  }
}


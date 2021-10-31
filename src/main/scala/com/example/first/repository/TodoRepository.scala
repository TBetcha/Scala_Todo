package com.example.first.repository

import java.util.UUID

import cats.effect.IO
import com.example.first.Todos
import com.example.first.Todos.{ErrorMessage, Todo}
import doobie.implicits._
import doobie.util.meta.Meta
import doobie.util.transactor.Transactor

class TodoRepository(transactor: Transactor[IO]) {

  implicit val uuidMeta: Meta[UUID] = Meta[String].imap[UUID](UUID.fromString)(_.toString)

  //fix this to deal with an option of user id of fix user id to have a value, which is prob what will happen
  def findById(id: Long): Option[Todos.Todo] = {
    sql"""
         SELECT *
         FROM firstscala
         WHERE 1=1
         AND id = $id""".query[Todo].option.transact(transactor).unsafeRunSync()
  }

  def createTodo(todo: Todos.Todo): IO[Todo] = {
    sql"INSERT INTO firstscala(name, completed) VALUES (${todo.name}, ${todo.completed})".update
      .withUniqueGeneratedKeys[Long]("id")
      .transact(transactor).map { id => todo.copy(id = Some(id))
    }
  }


  def updateTodo(id: Long, todo: Todo): IO[Either[ErrorMessage.type, Todo]] = {
    sql"UPDATE firstscala SET completed = ${todo.completed}, name = ${todo.name} WHERE id = $id".update.run
      .transact(transactor).map { affectedRows =>
      if (affectedRows == 1) {
        Right(todo.copy(id = Some(id)))
      } else {
        Left(ErrorMessage)
      }
    }
  }
}





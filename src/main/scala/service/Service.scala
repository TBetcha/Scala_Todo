package service

import java.util.UUID

import cats.effect.IO
import cats.syntax.semigroupk._
import com.example.first.Todos.Todo.{todoDecoder, todoEncoder}
import com.example.first.Todos.{ErrorMessage, Todo}
import com.example.first.Users.User
import com.example.first.Users.User.userDecoder
import com.example.first.repository.{TodoRepository, UserRepository}
import doobie.util.meta.Meta
import io.circe.syntax.EncoderOps
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Location
import org.http4s.{HttpRoutes, Uri}

class Service(todoRepository: TodoRepository, userRepository: UserRepository) extends Http4sDsl[IO] {

  def allRoutes: HttpRoutes[IO] = {
    todoRoutes <+> userRoutes
  }

  val todoRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> Root / "todos" =>
      for {
        todo <- req.decodeJson[Todo]
        createdTodo <- todoRepository.createTodo(todo)
        response <- Created(createdTodo.asJson, Location(Uri.unsafeFromString(s"/todos/${createdTodo.id.get}")))
      } yield response
    case GET -> Root / "todos" / LongVar(id) =>
      todoRepository.findById(id) match {
        case Some(todo) => Ok(todo.asJson)
        case _ => BadRequest(ErrorMessage(s" The todo with id ${id} cannot be found"))
      }
    case req@PUT -> Root / "todos" / LongVar(id) =>
      for {
        todo <- req.decodeJson[Todo]
        updated <- todoRepository.updateTodo(id, todo)
        resp <- todoResult(updated)
      } yield resp
  }

  private def todoResult(result: Either[ErrorMessage.type, Todo]) = {
    result match {
      case Left(ErrorMessage) => BadRequest(ErrorMessage("Could not process this request"))
      case Right(todo) => Ok(todo.asJson)
    }
  }

  implicit val uuidMeta: Meta[UUID] = Meta[String].imap[UUID](UUID.fromString)(_.toString)
  val userRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> Root / "users" =>
      for {
        user <- req.decodeJson[User]
        createdUser <- userRepository.createUser(user)
        resp <- Created(createdUser.asJson, Location(Uri.unsafeFromString(s"/users/${createdUser.id.get}")))
      } yield resp

    case req@GET -> Root / "users" / "login"  =>
      for {
       sentUser <- req.decodeJson[User]
        res <- userRepository.userLogin(sentUser)
        resp <- loginResult(res,sentUser)
      } yield resp
  }

  private def loginResult(res: Option[User], sentUser:User) = {
    val user = res.get
    val creds = userRepository.checkPassword(sentUser.password, user.password)
    creds match {
      case "Success" => Ok("Success".asJson)
      case _ => BadRequest(ErrorMessage("Invalid Credentials"))
    }
  }
}











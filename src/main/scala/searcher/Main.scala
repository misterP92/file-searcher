package searcher

import searcher.file.Index
import scala.util.{Failure, Success}

object Main extends App {
  println(s"Hej, args: ${args.mkString("Array(", ", ", ")")}")
  Program.readFile(args).map(Index(_)) match {
    case Right(Success(index)) =>  Program.start(index)
    case Right(Failure(exception)) => println(exception)
    case Left(error) => println(error)
  }
}

package searcher

import searcher.file.IndexedFiles
import scala.util.{Failure, Success}

object Main extends App {
  Program.readFile(args).map(IndexedFiles(_)) match {
    case Right(Success(index)) =>  Program.start(index)
    case Right(Failure(exception)) => println(exception)
    case Left(error) => println(error)
  }
}

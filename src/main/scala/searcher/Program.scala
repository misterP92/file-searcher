package searcher

import searcher.errorHandling.FileReaderError
import searcher.errorHandling.FileReaderError._
import searcher.file.{Index, Sentence}

import java.io.File
import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.Try

object Program {
  private val SpaceDelimiter: String = " "

  def readFile(args: Array[String]): Either[FileReaderError, File] = {
    for {
      path <- args.headOption.toRight(MissingPath)
      file <- Try(new java.io.File(path))
        .fold(
          throwable => Left(FileNotFound(throwable)),
          file => if (file.isDirectory) Right(file) else Left(NotADirectory(s"Path [$path] is not a directory")))
    } yield file
  }

  @tailrec
  def start(indexedFiles: Index): Unit = {
    print(s"search> ")
    val searchString = readLine()
    if (searchString.equals(":quit")) {
      println("Quiting the application")
    } else {
      val sentence = Sentence.fromStrings(searchString.split(SpaceDelimiter).toList)
      val result = indexedFiles.searchForWordInFiles(sentence).limitToTen
      result.prettyPrint()
      start(indexedFiles)
    }
  }
}

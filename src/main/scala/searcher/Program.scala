package searcher

import searcher.errorHandling.FileReaderError
import searcher.errorHandling.FileReaderError._
import searcher.file.IndexedFiles.searchForWords
import searcher.file.{FileResult, IndexedFiles, Words}

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

  def start(indexedFiles: IndexedFiles): Unit = {
    innerStart(indexedFiles)(searchForWords)
  }

  @tailrec
  def innerStart(indexedFiles: IndexedFiles)(searcher: (IndexedFiles, Words, Seq[FileResult] => Seq[FileResult]) => IndexedFiles): Unit = {
    print(s"search> ")
    readLine() match {
      case ":quit" => println("Quiting the application")
      case ":clear" => innerStart(indexedFiles.withClearedMemory)(searcher)
      case searchString =>
        val words = Words.fromStrings(searchString.split(SpaceDelimiter))
        println(s"Will be searching for: ${words.asString}")
        val result = searcher(indexedFiles, words, limitResultFiles)
        result.latestResult.prettyPrint()
        innerStart(result)(searcher)
    }
  }

  private def limitResultFiles: Seq[FileResult] => Seq[FileResult] = {
    case listOfFiles if listOfFiles.length > SizeAtMost => listOfFiles.sortBy(_.calcPercentage).tail
    case listOfFiles => listOfFiles
  }
}



















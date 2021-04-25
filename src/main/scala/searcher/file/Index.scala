package searcher.file

import searcher.errorHandling.IndexingError.FileContentExtractionError

import java.io.File
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object Index {
  def apply(directoryPath: File): Try[Index] = {
    Try(directoryPath.listFiles().map(FileRepresentation(_) match {
      case Success(value) => value
      case Failure(exception) =>
        println(exception.getMessage, exception)
        throw FileContentExtractionError(exception)
    })).map(_.toList).map { listOfFiles =>
      println(s"Loaded in ${listOfFiles.length} amount of files.")
      listOfFiles
    }.map(new Index(_))
  }
}

final case class Index(files: List[FileRepresentation]) {

  def searchForWordInFiles(searchSentence: Sentence): Seq[FileDefinition] = searchFromFiles(searchSentence.words)

  private def searchFromFiles(words: List[Word]): Seq[FileDefinition] = {
    @tailrec
    def innerSearch(filesToCheck: List[FileRepresentation], result: Seq[FileDefinition]): Seq[FileDefinition] = filesToCheck match {
      case Nil => result
      case _ :: _ if result.reachedAllowedCapacity => result
      case head :: tail =>
        val updatedWords = searchWordsInOneFile(words, head)
        val resultForFile = FileDefinition(head.path, updatedWords)
        innerSearch(tail, resultForFile +: result)
    }

    innerSearch(files, Seq.empty)
  }

  private def searchWordsInOneFile(words: List[Word], file: FileRepresentation): List[Word] = {
    @tailrec
    def innerSearch(words: List[Word], file: FileRepresentation, result: List[Word]): List[Word] = words match {
      case Nil => result
      case head :: tail if file.containsWord(head.wordName) =>
        innerSearch(tail, file, head.withFound +: result)
      case head :: tail => innerSearch(tail, file, head +: result)
    }
    innerSearch(words, file, List.empty)
  }
}

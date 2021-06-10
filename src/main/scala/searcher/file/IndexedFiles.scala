package searcher.file

import searcher.errorHandling.IndexingError.FileContentExtractionError
import searcher.file.Words.Word

import java.io.File
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object IndexedFiles {
  def apply(directoryPath: File): Try[IndexedFiles] = {
    Try(directoryPath.listFiles().map(parseFile(toFileRep))).map(toListPrintResult).map(new IndexedFiles(_))
  }

  private def parseFile(fileToFileRep: File => Try[FileRepresentation]): File => FileRepresentation = fileToFileRep(_) match {
    case Success(value) => value
    case Failure(exception) =>
      println(exception.getMessage, exception)
      throw FileContentExtractionError(exception)
  }

  private def toFileRep: File => Try[FileRepresentation] = FileRepresentation(_)

  private def toListPrintResult: Array[FileRepresentation] => List[FileRepresentation] =
    listOfFiles => { println(s"Loaded in ${listOfFiles.length} amount of files."); listOfFiles.toList }

  def searchForWords(indexedFiles: IndexedFiles, words: Words, resultLimiter: Seq[FileResult] => Seq[FileResult]): IndexedFiles = {
    @tailrec
    def innerSearch(filesToCheck: List[FileRepresentation], result: Seq[FileResult], updatedFiles: List[FileRepresentation]): IndexedFiles = filesToCheck match {
      case Nil => IndexedFiles(updatedFiles, result)
      case _ :: _ if result.reachedAllowedCapacity => IndexedFiles(updatedFiles, result)
      case headFile :: tail =>
        searchWordsInOneFile(words, headFile) match {
          case Nil => innerSearch(tail, result, headFile +: updatedFiles)
          case wordsInFile =>
            val fileResult = FileResult(headFile.path, wordsInFile.length, words.wordsLength)
            innerSearch(tail, resultLimiter(fileResult +: result), headFile.withNewWords(wordsInFile) +: updatedFiles)
        }
    }

    innerSearch(indexedFiles.files, List.empty, List.empty).reverseLatestRun
  }

  private def searchWordsInOneFile(words: Words, file: FileRepresentation): List[Word] = {
    @tailrec
    def innerSearch(words: List[Word], result: List[Word]): List[Word] = words match {
      case Nil => result
      case head :: tail if file.containsWord(head) => innerSearch(tail, head +: result)
      case _ :: tail => innerSearch(tail, result)
    }
    innerSearch(words.words, List.empty)
  }
}

final case class IndexedFiles(files: List[FileRepresentation], latestResult: Seq[FileResult] = Seq.empty) {
  def reverseLatestRun: IndexedFiles = this.copy(latestResult = latestResult.reverse)
  def withClearedMemory: IndexedFiles = this.copy(files = files.map(_.clearMemory))
}

package searcher.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.io.File

object IndexedFilesSuite {
  private val ListOfWords: Array[String] = Array("hej", "my", "name", "is", "slim", "shady")
  private val DefaultSentence: Words = Words.fromStrings(ListOfWords)
  private val DefaultPathToFiles: String = "src/test/resources/textFiles"
}

class IndexedFilesSuite extends AnyFunSuiteLike with MockFactory {
  import IndexedFilesSuite._

  private def setUpIndexAndRun(searchString: Words): Seq[FileResult] = {
    val pathToFileStr = new File(DefaultPathToFiles)
    val index = IndexedFiles(pathToFileStr).toOption.getOrElse(fail("Could not read files in directory"))
    IndexedFiles.searchForWords(index, searchString, {
      case listOfFiles if listOfFiles.length > 10 => listOfFiles.sortBy(_.calcPercentage).tail
      case listOfFiles => listOfFiles
    }).latestResult
  }

  test("Was able to produce result for words") {
    val expectedList: List[String] = List("50 %", "17 %")
    val result = setUpIndexAndRun(DefaultSentence)
    result.size shouldBe 2
    result.zip(expectedList).map { case (actual, expected) =>
      actual.prettyCalcPercentage shouldBe expected
    }
  }

  test("No words were provided") {
    val expectedList: List[String] = List("0 %", "0 %")
    val result = setUpIndexAndRun(Words(List.empty))
    result.size shouldBe 2
    result.zip(expectedList).map { case (actual, expected) =>
      actual.prettyCalcPercentage shouldBe expected
    }
  }
}

package searcher.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.io.File

object IndexSuite {
  private val ListOfWords: List[String] = List("hej", "my", "name", "is", "slim", "shady")
  private val DefaultSentence: Sentence = Sentence.fromStrings(ListOfWords)
  private val DefaultPathToFiles: String = "src/test/resources/textFiles"
}

class IndexSuite extends AnyFunSuiteLike with MockFactory {
  import IndexSuite._

  private def setUpIndexAndRun(searchString: Sentence): Seq[FileDefinition] = {
    val pathToFileStr = new File(DefaultPathToFiles)
    val index = Index(pathToFileStr).toOption.getOrElse(fail("Could not read files in directory"))
    index.searchForWordInFiles(searchString)
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
    val result = setUpIndexAndRun(Sentence(List.empty))
    result.size shouldBe 2
    result.zip(expectedList).map { case (actual, expected) =>
      actual.prettyCalcPercentage shouldBe expected
    }
  }
}

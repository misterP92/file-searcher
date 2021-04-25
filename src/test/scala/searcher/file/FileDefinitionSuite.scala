package searcher.file

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.TableDrivenPropertyChecks
import searcher.file.FileDefinition.PercentageFound
import searcher.file.FileRepresentation.FilePath

object FileDefinitionSuite {
  private val PathForFile: String = "path/to/some/file/file.txt"
  private val DefaultWords: List[Word] = List(Word("hej"), Word("this").withFound, Word("is").withFound, Word("ok"))
}

class FileDefinitionSuite extends AnyFunSuiteLike with TableDrivenPropertyChecks {
  import FileDefinitionSuite._

  test("Table testing for PercentageFound") {
    val tables = Table(
      ("Name", "InputWords", "ExpectedPercentage"),
      ("List is 50 %", DefaultWords, 0.50),
      ("Empty list is 0 %", List.empty, 0.0),
      ("List is 100 %", List(Word("hej").withFound, Word("this").withFound, Word("is").withFound, Word("ok").withFound), 1.0),
      ("List Is 25 %", List(Word("hej"), Word("this"), Word("is"), Word("ok").withFound), 0.25),
      ("List with words is 0 %", List(Word("hej"), Word("this"), Word("is"), Word("ok")), 0.0)
    )

    forAll(tables) { (name: String, words: List[Word], expectedValue: Double) =>
      println(name)
      val percentage = PercentageFound(words)
      percentage.calcPercentage shouldBe expectedValue
    }
  }

  test("Test that FileDefinition apply function works") {
    val file = FileDefinition(FilePath(PathForFile), DefaultWords)
    file.fileName.path shouldBe PathForFile
    file.fileName.getOnlyName shouldBe "file.txt"
    file.percentage.calcPercentage shouldBe 0.50
    file.prettyCalcPercentage shouldBe "50 %"
  }

  test("Test that FileDefinition with no words works") {
    val file = FileDefinition(FilePath(PathForFile), List.empty)
    file.fileName.path shouldBe PathForFile
    file.fileName.getOnlyName shouldBe "file.txt"
    file.percentage.calcPercentage shouldBe 0.0
    file.prettyCalcPercentage shouldBe "0 %"
  }

  test("Test that wrong FilePath returns it self") {
    val file = FileDefinition(FilePath("wrongpath....hej.txt"), List.empty)
    file.fileName.path shouldBe "wrongpath....hej.txt"
    file.fileName.getOnlyName shouldBe "wrongpath....hej.txt"
    file.percentage.calcPercentage shouldBe 0.0
    file.prettyCalcPercentage shouldBe "0 %"
  }

  test("Test that FileDefinition class works") {
    val file = new FileDefinition(FilePath(PathForFile), PercentageFound(FileDefinition.Calc(4, 2)))
    file.fileName.path shouldBe PathForFile
    file.fileName.getOnlyName shouldBe "file.txt"
    file.percentage.calcPercentage shouldBe 0.50
    file.prettyCalcPercentage shouldBe "50 %"
  }

}

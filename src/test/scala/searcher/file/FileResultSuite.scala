package searcher.file

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.TableDrivenPropertyChecks
import searcher.file.FileRepresentation.FilePath
import searcher.file.FileResult.WordDifferentiate

object FileResultSuite {
  private val PathForFile: String = "path/to/some/file/file.txt"
  private val DefaultWords: (Int, Int) = (2, 4)
}

class FileResultSuite extends AnyFunSuiteLike with TableDrivenPropertyChecks {
  import FileResultSuite._

  test("Table testing for PercentageFound") {
    val tables = Table(
      ("Name", "InputWords", "ExpectedPercentage"),
      ("List is 50 %", DefaultWords, 0.50),
      ("Empty list is 0 %", (0, 0), 0.0),
      ("List is 100 %", (5, 5), 1.0),
      ("List Is 25 %", (1, 4), 0.25),
      ("List with words is 0 %", (0, 4), 0.0)
    )

    forAll(tables) { case (name: String, (found: Int, existing: Int), expectedValue: Double) =>
      println(name)
      val percentage = WordDifferentiate(found, existing)
      percentage.calcPercentage shouldBe expectedValue
    }
  }

  test("Test that FileDefinition apply function works") {
    val file = FileResult(FilePath(PathForFile), DefaultWords._1, DefaultWords._2)
    file.fileName.path shouldBe PathForFile
    file.fileName.getOnlyName shouldBe "file.txt"
    file.percentage.calcPercentage shouldBe 0.50
    file.prettyCalcPercentage shouldBe "50 %"
  }

  test("Test that FileDefinition with no words works") {
    val file = FileResult(FilePath(PathForFile), 0, 0)
    file.fileName.path shouldBe PathForFile
    file.fileName.getOnlyName shouldBe "file.txt"
    file.percentage.calcPercentage shouldBe 0.0
    file.prettyCalcPercentage shouldBe "0 %"
  }

  test("Test that wrong FilePath returns it self") {
    val file = FileResult(FilePath("wrongpath....hej.txt"), 0, 0)
    file.fileName.path shouldBe "wrongpath....hej.txt"
    file.fileName.getOnlyName shouldBe "wrongpath....hej.txt"
    file.percentage.calcPercentage shouldBe 0.0
    file.prettyCalcPercentage shouldBe "0 %"
  }

  test("Test that FileDefinition class works") {
    val file = new FileResult(FilePath(PathForFile), WordDifferentiate(2, 4))
    file.fileName.path shouldBe PathForFile
    file.fileName.getOnlyName shouldBe "file.txt"
    file.percentage.calcPercentage shouldBe 0.50
    file.prettyCalcPercentage shouldBe "50 %"
  }

}

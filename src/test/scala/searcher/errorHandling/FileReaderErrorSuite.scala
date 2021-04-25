package searcher.errorHandling

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class FileReaderErrorSuite extends AnyFunSuiteLike {
  import FileReaderError._

  test("Is MissingPath error") {
    val error: FileReaderError = MissingPath
    error shouldBe MissingPath
  }

  test("Is FileNotFound error") {
    val error: FileReaderError = FileNotFound(new Exception("failed"))
    error match {
      case ex: FileNotFound => ex.underlyingError.getMessage shouldBe "failed"
      case _ => fail("Wrong type")
    }
  }

  test("Is NotADirectory error") {
    val error: FileReaderError = NotADirectory("failed")
    error match {
      case ex: NotADirectory => ex.underlyingMsg shouldBe "failed"
      case _ => fail("Wrong type")
    }
  }
}

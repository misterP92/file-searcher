package searcher.errorHandling

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class IndexingErrorSuite extends AnyFunSuiteLike {
  import IndexingError._

  test("Is FileContentExtractionError error") {
    val error: IndexingError = FileContentExtractionError(new Exception("failed"))
    error match {
      case ex: FileContentExtractionError => ex.underlyingError.getMessage shouldBe "failed"
      case _ => fail("Wrong type")
    }
  }
}

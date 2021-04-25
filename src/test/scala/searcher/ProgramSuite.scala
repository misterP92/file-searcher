package searcher

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import searcher.errorHandling.FileReaderError._

object ProgramSuite {
  private val DefaultDirectory: String = "src/test/resources/textFiles"
  private val DefaultArgs: Array[String] = Array(DefaultDirectory)
}
class ProgramSuite extends AnyFunSuiteLike {
  import ProgramSuite._

  test("Successfully read a directory") {
    Program.readFile(DefaultArgs) match {
      case Right(value) =>
        value.listFiles().length shouldBe 2
      case Left(error) => fail(s"Received an unexpected error: $error")
    }
  }

  test("Fail reading a directory with MissingPath") {
    Program.readFile(Array.empty) match {
      case Left(MissingPath) => ()
      case unexpectedValue => fail(s"Received an unexpected response: $unexpectedValue")
    }
  }

  test("Fail reading a directory with NotADirectory") {
    Program.readFile(Array("src/test/resources/textFiles/203.txt")) match {
      case Left(NotADirectory(msg)) => msg shouldBe "Path [src/test/resources/textFiles/203.txt] is not a directory"
      case unexpectedValue => fail(s"Received an unexpected response: $unexpectedValue")
    }
  }
}

package searcher.file

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.io.File
import scala.util.{Failure, Success, Try}

class FileRepresentationSuite extends AnyFunSuiteLike {

  test("Test that file import works properly") {
    val pathToFileStr = "src/test/resources/textFiles"
    Try(new File(pathToFileStr)).map(_.listFiles().map(FileRepresentation(_))) match {
      case Success(value) =>
        value.collectFirst { case Failure(error) => error } match {
          case None =>
            val listOfValues = value.collect { case Success(file) => file }
            listOfValues.length shouldBe 2
            listOfValues.count(_.containsWord("is")) shouldBe 2
          case Some(error) => fail(s"Failed cause of ${error.getMessage}")
        }
      case Failure(exception) => fail(s"Failed cause of ${exception.getMessage}")
    }
  }

  test("Test that file import fails with wrong path") {
    val pathToFileStr = "random/Path"
    Try(new File(pathToFileStr)).map(_.listFiles().map(FileRepresentation(_))) match {
      case Success(value) => fail(s"Failed cause of ${value.mkString("Array(", ", ", ")")}")
      case Failure(exception) => ()
    }
  }

  test("Test that file import fails with path with nofiles") {
    val pathToFileStr = "src/"
    Try(new File(pathToFileStr)).map(_.listFiles().map(FileRepresentation(_))) match {
      case Success(value) =>
        value.collectFirst { case Failure(error) => error } match {
          case None => fail("Should have received an error")
          case Some(error) => println(s"Failed cause of ${error.getMessage}")
        }
      case Failure(exception) => fail(s"Failed cause of ${exception.getMessage}")
    }
  }
}

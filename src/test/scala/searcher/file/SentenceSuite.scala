package searcher.file

import org.scalatest.funsuite.AnyFunSuiteLike

object SentenceSuite {
  private val ListOfWords: List[String] = List("hej", "my", "name", "is", "what", "manameis", "slim", "shady")
}

class SentenceSuite extends AnyFunSuiteLike {
  import SentenceSuite._

  test("Test that Sentence fromStrings function works ") {
    val sentence = Sentence.fromStrings(ListOfWords)

    sentence.words.map {
      case Word(word, false) if ListOfWords.contains(word) => ()
      case Word(word, _) => fail(s"Word was not found: [$word]")
    }
  }

  test("Test that Sentence class works") {
    val sentence = Sentence(ListOfWords.map(Word(_)))
    sentence.words.map {
      case Word(word, false) if ListOfWords.contains(word) => ()
      case Word(word, _) => fail(s"Word was not found: [$word]")
    }
  }
}

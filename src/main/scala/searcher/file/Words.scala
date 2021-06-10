package searcher.file

import scala.util.matching.Regex

object Words {
  def fromStrings(strings: Array[String]): Words = {
    import Word._
    strings.toList match {
      case ":lazy" :: restOfWords => new Words(restOfWords.filterNot(_.startsWith(":")).map(StringWord))
      case words => new Words(words.filterNot(_.startsWith(":")).map(wordToRegex))
    }
  }

  private def wordToRegex(wordStr: String): Word.RegexWord = Word.RegexWord(prepareRegex(wordStr).r, wordStr)

  private def prepareRegex(wordStr: String): String = s"\\b${ if(wordStr.length < 2) wordStr else {
    wordStr.headOption.map(oneChar => s"[${Character.toUpperCase(oneChar)}${Character.toLowerCase(oneChar)}]").mkString + wordStr.tail
  }}\\b"

  sealed trait Word {
    def underlying: String
  }

  object Word {
    final case class StringWord(value: String) extends Word {
      override def underlying: String = value
    }
    final case class RegexWord(value: Regex, original: String) extends Word {
      override def underlying: String = original
      override def equals(obj: Any): Boolean = obj match {
        case RegexWord(_, org) =>
          println(s"Mine:: $original   onther obj:: $org")
          this.original.equals(org)
        case _ => false
      }
    }
  }
}

final case class Words(words: List[Words.Word]) {
  val wordsLength: Int = words.length
  def asString: String = words.mkString(", ")
}
package searcher.file

object Sentence {
  def fromStrings(strings: List[String]): Sentence = new Sentence(strings.map(Word(_)))
}

final case class Sentence(words: List[Word])

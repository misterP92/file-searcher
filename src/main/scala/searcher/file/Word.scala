package searcher.file

object Word {
  private val NotFound: Boolean = false
  private val Found: Boolean = true
  def apply(wordName: String): Word = new Word(wordName, NotFound)
}

final case class Word(wordName: String, foundIn: Boolean) {
  def withFound: Word = this.copy(foundIn = Word.Found)
}

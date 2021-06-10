package searcher.file

import searcher.file.FileRepresentation.FilePath

object FileResult {

  final case class WordDifferentiate(found: Double, existing: Double) {
    def calcPercentage: Double = if(existing != 0.0) found / existing else 0.0
  }

  object WordDifferentiate {
    def apply(foundWords: Int, definedWords: Int): WordDifferentiate = {
      val toDouble: Double => Double = BigDecimal(_).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
      new WordDifferentiate(toDouble(foundWords), toDouble(definedWords))
    }
  }

  def apply(fileName: FilePath, foundWords: Int, definedWords: Int): FileResult = {
    new FileResult(fileName, WordDifferentiate(foundWords, definedWords))
  }
}

final case class FileResult(fileName: FilePath, percentage: FileResult.WordDifferentiate) {
  def calcPercentage: Double = percentage.calcPercentage
  def prettyCalcPercentage: String = s"${(this.calcPercentage * 100).round} %"
}

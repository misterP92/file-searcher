package searcher.file

import searcher.file.FileRepresentation.FilePath

object FileDefinition {
  private val Found: Boolean = true

  final case class Calc(existing: Int, found: Int) {
    private def toBigDec(toChange: Double): Double = BigDecimal(toChange).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    def calcPercentage: Double = if(existing != 0.0) toBigDec(found) / toBigDec(existing) else 0.0
    def withAddedFound: Calc = copy(found = found + 1)
    def withAddedExisting: Calc = copy(existing = existing + 1)
  }

  final case class PercentageFound(percentage: Calc) {
    def calcPercentage: Double = percentage.calcPercentage
  }

  object PercentageFound {
    def apply(wordsWithFound: List[Word]): PercentageFound = {
      val toCalc = wordsWithFound.foldLeft(Calc(0, 0)) {
        case (x, Word(_, Found)) => x.withAddedFound.withAddedExisting
        case (x, _) => x.withAddedExisting
      }
      new PercentageFound(toCalc)
    }
  }

  def apply(fileName: FilePath, wordsWithFound: List[Word]): FileDefinition = {
    new FileDefinition(fileName, PercentageFound(wordsWithFound))
  }
}

final case class FileDefinition(fileName: FilePath, percentage: FileDefinition.PercentageFound) {
  def calcPercentage: Double = percentage.calcPercentage
  def prettyCalcPercentage: String = s"${(this.calcPercentage * 100).round} %"
}

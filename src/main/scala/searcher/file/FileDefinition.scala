package searcher.file

import searcher.file.FileRepresentation.FilePath

object FileDefinition {
  private val Found: Boolean = true

  final case class Calc(existing: Int, found: Int) {
    def calcPercentage: Double = BigDecimal(found).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble / BigDecimal(existing).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
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
  def prettyCalcPercentage: String = s"${
    val value = this.calcPercentage * 100
    value - (value % 0.01)
  } %"
}

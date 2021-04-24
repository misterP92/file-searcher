import searcher.file.FileDefinition

import scala.annotation.tailrec

package object searcher {
  private val SizeAtMost: Int = 10
  private val NotFound: Double = 0.0

  implicit class FileDefSeqDecorator(underlying: Seq[FileDefinition]) {
    def limitToTen: Seq[FileDefinition] = {
      @tailrec
      def inner(current: List[FileDefinition], most: List[FileDefinition]): List[FileDefinition] = current match {
        case Nil => most
        case head :: tail if most.size == SizeAtMost =>
          val newSetOfFiles = most.headOption
            .flatMap(one => if(head.calcPercentage > one.calcPercentage) Some(head) else None)
            .map(_ +: most.tail)
            .map(toReAdd => toReAdd.sortBy(_.calcPercentage).reverse)
            .getOrElse(most)
          inner(tail, newSetOfFiles)
        case head :: tail if head.calcPercentage == NotFound => inner(tail, most)
        case head :: tail => inner(tail, (head +: most).sortBy(_.calcPercentage).reverse)
      }

      inner(underlying.toList, List.empty)
    }

    def prettyPrint(): Unit = if(underlying.isEmpty) println("No matches found") else {
      underlying.foreach { one =>
        println(s"File name: ${one.fileName.getOnlyName} : ${one.prettyCalcPercentage}")
      }
    }
  }
}

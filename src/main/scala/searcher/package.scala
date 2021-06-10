import searcher.file.FileResult

package object searcher {
  val SizeAtMost: Int = 10

  implicit class FileResSeqRich(underlying: Seq[FileResult]) {

    def prettyPrint(): Unit = if(underlying.isEmpty) println("No matches found") else {
      underlying.foreach { one =>
        println(s"File name: ${one.fileName.getOnlyName} : ${one.prettyCalcPercentage}")
      }
    }

    def reachedAllowedCapacity: Boolean = underlying.count(_.calcPercentage == 1.0) == SizeAtMost
  }
}

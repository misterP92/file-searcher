package searcher.file

import java.io.File
import scala.annotation.tailrec
import scala.io.Codec
import scala.io.Codec._
import scala.util.{Failure, Try}

object FileRepresentation {
  private val Delimiter: String = "/"
  final case class FilePath(path: String) {
    def getOnlyName: String = {
      path.split(Delimiter).toList.reverse.headOption.getOrElse(path)
    }
  }

  private val CodecList: Seq[Codec] = Seq(UTF8, ISO8859, fileEncodingCodec, default)

  def apply(file: File): Try[FileRepresentation] = {
    tryEncodings(file, CodecList).map(new FileRepresentation(FilePath(file.getPath), _))
  }

  @tailrec
  private def tryEncodings(file: File, codec: Seq[Codec], error: Option[Throwable] = None): Try[String] = codec match {
    case Nil => error.map(Failure(_)).getOrElse(Failure(new Exception("No codec worked")))
    case headCodec :: tail =>
      val source = getContent(file, headCodec)
      if(source.isSuccess) source else tryEncodings(file, tail, source.failed.toOption)
  }

  private def getContent(file: File, codec: Codec): Try[String] = Try {
    val fileSource = scala.io.Source.fromFile(file)(codec)
    val fileContent = fileSource.mkString
    fileSource.close()
    fileContent
  }
}

final case class FileRepresentation(path: FileRepresentation.FilePath, content: String) {
  def containsWord(word: String): Boolean = content.contains(word)
}

package searcher.file

import searcher.file.Words.Word.{RegexWord, StringWord}

import java.io.File
import scala.io.Codec
import scala.io.Codec._
import scala.util.{Failure, Try}

object FileRepresentation {
  private val CodecList: Seq[Codec] = Seq(UTF8, ISO8859)
  private val Delimiter: String = "/"
  final case class FilePath(path: String) {
    def getOnlyName: String = path.split(Delimiter).toList.reverse.headOption.getOrElse(path)
  }

  final case class FileContent(content: String, memorizedWords: Set[Words.Word]) {
    def containsWord(word: Words.Word): Boolean = {
      if(memorizedWords(word)) {
        println(s"Found a memorized word: $word")
        true
      } else word match {
        case RegexWord(value, _) => value.findFirstIn(content).nonEmpty
        case StringWord(value) => content.contains(value)
      }
    }

    def clearMemory: FileContent = this.copy(memorizedWords = Set.empty)
    def withNewWords(words: List[Words.Word]): FileContent = this.copy(memorizedWords = memorizedWords ++ words)
  }

  def apply(file: File): Try[FileRepresentation] = tryEncodings(file, getContent, toFileRep)(CodecList)

  private def tryEncodings(file: File, parse: (File, Codec) => Try[String], toClass: (File, String) => FileRepresentation): Seq[Codec] => Try[FileRepresentation] = {
    case Nil => Failure(new Exception("No codec worked"))
    case headCodec :: tail =>
      val source = parse(file, headCodec).map(toClass(file, _))
      if(source.isSuccess) source else tryEncodings(file, parse, toClass)(tail)
  }

  private def toFileRep(file: File, content: String): FileRepresentation = new FileRepresentation(FilePath(file.getPath), FileContent(content, Set.empty))

  private def getContent(file: File, codec: Codec): Try[String] = Try {
    val fileSource = scala.io.Source.fromFile(file)(codec)
    val fileContent = fileSource.mkString
    fileSource.close()
    fileContent
  }
}

final case class FileRepresentation(path: FileRepresentation.FilePath, content: FileRepresentation.FileContent) {
  def containsWord(word: Words.Word): Boolean = content.containsWord(word)
  def clearMemory: FileRepresentation = copy(content = content.clearMemory)
  def withNewWords(words: List[Words.Word]): FileRepresentation = this.copy(content = content.withNewWords(words))
}

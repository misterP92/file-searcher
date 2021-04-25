package searcher.errorHandling

sealed trait FileReaderError


object FileReaderError {
  final case object MissingPath extends FileReaderError
  final case class FileNotFound(underlyingError: Throwable) extends FileReaderError
  final case class NotADirectory(underlyingMsg: String) extends FileReaderError
}

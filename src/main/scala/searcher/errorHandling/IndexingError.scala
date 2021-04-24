package searcher.errorHandling

sealed trait IndexingError extends Exception

object IndexingError {
  final case class FileContentExtractionError(error: Throwable) extends IndexingError
  final case object PathFailure extends IndexingError
}

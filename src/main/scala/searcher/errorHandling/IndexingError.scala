package searcher.errorHandling

sealed trait IndexingError extends Exception

object IndexingError {
  final case class FileContentExtractionError(underlyingError: Throwable) extends IndexingError
}

package edu.furman.fufolio

package pluperfectutils {

  case class CiteException(message: String = "", cause: Option[Throwable] = None) extends Exception(message) {
    cause.foreach(initCause)
  }

}

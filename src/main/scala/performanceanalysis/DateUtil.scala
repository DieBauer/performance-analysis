package performanceanalysis

import java.time.LocalDateTime

import scala.util.matching.Regex.Match
import scala.util.{Failure, Success, Try}

object DateUtil {

  private val regex = """(\d{1,2}|\d{4})[ -/\.](\d{1,2})[ -/\.](\d{1,2}|\d{4})[ T]( \d|\d{2})[:.](\d{2})[.:](\d{2})(\.\d{0,9})?""".r

  private def parseNano(s: String): Int = {
    // note that an optional group will be null when not supplied
    Try(Option(s)).toOption.flatten match {
      case None => 0
      case Some(".") => 0
      case Some(s) => (s.toDouble * 1000000000).toInt
    }
  }

  private def dateParser(iYear: Int, iMonth: Int, iDay: Int): String => Option[LocalDateTime] = {
    (s: String) => Try {
      val dtr: Option[Match] = regex.findFirstMatchIn(s)
      dtr.map { m =>
        val year = m.group(iYear).toInt
        val month = m.group(iMonth).toInt
        val day = m.group(iDay).toInt
        val hour = m.group(4).toInt
        val min = m.group(5).toInt
        val sec = m.group(6).toInt
        val nano = parseNano(m.group(7))
        LocalDateTime.of(year, month, day, hour, min, sec, nano)
      }
    } match {
      case Success(option) => option
      case Failure(_) => None
    }
  }

  val ymdParser = dateParser(1, 2, 3)

  val dmyParser = dateParser(3, 2, 1)

  val mdyParser = dateParser(3, 1, 2)


}

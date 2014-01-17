package org.tesobe.openpep

import net.liftweb.common.Full
import net.liftweb.util.Props
import org.json.CDL
import org.json.JSONArray
import org.json.JSONException
import scala.io.Source
import scala.util.{Try, Failure}

object CSV2Json {

  def convert: Option[JSONArray] = {
    Props.get("sampleCsvPath") match{
    // Props.get("csvPath") match{
      case Full(path) =>{
        val csv: String = Source.fromFile(path).mkString
        Try(new JSONArray(
          CDL.toJSONArray(csv)
          .toString
          .replaceAll("(\"([0-9]+)(\\.[0-9]+)?\")+", "$2$3")
        )).toOption

      }
      case _ => None
    }
  }
}

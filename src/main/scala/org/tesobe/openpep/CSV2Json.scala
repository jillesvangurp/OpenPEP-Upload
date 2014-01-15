package org.tesobe.openpep

import net.liftweb.common.Full
import net.liftweb.util.Props
import org.json.CDL
import org.json.JSONArray
import org.json.JSONException
import scala.io.Source
import scala.util.{Try, Failure}
import au.com.bytecode.opencsv.CSVReader
import java.io.StringReader

object CSV2Json {

  def convert: Option[JSONArray] = {
    Props.get("sampleCsvPath") match{
    // Props.get("csvPath") match{
      case Full(path) =>{
        val csv: String = Source.fromFile(path).mkString
        // val jsonString = Try(CDL.toJSONArray(csv))
        val jsonString = Try(CDL.toJSONArray(csv))
        // println("****************"+ jsonString)
        jsonString.toOption
      }
      case _ => None
    }
  }
}

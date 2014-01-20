/**
Open PEP Search
Copyright (C) 2013, 2013, TESOBE / Music Pictures Ltd

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Email: contact@tesobe.com
TESOBE / Music Pictures Ltd
Osloerstrasse 16/17
Berlin 13359, Germany

  This product includes software developed at
  TESOBE (http://www.tesobe.com/)
  by
  Simon Redfern : simon AT tesobe DOT com
  Nina Gänsdorfer: nina AT tesobe DOT com
  Ayoub Benali: ayoub AT tesobe DOT com

 */
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
    Props.get("csvPath") match{
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

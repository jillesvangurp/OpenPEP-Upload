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

import net.liftweb.util.Props
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.node.NodeBuilder._
import org.json.JSONArray
import scala.io.Source

object Upload {

  def main(args : Array[String]) {
    // clustername has to be set both for the node as for the client
    val node = nodeBuilder().clusterName("myCluster").client(true).node()
    val settings = ImmutableSettings.settingsBuilder()
      .put("cluster.name", "myCluster")
      .put("index.number_of_shards", 13)
      .put("index.number_of_replicas", 1)
     .build()
    val client = new TransportClient(settings)
    client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300))

    def createIndexWithProperties(indexName: String) = {
      val indexRequest = new CreateIndexRequest(indexName).settings(settings)
      client.admin().indices().create(indexRequest).actionGet()
    }

    def getJson {
      val json = CSV2Json.convert
      json match {
        case Some(converted) =>
          createESDocuments(Props.get("es_index", "people"), Props.get("es_type", "person"), converted)
        case None => {
          println("Converting CSV to JSON failed.")
        }
      }
    }

    def createESDocuments (es_index: String, es_type: String, json: JSONArray){
      createIndexWithProperties(es_index)

      var bulkJson: List[String] = Nil
      for(i <- 0 to json.length-1 ){
        bulkJson = bulkJson :+ json.get(i).toString
      }

      val bulkRequest = client.prepareBulk()
      // in for loop we are adding  prepareindex call to bulkRequest for each json
      bulkJson.zipWithIndex.foreach{
        line => bulkRequest.add(client.prepareIndex(es_index, es_type, line._2.toString).setSource(line._1))
      }

      bulkRequest.execute().actionGet()
      client.close()
    }

    getJson
  }
}

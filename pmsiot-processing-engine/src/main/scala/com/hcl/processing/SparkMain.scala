package com.hcl.processing;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.bson.Document;
import org.mongodb.scala.MongoClient;
import org.mongodb.scala.MongoCollection;
import org.mongodb.scala.MongoDatabase;
import ScalaConstants._;
import com.mongodb.DBCursor;
import com.hcl.pmsiot.mongo.MongoDao;

import com.google.gson.Gson;

import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.JavaConverters._
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.libs.json.Format
import play.api.libs.json.JsSuccess
import play.api.libs.json.Writes

object SparkMain {

  def main(args: Array[String]): Unit = {

    val sparkConfiguartion = new SparkConf().setAppName("Demo").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConfiguartion);
    val streamingContext = new StreamingContext(sparkContext, Seconds(10));
    val message = KafkaUtils.createStream(streamingContext, Kafka_Zookeeper_URL, Kafka_App_Name, Map(Kafka_Topic -> 1));
    val unformattedCoordinates = message.map(_._2) // getting only value part not key
    unformattedCoordinates.print();

    val props = new Properties()
    props.put("bootstrap.servers", Kafka_Broker_URI)
    props.put("key.serializer", Key_Serializer)
    props.put("value.serializer", Value_Serializer)

    Logger.getLogger("org").setLevel(Level.WARN)

    println("======== Mongo Data ========")
    case class Building(Location: String, x_coordinate: Double, y_coordinate: Double)
    implicit val implicitBuildingrWrites = new Writes[Building] {
        def writes(building: Building): JsValue = {
          Json.obj(
            "location" -> building.Location,
            "latitude" -> building.x_coordinate,
            "longitude" -> building.y_coordinate
          )
        }
      }
    
    
    val connectMongo : MongoDao = new MongoDao();
    var dbcursor: DBCursor = connectMongo.getBuildingData();
    var buildingList: List[Building] = List();
   
    var building: Building = null.asInstanceOf[Building]
    while (dbcursor.hasNext()) {
      var document = dbcursor.next();
      building = Building(document.get("Location").toString(), document.get("x_coordinate").toString().toDouble, document.get("y_coordinate").toString().toDouble);
      buildingList = buildingList.:::(List(building));
     
    }
    println(buildingList);
    <!-- new end-->

    def getDistanceFromLatLonInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double = {

      val R = 6371; // Radius of the earth in km
      val dLat = deg2rad(lat2 - lat1); // deg2rad below
      val dLon = deg2rad(lon2 - lon1);
      var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
      var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      var d = R * c; // Distance in km
      return d;
    }

    def deg2rad(deg: Double) = {
      deg * (Math.PI / 180)
    }

    val gson = new Gson;
    unformattedCoordinates.foreachRDD(rdd => {
      for (item <- rdd.collect().toArray) {
        var subString = item.substring(1, item.length() - 1);
        var arrayOfCoordinates = subString.split(",");
        var lat = arrayOfCoordinates(0);
        var longi = arrayOfCoordinates(1);
        var userId = arrayOfCoordinates(2);
        var personCoordinates = arrayOfCoordinates.map(o => o.toDouble);
        //def notifibuilding: List[Building] = lis.filter(building => (building.x > personCoordinates(0) - 100 && building.x < personCoordinates(0) + 100 && building.y > personCoordinates(1) - 100 && building.y < personCoordinates(1) + 100))
        def notifibuilding: List[Building] = buildingList.filter(building => getDistanceFromLatLonInKm(building.x_coordinate, building.y_coordinate, personCoordinates(0), personCoordinates(1)) * 1000 < 100)
        println(notifibuilding);
        
        //Saving User location to mongo
        /*if(userId != "") {
          val userLocation : UserLocation = new UserLocation(userId, lat, longi);
          connectMongo.saveorUpdateUserLocation(userLocation);
        }*/
        
        val producer = new KafkaProducer[String, String](props)
        //val jsonString = Util.getJsonFromList(notifibuilding.asJava);
        //val jsonString = gson.toJson(notifibuilding);
        val record = new ProducerRecord(Kafka_Processed_Topic, userId,  Json.toJson(notifibuilding).toString())
        producer.send(record)
        producer.close();
      }
    })
    

    streamingContext.start();
    streamingContext.awaitTermination();

  }
}
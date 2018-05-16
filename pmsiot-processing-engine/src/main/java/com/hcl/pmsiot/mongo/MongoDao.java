package com.hcl.pmsiot.mongo;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;



public class MongoDao {

	private MongoClient mongoclient;
	
	private DB db;
	
	public MongoDao() throws UnknownHostException {
		mongoclient = new MongoClient();
		db = mongoclient.getDB("campus");
		System.out.println("Connected");
		System.out.println("Feching Records.....");
	}
/*	public static void main(String[] args) throws Exception {
		System.out.println(ConnectDb.getMongoData());
	}*/
	
	@SuppressWarnings({ "resource", "deprecation" })
	public DBCursor getBuildingData()
	{
		
		DBCollection collection = db.getCollection("buildings");
		DBCursor cursor = collection.find();
		System.out.println("Completed");
		return cursor;
		//mongoclient.close();
	}
	
	
}
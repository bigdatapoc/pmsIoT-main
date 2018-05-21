package com.hcl.pmsiot.mongo;

import java.net.UnknownHostException;

import com.hcl.pmsiot.Constants.MongoConstants;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;



public class MongoDao {

	private MongoClient mongoclient;
	
	private DB db;
	
	public MongoDao() throws UnknownHostException {
		mongoclient = new MongoClient();
		db = mongoclient.getDB(MongoConstants.MongoDocumentName);
		}

	@SuppressWarnings({ "resource", "deprecation" })
	public DBCursor getBuildingData()
	{
		
		DBCollection collection = db.getCollection(MongoConstants.MongoCollectionForBuildings);
		DBCursor cursor = collection.find();
		return cursor;
		//mongoclient.close();
	}
	
	

	
}
package com.hcl.pmsiot.mongo;

import java.net.UnknownHostException;

import com.hcl.pmsiot.Constants.OperationConstants;
import com.hcl.pmsiot.data.UserLocation;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;



public class MongoDao {

	private MongoClient mongoclient;
	
	private DB db;
	
	public MongoDao() throws UnknownHostException {
		mongoclient = new MongoClient("192.168.99.100", 27017);
		db = mongoclient.getDB(OperationConstants.MongoDocumentName);
		
	}
/*	public static void main(String[] args) throws Exception {
		System.out.println(ConnectDb.getMongoData());
	}*/
	
	@SuppressWarnings({ "resource", "deprecation" })
	public DBCursor getBuildingData()
	{
		
		DBCollection collection = db.getCollection(OperationConstants.MongoCollectionForBuildings);
		DBCursor cursor = collection.find();
		return cursor;
		//mongoclient.close();
	}
	
	
	public void saveorUpdateUserLocation(UserLocation userLoc) {
		
		if(getUserLocation(userLoc.getUserId()) != null) {
			updateUserLocation(userLoc);
		}
		else {
			saveUserLocation(userLoc);
		}
	}
	
	public 	void saveUserLocation(UserLocation userLoc) {

		DBCollection table = db.getCollection(OperationConstants.MongoCollectionFoUsers);
		BasicDBObject document = new BasicDBObject();
		document.put("userId", userLoc.getUserId());
		document.put("latitude", userLoc.getLatitude());
		document.put("longitude", userLoc.getLongitude());
		table.insert(document);
		
	}
	
	public  void updateUserLocation(UserLocation userLoc) {
		
		DBCollection table = db.getCollection(OperationConstants.MongoCollectionFoUsers);
		BasicDBObject query = new BasicDBObject();					
		query.put("userId", userLoc.getUserId());

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("latitude", userLoc.getLatitude());
		newDocument.put("longitude", userLoc.getLongitude());
		
					
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		table.update(query, updateObj);
	}	
	
	public UserLocation getUserLocation(String userId) {								
		UserLocation userLocation= null;
		DBCollection table = db.getCollection(OperationConstants.MongoCollectionFoUsers);

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("userId", userId);
		DBCursor cursor = table.find(searchQuery);

		if (cursor.hasNext()) {
			DBObject document = cursor.next();
			
			userLocation = new UserLocation();
			userLocation.setUserId((String) document.get("userId"));
			userLocation.setLatitude((String) document.get("latitude"));
			userLocation.setLongitude((String) document.get("longitude"));
			
		}
		return userLocation;
	}
}
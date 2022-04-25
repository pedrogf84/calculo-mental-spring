package com.pedro.calculomental.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.pedro.calculomental.dtos.BaseDto;


/**
 * The Class MongoConnectorDao.
 *
 * @param <T> the generic type
 */
@PropertySource("classpath:application.properties")
public class MongoConnectorDao <T extends BaseDto> {
	
	/** The logger. */
	private static Logger LOGGER = LoggerFactory.getLogger(MongoConnectorDao.class);
	
	/** Generic Type T is not kept at runtime, so create a private member for it. */
	private Class<T> type;

	/** The connection conf. */
	@Value("${connection.srv}")
	private String connectionConf; 
	
	/** The data base name. */
	@Value("${connection.database}")
	private String dataBaseName;
	
	/**  Collection Name. */
	private String collectionName;

	/**
	 * Instantiates a new MongoDao.
	 *
	 * @param collectionName the collection name
	 * @param type the type
	 */
	public MongoConnectorDao(String collectionName, Class<T> type) {
		this.type = type;
		this.collectionName = collectionName;
	}

	/**
	 * Method for creating connection and retrieve target mongo collection.
	 *
	 * @return mongo collection object
	 */
	private MongoCollection<Document> obtainMongoCollection(){
		LOGGER.info("MongoConnectorDao.obtainMongoCollection.in");
		
		MongoClient mongoClient = MongoClients.create(this.connectionConf);
		MongoDatabase mongoDataBase = mongoClient.getDatabase(this.dataBaseName);
	
		LOGGER.info("MongoConnectorDao.obtainMongoCollection.out");
		return mongoDataBase.getCollection(collectionName);
	}

	/**
	 * Method object to json.
	 *
	 * @param object the object
	 * @return the json
	 */
	private String toJson(Object object) {		
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	/**
	 * Parsing document to Dto.
	 *
	 * @param document the document
	 * @return target dto
	 */
	private T toDto(Document document) {
		Gson gson = new Gson();
		return gson.fromJson(this.toJson(document), this.type);
	}

	/**
	 * Creates a new entry within the collection.
	 *
	 * @param data the data
	 * @throws MongoException the mongo exception
	 */
	public void create(T data) throws MongoException{
		LOGGER.info("MongoConnectorDao.create.in - data: {}", this.toJson(data));
	
		this.obtainMongoCollection().insertOne(Document.parse(this.toJson(data)));
			
		LOGGER.info("MongoConnectorDao.create.out");
	}
	
	
	/**
	 * Retrieves all entries from the current collection.
	 * Note: Collscan may happens
	 *
	 * @return the list
	 */
	public List<T> read(){
		LOGGER.info("MongoConnectorDao.read.in");
		
		List<Document> response =this.obtainMongoCollection().find().into(new ArrayList<>());
		List<T> output = response.stream().map(entry -> this.toDto(entry)).collect(Collectors.toList());
		
		LOGGER.info("MongoConnectorDao.read.out - response: {}", this.toJson(output));
		return output;
	}
	
	
	/**
	 * Retrieves all matches from the current collection base on key-value parameter.
	 *
	 * @param key the key
	 * @param value the value
	 * @return matches
	 */
	public List<T> read(String key, Object value){
		LOGGER.info("MongoConnectorDao.read.in - fieldName:{}, value: {}", key, this.toJson(value));
		
		List<Document> response = this.obtainMongoCollection().find(new Document(key, value)).into(new ArrayList<>());
		List<T> output = response.stream().map(entry -> this.toDto(entry)).collect(Collectors.toList());
		
		LOGGER.info("MongoConnectorDao.read.out - response: {}", this.toJson(output));
		return output;
	}

	/**
	 * Updates a single entry.
	 *
	 * @param data the data
	 * @return updated data
	 * @throws MongoException the mongo exception
	 */
	public Long update(T data) throws MongoException {
		LOGGER.info("MongoConnectorDao.update.in - data: {}", this.toJson(data));

		UpdateResult result = this.obtainMongoCollection().replaceOne(new Document("id", data.getId()), 
											  Document.parse(this.toJson(data)));

		LOGGER.info("MongoConnectorDao.update.out - Documents updated: {}", result.getModifiedCount());
		return result.getModifiedCount();
	}

	/**
	 * Deletes the entry which matches the data provided.
	 *
	 * @param data the data
	 * @return Number of deleted entries
	 * @throws MongoException the mongo exception
	 */
	public Long delete(T data) throws MongoException{
		LOGGER.info("MongoConnectorDao.delete.in - data: {}", this.toJson(data));
		
		DeleteResult result = this.obtainMongoCollection().deleteOne(Document.parse(this.toJson(data)));
		
		LOGGER.info("MongoConnectorDao.delete.out - deleted documents: {}", result.getDeletedCount());
		return result.getDeletedCount();
	}
		
}

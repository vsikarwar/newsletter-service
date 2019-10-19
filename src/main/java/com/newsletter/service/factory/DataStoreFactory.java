package com.newsletter.service.factory;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.newsletter.service.datastore.DataStore;
import com.newsletter.service.datastore.InMemoryDataStore;

@Component
public class DataStoreFactory {
	
	public static final String IN_MEMORY_DATA_STORE = "INMEMORY";
	private static final DataStoreFactory INSTANCE = new DataStoreFactory();
	
	Map<String, DataStore> dataStores = new HashMap<>();
	
//	private DataStoreFactory() {
//		dataStores = new HashMap<>();
//		init();
//	}
	
	@PostConstruct
	private void init() {
		dataStores.put(IN_MEMORY_DATA_STORE, new InMemoryDataStore());
	}
	
	public static DataStoreFactory instance() {
		return INSTANCE;
	}
		
	
	public DataStore getDataStore(String type) {
		//default configured data store is in-memory data store
		if(dataStores.containsKey(type)) {
			return dataStores.get(type);
		}
		throw new IllegalArgumentException();
	}

}

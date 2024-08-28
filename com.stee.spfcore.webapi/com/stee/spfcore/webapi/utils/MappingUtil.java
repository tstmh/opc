package com.stee.spfcore.webapi.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MappingUtil  {

    public static <T> List<T> map(Class<T> type, List<Object[]> records){
		List<T> result = new LinkedList<>();
			for(Object[] record : records){
				result.add(map(type, record));
			}
		return result;
	}
    
    @SuppressWarnings("unchecked")
	public static <T> List<T> getResultList(Query query, Class<T> type){
		List<Object[]> records = query.getResultList();
		return map(type, records);
    }
    
    public static <T> T map(Class<T> type, Object[] tuple){
    	List<Class<?>> tupleTypes = new ArrayList<>();
    	for(Object field : tuple){
    		tupleTypes.add(field.getClass());
    	}
    	try {
    		Constructor<T> ctor = type.getConstructor(tupleTypes.toArray(new Class<?>[tuple.length]));
    		return ctor.newInstance(tuple);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
		}
	}

}
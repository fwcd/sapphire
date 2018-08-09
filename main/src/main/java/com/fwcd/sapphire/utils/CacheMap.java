package com.fwcd.sapphire.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CacheMap<T> {
	private final Map<Object, T> data;
	
	public CacheMap() {
		data = new HashMap<>();
	}
	
	public void store(Object key, T value) {
		data.put(key, value);
	}
	
	public T getOrNull(Object key) {
		return data.get(key);
	}
	
	public T getOrStore(Object key, Supplier<T> supplier) {
		T mapping = getOrNull(key);
		
		if (mapping == null) {
			mapping = supplier.get();
			store(key, mapping);
		}
		
		return mapping;
	}
}

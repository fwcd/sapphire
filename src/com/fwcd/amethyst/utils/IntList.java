package com.fwcd.amethyst.utils;

import java.util.Arrays;

public class IntList {
	private int[] data;
	private int size = 0;
	
	public IntList() {
		this(10);
	}
	
	public IntList(int initialSize) {
		data = new int[initialSize];
	}
	
	public void addAll(int... array) {
		int offset = size;
		size += array.length;
		ensureCapacity();
		System.arraycopy(array, 0, data, offset, array.length);
	}
	
	private void ensureCapacity() {
		if (size >= data.length - 1) {
			int[] newArr = Arrays.copyOf(data, data.length + 10);
			data = newArr;
		}
	}
	
	public int size() {
		return size;
	}
	
	public void add(int v) {
		ensureCapacity();
		data[size] = v;
		size++;
	}
	
	public void removeLast() {
		size--;
	}
	
	public int get(int i) {
		if (i < size) {
			return data[i];
		} else {
			throw new IndexOutOfBoundsException(Integer.toString(i));
		}
	}
	
	public int[] toArray() {
		return Arrays.copyOf(data, size);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(data);
	}
}

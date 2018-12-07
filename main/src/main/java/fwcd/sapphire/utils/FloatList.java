package fwcd.sapphire.utils;

import java.util.Arrays;

public class FloatList {
	private float[] data;
	private int size = 0;
	
	public FloatList() {
		this(10);
	}
	
	public FloatList(int initialSize) {
		data = new float[initialSize];
	}
	
	public void addAll(float... array) {
		int offset = size;
		size += array.length;
		ensureCapacity();
		System.arraycopy(array, 0, data, offset, array.length);
	}
	
	private void ensureCapacity() {
		while (size >= data.length - 1) {
			float[] newArr = Arrays.copyOf(data, data.length + 10);
			data = newArr;
		}
	}
	
	public int size() {
		return size;
	}
	
	public void add(float v) {
		ensureCapacity();
		data[size] = v;
		size++;
	}
	
	public void removeLast() {
		size--;
	}
	
	public float get(int i) {
		if (i < size) {
			return data[i];
		} else {
			throw new IndexOutOfBoundsException(Integer.toString(i));
		}
	}
	
	public float[] toArray() {
		return Arrays.copyOf(data, size);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(data);
	}
}

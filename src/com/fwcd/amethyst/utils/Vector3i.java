package com.fwcd.amethyst.utils;

import org.lwjgl.util.vector.Vector3f;

public class Vector3i {
	private final int x;
	private final int y;
	private final int z;
	
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3i add(Vector3i other) {
		return add(other.x, other.y, other.z);
	}

	public Vector3i add(int x, int y, int z) {
		return new Vector3i(this.x + x, this.y + y, this.z + z);
	}
	
	public Vector3i sub(Vector3i other) {
		return sub(other.x, other.y, other.z);
	}

	public Vector3i sub(int x, int y, int z) {
		return new Vector3i(this.x - x, this.y - y, this.z - z);
	}
	
	public double length() {
		return Math.sqrt((x * x) + (y * y) + (z * z));
	}
	
	public Vector3i scale(double factor) {
		return new Vector3i((int) (x * factor), (int) (y * factor), (int) (z * factor));
	}
	
	public Vector3i normalize() {
		return scale(1 / length());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Vector3f toVector3f() {
		return new Vector3f(x, y, z);
	}
}

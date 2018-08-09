package com.fwcd.sapphire.utils;

import org.lwjgl.util.vector.Vector3f;

public class ImmutableVec3f {
	private final float x;
	private final float y;
	private final float z;
	
	public ImmutableVec3f(Vector3f vec) {
		this(vec.x, vec.y, vec.z);
	}
	
	public ImmutableVec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ImmutableVec3f other = (ImmutableVec3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ImmutableVec3f [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}

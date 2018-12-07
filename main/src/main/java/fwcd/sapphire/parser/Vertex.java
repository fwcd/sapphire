package fwcd.sapphire.parser;

public class Vertex {
	private static final int NO_VALUE = -1;
	
	private final float x;
	private final float y;
	private final float z;
	private final int index;
	private final float length;
	
	private Vertex duplicate = null;
	private int textureIndex = NO_VALUE;
	private int normalIndex = NO_VALUE;
	
	public Vertex(float x, float y, float z, int index) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.index = index;
		length = (float) Math.sqrt((x * x) + (y * y) + (z * z));
	}

	public boolean isSet() {
		return textureIndex != NO_VALUE && normalIndex != NO_VALUE;
	}
	
	public boolean hasSameTextureAndNormal(int otherTextureIndex, int otherNormalIndex) {
		return textureIndex == otherTextureIndex && normalIndex == otherNormalIndex;
	}
	
	public Vertex getDuplicate() {
		return duplicate;
	}

	public void setDuplicate(Vertex duplicate) {
		this.duplicate = duplicate;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
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

	public int getIndex() {
		return index;
	}

	public float getLength() {
		return length;
	}
}

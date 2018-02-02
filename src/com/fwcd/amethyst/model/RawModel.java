package com.fwcd.amethyst.model;

/**
 * Holds an OpenGL id as a reference to a VAO.
 */
public class RawModel {
	private final int vaoID;
	private final int vertexCount;
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}

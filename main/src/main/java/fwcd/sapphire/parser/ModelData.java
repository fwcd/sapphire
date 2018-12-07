package fwcd.sapphire.parser;

import fwcd.sapphire.model.ModelLoader;
import fwcd.sapphire.model.RawModel;

public class ModelData {
	private final float[] vertices;
	private final float[] textureCoords;
	private final float[] normals;
	private final int[] indices;
	private final float furthestPoint;
	
	public ModelData(
			float[] vertices,
			float[] textureCoords,
			float[] normals,
			int[] indices,
			float furthestPoint
	) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public float getFurthestPoint() {
		return furthestPoint;
	}
	
	public RawModel loadToVAO(ModelLoader loader) {
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
}

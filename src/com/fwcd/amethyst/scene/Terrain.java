package com.fwcd.amethyst.scene;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.fwcd.amethyst.model.ModelLoader;
import com.fwcd.amethyst.model.RawModel;
import com.fwcd.amethyst.model.TerrainHeightMap;
import com.fwcd.amethyst.model.TerrainTexturePack;
import com.fwcd.amethyst.utils.AmethystMath;

public class Terrain {
	private final float size = 800;
	
	private final float x;
	private final float z;
	private final TerrainTexturePack textures;
	private final RawModel model;
	private final TerrainHeightMap heightMap;
	
	private float vertexDist = Float.NaN;
	
	public Terrain(int gridX, int gridZ, ModelLoader loader, TerrainTexturePack textures) {
		this(gridX, gridZ, loader, textures, null);
	}
	
	public Terrain(
			int gridX,
			int gridZ,
			ModelLoader loader,
			TerrainTexturePack textures,
			TerrainHeightMap heightMap
	) {
		this.textures = textures;
		this.heightMap = heightMap;
		model = generateTerrain(loader);
		x = gridX * size;
		z = gridZ * size;
	}
	
	public Matrix4f getTransformationMatrix() {
		return AmethystMath.newTransformationMatrix(getPos(), 0, 0, 0, 1);
	}
	
	public Vector3f getPos() {
		return new Vector3f(x, 0, z); // TODO: Support other y-values
	}
	
	private RawModel generateTerrain(ModelLoader loader) {
		final int vertexCount = (heightMap == null) ? 128 : heightMap.getImageHeight();
		vertexDist = size / (float) vertexCount;
		
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount-1) * (vertexCount - 1)];
		int vertexPointer = 0;
		for (int i=0; i<vertexCount; i++) {
			for (int j=0; j<vertexCount; j++) {
				float y = (heightMap == null) ? 0 : heightMap.getHeightAt(j, i);
				
				vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * size;
				vertices[vertexPointer * 3 + 1] = y;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * size;
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz=0; gz<vertexCount-1; gz++){
			for (int gx=0; gx<vertexCount-1; gx++){
				int topLeft = (gz * vertexCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTextures() {
		return textures;
	}

	public float getHeightAt(float x, float z) {
		if (heightMap == null) {
			System.out.println("No height map at " + x + ", " + z);
			return 0;
		} else {
			return heightMap.getHeightAt((int) ((x - this.x) / vertexDist), (int) ((z - this.z) / vertexDist));
		}
	}

	public boolean contains(float x, float z) {
		return x >= this.x && x < (this.x + size)
				&& z >= this.z && z < (this.z + size);
	}
}

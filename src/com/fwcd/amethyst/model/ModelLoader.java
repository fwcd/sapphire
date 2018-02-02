package com.fwcd.amethyst.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.fwcd.amethyst.utils.AnyFile;

public class ModelLoader implements AutoCloseable {
	public static final int VAO_VERTICES_INDEX = 0;
	public static final int VAO_TEXTURES_INDEX = 1;
	public static final int VAO_NORMALS_INDEX = 2;
	
	private final List<Integer> vaos = new ArrayList<>();
	private final List<Integer> vbos = new ArrayList<>();
	private final List<Integer> textures = new ArrayList<>();
	
	private boolean mipmapTextures = true;
	private float mipmapLevel = -0.5F; // Smaller: lesser mimapping (thus sharper textures and slower rendering)
	
	public RawModel loadToVAO(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(VAO_VERTICES_INDEX, 3, vertices);
		storeDataInAttributeList(VAO_TEXTURES_INDEX, 2, textureCoords);
		storeDataInAttributeList(VAO_NORMALS_INDEX, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public Texture loadTexture(AnyFile file) {
		Texture texture = new Texture(file);
		textures.add(texture.getID());
		
		if (mipmapTextures) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, mipmapLevel);
		}
		
		return texture;
	}
	
	@Override
	public void close() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int dimensions, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, dimensions, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}

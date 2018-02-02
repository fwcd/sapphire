package com.fwcd.amethyst.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.fwcd.amethyst.utils.AnyFile;
import com.fwcd.amethyst.utils.IntList;

public abstract class ShaderProgram implements AutoCloseable {
	private final IntList boundAttributes = new IntList();
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(AnyFile vertexFile, AnyFile fragmentFile) {
		vertexShaderID = vertexFile.mapStream(in -> loadShader(in, GL20.GL_VERTEX_SHADER));
		fragmentShaderID = fragmentFile.mapStream(in -> loadShader(in, GL20.GL_FRAGMENT_SHADER));
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		registerUniforms();
	}
	
	public int[] getBoundAttributes() {
		return boundAttributes.toArray();
	}
	
	/**
	 * Registers all uniform variables. It is safe to use these
	 * for non-null fields as this method will be invoked in the
	 * constructor.
	 */
	protected abstract void registerUniforms();
	
	protected Uniform createUniform(String name) {
		return new Uniform(programID, name);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	@Override
	public void close() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteProgram(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName) {
		boundAttributes.add(attribute);
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	private int loadShader(InputStream in, int type) {
		StringBuilder shaderSource = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			throw new RuntimeException("Could not compile shader.");
		}
		
		return shaderID;
	}
}

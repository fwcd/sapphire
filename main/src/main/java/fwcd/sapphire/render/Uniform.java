package fwcd.sapphire.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * A global OpenGL-variable used to communicate information with
 * the shaders.
 * 
 * @author fwcd
 *
 */
public class Uniform {
	private final int location;
	
	/**
	 * Constructs and registers a new uniform. <b>This needs
	 * to be called in an OpenGL-context!!</b>
	 * 
	 * @param programID - The id of the shader program
	 * @param name - The variable name (which is also used in the GLSL-shader programs)
	 */
	public Uniform(int programID, String name) {
		location = GL20.glGetUniformLocation(programID, name);
	}
	
	public int getLocation() {
		return location;
	}
	
	public void setInt(int value) {
		GL20.glUniform1i(location, value);
	}
	
	public void setFloat(float value) {
		GL20.glUniform1f(location, value);
	}
	
	public void setVector(Vector3f vec) {
		GL20.glUniform3f(location, vec.x, vec.y, vec.z);
	}
	
	public void setBool(boolean value) {
		GL20.glUniform1i(location, value ? 1 : 0);
	}
	
	public void setMatrix(Matrix4f matrix) {
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
}

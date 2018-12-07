package fwcd.sapphire.utils;

import java.util.Collection;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SapphireMath {
	private static final SinTable SIN_TABLE = new SinTable();
	public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
	
	private SapphireMath() {}
	
	public static Vector3f invert(Vector3f vec) {
		return new Vector3f(-vec.x, -vec.y, -vec.z);
	}
	
	public static Matrix4f newTransformationMatrix(Vector3f translation, float rotationX, float rotationY, float rotationZ, float scale) {
		Matrix4f matrix = new Matrix4f();
		
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		if (rotationX != 0F) {
			Matrix4f.rotate((float) Math.toRadians(rotationX), X_AXIS, matrix, matrix);
		}
		if (rotationY != 0F) {
			Matrix4f.rotate((float) Math.toRadians(rotationY), Y_AXIS, matrix, matrix);
		}
		if (rotationZ != 0F) {
			Matrix4f.rotate((float) Math.toRadians(rotationZ), Z_AXIS, matrix, matrix);
		}
		if (scale != 0F) {
			Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		}
		
		return matrix;
	}
	
	public static float[] unboxFloats(Collection<Float> boxed) {
		float[] array = new float[boxed.size()];
		
		int i = 0;
		for (float v : boxed) {
			array[i] = v;
			i++;
		}
		
		return array;
	}
	
	public static int[] unboxInts(Collection<Integer> boxed) {
		int[] array = new int[boxed.size()];
		
		int i = 0;
		for (int v : boxed) {
			array[i] = v;
			i++;
		}
		
		return array;
	}
	
	public static float sin(float deg) {
		return SIN_TABLE.sin(deg);
	}
	
	public static float cos(float deg) {
		return SIN_TABLE.cos(deg);
	}
}

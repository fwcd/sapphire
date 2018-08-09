package com.fwcd.sapphire.scene;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.fwcd.sapphire.model.TexModel;
import com.fwcd.sapphire.utils.SapphireMath;

public class Entity {
	private final EntityBehavior[] behaviors;
	private final TexModel model;
	
	private Vector3f position;
	private Vector3f velocity = null;
	private Vector3f acceleration = null;
	private boolean inMotion = false;
	
	private float rotationX;
	private float rotationY;
	private float rotationZ;
	private float scale;
	private Scene scene;
	
	public Entity(TexModel model, EntityBehavior... behaviors) {
		this.model = model;
		this.behaviors = behaviors;
		
		position = new Vector3f(0, 0, 0);
		rotationX = 0;
		rotationY = 0;
		rotationZ = 0;
		scale = 1;
	}
	
	public void enableGravity(float yAcceleration) {
		velocity = new Vector3f(0, 0, 0);
		acceleration = new Vector3f(0, yAcceleration, 0);
	}
	
	protected void uncheckedTranslate(float dx, float dy, float dz) {
		position.x += dx;
		position.y += dy;
		position.z += dz;
	}
	
	protected void uncheckedRotate(float dx, float dy, float dz) {
		rotationX += dx;
		rotationY += dy;
		rotationZ += dz;
	}
	
	protected void uncheckedScale(float factor) {
		scale *= factor;
	}
	
	public boolean translate(float dx, float dy, float dz) {
		float newX = position.x + dx;
		float newY = position.y + dy;
		float newZ = position.z + dz;
		
		if (validateMoveTo(newX, newY, newZ)) {
			position.x = newX;
			position.y = newY;
			position.z = newZ;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean rotate(float dx, float dy, float dz) {
		float newRotX = rotationX + dx;
		float newRotY = rotationY + dy;
		float newRotZ = rotationZ + dz;
		
		if (validateRotationTo(newRotX, newRotY, newRotZ)) {
			rotationX = newRotX;
			rotationY = newRotY;
			rotationZ = newRotZ;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean scale(float factor) {
		float newScale = scale * factor;
		
		if (validateScaleTo(newScale)) {
			scale = newScale;
			return true;
		} else {
			return false;
		}
	}
	
	public void onTick() {
		if (velocity != null) {
			inMotion = translate(velocity.x, velocity.y, velocity.z);
			if (!inMotion) {
				velocity = new Vector3f(0, 0, 0);
			}
		}
		
		if (acceleration != null) {
			Vector3f.add(velocity, acceleration, velocity);
		}
		
		for (EntityBehavior behavior : behaviors) {
			behavior.onTick(this, scene);
		}
	}
	
	public Matrix4f getTransformationMatrix() {
		return SapphireMath.newTransformationMatrix(position, rotationX, rotationY, rotationZ, scale);
	}
	
	public TexModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public boolean setX(float x) {
		if (validateMoveTo(x, position.y, position.z)) {
			position.x = x;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setY(float y) {
		if (validateMoveTo(position.x, y, position.z)) {
			position.y = y;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setZ(float z) {
		if (validateMoveTo(position.x, position.y, z)) {
			position.z = z;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setPosition(Vector3f position) {
		if (validateMoveTo(position.x, position.y, position.z)) {
			this.position = position;
			return true;
		} else {
			return false;
		}
	}

	public float getRotationX() {
		return rotationX;
	}

	public boolean setRotationX(float rotationX) {
		if (validateRotationTo(rotationX, rotationY, rotationZ)) {
			this.rotationX = rotationX;
			return true;
		} else {
			return false;
		}
	}

	public float getRotationY() {
		return rotationY;
	}

	public boolean setRotationY(float rotationY) {
		if (validateRotationTo(rotationX, rotationY, rotationZ)) {
			this.rotationY = rotationY;
			return true;
		} else {
			return false;
		}
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public boolean setRotationZ(float rotationZ) {
		if (validateRotationTo(rotationX, rotationY, rotationZ)) {
			this.rotationZ = rotationZ;
			return true;
		} else {
			return false;
		}
	}

	public float getScale() {
		return scale;
	}

	public boolean setScale(float scale) {
		if (validateScaleTo(scale)) {
			this.scale = scale;
			return true;
		} else {
			return false;
		}
	}
	
	private boolean validateMoveTo(float newX, float newY, float newZ) {
		for (EntityBehavior behavior : behaviors) {
			Vector3f intercept = behavior.interceptMotion(this, scene, newX, newY, newZ);
			if (intercept != null) {
				position = intercept;
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateRotationTo(float newRotX, float newRotY, float newRotZ) {
		for (EntityBehavior behavior : behaviors) {
			Vector3f intercept = behavior.interceptRotation(this, scene, newRotX, newRotY, newRotZ);
			if (intercept != null) {
				rotationX = intercept.x;
				rotationY = intercept.y;
				rotationZ = intercept.z;
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateScaleTo(float newScale) {
		for (EntityBehavior behavior : behaviors) {
			float intercept = behavior.interceptScaling(this, scene, newScale);
			if (intercept != newScale) {
				scale = intercept;
				return false;
			}
		}
		
		return true;
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Vector3f getVelocity() {
		if (velocity == null) {
			velocity = new Vector3f(0, 0, 0);
		}
		
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public Vector3f getAcceleration() {
		if (acceleration == null) {
			acceleration = new Vector3f(0, 0, 0);
		}
		
		return acceleration;
	}

	public void setAcceleration(Vector3f acceleration) {
		this.acceleration = acceleration;
	}
	
	public boolean isInMotion() {
		return inMotion;
	}
}

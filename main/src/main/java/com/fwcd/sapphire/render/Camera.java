package com.fwcd.sapphire.render;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.fwcd.sapphire.scene.Entity;
import com.fwcd.sapphire.utils.SapphireMath;
import com.fwcd.sapphire.utils.Vector3i;

public class Camera {
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitchDeg = 0; // Up-down rotation
	private float yawDeg = 0; // Left-right rotation
	private float rollDeg = 0; // Clockwise-counterclockwise rotation
	
	private Entity trackedEntity = null;
	private boolean firstPerson = true; // True: first person, false: third person
	private float firstPersonHeightOffset = 3F;
	private float thirdPersonDistToTrackedEntity = 10F;
	private float angleAroundTrackedEntity = 0;
	
	public void bindEntity(Entity entity) {
		trackedEntity = entity;
		syncWithEntity();
	}
	
	public void unbindEntity() {
		trackedEntity = null;
	}
	
	private void syncWithEntity() {
		if (trackedEntity != null) {
			Vector3f entityPos = trackedEntity.getPosition();
			float theta = trackedEntity.getRotationY() + angleAroundTrackedEntity;
			yawDeg = 180 - theta;
			
			if (firstPerson) {
				position.x = entityPos.x;
				position.y = entityPos.y + firstPersonHeightOffset;
				position.z = entityPos.z;
			} else {
				float hDist = thirdPersonDistToTrackedEntity * SapphireMath.cos(pitchDeg);
				float vDist = thirdPersonDistToTrackedEntity * SapphireMath.sin(pitchDeg);
				float offsetX = hDist * SapphireMath.sin(theta);
				float offsetZ = hDist * SapphireMath.cos(theta);
				position.x = entityPos.x - offsetX;
				position.y = entityPos.y + vDist;
				position.z = entityPos.z - offsetZ;
			}
		}
	}
	
	public void ascend(float delta) {
		translate(0, delta, 0);
	}
	
	public void descend(float delta) {
		translate(0, -delta, 0);
	}
	
	public void moveRight(float delta) {
		float dx = SapphireMath.cos(yawDeg) * delta;
		float dz = SapphireMath.sin(yawDeg) * delta;
		translate(dx, 0, dz);
	}
	
	public void moveLeft(float delta) {
		moveRight(-delta);
	}
	
	public void moveBackward(float delta) {
		moveForward(-delta);
	}
	
	public void moveForward(float delta) {
		float dx = SapphireMath.cos(yawDeg - 90) * delta;
		float dz = SapphireMath.sin(yawDeg - 90) * delta;
		translate(dx, 0, dz);
	}
	
	private void translate(float dx, float dy, float dz) {
		if (trackedEntity == null) {
			position.x += dx;
			position.y += dy;
			position.z += dz;
		} else {
			trackedEntity.translate(dx, dy, dz);
			syncWithEntity();
		}
	}
	
	public void rotateRoll(float deltaDeg) {
		rollDeg += deltaDeg;
		// TODO: Handle entity tracking as with the yaw
	}
	
	public void rotatePitch(float deltaDeg) {
		pitchDeg += deltaDeg;
		// TODO: Handle entity tracking as with the yaw
	}
	
	public void rotateYaw(float deltaDeg) {
		if (trackedEntity == null) {
			yawDeg += deltaDeg;
		} else {
			trackedEntity.rotate(0, -deltaDeg, 0);
			syncWithEntity();
		}
	}

	public Matrix4f getViewMatrix() {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.rotate(
				(float) Math.toRadians(pitchDeg),
				SapphireMath.X_AXIS,
				matrix,
				matrix
		);
		Matrix4f.rotate(
				(float) Math.toRadians(yawDeg),
				SapphireMath.Y_AXIS,
				matrix,
				matrix
		);
		Matrix4f.rotate(
				(float) Math.toRadians(rollDeg),
				SapphireMath.Z_AXIS,
				matrix,
				matrix
		);
		Matrix4f.translate(
				SapphireMath.invert(position),
				matrix,
				matrix
		);
		
		return matrix;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitchDeg;
	}

	public float getYaw() {
		return yawDeg;
	}

	public float getRoll() {
		return rollDeg;
	}

	public void setPos(Vector3i position) {
		this.position = position.toVector3f();
	}
	
	public void toggleFirstPeson() {
		firstPerson = !firstPerson;
	}
	
	public Entity getNullableTrackedEntity() {
		return trackedEntity;
	}
	
	public boolean isFirstPerson() {
		return firstPerson;
	}
}

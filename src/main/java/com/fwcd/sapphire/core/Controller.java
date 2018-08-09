package com.fwcd.sapphire.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import com.fwcd.sapphire.render.Camera;
import com.fwcd.sapphire.scene.Entity;

/**
 * The controller used to handle user inputs.
 */
public class Controller {
	private final Camera camera;
	
	// Mapping user input to functionality
	private final List<Consumer<Vector2f>> mouseBinds = new ArrayList<>();
	private final Map<Integer, Runnable> keyPressBinds = new HashMap<>();
	private final Map<Integer, Runnable> keyHoldBinds = new HashMap<>();
	
	private float moveSpeed = 0.3F;
	private float rotationSpeed = 0.3F;
	
	private Vector2f lastMousePos = null;
	
	public Controller(Camera camera) {
		this.camera = camera;
		grabMouse();
		
		mapKeyHold(Keyboard.KEY_W, this::moveForward);
		mapKeyHold(Keyboard.KEY_S, this::moveBack);
		mapKeyHold(Keyboard.KEY_A, this::moveLeft);
		mapKeyHold(Keyboard.KEY_D, this::moveRight);
		mapKeyPress(Keyboard.KEY_R, camera::toggleFirstPeson);
		mapKeyHold(Keyboard.KEY_SPACE, this::ascend);
		mapKeyHold(Keyboard.KEY_LSHIFT, this::descend);
		mapKeyHold(Keyboard.KEY_ESCAPE, this::freeMouse);
		
		mouseBinds.add(this::rotateCam);
	}
	
	public void bindEntity(Entity entity) {
		camera.bindEntity(entity);
	}
	
	public void unbindEntity() {
		camera.unbindEntity();
	}
	
	public void mapKeyPress(int keyCode, Runnable onPress) {
		keyPressBinds.put(keyCode, onPress);
	}
	
	public void mapKeyHold(int keyCode, Runnable onHold) {
		keyHoldBinds.put(keyCode, onHold);
	}
	
	public void unmapKeyPress(int keyCode) {
		keyPressBinds.remove(keyCode);
	}
	
	public void unmapKeyHold(int keyCode) {
		keyHoldBinds.remove(keyCode);
	}
	
	public void unmapKey(int keyCode) {
		unmapKeyPress(keyCode);
		unmapKeyHold(keyCode);
	}
	
	public void tick() {
		if (mouseGrabbed()) {
			Vector2f mousePos = new Vector2f(Mouse.getX(), Mouse.getY());
			
			if (lastMousePos != null) {
				Vector2f delta = Vector2f.sub(mousePos, lastMousePos, null);
				
				for (Consumer<Vector2f> mouseHandler : mouseBinds) {
					mouseHandler.accept(delta);
				}
			}
		}
		
		for (int key : keyHoldBinds.keySet()) {
			if (Keyboard.isKeyDown(key)) {
				keyHoldBinds.get(key).run();
			}
		}
	}
	
	private boolean mouseGrabbed() {
		return Mouse.isGrabbed();
	}
	
	private void grabMouse() {
		Mouse.setGrabbed(true);
	}
	
	private void freeMouse() {
		Mouse.setGrabbed(false);
	}
	
	private void rotateCam(Vector2f delta) {
		camera.rotateYaw(delta.getX() * rotationSpeed);
		camera.rotatePitch(-delta.getY() * rotationSpeed);
	}
	
	private void ascend() {
		camera.ascend(moveSpeed);
	}
	
	private void descend() {
		camera.descend(moveSpeed);
	}
	
	private void moveForward() {
		camera.moveForward(moveSpeed);
	}
	
	private void moveBack() {
		camera.moveBackward(moveSpeed);
	}
	
	private void moveLeft() {
		camera.moveLeft(moveSpeed);
	}
	
	private void moveRight() {
		camera.moveRight(moveSpeed);
	}

	protected void onKeyDown(int pressedKey) {
		Runnable onPress = keyPressBinds.get(pressedKey);
		
		if (onPress != null) {
			onPress.run();
		}
	}
	
	protected void onKeyHold(int heldKey) {
		Runnable onHold = keyHoldBinds.get(heldKey);
		
		if (onHold != null) {
			onHold.run();
		}
	}

	protected void onKeyUp(int releasedKey) {
		
	}
	
	protected void onMouseMove(Vector2f delta) {
		if (mouseGrabbed()) {
			for (Consumer<Vector2f> mouseListener : mouseBinds) {
				mouseListener.accept(delta);
			}
		}
	}
	
	protected void onMouseDown(Vector2f pos, int mouseButton) {
		if (!mouseGrabbed()) {
			grabMouse();
		}
	}

	protected void onMouseHold(Vector2f pos, int mouseButton) {
		
	}
	
	protected void onMouseUp(Vector2f pos, int mouseButton) {
		
	}
}

package fwcd.sapphire.core;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class GameLoop {
	private final SceneWindow app;
	
	private final Set<Integer> activeKeys = new HashSet<>();
	private final Set<Integer> activeMouseButtons = new HashSet<>();
	
	private int maxFPS = 60; // 0 means unlimited
	private int maxTPS = 60; // 0 means unlimited
	private int maxIPS = 128; // 0 means unlimited
	private int cachedTickDelay = -1; // -1 means not determined
	private int cachedInputDelay = -1; // -1 means not determined
	
	private boolean running = true;
	private boolean logToConsole = true;
	
	private int frames = 0;
	private int ticks = 0;
	private int inputs = 0;
	
	private int fps = 0;
	private int tps = 0;
	private int ips = 0;
	
	public GameLoop(SceneWindow app) {
		this.app = app;
	}
	
	public void runTickLoop() {
		while (running) {
			app.tick();
			ticks++;
			
			try {
				Thread.sleep(tickDelay());
			} catch (InterruptedException e) {
				break; // Closes the tick loop upon interruption
			}
		}
	}
	
	public void runRenderLoop() {
		long lastSecond = now();
		while (!Display.isCloseRequested() && running) {
			if (fps > 0) {
				Display.sync(maxFPS);
			}
			
			app.render();
			frames++;
			
			if ((now() - lastSecond) > 1000) {
				fps = frames;
				tps = ticks;
				ips = inputs;
				
				frames = 0;
				ticks = 0;
				inputs = 0;
				lastSecond = now();
				
				if (logToConsole) {
					System.out.println(
							Integer.toString(fps) + " FPS "
							+ "(max. " + Integer.toString(maxFPS) + "), "
							+ Integer.toString(tps) + " TPS "
							+ "(max. " + Integer.toString(maxTPS) + "), "
							+ Integer.toString(ips) + " IPS "
							+ "(max. " + Integer.toString(maxIPS) + ")"
					);
				}
			}
		}
		running = false;
	}
	
	public void runInputDispatchLoop() {
		Controller controller = app.getController();
		
		while (running) {
			// Deal with keys
			
			while (Keyboard.next()) {
				boolean keyDown = Keyboard.getEventKeyState();
				int key = Keyboard.getEventKey();
				
				if (keyDown) {
					activeKeys.add(key);
					controller.onKeyDown(key);
				} else {
					activeKeys.remove(key);
					controller.onKeyUp(key);
				}
			}
			
			for (int key : activeKeys) {
				controller.onKeyHold(key);
			}

			// Deal with mouse buttons
			
			Vector2f mousePos = new Vector2f(Mouse.getX(), Mouse.getY());
			
			for (int mouseButton : activeMouseButtons) {
				controller.onMouseHold(mousePos, mouseButton);
			}
			
			while (Mouse.next()) {
				int mouseButton = Mouse.getEventButton();
				if (mouseButton >= 0) {
					boolean buttonDown = Mouse.getEventButtonState();
					
					if (buttonDown) {
						activeMouseButtons.add(mouseButton);
						controller.onMouseDown(mousePos, mouseButton);
					} else {
						activeMouseButtons.remove(mouseButton);
						controller.onMouseUp(mousePos, mouseButton);
					}
				}
			}
			
			
			// Deal with mouse movement
			
			controller.onMouseMove(new Vector2f(Mouse.getDX(), Mouse.getDY()));
			
			try {
				Thread.sleep(inputDelay());
			} catch (InterruptedException e) {
				break; // Break loop if interrupted
			}
			
			inputs++;
		}
	}

	public int getMaxFPS() {
		return maxFPS;
	}
	
	public int getMaxTPS() {
		return maxTPS;
	}
	
	/**
	 * Fetches the amount of frames rendered in the last second.
	 * 
	 * @return The frames per second
	 */
	public int getFPS() {
		return fps;
	}
	
	/**
	 * Fetches the amount of game ticks in the last second.
	 * Usually bounded to some fixed number.
	 * 
	 * @return The ticks per second
	 */
	public int getTPS() {
		return tps;
	}
	
	/**
	 * Fetches the amount of input dispatches in the last second.
	 * Usually bounded to some fixed number.
	 * 
	 * @return The input dispatches per second
	 */
	public int getIPS() {
		return ips;
	}
	
	/**
	 * Sets the maximum ticks per second. 0 means unlimited.
	 */
	public void setMaxTPS(int tps) {
		maxTPS = tps;
		cachedTickDelay = -1;
	}
	
	/**
	 * Sets the maximum input dispatches per second. 0 means unlimited.
	 * 
	 * @param ips - The maximum input dispatches per second
	 */
	public void setMaxIPS(int ips) {
		maxIPS = ips;
		cachedInputDelay = -1;
	}
	
	/**
	 * Sets the maximum frames per second. 0 means unlimited.
	 */
	public void setMaxFPS(int fps) {
		maxFPS = fps;
	}
	
	public void setVSyncEnabled(boolean vsync) {
		Display.setVSyncEnabled(vsync);
	}
	
	public void stopAll() {
		running = false;
	}
	
	public void setLogToConsole(boolean logToConsole) {
		this.logToConsole = logToConsole;
	}

	private long tickDelay() {
		if (cachedTickDelay < 0) {
			if (maxTPS <= 0) {
				cachedTickDelay = 0;
			} else {
				cachedTickDelay = 1000 / maxTPS;
			}
		}
		
		return cachedTickDelay;
	}
	
	private long inputDelay() {
		if (cachedInputDelay < 0) {
			if (maxIPS <= 0) {
				cachedInputDelay = 0;
			} else {
				cachedInputDelay = 1000 / maxIPS;
			}
		}
		
		return cachedInputDelay;
	}
	
	private long now() {
		return System.currentTimeMillis();
	}
}

package com.fwcd.sapphire.utils;

import org.lwjgl.util.vector.Vector3f;

public class RGBColor {
	public static final RGBColor RED = new RGBColor(255, 0, 0);
	public static final RGBColor GREEN = new RGBColor(0, 255, 0);
	public static final RGBColor BLUE = new RGBColor(0, 0, 255);
	public static final RGBColor BLACK = new RGBColor(0, 0, 0);
	public static final RGBColor WHITE = new RGBColor(255, 255, 255);
	public static final RGBColor YELLOW = new RGBColor(255, 255, 0);
	public static final RGBColor MAGENTA = new RGBColor(255, 0, 255);
	public static final RGBColor CYAN = new RGBColor(0, 255, 255);
	
	private final int rgb;
	@LazilyLoaded
	private Vector3f vector = null;
	
	public RGBColor(int rgb) {
		this.rgb = rgb;
	}
	
	public RGBColor(int r, int g, int b) {
		rgb = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
	}
	
	public int getRGB() {
		return rgb;
	}
	
	public int getR() {
		return (rgb >> 16) & 0xFF;
	}
	
	public int getG() {
		return (rgb >> 8) & 0xFF;
	}
	
	public int getB() {
		return rgb & 0xFF;
	}
	
	public float getFloatR() {
		return getR() / 255F;
	}
	
	public float getFloatG() {
		return getG() / 255F;
	}
	
	public float getFloatB() {
		return getB() / 255F;
	}
	
	public RGBColor brighter() {
		int r = Math.min(getR() + 40, 255);
		int g = Math.min(getG() + 40, 255);
		int b = Math.min(getB() + 40, 255);
		return new RGBColor(r, g, b);
	}
	
	public RGBColor darker() {
		int r = Math.max(getR() - 40, 0);
		int g = Math.max(getG() - 40, 0);
		int b = Math.max(getB() - 40, 0);
		return new RGBColor(r, g, b);
	}
	
	public Vector3f toVector() {
		if (vector == null) {
			vector = new Vector3f(
					getFloatR(),
					getFloatG(),
					getFloatB()
			);
		}
		
		return vector;
	}
}

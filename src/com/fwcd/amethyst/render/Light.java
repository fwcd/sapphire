package com.fwcd.amethyst.render;

import org.lwjgl.util.vector.Vector3f;

import com.fwcd.amethyst.utils.RGBColor;

public class Light {
	private Vector3f position;
	private RGBColor color;
	
	public Light(float x, float y, float z, RGBColor color) {
		this(new Vector3f(x, y, z), color);
	}
	
	public Light(Vector3f position, RGBColor color) {
		this.position = position;
		this.color = color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public RGBColor getColor() {
		return color;
	}
}

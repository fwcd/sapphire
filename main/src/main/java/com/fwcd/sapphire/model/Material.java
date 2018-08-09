package com.fwcd.sapphire.model;

public class Material {
	public static final Material DEFAULT = new Material(2, 0);
	public static final Material SHINY = new Material(3, 4);
	
	private final float shineDamper;
	private final float reflectivity;
	
	public Material(float shineDamper, float reflectivity) {
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}
}

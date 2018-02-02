package com.fwcd.amethyst.api;

import com.fwcd.amethyst.utils.AnyFile;
import com.fwcd.amethyst.utils.ResourceFile;

/**
 * A resource entity asset. Mainly a convenience
 * class to reduce boilerplate.
 * 
 * @author Fredrik
 *
 */
public class REA implements EntityAsset {
	private final ResourceFile model;
	private final ResourceFile texture;
	
	/**
	 * Creates a new resource entity asset.
	 * 
	 * @param modelResource - The location inside the resource folder inside the jar
	 * @param textureResource - The location inside the resource folder inside the jar
	 */
	public REA(String modelResource, String textureResource) {
		model = new ResourceFile("/resources/" + modelResource);
		texture = new ResourceFile("/resources/" + textureResource);
	}
	
	@Override
	public AnyFile getOBJModel() {
		return model;
	}

	@Override
	public AnyFile getTexture() {
		return texture;
	}
}

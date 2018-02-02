package com.fwcd.amethyst.api;

import com.fwcd.amethyst.utils.AnyFile;
import com.fwcd.amethyst.utils.ResourceFile;

/**
 * A resource terrain asset. Mainly a convenience
 * class to reduce boilerplate.
 * 
 * @author Fredrik
 *
 */
public class RTA implements TerrainAsset {
	private final ResourceFile bg;
	private final ResourceFile rTexture;
	private final ResourceFile gTexture;
	private final ResourceFile bTexture;
	private final ResourceFile blendMap;
	private final ResourceFile heightMap;
	
	/**
	 * Creates a new resource terrain asset. Locations are specified inside
	 * the resource folder of the JAR.
	 */
	public RTA(
			String bgResource,
			String rTexResource,
			String gTexResource,
			String bTexResource,
			String blendMapResource
	) {
		bg = new ResourceFile("/resources/" + bgResource);
		rTexture = new ResourceFile("/resources/" + rTexResource);
		gTexture = new ResourceFile("/resources/" + gTexResource);
		bTexture = new ResourceFile("/resources/" + bTexResource);
		blendMap = new ResourceFile("/resources/" + blendMapResource);
		heightMap = null;
	}
	
	/**
	 * Creates a new resource terrain asset. Locations are specified inside
	 * the resource folder of the JAR.
	 */
	public RTA(
			String bgResource,
			String rTexResource,
			String gTexResource,
			String bTexResource,
			String blendMapResource,
			String heightMapResource
	) {
		bg = new ResourceFile("/resources/" + bgResource);
		rTexture = new ResourceFile("/resources/" + rTexResource);
		gTexture = new ResourceFile("/resources/" + gTexResource);
		bTexture = new ResourceFile("/resources/" + bTexResource);
		blendMap = new ResourceFile("/resources/" + blendMapResource);
		heightMap = new ResourceFile("/resources/" + heightMapResource);
	}

	@Override
	public AnyFile getBGTexture() {
		return bg;
	}

	@Override
	public AnyFile getRTexture() {
		return rTexture;
	}

	@Override
	public AnyFile getGTexture() {
		return gTexture;
	}

	@Override
	public AnyFile getBTexture() {
		return bTexture;
	}

	@Override
	public AnyFile getBlendMap() {
		return blendMap;
	}

	@Override
	public AnyFile getHeightMap() {
		return heightMap;
	}
}

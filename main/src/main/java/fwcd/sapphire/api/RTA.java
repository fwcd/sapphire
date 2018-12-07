package fwcd.sapphire.api;

import fwcd.sapphire.utils.AnyFile;
import fwcd.sapphire.utils.ResourceFile;

/**
 * A resource terrain asset. Mainly a convenience
 * class to reduce boilerplate.
 * 
 * @author fwcd
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
		bg = new ResourceFile("/" + bgResource);
		rTexture = new ResourceFile("/" + rTexResource);
		gTexture = new ResourceFile("/" + gTexResource);
		bTexture = new ResourceFile("/" + bTexResource);
		blendMap = new ResourceFile("/" + blendMapResource);
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
		bg = new ResourceFile("/" + bgResource);
		rTexture = new ResourceFile("/" + rTexResource);
		gTexture = new ResourceFile("/" + gTexResource);
		bTexture = new ResourceFile("/" + bTexResource);
		blendMap = new ResourceFile("/" + blendMapResource);
		heightMap = new ResourceFile("/" + heightMapResource);
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

package com.fwcd.sapphire.render;

import com.fwcd.sapphire.utils.ResourceFile;

public class TerrainShader extends CommonShader {
	private static final ResourceFile VERTEX_FILE = new ResourceFile("/resources/shaders/terrainVertexShader.vert");
	private static final ResourceFile FRAGMENT_FILE = new ResourceFile("/resources/shaders/terrainFragmentShader.frag");
	
	private Uniform backgroundTexture;
	private Uniform rTexture;
	private Uniform gTexture;
	private Uniform bTexture;
	private Uniform blendMap;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void registerUniforms() {
		super.registerUniforms();
		backgroundTexture = createUniform("backgroundTexture");
		rTexture = createUniform("rTexture");
		gTexture = createUniform("gTexture");
		bTexture = createUniform("bTexture");
		blendMap = createUniform("blendMap");
	}
	
	public void connectTextureUnits() {
		// These "magic numbers" are the terrainSlots used in TerrainRenderer.bindTextures()
		
		backgroundTexture.setInt(0);
		rTexture.setInt(1);
		gTexture.setInt(2);
		bTexture.setInt(3);
		blendMap.setInt(4);
	}
}

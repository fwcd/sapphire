package com.fwcd.sapphire.render;

import com.fwcd.sapphire.utils.ResourceFile;

public class StaticShader extends CommonShader {
	private static final ResourceFile VERTEX_FILE = new ResourceFile("/resources/shaders/vertexShader.vert");
	private static final ResourceFile FRAGMENT_FILE = new ResourceFile("/resources/shaders/fragmentShader.frag");

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
}

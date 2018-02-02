package com.fwcd.amethyst.render;

import com.fwcd.amethyst.utils.ResourceFile;

public class StaticShader extends CommonShader {
	private static final ResourceFile VERTEX_FILE = new ResourceFile("/resources/shaders/vertexShader.txt");
	private static final ResourceFile FRAGMENT_FILE = new ResourceFile("/resources/shaders/fragmentShader.txt");

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
}

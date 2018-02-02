package com.fwcd.amethyst.model;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import com.fwcd.amethyst.utils.AnyFile;

public class Texture {
	// Between 0 and 255 - The number above which all alpha will actually be rendered transparent (lower numbers might affect performance thus)
	private static final int ALPHA_THRESHOLD = 10;
	
	private int width;
	private int height;
	private int textureID;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	protected Texture(int id) {
		textureID = id;
	}
	
	protected Texture(AnyFile file) {
		textureID = file.mapStream(this::load);
	}
	
	private int load(InputStream stream) {
		int[] pixels = null;
		try {
			BufferedImage image = ImageIO.read(stream);
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		int[] data = new int[width * height];
		for (int i=0; i<width*height; i++) {
			int a = (pixels[i] & 0xFF000000) >> 24;
			int r = (pixels[i] & 0xFF0000) >> 16;
			int g = (pixels[i] & 0xFF00) >> 8;
			int b = pixels[i] & 0xFF;
			
			if (a > ALPHA_THRESHOLD) {
				hasTransparency = true;
			}
			
			data[i] = (a << 24) | (b << 16) | (g << 8) | r;
		}
		
		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
		buffer.put(data).flip();

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glBindTexture(GL_TEXTURE_2D, 0);
		return result;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getID() {
		return textureID;
	}
	
	public void setTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public boolean hasTransparency() {
		return hasTransparency;
	}

	public boolean usesFakeLighting() {
		return useFakeLighting;
	}

	public void setFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
}

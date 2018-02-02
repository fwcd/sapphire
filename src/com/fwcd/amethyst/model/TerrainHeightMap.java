package com.fwcd.amethyst.model;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.fwcd.amethyst.utils.AnyFile;

public class TerrainHeightMap {
	private static final float MAX_PIXEL_COLOR = 255 * 255 * 255;
	
	private final float maxTerrainHeight;
	private final float minTerrainHeight;
	private final int imgWidth;
	private final int imgHeight;
	private final int[] rgbPixels;
	
	public TerrainHeightMap(AnyFile file, float minTerrainHeight, float maxTerrainHeight) {
		this.minTerrainHeight = minTerrainHeight;
		this.maxTerrainHeight = maxTerrainHeight;
		
		BufferedImage img = file.mapStream(ImageIO::read);
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		rgbPixels = img.getRGB(0, 0, imgWidth, imgHeight, null, 0, imgWidth);
	}
	
	public int getImageWidth() {
		return imgWidth;
	}
	
	public int getImageHeight() {
		return imgHeight;
	}
	
	private int getRGB(int imgX, int imgY) {
		return rgbPixels[(imgY * imgWidth) + imgX];
	}
	
	public float getHeightAt(int sceneX, int sceneZ) {
		if (sceneX < 0 || sceneX >= imgWidth || sceneZ < 0 || sceneZ >= imgHeight) {
			return 0; // For any coordinate out of bounds - TODO: Might find a better solution for this
		}
		
		float height = getRGB(sceneX, sceneZ);
		height += MAX_PIXEL_COLOR;
		height /= MAX_PIXEL_COLOR; // Squeeze into range [0; 1]
		height *= (maxTerrainHeight - minTerrainHeight);
		height += minTerrainHeight;
		
		return height;
	}
}

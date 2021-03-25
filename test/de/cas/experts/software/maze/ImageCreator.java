package de.cas.experts.software.maze;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ImageCreator {
	
	private static final int BLACK = 0;
	private static final int WHITE = 255;
	
	private static final char BLACK_STRING = '#';
	
	public static BufferedImage createImageFromStrings(String[] imageStrings) {
		int width = imageStrings[0].length();
		int height = imageStrings.length;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = image.getRaster();
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
		    	if(imageStrings[y].charAt(x) == BLACK_STRING) {
		    		setBlack(raster, x, y);
		    	} else {
		    		setWhite(raster, x, y);
		    	}
		    }
		}
		return image;
	}
	
	private static void setBlack(WritableRaster raster, int x, int y) {
		raster.setSample(x, y, 0, BLACK);
	}
	
	private static void setWhite(WritableRaster raster, int x, int y) {
		raster.setSample(x, y, 0, WHITE);
	}
}

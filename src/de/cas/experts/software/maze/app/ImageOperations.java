package de.cas.experts.software.maze.app;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.List;

import de.cas.experts.software.maze.Location;

public class ImageOperations {
	
	private static final int[] RED = new int[] {255, 0, 0};
	private static final int[] BLUE = new int[] {0, 0, 255};
	
	private static final int BLACK = 0;
	
	public static BufferedImage addPathToImage(BufferedImage image, List<Location> path) {
		BufferedImage copy = copyImage(image);
	    WritableRaster raster = copy.getRaster();
	    for(Location location : path) {
	    	setPathColor(raster, location);
	    }
	    if(path.size() > 0) {
	    	setPOIColor(raster, path.get(0));
	    	setPOIColor(raster, path.get(path.size() - 1));
	    }
	    return copy;
	}

	private static void setPathColor(WritableRaster raster, Location location) {
		if(raster.getSampleModel().getNumBands() == 3) {
			raster.setPixel(location.getColumn(), location.getRow(), RED);
		} else {
			raster.setSample(location.getColumn(), location.getRow(), 0, BLACK);
		}
	}
	
	private static void setPOIColor(WritableRaster raster, Location location) {
		if(raster.getSampleModel().getNumBands() == 3) {
			raster.setPixel(location.getColumn(), location.getRow(), BLUE);
		} else {
			raster.setSample(location.getColumn(), location.getRow(), 0, BLACK);
		}
	}
	
	

	private static BufferedImage copyImage(BufferedImage image) {
		BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
	    Graphics graphics = copy.getGraphics();
	    graphics.drawImage(image, 0, 0, null);
	    graphics.dispose();
		return copy;
	}
}

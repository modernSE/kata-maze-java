package de.cas.experts.software.maze;

import static java.util.stream.Collectors.toList;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageGraph {
	
	private enum Pixel {
		WALL, PATH
	}
	
	private int numRows = 0;
	private int numCols = 0;
	private Map<Location, Pixel> pixels = new HashMap<>();
	
	private ImageGraph(Map<Location, Pixel> pixels, int numRows, int numCols) {
		this.pixels = pixels;
		this.numRows = numRows;
		this.numCols = numCols;
	}
	
	public static ImageGraph fromImage(BufferedImage image) {
		int numRows = image.getHeight();
		int numCols = image.getWidth();
		Map<Location, Pixel> pixels = new HashMap<>();
		for(int row = 0; row < numRows; ++row) {
			for(int column = 0; column < numCols; ++column) {
				if((image.getRGB(column, row) & 0xFFFFFF) > 0) {
					pixels.put(new Location(row, column), Pixel.PATH);
				} else {
					pixels.put(new Location(row, column), Pixel.WALL);
				}
			}
		}
		return new ImageGraph(pixels, numRows, numCols);
	}
	
	List<Location> getNeighbors(Location location) {
		List<Offset> offsets = Arrays.asList(new Offset(-1, 0), new Offset(0, 1), new Offset(1, 0), new Offset(0, -1));
		return offsets.stream().map(location::addOffset).filter(this::isInside).filter(this::isPath).collect(toList());
	}

	public int getNumberOfRows() {
		return numRows;
	}
	
	public int getNumberOfColumns() {
		return numCols;
	}
	
	public boolean isPath(Location location) {
		return getPixel(location) == Pixel.PATH;
	}

	public boolean isWall(Location location) {
		return getPixel(location) == Pixel.WALL;
	}
	
	private Pixel getPixel(Location location) {
		return pixels.get(location);
	}

	private boolean isInside(Location location) {
		return (0 <= location.getColumn() && location.getColumn() < getNumberOfColumns() && //
				0 <= location.getRow() && location.getRow() < getNumberOfRows());
	}

}

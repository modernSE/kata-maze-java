package de.cas.experts.software.maze;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MazeSolverTest {
	
	private ImageGraph graph;
	private MazeSolver mazeSolver;
	
	private Location origin;
	private Location destination;
	
	private List<Location> path;

	@BeforeEach
	void setUp() {
		String[] imageStrings = {
				"#.######",
				"#.##...#",
				"#....###",
				"#.##.#.#",
				"#..#####",};
		BufferedImage image = ImageCreator.createImageFromStrings(imageStrings);
		graph = ImageGraph.fromImage(image);
		mazeSolver = new MazeSolver(image);
	}
	
	@Test
	void testOriginDestinationEqual() {
		givenOrigin(loc(1, 1));
		givenDestination(loc(1, 1));
		
		whenFindPath();
		
		thenPathIsValid();
	}

	@Test
	void testPath() {
		givenOrigin(loc(1, 1));
		givenDestination(loc(1, 6));
		
		whenFindPath();
		
		thenPathIsValid();
	}
	
	@Test
	void testPathOnBorder() {
		givenOrigin(loc(0, 1));
		givenDestination(loc(4, 2));
		
		whenFindPath();
		
		thenPathIsValid();
	}
	
	@Test
	void testOriginOnWall() {
		givenOrigin(loc(1, 2));
		givenDestination(loc(4, 2));
		
		whenFindPath();
		
		thenPathIsEmpty();
	}

	@Test
	void testNotConnected() {
		givenOrigin(loc(1, 1));
		givenDestination(loc(3, 6));
		
		whenFindPath();
		
		thenPathIsEmpty();
	}
	
	private void whenFindPath() {
		path = mazeSolver.findPath(origin, destination);
	}
	
	private void thenPathIsEmpty() {
		assertEquals(path.size(), 0);
	}
	
	private void givenDestination(Location loc) {
		destination = loc;
	}

	private void givenOrigin(Location loc) {
		origin = loc;
	}

	private Location loc(int row, int column) {
		return new Location(row, column);
	}
	
	private void thenPathIsValid() {
		assertFalse(path.isEmpty());
		assertEquals(origin, path.get(0));
		assertEquals(destination, path.get(path.size() - 1));
		
		for (Location location : path) {
			assertTrue(graph.isPath(location));
		}
		
		if(path.size() > 1) {
			for(int i = 0; i < path.size() - 1; ++i) {
				Location from = path.get(i);
				Location to = path.get(i + 1);
				assertEquals(from.manhattanDistance(to), 1);
			}
		}
		
	}
	
}

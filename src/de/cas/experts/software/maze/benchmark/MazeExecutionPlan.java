package de.cas.experts.software.maze.benchmark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import de.cas.experts.software.maze.ImageGraph;
import de.cas.experts.software.maze.Location;

@State(Scope.Benchmark)
public class MazeExecutionPlan {
	
	/**
	 * 512*384
	 */
	private static final String CRACK = "crack.png";
	
	/**
	 * 1024*768
	 */
	private static final String THESEUS = "theseus.png";
	
	/**
	 * 5769*5769
	 * 
	 * Reduce NUM_QUERIES to 1, increase Java heap space.
	 */
	private static final String LARGER = "larger.png";
	
	private Random random;
	
	@Param({CRACK})
	public String imageFile;
	
	static final int NUM_QUERIES = 100;
	
	private ImageGraph graph;
	private List<Query> queries;
	
	@Setup(Level.Trial)
	public void setUp() throws IOException {
		String imageFilePath = System.getProperty("user.dir") + "/data/mazes/" + imageFile;
		BufferedImage image = ImageIO.read(new File(imageFilePath));
		graph = ImageGraph.fromImage(image);
	}
	
	@Setup(Level.Iteration)
	public void setUpIteration() {
		random = new Random(42);
		queries = new ArrayList<>(NUM_QUERIES);
		
		for (int i = 0; i < NUM_QUERIES; ++i) {
			Location origin = randomGraphLocation(random);
			Location destination = randomGraphLocation(random);
			queries.add(new Query(origin, destination));
		}
	}
	
	private Location randomGraphLocation(Random random) {
		Location location = randomLocation(random);
		while(graph.isWall(location)) {
			location = randomLocation(random);
		}
		return location;
	}
	
	private Location randomLocation(Random random) {
		return new Location(random.nextInt(graph.getNumberOfRows()), random.nextInt(graph.getNumberOfColumns()));
	}

	public ImageGraph getGraph() {
		return graph;
	}

	public List<Query> getQueries() {
		return queries;
	}
}

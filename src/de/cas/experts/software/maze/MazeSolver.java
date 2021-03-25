package de.cas.experts.software.maze;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class MazeSolver {
	
	private ImageGraph imageGraph;
	
	public MazeSolver(BufferedImage image) {
		imageGraph = ImageGraph.fromImage(image);
	}
	
	public List<Location> findPath(Location origin, Location destination) {
		return findPath(imageGraph, origin, destination);
	}
	
	public static List<Location> findPath(ImageGraph graph, Location origin, Location destination) {
		if(graph.isWall(origin) || graph.isWall(destination)) {
			return Collections.emptyList();
		}
		Map<Location, Location> parents = performBFS(graph, origin);
		List<Location> path = reconstructPath(origin, destination, parents);
		return path;
	}

	private static Map<Location, Location> performBFS(ImageGraph graph, Location origin) {
		Queue<Location> q = new LinkedList<>();
		q.add(origin);
		
		Map<Location, Location> parents = new HashMap<>();
		parents.put(origin,  origin);
		
		while(!q.isEmpty()) {
			Location current = q.remove();
			List<Location> neighbors = graph.getNeighbors(current);
			
			for(Location neighbor : neighbors) {
				if(!parents.containsKey(neighbor)) {
					parents.put(neighbor,  current);
					q.add(neighbor);
				}
			}
		}
		return parents;
	}
	
	private static List<Location> reconstructPath(Location origin, Location destination,
			Map<Location, Location> parents) {
		List<Location> path = new ArrayList<>();
		if(parents.containsKey(destination)) {
			path.add(destination);
			Location location = destination;
			while(!location.equals(origin)) {
				location = parents.get(location);
				path.add(location);
			}
			Collections.reverse(path);
		}
		return path;
	}

}

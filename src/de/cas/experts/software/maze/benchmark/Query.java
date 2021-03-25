package de.cas.experts.software.maze.benchmark;

import de.cas.experts.software.maze.Location;

public class Query {
	public final Location origin;
	public final Location destination;
	
	public Query(Location origin, Location destination) {
		this.origin = origin;
		this.destination = destination;
	}
}
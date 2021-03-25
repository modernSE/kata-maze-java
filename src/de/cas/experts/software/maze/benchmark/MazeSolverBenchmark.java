package de.cas.experts.software.maze.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import de.cas.experts.software.maze.MazeSolver;

@Warmup(iterations = 1)
@Measurement(iterations = 1)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 0)
public class MazeSolverBenchmark {
	
	@Benchmark
	@OperationsPerInvocation(MazeExecutionPlan.NUM_QUERIES)
	public void findPath(MazeExecutionPlan executionPlan, Blackhole bh) {
		for(Query query : executionPlan.getQueries()) {
			bh.consume(MazeSolver.findPath(executionPlan.getGraph(), query.origin, query.destination));
		}
	}

}

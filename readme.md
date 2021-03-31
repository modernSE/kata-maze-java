# Maze solver kata
Load images representing mazes, find paths.

## Task
Performance! Optimize the code and squeeze every drop of performance out of it. How fast can you go?

4 times faster?  
40 times faster!?  
or even ... more?

The rules:
* JUnit Tests have to pass.
* The signatures of all public methods must remain untouched. All other (i.e. private, protected, or package-private) members, methods, or classes can be altered as you deem necessary.
* Try to stay clean. Hide complexity and low-level optimizations.

## Technical Details
It's a maven project. So to open it in Eclipse, use Import > Existing Maven Project ...
Tested with OpenJDK 11.

The package `de.cas.experts.software.maze` contains the algorithm and data structures you have to optimize.

The sub-package `.benchmark` contains benchmark code. Building it requires maven. The maven build uses the shade plugin to build a benchmark.jar:

        $ mvn clean package
        $ java -jar target/benchmark.jar

To run the benchmark from eclipse, right click pom.xml > Run as > Maven build > clean package. Then simply run the MazeSolverBenchmark: Run as > Java Application

The sub-package `.app` contains the UI part. You can have a look at it if you find it interesting, but there is not much to do here.

## The mazes
The mazes are taken from this a*maze*ing [collection](https://www.astrolog.org/labyrnth/maze.htm). Feel free to try other mazes as well, for best support convert them to 24bit .png.

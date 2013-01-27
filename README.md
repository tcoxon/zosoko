# 造倉庫 Zōsōko

Sōkoban puzzle generator

## Options

* -width=W Sets the width of generated puzzles. Must be a multiple of 3.
* -height=H Sets the height of generated puzzles. Must be a multiple of 3.
* -boxes=N Sets the number of boxes and goals in generated puzzles.
* -unbounded Allow the player to walk around outside the bounds of the puzzle.
* -test Always generates the puzzle designed for testing the renderer and controller.
* -limit-goal-experiments=N Limit the number of combinations of goals the generator tries for each map.
* -limit-depth=N Limit the depth of the farthest state search.
* -box-lines Sets the scoring metric to purely box-lines rather than the default based on the paper.

The best results seem to be given with -box-lines.

## Controls

* Blue squares are boxes.
* The red circle is the player.
* Use the arrow keys to move the player and push boxes.
* Press R to reset the puzzle.
* Press F5 to generate a new puzzle.


## TODO

* Needs significant clean-up.
* Profiling indicates the slowest part is PuzzleMap.getPlayerSpacePartition().
  The use of this can be eliminated entirely by implementing the suggestions in
  PlayerCloud, or maybe there is a way to speed it up?
* After that is Coords.add, which must mean that there are no further
  bottlenecks.
* See if making FarthestStateFinder.go return a set of states would make the
  default scoring metric work better.


## Links

The paper this library is based on:

* [http://larc.unt.edu/ian/pubs/GAMEON-NA_METH_03.pdf](http://larc.unt.edu/ian/pubs/GAMEON-NA_METH_03.pdf)


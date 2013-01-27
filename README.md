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

## Controls

* Blue squares are boxes.
* The red circle is the player.
* Use the arrow keys to move the player and push boxes.
* Press R to reset the puzzle.
* Press F5 to generate a new puzzle.


## TODO

* Add optional limits to farthest state depth
* Implement PuzzleGenerator.score()


## Links

The paper this project is based on:

* [http://larc.unt.edu/ian/pubs/GAMEON-NA_METH_03.pdf](http://larc.unt.edu/ian/pubs/GAMEON-NA_METH_03.pdf)


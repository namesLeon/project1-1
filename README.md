# Graph Colouring Game

A Java Swing application for exploring and playing graph-colouring games. The
application can display graphs, generate connected random graphs, estimate
their chromatic number, and provide three timed game modes with persistent
leaderboards.

## Features

- **Sandbox**: load a graph from a text file or generate a random graph.
- **Exact algorithm**: use a DSATUR-based branch-and-bound search to find the
  chromatic number. The search has a 30-second time limit.
- **Upper bound**: calculate a colouring upper bound using a greedy ordering.
- **Lower bound**: calculate a lower bound from the size of a maximum clique
  using the Bron-Kerbosch algorithm.
- **Special-case recognition**: detect common graph families such as complete,
  bipartite, cycle, star, path, tree, and wheel graphs.
- **Game modes**:
  - **Random Order**: colour vertices in a computer-selected order; coloured
    vertices cannot be changed.
  - **I Changed My Mind**: colour vertices in a fixed order with undo support
    for recent moves.
  - **To The Bitter End**: colour any vertex in any order as quickly as
    possible.
- **Hints and colour generation**: use computer suggestions or generate
  additional visually distinct colours.
- **Leaderboards**: scores are stored under `App/Games/Leaderboards/`.

## Requirements

- Java Development Kit (JDK) 8 or newer.
- A graphical desktop environment capable of running Swing applications.

No external libraries or build tools are required.

## Running the application

The main class is `App.Main.Main`.

### From an IDE

Import the repository as a Java project and run
`App.Main.Main.main()`. Set the working directory to the directory **above**
the repository root if images, sounds, graph files, or leaderboards are not
found. The current source uses paths beginning with `project-1.1/`.

### From PowerShell

From the repository root:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem -Recurse -Filter *.java App | ForEach-Object { $_.FullName })
Set-Location ..
java -cp "project-1.1\out" App.Main.Main
```

Running from the parent directory is intentional: the application currently
uses relative paths such as `project-1.1/App/Images/graphColoring.png` and
`project-1.1/App/Games/Leaderboards`.

## Using the application

1. Start the application to open the main menu.
2. Select **Sandbox** to inspect or colour a graph and run the available
   algorithms.
3. Select a game mode to read its instructions, generate a graph, and begin
   colouring.
4. Choose colours for vertices without assigning the same colour to adjacent
   vertices.
5. Enter a username at the end of a game to save the score, or open a mode's
   leaderboard to view saved scores.

Random graph generation requires a positive number of vertices and a number of
edges between `vertices - 1` and
`vertices * (vertices - 1) / 2`. Generated graphs are connected.

## Graph file format

Graphs are stored in `App/Txt/Graphs/`. The parser expects an undirected graph
with one-based vertex identifiers:

```text
// Optional comments
VERTICES = 4
EDGES = 4
1 2
2 3
3 4
4 1
```

`EDGES` must be followed by exactly that many lines, with each line containing
two vertex identifiers separated by a space. Existing sample files include
`graph01_2022.txt` through `graph20_2022.txt` and `testgraph.txt`.

## Project structure

```text
App/
├── Algorithms/       Graph generation, bounds, exact colouring, and special cases
├── GUI/              Swing screens, graph visualisation, sound effects, and assets
├── Games/            The three game modes and leaderboard persistence
├── Main/             Application entry point
└── Txt/              Graph parser and sample graph files
```

## Scoring and persistence

Scores are calculated from the number of colours, elapsed time, vertices, and
edges. Each game mode has its own leaderboard file:

- `App/Games/Leaderboards/RandomOrder_leaderBoard.txt`
- `App/Games/Leaderboards/IChangedMyMind_leaderboard.txt`
- `App/Games/Leaderboards/ToTheBitterEnd_leaderboard.txt`

The application creates the leaderboard directory when needed. Running the
application may therefore modify these text files.

## Troubleshooting

- **The window does not start**: ensure a JDK is installed and that the
  `java` and `javac` commands are on `PATH`.
- **Missing image or sound**: run with the working directory set to the
  directory above the repository, as described in
  [Running the application](#running-the-application).
- **Graph input errors**: check the `VERTICES`, `EDGES`, and edge-line counts,
  and use one-based vertex IDs.
- **Exact algorithm timeout**: exact colouring is exponential in the general
  case. Use the upper and lower bounds for larger graphs, or wait for the
  exact search to finish within its time limit.

## License

No license file is currently included in this repository. Add a license before
redistributing the project.
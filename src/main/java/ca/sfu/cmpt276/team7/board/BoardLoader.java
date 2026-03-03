package ca.sfu.cmpt276.team7.board;

import ca.sfu.cmpt276.team7.cells.BarrierCell;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.cells.WallCell;
import ca.sfu.cmpt276.team7.core.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads a {@link Board} from a simple ASCII map file.
 * <p>
 * Legend:
 * <ul>
 *   <li># = Wall</li>
 *   <li>. = Floor</li>
 *   <li>B = Barrier</li>
 *   <li>S = Start (stored as FloorCell + board.setStartPosition)</li>
 *   <li>E = Exit  (stored as FloorCell + board.setEndPosition)</li>
 *   <li>K = Key marker (stored as FloorCell; position added to keyPositions)</li>
 *   <li>T = Trap marker (stored as FloorCell; position added to trapPositions)</li>
 *   <li>G = Goblin spawn marker (stored as FloorCell; position added to goblinSpawns)</li>
 *   <li>O = Ogre spawn marker (stored as FloorCell; position added to ogreSpawns)</li>
 * </ul>
 * </p>
 * <p>
 * Note: This loader does NOT create reward/punishment objects.
 * It only builds the Board's structural grid and records marker positions.
 * Gameplay systems can later interpret markers and place rewards/traps/enemies.
 * </p>
 */
public final class BoardLoader {

    /**
     * Container for the loaded Board plus marker/spawn positions.
     */
    public static final class Result {
        private final Board board;
        private final List<Position> goblinSpawns;
        private final List<Position> ogreSpawns;
        private final List<Position> keyPositions;
        private final List<Position> trapPositions;

        /**
         * @param board loaded board
         * @param goblinSpawns goblin spawn marker positions
         * @param ogreSpawns ogre spawn marker positions
         * @param keyPositions key marker positions
         * @param trapPositions trap marker positions
         */
        public Result(Board board,
                      List<Position> goblinSpawns,
                      List<Position> ogreSpawns,
                      List<Position> keyPositions,
                      List<Position> trapPositions) {
            this.board = board;
            this.goblinSpawns = goblinSpawns;
            this.ogreSpawns = ogreSpawns;
            this.keyPositions = keyPositions;
            this.trapPositions = trapPositions;
        }

        /** @return loaded board */
        public Board getBoard() {
            return board;
        }

        /** @return goblin spawn marker positions */
        public List<Position> getGoblinSpawns() {
            return goblinSpawns;
        }

        /** @return ogre spawn marker positions */
        public List<Position> getOgreSpawns() {
            return ogreSpawns;
        }

        /** @return key marker positions */
        public List<Position> getKeyPositions() {
            return keyPositions;
        }

        /** @return trap marker positions */
        public List<Position> getTrapPositions() {
            return trapPositions;
        }
    }

    private BoardLoader() {
        // Utility class: no instances
    }

    /**
     * Loads a Board from an ASCII map file.
     *
     * @param mapPath path to the map file
     * @return result containing board + marker lists
     * @throws IOException if file cannot be read
     * @throws IllegalArgumentException if map is invalid (non-rectangular, missing S/E, unknown chars)
     */
    public static Result load(Path mapPath) throws IOException {
        if (mapPath == null) throw new IllegalArgumentException("mapPath cannot be null");

        List<String> lines = readNonEmptyLines(mapPath);
        if (lines.isEmpty()) throw new IllegalArgumentException("Map file is empty");

        int height = lines.size();
        int width = lines.get(0).length();

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).length() != width) {
                throw new IllegalArgumentException(
                        "Map must be rectangular. Line " + (i + 1) + " has different length"
                );
            }
        }

        Cell[][] grid = new Cell[height][width];

        Position start = null;
        Position end = null;

        List<Position> goblinSpawns = new ArrayList<Position>();
        List<Position> ogreSpawns = new ArrayList<Position>();
        List<Position> keyPositions = new ArrayList<Position>();
        List<Position> trapPositions = new ArrayList<Position>();

        for (int y = 0; y < height; y++) {
            String row = lines.get(y);
            for (int x = 0; x < width; x++) {
                char c = row.charAt(x);
                Position pos = new Position(x, y);

                Cell cell;
                switch (c) {
                    case '#':
                        cell = new WallCell(pos);
                        break;
                    case 'B':
                        cell = new BarrierCell(pos);
                        break;
                    case '.':
                        cell = new FloorCell(pos);
                        break;

                    case 'S':
                        start = pos;
                        cell = new FloorCell(pos);
                        break;

                    case 'E':
                        end = pos;
                        cell = new FloorCell(pos);
                        break;

                    case 'K':
                        keyPositions.add(pos);
                        cell = new FloorCell(pos);
                        break;

                    case 'T':
                        trapPositions.add(pos);
                        cell = new FloorCell(pos);
                        break;

                    case 'G':
                        goblinSpawns.add(pos);
                        cell = new FloorCell(pos);
                        break;

                    case 'O':
                        ogreSpawns.add(pos);
                        cell = new FloorCell(pos);
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown map char '" + c + "' at (" + x + "," + y + ")");
                }

                grid[y][x] = cell;
            }
        }

        if (start == null) throw new IllegalArgumentException("Map missing start 'S'");
        if (end == null) throw new IllegalArgumentException("Map missing exit 'E'");

        Board board = new Board(grid);
        board.setStartPosition(start);
        board.setEndPosition(end);

        return new Result(board, goblinSpawns, ogreSpawns, keyPositions, trapPositions);
    }

    /**
     * Reads all non-empty lines from the map file
     *
     * @param mapPath map file path
     * @return list of non-empty lines
     * @throws IOException if file cannot be read
     */
    private static List<String> readNonEmptyLines(Path mapPath) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = Files.newBufferedReader(mapPath);
        try {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(rstrip(line));
                }
            }
        } finally {
            br.close();
        }
        return lines;
    }

    /**
     * Removes trailing spaces without destroying leading spaces (if any)
     *
     * @param s input string
     * @return string without trailing whitespace
     */
    private static String rstrip(String s) {
        int end = s.length();
        while (end > 0 && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }
        return s.substring(0, end);
    }
}
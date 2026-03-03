package ca.sfu.cmpt276.team7.board;

import ca.sfu.cmpt276.team7.cells.*;
import ca.sfu.cmpt276.team7.core.*;
import ca.sfu.cmpt276.team7.punishments.*;
import ca.sfu.cmpt276.team7.rewards.*;

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
 *   <li>K = Key (RewardCell with RegularReward)</li>
 *   <li>T = Trap (PunishmentCell with SpikeTrapPunishment)</li>
 *   <li>G = Goblin spawn (stored as FloorCell; position added to goblinSpawns)</li>
 *   <li>O = Ogre spawn   (stored as FloorCell; position added to ogreSpawns)</li>
 * </ul>
 * </p>
 */
public final class BoardLoader {

    /**
     * a simple container for the loaded Board plus spawn positions
     */
    public static final class Result {
        private final Board board;
        private final List<Position> goblinSpawns;
        private final List<Position> ogreSpawns;

        /**
         * @param board loaded board
         * @param goblinSpawns list of goblin spawn positions
         * @param ogreSpawns list of ogre spawn positions
         */
        public Result(Board board, List<Position> goblinSpawns, List<Position> ogreSpawns) {
            this.board = board;
            this.goblinSpawns = goblinSpawns;
            this.ogreSpawns = ogreSpawns;
        }

        /** @return loaded board */
        public Board getBoard() {
            return board;
        }

        /** @return goblin spawn positions */
        public List<Position> getGoblinSpawns() {
            return goblinSpawns;
        }

        /** @return ogre spawn positions */
        public List<Position> getOgreSpawns() {
            return ogreSpawns;
        }
    }

    private BoardLoader() {
        // Utility class: no instances
    }

    /**
     * loads a Board from an ASCII map file
     *
     * @param mapPath path to the map file
     * @return result containing board + spawn lists
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
            if (lines.get(i).length() != width) throw new IllegalArgumentException("Map must be rectangular. Line " + (i + 1) + " has different length");
        }

        Cell[][] grid = new Cell[height][width];
        Board board = new Board(grid); // temporary; we'll fill grid next

        List<Position> goblinSpawns = new ArrayList<Position>();
        List<Position> ogreSpawns = new ArrayList<Position>();

        for (int y = 0; y < height; y++) {
            String row = lines.get(y);
            for (int x = 0; x < width; x++) {
                char c = row.charAt(x);
                Position pos = new Position(x, y);

                Cell cell;
                switch (c) {
                    case '#':
                        cell = new WallCell(pos); // concrete class in cells 
                        break;
                    case 'B':
                        cell = new BarrierCell(pos); // concrete class in cells 
                        break;
                    case '.':
                        cell = new FloorCell(pos); // concrete class in cells 
                        break;
                    case 'S':
                        board.setStartPosition(pos);
                        cell = new FloorCell(pos); // concrete class in cells 
                        break;
                    case 'E':
                        board.setEndPosition(pos);
                        cell = new FloorCell(pos); // concrete class in cells 
                        break;
                    case 'K':
                        // keep values simple; can tweak later
                        cell = new RewardCell(pos, new RegularReward(1)); // concrete class in cells 
                        break;
                    case 'T':
                        cell = new PunishmentCell(pos, new SpikeTrapPunishment(5));
                        break;
                    case 'G':
                        goblinSpawns.add(pos);
                        cell = new FloorCell(pos); // concrete class in cells 
                        break;
                    case 'O':
                        ogreSpawns.add(pos);
                        cell = new FloorCell(pos); // concrete class in cells 
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown map char '" + c + "' at (" + x + "," + y + ")");
                }

                grid[y][x] = cell;
            }
        }

        if (board.getStartPosition() == null) throw new IllegalArgumentException("Map missing start 'S'.");
        if (board.getEndPosition() == null) throw new IllegalArgumentException("Map missing exit 'E'.");

        // Board constructor validated grid for nulls; now it's filled, so we are good
        // However, note: we constructed Board before filling. That means validation ran too early
        // To keep it clean, we should construct Board AFTER filling
        //
        // do that properly:
        Board finalBoard = new Board(grid);
        finalBoard.setStartPosition(board.getStartPosition());
        finalBoard.setEndPosition(board.getEndPosition());

        return new Result(finalBoard, goblinSpawns, ogreSpawns);
    }

    /**
     * Reads all non empty lines from the map file
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
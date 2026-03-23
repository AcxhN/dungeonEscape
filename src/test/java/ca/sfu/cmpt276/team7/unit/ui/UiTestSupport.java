package ca.sfu.cmpt276.team7.unit.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.cells.WallCell;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.ui.RenderItem;
import ca.sfu.cmpt276.team7.ui.RenderKind;

public class UiTestSupport {
    private UiTestSupport() {};

    public static Board makeSimpleBoard(int width, int height) {
        Cell[][] grid = new Cell[height][width];

        Position start = new Position(0, 1);
        Position end = new Position(width - 1, height - 1);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Position pos = new Position(x, y);

                if ((x == 0 || x == width - 1 || y == 0 || y == height - 1)
                    && !pos.equals(start) && !pos.equals(end)) {
                    grid[y][x] = new WallCell(new Position(x, y));
                } else {
                    grid[y][x] = new FloorCell(new Position(x, y));
                }
            }
        }

        Board board = new Board(grid);
        board.setStartPosition(new Position(0, 1));
        board.setEndPosition(new Position(width - 1, height - 2));

        return board;
    }

    public static List<String> getOnlyTexts(List<RenderItem> items) {
        List<String> texts = new ArrayList<>();

        for (RenderItem item : items) {
            if (item.getKind() == RenderKind.TEXT) {
                texts.add(item.getText());
            }
        }

        return texts;
    }

    public static class FakeClock implements LongSupplier {
        private long nowMs = 0L;

        @Override
        public long getAsLong() {
            return nowMs;
        }

        public void advanceMs(long ms) {
            nowMs += ms;
        }
    }
}

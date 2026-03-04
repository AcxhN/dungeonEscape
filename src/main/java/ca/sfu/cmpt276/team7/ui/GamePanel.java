package ca.sfu.cmpt276.team7.ui;

import ca.sfu.cmpt276.team7.*;
import ca.sfu.cmpt276.team7.cell.*;
import ca.sfu.cmpt276.team7.core.*;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

/**
 * Swing panel responsible for rendering the game.
 * <p>
 * This panel bultds a {@link DrawQueue} each frame based on the current {@link Screenstate},
 * then renders all {@link RenderItem} in layer order.
 * 
 * @author Yui Matsumo
 * @version 1.0
 */
public class GamePanel extends JPanel {

    private DrawQueue drawQueue = new DrawQueue();
    private Game game;
    private Board board;

    /**
     *  Creates a panel that renders the given game and board.
     * 
     * @param game  the game model (screen state, score, timers, etc.)
     * @param board the board model (grid, characters, etc.)
     */
    public GamePanel(Game game, Board board) {
        this.game = game;
        this.board = board;

        setBackground(Color.BLACK);
        setOpaque(true);
    }

    /** Layer indices (lower values are drawn first / behind). */
    private int cellLayer = 0;
    private int rewardLayer = 1;
    private int characterLayer = 1;
    private int popupRectLayer = 2;
    private int hudLayer = 3;
    private int popupContentsLayer = 4;

    /** On-screen cell size in pixels. */
    private int cellWidth = 50;
    private int cellHeight = 50;

    /** Sprite-sheet tile sizes and padding (source coordinates). */
    private int gameSrcSize = 64;
    private int screenSrcSize = 200;
    private int srcPadding = 5;

    /**
     * Computes the X/Y offset in a sprite sheet for a given tile "order".
     * 
     * @param order tile index (0-based) along the sheet
     * @param srcSize tile size (without padding) in the source sheet
     * @return the source coordinate (x or y) including padding
     */
    private int srcSize(int order, int srcSize) {
        return ((srcSize + (srcPadding * 2)) * order) + srcPadding;
    }

    /**
     * Measures the width of a string in pixels for a given font.
     * 
     * @param text the text to measure
     * @param font the font used for measurement
     * @return width in pixels
     */
    private int getTextWidth(String text, Font font) {
        FontMetrics fm = getFontMetrics(font);
        return fm.stringWidth(text);
    }

    /**
     * Measures the height of a font in pixels (ascent + descent).
     * 
     * @param font the font used for measurement
     * @return height in pixels
     */
    private int getTextHeight(Font font) {
        FontMetrics fm = getFontMetrics(font);
        return fm.getAscent() + fm.getDescent();
    }

    /**
     * Computes the top-left pixel position to center text in the panel.
     * 
     * @param text the text to center
     * @param font the font to use
     * @return {x, y} where y is the text baseline position
     */
    private int[] getCenteredTextXY(String text, Font font) {
        FontMetrics fm = getFontMetrics(font);
        int textW = fm.stringWidth(text);

        int x = getWidth()/2 - textW/2;

        int ascent = fm.getAscent();
        int descent = fm.getDescent();

        int y = getHeight()/2 + (ascent - descent)/2;

        return new int[]{x, y};
    }

    /**
     * Computes the x position to center text horizontally in the panel.
     * 
     * @param text the text to center
     * @param font the font to use
     * @return x coordinate for the text origin
     */
    private int getCenteredTextX(String text, Font font) {
        int x = getWidth()/2 - getTextWidth(text, font)/2;
        return x;
    }

    /**
     * Computes the top-left pixel position to center a rectangle in the panel.
     * 
     * @param width rectangle width
     * @param height rectangle height
     * @return {x, y} top-left corner
     */
    private int[] getCenteredRectXY(int width, int height) {
        int x = getWidth()/2 - width/2;
        int y = getHeight()/2 - height/2;
        return new int[]{x, y};
    }

    /**
     * Preferred size is derived from the board dimensions and cell size.
     * 
     * @return preferred panel size
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(board.getWidth() * cellWidth, board.getHeight() * cellHeight);
    }


    /**
     * Enqueues a floor tile at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueueFloor(int x, int y) {
        int srcSize = srcSize(0, gameSrcSize);
        RenderItem floor = RenderItem.sprite(cellLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(floor);
    }

    /**
     * Enqueues a wall tile at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueueWall(int x, int y) {
        int srcSize = srcSize(1, gameSrcSize);
        RenderItem wall = RenderItem.sprite(cellLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(wall);
    } 

    /**
     * Enqueues a barrier tile at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueueBarrier(int x, int y) {
        int srcSize = srcSize(1, gameSrcSize);
        RenderItem barrier = RenderItem.sprite(cellLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(barrier);
    }

    /**
     * Enqueues a bonus reward sprite at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueueBonusReward(int x, int y) {
        int srcSize = srcSize(4, gameSrcSize);
        RenderItem bonusReward = RenderItem.sprite(rewardLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(bonusReward);
    }

    /**
     * Enqueues a regular reward sprite at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueueRegularReward(int x, int y) {
        int srcSize = srcSize(3, gameSrcSize);
        RenderItem regularReward = RenderItem.sprite(rewardLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(regularReward);
    }

    /**
     * Enqueues a trap/punishment tile at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueuePunishment(int x, int y) {
        int srcSize = srcSize(2, gameSrcSize);
        RenderItem punishment = RenderItem.sprite(cellLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(punishment);
    }

    /**
     * Enqueues all board cells by inspecting each {@link Cell} type.
     * 
     * @param grid the board grid of cells
     */
    private void enqueueCells(Cell[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Cell cell = grid[i][j];
                int x = j * cellWidth;
                int y = i * cellHeight;
                if (cell instanceof WallCell) {
                    enqueueWall(x, y);
                } else if (cell instanceof FloorCell) {
                    enqueueFloor(x, y);
                } else if (cell instanceof BarrierCell) {
                    enqueueBarrier(x, y);
                } else if (cell instanceof BonusReward) {
                    enqueueFloor(x, y);
                    enqueueBonusReward(x, y);
                } else if (cell instanceof RegularReward) {
                    enqueueFloor(x, y);
                    enqueueRegularReward(x, y);
                } else if (cell instanceof Punishment) {
                    enqueuePunishment(x, y);
                }
            }
        }
    }


    /**
     * Enqueues the player sprite at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueuePlayer(int x, int y) {
        int srcSize = srcSize(7, gameSrcSize);
        RenderItem player = RenderItem.sprite(characterLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(player);
    }

    /**
     * Enqueues the goblin sprite at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueueGoblin(int x, int y) {
        int srcSize = srcSize(6, gameSrcSize);
        RenderItem goblin = RenderItem.sprite(characterLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(goblin);
    }

    /**
     * Enqueues the ogre sprite at the given pixel position.
     * 
     * @param x pixel x
     * @param y pixel y
     */
    private void enqueueOgre(int x, int y) {
        int srcSize = srcSize(5, gameSrcSize);
        RenderItem ogre = RenderItem.sprite(characterLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(ogre);
    }

    /**
     * Enqueues all characters on the board, converting grid coordinates to pixels.
     * 
     * @param characters list of active characters
     */
    private void enqueueCharacters(List<Character> characters) {
        for (Character character : characters) {
            int x = character.getPosition().getX() * cellWidth;
            int y = character.getPosition().getY() * cellHeight;
            
            if (character instanceof Player) {
                enqueuePlayer(x, y);
            } else if (character instanceof Goblin) {
                enqueueGoblin(x, y);
            } else if (character instanceof Ogre) {
                enqueueOgre(x, y);
            }
        }
    }


    /**
     * Enqueues the start screen UI (title and prompt).
     */
    private void enqueueStartScreen() {
        String titleText = "TEAM 7 GAME";
        Font titleFont = new Font("SansSerif", Font.BOLD, 50);
        int[] titleXY = getCenteredTextXY(titleText, titleFont);
        
        RenderItem title = RenderItem.text(0, titleXY[0], titleXY[1], Color.WHITE, titleText, titleFont);
        drawQueue.enqueue(title);

        String startText = "START";
        Font startFont = new Font("SansSerif", Font.BOLD, 24);
        int textPadding = 20;
        int startX = getCenteredTextX(startText, startFont);
        int startY = titleXY[1] + textPadding + getTextHeight(startFont);

        RenderItem start = RenderItem.text(0, startX, startY, Color.WHITE, startText, startFont);
        drawQueue.enqueue(start);
    }

    /**
     * Enqueues the end screen UI (image, result text, comment, score, and replay prompt).
     * 
     * @param score final score
     * @param endReason reason the game ended
     */
    private void enqueueEndScreen(int score, EndReason endReason) {
        int srcSize = srcSize(1, screenSrcSize); // Temporary: we should probably prepare an error placeholder image, just in case.
        int w = cellWidth * 4;
        int h = cellHeight * 4;
        int[] imageXY = getCenteredRectXY(w, h);

        String resultText = "GAME OVER";
        String commentText = "";

        switch (endReason) {
            case WIN:
                srcSize = srcSize(1, screenSrcSize);
                resultText = "YOU WIN";
                commentText = "You braved the terrors of the dungeon and emerged a rich man"; 
                break;
            
            case LOSE_BY_TRAP:
                srcSize = srcSize(2, screenSrcSize);
                commentText = "Lost your footing near a pit of spikes!";
                break;

            case LOSE_BY_OGRE:
                srcSize = srcSize(2, screenSrcSize);
                commentText = "Bumped into an ogre!";
                break;
            
            case LOSE_BY_GOBLIN:
                srcSize = srcSize(2, screenSrcSize);
                commentText = "Caught by a goblin!";
                break;
        }

        int imageY = imageXY[1] - 50;
        RenderItem endImage = RenderItem.sprite(0, imageXY[0], imageY, w, h, SheetId.SCREEN_ATLAS, srcSize, srcPadding, screenSrcSize, screenSrcSize);
        drawQueue.enqueue(endImage);

        //result
        int textPadding = 10;
        Font resultFont = new Font("SansSerif", Font.BOLD, 40);
        int resultX = getCenteredTextX(resultText, resultFont);
        int resultY = imageY + h + textPadding + getTextHeight(resultFont)/2;
        
        RenderItem result = RenderItem.text(0, resultX, resultY, Color.WHITE, resultText, resultFont);
        drawQueue.enqueue(result);

        //comment
        Font commentFont = new Font("SansSerif", Font.PLAIN, 17);
        int commentX = getCenteredTextX(commentText, commentFont);
        int commentY = resultY + textPadding-5 + getTextHeight(commentFont);
        
        RenderItem comment = RenderItem.text(0, commentX, commentY, Color.WHITE, commentText, commentFont);
        drawQueue.enqueue(comment);

        //finalScore
        String finalScoreText = "Final Score: " + score;
        int finalScoreX = getCenteredTextX(finalScoreText, commentFont);
        int finalScoreY = commentY  + getTextHeight(commentFont);
        
        RenderItem finalScore = RenderItem.text(0, finalScoreX, finalScoreY, Color.WHITE, finalScoreText, commentFont);
        drawQueue.enqueue(finalScore);

        //playAgain
        String playAgainText = "Play Again?";
        Font playAgainFont = new Font("SansSerif", Font.BOLD, 15);
        int playAgainX = getCenteredTextX(playAgainText, playAgainFont);
        int playAgainY = finalScoreY + textPadding + getTextHeight(playAgainFont);
        
        RenderItem playAgain = RenderItem.text(0, playAgainX, playAgainY, Color.WHITE, playAgainText, playAgainFont);
        drawQueue.enqueue(playAgain);
    }


    /**
     * Computes aligned positions for HUD text and images.
     * 
     * @param font font used for the text measurements
     * @param keyText key counter text
     * @param coinText score text
     * @param imageH icon height
     * @param textX base x for text
     * @param centerY vertical center line for the HUD row
     * @param padding padding between text and icon
     * @return {imageX, textY, imageY}
     */
    private int[] hudGetXY(Font font, String keyText, String coinText, int imageH, int textX, int centerY, int padding) {
        //imageX
        int keyW = getTextWidth(keyText, font);
        int coinW = getTextWidth(coinText, font);

        int longerW = (keyW >= coinW) ? keyW : coinW;
        int imageX = textX + longerW + padding;
        
        //y
        FontMetrics fm = getFontMetrics(font);
        int textY = centerY + (fm.getAscent() - fm.getDescent())/2;

        int imageY = centerY - imageH/2;

        return new int[]{imageX, textY, imageY};
    }

    /**
     * Enqueues the HUD elements: timer, keys collected, and score.
     * 
     * @param score current score
     * @param totalKey total keys available
     * @param collectedKey keys collected so far
     * @param sec elapsed seconds
     */
    private void enqueueHud(int score, int totalKey, int collectedKey, int sec) {
        //Key/score
        Font font = new Font("SansSerif", Font.BOLD, 15);
        String keyText = collectedKey + " / " + totalKey;
        String coinText = Integer.toString(score);

        int w = 20;
        int h = 20;
        int textX = 15;
        int centerY = 30;
        int padding = 5;

        int[] imageXTextYImageY = hudGetXY(font, keyText, coinText, h, textX, centerY, padding);
        int imageX = imageXTextYImageY[0];
        int textY = imageXTextYImageY[1];
        int imageY = imageXTextYImageY[2];


        //key text      
        RenderItem keyItem = RenderItem.text(hudLayer, textX, textY + h, Color.WHITE, keyText, font);
        drawQueue.enqueue(keyItem);
        //image
        int srcSizeKey = srcSize(8, gameSrcSize);
        RenderItem keyImage = RenderItem.sprite(characterLayer, imageX, imageY, w, h, SheetId.GAME_ATLAS, srcSizeKey, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(keyImage);

        //score text        
        RenderItem coinItem = RenderItem.text(hudLayer, textX, textY + h*2, Color.WHITE, coinText, font);
        drawQueue.enqueue(coinItem);
        //image
        int srcSizeCoin = srcSize(9, gameSrcSize);
        RenderItem coinImage = RenderItem.sprite(characterLayer, imageX, imageY + h*2, w, h, SheetId.GAME_ATLAS, srcSizeCoin, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(coinImage);

        //timer
        Font timerFont = new Font("SansSerif", Font.BOLD, 20);
        String timeText = String.format("%d:%02d", sec / 60, sec % 60);
        RenderItem timer = RenderItem.text(hudLayer, textX, textY - 3, Color.WHITE, timeText, timerFont);
        drawQueue.enqueue(timer);
    }

    /**
     * Enqueues the popup overlay for the given reason (image, text box, and prompt).
     * 
     * @param popupReason popupReason the reason the popup is being shown
     */
    private void enqueuePopups(PopupReason popupReason) {
        int imageW = cellWidth * 4;
        int imageH = cellHeight * 4;

        int[] imageXY = getCenteredRectXY(imageW, imageH);
        int imageX = imageXY[0];
        int imageY = imageXY[1] - 40;
        int imagePadding = 5;

        int textBoxW = cellWidth * 7;
        int textBoxH = cellHeight;
        int[] textBoxXY = getCenteredRectXY(textBoxW, textBoxH);
        int textBotX = textBoxXY[0];
        int textBoxY = textBoxXY[1] + imageH/2 + 15;
        int textXPadding = 8;

        String commentText = "";
        int srcSize = srcSize(0, screenSrcSize); // Temporary: we should probably prepare an error placeholder image, just in case.

        RenderItem imageRect = RenderItem.rect(popupRectLayer, imageX - imagePadding, imageY - imagePadding, imageW + imagePadding*2, imageH + imagePadding*2, Color.BLACK);
        drawQueue.enqueue(imageRect);
        RenderItem textRect = RenderItem.rect(popupRectLayer, textBotX, textBoxY, textBoxW, textBoxH, Color.BLACK);
        drawQueue.enqueue(textRect);

        switch (popupReason) {
            case OGRE_HIT:
                srcSize = srcSize(0, screenSrcSize);
                commentText = "* An ogre energes from the darkness. It demands all of your gold!";
                break;
        
            case BONUS_COLLECTED:
                break;
        }
        
        RenderItem popupImage = RenderItem.sprite(popupContentsLayer, imageX, imageY, imageW, imageH, SheetId.SCREEN_ATLAS, srcSize, srcPadding, screenSrcSize, screenSrcSize);
        drawQueue.enqueue(popupImage);

        Font commentFont = new Font("SansSerif", Font.PLAIN, 11);
        int commentH = getTextHeight(commentFont);
        
        RenderItem comment = RenderItem.text(popupContentsLayer, textBotX + textXPadding, textBoxY + commentH, Color.WHITE, commentText, commentFont);
        drawQueue.enqueue(comment);


        String operationText = "Press space to continue...";
        Font operationFont = new Font("SansSerif", Font.PLAIN, 11);
        int operationX = getCenteredTextX(operationText, operationFont);
        
        RenderItem operation = RenderItem.text(popupContentsLayer, operationX, textBoxY + textBoxH - 10, Color.WHITE, operationText, operationFont);
        drawQueue.enqueue(operation);
    }


    /**
     * Rebuilds the draw queue for the current frame based on {@link ScreenState}.
     * 
     * @param game the game model
     * @param board the board model
     */
    private void buildDrawQueue(Game game, Board board) {
        drawQueue.clear();

        ScreenState screenState = game.getScreenState();
        switch (screenState) {
            case START:
                enqueueStartScreen();
                break;

            case PLAYING:
                enqueueCells(board.getGrid());
                enqueueCharacters(board.getCharacters());
                enqueueHud(game.getTotalScore(), game.getTotalRegularRewards(), game.getCollectedRegularRewards(), game.getElapsedSeconds());
                break;
            
            case PAUSE:
                enqueueCells(board.getGrid());
                enqueueCharacters(board.getCharacters());
                enqueuePopups(game.getPopupReason());
                enqueueHud(game.getTotalScore(), game.getTotalRegularRewards(), game.getCollectedRegularRewards(), game.getElapsedSeconds());
                break;
                
            case END:
                enqueueEndScreen(game.getTotalScore(), game.getEndReason());
                break;
        }
    }
    

    /**
     * Paints the panel by rebuilding the draw queue and rendering all items.
     * 
     * @param g the graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        buildDrawQueue(game, board);
        drawQueue.renderAll(g);
    }
}

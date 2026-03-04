package ca.sfu.cmpt276.team7.ui;

import ca.sfu.cmpt276.team7.*;
import ca.sfu.cmpt276.team7.cell.*;
import ca.sfu.cmpt276.team7.core.*;

import ca.sfu.cmpt276.team7.temps.Board;
import ca.sfu.cmpt276.team7.temps.Cell;
import ca.sfu.cmpt276.team7.temps.FloorCell;
import ca.sfu.cmpt276.team7.temps.Player;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

/*実際の描画
* gameの方で、JFrame（ウィンドウ）作って(もしかしたらここでwidthとheighが参照されるのかも)、repaint()呼び出してもらう*/

// 描画位置の修正をまとめてできるように出しておく　基準点どこなん？

public class GamePanel extends JPanel {

    private DrawQueue drawQueue = new DrawQueue();
    private Game game;
    private Board board;

    public GamePanel(Game game, Board board) {
        this.game = game;
        this.board = board;
    }

    private int cellLayer = 0;
    private int rewardLayer = 1;
    private int characterLayer = 1;
    private int popupRectLayer = 2;
    private int popupContentsLayer = 3;
    private int hudLayer = 4;

    private int cellWidth = 50;
    private int cellHeight = 50;

    private int gameSrcSize = 64;
    private int screenSrcSize = 200;
    private int srcPadding = 5;

    private int srcSize(int order, int srcSize) {
        return ((srcSize + (srcPadding * 2)) * order) + srcPadding;
    }

    private int getTextWidth(String text, Font font) {
        FontMetrics fm = getFontMetrics(font);
        return fm.stringWidth(text);
    }
    private int getTextHeight(Font font) {
        FontMetrics fm = getFontMetrics(font);
        return fm.getAscent() + fm.getDescent();
    }

    private int[] getCenteredTextXY(String text, Font font) {
        FontMetrics fm = getFontMetrics(font);
        int textW = fm.stringWidth(text);

        int x = getWidth()/2 - textW/2;

        int ascent = fm.getAscent();
        int descent = fm.getDescent();

        int y = getHeight()/2 + (ascent - descent)/2;

        return new int[]{x, y};
    }
    private int getCenteredTextX(String text, Font font) {
        int x = getWidth()/2 - getTextWidth(text, font)/2;
        return x;
    }

    private int[] getCenteredRectXY(int width, int height) {
        int x = getWidth()/2 - width/2;
        int y = getHeight()/2 - height/2;
        return new int[]{x, y};
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(board.getWidth() * cellWidth, board.getHeight() * cellHeight);
    }


    private void enqueueFloor(int x, int y) {
        // 床セルを描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(0, gameSrcSize);
        RenderItem floor = RenderItem.sprite(cellLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(floor);
    }
    private void enqueueWall(int x, int y) {
        // 壁セルを描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(1, gameSrcSize);
        RenderItem wall = RenderItem.sprite(cellLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(wall);
    } 
    private void enqueueBarrier(int x, int y) {
        // バリアセルを描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(1, gameSrcSize);
        RenderItem barrier = RenderItem.sprite(cellLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(barrier);
    }
    private void enqueueBonusReward(int x, int y) {
        // ボーナス報酬を描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(4, gameSrcSize);
        RenderItem bonusReward = RenderItem.sprite(rewardLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(bonusReward);
    }
    private void enqueueRegularReward(int x, int y) {
        // レギュラー報酬を描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(3, gameSrcSize);
        RenderItem regularReward = RenderItem.sprite(rewardLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(regularReward);
    }
    private void enqueuePunishment(int x, int y) {
        // トラップを描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(2, gameSrcSize);
        RenderItem punishment = RenderItem.sprite(cellLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(punishment);
    }

    private void enqueueCells(Cell[][] grid) { //壁床バリアセル参照cell[][]参照
        // ゲーム画面をdrawQueueに追加する.セルの種類を判別して描く
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Cell cell = grid[i][j];
                int x = j * cellWidth; //例: セルの幅が50ピクセルの場合
                int y = i * cellHeight; //例: セルの高さが50ピクセルの場合
                if (cell instanceof WallCell) {
                    enqueueWall(x, y);
                } else if (cell instanceof FloorCell) {
                    enqueueFloor(x, y);
                } else if (cell instanceof BarrierCell) {
                    enqueueBarrier(x, y);
                } else if (cell instanceof BonusReward) { // ここから下は二重にしてもいいかも
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


    private void enqueuePlayer(int x, int y) {
        // プレイヤーを描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(7, gameSrcSize);
        RenderItem player = RenderItem.sprite(characterLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(player);
    }
    private void enqueueGoblin(int x, int y) {
        // ゴブリンを描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(6, gameSrcSize);
        RenderItem goblin = RenderItem.sprite(characterLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(goblin);
    }
    private void enqueueOgre(int x, int y) {
        // オーグを描画するためのRenderItemを作成してdrawQueueに追加する
        int srcSize = srcSize(5, gameSrcSize);
        RenderItem ogre = RenderItem.sprite(characterLayer, x, y, cellWidth, cellHeight, SheetId.GAME_ATLAS, srcSize, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(ogre);
    }

    private void enqueueCharacters(List<Character> characters) { //ボードのキャラリスト、それに付随する位置情報、ボードにある初期座標を参照
        // キャラクターをdrawQueueに追加する
        // positionのxとyをpixelに変換する必要があるか確認
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


    private void enqueueStartScreen() {
        // スタート画面をdrawQueueに追加する
        // スタート画面の描画に必要な情報を保持する 画像（未定）、タイトル、メッセージ、スペースで開始
        //TEXT, TEXT
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

    private void enqueueEndScreen(int score, EndReason endReason) {
        // エンド画面をdrawQueueに追加する
        // レイヤー０
        // [IMAGE, TEXT(big), TEXT], TEXT
        int srcSize = srcSize(1, screenSrcSize); //仮　エラー用の画像用意しといたほうがいいかも
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
    private void enqueueHud(int score, int totalKey, int collectedKey) { //ゲームの鍵、ポイント情報参照
        // UIをdrawQueueに追加する
        // レイヤー2
        // TEXT, TEXT, IMAGE, IMAGE

        //Key/score
        Font font = new Font("SansSerif", Font.BOLD, 15);
        String keyText = collectedKey + " / " + totalKey;
        String coinText = Integer.toString(score);

        int w = 20;
        int h = 20;
        int textX = 15;
        //int imageX = textX + 35;
        int centerY = 30;
        int padding = 5;

        int[] imageXTextYImageY = hudGetXY(font, keyText, coinText, h, textX, centerY, padding);
        int imageX = imageXTextYImageY[0];
        int textY = imageXTextYImageY[1];
        int imageY = imageXTextYImageY[2];


        //key text      
        RenderItem keyItem = RenderItem.text(hudLayer, textX, textY, Color.WHITE, keyText, font);
        drawQueue.enqueue(keyItem);
        //image
        int srcSizeKey = srcSize(8, gameSrcSize);
        RenderItem keyImage = RenderItem.sprite(characterLayer, imageX, imageY, w, h, SheetId.GAME_ATLAS, srcSizeKey, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(keyImage);

        //score text        
        RenderItem coinItem = RenderItem.text(hudLayer, textX, textY + h, Color.WHITE, coinText, font);
        drawQueue.enqueue(coinItem);
        //image
        int srcSizeCoin = srcSize(9, gameSrcSize);
        RenderItem coinImage = RenderItem.sprite(characterLayer, imageX, imageY + h, w, h, SheetId.GAME_ATLAS, srcSizeCoin, srcPadding, gameSrcSize, gameSrcSize);
        drawQueue.enqueue(coinImage);
    }

    private void enqueuePopups(PopupReason popupReason) { //ポップアップの理由を参照
        // ポップアップをdrawQueueに追加する
        //レイヤー2か3
        // RECT, RECT -> [IMAGE, TEXT], TEXT []内入れ替え

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
        int srcSize = srcSize(0, screenSrcSize); //仮　何かエラー用の画像用意しといた方がいいかも

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


    private void buildDrawQueue(Graphics g, Game game, Board board) {
        drawQueue.clear();
        // 描画予定のアイテムをdrawQueueに追加する
        // switch文でゲームのスクリーンステートを確認して、enqueueするアイテムを変える
        ScreenState screenState = game.getScreenState();
        switch (screenState) {
            case START:
                enqueueStartScreen();
                break;

            case PLAYING:
                enqueueCells(board.getGrid());
                enqueueCharacters(board.getCharacters());
                enqueueHud(game.getTotalScore(), game.getTotalRegularRewards(), game.getCollectedRegularRewards());
                break;
            
            case PAUSE:
                enqueueCells(board.getGrid());
                enqueueCharacters(board.getCharacters());
                enqueuePopups(game.getPopupReason());
                enqueueHud(game.getTotalScore(), game.getTotalRegularRewards(), game.getCollectedRegularRewards());
                break;
                
            case END:
                enqueueEndScreen(game.getTotalScore(), game.getEndReason());
                break;
        }
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        //ウィンドウ作って最初に背景を黒く塗る
        super.paintComponent(g);
        buildDrawQueue(g, game, board);
        // drawQueueからアイテムを取り出して描画する
        drawQueue.renderAll(g);
    }
}

// mainとかGame側でJFrame作る <- ここでwidthとheight参照
/*
import javax.swing.*;
SwingUtilities.invokeLater(() -> {
    Game game = new Game(); //仮
    Board board = new Board(); //仮
    GamePanel gamePanel = new GamePanel(game, board);  // 引数は仮

    JFrame frame = new JFrame("Team 7 Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.add(gamePanel);
    frame.pack();　// panel.getPreferredSize()が使われる

    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
});

//tickの駆動　最後にgamePanel.repaint()呼び出す
//参考https://tastasichi.com/swing-jframe/
*/
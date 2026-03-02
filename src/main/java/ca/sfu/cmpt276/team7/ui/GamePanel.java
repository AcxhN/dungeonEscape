package ca.sfu.cmpt276.team7.ui;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

/*実際の描画
* gameの方で、JFrame（ウィンドウ）作って(もしかしたらここでwidthとheighが参照されるのかも)、repaint()呼び出してもらう*/

// 描画一の修正をまとめてできるように出しておく　基準点どこなん？

public class GamePanel extends JPanel {

    private DrawQueue drawQueue = new DrawQueue();
    private Game game;
    private Board board;

    public GamePanel(Game game, Board board) {
        this.game = game;
        this.board = board;
    }

    private int cellLayer = 0;
    private int characterLayer = 1;

    private int cellWidth = 50; //例
    private int cellHeight = 50; //例
    private int characterWidth = 30; //例
    private int characterHeight = 30; //例


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(board.getWidth() * cellWidth, board.getHeight() * cellHeight); //widthとheightが何の値を取るのかは要確認
    }


    private void enqueueFloor(int x, int y) {
        // 床セルを描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem floor = new RenderItem(cellLayer, RenderKind.IMAGE, x, y, cellWidth, cellHeight, Color.LIGHT_GRAY, null, null); //例
        drawQueue.enqueue(floor);
    }

    private void enqueueWall(int x, int y) {
        // 壁セルを描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem wall = new RenderItem(cellLayer, RenderKind.IMAGE, x, y, cellWidth, cellHeight, Color.DARK_GRAY, null, null); //例
        drawQueue.enqueue(wall);
    } 

    private void enqueueBarrier(int x, int y) {
        // バリアセルを描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem barrier = new RenderItem(cellLayer, RenderKind.IMAGE, x, y, cellWidth, cellHeight, Color.GRAY, null, null); //例
        drawQueue.enqueue(barrier);
    }

    private void enqueueBonusReward(int x, int y) {
        // ボーナス報酬を描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem bonusReward = new RenderItem(cellLayer, RenderKind.IMAGE, x, y, cellWidth, cellHeight, Color.YELLOW, null, null); //例
        drawQueue.enqueue(bonusReward);
    }
    
    private void enqueueRegularReward(int x, int y) {
        // レギュラー報酬を描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem regularReward = new RenderItem(cellLayer, RenderKind.IMAGE, x, y, cellWidth, cellHeight, Color.ORANGE, null, null); //例
        drawQueue.enqueue(regularReward);
    }

    private void enqueuePunishment(int x, int y) {
        // トラップを描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem punishment = new RenderItem(cellLayer, RenderKind.IMAGE, x, y, cellWidth, cellHeight, Color.GREEN, null, null); //例
        drawQueue.enqueue(punishment);
    }

    private void enqueueCells(Board board) { //壁床バリアセル参照cell[][]参照
        // ゲーム画面をdrawQueueに追加する.セルの種類を判別して描く
        Cell[][] grid = board.getGrid();
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
                    enqueueBonusReward(x, y);
                } else if (cell instanceof RegularReward) {
                    enqueueRegularReward(x, y);
                } else if (cell instanceof Punishment) {
                    enqueuePunishment(x, y);
                }
            }
        }
    }


    private void enqueuePlayer(int x, int y) {
        // プレイヤーを描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem player = new RenderItem(characterLayer, RenderKind.IMAGE, x, y, characterWidth, characterHeight, Color.BLUE, null, null); //例
        drawQueue.enqueue(player);
    }

    private void enqueueGoblin(int x, int y) {
        // ゴブリンを描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem goblin = new RenderItem(characterLayer, RenderKind.IMAGE, x, y, characterWidth, characterHeight, Color.RED, null, null); //例
        drawQueue.enqueue(goblin);
    }

    private void enqueueOgre(int x, int y) {
        // オーグを描画するためのRenderItemを作成してdrawQueueに追加する
        RenderItem ogre = new RenderItem(characterLayer, RenderKind.IMAGE, x, y, characterWidth, characterHeight, Color.MAGENTA, null, null); //例
        drawQueue.enqueue(ogre);
    }

    private void enqueueCharacters(Board board) { //ボードのキャラリスト、それに付随する位置情報、ボードにある初期座標を参照
        // キャラクターをdrawQueueに追加する
        // positionのxとyをpixelに変換する必要があるか確認
        List<Character> characters = board.getCharacters();
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


/*
    private void enqueueStartScreen() {
        // スタート画面をdrawQueueに追加する
        // スタート画面の描画に必要な情報を保持する 画像（未定）、タイトル、メッセージ、スペースで開始
        RenderItem startScreen = new RenderItem(0, RenderKind.TEXT, 0, 0, 0, 0, null, "Press SPACE to start", null); //例
        drawQueue.enqueue(startScreen);
    }

    private void enqueueEndScreen(Game game) {
        // エンド画面をdrawQueueに追加する
        // レイヤー０
        int score = game.getScore();
        
        RenderItem endScreen = new RenderItem(); //ボードの点数確認、ゲームステートで勝敗理由確認
        drawQueue.enqueue(endScreen);
    }

    private void enqueueHUD(Game game) { //ゲームの鍵、ポイント情報参照
        // UIをdrawQueueに追加する
        // レイヤー2
        RenderItem hud = new RenderItem();
        drawQueue.enqueue(hud);
    }

    private void enqueuePopups(PopupState popupState) { //ポップアップの理由を参照
        // ポップアップをdrawQueueに追加する
        //レイヤー2か3
        RenderItem popup = new RenderItem();
        drawQueue.enqueue(popup);
    }


    private void buildDrawQueue(Game game) {
        drawQueue.clear();
        // 描画予定のアイテムをdrawQueueに追加する
        // switch文でゲームのスクリーンステートを確認して、enqueueするアイテムを変える
        ScreenState screenState = game.getScreenState();
        if (screenState == ScreenState.START) {
            enqueueStartScreen();
        } else if (screenState == ScreenState.PLAYING ) {
            enqueueSpecialCells(game.getBoard());
            enqueueCharacters(game.getBoard());
            enqueueHUD(game);
        } else if (screenState == ScreenState.PAUSE) {
            enqueueGameScreen(game.getBoard());
            enqueueSpecialCells(game.getBoard());
            enqueueCharacters(game.getBoard());
            enqueueHUD(game);
            enqueuePopups(game.getPopupReason());
        } else if (screenState == ScreenState.END) {
            enqueueEndScreen();
        }
    }
*/

    private void buildDrawQueue(Board board) {
        // 描画予定のアイテムをdrawQueueに追加する
        drawQueue.clear(); //前のフレームのアイテムをクリア
        enqueueCells(board);
        enqueueCharacters(board);
    }

    @Override
    protected void paintComponent(Graphics g) {
        //ウィンドウ作って最初に背景を黒く塗る
        super.paintComponent(g);
        buildDrawQueue(game, board);
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

    JFrame frame = new JFrame("Team7's Game");
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
package ca.sfu.cmpt276.team7.ui;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

// 描画予定のアイテムを管理するクラス

public class DrawQueue {

    private Queue<RenderItem> queue;

    private final BufferedImage gameAtlas;
    private final BufferedImage screensAtlas;

    public DrawQueue() {
        this.queue = new LinkedList<>();
        this.gameAtlas = loadImage("/sprites/atlas_screens.png");
        this.screensAtlas = loadImage("/sprites/atlas_game.png");
    }

    private static BufferedImage loadImage(String resourcePath) {
        try (InputStream is = DrawQueue.class.getResourceAsStream(resourcePath)) {
            return ImageIO.read(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + resourcePath, e);
        }
    }

    private BufferedImage selectSheet(SheetId id) {
        switch (id) {
            case GAME_ATLAS:
                return gameAtlas;

            case SCREEN_ATLAS:
                return screensAtlas;

            case NONE:
            default:
                throw new IllegalArgumentException("Invalid sheet id: " + id);
        }
    }


    public void enqueue(RenderItem item) {
        this.queue.offer(item);
    }

    public void clear() {
        this.queue.clear();
    }


    public void renderAll(Graphics g) {
        List<RenderItem> items = new ArrayList<>(queue);
        items.sort((a, b) -> Integer.compare(a.getLayer(), b.getLayer()));

        for (RenderItem item : queue) {
            // RenderItemの種類に応じて描画する
            switch (item.getKind()) {
                case RECTANGLE:
                    // 四角形描画
                    g.setColor(item.getColor());
                    g.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
                    break;
                    
                case TEXT:
                    // テキスト描画
                    g.setColor(item.getColor());
                    g.setFont(item.getFont());
                    g.drawString(item.getText(), item.getX(), item.getY());
                    break;

                case SPRITE:
                    // 画像描画
                    //Image sheet = new ImageIcon(item.getSheetPath()).getImage();
                    BufferedImage sheet = selectSheet(item.getSheetId());

                    int x1 = item.getX();
                    int y1 = item.getY();
                    int x2 = x1 + item.getWidth();
                    int y2 = y1 + item.getHeight();

                    int srcX1 = item.getSrcX();
                    int srcY1 = item.getSrcY();
                    int srcX2 = srcX1 + item.getSrcW();
                    int srcY2 = srcY1 + item.getSrcH();

                    g.drawImage(sheet, x1, y1, x2, y2, srcX1, srcY1, srcX2, srcY2, null);
                    break;
            }
        }
    }
}

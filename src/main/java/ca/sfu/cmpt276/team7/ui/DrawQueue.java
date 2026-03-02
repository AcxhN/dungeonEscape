package ca.sfu.cmpt276.team7.ui;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

// 描画予定のアイテムを管理するクラス

public class DrawQueue {

    private Queue<RenderItem> queue;

    public DrawQueue() {
        this.queue = new LinkedList<>();
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

        for (RenderItem item : items) {
            g.setColor(item.getColor());
            g.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
        }
    }

    /*
    public void renderAll(Graphics g) {
        for (RenderItem item : queue) {
            // RenderItemの種類に応じて描画する
            switch (item.getKind()) {
                case TEXT:
                    // テキスト描画
                    g.setColor(item.getColor());
                    g.drawString(item.getText(), item.getX(), item.getY());
                    break;
                case IMAGE:
                    // 画像描画
                    Image image = new ImageIcon(item.getImagePath()).getImage();
                    g.drawImage(image, item.getX(), item.getY(), item.getWidth(), item.getHeight(), null);
                    break;
                case RECTANGLE:
                    // 四角形描画
                    g.setColor(item.getColor());
                    g.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
                    break;
            }
        }
    }
    */
}

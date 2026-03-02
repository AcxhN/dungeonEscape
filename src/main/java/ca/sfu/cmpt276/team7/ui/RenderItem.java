package ca.sfu.cmpt276.team7.ui;

import java.awt.Color;
/*描画予定のアイテム */

public class RenderItem {
    private final int layer;
    private final RenderKind kind;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private final Color color;
    private final String text;
    private final String imagePath;

    public RenderItem(int layer, RenderKind kind, int x, int y, int width, int height, Color color, String text, String imagePath) {
        this.layer = layer;
        this.kind = kind;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.text = text;
        this.imagePath = imagePath;
    }

    public int getLayer() {
        return layer;
    }
    
    public RenderKind getKind() {
        return kind;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public String getImagePath() {
        return imagePath;
    }
}

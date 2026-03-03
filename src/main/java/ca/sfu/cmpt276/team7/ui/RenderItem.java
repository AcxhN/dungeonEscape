package ca.sfu.cmpt276.team7.ui;

import java.awt.Color;
/*描画予定のアイテム */

public class RenderItem {
    private final int layer;
    private final RenderKind kind;

    // 共通
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    // RECT/TEXT
    private final Color color;

    private final String text;
    private final String fontName;
    private final String fontStyle;
    private final int fontSize;

    // SPRITE
    private final String sheetPath;
    private final int srcX;
    private final int srcY;
    private final int srcW;
    private final int srcH;

    

    private RenderItem(int layer, RenderKind kind, int x, int y, int width, int height,
                        Color color, String text, String fontName, String fontStyle, int fontSize,
                        String sheetPath, int srcX, int srcY, int srcW, int srcH) {
        this.layer = layer;
        this.kind = kind;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.text = text;
        this.fontName = fontName;
        this.fontStyle = fontStyle;
        this.fontSize = fontSize;
        this.sheetPath = sheetPath;
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcW = srcW;
        this.srcH = srcH;
    }

    public static RenderItem rect(int layer, int x, int y, int width, int height, Color color){
        return new RenderItem(layer, RenderKind.RECTANGLE, x, y, width, height, color, "", "", "", 0, "", 0, 0, 0, 0);
    }

    public static RenderItem text(int layer, int x, int y, Color color, String text, String fontName, String fontStyle, int fontSize){
        return new RenderItem(layer, RenderKind.TEXT, x, y, 0, 0, color, text, fontName, fontStyle, fontSize, "", 0, 0, 0, 0);
    }

    public static RenderItem sprite(int layer, int x, int y, int width, int height, String sheetPath, int srcX, int srcY, int srcW, int srcH){
        return new RenderItem(layer, RenderKind.SPRITE, x, y, width, height, null, "", "", "", 0, sheetPath, srcX, srcY, srcW, srcH);
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
    
    public String getFontName() {
        return fontName;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getSheetPath() {
        return sheetPath;
    }

    public int getSrcX() {
        return srcX;
    }

    public int getSrcY() {
        return srcY;
    }
    
    public int getSrcW() {
        return srcW;
    }

    public int getSrcH() {
        return srcH;
    }
}

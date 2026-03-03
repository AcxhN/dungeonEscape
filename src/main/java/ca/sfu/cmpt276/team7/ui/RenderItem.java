package ca.sfu.cmpt276.team7.ui;

import java.awt.Color;
import java.awt.Font;
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

    // TEXT
    private final String text;
    private final Font font;

    // SPRITE
    private final SheetId sheetId;
    private final int srcX;
    private final int srcY;
    private final int srcW;
    private final int srcH;
// ColorとFontって初期値どうする？
    

    private RenderItem(int layer, RenderKind kind, int x, int y, int width, int height,
                        Color color, String text, Font font,
                        SheetId sheetId, int srcX, int srcY, int srcW, int srcH) {
        this.layer = layer;
        this.kind = kind;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.text = text;
        this.font = font;
        this.sheetId = sheetId;
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcW = srcW;
        this.srcH = srcH;
    }

    public static RenderItem rect(int layer, int x, int y, int width, int height, Color color) {
        return new RenderItem(layer, RenderKind.RECTANGLE, x, y, width, height, color, "", null, SheetId.NONE, 0, 0, 0, 0);
    }

    public static RenderItem text(int layer, int x, int y, Color color, String text, Font font) {
        return new RenderItem(layer, RenderKind.TEXT, x, y, 0, 0, color, text, font, SheetId.NONE, 0, 0, 0, 0);
    }

    public static RenderItem sprite(int layer, int x, int y, int width, int height, SheetId sheetId, int srcX, int srcY, int srcW, int srcH) {
        return new RenderItem(layer, RenderKind.SPRITE, x, y, width, height, null, "", null, sheetId, srcX, srcY, srcW, srcH);
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
    
    public Font getFont() {
        return font;
    }

    public SheetId getSheetId() {
        return sheetId;
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

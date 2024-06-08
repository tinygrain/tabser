package app.tabser.rendering;

import java.io.InputStream;

public interface GraphicsLoader {
    InputStream getGraphicAsStream(int resId, int xPos, int yPos, int width, int height);
}

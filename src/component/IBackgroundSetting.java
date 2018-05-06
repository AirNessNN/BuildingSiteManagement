package component;

import java.awt.*;

/**
 * 背景颜色改变
 */
public interface IBackgroundSetting {
    void setNormalColor(Color color);
    void setEnteredColor(Color color);
    void setPressedColor(Color color);
    void setReleasedColor(Color color);
    void setExitedColor(Color color);

    Color getNormalColor();
    Color getEnteredColor();
    Color getPressedColor();
    Color getReleasedColor();
    Color getExitedColor();
}

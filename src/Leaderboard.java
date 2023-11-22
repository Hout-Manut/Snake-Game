package src;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class Leaderboard {
    private static final int width = 1280;
    private static final int height = 720;
    static final int PIXEL_SIZE = 20;
    static final int NUMBER_OF_PIXELS = (width * height) / (PIXEL_SIZE * PIXEL_SIZE);

    static final String backgroundColor = "#eaeaea";
    static final int fps = 60;

    private Panel panel;

    private int delay = 1000 / fps;
    private int state;
    private long frames = 0;
    private int offset;
    private int backOffset;
    private int alpha;
    private int selected;
    private Color rgb;
    private Timer timer;
    /* States
     * 0 = Transition in
     * 1 = Static
     * 2 = Transition out
     */

    private final int buttonHeight;

    Leaderboard(Panel panel) {
        this.panel = panel;
    }
}

package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TankControl implements KeyListener{
    private Tank player;
    private final int up;
    private final int down;
    private final int right;
    private final int left;
    private final int shoot;

    public TankControl(Tank t1, int up, int down, int left, int right, int shoot) {
        this.player = t1;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == up) {
            this.player.toggleUpPressed();
        }
        if (keyPressed == down) {
            this.player.toggleDownPressed();
        }
        if (keyPressed == left) {
            this.player.toggleLeftPressed();
        }
        if (keyPressed == right) {
            this.player.toggleRightPressed();
        }


    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased  == up) {
            this.player.unToggleUpPressed();
        }
        if (keyReleased == down) {
            this.player.unToggleDownPressed();
        }
        if (keyReleased  == left) {
            this.player.unToggleLeftPressed();
        }
        if (keyReleased  == right) {
            this.player.unToggleRightPressed();
        }

    }
}



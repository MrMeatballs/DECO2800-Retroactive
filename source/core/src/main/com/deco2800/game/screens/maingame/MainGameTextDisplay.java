package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import com.deco2800.game.events.listeners.EventListener1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 *
 * By default, will not display anything, but can call MainGameTextDisplay.display to display a
 * box with text (and optional image). Can then call remove to remove it.
 */
public class MainGameTextDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameTextDisplay.class);

    private Table table;
    private Texture texture;
    private Label displayText;
    private boolean visible;
    private long startTime;

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("create_textbox",
                (EventListener1<String>) this::display);
        // Load background texture
        texture = new Texture(Gdx.files.internal(
                "images/ui/elements/Textbox_1024.png"));
    }

    /**
     * Create an empty table for storing the background image
     */
    private void addActors() {
        table = new Table();
        table.bottom();
        stage.addActor(table);
    }

    /**
     * Displays the text box at the bottom of the screen containing the given text.
     *
     * @param text The text to display
     */
    private void display(String text) {
        // Check to see if we're already displaying anything, and do nothing if we are
        // TODO Probably should overwrite rather than do nothing
        if (visible) {
            return;
        }

        // Divide screen into a more manageable grid
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;

        // Display background texture
        table.setSize(Gdx.graphics.getWidth(), rowHeight*4);
        Image background = new Image(texture);
        background.setScaleX((colWidth*8)/background.getWidth());
        background.setOrigin(Align.center);
        table.add(background);

        // Display Text
        displayText = new Label(text, skin, "large");
        displayText.setSize(colWidth*6, rowHeight*3);
        displayText.setPosition(colWidth*2, rowHeight);
        displayText.setFontScale((float) (colWidth*10)/1280); // Scale font to screen size
        displayText.setWrap(true);

        stage.addActor(displayText);

        visible = true;
        startTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Removes all current visual components from the screen (but doesn't do a full cleanup)
     */
    private void hide() {
        table.clear();
        displayText.setText("");
        visible = false;
    }

    /**
     * Removes the textbox after a set amount of time (DURATION)
     */
    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (visible && currentTime - startTime >= 3000L) {
            // (3000ms) has passed, hide textbox
            hide();
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
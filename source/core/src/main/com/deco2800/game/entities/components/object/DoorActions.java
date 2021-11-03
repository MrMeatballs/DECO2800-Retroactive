package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoorActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(DoorActions.class);
    private static final String PROMPT_MESSAGE = "You opened a door! That's pretty cool.";
    private static final String UPDATE_ANIMATION = "update_animation";

    private static EventHandler screenEvents = ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents();
    private static  boolean hasOpenedDoor = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "door_close_left");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with DOOR, triggering door animation");
            entity.getEvents().trigger(UPDATE_ANIMATION, "Door_open_left");
            if (!hasOpenedDoor) {
                screenEvents.trigger("create_textbox", PROMPT_MESSAGE);
                hasOpenedDoor = true;
            }
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("DOOR started collision with PLAYER, highlighting door");
            entity.getEvents().trigger(UPDATE_ANIMATION, "Door_left_highlighted");
        } else {
            logger.debug("DOOR ended collision with PLAYER, un-highlighting door");
            entity.getEvents().trigger(UPDATE_ANIMATION, "door_close_left");
        }
    }
}

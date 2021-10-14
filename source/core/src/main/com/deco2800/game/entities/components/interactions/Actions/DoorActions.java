package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoorActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", "door_close_left");
    }

    @Override
    public void onInteraction(Entity target) {
        String string = "You opened a door! That's pretty cool.";
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with DOOR, triggering door animation");
            ((MainGameScreen) ServiceLocator.getGame().getScreen())
                    .getMainGameEntity().getEvents().trigger("create_textbox", string);
            //entity.getEvents().trigger("update_animation", "Door_left_highlighted_d");
            entity.getEvents().trigger("update_animation", "Door_open_left");
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("DOOR started collision with PLAYER, highlighting door");
            entity.getEvents().trigger("update_animation", "Door_left_highlighted");
        } else {
            logger.debug("DOOR ended collision with PLAYER, un-highlighting door");
            entity.getEvents().trigger("update_animation", "door_close_left");
        }
    }
}

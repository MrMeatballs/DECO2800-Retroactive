package com.deco2800.game.components;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Null;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class InteractableComponentTest {
    EventHandler handler;

    @BeforeEach
    void beforeEach() {
        //handler = new EventHandler();
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldTriggerCollisionEvent() {
        Entity player = createPlayer();
        Entity object = createObject(player);

        EventListener0 listener = mock(EventListener0.class);
        object.getEvents().addListener("interactionStart", listener);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture objectFixture = object.getComponent(HitboxComponent.class).getFixture();
        object.getEvents().trigger("collisionStart", objectFixture, playerFixture);

        verify(listener).handle(); // Check that listener ran when the object was collided with
    }

    @Test
    void shouldEndCollisionEvent() {
        Entity player = createPlayer();
        Entity object = createObject(player);

        EventListener0 listener = mock(EventListener0.class);
        object.getEvents().addListener("interactionEnd", listener);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture objectFixture = object.getComponent(HitboxComponent.class).getFixture();
        object.getEvents().trigger("collisionEnd", objectFixture, playerFixture);

        verify(listener).handle(); // Check that listener ran when the object was not collided
    }

    Entity createPlayer() {
        Entity player =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        player.create();
        return player;
    }

    Entity createObject(Entity player) {
        Entity object =
                new Entity()
                        .addComponent(new InteractableComponent(player))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        object.create();
        return object;
    }
}

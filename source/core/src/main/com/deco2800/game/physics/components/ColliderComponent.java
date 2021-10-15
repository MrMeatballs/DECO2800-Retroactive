package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.generic.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent.AlignX;
import com.deco2800.game.physics.components.PhysicsComponent.AlignY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Attaches a physics collider to an entity. By default, this is a rectangle the same size as the
 * entity's scale. This allows an entity to collide with other physics objects, or detect collisions
 * without interaction (if sensor = true)
 */
public class ColliderComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(ColliderComponent.class);
  private static final float X_SCALE = 1f;
  private static final float Y_SCALE = 0.5f;

  private final FixtureDef fixtureDef;
  private Fixture fixture;
  private Vector2 scale;

  public ColliderComponent() {
    fixtureDef = new FixtureDef();
  }

  @Override
  public void create() {
    if (fixtureDef.shape == null) {
      logger.trace("{} Setting default bounding box", this);
      fixtureDef.shape = makeBoundingBox();
    }

    Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
    fixture = physBody.createFixture(fixtureDef);
  }

  /**
   * Set physics as a box with a given size. Box is centered around the entity.
   *
   * @param size size of the box
   * @return self
   */
  public ColliderComponent setAsBox(Vector2 size) {
    return setAsBox(size, entity.getCenterPosition());
  }

  /**
   * Set physics as a box with a given size. Box is aligned based on alignment.
   *
   * @param size size of the box
   * @param alignX how to align x relative to entity
   * @param alignY how to align y relative to entity
   * @return self
   */
  public ColliderComponent setAsIsoAligned(Vector2 size, AlignX alignX, AlignY alignY) {
    scale = size;
    Vector2 position = new Vector2();
    switch (alignX) {
      case LEFT:
        position.x = size.x / 2;
        break;
      case CENTER:
        position.x = entity.getCenterPosition().x;
        break;
      case RIGHT:
        position.x = entity.getScale().x - (size.x / 2);
        break;
    }

    switch (alignY) {
      case BOTTOM:
        position.y = size.y / 2;
        break;
      case CENTER:
        position.y = entity.getCenterPosition().y;
        break;
      case TOP:
        position.y = entity.getScale().y - (size.y / 2);
        break;
    }

    return setAsIso(size, position);
  }

  public ColliderComponent setIsoShape(float bottomLeft, float bottomRight) {
      return setIsoShapeAligned(
              bottomLeft,
              bottomRight,
              AlignX.CENTER,
              AlignY.BOTTOM
      );
  }

  public ColliderComponent setIsoShapeAligned(
          float bottomLeft,
          float bottomRight,
          AlignX alignX,
          AlignY alignY
  ) {
      // TODO TESTING
      Vector2 position = new Vector2();

      // TODO Need to calc proper height and width
      float size = bottomLeft + bottomRight;

      switch (alignX) {
          case LEFT:
              position.x = size / 2;
              break;
          case CENTER:
              position.x = entity.getCenterPosition().x;
              break;
          case RIGHT:
              position.x = entity.getScale().x - ((bottomLeft + bottomRight) / 2);
              break;
      }

      switch (alignY) {
          case BOTTOM:
              position.y = 0;
              break;
          case CENTER:
              position.y = entity.getCenterPosition().y;
              break;
          case TOP:
              position.y = Math.max(bottomLeft, bottomRight);
              break;
      }

      return changeIsoShape(bottomLeft, bottomRight, position);
  }

  private ColliderComponent changeIsoShape (
          float bottomLeft,
          float bottomRight,
          Vector2 position
  ) {
      PolygonShape bound = new PolygonShape();

      float left = bottomLeft / 2;
      float right = bottomRight / 2;

      // Hardcoded scaling factor. The 1:2 ratio gives an angle of 26.565 deg,
      // the cosine of which is 0.894.
      float scaling = 0.894f;

      // Each point is offset by the given alignment
      float offset = position.x;

      Vector2 south = isoVector2(offset, 0 + position.y);
      Vector2 east = isoVector2(offset + right, right + position.y);
      Vector2 north = isoVector2(offset + right - left, right + left + position.y);
      Vector2 west = isoVector2(offset - left, left + position.y);

      // Collect each of the isometric parallelogram's corners
      Vector2[] points = new Vector2[]{west, north, east, south};
      System.out.println("Points");
      for (Vector2 vec : points) {
          System.out.println(vec);
      }

      bound.set(points);
      setShape(bound);
      return this;
  }

  private Vector2 isoVector2(float x, float y) {
      return new Vector2(x, y * Y_SCALE);
  }

  /**
   * Set physics as a box with a given size and local position. Box is centered around the position.
   *
   * @param size size of the box
   * @param position position of the box center relative to the entity.
   * @return self
   */
  public ColliderComponent setAsBox(Vector2 size, Vector2 position) {
    PolygonShape bbox = new PolygonShape();

    bbox.setAsBox(size.x / 2, size.y / 2, position, 0); //angle: 0.45f

    setShape(bbox);
    return this;
  }

  public ColliderComponent setAsIso(Vector2 size, Vector2 position) {
      PolygonShape bound = new PolygonShape();

      /*
        height would normally be tan(30 deg) * size.y for iso, but we estimate
        it with a 1:2 ratio. Therefore, height is half of the y size.
       */
      float height = 0.5f * size.y;

      Vector2 west = new Vector2(position.x - (size.x / 2), (height / 2));
      Vector2 north = new Vector2(position.x, height);
      Vector2 east = new Vector2(position.x + (size.x / 2), (height / 2));
      Vector2 south = new Vector2(position.x, 0);
      // Collect each of the isometric parallelogram's corners
      Vector2[] points = new Vector2[]{west, north, east, south};
      System.out.println("Points");
      for (Vector2 vec : points) {
          System.out.println(vec);
      }

      bound.set(points);
      setShape(bound);
      return this;
  }

  /**
   * Set friction. This affects the object when touching other objects, but does not affect friction
   * with the ground.
   *
   * @param friction friction, default = 0
   * @return self
   */
  public ColliderComponent setFriction(float friction) {
    if (fixture == null) {
      fixtureDef.friction = friction;
    } else {
      fixture.setFriction(friction);
    }
    return this;
  }

  /**
   * Set whether this physics component is a sensor. Sensors don't collide with other objects but
   * still trigger collision events. See: https://www.iforce2d.net/b2dtut/sensors
   *
   * @param isSensor true if sensor, false if not. default = false.
   * @return self
   */
  public ColliderComponent setSensor(boolean isSensor) {
    if (fixture == null) {
      fixtureDef.isSensor = isSensor;
    } else {
      fixture.setSensor(isSensor);
    }
    return this;
  }

  /**
   * Set density
   *
   * @param density Density and size of the physics component determine the object's mass. default =
   *     0
   * @return self
   */
  public ColliderComponent setDensity(float density) {
    if (fixture == null) {
      fixtureDef.density = density;
    } else {
      fixture.setDensity(density);
    }
    return this;
  }

  /**
   * Set restitution
   *
   * @param restitution restitution is the 'bounciness' of an object, default = 0
   * @return self
   */
  public ColliderComponent setRestitution(float restitution) {
    if (fixture == null) {
      fixtureDef.restitution = restitution;
    } else {
      fixture.setRestitution(restitution);
    }
    return this;
  }

  /**
   * Set shape
   *
   * @param shape shape, default = bounding box the same size as the entity
   * @return self
   */
  public ColliderComponent setShape(Shape shape) {
    if (fixture == null) {
      fixtureDef.shape = shape;
    } else {
      logger.error("{} Cannot set Collider shape after create(), ignoring.", this);
    }
    return this;
  }

  /** @return Physics fixture of this collider. Null before created() */
  public Fixture getFixture() {
    return fixture;
  }

  /**
   * Set the collider layer, used in collision logic
   * @param layerMask Bitmask of {@link PhysicsLayer} this collider belongs to
   * @return self
   */
  public ColliderComponent setLayer(short layerMask) {
    if (fixture == null) {
      fixtureDef.filter.categoryBits = layerMask;
    } else {
      Filter filter = fixture.getFilterData();
      filter.categoryBits = layerMask;
      fixture.setFilterData(filter);
    }
    return this;
  }

  /**
   * @return The {@link PhysicsLayer} this collider belongs to
   */
  public short getLayer() {
    if (fixture == null) {
      return fixtureDef.filter.categoryBits;
    }
    return fixture.getFilterData().categoryBits;
  }

  /**
   * @return Size of collider component
   */
  public Vector2 getScale() {
    return scale;
  }

  private Shape makeBoundingBox() {
      PolygonShape bbox = new PolygonShape();
      Vector2 center = entity.getScale().scl(0.5f);

      /*
        height would normally be tan(30 deg) * size.y for iso, but we estimate
        it with a 1:2 ratio. Therefore, height is half of the y size.
       */
      float height = 0.5f * center.y;

      Vector2 west = new Vector2(0 - (center.x / 2), (height / 2));
      Vector2 north = new Vector2(0, height);
      Vector2 east = new Vector2((center.x / 2), (height / 2));
      Vector2 south = new Vector2(0, 0);
      // Collect each of the isometric parallelogram's corners
      Vector2[] points = new Vector2[]{west, north, east, south};

      bbox.set(points);
      setShape(bbox);
      return bbox;
    }
}

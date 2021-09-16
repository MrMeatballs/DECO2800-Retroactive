package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.areas.components.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_TREES = 7;
  private static final int NUM_GHOSTS = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final GridPoint2 BED_SPAWN = new GridPoint2(5, 20);
  private static final GridPoint2 DOOR_SPAWN = new GridPoint2(5, 10);
  private static final GridPoint2 TV_SPAWN = new GridPoint2(5, 15);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
          "images/objects/tree/tree.png",
          "images/tiles/ortho/ortho_grass_1.png",
          "images/tiles/ortho/ortho_grass_2.png",
          "images/tiles/ortho/ortho_grass_3.png",
          "images/tiles/hex/hex_grass_1.png",
          "images/tiles/hex/hex_grass_2.png",
          "images/tiles/hex/hex_grass_3.png",
          "images/tiles/iso/iso_grass_1.png",
          "images/tiles/iso/iso_grass_2.png",
          "images/tiles/iso/iso_grass_3.png",
          "images/tiles/iso/iso_floor_1.png",
          "images/tiles/iso/iso_floor_1_alt.png",
          "images/objects/door/door_animationL.png",
          "images/objects/tv/TV_animationL.png",
          "images/objects/furniture/coffee_table_left.png"
  };

  private static final String[] forestTextureAtlases = {
          "images/tiles/iso/iso_terrain_grass.atlas",
          "images/objects/bed/bed.atlas",
          "images/tiles/iso/iso_terrain_grass.atlas",
          "images/characters/boy_01/boy_01.atlas",
          "images/characters/mum_01/mum_01.atlas",
          "images/objects/door/door_animationL.atlas",
          "images/objects/tv/TV_animationL.atlas",
          "images/objects/energy_drink/energy.atlas",
          "images/characters/girl_00/girl_00.atlas",
          "images/characters/boy_00/boy_00.atlas",
          "images/objects/banana_peel/banana.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  public Entity player;
  public Entity mum;

  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();
    displayUI();
    spawnTerrain();
    player = spawnPlayer();
    spawnEnergyDrink();
    spawnBananaPeel();
    spawnBed();
    spawnDoor();
    spawnTV();
//    spawnTrees();
//    spawnGhosts();
//    spawnGhostKing();
    mum = spawnMum();
    //playMusic();
  }

  public Entity getPlayer(){
    return player;
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO_ISO);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    /*spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
        new GridPoint2(tileBounds.x, 0),
        false,
        false);
    // Top
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
        new GridPoint2(0, tileBounds.y),
        false,
        false);
    // Bottom
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
        GridPoint2Utils.ZERO, false, false);*/
  }

  private void spawnEnergyDrink() {
    Entity drink = ObstacleFactory.createEnergyDrink();
    spawnEntityAt(drink, new GridPoint2(10,10), true, true);
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  private void spawnFurniture(){
    Entity furniture = ObstacleFactory.createFurniture("images/objects/furniture/coffee_table_left.png");

  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnBananaPeel(){
    Entity peel = ObstacleFactory.createBananaPeel();
    spawnEntityAt(peel, new GridPoint2(15,15), true, true);
  }

  private void spawnBed() {
    // Note: interactable objects must be created AFTER the player, as it requires the player
    // entity as an argument
    Entity bed = ObstacleFactory.createBed();
    spawnEntityAt(bed, BED_SPAWN, true, true);
  }

  private void spawnDoor(){
    Entity door = ObstacleFactory.createDoor();
    spawnEntityAt(door, DOOR_SPAWN, true, true);
  }

  private void spawnTV(){
    Entity tv = ObstacleFactory.createTV();
    spawnEntityAt(tv, TV_SPAWN, true, true);
  }


  private Entity spawnMum() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity mum = NPCFactory.createMum(player);
    spawnEntityAt(mum, randomPos, true, true);

    return mum;
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}

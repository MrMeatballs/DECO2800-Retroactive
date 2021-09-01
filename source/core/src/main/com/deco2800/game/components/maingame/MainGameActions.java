package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
  private GdxGame game;

  public MainGameActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("winDefault", this::onWinDefault);
    entity.getEvents().addListener("lossTimed", this::onLossTimed);
    entity.getEvents().addListener("lossCaught", this::onLossCaught);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to main menu screen...");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onWinDefault() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default win screen...");
    game.setScreen(GdxGame.ScreenType.WIN_DEFAULT);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onLossTimed() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default loss screen...");
    game.setScreen(GdxGame.ScreenType.LOSS_TIMED);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onLossCaught() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default loss screen...");
    game.setScreen(GdxGame.ScreenType.LOSS_CAUGHT);
  }
}

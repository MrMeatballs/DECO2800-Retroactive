package com.deco2800.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseAttack;
  private int stamina;

  public CombatStatsComponent(int health, int baseAttack, int stamina) {
    setHealth(health);
    setBaseAttack(baseAttack);
    setStamina(stamina);
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isDead() {
    return health == 0;
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (health >= 0) {
      this.health = health;
    } else {
      this.health = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  /**
   * Returns the entity's stamina.
   *
   * @return entity's stamina
   */
  public int getStamina() {
    return stamina;
  }

  /**
   * Sets the entity's stamina. Stamina has a minimum bound of 0.
   *
   * @param stamina stamina
   */
  public void setStamina(int stamina) {
    if (stamina >= 0) {
      this.stamina = stamina;
    } else {
      this.stamina = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateStamina", this.stamina);
    }
  }

  /**
   * Adds to the player's stamina. The amount added can be negative.
   *
   * @param stamina stamina to add
   */
  public void addStamina(int stamina) {
    setStamina(this.stamina + stamina);
  }

  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getBaseAttack();
    setHealth(newHealth);
  }

  /**
  public void run() {
    int newStamina = getStamina() - attacker.getBaseAttack();
    setHealth(newHealth);
  }
   */ //TODO decrease stamina
}

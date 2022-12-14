package com.JaguarGame.GameServer.util;

public class Player {
  private final int id;
  private final String name;
  private Game game;

  public Player(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Game getGame() {
    return this.game;
  }

  public void setGame(Game game) {
    this.game = game;
    if (!this.game.hasPlayer1()) {
      this.game.setPlayer1(this);
      System.out.println("Set player '" + this.name + "' as JAGUAR on game " + game.getId() + ".");
    } else {
      this.game.setPlayer2(this);
      System.out.println("Set player '" + this.name + "' as DOGS on game " + game.getId() + ".");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Player))
      return false;
    Player player = (Player) o;
    return this.id == player.id;
  }

  @Override
  public int hashCode() {
    return this.id;
  }
}

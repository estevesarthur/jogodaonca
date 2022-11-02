package com.JaguarGame.Client;

//JOGO é startado desta classe que chama a ClientGame
public class GameClient {
  public static void main(String [] args) {
    if (args.length != 2) {
      System.out.println("Usage: java GameClient <host> <name>");
      System.exit(1);
    }

    ClientGame game = new ClientGame(args[0]);
    game.start(args[1]);
  }
}
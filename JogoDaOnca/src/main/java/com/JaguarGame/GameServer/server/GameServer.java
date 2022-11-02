package com.JaguarGame.GameServer.server;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class GameServer {
  public static void main(String[] args) {
    try {
      java.rmi.registry.LocateRegistry.createRegistry(1099);
      System.out.println("RMI registry ready.");
    } catch (RemoteException e) {
      System.out.println("RMI registry already running.");
    }

    try {
      Naming.rebind("Game", new Game());
      System.out.println("GameServer is ready.");
    } catch (Exception e) {
      System.out.println("GameServer failed:");
      e.printStackTrace();
    }
  }
}

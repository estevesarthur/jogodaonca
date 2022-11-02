package com.JaguarGame.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {
    public int registerPlayer(String playerName) throws RemoteException;

    public int endGame(int playerId) throws RemoteException;

    public int hasGame(int playerId) throws RemoteException;

    public int isMyTurn(int playerId) throws RemoteException;

    public String getGrid(int playerId) throws RemoteException;

    public int sendMove(int playerId, int dogId, Direction direction) throws RemoteException;

    public String getOpponent(int playerId) throws RemoteException;
}

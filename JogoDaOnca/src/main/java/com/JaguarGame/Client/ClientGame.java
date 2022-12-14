package com.JaguarGame.Client;

import com.JaguarGame.Common.Direction;
import com.JaguarGame.Common.GameInterface;
import com.JaguarGame.Common.PlayerType;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class ClientGame {

	private GameInterface remoteGame;
	private int playerId;
	private PlayerType playerType;
	private String opponentName;
	private boolean gameEnded;

	public ClientGame(String serverName) {
		try {
			this.remoteGame = (GameInterface) Naming.lookup("//" + serverName + "/JaguarGame");
			this.gameEnded = false;
		} catch (Exception e) {
			System.out.println("GameClient failed:");
			e.printStackTrace();
		}
	}
	public void start(String playerName) {
		try {
			this.register(playerName);
			this.waitForGame();
			this.play();

		} catch (Exception e) {
			System.out.println("\nGameClient failed:");
			e.printStackTrace();
		}
	}
	private void register(String playerName) throws RemoteException, Exception {
		int id = remoteGame.registerPlayer(playerName);

		if (id == -1)
			throw new Exception("Player '" + playerName + "' already registered.");
		if (id == -2)
			throw new Exception("Reached max number of registered players.");
		if (id < 0)
			throw new Exception("Unknown error");

		this.playerId = id;

		System.out.println("Registered with ID " + this.playerId + ".");
	}

	private void waitForGame() throws RemoteException, InterruptedException {
		System.out.print("Waiting for opponent match...");
		int hasGame = 0;
		while ((hasGame = this.remoteGame.hasGame(this.playerId)) == 0) {
			sleep(500);
			System.out.print(".");
		}

		if (hasGame == -2) {
			throw new RemoteException("Matchmaking timeout occured.");
		}

		this.opponentName = this.remoteGame.getOpponent(this.playerId);
		System.out.println("\nFound opponent! His name is " + this.opponentName + ".\n");

		if (hasGame == 1) {
			System.out.println("You are the JAGUAR!");
			this.playerType = PlayerType.Jaguar;
		} else if (hasGame == 2) {
			System.out.println("You are the DOGS!");
			this.playerType = PlayerType.Dog;
		}
	}

	private void showGrid() throws RemoteException {
		System.out.println(this.remoteGame.getGrid(this.playerId));
	}

	private void play() throws RemoteException, Exception {
		int isMyTurn = 0;
		while (isMyTurn == 0 || isMyTurn == 1) {
			isMyTurn = this.remoteGame.isMyTurn(this.playerId);
			switch (isMyTurn) {
				case -1:
					throw new Exception("Unknown error");
				case 0:
					theirTurn();
					break;
				case 1:
					myTurn();
					break;
				case 2:
					winner();
					break;
				case 3:
					loser();
					break;
				case 4:
					throw new Exception("Unknown error");
				case 5:
					winnerByWO();
					break;
				case 6:
					loserByWO();
					break;
				default:
					throw new Exception("Unknown error");
			}

			if (this.gameEnded)
				return;
		}
	}

	private void myTurn() throws InterruptedException, RemoteException {
		System.out.println("\n" + this.remoteGame.getGrid(this.playerId));
		if (this.playerType == PlayerType.Jaguar)
			doJaguarPlay();
		else
			doDogPlay();
	}

	private void theirTurn() throws InterruptedException {
		System.out.println("Waiting for " + this.opponentName + " to play.");
		sleep(1000);
	}

	private void winner() throws InterruptedException {
		System.out.println("You win.");
		this.gameEnded = true;
	}

	private void loser() throws InterruptedException {
		System.out.println("You lose.");
		this.gameEnded = true;
	}

	private void winnerByWO() throws InterruptedException {
		System.out.println("You win by WO.");
		this.gameEnded = true;
	}

	private void loserByWO() throws InterruptedException {
		System.out.println("You lose by WO.");
		this.gameEnded = true;
	}

	private void doJaguarPlay() throws RemoteException {
		int moveResult;
		while ((moveResult = this.remoteGame.sendMove(this.playerId, -1, promptForDirection())) == 0) {
			if (moveResult == 0) {
				System.out.println("Invalid movement. Try again.");
			}
		}

		if (moveResult == -1)
			throw new RemoteException("Unexpected error.");
	}

	private void doDogPlay() throws RemoteException {
		int moveResult;
		while ((moveResult = this.remoteGame.sendMove(this.playerId, promptForDog(), promptForDirection())) == 0) {
			if (moveResult == 0) {
				System.out.println("Invalid movement. Try again.");
			}
		}

		if (moveResult == -1)
			throw new RemoteException("Unexpected error.");
	}

	private Direction promptForDirection() {
		StringBuilder output = new StringBuilder("Which way you wanna go?\n\t");

		Direction[] directions = Direction.values();
		output.append("\n\t| 7 | 8 | 9 |\n");
		output.append("\t|---|---|---|\n");
		output.append("\t| 4 |   | 6 |\n");
		output.append("\t|---|---|---|\n");
		output.append("\t| 1 | 2 | 3 |\n");

		output.append("\n").append(" >> ");

		int playerChoice = 0;
		Direction dir = null;

		while (dir == null) {
			System.out.print(output.toString());
			playerChoice = Integer.parseInt(new Scanner(System.in).next());

			switch (playerChoice) {
				case 1:
					dir = Direction.DownLeft;
					break;
				case 2:
					dir = Direction.Down;
					break;
				case 3:
					dir = Direction.DownRight;
					break;
				case 4:
					dir = Direction.Left;
					break;
				case 6:
					dir = Direction.Right;
					break;
				case 7:
					dir = Direction.UpLeft;
					break;
				case 8:
					dir = Direction.Up;
					break;
				case 9:
					dir = Direction.UpRight;
					break;
				default:
					dir = null;
					break;
			}
		}

		return dir;
	}

	public int promptForDog() {
		StringBuilder output = new StringBuilder("Which doge you wanna move?\n\t");

		for (int i = 0; i < 14; i++) {
			output.append(i);
			if (i < 13)
				output.append(", ");
		}

		output.append("\n >> ");

		int playerChoice = -1;

		while (playerChoice < 0 || playerChoice > 13) {
			System.out.print(output.toString());
			playerChoice = Integer.parseInt(new Scanner(System.in).next());
		}

		return playerChoice;
	}
}
package com.herokuapp.darkfire.neuroga.player;

import java.util.Random;

import com.herokuapp.darkfire.neuroga.game.Game2048;

public class RandomPlayer {

	private Game2048 game;
	
	public RandomPlayer(Game2048 game) {
		this.game = game;
	}
	
	public String getMove() {
		var possibleAction = new String[] {"left", "right", "up", "down"};
		var action = possibleAction[new Random().nextInt(possibleAction.length)];
		return action;
	}
	
}

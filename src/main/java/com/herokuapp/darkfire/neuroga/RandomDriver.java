package com.herokuapp.darkfire.neuroga;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.herokuapp.darkfire.neuroga.game.Game2048;
import com.herokuapp.darkfire.neuroga.player.RandomPlayer;

public class RandomDriver {

	public static void main(String[] args) {
		try {
			new RandomDriver().run(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Game2048 run(boolean gui) throws Exception {
		Game2048 game = new Game2048(false);

		if (gui) {
			JFrame gameFrame = new JFrame();
			gameFrame.setTitle("Random Player");
			gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			gameFrame.setSize(340, 400);
			gameFrame.setResizable(false);
			gameFrame.add(game);
			gameFrame.setLocationRelativeTo(null);
			gameFrame.setVisible(true);
		}
		
		
		var player = new RandomPlayer(game);
		
		while (!game.isMyLose() && !game.isMyWin()) {
			game.acceptMove(player.getMove());
			if(gui) Thread.sleep(500);
		}
		return game;
	}
}

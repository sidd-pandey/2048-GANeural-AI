package com.herokuapp.darkfire.neuroga.player;

import java.util.OptionalDouble;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import com.herokuapp.darkfire.neuroga.game.Game2048;
import com.herokuapp.darkfire.neuroga.game.Game2048.Tile;
import com.herokuapp.darkfire.neuroga.utilities.RandomIndexSelector;

public class NeuralPlayer {

	

	private BasicNetwork network;
	private NormalizedField[] tileFields;
	private String[] possibleMoves = new String[] {"left","right", "up","down"};

	public NeuralPlayer(BasicNetwork network) {
		tileFields = new NormalizedField[16];
		for (int i = 0; i < tileFields.length; i++) 
			tileFields[i] = new NormalizedField(NormalizationAction.Normalize, 
					""+(i+1));
		
		this.network = network;
		
	}
	
	public double scorePilot() {
		Game2048 game = new Game2048(false);
		
		int max_moves = 1000 * 5;
		int moves = 0;
		int tileCountChangeScore =0;
		int changeInScore = 0;
		while (!game.isMyLose() && !game.isMyWin() && moves < max_moves) {
			//prepare the input
			
			int oldtiles = (int) Stream.of(game.getMyTiles())
					.mapToDouble(Tile::getValue)
					.filter(val -> val > 0)
					.count();
			int oldScore = game.getMyScore();
			
			double[] tileVals = Stream.of(game.getMyTiles())
				.mapToDouble(Tile::getValue)
				.toArray();
			
			MLData input = new BasicMLData(tileVals);
			MLData output = this.network.compute(input);
			
			double[] prob = output.getData();
			game.acceptMove(possibleMoves[RandomIndexSelector.get(prob)]);
			
			int newtiles = (int) Stream.of(game.getMyTiles())
					.mapToDouble(Tile::getValue)
					.filter(val -> val > 0)
					.count();
			int newScore = game.getMyScore();
			moves++;
			
			tileCountChangeScore += (newtiles-oldtiles);
			changeInScore += (newScore - oldScore);
			
		}
		
		//get the max score
		OptionalDouble maxScore = Stream.of(game.getMyTiles())
			.mapToDouble(Tile::getValue)
			.max();
		
		return (maxScore.getAsDouble()*5 + tileCountChangeScore + changeInScore)+game.getMyScore()/10;
	}

	
	public double scorePilot(int times) {
		double points = 0;
		for (int i = 0; i < times; i++)
			points += scorePilot();
		return points/times;
	}
	
	public int playGuiGame() {
		JFrame gameFrame = new JFrame();
		gameFrame.setTitle("Neural Player");
		gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gameFrame.setSize(340, 400);
		gameFrame.setResizable(false);

		Game2048 game = new Game2048(false);
		gameFrame.add(game);

		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);
		
		
		while (!game.isMyLose() && !game.isMyWin()) {
			//prepare the input
			double[] tileVals = Stream.of(game.getMyTiles())
				.mapToDouble(Tile::getValue)
				.toArray();
			
			MLData input = new BasicMLData(tileVals);
			MLData output = this.network.compute(input);
			
			double[] prob = output.getData();
			game.acceptMove(possibleMoves[RandomIndexSelector.get(prob)]);

			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return game.getMyScore();
	}
	
}

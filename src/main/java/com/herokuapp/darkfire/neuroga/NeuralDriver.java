package com.herokuapp.darkfire.neuroga;

import java.io.File;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import com.herokuapp.darkfire.neuroga.game.Game2048;
import com.herokuapp.darkfire.neuroga.game.Game2048.Tile;
import com.herokuapp.darkfire.neuroga.utilities.RandomIndexSelector;

public class NeuralDriver {

	public static void main(String[] args) {
		try {
			new NeuralDriver().run(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Game2048 run(boolean gui) throws Exception {
		BasicNetwork  network =
		( BasicNetwork ) EncogDirectoryPersistence.loadObject(new File("saved_nn/nn_1532933065869") ) ;
		
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
		
		String[] possibleMoves = new String[] {"left","right", "up","down"};
		
		while (!game.isMyLose() && !game.isMyWin()) {
			double[] tileVals = Stream.of(game.getMyTiles())
					.mapToDouble(Tile::getValue)
					.toArray();
				
				MLData input = new BasicMLData(tileVals);
				MLData output = network.compute(input);
				
				double[] prob = output.getData();
				game.acceptMove(possibleMoves[RandomIndexSelector.get(prob)]);
				
				if (gui) Thread.sleep(500);
		}
		return game;
	}
}

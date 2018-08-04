package com.herokuapp.darkfire.neuroga;

import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.stream.Stream;

import com.herokuapp.darkfire.neuroga.game.Game2048;
import com.herokuapp.darkfire.neuroga.game.Game2048.Tile;

public class AverageResultsRun {

	public static void main(String... args) {

		printAverageResults();
	}

	private static void printAverageResults() {

		
		final var maxGames = 5000;
		System.out.println("Calulating average points score for "+maxGames+" games "
				+ "between Neural Network and Random Player");

		
		var nnPlayerScore = 0;
		var randPlayerScore = 0;
		
		var bestTileNN = new TreeMap<Double, Integer>();
		var bestTileRand = new TreeMap<Double, Integer>();
		
		for (int i = 0; i < maxGames; i++) {
			if (i % 1000 == 0 ) System.out.println("Completed "+(i)+"th simulation");
			try {
				
				var nngame = new NeuralDriver().run(false);
				nnPlayerScore += nngame.getMyScore();
				var maxScoreNN = getMaxTile(nngame);
				bestTileNN.put(maxScoreNN, bestTileNN.getOrDefault(maxScoreNN, 0)+1);
				
				var rgame = new RandomDriver().run(false);
				randPlayerScore += rgame.getMyScore();
				var maxScoreR = getMaxTile(rgame);
				bestTileRand.put(maxScoreR, bestTileRand.getOrDefault(maxScoreR, 0)+1);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Average Random Score: " + (double)randPlayerScore/maxGames);
		System.out.println("Average Neural Player Score: " + (double)nnPlayerScore/maxGames);
		
		System.out.println("Best Tile Frequency for Random Player: ");
		bestTileRand.forEach((tile, count) -> System.out.println(tile+" = " + count));
		System.out.println("Best Tile Frequency for NN Player: ");
		bestTileNN.forEach((tile, count) -> System.out.println(tile+" = " + count));


	}
	
	public static double getMaxTile(Game2048 game) {
		OptionalDouble maxScore = Stream.of(game.getMyTiles())
				.mapToDouble(Tile::getValue)
				.max();
		return maxScore.getAsDouble();
	}
}

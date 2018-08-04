package com.herokuapp.darkfire.neuroga.nn;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.networks.BasicNetwork;

import com.herokuapp.darkfire.neuroga.player.NeuralPlayer;

public class GameScore implements CalculateScore {

	@Override
	public double calculateScore(MLMethod model) {
		var player = new NeuralPlayer((BasicNetwork) model);
		return player.scorePilot(1);
	}

	@Override
	public boolean requireSingleThreaded() {
		return false;
	}

	@Override
	public boolean shouldMinimize() {
		return false;
	}

}

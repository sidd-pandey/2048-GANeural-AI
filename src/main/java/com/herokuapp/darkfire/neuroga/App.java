package com.herokuapp.darkfire.neuroga;

import java.io.File;

import org.encog.engine.network.activation.ActivationReLU;
import org.encog.engine.network.activation.ActivationSoftMax;
import org.encog.ml.MLMethod;
import org.encog.ml.MLResettable;
import org.encog.ml.MethodFactory;
import org.encog.ml.genetic.MLMethodGeneticAlgorithm;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.persist.EncogDirectoryPersistence;

import com.herokuapp.darkfire.neuroga.nn.GameScore;
import com.herokuapp.darkfire.neuroga.player.NeuralPlayer;


public class App 
{
	public static BasicNetwork createNetwork(){
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(16);
		pattern.addHiddenLayer(100);
		pattern.addHiddenLayer(25);
		pattern.setOutputNeurons(4);
		pattern.setActivationOutput(new ActivationSoftMax());
		pattern.setActivationFunction(new ActivationReLU());
		BasicNetwork network = (BasicNetwork)pattern.generate();
		network.reset();
		return network;
	}
	
	public static void main(String args[]){
		
		
		MLTrain train = new MLMethodGeneticAlgorithm(new MethodFactory(){
			@Override
			public MLMethod factor() {
				final BasicNetwork result = createNetwork();
				((MLResettable)result).reset();
				return result;
			}},new GameScore(),1000);
		
		int epoch = 1;
		int max_epoch = 50;

		System.out.println("Starting learning...");
		for(int i=0 ; i< max_epoch; i++) {
			train.iteration();
			System.out.println("Epoch #" + epoch + 
					" Score:" + train.getError());
			epoch++;
		} 
		train.finishTraining();
		System.out.println("Finished learning...");
		
		System.out.println("\nHow the winning network performes:");
		var bestNetwork = (BasicNetwork)train.getMethod();
		var pilot = new NeuralPlayer(bestNetwork);
		System.out.println(pilot.scorePilot(10));
		
		System.out.println("Saving to disk...");
		EncogDirectoryPersistence.saveObject(new File("saved_nn/nn_"+System.currentTimeMillis()), bestNetwork);
		
		System.out.println("Preparing to launch gui session...");
		pilot = new NeuralPlayer(bestNetwork);
		pilot.playGuiGame();
		
	}

}

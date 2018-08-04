package com.herokuapp.darkfire.neuroga.utilities;

import java.util.Arrays;
import java.util.Random;

public class RandomIndexSelector {

	public static int get(double[] probs) {
		
		int sum = Arrays.stream(probs)
			.mapToInt(p -> (int)(p*10))
			.sum()+probs.length;
		
		var narr = new int[sum];
		
		var csum = 0;
		for (int i = 0; i < probs.length; i++) {
			int fill = (int)(probs[i]*10)+1;
			for (int j = csum; j < csum+fill; j++) narr[j] = i;
			csum += fill;
		}
		
		return narr[new Random().nextInt(narr.length)];
	}
}

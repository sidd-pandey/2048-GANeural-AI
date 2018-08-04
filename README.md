# Neural Network powered AI to play 2048
Java project using encog to train a neural network to play 2048. Neural Network weights are tuned using Genetic Algorithm


#### Average Score by Random Moves Player: 1086.2768
#### Average Score by Neural Player: 1160.16


| Max Tile Reached |	Random | Neural Network|
| ------------- |:-------------:| -----:|
|16  | 17    |	13   |
|32  | 329 	 |  338  |
|64  |	1874 | 	1867 |
|128 |	2419 |	2337 |
|256 |	361  |	444  |
|512 |	0 	 | 1     |

![alt text](https://github.com/sidd-pandey/2048-GANeural-AI/blob/master/barplot.png "Count of max tiles reached")

Even though it looks like random player out performs NN in raching till 128, NN ai outperforms the former by reaching tile 256 more number of times and it also reaches tile 512 once. The averge score of both players are comparable, with NN's average score slightly higher.

Feed Forward neural networks are used, with 16 tiles as input, 2 hidden layer with 100 and 25 ReLu units, output layer has 4 neurons with softmax activation function. 

The fitness function used right now is maxScore*5 + tileCountChangeScore + changeInScore + finalScore/10. The main intution for adding other components other than final score is to enable tune NN to reach higher tiles (max score) but also make moves which maximizes the collpases more number of tiles (else in many cases, NN was always repeatedly choosing a certain move, even though there was no change in game board through that move, ultimately getting stuck).

Java implementaion of 2048 was taken from https://github.com/bulenkov/2048.

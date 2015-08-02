/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trainer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEventType;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Carlos
 */
public class Trainer implements LearningEventListener {
        public PrintWriter filePtr;
        public static void main(String[] args) throws IOException {
        new Trainer().run();
    }
    
    /**
     * Runs this sample
     */
    public void run() throws IOException {
    	System.out.println("Numero de neuronas de la capa oculta");
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //int numHiddenNeurons = Integer.parseInt(reader.readLine());
        int numHiddenNeurons = 40;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String fileName = "Log"+numHiddenNeurons+".txt";
         filePtr = new PrintWriter(new FileWriter(fileName)); 
        filePtr.println(numHiddenNeurons+" neuronas ocultas");
        filePtr.println("Inicio a las: "+dateFormat.format(date));
        
        // create training set (logical XOR function)
        int rows=0;
        DataSet trainingSet = new DataSet(784, 10);
        
        String fileToParse = "digitDatabase.csv";
        BufferedReader br = null;
        String delimiter = ",";
        
        
        //Delimiter used in CSV file
        try {
 
		br = new BufferedReader(new FileReader(fileToParse));
                String line;
                br.readLine();
		while ((line = br.readLine()) != null && rows<1000) {
		        // use comma as separator
			String[] data = line.split(delimiter);
                        rows++;
                        double[] row = new double[784];
                        double[] out;

                        for (int i=1; i<785; i++){
                            row[i-1]=Double.parseDouble(data[i]);
                        }
                        switch (data[0])
                        {
                            case "0": out = new double[]{1,0,0,0,0,0,0,0,0,0}; break;
                            case "1": out = new double[]{0,1,0,0,0,0,0,0,0,0}; break;
                            case "2": out = new double[]{0,0,1,0,0,0,0,0,0,0}; break;
                            case "3": out = new double[]{0,0,0,1,0,0,0,0,0,0}; break;
                            case "4": out = new double[]{0,0,0,0,1,0,0,0,0,0}; break;
                            case "5": out = new double[]{0,0,0,0,0,1,0,0,0,0}; break;
                            case "6": out = new double[]{0,0,0,0,0,0,1,0,0,0}; break;
                            case "7": out = new double[]{0,0,0,0,0,0,0,1,0,0}; break;
                            case "8": out = new double[]{0,0,0,0,0,0,0,0,1,0}; break;
                            case "9": out = new double[]{0,0,0,0,0,0,0,0,0,1}; break;
                            default: out = new double[]{0,0,0,0,0,0,0,0,0,0}; break;
                        }
                        
                        trainingSet.addRow(row, out);
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
        
        // create multi layer perceptron
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 784, numHiddenNeurons, 10);

        // enable batch if using MomentumBackpropagation
        if( myMlPerceptron.getLearningRule() instanceof MomentumBackpropagation )
        	((MomentumBackpropagation)myMlPerceptron.getLearningRule()).setMomentum(0.7);

        LearningRule learningRule = myMlPerceptron.getLearningRule();
        learningRule.addListener(this);
        
        // learn the training set
        System.out.println("Training neural network...");
        myMlPerceptron.learn(trainingSet);

        // test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(myMlPerceptron, trainingSet, filePtr);

        // save trained neural network
        myMlPerceptron.save("myMlPerceptron.nnet");
        
        // load saved neural network
        NeuralNetwork loadedMlPerceptron = NeuralNetwork.load("myMlPerceptron.nnet");

        // test loaded neural network
        System.out.println("Testing loaded neural network");
        testNeuralNetwork(loadedMlPerceptron, trainingSet, filePtr);
        
        
        date = new Date();
        filePtr.println("Fin a las: "+dateFormat.format(date));

        
        filePtr.close();

    }

    /**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param trainingSet training set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet, PrintWriter filePtr) {

        for(DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString( testSetRow.getInput() ) );
            System.out.println(" Output: " + Arrays.toString( networkOutput) );
            
           
        }
    }
    
    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation)event.getSource();
        if (event.getEventType() != LearningEventType.LEARNING_STOPPED){
            System.out.println(bp.getCurrentIteration() + ". iteration : "+ bp.getTotalNetworkError());
            filePtr.println(bp.getCurrentIteration() + ". iteration : "+ bp.getTotalNetworkError());
        }  
    }  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import ij.ImagePlus;
import ij.macro.MacroConstants;
import ij.process.ImageProcessor;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Carlos
 */
public class Tester {
    private NeuralNetwork loadedMlPerceptron;

        // test loaded neural network
    
    public static void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for(DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            //System.out.print("Input: " + Arrays.toString( testSetRow.getInput() ) );
            //System.out.println(" Output: " + Arrays.toString( networkOutput) );
            
            double maxval = networkOutput[0];
            int numberPredicted=0;
            for (int i=0; i<10; i++)
            {
                if (networkOutput[i]>maxval){
                    numberPredicted=i;
                    maxval=networkOutput[i];
                }
            }
            System.out.println("Salida: "+numberPredicted+" ("+maxval+").");
        }
    }
        
    public void loadNetwork(){
            loadedMlPerceptron = NeuralNetwork.load("myMlPerceptron.nnet");
            //Cargar archivo y guardarlo en el dataset
            DataSet dataset = new DataSet(784);


            //Load the file
            JFileChooser myChooser = new JFileChooser();
            myChooser.showOpenDialog(myChooser);
            File file = myChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            //Resize the image
            ImagePlus image = new ImagePlus(path);
            ImageProcessor ip = image.getProcessor();
            ip.setInterpolate(true);
            ImageProcessor newIp = ip.resize(28, 28);
            ImagePlus newImage = new ImagePlus("resized.pbm", newIp);
            image.show();
            newImage.show();
            BufferedImage buffer = image.getBufferedImage();
            
            double[] row = new double[784];
            byte[] pixeles = (byte[])newImage.getProcessor().getPixels();
            for (int i=0; i<784; i++){
                if (pixeles[i]==-1){
                    row[i]=1;
                }else /*if (pixeles[i]==-1)*/{
                    row[i]=0;
                }
            }
            dataset.addRow(row);
            testNeuralNetwork(loadedMlPerceptron, dataset);
    }
        
    public static void main(String[] args) {
        try {
            Tester myTester = new Tester();
            myTester.loadNetwork();
            System.out.println("Presione una tecla para finalizar");
            System.in.read();
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

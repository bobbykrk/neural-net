package com.company;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.Switch;
import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationRamp;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.normalize.DataNormalization;
import org.encog.util.normalize.input.InputField;
import org.encog.util.normalize.input.InputFieldCSV;
import org.encog.util.normalize.input.InputFieldCSVText;
import org.encog.util.normalize.output.OutputFieldRangeMapped;
import org.encog.util.normalize.output.nominal.OutputOneOf;
import org.encog.util.normalize.target.NormalizationStorageCSV;
import org.encog.util.simple.EncogUtility;
import org.encog.util.simple.TrainingSetUtil;
import org.encog.ml.data.specific.CSVNeuralDataSet;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;

import org.encog.Encog;
import org.encog.app.analyst.AnalystFileFormat;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.csv.normalize.AnalystNormalizeCSV;
import org.encog.app.analyst.script.normalize.AnalystField;
import org.encog.app.analyst.wizard.AnalystWizard;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.csv.CSVFormat;

import javax.swing.*;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.martiansoftware.jsap.*;
import sun.launcher.resources.launcher;

/**
 *
 */
public class NeuralNet {
    public static final File MYDIR = new File(".");

    public static void main(final String args[]) throws Exception {

        // do obslugi opcji wykorzystuje JSAP http://www.martiansoftware.com/jsap/
        SimpleJSAP jsap = new SimpleJSAP(
                "NeuralNet",
                "Program używa sieci neuronowej do klasyfikacji punktów na płaszczyźnie",
                new Parameter[] {
                        new FlaggedOption( "layer", JSAP.INTEGER_PARSER, null, JSAP.NOT_REQUIRED, 'l', JSAP.NO_LONGFLAG,
                                "Liczba wezlow w warstwach posrednich" ).setList(true).setListSeparator(','),
                        new FlaggedOption( "training file", JSAP.STRING_PARSER, "test.csv", JSAP.REQUIRED, 't', "training",
                                "Plik z danymi trenujacymi" )
                }
        );

        JSAPResult config = jsap.parse(args);
        if ( jsap.messagePrinted() ) System.exit( 1 );

        NeuralNet nn = new NeuralNet(config.getIntArray("layer"), config.getString("training file"));

//        String[] names = config.getStringArray("name");
//        String[] languages = config.getStringArray("verbose");


//        for (int i = 0; i < languages.length; ++i) {
//            System.out.println("language=" + languages[i]);
//        }



//        for (int i = 0; i < config.getInt("count"); ++i) {
//            for (int j = 0; j < names.length; ++j) {
//                System.out.println((config.getBoolean("verbose") ? "Hello" : "Hi")
//                        + ", "
//                        + names[j]
//                        + "!");
//            }
//        }

//        if (args.length == 0) {
//            System.out.println("Usage:\n\nXORCSV [xor.csv]");
//            return;
//        } else {
//            final MLDataSet trainingSet = TrainingSetUtil.loadCSVTOMemory(
//                    CSVFormat.ENGLISH, args[0], false, 2, 1);
//
//            BasicNetwork network = new BasicNetwork();
//            network.addLayer(new BasicLayer(null,true,2));
//            network.addLayer(new BasicLayer(new ActivationSigmoid(),true,10));
//            network.addLayer(new BasicLayer(new ActivationSigmoid(),true,10));
//            network.addLayer(new BasicLayer(new ActivationSigmoid(),false,3));
//            network.getStructure().finalizeStructure();
//            network.reset();
//
//
//        }

//        int output_count = 0;
//        output_count = normalizeFile("test.csv","test-norm.csv");
//            ev_retrain(output_count);



//        Encog.getInstance().shutdown();
    }


    public NeuralNet(int[] layers, String trainingFile  ) throws Exception{
        this.layers = layers;
        this.training_file = trainingFile;
        normalizeFile(trainingFile,"norm" + trainingFile);
        ev_retrain();
        Encog.getInstance().shutdown();
    }



    public void ev_retrain() throws Exception
    {
        BasicNetwork network = new BasicNetwork();

        // utworzenie wezlow wejsciowych
        network.addLayer(new BasicLayer(null,true,2));

        //utworzenie warstw ukrytych
        for (int anA : this.layers) {
            network.addLayer(new BasicLayer(new ActivationSigmoid(), true, anA));
        }

        // utworzenie warstwy wyjsciowej
        network.addLayer(new BasicLayer(new ActivationRamp(0.75, 0.25, 1, 0),false,outputNodesNumber));
        network.getStructure().finalizeStructure();
        network.reset();

        MLDataSet trainingSet = new CSVNeuralDataSet("norm"+training_file, 2,outputNodesNumber, false);
        final Backpropagation train = new Backpropagation(network, trainingSet, 0.1, 0.3);

        int epoch = 1;

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while(train.getError() > 0.01 && epoch < 1000);
        train.finishTraining();

        // test the neural network
        System.out.println("Neural Network Results:");
        CSVWriter outputCSV = new CSVWriter(new FileWriter("output.csv"), ',');
        String[] cols = {"X", "Y", "color"};
        outputCSV.writeNext(cols);
        for(MLDataPair pair: trainingSet ) {
            final MLData output = network.compute(pair.getInput());
            MLData input = pair.getInput();

            System.out.println(Arrays.toString(input.getData())
                    + ", actual=" + Arrays.toString(output.getData())
                    + "\t\t,ideal=" + Arrays.toString(pair.getIdeal().getData())
            );

            String[] row = {Double.toString(input.getData()[0]),Double.toString(input.getData()[1]),"3"};
            outputCSV.writeNext(row);
        }

        outputCSV.close();


    }


    /*
    * Metoda normalizuje plik source i zapisuje wynik w pliku target
    * Normalizacji podlegaja wspolrzedne punktu (do zakresu 0..1) oraz kolory punktow (do postaci 1 z N)
    * Oczekiwany format pliku to
    * <wspolrzedna x>,<wspolrzedna y>,<nazwa koloru>
    * Przykład:
    * 6.53068170432877,4.22782059769955,1
    * -3.6645269162385,-1.56901983393263,1
    * 2.30327785973916,1.77579994052398,2
    * -2.58335245652072,1.85762671895065,2
    * 0.210664559429051,-1.72101874043196,1
    * -5.79251691922912,-0.571824073813365,2
    * -3.80410962610863,0.590043488682297,1
    *
    * Nazwa koloru sama w sobie nie ma znaczenia, wazne zeby kolory reprezentujace ta
    * sama barwe mialy taka sama reprezentacje tekstowa
    */
    public void normalizeFile(String source, String target) {
        ReadCSV r = new ReadCSV(source, false, ',');
        HashSet<String> colors = new HashSet<String>();
        while (r.next()){
            colors.add(r.get(2));
        }
        // zapisuje potrzebna liczbe wezlow wyjsciowych
        outputNodesNumber = colors.size();

        File rawFile = new File(MYDIR, source);
        DataNormalization norm = new DataNormalization();
        InputField inputHorizontalPosition, inputVerticalPosition ;
        InputFieldCSVText inputColor;

        norm.addInputField(inputHorizontalPosition = new InputFieldCSV(true, rawFile, 0));
        norm.addInputField(inputVerticalPosition = new InputFieldCSV(true, rawFile, 1));
        norm.addInputField(inputColor = new InputFieldCSVText(true, rawFile, 2));

        for (String color : colors) {
            inputColor.addMapping(color);
        }

        norm.addOutputField(new OutputFieldRangeMapped(inputHorizontalPosition, 0, 1));
        norm.addOutputField(new OutputFieldRangeMapped(inputVerticalPosition, 0, 1));
        norm.addOutputField(new OutputOneOf(inputColor, 1, 0));


        File outputFile = new File(MYDIR, target);
        norm.setCSVFormat(CSVFormat.ENGLISH);
        norm.setTarget(new NormalizationStorageCSV(CSVFormat.ENGLISH, outputFile));

        norm.setReport(new ConsoleStatusReportable());
        norm.process();
    }

    private  String training_file = null;
    private  String test_file = null;
    private int[] layers = null;
    private int outputNodesNumber = 0;
}


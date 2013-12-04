package com.company;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.Switch;
import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
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
    /**
     * The input necessary for XOR.
     */
    public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
            { 0.0, 1.0 }, { 1.0, 1.0 } };

    /**
     * The ideal data necessary for XOR.
     */
    public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

    public NeuralNet(int[] layers){
        this.layers = layers;
        System.out.println(Arrays.toString(layers));
        int output_count = normalizeFile("test.csv","test-norm.csv");
        ev_retrain(output_count);
        Encog.getInstance().shutdown();
    }

    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) throws Exception {

        SimpleJSAP jsap = new SimpleJSAP(
                "NeuralNet",
                "Program używa sieci neuronowej do klasyfikacji punktów na płaszczyźnie",
                new Parameter[] {
                        new FlaggedOption( "layer", JSAP.INTEGER_PARSER, null, JSAP.NOT_REQUIRED, 'l', JSAP.NO_LONGFLAG,
                                "Liczba wezlow w warstwach posrednich" ).setList(true).setListSeparator(','),
                        new FlaggedOption( "training file", JSAP.STRING_PARSER, "test-norm.csv", JSAP.REQUIRED, 't', "training",
                                "Plik z danymi trenujacymi" )
                }
        );

        JSAPResult config = jsap.parse(args);
        if ( jsap.messagePrinted() ) System.exit( 1 );

        NeuralNet nn = new NeuralNet(config.getIntArray("layer"));

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

    public void ev_retrain(int output_count)
    {
        int[] a  = {10,10};
        BasicNetwork network = new BasicNetwork();

        // utworzenie wezlow wejsciowych
        network.addLayer(new BasicLayer(null,true,2));

        //utworzenie warstw ukrytych
        for (int anA : this.layers) {
            network.addLayer(new BasicLayer(new ActivationSigmoid(), true, anA));
        }

        // utworzenie warstwy wyjsciowej
        network.addLayer(new BasicLayer(new ActivationSigmoid(),false,output_count));
        network.getStructure().finalizeStructure();
        network.reset();




        // setup for training
   //     iteration = 0;
     //   network.randomize();
        MLDataSet trainingSet = new CSVNeuralDataSet("test-norm.csv", 2,output_count, false);
        final Backpropagation train = new Backpropagation(network, trainingSet, 0.1, 0.3);
//        grid.clear();
  //      plotPoints();
    //    pOutput.innerHTML = "Ready";


        int epoch = 1;

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while(train.getError() > 0.01 && epoch < 1000);
        train.finishTraining();

        // test the neural network
        System.out.println("Neural Network Results:");
        for(MLDataPair pair: trainingSet ) {
            final MLData output = network.compute(pair.getInput());

            System.out.println(Arrays.toString(pair.getInput().getData())
                    + ", actual=" + Arrays.toString(output.getData()) + ",ideal=" + Arrays.toString(pair.getIdeal().getData()));
        }


    }

    public static void dumpFieldInfo(EncogAnalyst analyst) {
        System.out.println("Fields found in file:");
        for (AnalystField field : analyst.getScript().getNormalize()
                .getNormalizedFields()) {

            StringBuilder line = new StringBuilder();
            line.append(field.getName());
            line.append(",action=");
            line.append(field.getAction());
            line.append(",min=");
            line.append(field.getActualLow());
            line.append(",max=");
            line.append(field.getActualHigh());
            System.out.println(line.toString());
        }
    }

    public static int normalizeFile(String source, String target) {
//        File sourceFile = new File(source);
//        File targetFile = new File(target);
//
//        EncogAnalyst analyst = new EncogAnalyst();
//
//        AnalystWizard wizard = new AnalystWizard(analyst);
//        wizard.wizard(sourceFile, true, AnalystFileFormat.DECPNT_COMMA);
//
//        dumpFieldInfo(analyst);
//
//        final AnalystNormalizeCSV norm = new AnalystNormalizeCSV();
//
//        norm.analyze(sourceFile, true, CSVFormat.ENGLISH, analyst);
//        norm.setProduceOutputHeaders(true);
//        norm.normalize(targetFile);
//        Encog.getInstance().shutdown();
        ReadCSV r = new ReadCSV(source, false, ',');

        HashSet<String> names = new HashSet();
        while (r.next()){
            names.add(r.get(2));
        }


        File rawFile = new File(MYDIR, source);
        DataNormalization norm = new DataNormalization();
        InputField inputHorizontalPosition, inputVerticalPosition ;
        InputFieldCSVText inputClass;

        norm.addInputField(inputHorizontalPosition = new InputFieldCSV(true, rawFile, 0));
        norm.addInputField(inputVerticalPosition = new InputFieldCSV(true, rawFile, 1));
        norm.addInputField(inputClass = new InputFieldCSVText(true, rawFile, 2));

        for (String name : names) {
            inputClass.addMapping(name);
        }






        // define how we should normalize

        norm.addOutputField(new OutputFieldRangeMapped(inputHorizontalPosition, 0, 1));
        norm.addOutputField(new OutputFieldRangeMapped(inputVerticalPosition, 0, 1));
        norm.addOutputField(new OutputOneOf(inputClass, 1, 0));

        // define where the output should go
        File outputFile = new File(MYDIR, target);
        norm.setCSVFormat(CSVFormat.ENGLISH);
        norm.setTarget(new NormalizationStorageCSV(CSVFormat.ENGLISH, outputFile));

        // process
        norm.setReport(new ConsoleStatusReportable());
        norm.process();
        System.out.println("Output written to: "
                + outputFile.getPath());

        return names.size();
    }

    private static String test_file = null;
    private int[] layers = null;
}


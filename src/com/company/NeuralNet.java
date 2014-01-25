package com.company;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationRamp;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
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
import org.encog.ml.data.specific.CSVNeuralDataSet;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;

import java.util.*;

import com.martiansoftware.jsap.*;

/**
 *
 */
public class NeuralNet {
    public static final File MYDIR = new File("./tests");
    public static final double MAX_ERROR = 0.01;
    public static final double MAX_EPOCH = 1000;

    private  String data_file = null;
    private  String norm_data_file = null;
    private  String norm_training_file = null;
    private  String norm_test_file = null;
    private  String result_file = null;
    private int[] layers = null;
    private int outputNodesNumber = 0;

    public static void main(final String args[]) throws Exception {

        // do obslugi opcji wykorzystuje JSAP http://www.martiansoftware.com/jsap/
        SimpleJSAP jsap = new SimpleJSAP(
                "NeuralNet",
                "Program używa sieci neuronowej do klasyfikacji punktów na płaszczyźnie",
                new Parameter[] {
                        new FlaggedOption( "layer", JSAP.INTEGER_PARSER, null, JSAP.NOT_REQUIRED, 'l', JSAP.NO_LONGFLAG,
                                "Liczba wezlow w warstwach posrednich" ).setList(true).setListSeparator(','),
                        new FlaggedOption( "training file", JSAP.STRING_PARSER, "data.csv", JSAP.REQUIRED, 't', "training",
                                "Plik z danymi trenujacymi" )
                }
        );

        JSAPResult config = jsap.parse(args);
        if ( jsap.messagePrinted() ) System.exit( 1 );
//
//        NeuralNet nn = new NeuralNet(config.getIntArray("layer"), config.getString("training file"));

        List<int[]> params = new ArrayList();
        int layers[] = new int[]{};
        params.add(layers);
        layers = new int[]{10};
        params.add(layers);
        layers = new int[]{20};
        params.add(layers);
        layers = new int[]{50};
        params.add(layers);
        layers = new int[]{10,10};
        params.add(layers);

        for(int[] l : params){
            File folder = new File("./tests/");
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile()) {
                    if(fileEntry.getName().matches("set_.*[^(png)]")){
                        System.out.println(fileEntry.getName());
                        NeuralNet nn = new NeuralNet(l, fileEntry.getName(), 0.25, true);
                    }
                }
            }
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile()) {
                    if(fileEntry.getName().matches("set_.*[^(png)]")){
                        System.out.println(fileEntry.getName());
                        NeuralNet nn = new NeuralNet(l, fileEntry.getName(), 0.25, false);
                    }
                }
            }
        }

    }


    public NeuralNet(int[] layers, String trainingFile, double div, boolean outputType) throws Exception{
        this.layers = layers;
        this.data_file = trainingFile;
        this.norm_data_file = "norm_" + trainingFile;
        this.norm_test_file = "test_" + norm_data_file;
        this.norm_training_file = "training_" + norm_data_file;
        if(outputType){
            this.result_file = "results_" + stringify(layers) + "_binary";
        } else {
            this.result_file = "results_" + stringify(layers) + "_one_of";
        }
        new File("./" + result_file).mkdir();
        run(div, outputType);
        Encog.getInstance().shutdown();
    }

    /**
     * Budowanie sieci
     * @return zbudowana sieć neuronowa
     */
    public BasicNetwork createNetwork(){
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
        return network;
    }


    /**
     * Trening sieci
     * @param network sieć
     * @param trainingSet zbiór danych trenujących
     */
    public void train(BasicNetwork network, MLDataSet trainingSet){
        final Backpropagation train = new Backpropagation(network, trainingSet, 0.1, 0.3);

        int epoch = 1;

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while(train.getError() > MAX_ERROR && epoch < MAX_EPOCH);
        train.finishTraining();
    }

    public void predict(BasicNetwork network, MLDataSet testSet) throws Exception
    {

        // test the neural network
        System.out.println("Neural Network Results:");
        CSVWriter outputCSV = new CSVWriter(new FileWriter("./"+result_file+"/" + data_file + "_output.csv"), ',');
        PrintWriter metricsOut = new PrintWriter("./"+result_file+"/" + data_file + "_metrics.csv");
        String[] cols = {"X", "Y", "color"};
        Map colMap = new HashMap<Integer,Integer>();
        outputCSV.writeNext(cols);
        Double error = 0.0;
        for(MLDataPair pair: testSet ) {
            MLData input = pair.getInput();
            final MLData output = network.compute(input);
            double [] result = output.getData();
            double [] ideal = pair.getIdeal().getData();
            for(int i=0;i<result.length;i++){
                error += Math.pow(result[i] - ideal[i], 2);
            }
//            System.out.println(Arrays.toString(input.getData())
//                    + ", actual=" + Arrays.toString(output.getData())
//                    + "\t\t,ideal=" + Arrays.toString(pair.getIdeal().getData())
//            );
//
            String[] row = {Double.toString(input.getData()[0]),
                    Double.toString(input.getData()[1]),
                    Arrays.toString(output.getData()),
                    Arrays.toString(pair.getIdeal().getData())
            };
            outputCSV.writeNext(row);
//
//            metricsOut.println(getColor(colMap, Arrays.hashCode(normalizeOutput(output.getData()))) + " " +
//                               getColor(colMap, Arrays.hashCode(normalizeOutput(pair.getIdeal().getData()))));
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./"+result_file+"/" + data_file + "_params.csv")));
        out.println("error: " + error/2);
        out.close();
        outputCSV.close();
        metricsOut.close();
    }

    private String getColor(Map<Integer, Integer> colMap, int col){
        Integer val = colMap.get(col);
        if(val == null){
            val = colMap.size();
            colMap.put(col,val);
        }
        return val.toString();
    }

    boolean[] normalizeOutput(double[] output){
        boolean[] res = new boolean[output.length];
        for(int i=0;i<output.length;i++){
            res[i] = (output[i] < 0.5);
        }
        return res;
    }

    public void run(double div, boolean outputType) throws Exception {
        normalizeFile(data_file, norm_data_file, outputType);
        divide(div);
        MLDataSet trainingSet = new CSVNeuralDataSet(MYDIR + File.separator + norm_training_file, 2,outputNodesNumber, false);
        MLDataSet testSet = new CSVNeuralDataSet(MYDIR + File.separator + norm_test_file, 2,outputNodesNumber, false);

        BasicNetwork network = createNetwork();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./"+result_file+"/" + data_file + "_params.csv", true)));
        long time = System.currentTimeMillis();
        train(network, trainingSet);
        out.println("training_time: " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        predict(network,testSet);
        out.println("predict_time: " + (System.currentTimeMillis() - time));
        out.close();
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
    public void normalizeFile(String source, String target, boolean outputType) {
        ReadCSV r = new ReadCSV(MYDIR + File.separator + source, false, ',');
        Set<String> colors = new HashSet<String>();
        while (r.next()){
            colors.add(r.get(2));
        }
        // zapisuje potrzebna liczbe wezlow wyjsciowych
        if(outputType){outputNodesNumber = colors.size();}
        else {outputNodesNumber = Integer.toBinaryString(colors.size()-1).length();}

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
        norm.addOutputField(outputType ? new OutputOneOf(inputColor, 1, 0) : new OutputBinary(inputColor, 1, 0));


        File outputFile = new File(MYDIR, target);
        norm.setCSVFormat(CSVFormat.ENGLISH);
        norm.setTarget(new NormalizationStorageCSV(CSVFormat.ENGLISH, outputFile));

        norm.setReport(new ConsoleStatusReportable());
        norm.process();
    }

    /**
     * losowo dzieli dane na zbiory: treningowy i testowy
     * @param val procentowa zawartość zbioru testowego
     * @throws IOException
     */
    public void divide(double val) throws IOException {
        val = Math.abs(val);
        if(val>1.0)val=1.0/val;

        BufferedReader br = new BufferedReader(new FileReader(MYDIR + File.separator + norm_data_file));
        File testDataOutFile = new File(MYDIR + File.separator + norm_test_file);
        File trainingDataOutFile = new File(MYDIR + File.separator + norm_training_file);
        if(testDataOutFile.exists() && trainingDataOutFile.exists()) return;
        PrintWriter testDataOut = new PrintWriter(testDataOutFile);
        PrintWriter trainingDataOut = new PrintWriter(trainingDataOutFile);
        List<String> els = new ArrayList<String>();
        String line = br.readLine();
        while(line != null){
            els.add(line);
            line = br.readLine();
        }
        Collections.shuffle(els);
        int num = (int)(((double)els.size())*val);
        for(int i=0;i<num;i++){
            testDataOut.println(els.get(i));
        }
        for(int i=num;i<els.size();i++){
            trainingDataOut.println(els.get(i));
        }
        br.close();
        testDataOut.close();
        trainingDataOut.close();
    }

    public static String stringify(int[] tab){
        StringBuilder sb = new StringBuilder();
        if(tab.length>0)sb.append(tab[0]);
        for(int i=1;i<tab.length;i++){
            sb.append("-").append(tab[i]);
        }
        return sb.toString();
    }

}


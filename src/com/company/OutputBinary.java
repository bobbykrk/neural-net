package com.company;


import java.util.ArrayList;
import java.util.List;

import org.encog.util.normalize.input.InputField;
import org.encog.util.normalize.input.InputFieldCSVText;
import org.encog.util.normalize.output.BasicOutputField;
import org.encog.util.normalize.output.nominal.NominalItem;

/**
 * An output field that uses the "on of" technique to represent input data. For
 * example, if there were five nominal items, or classes, given then each one
 * would be represented by a single output neuron that would be on or off.
 *
 * Often the OutputEquilateral class is a better choice to represent nominal
 * items.
 *
 */
public class OutputBinary extends BasicOutputField {

    /**
     * The nominal items to represent.
     */
    private final List<NominalItem> items = new ArrayList<NominalItem>();

    /**
     * What is the true value, often just "1".
     */
    private double trueValue;

    /**
     * What is the true value, often just "0" or "-1".
     */
    private double falseValue;

    /**
     * Default constructor for reflection.
     */
    public OutputBinary() {
        this(1,-1);
    }

    /**
     * Construct a one-of field and specify the true and false value.
     *
     * @param trueValue
     *            The true value.
     * @param falseValue
     *            The false value.
     */
    public OutputBinary(final double trueValue, final double falseValue) {
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    /**
     * Base the field on a csv text field.
     * @param csv The field.
     * @param trueValue
     *            The true value.
     * @param falseValue
     *            The false value.
     */
    public OutputBinary(InputFieldCSVText csv, final double trueValue, final double falseValue) {
        this.trueValue = trueValue;
        this.falseValue = falseValue;

        for(int i=0;i<Integer.toBinaryString(csv.getMappings().keySet().size()-1).length();i++) {
            addItem(csv,i);
        }
    }

    /**
     * Add a nominal value specifying a single value, the high and low values
     * will be 0.5 below and 0.5 above.
     *
     * @param inputField The input field to use.
     * @param value The value to calculate the high and low values off of.
     */
    public void addItem(final InputField inputField, final double value) {
        addItem(inputField, value - 0.5, value + 0.5);
    }

    /**
     * Add a nominal item, specify the low and high values.
     *
     * @param inputField The input field to base everything from.
     * @param low The high value for this nominal item.
     * @param high The low value for this nominal item.
     */
    public void addItem(final InputField inputField, final double low,
                        final double high) {
        final NominalItem item = new NominalItem(inputField, low, high);
        this.items.add(item);
    }

    /**
     * Calculate the value for the specified subfield.
     *
     * @param subfield
     *            The subfield to calculate for.
     * @return The calculated value for this field.
     */
    public double calculate(final int subfield) {
        final NominalItem item = this.items.get(subfield);
        InputField field = item.getInputField();
        final int currentValue = (int)field.getCurrentValue();

        return binPos(currentValue, subfield) ? this.trueValue : this.falseValue;
    }

    private static boolean binPos(int val, int pos){
        String bin = new StringBuilder(Integer.toBinaryString(val)).reverse().toString();
        if(pos<bin.length()) {
            if(bin.charAt(pos) == '0'){
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    /**
     * @return The false value.
     */
    public double getFalseValue() {
        return this.falseValue;
    }

    /**
     * @return The number of subfields, or nominal classes.
     */
    public int getSubfieldCount() {
        return this.items.size();
    }

    /**
     * @return The true value.
     */
    public double getTrueValue() {
        return this.trueValue;
    }

    /**
     * Not needed for this sort of output field.
     */
    public void rowInit() {
    }

    public static void main(String[] args){
        System.out.println(Integer.toBinaryString(1000));
        for(int i=0;i<15;i++){
            System.out.println(OutputBinary.binPos(1000,i));
        }

    }
}
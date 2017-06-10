package co.edu.eafit.numerico;

import java.util.ArrayList;

public class InterpolationTable {

  public static ArrayList<Double> valoresXn;
  public static ArrayList<Double> valoresFxn;
  public static ArrayList<String> xNValuesStrings;
  public static ArrayList<String> fXnValuesStrings;
  static double[] xn;
  static double[] fXn;

  public InterpolationTable(ArrayList<Double> xNValues, ArrayList<Double> fXnValues) {
    valoresXn = new ArrayList<>();
    valoresFxn = new ArrayList<>();
    valoresXn = xNValues;
    valoresFxn = fXnValues;
    createArrays();
  }

  private void createArrays() {
    xn = new double[valoresXn.size()];
    for (int i = 0; i < xn.length; i++) {
      xn[i] = valoresXn.get(i).doubleValue();
    }
    fXn = new double[valoresXn.size()];
    for (int i = 0; i < fXn.length; i++) {
      fXn[i] = valoresFxn.get(i).doubleValue();
    }
  }

}

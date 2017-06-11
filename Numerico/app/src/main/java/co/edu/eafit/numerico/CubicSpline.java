package co.edu.eafit.numerico;

import java.util.ArrayList;

public class CubicSpline{
    // Valor para evaluar el polinomio respuesta.
    FunctionsEvaluator fEvaluator;
    private double x;
    private final ArrayList<ArrayList<Double>> resultMatrix = new ArrayList<>();
    private ArrayList<Double> copiedXnValues = new ArrayList<>();
    private double [][] interpolationMatrix;
    private int [] marks;
    int eqsDone = 0;
    private final ArrayList<Double> coefficients = new ArrayList<>();
    private final ArrayList<String> individualPolynomials = new ArrayList<>();
    private String polynomial;
    private final ArrayList<String> equations = new ArrayList<>();
    private boolean isAppOnEvalView = false;
    private final boolean isAppOnEnterXView = false;

    public CubicSpline(ArrayList<Double> xnvalues, ArrayList<Double> fxnvalues){
        this.setPoints(xnvalues, fxnvalues);
    }
    public void setPoints(ArrayList<Double> xnvalues, ArrayList<Double> fxnvalues){
        resultMatrix.clear();
        copiedXnValues.clear();
        coefficients.clear();
        individualPolynomials.clear();
        equations.clear();
        copiedXnValues = (ArrayList<Double>)xnvalues.clone();
        int sXEqs = (copiedXnValues.size() - 1) * 2;
        int dSXEqs = copiedXnValues.size() - 2;
        int d2SXEqs = copiedXnValues.size() - 2;
        int supEqs = 2;
        int totalEqs = 4 * (copiedXnValues.size() - 1);
        for(int i = 0; i < totalEqs; i++){
            resultMatrix.add(new ArrayList<Double>());  //Se pone un nuevo ArrayList en cada posición.
        }

        //Ecuaciones con s(x).
        buildSXEqs(sXEqs, totalEqs, fxnvalues);
        //Ecuaciones con s'(x).
        buildDSXEqs(dSXEqs, totalEqs);
        //Ecuaciones con s''(x).
        buildD2SXEqs(d2SXEqs, totalEqs);
        //Ecuaciones con s''(x) + suposición.
        buildSupEqs(supEqs, totalEqs);

        convertResultToMatrix();
        marks = Matrix.fillMarks(interpolationMatrix.length);
        solveWithTotalPivoting();
        if(coefficients.size() > 0){
            buildPiecewisePolynomial(totalEqs);
        }else{  //Hubo algún error resolviendo el sistema de ecuaciones.
            polynomial = "Polinimio No Determinado";
        }
        //System.out.println(polynomial);
    }

    public void buildSXEqs(int sXEqs, int totalEqs, ArrayList<Double> FXnValues){
        int xPos = 0;
        for(int i = 0; i < sXEqs; i++){
            int zeroCont = (i / 2) * 4; //Se hace para saber cuántos ceros agregar al inicio.

            String equation = "";
            int subIndex = i / 2;

            for(int j = 0; j < zeroCont; j++){  //j es sólo para iterar.
                resultMatrix.get(i).add(0.0);
            }

            resultMatrix.get(i).add(Math.pow(copiedXnValues.get(xPos), 3));
            equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "a" + subIndex + " + ";
            resultMatrix.get(i).add(Math.pow(copiedXnValues.get(xPos), 2));
            equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "b" + subIndex + " + ";
            resultMatrix.get(i).add(copiedXnValues.get(xPos));
            equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "c" + subIndex + " + ";
            resultMatrix.get(i).add(1.0);
            equation += "d" + subIndex + " = ";

            for(int k = (zeroCont + 4); k < totalEqs; k++){  //k es sólo para iterar.
                resultMatrix.get(i).add(0.0);
            }

            resultMatrix.get(i).add(FXnValues.get(xPos));
            equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1);

            equations.add(equation);

            if((i % 2) == 0){
                xPos++;
            }
        }
        eqsDone += sXEqs;
    }

    public void buildDSXEqs(int dSXEqs, int totalEqs){
        int xPos = 1;
        for(int i = eqsDone; i < (eqsDone + dSXEqs); i++){
            int zeroCount = ((xPos - 1) * 4);  //Se hace para saber cuántos ceros agregar al inicio.

            String equation = "";
            int subIndex = xPos - 1;

            for(int j = 0; j < zeroCount; j++){  //j es sólo para iterar.
                resultMatrix.get(i).add(0.0);
            }

            resultMatrix.get(i).add(3.0 * Math.pow(copiedXnValues.get(xPos), 2));
            equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "a" + subIndex + " + ";
            resultMatrix.get(i).add(2.0 * copiedXnValues.get(xPos));
            equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "b" + subIndex + " + ";
            resultMatrix.get(i).add(1.0);
            equation += "c" + subIndex + " = ";
            resultMatrix.get(i).add(0.0);
            resultMatrix.get(i).add(-3.0 * Math.pow(copiedXnValues.get(xPos), 2));
            equation += ((-1) * resultMatrix.get(i).get(resultMatrix.get(i).size() - 1)) + "a"
                    + (subIndex + 1) + " + ";
            resultMatrix.get(i).add(-2.0 * copiedXnValues.get(xPos));
            equation += ((-1) * resultMatrix.get(i).get(resultMatrix.get(i).size() - 1)) + "b"
                    + (subIndex + 1) + " + ";
            resultMatrix.get(i).add(-1.0);
            equation += "c" + (subIndex + 1);
            resultMatrix.get(i).add(0.0);

            equations.add(equation);

            for(int k = (zeroCount + 8); k < (totalEqs + 1); k++){  //Para añadir también el valor
                //del término independiente.
                resultMatrix.get(i).add(0.0);
            }

            xPos++;
        }
        eqsDone += dSXEqs;
    }

    public void buildD2SXEqs(int d2SXEqs, int totalEqs){
        int xPos = 1;
        for(int i = eqsDone; i < (eqsDone + d2SXEqs); i++){
            int zeroCount = ((xPos - 1) * 4);  //Se hace para saber cuántos ceros agregar al inicio.

            String equation = "";
            int subIndex = xPos - 1;

            for(int j = 0; j < zeroCount; j++){  //j es sólo para iterar.
                resultMatrix.get(i).add(0.0);
            }

            resultMatrix.get(i).add(6.0 * copiedXnValues.get(xPos));
            equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "a" + subIndex + " + ";
            resultMatrix.get(i).add(2.0);
            equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "b" + subIndex + " = ";
            resultMatrix.get(i).add(0.0);
            resultMatrix.get(i).add(0.0);
            resultMatrix.get(i).add(-6.0 * copiedXnValues.get(xPos));
            equation += ((-1)* resultMatrix.get(i).get(resultMatrix.get(i).size() - 1)) + "a"
                    + (subIndex + 1) + " + ";
            resultMatrix.get(i).add(-2.0);
            equation += ((-1)* resultMatrix.get(i).get(resultMatrix.get(i).size() - 1)) + "b"
                    + (subIndex + 1);
            resultMatrix.get(i).add(0.0);
            resultMatrix.get(i).add(0.0);

            equations.add(equation);

            for(int k = (zeroCount + 8); k < (totalEqs + 1); k++){  //Para añadir también el valor
                //del término independiente.
                resultMatrix.get(i).add(0.0);
            }

            xPos++;
        }
        eqsDone += d2SXEqs;
    }

    public void buildSupEqs(int supEqs, int totalEqs){
        int xPos = 0;
        for(int i = eqsDone; i < (eqsDone + supEqs); i++){

            String equation = "";
            int subIndex = xPos;

            if(xPos == 0){
                resultMatrix.get(i).add(6.0 * copiedXnValues.get(xPos));
                equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "a" + subIndex
                        + " + ";
                resultMatrix.get(i).add(2.0);
                equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "b" + subIndex
                        + " = ";
                resultMatrix.get(i).add(0.0);
                resultMatrix.get(i).add(0.0);
                for(int j = 4; j < (totalEqs + 1); j++){  //Para añadir también el valor del término
                    //independiente.
                    resultMatrix.get(i).add(0.0);
                }

                equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1);

            }else{
                subIndex -= 1;  //Se hace para que el subíndice de los coeficientes sea coherente.

                for(int k = 0; k < (totalEqs - 4); k++){  //Para añadir ceros antes de los últimos
                    //cuatro términos.
                    resultMatrix.get(i).add(0.0);
                }
                resultMatrix.get(i).add(6.0 * copiedXnValues.get(xPos));
                equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "a" + subIndex
                        + " + ";
                resultMatrix.get(i).add(2.0);
                equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1) + "b" + subIndex
                        + " = ";
                resultMatrix.get(i).add(0.0);
                resultMatrix.get(i).add(0.0);
                resultMatrix.get(i).add(0.0);  //Esto se hace para añadir el término independiente.

                equation += resultMatrix.get(i).get(resultMatrix.get(i).size() - 1);
            }

            xPos = copiedXnValues.size() - 1;  //Hace referencia al último punto.

            equations.add(equation);
        }
        eqsDone += supEqs;
    }

    public void convertResultToMatrix(){
        interpolationMatrix = new double [resultMatrix.size()][(resultMatrix.size() + 1)];
        for(int i = 0; i < resultMatrix.size(); i++){
            for(int j = 0; j < (resultMatrix.size() + 1); j++){
                interpolationMatrix[i][j] = resultMatrix.get(i).get(j);
            }
        }
    }

    public void solveWithTotalPivoting(){
        double [][] result = cloneMatrix(interpolationMatrix);
        for (int k = 0; k <= (result[0].length - 2); k++){
            result = calculateTotalPivoting(result, result.length, k);
            if(result == null) return;

            for (int i = k+1; i < result.length; i++){
                if(result[k][k] != 0){
                    double mult = result[i][k] / result[k][k];

                    for (int j = k; j < (result.length + 1); j++) {
                        result[i][j] = result[i][j] - (mult*result[k][j]);
                    }
                }else{
                    // El sistema tiene soluciones infinitas.
                    return;
                }
            }
        }
        if(result[result.length-1][result.length-1] == 0){
        }else{  //Se le puede aplicar sustitución regresiva a la matriz para encontrar x.
            double[] coeffArray = new double[result.length];
            coeffArray = Matrix.regressiveSubstitutionElimination(result, result.length);
            orderCoefficients(coeffArray);
        }
    }

    public double[][] calculateTotalPivoting(double[][]Ab, int n, int k){
        double max = 0;
        int maxRow = k;
        int maxColumn = k;
        for (int r = k; r < n; r++){
            for (int s = k; s < n; s++){
                if (Math.abs(Ab[r][s]) > max){
                    max = Math.abs(Ab[r][s]);
                    maxRow = r;
                    maxColumn = s;
                }
            }
        }
        if(max == 0) {
            // El sistema tiene infinitas soluciones.
            return null;
        }else{
            if(maxRow != k){
                Ab = Matrix.changeRow(Ab, k, maxRow);
            }
            if(maxColumn != k) {
                Ab = Matrix.changeCol(Ab, k, maxColumn);
                marks = Matrix.changeMarks(marks, maxColumn, k);
            }
            return Ab;
        }
    }

    public void orderCoefficients(double[] x){
        boolean allSolutionsFound = false;
        for(int i = 0 , j = 0; i < marks.length && !allSolutionsFound; i++){
            if(marks[i] == j+1){
                coefficients.add(j, x[i]);
                if(j < marks.length-1){
                    i = -1;
                    j++;
                }
                else{
                    allSolutionsFound = true;
                }
            }
        }
    }

    public void buildPiecewisePolynomial(int totalEqs){
        polynomial = "";
        for(int i = 0; i < (copiedXnValues.size() - 1); i++){
            int pos = (i * 4);
            String individualPolynomial = "";
            individualPolynomial += String.valueOf(coefficients.get(pos)) + "*(x^3) + "
                    +  String.valueOf(coefficients.get(pos+1)) + "*(x^2) + "
                    +  String.valueOf(coefficients.get(pos+2)) + "*(x) + "
                    +  String.valueOf(coefficients.get(pos+3));
            individualPolynomials.add(individualPolynomial);  //Se agrega sólo la función.

            //Luego se le adicionan los límites.
            String interval = "";
            interval = "    for x in (" + String.valueOf(copiedXnValues.get(i))
                    +  "," + String.valueOf(copiedXnValues.get(i+1)) + ")";
            individualPolynomial += interval;

            polynomial += individualPolynomial + "\n";
        }
    }

    public String evaluate(Double xvalue){
        String resultText = "";
        x = xvalue;
        if(x >= copiedXnValues.get(0) && x <= copiedXnValues.get(copiedXnValues.size()-1)){
            String methodResultString = "The result of Cubic Spline' s piecewise polynomial"
                    + " evaluated in x = " + String.valueOf(x) + " is "
                    + "p(" + String.valueOf(x) +") = ";
            resultText+=methodResultString;
            for(int i = 0; i < (copiedXnValues.size() - 1); i++){
                if(x >= copiedXnValues.get(i) && x <= copiedXnValues.get(i+1)){
                    fEvaluator = new FunctionsEvaluator(individualPolynomials.get(i), "", "", "");
                    fEvaluator.buildFunctions(individualPolynomials.get(i), 0);
                    //La función construida quedaría
                    //en la primera posición del arreglo
                    //en FunctionsEvaluator.
                }
            }
            double result = fEvaluator.f(x);
            resultText+= String.valueOf(result);
            isAppOnEvalView = true;
        }else{

        }
        return resultText;

    }

    public double [][]cloneMatrix(double [][] source) {
        double [][] copiedInterpolationMatrix = new double [source.length][source.length+1];
        for(int i = 0; i < source.length; i++){
            for(int j = 0; j < (source.length + 1); j++){
                copiedInterpolationMatrix[i][j] = interpolationMatrix[i][j];
            }
        }
        return copiedInterpolationMatrix;
    }

    @Override
    public String toString() {
        return polynomial;
    }
}
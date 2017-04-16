package co.edu.eafit.numerico;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

public class Evaluator extends Activity {

    private JEP myParser;
    private DJep derivate;

    public void preparateJep()
    {
        myParser = new JEP();
        myParser.addStandardFunctions();
        myParser.addStandardConstants();
        myParser.setAllowUndeclared(true);
        myParser.setAllowAssignment(true);
        myParser.addComplex();
        myParser.addFunction("sen", new org.nfunk.jep.function.Sine());
        myParser.addFunction("cos", new org.nfunk.jep.function.Cosine());
        myParser.addVariable("x", 0);
        myParser.setImplicitMul(true);
    }

    public void preparateDerivate()
    {
        derivate = new DJep();
        derivate.addStandardConstants();
        derivate.addStandardFunctions();
        derivate.addComplex();
        derivate.setAllowUndeclared(true);
        derivate.setAllowAssignment(true);
        derivate.setImplicitMul(true);
        derivate.addFunction("sen", new org.nfunk.jep.function.Sine());
        derivate.addStandardDiffRules();
    }

    public double evaluate(String variable, double valor, String funcion)
    {
        preparateJep();
        myParser.parseExpression(funcion);
        myParser.addVariable(variable, valor);
        return myParser.getValue();
    }

    public double evaluateEuler(String variable1, String variable2, double valor1,
                               double valor2, String funcion)
    {
        preparateJep();
        funcion = funcion.replaceAll("sen", "sin");
        myParser.parseExpression(funcion);
        myParser.addVariable(variable1, valor1);
        myParser.addVariable(variable2, valor2);
        return myParser.getValue();
    }

    public String derivate(String funcion)
    {
        try{
            preparateDerivate();
            funcion = funcion.replaceAll("sen", "sin");
            Node node = derivate.parse(funcion);
            Node diff = derivate.differentiate(node,"x");
            Node simp = derivate.simplify(diff);
            String deff = derivate.toString(simp);
            return deff;
        }catch(ParseException e){
            Message("Wrong iteration value");
            return "";
        }
    }

    public boolean checkFunc (String funcion)
    {
        preparateJep();
        myParser.parseExpression(funcion);
        myParser.addVariable("x", 1);
        myParser.addVariable("y", 1);
        boolean error = myParser.hasError();
        return error;
    }


    public static double round(double numero)
    {
        int cifras = (int) Math.pow(10,3);
        return Math.rint (numero * cifras) / cifras;
    }

    public void Message(String s){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setMessage(s);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
}


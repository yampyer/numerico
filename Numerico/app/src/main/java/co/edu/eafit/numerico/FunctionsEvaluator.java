package co.edu.eafit.numerico;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.CustomFunction;
import de.congrace.exp4j.ExpressionBuilder;

public class FunctionsEvaluator {
    // Save state of functions built by evaluator.
    // fX is the position 0, position 1 is dfX, position 2 is d2fX, position 3 is gX
    private boolean[] builtFunctionsStatus = new boolean[4];
    private String[] inputFunctions = new String[4];
    public static Calculable[] builtExpressions = new Calculable[4];
    private String variable = "x";


    //Constructor to save functions entered by user.
    public FunctionsEvaluator(String fX, String dfX, String d2fX, String gX){
        this.inputFunctions[0] = fX;
        this.inputFunctions[1] = dfX;
        this.inputFunctions[2] = d2fX;
        this.inputFunctions[3] = gX;
        checkFunctions();
    }

    public FunctionsEvaluator(){

    }

    public void checkFunctions(){

        for(int i = 0; i < 4; i++){
            if(!inputFunctions[i].isEmpty()){
                builtFunctionsStatus[i] = buildFunctions(inputFunctions[i],i);
            }else{
                builtFunctionsStatus[i] = false;
            }
        }
    }

    public boolean buildFunctions(String function, int position){
        try {
            if(function.contains("log10")){
                // function Log(x) on base 10.
                CustomFunction log10Func = new CustomFunction("log10") {
                    @Override
                    public double applyFunction(double... arg0) {
                        return Math.log10(arg0[0]);
                    }
                };
                builtExpressions[position] = new ExpressionBuilder(function)
                        .withCustomFunction(log10Func)
                        .withVariableNames(variable).build();
            }else{
                builtExpressions[position] = new ExpressionBuilder(function)
                        .withVariableNames(variable).build();
            }
            return true;
        }catch(Exception e){
            // Was not possible to build the function.
            return false;
        }
    }

    public double f(double x){
        //double y = (7.14292992) - (20.0471*x) + (19.0956*x*x) - (7.45*x*x*x) + (x*x*x*x);
        builtExpressions[0].setVariable(variable, x);
        double y = builtExpressions[0].calculate();
        return y;
    }

    public double dF(double x){
        //double y = (-20.0471) + (2*19.0956*x) - (3*7.45*x*x) + (4*x*x*x);
        builtExpressions[1].setVariable(variable, x);
        double y = builtExpressions[1].calculate();
        return y;
    }

    public double d2F(double x){
        //double y = (2*19.0956) - (3*2*7.45*x) + (4*3*x*x);
        builtExpressions[2].setVariable(variable, x);
        double y = builtExpressions[2].calculate();
        return y;
    }

    public double g(double x){
        //double y = Math.log((((x*x) + (5*x) + 3) / x));
        builtExpressions[3].setVariable(variable, x);
        double y = builtExpressions[3].calculate();
        return y;
    }

    public boolean[] getBuiltFunctionsStatus() {
        return builtFunctionsStatus;
    }

    public Calculable[] getBuiltExpressions() {
        return builtExpressions;
    }

    public String[] getInputFunctions() {
        return inputFunctions;
    }


    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

}
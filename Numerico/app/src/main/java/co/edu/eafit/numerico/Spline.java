package co.edu.eafit.numerico;

public class Spline {
    double[] x;
    double[] y;
    int      nMin;

    public Spline(final int nMin, final int n) {
        this.nMin = nMin;
        x = new double[n];
        y = new double[n];
    }

    public void addValues(double[] xlist, double[] ylist) {
        x = xlist;
        y = ylist;
    }

    /**
     * Returns the derivative of the spline at the given time.
     */
    public double derivate(final double time) {
        int index = getPreviousIndex(time);
        if (index < 0)
            index = 0;
        return derivate(time, index);
    }

    public double derivate(final double time, final int index) {
        double a = y[index];
        double b = y[index + 1];

        double duration = x[index + 1] - x[index];

        return (b - a) / duration;
    }

    /**
     * Returns the last index before the given time.
     *
     * @param time
     * @return
     */
    public int getPreviousIndex(final double time) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] > time)
                return i - 1;
        }
        return x.length - 1;
    }

    /**
     * Returns the interpolation by the splie at the given time.
     *
     * @param time
     * @return
     */
    public double interpolate(final double time) {
        if (time <= x[nMin])
            return y[nMin];
        if (time >= x[x.length - 1 - nMin])
            return y[y.length - 1 - nMin];

        int index = getPreviousIndex(time);
        if (index < 0)
            index = 0;
        return interpolate(time, index);
    }

    double interpolate(final double time, final int index) {
        if (index == y.length)
            return y[y.length - 1];

        double a = y[index];
        double b = y[index + 1];

        double duration = x[index + 1] - x[index];
        double ratio = (time - x[index]) / duration;

        return a + ratio * (b - a);
    }

    @Override
    public String toString() {
        String result = "";
        for(int i=0;i<x.length-1;i++){
            Double m = ((y[i+1]-y[i])/(x[i+1]-x[i]));
            result+="f(x)="+m+"(x)+"+(y[i]+m*(-x[i]))+" when x in ("+x[i]+","+x[i+1]+")\n";
        }
        return result;
    }
}


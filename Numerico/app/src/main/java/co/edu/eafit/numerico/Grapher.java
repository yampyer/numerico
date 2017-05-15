package co.edu.eafit.numerico;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import android.widget.ZoomButton;

import java.util.ArrayList;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

public class Grapher extends AppCompatActivity {

    private XYPlot mySimpleXYPlot;
    XYSeries drawSerie, drawGx, drawDFx, drawD2Fx;
    ArrayList<Double> arreglo = new ArrayList<Double>();
    private PointF minXY;
    private PointF maxXY;
    float izq, der, arriba, abajo;
    LineAndPointFormatter seriesFormat, seriesGx, seriesDFx, seriesD2Fx;
    String fx_stg, x0_stg, x1_stg, delta_stg, gx_stg, dfx_stg, d2fx_stg;
    double fx, x0, x1, delta, gx, dfx, d2fx;
    boolean dfx_estado, d2fx_estado, gx_estado;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grapher);

        mySimpleXYPlot = (XYPlot) findViewById(R.id.dynamicXYPlot);

        ZoomButton zoomPlus = (ZoomButton) findViewById(R.id.zoomPlus);

        ZoomButton zoomMinus = (ZoomButton) findViewById(R.id.zoomMinus);

        mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        mySimpleXYPlot.centerOnDomainOrigin(0);
        mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setStrokeWidth(5);
        mySimpleXYPlot.getGraphWidget().getDomainLabelPaint().setTextSize(PixelUtils.dpToPix(15));
        mySimpleXYPlot.centerOnRangeOrigin(0);
        mySimpleXYPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getRangeOriginLinePaint().setStrokeWidth(5);
        mySimpleXYPlot.getGraphWidget().getRangeLabelPaint().setTextSize(PixelUtils.dpToPix(15));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // verification of intent coming from
        Intent intent = this.getIntent();
        if (intent != null) {
            String strdata = intent.getExtras().getString("Uniqid");
            if (strdata.equals("Busquedas")) {
                fx_stg = IncrementalSearch.funcionbsq2;
                x0_stg = IncrementalSearch.val_iniIn;
                delta_stg = IncrementalSearch.val_iniD;

                x0 = Double.parseDouble(x0_stg);
                if (x0 == 0 || x0 == 1) {
                    x1 = 5 + x0;
                } else {
                    x1 = Math.pow(x0, 2);
                }
                delta = Double.parseDouble(delta_stg);
            }
            if (strdata.equals("DatosUnaVariable")) {

                fx_stg = OneVariableInput.fXTodos;
                dfx_stg = OneVariableInput.dfXTodos;
                d2fx_stg = OneVariableInput.d2fXTodos;
                gx_stg = OneVariableInput.gXTodos;
                delta_stg = null;
                x0 = -10;
                x1 = 10;
                gx_estado = true;
                dfx_estado = true;
                d2fx_estado = true;
            }
            if (strdata.equals("Biseccion")) {

                fx_stg = Bisection.funcionbisc2;
                x0_stg = Bisection.val_iniIn;
                x1_stg = Bisection.val_iniS;
                delta_stg = null;
                x0 = Double.parseDouble(x0_stg);
                x1 = Double.parseDouble(x1_stg);
            }
            if (strdata.equals("ReglaFalsa")) {
                fx_stg = FalsePosition.funcionRF2;
                x0_stg = FalsePosition.val_iniIn;
                x1_stg = FalsePosition.val_iniS;
                delta_stg = null;
                x0 = Double.parseDouble(x0_stg);
                x1 = Double.parseDouble(x1_stg);
            }
            if (strdata.equals("PuntoFijo")) {
                fx_stg = FixedPoint.val_funcionPF;
                gx_stg = FixedPoint.val_funcionG;
                x0_stg = FixedPoint.val_iniIn;
                x1_stg = null;
                delta_stg = null;
                x0 = Double.parseDouble(x0_stg);
                if (x0 == 0 || x0 == 1) {
                    x1 = 5 + x0;
                } else {
                    x1 = Math.pow(x0, 2);
                }
                gx_estado = true;
            }
            if (strdata.equals("Newton")) {
                fx_stg = NewtonMethod.funcionfx;
                dfx_stg = NewtonMethod.funciondfx;
                x0_stg = NewtonMethod.val_iniIn;
                x1_stg = null;
                delta_stg = null;
                x0 = Double.parseDouble(x0_stg);
                if (x0 == 0 || x0 == 1) {
                    x1 = 5 + x0;
                } else {
                    x1 = Math.pow(x0, 2);
                }
                dfx_estado = true;
            }
            if (strdata.equals("Secante")) {
                fx_stg = SecantMethod.funcionMS2;
                x0_stg = SecantMethod.val_iniIn;
                x1_stg = SecantMethod.val_iniS;
                delta_stg = null;
                x0 = Double.parseDouble(x0_stg);
                x1 = Double.parseDouble(x1_stg);
            }
            if (strdata.equals("RaicesMultiplez")) {
                fx_stg = MultipleRoots.funcionRM1;
                dfx_stg = MultipleRoots.funcionRM2;
                d2fx_stg = MultipleRoots.funcionRM3;
                x0_stg = MultipleRoots.val_iniIn;
                x1_stg = null;
                delta_stg = null;
                x0 = Double.parseDouble(x0_stg);
                if (x0 == 0 || x0 == 1) {
                    x1 = 5 + x0;
                } else {
                    x1 = Math.pow(x0, 2);
                }
                dfx_estado = true;
                d2fx_estado = true;
            }
            if (strdata.equals("AlgunasFunciones")) {
                fx_stg = OneVariableInput.fXTodos;
                dfx_stg = OneVariableInput.dfXTodos;
                d2fx_stg = OneVariableInput.d2fXTodos;
                gx_stg = OneVariableInput.gXTodos;
                delta_stg = null;
                x0 = -10;
                x1 = 10;
                if (dfx_stg != null) {
                    dfx_estado = true;
                }
                if (gx_stg != null) {
                    gx_estado = true;
                }
                if (d2fx_stg != null) {
                    d2fx_estado = true;
                }
            }
        }

        calculate();

        izq = minXY.x - 2.0f;
        der = maxXY.x + 2.0f;
        mySimpleXYPlot.setDomainBoundaries(izq - 1.0f, der + 1.0f, BoundaryMode.FIXED);

        mySimpleXYPlot.redraw();

        // Zoom
        zoomPlus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                clean();
                izq = minXY.x + 0.5f;
                der = maxXY.x - 0.5f;
                if (izq >= -0.1 || der <= 0.1) {
                    message();
                } else {
                    mySimpleXYPlot.setDomainBoundaries(izq, der, BoundaryMode.FIXED);
                    mySimpleXYPlot.redraw();
                }
            }
        });

        zoomMinus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clean();
                izq = minXY.x - 0.5f;
                der = maxXY.x + 0.5f;
                arriba = maxXY.y + 1.0f;
                abajo = minXY.y - 1.0f;
                mySimpleXYPlot.setDomainBoundaries(izq, der, BoundaryMode.FIXED);
                mySimpleXYPlot.setRangeBoundaries(abajo, arriba, BoundaryMode.FIXED);
                mySimpleXYPlot.redraw();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void message() {
        Toast.makeText(this, "Very close", Toast.LENGTH_SHORT).show();
    }

    // clean arraylist
    public void clean() {

        arreglo.clear();
        arreglo_dfx.clear();
        arreglo_d2fx.clear();
        arreglo_gx.clear();
        mySimpleXYPlot.clear();
        mySimpleXYPlot.redraw();

        calculate();
    }

    // calculate function fx depending on initial variables of method
    public void calculate() {

        Evaluator myParser = new Evaluator();

        if (delta_stg == null && x1_stg == null) {

            for (double x = x0; x < x1; x += 0.01) {

                arreglo.add(x);
                try {
                    fx = myParser.evaluate("x", x, fx_stg);
                } catch (Exception e) {}
                arreglo.add(fx);
            }
            rePaint();

        } else if (delta_stg == null && x1 != x0) {
            for (double x = x0; x < x1; x += 0.01) {

                arreglo.add(x);
                try {
                    fx = myParser.evaluate("x", x, fx_stg);
                } catch (Exception e) {}
                arreglo.add(fx);
            }
            rePaint();

        } else if (delta_stg != null) {
            for (double x = x0; x < x1; x += delta) {

                arreglo.add(x);
                try {
                    fx = myParser.evaluate("x", x, fx_stg);
                } catch (Exception e) {}
                arreglo.add(fx);
            }
            rePaint();

        }

    }

    ArrayList<Double> arreglo_gx = new ArrayList<Double>();

    // calculate function gx and graph
    public void calcGx() {
        Evaluator myParser = new Evaluator();

        for (double x = x0; x < x1; x += 0.01) {

            arreglo_gx.add(x);
            try {
                gx = myParser.evaluate("x", x, gx_stg);
            } catch (Exception e) {}
            arreglo_gx.add(gx);
        }

        drawGx = new SimpleXYSeries(arreglo_gx, SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "G(x)");

        seriesGx = new LineAndPointFormatter(Color.rgb(0, 204, 61), null, null, null);
        seriesGx.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        seriesGx.getLinePaint().setStrokeWidth(10);

        mySimpleXYPlot.addSeries(drawGx, seriesGx);
        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(), mySimpleXYPlot.getCalculatedMinY().floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(), mySimpleXYPlot.getCalculatedMaxY().floatValue());


    }

    ArrayList<Double> arreglo_dfx = new ArrayList<Double>();

    // calculate function f'x and graph
    public void calcdFx() {
        Evaluator myParser = new Evaluator();

        for (double x = x0; x < x1; x += 0.01) {

            arreglo_dfx.add(x);
            try {
                dfx = myParser.evaluate("x", x, dfx_stg);
            } catch (Exception e) {}
            arreglo_dfx.add(dfx);
        }

        drawDFx = new SimpleXYSeries(arreglo_dfx, SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "dF(x)");

        seriesDFx = new LineAndPointFormatter(Color.rgb(255, 0, 0), null, null, null);
        seriesDFx.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        seriesDFx.getLinePaint().setStrokeWidth(10);

        mySimpleXYPlot.addSeries(drawDFx, seriesDFx);
        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(), mySimpleXYPlot.getCalculatedMinY().floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(), mySimpleXYPlot.getCalculatedMaxY().floatValue());

    }

    ArrayList<Double> arreglo_d2fx = new ArrayList<Double>();

    // calculate function f''x and graph
    public void calcd2Fx() {
        Evaluator myParser = new Evaluator();

        for (double x = x0; x < x1; x += 0.01) {

            arreglo_d2fx.add(x);
            try {
                d2fx = myParser.evaluate("x", x, d2fx_stg);
            } catch (Exception e) {}
            arreglo_d2fx.add(d2fx);
        }

        drawD2Fx = new SimpleXYSeries(arreglo_d2fx, SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "d2F(x)");

        seriesD2Fx = new LineAndPointFormatter(Color.rgb(132, 0, 204), null, null, null);
        seriesD2Fx.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        seriesD2Fx.getLinePaint().setStrokeWidth(10);

        mySimpleXYPlot.addSeries(drawD2Fx, seriesD2Fx);
        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(), mySimpleXYPlot.getCalculatedMinY().floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(), mySimpleXYPlot.getCalculatedMaxY().floatValue());

    }

    // calculate function fx and graph
    public void rePaint() {

        drawSerie = new SimpleXYSeries(arreglo, SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "F(x)");

        seriesFormat = new LineAndPointFormatter(Color.rgb(0, 0, 255), null, null, null);
        seriesFormat.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        seriesFormat.getLinePaint().setStrokeWidth(10);

        mySimpleXYPlot.addSeries(drawSerie, seriesFormat);

        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(), mySimpleXYPlot.getCalculatedMinY().floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(), mySimpleXYPlot.getCalculatedMaxY().floatValue());

        if (gx_estado) {
            calcGx();
        }
        if (dfx_estado) {
            calcdFx();
        }
        if (d2fx_estado) {
            calcd2Fx();
        }

    }

}
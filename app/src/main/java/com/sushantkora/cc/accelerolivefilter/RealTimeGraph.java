package com.sushantkora.cc.accelerolivefilter;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RealTimeGraph extends AppCompatActivity {

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series_x,series_y,series_z,series_normal;
    private double minX=0-100*0.01;
    private double lastX = 0;
    GraphView graph_x,graph_y,graph_z, graph_normal;
    Viewport viewport;
    float x=0,y=0;
    HandlerClass handlerClass=new HandlerClass();
    Random random=new Random(1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_graph);
        // we get graph_normal view instance
        graph_x=(GraphView)findViewById(R.id.graph_x);
        graph_x.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph_x.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        // data
        series_x = new LineGraphSeries<DataPoint>();
        series_x.setTitle("X");
        series_x.setColor(Color.RED);

        series_y = new LineGraphSeries<DataPoint>();
        series_y.setTitle("Y");
        series_y.setColor(Color.GREEN);

        graph_x.getLegendRenderer().setVisible(true);
        graph_x.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph_y.getLegendRenderer().setVisible(true);
        graph_y.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        List<Double> data=new ArrayList<>(200);
        for(int i=0;i<200;i++){
            data.add(Math.sin(2*3.14*0.005*i)*20+random.nextDouble()*2);
            series_x.appendData(new DataPoint(i,data.get(i)), false, 200);
        }

        graph_x.addSeries(series_x);

        graph_x.getLegendRenderer().setVisible(true);
        graph_x.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        SGolay sGolay=new SGolay(4,15);
        double[]filteredData=new double[200];
        for (int i=15;i<data.size();i++){
            filteredData[i-15]=sGolay.GetFiltredData(GetFrameData(i,15,data));
        }
        for(int i=0;i<15;i++){
            series_y.appendData(new DataPoint(i,data.get(i)),false,200);
        }
        for (int j=0;j<filteredData.length;j++){
            series_y.appendData(new DataPoint(j+15,filteredData[j]),false,200);
        }
        graph_x.addSeries(series_y);
    }
    public RealMatrix GetFrameData(int index, int frameSize, List<Double> data){
        double[] frameData=new double[2*frameSize+1];
        for (int i=-frameSize;i<=frameSize && index+i<data.size();i++){
            frameData[i+frameSize]=data.get(index+i);
        }
        RealMatrix frameDataMatrix=new Array2DRowRealMatrix(frameData);
        return  frameDataMatrix.transpose();
    }
    class HandlerClass extends Handler {
        HandlerClass() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    addEntry();
                    return;

                default:
                    return;
            }
        }
    }


    private void addEntry() {
        series_x.appendData(new DataPoint(x, y), false, 100);
        x+=0.01;
        y+=0.01;
    }

}
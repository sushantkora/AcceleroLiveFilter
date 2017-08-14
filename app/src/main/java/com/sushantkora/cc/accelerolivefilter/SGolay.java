package com.sushantkora.cc.accelerolivefilter;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by cc on 07-08-2017.
 */

public class SGolay {
    int polynomialOrder, frameSize;
    double [][] Atranspose,A;
    RealMatrix H;
    public SGolay(int polynomialOrder,int frameSize){
        this.polynomialOrder=polynomialOrder;
        this.frameSize=frameSize;

        double [][] a_transpose=GetATranspose();
        double[][]a=GetA(a_transpose);

        RealMatrix A_trans = new Array2DRowRealMatrix(a_transpose);
        RealMatrix A=new Array2DRowRealMatrix(a);

        RealMatrix Atrans_A=A_trans.multiply(A);
        RealMatrix Atrans_A_Inverse=new LUDecomposition(Atrans_A).getSolver().getInverse();
        RealMatrix Atrans_A_Inverse_Atrans=Atrans_A_Inverse.multiply(A_trans);
        this.H=Atrans_A_Inverse_Atrans;
    }
    public double GetFiltredData(RealMatrix frameData)
    {
        RealMatrix A_cap=H.multiply(frameData.transpose());
       return  ((A_cap.transpose())).getData()[0][0];
    }

    public double[][]GetATranspose(){
        double[][] a_trans=new double[polynomialOrder+1][2*frameSize+1];
        for (int i=0;i<=polynomialOrder;i++){
            for (int j=-frameSize;j<=frameSize;j++){
                if(i==0 && j==0){
                    a_trans[i][j]=1;
                }
                else {
                    a_trans[i][j+frameSize]=Math.pow(j,i);
                }
            }
        }
        return a_trans;
    }
    public double[][]GetA( double[][] a_trans){
        double [][]a=new double[2*frameSize+1][polynomialOrder+1];
        for (int i=0;i<=polynomialOrder;i++){
            for (int j=-frameSize;j<=frameSize;j++){
                a[j+frameSize][i]=a_trans[i][j+frameSize];
            }
        }
        return a;
    }

}

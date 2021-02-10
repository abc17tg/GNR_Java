package GNRjava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class GNR
{
    private static Vector<Integer> MinData = new Vector<Integer>();
    private static Vector<Double> CallData = new Vector<Double>();
    private static Vector<Double> CallData_TCBH = new Vector<Double>();
    private static Vector<Integer> TimeData = new Vector<Integer>();
    private static String method = new String();
    private static Vector<Integer> MinDataGNR = new Vector<Integer>();
    private static Vector<Double> CallDataGNR = new Vector<Double>();

    public GNR() {}

    public Vector<Integer> Get_MinData() { return MinData; }
    public Vector<Double> Get_CallData() { return CallData; }
    public Vector<Double> Get_CallData_TCBH() { return CallData_TCBH; }
    public Vector<Integer> Get_TimeData() { return TimeData; }
    public String Get_method() { return method; }
    public Vector<Integer> Get_MinDataGNR()
    {
        return MinDataGNR;
    }
    public Vector<Double> Get_CallDataGNR()
    {
        return CallDataGNR;
    }

    public void Set_MinData(Vector<Integer> x) { MinData=x; }
    public void Set_CallData(Vector<Double> x) { CallData=x; }
    public void Set_CallData_TCBH(Vector<Double> x) { CallData_TCBH=x; }
    public void Set_TimeData(Vector<Integer> x) { TimeData=x; }
    public void Set_method(String x) { method=x; }
    public void Set_MinDataGNR(Vector<Integer> x) { MinDataGNR=x; }
    public void Set_CallDataGNR(Vector<Double> x)
    {
        CallDataGNR=x;
    }

    public double Av(Vector<Double> x)
    {
        double sum = 0;
        for(int i=0; i<x.size(); i++)
        {
            sum += x.get(i);
        }
        return sum/x.size();
    }


    public double AvI(Vector<Integer> x)
    {
        int sum = 0;
        for(int i=0; i<x.size(); i++)
        {
            sum += x.get(i);
        }
        return sum/x.size();
    }


    public double MaxD(Vector<Double> x)
    {
        return Collections.max(x);
    }

    public String MaxI(Vector<Integer> x)
    {
        int y = Collections.max(x);
        if(Math.floor(y/60)==0)
            return y + " [s]";
        else
            return String.format("%02d",(int)(Math.floor(y / 3600))) + ":" + String.format("%02d",(int)(Math.floor((y % 3600 - y % 60)/60))) + ":" + String.format("%02d",(y % 60));
    }

    public static double Sum(Vector<Double> x)
    {
        double sum = 0;
        for(int i=0; i<x.size(); i++)
        {
            sum += x.get(i);
        }
        return sum;
    }

    public double MinD(Vector<Double> x)
    {
        return Collections.min(x);
    }

    public String MinI(Vector<Integer> x)
    {
        int y = Collections.min(x);
        if(Math.floor(y/60)==0)
            return y + " [s]";
        else
            return String.format("%02d",(int)(Math.floor(y / 3600))) + ":" + String.format("%02d",(int)(Math.floor((y % 3600 - y % 60)/60))) + ":" + String.format("%02d",(y % 60));

    }

    public Vector<Double> readFileWritetoVector(String addr) throws IOException
    {
        FileReader fr = new FileReader(addr);
        BufferedReader inputFile = new BufferedReader(fr);
        Vector<Double> x = new Vector<>();
        String s="";
        if (inputFile != null)
        {
            while (inputFile.ready())
            {
                s=inputFile.readLine();
                if(s != null)
                {
                    x.add(Double.parseDouble(s.trim()));
                }
            }
        }
        inputFile.close();
        return x;
    }

    public Vector<Integer> readFileWritetoVectorI(String addr) throws IOException
    {
        FileReader fr = new FileReader(addr);
        BufferedReader inputFile = new BufferedReader(fr);
        Vector<Integer> x = new Vector<>();
        String s="";
        if (inputFile != null)
        {
            while (inputFile.ready())
            {
                s=inputFile.readLine();
                if(s != null)
                {
                    x.add(Integer.parseInt(s.trim()));
                }
            }
        }
        inputFile.close();
        return x;
    }

    public String sTohour(int s)
    {
        if (Math.floor(s / 60) == 0)
            return s + " [s]";
        else
            return String.format("%02d",(int)(Math.floor(s / 3600))) + ":" + String.format("%02d",(int)(Math.floor((s % 3600 - s % 60) / 60))) + ":" + String.format("%02d",(s % 60));
    }

    public String minTohour(int m)
    {
        return String.format("%02d:%02d",(int)(Math.floor(m / 60)),(m % 60));
    }

    public static int GNR_H(Vector<Double> y)
    {
        int i = 0, j = 60, count=0;
        Vector<Double> tempY0=new Vector<>(y.subList(i,j));

        for (i=0, j=60;j <= y.size(); i++, j++)
        {
            Vector<Double> tempY1= new Vector<>(y.subList(i,j));
            if (Sum(tempY1) > Sum(tempY0))
            {
                tempY0 = tempY1;
                count=0;
            } else count++;
        }
        return y.size()-count-60;
    }
}

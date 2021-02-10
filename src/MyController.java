import GNRjava.GNR;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MyController extends GNR
{
    @FXML
    public Pane infoPanel;
    @FXML
    public ComboBox choiceMethodBox;
    @FXML
    public LineChart<String,Number> plotGNRchart;
    @FXML
    public TextArea methodTextBox;
    @FXML
    public TextArea methodGNRbox;
    @FXML
    public AnchorPane mainPanel;
    @FXML
    public Label methodLabel;
    @FXML
    public Polygon playButton1;
    @FXML
    public TextArea infoText;
    @FXML
    public Text playText;
    @FXML
    public AnchorPane methodPanel;
    @FXML
    public TextArea calcBox;
    @FXML
    public Button CalButton;
    @FXML
    public Button PlotButton;
    @FXML
    public ImageView backButton;
    @FXML
    public Label backLabel;



    public MyController() { }


    @FXML
    public void PlayButton(MouseEvent mouseEvent) throws IOException
    {
        if (!choiceMethodBox.getSelectionModel().isEmpty())
        {
            Set_method(choiceMethodBox.getSelectionModel().getSelectedItem().toString());

            Parent root1 = FXMLLoader.load(getClass().getClassLoader().getResource("GNRjava/methodWIN.fxml"));
            Scene scene = new Scene(root1,720, 480);
            Stage stage =(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            //Stage stage = new Stage();
            stage.setTitle(Get_method());
            stage.setScene(scene);
            stage.show();

        }
    }

    @FXML
    public void showInfo(MouseEvent mouseEvent)
    {
        if (!infoPanel.isVisible()) infoPanel.setVisible(true);
        if (infoText.getProperties().isEmpty())
        {
            infoText.setWrapText(true);
            infoText.setText("Plik time.txt powinien być formatu .txt i zawierać wyrażone w sekundach, czasy trwania połączeń zarejestrowanych w ciągu jednej doby\n\nPlik min.txt powinien być formatu.txt i zawierać kolumnę z numerem minuty w ciągu doby powiązaną z licznością wywołań\n\nPlik calls.txt powinien być formatu.txt i zawierać kolumnę z licznością wywołań jakie zarejestrowano w danej minucie doby kolumna musi mieć tyle samo pozycji co kolumna pliku min.txt\n\nPliki calls.txt i min.txt powinny znajdować się w folderze programu w katalogu o nazwie metody. \n\nDodatkowo w katalogu FDMH jest katalog Days w którym są pomiary z kilku dni o nazwach callsX.txt (X numer kolejnego dnia np. 1, 2, ...)");
        }
    }

    @FXML
    public void hideInfo(MouseEvent mouseEvent)
    {
        if (infoPanel.isVisible()) infoPanel.setVisible(false);
    }

    @FXML
    public void choiceMethodBoxOK(MouseEvent mouseEvent)
    {
        if (choiceMethodBox.getItems().isEmpty())
        {
            choiceMethodBox.getItems().add("TCBH");
            choiceMethodBox.getItems().add("FDMH");
            choiceMethodBox.setValue("TCBH");
        }
    }

    @FXML
    public void calButtonOK(MouseEvent mouseEvent)
    {
        try
        {
            Set_MinData(readFileWritetoVectorI(System.getProperty("user.dir")+"\\src\\Files\\TCBH\\min.txt"));
            Set_CallData_TCBH(readFileWritetoVector(System.getProperty("user.dir")+"\\src\\Files\\TCBH\\calls.txt"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (Get_method().equals("TCBH"))
        {
            try
            {
                Set_TimeData(readFileWritetoVectorI(System.getProperty("user.dir")+"\\src\\Files\\TCBH\\time.txt"));
                Set_CallData(Get_CallData_TCBH());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }else if (Get_method().equals("FDMH"))
        {
            try
            {   Vector<Vector<Double>> tempFR= new Vector<>();
                Vector<Double> tempCalls = new Vector<>();
                Vector<Double> temptempCalls = new Vector<>();
                int tempDL = new File(System.getProperty("user.dir") + "\\src\\Files\\FDMH\\Days\\").list().length;
                for (int i = 1; i<=tempDL; i++)
                    tempFR.add(new Vector<>(readFileWritetoVector(System.getProperty("user.dir")+"\\src\\Files\\FDMH\\Days\\calls"+i+".txt")));

                for (int j=0;j<tempFR.get(0).size();j++)
                {
                    double suma=0;
                    for (int i=0;i<tempFR.size();i++)
                    {
                      //  temptempCalls.add(tempFR.get(i).get(j));
                    suma+=tempFR.get(i).get(j);
                    }
                    //tempCalls.add(Sum(temptempCalls)/tempFR.size());
                    tempCalls.add(suma/tempFR.size());
                    temptempCalls.clear();
                }

                Set_CallData(tempCalls);
                Set_TimeData(readFileWritetoVectorI(System.getProperty("user.dir")+"\\src\\Files\\FDMH\\time.txt"));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        String vec="";
        int ind = GNR_H(Get_CallData_TCBH());
        Set_CallDataGNR(new Vector<>(Get_CallData().subList(ind,ind+60)));
        Set_MinDataGNR(new Vector<>(Get_MinData().subList(ind,ind+60)));

        for (int i=0; i < Get_CallDataGNR().size(); i++)
        {
            vec += " " + minTohour(Get_MinDataGNR().get(i)) + "     " + String.format("%.8f", Get_CallDataGNR().get(i)) + "    " + String.format("%.6f", Get_CallDataGNR().get(i) * AvI(Get_TimeData())) + "\n";
        }

        String ob;
        ob="Obliczenia dla GNR:\n\n";
        ob+="Średnia ilość wywołań:\n";
        ob+=String.format("%.8f",Av(Get_CallDataGNR()));
        ob+="\nMax ilość wywołań:\n";
        ob+=String.format("%.8f",MaxD(Get_CallDataGNR()));
        ob+="\nMin ilość wywołań:\n";
        ob+=String.format("%.8f",MinD(Get_CallDataGNR()));
        ob+="\n\nŚredni czas rozmowy:\n";
        ob+=sTohour((int)(AvI(Get_TimeData())));
        ob+="\nMax czas rozmowy:\n";
        ob+=MaxI(Get_TimeData());
        ob+="\nMin czas rozmowy:\n";
        ob+=MinI(Get_TimeData());
        ob+="\n\nŚrednie natężenie ruchu:\n";
        ob+=String.format("%.8f",AvI(Get_TimeData())*Av(Get_CallDataGNR()));
        ob+="\nMax natężenie ruchu:\n";
        ob+=String.format("%.8f",AvI(Get_TimeData())*MaxD(Get_CallDataGNR()));
        ob+="\nMin natężenie ruchu:\n";
        ob+=String.format("%.8f",AvI(Get_TimeData())*MinD(Get_CallDataGNR()));

        calcBox.setText(ob);

        methodGNRbox.setText("godzina   wywołania    natężenie\n" + vec);

    }

    @FXML
    public void plotButtonOK(MouseEvent mouseEvent) throws IOException
    {
        if (!Get_CallData().isEmpty() && !Get_MinData().isEmpty() && !Get_CallDataGNR().isEmpty() && !Get_MinDataGNR().isEmpty())
        {
            Parent root1 = FXMLLoader.load(getClass().getClassLoader().getResource("GNRjava/plotGNR.fxml"));
            Scene scene = new Scene(root1,1280,720);
            //Stage stage =(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            stage.setTitle(Get_method() + " plot");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void methodInit(MouseEvent mouseEvent)
    {
        if (methodTextBox.getProperties().isEmpty())
        {
            if (Get_method().equals("TCBH"))
            {
                methodTextBox.setText("TCBH, czyli Time-Consistent Busy Hour - Spójna czasowo godzina zajętości. 1-godzinny okres rozpoczynający się o tej samej godzinie każdego dnia, dla którego średnia ruchu jest największa. Zalecana standardowa Get_method() obliczania średniej dziennej wymaga ciągłego mierzenia wszystkich kwadransów dla wszystkich odnośnych dni i wybierania najbardziej ruchliwej godziny po uśrednieniu tych kwadransów dla wszystkich dni. Ta Get_method() jest najbardziej wartościowa w sytuacjach stabilnych profili ruchu. Codzienne ciągłe pomiary dostarczają danych niezbędnych do potwierdzenia stabilności profilu. Jest to najbardziej dokładna Get_method() ze wszystkich, ale wymaga ciągłego mierzenia przez całą dobę.");
            } else if (Get_method().equals("FDMH"))
            {
                methodTextBox.setText("FDMH, czyli Fixed Daily Measurement Hour - Jest to stała godzina pomiaru dziennego. Oszacowanie prawdopodobnej godziny zajętości używając metody TCBH i wykonanie pomiarów o tej porze każdego dnia oraz uśrednienie tych dni, żeby uzyskać średnie natężenie ruchu podczas tej godziny. Get_method() ta jest szacowana na 90% dokładności TCBH, jednocześnie będąc najprostszą metodą pomiarową, redukując też tym zasoby potrzebne do wykonania pomiarów.");
            }
        }

    }

    @FXML
    public void plotFill(MouseEvent mouseEvent)
    {
        if (plotGNRchart.getData().isEmpty())
        {
            Vector<Double> N1=new Vector<>();
            double T = AvI(Get_TimeData());
            for(int i = 0; i< Get_CallData().size(); i++)
                N1.add(Get_CallData().get(i)*T);

            Vector<Double> N2=new Vector<>();
            for(int i = 0; i< Get_CallDataGNR().size(); i++)
                N2.add(Get_CallDataGNR().get(i)*T);
            plotGNRchart.getXAxis().setTickMarkVisible(false);
            //plotGNRchart.getXAxis().setTickLabelRotation(45);

            XYChart.Series<String,Number> series1 = new XYChart.Series();
            XYChart.Series<String,Number> series2 = new XYChart.Series();
            XYChart.Series<String,Number> Mv1 = new XYChart.Series();
            XYChart.Series<String,Number> Mv2 = new XYChart.Series();

            for(int i = 0; i< Get_MinData().size(); i++)
                series1.getData().add(new XYChart.Data(minTohour(Get_MinData().get(i)), N1.get(i)));
            for(int i = 0; i< Get_MinDataGNR().size(); i++)
                series2.getData().add(new XYChart.Data(minTohour(Get_MinDataGNR().get(i)), N2.get(i)));
            Mv1.getData().add(new XYChart.Data(minTohour(Get_MinDataGNR().get(0)), 0));
            Mv1.getData().add(new XYChart.Data(minTohour(Get_MinDataGNR().get(0)), N2.get(0)));
            Mv2.getData().add(new XYChart.Data(minTohour(Get_MinDataGNR().get(59)), 0));
            Mv2.getData().add(new XYChart.Data(minTohour(Get_MinDataGNR().get(59)), N2.get(59)));

            plotGNRchart.getData().addAll(series1, series2, Mv1, Mv2);


        }

    }

    @FXML
    public void backButtonOK(MouseEvent mouseEvent) throws IOException
    {
        Parent root1 = FXMLLoader.load(getClass().getClassLoader().getResource("GNRjava/mainWindowGNR.fxml"));
        Scene scene = new Scene(root1,720, 480);
        Stage stage =(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setTitle("GNR");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void show_backLabel(MouseEvent mouseEvent)
    {
        if (!backLabel.isVisible()) backLabel.setVisible(true);
    }

    @FXML
    public void hide_backLabel(MouseEvent mouseEvent)
    {
        if (backLabel.isVisible()) backLabel.setVisible(false);
    }
}

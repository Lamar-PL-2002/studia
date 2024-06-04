import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.jdbc.JDBCCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Wykresy2 extends JFrame {

    String userDB = "root";
    String passDB = "";
    String polaczenieURL = "jdbc:mysql://localhost:3306/pv";
    Statement stm;
    Connection connection;
    ResultSet PrzeslaniePolecenia1;
    JFreeChart chart;

    Wykresy2(){
        super("Wykres wszystkich danych:");
        setSize(1000,1000);
        Image imageIconFrame = new ImageIcon(getClass().getClassLoader().getResource("Solary.png")).getImage();
        setIconImage(imageIconFrame);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Polecenie1();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Wykresy2();
    }

    public void Polecenie1(){
        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement();

            JDBCCategoryDataset dataset = new JDBCCategoryDataset("jdbc:mysql://localhost:3306/pv", "com.mysql.cj.jdbc.Driver", "root", "");

            String sql1 = "SELECT\n" +
                    "  DISTINCT Okres AS Okres_Energii,\n" +
                    "  ilosc_Energii AS srednia_energia_miesiecznie,\n" +
                    "  Brutto AS srednia_cena_brutto\n" +
                    "FROM faktura;";
            PrzeslaniePolecenia1 = stm.executeQuery(sql1);
            System.out.println(PrzeslaniePolecenia1);

            while (PrzeslaniePolecenia1.next()){
                Date Okres = Date.valueOf(PrzeslaniePolecenia1.getString("Okres_Energii"));
                float brutto = PrzeslaniePolecenia1.getFloat("srednia_cena_brutto");
                String ilosc_Energii = PrzeslaniePolecenia1.getString("srednia_energia_miesiecznie");
                dataset.setValue(brutto, Okres.toString(), ilosc_Energii);
            }

            chart =
                    ChartFactory.createBarChart(
                            "Wykres wszystkich rachunków","Ilość_Energi","Rachunek - Brutto - złoty", dataset,PlotOrientation.VERTICAL, true, true, false);
            connection.close();
            ChartPanel panel = new ChartPanel(chart);
            setContentPane(panel);
        }catch (Exception e) {
            System.out.println("Problem"+e.getMessage());
        }
    }

}

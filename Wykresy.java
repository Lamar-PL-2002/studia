import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Wykresy extends JFrame {

    String userDB = "root";
    String passDB = "";
    String polaczenieURL = "jdbc:mysql://localhost:3306/pv";
    Statement stm;
    Connection connection;
    ResultSet PrzeslaniePolecenia1;
    JFreeChart chart;

    Wykresy(){
        super("Wykresy Faktur:");
        setSize(800,800);
        Image imageIconFrame = new ImageIcon(getClass().getClassLoader().getResource("Solary.png")).getImage();
        setIconImage(imageIconFrame);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        WyciagnanieSQLZBazy4();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Wykresy();
    }

    public void WyciagnanieSQLZBazy4(){

        // ResultSet PrzeslaniePolecenia2;
        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement();

            JDBCCategoryDataset dataset = new JDBCCategoryDataset("jdbc:mysql://localhost:3306/pv", "com.mysql.cj.jdbc.Driver", "root", "");

            //String sql1 = "SELECT Okres, Brutto, ilosc_Energii FROM faktura ";
            //String sql1 = "SELECT YEAR(Okres) AS Rok, MONTH(Okres) AS Miesiac, AVG(ilosc_Energii) AS srednia_energia_miesiecznie FROM faktura GROUP BY YEAR(Okres)";
           /* String sql1 = "SELECT\n" +
                    "  DISTINCT DATE_FORMAT(Okres, '%Y-%m') AS Okres_Energii,\n" +
                    "  AVG(ilosc_Energii) AS srednia_energia_miesiecznie\n" +
                    "FROM faktura\n" +
                    "GROUP BY DATE_FORMAT(Okres, '%Y-%m')\n" +
                    "ORDER BY Okres_Energii;"; */
           /* String sql1 = "SELECT\n" +
                    "  STR_TO_DATE(Okres, '%Y-%m') AS Okres_Energii,\n" +
                    "  AVG(ilosc_Energii) AS srednia_energia_miesiecznie\n" +
                    "FROM faktura\n" +
                    "GROUP BY STR_TO_DATE(Okres, '%Y-%m')\n" +
                    "ORDER BY Okres_Energii;"; */

          String sql1 = "SELECT\n" +
                    "  DISTINCT DATE_FORMAT(Okres, '%Y-%m-%d') AS Okres_Energii,\n" +
                    "  AVG(ilosc_Energii) AS srednia_energia_miesiecznie,\n" +
                    "  AVG(Brutto) AS srednia_cena_brutto\n" +
                    "FROM faktura\n" +
                    "GROUP BY DATE_FORMAT(Okres, '%Y-%m')\n" +
                    "ORDER BY Okres_Energii;";

            PrzeslaniePolecenia1 = stm.executeQuery(sql1);
            System.out.println(PrzeslaniePolecenia1);

            while (PrzeslaniePolecenia1.next()){
               //Date Okres = PrzeslaniePolecenia1.getDate("Okres");
                Date Okres = Date.valueOf(PrzeslaniePolecenia1.getString("Okres_Energii"));
                float brutto = PrzeslaniePolecenia1.getFloat("srednia_cena_brutto");
                String ilosc_Energii = PrzeslaniePolecenia1.getString("srednia_energia_miesiecznie");
                dataset.setValue(brutto, Okres.toString(), ilosc_Energii);
            }

            chart =
                    ChartFactory.createBarChart3D("Przedstawienie Zużycia Rachunków za Prąd:",
                            "Ilość Energii","Rachunek - Brutto - [zł]",
                            dataset, PlotOrientation.VERTICAL, true,true,false);
            PrzeslaniePolecenia1.close();
            connection.close();
            ChartPanel panel = new ChartPanel(chart);
            setContentPane(panel);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

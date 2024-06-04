import com.mysql.jdbc.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Logowanie extends JFrame implements ActionListener {

    JTextField LoginPole, HasloPole;
    JLabel LoginTekst, HasloTekst, Sprawdzenie, Tytul;
    JButton Zaloguj, czysc, buttonZamknij, buttonInfo;
    JMenuItem menuItemZamknij, menuItemInfo;

    String login, haslo;
    static String DaneBazy;
    Logowanie(){
        super("System_Logowania do Układu PV:");
        setSize(400,350);
        Image imageIconFrame = new ImageIcon(getClass().getClassLoader().getResource("login.png")).getImage();
        ImageIcon imageIconZamknij = new ImageIcon(getClass().getClassLoader().getResource("zamknij.png"));
        ImageIcon imageIconInfo = new ImageIcon(getClass().getClassLoader().getResource("info.png"));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setIconImage(imageIconFrame);

        JPanel panelG = new JPanel(new BorderLayout());
        JPanel panelN = new JPanel(null);

        JToolBar pasek=new JToolBar();
        buttonZamknij = new JButton(imageIconZamknij);
        buttonZamknij.addActionListener(this);
        buttonInfo = new JButton(imageIconInfo);
        buttonInfo.addActionListener(this);
        panelG.add(pasek,BorderLayout.NORTH);

        LoginPole = new JTextField();
        LoginPole.setBounds(130,80,120,20);

        HasloPole = new JTextField();
        HasloPole.setBounds(130,140,120,20);

        Tytul = new JLabel("Logowanie do Systemu:");
        Tytul.setBounds(130,35,170,20);
        Tytul.setForeground(Color.white);

        LoginTekst = new JLabel("Login:");
        LoginTekst.setBounds(170,55,120,20);
        LoginTekst.setForeground(Color.white);

        HasloTekst = new JLabel("Haslo:");
        HasloTekst.setBounds(170,110,120,20);
        HasloTekst.setForeground(Color.white);

        Sprawdzenie = new JLabel("");
        Sprawdzenie.setBounds(130,260,150,20);
        Sprawdzenie.setForeground(Color.white);

        Zaloguj = new JButton("Zaloguj");
        Zaloguj.setBounds(130,220,120,20);

        czysc = new JButton("Czysc");
        czysc.setBounds(130,180,120,20);

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menuProgram = new JMenu("Program");
        JMenu menuInfo = new JMenu("Info");
        jMenuBar.add(menuProgram);
        jMenuBar.add(menuInfo);

        menuItemZamknij = new JMenuItem("Zamknij System Logownia");
        menuItemInfo = new JMenuItem("O Systemie Logowania");

        menuProgram.add(menuItemZamknij);
        menuInfo.add(menuItemInfo);

        panelG.add(Tytul);
        panelN.add(LoginPole);
        panelN.add(LoginTekst);
        panelN.add(HasloTekst);
        panelN.add(HasloPole);
        panelN.add(Sprawdzenie);
        panelN.add(Zaloguj);
        panelN.add(czysc);

        menuItemZamknij.addActionListener(this);
        menuItemInfo.addActionListener(this);
        Zaloguj.addActionListener(this);
        czysc.addActionListener(this);

        setJMenuBar(jMenuBar);
        panelN.setBackground(new Color(0xFF19241C,true));
        panelG.add(panelN);
        setContentPane(panelG);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Logowanie();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == Zaloguj){
            login = LoginPole.getText();
            haslo = HasloPole.getText();
            LogowanieBaza(login,haslo);

      /*      if(login.equals("Jan") && haslo.equals("123")){
                Sprawdzenie.setText("Udalo ci sie zalogowac");
                new Tlo();
            }else{
                Sprawdzenie.setText("Nie udalo ci sie zalogowac");
            } */

        }else if(o == czysc){
            LoginPole.setText(null);
            HasloPole.setText(null);
            Sprawdzenie.setText(null);
        } else if (o == menuItemInfo) {
            JOptionPane.showMessageDialog(Logowanie.this,"Aplikacja Logujaca do programów \n"+
                    "Konwereta PV i jego zarzadzania\n\n"+
                    "Autor programu: Jan Warchol");
        } else if (o == menuItemZamknij) {
            int nr = JOptionPane.showConfirmDialog( Logowanie.this,   "Czy chcesz wyłączyć aplikację?",   "Pytanie",   JOptionPane.YES_NO_OPTION);
            if(nr == 0) {
                System.exit(0);
            }
        } else if (o == buttonZamknij) {
            int nr = JOptionPane.showConfirmDialog( Logowanie.this,   "Czy chcesz wyłączyć aplikację?",   "Pytanie",   JOptionPane.YES_NO_OPTION);
            if(nr == 0) {
                System.exit(0);
            }
        } else if (o == buttonInfo) {
            JOptionPane.showMessageDialog(Logowanie.this,"Aplikacja Logujaca do programów \n"+
                    "przykłady komponentów graficznych jako menu\n\n"+
                    "Autor programu: Jan Warchol");
        }
    }
        private void LogowanieBaza(String login, String haslo){
            String userDB = "root";
            String passDB = "";
            String polaczenieURL = "jdbc:mysql://localhost:3306/pv";
           try{
                //System.out.println("Polacenie Udane");
               Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);

                Statement stm = connection.createStatement();
                //mysql query to run

               String sql = "select * from logowanie where Login ='"+login+"' and Haslo ='"+haslo+"'";

                    ResultSet rs = stm.executeQuery(sql);
                        if(rs.next()){
                            //Gdy login i haslo sa poprawne wchodzimi na Panele
                            Sprawdzenie.setText("Udalo sie Zalogowac");
                            dispose();
                            new Tlo();
                        } else {
                            Sprawdzenie.setText("Nie udalo ci sie zalogowac");
                            LoginPole.setText(null);
                            HasloPole.setText(null);
                            //Sprawdzenie.setText(null);
                        }
                        connection.close();
                //Sprawdzenie.setText("Udalo sie Zalogowac");
                //new Tlo();
            } catch (Exception e) {
                //Sprawdzenie.setText("Nie udalo ci sie zalogowac");
                System.out.println(e.getMessage());
            }
           
        }
}
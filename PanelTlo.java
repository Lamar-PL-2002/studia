
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class PanelTlo extends JPanel implements ActionListener ,Runnable {
    JButton buttonZamknij, buttonInfo, ZapiszdoPliku, OdczytZPliku, Pojecia, Produkty, Czas, Pokaz_Obraz_Danych, Pokaz_Wykres_Danych,WprowadzDaneStanu,WprowadzDaneStanu2;

    int hours = 0, minutes = 0, seconds = 0;
    String timeString = "";

    Thread t = null;
    JButton NastepnyPanel2, NastepnyPanel4, PobierzDane2, PobierzDane4;

    JPanel PanelG, PanelN;
    JPanel[] PanelW = new JPanel[4]; //PanelW - jest na 4 oknieka

    // Okienko1
    JLabel GlownyTekstDoWprowadzeniaDanych, Skladnik_Oplat, Okres, Jednostka_Miary, Ilosc,Cena_Jednotkowa,Vat,Brutto, Data_Wprowadz_E;

    JTextField Podaj_Skladnik_Oplat, Podaj_Okres, Podaj_Jednostka_Miary, Podaj_Ilosc, Podaj_Cena_Jednostkowa;

    //Okienko 2
    JLabel EtykietaPodsumowanie, E_Skladnik_Oplat, E_Okres,E_Jednostka_Miary,E_Ilosc,E_Cena_Jednostkowa,E_VAT,E_Brutto;
    //Wyniki Okienka2;
    JLabel Wynik_Skladnik_Oplat,Wynik_Okres,Wynik_Jednostka_Miary,Wynik_Ilosc,Wynik_Cena_Jednastkowa,Wynik_Vat,Wynik_Brutto;

    //Okienko3
    JLabel Etykieta_Glowka_Bloczku_nr_3,Etykieta_Data_Odczytu, Etykieta_Wskazanie_Licznika,Etykieta_Kierunek,Etykieta_ilosc_energi;

    JTextField Data_odczytu,Wskazanie_Licznika,Kierunek, ilosc_energi;

    //okienko4
    JLabel Etykieta_Glowka_Bloczku_nr_4,Etykieta_Data_Odczytu_Wynik, Etykieta_Wskazanie_Licznika_Wynik,Etykieta_Kierunek_Wynik,Etykieta_ilosc_energi_Wynik;

    JLabel Data_Odczytu_Wynik, Wskazanie_Licznika_Wynik,Kierunek_Wynik,ilosc_energi_Wynik;

    JDateChooser datachose = new JDateChooser();


    public double brutto;
    public int oprocentowanie = 23;
    public double podatek;
    public double netto;

    ImageIcon imageIconZamknij = new ImageIcon(getClass().getClassLoader().getResource("zamknij.png"));
    ImageIcon imageIconInfo = new ImageIcon(getClass().getClassLoader().getResource("info.png"));
    ImageIcon imageIconSave = new ImageIcon(getClass().getClassLoader().getResource("paste.png"));
    ImageIcon imageIconLoade = new ImageIcon(getClass().getClassLoader().getResource("copy.png"));
    ImageIcon imageIconLearn = new ImageIcon(getClass().getClassLoader().getResource("PvInfo.png"));
    ImageIcon imageIconProduct = new ImageIcon(getClass().getClassLoader().getResource("Deye.png"));

    // ArrayList<String> array = new ArrayList<String>();

    ResultSet PrzeslaniePolecenia2;

    String userDB = "root";
    String passDB = "";
    String polaczenieURL = "jdbc:mysql://localhost:3306/pv";
    Statement stm;
    Connection connection;
    PanelTlo() {
        super();

        setLayout(new BorderLayout()); // Ustawienie layoutu dla PanelTlo

        PanelG = new JPanel(new BorderLayout()); // Panel Glowny
        PanelN = new JPanel(); // Na 4 komurnik
        PanelN.setBackground(Color.black);
        GridLayout layout = new GridLayout(2, 1);

        layout.setHgap(5);
        layout.setVgap(5);
        PanelN.setLayout(layout);
        for (int i = 0; i <= 3; i++) {
            PanelW[i] = new JPanel(null);
        }
        JToolBar pasek = new JToolBar();
        buttonZamknij = new JButton("Wyloguj", imageIconZamknij);
        buttonZamknij.addActionListener(this);
        buttonInfo = new JButton("O Programie", imageIconInfo);
        buttonInfo.addActionListener(this);
        ZapiszdoPliku = new JButton("Zapis Dane do Pliku", imageIconSave);
        ZapiszdoPliku.addActionListener(this);
        OdczytZPliku = new JButton("Wczytaj Dane z pliku", imageIconLoade);
        OdczytZPliku.addActionListener(this);
        Pojecia = new JButton("Pojęcia odnośnie Paneli Fotowoltaicznych", imageIconLearn);
        Pojecia.addActionListener(this);
        Produkty = new JButton("Producenci i produkty do paneli Fotowoltaicznych", imageIconProduct);
        Produkty.addActionListener(this);
        Czas = new JButton();
        Czas.addActionListener(this);
        Pokaz_Obraz_Danych = new JButton("Pokaz Wykresy danych");
        Pokaz_Obraz_Danych.setFont(new Font("Verdana", Font.BOLD, 20));
        Pokaz_Obraz_Danych.addActionListener(this);
        Pokaz_Wykres_Danych = new JButton("Pokaz Wykres wszystkich danych");
        Pokaz_Wykres_Danych.setFont(new Font("Verdana", Font.BOLD, 20));
        Pokaz_Wykres_Danych.addActionListener(this);
        pasek.add(buttonZamknij);
        pasek.add(buttonInfo);
        pasek.add(ZapiszdoPliku);
        pasek.add(OdczytZPliku);
        pasek.add(Pojecia);
        pasek.add(Produkty);
        pasek.add(Czas);
        pasek.add(Pokaz_Obraz_Danych);
        pasek.add(Pokaz_Wykres_Danych);
        PanelG.add(pasek, BorderLayout.NORTH);
        PanelG.add(PanelN, BorderLayout.CENTER); // Dodanie Grida do Panela Bordera

        GlownyTekstDoWprowadzeniaDanych = new JLabel("Wprowadz Dane:");
        GlownyTekstDoWprowadzeniaDanych.setBounds(380, -20, 250, 100);
        GlownyTekstDoWprowadzeniaDanych.setFont(new Font("Verdana", Font.BOLD, 24));
        GlownyTekstDoWprowadzeniaDanych.setForeground(Color.black);

        Skladnik_Oplat = new JLabel("Wprowadz dane odnośnie rachunku: ");
        Skladnik_Oplat.setBounds(120, 20, 600, 100);
        Skladnik_Oplat.setFont(new Font("Verdana", Font.BOLD, 24));
        Skladnik_Oplat.setForeground(Color.black);

        Okres = new JLabel("Wprowadz okres: ");
        Okres.setBounds(120, 60, 400, 100);
        Okres.setFont(new Font("Verdana", Font.BOLD, 24));
        Okres.setForeground(Color.black);

        Jednostka_Miary = new JLabel("Wprowadz Jednsotke miary: ");
        Jednostka_Miary.setBounds(120, 100, 400, 100);
        Jednostka_Miary.setFont(new Font("Verdana", Font.BOLD, 24));
        Jednostka_Miary.setForeground(Color.black);

        Ilosc = new JLabel("Wprowadz Ilosc: ");
        Ilosc.setBounds(120, 140, 400, 100);
        Ilosc.setFont(new Font("Verdana", Font.BOLD, 24));
        Ilosc.setForeground(Color.black);

        Cena_Jednotkowa = new JLabel("Wprowadz Cene Jednostkowa: ");
        Cena_Jednotkowa.setBounds(120, 180, 600, 100);
        Cena_Jednotkowa.setFont(new Font("Verdana", Font.BOLD, 24));
        Cena_Jednotkowa.setForeground(Color.black);

        Vat = new JLabel("Wprowadz VAT: ");
        Vat.setBounds(120, 180, 400, 100);
        Vat.setFont(new Font("Verdana", Font.BOLD, 24));
        Vat.setForeground(Color.black);

        Brutto = new JLabel("Wprowadz Brutto: ");
        Brutto.setBounds(120, 220, 400, 100);
        Brutto.setFont(new Font("Verdana", Font.BOLD, 24));
        Brutto.setForeground(Color.black);

       /* Data_Wprowadz_E = new JLabel("Wprowadza Date: ");
        Data_Wprowadz_E.setBounds(120, 220, 400, 100);
        Data_Wprowadz_E.setFont(new Font("Verdana", Font.BOLD, 24));
        Data_Wprowadz_E.setForeground(Color.black); */

        datachose.setBounds(630, 100, 100, 25);
        //datachose.setFont(new Font("Verdana", Font.BOLD, 24));
        //datachose.setForeground(Color.black);

        WprowadzDaneStanu = new JButton("Wyśli do Bazy");
        WprowadzDaneStanu.setBounds(400, 400, 155, 50);
        WprowadzDaneStanu.setForeground(Color.black);
        WprowadzDaneStanu.addActionListener(this);

        t = new Thread(this);
            t.start();


/*        PodzialMiesieczny = new String[40];
        for (int i = 0; i <40; i++) {
            PodzialMiesieczny[i] = "Miesiac " + ((i % 12) + 1) + " rok " + (2020 + i / 12);
            kwartaly = new JComboBox<>(PodzialMiesieczny);
        }
        kwartaly.setBounds(600,60,150,25);
        kwartaly.setForeground(Color.black);
        kwartaly.setForeground(Color.black);
*/
        Podaj_Skladnik_Oplat = new JTextField();
        Podaj_Skladnik_Oplat.setBounds(630, 60, 100, 25);
       // Podaj_Okres = new JTextField();
       // Podaj_Okres.setBounds(630, 100, 100, 25);
        Podaj_Jednostka_Miary = new JTextField();
        Podaj_Jednostka_Miary.setBounds(630, 140, 100, 25);
        Podaj_Ilosc = new JTextField();
        Podaj_Ilosc.setBounds(630,180,100,25);
        Podaj_Cena_Jednostkowa = new JTextField();
        Podaj_Cena_Jednostkowa.setBounds(630,220,100,25);

        PanelW[0].add(GlownyTekstDoWprowadzeniaDanych);
        PanelW[0].add(Skladnik_Oplat);
        PanelW[0].add(Okres);
        PanelW[0].add(Jednostka_Miary);
        PanelW[0].add(Ilosc);
        PanelW[0].add(WprowadzDaneStanu);
        PanelW[0].add(Cena_Jednotkowa);
        //PanelW[0].add(Data_Wprowadz_E);
        PanelW[0].add(datachose);

        PanelW[0].add(Podaj_Skladnik_Oplat);
       // PanelW[0].add(Podaj_Okres);
        PanelW[0].add(Podaj_Jednostka_Miary);
        PanelW[0].add(Podaj_Ilosc);
        PanelW[0].add(Podaj_Cena_Jednostkowa);

        //Okienko_2;

        EtykietaPodsumowanie = new JLabel("Wyniki z Okresu:");
        EtykietaPodsumowanie.setBounds(380, -20, 250, 100);
        EtykietaPodsumowanie.setFont(new Font("Verdana", Font.BOLD, 24));
        EtykietaPodsumowanie.setForeground(Color.black);

        E_Skladnik_Oplat = new JLabel("Wybierz Składnik Oplat: ");
        E_Skladnik_Oplat.setBounds(300, 30, 350, 100);
        E_Skladnik_Oplat.setFont(new Font("Verdana", Font.BOLD, 24));
        E_Skladnik_Oplat.setForeground(Color.black);

        E_Okres = new JLabel("Okres: ");
        E_Okres.setBounds(300, 70, 350, 100);
        E_Okres.setFont(new Font("Verdana", Font.BOLD, 24));
        E_Okres.setForeground(Color.black);

        E_Jednostka_Miary = new JLabel("Jednostka Miary: ");
        E_Jednostka_Miary.setBounds(300, 110, 350, 100);
        E_Jednostka_Miary.setFont(new Font("Verdana", Font.BOLD, 24));
        E_Jednostka_Miary.setForeground(Color.black);

        E_Ilosc = new JLabel("Ilosc: ");
        E_Ilosc.setBounds(300, 150, 350, 100);
        E_Ilosc.setFont(new Font("Verdana", Font.BOLD, 24));
        E_Ilosc.setForeground(Color.black);

        E_Cena_Jednostkowa = new JLabel("Cena Jednostkowa: ");
        E_Cena_Jednostkowa.setBounds(300, 190, 350, 100);
        E_Cena_Jednostkowa.setFont(new Font("Verdana", Font.BOLD, 24));
        E_Cena_Jednostkowa.setForeground(Color.black);

        E_VAT = new JLabel("Podatek:");
        E_VAT.setBounds(300, 230, 350, 100);
        E_VAT.setFont(new Font("Verdana", Font.BOLD, 24));
        E_VAT.setForeground(Color.black);

        E_Brutto = new JLabel("Kwota Brutto:");
        E_Brutto.setBounds(300, 270, 350, 100);
        E_Brutto.setFont(new Font("Verdana", Font.BOLD, 24));
        E_Brutto.setForeground(Color.black);

        Wynik_Skladnik_Oplat = new JLabel("Test1");
        Wynik_Skladnik_Oplat.setBounds(660, 30, 250, 100);
        Wynik_Skladnik_Oplat.setFont(new Font("Verdana", Font.BOLD, 24));
        Wynik_Skladnik_Oplat.setForeground(Color.black);

        Wynik_Okres = new JLabel("Test2");
        Wynik_Okres.setBounds(660, 70, 250, 100);
        Wynik_Okres.setFont(new Font("Verdana", Font.BOLD, 24));
        Wynik_Okres.setForeground(Color.black);

        Wynik_Jednostka_Miary = new JLabel("Test3");
        Wynik_Jednostka_Miary.setBounds(660, 110, 250, 100);
        Wynik_Jednostka_Miary.setFont(new Font("Verdana", Font.BOLD, 24));
        Wynik_Jednostka_Miary.setForeground(Color.black);

        Wynik_Ilosc = new JLabel("Test4");
        Wynik_Ilosc.setBounds(660, 150, 250, 100);
        Wynik_Ilosc.setFont(new Font("Verdana", Font.BOLD, 24));
        Wynik_Ilosc.setForeground(Color.black);

        Wynik_Cena_Jednastkowa = new JLabel("Test5");
        Wynik_Cena_Jednastkowa.setBounds(660, 190, 250, 100);
        Wynik_Cena_Jednastkowa.setFont(new Font("Verdana", Font.BOLD, 24));
        Wynik_Cena_Jednastkowa.setForeground(Color.black);

        Wynik_Vat = new JLabel("Test6");
        Wynik_Vat.setBounds(660, 230, 250, 100);
        Wynik_Vat.setFont(new Font("Verdana", Font.BOLD, 24));
        Wynik_Vat.setForeground(Color.black);

        Wynik_Brutto = new JLabel("Test7");
        Wynik_Brutto.setBounds(660, 270, 250, 100);
        Wynik_Brutto.setFont(new Font("Verdana", Font.BOLD, 24));
        Wynik_Brutto.setForeground(Color.black);

       /* PoprzedniPanel2 = new JButton("Poprzedni");
        PoprzedniPanel2.setBounds(300, 375, 155, 50);
        PoprzedniPanel2.setFont(new Font("Verdana", Font.BOLD, 12));
        PoprzedniPanel2.setForeground(Color.black);
        PoprzedniPanel2.addActionListener(this); */

       NastepnyPanel2 = new JButton("Nastepny");
        NastepnyPanel2.setBounds(600, 375, 155, 50);
        NastepnyPanel2.setFont(new Font("Verdana", Font.BOLD, 12));
        NastepnyPanel2.setForeground(Color.black);
        NastepnyPanel2.addActionListener(this);

        PobierzDane2 = new JButton("pobierz Dane z bazy");
        PobierzDane2.setBounds(325, 375, 175, 50);
        PobierzDane2.setFont(new Font("Verdana", Font.BOLD, 12));
        PobierzDane2.setForeground(Color.black);
        PobierzDane2.addActionListener(this);

        PanelW[1].add(EtykietaPodsumowanie);
        PanelW[1].add(E_Skladnik_Oplat);
        PanelW[1].add(E_Okres);
        PanelW[1].add(E_Jednostka_Miary);
        PanelW[1].add(E_Ilosc);
        PanelW[1].add(E_Cena_Jednostkowa);
        PanelW[1].add(E_VAT);
        PanelW[1].add(E_Brutto);
        PanelW[1].add(Wynik_Skladnik_Oplat);
        PanelW[1].add(Wynik_Okres);
        PanelW[1].add(Wynik_Jednostka_Miary);
        PanelW[1].add(Wynik_Ilosc);
        PanelW[1].add(Wynik_Cena_Jednastkowa);
        PanelW[1].add(Wynik_Vat);
        PanelW[1].add(Wynik_Brutto);
        //PanelW[1].add(PoprzedniPanel2);
        PanelW[1].add(NastepnyPanel2);
        PanelW[1].add(PobierzDane2);

       //Okienko 3:

        Etykieta_Data_Odczytu = new JLabel("Data_Odczytu: ");
        Etykieta_Data_Odczytu.setBounds(150, 20, 350, 100);
        Etykieta_Data_Odczytu.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_Data_Odczytu.setForeground(Color.black);

        Etykieta_Wskazanie_Licznika = new JLabel("Wskazanie Licznika: ");
        Etykieta_Wskazanie_Licznika.setBounds(150, 80, 400, 100);
        Etykieta_Wskazanie_Licznika.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_Wskazanie_Licznika.setForeground(Color.black);

        Etykieta_Kierunek = new JLabel("Kierunek");
        Etykieta_Kierunek.setBounds(150, 140, 400, 100);
        Etykieta_Kierunek.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_Kierunek.setForeground(Color.black);

        Etykieta_ilosc_energi = new JLabel("Ilosc Energi: ");
        Etykieta_ilosc_energi.setBounds(150, 200, 400, 100);
        Etykieta_ilosc_energi.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_ilosc_energi.setForeground(Color.black);

        Etykieta_Glowka_Bloczku_nr_3 = new JLabel("Podaj dane:");
        Etykieta_Glowka_Bloczku_nr_3.setBounds(380, -20, 250, 100);
        Etykieta_Glowka_Bloczku_nr_3.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_Glowka_Bloczku_nr_3.setForeground(Color.black);

        Data_odczytu = new JTextField();
        Data_odczytu.setBounds(450, 60, 100, 25);
        Wskazanie_Licznika = new JTextField();
        Wskazanie_Licznika.setBounds(450, 120, 100, 25);
        Kierunek = new JTextField();
        Kierunek.setBounds(450, 180, 100, 25);

        ilosc_energi = new JTextField();
        ilosc_energi.setBounds(450, 240, 100, 25);

        WprowadzDaneStanu2 = new JButton("Wyśli do Bazy");
        WprowadzDaneStanu2.setBounds(400, 325, 155, 50);
        WprowadzDaneStanu2.setForeground(Color.black);
        WprowadzDaneStanu2.addActionListener(this);

        PanelW[2].add(Etykieta_Data_Odczytu);
        PanelW[2].add(Etykieta_Wskazanie_Licznika);
        PanelW[2].add(Etykieta_Kierunek);
        PanelW[2].add(Etykieta_ilosc_energi);
        PanelW[2].add(Data_odczytu);
        PanelW[2].add(Wskazanie_Licznika);
        PanelW[2].add(Kierunek);
        PanelW[2].add(ilosc_energi);
        PanelW[2].add(Etykieta_Glowka_Bloczku_nr_3);
        PanelW[2].add(WprowadzDaneStanu2);

        //Okienko 4:
        Etykieta_Glowka_Bloczku_nr_4 = new JLabel("Wyniki:");
        Etykieta_Glowka_Bloczku_nr_4.setBounds(380, -20, 250, 100);
        Etykieta_Glowka_Bloczku_nr_4.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_Glowka_Bloczku_nr_4.setForeground(Color.black);

        Etykieta_Data_Odczytu_Wynik = new JLabel("Data_Odczytu:");
        Etykieta_Data_Odczytu_Wynik.setBounds(300, 30, 350, 100);
        Etykieta_Data_Odczytu_Wynik.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_Data_Odczytu_Wynik.setForeground(Color.black);

        Etykieta_Wskazanie_Licznika_Wynik = new JLabel("Wskazanie Licznika: ");
        Etykieta_Wskazanie_Licznika_Wynik.setBounds(300, 70, 350, 100);
        Etykieta_Wskazanie_Licznika_Wynik.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_Wskazanie_Licznika_Wynik.setForeground(Color.black);

        Etykieta_Kierunek_Wynik = new JLabel("Kierunek: ");
        Etykieta_Kierunek_Wynik.setBounds(300, 110, 350, 100);
        Etykieta_Kierunek_Wynik.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_Kierunek_Wynik.setForeground(Color.black);

        Etykieta_ilosc_energi_Wynik = new JLabel("Ilosc Energi: ");
        Etykieta_ilosc_energi_Wynik.setBounds(300, 150, 350, 100);
        Etykieta_ilosc_energi_Wynik.setFont(new Font("Verdana", Font.BOLD, 24));
        Etykieta_ilosc_energi_Wynik.setForeground(Color.black);

        Data_Odczytu_Wynik = new JLabel("Test1");
        Data_Odczytu_Wynik.setBounds(660, 30, 250, 100);
        Data_Odczytu_Wynik.setFont(new Font("Verdana", Font.BOLD, 24));
        Data_Odczytu_Wynik.setForeground(Color.black);

        Wskazanie_Licznika_Wynik = new JLabel("Test2");
        Wskazanie_Licznika_Wynik.setBounds(660, 70, 250, 100);
        Wskazanie_Licznika_Wynik.setFont(new Font("Verdana", Font.BOLD, 24));
        Wskazanie_Licznika_Wynik.setForeground(Color.black);

        Kierunek_Wynik = new JLabel("Test3");
        Kierunek_Wynik.setBounds(660, 110, 250, 100);
        Kierunek_Wynik.setFont(new Font("Verdana", Font.BOLD, 24));
        Kierunek_Wynik.setForeground(Color.black);

        ilosc_energi_Wynik = new JLabel("Test4");
        ilosc_energi_Wynik.setBounds(660, 150, 250, 100);
        ilosc_energi_Wynik.setFont(new Font("Verdana", Font.BOLD, 24));
        ilosc_energi_Wynik.setForeground(Color.black);

      /*  PoprzedniPanel4 = new JButton("Poprzedni");
        PoprzedniPanel4.setBounds(300, 275, 155, 50);
        PoprzedniPanel4.setFont(new Font("Verdana", Font.BOLD, 12));
        PoprzedniPanel4.setForeground(Color.black);
        PoprzedniPanel4.addActionListener(this); */

        NastepnyPanel4 = new JButton("Nastepny");
        NastepnyPanel4.setBounds(600, 275, 155, 50);
        NastepnyPanel4.setFont(new Font("Verdana", Font.BOLD, 12));
        NastepnyPanel4.setForeground(Color.black);
        NastepnyPanel4.addActionListener(this);

        PobierzDane4 = new JButton("Pobierz Dane z bazy");
        PobierzDane4.setBounds(325, 275, 175, 50);
        PobierzDane4.setFont(new Font("Verdana", Font.BOLD, 12));
        PobierzDane4.setForeground(Color.black);
        PobierzDane4.addActionListener(this);

        PanelW[3].add(Etykieta_Data_Odczytu_Wynik);
        PanelW[3].add(Etykieta_Wskazanie_Licznika_Wynik);
        PanelW[3].add(Etykieta_Kierunek_Wynik);
        PanelW[3].add(Etykieta_ilosc_energi_Wynik);
        PanelW[3].add(Data_Odczytu_Wynik);
        PanelW[3].add(Wskazanie_Licznika_Wynik);
        PanelW[3].add(Kierunek_Wynik);
        PanelW[3].add(ilosc_energi_Wynik);
        PanelW[3].add(Etykieta_Glowka_Bloczku_nr_4);
        //PanelW[3].add(PoprzedniPanel4);
        PanelW[3].add(NastepnyPanel4);
        PanelW[3].add(PobierzDane4);


        for (int i = 0; i <=3; i++) {
            PanelW[i].setBackground(new Color(175, 225, 175));
        }

        PanelN.add(PanelW[0]); // Dodaj PanelW[0] do PanelN
        PanelN.add(PanelW[1]);
        PanelN.add(PanelW[2]);
        PanelN.add(PanelW[3]);

        add(PanelG, BorderLayout.CENTER); // Dodaj PanelG do PanelTlo
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Obsługa zdarzeń przycisków
        Object o = e.getSource();
        if (o == buttonZamknij) {
            int nr = JOptionPane.showConfirmDialog(PanelTlo.this, "Czy chcesz wyłączyć aplikację?", "Pytanie", JOptionPane.YES_NO_OPTION);
            if (nr == 0) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                System.exit(0);
            }
        } else if (o == buttonInfo) {
            JOptionPane.showMessageDialog(PanelTlo.this, "Aplikacja Zarzadzanie Panelami PV \n" +
                    "Autor programu: Jan Warchol");
        } else if (o == Pojecia) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.kompaniasolarna.pl/slowniczek-oze/fotowoltaika-slownik-2/"));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        } else if (o == Produkty) {
            try {
                Desktop.getDesktop().browse(new URI("https://oze-shop.pl/ranking-falownikow-fotowoltaicznych-2024/"));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        } else if (o == OdczytZPliku) {
            try {
                Odczyt_z_Pliku();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Błąd podczas odczytu danych do pliku: " + ex.getMessage());
            }
        } else if (o == ZapiszdoPliku) {
            try {
                Zapis_do_Pliku();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Błąd podczas zapisu danych do pliku: " + ex.getMessage());
            }
        } else if (o == Pokaz_Obraz_Danych) {
            new Wykresy();
        } else if (o == Pokaz_Wykres_Danych) {
            new Wykresy2();
        }
        if(o == WprowadzDaneStanu){
            WyciagnanieSQLZBazy1();
        }
        if(o == WprowadzDaneStanu2){
            WyciagnanieSQLZBazy2();
        }
        if(o == NastepnyPanel2){
            WyciagnanieSQLZBazy3();
        }
        if(o == PobierzDane2){
            WyciagnanieSQLZBazy4();
        }
        /*if(o == PoprzedniPanel2){
            WyciagnanieSQLZBazy5();
        } */
        if(o == PobierzDane4){
            WyciagnanieSQLZBazy6();
        }
        if(o == NastepnyPanel4){
            WyciagnanieSQLZBazy7();
        }
      /*  if(o == PoprzedniPanel4){
            WyciagnanieSQLZBazy8();
        } */
    }
        private void WyciagnanieSQLZBazy1(){

            try {
                //System.out.println("Polacenie Udane");
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
                stm = connection.createStatement(); //Otwarcie strumienia

                int liczba_Podaj_Ilosc = Integer.parseInt(Podaj_Ilosc.getText());
                float liczba_Podaj_Cena_Jednostkowa = Float.parseFloat(Podaj_Cena_Jednostkowa.getText());

                Losowanie_Nettto();

                /*String sql1 = "INSERT INTO faktura(Skladnik_Oplat,Okres,jednostka_miary,ilość Energii,Cena_Jednostkowa,Netto" +
                        "Oprocentowanie, VAT, Brutto)"+"VALUES('"+Podaj_Skladnik_Oplat+"','"+Podaj_Okres+"','"+Jednostka_Miary+"','"+liczba_Podaj_Ilosc+"','"+liczba_Podaj_Cena_Jednostkowa+
                        "','"+netto+"','"+oprocentowanie+"','"+podatek+"','"+brutto+")"; */

                SimpleDateFormat Formatdaty = new SimpleDateFormat("yyyy-MM-dd");
                String  WartoscFormatowejDaty = Formatdaty.format(datachose.getDate());

                /*String sql1 = "INSERT INTO faktura" +
                        "(Skladnik_Oplat,Okres,jednostka_miary,ilosc_Energii,Cena_Jednostkowa,Netto,Oprocentowanie, VAT, Brutto, Data)"
                        +"VALUES('" + Podaj_Skladnik_Oplat.getText() + "','" + Podaj_Okres.getText() + "','" +
                        Podaj_Jednostka_Miary.getText() + "','" + liczba_Podaj_Ilosc + "','" +
                        liczba_Podaj_Cena_Jednostkowa + "','" + netto + "','" + oprocentowanie + "','" +
                        podatek + "','" + brutto + "','"+ WartoscFormatowejDaty + "')"; */

                String sql1 = "INSERT INTO faktura" +
                        "(Skladnik_Oplat,Okres,jednostka_miary,ilosc_Energii,Cena_Jednostkowa,Netto,Oprocentowanie, VAT, Brutto)"
                        +"VALUES('" + Podaj_Skladnik_Oplat.getText() + "','" + WartoscFormatowejDaty + "','" +
                        Podaj_Jednostka_Miary.getText() + "','" + liczba_Podaj_Ilosc + "','" +
                        liczba_Podaj_Cena_Jednostkowa + "','" + netto + "','" + oprocentowanie + "','" +
                        podatek + "','" + brutto +"')";


                int PrzeslaniePolecenia1 = stm.executeUpdate(sql1);

                if (PrzeslaniePolecenia1 > 0) {
                    System.out.println("Przesłanie danych do bazy udane.");
                } else {
                    System.out.println("Nie udało się przesłać danych do bazy.");
                }

                System.out.println("Udalo sie polaczyc do bazy");
                //connection.close();
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    private void WyciagnanieSQLZBazy2(){
        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement();

            String sql1 = "INSERT INTO odczyty" +
                    "(Wskazanie_Licznika,Kierunek,ilosc_Energii,data_odczytu)"
                    +"VALUES('" +Wskazanie_Licznika.getText()+"','"+Kierunek.getText()+"','"+ilosc_energi.getText()+"','"+Data_odczytu.getText()+"')";
            int PrzeslaniePolecenia1 = stm.executeUpdate(sql1);

            if (PrzeslaniePolecenia1 > 0) {
                System.out.println("Przesłanie danych do bazy udane.");
            } else {
                System.out.println("Nie udało się przesłać danych do bazy.");
            }
            System.out.println("Udalo sie polaczyc do bazy");

           /* if (PrzeslaniePolecenia2 > 0) {
                System.out.println("Przesłanie danych do bazy udane.");
            } else {
                System.out.println("Nie udało się przesłać danych do bazy.");
            } */
            //connection.close();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void WyciagnanieSQLZBazy3(){

        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            //String sql1 = "SELECT Skladnik_Oplat,Okres,Jednostka_miary,ilosc_Energii,Cena_Jednostkowa,Oprocentowanie,Brutto FROM faktura";
          //  PrzeslaniePolecenia2 = stm.executeQuery(sql1);

            PrzeslaniePolecenia2.next();
                String PobierzSkladnik_Oplat = PrzeslaniePolecenia2.getString("Skladnik_Oplat");
                String PobierzOkres = PrzeslaniePolecenia2.getString("Okres");
                String PobierzIloscEnergi = PrzeslaniePolecenia2.getString("ilosc_Energii");
                String PobierzJednostka_miary = PrzeslaniePolecenia2.getString("Jednostka_miary");
                String PobierzCena_Jednostkowa = PrzeslaniePolecenia2.getString("Cena_Jednostkowa");
                String PobierzOprocentowanie = PrzeslaniePolecenia2.getString("Oprocentowanie");
                String PobierzPodatek = PrzeslaniePolecenia2.getString("Brutto");

                System.out.print(PobierzSkladnik_Oplat+",");
                System.out.print(PobierzOkres+",");
                System.out.print(PobierzIloscEnergi+",");
                System.out.print(PobierzJednostka_miary+",");
                System.out.print(PobierzCena_Jednostkowa+",");
                System.out.print(PobierzOprocentowanie+",");
                System.out.println(PobierzPodatek);

                Wynik_Skladnik_Oplat.setText(PobierzSkladnik_Oplat);
                Wynik_Okres.setText(PobierzOkres);
                Wynik_Jednostka_Miary.setText(PobierzJednostka_miary);
                Wynik_Ilosc.setText(PobierzIloscEnergi);
                Wynik_Cena_Jednastkowa.setText(PobierzCena_Jednostkowa);
                Wynik_Vat.setText(PobierzOprocentowanie);
                Wynik_Brutto.setText(PobierzPodatek);
                System.out.println("Wyświetlono dane.");
           connection.close();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void WyciagnanieSQLZBazy4(){

       // ResultSet PrzeslaniePolecenia2;
        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement();

            String sql1 = "SELECT Skladnik_Oplat,Okres,Jednostka_miary,ilosc_Energii,Cena_Jednostkowa,Oprocentowanie,Brutto FROM faktura";
            PrzeslaniePolecenia2 = stm.executeQuery(sql1);

            PrzeslaniePolecenia2.next();

                String PobierzSkladnik_Oplat = PrzeslaniePolecenia2.getString("Skladnik_Oplat");
                String PobierzOkres = PrzeslaniePolecenia2.getString("Okres");
                String PobierzIloscEnergi = PrzeslaniePolecenia2.getString("ilosc_Energii");
                String PobierzJednostka_miary = PrzeslaniePolecenia2.getString("Jednostka_miary");
                String PobierzCena_Jednostkowa = PrzeslaniePolecenia2.getString("Cena_Jednostkowa");
                String PobierzOprocentowanie = PrzeslaniePolecenia2.getString("Oprocentowanie");
                String PobierzPodatek = PrzeslaniePolecenia2.getString("Brutto");

                System.out.print(PobierzSkladnik_Oplat + ",");
                System.out.print(PobierzOkres + ",");
                System.out.print(PobierzIloscEnergi + ",");
                System.out.print(PobierzJednostka_miary + ",");
                System.out.print(PobierzCena_Jednostkowa + ",");
                System.out.print(PobierzOprocentowanie + ",");
                System.out.println(PobierzPodatek);

                Wynik_Skladnik_Oplat.setText(PobierzSkladnik_Oplat);
                Wynik_Okres.setText(PobierzOkres);
                Wynik_Jednostka_Miary.setText(PobierzJednostka_miary);
                Wynik_Ilosc.setText(PobierzIloscEnergi);
                Wynik_Cena_Jednastkowa.setText(PobierzCena_Jednostkowa);
                Wynik_Vat.setText(PobierzOprocentowanie);
                Wynik_Brutto.setText(PobierzPodatek);
                System.out.println("Wyświetlono dane.");
               // connection.close();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void WyciagnanieSQLZBazy5(){

        // ResultSet PrzeslaniePolecenia2;
        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
         //   String sql1 = "SELECT Skladnik_Oplat,Okres,Jednostka_miary,ilosc_Energii,Cena_Jednostkowa,Oprocentowanie,Brutto FROM faktura";
           // PrzeslaniePolecenia2 = stm.executeQuery(sql1);

                PrzeslaniePolecenia2.next();
                String PobierzSkladnik_Oplat = PrzeslaniePolecenia2.getString("Skladnik_Oplat");
                String PobierzOkres = PrzeslaniePolecenia2.getString("Okres");
                String PobierzIloscEnergi = PrzeslaniePolecenia2.getString("ilosc_Energii");
                String PobierzJednostka_miary = PrzeslaniePolecenia2.getString("Jednostka_miary");
                String PobierzCena_Jednostkowa = PrzeslaniePolecenia2.getString("Cena_Jednostkowa");
                String PobierzOprocentowanie = PrzeslaniePolecenia2.getString("Oprocentowanie");
                String PobierzPodatek = PrzeslaniePolecenia2.getString("Brutto");

                System.out.print(PobierzSkladnik_Oplat + ",");
                System.out.print(PobierzOkres + ",");
                System.out.print(PobierzIloscEnergi + ",");
                System.out.print(PobierzJednostka_miary + ",");
                System.out.print(PobierzCena_Jednostkowa + ",");
                System.out.print(PobierzOprocentowanie + ",");
                System.out.println(PobierzPodatek);

                Wynik_Skladnik_Oplat.setText(PobierzSkladnik_Oplat);
                Wynik_Okres.setText(PobierzOkres);
                Wynik_Jednostka_Miary.setText(PobierzJednostka_miary);
                Wynik_Ilosc.setText(PobierzIloscEnergi);
                Wynik_Cena_Jednastkowa.setText(PobierzCena_Jednostkowa);
                Wynik_Vat.setText(PobierzOprocentowanie);
                Wynik_Brutto.setText(PobierzPodatek);
                System.out.println("Wyświetlono dane.");
                //connection.close();
            }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void WyciagnanieSQLZBazy6(){

        // ResultSet PrzeslaniePolecenia2;
        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement();

            String sql1 = "SELECT Wskazanie_Licznika, Kierunek, ilosc_Energii, data_odczytu FROM odczyty";
            PrzeslaniePolecenia2 = stm.executeQuery(sql1);

            PrzeslaniePolecenia2.next();

            String  PobierzWskaznik_licznika = PrzeslaniePolecenia2.getString("Wskazanie_Licznika");
            String  PobierzKierunek = PrzeslaniePolecenia2.getString("Kierunek");
            String  PobierzIlosc_Energii_2 = PrzeslaniePolecenia2.getString("ilosc_Energii");
            String  PobierzDataOdczytu = PrzeslaniePolecenia2.getString("data_odczytu");


            System.out.print( PobierzWskaznik_licznika + ",");
            System.out.print( PobierzKierunek + ",");
            System.out.print(PobierzIlosc_Energii_2 + ",");
            System.out.print(PobierzDataOdczytu + ",");


            Data_Odczytu_Wynik.setText(PobierzDataOdczytu);
            Wskazanie_Licznika_Wynik.setText(PobierzWskaznik_licznika);
            Kierunek_Wynik.setText(PobierzKierunek);
            ilosc_energi_Wynik.setText(PobierzIlosc_Energii_2);

            System.out.println("Wyświetlono dane.");
            //connection.close();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void WyciagnanieSQLZBazy7(){

        // ResultSet PrzeslaniePolecenia2;
        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement();

            //String sql1 = "SELECT Wskaznik_Licznika, Kierunek, ilosc_Energii, data_odczytu FROM odczyty";
            //PrzeslaniePolecenia2 = stm.executeQuery(sql1);

            PrzeslaniePolecenia2.next();

            String  PobierzWskaznik_licznika = PrzeslaniePolecenia2.getString("Wskazanie_Licznika");
            String  PobierzKierunek = PrzeslaniePolecenia2.getString("Kierunek");
            String  PobierzIlosc_Energii_2 = PrzeslaniePolecenia2.getString("ilosc_Energii");
            String  PobierzDataOdczytu = PrzeslaniePolecenia2.getString("data_odczytu");


            System.out.print( PobierzWskaznik_licznika + ",");
            System.out.print( PobierzKierunek + ",");
            System.out.print(PobierzIlosc_Energii_2 + ",");
            System.out.print(PobierzDataOdczytu + ",");

            Data_Odczytu_Wynik.setText(PobierzDataOdczytu);
            Wskazanie_Licznika_Wynik.setText(PobierzWskaznik_licznika);
            Kierunek_Wynik.setText(PobierzKierunek);
            ilosc_energi_Wynik.setText(PobierzIlosc_Energii_2);

            System.out.println("Wyświetlono dane.");
            connection.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void WyciagnanieSQLZBazy8(){

        // ResultSet PrzeslaniePolecenia2;
        try {
            //System.out.println("Polacenie Udane");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(polaczenieURL, userDB, passDB);
            stm = connection.createStatement();


            //String sql1 = "SELECT Wskaznik_Licznika, Kierunek, ilosc_Energii, data_odczytu FROM odczyty";
            //PrzeslaniePolecenia2 = stm.executeQuery(sql1);

            //PrzeslaniePolecenia2.next();

            if(!PrzeslaniePolecenia2.isLast()) {
                PrzeslaniePolecenia2.previous();
                String PobierzWskaznik_licznika = PrzeslaniePolecenia2.getString("Wskazanie_Licznika");
                String PobierzKierunek = PrzeslaniePolecenia2.getString("Kierunek");
                String PobierzIlosc_Energii_2 = PrzeslaniePolecenia2.getString("ilosc_Energii");
                String PobierzDataOdczytu = PrzeslaniePolecenia2.getString("data_odczytu");


                System.out.print(PobierzWskaznik_licznika + ",");
                System.out.print(PobierzKierunek + ",");
                System.out.print(PobierzIlosc_Energii_2 + ",");
                System.out.print(PobierzDataOdczytu + ",");

                Data_Odczytu_Wynik.setText(PobierzDataOdczytu);
                Wskazanie_Licznika_Wynik.setText(PobierzWskaznik_licznika);
                Kierunek_Wynik.setText(PobierzKierunek);
                ilosc_energi_Wynik.setText(PobierzIlosc_Energii_2);

                System.out.println("Wyświetlono dane.");
                //connection.close();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Losowanie_Nettto(){
        double WartoscGorna = 190;
        Random random = new Random();
        netto = random.nextDouble(WartoscGorna + 1);
        podatek = netto * (oprocentowanie/100.0f);
        System.out.println(podatek);
        brutto = netto + podatek;
    }

     public void Odczyt_z_Pliku() throws  IOException{
         JFileChooser fileChooser2 = new JFileChooser();
         // Otwarcie okna dialogowego wyboru pliku
         int wybor2 = fileChooser2.showOpenDialog(this);

         // Sprawdzanie, czy użytkownik wybrał plik
         if (wybor2 == JFileChooser.APPROVE_OPTION) {
             // Pobranie wybranego pliku
             File plik = fileChooser2.getSelectedFile();
             FileReader fr = new FileReader(plik);

             // Utworzenie obiektu BufferedReader
             BufferedReader br = new BufferedReader(fr);
             ArrayList<String> wiersze = new ArrayList<String>();

             String wiersz;
             while ((wiersz = br.readLine()) != null) {
                 wiersze.add(wiersz);
             }

             if (wiersze.size() < 12) {
                 JOptionPane.showMessageDialog(null, "Plik nie zawiera wystarczającej liczby danych!");
                 return;
             }

             // Wczytanie danych do JLabel w okienku 2
             Wynik_Skladnik_Oplat.setText(wiersze.get(5));
             Wynik_Okres.setText(wiersze.get(6));
             Wynik_Jednostka_Miary.setText(wiersze.get(7));
             Wynik_Ilosc.setText(wiersze.get(8));
             Wynik_Cena_Jednastkowa.setText(wiersze.get(9));
             Wynik_Vat.setText(wiersze.get(10));
             Wynik_Brutto.setText(wiersze.get(11));

             // Wczytanie danych do JLabel w okienku 4
             Data_Odczytu_Wynik.setText(wiersze.get(0));
             Wskazanie_Licznika_Wynik.setText(wiersze.get(1));
             Kierunek_Wynik.setText(wiersze.get(2));
             ilosc_energi_Wynik.setText(wiersze.get(3));

             // Zamknięcie pliku
             br.close();

             //Komunikat że Dane Odczytane
             JOptionPane.showMessageDialog(null, "Dane Odczytane z Pliku pliku: " + plik.getAbsolutePath());
         }
    }

    public void Zapis_do_Pliku() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT file - (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int wybor = fileChooser.showSaveDialog(this);
        if (wybor == JFileChooser.APPROVE_OPTION) {
            File plik = fileChooser.getSelectedFile();
            FileWriter fw = new FileWriter(plik);

            // Utworzenie obiektu BufferedWriter
            BufferedWriter bw = new BufferedWriter(fw);

            // Zapisanie danych do pliku
            bw.write( Data_Odczytu_Wynik.getText() + "\n");
            bw.write( Wskazanie_Licznika_Wynik.getText() + "\n");
            bw.write( Kierunek_Wynik.getText() + "\n");
            bw.write(ilosc_energi_Wynik.getText() + "\n");
            bw.write("---------------------------------\n");
            bw.write( Wynik_Skladnik_Oplat.getText() + "\n");
            bw.write( Wynik_Okres.getText() + "\n");
            bw.write( Wynik_Jednostka_Miary.getText() + "\n");
            bw.write(Wynik_Ilosc.getText() + "\n");
            bw.write( Wynik_Cena_Jednastkowa.getText() + "\n");
            bw.write(Wynik_Vat.getText() + "%\n");
            bw.write( Wynik_Brutto.getText() + "\n");

            // Zamknięcie Bufora
            bw.close();

            // Komunikat sprawdzajacy czy plik został poprawnie zrohbionyu
            JOptionPane.showMessageDialog(null, "Dane zapisane do pliku: " + plik.getAbsolutePath());
            }
        }

    //Wątek odpowiedzialny za Działanie czasu
    @Override
    public void run() {
        try {
            while (true){
                //Obiekt pobierajacy instacje klasy kalendarz
                Calendar calendar = Calendar.getInstance();
                hours = calendar.get(Calendar.HOUR_OF_DAY);
                //Jeżeli godzian bedzie 0;
                if(hours >= 23 || hours == 0){
                    hours = 0;
                    seconds = 0;
                    minutes = 0;
                }
                minutes = calendar.get(Calendar.MINUTE);
                seconds = calendar.get(Calendar.SECOND);

                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                Date date = calendar.getTime();
                timeString = format.format(date);

                WezCzas();
            }
        }catch (Exception e){
            System.out.println("Nie nadano poprawnie godziny "+e.getMessage());
        }
    }

    public void WezCzas(){
        Czas.setFont(new Font("Verdana", Font.BOLD, 20));
        Czas.setText(timeString);
    }
}

import javax.swing.*;
import java.awt.*;

public class Tlo extends JFrame {

    Tlo(){
        super("Kontroler domowego PV");
        setSize(1920,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image imageIconFrame = new ImageIcon(getClass().getClassLoader().getResource("Solary.png")).getImage();
        setIconImage(imageIconFrame);
        PanelTlo paneltlo = new PanelTlo();

        setContentPane(paneltlo); // Ustaw PanelTlo jako kontent głównego okna
        setVisible(true);
    }

    public static void main(String[] args) {
       // new Logowanie();
        new Tlo();
    }
}

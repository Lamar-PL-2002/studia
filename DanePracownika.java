import java.util.Scanner;

public class DanePracownika {
	
	private String imie;
	private String nazwisko;
	private double zarobki;
	private static String nazwaFirmy;
	
	public DanePracownika(String imie, String nazwisko, double zarobki) {
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.zarobki = zarobki;
	}
	
	public void ustawZarobek(double zarobki) {
		if(zarobki >= 1500) {
			this.zarobki = zarobki;
		}else {
			System.out.println("Zarobki nie mogą być mniejsze niż 1500 złoty!");
		}
	}
	
	public static void ustawnazweFirmy(String nazwa) {
		nazwaFirmy = nazwa;
	}
	
	public void wyswietlInformacje(){
		System.out.println("Imie: "+ imie);
		System.out.println("Nazwisko: "+ nazwisko);
		System.out.println("Zarobki: "+ zarobki);
		System.out.println("Firma: "+ nazwaFirmy);
	}
	
}

class Firma {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Nazwa Firmy: ");
		String nazwaFirmy = scanner.nextLine();
		DanePracownika.ustawnazweFirmy(nazwaFirmy);;
		DanePracownika[] pracownicy = new DanePracownika[3];
		
		for(int i = 0; i < 3; i++) {
			System.out.println("Podaj dane pracownika" + (i + 1) + ":");
			System.out.print("Imie:");
			String imie = scanner.nextLine();
			System.out.print("Nazwisko: ");
			String nazwisko = scanner.nextLine();
			System.out.print("Zarobki: ");
			Double zarobki = Double.parseDouble(scanner.nextLine());
			System.out.println("--------------------------------");
			pracownicy[i] = new DanePracownika(imie,nazwisko,zarobki); 
		}
		scanner.close();
		
		System.out.println("Informacje o Pracownikach: ");
		for(DanePracownika pracownik : pracownicy) {
			pracownik.wyswietlInformacje();
			System.out.println("---------------------");
		}
	}	
}
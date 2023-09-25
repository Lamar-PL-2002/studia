import java.util.Random;

public class Loteria {

	public static void main(String[] args) {
		Losowanie Liczba1 = new Losowanie();
		Losowanie Liczba2 = new Losowanie(20);
		Losowanie Liczba3 = new Losowanie(0,10);
		
		System.out.println("Liczba 1 " + Liczba1.pobierzLiczbe());
		System.out.println("Liczba 2 " + Liczba2.pobierzLiczbe());
		System.out.println("Liczba 3 " + Liczba3.pobierzLiczbe());
	}

}

class Losowanie {
	private int liczbaLos;
	
	public Losowanie() {
		//Loswanie od 0 do 100, bez argumentu;
		Random rand = new Random();
		liczbaLos = rand.nextInt(101);
	}
	
	
	public Losowanie(int goraWartosc) {
		//Wartosc Agrumnentu;
		Random rand = new Random();
		liczbaLos = rand.nextInt(goraWartosc +1);
	}
	
	public Losowanie(int dolnaWartosc, int goraWartosc) {
		//wartosc 2 Argumentow;
		Random rand = new Random();
		liczbaLos = rand.nextInt(goraWartosc - dolnaWartosc + 1) + dolnaWartosc;
	}
	
	public int pobierzLiczbe() {
		return liczbaLos;
	}
	
}


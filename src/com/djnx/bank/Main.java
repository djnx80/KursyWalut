package com.djnx.bank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5700152220777523913L;
	private JButton bQuit, bPobierzDane, bWybierzWaluty, bPokazWybrane, bZapisz, bWyslij, bZmienPlik;
	private JLabel lDoKogo, lNazwaPliku;
	private JTextField tfDoKogo, tfNazwaPliku;
	private static String waluty[] = new String[40];
	private static String wybraneWaluty[] = new String[40];
	DOMParser zBanku;
	UsePDF myPDF;
	String mojaLista, mojaNazwaPliku;
	File plikDoZapisu;
	private JList<String> listaWalut;
	private int ileWybranychWalut;
	boolean czyPobrane, czyWybrane;

	public Main() {
		setSize(535, 210); // okienko
		setTitle("Kursy walut");
		setLayout(null);

		bPobierzDane = new JButton("Pobierz XML");
		bPobierzDane.setBounds(10, 130, 125, 30);
		bPobierzDane.addActionListener(this);
		add(bPobierzDane);
		bWybierzWaluty = new JButton("Wybierz waluty");
		bWybierzWaluty.setBounds(135, 130, 125, 30);
		bWybierzWaluty.addActionListener(this);
		add(bWybierzWaluty);
		bPokazWybrane = new JButton("Pokaz wybrane");
		bPokazWybrane.setBounds(260, 130, 125, 30);
		bPokazWybrane.addActionListener(this);
		add(bPokazWybrane);
		bQuit = new JButton("Wyjœcie");
		bQuit.setBounds(385, 130, 125, 30);
		bQuit.addActionListener(this);
		add(bQuit);

		bZmienPlik = new JButton("Wybierz...");
		bZmienPlik.setBounds(315, 80, 97, 20);
		bZmienPlik.addActionListener(this);
		add(bZmienPlik);
		bZapisz = new JButton("Zapisz");
		bZapisz.setBounds(413, 80, 97, 20);
		bZapisz.addActionListener(this);
		add(bZapisz);
		bWyslij = new JButton("Wyœlij E-mail");
		bWyslij.setBounds(315, 30, 195, 20);
		bWyslij.addActionListener(this);
		add(bWyslij);

		lDoKogo = new JLabel("Wyœlij mail na adres: ");
		lDoKogo.setBounds(10, 10, 200, 20);
		add(lDoKogo);

		lNazwaPliku = new JLabel("Zapisz plik pdf jako: ");
		lNazwaPliku.setBounds(10, 60, 200, 20);
		add(lNazwaPliku);
		tfDoKogo = new JTextField("przyk³ad@gmail.com");
		tfDoKogo.setBounds(10, 30, 300, 20);
		add(tfDoKogo);
		tfNazwaPliku = new JTextField("");
		tfNazwaPliku.setBounds(10, 80, 300, 20);
		add(tfNazwaPliku);

		mojaLista = "";
		czyPobrane = false;
		czyWybrane = false;
		mojaNazwaPliku = "przyk³ad.pdf";
		plikDoZapisu = null;
	}

	public static void main(String[] args) {
		Main okno = new Main();
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		okno.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bQuit) {
			dispose();
		} else if (source == bPobierzDane) {
			pobierzDane();
		} else if (source == bWybierzWaluty) {
			wybierzDane();
		} else if (source == bZapisz) {
			zapiszPlik();
		} else if (source == bPokazWybrane) {
			pokazWybrane();
		} else if (source == bZmienPlik) {
			wybierzPlikDoZapisu();
		} else if (source == bWyslij) {
			wyslijMail();
		}

	}

	private void wyslijMail() {
		// if (czyWybrane == false) {
		// JOptionPane.showMessageDialog(null, "Najpierw wybierz waluty.");
		// } else
		if (plikDoZapisu == null) {
			JOptionPane.showMessageDialog(null, "Najpierw wybierz za³¹cznik");
		} else {
			SendMailWithAtt sendMe = new SendMailWithAtt();
			sendMe.tryToSend(tfDoKogo.getText(), plikDoZapisu.getAbsolutePath());
		}
	}

	private void wybierzPlikDoZapisu() {

		JFileChooser saveFile = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF File", "pdf");
		saveFile.setFileFilter(filter);

		if (saveFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			plikDoZapisu = saveFile.getSelectedFile();
			mojaNazwaPliku = plikDoZapisu.getAbsolutePath();
			tfNazwaPliku.setText(mojaNazwaPliku);
			// nie daje automatycznego zapisywania, dziêki temu mo¿na wys³aæ
			// inne pliki
			// zapiszPlik();
		}

	}

	private void pobierzDane() {
		zBanku = new DOMParser();
		if (zBanku.getWaluty(waluty) > 0) {
			czyPobrane = true;
			wybierzDane();
		} else {
			JOptionPane.showMessageDialog(null, "Nie odczyta³em ¿adnej pozycji z XML");
		}
	}

	private void wybierzDane() {
		if (czyPobrane == false) {
			JOptionPane.showMessageDialog(null, "Musisz najpierw wczytaæ plik XML");
			return;
		}
		try {
			listaWalut = new JList<String>(waluty);
			JOptionPane.showMessageDialog(null, listaWalut, "Wybierz waluty z listy", JOptionPane.PLAIN_MESSAGE);
			mojaLista = Arrays.toString(listaWalut.getSelectedIndices());
			odczytajZListy(mojaLista);
			czyWybrane = true;
		} catch (Exception e) {
			System.out.println("Nie wybra³eœ ¿adnej waluty");
		}
	}

	public void odczytajZListy(String lista) {
		if (lista.equals("") == true) {
			JOptionPane.showMessageDialog(null, "Musisz najpierw wczytaæ plik XML");
			return;
		}
		System.out.println("lista=" + lista);
		String liczba = "";

		ileWybranychWalut = 0;

		for (int i = 0; i < lista.length(); i++) {
			String znak = lista.substring(i, i + 1);
			if (znak.equals("[")) {
				// startuj
			} else if (znak.equals(" ")) {
				// zignoruj spacje
			} else if (znak.equals(",")) {
				// nowa liczba
				wybraneWaluty[ileWybranychWalut] = waluty[Integer.parseInt(liczba)];
				ileWybranychWalut++;
				liczba = "";
			} else if (znak.equals("]")) {
				wybraneWaluty[ileWybranychWalut] = waluty[Integer.parseInt(liczba)];
				ileWybranychWalut++;
			} else {
				// pozosta³y same cyfry
				liczba = liczba + znak;
			}
		}
	}

	public void pokazWybrane() {
		if (ileWybranychWalut == 0) {
			JOptionPane.showMessageDialog(null, "Musisz najpierw wybraæ waluty");
			return;
		}

		String mojewalutyTemp = "";

		for (int i = 0; i < ileWybranychWalut; i++) {
			mojewalutyTemp = mojewalutyTemp + "Kod waluty: " + wybraneWaluty[i] + "  Kurs: "
					+ zBanku.getWynik(wybraneWaluty[i]) + "\n";
		}
		JOptionPane.showMessageDialog(null, mojewalutyTemp);
	}

	public void zapiszPlik() {
		myPDF = new UsePDF(plikDoZapisu);
		String linia = "";

		myPDF.addLine("Raport z kursami walut");
		myPDF.addLine("------------------------------");
		// pobierz aktualna datê
		Date aktData = new Date();
		SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
		String dataString = dataFormat.format(aktData);
		myPDF.addLine("Kursy walut z dnia:  " + dataString);

		// Pobranie aktualnego czasu wykorzystuj¹c Calendar
		Calendar calendar = Calendar.getInstance();
		linia = "Aktualna godzina:   " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
		myPDF.addLine(linia);
		myPDF.addLine(" ");

		for (int i = 0; i < ileWybranychWalut; i++) {
			linia = "Kod waluty: " + wybraneWaluty[i] + "     Kurs: " + zBanku.getWynik(wybraneWaluty[i]);
			myPDF.addLine(linia);
		}

		myPDF.closePDF();

	}
}

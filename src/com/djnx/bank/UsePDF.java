package com.djnx.bank;

import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class UsePDF {
	private Document dokument;

	public UsePDF(File plik) {
		try {
			dokument = new Document();
			@SuppressWarnings("unused") // wy³¹cza warning o nieu¿ywanej zmiennej
			String sc = "plicz.pdf";
			PdfWriter pdfWriter = PdfWriter.getInstance(dokument, new FileOutputStream(plik));
			dokument.open();
			JOptionPane.showMessageDialog(null, "Otworzylem plik pdf do zapisu\n" + plik.getAbsolutePath());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Nie uda³o siê utworzyæ pliku pdf,\nlub odmowa dostêpu do pliku.");
			System.out.println("plik: " + plik);
		}
	}

	public void closePDF() {
		dokument.close();

	}

	public void addLine(String linia) {
		try {
			// JOptionPane.showMessageDialog(null, "zapisuje linie");
			Paragraph paragraph = new Paragraph();
			paragraph.add(linia);
			dokument.add(paragraph);
		} catch (Exception e) {

		}
	}

}

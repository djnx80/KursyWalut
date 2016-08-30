package com.djnx.bank;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DOMParser {

	private static Node korzen;
	private static Element element;
	private static NodeList listaElementow;

	public DOMParser() {
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			URL url = new URL("http://www.nbp.pl/kursy/xml/LastA.xml");
			Document doc = dBuilder.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			listaElementow = doc.getElementsByTagName("pozycja");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie uda³o siê otworzyæ pliku XML z\nhttp://www.nbp.pl/kursy/xml/");
		}
	}

	public int getWaluty(String[] waluty) {
		int i;
		for (i = 0; i < listaElementow.getLength(); i++) {
			korzen = listaElementow.item(i);

			if (korzen.getNodeType() == Node.ELEMENT_NODE) {
				element = (Element) korzen;
				String waluta = element.getElementsByTagName("kod_waluty").item(0).getTextContent();
				// String kurs
				// =element.getElementsByTagName("kurs_sredni").item(0).getTextContent();
				waluty[i] = waluta;
			}
			if (i > 40) {
				return i;
			}
		}
		return i;
	}

	public String getWynik(String typWaluty) {
		for (int i = 0; i < listaElementow.getLength(); i++) {
			korzen = listaElementow.item(i);

			if (korzen.getNodeType() == Node.ELEMENT_NODE) {
				element = (Element) korzen;
				String waluta = element.getElementsByTagName("kod_waluty").item(0).getTextContent();
				String kurs = element.getElementsByTagName("kurs_sredni").item(0).getTextContent();
				if (waluta.equals(typWaluty)) {
					return kurs;
				}
			}
		}
		return null;
	}

}
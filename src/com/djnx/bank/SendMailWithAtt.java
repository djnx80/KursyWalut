package com.djnx.bank;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;

import java.util.Properties;

class SendMailWithAtt {

	private static final String HOST = "smtp.gmail.com";
	private static final int PORT = 465;
	private static final String LOGIN = "kontoprobnetestowe@gmail.com";
	private static final String HASLO = "test123test";
	private static final String TEMAT = "Raport z kursami walut";
	private static final String WIADOMOSC = "Witaj! W <i>za³¹czniku</i> znajdziesz <b>raport</b> w postaci pliku PDF.";

	public SendMailWithAtt() {

	}

	public void tryToSend(String doAdres, String zalacznik) {
		try {
			System.out.println("Próbujê wys³aæ maila. " + zalacznik);
			new SendMailWithAtt().send(doAdres, zalacznik);
		} catch (AddressException ae) {
			JOptionPane.showMessageDialog(null, "Z³y adres maila.");

		} catch (MessagingException e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Coœ posz³o nie tak z wys³aniem maila.\nByæ mo¿e brak za³¹cznika");
		}
	}

	void send(String adr, String zal) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.auth", "true");

		// pobierz sesjê i stwórz wiadomoœæ
		Session sesjaMail = Session.getDefaultInstance(props, null);
		MimeMessage wiadomosc = new MimeMessage(sesjaMail);
		wiadomosc.setSubject(TEMAT);
		MimeBodyPart cialoWiadomosci = new MimeBodyPart();
		cialoWiadomosci.setContent(WIADOMOSC, "text/html; charset=ISO-8859-2");

		// dodanie za³¹cznika
		MimeBodyPart zalacznikZrodlo = new MimeBodyPart();
		FileDataSource zalacznik = new FileDataSource(zal);
		zalacznikZrodlo.setDataHandler(new DataHandler(zalacznik));
		zalacznikZrodlo.setFileName(zalacznik.getName());

		// po³¹czenie wiadomoœci z za³¹cznikiem
		Multipart caloscWiadomosci = new MimeMultipart();
		caloscWiadomosci.addBodyPart(cialoWiadomosci);
		caloscWiadomosci.addBodyPart(zalacznikZrodlo);

		// ustaw i dodaj adresy
		wiadomosc.setContent(caloscWiadomosci);
		wiadomosc.addRecipient(Message.RecipientType.TO, new InternetAddress(adr));
		Transport transport = sesjaMail.getTransport();
		transport.connect(HOST, PORT, LOGIN, HASLO);

		// wyœlij
		transport.sendMessage(wiadomosc, wiadomosc.getRecipients(Message.RecipientType.TO));
		transport.close();
		JOptionPane.showMessageDialog(null, "Wys³ano maila z raportem.");
	}
}

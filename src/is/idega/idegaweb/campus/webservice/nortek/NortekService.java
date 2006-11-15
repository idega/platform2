package is.idega.idegaweb.campus.webservice.nortek;

import java.util.Date;

public interface NortekService {
	public boolean isCardValid(String cardSerialNumber);
	
	public boolean banCard(String cardSerialNumber);
	
	public boolean addAmountToCard(String cardSerialNumber, Date timestamp, double amount, String terminal);
}

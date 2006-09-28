package se.idega.idegaweb.commune.adulteducation.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface AdultEducationBusinessHome extends IBOHome {
	public AdultEducationBusiness create() throws CreateException, RemoteException;
}
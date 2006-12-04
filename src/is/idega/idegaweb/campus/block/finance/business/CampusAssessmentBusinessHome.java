package is.idega.idegaweb.campus.block.finance.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CampusAssessmentBusinessHome extends IBOHome {
	public CampusAssessmentBusiness create() throws CreateException, RemoteException;
}
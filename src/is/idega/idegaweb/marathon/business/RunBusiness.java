package is.idega.idegaweb.marathon.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;


public interface RunBusiness extends com.idega.business.IBOService{
    public int saveUser(String name,String ssn,IWTimestamp dateOfBirth,String gender,String address,String postal,String city,String country,String tel,String mobile,String email) throws RemoteException;
    public void saveRun(int userID,String run,String distance,String year,String nationality,String tshirt,String chipNumber,String groupName,String bestTime,String goalTime, Locale locale) throws RemoteException;
    public void createNewGroupYear(IWContext iwc, Group run, String year) throws RemoteException;
    public Collection getRuns() throws RemoteException;
    public Collection getYears(Group run) throws RemoteException;
    public Map getYearsMap(Group run) throws RemoteException;
    public List getDistancesMap(Group run, String year) throws RemoteException;
    public Collection getCountries() throws RemoteException;
  	public boolean isRegisteredInRun(int runID, String personalID) throws RemoteException;
}
package is.idega.idegaweb.marathon.business;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;


public interface RunBusiness extends com.idega.business.IBOService{
    public int saveUser(String name,String ssn,IWTimestamp dateOfBirth,String gender,String address,String postal,String city,String country,String tel,String mobile,String email);
    public void saveRun(int userID,String run,String distance,String year,String nationality,String tshirt,String chipNumber,String groupName,String bestTime,String goalTime, Locale locale);
    public void createNewGroupYear(IWContext iwc, Group run, String year);
    public Collection getRuns();
    public Collection getYears(Group run);
    public Map getYearsMap(Group run);
    public List getDistancesMap(Group run, String year);
    public Collection getCountries();
}

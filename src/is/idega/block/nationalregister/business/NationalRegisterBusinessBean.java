package is.idega.block.nationalregister.business;

import is.idega.block.nationalregister.data.NationalRegister;
import is.idega.block.nationalregister.data.NationalRegisterHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.core.data.Country;
import com.idega.core.data.CountryHome;
import com.idega.core.data.PostalCode;
import com.idega.core.data.PostalCodeHome;
import com.idega.data.IDOLookup;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class NationalRegisterBusinessBean extends IBOServiceBean implements NationalRegisterBusiness {
	public NationalRegister getEntryBySSN(String ssn) {
		try {
			Collection c = getNationalRegisterHome().findAllBySSN(ssn);

			if (c != null) {
				Iterator it = c.iterator();
				if (it.hasNext()) {
					return (NationalRegister) it.next();
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}

		return null;
	}

	public boolean updateEntry(
		String symbol,
		String oldId,
		String ssn,
		String familyId,
		String name,
		String commune,
		String street,
		String building,
		String floor,
		String sex,
		String maritialStatus,
		String empty,
		String prohibitMarking,
		String nationality,
		String placeOfBirth,
		String spouseSSN,
		String fate,
		String parish,
		String po,
		String address) {
			
			
		try {
			UserBusiness userBiz = (UserBusiness) getServiceInstance(UserBusiness.class);	
			NationalRegister reg = getEntryBySSN(ssn);
			if (reg == null) {
				reg = getNationalRegisterHome().create();
			}
			reg.setAddress(address);
			reg.setBuilding(building);
			reg.setCommune(commune);
			reg.setFamilyId(familyId);
			reg.setFate(fate);
			reg.setFloor(floor);
			reg.setMaritalStatus(maritialStatus);
			reg.setName(name);
			reg.setNationality(nationality);
			reg.setOldId(oldId);
			reg.setParish(parish);
			reg.setPlaceOfBirth(placeOfBirth);
			reg.setPO(po);
			reg.setProhibitMarking(prohibitMarking);
			reg.setSex(sex);
			reg.setSpouseSSN(spouseSSN);
			reg.setSSN(ssn);
			reg.setStreet(street);
			reg.setSymbol(symbol);

			reg.store();
			
			IWTimestamp t = new IWTimestamp();
			
			String day = ssn.substring(0,2);
			String month = ssn.substring(2,4);
			String year = ssn.substring(4,6);
				
			int iDay = Integer.parseInt(day);
			int iMonth = Integer.parseInt(month);
			int iYear = Integer.parseInt(year);
			if (ssn.substring(9).equals("9"))
				iYear += 1900;
			else if (ssn.substring(9).equals("0"))
				iYear += 2000;
			else if (ssn.substring(9).equals("8"))
				iYear += 1800;
			t.setHour(0);
			t.setMinute(0);
			t.setSecond(0);
			t.setMilliSecond(0);
			t.setDay(iDay);
			t.setMonth(iMonth);
			t.setYear(iYear);
			
			GenderHome home = (GenderHome) getIDOHome(Gender.class);
			
			Gender gender = null;
			if (sex.equals("1") || sex.equals("3"))
				gender = home.getMaleGender();
			else
				gender = home.getFemaleGender();

			User user = userBiz.createUserByPersonalIDIfDoesNotExist(name,ssn,gender,t);

			Country country = ((CountryHome)getIDOHome(Country.class)).findByIsoAbbreviation("IS");
			
			PostalCode poCode = null;
			if (po != null && !po.trim().equals("")) {
				try {				
			  	poCode = ((PostalCodeHome)getIDOHome(PostalCode.class)).findByPostalCodeAndCountryId(po,((Integer)country.getPrimaryKey()).intValue());
				}
				catch(FinderException e) {
					poCode = null;
				}
			}

			Integer id = null;
			if (poCode != null)
				id = (Integer)poCode.getPrimaryKey();

			userBiz.updateUsersMainAddressOrCreateIfDoesNotExist((Integer) user.getPrimaryKey(), address, id, null, null, null, null);
			userBiz.updateUsersCoAddressOrCreateIfDoesNotExist((Integer) user.getPrimaryKey(), address, id, null, null, null, null);
		}
		catch (CreateException e) {
			e.printStackTrace();
			return false;
		}
		catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
		catch (FinderException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	protected NationalRegisterHome getNationalRegisterHome() {
		NationalRegisterHome home = null;
		try {
			home = (NationalRegisterHome) IDOLookup.getHome(NationalRegister.class);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return home;
	}
}
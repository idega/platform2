package is.idega.idegaweb.member.isi.block.accounting.webservice.general.business;

import is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.AddressInfo;
import is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.UserInfo;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

public class GeneralAccountingServiceBusinessBean extends IBOServiceBean
		implements GeneralAccountingServiceBusiness {

	public UserInfo getUserInfo(String personalID) {
		UserInfo userInfo = new UserInfo();
		User user = null;
		try {
			try {
				user = getUserBusiness().getUser(personalID);
			} catch (FinderException ex) {
				userInfo.setValid(false);
				return userInfo;
			}
			userInfo.setSocialsecurity(personalID);
			setNames(user, userInfo);
			setAddress(user, userInfo);
			userInfo.setValid(true);
			return userInfo;
		} catch (RemoteException ex) {
			userInfo.setValid(false);
			userInfo.setError("error");
			return userInfo;
		}
	}

	private void setNames(User user, UserInfo userInfo) {
		String firstName = user.getFirstName();
		userInfo.setFirstName(firstName);

		String middleName = user.getMiddleName();
		userInfo.setMiddleName(middleName);

		String lastName = user.getLastName();
		userInfo.setLastName(lastName);
	}

	private void setAddress(User user, UserInfo userInfo)
			throws RemoteException {
		Address mainAddress = getUserBusiness().getUsersMainAddress(user);
		AddressInfo mainAddressInfo = getAdressInfo(mainAddress);
		userInfo.setAddress(mainAddressInfo);
	}

	private AddressInfo getAdressInfo(Address address) {
		AddressInfo addressInfo = new AddressInfo();
		if (address == null) {
			return null;
		}

		String streetName = address.getStreetName();
		addressInfo.setStreetName(streetName);

		String streetNumber = address.getStreetNumber();
		addressInfo.setStreetNumber(streetNumber);

		PostalCode postalCode = address.getPostalCode();
		if (postalCode != null) {
			String postalCodeString = postalCode.getPostalCode();
			addressInfo.setPostalcode(postalCodeString);
		}

		String city = address.getCity();
		addressInfo.setCity(city);

		Country country = address.getCountry();
		if (country != null) {
			String countryName = country.getName();
			addressInfo.setCountry(countryName);
		}

		return addressInfo;
	}

	private UserBusiness getUserBusiness() throws IBOLookupException {
		return (UserBusiness) IBOLookup
						.getServiceInstance(getIWApplicationContext(),
								UserBusiness.class);
	}
}
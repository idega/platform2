package se.idega.idegaweb.commune.printing.business;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.HashMap;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.lowagie.text.ElementTags;
import com.lowagie.text.xml.XmlPeer;

/**
 * The <CODE>Tags</CODE> -class maps several XHTML-tags to iText-objects.
 */

public class CommuneUserTagMap extends HashMap {

	/**
	 * Constructs an HtmlTagMap.
	 */

	public CommuneUserTagMap(IWApplicationContext iwac, User user) {
		super();
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, iwac.getApplicationSettings().getDefaultLocale());
		UserBusiness ub = getUserBusiness(iwac);

		XmlPeer peer = new XmlPeer(ElementTags.ITEXT, CommuneUserTags.USERLETTER);
		put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.NAME);
		peer.setContent(user.getName());
		put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.FULLNAME);
		peer.setContent(user.getName());
		put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.FIRSTNAME);
		peer.setContent(user.getFirstName());
		put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.MIDDLENAME);
		peer.setContent(user.getMiddleName());
		put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.LASTNAME);
		peer.setContent(user.getLastName());
		put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.PERSONALID);
		peer.setContent(user.getPersonalID());
		put(peer.getAlias(), peer);

		if (user.getDateOfBirth() != null) {
			peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.BIRTHDATE);
			peer.setContent(df.format(user.getDateOfBirth()));
			put(peer.getAlias(), peer);
		}

		if (ub != null) {
			try {
				peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.EMAIL);
				peer.setContent(ub.getUsersMainEmail(user).getEmailAddress());
				put(peer.getAlias(), peer);

				Address address = getUserMailReceiveAddress(ub, user);

				peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.STREETADDRESS);
				peer.setContent(address.getStreetAddress());
				put(peer.getAlias(), peer);

				peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.POSTALADDRESS);
				peer.setContent(address.getPostalAddress());
				put(peer.getAlias(), peer);

				peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.ADDRESSCOUNTRY);
				peer.setContent(address.getCountry().getName());
				put(peer.getAlias(), peer);

				peer = new XmlPeer(ElementTags.CHUNK, CommuneUserTags.PHONE);
				peer.setContent(ub.getUsersHomePhone(user).getNumber());
				put(peer.getAlias(), peer);
			}
			catch (Exception ex) {
			}

		}

	}

	private Address getUserMailReceiveAddress(UserBusiness ub, User user) throws RemoteException {
		int userID = ((Integer) user.getPrimaryKey()).intValue();
		Address addr = ub.getUserAddressByAddressType(userID, ub.getAddressHome().getAddressType2());
		if (addr == null) {
			addr = ub.getUsersMainAddress(userID);
		}
		else if (addr.getStreetName() == null) {
			addr = ub.getUsersMainAddress(userID);
		}
		return addr;
	}

	private CommuneUserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwac, CommuneUserBusiness.class);
		}
		catch (RemoteException ex) {
		}
		return null;
	}

}
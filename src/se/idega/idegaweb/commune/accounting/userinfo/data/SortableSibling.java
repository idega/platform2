package se.idega.idegaweb.commune.accounting.userinfo.data;

import com.idega.user.data.User;


/**
 * Is used to set sibling orders by age to children in childcare.
 * 
 * @author Joakim
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusinessBean#getSiblingOrder(ChildCareContract)
 */
public class SortableSibling implements Comparable{
	private User sibling;

	public SortableSibling(){
	}
	
	public SortableSibling(User u){
		sibling = u;
	}	
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) throws DateOfBirthMissingException {
		SortableSibling other = (SortableSibling)o;
		if(null==sibling.getDateOfBirth() || null==other.getSibling().getDateOfBirth()){
			throw new DateOfBirthMissingException("Date of birth missing");
		}
		if(sibling.getDateOfBirth().after(other.getSibling().getDateOfBirth())){
			return -1;
		} else if(sibling.getDateOfBirth().before(other.getSibling().getDateOfBirth())){
			return 1;
		}

		// since date of birth is equal, compare ssn and return 0 if equal
		return sibling.getPersonalID ().compareTo (other.getSibling ().getPersonalID ());
	}

	public User getSibling() {
		return sibling;
	}

	public void setSibling(User user) {
		sibling = user;
	}
}

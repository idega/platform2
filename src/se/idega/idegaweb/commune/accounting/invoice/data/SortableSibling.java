package se.idega.idegaweb.commune.accounting.invoice.data;

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
	public int compareTo(Object o) {
		SortableSibling other = (SortableSibling)o;
		if(sibling.getDateOfBirth().after(other.getSibling().getDateOfBirth())){
			return -1;
		} else if(sibling.getDateOfBirth().before(other.getSibling().getDateOfBirth())){
			return 1;
		}
		return 0;
	}

	public User getSibling() {
		return sibling;
	}

	public void setSibling(User user) {
		sibling = user;
	}
}

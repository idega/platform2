package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.user.data.User;


/**
 * @author Joakim
 *
 */
public class SortableSibling implements Comparable{
	private User sibling;

	public SortableSibling(){
	}
	
	public SortableSibling(User u){
		sibling = u;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		User other = (User)o;
		if(sibling.getDateOfBirth().after(other.getDateOfBirth())){
			return 1;
		} else if(sibling.getDateOfBirth().before(other.getDateOfBirth())){
			return -1;
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

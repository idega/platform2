package is.idega.idegaweb.travel.block.search.business;

import java.util.Collection;

import com.idega.business.IBOSessionBean;

/**
 * @author gimmi
 */
public class ServiceSearchSessionBean extends IBOSessionBean implements ServiceSearchSession {

	private int SEARCH_STATE;
	private Collection products = null;
	private Collection bookings = null;
	private Exception e = null;
	
	public Collection getProducts() {
		return products;
	}
	
	public void setProducts(Collection coll) {
		this.products = coll;
	}
	
	public void setState(int state) {
		this.SEARCH_STATE = state;
	}

	public int getState() {
		return this.SEARCH_STATE;
	}
	
	public void setBookingsSavedFromBasket(Collection bookings) {
		this.bookings = bookings;
	}
	
	public Collection getBookingsSavedFromBasket() {
		Collection coll = bookings;
		bookings = null;
		return coll;
	}
	
	/**
	 * Store an exception to catch later, use throwException to throw it
	 * @param e The Exception to throw
	 */
	public void setException(Exception e) {
		this.e = e;
	}
	
	/**
	 * Throws the stored exception, if it is availble
	 * @throws Exception
	 */
	public void throwException() throws Exception {
		if (e != null) {
			Exception ex = e;
			e = null;
			throw ex;
		}
	}
	
}

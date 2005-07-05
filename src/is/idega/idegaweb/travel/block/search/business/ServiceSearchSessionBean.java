package is.idega.idegaweb.travel.block.search.business;

import java.util.Collection;

import com.idega.business.IBOSessionBean;
import com.idega.idegaweb.IWResourceBundle;

/**
 * @author gimmi
 */
public class ServiceSearchSessionBean extends IBOSessionBean implements ServiceSearchSession {

	private int SEARCH_STATE;
	private Collection products = null;
	
	private Collection bookings = null;
	private Exception e = null;
	
	private boolean addToBasketSuccess = false;
	private String addToBasketErrorKey = null;
	private String addToBasketErrorIfNull = null;
	
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
	
	/**
	 * Set wether the item was successfully added to the basket
	 * @param success
	 */
	public void setAddToBasketSuccess(boolean success) {
		this.addToBasketSuccess = success;
	}
	
	/**
	 * Get if the Item was successfully added to the basket
	 * @return
	 */
	public boolean getAddToBasketSuccess() {
		boolean b = addToBasketSuccess;
		addToBasketSuccess = false;
		return b;
	}
	
	public void setAddToBasketErrorLocalizedKey(String key, String ifNull) {
		this.addToBasketErrorKey = key;
		this.addToBasketErrorIfNull = ifNull;
	}
	
	public String getAddToBasketError(IWResourceBundle iwrb) {
		if (addToBasketErrorKey != null) {
			String tmp = iwrb.getLocalizedString(addToBasketErrorKey, addToBasketErrorIfNull);
			this.addToBasketErrorKey = null;
			this.addToBasketErrorIfNull = null;
			return tmp;
		} else if (addToBasketErrorIfNull != null) {
			return addToBasketErrorIfNull;
		}
		return null;
	}
	
}

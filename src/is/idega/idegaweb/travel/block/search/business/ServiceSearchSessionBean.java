package is.idega.idegaweb.travel.block.search.business;

import java.util.Collection;

import com.idega.business.IBOSessionBean;

/**
 * @author gimmi
 */
public class ServiceSearchSessionBean extends IBOSessionBean implements ServiceSearchSession {

	private int SEARCH_STATE;
	private Collection products = null;
		
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
	
}

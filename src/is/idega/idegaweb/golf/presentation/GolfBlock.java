/*
 * Created on 5.10.2003
 */
package is.idega.idegaweb.golf.presentation;

import com.idega.presentation.Block;

/**
 * @author laddi
 */
public class GolfBlock extends Block {

	protected final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
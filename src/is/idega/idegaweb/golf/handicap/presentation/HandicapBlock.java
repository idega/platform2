/*
 * Created on 5.10.2003
 */
package is.idega.idegaweb.golf.handicap.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.presentation.IWContext;

import is.idega.idegaweb.golf.handicap.data.Strokes;
import is.idega.idegaweb.golf.presentation.GolfBlock;

/**
 * @author laddi
 */
public class HandicapBlock extends GolfBlock {

	protected final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf.handicap";

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
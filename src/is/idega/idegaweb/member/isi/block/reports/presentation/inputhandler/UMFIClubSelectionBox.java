/*
 * Created on Nov 13, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UMFIClubSelectionBox extends GroupSelectionBox {
	/**
	 * Creates a new <code>RegionalUnionSelectionBox</code> with all regional unions.
	 * @param name	The name of the <code>RegionalUnionSelectionBox</code>
	 */
	public UMFIClubSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_CLUB, metaDataMap);
	}
	
	/**
	 * 
	 */
	public UMFIClubSelectionBox() {
		super("", IWMemberConstants.GROUP_TYPE_CLUB, metaDataMap);
	}
	
	static private Map metaDataMap = getMetaDataMap();
	
	static private Map getMetaDataMap() {
		Map metaDataMap = new Hashtable();
		metaDataMap.put(IWMemberConstants.META_DATA_CLUB_IN_UMFI, "true");
		return metaDataMap;
	}
}

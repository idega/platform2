/*
 * Created on Dec 16, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ReportGeneratorRegionalUnionSelectionBox extends GroupSelectionBox {
	/**
	 * Creates a new <code>LeagueSelectionBox</code> with all leagues.
	 * @param name	The name of the <code>LeagueSelectionBox</code>
	 */
	public ReportGeneratorRegionalUnionSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
		setResultAsString();
	}
	
	/**
	 * 
	 */
	public ReportGeneratorRegionalUnionSelectionBox() {
		super();
		setGroupType(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
		setName(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
		setResultAsString();
	}
}

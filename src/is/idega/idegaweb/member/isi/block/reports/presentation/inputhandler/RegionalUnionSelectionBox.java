package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.business.InputHandler;
 
/**
 * A presentation object for dynamic reports to choose Leagues from a selectionbox
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class RegionalUnionSelectionBox extends GroupSelectionBox implements InputHandler{

	
	/**
	 * Creates a new <code>RegionalUnionSelectionBox</code> with all regional unions.
	 * @param name	The name of the <code>RegionalUnionSelectionBox</code>
	 */
	public RegionalUnionSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
	}
	
	/**
	 * 
	 */
	public RegionalUnionSelectionBox() {
		super();
		setGroupType(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
		setName(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
	}
	

	

}

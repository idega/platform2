package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.business.InputHandler;
 
/**
 * A presentation object for dynamic reports to choose a club from a selectionbox
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class ClubSelectionBox extends GroupSelectionBox implements InputHandler{

	/**
	 * Creates a new <code>ClubSelectionBox</code> with all clubs.
	 * @param name	The name of the <code>ClubSelectionBox</code>
	 */
	public ClubSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_CLUB);
	}
	
	/**
	 * 
	 */
	public ClubSelectionBox() {
		super();
		setGroupType(IWMemberConstants.GROUP_TYPE_CLUB);
		setName(IWMemberConstants.GROUP_TYPE_CLUB);
	}
	
}

package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.business.InputHandler;
 
/**
 * A presentation object for dynamic reports to choose Leagues from a selectionbox
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class LeagueSelectionBox extends GroupSelectionBox implements InputHandler{

	/**
	 * Creates a new <code>LeagueSelectionBox</code> with all leagues.
	 * @param name	The name of the <code>LeagueSelectionBox</code>
	 */
	public LeagueSelectionBox(String name) {
		super(name,IWMemberConstants.GROUP_TYPE_LEAGUE);
	}
	
	/**
	 * 
	 */
	public LeagueSelectionBox() {
		super();
		setGroupType(IWMemberConstants.GROUP_TYPE_LEAGUE);
		setName(IWMemberConstants.GROUP_TYPE_LEAGUE);
	}
	
}

/*
 * Created on 17.5.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;

import javax.ejb.FinderException;

import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.text.Link;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;


/**
 * Title: ClubTournamentLink
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class ClubTournamentLink extends Link {
	
	private ICPage _tournamentPage=null;
	private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";
	
	/**
	 * 
	 */
	public ClubTournamentLink() {
		super();
	}
	
	
	public void main(IWContext iwc) throws Exception {
		Page page = this.getParentPage();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.setText(iwrb.getLocalizedString("tournaments","Tournaments"));
		if(page != null) {
			int rootPageID = page.getDPTRootPageID();
			if(rootPageID != -1) {
				try {
					Group gr = ((GroupHome)IDOLookup.getHome(Group.class)).findByHomePageID(rootPageID);
					Union union = ((UnionHome)IDOLookup.getHome(Union.class)).findUnionByIWMemberSystemGroup(gr);

					if(_tournamentPage!=null) {
						this.setPage(_tournamentPage);
					}
					this.addParameter(TournamentList.PRM_UNION_ID,union.getPrimaryKey().toString());

				} catch (FinderException e) {
					// No Group found
					System.out.println("["+this.getClassName()+"]: no Group has this page("+rootPageID+") as homepage");
				}
			} else {
//
			}
		}
	}
	
	public void setTournamentListPage(ICPage tournamentList) {
		_tournamentPage = tournamentList;
	}
	
	public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
	}

	
}

package is.idega.idegaweb.golf.tournament.presentation;

import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.tournament.business.TournamentController;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class TournamentLists extends TournamentBlock{

	private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";

	private String PARAMETER_UNION_ID = "tl_pui";
	private String PARAMETER_YEAR = "tl_y";
	
	private IWResourceBundle iwrb;
	Image flag;
	Union union;
	int iYear;

	public TournamentLists() {
		super();	
	}

	public void mainAdmin() throws SQLException {
		Table table = new Table();
		
		Union[] unions = (Union[]) ((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("abbrevation");
		
		int row = 1;
		table.add(getHeader(iwrb.getLocalizedString("tournament.tournament","Tournament")), 1, row);
		Link link;
		for (int i = 0; i < unions.length; i++) {
			++row;
			link = new Link(getText(unions[i].getAbbrevation()+" "+unions[i].getName()));
			link.addParameter(PARAMETER_UNION_ID, unions[i].getID());
			table.add(link, 1, row);
		}
		add(table);
	}
	
	public void clubAdmin() throws SQLException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_UNION_ID);
		Table table = new Table();
		form.add(table);
		
		Tournament[] tournaments = (Tournament[]) ((Tournament) IDOLookup.instanciateEntity(Tournament.class)).findAll("select * from tournament where union_id = " + union.getID() + " and START_TIME < '" + iYear + "-12-31' and START_TIME > '" + iYear + "-01-01' order by START_TIME");

		DropdownMenu year = new DropdownMenu(PARAMETER_YEAR);
		for (int i = 2001 ; i <= IWTimestamp.RightNow().getYear(); i++) {
			year.addMenuElement(Integer.toString(i), Integer.toString(i));
		}
		year.setSelectedElement(Integer.toString(iYear));
		year.setToSubmit();
		
		int row = 1;
		int totalCount = 0;
		String[] sqlRes;
		table.mergeCells(1, row, 3, row);
		table.setAlignment(1, row, "right"); 
		table.add(year, 1, row);		
		
		++row;
		table.add(getHeader(iwrb.getLocalizedString("tournament.tournament", "Tournament")), 2, row);
		table.add(getHeader(iwrb.getLocalizedString("tournament.count","Count")), 3, row);
		for (int i = 0; i < tournaments.length; i++) {
			++row;
			if (tournaments[i].isTournamentFinished()) {
				table.add(flag, 1, row);	
			}
			table.add(getText(tournaments[i].getName()), 2, row);
			try {
				sqlRes = SimpleQuerier.executeStringQuery("select count(*) from tournament_member where tournament_id = "+tournaments[i].getID());
				if (sqlRes != null && sqlRes.length > 0) {	
					table.add(getText(sqlRes[0]), 3, row);
					totalCount += Integer.parseInt(sqlRes[0]);	
				} else {
					table.add(getText("0"), 3, row);	
				}
			} catch (Exception e) {
				table.add(getText("-err-"), 3, row);	
				e.printStackTrace();
			}
		}
		++row;
		HorizontalRule hr = new HorizontalRule("100%");
		hr.setNoShade(true);
		table.mergeCells(1, row, 3, row);
		table.add(hr, 1, row);
		
		++row;
		table.add(getHeader(iwrb.getLocalizedString("tournament.total","Total")), 2, row);
		table.add(getHeader(Integer.toString(totalCount)), 3, row);
		table.setColumnAlignment(3, "right");
		add(form);
	}

//	private Text getText(String content) {
//		Text text = new Text(content);
//		text.setFontSize(Text.FONT_SIZE_10_HTML_2);
//		return text;
//	}
//	
//	private Text getHeader(String content) {
//		Text text = getText(content);
//		text.setBold(true);
//		return text;	
//	}

	private void init(IWContext modinfo) throws SQLException {
		iwrb = getResourceBundle(modinfo);
		Member member = (Member) modinfo.getSession().getAttribute("member_login");
		if (member != null) {
			try {
				union = member.getMainUnion();	
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}
		}	
		String sYear = modinfo.getParameter(PARAMETER_YEAR);
		if (sYear == null) {
			iYear = IWTimestamp.RightNow().getYear();
		}else {
			iYear = Integer.parseInt(sYear);
		}
		flag = getBundle(modinfo).getImage("shared/flag.gif");
	}

	public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
	}
	
	private void error(Exception e) {
		add("Villa kom upp ("+e.getMessage()+")");
	}

	public void main(IWContext modinfo) {
		try {
			init(modinfo);
			
			if (union != null) {
				if (union.getID() == 3) {
					String sUnionId = modinfo.getParameter(PARAMETER_UNION_ID);
					if (sUnionId == null) {
						mainAdmin();
					}else {
						try {
							union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(Integer.parseInt(sUnionId));
						}
						catch (FinderException fe) {
							throw new SQLException(fe.getMessage());
						}
						clubAdmin();
						BackButton bb = new BackButton();
						add(bb);
					}
				}else if (union.getID() > 0) { 	
					clubAdmin();
				}				
			}
			
		} catch (SQLException e) {
			error(e);
		}
	}

	protected boolean tournamentMustBeSet() {
		return false;
	}
}

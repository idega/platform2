package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.GolfField;
import is.idega.idegaweb.golf.SqlTime;
import is.idega.idegaweb.golf.TableInfo;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.startingtime.business.TeeTimeBusinessBean;
import is.idega.idegaweb.golf.startingtime.business.TeetimeSearchResult;
import is.idega.idegaweb.golf.startingtime.data.TeeTime;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;


/**
 * Title: TeeTimeSearch
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */

public class TeeTimeSearch extends GolfBlock {
	
	/**
	 * Time interval between options when searching for teetime.
	 */
	private static final int TEE_TIME_SEARCH_INTERVALE = 60;

	private TeeTimeBusinessBean service = new TeeTimeBusinessBean();

	private String _blockWidth = Table.HUNDRED_PERCENT;
	
	private int _numberOfResultColumns = 10;
	
	private ICPage _teeTimeTablePage = null;
	private ICPage _teeTimesPage = null;
	
	private int width = 160;
	
	private boolean lockedAsWapLayout = false;
	private ICPage backPage = null;

	public void main(IWContext modinfo) throws Exception {
		IWTimestamp funcDate = new IWTimestamp();

		try {
			removeUnionIdSessionAttribute(modinfo);

			if (modinfo.getSessionAttribute("side_num") == null && modinfo.getParameter("side") == null)
				modinfo.setSessionAttribute("side_num", "0");
			else if (modinfo.getParameter("side") != null) modinfo.setSessionAttribute("side_num", modinfo.getParameter("side"));

			if (modinfo.getSessionAttribute("when") == null && modinfo.getParameter("hvenaer") == null)
				modinfo.setSessionAttribute("when", "0");
			else if (modinfo.getParameter("hvenaer") != null) modinfo.setSessionAttribute("when", modinfo.getParameter("hvenaer"));

			if (modinfo.getSessionAttribute("field_id") == null && modinfo.getParameter("hvar") == null)
				modinfo.setSessionAttribute("field_id", "1");
			else if (modinfo.getParameter("hvar") != null) modinfo.setSessionAttribute("field_id", modinfo.getParameter("hvar"));

			if (modinfo.getSessionAttribute("date") == null && modinfo.getRequest().getParameter("day") == null)
				modinfo.setSessionAttribute("date", new IWTimestamp().toSQLDateString());
			else if (modinfo.getRequest().getParameter("day") != null) modinfo.setSessionAttribute("date", modinfo.getRequest().getParameter("day"));


		} catch (Exception E) {
			E.printStackTrace();
		}

		Form myForm = new Form();
		GolfField myField = new GolfField();
		GolfField Today = new GolfField();
		TableInfo myTableInfo = new TableInfo();



		Vector Groups = new Vector();

//		boolean search = true;
//		Table startTable = new Table(1, 2);
//		startTable.setHeight(1, "15");
//		startTable.setCellpadding(0);
//		startTable.setCellspacing(0);
//		startTable.add(Text.emptyString(), 1, 1);
//		if (search) {
//			startTable.add(getSearchForm(modinfo, funcDate), 1, 2);
//		}
//		add(startTable);
		
		boolean results = (modinfo.getParameter("results") != null);
		
		boolean result_part = (modinfo.getParameter("part")!=null);
		
		if(result_part){
			addWapResults(modinfo, null, null, null);
		} else {

		
			if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
				if(!results) {
					add(getWapSearchForm(modinfo, funcDate));
				}
			} else {
				add(getSearchForm(modinfo, funcDate));
			}
			
			if (results) {
				if (modinfo.getParameterValues("fields") != null && modinfo.getParameter("fjoldi") != null && !modinfo.getParameter("fjoldi").equals("")) {
					if (numericString(modinfo.getParameter("fjoldi"))) {
						String[] myParameters = modinfo.getParameterValues("fields");
						int fields;
						for (int i = 0; i < myParameters.length; i++) {
							fields = Integer.parseInt(myParameters[i]);
							myField = getFieldInfo(fields, modinfo.getParameter("date").toString());
							Today = getFieldInfo(fields, funcDate.toSQLDateString());
							try {
								Groups = search(funcDate, modinfo, myField, Today, Integer.parseInt(modinfo.getParameter("fjoldi").toString()), modinfo.getParameter("date").toString(), modinfo.getParameter("ftime").toString(), modinfo.getParameter("ltime").toString(), 0, 36);
								if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
									addWapResults(modinfo, Groups, myField, modinfo.getParameter("date").toString());
								} else {
									this.add(getResultTable(modinfo, Groups, myField, modinfo.getParameter("date").toString(), _numberOfResultColumns));
								}
															
							} catch (Exception E) {
								E.printStackTrace();
								if (E.getMessage().equals("Error1")) {
									Table Error1 = new Table(1, 1);
									Error1.setWidth(_blockWidth);
									Error1.setHeight(1, "21");
									//Error1.setBorder(1);
									Error1.setColumnAlignment(1, "center");
									Error1.add(this.getSmallErrorText(getResourceBundle().getLocalizedString("start.search.error1", "_")), 1, 1);
									this.add(Error1);
									break;
								}
								if (E.getMessage().equals("Error2")) {
									Table Error2 = new Table(1, 1);
									Error2.setWidth(_blockWidth);
									Error2.setHeight(1, "21");
									//Error2.setBorder(1);
									Error2.setColumnAlignment(1, "center");
									Error2.add(this.getSmallErrorText(getResourceBundle().getLocalizedString("start.search.error2", "_")), 1, 1);
									this.add(Error2);
									break;
								}
								if (E.getMessage().equals("Error3")) {
									Table Error3 = new Table(1, 3);
	
									Error3.setWidth(_blockWidth);
									Error3.setHeight(1, "30");
									Error3.setHeight(2, "25");
									Error3.setCellspacing(0);
									//Error3.setBorder(1);
									Error3.setColumnAlignment(1, "center");
									Error3.add(this.getSmallErrorText(getFieldName(myField.get_field_id())), 1, 1);
									Error3.add(this.getSmallErrorText(getResourceBundle().getLocalizedString("start.search.error3", "_")), 1, 2);
									this.add(Error3);
								}
							}
						}
					} else {
						Table Error = new Table(1, 1);
						Error.setWidth(_blockWidth);
						Error.setHeight(1, "21");
	
						Error.setColumnAlignment(1, "center");
						Error.add(this.getSmallErrorText(getResourceBundle().getLocalizedString("start.search.error4", "_")), 1, 1);
						this.add(Error);
					}
				} else {
					Table Error = new Table(1, 1);
					Error.setWidth(_blockWidth);
					Error.setHeight(1, "21");
	
					Error.setColumnAlignment(1, "center");
					Error.add(this.getSmallErrorText(getResourceBundle().getLocalizedString("start.search.error5", "_")), 1, 1);
					this.add(Error);
				}
			}
		}
	}

	public Link getSearchLink(IWContext modinfo, boolean inUse) {
		Link myLink;
		if (inUse)
			myLink = new Link(getResourceBundle().getImage("tabs/search.gif"));
		else
			myLink = new Link(getResourceBundle().getImage("tabs/search1.gif"));

		myLink.addParameter("state", "search");

		return myLink;
	}

	public Link getEntryLink(IWContext modinfo, boolean inUse) {
		Link myLink;
		if (inUse)
			myLink = new Link(getResourceBundle().getImage("tabs/teetimes.gif"));
		else
			myLink = new Link(getResourceBundle().getImage("tabs/teetimes1.gif"));

		if(_teeTimesPage!=null) {
			myLink.setPage(_teeTimesPage);
		}
		
		return myLink;
	}

	public Form getWapSearchForm(IWContext modinfo, IWTimestamp dateFunc) throws IOException, SQLException {
		Form myForm = new Form();
		myForm.add(new HiddenInput("state", "search"));
		
		Table table = new Table();
		
		InterfaceObject fieldDropdownMenu = getFieldDropdownMenu(modinfo);
		
		DropdownMenu countOfPlayers = new DropdownMenu("fjoldi");
		countOfPlayers.addMenuElement(1,"1");
		countOfPlayers.addMenuElement(2,"2");
		countOfPlayers.addMenuElement(3,"3");
		countOfPlayers.addMenuElement(4,"4");
		countOfPlayers.setSelectedElement(1);
		
		InterfaceObject playerCountInputBox = countOfPlayers;//insertEditBox("fjoldi", 2);
		InterfaceObject firstTimeDropdownMenu = insertTimeDrowdown("ftime", "22:00", getHours(getFirstOpentime()), getHours(getLastClosetime()), TEE_TIME_SEARCH_INTERVALE);
		InterfaceObject lastTimeDropdownMenu = insertTimeDrowdown("ltime", "22:00", getHours(getFirstOpentime()), getHours(getLastClosetime()), TEE_TIME_SEARCH_INTERVALE);
		InterfaceObject dateDropdownMenu = insertDropdown("date", dateFunc, getMaxDaysShown(), modinfo);
		
		Label vollur = new Label(getResourceBundle().getLocalizedString("start.search.course", "Course"),fieldDropdownMenu);
		Label fjoldi = new Label(getResourceBundle().getLocalizedString("start.search.how_many", "How many?"),playerCountInputBox);
		Label fKL = new Label(getResourceBundle().getLocalizedString("start.search.from", "From"),firstTimeDropdownMenu);
		Label tKL = new Label(getResourceBundle().getLocalizedString("start.search.to", "To"),lastTimeDropdownMenu);
		Label dags = new Label(getResourceBundle().getLocalizedString("start.search.date", "Date"),dateDropdownMenu);

		int row = 1;
		table.add(vollur,1,row++);
		table.add(fieldDropdownMenu,1,row++);
		
		table.add(fjoldi,1,row++);
		table.add(playerCountInputBox,1,row++);
		
		table.add(dags,1,row++);
		
//		String funcyDateRS = dateFunc.toSQLDateString();

//		DropdownMenu myDropdown = new DropdownMenu("date");
//		for (int i = 0; i < getMaxDaysShown(); i++) {
//			myDropdown.addMenuElement(getNextDaysRS(dateFunc, funcyDateRS, i), getNextDays(dateFunc, funcyDateRS, i));
//		}
//		myDropdown.keepStatusOnAction();
		
		table.add(dateDropdownMenu,1,row++);

		
		table.add(fKL,1,row++);
//		DropdownMenu fHours = new DropdownMenu("ftime_h");
//		for(int i = 0;i<24;i++) {
//			fHours.addMenuElement(i,String.valueOf(i));
//		}
//		fHours.keepStatusOnAction();
		
//		DropdownMenu fMinutes = new DropdownMenu("ftime_m");
//		fMinutes.addMenuElement(0,"00");
//		fMinutes.addMenuElement(30,"30");
//		//fMinutes.addMenuElement(59,"59");
//		fMinutes.keepStatusOnAction();
//		
//		myForm.add(fHours);
//		myForm.add(":");
//		myForm.add(fMinutes);
		table.add(firstTimeDropdownMenu,1,row++);

		
		table.add(tKL,1,row++);
//		DropdownMenu lHours = new DropdownMenu("ltime_h");
//		for(int i = 0;i<24;i++) {
//			lHours.addMenuElement(i,String.valueOf(i));
//		}
//		lHours.keepStatusOnAction();
//		
//		DropdownMenu lMinutes = new DropdownMenu("ltime_m");
//		lMinutes.addMenuElement(0,"00");
//		lMinutes.addMenuElement(30,"30");
//		//lMinutes.addMenuElement(59,"59");
//		lMinutes.keepStatusOnAction();
		
//		myForm.add(lHours);
//		myForm.add(":");
//		myForm.add(lMinutes);
		table.add(lastTimeDropdownMenu,1,row++);

		

		
		GenericButton bSearch = getSubmitButton();
		bSearch.setName(localize("start.search","Search"));
		table.add(bSearch,1,row++);
		
		insertHiddenInput("results", "1", myForm);

		myForm.add(table);
		
		return myForm;
	}


	public Form getSearchForm(IWContext modinfo, IWTimestamp dateFunc) throws IOException, SQLException {

		Form myForm = new Form();
		myForm.add(new HiddenInput("state", "search"));
		
		Table frameTable = new Table();
		//frameTable.setBorder(1);
		frameTable.setWidth(_blockWidth);
		frameTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_LEFT);
		frameTable.setCellspacing(0);
		frameTable.setCellpadding(0);
		frameTable.setCellpadding(1,1,15);
		
		Table mergeTable = new Table(3, 4);
		mergeTable.mergeCells(2,1,3,1);
		mergeTable.mergeCells(2,2,3,2);
		mergeTable.mergeCells(2,3,3,3);
		mergeTable.setAlignment("left");
		//mergeTable.setBorder(1);
		mergeTable.setCellspacing(0);
		mergeTable.setCellpadding(4);
		
		mergeTable.setCellpaddingRight(1,1,3);
		mergeTable.setCellpaddingRight(1,2,3);
		mergeTable.setCellpaddingRight(1,3,3);
		mergeTable.setCellpaddingRight(1,4,3);
		
		mergeTable.setNoWrap(1,1);
		mergeTable.setNoWrap(1,2);
		mergeTable.setNoWrap(1,3);
		mergeTable.setNoWrap(1,4);
		mergeTable.setAlignment(3,4,Table.HORIZONTAL_ALIGN_RIGHT);

		Table SelectSubmit = new Table(2,2);
		SelectSubmit.setAlignment("left");
		//SelectSubmit.setBorder(3);
		SelectSubmit.setCellspacing(0);
		SelectSubmit.setCellpadding(0);
		SelectSubmit.setCellpaddingRight(1,2,5);
		SelectSubmit.setCellpaddingLeft(2,2,5);
		SelectSubmit.setAlignment(2, 2, "center");

		Text vollur = getText(getResourceBundle().getLocalizedString("start.search.course", "Course"));
		Text fjoldi = getText(getResourceBundle().getLocalizedString("start.search.how_many", "How many?"));
		Text fKL = getText(getResourceBundle().getLocalizedString("start.search.from", "From"));
		Text tKL = getText(getResourceBundle().getLocalizedString("start.search.to", "To"));
		Text dags = getText(getResourceBundle().getLocalizedString("start.search.date", "Date"));


		SelectSubmit.setVerticalAlignment(2, 2, "bottom");

		SelectSubmit.add(vollur, 1, 1);
		SelectSubmit.setCellpaddingLeft(1,1,5);

		SelectSubmit.add(insertSelectionBox("fields", modinfo, 6), 1, 2);
		
		mergeTable.add(dags, 1, 1);
		mergeTable.add(insertDropdown("date", dateFunc, getMaxDaysShown(), modinfo), 2, 1);
		
		mergeTable.add(fKL, 1, 2); 
		mergeTable.add(insertTimeDrowdown("ftime", "22:00", getHours(getFirstOpentime()), getHours(getLastClosetime()), 30), 2, 2);
		
		mergeTable.add(tKL, 1, 3);
		mergeTable.add(insertTimeDrowdown("ltime", "22:00", getHours(getFirstOpentime()), getHours(getLastClosetime()), 30), 2, 3);
		
		mergeTable.add(fjoldi, 1, 4);
		mergeTable.add(insertEditBox("fjoldi", 2), 2, 4);
		
		GenericButton bSearch = getSubmitButton();
		bSearch.setName(localize("start.search","Search"));

		mergeTable.add(bSearch, 3, 4);
		
		SelectSubmit.add(mergeTable, 2, 2);
		
		insertHiddenInput("results", "1", myForm);

		frameTable.add(SelectSubmit,1,1);
		myForm.add(frameTable);
		
		return myForm;
	}

	public boolean checkTime(int begin_hour, int begin_min, int end_hour, int end_min) {
		return (begin_hour < end_hour || (begin_hour == end_hour && begin_min < end_min)) && begin_hour >= 0 && begin_hour <= 24 && end_hour >= 0 && end_hour <= 24 && begin_min >= 0 && begin_min < 60 && end_min >= 0 && end_min < 60;
	}

	public Table getResultTable(IWContext modinfo, Vector Groups, GolfField info, String date1, int resultCol) throws SQLException, IOException, FinderException {

		Table myTable = new Table(2, 2);
		myTable.mergeCells(1, 2, 2, 2);
		myTable.setRowStyleClass(1,getHeaderRowClass());
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		myTable.setRowAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		myTable.setCellpaddingLeft(1,1,15);
		myTable.setCellpaddingRight(2,1,15);
		myTable.setWidth(_blockWidth);
		myTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

		Vector myVector = new Vector();
		Vector boolVector = new Vector();

		myVector = (Vector) Groups.elementAt(0);
		boolVector = (Vector) Groups.elementAt(1);

		int count = 0;

		for (int i = 0; i < boolVector.size(); i++) {
			if (((Boolean) boolVector.elementAt(i)).booleanValue()) count++;
		}

		Link myLink = getSmallHeaderLink(getFieldName(info.get_field_id()));
		if(_teeTimeTablePage != null) {
			myLink.setPage(_teeTimeTablePage);
		}
		myLink.addParameter("hvar", "" + info.get_field_id());
		myLink.addParameter("search", "1");
		myLink.addParameter("club", "" + getFieldUnion(info.get_field_id()));
		myLink.addParameter("day", date1);

		myTable.add(myLink, 1, 1);
		Text smallText = getSmallHeader("");

		if ((count % 10 == 1 || (count % 100) % 10 == 1) && count % 100 != 11) {
			smallText.setText(" (" + count + " " + getResourceBundle().getLocalizedString("start.search.available_tee_time", "Available tee time") + ")");
			myTable.add(smallText, 2, 1);
		} else if (count != 0) {
			smallText.setText(" (" + count + " " + getResourceBundle().getLocalizedString("start.search.available_tee_times", "Available tee_times") + ")");
			myTable.add(smallText, 2, 1);
		} else {
			smallText.setText(" (" +getResourceBundle().getLocalizedString("start.search.no_tee_times", "_")+")");
			myTable.add(smallText, 2, 1);
		}

		boolean first = true;

		Link[] Times = new Link[count];
		int links = 0;
		int hour = 0;
		int rows = 0;

		if (count != 0) {
			Table resultTable = new Table(resultCol, 1);
			resultTable.setCellspacing(0);
			resultTable.setCellpadding(0);
			resultTable.setWidth(_blockWidth);

			for (int i = 0; i < boolVector.size(); i++) {
				if (((Boolean) boolVector.elementAt(i)).booleanValue()) {

					hour = getHours(TimeVsGroupnum(Integer.parseInt(myVector.elementAt(i).toString()), info) + ":00");

					Times[links] = getLink(TimeVsGroupnum(Integer.parseInt(myVector.elementAt(i).toString()), info));

					if (hour < 13)
						Times[links].addParameter("hvenaer", "0");
					else if (hour < 17)
						Times[links].addParameter("hvenaer", "1");
					else
						Times[links].addParameter("hvenaer", "2");

					if(_teeTimeTablePage != null) {
						Times[links].setPage(_teeTimeTablePage);
					}
					Times[links].addParameter("hvar", "" + info.get_field_id());
					Times[links].addParameter("search", "1");
					Times[links].addParameter("club", "" + getFieldUnion(info.get_field_id()));
					Times[links].addParameter("day", date1);

					if (links % resultCol == 0) {
						rows++;
						resultTable.setRows(rows);
						resultTable.setRowStyleClass(rows,((rows%2==0)?getDarkRowClass():getLightRowClass()));
					}

					resultTable.add(Times[links], links % resultCol + 1, rows);
					links++;
				}
			}

			for (int i = 1; i <= resultCol; i++) {
				resultTable.setColumnAlignment(i, "center");
			}


			myTable.add(resultTable, 1, 2);
		}

		return myTable;
	}
	
	public void addWapResults(IWContext modinfo, Vector Groups, GolfField info2, String date) throws SQLException, IOException, FinderException {
		
		Form myForm = new Form();
		myForm.setClassToInstanciateAndSendTo(RegisterTime.class);
		
		TeetimeSearchResult result = (TeetimeSearchResult)IBOLookup.getSessionInstance(modinfo,TeetimeSearchResult.class);		
		result.setSublistSize(10);
		if(Groups != null){
			Vector myVector = new Vector();
			Vector boolVector = new Vector();
			Vector resultList = new Vector();
			
			myVector = (Vector) Groups.elementAt(0);
			boolVector = (Vector) Groups.elementAt(1);			
			
			for (int i = 0; i < boolVector.size(); i++) {
				if (((Boolean) boolVector.elementAt(i)).booleanValue()){
					resultList.add(myVector.elementAt(i));
				}
			}
			
			result.cachResult(resultList,info2,date);
			
		} else if(!result.isInitialized()){
			add(localize("start.session_timeout","Session has expired"));
			return;
		}
		
		myForm.addParameter("search", "1");					
		myForm.addParameter("field_id",result.getFieldInfo().get_field_id());
		myForm.addParameter("date", result.getDate());
		myForm.addParameter("union_id", getFieldUnion(result.getFieldInfo().get_field_id()));
		myForm.addParameter("skraMarga",modinfo.getParameter("fjoldi"));
		
		myForm.addParameter(RegisterTime.PRM_LOCKED_AS_WML_LAYOUT, "y");
		if(backPage!=null){
			myForm.addParameter(RegisterTime.PRM_BACK_PAGE,backPage.getPrimaryKey().toString());
		}

		
		String headerString = getFieldName(result.getFieldInfo().get_field_id())+" - "+result.getDate();
		
		int count = result.getResultSize();

		if ((count % 10 == 1 || (count % 100) % 10 == 1) && count % 100 != 11) {
			headerString += (" (" + count + " " + getResourceBundle().getLocalizedString("start.search.available_tee_time", "Available tee time") + ")");
		} else if (count != 0) {
			headerString += (" (" + count + " " + getResourceBundle().getLocalizedString("start.search.available_tee_times", "Available tee_times") + ")");
		} else {
			headerString += (" (" +getResourceBundle().getLocalizedString("start.search.no_tee_times", "_")+")");
		}
		
		Text headerText = new Text(headerString);
		Paragraph p = new Paragraph();
		p.add(headerText);
		add(p);
		

		boolean first = true;

//		Link[] Times = new Link[count];
//		int links = 0;
		//int hour = 0;
//		int rows = 0;
		
		DropdownMenu radio = new DropdownMenu("line");

		if (count != 0) {
			String part = modinfo.getParameter("part");
			List l;
			if("next".equals(part)){
				l = result.next();
			} else if("prev".equals(part)) {
				l=result.prev();
			} else {
				l=result.current();
			}
			for (Iterator iter = l.iterator(); iter.hasNext();) {
				String element = (String) iter.next().toString();
				radio.addMenuElement(element,TimeVsGroupnum(Integer.parseInt(element), result.getFieldInfo()));
			}
			
			myForm.add(radio);
			if(result.hasPrevious()){
				Link prev = new Link(localize("prev","Previous"));
				prev.addParameter("part","prev");
				prev.maintainParameter("skraMarga",modinfo);
				prev.maintainParameter("fjoldi",modinfo);
				add(prev);
			}
			if(result.hasNext()){
				Link next = new Link(localize("next","Next"));
				next.addParameter("part","next");
				next.maintainParameter("skraMarga",modinfo);
				next.maintainParameter("fjoldi",modinfo);
				add(next);
			}
			myForm.add(new SubmitButton(localize("start.reserve","Reserve")));
			
			add(myForm);
		}
	}

	public Vector search(IWTimestamp funcDate, IWContext modinfo, GolfField info, GolfField today, int fjoldi, String date, String firstTime, String lastTime, int firstHandicap, int LastHandicap) throws SQLException, IOException, Exception {

		boolean is_allowed = false;

		for (int i = 0; i < today.get_days_shown(); i++) {
			if (getNextDaysRS(funcDate, funcDate.toSQLDateString(), i).equals(date)) {
				is_allowed = true;
			}
		}
		if (!is_allowed) throw new Exception("Error3");

		int numOfGroup = fjoldi / 4;
		if (fjoldi % 4 > 0) numOfGroup++;

		Vector frameGroups = new Vector();
		Vector Groups = new Vector();
		Vector boolGroups = new Vector();
		frameGroups.add(0, Groups);
		frameGroups.add(1, boolGroups);

		int gr = numberOfGroups(firstTime, lastTime, info.get_interval());
		int firstgr = numberOfGroups(info.get_open_hour(), info.get_open_min(), firstTime, info.get_interval()) + 1;
		int lastgr = firstgr + gr;

		int j = firstgr;
		for (int i = 0; i <= gr; i++) {
			Groups.add(i, new Integer(j++));
			boolGroups.add(i, new Boolean(true));
		}

		TeeTime[] result = service.getTableEntries(date, firstgr, (lastgr + numOfGroup), info.get_field_id());

		Vector RSvector = new Vector();
		Vector group_num = new Vector();
		Vector name = new Vector();
		Vector handicap = new Vector();
		Vector club = new Vector();

		RSvector.add(0, group_num);
		RSvector.add(1, name);
		RSvector.add(2, handicap);
		RSvector.add(3, club);

		int k = 0;
		for (int i = 0; i < result.length; i++) {
			group_num.add(k, "" + result[i].getGroupNum());
			name.add(k, result[i].getName());
			handicap.add(k, "" + result[i].getHandicap());
			club.add(k, result[i].getClubName());

			k++;
		}
		group_num.add(k, "-1"); // sett inn �ar sem group_num m� ekki vera af
		// lengd 0 er aldrei fari� � seinni for-loopuna
		// og ekki passa� upp � a� allir komist fyrir
		// lokun.

		int count = 0;
		int p = 0;
		int m = 0;

		for (m = firstgr; m <= lastgr; m++) {
			for (int n = 0; n < group_num.size(); n++) {
				if (Integer.parseInt(group_num.elementAt(n).toString()) >= m && Integer.parseInt(group_num.elementAt(n).toString()) < (m + numOfGroup)) {
					count++;
				}
				//				out.print( "<br>" +count + " > " + (numOfGroup * 4 - fjoldi)
				// + " ||
				// ( " + (m
				// + numOfGroup) + " > " + getLastGroup(info)+1 + " && " + count
				// + " >
				// " +
				// (-(m-getLastGroup(info))*4 - fjoldi ) + " )" );
				if (count > (numOfGroup * 4 - fjoldi) || (((m + numOfGroup) > getLastGroup(info) + 1) && (count > (-(m - getLastGroup(info)) * 4 - fjoldi)))) {
					boolGroups.set(p, new Boolean(false));
					count = 0;
				}
			}
			count = 0;
			p++;
		}

		for (int i = 0; i < Groups.size(); i++) {
			if (Integer.parseInt(Groups.elementAt(i).toString()) < 1 || Integer.parseInt(Groups.elementAt(i).toString()) > getLastGroup(info)) boolGroups.set(i, new Boolean(false));
		}

		return frameGroups;

	}

	public int numberOfGroups(String firstTime, String lastTime, int interval) throws Exception {

		SqlTime mySqlTime = new SqlTime(firstTime);
		int firsthour = mySqlTime.get_hour();
		int firstmin = mySqlTime.get_min();
		mySqlTime.set_sqltime(lastTime);
		int lasthour = mySqlTime.get_hour();
		int lastmin = mySqlTime.get_min();

		if (!checkTime(firsthour, firstmin, lasthour, lastmin)) throw new Exception("Error1");

		int time = (lasthour - firsthour) * 60 + (lastmin - firstmin);

		return time / interval;
	}

	public int numberOfGroups(int firsthour, int firstmin, String lastTime, int interval) {

		SqlTime mySqlTime = new SqlTime(lastTime);
		int lasthour = mySqlTime.get_hour();
		int lastmin = mySqlTime.get_min();

		int time = (lasthour - firsthour) * 60 + (lastmin - firstmin);

		return time / interval;
	}

	public GolfField getFieldInfo(int field, String date) throws SQLException, IOException {
		StartingtimeFieldConfig FieldConfig = service.getFieldConfig(field, date);
		GolfField field_info = new GolfField(new IWTimestamp(FieldConfig.getOpenTime()).toSQLTimeString(), new IWTimestamp(FieldConfig.getCloseTime()).toSQLTimeString(), FieldConfig.getMinutesBetweenStart(), field, date, FieldConfig.getDaysShown(), FieldConfig.publicRegistration());
		return field_info;
	}

	public SubmitButton insertButton(String btnName, String Method, String Action, Form theForm) {
		SubmitButton mySubmit = (SubmitButton) getButton(new SubmitButton(btnName));
		theForm.addObject(mySubmit);

		theForm.setMethod(Method);
		theForm.setAction(Action);

		return mySubmit;
	}

	private SubmitButton insertButton(Image image, String imageName, String Method, String Action, Form theForm) {
		SubmitButton mySubmit = (SubmitButton) getButton(new SubmitButton(image, imageName));
		theForm.addObject(mySubmit);

		theForm.setMethod(Method);
		theForm.setAction(Action);
		return mySubmit;
	}

	public SubmitButton insertButton(String btnName) {
		SubmitButton mySubmit = (SubmitButton) getButton(new SubmitButton(btnName));
		return mySubmit;
	}

	public SubmitButton insertButton(Image myImage, String btnName) {
		SubmitButton mySubmit = (SubmitButton) getButton(new SubmitButton(myImage, btnName));
		return mySubmit;
	}

	public HiddenInput insertHiddenInput(String inpName, String value, Form theForm) {
		HiddenInput myObject = new HiddenInput(inpName, value);
		theForm.addObject(myObject);

		return myObject;
	}
	
	public DropdownMenu getFieldDropdownMenu(IWContext modinfo) throws IOException, SQLException {
		DropdownMenu myDropdownMenu = new DropdownMenu("fields");
		Field[] field = service.getStartingEntryField();
		for (int i = 0; i < field.length; i++) {
			try {
				Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(field[i].getUnionID());
				myDropdownMenu.addMenuElement(field[i].getID(), union.getAbbrevation() + " - " + field[i].getName());
			}
			catch (FinderException fe) {
				log(fe);
			}
		}
		myDropdownMenu.keepStatusOnAction();
		return myDropdownMenu;
	}

	public SelectionBox insertSelectionBox(String SelectionBoxName, IWContext modinfo, int height) throws IOException, SQLException {
		SelectionBox mySelectionBox = (SelectionBox) getStyledInterface(new SelectionBox(SelectionBoxName));
		mySelectionBox.setHeight(height);
		mySelectionBox.setWidth(String.valueOf(width));
		Field[] field = service.getStartingEntryField();
		for (int i = 0; i < field.length; i++) {
			try {
				Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(field[i].getUnionID());
				mySelectionBox.addElement("" + field[i].getID(), union.getAbbrevation() + " - " + field[i].getName());
			}
			catch (FinderException fe) {
				log(fe);
			}
		}
		mySelectionBox.keepStatusOnAction();
		return mySelectionBox;
	}

	public DropdownMenu insertClubSelectionBox(String SelectionBoxName, IWContext modinfo, int height) throws IOException, SQLException {
		DropdownMenu mySelectionBox = (DropdownMenu) getStyledInterface(new DropdownMenu(SelectionBoxName));
		mySelectionBox.setMarkupAttribute("size", Integer.toString(height));

		Union[] union = service.getStartingEntryUnion();
		for (int i = 0; i < union.length; i++) {
			mySelectionBox.addMenuElement("" + union[i].getID(), union[i].getName());
		}
		if (union.length > 0) mySelectionBox.setSelectedElement(Integer.toString(union[0].getID()));
		mySelectionBox.keepStatusOnAction();
		return mySelectionBox;
	}

	public DropdownMenu insertDropdown(String dropdownName, int countFrom, int countTo) {
		String from = Integer.toString(countFrom);
		DropdownMenu myDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(dropdownName));

		for (; countFrom <= countTo; countFrom++) {
			myDropdown.addMenuElement(Integer.toString(countFrom), Integer.toString(countFrom));
		}
		myDropdown.keepStatusOnAction();

		return myDropdown;
	}

	public DropdownMenu insertDropdown(String dropdownName, IWTimestamp funcDate, GolfField today, IWContext modinfo) {
		//String funcyDate = funcDate.getDateStamp();
		String funcyDateRS = funcDate.toSQLDateString();

		DropdownMenu myDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(dropdownName));

		//		myDropdown.addMenuElement(funcyDateRS, getNextDays(funcDate,
		// funcyDateRS,
		// 0));

		for (int i = 0; i < today.get_days_shown(); i++) {
			myDropdown.addMenuElement(getNextDaysRS(funcDate, funcyDateRS, i), getNextDays(funcDate, funcyDateRS, i));
		}

		myDropdown.keepStatusOnAction();
		myDropdown.setToSubmit();
		myDropdown.setSelectedElement(modinfo.getSession().getAttribute("date").toString());

		return myDropdown;
	}

	public DropdownMenu insertDropdown(String dropdownName, IWTimestamp funcDate, int days_shown, IWContext modinfo) {
		//String funcyDate = funcDate.getDateStamp();
		String funcyDateRS = funcDate.toSQLDateString();

		DropdownMenu myDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(dropdownName));

		//		myDropdown.addMenuElement(funcyDateRS, getNextDays(funcDate,
		// funcyDateRS,
		// 0));

		for (int i = 0; i < days_shown; i++) {
			myDropdown.addMenuElement(getNextDaysRS(funcDate, funcyDateRS, i), getNextDays(funcDate, funcyDateRS, i));
		}

		myDropdown.keepStatusOnAction();
		//		myDropdown.setToSubmit();
		//		myDropdown.setSelectedElement(modinfo.getSession().getAttribute("date").toString());

		return myDropdown;
	}

	public DropdownMenu insertDrowdown(String dropdownName, TableInfo myTableInfo, IWContext modinfo) throws SQLException, IOException {
		//		PrintWriter out = modinfo.getResponse().getWriter();

		DropdownMenu myDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(dropdownName));
		int end = myTableInfo.get_row_num();
		int interval = myTableInfo.get_interval();
		int pic_min = myTableInfo.get_first_pic_min();
		int pic_hour = myTableInfo.get_first_pic_hour();
		int first_group = myTableInfo.get_first_group();
		String Time;
		String TimeVal;
		int val = 0;

		for (int i = 1; i <= end; i++) {

			if (pic_min >= 60) {
				pic_min -= 60;
				pic_hour++;
			}

			if (pic_min < 10)
				Time = pic_hour + ":0" + pic_min;
			else
				Time = pic_hour + ":" + pic_min;

			val = (first_group + i) - 1;
			TimeVal = Integer.toString(val);

			myDropdown.addMenuElement(TimeVal, Time);

			pic_min += interval;
		}
		myDropdown.keepStatusOnAction();
		return myDropdown;
	}

	/*
	 * public String getTime( int end, GolfField myGolfField) { int interval =
	 * myGolfField.get_interval(); int openMin = myGolfField.get_open_min(); int
	 * openHour = myGolfField.get_open_hour(); String Time = ""; for(int i = 1;
	 * i <= end; i ++){ if (openMin >= 60){ openMin -= 60; openHour++; } if
	 * (openMin < 10) Time = openHour + ":0" + openMin; else Time = openHour +
	 * ":" + openMin; openMin += interval; } return Time; }
	 */

	public DropdownMenu insertDrowdown(String dropdownName, String auto, int bil) {

		DropdownMenu myDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(dropdownName));

		String time;
		String TimeVal;

		int pic_hour;

		for (int i = 0; i <= 23; i++) {
			pic_hour = i;

			if (pic_hour != 24) {
				for (int pic_min = 0; pic_min < 60; pic_min += bil) {
					if (pic_min < 10 && pic_hour < 10)
						time = "0" + pic_hour + ":0" + pic_min;
					else if (pic_min < 10)
						time = "" + pic_hour + ":0" + pic_min;
					else if (pic_hour < 10)
						time = "0" + pic_hour + ":" + pic_min;
					else
						time = "" + pic_hour + ":" + pic_min;

					TimeVal = time + ":00";

					myDropdown.addMenuElement(TimeVal, time);
				}
			} else {
				myDropdown.addMenuElement("24:00:00", "00:00");
				continue;
			}
		}

		//myDropdown.setSelectedElement(auto + ":00");
		myDropdown.keepStatusOnAction();

		return myDropdown;
	}

	public DropdownMenu insertDrowdown(String dropdownName, String auto, int bil, boolean ltime) {

		DropdownMenu myDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(dropdownName));

		String time;
		String TimeVal;
		boolean first = true;
		int pic_hour;

		for (int i = 0; i <= 24; i++) {
			pic_hour = i;

			if (pic_hour != 24) {
				for (int pic_min = 0; pic_min < 60; pic_min += bil) {
					if (!first) {
						if (pic_min < 10 && pic_hour < 10)
							time = "0" + pic_hour + ":0" + pic_min;
						else if (pic_min < 10)
							time = "" + pic_hour + ":0" + pic_min;
						else if (pic_hour < 10)
							time = "0" + pic_hour + ":" + pic_min;
						else
							time = "" + pic_hour + ":" + pic_min;

						TimeVal = time + ":00";

						myDropdown.addMenuElement(TimeVal, time);

					}
					first = false;
				}
			} else {
				myDropdown.addMenuElement("24:00:00", "00:00");
				continue;
			}

		}

		//myDropdown.setSelectedElement(auto + ":00");
		myDropdown.keepStatusOnAction();

		return myDropdown;
	}

	public DropdownMenu insertTimeDrowdown(String dropdownName, String auto, int firstHour, int lastHour, int interval) {

		DropdownMenu myDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(dropdownName));

		String time;
		String TimeVal;
		//		boolean first = true;
		int pic_hour;
		if (lastHour != 24) lastHour++;

		for (int i = firstHour; i <= lastHour; i++) {
			pic_hour = i;
			if (pic_hour != 24) {
				for (int pic_min = 0; pic_min < 60; pic_min += interval) {
					if (pic_min < 10 && pic_hour < 10)
						time = "0" + pic_hour + ":0" + pic_min;
					else if (pic_min < 10)
						time = "" + pic_hour + ":0" + pic_min;
					else if (pic_hour < 10)
						time = "0" + pic_hour + ":" + pic_min;
					else
						time = "" + pic_hour + ":" + pic_min;

					TimeVal = time + ":00";

					myDropdown.addMenuElement(TimeVal, time);

					if (i == lastHour) break;

				}
			} else {
				myDropdown.addMenuElement("24:00:00", "00:00");
				continue;
			}
		}

		//myDropdown.setSelectedElement(auto + ":00");
		myDropdown.keepStatusOnAction();

		return myDropdown;
	}

	public TextInput insertEditBox(String name) {
		TextInput myInput = (TextInput) getStyledInterface(new TextInput(name));

		myInput.keepStatusOnAction();

		return myInput;
	}

	public TextInput insertEditBox(String name, int size) {
		IntegerInput myInput = (IntegerInput) getStyledInterface(new IntegerInput(name));
		myInput.setSize(size);
		myInput.setValue(1);
		
		myInput.keepStatusOnAction();

		return myInput;
	}

	public String getNextDaysRS(IWTimestamp funcDate, String date, int days) {
		IWCalendar calendar = new IWCalendar(funcDate);
		int day, month, year;
		StringTokenizer Timetoken = new StringTokenizer(date, "-");

		year = Integer.parseInt(Timetoken.nextToken());
		month = Integer.parseInt(Timetoken.nextToken());
		day = Integer.parseInt(Timetoken.nextToken()) + days;

		if (day > calendar.getLengthOfMonth(month, year)) {
			day -= (calendar.getLengthOfMonth(month, year));
			month++;
		}
		if (month > 12) {
			year++;
			month -= 12;
		}
		String d, m;

		if (day < 10)
			d = "0" + day;
		else
			d = "" + day;

		if (month < 10)
			m = "0" + month;
		else
			m = "" + month;

		return year + "-" + m + "-" + d;

	}

	public String getNextDays(IWTimestamp funcDate, String date, int days) {
		IWCalendar calendar = new IWCalendar(funcDate);
		int day, month, year;
		StringTokenizer Timetoken = new StringTokenizer(date, "-");

		year = Integer.parseInt(Timetoken.nextToken());
		month = Integer.parseInt(Timetoken.nextToken());
		day = Integer.parseInt(Timetoken.nextToken()) + days;

		if (day > calendar.getLengthOfMonth(month, year)) {
			day -= calendar.getLengthOfMonth(month, year);
			month++;
		}
		if (month > 12) {
			year++;
			month -= 12;
		}

		String mon = calendar.getMonthName(month);

		if (day < 10)
			return day + ".&nbsp;&nbsp;" + mon.toLowerCase() + " " + year;
		else
			return day + ". " + mon.toLowerCase() + " " + year;
	}

	public String getFieldName(int field_id) throws SQLException, IOException, FinderException {
		return service.getFieldName(field_id);
	}

	public String getFirstOpentime() throws SQLException, IOException {
		String time = "08:00:00";
		if (service.getFirstOpentime() != null) time = service.getFirstOpentime().toSQLTimeString();
		return time;
	}

	public int getMaxDaysShown() throws SQLException, IOException {
		return service.getMaxDaysShown();
	}

	public String getLastClosetime() throws SQLException, IOException {
		String time = "23:00:00";
		if (service.getLastClosetime() != null) time = service.getLastClosetime().toSQLTimeString();
		return time;
	}

	public int getFieldUnion(int field_id) throws SQLException, IOException, FinderException {
		return service.getFieldUnion(field_id);
	}

	//	/#### Skilar Klukkustundinni �r streng � forminu 'klst:min:sec'
	// \(08:00:00)\
	// ####///
	public int getHours(String Hours) {
		SqlTime mySqlTime = new SqlTime(Hours);
		return mySqlTime.get_hour();
	}

	//	/######## Skilar n�meri � �v� holli sem er s��ast fyrir lokun
	// #####////////
	public int getLastGroup(GolfField myGolfField) {

		int interval = myGolfField.get_interval();

		int Hours = myGolfField.get_close_hour() - myGolfField.get_open_hour();
		int Min = myGolfField.get_close_min() - myGolfField.get_open_min();

		return (Hours * 60 + Min) / interval;
	}

	//	/#### Skilar t�ma m.v. v�ll og n�mer � holli ####///
	public String TimeVsGroupnum(int group, GolfField myGolfField) {

		int interval = myGolfField.get_interval();
		int openHour = myGolfField.get_open_hour();
		int openMin = myGolfField.get_open_min();

		int Hour = openHour + ((group - 1) * interval) / 60;
		int Min = openMin + ((group - 1) * interval) % 60;

		if (Min >= 60) {
			Min -= 60;
			Hour++;
		}

		String time;

		if (Min < 10 && Hour < 10)
			time = "0" + Hour + ":0" + Min;
		else if (Min < 10)
			time = "" + Hour + ":0" + Min;
		else if (Hour < 10)
			time = "0" + Hour + ":" + Min;
		else
			time = "" + Hour + ":" + Min;

		return time;
	}

	public boolean numericString(String myString) {

		boolean isTrue = true;

		for (int i = 0; i < myString.length(); i++) {
			if (!(myString.charAt(i) == '0' || myString.charAt(i) == '1' || myString.charAt(i) == '2' || myString.charAt(i) == '3' || myString.charAt(i) == '4' || myString.charAt(i) == '5' || myString.charAt(i) == '6' || myString.charAt(i) == '7' || myString.charAt(i) == '8' || myString.charAt(i) == '9')) isTrue = false;
		}

		return isTrue;
	}



	public ICPage getTeeTimesPage() {
		return _teeTimesPage;
	}

	public void setTeeTimesPage(ICPage p) {
		_teeTimesPage = p;
	}

	public ICPage getTeeTimeTablePage() {
		return _teeTimeTablePage;
	}

	public void setTeeTimeTablePage(ICPage p) {
		_teeTimeTablePage = p;
	}

	public void setNumberOfResultColumns(int n) {
		if(n>0) {
			_numberOfResultColumns = n;
		}
	}
	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return Returns the lockedAsWapLayout.
	 */
	public boolean isLockedAsWapLayout() {
		return lockedAsWapLayout;
	}
	/**
	 * @param lockedAsWapLayout The lockedAsWapLayout to set.
	 */
	public void setLockedAsWapLayout(boolean lockedAsWapLayout) {
		this.lockedAsWapLayout = lockedAsWapLayout;
	}
	/**
	 * @param backPage The backPage to set.
	 */
	public void setBackPageForWMLMode(ICPage backPage) {
		this.backPage = backPage;
	}
}

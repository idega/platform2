/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.service.StartService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class TeeTimeFieldConfiguration extends GolfBlock {

	private StartService service = new StartService();
	private StartingtimeFieldConfig _currentFieldConfig = null;

	public void main(IWContext modinfo) throws Exception {
		this.empty();
		getParentPage().setTitle(localize("start.field_configuration","Field configuration"));
		try {
			
			String fieldId = (String) modinfo.getSession().getAttribute("field_id");
			String date = (String) modinfo.getSession().getAttribute("date");

			if( modinfo.getParameter("fieldID") == null) {
				if(fieldId != null ) {
					_currentFieldConfig = service.getFieldConfig(Integer.parseInt(fieldId),((date==null)?IWTimestamp.RightNow():new IWTimestamp(date)));
				} else {
					Field[] field = null;
					if (isAdmin(((Member) modinfo.getSession().getAttribute("member_login")).getID(), false, false)) {
						field = service.getStartingEntryField();
					}else {
						field = service.getFields((String) modinfo.getSessionAttribute("member_main_union_id"));
					}
					if(field != null && field.length > 0 ) {
						_currentFieldConfig = service.getFieldConfig(field[0].getID(), ((date==null)?IWTimestamp.RightNow():new IWTimestamp(date)));
					}
				}
			}
			

			
			if (modinfo.getRequest().getParameter("btnSkra") != null || modinfo.getRequest().getParameter("btnSkra.x") != null) {
				Form myForm = new Form();
				if (!storeConfig(modinfo)) {
					setFeedBack(myForm, false);
				} else {
					setFeedBack(myForm, true);
				}
			} else
				drawTable(modinfo);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public DropdownMenu dropdownInterval(IWContext iwc, String dropdownName, int first, int next, int last) {
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		myDropdown.addMenuElement("0", "m’n");
		myDropdown.addMenuElement(Integer.toString(first), Integer.toString(first));
		myDropdown.addMenuElement(Integer.toString(next), Integer.toString(next));
		myDropdown.addMenuElement(Integer.toString(last), Integer.toString(last));

		myDropdown.keepStatusOnAction();
		if(iwc.getParameter(dropdownName) == null && _currentFieldConfig != null) {
			myDropdown.setSelectedElement(String.valueOf(_currentFieldConfig.getMinutesBetweenStart()));
		} else {
			myDropdown.setSelectedElement("m’n");
		}
		myDropdown.setToSubmit();

		return myDropdown;
	}

	public HiddenInput insertHiddenInput(String inpName, String value, Form theForm) {
		HiddenInput myObject = new HiddenInput(inpName, value);
		theForm.addObject(myObject);

		return myObject;
	}

	public DropdownMenu FieldDropdown(String dropdownName, IWContext modinfo) throws IOException {
		Field[] field = null;
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);
		String fieldId = (String) modinfo.getSession().getAttribute("field_id");
		String prmFieldID = modinfo.getParameter("fieldID");

		if(prmFieldID != null) {
			fieldId = prmFieldID;
		}

		try {
			if (isAdmin(((Member) modinfo.getSession().getAttribute("member_login")).getID(), false, false))
				field = service.getStartingEntryField();
			else
				field = service.getFields((String) modinfo.getSessionAttribute("member_main_union_id"));

			for (int i = 0; i < field.length; i++) {
				myDropdown.addMenuElement(field[i].getID(), field[i].getName());
			}
			myDropdown.keepStatusOnAction();
			myDropdown.setToSubmit();
			myDropdown.setSelectedElement(fieldId);
		} catch (SQLException E) {
			E.printStackTrace();
		}
		return myDropdown;
	}

	public DropdownMenu insertDropdown(String dropdownName, int countFrom, int countTo) {
		String from = Integer.toString(countFrom);
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		for (; countFrom <= countTo; countFrom++) {
			myDropdown.addMenuElement(Integer.toString(countFrom), Integer.toString(countFrom));
		}
		
		if(_currentFieldConfig!=null) {

			if("daysShown".equals(dropdownName)) {
				myDropdown.setSelectedElement(String.valueOf(_currentFieldConfig.getDaysShown()));
			}else if("daysShownNonMember".equals(dropdownName)) {
				myDropdown.setSelectedElement(String.valueOf(_currentFieldConfig.getDaysShownNonMember()));
			}

		}
		
		myDropdown.keepStatusOnAction();
		

		return myDropdown;
	}

	public DropdownMenu insertDropdown_opentime(String dropdownName, int interval) {
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		int hour = 12;
		int min = 60 - interval;
		String Time, TimeRS;
		Vector timeVector = new Vector();
		Vector timeVectorRS = new Vector();

		while (hour > -1) {

			if (min < 10 && hour < 10)
				Time = "0" + hour + ":0" + min;
			else if (min < 10 && hour > 9)
				Time = hour + ":0" + min;
			else if (min > 9 && hour < 10)
				Time = "0" + hour + ":" + min;
			else
				Time = hour + ":" + min;

			if (min < 10 && hour < 10)
				TimeRS = "0" + hour + ":0" + min + ":00";
			else if (min < 10 && hour > 9)
				TimeRS = hour + ":0" + min + ":00";
			else if (min > 9 && hour < 10)
				TimeRS = "0" + hour + ":" + min + ":00";
			else
				TimeRS = hour + ":" + min + ":00";

			if (min % 10 == 0) {
				timeVector.addElement(Time);
				timeVectorRS.addElement(TimeRS);
			}

			min -= interval;

			if (min < 0) {
				min += 60;
				hour--;
			}
		}

		for (int i = timeVectorRS.size() - 1; i >= 0; i--)
			myDropdown.addMenuElement(timeVectorRS.get(i).toString(), timeVector.get(i).toString());

		timeVectorRS.removeAllElements();
		timeVector.removeAllElements();
		
		if(_currentFieldConfig!=null) {
			IWTimestamp time = new IWTimestamp(_currentFieldConfig.getOpenTime());
			min = time.getMinute();
			hour = time.getHour();
			if (min < 10 && hour < 10)
				TimeRS = "0" + hour + ":0" + min + ":00";
			else if (min < 10 && hour > 9)
				TimeRS = hour + ":0" + min + ":00";
			else if (min > 9 && hour < 10)
				TimeRS = "0" + hour + ":" + min + ":00";
			else
				TimeRS = hour + ":" + min + ":00";

			myDropdown.setSelectedElement(TimeRS);
		}

		return myDropdown;
	}

	public DropdownMenu insertDropdown_closetime(String dropdownName, int interval) {
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		int hour = 17;
		int min = 0 + interval;
		String Time, TimeRS;

		while (hour < 24) {

			if (min < 10 && hour < 10)
				Time = "0" + hour + ":0" + min;
			else if (min < 10 && hour > 9)
				Time = hour + ":0" + min;
			else if (min > 9 && hour < 10)
				Time = "0" + hour + ":" + min;
			else
				Time = hour + ":" + min;

			if (min < 10 && hour < 10)
				TimeRS = "0" + hour + ":0" + min + ":00";
			else if (min < 10 && hour > 9)
				TimeRS = hour + ":0" + min + ":00";
			else if (min > 9 && hour < 10)
				TimeRS = "0" + hour + ":" + min + ":00";
			else
				TimeRS = hour + ":" + min + ":00";

			if (min % 10 == 0) myDropdown.addMenuElement(TimeRS, Time);

			min += interval;

			if (min >= 60) {
				min -= 60;
				hour++;
			}
		}
		
		if(_currentFieldConfig!=null) {
			IWTimestamp time = new IWTimestamp(_currentFieldConfig.getCloseTime());
			min = time.getMinute();
			hour = time.getHour();
			if (min < 10 && hour < 10)
				TimeRS = "0" + hour + ":0" + min + ":00";
			else if (min < 10 && hour > 9)
				TimeRS = hour + ":0" + min + ":00";
			else if (min > 9 && hour < 10)
				TimeRS = "0" + hour + ":" + min + ":00";
			else
				TimeRS = hour + ":" + min + ":00";

			myDropdown.setSelectedElement(TimeRS);
		}
		

		return myDropdown;
	}


	public DropdownMenu insertDropdown(String dropdownName, IWCalendar funcDate, IWContext modinfo) {

		IWTimestamp stamp = new IWTimestamp();

		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		for (int i = 0; i < 25; i++) {
			myDropdown.addMenuElement(stamp.toSQLDateString(), stamp.getLocaleDate(modinfo.getCurrentLocale()));
			stamp.addDays(1);
		}

		myDropdown.keepStatusOnAction();
		myDropdown.setToSubmit();
		myDropdown.setSelectedElement(modinfo.getSession().getAttribute("date").toString());

		return myDropdown;
	}

	public void setGraphic(Form myForm) {

		Table Header = new Table(2, 1);
		Header.setWidth(1, "10");

		Header.setCellpadding(0);
		Header.setCellspacing(0);
		getParentPage().setMarginWidth(0);
		getParentPage().setMarginHeight(0);
		getParentPage().setLeftMargin(0);
		getParentPage().setTopMargin(0);

		Header.setWidth("100%");

		myForm.add(Header);

		//getParentPage().setBackgroundColor("#8ab490");
	}

	public boolean storeConfig(IWContext modinfo) throws SQLException, IOException {
		String BeginDate = "";

		try {
			StartingtimeFieldConfig conf = (StartingtimeFieldConfig) IDOLookup.instanciateEntity(StartingtimeFieldConfig.class);
			String FieldID = modinfo.getRequest().getParameter("fieldID");
			String DaysShown = modinfo.getRequest().getParameter("daysShown");
			String DaysShownNonMember = modinfo.getRequest().getParameter("daysShownNonMember");
			String pubReg = modinfo.getRequest().getParameter("public_reg");
			String nonmemberReg = modinfo.getRequest().getParameter("nonmember_reg");
			BeginDate = modinfo.getRequest().getParameter("beginDate");
			String Interval = modinfo.getRequest().getParameter("interval");
			String OpenTime = modinfo.getRequest().getParameter("openTime");
			String CloseTime = modinfo.getRequest().getParameter("closeTime");

			boolean pReg = (pubReg != null && pubReg.equals("true"));
			boolean nReg = (nonmemberReg != null && nonmemberReg.equals("true"));

			if (OpenTime == null || CloseTime == null) return false;

			IWTimestamp beginDate = new IWTimestamp(BeginDate);
			IWTimestamp openTime = new IWTimestamp(OpenTime);
			IWTimestamp closeTime = new IWTimestamp(CloseTime);

			//System.err.println("Select * from " + conf.getEntityName() + "
			// WHERE begin_date ='"+beginDate.toSQLDateString()+"'");
			List oldRecord = EntityFinder.findAll(conf, "Select * from " + conf.getEntityName() + " WHERE begin_date ='" + beginDate.toSQLDateString() + "' and field_id = " + FieldID);

			if (oldRecord == null) {
				conf.setFieldID(new Integer(FieldID));
				conf.setBeginDate(beginDate.getTimestamp());
				conf.setOpenTime(openTime.getTimestamp());
				conf.setCloseTime(closeTime.getTimestamp());
				conf.setMinutesBetweenStart(new Integer(Interval));
				conf.setDaysShown(new Integer(DaysShown));
				conf.setDaysShownNonMember(new Integer(DaysShownNonMember));
				conf.setPublicRegistration(pReg);
				conf.setNonMemberRegistration(nReg);

				conf.insert();
			} else {
				conf = (StartingtimeFieldConfig) oldRecord.get(0);
				conf.setFieldID(new Integer(FieldID));
				conf.setBeginDate(beginDate.getTimestamp());
				conf.setOpenTime(openTime.getTimestamp());
				conf.setCloseTime(closeTime.getTimestamp());
				conf.setMinutesBetweenStart(new Integer(Interval));
				conf.setDaysShown(new Integer(DaysShown));
				conf.setDaysShownNonMember(new Integer(DaysShownNonMember));
				conf.setPublicRegistration(pReg);
				conf.setNonMemberRegistration(nReg);

				conf.update();
			}
		} catch (SQLException E) {
			return false;
		}
		return true;
	}

	public void setFeedBack(Form myForm, boolean isOk) {

		Table myTable = new Table(2, 7);
		if (isOk) {
			myTable.add(getLocalizedMessage("start.registration_was_successful","Registration was successful"), 2, 3);
			myTable.add(getCloseButton(), 2, 7);
		} else {
			myTable.add(getLocalizedMessage("start.registration_was_not_successful","Registration was not successful"), 2, 2);
			myTable.add(getButton(new BackButton()), 2, 7);
		}

		myTable.setWidth(1, "25");
		myTable.setAlignment("center");
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);

		myForm.add(myTable);

		add(myForm);

	}

	public void drawTable(IWContext modinfo) throws IOException {
		IWCalendar funcDate = new IWCalendar();
		Table firstTable = new Table(5, 9);	

		//firstTable.setBorder(1);
		firstTable.mergeCells(1, 2, 2, 2);
		firstTable.setAlignment("center");
		Form myForm = new Form();
		//setGraphic(myForm);

		// Choose field
		firstTable.add(getLocalizedSmallText("field","Field"), 1, 1);
		firstTable.add(FieldDropdown("fieldID", modinfo), 1, 2);

		// Choose the day when the configuration should be activated
		firstTable.add(getLocalizedSmallText("start.activated","Activated"), 3, 1);
		firstTable.add(insertDropdown("beginDate", funcDate, modinfo), 3, 2);

		// Choose how many days are free for registration
		firstTable.setAlignment(4, 2, "right");
		firstTable.add(getLocalizedSmallText("start.days_in_registration:members","Days in registration: Members"), 4, 1);
		firstTable.add(insertDropdown("daysShown", 1, 28), 4, 2);
		firstTable.add(getLocalizedSmallText("start.others","Others"), 5, 1);
		firstTable.add(insertDropdown("daysShownNonMember", 1, 28), 5, 2);

		// choose interval
		firstTable.add(getLocalizedSmallText("start.interval","Interval"), 1, 4);
		firstTable.add(dropdownInterval(modinfo,"interval", 8, 10, 12), 1, 5);

		firstTable.add(getLocalizedSmallText("start.online_registration_members","On-line registration: members"), 1, 7);
		CheckBox pBox = new CheckBox("public_reg", "true");
		if(_currentFieldConfig != null) {
			pBox.setChecked(_currentFieldConfig.publicRegistration());
		} else {
			pBox.setChecked("true".equals(modinfo.getParameter("public_reg")));
		}
		firstTable.add(pBox, 2, 7);
		
		firstTable.add(getLocalizedSmallText("start.online_registration_others","On-line registration: others"), 1, 8);
		CheckBox mBox = new CheckBox("nonmember_reg", "true");
		if(_currentFieldConfig != null) {
			mBox.setChecked(_currentFieldConfig.getNonMemberRegistration());
		} else {
			mBox.setChecked("true".equals(modinfo.getParameter("nonmember_reg")));
		}
		firstTable.add(mBox, 2, 8);

		if (modinfo.getRequest().getParameter("interval") != null) {
			String Bil = modinfo.getRequest().getParameter("interval");
			if (!Bil.equals("0")) {
				firstTable.add(getLocalizedSmallText("start.first_group:", "First group:"), 3, 4);
				firstTable.add(insertDropdown_opentime("openTime", Integer.parseInt(Bil)), 3, 5);
				firstTable.add(getLocalizedSmallText("start.last_group:", "Last group:"), 4, 4);
				firstTable.add(insertDropdown_closetime("closeTime", Integer.parseInt(Bil)), 4, 5);
			}
		} else if(_currentFieldConfig != null) {
			String Bil = String.valueOf(_currentFieldConfig.getMinutesBetweenStart());
			if (!Bil.equals("0")) {
				firstTable.add(getLocalizedSmallText("start.first_group:", "First group:"), 3, 4);
				firstTable.add(insertDropdown_opentime("openTime", Integer.parseInt(Bil)), 3, 5);
				firstTable.add(getLocalizedSmallText("start.last_group:", "Last group:"), 4, 4);
				firstTable.add(insertDropdown_closetime("closeTime", Integer.parseInt(Bil)), 4, 5);
			}
		}

		GenericButton button = getButton(new SubmitButton(localize(GolfBlock.LOCALIZATION_SAVE_KEY,"Save"), "btnSkra","1"));
		
		
		firstTable.add(button, 3, 9);
		firstTable.add(getCloseButton(), 4, 9);


		myForm.setMethod("post");
		myForm.add(firstTable);
		add(myForm);
	}

	public boolean isAdmin(int memberId, boolean clubadmin, boolean clubworker) throws SQLException {

		Member member = null;
		try {
			member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
		} catch (FinderException e) {
			e.printStackTrace();
		}

		if (member != null) {
			String groupName = null;
			Group[] access = member.getGroups();
			for (int i = 0; i < access.length; i++) {
				groupName = access[i].getName();

				if ("administrator".equals(groupName)) return true;
				if (clubadmin && "club_admin".equals(groupName)) return true;
				if (clubworker && "club_worker".equals(groupName)) return true;
			}
		}

		return false;
	}
}
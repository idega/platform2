package is.idega.idegaweb.golf.startingtime.presentation;

import com.idega.presentation.PresentationObjectContainer;
import is.idega.idegaweb.golf.startingtime.business.TeeTimeBusiness;
import is.idega.idegaweb.golf.entity.Tournament;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.FloatInput;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.Image;
import is.idega.idegaweb.golf.GolfField;
import com.idega.data.IDOLegacyEntity;
import com.idega.util.IWTimeStamp;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.Tournament;
import com.idega.data.EntityFinder;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.Member;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.BackButton;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.startingtime.data.TeeTime;
import com.idega.util.text.TextSoap;
import com.idega.jmodule.login.business.AccessControl;
import com.idega.util.idegaCalendar;
import is.idega.idegaweb.golf.entity.Group;
import com.idega.presentation.ui.Window;
import is.idega.idegaweb.golf.TableInfo;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.service.StartService;

import java.sql.SQLException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.text.DecimalFormat;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class TeetimeOptions extends is.idega.idegaweb.golf.templates.page.JmoduleWindowModuleWindow {

  public StartService service;

  public TeetimeOptions() {
    super();
    this.setWidth(520);
    this.setHeight(400);
    this.setName("Options");
    service = new StartService();
  }


public void main(IWContext iwc) throws Exception {
    super.main(iwc);

    if(iwc.getParameter("btnSkra.x") != null)
    {
            Form myForm = new Form();
            if( !storeConfig(iwc)){
                    setFeedBack(myForm, false);
            }
            else{
                    setFeedBack(myForm, true);
            }
    }
    else drawTable( iwc );


}

public DropdownMenu dropdownInterval(String dropdownName, int first, int next, int last )
{
	DropdownMenu myDropdown = new DropdownMenu(dropdownName);

	myDropdown.addMenuElement("0", "mín");
	myDropdown.addMenuElement(Integer.toString(first), Integer.toString(first));
	myDropdown.addMenuElement(Integer.toString(next), Integer.toString(next));
	myDropdown.addMenuElement(Integer.toString(last), Integer.toString(last));

	myDropdown.keepStatusOnAction();
	myDropdown.setSelectedElement("mín");
	myDropdown.setToSubmit();

	return myDropdown;
}

public HiddenInput insertHiddenInput(String inpName, String value, Form theForm){
	HiddenInput myObject = new HiddenInput(inpName, value);
	theForm.add( myObject );

	return myObject;
}

public DropdownMenu FieldDropdown(String dropdownName, IWContext iwc)throws IOException
{
	Field[] field = null;
	DropdownMenu myDropdown = new DropdownMenu(dropdownName);
	String fieldId = (String)iwc.getSession().getAttribute("field_id");


	try
	{
		if(isAdmin(((Member)iwc.getSession().getAttribute("member_login")).getID(),false,false))
			field = service.getStartingEntryField();
		else
			field = service.getFields((String)iwc.getSessionAttribute("member_main_union_id"));


		for(int i = 0; i < field.length; i++){
			myDropdown.addMenuElement(field[i].getID(), field[i].getName());
		}
		myDropdown.keepStatusOnAction();
		myDropdown.setToSubmit();
		myDropdown.setSelectedElement(fieldId);
	}
	catch (SQLException E) {
		E.printStackTrace();
        }
	return myDropdown;
}

public DropdownMenu insertDropdown(String dropdownName, int countFrom, int countTo)
{
	String from = Integer.toString(countFrom);
	DropdownMenu myDropdown = new DropdownMenu(dropdownName);

	for(; countFrom <= countTo; countFrom++)
	{
		myDropdown.addMenuElement(Integer.toString(countFrom), Integer.toString(countFrom));
	}
	myDropdown.keepStatusOnAction();

	return myDropdown;
}

public DropdownMenu insertDropdown_opentime(String dropdownName, int interval)
{
	DropdownMenu myDropdown = new DropdownMenu(dropdownName);

	int hour = 12;
	int min = 60 - interval;
	String Time, TimeRS;
	Vector timeVector = new Vector();
	Vector timeVectorRS = new Vector();

	while (hour > -1){

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

		if (min%10 == 0){
			timeVector.addElement( Time );
			timeVectorRS.addElement( TimeRS );
			}

		min -= interval;

		if (min < 0){
			min += 60;
			hour--;
		}
	}


		for (int i = timeVectorRS.size()-1; i >= 0; i--)
			myDropdown.addMenuElement( timeVectorRS.get(i).toString(), timeVector.get(i).toString());

			timeVectorRS.removeAllElements();
			timeVector.removeAllElements();


	return myDropdown;
}





public DropdownMenu insertDropdown_closetime(String dropdownName, int interval)
{
	DropdownMenu myDropdown = new DropdownMenu(dropdownName);

	int hour = 17;
	int min = 0 + interval;
	String Time, TimeRS;


	while (hour < 24){



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

		if (min%10 == 0)
			myDropdown.addMenuElement( TimeRS, Time);


		min += interval;

		if (min >= 60){
			min -= 60;
			hour++;
		}
	}


	return myDropdown;
}

public DropdownMenu insertDrowdown( String dropdownName, TableInfo myTableInfo, IWContext iwc) throws IOException
{

	DropdownMenu myDropdown = new DropdownMenu(dropdownName);
    int end = myTableInfo.get_row_num();
    int interval = myTableInfo.get_interval();
    int pic_min = myTableInfo.get_first_pic_min();
	int pic_hour = myTableInfo.get_first_pic_hour();
	int first_group = myTableInfo.get_first_group();
	String Time;
	String TimeVal;
	int val = 0;

 	for(int i = 1; i <= end; i += 2){

		if (pic_min >= 60){
			pic_min -= 60;
			pic_hour++;
		}

		if (pic_min < 10)
			Time = pic_hour + ":0" + pic_min;// +":00";
		else
			Time = pic_hour + ":" + pic_min;// +":00";

		val = (first_group + i)/2;
		TimeVal = Integer.toString(val);

		myDropdown.addMenuElement(TimeVal, Time);

		pic_min += interval;
	}
	myDropdown.keepStatusOnAction();
	return myDropdown;
 }


public TextInput insertEditBox(String name)
{
	TextInput myInput = new TextInput(name);
	return myInput;
}

public TextInput insertEditBox(String name, int size)
{
	TextInput myInput = new TextInput(name);
	myInput.setSize(size);
	return myInput;
}



public DropdownMenu insertDropdown(String dropdownName, idegaCalendar funcDate, IWContext iwc)
{

	IWTimeStamp stamp = new IWTimeStamp();

	DropdownMenu myDropdown = new DropdownMenu(dropdownName);

	for (int i = 0; i < 25 ; i++){
		myDropdown.addMenuElement(stamp.toSQLDateString() , stamp.getLocaleDate(iwc));//) getDateString(true,iwc));
		stamp.addDays(1);
	}

	myDropdown.keepStatusOnAction();
	myDropdown.setToSubmit();
	myDropdown.setSelectedElement(iwc.getSession().getAttribute("date").toString());

	return myDropdown;
}

/*
public void setGraphic(Form myForm)
{

	Table Header = new Table(2, 1);
	Header.setHeight("90");
	Header.setWidth(1, "10");

	Header.setCellpadding(0);
	Header.setCellspacing(0);
	this.setMarginWidth(0);
	this.setMarginHeight(0);
	this.setLeftMargin(0);
	this.setTopMargin(0);
	this.setScrollbar(true);

	Header.setBackgroundImage(2, 1, new Image(picUrl_1));
	Header.setBackgroundImage(1, 1, new Image(picUrl_2));

	Header.setWidth("100%");

	myForm.add(Header);

        this.setBackgroundColor("#8ab490");
}
*/

public boolean storeConfig(IWContext iwc)throws SQLException, IOException
{
	String BeginDate = "";

	try
	{
		StartingtimeFieldConfig conf = ((is.idega.idegaweb.golf.entity.StartingtimeFieldConfigHome)com.idega.data.IDOLookup.getHomeLegacy(StartingtimeFieldConfig.class)).createLegacy();
		String FieldID = iwc.getParameter("fieldID");
		String DaysShown = iwc.getParameter("daysShown");
                String pubReg = iwc.getParameter("public_reg");
		BeginDate = iwc.getParameter("beginDate");
		String Interval = iwc.getParameter("interval");
		String OpenTime = iwc.getParameter("openTime");
		String CloseTime = iwc.getParameter("closeTime");

                boolean pReg = (pubReg != null && pubReg.equals("true") );


		if(OpenTime == null || CloseTime == null)
			return false;

		IWTimeStamp beginDate = new IWTimeStamp(BeginDate);
		IWTimeStamp openTime = new IWTimeStamp(OpenTime);
		IWTimeStamp closeTime = new IWTimeStamp(CloseTime);

                //System.err.println("Select * from " + conf.getEntityName() + " WHERE begin_date ='"+beginDate.toSQLDateString()+"'");
                List oldRecord = EntityFinder.findAll(conf,"Select * from " + conf.getEntityName() + " WHERE begin_date ='"+beginDate.toSQLDateString()+"' and field_id = "+FieldID );


                if (oldRecord == null){
                  conf.setFieldID(new Integer(FieldID));
                  conf.setBeginDate(beginDate.getTimestamp());
                  conf.setOpenTime(openTime.getTimestamp());
                  conf.setCloseTime(closeTime.getTimestamp());
                  conf.setMinutesBetweenStart(new Integer(Interval));
                  conf.setDaysShown(new Integer(DaysShown));
                  conf.setPublicRegistration(pReg);

                  conf.insert();
                }else{
                  conf = (StartingtimeFieldConfig)oldRecord.get(0);
                  conf.setFieldID(new Integer(FieldID));
                  conf.setBeginDate(beginDate.getTimestamp());
                  conf.setOpenTime(openTime.getTimestamp());
                  conf.setCloseTime(closeTime.getTimestamp());
                  conf.setMinutesBetweenStart(new Integer(Interval));
                  conf.setDaysShown(new Integer(DaysShown));
                  conf.setPublicRegistration(pReg);

                  conf.update();
                }
	}
	catch (SQLException E) {
		return false;
    }
		return true;
}

public void setFeedBack(Form myForm, boolean isOk)
{
	//setGraphic(myForm);
	String btnCloseUrl = "/pics/rastimask/Takkar/TLoka1.gif";
	String btnBackUrl = "/pics/rastimask/Takkar/Ttilbaka1.gif";

	Table myTable = new Table(2,7);
	//Text txt = new Text("Skráning hefur ekki tekist. Hugsanlegar ástæður:", true, false, true)
	if(isOk){
		myTable.add(new Text("Skráning hefur tekist!", true, false, true), 2, 3);
		myTable.add(new CloseButton(new Image(btnCloseUrl)), 2, 7);
	}
	else{
		myTable.add(new Text("Skráning hefur ekki tekist. Hugsanlegar ástæður:", true, false, true), 2, 2);
		myTable.addText("* Stilling er til fyrir þessa dagsetningu á þessum velli", 2, 3);
		myTable.addText("* Ekki hefur verið valið bil á milli holla og opnunar og lokunartími", 2, 4);
		myTable.addText("- Að öðrum kosti, hafðu samband við idega", 2, 5);
		myTable.add(new BackButton(new Image(btnBackUrl)), 2, 7);
	}






	myTable.setWidth(1, "25");
	myTable.setAlignment("center");
	myTable.setCellpadding(0);
	myTable.setCellspacing(0);

	myForm.add(myTable);

	this.add(myForm);

}

public void drawTable(IWContext iwc)throws IOException
{
	idegaCalendar funcDate = new idegaCalendar();
	Table firstTable = new Table(4, 8);
	firstTable.mergeCells(1, 2, 2, 2);
	firstTable.setAlignment("center");
	Form myForm = new Form();
	//setGraphic(myForm);

	// Velja völl
	firstTable.addText(iwrb.getLocalizedString("start.options.field","Field"), 1, 1);
	firstTable.add(FieldDropdown("fieldID", iwc), 1, 2);

	// Velja dags. sem stillingar taka gildi
	firstTable.addText(iwrb.getLocalizedString("start.options.begindate","Begin date"), 3, 1);
	firstTable.add(insertDropdown("beginDate", funcDate, iwc), 3, 2);

	// Velja fjölda daga í skráningu
	firstTable.addText(iwrb.getLocalizedString("start.options.days_shown","Days shown"), 4, 1);
	firstTable.add(insertDropdown("daysShown", 1, 28 ), 4, 2);

	// Velja bil milli holla
	firstTable.addText(iwrb.getLocalizedString("start.options.interval","Interval"), 1, 4);
	firstTable.add(dropdownInterval("interval", 8, 10, 12 ), 1, 5);

        firstTable.add(new Text(iwrb.getLocalizedString("start.options.pubic_reg","Public registration")), 1, 7);
        firstTable.add(new CheckBox("public_reg","true"), 2, 7);


	if(iwc.getParameter("interval") != null)
	{
		String Bil = iwc.getParameter("interval");
		if(! Bil.equals("0")){
			firstTable.addText(iwrb.getLocalizedString("start.options.opentime","Opentime"), 3, 4);
			firstTable.add(insertDropdown_opentime("openTime", Integer.parseInt(Bil) ), 3, 5);
			firstTable.addText(iwrb.getLocalizedString("start.options.closetime","Closetime"), 4, 4);
			firstTable.add(insertDropdown_closetime("closeTime", Integer.parseInt(Bil) ), 4, 5);
		}
	}

// submit

	firstTable.add(new SubmitButton(iwrb.getImage("/buttons/register.gif","register"), "btnSkra"), 3, 8);
	firstTable.add(new CloseButton(iwrb.getImage("/buttons/cancel.gif","cancel")), 4, 8);



// stilli form, adda töflunni inn í það og forminu svo á síðuna.
	myForm.setMethod("post");
	//myForm.setAction("admin.jsp");
	myForm.add(firstTable);
	this.add(myForm);
}




public boolean isAdmin(int memberId, boolean clubadmin, boolean clubworker)throws SQLException{

	Member member = null;
	try{
		member = ((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKeyLegacy(memberId);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}

          if (member != null){
            String groupName = null;
            Group[] access = member.getGroups();
            for(int i = 0; i < access.length; i++){
                    groupName = access[i].getName();

                    if ("administrator".equals(groupName))
                    	return true;
                    if( clubadmin && "club_admin".equals(groupName))
                    	return true;
                    if( clubworker && "club_worker".equals(groupName))
                    	return true;
            }
          }

	return false;
}
















}

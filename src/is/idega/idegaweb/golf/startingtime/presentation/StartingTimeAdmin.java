/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.startingtime.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.TableInfo;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.service.StartService;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class StartingTimeAdmin extends GolfBlock {

	private StartService service = new StartService();

	public void main(IWContext modinfo) throws Exception {
		getParentPage().setTitle("Field configuration");
		try {
			if(modinfo.getRequest().getParameter("btnSkra.x") != null)
			{
				Form myForm = new Form();
				if( !storeConfig(modinfo)){
					setFeedBack(myForm, false);
				}
				else{
					setFeedBack(myForm, true);
				}
			}
			else drawTable(modinfo );
		}
		catch (Exception E) {
			E.printStackTrace();
		}
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
		theForm.addObject( myObject );

		return myObject;
	}

	public DropdownMenu FieldDropdown(String dropdownName, IWContext modinfo)throws IOException
	{
		Field[] field = null;
		DropdownMenu myDropdown = new DropdownMenu(dropdownName);
		String fieldId = (String)modinfo.getSession().getAttribute("field_id");


		try
		{
			if(isAdmin(((Member)modinfo.getSession().getAttribute("member_login")).getID(),false,false))
				field = service.getStartingEntryField();
			else
				field = service.getFields((String)modinfo.getSessionAttribute("member_main_union_id"));


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

	public DropdownMenu insertDrowdown( String dropdownName, TableInfo myTableInfo, IWContext modinfo) throws IOException
	{
		PrintWriter out = modinfo.getResponse().getWriter();

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

	public DropdownMenu insertDropdown(String dropdownName, IWCalendar funcDate, IWContext modinfo)
	{

		IWTimestamp stamp = new IWTimestamp();

		DropdownMenu myDropdown = new DropdownMenu(dropdownName);

		for (int i = 0; i < 25 ; i++){
			myDropdown.addMenuElement(stamp.toSQLDateString() , stamp.getLocaleDate(modinfo.getCurrentLocale()));
			stamp.addDays(1);
		}

		myDropdown.keepStatusOnAction();
		myDropdown.setToSubmit();
		myDropdown.setSelectedElement(modinfo.getSession().getAttribute("date").toString());

		return myDropdown;
	}


	public void setGraphic(Form myForm)
	{
		String picUrl_1 = "/pics/rastima_popup/stilling.gif";
		String picUrl_2 = "/pics/rastima_popup/tile.gif";

		Table Header = new Table(2, 1);
		Header.setHeight("90");
		Header.setWidth(1, "10");

		Header.setCellpadding(0);
		Header.setCellspacing(0);
		getParentPage().setMarginWidth(0);
		getParentPage().setMarginHeight(0);
		getParentPage().setLeftMargin(0);
		getParentPage().setTopMargin(0);

		Header.setBackgroundImage(2, 1, new Image(picUrl_1));
		Header.setBackgroundImage(1, 1, new Image(picUrl_2));

		Header.setWidth("100%");

		myForm.add(Header);

		getParentPage().setBackgroundColor("#8ab490");
	}


	public boolean storeConfig(IWContext modinfo)throws SQLException, IOException
	{
		String BeginDate = "";

		try
		{
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

	                boolean pReg = (pubReg != null && pubReg.equals("true") );
	                boolean nReg = (nonmemberReg != null && nonmemberReg.equals("true"));


			if(OpenTime == null || CloseTime == null)
				return false;

			IWTimestamp beginDate = new IWTimestamp(BeginDate);
			IWTimestamp openTime = new IWTimestamp(OpenTime);
			IWTimestamp closeTime = new IWTimestamp(CloseTime);

	                //System.err.println("Select * from " + conf.getEntityName() + " WHERE begin_date ='"+beginDate.toSQLDateString()+"'");
	                List oldRecord = EntityFinder.findAll(conf,"Select * from " + conf.getEntityName() + " WHERE begin_date ='"+beginDate.toSQLDateString()+"' and field_id = "+FieldID );


	                if (oldRecord == null){
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
	                }else{
	                  conf = (StartingtimeFieldConfig)oldRecord.get(0);
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
		}
		catch (SQLException E) {
			return false;
	    }
			return true;
	}

	public void setFeedBack(Form myForm, boolean isOk)
	{
		setGraphic(myForm);
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

		add(myForm);

	}

	public void drawTable(IWContext modinfo)throws IOException
	{
		IWCalendar funcDate = new IWCalendar();
		Table firstTable = new Table(5, 9);
		firstTable.mergeCells(1, 2, 2, 2);
		firstTable.setAlignment("center");
		Form myForm = new Form();
		setGraphic(myForm);

		// Velja völl
		firstTable.addText("Völlur", 1, 1);
		firstTable.add(FieldDropdown("fieldID", modinfo), 1, 2);

		// Velja dags. sem stillingar taka gildi
		firstTable.addText("Tekur gildi", 3, 1);
		firstTable.add(insertDropdown("beginDate", funcDate, modinfo), 3, 2);

		// Velja fjölda daga í skráningu
		firstTable.setAlignment(4,2,"right");
		firstTable.addText("Dagar í skráningu: félagar", 4, 1);
		firstTable.add(insertDropdown("daysShown", 1, 28 ), 4, 2);
		firstTable.addText("aðrir", 5, 1);
		firstTable.add(insertDropdown("daysShownNonMember", 1, 28 ), 5, 2);

		// Velja bil milli holla
		firstTable.addText("Bil milli holla", 1, 4);
		firstTable.add(dropdownInterval("interval", 8, 10, 12 ), 1, 5);

	        firstTable.add(new Text("Netskráning félagar"), 1, 7);
	        firstTable.add(new CheckBox("public_reg","true"), 2, 7);

	        firstTable.add(new Text("Netskráning aðrir"), 1, 8);
	        firstTable.add(new CheckBox("nonmember_reg","true"), 2, 8);

		if(modinfo.getRequest().getParameter("interval") != null)
		{
			String Bil = modinfo.getRequest().getParameter("interval");
			if(! Bil.equals("0")){
				firstTable.addText("Fyrsta holl:", 3, 4);
				firstTable.add(insertDropdown_opentime("openTime", Integer.parseInt(Bil) ), 3, 5);
				firstTable.addText("Síðasta holl:", 4, 4);
				firstTable.add(insertDropdown_closetime("closeTime", Integer.parseInt(Bil) ), 4, 5);
			}
		}

//	 submit
		String btnSkraUrl = "/pics/rastimask/Takkar/Tskra1.gif";
		String btnCancelUrl = "/pics/rastimask/Takkar/Thaetta-vid1.gif";
		firstTable.add(new SubmitButton(new Image(btnSkraUrl), "btnSkra"), 3, 9);
		firstTable.add(new CloseButton(new Image(btnCancelUrl)), 4, 9);



//	 stilli form, adda töflunni inn í það og forminu svo á síðuna.
		myForm.setMethod("post");
		//myForm.setAction("admin.jsp");
		myForm.add(firstTable);
		add(myForm);
	}




	public boolean isAdmin(int memberId, boolean clubadmin, boolean clubworker)throws SQLException{

		Member member = null;
		try{
			member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
		}
		catch(FinderException e)
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
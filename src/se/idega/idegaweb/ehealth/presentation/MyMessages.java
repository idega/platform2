/*
 * Created on 2004-okt-13
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;



import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.component.UIComponent;

import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Page;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;


/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MyMessages extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = "form_messages";
	private String keyCase = prefix + "case";
	private String keyCaseNo = prefix + "case_no";
	private String keyDate= prefix + "date";
	
	private String keyReceiver = prefix + "receiver";
	private String keySender = prefix + "sender";
	private String keyCareUnit = prefix + "care_unit";
	
	//private String prmReply = prefix + "reply";
	//private String prmClear = prefix + "clear";
	//private String prmSend = prefix + "send";
	private String prmDelete = prefix + "delete";
	
	private String prmFrom = prefix + "from";
	private String prmTo = prefix + "to";
	private String prmSearch = prefix + "search";
	
	private String keyMessage1U1 = prefix + "mess1U1";
	private String keyReply1U1 = prefix + "repl1U1";
	private String keyMessage2U1 = prefix + "mess2U1";
	private String keyReply2U1 = prefix + "repl2U1";
	private String keyMessage3U1 = prefix + "mess3U1";
	private String keyReply3U1 = prefix + "repl3U1";
	private String keyMessage4U1 = prefix + "mess4U1";
	private String keyReply4U1 = prefix + "repl4U1";
	private String keyMessage5U1 = prefix + "mess5U1";
	private String keyReply5U1 = prefix + "repl5U1";
	
	private String keyMessage1U2 = prefix + "mess1U2";
	private String keyReply1U2 = prefix + "repl1U2";
	private String keyMessage2U2 = prefix + "mess2U2";
	private String keyReply2U2 = prefix + "repl2U2";
	private String keyMessage3U2 = prefix + "mess3U2";
	private String keyReply3U2 = prefix + "repl3U2";
	private String keyMessage4U2 = prefix + "mess4U2";
	private String keyReply4U2 = prefix + "repl4U2";
	private String keyMessage5U2 = prefix + "mess5U2";
	private String keyReply5U2 = prefix + "repl5U2";
	
	private String keyMessage1A1 = prefix + "mess1A1";
	private String keyReply1A1 = prefix + "repl1A1";
	private String keyMessage2A1 = prefix + "mess2A1";
	private String keyReply2A1 = prefix + "repl2A1";
	private String keyMessage3A1 = prefix + "mess3A1";
	private String keyReply3A1 = prefix + "repl3A1";
	private String keyMessage4A1 = prefix + "mess4A1";
	private String keyReply4A1 = prefix + "repl4A1";
	private String keyMessage5A1 = prefix + "mess5A1";
	private String keyReply5A1 = prefix + "repl5A1";
	
	private int userID = -1;
	private User user;
	IWContext _iwc = null;
	
	public String name = null;
	public String fname = null;
	public String lname = null;
	Age age = null;
	
	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
		userID = iwc.getUserId();
		
		if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
			fname = user.getFirstName();
			if (fname == null)
				fname = "";
			lname = user.getLastName();
			if (lname == null)
				lname = "";
			name = fname + " " + lname;
		}
		else 
			name = "-";
		
		if (user != null && user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());
		else if (user != null && user.getPersonalID() != null)
			age = new Age(PIDChecker.getInstance().getDateFromPersonalID(user.getPersonalID()));
		
		add(getAppointmentHistoryForm());
		
	}
	
	
	
	public UIComponent getAppointmentHistoryForm(){
		Form myForm = new Form();
		myForm.setName(prmForm);
		Table T = new Table(1, 5);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setBorderColor("#000000");
		T.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_TOP);
		T.setVerticalAlignment(1, 4, Table.VERTICAL_ALIGN_BOTTOM);
		
		T.add(getSearchSortTable(), 1, 1);
		T.add(getHeadingTable(), 1, 2);
		T.add(getInfoLayer(), 1, 3);
		T.add(getTableButtons(), 1, 5);
		
		
		T.add(new Break(), 1, 3);
		T.setHeight(1, 4, "130");		
		T.setHeight(1, 5, "90");
		myForm.add(T);
		
		Page pVisit = this.getParentPage();
		if (pVisit != null) {
			Script S = pVisit.getAssociatedScript();
			pVisit.setOnLoad("setRowColor(document.getElementById('lay1_1'));");
			S.addFunction("setRowColor(obj)", setRowColorScript());
			Script timeScript = myForm.getAssociatedFormScript();
			if (timeScript == null) {
				timeScript = new Script();
				myForm.setAssociatedFormScript(timeScript);
			}
		}
		ArrayList info = new ArrayList();
		if (user !=null && user.getGroupID() == 142 ) {
			info.add(localize(keyMessage1A1, "Message"));
			info.add(localize(keyReply1A1, "Reply"));
			info.add(localize(keyMessage2A1, "Message"));
			info.add(localize(keyReply2A1, "Reply"));
			info.add(localize(keyMessage3A1, "Message"));
			info.add(localize(keyReply3A1, "Reply"));
			info.add(localize(keyMessage4A1, "Message"));
			info.add(localize(keyReply4A1, "Reply"));
			info.add(localize(keyMessage5A1, "Message"));
			info.add(localize(keyReply5A1, "Reply"));
		}
		else if (age != null && age.getYears() >= 70){
			info.add(localize(keyMessage1U1, "Message"));
			info.add(localize(keyReply1U1, "Reply"));
			info.add(localize(keyMessage2U1, "Message"));
			info.add(localize(keyReply2U1, "Reply"));
			info.add(localize(keyMessage3U1, "Message"));
			info.add(localize(keyReply3U1, "Reply"));
			info.add(localize(keyMessage4U1, "Message"));
			info.add(localize(keyReply4U1, "Reply"));
			info.add(localize(keyMessage5U1, "Message"));
			info.add(localize(keyReply5U1, "Reply"));
		}
		else {
			info.add(localize(keyMessage1U2, "Message"));
			info.add(localize(keyReply1U2, "Reply"));
			info.add(localize(keyMessage2U2, "Message"));
			info.add(localize(keyReply2U2, "Reply"));
			info.add(localize(keyMessage3U2, "Message"));
			info.add(localize(keyReply3U2, "Reply"));
			info.add(localize(keyMessage4U2, "Message"));
			info.add(localize(keyReply4U2, "Reply"));
			info.add(localize(keyMessage5U2, "Message"));
			info.add(localize(keyReply5U2, "Reply"));
		}
		
		
		Layer layer = new Layer(Layer.DIV);
		layer.setVisibility("hidden");
		layer.setOverflow("scroll");
		layer.setPositionType("absolute");
		layer.setWidth("610");
		layer.setHeight("150");
		layer.setMarkupAttribute("class", "ehealth_div");
		
		
		
		int theRow=1;
		Iterator iter = info.iterator();
		while (iter.hasNext()){
			Layer layers = (Layer) layer.clone();
			layers.setID("lay" + theRow + "_");	
			String text = (String) iter.next();
			layers.add(text);
						
			T.add(layers, 1, 3);
			theRow++;
		}
		
		return myForm;		
		
	}
	
		
	private Layer getInfoLayer(){
		Layer layerInfo = new Layer(Layer.DIV);
		layerInfo.setOverflow("scroll");
		layerInfo.setPositionType("relative");
		layerInfo.setWidth("610");
		layerInfo.setHeight("100");
		layerInfo.setMarkupAttribute("class", "ehealth_div");
		
		Table tableInfo = new Table(13, 10);
		tableInfo.setNoWrap();
		tableInfo.setCellpadding(0);
		tableInfo.setCellspacing(0);
		tableInfo.setBorder(0);			
		tableInfo.setWidth(570);
		int column = 1;
		tableInfo.setWidth(column++, 1, "15");
		tableInfo.setWidth(column++, 1, "5");
		tableInfo.setWidth(column++, 1, "45"); //case no
		tableInfo.setWidth(column++, 1, "5");
		tableInfo.setWidth(column++, 1, "110");
		tableInfo.setWidth(column++, 1, "5");
		tableInfo.setWidth(column++, 1, "100");
		tableInfo.setWidth(column++, 1, "5");
		tableInfo.setWidth(column++, 1, "100");
		tableInfo.setWidth(column++, 1, "5");
		tableInfo.setWidth(column++, 1, "110");
		tableInfo.setWidth(column++, 1, "5");
		tableInfo.setWidth(column++, 1, "90");
		
		tableInfo.setNoWrap();
		
		
		Image transpImg = Table.getTransparentCell(_iwc);
		transpImg.setWidth(5);
		transpImg.setHeight(16);
		
		
		Image greenArrow = getBundle(_iwc).getImage("greenArrow.gif");
		Image redArrow = getBundle(_iwc).getImage("redArrow.gif");
		
		Layer layer = new Layer(Layer.DIV);
		layer.setOnMouseOver("setRowColor(this);");
		layer.setPositionType("relative");
		layer.setHeight(16);
		
		
		int theRow = 1;
		int theColumn = 1;
		
		ArrayList cases = new ArrayList();
		ArrayList caseNo = new ArrayList();
		ArrayList dates = new ArrayList();		
		ArrayList receivers = new ArrayList();
		ArrayList senders = new ArrayList();
		ArrayList careunits = new ArrayList();
		
		if (user !=null && userID == 45 || userID == 46) {
			caseNo.add("0241");
			caseNo.add("0241");
			caseNo.add("0214");
			caseNo.add("0214");
			caseNo.add("0178");
			caseNo.add("0178");
			caseNo.add("0165");
			caseNo.add("0165");
			caseNo.add("0124");
			caseNo.add("0124");
			cases.add("Ont i foten");
			cases.add("Sv:Ont i foten");
			cases.add("Halsont");
			cases.add("Sv:Halsont");
			cases.add("B�ld p� smalb..");
			cases.add("Sv:B�ld p� sm..");
			cases.add("Feber och huv..");
			cases.add("Sv:Feber och..");
			cases.add("Feber");
			cases.add("Sv:Feber");
			dates.add("2004-10-11");
			dates.add("2004-10-12");
			dates.add("2004-10-06");
			dates.add("2004-10-07");
			dates.add("2004-06-15");
			dates.add("2004-06-17");
			dates.add("2004-02-07");
			dates.add("2004-02-08");
			dates.add("2003-12-16");
			dates.add("2003-12-19");
			receivers.add(name);
			receivers.add("Lasse Aronsson");
			receivers.add(name);
			receivers.add("Astrid Axelsson");
			receivers.add(name);
			receivers.add("Lasse Aronsson");
			receivers.add(name);
			receivers.add("Astrid Axelsson");
			receivers.add(name);
			receivers.add("Lasse Aronsson");
			receivers.add(name);
			senders.add("Lasse Aronsson");
			senders.add(name);
			senders.add("Astrid Axelsson");
			senders.add(name);
			senders.add("Lasse Aronsson");
			senders.add(name);
			senders.add("Astrid Axelsson");
			senders.add(name);
			senders.add("Lasse Aronsson");
			senders.add(name);
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");			
		}
		else if (age != null && age.getYears() >= 70){
			caseNo.add("0241");
			caseNo.add("0241");
			caseNo.add("0214");
			caseNo.add("0214");
			caseNo.add("0178");
			caseNo.add("0178");
			caseNo.add("0165");
			caseNo.add("0165");
			caseNo.add("0124");
			caseNo.add("0124");
			cases.add("Ont i foten");
			cases.add("Sv:Ont i foten");
			cases.add("Halsont");
			cases.add("Sv:Halsont");
			cases.add("B�ld p� smalb..");
			cases.add("Sv:B�ld p� sm..");
			cases.add("Feber och huv..");
			cases.add("Sv:Feber och..");
			cases.add("Feber");
			cases.add("Sv:Feber");
			dates.add("2004-10-11");
			dates.add("2004-10-12");
			dates.add("2004-10-06");
			dates.add("2004-10-07");
			dates.add("2004-06-15");
			dates.add("2004-06-17");
			dates.add("2004-02-07");
			dates.add("2004-02-08");
			dates.add("2003-12-16");
			dates.add("2003-12-19");
			receivers.add("Dr Magne Syhl");
			receivers.add(name);
			receivers.add("Dr Alve Don");
			receivers.add(name);
			receivers.add("Dr Magne Syhl");
			receivers.add(name);
			receivers.add("Dr Inga Pren");
			receivers.add(name);
			receivers.add("Dr Magne Syhl");
			receivers.add(name);
			senders.add(name);
			senders.add("Dr Magne Syhl");
			senders.add(name);
			senders.add("Dr Alve Don");
			senders.add(name);
			senders.add("Dr Magne Syhl");
			senders.add(name);
			senders.add("Dr Inga Pren");
			senders.add(name);
			senders.add("Dr Magne Syhl");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("�stberga VC");
			careunits.add("�stberga VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Flogsta VC");
			careunits.add("Flogsta VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			
			
		}
		else{
			caseNo.add("0241");
			caseNo.add("0241");
			caseNo.add("0214");
			caseNo.add("0214");
			caseNo.add("0178");
			caseNo.add("0178");
			caseNo.add("0165");
			caseNo.add("0165");
			caseNo.add("0124");
			caseNo.add("0124");
			cases.add("Ont i foten");
			cases.add("Sv:Ont i foten");
			cases.add("Halsont");
			cases.add("Sv:Halsont");
			cases.add("B�ld p� smalb..");
			cases.add("Sv:B�ld p� sm..");
			cases.add("Feber och huv..");
			cases.add("Sv:Feber och..");
			cases.add("Feber");
			cases.add("Sv:Feber");
			dates.add("2004-10-11");
			dates.add("2004-10-12");
			dates.add("2004-10-06");
			dates.add("2004-10-07");
			dates.add("2004-06-15");
			dates.add("2004-06-17");
			dates.add("2004-02-07");
			dates.add("2004-02-08");
			dates.add("2003-12-16");
			dates.add("2003-12-19");
			receivers.add("Dr Magne Syhl");
			receivers.add(name);
			receivers.add("Dr Alve Don");
			receivers.add(name);
			receivers.add("Dr Magne Syhl");
			receivers.add(name);
			receivers.add("Dr Inga Pren");
			receivers.add(name);
			receivers.add("Dr Magne Syhl");
			receivers.add(name);
			senders.add(name);
			senders.add("Dr Magne Syhl");
			senders.add(name);
			senders.add("Dr Alve Don");
			senders.add(name);
			senders.add("Dr Magne Syhl");
			senders.add(name);
			senders.add("Dr Inga Pren");
			senders.add(name);
			senders.add("Dr Magne Syhl");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("�stberga VC");
			careunits.add("�stberga VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Flogsta VC");
			careunits.add("Flogsta VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			
		}
		
		Iterator icaseNo = caseNo.iterator();
		Iterator icases = cases.iterator();		
		Iterator idates = dates.iterator();
		Iterator ireceivers = receivers.iterator();
		Iterator isenders = senders.iterator();
		Iterator icareunits = careunits.iterator();
		
					
		while (idates.hasNext()) {		
			
			for (theColumn = 1; theColumn <= 13; theColumn++) {
				Layer layers = (Layer) layer.clone();
				layers.setID("lay" + theRow + "_"+ theColumn);
				if (theColumn % 2 == 0){
					layers.add(transpImg);
					layers.setWidth("5");
				}
				else if (theColumn == 1){
					if (user !=null && user.getGroupID() == 142 ) {
						if (theRow % 2 == 0){
							layers.add(redArrow);
						}
						else
							layers.add(greenArrow);
					}
					else{
						if (theRow % 2 == 0){
							layers.add(greenArrow);
						}
						else
							layers.add(redArrow);
					}
						
				}
				
				else if (theColumn == 3){
					String theCaseNo = (String) icaseNo.next();
					layers.add(theCaseNo);
				}
				
				else if (theColumn == 5){
					String theCase = (String) icases.next();
					layers.add(theCase);
				}
				else if (theColumn == 7){
					String theDate = (String) idates.next();
					layers.add(theDate);
				}
				else if (theColumn == 9){
					String theReceiver = (String) ireceivers.next();
					layers.add(theReceiver);
				}
				else if (theColumn == 11){
					String theSender = (String) isenders.next();
					layers.add(theSender);
				}
				else if (theColumn == 13){
					String theCU = (String) icareunits.next();
					layers.add(theCU);
				}
				
				tableInfo.add(layers, theColumn, theRow);
			}
			theRow++;
			
		}
	
		layerInfo.add(tableInfo);
		
		return layerInfo;
	}
	
	private Layer getHeadingTable(){
		Layer layerHead = new Layer(Layer.DIV);
		layerHead.setMarkupAttribute("class", "ehealth_div_no_border");
		
		Table table = new Table(13, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth(570);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		int row = 3;
		int column = 1;
		table.setWidth(column++, 1, "15");
		table.setWidth(column++, 1, "5");
		table.setWidth(column++, 1, "45");
		table.setWidth(column++, 1, "5");
		table.setWidth(column++, 1, "110");
		table.setWidth(column++, 1, "5");
		table.setWidth(column++, 1, "100");
		table.setWidth(column++, 1, "5");
		table.setWidth(column++, 1, "100");
		table.setWidth(column++, 1, "5");
		table.setWidth(column++, 1, "110");
		table.setWidth(column++, 1, "5");
		table.setWidth(column++, 1, "90");
		
		Text tcase = getLocalizedSmallHeader(keyCase,"Case");
		Text tcaseNo = getLocalizedSmallHeader(keyCaseNo,"No");
		Text date = getLocalizedSmallHeader(keyDate,"Date");
		Text receiver = getLocalizedSmallHeader(keyReceiver,"Receiver");
		Text sender = getLocalizedSmallHeader(keySender,"Sender");
		Text careunit = getLocalizedSmallHeader(keyCareUnit,"Care unit");
		
		table.add(tcaseNo, row++, 1);
		row++;
		table.add(tcase, row++, 1);
		row++;
		table.add(date, row++, 1);
		row++;
		table.add(receiver, row++, 1);
		row++;
		table.add(sender, row++, 1);
		row++;
		table.add(careunit, row++, 1);	
		layerHead.add(table);
		
		return layerHead;
	}
	
	
	private String setRowColorScript() {
		StringBuffer s = new StringBuffer();
		
		
		s.append("function setRowColor(obj){").append(" \n\t");
		s.append("elementBase = obj.id.substring(0, 5);").append(" \n\t");
		s.append("if (elementBase.indexOf('_') <= 0)").append(" \n\t");
		s.append("elementBase = elementBase + '_';").append(" \n\t");
		s.append("for(i=1;i<document.all.tags('div').length;i++){").append(" \n\t");
		//hide the layers that contains all the info, the naming of the layers is lay1_, lay2_, lay3_ etc
		s.append("if (document.all.tags('div')[i].id.substr(document.all.tags('div')[i].id.length-1, 1) == '_'){").append(" \n\t");
		s.append("document.all.tags('div')[i].style.visibility = 'hidden'");
		s.append("}").append("\n\t");
		s.append("document.all.tags('div')[i].style.backgroundColor = '#ffffff';");
		s.append("}").append("\n\t");
		s.append("for (i = 1; i <= 13; i++){").append(" \n\t");
		s.append("elementName = eval(elementBase + i);").append(" \n\t");		
		s.append("document.getElementById(elementName.id).style.backgroundColor = '#CCCCCC';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("showlayer = eval(elementBase + '.id');").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'visible';").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
	
	private Table getTableButtons() {
		Table table = new Table(5, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		
		table.setWidth(2, 1, "10");
		table.setWidth(4, 1, "10");
		
		//GenericButton reply = getButton(new GenericButton("reply", localize(prmReply, "Reply")));
		GenericButton clear = getButton(new GenericButton("delete", localize(prmDelete, "Delete")));
		//GenericButton send = getButton(new GenericButton("send", localize(prmSend, "Send")));
				
		//table.add(reply, 1, 1);
		table.add(clear, 1, 1);
		//table.add(send, 5, 1);
		
		return table;
		
	}
	
	private Table getSearchSortTable(){
		
		Table table = new Table(3, 3);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
		
		table.setHeight(1, 1, "25");
		table.setHeight(1, 2, "25");
		table.setHeight(1, 3, "25");
		table.setWidth(2, 1, "20");
		
		IWTimestamp stamp = new IWTimestamp();
		
		DateInput from = (DateInput) getStyledInterface(new DateInput(prmFrom, true));
		from.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
		
		DateInput to = (DateInput) getStyledInterface(new DateInput(prmTo, true));
		to.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
		
		
		GenericButton search = getButton(new GenericButton("search", localize(prmSearch, "Search")));
		
		table.add(from, 1, 1);
		table.add(to, 3, 1);
		table.add(search, 1, 2);
		
		return table;
	}
	
	
}

/*
 * Created on 2004-okt-13
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;



import java.util.ArrayList;
import java.util.Iterator;

import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.Age;


/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MyInbox extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = "form_inbox";
	private String prmCase = prefix + "case";
	private String prmDate= prefix + "date";
	
	private String prmReceiver = prefix + "receiver";
	private String prmSender = prefix + "sender";
	private String prmCareUnit = prefix + "care_unit";
	
	private String prmReply = prefix + "reply";
	private String prmClear = prefix + "clear";
	private String prmDelete = prefix + "delete";
	private String prmSend = prefix + "send";
	
	private String keyReply1U1 = prefix + "repl1U1";
	private String keyReply2U1 = prefix + "repl2U1";
	private String keyReply3U1 = prefix + "repl3U1";
	private String keyReply4U1 = prefix + "repl4U1";
	private String keyReply5U1 = prefix + "repl5U1";
	
	private String keyReply1U2 = prefix + "repl1U2";
	private String keyReply2U2 = prefix + "repl2U2";
	private String keyReply3U2 = prefix + "repl3U2";
	private String keyReply4U2 = prefix + "repl4U2";
	private String keyReply5U2 = prefix + "repl5U2";
	
	private String keyMessage1U1 = prefix + "mess1U1";
	private String keyMessage3U1 = prefix + "mess3U1";
	private String keyMessage5U1 = prefix + "mess5U1";
	
	private String keyMessage2U2 = prefix + "mess2U2";
	private String keyMessage4U2 = prefix + "mess4U2";
	
	private int userID = -1;
	private User user;
	IWContext _iwc = null;
	Age age = null;
	public String name = null;

	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
		userID = iwc.getUserId();
		
		
		if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
			
			name = user.getFirstName() + " " + user.getLastName();
		}
		else 
			name = "-";
		
		if (user != null && user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());
		else if (user != null && user.getPersonalID() != null)
			age = new Age(PIDChecker.getInstance().getDateFromPersonalID(user.getPersonalID()));
		
		add(getAppointmentHistoryForm());
		
		
	}
	
	
	
	public PresentationObject getAppointmentHistoryForm(){
		Form myForm = new Form();
		myForm.setName(prmForm);
		Table T = new Table(1, 4);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setBorderColor("#000000");
		T.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_TOP);
		T.setVerticalAlignment(1, 4, Table.VERTICAL_ALIGN_BOTTOM);
		
	
		T.add(getHeadingTable(), 1, 1);
		T.add(getInfoLayer(), 1, 2);
		T.add(getTableButtons(), 1, 4);
		
		
		T.add(new Break(), 1, 3);
		T.setHeight(1, 3, "130");		
		T.setHeight(1, 4, "90");
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
		if (user !=null && userID == 45 || userID == 46) {
			info.add(localize(keyMessage1U1, "Message"));
			info.add(localize(keyMessage2U2, "Message"));
			info.add(localize(keyMessage3U1, "Message"));
			info.add(localize(keyMessage4U2, "Message"));
			info.add(localize(keyMessage5U1, "Message"));
			
		}
		else if (age != null && age.getYears() >= 70){
			info.add(localize(keyReply1U1, "Reply"));
			info.add(localize(keyReply2U1, "Reply"));
			info.add(localize(keyReply3U1, "Reply"));
			info.add(localize(keyReply4U1, "Reply"));
			info.add(localize(keyReply5U1, "Reply"));
			
		}
		else {
			info.add(localize(keyReply1U2, "Reply"));
			info.add(localize(keyReply2U2, "Reply"));
			info.add(localize(keyReply3U2, "Reply"));
			info.add(localize(keyReply4U2, "Reply"));
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
		
		Table tableInfo = new Table(9, 6);
		tableInfo.setNoWrap();
		tableInfo.setCellpadding(0);
		tableInfo.setCellspacing(0);
		tableInfo.setBorder(0);			
		tableInfo.setWidth(570);
		tableInfo.setWidth(1, 1, "100");
		tableInfo.setWidth(2, 1, "15");
		tableInfo.setWidth(3, 1, "70");
		tableInfo.setWidth(4, 1, "15");
		tableInfo.setWidth(5, 1, "100");
		tableInfo.setWidth(6, 1, "15");
		tableInfo.setWidth(7, 1, "120");
		tableInfo.setWidth(8, 1, "15");
	
		
		
		Image transpImg = Table.getTransparentCell(_iwc);
		transpImg.setWidth(15);
		transpImg.setHeight(13);
		
			
		Layer layer = new Layer(Layer.DIV);
		layer.setOnMouseOver("setRowColor(this);");
		layer.setPositionType("relative");
		layer.setHeight(13);
		
		
		int theRow = 1;
		int theColumn = 1;
		
		ArrayList cases = new ArrayList();
		ArrayList dates = new ArrayList();		
		ArrayList receivers = new ArrayList();
		ArrayList senders = new ArrayList();
		ArrayList careunits = new ArrayList();
		
		
		if (user !=null && userID == 45 || userID == 46) {
			cases.add("Ont i foten");
			cases.add("Halsont");
			cases.add("Böld på smalb..");
			cases.add("Feber och huv..");
			cases.add("Feber");
			dates.add("2004-10-11");
			dates.add("2004-10-06");
			dates.add("2004-06-15");
			dates.add("2004-02-07");
			dates.add("2003-12-16");
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			senders.add("Lasse Aronsson");
			senders.add("Astrid Axelsson");
			senders.add("Lasse Aronsson");
			senders.add("Astrid Axelsson");
			senders.add("Lasse Aronsson");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			careunits.add("Gimo VC");
			
		}
		else if (age != null && age.getYears() >= 70){
			cases.add("Sv:Ont i foten");
			cases.add("Sv:Halsont");
			cases.add("Sv:Böld på sm..");
			cases.add("Sv:Feber och..");
			cases.add("Sv:Feber");
			dates.add("2004-10-12");
			dates.add("2004-10-07");
			dates.add("2004-06-17");
			dates.add("2004-02-08");
			dates.add("2003-12-19");			
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			senders.add("Dr Magne Syhl");
			senders.add("Dr Inga Pren");
			senders.add("Dr Alve Don");
			senders.add("Dr Inga Pren");
			senders.add("Dr Magne Syhl");
			careunits.add("Gimo VC");
			careunits.add("Östberga VC");
			careunits.add("Flogsta VC");
			careunits.add("Östberga VC");
			careunits.add("Gimo VC");			
		}
		else{
			cases.add("Sv:Ont i foten");
			cases.add("Sv:Halsont");
			cases.add("Sv:Böld på sm..");
			cases.add("Sv:Feber och..");
			cases.add("Sv:Feber");
			dates.add("2004-10-12");
			dates.add("2004-10-07");
			dates.add("2004-06-17");
			dates.add("2004-02-08");
			dates.add("2003-12-19");			
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			receivers.add(name);
			senders.add("Dr Magne Syhl");
			senders.add("Dr Inga Pren");
			senders.add("Dr Alve Don");
			senders.add("Dr Inga Pren");
			senders.add("Dr Magne Syhl");
			careunits.add("Gimo VC");
			careunits.add("Östberga VC");
			careunits.add("Flogsta VC");
			careunits.add("Östberga VC");
			careunits.add("Gimo VC");	
		}
		
		Iterator icases = cases.iterator();
		Iterator idates = dates.iterator();
		Iterator ireceivers = receivers.iterator();
		Iterator isenders = senders.iterator();
		Iterator icareunits = careunits.iterator();
		
						
		while (idates.hasNext()) {
			
			for (theColumn = 1; theColumn <= 9; theColumn++) {
				Layer layers = (Layer) layer.clone();
				layers.setID("lay" + theRow + "_"+ theColumn);
				if (theColumn % 2 == 0){
					layers.add(transpImg);
					layers.setWidth("15");
				}
				else if (theColumn == 1){
					String theCase = (String) icases.next();
					layers.add(theCase);
				}
				else if (theColumn == 3){
					String theDate = (String) idates.next();
					layers.add(theDate);
				}
				else if (theColumn == 5){
					String theReceiver = (String) ireceivers.next();
					layers.add(theReceiver);
				}
				else if (theColumn == 7){
					String theSender = (String) isenders.next();
					layers.add(theSender);
				}
				else if (theColumn == 9){
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
		
		Table table = new Table(9, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth(570);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		
		table.setWidth(1, 1, "100");
		table.setWidth(2, 1, "15");
		table.setWidth(3, 1, "70");
		table.setWidth(4, 1, "15");
		table.setWidth(5, 1, "100");
		table.setWidth(6, 1, "15");
		table.setWidth(7, 1, "120");
		table.setWidth(8, 1, "15");
		
		Text tcase = getLocalizedSmallHeader(prmCase,"Case");
		Text date = getLocalizedSmallHeader(prmDate,"Date");
		Text receiver = getLocalizedSmallHeader(prmReceiver,"Receiver");
		Text sender = getLocalizedSmallHeader(prmSender,"Sender");
		Text careunit = getLocalizedSmallHeader(prmCareUnit,"Care unit");
		
				
		table.add(tcase, 1, 1);
		table.add(date, 3, 1);
		table.add(receiver, 5, 1);
		table.add(sender, 7, 1);
		table.add(careunit, 9, 1);		
		layerHead.add(table);
		
		return layerHead;
	}
	
	
	private String setRowColorScript() {
		StringBuffer s = new StringBuffer();
		
		
		s.append("function setRowColor(obj){").append(" \n\t");
		s.append("elementBase = obj.id.substring(0, 5);").append(" \n\t");
		s.append("for(i=1;i<document.all.tags('div').length;i++){").append(" \n\t");
		s.append("if (document.all.tags('div')[i].id.length == 5){").append(" \n\t");
		s.append("document.all.tags('div')[i].style.visibility = 'hidden'");
		s.append("}").append("\n\t");
		s.append("document.all.tags('div')[i].style.backgroundColor = '#ffffff';");
		s.append("}").append("\n\t");
		s.append("for (i = 1; i <= 9; i++){").append(" \n\t");
		s.append("elementName = eval(elementBase + i);").append(" \n\t");		
		s.append("document.getElementById(elementName.id).style.backgroundColor = '#CCCCCC';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("showlayer = eval(elementBase + '.id');").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'visible';").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
	
	private Table getTableButtons() {
		Table table = new Table(5, 2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_BOTTOM);
		
		table.setWidth(2, 1, "10");
		table.setWidth(4, 1, "10");
		table.setHeight(1, 2, "25");
		table.mergeCells(1, 2, 2, 2);
		
		GenericButton reply = getButton(new GenericButton("reply", localize(prmReply, "Reply")));
		GenericButton clear = getButton(new GenericButton("clear", localize(prmClear, "Clear")));
		GenericButton send = getButton(new GenericButton("send", localize(prmSend, "Send")));
		GenericButton delete = getButton(new GenericButton("delete", localize(prmDelete, "Delete")));
				
		table.add(reply, 1, 1);
		table.add(clear, 3, 1);
		table.add(send, 5, 1);
		table.add(delete, 1, 2);
		
		return table;
		
	}
	
	
}

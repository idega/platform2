/*
 * Created on 2004-okt-21
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;



import javax.faces.component.UIComponent;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Page;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;


/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AdminChangeDoctorHealthCentre extends EHealthBlock {
	
	private String prefix = "admin_";
	private String prmForm = prefix + "form_doctor";
	private String prmMessage = prefix + "prm_mess";
	
	private String keyAcceptAppl = prefix + "accept_doc_applic";
	private String keyDeclineAppl = prefix + "decline_doc_applic";
	private String keyConfirmAppl = prefix + "confirm";
	private String keyStandardAnswer = prefix + "standard_answ";
	private String keyInfoMessage = prefix + "info_message";
	private String keyMessAccept = prefix + "message_accept";
	private String keyMessDecline = prefix + "message_decline";
	private String keyDate = prefix + "date";
	private String keyApplicant = prefix + "applicant";
	private String keyStatus = prefix + "status";
	
	
	private int userID = -1;
	private User user;
	IWContext _iwc = null;

	public String name = null;
	public String fname = null;
	public String lname = null;
	
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
		
		add(getAdminForm());
		
	}
	
	
	
	public UIComponent getAdminForm(){
		Form myForm = new Form();
		myForm.setName(prmForm);
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setBorderColor("#000000");
		T.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_TOP);
		T.setVerticalAlignment(1, 4, Table.VERTICAL_ALIGN_BOTTOM);
		
		T.add(getHeadingTable(), 1, 2);
		T.add(getInfoLayer(), 1, 3);
		T.add(getMessageTable(), 1, 4);
		
		T.add(new Break(), 1, 3);
		T.setWidth(1, 3, "200");
		T.setHeight(1, 3, "190");		
		T.setHeight(1, 4, "100");
		myForm.add(T);
		
		Page pVisit = this.getParentPage();
		if (pVisit != null) {
			Script S = pVisit.getAssociatedScript();
			S.addFunction("setTextAreaVal(obj)", setTextAreaValueScript());
			pVisit.setOnLoad("setRowColor(document.getElementById('lay1_1'));");			
			S.addFunction("setRowColor(obj)", setRowColorScript());
			
			Script timeScript = myForm.getAssociatedFormScript();
			if (timeScript == null) {
				timeScript = new Script();
				myForm.setAssociatedFormScript(timeScript);
			}
		}
		
		String infoDiv[] = {"14 oktober<br>Astrid Axelsson<br><b>Meddelande</b><br>Lorum ispum dolor sit amet kol ase.",
				"8 oktober<br>Ulla Bengtsson<br><b>Meddelande</b><br>Lorum ispum dolor sit amet kol ase.",
				"5 oktober<br>Ivar Johansson<br><b>Meddelande</b><br>Lorum ispum dolor sit amet kol ase.",
				"7 september<br>Rut Bergqvist<br><b>Meddelande</b><br>Lorum ispum dolor sit amet kol ase.",
				"6 september<br>Siw Andersson<br><b>Meddelande</b><br>Lorum ispum dolor sit amet kol ase."};
				
		Layer layer = new Layer(Layer.DIV);
		layer.setVisibility("hidden");
		layer.setPositionType("absolute");
		layer.setOverflow("auto");
		layer.setWidth("400");
				
		int theRow;
		for (theRow = 1; theRow <= 5; theRow++) {
			Layer layers = (Layer) layer.clone();
			layers.setID("lay" + theRow + "_");			
			layers.add(infoDiv[theRow-1]);
			
			T.add(layers, 1, 3);
		}
		
		
		return myForm;
		
		
	}
	
	private Table getMessageTable(){
		Table table = new Table(4, 4);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.mergeCells(1, 1, 4, 1);
		table.mergeCells(1, 2, 4, 2);
		
		table.setHeight(1, 3, "25");
		table.setHeight(1, 4, "25");
		table.setWidth(2, 3, "10");
		table.setWidth(4, 4, "100");
		
		table.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(3, 3, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 4, Table.VERTICAL_ALIGN_BOTTOM);
		
		Text infoMessage = getLocalizedText(keyInfoMessage,"The message which is sent when accepting or declining a patient");
		
		TextArea textMessage = (TextArea) getStyledInterface(new TextArea(prmMessage));
		textMessage.setRows(6);
		textMessage.setStyleClass("lul_text_medium");
		textMessage.setValue(localize(keyStandardAnswer, "Standard answer..."));
		
		GenericButton accept = getButton(new GenericButton("accept_appl", localize(keyAcceptAppl, "Accept application")));
		accept.setOnClick("javascript:setTextAreaVal('acc');");
		GenericButton decline = getButton(new GenericButton("decl_appl", localize(keyDeclineAppl, "Decline application")));
		decline.setOnClick("javascript:setTextAreaVal('dec');");
		
		SubmitButton confirm = (SubmitButton) getStyledInterface(new SubmitButton("confirm"));
		confirm.setValue(localize(keyConfirmAppl,"Confirm"));
		confirm.setOnClick("alert('Ditt svar har skickats till berörd patient')");
		
		table.add(infoMessage, 1, 1);
		table.add(textMessage, 1, 2);
		table.add(accept, 1, 3);
		table.add(decline, 3, 3);
		table.add(confirm, 1, 4);
		
		
		return table;
		
	}
	
	private Layer getInfoLayer(){
		Layer layerInfo = new Layer(Layer.DIV);
		layerInfo.setOverflow("scroll");
		layerInfo.setPositionType("relative");
		layerInfo.setWidth("500");
		layerInfo.setHeight("75");
		layerInfo.setMarkupAttribute("class", "ehealth_div");
		
		
		Table tableInfo = new Table(5, 6);
		tableInfo.setNoWrap();
		tableInfo.setCellpadding(0);
		tableInfo.setCellspacing(0);
		tableInfo.setBorder(0);			
		tableInfo.setWidth(450);
		tableInfo.setWidth(1, 1, "100");
		tableInfo.setWidth(2, 1, "20");
		tableInfo.setWidth(3, 1, "110");
		tableInfo.setWidth(4, 1, "20");
		tableInfo.setWidth(5, 1, "200");
		
		Image transpImg = Table.getTransparentCell(_iwc);
		transpImg.setWidth(20);
		transpImg.setHeight(13);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setOnMouseOver("setRowColor(this);");
		layer.setPositionType("relative");
		layer.setHeight(13);
		
		int theRow = 1;
		int theColumn = 1;
		
		String dates[] = {"2004-10-14", "2004-10-08", "2004-10-05", "2004-09-07", "2004-09-06"};
		String vcs[] = {"Astrid Axelsson", "Ulla Bengtsson", "Ivar Johansson", "Rut Bergqvist", "Siw Andersson"};
		String visitypes[] = {"Ny", "Beviljad", "Avslagen","Avslagen", "Beviljad"};
		
		
		for (theRow = 1; theRow <= 5; theRow++) {
			
			for (theColumn = 1; theColumn <= 5; theColumn++) {
				Layer layers = (Layer) layer.clone();
				layers.setID("lay" + theRow + "_"+ theColumn);
				if (theColumn % 2 == 0){
					layers.add(transpImg);
					layers.setWidth("20");
				}
				else if (theColumn == 1){
					layers.add(dates[theRow-1]);
				}
				else if (theColumn == 3){
					layers.add(vcs[theRow-1]);
				}
				else if (theColumn == 5){
					layers.add(visitypes[theRow-1]);
				}
				
				
				tableInfo.add(layers, theColumn, theRow);
			}
			
		}
		
		layerInfo.add(tableInfo);
		
		return layerInfo;
	}
	
	private Layer getHeadingTable(){
		Layer layerHead = new Layer(Layer.DIV);
		layerHead.setMarkupAttribute("class", "ehealth_div_no_border");
		
		Table table = new Table(5, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		
		table.setWidth(450);
		table.setWidth(1, 1, "100");
		table.setWidth(2, 1, "20");
		table.setWidth(3, 1, "110");
		table.setWidth(4, 1, "20");
		table.setWidth(5, 1, "200");
		
		Text date = getLocalizedSmallHeader(keyDate,"Date");
		Text applicant = getLocalizedSmallHeader(keyApplicant,"Applicant");
		Text status = getLocalizedSmallHeader(keyStatus,"Status");
		
		table.add(date, 1, 1);
		table.add(applicant, 3, 1);
		table.add(status, 5, 1);
		
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
		s.append("for (i = 1; i <= 5; i++){").append(" \n\t"); // i <= this is the number of columns
		s.append("elementName = eval(elementBase + i);").append(" \n\t");		
		s.append("document.getElementById(elementName.id).style.backgroundColor = '#CCCCCC';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("showlayer = eval(elementBase + '.id');").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'visible';").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
	
	private String setTextAreaValueScript() {
		StringBuffer s = new StringBuffer();
		
		
		s.append("function setTextAreaVal(obj){").append(" \n\t");
		s.append("var messageArea = document.").append(prmForm).append(".").append(prmMessage).append("; \n\t");
		
		s.append("if (obj == 'acc'){").append(" \n\t");
		s.append("messageArea").append(".value = '").append(localize(keyMessAccept, "Your request regarding change of house doctor has been accepted...")).append("';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("else if (obj == 'dec'){").append(" \n\t");
		s.append("messageArea").append(".value = '").append(localize(keyMessDecline, "We are sorry that we connot offer you a ...")).append("';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t\t");
		
		return s.toString();
	}
	
	
		
}

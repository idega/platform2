/*
 * Created on 2004-okt-04
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;


import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Registration extends EHealthBlock {
	
	private String prefix = "registration_";
	private String prmForm = prefix + "the_frm";
	
	private String prmCareUnit = prefix + "care_unit";
	private String prmDate = prefix + "date";
	private String prmResponsibleDr = prefix + "resp_dr";
	private String prmReason = prefix + "reason";
	private String prmLayer = prefix + "layer";
	

	public void main(IWContext iwc) throws Exception {
		
		add(getVisitForm(iwc));
		
		
	}
	
	
	//public PresentationObject getVisitForm(IWContext iwc, User userVK) throws java.rmi.RemoteException {
	public PresentationObject getVisitForm(IWContext iwc) throws java.rmi.RemoteException {
		Form myForm = new Form();
		myForm.setName("form_visit");
		Table T = new Table(1, 2);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		
		
		Table table = new Table(7, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		//table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_BOTTOM);
		
		
		table.setWidth(550);
		
		
		myForm.add(table);
						
			Text date = getLocalizedText(prmDate,"Date");
			Text careUnit = getLocalizedText(prmCareUnit,"Care unit");
			Text respDr = getLocalizedText(prmResponsibleDr,"Responsible Dr");
			Text regReason = getLocalizedText(prmReason,"Reason for registration");

			table.setWidth(2, 1, "20");
			table.setWidth(4, 1, "20");
			table.setWidth(6, 1, "20");
						
			
			table.add(date, 1, 1);
			table.add(careUnit, 3, 1);
			table.add(respDr, 1, 5);
			table.add(regReason, 7, 1);
			
			
			T.add(table, 1, 1);
			
			Layer layerInfo = new Layer(prmLayer);
			layerInfo.setOverflow("scroll");
			layerInfo.setWidth(550);
			layerInfo.setHeight(50);
			
			Table tableInfo = new Table(7, 4);
			tableInfo.setCellpadding(0);
			tableInfo.setCellspacing(0);
			tableInfo.setBorder(0);			
			table.setWidth(550);
			table.setWidth(1, 1, "50");
			table.setWidth(2, 1, "20");
			table.setWidth(3, 1, "100");
			table.setWidth(4, 1, "20");
			table.setWidth(5, 1, "100");
			table.setWidth(6, 1, "20");
			table.setWidth(7, 1, "100");
			
			
			
			layerInfo.add(table);
		
		return myForm;
	}
	
}

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
import com.idega.presentation.TableCell;
import com.idega.presentation.text.Break;
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
	private String prmForm = prefix + "form_visit";
	
	private String prmCareUnit = prefix + "care_unit";
	private String prmDate = prefix + "date";
	private String prmResponsibleDr = prefix + "resp_dr";
	private String prmReason = prefix + "reason";
	private String prmLayer = prefix + "layer";
	

	public void main(IWContext iwc) throws Exception {
		add(getRegistrationInfoForm(iwc));
		
		
	}
	
	
		
	public PresentationObject getRegistrationInfoForm(IWContext iwc) throws java.rmi.RemoteException {
		Form myForm = new Form();
		myForm.setName(prmForm);
		Table T = new Table(1, 2);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setBorderColor("#000000");
		
		
		
		Table table = new Table(7, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		
		table.setWidth(550);
		table.setWidth(1, 1, "70");
		table.setWidth(2, 1, "20");
		table.setWidth(3, 1, "120");
		table.setWidth(4, 1, "20");
		table.setWidth(5, 1, "150");
		table.setWidth(6, 1, "20");
		table.setWidth(7, 1, "150");
		
		
	
						
			Text date = getLocalizedText(prmDate,"Date");
			Text careUnit = getLocalizedText(prmCareUnit,"Care unit");
			Text respDr = getLocalizedText(prmResponsibleDr,"Responsible Dr");
			Text regReason = getLocalizedText(prmReason,"Reason for registration");
			
			table.add(date, 1, 1);
			table.add(careUnit, 3, 1);
			table.add(respDr, 5, 1);
			table.add(regReason, 7, 1);
			
			
			T.add(table, 1, 1);
			
			Layer layerInfo = new Layer(Layer.DIV);
			layerInfo.setOverflow("scroll");
			layerInfo.setMarkupAttribute("overflow-x", "visible");
			layerInfo.setPositionType("absolute");
			layerInfo.setWidth("580");
			layerInfo.setHeight("70");
			layerInfo.setMarkupAttribute("class", "lul_div");
			
			int row = 1;
			Table tableInfo = new Table(7, 6);
			tableInfo.setCellpadding(0);
			tableInfo.setCellspacing(0);
			tableInfo.setBorder(0);			
			tableInfo.setWidth(550);
			tableInfo.setWidth(1, 1, "70");
			tableInfo.setWidth(2, 1, "20");
			tableInfo.setWidth(3, 1, "120");
			tableInfo.setWidth(4, 1, "20");
			tableInfo.setWidth(5, 1, "150");
			tableInfo.setWidth(6, 1, "20");
			tableInfo.setWidth(7, 1, "150");
			
			
			
			tableInfo.add("2004-10-04", 1, row);
			tableInfo.add("Vårdenheten A", 3, row);
			tableInfo.add("Dr Magne Cyl", 5, row);
			tableInfo.add("Operation", 7, row);
			row++;
			tableInfo.add("2004-10-04", 1, row);
			tableInfo.add("Vårdenheten A", 3, row);
			tableInfo.add("Dr Magne Cyl", 5, row);
			tableInfo.add("Operation", 7, row);
			row++;
			tableInfo.add("2004-10-04", 1, row);
			tableInfo.add("Vårdenheten A", 3, row);
			tableInfo.add("Dr Magne Cyl", 5, row);
			tableInfo.add("Operation", 7, row);
			row++;
			tableInfo.add("2004-10-04", 1, row);
			tableInfo.add("Vårdenheten A", 3, row);
			tableInfo.add("Dr Magne Cyl", 5, row);
			tableInfo.add("Operation", 7, row);
			row++;
			tableInfo.add("2004-10-04", 1, row);
			tableInfo.add("Vårdenheten A", 3, row);
			tableInfo.add("Dr Magne Cyl", 5, row);
			tableInfo.add("Operation", 7, row);
			
			layerInfo.add(tableInfo);
			
			T.add(layerInfo, 1, 2);
			myForm.add(T);
		return myForm;
	}
	
}

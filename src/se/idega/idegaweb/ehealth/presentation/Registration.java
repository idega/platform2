/*
 * Created on 2004-okt-04
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;


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
	private String prmInfo = prefix + "info";
	
	IWContext _iwc = null;

	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
		add(getRegistrationInfoForm());
		
	}
	
	
		
	public PresentationObject getRegistrationInfoForm() {
		Form myForm = new Form();
		myForm.setName(prmForm);
		Table T = new Table(1, 4);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setBorderColor("#000000");		
		T.add(getHeadingTable(), 1, 1);
		T.add(getInfoLayer(), 1, 2);
		T.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_TOP);
		T.setVerticalAlignment(1, 4, Table.VERTICAL_ALIGN_BOTTOM);
		
		T.add(new Break(), 1, 3);
		T.setHeight(1, 3, "80");		
		T.setHeight(1, 4, "20");
		
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
			
			String infoDiv[] = {"11 oktober, kl 10.30<br>Gimo VC, Dr Magne Syhl<br>Anledning: operation",
					"23 juni, kl 11.30<br>Gimo VC, Dr Magne Syhl<br>Anledning: operation",
					"15 februari, kl 09.00<br>Gimo VC, Dr Magne Syhl<br>Anledning: operation",
					"7 februari, kl 13.30<br>Gimo VC, Dr Magne Syhl<br>Anledning: operation",
			"18 december, kl 10.00<br>Gimo VC, Dr Magne Syhl<br>Anledning: operation"};
			
			Layer layer = new Layer(Layer.DIV);
			layer.setVisibility("hidden");
			layer.setPositionType("absolute");
			
			
			
			int theRow;
			for (theRow = 1; theRow <= 5; theRow++) {
				Layer layers = (Layer) layer.clone();
				layers.setID("lay" + theRow + "_");			
				layers.add(infoDiv[theRow-1]);
				
				T.add(layers, 1, 3);
			}
			
			
			GenericButton info = getButton(new GenericButton("info", localize(prmInfo, "Info")));
			T.add(info, 1, 4);
			
		return myForm;
	}
	
	private Layer getInfoLayer(){
		Layer layerInfo = new Layer(Layer.DIV);
		layerInfo.setOverflow("scroll");
		layerInfo.setPositionType("relative");
		layerInfo.setWidth("610");
		layerInfo.setHeight("75");
		layerInfo.setMarkupAttribute("class", "ehealth_div");
		
		
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
		
		
		
		Image transpImg = Table.getTransparentCell(_iwc);
		transpImg.setWidth(20);
		transpImg.setHeight(13);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setOnMouseOver("setRowColor(this);");
		layer.setPositionType("relative");
		
		
		int theRow = 1;
		int theColumn = 1;
		
		String dates[] = {"2004-10-11", "2004-06-23", "2004-02-15", "2004-02-07", "2003-12-18"};
		String vcs[] = {"Gimo VC", "Gimo VC", "Gimo VC", "Gimo VC", "Gimo VC"};
		String doctors[] = {"Dr Magne Cyl", "Dr Magne Cyl", "Dr Magne Cyl", "Dr Magne Cyl", "Dr Magne Cyl"};
		String costs[] = {"Operation", "Operation", "Operation", "Operation", "Operation"};
		
		
		for (theRow = 1; theRow <= 5; theRow++) {
			
			for (theColumn = 1; theColumn <= 7; theColumn++) {
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
					layers.add(doctors[theRow-1]);
				}
				else if (theColumn == 7){
					layers.add(costs[theRow-1]);
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
		s.append("for (i = 1; i <= 7; i++){").append(" \n\t");
		s.append("elementName = eval(elementBase + i);").append(" \n\t");		
		s.append("document.getElementById(elementName.id).style.backgroundColor = '#CCCCCC';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("showlayer = eval(elementBase + '.id');").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'visible';").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
}

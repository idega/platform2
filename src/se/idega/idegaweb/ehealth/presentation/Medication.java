/*
 * Created on 2004-okt-11
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
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Medication extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_medication";
	
	private String prmMedicName = prefix + "medicin_name";
	private String prmMedicForm = prefix + "form";
	private String prmDose = prefix + "dose";
	private String prmPrescCareGiver = prefix + "prescr_care_giver";
	private String prmDateActive = prefix + "date_for_activation";
	private String prmRenewReceipe = prefix + "renew_receipe";
	private String prmLinkFass = prefix + "link_to_fass";
	private String prmShow = prefix + "visa";
	
	
	
	IWContext _iwc = null;

	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
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
		
		T.add(getSearchSortTable(), 1, 1);
		T.add(getHeadingTable(), 1, 2);
		T.add(getInfoLayer(), 1, 3);
		T.add(getTableButtons(), 1, 4);
		
		T.add(new Break(), 1, 3);
		T.setHeight(1, 3, "160");		
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
		
		String infoDiv[] = {"<b>Dosering</b><br>1 st 1 gånger dagligen.<br><br><b>Övrig information</b><br>Bör tas i samband med mat",
				"<b>Dosering</b><br>1 + 1 + 1 gånger dagligen.<br><br><b>Övrig information</b><br>Morgon, middag, kväll",
				"<b>Dosering</b><br>1 + 1 + 1 gånger dagligen.<br><br><b>Övrig information</b><br>Bör tas i samband med mat",
				"<b>Dosering</b><br>2 st 4 dagligen.<br><br><b>Övrig information</b><br>",
				"<b>Dosering</b><br>1 + 1 + 1 gånger dagligen.<br><br><b>Övrig information</b><br>Bör tas i samband med mat"};
		
		Layer layer = new Layer(Layer.DIV);
		layer.setVisibility("hidden");
		layer.setOverflow("scroll");
		layer.setPositionType("absolute");
		layer.setWidth("610");
		layer.setHeight("100");
		layer.setMarkupAttribute("class", "ehealth_div");
		
		
		
		int theRow;
		for (theRow = 1; theRow <= 5; theRow++) {
			Layer layers = (Layer) layer.clone();
			layers.setID("lay" + theRow + "_");	
			layers.add(infoDiv[theRow-1]);
						
			T.add(layers, 1, 3);
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
		
		Table tableInfo = new Table(11, 6);
		tableInfo.setNoWrap();
		tableInfo.setCellpadding(0);
		tableInfo.setCellspacing(0);
		tableInfo.setBorder(0);			
		tableInfo.setWidth(570);
		tableInfo.setWidth(1, 1, "88");
		tableInfo.setWidth(2, 1, "15");
		tableInfo.setWidth(3, 1, "60");
		tableInfo.setWidth(4, 1, "15");
		tableInfo.setWidth(5, 1, "60");
		tableInfo.setWidth(6, 1, "15");
		tableInfo.setWidth(7, 1, "100");
		tableInfo.setWidth(8, 1, "15");
		tableInfo.setWidth(10, 1, "15");
		
		
		Image transpImg = Table.getTransparentCell(_iwc);
		transpImg.setWidth(20);
		transpImg.setHeight(13);
		
		GenericButton renew = getButton(new GenericButton("renew", localize(prmRenewReceipe, "Renew receipe")));
		
		Layer layer = new Layer(Layer.DIV);
		layer.setOnMouseOver("setRowColor(this);");
		layer.setPositionType("relative");
		layer.setHeight(13);
		
		
		int theRow = 1;
		int theColumn = 1;
		
		
		
		String medicName[] = {"Furix", "Salazoppyrin", "Salazoppyrin", "Alvedon", "Furix"};
		String form[] = {"Tabl", "Tabl", "Tabl", "Tabl", "Tabl", "Tabl"};
		String dose[] = {"20 mg", "500 mg", "500 mg", "500 mg", "20 mg"};
		String caregivers[] = {"Dr Magne Syhl", "Dr Alve Don", "Dr Inga Pren", "Dr Alve Don", "Dr Alve Don"};
		String dates[] = {"2004-10-11", "2004-10-06", "2004-06-15", "2004-02-07", "2003-12-16"};
		
			
				
		for (theRow = 1; theRow <= 5; theRow++) {
			
			for (theColumn = 1; theColumn <= 11; theColumn++) {
				Layer layers = (Layer) layer.clone();
				layers.setID("lay" + theRow + "_"+ theColumn);
				if (theColumn % 2 == 0){
					layers.add(transpImg);
					layers.setWidth("20");
				}
				else if (theColumn == 1){
					layers.add(medicName[theRow-1]);
				}
				else if (theColumn == 3){
					layers.add(form[theRow-1]);
				}
				else if (theColumn == 5){
					layers.add(dose[theRow-1]);
				}
				else if (theColumn == 7){
					layers.add(caregivers[theRow-1]);
				}
				else if (theColumn == 9){
					layers.add(dates[theRow-1]);
				}
				else if (theColumn == 11){
					layers.add(renew);
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
		
		Table table = new Table(9, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth(570);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		
		table.setWidth(1, 1, "88");
		table.setWidth(2, 1, "20");
		table.setWidth(3, 1, "60");
		table.setWidth(4, 1, "20");
		table.setWidth(5, 1, "60");
		table.setWidth(6, 1, "20");
		table.setWidth(7, 1, "100");
		table.setWidth(8, 1, "20");
		
		Text name = getLocalizedSmallHeader(prmMedicName,"Medication");
		Text form = getLocalizedSmallHeader(prmMedicForm,"Form");
		Text dose = getLocalizedSmallHeader(prmDose,"Dose");
		Text presCaregiver = getLocalizedSmallHeader(prmPrescCareGiver,"Prescribed by");
		Text dateActivation = getLocalizedSmallHeader(prmDateActive,"Date for activation");
				
		table.add(name, 1, 1);
		table.add(form, 3, 1);
		table.add(dose, 5, 1);
		table.add(presCaregiver, 7, 1);
		table.add(dateActivation, 9, 1);
		
		
		layerHead.add(table);
		
		return layerHead;
	}
	
	private Table getSearchSortTable(){
		
		Table table = new Table(3, 5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_BOTTOM);
		table.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
		
		table.setHeight(1, 1, "25");
			
		DropdownMenu dropShow = (DropdownMenu) getStyledInterface(new DropdownMenu(prmShow));
		dropShow.addMenuElementFirst("1", "Visa");

		table.add(dropShow, 1, 1);
		table.add(new Break(2), 1, 1);
				
		return table;
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
		s.append("for (i = 1; i <= 11; i++){").append(" \n\t");
		s.append("elementName = eval(elementBase + i);").append(" \n\t");		
		s.append("document.getElementById(elementName.id).style.backgroundColor = '#CCCCCC';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("showlayer = eval(elementBase + '.id');").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'visible';").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
	
	private Table getTableButtons() {
		Table table = new Table(3, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		table.setWidth(2, 1, "20");
				
		
		GenericButton print = getButton(new GenericButton("linkFass", localize(prmLinkFass, "Link to Fass >")));
		table.add(print, 1, 1);
		
		
		return table;
		
	}
	
	
}

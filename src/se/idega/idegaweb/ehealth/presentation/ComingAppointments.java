/*
 * Created on 2004-okt-07
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
public class ComingAppointments extends EHealthBlock {
	
	private String prefix = "registration_";
	private String prmForm = prefix + "form_visit";
	
	private String prmCareUnit = prefix + "care_unit";
	private String prmDate = prefix + "date";
	private String prmAppointType = prefix + "appoint_type";
	private String prmInfo = prefix + "info";
	private String prmChangeAppoint = prefix + "change_appoint";
	private String prmCancelAppoint = prefix + "cancel_appoint";
	private String prmPay = prefix + "pay";
	
	
	IWContext _iwc = null;

	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
		add(getAppointmentsForm());
		
	}
	
	
	
	public PresentationObject getAppointmentsForm(){
		Form myForm = new Form();
		myForm.setName(prmForm);
		Table T = new Table(1, 4);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setBorderColor("#000000");
		T.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_TOP);
		
		T.add(getHeadingTable(), 1, 1);
		T.add(getInfoLayer(), 1, 2);
		
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
		
		String infoDiv[] = {"14 oktober, kl 10.30<br>Gimo VC, Dr Magne Syhl<br>Undersökning, provtagning",
				"18 oktober, kl 11.30<br>Gimo VC, Dr Magne Syhl<br>Genomgång av provsvar",
				"15 november, kl 09.00<br>Gimo VC, Dr Magne Syhl<br>Undersökning",
				"7 december, kl 13.30<br>Gimo VC, Dr Magne Syhl<br>Uppföljning",
				"16 december, kl 10.00<br>Gimo VC, Dr Magne Syhl<br>Undersökning, provtagning"};
		
		Layer layer = new Layer(Layer.DIV);
		layer.setVisibility("hidden");
		layer.setPositionType("absolute");
		
		T.add(new Break(), 1, 3);
		
		int theRow;
		for (theRow = 1; theRow <= 5; theRow++) {
			Layer layers = (Layer) layer.clone();
			layers.setID("lay" + theRow + "_");			
			layers.add(infoDiv[theRow-1]);
			
			T.add(layers, 1, 3);
		}

		T.setHeight(1, 3, "70");
		T.add(getTableButtons(), 1, 4);
		T.setHeight(1, 4, "25");
		myForm.add(T);
		return myForm;
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
		
		
		int theRow = 1;
		int theColumn = 1;
		
		String dates[] = {"2004-10-14", "2004-10-18", "2004-11-15", "2004-12-07", "2004-12-16", "2004-12-16"};
		String vcs[] = {"Gimo VC", "Gimo VC", "Gimo VC", "Gimo VC", "Gimo VC"};
		String visitypes[] = {"Undersökning, provtagning", "Genomgång av provsvar", "Undersökning", "Uppföljning", "Undersökning/provtagning"};
			
		
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
		
		
		/*tableInfo.add("<div id='lay1_1' onMouseOver='setRowColor(this);'>2004-10-14</div>", 1, row);
		tableInfo.add("<div id='lay1_2' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 2, row);
		tableInfo.add("<div id='lay1_3' onMouseOver='setRowColor(this);'>Gimo VC</div>", 3, row);
		tableInfo.add("<div id='lay1_4' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 4, row);
		tableInfo.add("<div id='lay1_5' onMouseOver='setRowColor(this);'>Undersökning, provtagning</div>", 5, row);
		row++;
		tableInfo.add("<div id='lay2_1' onMouseOver='setRowColor(this);'>2004-10-18</div>", 1, row);
		tableInfo.add("<div id='lay2_2' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 2, row);
		tableInfo.add("<div id='lay2_3' onMouseOver='setRowColor(this);'>Gimo VC</div>", 3, row);
		tableInfo.add("<div id='lay2_4' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 4, row);
		tableInfo.add("<div id='lay2_5' onMouseOver='setRowColor(this);'>Genomgång av provsvar</div>", 5, row);
		row++;
		tableInfo.add("<div id='lay3_1' onMouseOver='setRowColor(this);'>2004-11-15</div>", 1, row);
		tableInfo.add("<div id='lay3_2' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 2, row);
		tableInfo.add("<div id='lay3_3' onMouseOver='setRowColor(this);'>Gimo VC</div>", 3, row);
		tableInfo.add("<div id='lay3_4' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 4, row);
		tableInfo.add("<div id='lay3_5' onMouseOver='setRowColor(this);'>Undersökning</div>", 5, row);
		row++;
		tableInfo.add("<div id='lay4_1' onMouseOver='setRowColor(this);'>2004-12-07</div>", 1, row);
		tableInfo.add("<div id='lay4_2' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 2, row);
		tableInfo.add("<div id='lay4_3' onMouseOver='setRowColor(this);'>Gimo VC</div>", 3, row);
		tableInfo.add("<div id='lay4_4' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 4, row);
		tableInfo.add("<div id='lay4_5' onMouseOver='setRowColor(this);'>Uppföljning</div>", 5, row);
		row++;
		tableInfo.add("<div id='lay5_1' onMouseOver='setRowColor(this);'>2004-12-16</div>", 1, row);
		tableInfo.add("<div id='lay5_2' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 2, row);
		tableInfo.add("<div id='lay5_3' onMouseOver='setRowColor(this);'>Gimo VC</div>", 3, row);
		tableInfo.add("<div id='lay5_4' onMouseOver='setRowColor(this);'>" + transpImg + "</div>", 4, row);
		tableInfo.add("<div id='lay5_5' onMouseOver='setRowColor(this);'>Undersökning, provtagning</div>", 5, row);
		*/
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
				
		Text date = getLocalizedSmallHeader(prmDate,"Date");
		Text careUnit = getLocalizedSmallHeader(prmCareUnit,"Care unit");
		Text regReason = getLocalizedSmallHeader(prmAppointType,"Appointment type");
		
		table.add(date, 1, 1);
		table.add(careUnit, 3, 1);
		table.add(regReason, 5, 1);
		
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
		s.append("for (i = 1; i <= 5; i++){").append(" \n\t");
		s.append("elementName = eval(elementBase + i);").append(" \n\t");		
		s.append("document.getElementById(elementName.id).style.backgroundColor = '#CCCCCC';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("showlayer = eval(elementBase + '.id');").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'visible';").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
	
	private Table getTableButtons() {
		Table table = new Table(7, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		table.setWidth(2, 1, "20");
		table.setWidth(4, 1, "10");
		table.setWidth(6, 1, "10");
		
		GenericButton info = getButton(new GenericButton("info", localize(prmInfo, "Info")));
		table.add(info, 1, 1);
		GenericButton change_appoint = getButton(new GenericButton("change_appoint", localize(prmChangeAppoint, "Change appointment")));
		table.add(change_appoint, 3, 1);		
		GenericButton cancel_appoint = getButton(new GenericButton("cancel_appoint", localize(prmCancelAppoint, "Cancel appointment")));
		table.add(cancel_appoint, 5, 1);
		GenericButton pay = getButton(new GenericButton("pay", localize(prmPay, "Pay")));
		table.add(pay, 7, 1);		
		
		return table;
		
	}
	
	
}

/*
 * Created on 2004-okt-14
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
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.util.IWTimestamp;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Referrals extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_visit";
		
	private String prmDateReferral = prefix + "date_referral";
	private String prmSenderCG = prefix + "sender_cg";
	private String prmReceiverCG = prefix + "receiver_cg";
	private String prmType = prefix + "referral_type";
	private String prmFrom = prefix + "from";
	private String prmTo = prefix + "to";
	private String prmSearch = prefix + "search";
	
	
	
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
		
		T.add(new Break(), 1, 3);
		T.setHeight(1, 3, "160");		
		T.setHeight(1, 4, "40");
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
		
		String infoDiv[] = {"Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.",
				"Lorem ipsum consectetuer dolor sit amet,  iscing elit, sed diam nonmy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.",
				"Lorema met ipsum dolor sconsec it am nonummy nibh eu, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.",
				"Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.",
				"Lorem ipsum adipiscing dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat."};
		
		Layer layer = new Layer(Layer.DIV);
		layer.setVisibility("hidden");
		layer.setOverflow("scroll");
		layer.setPositionType("absolute");
		layer.setWidth("610");
		layer.setHeight("75");
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
		layerInfo.setHeight("75");
		layerInfo.setMarkupAttribute("class", "ehealth_div");
		
		Table tableInfo = new Table(7, 6);
		tableInfo.setNoWrap();
		tableInfo.setCellpadding(0);
		tableInfo.setCellspacing(0);
		tableInfo.setBorder(0);			
		tableInfo.setWidth(570);
		tableInfo.setWidth(1, 1, "125");
		tableInfo.setWidth(2, 1, "20");
		tableInfo.setWidth(3, 1, "110");
		tableInfo.setWidth(4, 1, "20");
		tableInfo.setWidth(5, 1, "150");
		tableInfo.setWidth(6, 1, "20");
		tableInfo.setWidth(7, 1, "125");
		
		Image transpImg = Table.getTransparentCell(_iwc);
		transpImg.setWidth(20);
		transpImg.setHeight(13);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setOnMouseOver("setRowColor(this);");
		layer.setPositionType("relative");
		layer.setHeight(13);
		
		
		int theRow = 1;
		int theColumn = 1;
		
		String dates[] = {"2004-10-11", "2004-10-06", "2004-06-15", "2004-02-07", "2003-12-16"};
		String caregivers[] = {"Dr Magne Syhl", "Dr Alve Don", "Dr Inga Pren", "Dr Alve Don", "Dr Alve Don"};
		String vcs[] = {"Gimo VC", "Gimo VC", "Gimo VC", "Gimo VC", "Gimo VC"};
		String visitypes[] = {"Konsultation", "Röntgen", "Röntgen", "Klinisk kemi..", "Konsultation", "Cyt"};
		
		
				
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
					layers.add(caregivers[theRow-1]);
				}
				else if (theColumn == 5){
					layers.add(vcs[theRow-1]);
				}
				else if (theColumn == 7){
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
		
		Table table = new Table(7, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth(570);
		table.setHeight(20);
		
		
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		
		table.setWidth(1, 1, "125");
		table.setWidth(2, 1, "20");
		table.setWidth(3, 1, "110");
		table.setWidth(4, 1, "20");
		table.setWidth(5, 1, "150");
		table.setWidth(6, 1, "20");
		table.setWidth(7, 1, "125");
		
		Text date = getLocalizedSmallHeader(prmDateReferral,"Date for referral");
		Text senderCG = getLocalizedSmallHeader(prmSenderCG,"Sender(CG)");
		Text receiverCG = getLocalizedSmallHeader(prmReceiverCG,"Receiver(CG)");
		Text referral = getLocalizedSmallHeader(prmType,"Type of referral");
				
		table.add(date, 1, 1);
		table.add(senderCG, 3, 1);
		table.add(receiverCG, 5, 1);
		table.add(referral, 7, 1);
		
		
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
		table.setHeight(1, 2, "25");
		table.setHeight(1, 3, "25");
		table.setHeight(1, 4, "25");
		table.setWidth(2, 1, "25");
	
		IWTimestamp stamp = new IWTimestamp();
		
		DateInput from = (DateInput) getStyledInterface(new DateInput(prmFrom, true));
		from.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
		
		DateInput to = (DateInput) getStyledInterface(new DateInput(prmTo, true));
		to.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
		
		
		
		DropdownMenu dropType = (DropdownMenu) getStyledInterface(new DropdownMenu(prmType));
		dropType.addMenuElementFirst("1", "Alla");
		dropType.addMenuElement("2", "Konsultation");
		dropType.addMenuElement("3", "Klinisk kemi - laboratorieprov");
		dropType.addMenuElement("4", "Röntgen");
		dropType.addMenuElement("5", "Bakt/Pat/Cyt");
			
		GenericButton search = getButton(new GenericButton("search", localize(prmSearch, "Search")));
		
		table.add(getSmallHeader(localize(prmFrom, "From")+": "), 1, 1);
		table.add(from, 1, 2);
		table.add(getSmallHeader(localize(prmTo, "To")+": "), 3, 1);
		table.add(to, 3, 2);
		table.add(dropType, 1, 3);
		table.add(search, 1, 4);
		
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

/*
 * Created on 2004-okt-19
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;




import java.util.ArrayList;
import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.util.Age;


/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class HealthCareOverview extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_care_overview";
	private String keySet1Text1U1 = prefix + "h_care_o_set1_text_1U1";
	private String keySet1Text2U1 = prefix + "h_care_o_set1_text_2U1";
	private String keySet1Text3U1 = prefix + "h_care_o_set1_text_3U1";
	private String keySet1Text4U1 = prefix + "h_care_o_set1_text_4U1";
	private String keySet1Text5U1 = prefix + "h_care_o_set1_text_5U1";
	
	private String keySet2Text1U1 = prefix + "h_care_o_set2_text_1U1";
	private String keySet2Text2U1 = prefix + "h_care_o_set2_text_2U1";
	private String keySet2Text3U1 = prefix + "h_care_o_set2_text_3U1";
	private String keySet2Text4U1 = prefix + "h_care_o_set2_text_4U1";
	private String keySet2Text5U1 = prefix + "h_care_o_set2_text_5U1";
	
	private String keySet1Text1U2 = prefix + "h_care_o_set1_text_1U2";
	private String keySet1Text2U2 = prefix + "h_care_o_set1_text_2U2";
	private String keySet1Text3U2 = prefix + "h_care_o_set1_text_3U2";
	private String keySet1Text4U2 = prefix + "h_care_o_set1_text_4U2";
	private String keySet1Text5U2 = prefix + "h_care_o_set1_text_5U2";
	
	private String keySet2Text1U2 = prefix + "h_care_o_set2_text_1U2";
	private String keySet2Text2U2 = prefix + "h_care_o_set2_text_2U2";
	private String keySet2Text3U2 = prefix + "h_care_o_set2_text_3U2";
	private String keySet2Text4U2 = prefix + "h_care_o_set2_text_4U2";
	private String keySet2Text5U2 = prefix + "h_care_o_set2_text_5U2";
	
	//private int userID = -1;
	//private User user;
	IWContext _iwc = null;
	private Image imageCircleD = null;
	private Image imageCircleU = null;
	private Image imageBgVert = null;
	
	Age age = null;
	
	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
		
		//userID = iwc.getUserId();
		
	/*	if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
		}
	*/
		imageCircleD = getBundle(iwc).getImage("circleDown.gif");
		imageCircleU = getBundle(iwc).getImage("circleUp.gif");
		imageBgVert = getBundle(iwc).getImage("bgbeigeVert.gif");
		
	//	add(getOverviewForm(iwc));
		add(getOverviewForm());
		
	
		
	}
	
	public PresentationObject getOverviewForm(){
		Form myForm = new Form();
		myForm.setName(prmForm);
		Table T = new Table(3, 3);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setBorderColor("#000000");
		T.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		T.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_TOP);
		//T.setVerticalAlignment(1, 4, Table.VERTICAL_ALIGN_BOTTOM);
		//T.add(getNavigationTree(iwc), 1, 1);
		T.add(getNavigationTree(), 1, 1);
	//	T.add(getInfoLayer(), 2, 1);
		
		
		
		T.add(new Break(3), 3, 1);
//		T.setHeight(1, 3, "130");		
		//T.setHeight(1, 4, "90");
		T.setWidth(2, 1, "40");
		myForm.add(T);
		
		
		Page pVisit = this.getParentPage();
		if (pVisit != null) {
			Script S = pVisit.getAssociatedScript();
			//pVisit.setOnLoad("setRowColor(document.getElementById('inf1_1'));");
		
			S.addFunction("setRowColor(obj)", setRowColorScript());
			S.addFunction("showHideLayer(obj)", showHideLayerScript());
			Script timeScript = myForm.getAssociatedFormScript();
			if (timeScript == null) {
				timeScript = new Script();
				myForm.setAssociatedFormScript(timeScript);
			}
		}
		
		
		ArrayList texts1 = new ArrayList();
		ArrayList texts2 = new ArrayList();
		
		if (age != null && age.getYears() >= 70){
			texts1.add(localize(keySet1Text1U1, "Texten"));
			texts1.add(localize(keySet1Text2U1, "Texten"));
			texts1.add(localize(keySet1Text3U1, "Texten"));
			texts1.add(localize(keySet1Text4U1, "Texten"));
			texts1.add(localize(keySet1Text5U1, "Texten"));
			
			texts2.add(localize(keySet2Text1U1, "Texten"));
			texts2.add(localize(keySet2Text2U1, "Texten"));
			texts2.add(localize(keySet2Text3U1, "Texten"));
			texts2.add(localize(keySet2Text4U1, "Texten"));
			texts2.add(localize(keySet2Text5U1, "Texten"));
			
		
		}
		else{
			texts1.add(localize(keySet1Text1U2, "Texten"));
			texts1.add(localize(keySet1Text2U2, "Texten"));
			texts1.add(localize(keySet1Text3U2, "Texten"));
			texts1.add(localize(keySet1Text4U2, "Texten"));
			texts1.add(localize(keySet1Text5U2, "Texten"));
			
			texts2.add(localize(keySet2Text1U2, "Texten"));
			texts2.add(localize(keySet2Text2U2, "Texten"));
			texts2.add(localize(keySet2Text3U2, "Texten"));
			texts2.add(localize(keySet2Text4U2, "Texten"));
			texts2.add(localize(keySet2Text5U2, "Texten"));
			
			
		}			
		
		Layer layerOut = new Layer(Layer.DIV);
		layerOut.setVisibility("visible");
		layerOut.setOverflow("scroll");
		layerOut.setPositionType("absolute");
		layerOut.setWidth("400");
		layerOut.setHeight("150");
		layerOut.setMarkupAttribute("class", "ehealth_div");
		
		
		Layer layer = new Layer(Layer.DIV);
		layer.setVisibility("hidden");
		layer.setOverflow("hidden");
		layer.setPositionType("absolute");
		layer.setWidth("350");
		layer.setHeight("150");
		layer.setMarkupAttribute("class", "ehealth_div_no_border");
		
		
		int theRow = 1;
		int i = 1;
		
		
		Iterator iter1 = texts1.iterator();
		Iterator iter2 = texts2.iterator();
		
			
			while (iter1.hasNext()) {
					Layer layers = (Layer) layer.clone();
					layers.setID("info" + i + "_"+ theRow);			
					String text = (String) iter1.next();
					layers.add(text);
					layerOut.add(layers);
					
					theRow++;
				}	
			i++;
			theRow = 1;
			while (iter2.hasNext()) {
					Layer layers = (Layer) layer.clone();
					layers.setID("info" + i + "_"+ theRow);			
					String text = (String) iter2.next();
					layers.add(text);
					layerOut.add(layers);
					
					theRow++;
				}	
			
		T.add(layerOut, 3, 1);
		return myForm;
	}
	
	//private Layer getNavigationTree(IWContext iwc) {
	private Layer getNavigationTree() {
		Layer layersNav = new Layer(Layer.DIV);
		layersNav.setOverflow("scroll");
		layersNav.setPositionType("relative");
		layersNav.setWidth("300");
		layersNav.setHeight("500");
		
		layersNav.setMarkupAttribute("class", "ehealth_div_no_border");
		
		Table table = new Table(3, 20);
		table.setNoWrap();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);	
		
		table.setWidth("250");
		//table.setHeight(50);
		
		int row = 1;
		int i = 1;
		
		
		for (i = 1; i <= 2; i++){
		
			table.mergeCells(2, row, 3, row);
			table.add(getLayer1(i), 2, row);
			table.setHeight(1, row, "16");
			table.setHeight(2, row, "16");
			table.setWidth(1, row, "16");
			table.setWidth(2, row, "16");
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			table.add(imageCircleD, 1, row++);			
			table.setBackgroundImage(1, row, imageBgVert);
			table.setHeight(2, row, "16");
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			table.add(getLayer2(i), 2, row++);
			table.setBackgroundImage(1, row, imageBgVert);
			table.setHeight(1, row, "20");
			table.add(getLayer3(i), 2, row++);			
			table.setHeight(1, row, "10");
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			table.setBackgroundImage(1, row++, imageBgVert);
			
			/*table.add(getLayer1(i), 2, row);
			table.add(getLayer2(i), 2, row);
			table.add(getLayer3(i), 2, row);
			*/
		}
		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.setHeight(1, row, "25");
		table.setBackgroundImage(1, row++, imageBgVert);
				
		layersNav.add(table);
		return layersNav;
	}
	
	private Layer getLayer1(int i){
		Layer layerInfo = new Layer(Layer.DIV);
		layerInfo.setOverflow("hidden");
		layerInfo.setVisibility("visible");
		layerInfo.setPositionType("relative");
		layerInfo.setHeight("16");
		layerInfo.setWidth("190");
		
		layerInfo.setID("lay" + i + "_1");
		layerInfo.setMarkupAttribute("class", "ehealth_div_top");
		Link text1 = new Link();
		text1.setNoURL();
		text1.setID("text" + i + "_1");
		if (i == 1){
			text1.setText("VÅRDBEGÄRAN 04-05-05");	
		}
		else if (i == 2){
			text1.setText("VÅRDBEGÄRAN 03-10-24");
		}
		
		else
			text1.setText("Lorum ispum dolor sit amet");
		
		text1.setOnClick("javascript:showHideLayer("+ i +", 1);return false;");
		
		layerInfo.add(text1);
		return layerInfo;
	}
	
	private Layer getLayer2(int i){
		Layer layerInfo = new Layer(Layer.DIV);
		layerInfo.setOverflow("hidden");
		layerInfo.setVisibility("hidden");
		layerInfo.setPositionType("relative");
		layerInfo.setHeight("25");
		layerInfo.setID("lay" + i + "_2");
		layerInfo.setMarkupAttribute("class", "ehealth_div_no_border");
		Link text1 = new Link();
		text1.setNoURL();
		text1.setID("text" + i + "_2");
		if (i == 1){
			text1.setText("Besök 04-04-30  10.00-11.00");	
		}
		else if (i == 2){
			text1.setText("Inskrivning 03-10-22 - 03-10-24");
		}
		else
			text1.setText("Besök 03-01-30 14.00-15.00 Gimo VC");
		
		text1.setOnClick("javascript:showHideLayer("+ i +", 2);return false;");
		layerInfo.add(imageCircleU);
		layerInfo.add(text1);
		return layerInfo;
	}
	
	private Layer getLayer3(int i){
		Layer layerInfo = new Layer(Layer.DIV);
		layerInfo.setOverflow("hidden");
		layerInfo.setVisibility("hidden");
		layerInfo.setPositionType("relative");
		layerInfo.setHeight("20");
		layerInfo.setWidth("300");
		layerInfo.setID("lay" + i + "_3");
		layerInfo.setStyleClass("ehealth_div_no_border");
		//layerInfo.setLeftPosition("10");
		layerInfo.setTopPosition("0");
		layerInfo.add(getInfoLayer(i));	
		return layerInfo;
	}
	
	
	private Layer getInfoLayer(int theCase){
		Layer layerInfo = new Layer(Layer.DIV);
		layerInfo.setOverflow("hidden");
		//layerInfo.setVisibility("hidden");
		layerInfo.setPositionType("relative");
		
		layerInfo.setWidth("270");
		layerInfo.setHeight("100");
		layerInfo.setStyleClass("ehealth_row_div");
	
		Table tableInfo = new Table(1, 10);
		tableInfo.setNoWrap();
		tableInfo.setCellpadding(0);
		tableInfo.setCellspacing(0);
		tableInfo.setBorder(0);			
		//tableInfo.setWidth(200);
		int column = 1;
		tableInfo.setWidth(column, 1, "200");
		//tableInfo.setWidth(column++, 1, "70");
				
		Image transpImg = Table.getTransparentCell(_iwc);
		transpImg.setWidth(15);
		transpImg.setHeight(16);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setOnMouseOver("setRowColor(this);");
		layer.setPositionType("relative");
		layer.setHeight(20);
		layer.setStyleClass("ehealth_row_div");
		
		Image corners = getBundle(_iwc).getImage("thirdLevel.gif");
		corners.setBorder(0);
		corners.setHeight(16);
		corners.setWidth(19);
		layer.setBackgroundImage(corners);
		
		
		int theRow = 1;
		
		ArrayList cases = new ArrayList();
		
		if (theCase == 1){
			cases.add("VÅRDPLATS: Akademigruppen Klas Löv ");
			cases.add("JOURNALANTECKNINGAR");	
		}
		else if (theCase == 2){
			cases.add("VÅRDPLATS: Ortopedavd 70 E2");
			cases.add("AKTIVITET: Operation");
			cases.add("JOURNALANTECKNINGAR");
		}
		
		Iterator iter = cases.iterator();
	
		while (iter.hasNext()) {
			
				Layer layers = (Layer) layer.clone();
				layers.setID("inf" + theCase + "_"+ theRow);
				String text = (String) iter.next();
				layers.add(text);
				tableInfo.add(layers, column, theRow);
				theRow++;
			}			
		
		layerInfo.add(tableInfo);
		
		return layerInfo;
	}
	
	private String showHideLayerScript() {
		StringBuffer s = new StringBuffer();
		
		
		s.append("function showHideLayer(cases, level){").append(" \n\t");
		s.append("stringThelayer = 'lay' + cases + '_' + level;").append(" \n\t");	
		s.append("theLayer = findObj(stringThelayer);").append(" \n\t");
		s.append("var info = -1;").append("\n\t");
		s.append("var inf = -1;").append("\n\t");
		
		s.append("if (theLayer.style) { ").append(" \n\t"); //IE
		/*
		s.append("for(info=1;info<document.all.tags('div').length;info++){").append(" \n\t");
		s.append("if (document.all.tags('div')[info].id.substr(0, 4) == 'info'){").append(" \n\t");
		//s.append("alert(document.all.tags('div')[info].id.substr(0, 4))").append(" \n\t");
		s.append("for(inf=1;inf<=5;inf++){").append(" \n\t");
		s.append("alert('info' + cases + '_' + inf)").append(" \n\t");
		s.append("showlayer = eval('info' + cases + '_' + inf + '.id');").append(" \n\t");
		s.append("showlayer = findObj(showlayer);").append(" \n\t");
		s.append("if (showlayer && showlayer.style.visibility == 'visible'){ ").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'hidden';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");		
		s.append("}").append("\n\t");		
		s.append("}").append("\n\t\t");
	*/
		s.append("for(i=1;i<document.all.tags('div').length;i++){").append(" \n\t");
		//hide the layers that contains all the info, the naming of the layers is lay1_, lay2_, lay3_ etc
		s.append("if (document.all.tags('div')[i].id.substr(0, 4) == 'info'){").append(" \n\t");
		s.append("document.all.tags('div')[i].style.visibility = 'hidden'");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");
		
		s.append("for(i=1;i<document.all.tags('div').length;i++){").append(" \n\t");
		//s.append("alert(document.all.tags('div').length)").append(" \n\t");
		//s.append("if (document.all.tags('div')[i].id.substr(0, 4) == 'info'){").append(" \n\t");
		//s.append("alert('info' + cases + '_' + i)").append(" \n\t");
		//s.append("showlayer = eval('info' + cases + '_' + i + '.id');").append(" \n\t");
		
	//	s.append("if (showlayer){ ").append(" \n\t");
	//	s.append("document.all(showlayer).style.visibility = 'hidden';").append(" \n\t");
	//	s.append("}").append("\n\t");
	//	s.append("}").append("\n\t");
	
		s.append("if (document.all.tags('div')[i].id.substr(0, 3) == 'lay'){").append(" \n\t");
		//s.append("if (document.all.tags('div')[i].id.substr(document.all.tags('div')[i].id.length-1, 1) == '2' || document.all.tags('div')[i].id.substr(document.all.tags('div')[i].id.length-1, 1) == '3'){ ").append(" \n\t");
		s.append("if (i <= 3){").append(" \n\t");
		s.append("if (level == 1){").append(" \n\t");
		s.append("showLayer = eval('lay' + cases + '_' + i + '.id');").append(" \n\t");
		s.append("layer3 = eval('lay' + cases + '_' + 3 + '.id');").append(" \n\t");
		s.append("layer3 = findObj(layer3);").append(" \n\t");
		s.append("layer2 = eval('lay' + cases + '_' + 2 + '.id');").append(" \n\t");
		s.append("layer2 = findObj(layer2);").append(" \n\t");
		s.append("if (layer2.style.visibility == 'visible'){").append(" \n\t");
		s.append("layer3.style.visibility = 'hidden';").append(" \n\t");
		s.append("layer3.style.position = 'absolute';").append(" \n\t");
		s.append("layer3.style.height = '0';").append(" \n\t");
		s.append("}else {").append("\n\t").append(" \n\t");
		s.append("layer3.style.position = 'relative';").append(" \n\t");
		s.append("layer3.style.height = '100';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("var showDiv = findObj(showLayer);").append(" \n\t");
		s.append("if (showDiv){ ").append(" \n\t");
		s.append("if (showDiv.style.visibility == 'hidden') {").append(" \n\t");
		s.append("showDiv.style.visibility = 'visible';").append(" \n\t");
		s.append("}else {").append("\n\t").append(" \n\t");
		s.append("showDiv.style.visibility = 'hidden';").append(" \n\t");
		s.append("showDiv.style.height = '0';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");
		
		s.append("return false;").append("\n\t");
		s.append("}").append("\n\t");
		s.append("else if (level == 2){").append("\n\t");
		//s.append("showLayer = eval('lay' + cases + '_' + 3 + '.id');").append(" \n\t");
		s.append("layer3 = eval('lay' + cases + '_' + 3 + '.id');").append(" \n\t");
		s.append("layer3 = findObj(layer3);").append(" \n\t");
		s.append("if (layer3.style.visibility == 'visible'){").append(" \n\t");
		s.append("layer3.style.visibility = 'hidden';").append(" \n\t");
		s.append("layer3.style.position = 'absolute';").append(" \n\t");
		//s.append("layer3.style.height = '0';").append(" \n\t");
		s.append("}else {").append("\n\t").append(" \n\t");
		s.append("layer3.style.position = 'relative';").append(" \n\t");
		s.append("layer3.style.visibility = 'visible';").append(" \n\t");
		//s.append("layer3.style.height = '100';").append(" \n\t");
		s.append("}").append("\n\t");
		/*
		s.append("var showDiv = findObj(showLayer);").append(" \n\t");
		s.append("if (showDiv){ ").append(" \n\t");
		s.append("if (showDiv.style.visibility == 'hidden') {").append(" \n\t");
		s.append("showDiv.style.visibility = 'visible';").append(" \n\t");
		s.append("showDiv.style.height = '100';").append(" \n\t");
		s.append("}else {").append("\n\t").append(" \n\t");
		s.append("showDiv.style.visibility = 'hidden';").append(" \n\t");
		s.append("showDiv.style.height = '0';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");
		*/
		s.append("return false;").append("\n\t");
		s.append("}").append("\n\t");
		s.append("else if (level == 3){").append("\n\t");
		s.append("showLayer = eval('lay' + cases + '_' + 3 + '.id');").append(" \n\t");
		s.append("var showDiv = findObj(showLayer);").append(" \n\t");
		s.append("if (showDiv){ ").append(" \n\t");
		s.append("alert(showDiv.id);").append(" \n\t");
		s.append("if (showDiv.style.visibility == 'hidden') {").append(" \n\t");
		s.append("showDiv.style.visibility = 'visible';").append(" \n\t");
		s.append("}else {").append("\n\t").append(" \n\t");
		s.append("showDiv.style.visibility = 'hidden';").append(" \n\t");
		//s.append("showDiv.style.height = '0';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");
		s.append("return false;").append("\n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");
		s.append("}").append("\n\t");
		s.append("return false;").append(" \n\t");
		s.append("}").append("\n\t\t\t");
		
		//s.append("}").append("\n\t"); //end of else, not ie
		
		
		
		return s.toString();
	}
		
	
	
	private String setRowColorScript() {
		StringBuffer s = new StringBuffer();
		
		
		s.append("function setRowColor(obj){").append(" \n\t");
		s.append("elementBase = obj.id.substring(0, 5);").append(" \n\t");
		s.append("elementEnd = obj.id.substr(3, 3);").append(" \n\t");
		s.append("if (elementBase.indexOf('_') <= 0)").append(" \n\t");   //if inf10 and higher
		s.append("elementBase = elementBase + '_';").append(" \n\t");
		s.append("for(i=1;i<document.all.tags('div').length;i++){").append(" \n\t");
		//hide the layers that contains all the info, the naming of the layers is lay1_, lay2_, lay3_ etc
		s.append("if (document.all.tags('div')[i].id.substr(0, 4) == 'info'){").append(" \n\t");
		s.append("document.all.tags('div')[i].style.visibility = 'hidden'");
		s.append("}").append("\n\t");
		s.append("document.all.tags('div')[i].style.backgroundColor = '#ffffff';");
		s.append("}").append("\n\t");
		//s.append("for (i = 1; i <= 1; i++){").append(" \n\t");
		//s.append("elementName = eval(elementBase + i);").append(" \n\t");	
		s.append("document.getElementById(obj.id).style.backgroundColor = '#CCCCCC';").append(" \n\t");
		//s.append("}").append("\n\t");
		s.append("showlayer = eval('info' + elementEnd + '.id');").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'visible';").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
}

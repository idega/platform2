/*
 * Created on 2004-okt-07
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;



import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.component.UIComponent;

import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Page;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.Form;



/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FAQ extends EHealthBlock {
	
	private String prefix = "patient_";
	private String prmForm = prefix + "form_visit";
	
	
	IWContext _iwc = null;

	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
		add(getAppointmentHistoryForm());
		
	}
	
	
	
	public UIComponent getAppointmentHistoryForm(){
		Form myForm = new Form();
		myForm.setName(prmForm);
		Table T = new Table(1, 3);
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setBorderColor("#000000");
		T.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_TOP);
			
		//T.add(getSearchSortTable(), 1, 1);
		//T.add(getHeadingTable(), 1, 2);
		T.add(getInfoLayer(), 1, 1);
		T.add(getLocalizedHeader("faq_reply", "Reply to selected question"), 1, 2);
		//T.add(getTableButtons(), 1, 4);
		
		T.add(new Break(), 1, 1);
		T.setWidth(1, 3, "200");
		//T.setHeight(1, 1, "160");		
		T.setHeight(1, 3, "160");
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
		
		info.add(localize("faq_how_to", "How to"));
		info.add(localize("faq_can_do", "What can I do?"));
		info.add(localize("faq_not_log_in", "Cannot log in"));
		info.add(localize("faq_browser", "Which browsers?"));
		info.add(localize("faq_version_browser", "Version of browser"));
		info.add(localize("faq_personal_info", "What is cookies?"));
		
		Layer layer = new Layer(Layer.DIV);
		layer.setVisibility("hidden");
		layer.setPositionType("absolute");
		layer.setWidth("400");
		layer.setPadding(5, "top");
				
		int theRow = 1; 
		Iterator iter = info.iterator();
		
		while (iter.hasNext()){
			Layer layers = (Layer) layer.clone();
			layers.setID("lay" + theRow + "_");	
			
			String theInfo = (String) iter.next();
			layers.add(theInfo);
			
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
		
		Table tableInfo = new Table(1, 8);
		tableInfo.setNoWrap();
		tableInfo.setCellpadding(0);
		tableInfo.setCellspacing(0);
		tableInfo.setBorder(0);			
		tableInfo.setWidth(570);
		tableInfo.setWidth(1, 1, "400");
	
		Image transpImg = Table.getTransparentCell(_iwc);
		transpImg.setWidth(20);
		transpImg.setHeight(13);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setOnMouseOver("setRowColor(this);");
		layer.setPositionType("relative");
		
		
		int theRow = 1;
		int theColumn = 1;
		
		ArrayList questions = new ArrayList();
		
		questions.add("Hur skaffar jag ett vårdkonto?");
		questions.add("Vad kan jag göra på Mitt Vårdkonto?");
		questions.add("Varför kan jag inte logga in på Mitt Vårdkonto?");
		questions.add("Finns det några krav på vilken webbläsare jag ska använda?");
		questions.add("Hur ser jag vilken version av Internet Explorer jag har");
		questions.add("Vad är cookies?");
		
		Iterator iter = questions.iterator();
		
		while (iter.hasNext()){
				Layer layers = (Layer) layer.clone();
				layers.setID("lay" + theRow + "_"+ theColumn);
				String question = (String) iter.next();
				layers.add(getSmallHeader(question));							
				tableInfo.add(layers, theColumn, theRow);
				
				theRow++;	
		}
		
		layerInfo.add(tableInfo);
		
		return layerInfo;
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
		s.append("for (i = 1; i <= 1; i++){").append(" \n\t");
		s.append("elementName = eval(elementBase + i);").append(" \n\t");		
		s.append("document.getElementById(elementName.id).style.backgroundColor = '#CCCCCC';").append(" \n\t");
		s.append("}").append("\n\t");
		s.append("showlayer = eval(elementBase + '.id');").append(" \n\t");
		s.append("document.all(showlayer).style.visibility = 'visible';").append(" \n\t");
		
		s.append("}").append("\n\t\t\t");
		
		return s.toString();
	}
	
	
		
}

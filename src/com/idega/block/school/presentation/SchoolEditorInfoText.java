/*
 * Created on 2003-nov-26
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.presentation;


import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Window;
import com.idega.presentation.text.Text;


/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolEditorInfoText extends Window {
	public static final int PARAMETER_TOPIC_ID_EDITOR = 0;
	public static final int PARAMETER_TOPIC_ID_DEPM =1;
	public static final int PARAMETER_TOPIC_ID_PERSON = 2;
	

//private Text TEXT_NORMAL;
	//private Text TEXT_TITLE;
	

	public SchoolEditorInfoText() {
		setWidth(300);
		setHeight(200);
		setResizable(true);
		setScrollbar(true);
		setTitle( "School editor information" );	
		
		}
	public void main( IWContext iwc ) throws Exception {
		boolean topicEditor = iwc.isParameterSet(SchoolUserEditor.PARAMETER_TOPIC_EDITOR);
		boolean topicDepm = iwc.isParameterSet(SchoolUserEditor.PARAMETER_TOPIC_DEPM);
		boolean topicPerson = iwc.isParameterSet(SchoolUserEditor.PARAMETER_TOPIC_PERSON);
		Table table = new Table();

		if (topicEditor) {
			Text tHeading = getTextTitle("Redigeraren");
			String sText = "I redigeraren kan du lägga in eller ändra adress- och kontaktuppgifter för en skola. Rektor för skolan, huvudrektorn, visas överst. Under Kontakta oss visas de kontaktpersoner som ni väljer att visa. Om skolan är indelad i enheter kan du välja att lägga in uppgifter och kontaktpersoner under en viss enhet. De personer som du skapat via admingränssnittet syns även här i redigeraren.";
			Text tText = getTextNormal(sText);
			table.add(tHeading, 1, 1);
			table.add(tText, 1, 2);

		} else if (topicDepm) {
			Text tHeading = getTextTitle("Skapa en enhet");
			String sText = "Om organisationen är enhetsindelad kan du i systemet skapa enheter genom att skriva in enhetens namn och eventuellt telefonnummer under rubriken ”Skapa en enhet” och klicka på ”Spara”.";
			Text tText = getTextNormal(sText);
			table.add(tHeading, 1, 1);
			table.add(tText, 1, 2);
		} else if (topicPerson){
			Text tHeading = getTextTitle("Skapa en kontaktperson för skolan eller enheten");
			Text tSubHeading = getTextTitleGray("Skapa Huvudrektor");
			
			String sTextHuvud = "För att skapa huvudrektorn på skolan väljer du typen Rektor samt kryssar i rutan Huvudrektor. Fyll sedan i fälten för kontaktuppgifterna. Du behöver inte välja någon enhet.";
			sTextHuvud = sTextHuvud + "För att personen ska visas måste du även kryssa i rutan Visa i kontaktlista och sedan klicka på Spara.";

			Text tTextHuvud = getTextNormal(sTextHuvud);
		
			Text tHeadingKontakt = getTextTitleGray("Skapa annan kontaktperson");
			String sTextKontakt = "För att skapa en kontaktperson väljer du typ av kontaktperson samt fyller i fälten för kontaktuppgifterna. Om personen tillhör någon enhet väljer du enhet i rullgardinsmenyn."; 
			sTextKontakt = sTextKontakt + "För att personen ska visas måste du även kryssa i rutan Visa i kontaktlista och sedan klicka på Spara.";
			Text tTextKontakt = getTextNormal(sTextKontakt);
			Text tHeadingEdit = getTextTitleGray("Redigera kontaktperson");
			String sTextEditKontakt = "För att redigera en kontaktperson klickar du på Ändra intill personen som ska redigeras. När du är färdig med dina ändringar klickar du på Spara.";
			Text sTextEdit = getTextNormal(sTextEditKontakt);
			Text tHeadingDelete = getTextTitleGray("Ta bort kontaktperson");
			String sTextDelete = "För att ta bort en kontaktperson klickar du på länken Radera intill kontaktpersonen.";
			Text tTextDelete = getTextNormal(sTextDelete);

			table.add(tHeading, 1, 1);
			table.add(tSubHeading, 1, 2);
			table.add(tTextHuvud, 1, 3);
			table.add(tHeadingKontakt, 1, 4);
			table.add(tTextKontakt, 1, 5);
			table.add(tHeadingEdit, 1, 6);
			table.add(sTextEdit, 1, 7);
			table.add(tHeadingDelete, 1, 8);
			table.add(tTextDelete, 1, 9);

			

		}
		add(table);
		
		
	}

private Text getTextNormal(String content) {
		/*if (TEXT_NORMAL == null) {
			return _tFormat.format(content, TextFormat.NORMAL);
	 
		}else {
			Text text = (Text) TEXT_NORMAL.clone();
			text.setText(content);
			return text;	
		}
*/
Text text = new Text (content);
String STYLE_SMALL_HEADER = "font-style:normal;text-decoration:none;color:#000000;"
			+ "font-size:10px;font-family:Verdana,Arial,Helvetica;font-weight:normal;";
text.setFontStyle(STYLE_SMALL_HEADER);

return text;
	}

	private Text getTextTitle(String content) {
		/*if (TEXT_TITLE == null) {
			return _tFormat.format(content, TextFormat.TITLE);
		}else {
			Text text = (Text) TEXT_TITLE.clone();
			text.setText(content);
			return text;	
		}
*/
Text text = new Text (content);
String STYLE_SMALL_HEADER = "font-style:normal;text-decoration:none;color:#000000;"
			+ "font-size:10px;font-family:Verdana,Arial,Helvetica;font-weight:bold;";
text.setFontStyle(STYLE_SMALL_HEADER);
return text;
	}
	
	private Text getTextTitleGray(String content) {
		/*	if (TEXT_TITLE == null) {
				return _tFormat.format(content, TextFormat.TITLE);
			}else {
				Text text = (Text) TEXT_TITLE.clone();
				text.setText(content);
				text.setFontColor("#386cb7");
				return text;	
			}
*/
Text text = new Text (content);
String STYLE_SMALL_HEADER = "font-style:normal;text-decoration:none;color:#386cb7;"
			+ "font-size:10px;font-family:Verdana,Arial,Helvetica;font-weight:bold;";
text.setFontStyle(STYLE_SMALL_HEADER);

return text;
		}


public void setTextStyleNormal() {
  	//this.TEXT_NORMAL = text;	
  }
  
  public void setTextStyleTitle() {
  	//this.TEXT_TITLE = text;	
  }

}

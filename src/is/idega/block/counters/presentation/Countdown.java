/*
 * Created on Mar 28, 2004
 *
 */
package is.idega.block.counters.presentation;

import java.sql.Timestamp;
import java.util.Date;

import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;

/**
 * Countdown
 * @author aron 
 * @version 1.0
 */
public class Countdown extends Block {
	
	private boolean showSeconds = true;
	private Date targetDate = new Date();
	private IWBundle iwb = null;
	private String digitWidth = "";
	private String digitHeight = "";
	private String seperatorWidth = "";
	private String seperatorHeight = "";
	private String digitFolderURL = "shared/digits/default/";
	
	public void main(IWContext iwc){
		iwb = getBundle(iwc);
		
		getParentPage().getAssociatedScript().addFunction("downcount",getJavascriptFunction());
		getParentPage().setOnLoad("getTime(false)");
		
		int col = 1;
		Table table  = new Table();
			
			table.add(getDigitImage("x"),col++,1);
			table.add(getDigitImage("a"),col++,1);
			table.add(getDigitImage("b"),col++,1);
			table.add(getSeperatorImage("c"),col++,1);
			table.add(getDigitImage("y"),col++,1);
			table.add(getDigitImage("z"),col++,1);
			table.add(getSeperatorImage("cz"),col++,1);
			table.add(getDigitImage("d"),col++,1);
			table.add(getDigitImage("e"),col++,1);
			table.add(getSeperatorImage("f"),col++,1);
			table.add(getDigitImage("g"),col++,1);
			table.add(getDigitImage("h"),col++,1);
			table.setCellpadding(0);
			table.setCellspacing(0);
			
		
		add(table);
	}
	
	private Image getDigitImage(String name){
		Image img = iwb.getImage(digitFolderURL+Digits.DIGIT0,name);
		if(!"".equals(digitWidth))
			img.setWidth(digitWidth);
		if(!"".equals(digitHeight))
			img.setHeight(digitHeight);
		return img;
	}
	
	private Image getSeperatorImage(String name){
		Image img = iwb.getImage(digitFolderURL+Digits.SEPERATOR,name);
		if(!"".equals(seperatorWidth))
			img.setWidth(seperatorWidth);
		if(!"".equals(seperatorHeight))
			img.setHeight(seperatorHeight);
		return img;
	}
	
	private  String getJavascriptFunction(){
		StringBuffer script = new StringBuffer();
	
		script.append(" function getTime(blinking) { ").append("\n").
		append(" c1 = new Image(); c1.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT1).getURL()).append("\"; \n").
		append(" c2 = new Image(); c2.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT2).getURL()).append("\"; \n").
		append(" c3 = new Image(); c3.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT3).getURL()).append("\"; \n").
		append(" c4 = new Image(); c4.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT4).getURL()).append("\"; \n").
		append(" c5 = new Image(); c5.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT5).getURL()).append("\"; \n").
		append(" c6 = new Image(); c6.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT6).getURL()).append("\"; \n").
		append(" c7 = new Image(); c7.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT7).getURL()).append("\"; \n").
		append(" c8 = new Image(); c8.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT8).getURL()).append("\"; \n").
		append(" c9 = new Image(); c9.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT9).getURL()).append("\"; \n").
		append(" c0 = new Image(); c0.src = \"").append(iwb.getImage(digitFolderURL+Digits.DIGIT0).getURL()).append("\"; \n").
		append(" Cc = new Image(); Cc.src = \"").append(iwb.getImage(digitFolderURL+Digits.SEPERATOR).getURL()).append("\"; \n").
		append(" Mc = new Image(); Mc.src = \"").append(iwb.getImage(digitFolderURL+Digits.BAND).getURL()).append("\"; \n").
		append(" Ec = new Image(); Ec.src = \"").append(iwb.getImage(digitFolderURL+Digits.EMPTY).getURL()).append("\"; \n").
		append(" now = new Date(); ");
		
		script.append(" later = new Date(); later.setTime(").append(targetDate.getTime()).append(");\n")
		.append(" blink = blinking; \n")
		.append(" if(later > now){\n")
		.append(" days = (later - now) / 1000 / 60 / 60 / 24;\n")
		.append(" daysRound = Math.floor(days);\n")
		.append(" hours = (later - now) / 1000 / 60 / 60 - (24 * daysRound);\n")
		.append(" hoursRound = Math.floor(hours);\n")
		.append(" minutes = (later - now) / 1000 /60 - (24 * 60 * daysRound) - (60 * hoursRound);\n")
		.append(" minutesRound = Math.floor(minutes);\n")
		.append(" 	seconds = (later - now) / 1000 - (24 * 60 * 60 * daysRound) - (60 * 60 * hoursRound) - (60 * minutesRound);\n")
		.append(" 	secondsRound = Math.round(seconds);\n")

		.append(" 	if (secondsRound <= 9) {\n")
		.append(" 		document.images.g.src = c0.src;\n")
		.append(" 		document.images.h.src = eval(\"c\"+secondsRound+\".src\");\n")
		.append(" 	}\n")
		.append(" 	else {\n")
		.append(" 		document.images.g.src = eval(\"c\"+Math.floor(secondsRound/10)+\".src\");\n")
		.append(" 		document.images.h.src = eval(\"c\"+(secondsRound%10)+\".src\")\n")
		.append(" 	}\n")
		.append(" 	if (minutesRound <= 9) {\n")
		.append(" 		document.images.d.src = c0.src;\n")
		.append(" 		document.images.e.src = eval(\"c\"+minutesRound+\".src\");\n")
		.append(" 	}\n")
		.append(" 	else {\n")
		.append(" 		document.images.d.src = eval(\"c\"+Math.floor(minutesRound/10)+\".src\");\n")
		.append(" 		document.images.e.src = eval(\"c\"+(minutesRound%10)+\".src\");\n")
		.append(" 	}\n")
		.append(" 	if (hoursRound <= 9) {\n")
		.append("     document.images.y.src = c0.src;\n")
		.append(" 		document.images.z.src = eval(\"c\"+hoursRound+\".src\");\n")
		.append(" 	}\n")
		.append(" 	else {\n")
		.append(" 		document.images.y.src = eval(\"c\"+Math.floor(hoursRound/10)+\".src\");\n")
		.append(" 		document.images.z.src = eval(\"c\"+(hoursRound%10)+\".src\");\n")
		.append(" 	}\n")
		.append(" 	if (daysRound <= 9) {\n")
		.append(" 		document.images.x.src = c0.src;\n")
		.append(" 		document.images.a.src = c0.src;\n")
		.append(" 		document.images.b.src = eval(\"c\"+Math.floor(daysRound%10)+\".src\");\n")
		.append(" 	}\n")
		.append(" 	else if (daysRound <= 99) {\n")
		.append(" 		document.images.x.src = c0.src;\n")
		.append(" 		document.images.a.src = eval(\"c\"+Math.floor((daysRound/10)%10)+\".src\");\n")
		.append(" 		document.images.b.src = eval(\"c\"+Math.floor(daysRound%10)+\".src\");\n")
		.append(" 	}\n")
		.append(" 	else if (daysRound <= 999){\n")
		.append(" 		document.images.x.src = eval(\"c\"+Math.floor(daysRound/100)+\".src\");\n")
		.append(" 		document.images.a.src = eval(\"c\"+Math.floor((daysRound/10)%10)+\".src\");\n")
		.append(" 		document.images.b.src = eval(\"c\"+Math.floor(daysRound%10)+\".src\");\n")
		.append(" 	}\n")

		.append(" 	}\n")
		.append(" 	else if(blink){\n")
		.append(" 	  document.images.h.src = Ec.src;\n")
		.append(" 	  document.images.g.src = Ec.src;\n")
		.append(" 	  document.images.e.src = Ec.src;\n")
		.append(" 	  document.images.d.src = Ec.src;\n")
		.append(" 	  document.images.z.src = Ec.src;\n")
		.append(" 	  document.images.y.src = Ec.src;\n")
		.append(" 	  document.images.x.src = Ec.src;\n")
		.append(" 	  document.images.b.src = Ec.src;\n")
		.append(" 	  document.images.a.src = Ec.src;\n")
		.append(" 	  blink = false;\n")

		.append(" 	}\n")
		.append(" 	else{\n")
		.append(" 	  document.images.h.src = c0.src;\n")
		.append(" 	  document.images.g.src = c0.src;\n")
		.append(" 	  document.images.e.src = c0.src;\n")
		.append(" 	  document.images.d.src = c0.src;\n")
		.append(" 	  document.images.z.src = c0.src;\n")
		.append(" 	  document.images.y.src = c0.src;\n")
		.append(" 	  document.images.x.src = c0.src;\n")
		.append(" document.images.b.src = c0.src;\n")
		.append(" 	  document.images.a.src = c0.src;\n")
		.append(" 	  blink = true;\n")
		.append(" 	}\n")
		.append(" 	newtime = window.setTimeout(\"getTime(blink);\", 1000);\n")
		.append(" 	}\n");
		
		return script.toString();
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return "is.idega.block.counters";
	}
	/**
	 * @param digitFolderURL The digitFolderURL to set.
	 */
	public void setDigitFolderURL(String digitFolderURL) {
		this.digitFolderURL = digitFolderURL;
	}
	/**
	 * @param digitHeight The digitHeight to set.
	 */
	public void setDigitHeight(String digitHeight) {
		this.digitHeight = digitHeight;
	}
	/**
	 * @param digitWidth The digitWidth to set.
	 */
	public void setDigitWidth(String digitWidth) {
		this.digitWidth = digitWidth;
	}
	/**
	 * @param iwb The iwb to set.
	 */
	public void setIwb(IWBundle iwb) {
		this.iwb = iwb;
	}
	/**
	 * @param seperatorHeight The seperatorHeight to set.
	 */
	public void setSeperatorHeight(String seperatorHeight) {
		this.seperatorHeight = seperatorHeight;
	}
	/**
	 * @param seperatorWidth The seperatorWidth to set.
	 */
	public void setSeperatorWidth(String seperatorWidth) {
		this.seperatorWidth = seperatorWidth;
	}
	/**
	 * @param showSeconds The showSeconds to set.
	 */
	public void setShowSeconds(boolean showSeconds) {
		this.showSeconds = showSeconds;
	}
	/**
	 * @param targetDate The targetDate to set.
	 */
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	
	public void setTargetTimestamp(Timestamp time){
		this.targetDate = new Date(time.getTime());
	}
	
	public void setTargetTimestamp(String timestamp){
		this.targetDate = new com.idega.util.IWTimestamp(timestamp).getDate();
	}
}

/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.presentation;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import se.nexus.nbs.sdk.NBSMessageHttp;
import com.idega.core.builder.business.ICBuilderConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Applet;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Script;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NBSSigningApplet extends PresentationObjectContainer {

	

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.block.pki";
	
//	private final static String PARM_SCRIPTABLE = "scriptable";
//	private final static String PARM_MAYSCRIPT = "mayscript";
	private final static String PARM_CABBASE = "cabbase";
	private final static String PARM_BUTTONCOLOR = "BUTTONCOLOR";
	private final static String PARM_USERCOLOR = "USERCOLOR";
	private final static String PARM_FIELDCOLOR = "FIELDCOLOR";
	private final static String PARM_MESSAGECOLOR = "MESSAGECOLOR";
	private final static String PARM_PANELCOLOR = "PANELCOLOR";
	private final static String PARM_FONT = "FONT";
	private final static String PARM_BUTTONFONT = "BUTTONFONT";
	private final static String PARM_USERFONT = "USERFONT";
	private final static String PARM_FIELDFONT = "FIELDFONT";
	private final static String PARM_MESSAGEFONT= "MESSAGEFONT";
	private final static String PARM_LOCALE = "locale";
	private final static String PARM_SIGNTEXT = "signtext";
//	private final static String PARM_SIGN_APPLET2_VIEWCLASS = "SignApplet2.viewclass";
	
	private HashMap appletParameterMap = new HashMap();
	private HashMap formParameterMap = new HashMap();
	
	private String _action = "";
	
	private String _appletWidth = null;
	private String _appletHeight = null;
	
	public final static String PARM_ERROR_MESSAGE = "text";
	private String _errorPageUrl = "error.jsp?";	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	NBSMessageHttp _nbsMessageHttp = null;

	private String _loggedOnPageID = null;
	private int _errorPageID = -1;
	private String _eventListenerClassName = null;
	private List _parameters = new Vector();

	private final static String BIDT_JAR_PATH_PROPERTY = "bidt_jar_path";
	private final static String BIDT_CAB_PATH_PROPERTY = "bidt_cab_path";
	private final static String DEFAULT_JAR_ARCHIVE = "archive/cbt_bidt_2_3_11.jar";
	private final static String DEFAULT_CAB_ARCHIVE = "archive/cbt_bidt_2_3_11.cab";


	public NBSSigningApplet(NBSMessageHttp nbsMessageHttp) {
		_nbsMessageHttp = nbsMessageHttp;

	}
	
	public void main(IWContext iwc) throws Exception{
				
		String html = new String(_nbsMessageHttp.toHttpMessage().getBody());
				
		// getting the applet
		int appletStart = html.indexOf("<APPLET");
		int appletEnd = html.indexOf("</APPLET>");
		String appletString = html.substring(appletStart, appletEnd);

//		<APPLET align="top" width="560" height="310" mayscript
//				code="com.ibm.cbt_bidt_2_3_11.thinclient.SignApplet2.class"        
//				archive="archive/cbt_bidt_2_3_11.jar"
//				name=SignApplet>
//			<param name="scriptable"      value="true">
//			<param name="mayscript"       value="true">
//			<param  name="cabbase" value="archive/cbt_bidt_2_3_11.cab">
//			<param name=BUTTONCOLOR value="#000000,#AAAAAA">
//			<param name=USERCOLOR value="#000000,#DDDDDD">
//			<param name=FIELDCOLOR value="#000000,#FFFFFF">
//			<param name=MESSAGECOLOR value="#FF0000,#FFFFFF">
//			<param name=PANELCOLOR value="#000000,#DDDDDD">
//			<PARAM  NAME="FONT"         VALUE="Dialog,PLAIN,12">
//			<PARAM  NAME="BUTTONFONT"   VALUE="Dialog,PLAIN,12">
//			<PARAM  NAME="USERFONT"     VALUE="Dialog,PLAIN,12">
//			<PARAM  NAME="FIELDFONT"    VALUE="Dialog,PLAIN,12">
//			<PARAM  NAME="MESSAGEFONT"  VALUE="Dialog,PLAIN,12">
//			<param name=locale value="sv">
//			<PARAM NAME="signtext" VALUE="ZHNhZnNkZmFzZGZh">
//			<PARAM NAME="SignApplet2.viewclass" VALUE="SignViewKeyInfo">
//		</APPLET>		
			 
		// prosessing the applet
		int appletConfStart = appletString.indexOf("<APPLET");
		int appletConfEnd = appletString.indexOf(">");
		String appletConfString = appletString.substring(appletConfStart, appletConfEnd) + ">";
		String appletParmString = appletString.substring((appletConfEnd+1),appletString.length());
		
		//prosess the appletConfString
//		<APPLET align="top" width="560" height="310" mayscript
//				code="com.ibm.cbt_bidt_2_3_11.thinclient.SignApplet2.class"        
//				archive="archive/cbt_bidt_2_3_11.jar"
//				name=SignApplet>
		
		String appletAlign = getAttributeValue(appletConfString,"align=\"","\"");
		String appletWidth = getAttributeValue(appletConfString,"width=\"","\"");
		String appletHeight = getAttributeValue(appletConfString,"height=\"","\"");
		String appletClass = getAttributeValue(appletConfString,"code=\"","\"");
		//String appletArchive = getAttributeValue(appletConfString,"archive=\"","\"");
		String appletName = getAttributeValue(appletConfString,"name=",">");
		
		Applet applet = new Applet();
		applet.setAlignment(appletAlign);
		applet.setWidth(((_appletWidth!=null)?_appletWidth:appletWidth));
		applet.setHeight(((_appletHeight!=null)?_appletHeight:appletHeight));
		applet.setAppletClass(appletClass);
		applet.setCodeArchive(getJarFilepath(iwc));
		applet.setAppletName(appletName);
		
		//prosess the appletParmString
//		<param name="scriptable"      value="true">
//		<param name="mayscript"       value="true">
//		<param  name="cabbase" value="archive/cbt_bidt_2_3_11.cab">
//		<param name=BUTTONCOLOR value="#000000,#AAAAAA">
//		<param name=USERCOLOR value="#000000,#DDDDDD">
//		<param name=FIELDCOLOR value="#000000,#FFFFFF">
//		<param name=MESSAGECOLOR value="#FF0000,#FFFFFF">
//		<param name=PANELCOLOR value="#000000,#DDDDDD">
//		<PARAM  NAME="FONT"         VALUE="Dialog,PLAIN,12">
//		<PARAM  NAME="BUTTONFONT"   VALUE="Dialog,PLAIN,12">
//		<PARAM  NAME="USERFONT"     VALUE="Dialog,PLAIN,12">
//		<PARAM  NAME="FIELDFONT"    VALUE="Dialog,PLAIN,12">
//		<PARAM  NAME="MESSAGEFONT"  VALUE="Dialog,PLAIN,12">
//		<param name=locale value="sv">
//		<PARAM NAME="signtext" VALUE="ZHNhZnNkZmFzZGZh">
//		<PARAM NAME="SignApplet2.viewclass" VALUE="SignViewKeyInfo">
		
		this.setCabFilepath(iwc);
		
		String lowerCaseAppletParmString = appletParmString.toLowerCase();
		Vector appletParameters = new Vector();
		
		int lastEnd =0;
		int start;
		int end;
		do{
			start = lowerCaseAppletParmString.indexOf("<",lastEnd);
			end = lowerCaseAppletParmString.indexOf(">",start);
			
			if(start==-1 || end==-1){
				break;
			} else {
				appletParameters.add(appletParmString.substring(start,end+1));			
			}
			lastEnd=end;
		} while(start!=-1 && end!=-1);
		

		start = -1;
		end = -1;
		String nameAttributeName = "name=";
		String valueAttributeName = "value=";
		int size=appletParameters.size();
		for(int i=0; i<size; i++){
			String currentParam = (String)appletParameters.get(i);
			String lowerCaseCurrentParam = currentParam.toLowerCase();
			
			start = lowerCaseCurrentParam.indexOf(nameAttributeName);
			end = lowerCaseCurrentParam.indexOf(" ",start+nameAttributeName.length());
			String name = currentParam.substring(start+nameAttributeName.length(),end);
			if(name.charAt(0)=='\"'){
				name = name.substring(1,name.length()-1);
			}
			
			
			String value = (String) appletParameterMap.get(name);
			if(value == null){
				start = lowerCaseCurrentParam.indexOf(valueAttributeName);
				end = lowerCaseCurrentParam.indexOf(">",start+valueAttributeName.length());
				value = currentParam.substring(start+valueAttributeName.length(),end);
				if(value.charAt(0)=='\"'){
					value = value.substring(1,value.lastIndexOf('\"'));
				}
			}
			
			applet.setParam(name,value);
		}
		
		
		this.add(applet);
		


		
		//getting the form
		int formStart = html.indexOf("<FORM ");
		int formEnd = html.indexOf("</FORM>");
		String formString = html.substring(formStart, formEnd);
		
//		<FORM   NAME="LogonForm"
//				METHOD="POST"
//				ACTION="/nacka/pki">
//		<INPUT  NAME=cbtInput VALUE="" TYPE=HIDDEN>
//		<INPUT  NAME=cbtAction VALUE="Sign" TYPE=HIDDEN>
//		</FORM>

		int formConfStart = formString.indexOf("<FORM");
		int formConfEnd = formString.indexOf(">");
		String formConfString = formString.substring(formConfStart, formConfEnd) + ">";
		String formInputString = formString.substring((formConfEnd+1),formString.length());
		
		
		//prosess the formConfString
		String formName = getAttributeValue(formConfString,"NAME=\"","\"");
		String formMethod = getAttributeValue(formConfString,"METHOD=\"","\"");
		//String formAction = getAttributeValue(formConfString,"ACTION=\"","\"");

		
		Form loginForm = new Form();
		loginForm.setName(formName);
		loginForm.setMethod(formMethod);
		//loginForm.setAction(formAction);
		loginForm.setAction(_action);
		

		//prosess the formInputString
		String lowerCaseFormInputString = formInputString.toLowerCase();
		Vector formInputs = new Vector();

		lastEnd =0;
		start = -1;
		end = -1;
		do{
			start = lowerCaseFormInputString.indexOf("<",lastEnd);
			end = lowerCaseFormInputString.indexOf(">",start);
	
			if(start==-1 || end==-1){
				break;
			} else {
				formInputs.add(formInputString.substring(start,end+1));			
			}
			lastEnd=end;
		} while(start!=-1 && end!=-1);


		start = -1;
		end = -1;
		nameAttributeName = "name=";
		valueAttributeName = "value=";
		size=formInputs.size();
		for(int i=0; i<size; i++){
			String currentInput = (String)formInputs.get(i);
			String lowerCaseCurrentInput = currentInput.toLowerCase();
	
			start = lowerCaseCurrentInput.indexOf(nameAttributeName);
			end = lowerCaseCurrentInput.indexOf(" ",start+nameAttributeName.length());
			String name = currentInput.substring(start+nameAttributeName.length(),end);
			if(name.charAt(0)=='\"'){
				name = name.substring(1,name.length()-1);
			}
	
	
			String value = (String) formParameterMap.get(name);
			if(value == null){
				start = lowerCaseCurrentInput.indexOf(valueAttributeName);
				end = lowerCaseCurrentInput.indexOf(" ",start+valueAttributeName.length());
				value = currentInput.substring(start+valueAttributeName.length(),end);
				if(value.charAt(0)=='\"'){
					value = value.substring(1,value.lastIndexOf('\"'));
				}
			}
			loginForm.addParameter(name,value);
		}
		if (_loggedOnPageID != null){
			loginForm.addParameter("ib_page", _loggedOnPageID);
		} else if (iwc.getParameter("ib_page") != null) {
			loginForm.addParameter("ib_page", iwc.getParameter("ib_page"));	
		}
		String idegaSessionVal = iwc.getSessionId();
		if(idegaSessionVal!=null){
			loginForm.addParameter("idega_session_id",idegaSessionVal);
		}
		String iwLanguageVal = iwc.getParameter("iw_language");
		if(iwLanguageVal!=null){
			loginForm.addParameter("iw_language",iwLanguageVal);
		}
		
		size = _parameters.size();
		for (int i = 0; i < size; i++) {
			Parameter parm = (Parameter)_parameters.get(i);
			loginForm.add(parm);
		}
		
		
		//if some eventListenerClassName is set then a hidden input is added to the form 
		//same as form.setEventListener(String str) 
		if(_eventListenerClassName != null){
			loginForm.setEventListener(_eventListenerClassName);
			//loginForm.addParameter(IWMainApplication.IdegaEventListenerClassParameter,IWMainApplication.getEncryptedClassName(_eventListenerClassName));
		}
		
		
		add(new Form());
		this.add(loginForm);


			 
		//getting the script
		int scriptStart = html.indexOf("<script ");
		int scritpEnd = html.indexOf("</script>");
		String scriptString = html.substring(scriptStart, scritpEnd) + "</script>";
		
//		<script language="JavaScript">
//		
//		  function onSignOK(signMessage) {
//			document.LogonForm.cbtInput.value = signMessage;
//			document.LogonForm.submit()
//		  }
//		     
//		  function onSignCancel(reason, message) { 
//			  alert(reason+"; "+message);
//			location = "error.jsp?text="+message;
//		  }   
//		</script>	

		Script script = new Script();
		
		if(scriptString.indexOf("onLogonOK")!= -1){
			script.addFunction("onLogonOK","\n function onLogonOK(logonMessage) {\n document.LogonForm.cbtInput.value = logonMessage;\n document.LogonForm.submit()\n }\n");
			if(_errorPageID != -1){
				script.addFunction("onLogonCancel","\n function onLogonCancel(reason, message) {\n alert(reason+\"; \"+message);\n location = \""+iwc.getRequestURI()+"?"+ICBuilderConstants.IB_PAGE_PARAMETER+"="+_errorPageID+"&"+PARM_ERROR_MESSAGE+"=\"+message;\n }\n ");
			} else {
				script.addFunction("onLogonCancel","\n function onLogonCancel(reason, message) {\n alert(reason+\"; \"+message);\n location = \""+_errorPageUrl+((_errorPageUrl.indexOf('?')==-1)?"?":"")+PARM_ERROR_MESSAGE+"=\"+message;\n }\n ");
			}
		} else {
			script.addFunction("noSignOK","\n function onSignOK(signMessage) {\n document.LogonForm.cbtInput.value = signMessage;\n document.LogonForm.submit()\n }\n");
			if(_errorPageID != -1){
				script.addFunction("onSignCancel","\n function onSignCancel(reason, message) {\n alert(reason+\"; \"+message);\n location = \""+iwc.getRequestURI()+"?"+ICBuilderConstants.IB_PAGE_PARAMETER+"="+_errorPageID+"&"+PARM_ERROR_MESSAGE+"=\"+message;\n }\n ");
			} else {
				script.addFunction("onSignCancel","\n function onSignCancel(reason, message) {\n alert(reason+\"; \"+message);\n location = \""+_errorPageUrl+((_errorPageUrl.indexOf('?')==-1)?"?":"")+PARM_ERROR_MESSAGE+"=\"+message;\n }\n ");
			}
		}
		this.add(script);

	}
	
	private String getAttributeValue(String theString, String attribute,String endTag){
		int start = theString.indexOf(attribute);
		int end = theString.indexOf(endTag,start+attribute.length());
		String attributeValue = theString.substring(start+attribute.length(),end);
		return attributeValue;
	}
	
	
	/**
	 * @return 
	 */
	public int getErrorPageID() {
		return _errorPageID;
	}

	/**
	 * @return 
	 */
	public String getLoggedOnPageID() {
		return _loggedOnPageID;
	}

	/**
	 * @param pageID - pageId of the page to go to if login is not successful
	 */
	public void setErrorPageID(int pageId) {
		_errorPageID = pageId;
	}

	/**
	 * @param pageID - id of the page to go to if login is successful
	 */
	public void setLoggedOnPageID(String pageID) {
		_loggedOnPageID = pageID;
	}

	/**
	 * @param string
	 */
	public void setEventListenerClassName(String eventListenerClassName) {
		_eventListenerClassName = eventListenerClassName;
	}

	private String getJarFilepath(IWContext iwc) {
		IWBundle iwb = this.getBundle(iwc);
		String jarArchive = iwb.getProperty(BIDT_JAR_PATH_PROPERTY);
		String path = iwb.getVirtualPathWithFileNameString(((jarArchive!=null)?jarArchive:DEFAULT_JAR_ARCHIVE));

		return path;
	}
	
	private void setCabFilepath(IWContext iwc) {
		IWBundle iwb = this.getBundle(iwc);
		String cabArchive = iwb.getProperty(BIDT_CAB_PATH_PROPERTY);
		String path = iwb.getVirtualPathWithFileNameString(((cabArchive!=null)?cabArchive:DEFAULT_CAB_ARCHIVE));
		appletParameterMap.put(PARM_CABBASE, path);
	}

	public void addParameter(Parameter parm) {
		if (parm != null) {
			_parameters.add(parm);
		}
	}
	




//	public void setScriptable(boolean value){
//		appletParameterMap.put(PARM_SCRIPTABLE,String.valueOf(value));
//	}
	
	public void setButtonColor(String color1, String color2){
		appletParameterMap.put(PARM_BUTTONCOLOR,color1+","+color2);
	}
	
	public void setUserColor(String color1, String color2){
		appletParameterMap.put(PARM_USERCOLOR,color1+","+color2);
	}
		
	public void setFieldColor(String color1, String color2){
		appletParameterMap.put(PARM_FIELDCOLOR,color1+","+color2);
	}
		
	public void setMessageColor(String color1, String color2){
		appletParameterMap.put(PARM_MESSAGECOLOR,color1+","+color2);
	}
	
	public void setPanelColor(String color1, String color2){
		appletParameterMap.put(PARM_PANELCOLOR,color1+","+color2);
	}
	
	
	public void setFont(String face, String style, int size){
		appletParameterMap.put(PARM_FONT,face+","+style+","+size);
	}
		
	public void setButtonFont(String face, String style, int size){
		appletParameterMap.put(PARM_BUTTONFONT,face+","+style+","+size);
	}
	
	public void setUserFont(String face, String style, int size){
		appletParameterMap.put(PARM_USERFONT,face+","+style+","+size);
	}
	
	public void setFieldFont(String face, String style, int size){
		appletParameterMap.put(PARM_FIELDFONT,face+","+style+","+size);
	}
	
	public void setMessageFont(String face, String style, int size){
		appletParameterMap.put(PARM_MESSAGEFONT,face+","+style+","+size);
	}
	
	
	
	public void setLocale(String locale){
		appletParameterMap.put(PARM_LOCALE,locale);	
	}
	
	public void setSignText(String signText){
		appletParameterMap.put(PARM_SIGNTEXT,signText);
	}
	
	public void setErrorPageUrl(String url){
		_errorPageUrl = url;
	}
	


	/**
	 * @param string
	 */
	public void setAppletHeight(String height) {
		_appletHeight = height;
	}

	/**
	 * @param string
	 */
	public void setAppletWidth(String width) {
		_appletWidth = width;
	}

}

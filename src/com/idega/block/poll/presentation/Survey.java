/*
 * Created on 27.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.poll.presentation;

import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.util.IWTimestamp;

/**
 * Title:		Survey
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class Survey extends Block {

	private int _iLocaleID;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.poll";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwbPoll;
	
	private IWTimestamp _date;
	
	private String style_question;
	private String style_answer;
//	private String style_checkbox;
//	private String style_radiobutton;
//	private String style_textbox;
//	private String style_textarea;
//	private String style_submitbutton;
	private String style_form_element;
	
	public final static String MODE_EDIT = "edit";
	public static final String MODE_POLL = "poll";
	private String _mode = MODE_POLL;
	public final static String PRM_MODE = "mfpo_mode";
	public final static String PRM_SWITCHTO_MODE = "mfpo_swto_mode";
	


	/**
	 * 
	 */
	public Survey() {
		super();
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void initializeInMain(IWContext iwc) throws Exception {
		super.initializeInMain(iwc);
		_iwrb = getResourceBundle(iwc);
		_iwb = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		_iwbPoll = getBundle(iwc);
		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
		_date = new IWTimestamp();
		
		
		processParameters(iwc);
	}

	/**
	 * @param iwc
	 */
	private void processParameters(IWContext iwc) {
		
		String mode = iwc.getParameter(PRM_MODE);
		String switchToMode = iwc.getParameter(PRM_SWITCHTO_MODE);
		if(switchToMode != null){
			_mode = switchToMode;
		} else if(mode!= null){
			_mode = mode;
		}


		
	}

	public void main(IWContext iwc) throws Exception {
		
		
		if(_mode.equals(MODE_EDIT)){
			add(new SurveyEditor(this.getICObjectInstanceID()));
		} else {
			if(this.hasEditPermission()){
				add(getAdminPart());
			}
		}

		
		
	}
	
	private Link getAdminPart() {
		Image editImage = _iwb.getImage("shared/edit.gif");
		//Link adminLink = new Link(_iwb.getImage("shared/edit.gif"));
		Link adminLink = new Link(editImage);
		adminLink.addParameter(PRM_SWITCHTO_MODE,MODE_EDIT);
//		adminLink.setWindowToOpen(PollAdminWindow.class, this.getICObjectInstanceID());
//		adminLink.addParameter(PollAdminWindow.prmID, pollID);
//		if (newObjInst)
//			adminLink.addParameter(PollAdminWindow.prmObjInstId, getICObjectInstanceID());
//		else if (newWithAttribute)
//			adminLink.addParameter(PollAdminWindow.prmAttribute, _sAttribute);

		return adminLink;
	}
	
	public synchronized Object clone(){
		//TMP		
		return (Survey)super.clone();
	}


}

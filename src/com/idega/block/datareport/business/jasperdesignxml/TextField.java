/*
 * Created on 31.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import com.idega.xml.XMLElement;

/**
 * Title:		TextField
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class TextField extends XMLElement {
	
	private static final String ATTRIBUTE_IS_STRETCH_WITH_OVERFLOW = "isStretchWithOverflow"; // (true | false) "false"
	
	private static final String ATTRIBUTE_EVALUATION_TIME = "evaluationTime"; // (Now | Report | Page | Column | Group) "Now"
	private static final String VALUE_EVALUATION_TIME_NOW = "Now";
	private static final String VALUE_EVALUATION_TIME_REPORT = "Report"; 
	private static final String VALUE_EVALUATION_TIME_PAGE = "Page";
	private static final String VALUE_EVALUATION_TIME_COLUMN = "Column";
	private static final String VALUE_EVALUATION_TIME_GROUP = "Group";
	
	//evaluationGroup CDATA #IMPLIED
	//pattern CDATA #IMPLIED
	
	private static final String ATTRIBUTE_IS_BLANK_WHEN_NULL = "isBlankWhenNull"; // (true | false) "false"
	
	private static final String ATTRIBUTE_HYPERLINK_TYPE = "hyperlinkType"; // (None | Reference | LocalAnchor | LocalPage | RemoteAnchor | RemotePage) "None"
	private static final String VALUE_HYPERLINK_TYPE_NONE = "None";
	private static final String VALUE_HYPERLINK_TYPE_REFERENCE = "Reference";
	private static final String VALUE_HYPERLINK_TYPE_LOCAL_ANCHOR = "LocalAnchor";
	private static final String VALUE_HYPERLINK_TYPE_LOCAL_PAGE = "LocalPage";
	private static final String VALUE_HYPERLINK_TYPE_REMOTE_ANCHOR = "RemoteAnchor";
	private static final String VALUE_HYPERLINK_TYPE_REMOTE_PAGE = "RemotePage";
	
	/**
	 * @param name
	 */
	public TextField() {
		super("textField");
	}
	
	public void setIsStretchWithOverflow(boolean value){
		this.setAttribute(ATTRIBUTE_IS_STRETCH_WITH_OVERFLOW, Boolean.toString(value));
	}
	
	public void setEvaluationTimeAsNow(){
		this.setAttribute(ATTRIBUTE_EVALUATION_TIME, VALUE_EVALUATION_TIME_NOW);
	}
	
	public void setEvaluationTimeAsReport(){
		this.setAttribute(ATTRIBUTE_EVALUATION_TIME, VALUE_EVALUATION_TIME_REPORT);
	}

	public void setEvaluationTimeAsPage(){
		this.setAttribute(ATTRIBUTE_EVALUATION_TIME, VALUE_EVALUATION_TIME_PAGE);
	}

	public void setEvaluationTimeAsColumn(){
		this.setAttribute(ATTRIBUTE_EVALUATION_TIME, VALUE_EVALUATION_TIME_COLUMN);
	}

	public void setEvaluationTimeAsGroup(){
		this.setAttribute(ATTRIBUTE_EVALUATION_TIME, VALUE_EVALUATION_TIME_GROUP);
	}
	
	public void setIsBlankWhenNull(boolean value){
		this.setAttribute(ATTRIBUTE_IS_BLANK_WHEN_NULL, Boolean.toString(value));
	}

	public void setHyperlinkTypeAsNone(){
		this.setAttribute(ATTRIBUTE_HYPERLINK_TYPE, VALUE_HYPERLINK_TYPE_NONE);
	}
	
	public void setHyperlinkTypeAsReference(){
		this.setAttribute(ATTRIBUTE_HYPERLINK_TYPE, VALUE_HYPERLINK_TYPE_REFERENCE);
	}

	public void setHyperlinkTypeAsLocalAnchor(){
		this.setAttribute(ATTRIBUTE_HYPERLINK_TYPE, VALUE_HYPERLINK_TYPE_LOCAL_ANCHOR);
	}

	public void setHyperlinkTypeAsLocalPage(){
		this.setAttribute(ATTRIBUTE_HYPERLINK_TYPE, VALUE_HYPERLINK_TYPE_LOCAL_PAGE);
	}

	public void setHyperlinkTypeAsRemoteAnchor(){
		this.setAttribute(ATTRIBUTE_HYPERLINK_TYPE, VALUE_HYPERLINK_TYPE_REMOTE_ANCHOR);
	}
	
	public void setHyperlinkTypeAsRemotePage(){
		this.setAttribute(ATTRIBUTE_HYPERLINK_TYPE, VALUE_HYPERLINK_TYPE_REMOTE_PAGE);
	}

}

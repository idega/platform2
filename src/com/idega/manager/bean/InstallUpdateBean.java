/*
 * $Id: InstallUpdateBean.java,v 1.2 2004/11/04 12:03:20 thomas Exp $
 * Created on Nov 3, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.manager.bean;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;


/**
 * 
 *  Last modified: $Date: 2004/11/04 12:03:20 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class InstallUpdateBean {
	
	   private HtmlForm form1 = new HtmlForm();

	    public HtmlForm getForm1() {
	        return form1;
	    }

	    public void setForm1(HtmlForm hf) {
	        this.form1 = hf;
	    }

	    private HtmlSelectBooleanCheckbox checkbox1 = new HtmlSelectBooleanCheckbox();

	    public HtmlSelectBooleanCheckbox getCheckbox1() {
	        return checkbox1;
	    }

	    public void setCheckbox1(HtmlSelectBooleanCheckbox hsbc) {
	        this.checkbox1 = hsbc;
	    }

	    private HtmlSelectBooleanCheckbox checkbox2 = new HtmlSelectBooleanCheckbox();

	    public HtmlSelectBooleanCheckbox getCheckbox2() {
	        return checkbox2;
	    }

	    public void setCheckbox2(HtmlSelectBooleanCheckbox hsbc) {
	        this.checkbox2 = hsbc;
	    }

	    private HtmlCommandButton button1 = new HtmlCommandButton();

	    public HtmlCommandButton getButton1() {
	        return button1;
	    }

	    public void setButton1(HtmlCommandButton hcb) {
	        this.button1 = hcb;
	    }

	    private HtmlCommandButton button2 = new HtmlCommandButton();

	    public HtmlCommandButton getButton2() {
	        return button2;
	    }

	    public void setButton2(HtmlCommandButton hcb) {
	        this.button2 = hcb;
	    }

	    private HtmlInputText textField1 = new HtmlInputText();

	    public HtmlInputText getTextField1() {
	        return textField1;
	    }

	    public void setTextField1(HtmlInputText hit) {
	        this.textField1 = hit;
	    }

	    private HtmlInputText textField2 = new HtmlInputText();

	    public HtmlInputText getTextField2() {
	        return textField2;
	    }

	    public void setTextField2(HtmlInputText hit) {
	        this.textField2 = hit;
	    }
}

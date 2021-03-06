/*
 * $Id: ReferenceNumber.java,v 1.27.2.1 2007/01/12 19:31:19 idegaweb Exp $
 * 
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 * 
 */
package com.idega.block.application.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HelpButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason </a>
 * @version 1.0
 */
public class ReferenceNumber extends Block {

	public static final String CAM_REF_NUMBER = "cam_ref_number";

	public static final int LAYOUT_VERTICAL = 1;

	public static final int LAYOUT_HORIZONTAL = 2;

	public static final int LAYOUT_STACKED = 3;

	private int inputLength = 10;

	private int layout = -1;

	private int pageId;

	private int referenceTextSize;

	private String backgroundImageUrl = null;

	private String referenceWidth = "";

	private String referenceHeight = "";

	private String referenceText;

	private String colour = "";

	private String referenceTextColour;

	private String styleAttribute = "font-size: 10pt";

	private String textStyles = "font-family: Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: #000000; text-decoration: none;";

	private String submitButtonAlignment;

	private boolean hasHelpButton = false;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";

	private Table outerTable;

	private Form myForm;

	private String submitButtonText = null;

	protected IWResourceBundle iwrb;

	protected IWBundle iwb;

	public ReferenceNumber() {
		super();
		setDefaultValues();
	}

	public void main(IWContext iwc) throws Exception {
		this.iwb = getBundle(iwc);
		this.iwrb = getResourceBundle(iwc);

		this.submitButtonText = this.iwrb.getLocalizedString("get", "Get");

		this.referenceText = this.iwrb.getLocalizedString("referenceNumber", "Referencenumber");
		setup();

		this.outerTable.add(this.myForm);
		add(this.outerTable);
	}

	private void setup() {
		Table referenceTable = new Table(1, 2);
		referenceTable.setBorder(0);
		referenceTable.setWidth(this.referenceWidth);
		referenceTable.setHeight(this.referenceHeight);
		if (!this.colour.equals("")) {
			referenceTable.setColor(this.colour);
		}
		referenceTable.setCellpadding(0);
		referenceTable.setCellspacing(0);
		if (!"".equals(this.backgroundImageUrl)) {
			referenceTable.setBackgroundImage(new Image(this.backgroundImageUrl));
		}

		HelpButton helpButton = new HelpButton(this.iwrb.getLocalizedString("help_headline", "Reference number"),
				this.iwrb.getLocalizedString("help", "Help"));

		Text referenceTexti = new Text(this.referenceText);
		if (this.referenceTextSize != -1) {
			referenceTexti.setFontSize(this.referenceTextSize);
		}

		if (this.referenceTextColour != null) {
			referenceTexti.setFontColor(this.referenceTextColour);
		}

		referenceTexti.setFontStyle(this.textStyles);

		Table inputTable;

		TextInput reference = new TextInput(CAM_REF_NUMBER);
		reference.setMarkupAttribute("style", this.styleAttribute);
		reference.setSize(this.inputLength);

		switch (this.layout) {
			case LAYOUT_HORIZONTAL:
				inputTable = new Table(3, 2);
				inputTable.setBorder(0);
				if (!(this.colour.equals(""))) {
					inputTable.setColor(this.colour);
				}
				inputTable.setCellpadding(0);
				inputTable.setCellspacing(0);
				inputTable.setAlignment(2, 1, "right");
				inputTable.setAlignment(2, 2, "right");
				inputTable.setWidth("100%");

				inputTable.add(referenceTexti, 2, 1);
				inputTable.add(reference, 2, 2);
				inputTable.setAlignment(2, 1, "right");
				inputTable.setAlignment(2, 2, "right");

				referenceTable.add(inputTable, 1, 1);
				break;

			case LAYOUT_VERTICAL:
				inputTable = new Table(3, 3);
				inputTable.setBorder(0);
				if (!(this.colour.equals(""))) {
					inputTable.setColor(this.colour);
				}
				inputTable.setCellpadding(0);
				inputTable.setCellspacing(0);
				inputTable.mergeCells(1, 2, 3, 2);
				inputTable.addText("", 1, 2);
				inputTable.setHeight(2, "10");
				inputTable.setAlignment(1, 1, "right");
				inputTable.setAlignment(1, 3, "right");

				inputTable.add(referenceTexti, 1, 1);
				inputTable.add(reference, 3, 1);

				referenceTable.add(inputTable, 1, 1);
				break;

			case LAYOUT_STACKED:
				inputTable = new Table(1, 2);
				inputTable.setBorder(0);
				inputTable.setCellpadding(0);
				inputTable.setCellspacing(0);
				inputTable.addText("", 1, 2);
				inputTable.setHeight(1, "2");
				if (!(this.colour.equals(""))) {
					inputTable.setColor(this.colour);
				}
				inputTable.setAlignment(1, 1, "left");
				inputTable.setAlignment(1, 2, "left");

				inputTable.add(referenceTexti, 1, 1);
				inputTable.add(reference, 1, 2);

				referenceTable.setAlignment(1, 1, "center");
				referenceTable.add(inputTable, 1, 1);
				break;
		}

		Table submitTable = new Table(1, 1);
		if (this.hasHelpButton) {
			submitTable = new Table(2, 1);
		}
		submitTable.setBorder(0);
		if (!this.colour.equals("")) {
			submitTable.setColor(this.colour);
		}
		submitTable.setRowVerticalAlignment(1, "middle");
		if (!this.hasHelpButton) {
			submitTable.setAlignment(1, 1, this.submitButtonAlignment);
		}
		else {
			submitTable.setAlignment(2, 1, "right");
		}
		submitTable.setWidth("100%");

		if (!this.hasHelpButton) {
			submitTable.add(new SubmitButton("tengja", this.submitButtonText), 1, 1);
		}
		else {
			submitTable.add(new SubmitButton("tengja", this.submitButtonText), 2, 1);
			submitTable.add(helpButton, 1, 1);
		}

		referenceTable.add(submitTable, 1, 2);
		this.myForm.add(referenceTable);
		if (this.pageId > 0) {
			this.myForm.setPageToSubmitTo(this.pageId);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void setHelpButton(boolean usehelp) {
		this.hasHelpButton = usehelp;
	}

	public void addHelpButton() {
		this.hasHelpButton = true;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	private void setDefaultValues() {
		this.referenceWidth = "148";
		this.referenceHeight = "89";
		this.submitButtonAlignment = "center";
		this.layout = LAYOUT_VERTICAL;

		this.outerTable = new Table();
		this.outerTable.setCellpadding(0);
		this.outerTable.setCellspacing(0);

		this.myForm = new Form();
		this.myForm.add(new HiddenInput("cam_fact_view", "50"));
		this.myForm.setMethod("post");
	}

	public void setVertical() {
		this.layout = LAYOUT_VERTICAL;
	}

	public void setHorizontal() {
		this.layout = LAYOUT_HORIZONTAL;
	}

	public void setStacked() {
		this.layout = LAYOUT_STACKED;
	}

	public void setStyle(String styleAttribute) {
		this.styleAttribute = styleAttribute;
	}

	public void setInputLength(int inputLength) {
		this.inputLength = inputLength;
	}

	public void setReferenceTextSize(int size) {
		this.referenceTextSize = size;
	}

	public void setReferenceTextColor(String color) {
		this.referenceTextColour = color;
	}

	public void setColor(String color) {
		this.colour = color;
	}

	public void setHeight(String height) {
		this.referenceHeight = height;
	}

	public void setWidth(String width) {
		this.referenceWidth = width;
	}

	public void setBackgroundImageUrl(String url) {
		this.backgroundImageUrl = url;
	}

	public void setSubmitButtonAlignment(String alignment) {
		this.submitButtonAlignment = alignment;
	}

	public void setTextStyle(String styleAttribute) {
		this.textStyles = styleAttribute;
	}

	public void setPage(com.idega.core.builder.data.ICPage page) {
		this.pageId = page.getID();
	}

	public synchronized Object clone() {
		ReferenceNumber obj = null;
		try {
			obj = (ReferenceNumber) super.clone();
			if (this.outerTable != null) {
				obj.outerTable = (Table) this.outerTable.clone();
			}
			if (this.myForm != null) {
				obj.myForm = (Form) this.myForm.clone();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
}
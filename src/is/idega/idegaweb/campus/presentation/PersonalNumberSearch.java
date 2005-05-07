/*
 * $Id: PersonalNumberSearch.java,v 1.8 2005/05/07 17:10:30 palli Exp $
 * 
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 * 
 */
package is.idega.idegaweb.campus.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HelpButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a>
 * @version 1.0
 */
public class PersonalNumberSearch extends Block {

	public static final String PERSONAL_NUMBER = "cam_personal_number";

	public static final int LAYOUT_VERTICAL = 1;

	public static final int LAYOUT_HORIZONTAL = 2;

	public static final int LAYOUT_STACKED = 3;

	private int inputLength = 10;

	private int maxInputLength = -1;

	private int layout = -1;

	private int pageId;

	private int numberTextSize;

	private String backgroundImageUrl = "";

	private String numberWidth = "";

	private String numberHeight = "";

	private String numberText;

	private String colour = "";

	private String numberTextColour;

	private String styleAttribute = "font-size: 10pt";

	private String textStyles = "font-family: Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: #000000; text-decoration: none;";

	private String submitButtonAlignment;

	private String submitButtonText = null;

	private boolean hasHelpButton = false;

	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";

	private Table outerTable;

	private Form myForm;

	protected IWResourceBundle iwrb;

	protected IWBundle iwb;

	public PersonalNumberSearch() {
		super();
		setDefaultValues();
	}

	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);

		// _numberImage = _iwrb.getLocalizedImageButton("get","Get");

		numberText = iwrb.getLocalizedString("personal_number", "Kennitala");
		submitButtonText = iwrb.getLocalizedString("get", "Get");
		setup();

		outerTable.add(myForm);
		add(outerTable);
	}

	private void setup() {
		Table numberTable = new Table(1, 2);
		numberTable.setBorder(0);
		numberTable.setWidth(numberWidth);
		numberTable.setHeight(numberHeight);
		if (!colour.equals("")) {
			numberTable.setColor(colour);
		}
		numberTable.setCellpadding(0);
		numberTable.setCellspacing(0);
		// if(!"".equals(_backgroundImageUrl))
		numberTable.setBackgroundImage(new Image(backgroundImageUrl));

		HelpButton helpButton = new HelpButton(iwrb.getLocalizedString("help_headline", "Personal number"),
				iwrb.getLocalizedString("personal_number_help", "Help"));

		Text numberTexti = new Text(numberText);
		if (numberTextSize != -1) {
			numberTexti.setFontSize(numberTextSize);
		}

		if (numberTextColour != null) {
			numberTexti.setFontColor(numberTextColour);
		}

		numberTexti.setFontStyle(textStyles);

		Table inputTable;

		TextInput number = new TextInput(PERSONAL_NUMBER);
		number.setMarkupAttribute("style", styleAttribute);
		number.setSize(inputLength);
		if (maxInputLength > 0)
			number.setMaxlength(maxInputLength);

		switch (layout) {
			case LAYOUT_HORIZONTAL:
				inputTable = new Table(3, 2);
				inputTable.setBorder(0);
				if (!(colour.equals(""))) {
					inputTable.setColor(colour);
				}
				inputTable.setCellpadding(0);
				inputTable.setCellspacing(0);
				inputTable.setAlignment(2, 1, "right");
				inputTable.setAlignment(2, 2, "right");
				inputTable.setWidth("100%");

				inputTable.add(numberTexti, 2, 1);
				inputTable.add(number, 2, 2);
				inputTable.setAlignment(2, 1, "right");
				inputTable.setAlignment(2, 2, "right");

				numberTable.add(inputTable, 1, 1);
				break;

			case LAYOUT_VERTICAL:
				inputTable = new Table(3, 3);
				inputTable.setBorder(0);
				if (!(colour.equals(""))) {
					inputTable.setColor(colour);
				}
				inputTable.setCellpadding(0);
				inputTable.setCellspacing(0);
				inputTable.setHorizontalAlignment("center");
				inputTable.mergeCells(1, 2, 3, 2);
				inputTable.addText("", 1, 2);
				inputTable.setHeight(2, "10");
				inputTable.setAlignment(1, 1, "right");
				inputTable.setAlignment(1, 3, "right");

				inputTable.add(numberTexti, 1, 1);
				inputTable.add(number, 3, 1);

				numberTable.add(inputTable, 1, 1);
				break;

			case LAYOUT_STACKED:
				inputTable = new Table(1, 2);
				inputTable.setBorder(0);
				inputTable.setCellpadding(0);
				inputTable.setCellspacing(0);
				inputTable.setHorizontalAlignment("center");
				inputTable.addText("", 1, 2);
				inputTable.setHeight(1, "2");
				if (!(colour.equals(""))) {
					inputTable.setColor(colour);
				}
				inputTable.setAlignment(1, 1, "left");
				inputTable.setAlignment(1, 2, "left");

				inputTable.add(numberTexti, 1, 1);
				inputTable.add(number, 1, 2);

				numberTable.add(inputTable, 1, 1);
				break;
		}

		Table submitTable = new Table(1, 1);
		if (hasHelpButton) {
			submitTable = new Table(2, 1);
		}
		submitTable.setBorder(0);
		if (!colour.equals("")) {
			submitTable.setColor(colour);
		}
		submitTable.setRowVerticalAlignment(1, "middle");
		if (!hasHelpButton) {
			submitTable.setAlignment(1, 1, submitButtonAlignment);
		}
		else {
			submitTable.setAlignment(2, 1, "right");
		}
		submitTable.setWidth("100%");

		if (!hasHelpButton) {
			submitTable.add(new SubmitButton(submitButtonText, "commit"), 1, 1);
		}
		else {
			submitTable.add(new SubmitButton(submitButtonText, "commit"), 2, 1);
			submitTable.add(helpButton, 1, 1);
		}

		numberTable.add(submitTable, 1, 2);
		myForm.add(numberTable);
		if (pageId > 0) {
			myForm.setPageToSubmitTo(pageId);
		}
	}

	public String getBundleIdentifier() {
		return (IW_BUNDLE_IDENTIFIER);
	}

	public void setHelpButton(boolean usehelp) {
		hasHelpButton = usehelp;
	}

	public void addHelpButton() {
		hasHelpButton = true;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	private void setDefaultValues() {
		numberWidth = "148";
		numberHeight = "89";
		submitButtonAlignment = "center";
		layout = LAYOUT_VERTICAL;

		outerTable = new Table();
		outerTable.setCellpadding(0);
		outerTable.setCellspacing(0);
		outerTable.setHorizontalAlignment("left");

		myForm = new Form();
		myForm.setMethod("post");
	}

	public void setVertical() {
		layout = LAYOUT_VERTICAL;
	}

	public void setHorizontal() {
		layout = LAYOUT_HORIZONTAL;
	}

	public void setStacked() {
		layout = LAYOUT_STACKED;
	}

	public void setStyle(String styleAttribute) {
		this.styleAttribute = styleAttribute;
	}

	public void setInputLength(int inputLength) {
		this.inputLength = inputLength;
	}

	public void setMaxInputLength(int inputLength) {
		maxInputLength = inputLength;
	}

	public void setnumberTextSize(int size) {
		numberTextSize = size;
	}

	public void setnumberTextColor(String color) {
		numberTextColour = color;
	}

	public void setColor(String color) {
		colour = color;
	}

	public void setHeight(String height) {
		numberHeight = height;
	}

	public void setWidth(String width) {
		numberWidth = width;
	}

	public void setBackgroundImageUrl(String url) {
		backgroundImageUrl = url;
	}

	public void setSubmitButtonAlignment(String alignment) {
		submitButtonAlignment = alignment;
	}

	public void setTextStyle(String styleAttribute) {
		textStyles = styleAttribute;
	}

	public void setPage(com.idega.core.builder.data.ICPage page) {
		pageId = ((Integer) page.getPrimaryKey()).intValue();
	}

	public synchronized Object clone() {
		PersonalNumberSearch obj = null;
		try {
			obj = (PersonalNumberSearch) super.clone();
			if (outerTable != null)
				obj.outerTable = (Table) outerTable.clone();
			if (myForm != null)
				obj.myForm = (Form) myForm.clone();

		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
}

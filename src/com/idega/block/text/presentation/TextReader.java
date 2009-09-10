package com.idega.block.text.presentation;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.2
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.idega.block.text.business.ContentFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.business.TextBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.business.TextFormatter;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.block.text.data.TxTextHome;
import com.idega.core.file.data.ICFile;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;

public class TextReader extends Block implements Builderaware {

	private boolean isAdmin = false;
	private boolean createInstance = true;

	private String sLocaleId;
	private String sAttribute = null;

	private int iTextId = -1;
	private int textSize = -1;
	private int tableTextSize = 1;
	private int headlineSize = -2;
	private String tableWidth = "";
	private String bgColor = null;
	private String textBgColor = null;
	private String textColor;
	private String headlineBgColor = null;
	private String headlineColor;
	private String tableAlignment = "top";
	private String textWidth = "100%";
	private String textStyle;
	private String textAlignment = "left";
	private String headlineStyle;
	private String spaceBetweenHeadlineAndBody = null;
	private boolean displayHeadline = true;
	private boolean enableDelete = false;
	private boolean reverse = false;
	private boolean crazy = false;
	private boolean viewall = false;
	private boolean newobjinst = false;
	private boolean newWithAttribute = false;
	
	private String textStyleName = "body";
	private String headlineStyleName = "headline";
	
	private String textStyleClassName = null;
	private String headlineStyleClassName = null;

	public static String prmTextId = "txtr.textid";

	private IWBundle iwb;
	private IWBundle iwcb;
	private IWResourceBundle iwrb;
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.text";
	public final static String CACHE_KEY = "tx_text";

	public TextReader() {
		setCacheable(getCacheKey(), 999999999); //cache indefinately
	}

	public TextReader(String sAttribute) {
		this();
		this.iTextId = -1;
		this.sAttribute = sAttribute;
	}

	public TextReader(int iTextId) {
		this();
		this.iTextId = iTextId;
		this.createInstance = false;
	}

	public void main(IWContext iwc) throws Exception {
		this.isAdmin = iwc.hasEditPermission(this);
		this.iwb = getBundle(iwc);
		this.iwcb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		this.iwrb = getResourceBundle(iwc);
		Locale locale = iwc.getCurrentLocale();

		TxText txText = null;
		LocalizedText locText = null;
		ContentHelper ch = null;
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		T.setWidth(this.textWidth);

		if (this.iTextId < 0) {
			String sTextId = iwc.getParameter(prmTextId);
			if (sTextId != null) {
				this.iTextId = Integer.parseInt(sTextId);
			}
			else if (getICObjectInstanceID() > 0) {
				this.iTextId = TextFinder.getObjectInstanceTextId(getICObjectInstance());
				if (this.iTextId <= 0) {
					this.newobjinst = true;
				}
			}
		}
		int iLocaleId = ICLocaleBusiness.getLocaleId(locale);

		if (this.iTextId > 0) {
			TxTextHome txHome = (TxTextHome) IDOLookup.getHome(TxText.class);
			txText = txHome.findByPrimaryKey(new Integer(this.iTextId));
		}
		else if (this.sAttribute != null) {
			txText = TextFinder.getText(this.sAttribute);
			this.newWithAttribute = true;
		}

		boolean hasId = false;

		if (txText != null) {
			this.iTextId = txText.getID();
			ch = ContentFinder.getContentHelper(txText.getContentId(), iLocaleId);
			if (ch != null) {
				locText = ch.getLocalizedText();
			}
			hasId = true;
		}

		if (ch != null && locText != null) {
			T.add(getTextTable(txText, locText, ch), 1, 1);

		}
		if (this.isAdmin) {
			T.add(getAdminPart(this.iTextId, this.enableDelete, this.newobjinst, this.newWithAttribute, hasId), 1, 2);
		}

		T.setBorder(0);

		add(T);
	}

	public PresentationObject getTextTable(TxText txText, LocalizedText locText, ContentHelper contentHelper) throws IOException, SQLException {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		int headerRow = 1;
		int bodyRow = 2;

		T.setWidth("100%");
		if(this.bgColor != null) {
			T.setColor(this.bgColor);
		}
		String sHeadline = locText.getHeadline() != null ? locText.getHeadline() : "";
		Text headline = getStyleText(sHeadline,this.headlineStyleName);
		if(this.headlineStyleClassName != null) {
			headline.setStyleClass(this.headlineStyleClassName);
		}
		if (this.headlineSize > -1) {
			headline.setFontSize(this.headlineSize);
		}
		if (this.headlineColor != null) {
			headline.setFontColor(this.headlineColor);
		}
		//headline.setBold();
		//headline.setAttribute("class","headlinetext");
		if (this.headlineStyle != null) {
			headline.setFontStyle(this.headlineStyle);
		}

		String textBody = locText.getBody() != null ? locText.getBody() : "";

		if (this.reverse) {
			textBody = TextFormatter.textReverse(textBody);
		}
		if (this.crazy) {
			textBody = TextFormatter.textCrazy(textBody);
		}

		textBody = TextSoap.formatText(textBody);

		if (this.displayHeadline) {
			if (headline.getText() != null) {
				T.add(headline, 1, headerRow);
				if (this.spaceBetweenHeadlineAndBody != null) {
					T.setHeight(2, this.spaceBetweenHeadlineAndBody);
					bodyRow = 3;
				}
			}
		}
		else {
			bodyRow = 1;
		}

		Text body = getStyleText(textBody,this.textStyleName);
		if(this.textStyleClassName != null) {
			body.setStyleClass(this.textStyleClassName);
		}
		if (this.textSize > -1) {
			body.setFontSize(this.textSize);
		}
		if (this.textColor != null) {
			body.setFontColor(this.textColor);
		}
		//body.setAttribute("class","bodytext");
		if (this.textStyle != null) {
			body.setFontStyle(this.textStyle);
		}

		///////////////// Image /////////////////////
		List files = contentHelper.getFiles();
		if (files != null && files.size()>0) {
			//Iterator iter = files.iterator();
			//while (iter.hasNext()) {
			try {
				//ICFile imagefile = (ICFile)iter.next();
				ICFile imagefile = (ICFile) files.get(0);
				int imid = ((Integer)imagefile.getPrimaryKey()).intValue();
				String att = imagefile.getMetaData(TextEditorWindow.imageAttributeKey);

				Image textImage = new Image(imid);
				if (att != null) {
					textImage.addMarkupAttributes(getAttributeMap(att));
				}
				T.add(textImage, 1, bodyRow);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
			//}
		}
		T.add(body, 1, bodyRow);

		///////////////// Adding to tables /////////////////////
		T.setAttribute(1, bodyRow, "style", "text-align:" + this.textAlignment);

		if (this.headlineBgColor != null) {
			T.setRowColor(headerRow, this.headlineBgColor);
		}
		if (this.textBgColor != null) {
			T.setRowColor(bodyRow, this.textBgColor);
		}

		return T;
	}

	public PresentationObject getAdminPart(int iTextId, boolean enableDelete, boolean newObjInst, boolean newWithAttribute, boolean hasId) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		if(this.bgColor != null) {
		  T.setColor(this.bgColor);
		}
		int column = 1;

		if (iTextId > 0) {
			Link breyta = new Link(this.iwcb.getImage("/shared/edit.gif"));
			breyta.setWindowToOpen(TextEditorWindow.class);
			breyta.addParameter(TextEditorWindow.prmTextId, iTextId);
			breyta.addParameter(TextEditorWindow.prmObjInstId, getICObjectInstanceID());
			T.add(breyta, column++, 1);

			if (enableDelete) {
				Link delete = new Link(this.iwcb.getImage("/shared/delete.gif"));
				delete.setWindowToOpen(TextEditorWindow.class);
				delete.addParameter(TextEditorWindow.prmDelete, iTextId);
				T.add(delete, column++, 1);
			}
		}
		if (this.createInstance && newObjInst && !hasId) {
			Link newLink = new Link(this.iwcb.getImage("/shared/create.gif"));
			newLink.setWindowToOpen(TextEditorWindow.class);
			if (newObjInst) {
				newLink.addParameter(TextEditorWindow.prmObjInstId, getICObjectInstanceID());
			}
			else if (newWithAttribute) {
				newLink.addParameter(TextEditorWindow.prmAttribute, this.sAttribute);
			}

			T.add(newLink, column++, 1);
		}
		//T.setAlignment(1,1,"left");
		//T.setAlignment(2,1,"right");
		return T;

	}

	public boolean deleteBlock(int instanceid) {
		return TextBusiness.deleteBlock(instanceid);
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public void setBgColor(String bgColor) {
	  this.bgColor = bgColor;
	}

	public void setTextBgColor(String textBgColor) {
		this.textBgColor = textBgColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public void setBackgroundColor(String BgColor) {
		this.headlineBgColor = BgColor;
		this.textBgColor = BgColor;
	}

	public void setHeadlineBgColor(String headlineBgColor) {
		this.headlineBgColor = headlineBgColor;
	}

	public void setHeadlineColor(String headlineColor) {
		this.headlineColor = headlineColor;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public void setTableTextSize(int tableTextSize) {
		this.tableTextSize = tableTextSize;
	}

	public void setTableWidth(String tableWidth) {
		this.tableWidth = tableWidth;
	}

	public void setHeadlineSize(int headlineSize) {
		this.headlineSize = headlineSize;
	}

	public void setTextStyle(String textStyle) {
		this.textStyle = textStyle;
	}

	public void setTextAlignment(String alignment) {
		this.textAlignment = alignment;
	}

	public void setHeadlineStyle(String headlineStyle) {
		this.headlineStyle = headlineStyle;
	}

	public void displayHeadline(boolean displayHeadline) {
		this.displayHeadline = displayHeadline;
	}

	public void setEnableDelete(boolean enableDelete) {
		this.enableDelete = enableDelete;
	}

	public void setAlignment(String alignment) {
		setHorizontalAlignment(alignment);
	}

	/**
	 * Sets alignment for the table around the text - added by gimmi@idega.is
	 */
	public void setHorizontalAlignment(String alignment) {
		this.tableAlignment = alignment;
	}

	public void setWidth(String textWidth) {
		this.textWidth = textWidth;
	}

	public void setSpaceAfterHeadlin(String space) {
		setSpaceAfterHeadline(space);
	}

	public void setSpaceAfterHeadline(String space) {
		this.spaceBetweenHeadlineAndBody = space;
	}

	public void setReverse() {
		this.reverse = true;
	}

	public void setCrazy() {
		this.crazy = true;
	}

	public void setViewAll() {
		this.viewall = true;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public String getCacheKey() {
		return CACHE_KEY;
	}

	public synchronized Object clone() {
		TextReader obj = null;
		try {
			obj = (TextReader) super.clone();

			obj.sLocaleId = this.sLocaleId;
			obj.sAttribute = this.sAttribute;

			obj.iTextId = this.iTextId;
			obj.textSize = this.textSize;
			obj.tableTextSize = this.tableTextSize;
			obj.headlineSize = this.headlineSize;
			obj.tableWidth = this.tableWidth;

			obj.textBgColor = this.textBgColor;
			obj.textColor = this.textColor;
			obj.headlineBgColor = this.headlineBgColor;
			obj.headlineColor = this.headlineColor;
			obj.tableAlignment = this.tableAlignment;
			obj.textWidth = this.textWidth;
			obj.textStyle = this.textStyle;
			obj.headlineStyle = this.headlineStyle;
			obj.displayHeadline = this.displayHeadline;
			obj.enableDelete = this.enableDelete;
			obj.viewall = this.viewall;
			obj.newobjinst = this.newobjinst;
			obj.newWithAttribute = this.newWithAttribute;
			obj.spaceBetweenHeadlineAndBody = this.spaceBetweenHeadlineAndBody;

		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { this.headlineStyleName, this.textStyleName };
		String[] styleValues = { "", "" };

		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
	}
	
	public void setHeadlineStyleClass(String styleClass) {
		this.headlineStyleClassName = styleClass;	
	}

	public void setTextStyleClass(String styleClass) {
		this.textStyleClassName = styleClass;	
	}
}
package com.idega.block.email.presentation;

import java.util.Collection;
import java.util.Iterator;
import com.idega.block.category.presentation.CategoryBlock;
import com.idega.block.email.business.EmailTopic;
import com.idega.block.email.business.MailBusiness;
import com.idega.block.email.business.MailFinder;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * 240 Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * 
 * @created 14. mars 2002
 * @version 1.0
 */

public class NewsLetter extends CategoryBlock {

	/**
	 * @todo Description of the Field
	 */
	public final static int DROP = 1;

	/**
	 * @todo Description of the Field
	 */
	public final static int SINGLE = 2;

	/**
	 * @todo Description of the Field
	 */
	public final static int CHECK = 3;

	/**
	 * @todo Description of the Field
	 */
	public static String EMAIL_BUNDLE_IDENTIFIER = "com.idega.block.email";

	private IWBundle iwb, core;

	private IWResourceBundle iwrb;

	private Collection topics;

	private Image submitImage, cancelImage;

	private int viewType = DROP;

	private String _inputStyle = "";

	private String _checkBoxStyle = "";

	private String _checkFontStyle = "";

	private int _inputLength = 0;

	private String _inputWidth = null;

	private boolean _submitBelow = false;

	private boolean _submitBelowTopics = false;

	private String _spaceBetween = "2";
	private int _spaceBeforeButtons = 12;

	private int archivePage = -1;

	private String archiveTarget = Link.TARGET_TOP_WINDOW;

	private boolean _showCancelImage;

	private String _bgColor = "#ffffff";
	
	private boolean useButtons = true;
	private boolean useLinks = false;
	private String arrowStyleClass;
	private String linkStyleClass;
	

	/** Constructor for the NewsLetter object */
	public NewsLetter() {
		setAutoCreate(false);
	}

	/**
	 * Gets the multible of the NewsLetter object
	 * 
	 * @return The multible value
	 */
	public boolean getMultible() {
		return false;
	}

	/**
	 * Gets the categoryType of the NewsLetter object
	 * 
	 * @return The category type value
	 */
	public String getCategoryType() {
		return "Newsletter";
	}

	/**
	 * Gets the bundleIdentifier of the NewsLetter object
	 * 
	 * @return The bundle identifier value
	 */
	public String getBundleIdentifier() {
		return EMAIL_BUNDLE_IDENTIFIER;
	}

	/**
	 * @param iwc
	 *            Description of the Parameter
	 * @todo Description of the Method
	 */
	public void main(IWContext iwc) {
		//debugParameters(iwc);
		iwb = getBundle(iwc);
		core = iwc.getIWMainApplication().getCoreBundle();
		iwrb = getResourceBundle(iwc);
		Table T = new Table();
		T.setCellpaddingAndCellspacing(0);
		T.setColor(_bgColor);
		T.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		int categoryID = getCategoryId();

		if (categoryID > 0) {
			processForm(iwc);
			topics = MailFinder.getInstance().getInstanceTopics(getICObjectInstanceID());
		}

		if (iwc.hasEditPermission(this)) {
			T.add(getAdminView(iwc), 1, row);
			T.setAlignment(1, row++, "left");
		}

		Form F = new Form();
		if (categoryID > 0) {
			if (topics != null && !topics.isEmpty()) {
				T.add(getMailInputTable(F, iwc), 1, row++);
				PresentationObject obj = null;

				switch (viewType) {
					case DROP:
						obj = getDropdownView(iwc);
						break;
					case CHECK:
						obj = getCheckBoxView(iwc);
						break;
					case SINGLE:
						obj = getCheckBoxView(iwc);
						break;
				}

				if (obj != null) {
					T.setHeight(row++, 6);
					T.add(obj, 1, row++);
				}
				if (_submitBelowTopics) {
					if (_spaceBeforeButtons > 0) {
						T.setHeight(row++, _spaceBeforeButtons);
					}
					T.add(getButtonsBelowTable(F, iwc), 1, row);
				}

			}
			else {
				T.add(iwrb.getLocalizedString("no_topic", "Please create a topic"), 1, row);
			}
		}
		else {
			T.add(iwrb.getLocalizedString("no_category", "Please create a category"), 1, row);
		}

		F.add(T);
		add(F);
	}

	/**
	 * Gets the dropdownView of the NewsLetter object
	 * 
	 * @param iwc
	 *            Description of the Parameter
	 * @return The dropdown view value
	 */
	public PresentationObject getDropdownView(IWContext iwc) {
		Table T = new Table();
		T.setColor(_bgColor);

		if (topics != null && topics.size() > 0) {
			DropdownMenu drp = new DropdownMenu("nl_list");
			Iterator iter = topics.iterator();
			if (topics.size() > 1) {
				while (iter.hasNext()) {
					EmailTopic tpc = (EmailTopic) iter.next();
					drp.addMenuElement(tpc.getListId(), tpc.getName());
				}
				T.add(drp, 1, 2);
			}
			else if (iter.hasNext()) {
				EmailTopic tpc = (EmailTopic) iter.next();
				T.add(new HiddenInput("nl_list", String.valueOf(tpc.getListId())));
			}
			if (archivePage > 0) {

				T.add(getArchiveLink(), 1, 2);
			}
			return T;
		}
		else
			return null;
	}

	private PresentationObject getMailInputTable(Form form, IWContext iwc) {
		Table T = new Table();
		T.setCellpaddingAndCellspacing(0);
		T.setColor(_bgColor);
		T.setWidth(Table.HUNDRED_PERCENT);
		TextInput email = new TextInput("nl_email");
		email.setStyleAttribute(_inputStyle);
		if (_inputLength != 0) {
			email.setLength(_inputLength);
		}
		if (_inputWidth != null) {
			email.setWidth(_inputWidth);
		}
		email.setContent(iwrb.getLocalizedString("enter_email_here", "Enter e-mail here"));
		email.setOnFocus("this.value=''");

		Table submitTable = new Table(2, 1);
		submitTable.setCellpaddingAndCellspacing(0);
		submitTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

		if (useButtons) {
			SubmitButton send, cancel;
			if (submitImage != null) {
				send = new SubmitButton(submitImage, "nl_send");
			}
			else {
				send = new SubmitButton(iwrb.getLocalizedImageButton("subscribe", "Subscribe"), "nl_send");
			}
			if (cancelImage != null) {
				cancel = new SubmitButton(cancelImage, "nl_stop");
			}
			else {
				cancel = new SubmitButton(iwrb.getLocalizedImageButton("unsubscribe", "Unsubscribe"), "nl_stop");
			}

			submitTable.add(send, 1, 1);
			if (_showCancelImage) {
				submitTable.add(cancel, 2, 1);
			}
		}
		else if (useLinks) {
			Link sendLink = new Link(iwrb.getLocalizedString("subscribe", "Subscribe"));
			sendLink.addParameter("nl_send", "true");
			sendLink.setToFormSubmit(form);
			if (linkStyleClass != null) {
				sendLink.setStyle(linkStyleClass);
			}
			Link sendArrow = new Link("&gt;&gt;");
			sendArrow.addParameter("nl_send", "true");
			sendArrow.setToFormSubmit(form);
			if (arrowStyleClass != null) {
				sendArrow.setStyle(arrowStyleClass);
			}

			Link cancelLink = new Link(iwrb.getLocalizedString("unsubscribe", "Unsubscribe"));
			cancelLink.addParameter("nl_stop", "true");
			cancelLink.setToFormSubmit(form);
			if (linkStyleClass != null) {
				cancelLink.setStyle(linkStyleClass);
			}
			Link cancelArrow = new Link("&gt;&gt;");
			cancelArrow.addParameter("nl_stop", "true");
			cancelArrow.setToFormSubmit(form);
			if (arrowStyleClass != null) {
				cancelArrow.setStyle(arrowStyleClass);
			}

			submitTable.add(sendLink, 1, 1);
			submitTable.add(Text.NON_BREAKING_SPACE, 1, 1);
			submitTable.add(sendArrow, 1, 1);
			if (_showCancelImage) {
				submitTable.add(cancelLink, 2, 1);
				submitTable.add(Text.NON_BREAKING_SPACE, 2, 1);
				submitTable.add(cancelArrow, 2, 1);
			}
		}

		if (_submitBelow) {
			T.add(email, 1, 1);
			T.setHeight(1, 2, _spaceBetween);
			if (!_submitBelowTopics) {
				T.add(submitTable, 1, 3);
			}

		}
		else {
			T.add(email, 1, 1);
			T.setWidth(2, 1, _spaceBetween);
			if (!_submitBelowTopics) {
				T.add(submitTable, 3, 1);
			}
		}
		return T;
	}

	/**
	 * Gets the checkBoxView of the NewsLetter object
	 * 
	 * @param iwc
	 *            Description of the Parameter
	 * @return The check box view value
	 */
	public PresentationObject getCheckBoxView(IWContext iwc) {
		Table T = new Table();
		T.setCellpaddingAndCellspacing(0);
		T.setColor(_bgColor);
		if (topics != null && topics.size() > 0) {
			CheckBox chk;
			Iterator iter = topics.iterator();
			int row = 1;
			if (topics.size() > 1)
				while (iter.hasNext()) {
					EmailTopic tpc = (EmailTopic) iter.next();
					chk = new CheckBox("nl_list", String.valueOf(tpc.getListId()));
					chk.setStyleAttribute(_checkBoxStyle);
					T.add(chk, 1, row);
					T.setCellpaddingLeft(1, row, 3);
					Text tpcName = new Text(tpc.getName());
					tpcName.setFontStyle(_checkFontStyle);
					T.add(tpcName, 2, row);
					row++;
					if (iter.hasNext()) {
						T.setHeight(row++, _spaceBetween);
					}
				}
			else if (iter.hasNext()) {
				EmailTopic tpc = (EmailTopic) iter.next();
				T.add(new HiddenInput("nl_list", String.valueOf(tpc.getListId())));
			}
			if (archivePage > 0) {
				T.mergeCells(1, row, 2, row);
				T.add(getArchiveLink(), 1, row);
			}
			return T;
		}
		else
			return null;
	}

	/**
	 * Gets the adminView of the NewsLetter object
	 * 
	 * @return The admin view value
	 */
	private PresentationObject getAdminView(IWContext iwc) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellpadding(0);
		if (topics != null && topics.size() > 0) {
			T.add(getAddLink(core.getImage("/shared/create.gif", "Send")), 1, 1);
		}
		if (getCategoryIds().length > 0 && getICObjectInstanceID() > 0) {
			T.add(getSetupLink(core.getImage("/shared/edit.gif", "Edit")), 1, 1);
		}

		T.add(getCategoryLink(core.getImage("/shared/detach.gif")), 1, 1);

		return T;
	}

	private PresentationObject getButtonsBelowTable(Form form, IWContext iwc) {
		Table submitTable = new Table(2, 1);
		submitTable.setCellpaddingAndCellspacing(0);
		submitTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		submitTable.setWidth(Table.HUNDRED_PERCENT);

		if (useButtons) {
			SubmitButton send, cancel;
			if (submitImage != null) {
				send = new SubmitButton(submitImage, "nl_send");
			}
			else {
				send = new SubmitButton(iwrb.getLocalizedImageButton("subscribe", "Subscribe"), "nl_send");
			}
			if (cancelImage != null) {
				cancel = new SubmitButton(cancelImage, "nl_stop");
			}
			else {
				cancel = new SubmitButton(iwrb.getLocalizedImageButton("unsubscribe", "Unsubscribe"), "nl_stop");
			}

			submitTable.add(send, 1, 1);
			if (_showCancelImage) {
				submitTable.add(cancel, 2, 1);
			}
		}
		else if (useLinks) {
			Link sendLink = new Link(iwrb.getLocalizedString("subscribe", "Subscribe"));
			sendLink.addParameter("nl_send", "true");
			sendLink.setToFormSubmit(form);
			
			//added 20.01.2005 - ac
			sendLink.setToolTip("Please fill in your email, mark the desired newsletter and choose subscribe or unsubscribe.");
			
			if (linkStyleClass != null) {
				sendLink.setStyle(linkStyleClass);
			}
			Link sendArrow = new Link("&gt;&gt;");
			sendArrow.addParameter("nl_send", "true");
			sendArrow.setToFormSubmit(form);
			if (arrowStyleClass != null) {
				sendArrow.setStyle(arrowStyleClass);
			}

			Link cancelLink = new Link(iwrb.getLocalizedString("unsubscribe", "Unsubscribe"));
			cancelLink.addParameter("nl_stop", "true");
			cancelLink.setToFormSubmit(form);
			if (linkStyleClass != null) {
				cancelLink.setStyle(linkStyleClass);
			}
			Link cancelArrow = new Link("&gt;&gt;");
			cancelArrow.addParameter("nl_stop", "true");
			cancelArrow.setToFormSubmit(form);
			if (arrowStyleClass != null) {
				cancelArrow.setStyle(arrowStyleClass);
			}

			submitTable.add(sendLink, 1, 1);
			submitTable.add(Text.NON_BREAKING_SPACE, 1, 1);
			submitTable.add(sendArrow, 1, 1);
			if (_showCancelImage) {
				submitTable.add(cancelLink, 2, 1);
				submitTable.add(Text.NON_BREAKING_SPACE, 2, 1);
				submitTable.add(cancelArrow, 2, 1);
			}
		}

		return submitTable;
	}

	private Link getArchiveLink() {
		Link L = new Link(iwrb.getLocalizedString("archive", "Archive"));
		L.setPage(archivePage);
		L.setTarget(archiveTarget);

		return L;
	}

	private void processForm(IWContext iwc) {
		if (iwc.isParameterSet("nl_email")) {
			String email = iwc.getParameter("nl_email");
			if (email.indexOf("@") > 0) {
				String[] sids = iwc.getParameterValues("nl_list");
				int[] ids = new int[sids.length];
				for (int i = 0; i < sids.length; i++) {
					ids[i] = Integer.parseInt(sids[i]);
				}
				if (iwc.isParameterSet("nl_send") || iwc.isParameterSet("nl_send.x")) {
					MailBusiness.getInstance().saveEmailToLists(email, ids);
				}
				else if (iwc.isParameterSet("nl_stop") || iwc.isParameterSet("nl_stop.x")) {
					MailBusiness.getInstance().removeEmailFromLists(email, ids);
				}
			}
		}
	}

	/**
	 * Gets the addLink of the NewsLetter object
	 * 
	 * @param image
	 *            Description of the Parameter
	 * @return The add link value
	 */
	private Link getAddLink(Image image) {
		Link L = new Link(image);
		L.setWindowToOpen(LetterWindow.class);
		L.addParameter(LetterWindow.prmInstanceId, getICObjectInstanceID());
		return L;
	}

	/**
	 * Gets the setupLink of the NewsLetter object
	 * 
	 * @param image
	 *            Description of the Parameter
	 * @return The setup link value
	 */
	private Link getSetupLink(Image image) {
		Link L = new Link(image);
		L.setWindowToOpen(SetupWindow.class);
		L.addParameter(SetupEditor.prmInstanceId, getICObjectInstanceID());
		return L;
	}

	/**
	 * Gets the categoryLink of the NewsLetter object
	 * 
	 * @param image
	 *            Description of the Parameter
	 * @return The category link value
	 */
	private Link getCategoryLink(Image image) {
		Link L = getCategoryLink();
		L.setImage(image);
		return L;
	}

	/**
	 * Sets the viewType attribute of the NewsLetter object
	 * 
	 * @param viewType
	 *            The new viewType value
	 */
	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	/**
	 * Sets the input style attribute of the NewsLetter object
	 * 
	 * @param inputStyle -
	 *            the new value for _inputStyle
	 */
	public void setInputStyle(String inputStyle) {
		_inputStyle = inputStyle;
	}

	/**
	 * Sets the checkBox style attribute
	 * 
	 * @param checkBoxStyle
	 */
	public void setCheckBoxStyle(String checkBoxStyle) {
		_checkBoxStyle = checkBoxStyle;
	}

	/**
	 * Sets the input length attribute of the NewsLetter object
	 * 
	 * @param inputLength -
	 *            the new value for _inputStyle
	 */
	public void setInputLength(int inputLength) {
		_inputLength = inputLength;
	}

	/**
	 * Sets the submitImage attribute of the NewsLetter object
	 * 
	 * @param submitImage
	 *            The new submitImage value
	 */
	public void setSubmitImage(Image submitImage) {
		this.submitImage = submitImage;
	}

	/**
	 * Sets the cancelImage attribute of the NewsLetter object
	 * 
	 * @param cancelImage
	 *            The new cancelImage value
	 */
	public void setCancelImage(Image cancelImage) {
		this.cancelImage = cancelImage;
	}

	/**
	 * Sets the submit button below the input
	 * 
	 * @param _submitBelow
	 *            The new _submitBelow value
	 */
	public void setSubmitBelowInput(boolean submitBelow) {
		_submitBelow = submitBelow;
	}

	/**
	 * Sets the space between the submit button and the input
	 * 
	 * @param _spaceBetween
	 *            The new _spaceBetween value
	 */
	public void setSpaceBetween(String spaceBetween) {
		_spaceBetween = spaceBetween;
	}

	public void setArchivePage(ICPage page) {
		this.archivePage = page.getID();
	}

	public void setArchiveTarget(String target) {
		this.archiveTarget = target;
	}

	/**
	 * Sets the _showCancelImage.
	 * 
	 * @param showUnsubscribe
	 *            The _showCancelImage to set
	 */
	public void setShowUnsubscribeButton(boolean showUnsubscribe) {
		this._showCancelImage = showUnsubscribe;
	}

	/**
	 * Sets the background color of the main table displaying the NewsLetter
	 * subscribe
	 * 
	 * @param color
	 */
	public void setBgColor(String color) {
		_bgColor = color;
	}

	/**
	 * Sets the submit (and cancel) buttons below the topics dropdown or
	 * checkboxes
	 * 
	 * @param submitBelowTopics
	 */
	public void setSubmitBelowTopics(boolean submitBelowTopics) {
		_submitBelowTopics = submitBelowTopics;
	}

	/**
	 * Sets the font style of the text displayd with each checkbox
	 * 
	 * @param style
	 */
	public void setCheckBoxFont(String style) {
		_checkFontStyle = style;
	}

	/**
	 * Sets the width of the input box
	 * 
	 * @param width
	 */
	public void setInputWidth(String width) {
		_inputWidth = width;
	}

	/**
	 * @param arrowStyleClass The arrowStyleClass to set.
	 */
	public void setArrowStyleClass(String arrowStyleClass) {
		this.arrowStyleClass = arrowStyleClass;
	}
	/**
	 * @param linkStyleClass The linkStyleClass to set.
	 */
	public void setLinkStyleClass(String linkStyleClass) {
		this.linkStyleClass = linkStyleClass;
	}
	/**
	 * @param useButtons The useButtons to set.
	 */
	public void setUseButtons(boolean useButtons) {
		this.useButtons = useButtons;
		this.useLinks = !useButtons;
	}
	/**
	 * @param useLinks The useLinks to set.
	 */
	public void setUseLinks(boolean useLinks) {
		this.useLinks = useLinks;
		this.useButtons = !useLinks;
	}
	/**
	 * @param space The spacing to set.
	 */
	public void setSaceBeforeButtons(int spacing) {
		_spaceBeforeButtons = spacing;
	}
}
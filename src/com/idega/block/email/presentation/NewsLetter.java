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

/**240
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    14. mars 2002
 * @version    1.0
 */

public class NewsLetter extends CategoryBlock {

  /**
   * @todo    Description of the Field
   */
  public final static int DROP = 1;
  /**
   * @todo    Description of the Field
   */
  public final static int SINGLE = 2;
  /**
   * @todo    Description of the Field
   */
  public final static int CHECK = 3;

  /**
   * @todo    Description of the Field
   */
  public static String EMAIL_BUNDLE_IDENTIFIER = "com.idega.block.email";

  private IWBundle iwb, core;
  private IWResourceBundle iwrb;
  private Collection topics;
  private Image submitImage,cancelImage;

  private int viewType = DROP;
  private String _inputStyle = "";
  private String _checkFontStyle = "";
  private int _inputLength = 0;
  private String _inputWidth = null;
  private boolean _submitBelow = false;
  private boolean _submitBelowTopics = false;
  private String _spaceBetween = "2";
  private int archivePage = -1;
  private String archiveTarget = Link.TARGET_TOP_WINDOW;
	private boolean _showCancelImage;
	private String _bgColor = "#ffffff";

  /**  Constructor for the NewsLetter object */
  public NewsLetter() {
    setAutoCreate(false);
  }

  /**
   *  Gets the multible of the NewsLetter object
   *
   * @return    The multible value
   */
  public boolean getMultible() {
    return false;
  }

  /**
   *  Gets the categoryType of the NewsLetter object
   *
   * @return    The category type value
   */
  public String getCategoryType() {
    return "Newsletter";
  }

  /**
   *  Gets the bundleIdentifier of the NewsLetter object
   *
   * @return    The bundle identifier value
   */
  public String getBundleIdentifier() {
    return EMAIL_BUNDLE_IDENTIFIER;
  }

  /**
   * @param  iwc  Description of the Parameter
   * @todo        Description of the Method
   */
  public void main(IWContext iwc) {
    //debugParameters(iwc);
    iwb = getBundle(iwc);
    core = iwc.getIWMainApplication().getCoreBundle();
    iwrb = getResourceBundle(iwc);
    Table T = new Table();
      T.setCellpaddingAndCellspacing(0);
      T.setColor(_bgColor);
    int row = 1;
    int categoryID = getCategoryId();

    if (categoryID > 0) {
      processForm(iwc);
      topics = MailFinder.getInstance().getInstanceTopics(getICObjectInstanceID());
    }

    if(iwc.hasEditPermission(this)){
      T.add(getAdminView(iwc), 1, row);
      T.setAlignment(1, row++, "left");
    }

    if (categoryID > 0 ){
      if(topics!=null && !topics.isEmpty()) {
        T.add(getMailInputTable(iwc),1,row++);
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

        if ( obj != null )
          T.add(obj,1,row);
        if(_submitBelowTopics)
        		T.add(getButtonsBelowTable(iwc),1,row);
        
        }
        else{
          T.add(iwrb.getLocalizedString("no_topic","Please create a topic"),1,row);
        }
    }
    else{
          T.add(iwrb.getLocalizedString("no_category","Please create a category"),1,row);
    }

    Form F = new Form();
    F.add(T);
    add(F);
  }

  /**
   *  Gets the dropdownView of the NewsLetter object
   *
   * @param  iwc  Description of the Parameter
   * @return      The dropdown view value
   */
  public PresentationObject getDropdownView(IWContext iwc) {
    Table T = new Table();
    T.setColor(_bgColor);

    if(topics != null && topics.size() > 0){
      DropdownMenu drp = new DropdownMenu("nl_list");
      Iterator iter = topics.iterator();
      if(topics.size() > 1){
				while (iter.hasNext()) {
					EmailTopic tpc = (EmailTopic) iter.next();
					drp.addMenuElement(tpc.getListId(), tpc.getName());
				}
				T.add(drp, 1, 2);
      }
      else if(iter.hasNext()){
				EmailTopic tpc = (EmailTopic) iter.next();
				T.add(new HiddenInput("nl_list",String.valueOf( tpc.getListId())));
      }
      if(archivePage>0){
      	
      	T.add(getArchiveLink(),1,2);
      }
      return T;
    }
    else
      return null;
  }

  private PresentationObject getMailInputTable(IWContext iwc){
    Table T = new Table();
      T.setCellpaddingAndCellspacing(0);
      T.setColor(_bgColor);
      TextInput email = new TextInput("nl_email");
      email.setStyleAttribute(_inputStyle);
      if(_inputLength != 0) {
      		email.setLength(_inputLength);
      }
      if(_inputWidth != null) {
      		email.setWidth(_inputWidth);
      }
      email.setContent(iwrb.getLocalizedString("enter_email_here","Enter e-mail here"));
      email.setOnFocus("this.value=''");
			SubmitButton send,cancel;
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
			
			 
				if ( _submitBelow ) {
					T.add(email, 1, 1);
					T.setHeight(1, 2, _spaceBetween);
					if(!_submitBelowTopics) {
						T.add(send, 1, 3);
						if (_showCancelImage)
							T.add(cancel, 1, 3);
					}
					
				}
				else {
					T.add(email, 1, 1);
					T.setWidth(2, 1, _spaceBetween);
					if(!_submitBelowTopics) {
						T.add(send, 3, 1);
						if (_showCancelImage)
							T.add(cancel, 3, 1);
					}
				}
			return T;
	}

  /**
   *  Gets the checkBoxView of the NewsLetter object
   *
   * @param  iwc  Description of the Parameter
   * @return      The check box view value
   */
  public PresentationObject getCheckBoxView(IWContext iwc) {
    Table T = new Table();
    T.setColor(_bgColor);
    if(topics != null && topics.size() > 0){
      CheckBox chk;
      Iterator iter = topics.iterator();
      int row = 1;
      if(topics.size() > 1)
	      while (iter.hasNext()) {
					EmailTopic tpc = (EmailTopic) iter.next();
					chk = new CheckBox("nl_list",String.valueOf(tpc.getListId()));
					T.add(chk, 1, row);
					Text tpcName = new Text(tpc.getName());
					tpcName.setFontStyle(_checkFontStyle);
					T.add(tpcName,2,row);
					row++;
	      }
      else if(iter.hasNext()){
				EmailTopic tpc = (EmailTopic) iter.next();
				T.add(new HiddenInput("nl_list",String.valueOf( tpc.getListId())));
      }
      if(archivePage>0){
      	T.mergeCells(1,row,2,row);
      	T.add(getArchiveLink(),1,row);
      }
      return T;
    }
    else
      return null;
  }

  /**
   *  Gets the adminView of the NewsLetter object
   *
   * @return    The admin view value
   */
  private PresentationObject getAdminView(IWContext iwc) {
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellpadding(0);
    if (topics != null && topics.size() > 0) {
      T.add(getAddLink(core.getImage("/shared/create.gif", "Send")), 1, 1);
    }
    if (getCategoryIds().length > 0 && getICObjectInstanceID()>0) {
      T.add(getSetupLink(core.getImage("/shared/edit.gif", "Edit")), 1, 1);
    }

      T.add(getCategoryLink(core.getImage("/shared/detach.gif")), 1, 1);

    return T;
  }
  private PresentationObject getButtonsBelowTable(IWContext iwc) {
  		Table T = new Table();
  		T.setCellpadding(0);
  		T.setCellspacing(0);
  		SubmitButton send,cancel;
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
  		
		T.add(send,1,1);
		T.setWidth(2,1,_spaceBetween);
		if (_showCancelImage)
			T.setAlignment(3,1,Table.HORIZONTAL_ALIGN_RIGHT);
			T.add(cancel, 3, 1);
  		
		return T;
  	
  }
  
  private Link getArchiveLink(){
  	Link L = new Link(iwrb.getLocalizedString("archive","Archive"));
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
			else if(iwc.isParameterSet("nl_stop")||iwc.isParameterSet("nl_stop.x")){
				MailBusiness.getInstance().removeEmailFromLists(email,ids);
			}		
      	}
	}
  }

  /**
   *  Gets the addLink of the NewsLetter object
   *
   * @param  image  Description of the Parameter
   * @return        The add link value
   */
  private Link getAddLink(Image image) {
    Link L = new Link(image);
    L.setWindowToOpen(LetterWindow.class);
    L.addParameter(LetterWindow.prmInstanceId,getICObjectInstanceID());
    return L;
  }

  /**
   *  Gets the setupLink of the NewsLetter object
   *
   * @param  image  Description of the Parameter
   * @return        The setup link value
   */
  private Link getSetupLink(Image image) {
    Link L = new Link(image);
    L.setWindowToOpen(SetupWindow.class);
    L.addParameter(SetupEditor.prmInstanceId, getICObjectInstanceID());
    return L;
  }

  /**
   *  Gets the categoryLink of the NewsLetter object
   *
   * @param  image  Description of the Parameter
   * @return        The category link value
   */
  private Link getCategoryLink(Image image) {
    Link L = getCategoryLink();
    L.setImage(image);
    return L;
  }

  /**
   *  Sets the viewType attribute of the NewsLetter object
   *
   * @param  viewType  The new viewType value
   */
  public void setViewType(int viewType) {
    this.viewType = viewType;
  }

  /**
   *  Sets the input style attribute of the NewsLetter object
  *
  * @param inputStyle - the new value for _inputStyle
  */
  public void setInputStyle(String inputStyle){
    _inputStyle = inputStyle;
  }

  /**
   *  Sets the input length attribute of the NewsLetter object
  *
  * @param inputLength - the new value for _inputStyle
  */
  public void setInputLength(int inputLength){
    _inputLength = inputLength;
  }

  /**
   *  Sets the submitImage attribute of the NewsLetter object
   *
   * @param  submitImage  The new submitImage value
   */
  public void setSubmitImage(Image submitImage) {
    this.submitImage = submitImage;
  }
  
  /**
   *  Sets the cancelImage attribute of the NewsLetter object
   *
   * @param  cancelImage  The new cancelImage value
   */
  public void setCancelImage(Image cancelImage){
  	this.cancelImage = cancelImage;
  }

  /**
   *  Sets the submit button below the input
   *
   * @param  _submitBelow  The new _submitBelow value
   */
  public void setSubmitBelowInput(boolean submitBelow) {
    _submitBelow = submitBelow;
  }

  /**
   *  Sets the space between the submit button and the input
   *
   * @param  _spaceBetween  The new _spaceBetween value
   */
  public void setSpaceBetween(String spaceBetween) {
    _spaceBetween = spaceBetween;
  }
  
  public void setArchivePage(ICPage page){
  		this.archivePage = page.getID();
  }
  
  public void setArchiveTarget(String target){
  	this.archiveTarget = target;
  }
	/**
	 * Sets the _showCancelImage.
	 * @param showUnsubscribe The _showCancelImage to set
	 */
	public void setShowUnsubscribeButton(boolean showUnsubscribe) {
		this._showCancelImage = showUnsubscribe;
	}
	/**
	 * Sets the background color of the main table displaying the NewsLetter subscribe
	 * @param color
	 */
	public void setBgColor(String color) {
	  _bgColor = color;
	}
	/**
	 * Sets the submit (and cancel) buttons below the topics dropdown or checkboxes
	 * @param submitBelowTopics
	 */
	public void setSubmitBelowTopics(boolean submitBelowTopics) {
		_submitBelowTopics = submitBelowTopics;
	}
	/**
	 * Sets the font style of the text displayd with each checkbox
	 * @param style
	 */
	public void setCheckBoxFont(String style) {
		_checkFontStyle = style;
	}
	/**
	 * Sets the width of the input box 
	 * @param width
	 */
	public void setInputWidth(String width) {
		_inputWidth = width;
	}

}
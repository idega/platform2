package is.idega.idegaweb.campus.presentation;




/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class EmailSetter extends CampusBlock{

 /*
  private String propParameter = is.idega.idegaweb.campus.data.SystemPropertiesBMPBean.getEntityTableName();
  private String localesParameter="iw_locales";
  private boolean isAdmin = false;

  public String getLocalizedNameKey(){
    return "emails";
  }

  public String getLocalizedNameValue(){
    return "Emails";
  }

 

  protected void control(IWContext iwc){
   
  

    if(isAdmin){
      if(iwc.getParameter("save")!=null){
        updateForm(iwc);
      }
      else if(iwc.getParameter("bundlesave")!=null){
        iwb.storeState();
      }
      else if(iwc.getParameter("bundlereload")!=null){
        iwb.reloadBundle();
        iwrb = iwb.getResourceBundle(LocaleUtil.getLocale(iwc.getParameter(localesParameter)));
      }

      add(getSetupForm(iwc));
    }
    else
      add("Hefur ekki réttindi");
  }

  private PresentationObject getSetupForm(IWContext iwc){
    Table T = new Table(1,10);
    T.setBorder(1);
    int row = 1;
    DropdownMenu localeDrop = LocalePresentationUtil.getAvailableLocalesDropdown(iwc.getIWMainApplication(),localesParameter);
    localeDrop.keepStatusOnAction();
    localeDrop.setToSubmit();
    SubmitButton save = new SubmitButton("save","Save");
    SubmitButton saveBundle = new SubmitButton("bundlesave","Bundlesave");
    SubmitButton reloadBundle = new SubmitButton("bundlereload","Bundlereload");
    T.add(localeDrop,1,row);
    T.add(save,1,row);
    T.add(saveBundle,1,row);
    T.add(reloadBundle,1,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("title_applied","Applied")),1,row++);
    T.add(getTextArea("ta1",iwrb.getLocalizedString("letter_applied")),1,row++);
    T.add(Edit.formatText(iwrb.getLocalizedString("title_invalid","Applied")),1,row++);
    T.add(getTextArea("ta2",iwrb.getLocalizedString("letter_invalid")),1,row++);
    T.add(Edit.formatText(iwrb.getLocalizedString("title_approved","Applied")),1,row++);
    T.add(getTextArea("ta3",iwrb.getLocalizedString("letter_approved")),1,row++);
    T.add(Edit.formatText(iwrb.getLocalizedString("title_allocated","Applied")),1,row++);
    T.add(getTextArea("ta4",iwrb.getLocalizedString("letter_allocated")),1,row++);

    Form myForm = new Form();
    myForm.add(T);
    return myForm;
  }

  private void updateForm(IWContext iwc){
    String Tx1 = iwc.getParameter("ta1");
    if(Tx1!=null){
      iwrb.setString("letter_applied",Tx1);
    }
    String Tx2 = iwc.getParameter("ta2");
    if(Tx2!=null){
      iwrb.setString("letter_invalid",Tx2);
    }
    String Tx3 = iwc.getParameter("ta3");
    if(Tx3!=null){
      iwrb.setString("letter_approved",Tx3);
    }
    String Tx4 = iwc.getParameter("ta4");
    if(Tx4!=null){
      iwrb.setString("letter_allocated",Tx4);
    }
  }


  private TextArea getTextArea(String name,String content){
    TextArea TA = new TextArea(name,content);
    TA.setStyleClass(Edit.styleAttribute);
    TA.setWidth(80);
    TA.setHeight(8);
    return TA;
  }

  public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
*/
}

package is.idegaweb.campus.allocation;

import is.idegaweb.campus.entity.SystemProperties;
import com.idega.idegaweb.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.util.idegaTimestamp;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.development.presentation.Localizer;
import com.idega.util.LocaleUtil;
import java.util.List;
import java.sql.SQLException;
import com.idega.data.EntityFinder;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class EmailSetter extends KeyEditor{

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.emails";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private String propParameter = SystemProperties.getEntityTableName();
  private String localesParameter="iw_locales";

  public EmailSetter(String sHeader) {
    super(sHeader);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  protected void control(ModuleInfo modinfo){
    iwb = getBundle(modinfo);
    if(modinfo.getParameter(localesParameter)!=null){
      iwrb = iwb.getResourceBundle(LocaleUtil.getLocale(modinfo.getParameter(localesParameter)));
    }
    else
      iwrb = getResourceBundle(modinfo);

    if(isAdmin){
      if(modinfo.getParameter("save")!=null){
        updateForm(modinfo);
      }
      else if(modinfo.getParameter("bundlesave")!=null){
        iwb.storeState();
      }
      else if(modinfo.getParameter("bundlereload")!=null){
        iwb.reloadBundle();
        iwrb = iwb.getResourceBundle(LocaleUtil.getLocale(modinfo.getParameter(localesParameter)));
      }
      add(getHomeLink());
      add(getSetupForm(modinfo));
    }
    else
      add("Hefur ekki réttindi");
  }

  private ModuleObject getSetupForm(ModuleInfo modinfo){
    Table T = new Table(1,10);
    T.setBorder(1);
    int row = 1;
    DropdownMenu localeDrop = Localizer.getAvailableLocalesDropdown(modinfo.getApplication(),localesParameter);
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
    T.add(formatText(iwrb.getLocalizedString("title_applied","Applied")),1,row++);
    T.add(getTextArea("ta1",iwrb.getLocalizedString("letter_applied")),1,row++);
    T.add(formatText(iwrb.getLocalizedString("title_invalid","Applied")),1,row++);
    T.add(getTextArea("ta2",iwrb.getLocalizedString("letter_invalid")),1,row++);
    T.add(formatText(iwrb.getLocalizedString("title_approved","Applied")),1,row++);
    T.add(getTextArea("ta3",iwrb.getLocalizedString("letter_approved")),1,row++);
    T.add(formatText(iwrb.getLocalizedString("title_allocated","Applied")),1,row++);
    T.add(getTextArea("ta4",iwrb.getLocalizedString("letter_allocated")),1,row++);

    Form myForm = new Form();
    myForm.add(T);
    return myForm;
  }

  private void updateForm(ModuleInfo modinfo){
    String Tx1 = modinfo.getParameter("ta1");
    if(Tx1!=null){
      iwrb.setString("letter_applied",Tx1);
    }
    String Tx2 = modinfo.getParameter("ta2");
    if(Tx2!=null){
      iwrb.setString("letter_invalid",Tx2);
    }
    String Tx3 = modinfo.getParameter("ta3");
    if(Tx3!=null){
      iwrb.setString("letter_approved",Tx3);
    }
    String Tx4 = modinfo.getParameter("ta4");
    if(Tx4!=null){
      iwrb.setString("letter_allocated",Tx4);
    }
  }

  private Link getHomeLink(){
    return new Link(new Image("/pics/list.gif"),"/allocation/index.jsp");
  }

  private TextArea getTextArea(String name,String content){
    TextArea TA = new TextArea(name,content);
    TA.setStyle(this.styleAttribute);
    TA.setWidth(80);
    TA.setHeight(8);
    return TA;
  }

}

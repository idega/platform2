/*
 * $Id: RoughOrderForm.java,v 1.6 2001/10/17 12:54:25 gummi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation;

import com.idega.block.application.business.ApplicationFinder;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.text.Text;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.core.accesscontrol.business.AccessControl;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RoughOrderForm extends PresentationObjectContainer {
  private final int statusEnteringPage_ = 0;
  private final int statusSubject_ = 1;
  private boolean isAdmin_ = false;
  private static final String IW_RESOURCE_BUNDLE = "is.idegaweb.campus";

  protected IWResourceBundle iwrb_;

  /**
   *
   */
  public RoughOrderForm() {
  }

  /*
   *
   */
  protected void control(IWContext iwc) {
    if (isAdmin_) {
      String statusString = iwc.getParameter("status");
      int status = 0;

      if (statusString == null){
        status = statusEnteringPage_;
      }
      else {
        status = Integer.parseInt(statusString);
      }

      if (status == statusEnteringPage_) {
        doSelectSubject(iwc);
      }
      else if (status == statusSubject_)
        doRoughOrdering(iwc);
    }
    else
      add(new Text("Ekki Réttindi"));
  }

  /*
   *
   */
  protected void doSelectSubject(IWContext iwc) {
    List subjects = ApplicationFinder.listOfSubject();
    Text textTemplate = new Text();

    Form form = new Form();
    Table t = new Table(2,3);
    t.setWidth(1,"50%");
    t.setWidth(2,"50%");

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb_.getLocalizedString("subjectPeriod","Veldu tímabil sem grófraða á fyrir"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(iwrb_.getLocalizedString("period","Tímabil"));
    text1.setBold();

    DropdownMenu subject = new DropdownMenu(subjects,"subject");
    subject.setStyle("formstyle");
    SubmitButton ok = new SubmitButton("ok",iwrb_.getLocalizedString("ok","grófraða"));
    ok.setStyle("idega");

    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);

    t.add(text1,1,1);
    t.add(subject,2,1);
    t.add(ok,2,3);
    form.add(new HiddenInput("status",Integer.toString(statusSubject_)));
    add(form);
  }

  /*
   *
   */
  protected void doRoughOrdering(IWContext iwc) {
    String subject = (String)iwc.getParameter("subject");
    int subject_id = Integer.parseInt(subject);

    RoughOrderer rough = new RoughOrderer();
    rough.createWaitingList(subject_id);
    Text heading = new Text();
    heading.setStyle("headlinetext");
    heading.setText(iwrb_.getLocalizedString("orderingDone","Grófröðun lokið"));

    add(heading);
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return(IW_RESOURCE_BUNDLE);
  }

  /**
   *
   */
  public void main(IWContext iwc){
    isAdmin_ = iwc.hasEditPermission(this);

    iwrb_ = getResourceBundle(iwc);
    //iwb = getBundle(iwc);
    control(iwc);
  }
}

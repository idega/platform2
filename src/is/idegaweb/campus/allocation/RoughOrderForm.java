/*
 * $Id: RoughOrderForm.java,v 1.1 2001/07/13 09:33:41 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation;

import com.idega.block.application.business.ApplicationFinder;
import com.idega.jmodule.object.Editor;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.Table;
import java.util.List;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RoughOrderForm extends Editor {
  private final int statusEnteringPage_ = 0;
  private final int statusSubject_ = 1;

  public RoughOrderForm() {
  }

  protected void control(ModuleInfo modinfo) {
    String statusString = modinfo.getParameter("status");
    int status = 0;

    if (statusString == null){
      status = statusEnteringPage_;
    }
    else {
      status = Integer.parseInt(statusString);
    }

    if (status == statusEnteringPage_) {
      doSelectSubject(modinfo);
    }
    else if (status == statusSubject_)
      doRoughOrdering(modinfo);
  }

  protected void doSelectSubject(ModuleInfo modinfo) {
    IWResourceBundle iwrb = getResourceBundle(modinfo);
    List subjects = ApplicationFinder.listOfSubject();
    Text textTemplate = new Text();

    Form form = new Form();
    Table t = new Table(2,3);
    t.setWidth(1,"50%");
    t.setWidth(2,"50%");

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb.getLocalizedString("subjectPeriod","Veldu tímabil sem grófraða á fyrir"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(iwrb.getLocalizedString("period","Tímabil"));
    text1.setBold();

    DropdownMenu subject = new DropdownMenu(subjects,"subject");
    subject.setStyle("formstyle");
    SubmitButton ok = new SubmitButton("ok",iwrb.getLocalizedString("ok","grófraða"));
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

  protected void doRoughOrdering(ModuleInfo modinfo) {
    IWResourceBundle iwrb = getResourceBundle(modinfo);
    String subject = (String)modinfo.getParameter("subject");
    int subject_id = Integer.parseInt(subject);

    RoughOrderer rough = new RoughOrderer();
    rough.createWaitingList(subject_id);
    Text heading = new Text();
    heading.setStyle("headlinetext");
    heading.setText(iwrb.getLocalizedString("orderingDone","Grófröðun lokið"));

    add(heading);
  }
}

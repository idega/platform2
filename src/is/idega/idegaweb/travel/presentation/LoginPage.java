package is.idega.travel.presentation;

import com.idega.presentation.IWContext;
import com.idega.block.login.presentation.Login;
import com.idega.presentation.text.Text;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.login.business.LoginBusiness;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class LoginPage extends TravelManager {

  private IWBundle bundle = null;
  private IWResourceBundle iwrb = null;

  private static String GRAY = "#CCCCCC";

  public LoginPage() {
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    bundle = super.getBundle();
    iwrb = super.getResourceBundle();

    insertLogin(iwc);
  }

  private void insertLogin(IWContext iwc) {
    add(Text.NON_BREAKING_SPACE);
    add(getLoginTable(iwc, bundle, iwrb));
  }

  protected static Login getLoginObject(IWContext iwc, IWResourceBundle iwrb) {
    Login login = new Login();
      login.setColor(GRAY);
      login.setUserTextColor(TravelManager.BLACK);
      login.setPasswordTextColor(TravelManager.BLACK);
      if (iwrb != null) {
        login.setPasswordText(iwrb.getLocalizedString("travel.password","Password "));
        login.setUserText(iwrb.getLocalizedString("travel.username","Username "));
      }
    return login;
  }

  protected static Table getLoginTable(IWContext iwc, IWBundle bundle, IWResourceBundle iwrb) {
    Table bigTable = new Table(1,1);
        bigTable.setCellspacing(40);
        bigTable.setCellpadding(0);
      Table table = new Table(1,1);
        bigTable.add(table,1,1);
        table.setHeight(210);
        table.setWidth(769);
        table.setColor(TravelManager.WHITE);
        table.setCellspacing(15);
        table.setCellpadding(0);

      Table innerTable = new Table(3,1);
        table.add(innerTable,1,1);
        innerTable.setWidth("100%");
        innerTable.setHeight("100%");
        innerTable.setCellpadding(1);
        innerTable.setCellspacing(1);

        innerTable.setColor(2,1,GRAY);

        Image logo = bundle.getImage("buttons/login_mynd.jpg");
          logo.setWidth(337);
          logo.setHeight(180);
        innerTable.add(logo,1,1);
        innerTable.setWidth(1,1,"337");
//        innerTable.setHeight(1,1,"180");

        Text middleHeader = new Text(iwrb.getLocalizedString("travel.welcome","Welcome"));
          middleHeader.setBold();
          middleHeader.setFontSize(Text.FONT_SIZE_10_HTML_2);
        Text middleContent = new Text(iwrb.getLocalizedString("travel.please_enter_your_username_and_password","Please enter your username and password"));
          middleContent.setFontSize(Text.FONT_SIZE_7_HTML_1);

        Table middleTextTable = new Table(1,2);
          middleTextTable.setWidth("80%");
          middleTextTable.add(middleHeader,1,1);
          middleTextTable.add(middleContent,1,2);

        if (!LoginBusiness.isLoggedOn(iwc))
        innerTable.add(middleTextTable,2,1);
        innerTable.add(getLoginObject(iwc, iwrb),2,1);
        innerTable.setWidth(2,1,"177");
        innerTable.setHeight(2,1,"180");
        innerTable.setAlignment(2,1,"center");


        Text rightHeader = new Text(iwrb.getLocalizedString("travel.usage_rules","Usage rules"));
          rightHeader.setBold();
          rightHeader.setFontSize(Text.FONT_SIZE_10_HTML_2);
        Text rightContent = new Text("Kerfi þetta er einungi heimilað aðilum innan ferðaþjónustu.");
          rightContent.addBreak();
          rightContent.addBreak();
          rightContent.addToText("Misnotkun varðar lög blablabla");
          rightContent.addBreak();
          rightContent.addBreak();
          rightContent.addToText("hérna vantar eitthvað djúsi stuff til að skrifa og hafa töff. Kannski ég fái einhvern annan í það :)");
          rightContent.addBreak();
          rightContent.addBreak();
          rightContent.addToText("Hafir þú gleymt notandanafni og/eða lykilorði hafðu þá samband við fsdfjli");

          rightContent.setFontSize(Text.FONT_SIZE_7_HTML_1);

        Table rightTextTable = new Table(1,2);
          rightTextTable.setWidth("100%");
          rightTextTable.setHeight("100%");
          rightTextTable.add(rightHeader,1,1);
          rightTextTable.add(rightContent,1,2);
          rightTextTable.setColor(TravelManager.WHITE);

        innerTable.add(rightTextTable,3,1);
        innerTable.setColor(3,1,GRAY);


    return bigTable;
  }

}
package com.idega.projects.golf.templates.page;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.interfaceobject.TextArea;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.CheckBox;
import com.idega.jmodule.object.interfaceobject.RadioButton;
import com.idega.jmodule.object.interfaceobject.Parameter;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.jmodule.object.Script;
import com.idega.projects.golf.business.GolferFriendsDataBusiness;
import java.sql.Date;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferFriendsSigningSheet extends JModuleObject {

  public final String nameInputName = "nameInputName";
  public final String sSNumberInputName = "sSNumberInputName";
  public final String anotherAmountInputName = "anotherAmountInputName";
  public final String anotherDurationInputName = "anotherDurationInputName";
  public final String accountNumberInputName = "accountNumberInputName";
  public final String sSNInputName = "sSNInputName";
  public final String cretidCardTypeInputName = "cretidCardTypeInputName";
  public final String creditCardNumberInputName = "creditCardNumberInputName";
  public final String creditCardExpDateInputName = "creditCardExpDateInputName";

  public final String amountRadioButtonName = "amountRadioButtonName";
  public final String durationRadioButtonName = "durationRadioButtonName";

  public final String amountButtonValue1 = "amountButtonValue1";
  public final String amountButtonValue2 = "amountButtonValue2";
  public final String amountButtonValue3 = "amountButtonValue3";
  public final String amountButtonValue4 = "amountButtonValue4";
  public final String amountButtonValue5 = "amountButtonValue5";
  public final String durationButtonValue1 = "durationButtonValue1";
  public final String durationButtonValue2 = "durationButtonValue2";
  public final String durationButtonValue3 = "durationButtonValue3";
  public final String durationButtonValue4 = "durationButtonValue4";
  public final String durationButtonValue5 = "durationButtonValue5";
  public final String giroPaymentCheckBoxName = "giroPaymentCheckBoxName";
  public final String yesOrNoButtonName = "yesOrNoButtonName";
  public final String noValue = "noValue";
  public final String yesValue = "yesValue";
  public String submitButtonName;
  public String submitButtonValue;
  public String controlParameterValue;
  public final String hiddenInputName = "hiddenInputName";

  protected Form form;
  {
    form = new Form();
  }

  protected Script script;

  protected String golferName;


  public final String adressAreaName = "adressAreaName";

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

  public int headlineTextReaderId;
  public TextReader headlineText;
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public GolferFriendsSigningSheet(int headlineTextReaderId, String golferName,
    String submitName, String submitValue, String controlParameterValue) {
    this.submitButtonName = submitName;
    this.submitButtonValue = submitValue;
    this.golferName = golferName;
    this.headlineTextReaderId = headlineTextReaderId;
    this.controlParameterValue = controlParameterValue;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void drawSigningForm(){

   //Bæta inn möguleikanum þegar það vantar ID
    if (headlineTextReaderId != 0){
      headlineText = new TextReader(headlineTextReaderId);
    }
    else{
      headlineText = new TextReader(1);  //temporarily
    }

/*0*/ Text[] sheetTableStrings = {new Text(iwrb.getLocalizedString("golferpage.supporter","Stuðningsaðili")+":"),
      new Text(iwrb.getLocalizedString("golferpage.name","Nafn:")+":"),
      new Text(iwrb.getLocalizedString("golferpage.social_security_number","Kennitala")+":"),
      new Text(iwrb.getLocalizedString("golferpage.adress","Heimilisfang")+":"),
/*4*/    new Text(iwrb.getLocalizedString("golferpage.support_amount","Stuðningsupphæð kr.")+":"),
      new Text(iwrb.getLocalizedString("golferpage.support_duration","Stuðningstímabil / skuldfærsla í hversu marga mánuði")+":"),
      new Text(iwrb.getLocalizedString("golferpage.payment_type","Greiðslufyrirkomulag")+":"),
      new Text(iwrb.getLocalizedString("golferpage.cash_payment","Staðgreitt")),
/*8*/    new Text(iwrb.getLocalizedString("golferpage.into_an_account","Lagt inn á reikning")),
      new Text(iwrb.getLocalizedString("golferpage.s_s_number","kt.")),
      new Text(iwrb.getLocalizedString("golferpage.pay_director","Einnig er hægt að koma greiðslu til framkvæmdarstjóra GK")),
      new Text(iwrb.getLocalizedString("golferpage.giro_payment","Gíróseðill")),
/*12*/   new Text(iwrb.getLocalizedString("golferpage.require_giro_payment","Óska eftir að fá sendan gíróseðil")),
      new Text(iwrb.getLocalizedString("golferpage.credit_card","Kreditkort")),
      new Text(iwrb.getLocalizedString("golferpage.credit_card_type","Kortategund")),
      new Text(iwrb.getLocalizedString("golferpage.credit_card_number","Kortanúmer")),
/*16*/   new Text(iwrb.getLocalizedString("golferpage.credit_card_exp_date","Gildistími")),
      new Text(iwrb.getLocalizedString("golferpage.another","Annað")+":"),
      new Text(iwrb.getLocalizedString("golferpage.name_may_appear","Nafn mitt má koma fram á heimasíðu ")
      +golferName), new Text(iwrb.getLocalizedString("golferpage.yes","Já")),
/*20*/   new Text(iwrb.getLocalizedString("golferpage.no","Nei")),
      new Text(iwrb.getLocalizedString("golferpage.once","Eitt skipti")),
      new Text("2 "+iwrb.getLocalizedString("golferpage.months","mán.")),
      new Text("3 "+iwrb.getLocalizedString("golferpage.months","mán.")),
/*24*/   new Text("4 "+iwrb.getLocalizedString("golferpage.months","mán.")),
      new Text("5 "+iwrb.getLocalizedString("golferpage.months","mán.")),
      new Text("1000"),
      new Text("2000"),
/*28*/   new Text("3000"),
      new Text("4000"),
      new Text("5000")};

    for (int i = 0; i < sheetTableStrings.length; i++) {
      sheetTableStrings[i].setFontSize(1);
//      sheetTableStrings[i].setFontColor("FFFFFF");
    }

    String headlinesColor = new String("FF6000");

    sheetTableStrings[0].setFontSize(2);
    sheetTableStrings[0].setBold();
    sheetTableStrings[0].setFontColor(headlinesColor);
    sheetTableStrings[4].setFontSize(2);
    sheetTableStrings[4].setBold();
    sheetTableStrings[4].setFontColor(headlinesColor);
    sheetTableStrings[5].setFontSize(2);
    sheetTableStrings[5].setBold();
    sheetTableStrings[5].setFontColor(headlinesColor);
    sheetTableStrings[6].setFontSize(2);
    sheetTableStrings[6].setBold();
    sheetTableStrings[6].setFontColor(headlinesColor);

    sheetTableStrings[7].setBold();
    sheetTableStrings[11].setBold();
    sheetTableStrings[13].setBold();



    TextArea addressArea = new TextArea(adressAreaName,20,2);


    TextInput nameInput = new TextInput(nameInputName);
    nameInput.keepStatusOnAction();
    nameInput.setAsNotEmpty(iwrb.getLocalizedString(
      "golferpage.please_give_name","Vinsamlegast gefið nafn"));
    nameInput.setAsAlphabeticText(iwrb.getLocalizedString(
      "golferpage.please_write_letters","Vinsamlegast skrifið bókstafi í nafni"));
    TextInput sSNumberInput = new TextInput(sSNumberInputName);
    sSNumberInput.setAsNotEmpty(iwrb.getLocalizedString(
      "golferpage.please_give_ss_number","Vinsamlegast gefið gilda íslenska kennitölu"));
    sSNumberInput.setAsIcelandicSSNumber(iwrb.getLocalizedString(
      "golferpage.please_give_ssn_integers","Vinsamlegast gefið kennitölu"));
    TextInput anotherAmountInput = new TextInput(anotherAmountInputName);
    anotherAmountInput.setAsIntegers(iwrb.getLocalizedString(
      "golferpage.please_give_amount_integers","Vinsamlegast aðeins tölustafi i upphæð"));
    TextInput anotherDurationInput = new TextInput(anotherDurationInputName);
    TextInput accountNumberInput = new TextInput(accountNumberInputName);
    accountNumberInput.setAsIntegers(iwrb.getLocalizedString(
      "golferpage.please_give_account_integers","Vinsamlegast aðeins tölustafi i reikningsnúmeri"));
    TextInput sSNInput = new TextInput(sSNInputName);
    sSNInput.setAsIntegers(iwrb.getLocalizedString(
      "golferpage.please_give_ssn_integers","Vinsamlegast aðeins tölustafi i kt."));
    TextInput cretidCardTypeInput = new TextInput(cretidCardTypeInputName);
    TextInput creditCardNumberInput = new TextInput(creditCardNumberInputName);
    creditCardNumberInput.setAsIntegers(iwrb.getLocalizedString(
      "golferpage.please_give_amount_integers","Vinsamlegast aðeins tölustafi i kortanúmeri"));
    TextInput creditCardExpDateInput = new TextInput(creditCardExpDateInputName);

    sSNumberInput.setSize(10);
    anotherAmountInput.setSize(10);
    anotherDurationInput.setSize(15);
    sSNInput.setSize(10);

    nameInput.keepStatusOnAction();
    sSNumberInput.keepStatusOnAction();
    anotherAmountInput.keepStatusOnAction();
    anotherDurationInput.keepStatusOnAction();
    accountNumberInput.keepStatusOnAction();
    sSNInput.keepStatusOnAction();
    cretidCardTypeInput.keepStatusOnAction();
    creditCardNumberInput.keepStatusOnAction();
    creditCardExpDateInput.keepStatusOnAction();

    RadioButton yesButton = new RadioButton(yesOrNoButtonName, yesValue);
    RadioButton noButton = new RadioButton(yesOrNoButtonName, noValue);
    noButton.setSelected();

    RadioButton amountRadioButton1 = new RadioButton( amountRadioButtonName, amountButtonValue1);
    RadioButton amountRadioButton2 = new RadioButton( amountRadioButtonName, amountButtonValue2);
    RadioButton amountRadioButton3 = new RadioButton( amountRadioButtonName, amountButtonValue3);
    RadioButton amountRadioButton4 = new RadioButton( amountRadioButtonName, amountButtonValue4);
    RadioButton amountRadioButton5 = new RadioButton( amountRadioButtonName, amountButtonValue5);
    RadioButton durationRadioButton1 = new RadioButton(durationRadioButtonName, durationButtonValue1);
    RadioButton durationRadioButton2 = new RadioButton(durationRadioButtonName, durationButtonValue2);
    RadioButton durationRadioButton3 = new RadioButton(durationRadioButtonName, durationButtonValue3);
    RadioButton durationRadioButton4 = new RadioButton(durationRadioButtonName, durationButtonValue4);
    RadioButton durationRadioButton5 = new RadioButton(durationRadioButtonName, durationButtonValue5);

    Parameter hiddenInput = new Parameter(hiddenInputName, "false");

    amountRadioButton1.setOnClick("this.form.hiddenInputName.value=true");
    amountRadioButton2.setOnClick("this.form.hiddenInputName.value=true");
    amountRadioButton3.setOnClick("this.form.hiddenInputName.value=true");
    amountRadioButton4.setOnClick("this.form.hiddenInputName.value=true");
    amountRadioButton5.setOnClick("this.form.hiddenInputName.value=true");

    amountRadioButton1.keepStatusOnAction();
    amountRadioButton2.keepStatusOnAction();
    amountRadioButton3.keepStatusOnAction();
    amountRadioButton4.keepStatusOnAction();
    amountRadioButton5.keepStatusOnAction();
    yesButton.keepStatusOnAction();
    noButton.keepStatusOnAction();
    durationRadioButton1.keepStatusOnAction();
    durationRadioButton2.keepStatusOnAction();
    durationRadioButton3.keepStatusOnAction();
    durationRadioButton4.keepStatusOnAction();
    durationRadioButton5.keepStatusOnAction();

    CheckBox giroPaymentCheckBox = new CheckBox(giroPaymentCheckBoxName);

    giroPaymentCheckBox.keepStatusOnAction();

    Image submitImage = new Image();
    submitImage = iwrb.getImage("/golferpage/skratakki.gif");

    SubmitButton submitButton = new SubmitButton(submitImage,
      //iwrb.getLocalizedString("golferpage.sign_myself","Skrá mig"),
      submitButtonName, submitButtonValue);


    Image signFormBackground;

    signFormBackground = iwb.getImage("/shared/formGrunn.gif");

    Table mainTable = new Table(2,22);

//    mainTable.setBackgroundImage(signFormBackground);

    mainTable.setCellpadding(4);
    mainTable.setCellspacing(4);

    mainTable.mergeCells(1,1,2,1);
    mainTable.mergeCells(1,2,2,2);
    mainTable.mergeCells(1,6,2,6);
    mainTable.mergeCells(1,7,2,7);
    mainTable.mergeCells(1,8,2,8);
    mainTable.mergeCells(1,9,2,9);
    mainTable.mergeCells(1,10,2,10);
    mainTable.mergeCells(1,11,2,11);

    mainTable.setAlignment(1, 1, "center");

    headlineText.setAlignment("center");

    mainTable.add(headlineText,1,1);
    mainTable.addBreak(1,2);
    mainTable.add(sheetTableStrings[0],1,2);
    mainTable.add(sheetTableStrings[1],1,3);
    mainTable.add(nameInput,2,3);
    mainTable.add(sheetTableStrings[2],1,4);
    mainTable.add(sSNumberInput,2,4);
    mainTable.add(sheetTableStrings[3],1,5);
    mainTable.add(addressArea,2,5);
    Table dummyTable1 = new Table(4,2);
    dummyTable1.setCellpadding(2);
    dummyTable1.mergeCells(1,1,4,1);
    dummyTable1.setAlignment("left");
    dummyTable1.add(sheetTableStrings[18],1,1);
    dummyTable1.add(sheetTableStrings[19],1,2);
    dummyTable1.add(yesButton,2,2);
    dummyTable1.add(sheetTableStrings[20],3,2);
    dummyTable1.add(noButton,4,2);
    mainTable.add(dummyTable1,1,6);

    mainTable.addBreak(1,7);
    mainTable.add(sheetTableStrings[4],1,7);

    Table dummyTable2 = new Table(6,2);
    dummyTable2.add(sheetTableStrings[26],1,1);
    dummyTable2.add(amountRadioButton1,2,1);
    dummyTable2.add(sheetTableStrings[27],3,1);
    dummyTable2.add(amountRadioButton2,4,1);
    dummyTable2.add(sheetTableStrings[28],5,1);
    dummyTable2.add(amountRadioButton3,6,1);
    dummyTable2.add(sheetTableStrings[29],1,2);
    dummyTable2.add(amountRadioButton4,2,2);
    dummyTable2.add(sheetTableStrings[30],3,2);
    dummyTable2.add(amountRadioButton5,4,2);
    dummyTable2.add(sheetTableStrings[17],5,2);
    dummyTable2.add(anotherAmountInput,6,2);
    mainTable.add(dummyTable2,1,8);

    mainTable.addBreak(1,9);
    mainTable.add(sheetTableStrings[5],1,9);
    Table dummyTable3 = new Table(6,2);
    dummyTable3.add(sheetTableStrings[21],1,1);
    dummyTable3.add(durationRadioButton1,2,1);
    dummyTable3.add(sheetTableStrings[22],3,1);
    dummyTable3.add(durationRadioButton2,4,1);
    dummyTable3.add(sheetTableStrings[23],5,1);
    dummyTable3.add(durationRadioButton3,6,1);
    dummyTable3.add(sheetTableStrings[24],1,2);
    dummyTable3.add(durationRadioButton4,2,2);
    dummyTable3.add(sheetTableStrings[25],3,2);
    dummyTable3.add(durationRadioButton5,4,2);
    dummyTable3.add(sheetTableStrings[17],5,2);
    dummyTable3.add(anotherDurationInput,6,2);
    mainTable.add(dummyTable3,1,10);
    mainTable.addBreak(1,11);
    mainTable.add(sheetTableStrings[6],1,11);

    Table dummyTable4 = new Table(1,1);
    dummyTable4.add(sheetTableStrings[7],1,1);
    mainTable.add(dummyTable4,1,12);
    mainTable.add(sheetTableStrings[8],1,13);
    mainTable.add(accountNumberInput,2,13);
    mainTable.add(sheetTableStrings[9],1,14);
    mainTable.add(sSNInput,2,14);
    mainTable.add(sheetTableStrings[10],1,15);

    Table bottomSheetTable = new Table(3,4);
    bottomSheetTable.setCellpadding(0);
    bottomSheetTable.setCellspacing(0);
    bottomSheetTable.mergeCells(2,1,3,1);
    Table dummyTable5 = new Table(1,1);
    dummyTable5.add(sheetTableStrings[11],1,1);
    mainTable.add(dummyTable5,1,16);
    mainTable.add(sheetTableStrings[12],1,17);
    mainTable.add(giroPaymentCheckBox,2,17);
    Table dummyTable6 = new Table(1,1);
    dummyTable6.add(sheetTableStrings[13],1,1);
    mainTable.add(dummyTable6,1,18);
    mainTable.add(sheetTableStrings[14],1,19);
    mainTable.add(cretidCardTypeInput,2,19);
    mainTable.add(sheetTableStrings[15],1,20);
    mainTable.add(creditCardNumberInput,2,20);
    mainTable.add(sheetTableStrings[16],1,21);
    mainTable.add(creditCardExpDateInput,2,21);

    Table dummyTable7 = new Table(1,1);
    dummyTable7.add(submitButton,1,1);
    dummyTable7.setCellpadding(5);
    mainTable.add(dummyTable7,2,22);

    mainTable.add(hiddenInput,1,1);

    form.add(mainTable);
    add(form);
  }

  public void emptyForm(){
    //not implemented yet
  }

  private void setScript(Script script){
    this.script = script;
    setAssociatedScript(script);
  }

  private Script getScript(){

    if (getAssociatedScript() == null){
      setScript(new Script());
    }
    else{
      script = getAssociatedScript();
    }
    return script;
  }

  private void setCheckSubmit(){
    if ( getScript().getFunction("checkSubmit") == null){
      getScript().addFunction("checkSubmit","function checkSubmit(inputs){\n\n}");
    }
  }

  public void earlyWarningMessages(){
    form.setOnSubmit("return checkSubmit(this)");
    setCheckSubmit();
    getScript().addToFunction("checkSubmit","if (warnIfNotValid (inputs) == false ){\n return false;\n}\n");
    getScript().addFunction("warnIfNotValid","function warnIfNotValid (inputs) {\n"
//      +"    alert(inputs."+hiddenInputName+".value +' and '+inputs."+anotherAmountInputName+".value+' and also '+(inputs."+anotherAmountInputName+".value!= '')+' or '+(inputs."+hiddenInputName+".value)); \n "
      +" isPaymentSet=((inputs."+anotherAmountInputName+".value!= '') || (inputs."+hiddenInputName+".value != 'false'));\n"
//      +"  alert(isPaymentSet);\n"
      +"   isAccountPayment=((inputs."+accountNumberInputName+".value!= '') && (inputs."+sSNInputName+".value!= ''));\n"
      +"   isCashPayment=((inputs."+cretidCardTypeInputName+".value!= '') && (inputs."+creditCardNumberInputName+".value!= '') && (inputs."+creditCardExpDateInputName+".value!= '')); "
      +"\n   isGiroPayment=(inputs."+giroPaymentCheckBoxName+".checked);\n"
      +"  if (isPaymentSet) {\n "
      +"     if ((isAccountPayment) || (isCashPayment) || (isGiroPayment)){\n "
      +"      return true;\n "
      +"    }\n"
      +"     else{\n"
      +"     alert ( ' Villa i tegund greiðslu!' );\n "
      +"    return false;\n "
      +"    }\n "
      +"  }\n "
      +"  alert ( ' Villa i greiðsluupphæð!' );\n"
      +"   return false;\n"
      +" }\n");
  }

  private String getPaymentAmount(ModuleInfo modinfo){
    String amount ="";

    if (modinfo.isParameterSet(amountRadioButtonName)){
//      System.err.println("ARRRRRRG ÞETTA Á AÐ VERA JÁ " + modinfo.isParameterSet(amountRadioButtonName)+"    og parameterinn er " +modinfo.getParameter(amountRadioButtonName));
      if (modinfo.getParameter(amountRadioButtonName).equalsIgnoreCase(amountButtonValue1)) amount = "1000";
      else if (modinfo.getParameter(amountRadioButtonName).equalsIgnoreCase(amountButtonValue2)) amount = "2000";
      else if (modinfo.getParameter(amountRadioButtonName).equalsIgnoreCase(amountButtonValue3)) amount = "3000";
      else if (modinfo.getParameter(amountRadioButtonName).equalsIgnoreCase(amountButtonValue4)) amount = "4000";
      else if (modinfo.getParameter(amountRadioButtonName).equalsIgnoreCase(amountButtonValue5)) amount = "5000";
      amount = amount+" or ";
    }
    else amount="";
    return amount+modinfo.getParameter(anotherAmountInputName);
  }

  private String getPaymentDuration(ModuleInfo modinfo){
    String duration ="";

    if (modinfo.isParameterSet(durationRadioButtonName)){
//      System.err.println("ARRRRRRG ÞETTA Á AÐ VERA JÁ " + modinfo.isParameterSet(durationRadioButtonName)+"    og parameterinn er " +modinfo.getParameter(durationRadioButtonName));
      if (modinfo.getParameter(durationRadioButtonName).equalsIgnoreCase(durationButtonValue1)) duration = "1";
      else if (modinfo.getParameter(durationRadioButtonName).equalsIgnoreCase(durationButtonValue2)) duration = "2";
      else if (modinfo.getParameter(durationRadioButtonName).equalsIgnoreCase(durationButtonValue3)) duration = "3";
      else if (modinfo.getParameter(durationRadioButtonName).equalsIgnoreCase(durationButtonValue4)) duration = "4";
      else if (modinfo.getParameter(durationRadioButtonName).equalsIgnoreCase(durationButtonValue5)) duration = "5";
      duration = duration+" months or ";
    }
    else duration="";
    return duration+modinfo.getParameter(anotherDurationInputName);
  }

  private void viewChooser(ModuleInfo modinfo){
    String[] parameterValues;
    parameterValues = modinfo.getParameterValues(submitButtonName);

    if (parameterValues.length<2){
//      drawSigningForm();
      //Possible changes possible here hence. what window to get when submitted the form.

      String name;
      String sSNumber;
      String email;
      String adress;
      String cardType;
      String cardNumber;
      String cardExpDate;
      boolean nameAppearance;
      String paymentDuration;
      String paymentAmount;
      String bankAccount;
      String bankSSNumer;
      boolean giroPayment;

      name = modinfo.getParameter(nameInputName);
      sSNumber = modinfo.getParameter(sSNumberInputName);
      adress = modinfo.getParameter(adressAreaName);
      email = "";  //temporarily
      bankAccount = modinfo.getParameter(accountNumberInputName);
      bankSSNumer = modinfo.getParameter(sSNInputName);
      cardType = modinfo.getParameter(cretidCardTypeInputName);
      cardNumber = modinfo.getParameter(creditCardNumberInputName);
      cardExpDate = modinfo.getParameter(creditCardExpDateInputName);
      nameAppearance = (modinfo.getParameter(yesOrNoButtonName) == yesValue);
      paymentDuration = getPaymentDuration(modinfo);
      paymentAmount = getPaymentAmount(modinfo);
      giroPayment = (modinfo.isParameterSet(giroPaymentCheckBoxName));

      try {
        GolferFriendsDataBusiness.insertFriendsData(name, sSNumber, email, adress, cardType, cardNumber, cardExpDate,
          nameAppearance, paymentAmount, paymentDuration, bankAccount, bankSSNumer, giroPayment);
      }
      catch (Exception ex) {

      }

      Table table = new Table(1,1);
      table.setAlignment("center");
      table.add(" Þú hefur verið skráður ");
      add(table);
    }
    else{
      //not submitted here
      emptyForm();
      drawSigningForm();
    }
  }

  public void main(ModuleInfo modinfo) {

    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    viewChooser(modinfo);
    earlyWarningMessages();

  }
}
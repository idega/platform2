/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.access.AccessControl;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.Editor;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;


/**
 * @author laddi
 */
public class UnionCorrect extends Editor {

  Member eMember = null;
  String OkImageUrl = "/pics/formtakks/saekja.gif";
  String SaveImageUrl = "/pics/formtakks/vista.gif";
  String CloseImageUrl = "/pics/formtakks/loka.gif";
  List eUMIs = null;
  boolean isClubAdmin = false;

  protected PresentationObject makeLinkTable(int l){
    return new Text("");
  }
  protected void control(IWContext modinfo){
    isClubAdmin = AccessControl.isClubAdmin(modinfo);
    String member_id = modinfo.getParameter("member_id");

    this.makeView();
    if(isAdmin || isClubAdmin){
    try{
      if(modinfo.getParameter("save")!=null || modinfo.getParameter("save.x")!=null){
        if(modinfo.getParameter("rb")!=null && member_id!=null){
          eMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));
          setMainUnion(eMember,Integer.parseInt(modinfo.getParameter("rb")));
        }
        else if(modinfo.getParameter("newumi")!=null && member_id != null){
          eMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));
          makeNewUMI(eMember);
        }
      }
      else if(modinfo.getParameter("ok")!=null || modinfo.getParameter("ok.x")!=null || modinfo.getParameter("ssn") != null){
        String ssn = modinfo.getParameter("ssn").trim();
        if(ssn!=null&& ssn.length() > 5){
          List members = EntityFinder.findAllByColumn((Member) IDOLookup.instanciateEntity(Member.class),"social_security_number",ssn);
          if( members!=null ){
            eMember = (Member) members.get(0);
          }
        }
      }
      else if(member_id!=null){
        eMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(member_id));
      }
      else
        add("enginn valinn");

      this.addLinks(formatText("Klúbbar"));
      this.addMain(doView());
      this.setBorder(2);
    }
    catch(FinderException sql){add("sql vandræði"); sql.printStackTrace();}
    catch(SQLException sql){add("sql vandræði"); sql.printStackTrace();}
    }
    else
      this.addMain(formatText("Ekki Réttindi"));


  }

  private PresentationObject doView(){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setWidth("200");
    if(eMember != null){
      T.add(formatText(eMember.getName()),1,3);
      T.add(formatText("Kt : "),1,4);
      T.add(formatText(eMember.getSocialSecurityNumber()),1,4);
      T.add(new HiddenInput("member_id",String.valueOf(eMember.getID())));
    }
    TextInput SSN = new TextInput("ssn");
    SSN.setMaxlength(10);
    SSN.setLength(10);
    T.add(SSN,1,1);
    T.add(getUnionsTable(),1,5);
    SubmitButton ok    = new SubmitButton(new Image(OkImageUrl),"ok");
    SubmitButton save = new SubmitButton(new Image(SaveImageUrl),"save");
    CloseButton close   = new CloseButton(new Image(CloseImageUrl));
    T.add(ok,1,1);
    T.add(save,1,7);
    T.add(close,1,7);

    Form myForm = new Form();
    myForm.add(T);

    return myForm;
  }

   public Table getUnionsTable(){
    Table bTable = new Table();
    bTable.setWidth("100%");
    try {
       if(eMember != null)
        eUMIs = EntityFinder.findAllByColumn((UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class),"member_id",eMember.getID());
    }
    catch (SQLException ex) {

    }

    if(eUMIs != null && eMember != null){
      int len = eUMIs.size();
      Table T = new Table(3,len+1);
      T.setCellpadding(0);
      T.setCellspacing(0);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setWidth("100%");
      //T.add(headerText("Klúbbur"),1,1);
      //T.add(headerText("Aðild"),2,1);
      UnionMemberInfo eUmi;
      Union eUni;
      int iUnId;
      String color = DarkColor;
      for (int i = 0; i < len; i++) {
        eUmi = (UnionMemberInfo) eUMIs.get(i);
        iUnId = eUmi.getUnionID();
        eUni = GolfCacher.getCachedUnion(iUnId);
        Text tAbbrevation = headerText(eUni.getAbbrevation());
        String type = eUmi.getMembershipType();
        Text tType = headerText(mbsShipMap(type));
        RadioButton RB = new RadioButton("rb",String.valueOf(eUni.getID()));
        if(type.equalsIgnoreCase("main")){
          tAbbrevation.setFontColor("#FF0000");
          tType.setFontColor("#FF0000");
          RB.setSelected();
        }

        T.add(tAbbrevation,1,i+2);
        T.add(tType,2,i+2);
        T.add(RB,3,i+2);
      }
      bTable.add(T);
    }
    else{

      Table T = new Table(2,2);
      T.setCellpadding(0);
      T.setCellspacing(0);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setWidth("100%");

      String color = DarkColor;
      CheckBox chk = new CheckBox("newumi");
      T.add(formatText("Er utan klúbba"),1,1);
      T.add(formatText("Setja í Gestaklúbb"),1,2);
      T.add(chk,2,2);

      bTable.add(T);
    }
    return bTable;
  }
  private String mbsShipMap(String type){
    if("main".equalsIgnoreCase(type))
      return "Aðalkl.";
    else if("sub".equalsIgnoreCase(type))
      return "Aukakl.";
    else return "";
  }

  public Text headerText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      T.setFontColor(this.DarkColor);
      T.setFontSize(this.fontSize);
      T.setBold();
    }
    return T;
  }
  public Text headerText(int i){
    return headerText(String.valueOf(i));
  }

   public void setMainUnion(Member member,int iUnionId) throws SQLException {

    UnionMemberInfo[] unies = (UnionMemberInfo[]) ((UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class)).findAllByColumn("member_id",member.getID());
    boolean needToCreateUnionMemberInfo = true;
    for (int i = 0; i < unies.length; i++) {

      if(unies[i].getCardId()==0)
        unies[i].setCardId(1);
      if(unies[i].getPaymentTypeID()==0)
        unies[i].setPaymentTypeID(1);
      if( unies[i].getUnionID()!= iUnionId ){
        unies[i].setMembershipType("sub");
      }
      else {
        needToCreateUnionMemberInfo = false;
        unies[i].setMembershipType("main");
        unies[i].setMemberStatus("A");
      }
      unies[i].update();
    }
    
    if(needToCreateUnionMemberInfo){
    		makeNewMainUMI(member,iUnionId);
    }
  }
   
   public void setMemberInactiveInAllSubUnions(Member member) throws SQLException {

    UnionMemberInfo[] unies = (UnionMemberInfo[]) ((UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class)).findAllByColumn("member_id",member.getID());

    for (int i = 0; i < unies.length; i++) {
    		if("sub".equals(unies[i].getMembershipType())){
    	        unies[i].setMemberStatus("I");
    	        unies[i].update();
    		}
    }
  }
   
   public void setMemberInactiveInMainUnion(Member member) throws SQLException {

    UnionMemberInfo[] unies = (UnionMemberInfo[]) ((UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class)).findAllByColumn("member_id",member.getID());

    for (int i = 0; i < unies.length; i++) {
    		if("main".equals(unies[i].getMembershipType())){
    	        unies[i].setMemberStatus("I");
    	        unies[i].update();
    		}
    }
  }

  public void makeNewUMI(Member eMember){
    UnionMemberInfo umi = (UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class);
    umi.setUnionID(1);
    umi.setMemberID(eMember.getID());
    umi.setCardId(1);
    umi.setPaymentTypeID(1);
    umi.setMembershipType("main");
    umi.setMemberStatus("A");
    try {
      umi.insert();
    }
    catch (Exception ex) {
    		ex.printStackTrace();
    }

  }
  
  public void makeNewMainUMI(Member eMember, int unionId){
    UnionMemberInfo umi = (UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class);
    umi.setUnionID(unionId);
    umi.setMemberID(eMember.getID());
    umi.setCardId(1);
    umi.setPaymentTypeID(1);
    umi.setMembershipType("main");
    umi.setMemberStatus("A");
    try {
      umi.insert();
    }
    catch (Exception ex) {
    		ex.printStackTrace();
    }
  }
    
  
  
}
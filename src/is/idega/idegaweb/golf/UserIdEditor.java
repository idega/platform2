package is.idega.idegaweb.golf;

import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;
import is.idega.idegaweb.golf.entity.UserId;
import is.idega.idegaweb.golf.entity.UserIdHome;
import is.idega.idegaweb.golf.entity.UserIds;
import is.idega.idegaweb.golf.entity.UserIdsHome;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.Editor;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;



public class UserIdEditor extends Editor {

  public  final String strAction = "usid_action";
  public final int ACT5 = 5;
  public final String prefix = "usid_";
  public DecimalFormat myFormatter = new DecimalFormat("0000000000000000");

  public UserIdEditor(){
    super();
  }

  protected void control(IWContext modinfo){

    try{

      if(modinfo.getParameter(strAction) == null){
        doMain(modinfo);
      }
      if(modinfo.getParameter(strAction) != null){
        String sAct = modinfo.getParameter(strAction);
        int iAct = Integer.parseInt(sAct);

        switch (iAct) {
          case ACT1 : doMain(modinfo);        break;
          case ACT2 : doChange(modinfo);      break;
          case ACT3 : doUpdate(modinfo);      break;
          case ACT4 : doMakeIds(modinfo);      break;
          case ACT5 : doTrulyMakeIds(modinfo);      break;
          default: doMain(modinfo);           break;
        }
      }
    }
    catch(SQLException S){
      S.printStackTrace();
    }
    catch(Exception S){
      S.printStackTrace();
    }
  }

  protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(4,1);
    int last = 4;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(this.DarkColor);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link("Yfirlit");
    Link1.setFontColor(this.LightColor);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link Link2 = new Link("Breyta");
    Link2.setFontColor(this.LightColor);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link Link3 = new Link("Smíða númer");
    Link3.setFontColor(this.LightColor);
    Link3.addParameter(this.strAction,String.valueOf(this.ACT4));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
      LinkTable.add(Link3,3,1);
    }
    return LinkTable;
  }

  protected void doMain(IWContext modinfo) throws SQLException{
    UserIds[] I = getUserIds();
    Hashtable H = getUnionHash(getUnions());
    int count = I.length;
    Table T = new Table(5,count+1);
    T.setWidth("100%");
    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,MiddleColor);
    T.setCellpadding(2);
    T.setCellspacing(1) ;
    //T.setColumnAlignment(3, "right");
    T.add(formatText("Nr"),1,1);
    T.add(formatText("Klúbbur"),2,1);
    T.add(formatText("Byrjar"),3,1);
    T.add(formatText("Endar"),4,1);
    T.add(formatText("Síðasta"),5,1);
    if(isAdmin){
      if(count > 0){
        for (int i = 0;i < count;i++){
          T.add(formatText( String.valueOf(i+1)),1,i+2);
          Integer id = new Integer(I[i].getUnionId());
          if(H.contains(id)){
             String Abbr = (String)H.get(id);
            T.add(formatText(Abbr),2,i+2);
          }
          T.add(formatText(I[i].getStart()),3,i+2);
          T.add(formatText(I[i].getEnding()),4,i+2);
          T.add(formatText(I[i].getLast() ),5,i+2);
        }
      }
    }

    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(T);

  }

  protected void doChange(IWContext modinfo) throws SQLException{

    Form myForm = new Form();
    Union[] U = getUnions();
    UserIds[] UserIds = getUserIds();
    int count = U.length;
    Table T = new Table(5,count+1);
    T.setWidth("100%");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setColumnAlignment(1,"right");
    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,MiddleColor);
    T.add(formatText("Nr"),1,1);
    T.add(formatText("Klúbbur"),2,1);
    T.add(formatText("Byrjar"),3,1);
    T.add(formatText("Endar"),4,1);

    for (int i = 1; i <= count ;i++){
      int unionId = U[i-1].getID();
      String sUnion = U[i-1].getAbbrevation();
      String rownum = String.valueOf(i);
      String s = "";
      TextInput sone,stwo,sthree,sfour;
      TextInput eone,etwo,ethree,efour;
      CheckBox saveCheck = new CheckBox(prefix+"chk"+i,"true");
      HiddenInput uionIdInput = new HiddenInput(prefix+"unid"+i,String.valueOf(unionId));

      sone    = new TextInput(prefix+"sone"+i,"0352");
      stwo    = new TextInput(prefix+"stwo"+i);
      sthree  = new TextInput(prefix+"sthree"+i,"1000");
      sfour   = new TextInput(prefix+"sfour"+i,"0001");
      eone    = new TextInput(prefix+"eone"+i,"0352");
      etwo    = new TextInput(prefix+"etwo"+i);
      ethree  = new TextInput(prefix+"ethree"+i,"1999");
      efour   = new TextInput(prefix+"efour"+i,"9999");

      int w = 3;
      sone.setSize(w);
      stwo.setSize(w);
      sthree.setSize(w);
      sfour.setSize(w);
      eone.setSize(w);
      etwo.setSize(w);
      ethree.setSize(w);
      efour.setSize(w);

      sone.setMaxlength(4);
      stwo.setMaxlength(4);
      sthree.setMaxlength(4);
      sfour.setMaxlength(4);
      eone.setMaxlength(4);
      etwo.setMaxlength(4);
      ethree.setMaxlength(4);
      efour.setMaxlength(4);

      T.add(formatText(rownum),1,i+1);
      T.add(sUnion,2,i+1);
      T.add(sone,3,i+1);
      T.add(stwo,3,i+1);
      T.add(sthree,3,i+1);
      T.add(sfour,3,i+1);
      T.add(eone,4,i+1);
      T.add(etwo,4,i+1);
      T.add(ethree,4,i+1);
      T.add(efour,4,i+1);
      T.add(uionIdInput);
      T.add(saveCheck,5,i+1);
    }
    myForm.add(T);
    myForm.add(new HiddenInput(prefix+"rowcount",String.valueOf(count)));
    myForm.add(new SubmitButton("Vista",this.strAction,String.valueOf(this.ACT3 )));

    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(myForm);

  }

  protected void doUpdate(IWContext modinfo) throws Exception{

    int count = Integer.parseInt(modinfo.getParameter(prefix+"rowcount"));
    String sone,stwo,sthree,sfour;
    String eone,etwo,ethree,efour;
    String chk;
    String sUnionId;
    UserIds[] ids = new UserIds[count];
    UserIds usid = null;

    for (int i = 1; i < count+1 ;i++){
      chk = modinfo.getParameter(prefix+"chk"+i);
      if(chk!=null){
      sone    = modinfo.getParameter(prefix+"sone"+i);
      stwo    = modinfo.getParameter(prefix+"stwo"+i);
      sthree  = modinfo.getParameter(prefix+"sthree"+i);
      sfour = modinfo.getParameter(prefix+"sfour"+i);
      eone    = modinfo.getParameter(prefix+"eone"+i);
      etwo    = modinfo.getParameter(prefix+"etwo"+i);
      ethree  = modinfo.getParameter(prefix+"ethree"+i);
      efour   = modinfo.getParameter(prefix+"efour"+i);
      sUnionId = modinfo.getParameter(prefix+"unid"+i);
      try {
        usid = ((UserIdsHome) IDOLookup.getHomeLegacy(UserIds.class)).create();
        usid.setStart(makeId(sone,stwo,sthree,sfour));
        usid.setEnding(makeId(eone,etwo,ethree,efour));
        usid.setLast(usid.getStart());
        usid.setUnionId(Integer.parseInt(sUnionId));
        usid.insert();
      }
      catch (Exception ex) {
      }

    }// for loop

  }
   doMain(modinfo);
  }

  private void doMakeIds(IWContext modinfo) throws Exception{
    Form form = new Form();
    Table T = new Table();
    T.setWidth("100%");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setColumnAlignment(1,"right");
    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.add(drpClubs(getUnions(),prefix+"drp"),1,1);
    T.add(new SubmitButton("Keyra",this.strAction,String.valueOf(this.ACT5 )),2,1);
    form.add(T);
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(form);
  }

   private void doTrulyMakeIds(IWContext modinfo) throws Exception{
    int id = Integer.parseInt(modinfo.getParameter(prefix+"drp"));
    UnionMemberInfo[] umi = getUMIs(id);
    UserIds U = ((UserIds[])((UserIds) IDOLookup.instanciateEntity(UserIds.class)).findAllByColumn("union_id",id))[0];
    UserId userid = null;
    long last = new Long(U.getLast()).longValue();
    for (int i = 0; i < umi.length; i++) {
      userid = ((UserIdHome) IDOLookup.getHomeLegacy(UserId.class)).create();
      userid.setMemberId(umi[i].getMemberID());
      userid.setUserId(makeId(last++));
      userid.insert();
    }
    U.setLast(myFormatter.format(last));
    U.update();

    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    //this.addMain("búinn");
    doMain(modinfo);
  }


  private String makeId(String one,String two,String three,String four) throws NumberFormatException{
    String s = one+two+three+four;
    Long L = new Long(s);
    s = myFormatter.format(L.longValue());
    return s;
  }

  private String makeId(long l) throws NumberFormatException{
    String s = myFormatter.format(l);
    return s;
  }
  private UserIds[] getUserIds()throws SQLException{
    UserIds[] U = (UserIds[]) ((UserIds)IDOLookup.instanciateEntity(UserIds.class)).findAll();
    return U;
  }

  private Union[] getUnions() throws SQLException{
    Union[] u = (Union[])((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("abbrevation");
    return u;
  }
  private Union[] getUnion(int unionid)throws SQLException, FinderException{
    Union[] u = new Union[1];
    u[0] = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(unionid);
    return u;
  }

  private Hashtable getUnionHash(Union[] U){
    Hashtable H = new Hashtable();
    for (int i = 0; i < U.length; i++) {
      H.put(new Integer(U[i].getID()),U[i].getAbbrevation());
    }
    return H;
  }

  private UnionMemberInfo[] getUMIs(int iUnionId)throws SQLException{
    UnionMemberInfo[] umis = (UnionMemberInfo[])((UnionMemberInfo) IDOLookup.instanciateEntity(UnionMemberInfo.class)).findAllByColumn("union_id",String.valueOf(iUnionId),"member_status","A");
    return umis;
  }

  private DropdownMenu drpClubs(Union[] unions,String name){
    DropdownMenu drp = new DropdownMenu(name);
    int len = unions.length;
    if(len > 1)
      drp.addMenuElement("0","Allir");
    for(int i = 0; i < len; i++){
      drp.addMenuElement(unions[i].getID(),unions[i].getAbbrevation()+ " \t "+unions[i].getName());
    }
    return drp;
  }
}
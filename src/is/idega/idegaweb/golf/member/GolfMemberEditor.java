package is.idega.idegaweb.golf.member;



import is.idega.idegaweb.golf.block.image.presentation.GolfImage;
import is.idega.idegaweb.golf.entity.Member;

import java.sql.SQLException;

import com.idega.block.media.presentation.ImageInserter;
import com.idega.presentation.Editor;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;







public class GolfMemberEditor extends Editor{



  private GolfMemberProfile profile = null;

  private Member eMember;

  private int iUnionId;

  private final String sAction = "mb.ed.action";

  private String sActPrm = "0";

  private int iAction = 0;

  private String prefix = "mb.ed.";

  private int iImageId = -1;

  private String styleAttribute = "font-size: 8pt";

  private String sMemberImageURL = "/pics/member/x.gif";

  private int bodyFontSize = 1;

  private int fontSize = 2;



  public GolfMemberEditor(){



  }



  public GolfMemberEditor(Member eMember,int iUnionId){

    this.eMember = eMember;

    this.iUnionId = iUnionId;

    profile = new GolfMemberProfile(eMember,iUnionId);

  }



  protected void control(IWContext iwc){

    if(iwc.getParameter(sAction) != null)

      sActPrm = iwc.getParameter(sAction);

    else

      sActPrm = "0";

    try{

      iAction = Integer.parseInt(sActPrm);

      switch(iAction){

        case ACT1:    break;

        case ACT2: break;

        case ACT3:  break;

        case ACT4: doUpdate(iwc); break;

      }

      doMain(iwc);

    }

    catch(Exception e){

      e.printStackTrace();

    }





  }

  private void doMain(IWContext iwc)throws SQLException{

    add("hallo");

    add(drawTable(iwc));

  }

  private PresentationObject drawTable(IWContext iwc) throws SQLException{

    Form form = new Form();

    PresentationObject imageObject = null;

    Image memberImg;

    if(iImageId >0) {

      memberImg = new GolfImage(iImageId);

      memberImg.setWidth(110);

      iwc.getSession().removeAttribute("image_id");

      imageObject = memberImg;

    }

    else if( eMember != null && eMember.getImageId() != 1){

      memberImg = new GolfImage(eMember.getImageId());

      memberImg.setWidth(110);

      imageObject = memberImg;

    }

    else{

      ImageInserter imageInsert = new ImageInserter("image_id");

      imageInsert.setHasUseBox(false);

      imageInsert.setMaxImageWidth(110);

      //imageInsert.setDefaultImageURL(sMemberImageURL);

      imageObject = imageInsert;

    }



    Table table = new Table(7, 5);

    //table.setBorder( 1);

    int firstrow = 1,secondrow = 3, thirdrow = 5;

    int firstcol = 1,secondcol = 3,thirdcol = 5, fourthcol = 7;



    table.setAlignment("center");

    table.setVerticalAlignment("top");

    table.setCellpadding(0);

    table.setCellspacing(0);



    table.setHeight(2,"10");

    table.setHeight(4,"10");

    table.setWidth(2,"10");

    table.setWidth(4,"10");

    table.setWidth(6,"10");



    table.setRowVerticalAlignment(1,"top");

    table.setRowVerticalAlignment(3,"top");

    table.setRowVerticalAlignment(5,"top");

    //table.setColumnAlignment(1,"center");

    //table.setVerticalAlignment(1,3,"bottom");



    //BorderTable memberTable = getMemberTable();

    AddressInput AI = new AddressInput();

    PresentationObject addressTable = AI.getAddressTable(this.profile);

    System.err.println("hallo");

    form.add(addressTable);

 
    return form;



  }

  private void doUpdate(IWContext iwc){



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



  public void main(IWContext iwc){

    control(iwc);

  }







}

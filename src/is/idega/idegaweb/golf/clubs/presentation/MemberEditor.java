/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.clubs.presentation.MemberEditor.Editor.EntityInsert;
import is.idega.idegaweb.golf.entity.Address;
import is.idega.idegaweb.golf.entity.AddressHome;
import is.idega.idegaweb.golf.entity.Card;
import is.idega.idegaweb.golf.entity.CardHome;
import is.idega.idegaweb.golf.entity.Country;
import is.idega.idegaweb.golf.entity.CountryHome;
import is.idega.idegaweb.golf.entity.Family;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.PaymentType;
import is.idega.idegaweb.golf.entity.Phone;
import is.idega.idegaweb.golf.entity.PhoneHome;
import is.idega.idegaweb.golf.entity.PhoneType;
import is.idega.idegaweb.golf.entity.PriceCatalogue;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;
import is.idega.idegaweb.golf.entity.UnionMemberInfoHome;
import is.idega.idegaweb.golf.entity.ZipCode;
import is.idega.idegaweb.golf.handicap.presentation.HandicapOverview;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.service.AccountViewer;
import is.idega.idegaweb.golf.service.GroupMemberInsertWindow;
import is.idega.idegaweb.golf.service.Tariffer;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import com.idega.block.image.presentation.SimpleUploaderWindow;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BorderTable;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HeaderTable;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;
import com.idega.util.text.SocialSecurityNumber;

/**
 * @author laddi
 */
public class MemberEditor extends GolfWindow {

  public MemberEditor() {
    setWidth(690);
    setHeight(610);
    setTitle("Member editor");
    setScrollbar(true);
    setResizable(true);
    setTitlebar(false);
    setMenubar(false);
    add(new Editor());
  }

  public class Editor extends GolfBlock {

    private static final int COUNTRY_ID = 1;

    IWResourceBundle iwrb;

    IWBundle iwb;

    public void main(IWContext modinfo) throws Exception {
      try {
        boolean doSocialSecuritySearchOnNewMember = true;

        String union_id = (String) (modinfo
            .getSessionAttribute("golf_union_id"));
        String member_id = modinfo.getParameter("member_id");

        IWResourceBundle iwrb = getResourceBundle();
        IWBundle iwb = getBundle();

        /// added by eiki 3.may 2001

        Table linkTable = new Table();
        linkTable.setWidth("100%");
        linkTable.setCellpadding(0);
        linkTable.setCellspacing(0);
        linkTable.setBackgroundImage(iwb.getImage("shared/greytiler.gif"));
        //linkTable.setColor("#BDBDBD");

        Link general = new Link(iwrb.getImage("member/general.gif",
            "Almennar upplýsingar kylfingsins"));
        general.addParameter("member_id", member_id);
        Link finance = new Link(iwrb.getImage("member/finance.gif",
            "Fjármál kylfingsins"), "/tarif/accountview.jsp");
        finance.addParameter("member_id", member_id);
        Link handicap = new Link(iwrb.getImage("member/handicap_overview.gif",
            iwrb.getLocalizedString("member.handicap", "Handicap")));
        handicap.addParameter("member_id", member_id);
        handicap.addParameter("menuaction", "handicap");
        Link history = new Link(iwrb.getImage("member/history.gif",
            "Saga kylfingsins"));
        history.addParameter("member_id", member_id);
        history.addParameter("menuaction", "history");
        Window commentWindow = new Window("Athugasemd", 400, 300,
            "/clubs/membercomment.jsp");
        Link comment = new Link(iwrb.getImage("member/comments.gif",
            "Athugasemd"), commentWindow);
        comment.addParameter("member_id", member_id);
        comment.addParameter("union_id", union_id);
        Window loginWindow = new Window("Login", 200, 270,
            "/clubs/memberlogin.jsp");
        loginWindow.setScrollbar(false);
        Link login = new Link(iwrb.getImage("member/password.gif", "Lykilorð"),
            loginWindow);
        login.addParameter("member_id", member_id);
        login.addParameter("union_id", union_id);
        Window clubsWindow = new Window("Athugasemd", 250, 250,
            "/clubs/unioncorrect.jsp");
        clubsWindow.setScrollbar(false);
        Link clubs = new Link(iwrb.getImage("member/clubs.gif", "Klúbbar"),
            clubsWindow);
        clubs.addParameter("member_id", member_id);

        if (member_id != null) {
          linkTable.add(general);
          linkTable.add(finance);
          linkTable.add(handicap);
          linkTable.add(history);
          linkTable.add(comment);
          linkTable.add(login);
          linkTable.add(clubs);
          add(linkTable);
        }
        ////

        String action = modinfo.getParameter("menuaction");
        if (action == null) {
          //debug check beacause of a member that is new in the system doesn't
          // have a member_id
          String imagecheck = modinfo.getParameter("image_id");

          if (doSocialSecuritySearchOnNewMember
              && ((member_id == null) && (imagecheck == null))) {
            Table tafel = new Table(2, 2);
            Form socialForm = new Form();
            TextInput input = new TextInput("socialnumber");
            String text1 = iwrb.getLocalizedString("member.newmembermessage",
                "Enter the SocialSecurityNumber of the new member");
            //Text texti = new Text("Skrifaðu inn kennitölu nýja félagans");
            Text texti = new Text(text1);
            texti.setBold();
            HiddenInput actionInput = new HiddenInput("menuaction",
                "checksocial");

            tafel.add(texti, 1, 1);
            tafel.add(input, 1, 2);
            tafel.add(new SubmitButton(), 2, 2);
            tafel.add(actionInput, 2, 1);
            socialForm.add(tafel);
            add(socialForm);
          } else
            main2(member_id, union_id, modinfo);

        } else if (action.equalsIgnoreCase("checksocial")) {
          String socialnumber = modinfo.getParameter("socialnumber");
          if ((socialnumber != null) && !(socialnumber.equalsIgnoreCase(""))) {
            List members = EntityFinder.findAllByColumn((Member) IDOLookup
                .instanciateEntity(Member.class), "social_security_number",
                socialnumber);
            if (members != null) {
              Member member = (Member) members.get(0);
              main2(Integer.toString(member.getID()), union_id, modinfo);
            } else
              main2(member_id, union_id, modinfo);
          } else
            main2(member_id, union_id, modinfo);
        } else if (action.equalsIgnoreCase("finance")) {
          if (member_id != null) add(new AccountViewer());
        } else if (action.equalsIgnoreCase("handicap")) {
          if (member_id != null) add(new HandicapOverview(member_id));
        } else if (action.equalsIgnoreCase("history")) {
          if (member_id != null) {
            Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class))
                .findByPrimaryKey(Integer.parseInt(member_id));
            MemberInfo meminf = member.getMemberInfo();
            Table T = new Table(1, 2);
            T.setHeight(1, "50");
            T.setWidth("100%");
            T.setCellpadding(4);
            T.setAlignment(1, 2, "center");
            Table tafla = new Table(1, 3);
            tafla.setCellspacing(0);
            tafla.setWidth(400);
            tafla.setColor(1, 1, "#336660");
            tafla.setColor(1, 2, "#ADCAB1");
            tafla.setColor(1, 3, "#CEDFD0");
            Text texti = new Text(iwrb.getLocalizedString("member.history",
                "History of")
                + " " + member.getName());
            texti.setFontColor("#FFFFFF");
            texti.setBold();
            tafla.add(texti);
            tafla.add(meminf.getHistory(), 1, 3);
            T.add(tafla, 1, 2);
            add(T);
          } else if (modinfo.getParameter("account_action") != null) {
            add(new AccountViewer());
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void main2(String memberString, String unionString, IWContext modinfo)
        throws IOException {
      GolfMemberInsert inserter = null;
      iwrb = getResourceBundle();
      iwb = getBundle();
      int union_id = -1;
      int memberId = -1;

      if (unionString == null) {
        //debug eiki
        add("Félagsnúmer, Session-ið er útrunnið");
      } else {
        union_id = Integer.parseInt(unionString);
      }

      if (memberString != null) {
        memberId = Integer.parseInt(memberString);
      }
      try {
        if (memberId != -1) {
          inserter = new GolfMemberInsert(modinfo, memberId, union_id);
        } else {
          inserter = new GolfMemberInsert(modinfo, union_id);
        }
        inserter.setBundle(iwrb, iwb);
        inserter.showInputForm();
      } catch (Exception er) {
        System.err.println(er.getMessage());
        er.printStackTrace(System.err);
      }
    }

    public class GolfMemberInsert {

      private AddressInsert addrInsert;

      private AddressInsert addrInsert2;

      private MemberInsert memberInsert;

      private PhoneInsert homePhoneInsert;

      private PhoneInsert gsmInsert;

      private PhoneInsert workPhoneInsert;

      private UnionMemberInfoInsert unionMemberInsert;

      private CardInsert cardInsert;

      private MemberInfoInsert memInfoInsert;

      private Form form = new Form();

      private IWContext modinfo;

      private Image memberImg = new Image("/pics/member/x.gif");

      private Image uppi = new Image("/pics/member/uppi.gif");

      private Image info = new Image("/pics/member/upplysingar.gif");

      private Vector vError = new Vector();

      private String strSessionId = "error";

      //private String strErrorPage = "membererror.jsp";
      private String strUnionId = null;

      private int iUnionId = -1;

      private int iMemberId = -1;

      private int iImageId = 1;

      private Member member, eMember;

      private List eUMIs = null;

      private String imageId;

      private boolean isUpdate;

      private int memberID = -1;

      private String DarkColor = "#336660", LightColor = "#CEDFD0",
          MiddleColor = "#ADCAB1";

      private String styleAttribute = "font-size: 8pt";

      private String sMemberImageURL = "/pics/member/x.gif";

      private int bodyFontSize = 1;

      private int fontSize = 2;

      private IWResourceBundle iwrb;

      private IWBundle iwb;

      public GolfMemberInsert(IWContext modinfo, int unionId)
          throws SQLException {
        this.modinfo = modinfo;
        isUpdate = false;
        this.iUnionId = unionId;
        strUnionId = String.valueOf(unionId);
        addrInsert = new AddressInsert(modinfo, "heimili1", "heimililand1",
            "postnr1");
        addrInsert2 = new AddressInsert(modinfo, "heimili2", "heimililand2",
            "postnr2");
        memberInsert = new MemberInsert(modinfo);
        homePhoneInsert = new PhoneInsert(modinfo, "simi", "similand1",
            "homephonetype");
        gsmInsert = new PhoneInsert(modinfo, "gsm", "similand2", "gsmphonetype");
        workPhoneInsert = new PhoneInsert(modinfo, "vinnusimi", "similand3",
            "workphonetype");
        unionMemberInsert = new UnionMemberInfoInsert(modinfo);
        unionMemberInsert.setUnionId(unionId);
        cardInsert = new CardInsert(modinfo);
        memInfoInsert = new MemberInfoInsert(modinfo);

      }

      public GolfMemberInsert(IWContext modinfo, int memberId, int unionId)
          throws SQLException, FinderException {
        this.modinfo = modinfo;
        memberID = memberId;
        this.iMemberId = memberID;
        this.iUnionId = unionId;
        eUMIs = EntityFinder.findAllByColumn((UnionMemberInfo) IDOLookup
            .instanciateEntity(UnionMemberInfo.class), "member_id", iMemberId);
        isUpdate = true;
        strUnionId = String.valueOf(unionId);
        member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class))
            .findByPrimaryKey(memberId);
        eMember = member;
        Address[] addressArr = member.getAddress();
        int addressLenght = addressArr.length;
        Phone[] phoneArr = member.getPhone();
        int phoneLength = phoneArr.length;
        int unionMemIfnId = -1;
        int memberInfId = -1;
        int cardId = -1;

        if (member.getUnionMemberInfo(String.valueOf(unionId), String
            .valueOf(memberId)) != null)
            unionMemIfnId = member.getUnionMemberInfo(String.valueOf(unionId),
                String.valueOf(memberId)).getID();
        if (member.getMemberInfo() != null)
            memberInfId = member.getMemberInfo().getID();

        iImageId = member.getImageId();

        memberInsert = new MemberInsert(modinfo, member.getID());

        if (addressLenght > 0) {
          addrInsert = new AddressInsert(modinfo, addressArr[0].getID(),
              "heimili1", "heimililand1", "postnr1");
        } else {
          addrInsert = new AddressInsert(modinfo, "heimili1", "heimililand1",
              "postnr1");
          //addrInsert = new AddressInsert(modinfo);
        }
        if (addressLenght > 1)
          addrInsert2 = new AddressInsert(modinfo, addressArr[1].getID(),
              "heimili2", "heimililand2", "postnr2");
        else
          addrInsert2 = new AddressInsert(modinfo, "heimili2", "heimililand2",
              "postnr2");

        if (phoneLength > 0)
          homePhoneInsert = new PhoneInsert(modinfo, phoneArr[0].getID(),
              "simi", "similand1", "homephonetype");
        else
          homePhoneInsert = new PhoneInsert(modinfo, "simi", "similand1",
              "homephonetype");
        if (phoneLength > 1)
          gsmInsert = new PhoneInsert(modinfo, phoneArr[1].getID(), "gsm",
              "similand2", "gsmphonetype");
        else
          gsmInsert = new PhoneInsert(modinfo, "gsm", "similand2",
              "gsmphonetype");
        if (phoneLength > 2)
          workPhoneInsert = new PhoneInsert(modinfo, phoneArr[2].getID(),
              "vinnusimi", "similand3", "workphonetype");
        else
          workPhoneInsert = new PhoneInsert(modinfo, "vinnusimi", "similand3",
              "workphonetype");
        if (unionMemIfnId != -1) {
          unionMemberInsert = new UnionMemberInfoInsert(modinfo, unionMemIfnId,
              memberId, unionId);
        } else {
          unionMemberInsert = new UnionMemberInfoInsert(modinfo);
        }
        if (memberInfId != -1) {
          memInfoInsert = new MemberInfoInsert(modinfo, memberInfId);
        } else {
          memInfoInsert = new MemberInfoInsert(modinfo);
        }

        cardId = unionMemberInsert.getUnionMemberInfo().getCardId();
        //debug
        if ((cardId != -1) && (cardId != 1)) {
          cardInsert = new CardInsert(modinfo, cardId);
        } else {
          cardInsert = new CardInsert(modinfo);
        }
        unionMemberInsert.setUnionId(unionId);
        unionMemberInsert.setMemberId(memberId);
      }

      public void setBundle(IWResourceBundle iwrb, IWBundle iwb) {
        this.iwb = iwb;
        this.iwrb = iwrb;
      }

      public void showInputForm() throws IOException, SQLException,
          FinderException {

        boolean isUpdating = false;
        if (modinfo.getParameter("save") != null
            || modinfo.getParameter("save.x") != null) {
          isUpdating = true;
          handleError();
        }

        PresentationObject imageObject = null;
        ImageInserter imageInsert = null;
        if (iImageId != 1) {
          imageInsert = new ImageInserter(member.getImageId());
        } else {
          imageInsert = new ImageInserter();
        }
        imageInsert.setHasUseBox(false);
        imageInsert.setMaxImageWidth(110);
        imageInsert.setWindowClassToOpen(SimpleUploaderWindow.class);
        //	    imageInsert.setDefaultImageURL(sMemberImageURL);
        imageObject = imageInsert;

        if (member != null)
            form.add(new HiddenInput("member_id", String
                .valueOf(member.getID())));
        /*
         * form.setAction(modinfo.getRequest().getRequestURI()+"?member_id="+member.getID());
         * else
         * form.setAction(modinfo.getRequest().getRequestURI()+"?cmd=submit");
         */

        boolean border = false;

        Table Frame = new Table(2, 1);
        Frame.setCellpadding(0);
        Frame.setCellspacing(0);
        Frame.setRowVerticalAlignment(1, "top");
        Table leftTable = new Table(1, 8);
        leftTable.setCellpadding(0);
        leftTable.setCellspacing(0);

        Frame.add(leftTable, 1, 1);
        Table table = new Table(4, 5);
        table.setCellpadding(0);
        table.setCellspacing(0);
        Frame.add(table, 2, 1);
        // debug
        if (border) {
          Frame.setBorder(1);
          leftTable.setBorder(1);
          table.setBorder(1);
        }

        int firstrow = 1, secondrow = 3, thirdrow = 5;
        int firstcol = 1, secondcol = 2, thirdcol = 4;
        if (isUpdating) {
          String sSaving = iwrb.getLocalizedString("member.saving", "Saving");
          Text text = new Text(sSaving, true, true, true);
          text.setFontColor("red");
          text.setFontSize("4");
          leftTable.add(text, 1, 5);
        }
        Frame.setAlignment("center");

        table.setCellpadding(0);
        table.setCellspacing(0);

        table.setHeight(firstrow, "220");
        table.setHeight(secondrow, "220");
        table.setHeight(thirdrow, "220");
        table.setWidth(1, "10");
        table.setWidth(2, "210");
        table.setWidth(3, "10");
        table.setWidth(4, "210");

        leftTable.setAlignment(1, 6, "center");
        leftTable.setHeight(1, "210");
        table.setRowVerticalAlignment(1, "top");
        table.setRowVerticalAlignment(3, "top");
        table.setRowVerticalAlignment(5, "top");

        BorderTable memberTable = getMemberTable();
        BorderTable addressTable = getAddressTable();
        BorderTable phoneTable = getPhoneTable();
        BorderTable cardTable = getCardTable();
        BorderTable groupTable = getGroupTable();
        BorderTable unionMemInfTable = getUnionMemberInfoTable();
        BorderTable memberInfoTable = getMemberInfoTable();
        BorderTable familyTable = getFamilyTable();
        BorderTable unionsTable = getUnionsTable();
        BorderTable statusTable = getStatusTable();

        String color = LightColor;
        memberTable.setColor(color);
        addressTable.setColor(color);
        phoneTable.setColor(color);
        cardTable.setColor(color);
        unionMemInfTable.setColor(color);
        memberInfoTable.setColor(color);
        familyTable.setColor(color);
        groupTable.setColor(color);
        unionsTable.setColor(color);

        color = DarkColor;
        memberTable.setBorderColor(color);
        addressTable.setBorderColor(color);
        phoneTable.setBorderColor(color);
        memberInfoTable.setBorderColor(color);
        unionMemInfTable.setBorderColor(color);
        cardTable.setBorderColor(color);
        familyTable.setBorderColor(color);
        groupTable.setBorderColor(color);
        unionsTable.setBorderColor(color);
        statusTable.setBorderColor(color);
        int b = 0;
        memberTable.setBorder(b);
        addressTable.setBorder(b);
        phoneTable.setBorder(b);
        memberInfoTable.setBorder(b);
        unionMemInfTable.setBorder(b);
        cardTable.setBorder(b);
        familyTable.setBorder(b);
        groupTable.setBorder(b);
        unionsTable.setBorder(b);
        statusTable.setBorder(b);

        String width = "100%";
        String height = "210";
        memberTable.setWidth(width);
        addressTable.setWidth(width);
        phoneTable.setWidth(width);
        cardTable.setWidth(width);
        unionMemInfTable.setWidth(width);
        familyTable.setWidth(width);
        groupTable.setWidth(width);
        memberInfoTable.setWidth(width);
        unionsTable.setWidth(width);
        statusTable.setWidth(width);

        memberTable.setHeight(height);
        addressTable.setHeight(height);
        phoneTable.setHeight(height);
        cardTable.setHeight(height);
        unionMemInfTable.setHeight(height);
        familyTable.setHeight(90);
        groupTable.setHeight(90);
        memberInfoTable.setHeight(50);
        //unionsTable.setHeight("200");

        Table T = new Table(1, 2);
        T.setHeight("100%");
        T.setCellpadding(0);
        T.setCellspacing(0);

        T.add(headerText(iwrb.getLocalizedString("member.photo", "Photo")), 1,
            1);
        T.add(imageObject, 1, 1);
        T.add(
            headerText(iwrb.getLocalizedString("member.handicap", "Handicap")),
            1, 2);
        T.add(memberInfoTable, 1, 2);

        leftTable.add(T, 1, 1);
        leftTable.add(headerText(iwrb.getLocalizedString("member.clubs",
            "Clubs")), 1, 3);
        leftTable.add(unionsTable, 1, 3);

        SubmitButton save = new SubmitButton(
            iwrb.getImage("/buttons/save.gif"), "save");
        leftTable.add(save, 1, 6);
        leftTable.add(statusTable, 1, 8);

        String sMember = iwrb.getLocalizedString("member.member", "Member");
        table.add(headerText(sMember), secondcol, firstrow);
        table.add(memberTable, secondcol, firstrow);

        String sAddress = iwrb.getLocalizedString("member.address", "Address");
        table.add(headerText(sAddress), secondcol, secondrow);
        table.add(addressTable, secondcol, secondrow);

        String sPhones = iwrb.getLocalizedString("member.phones", "Phones");
        table.add(headerText(sPhones), thirdcol, secondrow);
        table.add(phoneTable, thirdcol, secondrow);

        String sClub = iwrb.getLocalizedString("member.club", "Club");
        table.add(headerText(sClub), thirdcol, firstrow);
        table.add(unionMemInfTable, thirdcol, firstrow);

        String sCard = iwrb.getLocalizedString("member.card", "Card");
        table.add(headerText(sCard), thirdcol, thirdrow);
        table.add(cardTable, thirdcol, thirdrow);

        if (memberID != -1) {

          String sFamily = iwrb.getLocalizedString("member.family", "Family");
          String sGroups = iwrb.getLocalizedString("member.groups", "Groups");
          table.add(headerText(sFamily), secondcol, thirdrow);
          table.add(familyTable, secondcol, thirdrow);
          table.add(getFamilyLinkTable(), secondcol, thirdrow);
          table.add(headerText(sGroups), secondcol, thirdrow);
          table.add(groupTable, secondcol, thirdrow);
          table.add(getGroupLinkTable(), secondcol, thirdrow);
        }

        table.add(Text.getBreak(), firstcol, secondrow);
        //table.setAlignment(firstcol,secondrow,"center");
        table.setColor("#FFFFFF");
        table.setCellspacing(0);
        form.add(Frame);
        add(form);
      }

      private void handleError() throws IOException, SQLException {
        vError.addAll(memberInsert.getNeetedEmptyFields());

        if (vError.size() > 0) {
          modinfo.getSession().setAttribute(strSessionId, vError);
          // modinfo.getResponse().sendRedirect(strErrorPage);
          //getParentPage().setToRedirect(strErrorPage);
        } else
          insert();
      }

      private void insert() throws SQLException, IOException {

        if (member == null) {
          Family family = (Family) IDOLookup.createLegacy(Family.class);
          family.insert();
          unionMemberInsert.getUnionMemberInfo().setFamily(family);
        }

        imageId = modinfo.getParameter("image_id");
        if ((imageId != null) && (!imageId.equals("null"))) {
          int id = 1;
          try {
            id = Integer.parseInt(imageId);
          } catch (NumberFormatException nfe) {

          }
          if (id < 1) id = 1;
          memberInsert.getMember().setImageId(id);

        }

        memberInsert.store();
        member = memberInsert.getMember();

        memInfoInsert.setMemberId(member.getID());
        memInfoInsert.store();
        homePhoneInsert.store(member);
        gsmInsert.store(member);
        workPhoneInsert.store(member);
        addrInsert.store(member);
        addrInsert2.store(member);
        cardInsert.store();
        unionMemberInsert.setMemberId(member.getID());
        int cardID = cardInsert.getCard().getID();
        if (cardID == -1)
          unionMemberInsert.setCardId(1);
        else
          unionMemberInsert.setCardId(cardID);

        unionMemberInsert.store();
        /*
         * String type =
         * unionMemberInsert.getUnionMemberInfo().getMembershipType();
         * if(type.equalsIgnoreCase("main")) member.setMainUnion(iUnionId);
         */
        /*
         * if(modinfo.getSession().getAttribute("image_id") != null) { String
         * imId = (String) modinfo.getSession().getAttribute("image_id");
         * member.setimage_id(Integer.parseInt(imId));
         * modinfo.getSession().removeAttribute("image_id"); }
         */
        //debug
        // modinfo.getResponse().sendRedirect(modinfo.getRequest().getRequestURI()+"?&member_id="+member.getID());
        getParentPage().setToRedirect(
            modinfo.getRequest().getRequestURI() + "?&member_id="
                + member.getID());
      }

      private void checkEmptyFields(String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
          vError.add(strArr[i]);
        }

      }

      public void forwardPage(String strURL) {
        try {
          ServletContext sc = modinfo.getServletContext();
          RequestDispatcher rd;

          rd = sc.getRequestDispatcher(strURL);
          rd.forward(modinfo.getRequest(), modinfo.getResponse());
        } catch (java.io.IOException e) {
          System.out.println(e);
        } catch (javax.servlet.ServletException e) {
          System.out.println(e);
        }

      }

      public BorderTable getAddressTable() {
        BorderTable hTable = new BorderTable();
        Table table = new Table(2, 6);
        hTable.add(table);

        table.add(addrInsert.formatText(iwrb.getLocalizedString(
            "member.address", "Heimili")), 1, 1);
        table.add(addrInsert.formatText(iwrb.getLocalizedString(
            "member.zipcode", "Póstnr")), 1, 2);
        table.add(addrInsert.formatText(iwrb.getLocalizedString(
            "member.country", "Land")), 1, 3);

        table.add(addrInsert.formatText(iwrb.getLocalizedString(
            "member.address2", "Heimili 2")), 1, 4);
        table.add(addrInsert.formatText(iwrb.getLocalizedString(
            "member.zipcode", "Póstnr")), 1, 5);
        table.add(addrInsert.formatText(iwrb.getLocalizedString(
            "member.country", "Land")), 1, 6);

        table.add(addrInsert.getInputAddress(), 2, 1);
        table.add(addrInsert.getDropZipcode(), 2, 2);
        table.add(addrInsert.getDropCountry(), 2, 3);

        table.add(addrInsert2.getInputAddress(), 2, 4);
        table.add(addrInsert2.getDropZipcode(), 2, 5);
        table.add(addrInsert2.getDropCountry(), 2, 6);

        return hTable;
      }

      public BorderTable getPhoneTable() {

        BorderTable hTable = new BorderTable();
        //hTable.setHeaderText("Sími");
        Table table = new Table(2, 3);
        hTable.add(table);

        /*
         * table.add("Tegund", 1, 1); table.add("Númer", 1, 2);
         * 
         * table.add("Tegund", 1, 3); table.add("Númer", 1, 4);
         * 
         * table.add("Tegund", 1, 5); table.add("Númer", 1, 6);
         */

        homePhoneInsert.getInputPhoneNumber().setSize(11);
        workPhoneInsert.getInputPhoneNumber().setSize(11);
        gsmInsert.getInputPhoneNumber().setSize(11);

        table.add(homePhoneInsert.getDropType(), 1, 1);
        table.add(homePhoneInsert.getInputPhoneNumber(), 2, 1);

        table.add(workPhoneInsert.getDropType(), 1, 2);
        table.add(workPhoneInsert.getInputPhoneNumber(), 2, 2);

        table.add(gsmInsert.getDropType(), 1, 3);
        table.add(gsmInsert.getInputPhoneNumber(), 2, 3);

        table.setCellpadding(3);

        return hTable;
      }

      public BorderTable getCardTable() {
        return cardInsert.getInputTable();
      }

      public BorderTable getMemberTable() {
        return memberInsert.getInputTable();
      }

      public BorderTable getUnionMemberInfoTable() {
        return unionMemberInsert.getInputTable();
      }

      public BorderTable getMemberInfoTable() {
        return this.memInfoInsert.getInputTable();
      }

      /*
       * public HeaderTable getGroupTable() { return
       * this.groupInsert.getInputTable(false); }
       */

      public BorderTable getGroupTable() {
        BorderTable hTable = new BorderTable();
        //hTable.setHeaderText("Flokkar");
        try {

          if (eMember != null && iUnionId != -1) {//debug eiki && gimmi
            Union uni = ((UnionHome) IDOLookup.getHomeLegacy(Union.class))
                .findByPrimaryKey(iUnionId);
            Group[] groupArr = null;
            Group[] grArr = null;

            Group theGroup = (Group) IDOLookup.instanciateEntity(Group.class);
            grArr = (Group[]) theGroup
                .findAll("select group_.* from group_, union_group, group_member where member_id = '"
                    + eMember.getID()
                    + "' and group_.group_id = group_member.group_id and union_group.group_id = group_member.group_id and union_group.union_id = 3");
            groupArr = (Group[]) theGroup
                .findAll("select group_.* from group_, union_group, group_member where member_id = '"
                    + eMember.getID()
                    + "' and group_.group_id = group_member.group_id and union_group.group_id = group_member.group_id and union_group.union_id = "
                    + uni.getID());
            groupArr = joinArrays(grArr, groupArr);

            Table table = new Table(1, groupArr.length);
            table.setAlignment("center");
            int i = 0;

            for (; i < groupArr.length; i++) {
              Text t = new Text(groupArr[i].getName(), true, false, false);
              t.setFontFace("Arial");
              t.setFontSize(2);
              table.add(t, 1, i + 1);
            }
            hTable.add(table);
          } else {
            Table empty = new Table(1, 1);
            empty.add("Það þarf að vista áður<br>en flokkur er valinn");
            hTable.add(empty);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return hTable;
      }

      public BorderTable getFamilyTable() throws SQLException, FinderException {
        BorderTable familyTable = new BorderTable();
        UnionMemberInfo uniMem = null;
        if (eMember != null) {
          uniMem = eMember.getUnionMemberInfo(iUnionId);
          if (uniMem != null) {
            //debug eiki added union_id
            UnionMemberInfo[] uniarr = (UnionMemberInfo[]) uniMem
                .findAll("select * from union_member_info where family_id="
                    + uniMem.getFamilyId() + " and union_id=" + this.iUnionId);
            Vector vector = new Vector();
            for (int k = 0; k < uniarr.length; k++) {
              if (eMember != null && uniarr[k].getMemberID() != eMember.getID())
                  vector.addElement(((MemberHome) IDOLookup
                      .getHomeLegacy(Member.class)).findByPrimaryKey(uniarr[k]
                      .getMemberID()));
            }

            Table familyInnerTable = new Table(1, vector.size());
            for (int i = 0; i < vector.size(); i++) {
              familyInnerTable.add(new Link(((Member) vector.elementAt(i))
                  .getName(), modinfo.getRequestURI() + "?member_id="
                  + ((Member) vector.elementAt(i)).getID()), 1, i + 1);
            }

            familyTable.add(familyInnerTable);
          }
        }
        return familyTable;
      }

      public BorderTable getUnionsTable() throws SQLException {
        BorderTable bTable = new BorderTable();
        eUMIs = EntityFinder.findAllByColumn((UnionMemberInfo) IDOLookup
            .instanciateEntity(UnionMemberInfo.class), "member_id", iMemberId);
        if (eUMIs != null) {
          int len = eUMIs.size();
          Table T = new Table(3, len);
          T.setColumnAlignment(1, "left");
          T.setColumnAlignment(2, "center");
          T.setColumnAlignment(3, "right");
          T.setWidth("100%");
          //T.add(headerText("Klúbbur"),1,1);
          //T.add(headerText("Aðild"),2,1);
          UnionMemberInfo eUmi;
          Union eUni;
          int iUnId;
          for (int i = 0; i < len; i++) {
            eUmi = (UnionMemberInfo) eUMIs.get(i);
            iUnId = eUmi.getUnionID();
            eUni = GolfCacher.getCachedUnion(iUnId);
            Text tAbbrevation = headerText(eUni.getAbbrevation());
            Text tStatus = headerText(eUmi.getMemberStatus());
            Text tType = headerText(mbsShipMap(eUmi.getMembershipType()));
            //Text id = bodyText(eUmi.getID());
            if (iUnId == this.iUnionId) {
              tAbbrevation.setFontColor("FF0000");
              tType.setFontColor("FF0000");
              tStatus.setFontColor("FF0000");
            }
            T.add(tAbbrevation, 1, i + 1);
            T.add(tStatus, 2, i + 1);
            T.add(tType, 3, i + 1);
            //T.add(id,2,i+1);
          }
          bTable.add(T);
        }
        return bTable;
      }

      public BorderTable getStatusTable() {
        Table T = new Table();
        int row = 1;
        T.setColumnAlignment(1, "left");
        T.setColumnAlignment(2, "right");
        T.add(iwrb.getLocalizedString("member.status"), 1, 1);

        T.mergeCells(1, row, 2, row);
        row++;
        T.add("A :", 1, row);
        T.add(iwrb.getLocalizedString("member.active", "Virkur"), 2, row++);
        T.add("I :", 1, row);
        T.add(iwrb.getLocalizedString("member.inactive", "Óvirkur"), 2, row++);
        T.add("W :", 1, row);
        T.add(iwrb.getLocalizedString("member.waiting", "Í bið"), 2, row++);
        T.add("Q :", 1, row);
        T.add(iwrb.getLocalizedString("member.retired", "Hættur"), 2, row++);
        T.add("D :", 1, row);
        T.add(iwrb.getLocalizedString("member.deceased", "Látinn"), 2, row++);
        BorderTable bTable = new BorderTable();
        bTable.add(T);
        return bTable;
      }

      public PresentationObject getGroupLinkTable() throws SQLException {
        //Union uni = new Union(iUnionId);
        //GroupMemberInsertWindow group = new GroupMemberInsertWindow(eMember,
        // uni, false);
        //Link linkInsertGroup = new Link(iwrb.getImage("buttons/find.gif"),
        // group);
        //GroupMemberRemoveWindow groupRem = new
        // GroupMemberRemoveWindow(eMember, uni, false);
        //Link linkRemoveGroup = new
        // Link(iwrb.getImage("buttons/detach.gif"),groupRem);
        //Table T = new Table(2, 1);
        //T.add(linkInsertGroup, 1, 1);
        //T.add(linkRemoveGroup, 2, 1);
        Link link = GroupMemberInsertWindow.getLink(iwrb
            .getImage("buttons/find.gif"), eMember.getID(), iUnionId, 3);
        link.setWindowToOpen(GroupMemberInsertWindow.class);
        Table T = new Table(1, 1);
        T.add(link, 1, 1);
        return T;
      }

      public PresentationObject getFamilyLinkTable() throws SQLException,
          FinderException {
        Table T = new Table(2, 1);
        FamilyInsertWindow findFam = new FamilyInsertWindow(iMemberId, iUnionId);
        Link linkFindFamily = new Link(iwrb.getImage("buttons/find.gif"),
            findFam);
        FamilyDisconnecterWindow winNewFamily = new FamilyDisconnecterWindow(
            iMemberId, iUnionId);
        Link linkNewFamily = new Link(iwrb.getImage("buttons/detach.gif"),
            winNewFamily);
        T.add(linkNewFamily, 1, 1);
        T.add(linkFindFamily, 2, 1);
        return T;
      }

      private String mbsShipMap(String type) {
        if ("main".equalsIgnoreCase(type))
          return iwrb.getLocalizedString("member.main_club", "Aðalkl.");
        else if ("sub".equalsIgnoreCase(type))
          return iwrb.getLocalizedString("member.extra_club", "Aukakl.");
        else
          return "";
      }

      public Text bodyText(String s) {
        Text T = new Text();
        if (s != null) {
          T = new Text(s);
          T.setFontColor(this.DarkColor);
          T.setFontSize(this.bodyFontSize);
          T.setBold();
        }
        return T;
      }

      public Text bodyText(int i) {
        return bodyText(String.valueOf(i));
      }

      public Text headerText(String s) {
        Text T = new Text();
        if (s != null) {
          T = new Text(s);
          T.setFontColor(this.DarkColor);
          T.setFontSize(this.fontSize);
          T.setBold();
        }
        return T;
      }

      public Text headerText(int i) {
        return headerText(String.valueOf(i));
      }

      private Group[] joinArrays(Group[] p1, Group[] p2) {
        Vector v = new Vector();

        for (int i = 0; i < p1.length; i++) {
          v.add(p1[i]);
        }
        for (int i = 0; i < p2.length; i++) {
          v.add(p2[i]);
        }

        Group[] returnEntity = new Group[v.size()];

        for (int i = 0; i < v.size(); i++) {
          returnEntity[i] = (Group) v.get(i);
        }

        return returnEntity;
      }

    }

    public class AddressInsert extends EntityInsert {

      private Address address;

      private TextInput addressInput;

      private DropdownMenu zipDrop;

      private DropdownMenu countryDrop;

      private String addressName = "AddressInsert_address";

      private String zipName = "AddressInsert_zipcode";

      private String countryName = "AddressInsert_country";

      private String addressValue = null;

      private String zipValue = null;

      private String countryValue = null;

      private boolean isUpdate = false;

      private void init() {
        setStyle(addressInput);
        setStyle(countryDrop);
        setStyle(zipDrop);
      }

      public AddressInsert(IWContext modinfo) {
        super(modinfo);
        isUpdate = false;
        address = (Address) IDOLookup.createLegacy(Address.class);
        addressInput = new TextInput(addressName);
        countryDrop = countryDropDown(countryName, "1");
        zipDrop = zipDropDown(zipName, "2");
        init();
        //setVariables();
      }

      public AddressInsert(IWContext modinfo, String addressInputName,
          String countryDropDownName, String zipCodeDropDownName) {
        super(modinfo);
        isUpdate = false;
        address = (Address) IDOLookup.createLegacy(Address.class);
        addressName = addressInputName;
        countryName = countryDropDownName;
        zipName = zipCodeDropDownName;
        addressInput = new TextInput(addressName);
        countryDrop = countryDropDown(countryName, "1");
        zipDrop = zipDropDown(zipName, "2");
        init();
        //setVariables();
      }

      public AddressInsert(IWContext modinfo, int addressId)
          throws java.sql.SQLException, FinderException {
        super(modinfo, addressId);
        isUpdate = true;
        address = ((AddressHome) IDOLookup.getHomeLegacy(Address.class))
            .findByPrimaryKey(addressId);
        if (address.getStreet() != null)
          addressInput = new TextInput(addressName, address.getStreet());
        else
          addressInput = new TextInput(addressName, "");
        if (address.getCountryId() != -1)
          countryDrop = countryDropDown(countryName, String.valueOf(address
              .getCountryId()));
        else
          countryDrop = countryDropDown(countryName, "2");
        zipDrop = zipDropDown(zipName, String.valueOf(address.getZipcodeId()));
        init();
        //setVariables();
      }

      public AddressInsert(IWContext modinfo, int addressId,
          String addressInputName, String countryDropDownName,
          String zipCodeDropDownName) throws java.sql.SQLException,
          FinderException {
        super(modinfo, addressId);
        isUpdate = true;
        address = ((AddressHome) IDOLookup.getHomeLegacy(Address.class))
            .findByPrimaryKey(addressId);
        addressName = addressInputName;
        countryName = countryDropDownName;
        zipName = zipCodeDropDownName;
        if (address.getStreet() != null)
          addressInput = new TextInput(addressName, address.getStreet());
        else
          addressInput = new TextInput(addressName, "");
        if (address.getCountryId() != -1)
          countryDrop = countryDropDown(countryName, String.valueOf(address
              .getCountryId()));
        else
          countryDrop = countryDropDown(countryName, "2");
        zipDrop = zipDropDown(zipName, String.valueOf(address.getZipcodeId()));
        init();
        //setVariables();
      }

      private DropdownMenu countryDropDown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        try {
          Country country = ((CountryHome) IDOLookup
              .getHomeLegacy(Country.class)).findByPrimaryKey(COUNTRY_ID);
          //Country[] countryArr = (Country[]) country.findAll();
          Country[] countryArr = { country};
          for (int i = 0; i < countryArr.length; i++) {
            drp.addMenuElement(countryArr[i].getID(), countryArr[i].getName());
          }
          drp.setSelectedElement(selected);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return drp;
      }

      private DropdownMenu zipDropDown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        ZipCode zip = (ZipCode) IDOLookup.instanciateEntity(ZipCode.class);
        try {
          //ZipCode [] zipArr = (ZipCode[]) zip.findAllOrdered("code");
          ZipCode[] zipArr = (ZipCode[]) zip
              .findAll("select * from zipcode where country_id=" + COUNTRY_ID
                  + " order by code");

          for (int i = 0; i < zipArr.length; i++) {
            drp.addMenuElement(zipArr[i].getID(), zipArr[i].getCode() + " "
                + zipArr[i].getCity());
          }
          drp.setSelectedElement(selected);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return drp;
      }

      public void setVariables() {
        addressValue = getValue(addressName);
        zipValue = getValue(zipName);
        countryValue = getValue(countryName);

        addressInput.keepStatusOnAction();
        zipDrop.keepStatusOnAction();
        countryDrop.keepStatusOnAction();

        if (addressValue != null) {
          address.setStreet(addressValue);
        }
        if (zipValue != null) {
          address.setZipcodeId(new Integer(zipValue));
        }
        if (countryValue != null) {
          address.setCountryId(new Integer(countryValue));
        }
      }

      public TextInput getInputAddress() {
        return addressInput;
      }

      public DropdownMenu getDropCountry() {
        return countryDrop;
      }

      public DropdownMenu getDropZipcode() {
        return zipDrop;
      }

      public boolean areSomeFieldsEmpty() {
        return (isEmpty(addressName) || isEmpty(zipName) || isEmpty(countryName));
      }

      public boolean areNeetedFieldsEmpty() {
        return isEmpty(addressName);
      }

      public Vector getEmptyFields() {
        Vector vec = new Vector();

        if (isInvalid(addressValue)) {
          vec.addElement("Heimilisfang");
        }
        if (isInvalid(countryValue)) {
          vec.addElement("Land");
        }
        if (isInvalid(zipValue)) {
          vec.addElement("Póstnúmer");
        }

        return vec;
      }

      public Vector getNeetedEmptyFields() {
        setVariables();
        Vector vec = new Vector();

        if (isInvalid(addressValue)) {
          vec.addElement("Heimilisfang");
        }

        return vec;
      }

      public Address getAddress() {
        return this.address;
      }

      public void store() throws SQLException, IOException {

        PrintWriter out = modinfo.getResponse().getWriter();
        setVariables();
        if (addressValue == null) { return; }

        if (isUpdate)
          address.update();
        else
          address.insert();

      }

      /** If to link the address to a member */
      public void store(Member member) throws SQLException, IOException {

        PrintWriter out = modinfo.getResponse().getWriter();
        setVariables();
        if (addressValue == null) { return; }

        if (isUpdate) {
          if ((address.getStreet() != null)
              && (!address.getStreet().equals("")))
            address.update();
          else {
            address.removeFrom(member);
            address.delete();
          }
        } else if ((address.getStreet() != null)
            && (!address.getStreet().equals(""))) {
          address.insert();
          address.addTo(member);
        }

      }

    }

    public class MemberInsert extends EntityInsert {

      private Member member;

      private TextInput inputSocial;

      private TextInput inputName;

      private TextInput inputEmail;

      private TextInput inputJob;

      private TextInput inputWorkPlace;

      private DropdownMenu dropGender;

      private final String inputSocialName = "MemberInsert_sociasecuritynumber";

      private final String inputNameName = "MemberInsert_name";

      private final String inputEmailName = "MemberInsert_email";

      private final String inputJobName = "MemberInsert_job";

      private final String inputWorkPlaceName = "MemberInsert_workplace";

      private final String dropGenderName = "MemberInsert_gender";

      private String inputSocialValue;

      private String inputNameValue;

      private String inputEmailValue;

      private String inputJobValue;

      private String inputWorkPlaceValue;

      private String dropGenderValue;

      private String headerText = "Meðlimur";

      public boolean debug = true;

      public MemberInsert(IWContext modinfo) throws java.sql.SQLException {
        super(modinfo);
        isUpdate = false;

        member = (Member) IDOLookup.createLegacy(Member.class);
        if (debug) {
          member.setDefaultValues();
          // member.setCardId(1);
        }
        inputSocial = new TextInput(inputSocialName);
        if (modinfo.isParameterSet("socialnumber"))
            inputSocial.setContent(modinfo.getParameter("socialnumber"));
        inputName = new TextInput(inputNameName);
        inputName.setAsNotEmpty(iwrb.getLocalizedString(
            "member.please_insert_name", "Please enter a name"));
        inputSocial.setAsNotEmpty(iwrb.getLocalizedString(
            "member.please_insert_ssn", "Please enter Social Security Number"));
        inputEmail = new TextInput(inputEmailName);
        inputJob = new TextInput(inputJobName);
        inputWorkPlace = new TextInput(inputWorkPlaceName);
        dropGender = genderDrop(dropGenderName, "M");
        init();
        //setVariables();
      }

      public MemberInsert(IWContext modinfo, int memberId) throws SQLException,
          FinderException {
        super(modinfo, memberId);
        isUpdate = true;
        member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class))
            .findByPrimaryKey(memberId);

        if (member.getSocialSecurityNumber() != null)
          inputSocial = new TextInput(inputSocialName, member
              .getSocialSecurityNumber());
        else
          inputSocial = new TextInput(inputSocialName);
        if (member.getName() != null)
          inputName = new TextInput(inputNameName, member.getName());
        else
          inputName = new TextInput(inputNameName);
        if (member.getEmail() != null)
          inputEmail = new TextInput(inputEmailName, member.getEmail());
        else
          inputEmail = new TextInput(inputEmailName);

        if (member.getJob() != null)
          inputJob = new TextInput(inputJobName, member.getJob());
        else
          inputJob = new TextInput(inputJobName);
        if (member.getWorkPlace() != null)
          inputWorkPlace = new TextInput(inputWorkPlaceName, member
              .getWorkPlace());
        else
          inputWorkPlace = new TextInput(inputWorkPlaceName);

        if (member.getGender() != null)
          dropGender = genderDrop(dropGenderName, member.getGender());
        else
          dropGender = genderDrop(dropGenderName, "M");

        inputName.setAsNotEmpty("Vinsamlegast settu nafn");
        init();
        //setVariables();
      }

      private void init() {
        setStyle(inputSocial);
        setStyle(inputName);
        setStyle(inputName);
        setStyle(inputSocial);
        setStyle(inputEmail);
        setStyle(inputJob);
        setStyle(inputWorkPlace);
        setStyle(dropGender);
      }

      public TextInput getInputSocialSecurityNumber() {
        return this.inputSocial;
      }

      public TextInput getInputMemberName() {
        return this.inputName;
      }

      public TextInput getInputEmail() {
        return this.inputEmail;
      }

      public TextInput getInputJob() {
        return this.inputJob;
      }

      public TextInput getInputWorkPlace() {
        return this.inputWorkPlace;
      }

      public DropdownMenu getDropdownGender() {
        return dropGender;
      }

      public Vector getEmptyFields() {
        Vector vec = new Vector();

        if (isInvalid(inputSocialValue)) {
          vec.addElement("Kennitölu");
        } else if (!SocialSecurityNumber
            .isValidIcelandicSocialSecurityNumber(inputSocialValue)) {
          vec.addElement(iwrb.getLocalizedString("member.wrong_ssn",
              "Invalid social security number"));
        }
        if (isInvalid(inputNameValue)) {
          vec.addElement("Nafn");
        } else {
          Name name = new Name(inputNameValue);
          if (name.getLastName().equals("")) vec.addElement("Eftirnafn");
        }
        if (isInvalid(inputEmailValue)) {
          vec.addElement("Netfang");
        }
        if (isInvalid(inputJobValue)) {
          vec.addElement("Starfsheiti");
        }
        if (isInvalid(inputWorkPlaceValue)) {
          vec.addElement("Vinnustaður");
        }
        if (isInvalid(dropGenderValue)) {
          vec.addElement("Kyn");
        }
        return vec;
      }

      public Vector getNeetedEmptyFields() {
        setVariables();
        Vector vec = new Vector();

        if (isInvalid(inputSocialValue)) {
          vec.addElement("Kennitölu");
        } else if (!SocialSecurityNumber
            .isValidIcelandicSocialSecurityNumber(inputSocialValue)) {
          vec.addElement(iwrb.getLocalizedString("member.wrong_ssn",
              "Invalid social security number"));
        }
        if (isInvalid(inputNameValue)) {
          vec.addElement("Nafn");
        }
        if (isInvalid(dropGenderValue)) {
          vec.addElement("Kyn");
        }

        return vec;
      }

      public boolean areSomeFieldsEmpty() {
        return (getEmptyFields().size() > 0);
      }

      public Member getMember() {
        return this.member;
      }

      public boolean areNeetedFieldsEmpty() {
        return (getNeetedEmptyFields().size() > 0);
      }

      //precondition Have to call getNetedEmptyFields() !!!
      public void store() throws java.io.IOException, java.sql.SQLException {
        if (isUpdate())
          member.update();
        else
          member.insert();
      }

      public BorderTable getInputTable() {
        BorderTable hTable = new BorderTable();
        //hTable.setHeaderText(headerText);
        Table table = new Table(2, 6);
        hTable.add(table);

        table.add(formatText(iwrb.getLocalizedString("member.name", "Nafn")),
            1, 1);
        table.add(formatText(iwrb.getLocalizedString(
            "member.social_security_number", "SSN")), 1, 2);
        table.add(formatText(iwrb.getLocalizedString("member.gender", "Kyn")),
            1, 3);
        table.add(
            formatText(iwrb.getLocalizedString("member.email", "Netfang")), 1,
            4);
        table.add(formatText(iwrb.getLocalizedString("member.work_title",
            "Starfsheiti")), 1, 5);
        table.add(formatText(iwrb.getLocalizedString("member.workplace",
            "Vinnustaður")), 1, 6);
        table.add(getInputMemberName(), 2, 1);
        table.add(getInputSocialSecurityNumber(), 2, 2);
        table.add(getDropdownGender(), 2, 3);
        table.add(getInputEmail(), 2, 4);
        table.add(getInputJob(), 2, 5);
        table.add(getInputWorkPlace(), 2, 6);

        return hTable;
      }

      public void showInputForm() throws java.io.IOException,
          java.sql.SQLException {
        PrintWriter out = modinfo.getResponse().getWriter();
        try {

          Table table = new Table(2, 7);
          //table.setBorder(1);
          Form form = new Form();
          form.setAction(modinfo.getRequest().getRequestURI() + "?cmd=submit");
          table.add("Nafn", 1, 1);
          table.add(iwrb.getLocalizedString("member.social_security_number",
              "SSN"), 1, 2);
          table.add("Kyn", 1, 3);
          table.add("Netfang", 1, 4);
          table.add("Starfsheit", 1, 5);
          table.add("Vinnustaður", 1, 6);
          table.add(getInputMemberName(), 2, 1);
          table.add(getInputSocialSecurityNumber(), 2, 2);
          table.add(getDropdownGender(), 2, 3);
          table.add(getInputEmail(), 2, 4);
          table.add(getInputJob(), 2, 5);
          table.add(getInputWorkPlace(), 2, 6);
          table.add(new SubmitButton(), 2, 7);

          if (getValue("cmd") != null
              && getValue("cmd").equalsIgnoreCase("submit")) {
            store();
          }
          form.add(table);
          add(form);

        } catch (SQLException e) {
          out.println(e.getMessage());
          e.printStackTrace(out);
        } catch (Exception er) {
          out.println(er.getMessage());
          er.printStackTrace(out);
        }
      }

      public void setVariables() {
        inputSocialValue = getValue(inputSocialName);
        inputNameValue = getValue(inputNameName);
        inputEmailValue = getValue(inputEmailName);
        inputJobValue = getValue(inputJobName);
        inputWorkPlaceValue = getValue(inputWorkPlaceName);
        dropGenderValue = getValue(dropGenderName);
        setEntity();
      }

      private void setEntity() {

        if (!isInvalid(inputSocialValue)) {
          if (SocialSecurityNumber
              .isValidIcelandicSocialSecurityNumber(inputSocialValue)) {
            member.setSocialSecurityNumber(inputSocialValue);
            member.setDateOfBirth(SocialSecurityNumber
                .getDateFromSocialSecurityNumber(inputSocialValue));
          }
        }
        if (!isInvalid(inputNameValue)) {
          Name name = new Name(inputNameValue);
          member.setFirstName(name.getFirstName());
          member.setMiddleName(name.getMiddleName());
          member.setLastName(name.getLastName());
        }
        if (inputEmailValue != null) {
          member.setEmail(inputEmailValue);
        }
        if (inputJobValue != null) {
          member.setJob(inputJobValue);
        }
        if (inputWorkPlaceValue != null) {
          member.setWorkPlace(inputWorkPlaceValue);
        }
        if (dropGenderValue != null) {
          member.setGender(dropGenderValue);
        }
      }

      public DropdownMenu genderDrop(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        drp.addMenuElement("M", iwrb.getLocalizedString("member.male", "KK"));
        drp
            .addMenuElement("F", iwrb
                .getLocalizedString("member.female", "KVK"));
        drp.setSelectedElement(selected);
        return drp;
      }
    }

    public class UnionMemberInfoInsert extends EntityInsert {

      private UnionMemberInfo member;

      private Member eMember = null;

      private TextInput inputMemberNumber;

      private DropdownMenu dropMemberShipType;

      private DropdownMenu dropFamilyStatus;

      private DropdownMenu dropNumberOfPaiments;

      private TextInput inputLocker;

      private DateInput dateInputFirstPayday;

      private TextArea areaComment;

      private DropdownMenu dropVisible;

      private DropdownMenu dropPaymentTypes;

      private DropdownMenu dropCatalogue;

      private DropdownMenu dropActive;

      private HiddenInput hmbstype;

      private boolean mbshpChange = false;

      private final String inputMemberNumberName = "UnionMemberInfoInsert_member_number";

      private final String dropMemberShipTypeName = "UnionMemberInfoInsert_mbshiptype";

      private final String dropFamilyStatusName = "UnionMemberInfoInsert_family_status";

      private final String dropNumberOfPaimentsName = "UnionMemberInfoInsert_numpayments";

      private final String inputLockerName = "UnionMemberInfoInsert_locker";

      private final String dateInputFirstPaydayName = "UnionMemberInfoInsert_firstpayday";

      private final String areaCommentName = "UnionMemberInfoInsert_comment";

      private final String dropVisibleName = "UnionMemberInfoInsert_isvisible";

      private final String dropPaymentTypesName = "UnionMemberInfoInsert_paymenttypes";

      private final String dropCatalogueName = "UnionMemberInfoInsert_catalogue";

      private final String dropActiveName = "UnionMemberInfoInsert_isactive";

      private String inputMemberNumberValue;

      private String dropMemberShipTypeValue;

      private String dropFamilyStatusValue;

      private String dropNumberOfPaimentsValue;

      private String inputLockerValue;

      private Date dateInputFirstPaydayValue;

      private String areaCommentValue;

      private String dropVisibleValue;

      private String dropPaymentTypesValue;

      private String dropCatalogueValue;

      private String dropActiveValue;

      public UnionMemberInfoInsert(IWContext modinfo)
          throws java.sql.SQLException {
        super(modinfo);
        isUpdate = false;
        GregorianCalendar cal = new GregorianCalendar();
        dateInputFirstPayday = new DateInput(dateInputFirstPaydayName, true);
        dateInputFirstPayday.setDay(1);
        dateInputFirstPayday.setMonth(2);
        //dateInputFirstPayday.setYearRange(cal.get(cal.YEAR),
        // cal.get(cal.YEAR));
        dateInputFirstPayday.setYear(cal.get(cal.YEAR));

        member = (UnionMemberInfo) IDOLookup
            .createLegacy(UnionMemberInfo.class);
        member.setRegistrationDate(new Date(System.currentTimeMillis()));

        inputMemberNumber = new TextInput(inputMemberNumberName);
        inputMemberNumber.setLength(4);
        inputLocker = new TextInput(inputLockerName);
        inputLocker.setSize(8);
        areaComment = new TextArea(areaCommentName);
        dropMemberShipType = mbshiptypeDropdown(dropMemberShipTypeName, "main");
        hmbstype = new HiddenInput("hmbstype", "");
        dropVisible = visibleDropdown(dropVisibleName, "Y");
        dropPaymentTypes = paymentTypeDrop(dropPaymentTypesName, "1");
        dropCatalogue = catalogueDrop(dropCatalogueName, "0");
        dropActive = activeDropdown(dropActiveName, "A");
        dropNumberOfPaiments = numberOfPaymentDrop(dropNumberOfPaimentsName,
            "0");
        dropFamilyStatus = familyStatusDropdown(dropFamilyStatusName, "head");
        init();
        //setVariables();
      }

      public UnionMemberInfoInsert(IWContext modinfo, int unionMemInfoId,
          int memberID, int unionID) throws SQLException, FinderException {
        super(modinfo, unionMemInfoId);
        isUpdate = true;
        GregorianCalendar cal = new GregorianCalendar();
        member = ((UnionMemberInfoHome) IDOLookup
            .getHomeLegacy(UnionMemberInfo.class))
            .findByPrimaryKey(unionMemInfoId);
        eMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class))
            .findByPrimaryKey(memberID);
        setMemberId(memberID);
        setUnionId(unionID);
        dateInputFirstPayday = new DateInput(dateInputFirstPaydayName, true);
        //dateInputFirstPayday.setYearRange(cal.get(cal.YEAR),
        // cal.get(cal.YEAR));
        dateInputFirstPayday.setYear(cal.get(cal.YEAR));

        if (member.getMemberStatus() != null) {
          //debug
          dropActive = activeDropdown(dropActiveName, member.getMemberStatus());
          //dropActive = activeDropdown(dropActiveName, "A");
        } else
          dropActive = activeDropdown(dropActiveName, "A");
        if (member.getFamilyStatus() != null)
          dropFamilyStatus = familyStatusDropdown(dropFamilyStatusName, member
              .getFamilyStatus());
        else
          dropFamilyStatus = familyStatusDropdown(dropFamilyStatusName, "head");
        if (member.getPreferredInstallmentNr() != -1)
          dropNumberOfPaiments = numberOfPaymentDrop(dropNumberOfPaimentsName,
              String.valueOf(member.getPreferredInstallmentNr()));
        else
          dropNumberOfPaiments = numberOfPaymentDrop(dropNumberOfPaimentsName,
              "0");
        if (member.getLockerNumber() != null)
          inputLocker = new TextInput(inputLockerName, member.getLockerNumber());
        else
          inputLocker = new TextInput(inputLockerName);
        if (member.getComment() != null)
          areaComment = new TextArea(areaCommentName, member.getComment());
        else
          areaComment = new TextArea(areaCommentName);

        if (member.getVisible())
          dropVisible = visibleDropdown(dropVisibleName, "Y");
        else
          dropVisible = visibleDropdown(dropVisibleName, "N");
        if (member.getPaymentTypeID() != -1)
          dropPaymentTypes = paymentTypeDrop(dropPaymentTypesName, String
              .valueOf(member.getPaymentTypeID()));
        else
          dropPaymentTypes = paymentTypeDrop(dropPaymentTypesName, "1");
        if (member.getFirstInstallmentDate() != null) {
          dateInputFirstPayday.setDate(member.getFirstInstallmentDate());
        } else {
          dateInputFirstPayday.setDay(1);
          dateInputFirstPayday.setMonth(2);
        }
        if (member.getMembershipType() != null) {
          dropMemberShipType = mbshiptypeDropdown(dropMemberShipTypeName,
              member.getMembershipType());
          hmbstype = new HiddenInput("hmbstype", member.getMembershipType());
        } else {
          dropMemberShipType = mbshiptypeDropdown(dropMemberShipTypeName,
              "main");
          hmbstype = new HiddenInput("hmbstype", "");
        }
        String mn = String.valueOf(member.getMemberNumber());
        inputMemberNumber = new TextInput(inputMemberNumberName, mn);
        inputMemberNumber.setLength(4);
        inputLocker.setSize(8);
        dropCatalogue = catalogueDrop(dropCatalogueName, String.valueOf(member
            .getPriceCatalogueID()));
        init();
      }

      private void init() {
        dateInputFirstPayday.setStyle(this.styleAttribute);
        setStyle(inputMemberNumber);
        setStyle(inputLocker);
        setStyle(areaComment);
        setStyle(dropMemberShipType);
        setStyle(dropPaymentTypes);
        setStyle(dropVisible);
        setStyle(dropCatalogue);
        setStyle(dropActive);
        setStyle(dropNumberOfPaiments);
        setStyle(dropFamilyStatus);
      }

      public TextInput getInputMemberNumber() {
        return inputMemberNumber;
      }

      public UnionMemberInfo getUnionMemberInfo() {
        return member;
      }

      public void setUnionId(int id) {
        this.member.setUnionID(id);
      }

      public void setMemberId(int id) {
        this.member.setMemberID(id);
      }

      public void setCardId(int id) {
        this.member.setCardId(id);
      }

      //debug added by eiki
      public void setMemberStatus(String status) {
        this.member.setMemberStatus(status);
      }

      //debug added by eiki
      public void setMembershipType(String type) {
        this.member.setMembershipType(type);
      }

      public DropdownMenu getDropdownNumberOfPayments() {
        return this.dropNumberOfPaiments;
      }

      public DropdownMenu getDropdownFamilyStatus() {
        return this.dropFamilyStatus;
      }

      public TextInput getInputLocker() {
        return this.inputLocker;
      }

      public DateInput getDateInputFirstPaymentDate() {
        return dateInputFirstPayday;
      }

      public DropdownMenu getDropVisible() {
        return this.dropVisible;
      }

      public DropdownMenu getDropCatalogue() {
        return dropCatalogue;
      }

      public DropdownMenu getDropMemberStatus() {
        return dropActive;
      }

      public DropdownMenu getDropMemberShipType() {
        return dropMemberShipType;
      }

      public DropdownMenu getDropdownPaymentType() {
        return dropPaymentTypes;
      }

      public TextArea getAreaComment() {
        return areaComment;
      }

      public Vector getEmptyFields() {
        Vector vec = new Vector();

        if (isInvalid(inputLockerValue)) {
          vec.addElement("Skápanúmer");
        }
        if (!isDateInputValid(dateInputFirstPaydayName)) {
          vec.addElement("Dagsetning fyrstu afborgunar");
        }

        return vec;
      }

      public Vector getNeetedEmptyFields() {
        return new Vector();
      }

      public boolean areSomeFieldsEmpty() {
        return (getEmptyFields().size() > 0);
      }

      public boolean areNeetedFieldsEmpty() {
        return (getNeetedEmptyFields().size() > 0);
      }

      public void store() throws java.io.IOException, java.sql.SQLException {
        setVariables();

        if (isUpdate())
          member.update();
        else {
          if (member.getUnionID() == 0 || member.getUnionID() == -1
              || member.getMemberID() == 0 || member.getMemberID() == -1)
              throw new SQLException(
                  "Vantar UNION_ID OG/EÐA MEMBER_ID GILDI A KALLINN");
          int nextNumber = member.getMaxColumnValue("member_number",
              "union_id", String.valueOf(member.getUnionID()));
          nextNumber++;
          member.setMemberNumber(nextNumber);
          member.insert();
        }
      }

      public BorderTable getInputTable() {

        Table table = null;

        Image obj = new Image("/pics/formtakks/edit.gif");
        Window pcWindow = new Window("Pricecatalogue", 400, 400,
            "/tarif/pcmake.jsp");
        pcWindow.setScrollbar(false);
        pcWindow.setResizable(true);
        pcWindow.setParentToReload();
        Link sidan = new Link(obj, pcWindow);
        sidan.addParameter("catal_action", "main");
        sidan.addParameter("union_id", String.valueOf(member.getUnionID()));

        Link link = Tariffer.getExtraCatalogueLink("Skrá Gjaldflokk", String
            .valueOf(member.getUnionID()));
        link.setObject(new Image("/pics/formtakks/edit.gif"));

        if (isUpdate()) {
          table = new Table(2, 8);
          table.add(formatText(iwrb
              .getLocalizedString("member.number", "Númer")), 1, 1);
          table.add(formatText(iwrb
              .getLocalizedString("member.status", "Staða")), 1, 2);
          table.add(formatText(iwrb.getLocalizedString("member.payments",
              "Greiðslur")), 1, 3);
          table.add(formatText(iwrb.getLocalizedString("member.first_payment",
              "1.Gjaldd.")), 1, 4);
          //table.add(formatText("Greiðslumáti"), 1, 5);
          table.add(formatText(iwrb.getLocalizedString("member.web", "Vefur")),
              1, 5);
          table.add(formatText(iwrb.getLocalizedString("member.locker",
              "Skápur")), 1, 6);
          table.add(formatText(iwrb.getLocalizedString("member.family_status",
              "Fj.staða")), 1, 7);
          table.add(formatText(iwrb.getLocalizedString("member.special_price",
              "Sérgjald")), 1, 8);

          table.add(getInputMemberNumber(), 2, 1);

          table.add(getDropMemberStatus(), 2, 2);
          table.add(getDropMemberShipType(), 2, 2);
          table.add(hmbstype, 2, 2);
          table.add(getDropdownNumberOfPayments(), 2, 3);
          table.add(getDropdownPaymentType(), 2, 3);
          table.add(getDateInputFirstPaymentDate(), 2, 4);

          table.add(getDropVisible(), 2, 5);
          table.add(getInputLocker(), 2, 6);
          table.add(getDropdownFamilyStatus(), 2, 7);
          table.add(getDropCatalogue(), 2, 8);
          //table.add(link, 2, 8);
          table.add(sidan, 2, 8);
        } else {
          table = new Table(2, 8);
          table.add(formatText(iwrb
              .getLocalizedString("member.status", "Staða")), 1, 2);
          table.add(formatText(iwrb.getLocalizedString("member.payments",
              "Greiðslur")), 1, 3);
          table.add(formatText(iwrb.getLocalizedString("member.first_payment",
              "1.Gjaldd.")), 1, 4);
          //table.add(formatText("Greiðslumáti"), 1, 4);
          table.add(formatText(iwrb.getLocalizedString("member.web", "Vefur")),
              1, 5);
          table.add(formatText(iwrb.getLocalizedString("member.locker",
              "Skápur")), 1, 6);
          table.add(formatText(iwrb.getLocalizedString("member.family_status",
              "Fj.staða")), 1, 7);
          table.add(formatText(iwrb.getLocalizedString("member.special_price",
              "Sérgjald")), 1, 8);

          table.add(getDropMemberStatus(), 2, 2);
          table.add(getDropMemberShipType(), 2, 2);
          table.add(hmbstype, 2, 2);
          table.add(getDropdownNumberOfPayments(), 2, 3);
          table.add(getDropdownPaymentType(), 2, 3);
          table.add(getDateInputFirstPaymentDate(), 2, 4);
          table.add(getDropVisible(), 2, 5);
          table.add(getInputLocker(), 2, 6);
          table.add(getDropdownFamilyStatus(), 2, 7);
          table.add(getDropCatalogue(), 2, 8);
          //table.add(link, 2, 8);
          table.add(sidan, 2, 8);
        }

        BorderTable hTable = new BorderTable();
        //hTable.setHeaderText("Félags upplýsingar");
        hTable.add(table);
        return hTable;
      }

      public void setVariables() {
        inputMemberNumberValue = getValue(inputMemberNumberName);
        dropMemberShipTypeValue = getValue(dropMemberShipTypeName);
        String hmbshp = getValue("hmbstype");
        mbshpChange = dropMemberShipTypeValue.equalsIgnoreCase(hmbshp) ? false
            : true;
        dropNumberOfPaimentsValue = getValue(dropNumberOfPaimentsName);
        inputLockerValue = getValue(inputLockerName);
        dateInputFirstPaydayValue = getDateFromInput(dateInputFirstPaydayName);
        areaCommentValue = getValue(areaCommentName);
        dropVisibleValue = getValue(dropVisibleName);
        dropPaymentTypesValue = getValue(dropPaymentTypesName);
        dropCatalogueValue = getValue(dropCatalogueName);
        dropActiveValue = getValue(dropActiveName);
        dropFamilyStatusValue = getValue(dropFamilyStatusName);
        setEntity();
      }

      private void setEntity() {
        if (!isInvalid(dropNumberOfPaimentsValue)) {
          if (isDigitOnly(dropNumberOfPaimentsValue))
              member.setPreferredInstallmentNr(new Integer(
                  dropNumberOfPaimentsValue));
        }
        if (inputMemberNumberValue != null) {
          try {
            int number = Integer.parseInt(inputMemberNumberValue);
            member.setMemberNumber(number);
          } catch (NumberFormatException ex) {
            System.err.println("membernumber not integer:");
            ex.printStackTrace();
          }
        }
        if (inputLockerValue != null) {
          member.setLockerNumber(inputLockerValue);
        }
        if (dateInputFirstPaydayValue != null) {
          member.setFirstInstallmentDate(dateInputFirstPaydayValue);
        }
        /*
         * if (! isInvalid(areaCommentValue)) {
         * member.setComment(areaCommentValue); }
         */
        if (!isInvalid(dropVisibleValue)) {
          if (dropVisibleValue.equalsIgnoreCase("Y"))
            member.setVisible(true);
          else
            member.setVisible(false);
        }
        if (!isInvalid(dropPaymentTypesValue)) {
          member.setPaymentTypeID(new Integer(dropPaymentTypesValue));
        }
        if (!isInvalid(dropCatalogueValue)) {
          member.setPriceCatalogueID(Integer.parseInt(dropCatalogueValue));
        }
        member.setMembershipType(dropMemberShipTypeValue);
        if (mbshpChange && eMember != null) {
          try {
            eMember.setMainUnion(member.getUnionID());
          } catch (SQLException ex) {
            System.err.println("club changes fail");
          }
        }
        member.setMemberStatus(dropActiveValue);
        member.setFamilyStatus(dropFamilyStatusValue);

      }

      public DropdownMenu paymentTypeDrop(String name, String selected)
          throws java.sql.SQLException {
        DropdownMenu drp = new DropdownMenu(name);
        PaymentType type = (PaymentType) IDOLookup
            .instanciateEntity(PaymentType.class);
        PaymentType[] types = (PaymentType[]) type.findAll();

        for (int i = 0; i < types.length; i++) {
          String sname = types[i].getName();

          if (sname.length() > 5) sname = sname.substring(0, 5);
          drp.addMenuElement(types[i].getID(), sname);
        }
        drp.setSelectedElement(selected);
        return drp;
      }

      public DropdownMenu catalogueDrop(String name, String selected)
          throws java.sql.SQLException {
        if (selected.equals("-1")) selected = "0";
        DropdownMenu drp = new DropdownMenu(name);
        drp.addMenuElement(0, "-----");
        List list = Tariffer.getExtraCatalogList(String.valueOf(member
            .getUnionID()));
        if (list != null) {
          ListIterator iter = list.listIterator();
          PriceCatalogue cate = null;
          String sname;
          while (iter.hasNext()) {
            cate = (PriceCatalogue) iter.next();
            sname = cate.getName();
            if (sname.length() > 15) sname = sname.substring(0, 15) + "..";
            drp.addMenuElement(cate.getID(), sname);
          }
          drp.setSelectedElement(selected);
        }
        return drp;
      }

      public DropdownMenu numberOfPaymentDrop(String name, String selected)
          throws java.sql.SQLException {

        DropdownMenu drp = new DropdownMenu(name);
        drp.addMenuElement(0, "--");

        for (int i = 1; i < 13; i++) {
          drp.addMenuElement(i, String.valueOf(i));
        }
        drp.setSelectedElement(selected);
        return drp;
      }

      public DropdownMenu mbshiptypeDropdown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        drp.addMenuElement("main", iwrb.getLocalizedString("member.main_club",
            "Aðalkl."));
        drp.addMenuElement("sub", iwrb.getLocalizedString("member.extra_club",
            "Aukakl."));
        drp.setSelectedElement(selected);
        return drp;
      }

      public DropdownMenu activeDropdown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        drp.addMenuElement("A", iwrb.getLocalizedString("member.active",
            "Virkur"));
        drp.addMenuElement("I", iwrb.getLocalizedString("member.inactive",
            "Óvirkur"));
        drp.addMenuElement("W", iwrb.getLocalizedString("member.waiting",
            "Í bið"));
        drp.addMenuElement("Q", iwrb.getLocalizedString("member.retired",
            "Hættur"));
        drp.addMenuElement("D", iwrb.getLocalizedString("member.deceased",
            "Látinn"));
        drp.setSelectedElement(selected);
        return drp;
      }

      public DropdownMenu familyStatusDropdown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        drp.addMenuElement("head", iwrb.getLocalizedString("member.head",
            "Höfuð"));
        drp.addMenuElement("partner", iwrb.getLocalizedString("member.spouse",
            "Maki"));
        drp.addMenuElement("child", iwrb.getLocalizedString("member.child",
            "Barn"));
        drp.setSelectedElement(selected);
        return drp;
      }

      public DropdownMenu visibleDropdown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        drp.addMenuElement("Y", iwrb.getLocalizedString("member.visible",
            "Sjást á vef"));
        drp.addMenuElement("N", iwrb.getLocalizedString("member.not_visible",
            "Ekki sjást á vef"));
        drp.setSelectedElement(selected);
        return drp;
      }

    }

    public class PhoneInsert extends EntityInsert {

      private Phone phone;

      private String phoneNumberName = "PhoneInsert_phonenumber";

      private String countryName = "PhoneInsert_country";

      private String typeName = "PhoneInsert_phonetype";

      private TextInput inputPhoneNumber;

      private DropdownMenu countryDrop;

      private DropdownMenu typeDrop;

      private String phoneNumberValue;

      private String countryValue = "1";

      private String typeValue = "1";

      private String phoneType = "Sími";

      private String headerText = "Sími";

      public PhoneInsert(IWContext modinfo) {
        super(modinfo);
        isUpdate = false;
        phone = (Phone) IDOLookup.createLegacy(Phone.class);
        phone.setDefaultValues();
        inputPhoneNumber = new TextInput(phoneNumberName);
        countryDrop = countryDropDown(countryName, "1");
        typeDrop = typeDropDown(typeName, "1");
        init();
        //setVariables();
      }

      public PhoneInsert(IWContext modinfo, String inputPhoneName,
          String dropdownTypeName) {
        super(modinfo);
        isUpdate = false;
        phone = (Phone) IDOLookup.createLegacy(Phone.class);
        phone.setDefaultValues();
        phoneNumberName = inputPhoneName;
        typeName = dropdownTypeName;
        inputPhoneNumber = new TextInput(phoneNumberName);
        countryDrop = countryDropDown(countryName, "1");
        typeDrop = typeDropDown(typeName, "1");
        init();
        //setVariables();
      }

      public PhoneInsert(IWContext modinfo, String inputPhoneName,
          String countryDropName, String dropdownTypeName) {
        super(modinfo);
        isUpdate = false;
        phone = (Phone) IDOLookup.createLegacy(Phone.class);
        phone.setDefaultValues();
        phoneNumberName = inputPhoneName;
        typeName = dropdownTypeName;
        countryName = countryDropName;
        inputPhoneNumber = new TextInput(phoneNumberName);
        countryDrop = countryDropDown(countryName, "1");
        typeDrop = typeDropDown(typeName, "1");
        init();
        //setVariables();
      }

      public PhoneInsert(IWContext modinfo, int phoneId)
          throws java.sql.SQLException, FinderException {
        super(modinfo, phoneId);
        isUpdate = true;
        phone = ((PhoneHome) IDOLookup.getHomeLegacy(Phone.class))
            .findByPrimaryKey(phoneId);
        phone.setDefaultValues();
        inputPhoneNumber = new TextInput(phoneNumberName, phone.getNumber());
        countryDrop = countryDropDown(countryName, String.valueOf(phone
            .getCountryId()));
        if (phone.getPhoneTypeId() != -1)
          typeDrop = typeDropDown(typeName, String.valueOf(phone
              .getPhoneTypeId()));
        else
          typeDrop = typeDropDown(typeName, "1");
        init();
        //setVariables();
      }

      public PhoneInsert(IWContext modinfo, int phoneId, String inputPhoneName,
          String countryDropName, String dropdownTypeName)
          throws java.sql.SQLException, FinderException {
        super(modinfo, phoneId);
        isUpdate = true;
        phone = ((PhoneHome) IDOLookup.getHomeLegacy(Phone.class))
            .findByPrimaryKey(phoneId);
        phone.setDefaultValues();
        phoneNumberName = inputPhoneName;
        typeName = dropdownTypeName;
        countryName = countryDropName;
        inputPhoneNumber = new TextInput(phoneNumberName, phone.getNumber());
        countryDrop = countryDropDown(countryName, String.valueOf(phone
            .getCountryId()));
        if (phone.getPhoneTypeId() != -1)
          typeDrop = typeDropDown(typeName, String.valueOf(phone
              .getPhoneTypeId()));
        else
          typeDrop = typeDropDown(typeName, "1");
        init();
        //setVariables();
      }

      private void init() {
        setStyle(inputPhoneNumber);
        setStyle(countryDrop);
        setStyle(typeDrop);
      }

      public boolean areNeetedFieldsEmpty() {
        return isEmpty(phoneNumberName);
      }

      public Vector getNeetedEmptyFields() {

        Vector vec = new Vector();
        if (isInvalid(phoneNumberValue)) {
          vec.addElement(phoneType);
        }
        return vec;
      }

      public TextInput getInputPhoneNumber() {
        return inputPhoneNumber;
      }

      public DropdownMenu getDropCountry() {
        return countryDrop;
      }

      public DropdownMenu getDropType() {
        return this.typeDrop;
      }

      public boolean areAllFieldsEmpty() {
        return (isEmpty(phoneNumberName) && isEmpty(countryName) && isEmpty(typeName));
      }

      public boolean areSomeFieldsEmpty() {
        return areAllFieldsEmpty();
      }

      public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
      }

      public Vector getEmptyFields() {
        Vector vec = new Vector();

        if (isInvalid(phoneNumberValue)) {
          vec.addElement(phoneType);
        }

        return vec;
      }

      public void showInputForm() throws SQLException, IOException {
        PrintWriter out = modinfo.getResponse().getWriter();
        try {

          Table table = new Table(2, 3);
          Form form = new Form();
          form.setAction(modinfo.getRequest().getRequestURI() + "?cmd=submit");
          table.add("Tegund", 1, 1);
          table.add("Númer", 1, 2);
          table.add(getDropType(), 2, 1);
          table.add(getInputPhoneNumber(), 2, 2);
          table.add(new SubmitButton(), 2, 3);
          if (getValue("cmd") != null
              && getValue("cmd").equalsIgnoreCase("submit")) {
            this.store();
          }
          //modinfo.getResponse().sendRedirect(modinfo.);
          form.add(table);
          add(form);
        } catch (SQLException e) {
          out.println(e.getMessage());
          e.printStackTrace(out);
        } catch (Exception er) {
          out.println(er.getMessage());
          er.printStackTrace(out);
        }

      }

      public HeaderTable getInputTable(boolean submitButton) {

        HeaderTable hTable = new HeaderTable();
        hTable.setHeaderText(headerText);
        Table table = new Table(2, 3);
        hTable.add(table);
        table.add("Tegund", 1, 1);
        table.add("Númer", 1, 2);
        table.add(getDropType(), 2, 1);
        table.add(getInputPhoneNumber(), 2, 2);
        if (submitButton) table.add(new SubmitButton(), 2, 3);

        return hTable;

      }

      public void store() throws SQLException, IOException {
        setVariables();
        if (phoneNumberValue == null) {
          return;
        } else if (isUpdate()) {
          phone.update();
        } else {
          phone.insert();
        }
      }

      public void store(Member member) throws SQLException, IOException {
        setVariables();
        if (phoneNumberValue == null) {
          return;
        } else if (isUpdate()) {
          if ((phone.getNumber() != null) && (!phone.getNumber().equals(""))) {
            phone.update();
          } else {
            phone.removeFrom(member);
            phone.delete();
          }
        } else if ((phone.getNumber() != null)
            && (!phone.getNumber().equals(""))) {
          phone.insert();
          phone.addTo(member);
        }
      }

      public void setVariables() {
        phoneNumberValue = getValue(phoneNumberName);
        typeValue = getValue(typeName);

        if (!isInvalid(typeValue))
            phone.setPhoneTypeId(Integer.parseInt(typeValue));

        if (phoneNumberValue != null) {
          phone.setNumber(phoneNumberValue);
        } else
          phone.setNumber("");
        phone.setCountryId(new Integer(countryValue));
      }

      private DropdownMenu countryDropDown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        Country country = (Country) IDOLookup.instanciateEntity(Country.class);
        try {
          Country[] countryArr = (Country[]) country.findAll();
          for (int i = 0; i < countryArr.length; i++) {
            drp.addMenuElement(countryArr[i].getID(), countryArr[i].getName());
          }
          drp.setSelectedElement(selected);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return drp;
      }

      private DropdownMenu typeDropDown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        PhoneType type = (PhoneType) IDOLookup
            .instanciateEntity(PhoneType.class);
        try {
          PhoneType[] typeArr = (PhoneType[]) type.findAll();
          for (int i = 0; i < typeArr.length; i++) {
            drp.addMenuElement(String.valueOf(typeArr[i].getID()), typeArr[i]
                .getName());
          }
          drp.setSelectedElement(selected);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return drp;
      }
    }

    public class CardInsert extends EntityInsert {

      private Card card;

      private TextInput nameInput;

      private TextInput socialInput;

      private TextInput numberInput;

      private DropdownMenu typeDrop;

      private DropdownMenu expireMonth;

      private DropdownMenu expireYear;

      private String ownarName = "CardInsert_name";

      private String socialName = "CardInsert_social";

      private String numberName = "CardInsert_number";

      private String typeName = "CardInsert_type";

      private String expireMonthName = "CardInsert_expiremonth";

      private String expireYearName = "CardInsert_expireyear";

      private String ownarValue = null;

      private String socialValue = null;

      private String numberValue = null;

      private String typeValue = null;

      private String expireMonthValue = null;

      private String expireYearValue = null;

      private String headerText = "Kortaupplýsingar";

      public CardInsert(IWContext modinfo) {
        super(modinfo);
        card = (Card) IDOLookup.createLegacy(Card.class);
        nameInput = new TextInput(ownarName);
        setStyle(nameInput);
        socialInput = new TextInput(socialName);
        setStyle(socialInput);
        numberInput = new TextInput(numberName);
        setStyle(numberInput);
        typeDrop = typeDropDown(typeName, "");
        setStyle(typeDrop);
        setExireDate(null);

        isUpdate = false;

      }

      public CardInsert(IWContext modinfo, int cardId) throws SQLException,
          FinderException {

        super(modinfo, cardId);

        card = ((CardHome) IDOLookup.getHomeLegacy(Card.class))
            .findByPrimaryKey(cardId);

        if (card.getName() != null)
          nameInput = new TextInput(ownarName, card.getName());
        else
          nameInput = new TextInput(ownarName);
        if (card.getSocialSecurityNumber() != null)
          socialInput = new TextInput(socialName, card
              .getSocialSecurityNumber());
        else
          socialInput = new TextInput(socialName);
        if (card.getCardNumber() != null)
          numberInput = new TextInput(numberName, card.getCardNumber());
        else
          numberInput = new TextInput(numberName);

        if (card.getCardType() != null)
          typeDrop = typeDropDown(typeName, card.getCardType());
        else
          typeDrop = typeDropDown(typeName, "");
        setExireDate(card.getExpireDate());
        setStyle(nameInput);
        setStyle(socialInput);
        setStyle(numberInput);
        setStyle(typeDrop);
        setStyle(expireMonth);
        setStyle(expireYear);

        isUpdate = true;
      }

      private void setExireDate(java.sql.Date date) {
        GregorianCalendar cal = new GregorianCalendar();

        if (date != null) {
          IWTimestamp stamp = new IWTimestamp(date);
          expireMonth = monthDropDown(expireMonthName, String.valueOf(stamp
              .getMonth()));
          expireYear = yearDropDown(expireYearName, String.valueOf(stamp
              .getYear()), (cal.get(Calendar.YEAR) - 5), (cal
              .get(Calendar.YEAR) + 3));
        } else {
          expireMonth = monthDropDown(expireMonthName, "");
          expireYear = yearDropDown(expireYearName, "",
              (cal.get(Calendar.YEAR) - 5), (cal.get(Calendar.YEAR) + 3));
        }
      }

      public Card getCard() {
        return card;
      }

      public TextInput getNameInput() {
        return this.nameInput;
      }

      public TextInput getSocialSequrityNumberInput() {
        return this.socialInput;
      }

      public TextInput getNumberInput() {
        return this.numberInput;
      }

      public DropdownMenu getDropYear() {
        return this.expireYear;
      }

      public DropdownMenu getDropMonth() {
        return this.expireMonth;
      }

      public DropdownMenu getTypeDrop() {
        return this.typeDrop;
      }

      public boolean areNeetedFieldsEmpty() {
        if (areAllFieldsEmpty())
          return false;
        else
          return areSomeFieldsEmpty();

      }

      public boolean areAllFieldsEmpty() {

        boolean isEmpty = true;
        isEmpty = (isInvalid(expireMonthValue) && isInvalid(expireYearValue)/*
                                                                             * expireValue ==
                                                                             * null
                                                                             */
            && isEmpty(ownarName) && isEmpty(numberName) && isEmpty(socialName) && isEmpty(typeName));

        return isEmpty;

      }

      public boolean areSomeFieldsEmpty() {
        return (isInvalid(expireMonthValue) || isInvalid(expireYearValue)
            || (ownarValue == null) || (numberValue == null)
            || (socialValue == null) || (typeValue == null)
            || !ownarValue.equals("") || !numberValue.equals("")
            || !socialValue.equals("") || !typeValue.equals(""));
      }

      public void setVariables() {
        ownarValue = getValue(ownarName);
        numberValue = getValue(numberName);
        socialValue = getValue(socialName);
        typeValue = getValue(typeName);
        expireMonthValue = getValue(expireMonthName);
        expireYearValue = getValue(expireYearName);

        if (numberValue != null) {
          card.setCardNumber(numberValue);
        }
        if (!isInvalid(expireMonthValue) && !isInvalid(expireYearValue)) {
          IWTimestamp stamp = new IWTimestamp(1, Integer
              .parseInt(expireMonthValue), Integer.parseInt(expireYearValue));
          card.setExpireDate(stamp.getSQLDate());
        }
        if (typeValue != null) {
          card.setCardType(typeValue);
        }
        if (ownarValue != null) {
          card.setName(ownarValue);
        }
        if (numberValue != null) {
          card.setCardNumber(numberValue);
        }
        if (socialValue != null) {
          card.setSocialSecurityNumber(socialValue);
        }
      }

      public Vector getNeetedEmptyFields() {
        return getEmptyFields();
      }

      public Vector getEmptyFields() {
        Vector vec = new Vector();
        if (isInvalid(expireMonthValue) || isInvalid(expireYearValue)) {
          vec.addElement("Gildistími");
        }
        if (isInvalid(ownarValue)) {
          vec.addElement("Korthafi");
        }
        if (isInvalid(numberValue)) {
          vec.addElement("Kortanúmer");
        }
        if (isInvalid(socialValue)) {
          vec.addElement(iwrb.getLocalizedString(
              "member.social_security_number", "SSN"));
        }
        if (isInvalid(typeValue)) {
          vec.addElement("Tegund korts");
        }
        return vec;
      }

      public void store() throws SQLException, IOException {
        setVariables();
        Vector vError = getEmptyFields();
        int errSize = vError.size();
        if ((!isUpdate) && errSize == 5) {
          return;
        } else if ((!isUpdate) && errSize > 0) { return; }
        if (isUpdate) {
          card.update();
        } else {
          card.insert();
        }
      }

      public void showInputForm() throws SQLException, IOException {
        PrintWriter out = modinfo.getResponse().getWriter();
        try {
          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);
          Form form = new Form();
          Table table = new Table(2, 6);
          form.add(table);
          hTable.add(form);
          table.add(iwrb.getLocalizedString("member.social_security_number",
              "SSN"), 1, 1);
          table.add(getSocialSequrityNumberInput(), 2, 1);
          table.add("Gildistími", 1, 2);
          table.add(getDropMonth(), 2, 2);
          table.add(getDropYear(), 2, 2);
          table.add("Korthafi", 1, 3);
          table.add(getNameInput(), 2, 3);
          table.add("Kortanúmer", 1, 4);
          table.add(getNumberInput(), 2, 4);
          table.add("Tegund", 1, 5);
          table.add(getTypeDrop(), 2, 5);
          table.add(new SubmitButton("Vista"), 2, 6);
          form.add(new Parameter("cmd", "submit"));

          if (getValue("cmd") != null
              && getValue("cmd").equalsIgnoreCase("submit")) {
            store();
            if (card.getID() != -1)
                form.add(new Parameter("card_id", "" + card.getID()));
          }

          modinfo.getRequest().removeAttribute("cmd");
          add(hTable);
        } catch (SQLException e) {
          out.println(e.getMessage());
          e.printStackTrace(out);
        } catch (Exception er) {
          out.println(er.getMessage());
          er.printStackTrace(out);
        }
      }

      public BorderTable getInputTable() {

        BorderTable hTable = new BorderTable();
        //hTable.setHeaderText(headerText);
        Table table = new Table(2, 5);
        hTable.add(table);

        table.add(formatText(iwrb.getLocalizedString(
            "member.social_security_number", "Kennitala")), 1, 1);
        table.add(getSocialSequrityNumberInput(), 2, 1);

        table.add(formatText(iwrb.getLocalizedString("member.valid_through",
            "Gildistími")), 1, 2);
        table.add(getDropMonth(), 2, 2);
        table.add(getDropYear(), 2, 2);

        table.add(formatText(iwrb.getLocalizedString("member.card_owner",
            "Korthafi")), 1, 3);
        table.add(getNameInput(), 2, 3);

        table.add(formatText(iwrb.getLocalizedString("member.card_number",
            "Kortanúmer")), 1, 4);
        table.add(getNumberInput(), 2, 4);

        table.add(formatText(iwrb.getLocalizedString("member.card_type",
            "Tegund")), 1, 5);
        table.add(getTypeDrop(), 2, 5);

        return hTable;
      }

      private DropdownMenu monthDropDown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);
        String month = null;

        drp.addMenuElement("1", "Mán.");
        for (int i = 1; i < 13; i++) {

          if (i < 10)
            month = "0" + i;
          else
            month = String.valueOf(i);
          drp.addMenuElement(String.valueOf(i), month);
        }
        drp.setSelectedElement(selected);

        return drp;
      }

      private DropdownMenu yearDropDown(String name, String selected, int from,
          int to) {
        DropdownMenu drp = new DropdownMenu(name);
        int year = IWTimestamp.RightNow().getYear() + 1;
        drp.addMenuElement(String.valueOf(year), iwrb.getLocalizedString(
            "member.year", "Ár"));
        for (int i = from; i <= to; i++) {
          drp.addMenuElement(String.valueOf(i), String.valueOf(i));
        }
        drp.setSelectedElement(selected);

        return drp;
      }

      private DropdownMenu typeDropDown(String name, String selected) {
        DropdownMenu drp = new DropdownMenu(name);

        drp.addMenuElement("", "Tegund");
        drp.addMenuElement("visa", "Visa");
        drp.addMenuElement("eurocard", "Eurocard");
        drp.setSelectedElement(selected);

        return drp;
      }
    }

    public class FamilyDisconnecterWindow extends
        com.idega.presentation.ui.Window {

      private final String STORE_NAME = "STOREFAMILY";

      private UnionMemberInfo uniMemInfo = null;

      private String headerText = iwrb.getLocalizedString(
          "member.family.disconnect", "Disconnect family ties");

      private int unionId;

      public FamilyDisconnecterWindow(int memberId, int unionId)
          throws java.sql.SQLException, FinderException {

        setTitle(iwrb.getLocalizedString("member.family.findfamily",
            "Find family"));
        this.unionId = unionId;
        uniMemInfo = ((MemberHome) IDOLookup.getHomeLegacy(Member.class))
            .findByPrimaryKey(memberId).getUnionMemberInfo(unionId);
      }

      public void main(IWContext modinfo) {
        this.empty();
        add(getInputTable(modinfo));
      }

      public Form getInputTable(IWContext modinfo) {
        Form form = new Form();
        form.setMethod("get");
        try {

          String strStore = modinfo.getRequest()
              .getParameter(STORE_NAME + ".x");

          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);

          Table table = new Table(1, 3);

          Text t = new Text(iwrb.getLocalizedString(
              "member.family.disconnectconfirm", "Are you sure?"), true, false,
              false);
          t.setFontColor("red");
          table.setRowAlignment(1, "center");
          table.add(t, 1, 1);

          Table buttonTable = new Table(2, 1);

          buttonTable.add(new SubmitButton(
              iwrb.getImage("buttons/confirm.gif"), STORE_NAME, "storeval"), 1,
              1);
          buttonTable.add(new CloseButton(iwrb.getImage("buttons/close.gif")),
              2, 1);

          table.add(buttonTable, 1, 3);
          if (strStore != null) {

            store(modinfo);
            close();
            setParentToReload();
          }
          hTable.add(table);
          form.add(hTable);
        } catch (Exception e) {
          System.err.println("\n\nVilla i getInputTable\n\n");
          e.printStackTrace();
        }
        return form;
      }

      private void store(IWContext modinfo) throws SQLException, IOException {

        Family family = (Family) IDOLookup.createLegacy(Family.class);
        family.insert();

        uniMemInfo.setFamily(family);
        uniMemInfo.update();
      }

    }

    public class FamilyInsertWindow extends com.idega.presentation.ui.Window {

      private static final String NAME = "1";

      private static final String NAME_AND_MIDDLE = "2";

      private static final String SOSIAL_SEC_NUM = "3";

      private static final String ALL = "4";

      private final String STORE_NAME = "STOREFAMILY";

      private final String FIND_NAME = "FINDFAMILY";

      private final String STORE = "store";

      private final String FIND = "find";

      private UnionMemberInfo uniMemInfo = null;

      private Family family;

      private DropdownMenu selectFamily;

      private String selectionFamilyName = "familyBox";

      private String[] selectionFamilyValues;

      private String headerText = iwrb.getLocalizedString("member.family",
          "Family");

      private int unionId;

      private TextInput inputFind;

      private DropdownMenu choise;

      private String findValue;

      private String findName = "familyFinder";

      private String choiseName = "keyToSearch";

      private String choiseValue;

      private String[] selectGroupsValues = new String[0];

      public FamilyInsertWindow(int memberId, int unionId)
          throws java.sql.SQLException, FinderException {

        setTitle(iwrb.getLocalizedString("member.family.findfamily",
            "Find Family"));
        this.unionId = unionId;
        inputFind = new TextInput(findName);
        inputFind.keepStatusOnAction();
        choise = getChoises(choiseName);
        choise.keepStatusOnAction();
        Member mem = (Member) IDOLookup.createLegacy(Member.class);
        uniMemInfo = ((MemberHome) IDOLookup.getHomeLegacy(Member.class))
            .findByPrimaryKey(memberId).getUnionMemberInfo(unionId);
      }

      public void main(IWContext modinfo) {
        this.empty();
        add(getInputTable(modinfo));
      }

      private void setVariables(IWContext modinfo) {
        choiseValue = modinfo.getRequest().getParameter(choiseName);
        findValue = getValue(findName, modinfo);

        if (findValue != null && (!findValue.equals(""))) {
          //member.setFamilyId(Integer.parseInt(findValue));
        }

      }

      public Form getInputTable(IWContext modinfo) {
        Form form = new Form();
        form.setMethod("get");
        try {

          String strStore = modinfo.getRequest()
              .getParameter(STORE_NAME + ".x");
          String strFind = modinfo.getRequest().getParameter(FIND_NAME + ".x");

          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);

          Table table = new Table(1, 6);
          table.mergeCells(1, 1, 2, 1);
          table.add(choise, 1, 3);
          table.add(inputFind, 1, 3);

          Table buttonTable = new Table(2, 1);
          buttonTable.setCellpadding(0);
          buttonTable.setCellspacing(0);

          table.add(new SubmitButton(iwrb.getImage("/buttons/search.gif"),
              FIND_NAME, "findval"), 1, 4);

          buttonTable
              .add(new SubmitButton(iwrb.getImage("/buttons/register.gif"),
                  STORE_NAME, "storeval"), 1, 1);
          buttonTable.add(new CloseButton(iwrb.getImage("buttons/close.gif")),
              2, 1);

          table.add(buttonTable, 1, 6);
          if (strStore != null) {

            store(modinfo);
            close();
            setParentToReload();
          } else {
            if (strFind != null) {
              setVariables(modinfo);
              int numRecords = 0;
              List l = find(findValue, choiseValue);
              setSelectionBox(l);
              if (l != null) {
                numRecords = l.size();
              }
              table.add(iwrb.getLocalizedString(
                  "member.family.numberofrecords", "Number of records: ")
                  + numRecords, 1, 1);
              table.add(selectFamily, 1, 5);
            }
          }
          hTable.add(table);
          form.add(hTable);
        } catch (Exception e) {
          System.err.println("\n\nError in getInputTable\n\n");
          e.printStackTrace();
        }
        return form;
      }

      private void store(IWContext modinfo) {
        selectionFamilyValues = modinfo.getRequest().getParameterValues(
            selectionFamilyName);
        String familyId = selectionFamilyValues[0];
        int nOldFamilyID = this.uniMemInfo.getFamilyId();

        try {
          if (selectionFamilyValues != null && selectionFamilyValues.length > 0) {
            if (!familyId.equals("")) {
              uniMemInfo.setFamilyId(new Integer(familyId));
              uniMemInfo.update();
            }
          }
        } catch (SQLException e) {
          System.err.println("Error in store!! ... !!\n");
          System.err.println(e.getMessage());
        }

      }

      public String getValue(String attribute, IWContext modinfo) {
        return modinfo.getParameter(attribute);
      }

      private DropdownMenu getChoises(String name) {
        DropdownMenu drp = new DropdownMenu(name);
        drp.addMenuElement(NAME, iwrb
            .getLocalizedString("member.family.first_full_name"));
        drp.addMenuElement(NAME_AND_MIDDLE, iwrb.getLocalizedString(
            "member.family.first_middle_name", "First name and middle name"));
        drp.addMenuElement(SOSIAL_SEC_NUM, iwrb.getLocalizedString(
            "member.social_security_number", "SSN"));
        drp.addMenuElement(ALL, iwrb.getLocalizedString("member.family.all",
            "All"));
        return drp;
      }

      private List find(String toFind, String cmd) {
        toFind = toFind.replace('*', '%');
        Name name = new Name(toFind);

        List list = null;

        try {
          if ((name.getFirstName().equals("")) && (!cmd.equals(ALL))) { return list; }
          if (cmd.equals(SOSIAL_SEC_NUM)) {
            list = EntityFinder
                .findAll(
                    (UnionMemberInfo) IDOLookup
                        .instanciateEntity(UnionMemberInfo.class),
                    "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "
                        + unionId
                        + " and member.SOCIAL_SECURITY_NUMBER like '"
                        + name.getFirstName() + "'");
          } else if (cmd.equals(ALL)) {
            list = EntityFinder
                .findAll(
                    (UnionMemberInfo) IDOLookup
                        .instanciateEntity(UnionMemberInfo.class),
                    "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "
                        + unionId + " order by member.first_name");
          } else if (!name.getMiddleName().equals("")) {
            if (cmd.equals(this.NAME)) {
              list = EntityFinder
                  .findAll(
                      (UnionMemberInfo) IDOLookup
                          .instanciateEntity(UnionMemberInfo.class),
                      "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "
                          + unionId
                          + " and member.first_name like '"
                          + name.getFirstName()
                          + "' and member.middle_name like '"
                          + name.getMiddleName()
                          + "' and member.last_name like '"
                          + name.getLastName() + "'");
            } else { // if (cmd.equals(this.NAME_AND_MIDDLE))
              list = EntityFinder
                  .findAll(
                      (UnionMemberInfo) IDOLookup
                          .instanciateEntity(UnionMemberInfo.class),
                      "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "
                          + unionId
                          + " and member.first_name like '"
                          + name.getFirstName()
                          + "' and member.middle_name like '"
                          + name.getMiddleName() + "'");
            }
          } else if (!name.getLastName().equals("")) {
            list = EntityFinder
                .findAll(
                    (UnionMemberInfo) IDOLookup
                        .instanciateEntity(UnionMemberInfo.class),
                    "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "
                        + unionId
                        + " and member.first_name like '"
                        + name.getFirstName()
                        + "' and member.last_name like '"
                        + name.getLastName() + "'");
          } else {
            list = EntityFinder
                .findAll(
                    (UnionMemberInfo) IDOLookup
                        .instanciateEntity(UnionMemberInfo.class),
                    "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "
                        + unionId
                        + " and member.first_name like '"
                        + name.getFirstName() + "'");
          }
        } catch (Exception e) {
          System.err.println("Error in FamilyWindow " + e.getMessage());
          e.printStackTrace();
          return list;
        }

        return list;
      }

      private void setSelectionBox(List list) throws Exception {
        selectFamily = new DropdownMenu(selectionFamilyName);
        //selectFamily.setHeight(8);
        selectFamily.setMarkupAttribute("size", "8");
        Member mem = null;
        UnionMemberInfo uni = null;

        if ((list == null) || list.isEmpty()) {
          selectFamily.addMenuElement("", iwrb.getLocalizedString(
              "member.family.nosearchresult", "Nothing found"));
        } else if (list != null) {
          for (int i = 0; i < list.size(); i++) {
            uni = (UnionMemberInfo) list.get(i);
            mem = ((MemberHome) IDOLookup.getHomeLegacy(Member.class))
                .findByPrimaryKey(uni.getMemberID());
            if (mem != null) {
              selectFamily.addMenuElement(uni.getFamilyId(), mem.getName());
            }
          }
        }
      }

    }

    public class MemberInfoInsert extends EntityInsert {

      private MemberInfo memInfo;

      private String inputHandicapName = "MemberInfoInsert_memInfonumber";

      private int memberID = 1;

      private TextInput inputHandicap;

      private HiddenInput hiddenHandicap;

      private String inputHandicapValue;

      private String headerText = iwrb.getLocalizedString("member.handicap",
          "Handicap");

      private Text handicap = null;

      public MemberInfoInsert(IWContext modinfo) {
        super(modinfo);
        isUpdate = false;
        memInfo = (MemberInfo) IDOLookup.createLegacy(MemberInfo.class);
        memInfo.setDefaultValues();
        inputHandicap = new TextInput(inputHandicapName);
        //setVariables();
      }

      public MemberInfoInsert(IWContext modinfo, int memInfoId)
          throws java.sql.SQLException, FinderException {
        super(modinfo, memInfoId);
        isUpdate = true;
        memInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class))
            .findByPrimaryKey(memInfoId);
        memInfo.setDefaultValues();
        String h = com.idega.util.text.TextSoap
            .singleDecimalFormat((double) memInfo.getHandicap());
        handicap = formatText(h);
        handicap.setFontSize(6);
        hiddenHandicap = new HiddenInput(inputHandicapName, String
            .valueOf(memInfo.getHandicap()));
        inputHandicap = new TextInput(inputHandicapName, String.valueOf(memInfo
            .getHandicap()));
        //setVariables();
      }

      public MemberInfo getMemberInfo() {
        return this.memInfo;
      }

      public void setMemberId(int id) {
        memberID = id;
      }

      public boolean areNeetedFieldsEmpty() {
        return false;
      }

      public Vector getNeetedEmptyFields() {
        return new Vector();
      }

      public TextInput getInputHandicap() {
        inputHandicap.setMaxlength(4);
        inputHandicap.setLength(4);
        return inputHandicap;
      }

      public Text getHandicap() {
        return this.handicap;
      }

      public boolean areSomeFieldsEmpty() {
        return (isEmpty(inputHandicapName));
      }

      public Vector getEmptyFields() {
        Vector vec = new Vector();

        if (isInvalid(inputHandicapValue)) {
          vec
              .addElement(iwrb
                  .getLocalizedString("member.handicap", "Handicap"));
        }

        return vec;
      }

      public void showInputForm() throws SQLException, IOException {
        PrintWriter out = modinfo.getResponse().getWriter();
        try {

          Table table = new Table(2, 2);
          table.setBorder(1);
          Form form = new Form();
          form.setAction(modinfo.getRequest().getRequestURI() + "?cmd=submit");
          table.add(iwrb.getLocalizedString("member.handicap", "Handicap"), 1,
              1);
          table.add(getInputHandicap(), 2, 1);
          table.add(new SubmitButton(), 2, 2);
          if (getValue("cmd") != null
              && getValue("cmd").equalsIgnoreCase("submit")) {
            this.store();
          }
          form.add(table);
          add(form);
        } catch (SQLException e) {
          out.println(e.getMessage());
          e.printStackTrace(out);
        } catch (Exception er) {
          out.println(er.getMessage());
          er.printStackTrace(out);
        }

      }

      public BorderTable getInputTable() {
        BorderTable hTable = new BorderTable();
        if (isUpdate) {
          Table table = new Table(1, 1);
          hTable.add(table);
          table.add(getHandicap(), 1, 1);
          table.add(hiddenHandicap);
        } else {
          Table table = new Table(1, 1);
          hTable.add(table);
          table.add(formatText(iwrb.getLocalizedString("member.handicap",
              "Handicap")), 1, 1);
          table.add(getInputHandicap(), 1, 1);
        }

        return hTable;
      }

      public void store() throws SQLException, IOException {
        PrintWriter out = modinfo.getResponse().getWriter();
        setVariables();
        if (!isUpdate()) {
          IWTimestamp stamp = new IWTimestamp(new java.sql.Date(System
              .currentTimeMillis()));
          memInfo.setHistory(stamp.toString()
              + ": "
              + iwrb.getLocalizedString("member.member_added",
                  "Félagi nýskráður í kerfið"));
          memInfo.setMemberId(memberID);
          memInfo.setFirstHandicap(memInfo.getHandicap());
          memInfo.insert();
        }
      }

      public void setVariables() {
        inputHandicapValue = getValue(inputHandicapName);

        if (!isInvalid(inputHandicapValue)) {
          inputHandicapValue = com.idega.util.text.TextSoap.findAndReplace(
              inputHandicapValue, ",", ".");
          memInfo.setHandicap(Float.valueOf(inputHandicapValue));
        } else
          memInfo.setHandicap(Float.valueOf("100"));
      }
    }

    public abstract class EntityInsert {

      protected String errorRedirect;

      protected String sessionId;

      protected IWContext modinfo;

      protected boolean isUpdate;

      protected String headerText = "";

      private int entityID;

      public String styleAttribute = "font-size: 8pt";

      private int fontSize = 1;

      private String fontColor = "#336660";

      public EntityInsert(IWContext modinfo) {
        this.modinfo = modinfo;
        //errorRedirect = "membererror.jsp";
        sessionId = "error";
        isUpdate = false;
      }

      public EntityInsert(IWContext modinfo, int entityId) {
        this.modinfo = modinfo;
        //errorRedirect = "membererror.jsp";
        sessionId = "error";
        isUpdate = true;
        entityID = entityId;
      }

      public abstract boolean areSomeFieldsEmpty();

      public abstract boolean areNeetedFieldsEmpty();

      public abstract Vector getEmptyFields();

      public abstract Vector getNeetedEmptyFields();

      public abstract void store() throws SQLException, IOException;

      public abstract void setVariables();

      public boolean isDateInputValid(String inputName) {
        if (inputName + "_day" == null || (inputName + "_day").equals(""))
          return false;
        else if (inputName + "_month" == null
            || (inputName + "_month").equals(""))
          return false;
        else if (inputName + "_year" == null
            || (inputName + "_year").equals("")) return false;

        return true;
      }

      public boolean isDigitOnly(String value) {
        if (value == null || value.length() == 0) return false;
        char[] arr = value.toCharArray();
        for (int i = 0; i < arr.length; i++) {
          if (!Character.isDigit(arr[i])) return false;
        }
        return true;
      }

      public boolean isEmpty(String inputName) {
        return (getValue(inputName) == null || getValue(inputName).equals(""));
      }

      public String getValue(String attribute) {
        return modinfo.getParameter(attribute);
      }

      public void setErrorRedirectPageAndSessionID(String redirectPage,
          String sessionID) {
        this.errorRedirect = redirectPage;
        this.sessionId = sessionID;
      }

      protected boolean isUpdate() {
        return isUpdate;
      }

      protected java.sql.Date getDateFromInput(String inputName) {
        String strDay = getValue(inputName + "_day");
        String strMonth = getValue(inputName + "_month");
        String strYear = getValue(inputName + "_year");
        if (strDay == null || strMonth == null || strYear == null
            || strDay.equals("") || strMonth.equals("") || strYear.equals("")) { return null; }
        IWTimestamp stamp = new IWTimestamp(Integer.parseInt(strDay), Integer
            .parseInt(strMonth), Integer.parseInt(strYear));
        return stamp.getSQLDate();
      }

      public void setHeaderText(String text) {
        this.headerText = text;
      }

      public boolean isInvalid(String str) {
        return ((str == null) || str.equals(""));
      }

      protected Vector StringArrToVector(String[] strArr) {
        Vector vec = new Vector();
        for (int i = 0; i < strArr.length; i++) {
          vec.addElement(strArr[i]);
        }
        return vec;
      }

      public Text formatText(String s) {
        Text T = new Text();
        if (s != null) {
          T = new Text(s);
          T.setFontColor(this.fontColor);
          T.setFontSize(this.fontSize);
        }
        return T;
      }

      public Text formatText(int i) {
        return formatText(String.valueOf(i));
      }

      protected void setStyle(InterfaceObject O) {
        O.setStyleAttribute(this.styleAttribute);
      }
    }

  }
}
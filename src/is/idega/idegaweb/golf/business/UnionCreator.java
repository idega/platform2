package is.idega.idegaweb.golf.business;

import is.idega.idegaweb.golf.entity.*;

import com.idega.jmodule.news.data.*;
import com.idega.jmodule.forum.data.*;
import com.idega.jmodule.boxoffice.data.*;
import com.idega.jmodule.text.data.*;

import com.idega.data.EntityFinder;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class UnionCreator {

  private UnionCreator() {
  }


  public static Union createUnion(String name,String type,String abbrevation,String loginName,String passwd,Union parent,int CountryID,String city,String zipCode)throws Exception{
    Member member = ((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).createLegacy();
    String fullName = name+" - Admin";
    member.setFirstName(fullName);
    member.setLastName("");
    member.setSocialSecurityNumber("0000000000");
    member.insert();

    //HardCoded clubAdmin ID (14)
    Group clubAdmin = ((is.idega.idegaweb.golf.entity.GroupHome)com.idega.data.IDOLookup.getHomeLegacy(Group.class)).findByPrimaryKeyLegacy(14);
    member.addTo(clubAdmin);

    LoginTable lt = ((is.idega.idegaweb.golf.entity.LoginTableHome)com.idega.data.IDOLookup.getHomeLegacy(LoginTable.class)).createLegacy();
    lt.setMemberId(member.getID());
    lt.setUserLogin(loginName);
    lt.setUserPassword(passwd);
    lt.insert();
    Union u = createUnion(name,type,abbrevation,parent,CountryID,city,zipCode);
    member.setMainUnion(u.getID());
    return u;
  }

  public static Union createUnion(String name,String type,String abbrevation,Union parent,int CountryID,String city,String zipCode)throws Exception{
    ZipCode zip = getZipCode(zipCode,city,CountryID);
    Address addr = ((is.idega.idegaweb.golf.entity.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
    addr.setCountryId(CountryID);
    addr.setZipcode(zip);
    addr.insert();
    return createUnion(name,type,abbrevation,parent,addr);
  }

  public static Union createUnion(String name,String type,String abbrevation,Union parent,Address address)throws Exception{


    Union union = ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).createLegacy();
    union.setName(name);
    union.setUnionType(type);
    union.setAbbrevation(abbrevation);
    union.insert();


    String fieldName = union.getName()+" field";
    Field field = ((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).createLegacy();
    field.setFieldPar(72);
    field.setName(fieldName);
    field.setUnionID(union.getID());
    field.insert();

    /*
    TextModule fieldText = new TextModule();
    text.setTextHeadline(texts[i]);
    fieldText.insert();
    */

    NewsCategory news = new NewsCategory();
    news.setNewsCategoryName(union.getName()+" NewsCategory");
    news.insert();

    NewsCategoryAttributes nattr = new NewsCategoryAttributes();
    nattr.setNewsCategoryId(news.getID());
    nattr.setAttributeName("union_id");
    nattr.setAttributeId(union.getID());
    nattr.insert();

    Forum forum = new Forum();
    forum.setForumName(union.getName()+" Forum");
    forum.setForumDescription("GolfClub Forum");
    forum.setValid(true);
    forum.setGroupID(new Integer(1));
    forum.insert();

    ForumAttributes fattr = new ForumAttributes();
    fattr.setForumID(new Integer(forum.getID()));
    fattr.setAttributeName("union_id");
    fattr.setAttributeID(new Integer(union.getID()));
    fattr.insert();

    Issues issues = new Issues();
    issues.setIssueName("Files");
    issues.setID(union.getID());
    issues.insert();

    //HardCoded Issues category
    IssuesCategory cat = new IssuesCategory(1);
    //TODO unComment (temporary buildfix)
    //cat.addTo(issues);



    String[] texts = {"History","Tarif","Committies","General Information","Restaurant and shop","Facts","English"};

    for (int i = 0; i < texts.length; i++) {
        TextModule text = new TextModule();
        text.setTextHeadline(texts[i]);
        text.insert();
        text.addTo(union);
    }

    if(parent!=null){
      parent.addChild(union);
    }

    address.addTo(union);
    return union;
  }

  public static ZipCode getZipCode(String code,String city,int CountryID)throws Exception{
    ZipCode zip = ((is.idega.idegaweb.golf.entity.ZipCodeHome)com.idega.data.IDOLookup.getHomeLegacy(ZipCode.class)).createLegacy();
    //HardCoded ColumnNames
    java.util.List list = EntityFinder.findAllByColumn(zip,"code",code,"country_id",Integer.toString(CountryID));
    if(list!=null){
      if(list.size()>0){
        java.util.Iterator iter = list.iterator();
        while (iter.hasNext()){
          Object item = iter.next();
          if(item!=null){
            return (ZipCode)item;
          }
        }
      }
    }
    zip.setCity(city);
    zip.setCode(code);
    zip.setCountryID(CountryID);
    zip.insert();
    return zip;
  }
}

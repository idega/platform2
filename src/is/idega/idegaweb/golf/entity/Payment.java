//idega 2000 - eiki

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Payment extends GolfEntity{

	public Payment(){
		super();
	}

	public Payment(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
                addAttribute("account_id", "Reikningur", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Account");
		addAttribute("member_id", "Félagi", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Member");
		addAttribute("price_catalogue_id","Tegund",true,true,"java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.PriceCatalogue");
                addAttribute("payment_date","Greiðsludagur",true,true,"java.sql.Timestamp");
                addAttribute("last_updated","Síðast Breytt",true,true,"java.sql.Timestamp");
                addAttribute("cashier_id","Gerandi",true,true,"java.lang.Integer");
		addAttribute("status","Staða greiðslu",true,true,"java.lang.Boolean");
                addAttribute("name","Athugasemd",true,true,"java.lang.String");
		addAttribute("extra_info","Athugasemd",true,true,"java.lang.String");
		addAttribute("payment_type_id","Tegund",true,true,"java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.PaymentType");
                addAttribute("price", "Upphæð", true, true, "java.lang.Integer");
                addAttribute("union_id", "Klúbbur", true, true, "java.lang.Integer");
                addAttribute("installment_nr", "Greiðsla númer", true, true, "java.lang.Integer");
                addAttribute("total_installment", "Fjöldi greiðslna", true, true, "java.lang.Integer");
                addAttribute("round_id", "Umferð", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.PaymentRound");

	}

	public String getEntityName(){
		return "payment";
	}

        public int getAccountId(){
          return getIntColumnValue("account_id");
        }
        public void setAccountId(Integer account_id){
          setColumn("account_id", account_id);
        }
        public void setAccountId(int account_id){
          setColumn("account_id", account_id);
        }

	public int getMemberId(){
		return getIntColumnValue("member_id");
	}

	public void setMemberId(Integer member_id){
		setColumn("member_id", member_id);
	}

        public void setMemberId(int member_id){
		setColumn("member_id", member_id);
	}

	public int getPriceCatalogueId(){
		return getIntColumnValue("price_catalogue_id");
	}

	public void setPriceCatalogueId(Integer price_catalogue_id){
		setColumn("price_catalogue_id", price_catalogue_id);
	}

        public void setPriceCatalogueId(int price_catalogue_id){
		setColumn("price_catalogue_id", price_catalogue_id);
	}

	public int getPaymentTypeID(){
		return getIntColumnValue("payment_type_id");
	}

	public void setPaymentTypeID(Integer payment_type){
		setColumn("payment_type_id", payment_type);
	}

	public void setPaymentTypeID(int payment_type){
		setColumn("payment_type_id", new Integer(payment_type));
	}

	public Timestamp getPaymentDate(){
		return (Timestamp) getColumnValue("payment_date");
	}

	public void setPaymentDate(Timestamp payment_date){
		setColumn("payment_date", payment_date);
	}

        public Timestamp getLastUpdated(){
          return (Timestamp) getColumnValue("last_updated");
	}

	public void setLastUpdated(Timestamp last_updated){
          setColumn("last_updated", last_updated);
	}

        public int getCashierId(){
		return getIntColumnValue("cashier_id");
	}

	public void setCashierId(Integer member_id){
		setColumn("cashier_id", member_id);
	}

        public void setCashierId(int member_id){
		setColumn("cashier_id", member_id);
	}


	public boolean getStatus(){
          return getBooleanColumnValue("status");
	}

	public void setStatus(boolean status){
	  setColumn("status", status);
	}

        public String getName(){
	  return getStringColumnValue("name");
	}

	public void setName(String name){
	  setColumn("name", name );
	}

	public String getExtraInfo(){
	  return getStringColumnValue("extra_info");
	}

	public void setExtraInfo(String extra_info){
	  setColumn("extra_info", extra_info);
	}

        public int getPrice(){
          return getIntColumnValue("price");
	}

	public void setPrice(Integer price){
          setColumn("price", price);
	}

        public void setPrice(int price){
          setColumn("price", price);
        }

        public int getUnionId(){
          return getIntColumnValue("union_id");
	}

	public void setUnionId(Integer union_id){
          setColumn("union_id", union_id);
	}

        public void setUnionId(int union_id){
          setColumn("union_id", union_id);
        }

        public int getInstallmentNr(){
		return getIntColumnValue("installment_nr");
	}

	public void setInstallmentNr(int installment_nr){
		setColumn("installment_nr", installment_nr);
	}

	public void setInstallmentNr(Integer installment_nr){
		setColumn("installment_nr", installment_nr);
	}

        public int getTotalInstallment(){
		return getIntColumnValue("total_installment");
	}

	public void setTotalInstallment(Integer total_installment){
		setColumn("total_installment", total_installment);
	}

        public void setTotalInstallment(int total_installment){
		setColumn("total_installment", total_installment);
	}

        public int getRoundId(){
		return getIntColumnValue("round_id");
	}

	public void setRoundId(Integer round_id){
		setColumn("round_id", round_id);
	}

        public void setRoundId(int round_id){
		setColumn("round_id", round_id);
	}

}

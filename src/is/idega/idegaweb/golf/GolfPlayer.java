package is.idega.idegaweb.golf;
//import java.sql.*;

public class GolfPlayer{

private int MemberID;
private String Name;
private String HandyCap;
private String Club;
private int CardNo;

public GolfPlayer(){
	MemberID = 0;
	Name = "";
	HandyCap = "";
	Club = "";
	CardNo = 0;
}
public GolfPlayer( int memberid, String name, String handycap, String club, int cardNo){
	MemberID = memberid;
	Name = name;
	HandyCap = handycap;
	Club = club;
	CardNo = cardNo;
}

public int getMemberID(){
	return MemberID;
}

public String getName(){
	return Name;
}

public String getHandyCap(){
	return HandyCap;
}

public String getClub(){
	return Club;
}

public int getCardNo(){
	return CardNo;
}

public void setMemberID(int memberid){
	MemberID = memberid;
}

public void setName(String name){
	Name = name;
}

public void setHandyCap(String handycap){
	HandyCap = handycap;
}

public void setClub(String club){
	Club = club;
}

public void setCardNo(int cardNo){
	CardNo = cardNo;
}



} // class GolfPlayer

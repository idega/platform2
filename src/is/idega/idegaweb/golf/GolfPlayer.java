package is.idega.idegaweb.golf;
//import java.sql.*;

public class GolfPlayer{

private int MemberID;
private String Name;
private String Handicap;
private String Club;
private int CardNo;

public GolfPlayer(){
	MemberID = 0;
	Name = "";
	Handicap = "";
	Club = "";
	CardNo = 0;
}
public GolfPlayer( int memberid, String name, String handicap, String club, int cardNo){
	MemberID = memberid;
	Name = name;
	Handicap = handicap;
	Club = club;
	CardNo = cardNo;
}

public int getMemberID(){
	return MemberID;
}

public String getName(){
	return Name;
}

public String getHandicap(){
	return Handicap;
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

public void setHandicap(String handicap){
	Handicap = handicap;
}

public void setClub(String club){
	Club = club;
}

public void setCardNo(int cardNo){
	CardNo = cardNo;
}



} // class GolfPlayer

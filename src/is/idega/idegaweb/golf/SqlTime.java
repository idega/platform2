package is.idega.idegaweb.golf;

import java.util.*;


public class  SqlTime{

	private int hours, mins, secs;




	public SqlTime(){}

	public SqlTime( String time){
		StringTokenizer Timetoken = new StringTokenizer(time, ":.");
			hours = Integer.parseInt(Timetoken.nextToken());
			mins = Integer.parseInt(Timetoken.nextToken());
			secs = Integer.parseInt(Timetoken.nextToken());
	}

	public SqlTime( int hour, int min, int sec){
		hours = hour;
		mins = min;
		secs =sec;
	}

	public int get_hour(){
		return hours;
	}

	public int get_min(){
		return mins;
	}

	public int get_sec(){
		return secs;
	}





	public void set_sqltime( int hour, int min, int sec){
		hours = hour;
		mins = min;
		secs =sec;
	}


	public void set_sqltime( String time2 ){
			StringTokenizer Timetoken = new StringTokenizer(time2,":.");
			hours = Integer.parseInt(Timetoken.nextToken());
			mins = Integer.parseInt(Timetoken.nextToken());
			secs = Integer.parseInt(Timetoken.nextToken());
	}



} // class SqlTime

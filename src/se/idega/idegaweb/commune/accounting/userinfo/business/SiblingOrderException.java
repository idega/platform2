package se.idega.idegaweb.commune.accounting.userinfo.business;

public class SiblingOrderException extends Exception{
	SiblingOrderException(String s){
		super(s);
	}

	SiblingOrderException(String s, Throwable e){
		super(s, e);
	}
}



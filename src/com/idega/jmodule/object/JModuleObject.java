//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object;

import com.idega.jmodule.object.textObject.*;
import java.util.*;
import java.io.*;
import com.idega.jmodule.login.business.*;
import com.idega.idegaweb.IWCacheManager;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.3
*/
public class JModuleObject extends ModuleObjectContainer{

  private static Map permissionKeyMap = new Hashtable();
  private String cacheKey;
  private boolean cacheable=false;
  private long cacheInterval;

  private static final String concatter = "_";
  private static final String newline = "\n";

  public static boolean usingNewAcessControlSystem=false;

  public JModuleObject(){

  }

  public boolean isAdministrator(ModuleInfo modinfo)throws Exception{
    if(usingNewAcessControlSystem){
      return com.idega.core.accesscontrol.business.AccessControl.hasAdminPermission(this,modinfo);
    }
    else{
      return AccessControl.isAdmin(modinfo);
    }
  }

  /**
   * <H2>Unimplemented</H2>
   */
  public boolean isDeveloper(ModuleInfo modinfo)throws Exception{
    return false;
  }

  /**
   * <H2>Unimplemented</H2>
   */
  public boolean isUser(ModuleInfo modinfo)throws Exception{
    return false;
  }


  /**
   * <H2>Unimplemented</H2>
   */
  public boolean isMemberOf(ModuleInfo modinfo,String groupName)throws Exception{
    return false;
  }

  /**
   * <H2>Unimplemented</H2>
   */
  public boolean hasPermission(String permissionType,ModuleInfo modinfo,ModuleObject obj)throws Exception{
  //  return AccessControl.hasPermission(permissionType,modinfo,obj);
    return false;
  }

/* public boolean hasPermission(String permissionType,ModuleInfo modinfo)throws Exception{
    return hasPermission(permissionType,modinfo,this);
  }*/


  public String getModuleName(){
    return this.getClass().getName();
  }

  /**
   * Implement in subclasses:
   */
  protected void registerPermissionKeys(){
  }

  protected void registerPermissionKey(String permissionKey,String localizeableKey){
    Map m = getPermissionKeyClassMap();
    m.put(permissionKey,localizeableKey);
  }

  protected void registerPermissionKey(String permissionKey){
    registerPermissionKey(permissionKey,permissionKey);
  }

  private Map getPermissionKeyMap(){
    return permissionKeyMap;
  }

  private Map getPermissionKeyClassMap(){
    Map m = (Map)getPermissionKeyMap().get(this.getClass());
    if(m==null){
      m = new Hashtable();
      getPermissionKeyMap().put(this.getClass(),m);
    }
    return m;
  }

  String[] getPermissionKeys(JModuleObject obj){
    return getPermissionKeys(obj.getClass());
  }

  String[] getPermissionKeys(Class jModuleObjectClass){
    Map m = (Map)getPermissionKeyMap().get(jModuleObjectClass);
    if(m!=null){
      return (String[])m.keySet().toArray(new String[0]);
    }
    return null;
  }

  private static long twentyMinutes = 60*1000*20;

  public void setCacheable(String cacheKey){
    setCacheable(cacheKey,twentyMinutes);
  }

  public void setCacheable(String cacheKey,long millisecondsInterval){
    cacheable=true;
    this.cacheKey=cacheKey;
    this.cacheInterval=millisecondsInterval;
  }

  public void beginCacheing(ModuleInfo modinfo,StringBuffer buffer)throws Exception{
    PrintWriter servletWriter = modinfo.getWriter();
    modinfo.setCacheing(true);
    PrintWriter writer = new JModuleCacheWriter(servletWriter,buffer);
    modinfo.setCacheWriter(writer);
  }

  public void endCacheing(ModuleInfo modinfo,StringBuffer buffer){
    modinfo.setCacheing(false);
    IWCacheManager.getInstance(modinfo.getApplication()).setObject(getCacheKey(modinfo),buffer,cacheInterval);
  }

  public void _main(ModuleInfo modinfo)throws Exception{
    if(this.isCacheable()){
      setCacheKey(modinfo);
      if(isCacheValid(modinfo)){
      }
      else{
        //beginCacheing(modinfo);
        super._main(modinfo);
        //endCacheing(modinfo);
      }
    }
    else{
      super._main(modinfo);
    }
  }

  public void print(ModuleInfo modinfo)throws Exception{
    if(this.isCacheable()){
      if(isCacheValid(modinfo)){
        StringBuffer buffer = (StringBuffer)IWCacheManager.getInstance(modinfo.getApplication()).getObject(getCacheKey(modinfo));
        modinfo.getWriter().print(buffer.toString());
      }
      else{
        StringBuffer buffer = new StringBuffer();
        beginCacheing(modinfo,buffer);
        super.print(modinfo);
        endCacheing(modinfo,buffer);
      }
    }
    else{
      super.print(modinfo);
    }
  }


  private boolean isCacheValid(ModuleInfo modinfo){
    if(getCacheKey(modinfo)!=null){
      return IWCacheManager.getInstance(modinfo.getApplication()).isCacheValid(getCacheKey(modinfo));
    }
    return false;
  }

  private String getCacheKey(ModuleInfo modinfo){
    return cacheKey;
  }

  private void setCacheKey(ModuleInfo modinfo){
    boolean loggedon = LoginBusiness.isLoggedOn(modinfo);
    if(loggedon){
      String parameter = AccessControl.ACCESSCONTROL_GROUP_PARAMETER;
      String parametervalue = null;
      if(parameter.equals(AccessControl.CLUB_ADMIN_GROUP)){
        parametervalue = (String)modinfo.getSessionAttribute(parameter);
      }
      else{
        parametervalue = (String)modinfo.getSessionAttribute(parameter);
        parametervalue += concatter;
        parametervalue += AccessControl.CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE;
        parametervalue += AccessControl.getGolfUnionOfClubAdmin(modinfo);
      }
      cacheKey = cacheKey+concatter+parameter+concatter+parametervalue;
    }
    this.cacheKey += modinfo.getCurrentLocale().toString();
  }

  protected boolean isCacheable(){
    return this.cacheable;
  }


  public class JModuleCacheWriter extends java.io.PrintWriter{

    private PrintWriter underlying;
    private StringBuffer buffer;


    public JModuleCacheWriter(PrintWriter underlying,StringBuffer buffer){
      super(underlying);
      this.underlying = underlying;
      this.buffer=buffer;
    }

    public boolean checkError(){
      return underlying.checkError();
    }

    public void close(){
      underlying.close();
    }

    public void flush(){
      underlying.flush();
    }

    public void print(boolean b){
      print(String.valueOf(b));
    }

    public void print(char c){
      print(String.valueOf(c));
    }

    public void print(char[] s){
      print(String.valueOf(s));
    }

    public void print(double d){
      print(String.valueOf(d));
    }

    public void print(float f){
      print(String.valueOf(f));
    }

    public void print(int i){
      print(String.valueOf(i));
    }

    public void print(long l){
      print(String.valueOf(l));
    }

    public void print(Object o){
      print(String.valueOf(o));
    }

    public void print(String s){
      underlying.print(s);
      buffer.append(s);
    }

    public void println(){
      underlying.println();
      buffer.append(newline);
    }


    public void println(boolean b){
      println(String.valueOf(b));
    }

    public void println(char c){
      println(String.valueOf(c));
    }

    public void println(char[] s){
      println(String.valueOf(s));
    }

    public void println(double d){
      println(String.valueOf(d));
    }

    public void println(float f){
      println(String.valueOf(f));
    }

    public void println(int i){
      println(String.valueOf(i));
    }

    public void println(long l){
      println(String.valueOf(l));;
    }

    public void println(Object o){
      println(String.valueOf(o));
    }

    public void println(String s){
      print(s);
      println();
    }

    public void setError(){
      super.setError();
    }

    public void write(char[] buf){
      print(buf);
    }

    public void write(char[] buf,int off, int len){
      char[] newarray = new char[len];
      System.arraycopy(buf,off,newarray,0,len);
      write(newarray);
    }

    public void write(int c){
      print(c);
    }

    public void write(String s){
      print(s);
    }

    public void write(String s, int off, int len){
      write(s.substring(off,off+len));
    }


  }

}
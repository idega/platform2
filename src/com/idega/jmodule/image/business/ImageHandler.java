package com.idega.jmodule.image.business;

import javax.media.jai.*;
import com.sun.media.jai.codec.*;
import com.sun.media.jai.*;
//import javax.media.jai.widget.*;
//import javax.media.jai.iterator.*;
import java.io.*;
import java.sql.*;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;

import oracle.sql.*;
import oracle.jdbc.driver.*;

import com.idega.data.BlobInputStream;
import com.idega.data.BlobWrapper;
import com.idega.jmodule.client.imageModule;
import com.idega.jmodule.image.data.*;
import com.idega.servlet.IWCoreServlet;


/**
 * Title: ImageHandler
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company: idega
 * @author Eirikur Hrafnsson
 * @version 1.0
 *
 * Must seperate reading the image and resizing it for streams in the next version
 */

//public class ImageHandler extends JModule {
public class ImageHandler extends IWCoreServlet {

private PlanarImage originalImage = null;
private PlanarImage modifiedImage = null;
//private ImageDisplay canvas = null;
private boolean state = false;
private int imageId = -1;
private int modifiedImageId = -1;
private String contentType = null;
private String imageName = null;
private int width;
private int height;
private int scale = 1;
private int modifiedWidth = -1;
private int modifiedHeight = -1;
private float quality = 0.75f;
private boolean keepProportions = false;
private ImageEntity imageInfo = null;
private int brightness = 30;
private KernelJAI kernel;
private float sum = 9.0F;



public ImageHandler( int imageId ) throws Throwable{
  getImageFromDatabase(imageId);
  setModifiedImageAsOriginal();
}

public ImageHandler( String fileName ) throws Throwable{
  getImageFromFile(fileName);
  setModifiedImageAsOriginal();
}

public ImageHandler( PlanarImage originalImage, int ParentId ) throws Throwable{
  setImageId(ParentId);
  setOriginalImage(originalImage);
  setModifiedImageAsOriginal();
}

//crappy constructor fix this!
public ImageHandler( ImageEntity imageEntity ) throws Throwable{
  getImageFromDatabase( imageEntity.getID() );
  setModifiedImageAsOriginal();
}

public void getImageFromFile(String fileName) throws Throwable{

  File f = new File(fileName);

  if ( f.exists() && f.canRead() ) {
    originalImage = JAI.create("fileload", fileName);
  } else {
      System.err.println("The image \""+fileName+"\" does not exist or can't be read");
  }

}

public void getImageFromDatabase(int imageId) throws Throwable{

  setImageId(imageId);

  this.imageInfo = new ImageEntity( imageId );
  setContentType( imageInfo.getContentType() );
  setImageName( imageInfo.getName() );

  BlobWrapper wrapper = imageInfo.getImageValue();
  BlobInputStream inputStream = wrapper.getBlobInputStream();



  BufferedInputStream bufStream = new BufferedInputStream(inputStream);
  MemoryCacheSeekableStream memStream = new MemoryCacheSeekableStream(bufStream);

  originalImage = JAI.create("stream", memStream);
  setWidth(originalImage.getWidth());
  setHeight(originalImage.getHeight());

  //update the original entity
  //Debug for now...
  imageInfo.setWidth(""+originalImage.getWidth());
  imageInfo.setHeight(""+originalImage.getHeight());
  imageInfo.update();
  //

  inputStream.close();//closes the blobinputstream and closes misc stmt and connections
  bufStream.close();
  memStream.close();

}

public void setOriginalImage(PlanarImage originalImage){

  this.originalImage = originalImage;
  setWidth(originalImage.getWidth());
  setHeight(originalImage.getHeight());

}

private void setImageId( int imageId ){
  this.imageId = imageId;
}

public int getImageId(){
  return this.imageId;
}

public int getOriginalImageId(){
  return getImageId();
}

public void setBrightness( int brightness ){
  this.brightness = brightness;
}

private int getBrightness(){
  return this.brightness;
}

private void setModifiedImageId( int modifiedImageId ){
  this.modifiedImageId = modifiedImageId;
}

public int getModifiedImageId(){
  return this.modifiedImageId;
}

private void setContentType( String contentType ){
  this.contentType = contentType;
}

private String getContentType(){
  return this.contentType;
}

public void setImageName( String imageName ){
  this.imageName = imageName;
}

private String getImageName(){
  return this.imageName;
}


private void setWidth( int width ){
  this.width = width;
}

public int getWidth(){
  return this.width;
}

private void setHeight( int height ){
  this.height = height;
}

public int getHeight(){
  return this.height;
}

public void setModifiedWidth( int modifiedWidth ){
  this.modifiedWidth = modifiedWidth;
}

public int getModifiedWidth(){
  return this.modifiedWidth;
}

public void setModifiedHeight( int modifiedHeight ){
  this.modifiedHeight = modifiedHeight;
}

public int getModifiedHeight(){
  return this.modifiedHeight;
}

public boolean keepProportions(){
  return this.keepProportions;
}

public void keepProportions(boolean keepProportions){
  this.keepProportions = keepProportions;
}

private void setModifiedImageAttributes(){

  int tempWidth = getModifiedWidth();
  int tempHeight = getModifiedHeight();

  //if tempWidth and tempHeight =-1 then we are scaling
  if ( (tempWidth==-1) && (tempHeight ==-1) ){
    setModifiedWidth(getWidth()*getScale());
    setModifiedHeight(getHeight()*getScale());
  }
  else if( (tempWidth!=-1) && (tempHeight !=-1) ){//changed both
    setModifiedWidth(tempWidth);
    setModifiedHeight(tempHeight);
  }
  else {//we changed either the width or height

    if ( tempWidth == -1 ){//missing width
      if ( keepProportions() ) setModifiedWidth( (getWidth()*tempHeight)/getHeight());
      else setModifiedWidth( getWidth() );
      setModifiedHeight( tempHeight );
    }
    if( tempHeight == -1 ){//missing height
      if ( keepProportions() ) setModifiedHeight( (getHeight()*tempWidth)/getWidth() );
      else setModifiedHeight( getHeight() );
      setModifiedWidth( tempWidth );
    }

  }
}


public void resizeImage() throws Throwable{
  ParameterBlock pb = new ParameterBlock();
  //pb.addSource(originalImage);
  pb.addSource(getModifiedImage());
  RenderableImage ren = JAI.createRenderable("renderable", pb);
  setModifiedImageAttributes();
  RenderingHints effect = new RenderingHints(RenderingHints.KEY_INTERPOLATION , RenderingHints.VALUE_INTERPOLATION_BILINEAR );
  setModifiedImage((PlanarImage)ren.createScaledRendering(getModifiedWidth(), getModifiedHeight(), effect ));
 /*
  //try some rendering options
  //RenderingHints effect = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  //RenderingHints effect = new RenderingHints(RenderingHints.KEY_DITHERING , RenderingHints.VALUE_DITHER_ENABLE );
  // RenderingHints effect = new RenderingHints(RenderingHints.KEY_DITHERING , RenderingHints.VALUE_DITHER_DISABLE );
*/

}

public void convertModifiedImageToGrayscale(){
  setModifiedImage(convertColorToGray(getModifiedImage(), getBrightness() ));
}

public void convertModifiedImageToColor(){
  setModifiedImage(convertGrayToColor(getModifiedImage(), getBrightness() ));
}

private int getScale(){
  return this.scale;
}

public void setScale(int scale){
  this.scale = scale;
}

public void setModifiedImage( PlanarImage modifiedImage ){
  this.modifiedImage = modifiedImage;
  setModifiedWidth(modifiedImage.getWidth());
  setModifiedHeight(modifiedImage.getHeight());
}

public void setModifiedImageAsOriginal(){
  this.setModifiedImage(originalImage);
  setModifiedWidth(originalImage.getWidth());
  setModifiedHeight(originalImage.getHeight());
}

public PlanarImage getModifiedImage(){
  return this.modifiedImage;
}

public PlanarImage getOriginalImage(){
  return this.originalImage;
}
/*
*  1.0 best quality 0.75 high quality 0.5  medium quality  0.25 low quality 0.10 crappy quality
*
*/
public void setQuality(float quality){
  this.quality = quality;
}

private float getQuality(){
  return this.quality;
}

public com.idega.jmodule.object.Image getModifiedImageAsImageObject(HttpServlet servlet) throws Throwable{
  writeModifiedImageToFile(servlet.getServletContext().getRealPath("/")+"/pics/ModifiedImagetemp.jpg");//temporary storage
  //InputStream input = new FileInputStream("/pics/ModifiedImagetemp.jpg");
return new com.idega.jmodule.object.Image("/pics/ModifiedImagetemp.jpg",this.getImageName(),this.getModifiedWidth(),this.getModifiedHeight());

}


public void writeModifiedImageToDatabase() throws Throwable{

  writeModifiedImageToFile("imagemoduletemp.jpg");//temporary storage

  InputStream input = new FileInputStream("imagemoduletemp.jpg");

  Connection Conn = null;
  String dataBaseType = "";
  try{

    Conn = getConnection();
    dataBaseType = com.idega.data.DatastoreInterface.getDataStoreType(Conn);
    modifiedImageId = getNewImageID(Conn);
    String statement = "insert into image (image_id,image_value,content_type,image_name,from_file,parent_id,width,height) values("+ modifiedImageId +",?,?,?,'N','"+ getImageId() +"',"+ getModifiedWidth() +","+ getModifiedHeight() +")";
    PreparedStatement myPreparedStatement = Conn.prepareStatement ( statement );

  if( !(dataBaseType.equals("oracle")) ) {
    //Conn.setAutoCommit(false);
//content type er lesið í imageModule
//fileName með entity

    myPreparedStatement = Conn.prepareStatement(statement);
    myPreparedStatement.setBinaryStream(1, input, input.available() );
    myPreparedStatement.setString(2, getContentType() );
    myPreparedStatement.setString(3, getImageName() );
    myPreparedStatement.execute();
    myPreparedStatement.close();
    //Conn.commit();
  }
  //ORACLE SPECIFIC STARTS
  else {
	oracle.sql.BLOB blob;
	Conn.setAutoCommit(false);
	myPreparedStatement = Conn.prepareStatement ( statement );
        myPreparedStatement.setString(1, "00000001");//i stað hins venjulega empty_blob()
        myPreparedStatement.setString(2, getContentType() );
        myPreparedStatement.setString(3, getImageName() );
        myPreparedStatement.execute();
        myPreparedStatement.close();

        Conn.commit();

        int Start;
	int Finish;
	int StartID;
	int FinishID;
	int StartColumn;
	int FinishColumn;

        String ID;
	String tableName;
	String smaller="";
	String smaller2="";
	String cmd;
	String columnName;

	int doUpdate = statement.indexOf("update");
	//is an update statement

  if( doUpdate!=-1 ){

	//update file_ set file_value=?,content_type=?,file_name=? where file_id="+file_id+"
	  Start = doUpdate+7;
	  Finish = statement.indexOf("set")-1;

	  StartColumn = statement.indexOf("where")+6;
	  smaller2 = statement.substring(StartColumn,statement.length());
	  FinishColumn = smaller2.indexOf("=");
	  StartColumn = 0;

        StartID = FinishColumn+1;
	smaller = smaller2.substring(StartID,smaller2.length());
	FinishID = smaller.length();
	StartID = 0;

  }//end of if update
  else{
  //insert into file_ (file_id,file_value,content_type,file_name,date_added,from_file) values("+file_id+",?,?,?,'"+dags.getTimestampRightNow().toString()+"','N')")

  Start = statement.indexOf("into")+5;
  Finish = statement.indexOf("(")-1;

  StartColumn = Finish +2;
  smaller2 = statement.substring(StartColumn,statement.length());
  FinishColumn = smaller2.indexOf(",");
  StartColumn = 0;

  StartID = statement.indexOf("values(")+7;
  smaller = statement.substring(StartID,statement.length());
  FinishID = smaller.indexOf(",");
  StartID=0;

  }//end of else

  tableName = statement.substring(Start,Finish);
  columnName = smaller2.substring(StartColumn,FinishColumn);
  ID = smaller.substring(StartID,FinishID);

  cmd = "SELECT * FROM "+tableName+"  WHERE "+columnName+" ='"+ID+"' FOR UPDATE";
  System.out.println("CMD = "+cmd);

//do the filling
        Conn.setAutoCommit(false);
        Statement stmt2 = Conn.createStatement();
        ResultSet rest = stmt2.executeQuery(cmd);

        rest.next();
        blob = ((OracleResultSet)rest).getBLOB(3);

                // write the array of binary data to a BLOB
        OutputStream     outstream = blob.getBinaryOutputStream();

        int size = blob.getBufferSize();
        byte[] buffer = new byte[size];
        int length = -1;

        while ((length = input.read(buffer)) != -1)
            outstream.write(buffer, 0, length );

      outstream.flush();
      outstream.close();
      input.close();

      Conn.commit();

}//ORACLE SPECIFIC ENDS

}
  catch (SQLException E){
    System.out.println(E.toString());
  }
  catch (Exception E){
    System.out.println(E.toString());
  }
  finally{
    freeConnection(Conn);
  }

}

public void writeModifiedImageToFile(String filename) throws Throwable{

  if ( filename.equalsIgnoreCase("")) filename = getImageName();
  OutputStream output = new FileOutputStream(filename);

  JPEGEncodeParam jpgParam = new JPEGEncodeParam();
  jpgParam.setQuality(getQuality());

/*
for BMP support
    ImageEncoder tEncoder = ImageCodec.createImageEncoder("BMP", tOutput, null);

*/

  //here we need to specify different types of images.
  ImageEncoder imageEncoder = ImageCodec.createImageEncoder("JPEG",output,jpgParam);

  PlanarImage modified = getModifiedImage();

  if ( modified != null) imageEncoder.encode( getModifiedImage() );
  else System.out.println("getModifiedImage() returned null!");

  output.flush();
  output.close();
}




public int getNewImageID(Connection Conn)throws SQLException{

String dataBaseType = com.idega.data.DatastoreInterface.getDataStoreType(Conn);

Statement Stmt = Conn.createStatement();
ResultSet RS;
int imageId;

  if( !(dataBaseType.equals("oracle")) ) {
	RS = Stmt.executeQuery("select gen_id(image_gen,1) from rdb$database");
  }
  else{		//oracle
	RS = Stmt.executeQuery("select image_seq.nextval from dual");
  }

  RS.next();
  imageId = RS.getInt(1);

  Stmt.close();
  RS.close();
//  getSession().setAttribute("imageId",image_id);

return imageId;
}

public static PlanarImage convertColorToGray(PlanarImage src, int brightness) {
   PlanarImage dst = null;
   double b = (double) brightness;
   double[][] matrix = {
                         { .114D, 0.587D, 0.299D, b },
                           { .114D, 0.587D, 0.299D, b },
                           { .114D, 0.587D, 0.299D, b }
                    /*      { .114D, 0.200D, 0.100D, b },//red
                           { 0.05D, 0.700D, 0.050D, b },//green
                           { .114D, 0.200D, 0.100D, b }//blue
                           //{HUE,SATURATION,CONTRAST,BRIGHTNESS)*/
                       };
    if ( src != null ) {
       ParameterBlock pb = new ParameterBlock();
       pb.addSource(src);
       pb.add(matrix);
       dst = JAI.create("bandcombine", pb, null);
   }
    return dst;
}

 /** produce a 3 band image from a single band gray scale image */
 public static PlanarImage convertGrayToColor(PlanarImage src, int brightness) {
   PlanarImage dst = null;
   double b = (double) brightness;
   double[][] matrix = {
                          { 1.0D, b },
                          { 1.0D, b },
                          { 1.0D, b }
                       };

   if ( src != null ) {
       int nbands = src.getSampleModel().getNumBands();

// MUST check color model here
       if ( nbands == 1 ) {
           ParameterBlock pb = new ParameterBlock();
           pb.addSource(src);
           pb.add(matrix);
           dst = JAI.create("bandcombine", pb, null);
       } else {
           dst = src;
       }
   }

   return dst;
}

public void embossModifiedImage(){
  loadKernel(8);
  setModifiedImage( convolve(getModifiedImage(),8));
}

public void sharpenModifiedImage(){
  loadKernel(2);
  setModifiedImage( convolve(getModifiedImage(),2));
}

public void invertModifiedImage(){
  setModifiedImage( invert(getModifiedImage()));
}

public PlanarImage invert(PlanarImage source) {

  ParameterBlock pb = new ParameterBlock();
  pb.addSource(source);
  RenderableImage ren = JAI.createRenderable("renderable", pb);

  pb = new ParameterBlock();
  pb.addSource(ren);
  RenderableImage inv = JAI.createRenderable("invert", pb);

  PlanarImage dst = (PlanarImage)inv.createScaledRendering(source.getWidth(), source.getHeight(), null);

 return dst;
}

public PlanarImage convolve(PlanarImage source, int k) {
   ParameterBlock pb = new ParameterBlock();
   pb.addSource(source);
   pb.add(kernel);
   PlanarImage target = JAI.create("convolve", pb, null);
    // emboss (special case)
   if ( k == 8 ) {
       double[] constants = new double[3];
       constants[0] = 128.0;
       constants[1] = 128.0;
       constants[2] = 128.0;
       pb = new ParameterBlock();
       pb.addSource(target);
       pb.add(constants);
       target = JAI.create("addconst", pb, null);
   }

  return target;
}

 public void loadKernel(int choice) {

        float[] data = new float[9];

        switch( choice ) {
            case 0:
                data[0] = 0.0F; data[1] =-1.0F; data[2] = 0.0F;
                data[3] =-1.0F; data[4] = 5.0F; data[5] =-1.0F;
                data[6] = 0.0F; data[7] =-1.0F; data[8] = 0.0F;
            break;

            case 1:
                data[0] =-1.0F; data[1] =-1.0F; data[2] =-1.0F;
                data[3] =-1.0F; data[4] = 9.0F; data[5] =-1.0F;
                data[6] =-1.0F; data[7] =-1.0F; data[8] =-1.0F;
            break;
//sharpen very good
            case 2:
                data[0] = 1.0F; data[1] =-2.0F; data[2] = 1.0F;
                data[3] =-2.0F; data[4] = 5.0F; data[5] =-2.0F;
                data[6] = 1.0F; data[7] =-2.0F; data[8] = 1.0F;
            break;

            case 3:
                data[0] =-1.0F; data[1] = 1.0F; data[2] =-1.0F;
                data[3] = 1.0F; data[4] = 1.0F; data[5] = 1.0F;
                data[6] =-1.0F; data[7] = 1.0F; data[8] =-1.0F;
            break;

            case 4:
                data[0] =-1.0F; data[1] =-1.0F; data[2] =-1.0F;
                data[3] =-1.0F; data[4] = 8.0F; data[5] =-1.0F;
                data[6] =-1.0F; data[7] =-1.0F; data[8] =-1.0F;
            break;

            case 5:
                data[0] = 0.0F; data[1] =-1.0F; data[2] = 0.0F;
                data[3] =-1.0F; data[4] = 4.0F; data[5] =-1.0F;
                data[6] = 0.0F; data[7] =-1.0F; data[8] = 0.0F;
            break;

            case 6:
                data[0] = 1.0F; data[1] = 1.0F; data[2] = 1.0F;
                data[3] = 1.0F; data[4] = 1.0F; data[5] = 1.0F;
                data[6] = 1.0F; data[7] = 1.0F; data[8] = 1.0F;
            break;

            case 7:
                data[0] = 1.0F; data[1] = 2.0F; data[2] = 1.0F;
                data[3] = 2.0F; data[4] = 4.0F; data[5] = 2.0F;
                data[6] = 1.0F; data[7] = 2.0F; data[8] = 1.0F;
            break;
//emboss
            case 8:
                data[0] =-1.0F; data[1] =-2.0F; data[2] = 0.0F;
                data[3] =-2.0F; data[4] = 0.0F; data[5] = 2.0F;
                data[6] = 0.0F; data[7] = 2.0F; data[8] = 1.0F;
            break;

         /*   case 9:
                //get text for custom kernel
                for ( int i = 0; i < 3; i++ ) {
                    for ( int j = 0; j < 3; j++ ) {
                        try {
                            data[3*i+j] = Float.parseFloat(krn[i][j].getText());
                        } catch( NumberFormatException e ) {
                            data[3*i+j] = 0.0F;
                        }
                    }
                }
            break;
        }

        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                krn[i][j].setText("" + data[3*i+j]);
            }
        }
*/

        }
        normalize(data);
        kernel = new KernelJAI(3, 3, data);
}

public void normalize(float[] data) {
   sum = 0.0F;
    for ( int i = 0; i < data.length; i++ ) {
       sum += data[i];
   }
    if ( sum > 0.0F ) {
       for ( int i = 0; i < data.length; i++ ) {
          data[i] = data[i] / sum;
       }
   } else {
       sum = 1.0F;
   }
}



}//end of class


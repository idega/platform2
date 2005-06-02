package com.idega.block.creditcard.business;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/*
* This SslClient uses the following Apache modules:
*  - commons-logging-api.jar
*  - commons-logging.jar
*  - commons-httpclient-2.0.jar
*/

public class SSLClient 
{
  private String m_strHost = "";
  private int m_iPort = 0;
  private String m_strRealmUser = "";
  private String m_strRealmPwd = "";
  private SecureProtocolSocketFactory m_oSSLSocketFactory = null;

  public class MySSLSocketFactory implements SecureProtocolSocketFactory 
  {
    SSLContext m_oSSLCtx = null;
    MySSLSocketFactory(String _strKeyStorePass, String _strKeyStore)
      throws IOException
    {
      this(_strKeyStorePass, _strKeyStore, false);
    }
    
    MySSLSocketFactory(String _strKeyStorePass, String _strKeyStore, boolean _bTrustAll)
      throws IOException
    {
      super();
      try 
      {
        KeyManagerFactory oSSLKMF;
        KeyStore oSSLKS;
        char[] passphrase = _strKeyStorePass.toCharArray();

        // Set the truststore that contains the root certificates used
        // to authenticate the test server.
        // The production server uses well known certificates and therefore
        // uses the default "cacerts" file that is located in the 
        // Java Runtime folder /jre/lib/security
        m_oSSLCtx = SSLContext.getInstance("SSL");
        oSSLKMF = KeyManagerFactory.getInstance("SunX509");
        oSSLKS = KeyStore.getInstance("JKS");

        oSSLKS.load(new FileInputStream(_strKeyStore), passphrase);

        oSSLKMF.init(oSSLKS, passphrase);
        // A trustall TrustStore will not check the validity of the server
        // certificates. Therefore it is not very safe....
        if (_bTrustAll)
          m_oSSLCtx.init(oSSLKMF.getKeyManagers(), getTrustManager(), new java.security.SecureRandom());
        else
          m_oSSLCtx.init(oSSLKMF.getKeyManagers(), null, new java.security.SecureRandom());
      } 
      catch (Exception e) 
      {
    	  System.err.println("Error getting SSLClient : keyStorePass = "+_strKeyStorePass+", keyStore ="+_strKeyStore);
    	  e.printStackTrace(System.err);
        throw new IOException(e.getMessage());
      }

    }
    private TrustManager[] getTrustManager() 
    {
      TrustManager[] trustAllCerts = new TrustManager[]
      {
        new X509TrustManager() 
        {
          public java.security.cert.X509Certificate[] getAcceptedIssuers()
          {
            return null;
          }
          public void checkClientTrusted(java.security.cert.X509Certificate[] certs, 
                                       String authType) 
          {
          }
  
          public void checkServerTrusted(java.security.cert.X509Certificate[] certs, 
                                         String authType) 
          {
          }
        }
      };
      return trustAllCerts;
    }
    
    public Socket createSocket(String host, int port) 
      throws IOException, UnknownHostException 
    {
      // Create the socket
      SSLSocket socket = (SSLSocket)m_oSSLCtx.getSocketFactory().createSocket(host, port);
      // Do the SSL handshake
      socket.startHandshake();
      // Return a connected socket 
      return socket;
    }
  
    public Socket createSocket(Socket socket, String host, int port, boolean flag) 
      throws IOException, UnknownHostException 
    {
      SSLSocket socket2 = (SSLSocket)m_oSSLCtx.getSocketFactory().createSocket(host, port);
      // Do the SSL handshake
      socket2.startHandshake();
      // Return a connected socket 
      return socket;
    }
  
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) 
      throws IOException, UnknownHostException 
    {
      SSLSocket socket = (SSLSocket)m_oSSLCtx.getSocketFactory().createSocket(host, port, clientHost, clientPort);
      // Do the SSL handshake
      socket.startHandshake();
      // Return a connected socket 
      return socket;
    }
  }
  
  public SSLClient(String strHost, int iPort, String _strKeyStore, String _strKeyStorePass, String _strRealmUser, String _strRealmPwd) 
    throws IOException
  {
    m_strHost = strHost;
    m_iPort = iPort;
    m_strRealmUser = _strRealmUser;
    m_strRealmPwd = _strRealmPwd;
    m_oSSLSocketFactory = new MySSLSocketFactory(_strKeyStorePass, _strKeyStore, false);
  }
  
  public String sendRequest(String _strProcedure, String _strData) 
      throws Exception 
  {
    return sendRequest(_strProcedure, _strData, 10, 70);
  }
  
  public String sendRequest(String _strProcedure, String _strData, int _iConnectTimeoutSec, int _iRequestTimeoutSec) 
      throws Exception 
  {
    HttpClient oClient = new HttpClient();
    // Set Connection timeout
    oClient.setConnectionTimeout(_iConnectTimeoutSec * 1000);
    // Set Request timeout
    oClient.setTimeout(_iRequestTimeoutSec * 1000);
    // Set Basic-Authentication realm properties
    oClient.getState().setCredentials(null, m_strHost,
        new UsernamePasswordCredentials(m_strRealmUser, m_strRealmPwd));
    // Set the Socket Factory we will use to connect
    Protocol.registerProtocol("https", new Protocol("https", m_oSSLSocketFactory, m_iPort));
    // Configure the URI
    String strURI = "https://" + m_strHost + ":" + m_iPort + _strProcedure;
    // Finally prepare the parameters to post
    PostMethod oPost = new PostMethod(strURI);
    oPost.setDoAuthentication( true );
    oPost.setRequestBody(_strData);
    oPost.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE);
    try 
    {
      // Fire the call at the server...
      oClient.executeMethod(oPost);
    }
    catch (Exception ex) 
    {
      // MUST ADD BETTER, MORE SPECIFIC EXCEPTION HANDLING...
      throw new Exception("HttpClient request got exception:" + ex.getMessage(), ex);
    }
    return oPost.getResponseBodyAsString();
  }

  public static void main(String[] args) 
  {
    String strHost = "test.kortathjonustan.is";
    int iPort = 8443;
    String strKeyStorePass = "changeit";
    String strKeyStorePath = "/demoFolder/testkeys.jks";
    String strRequest = "site=22&user=idega&pwd=zde83af&d41=90000022&d42=8180001";
    try 
    {
      SSLClient sslclient = new SSLClient(strHost, iPort, strKeyStorePath,
                                          strKeyStorePass, "idega", "zde83af");

      System.out.println("More time goes into the first request because SSL session is not cached yet");
      System.out.println("Request [" + strRequest + "]");
      long lStart = System.currentTimeMillis();
      String strResponse = sslclient.sendRequest("/rpc/RequestAuthorisation", strRequest);
      System.out.println("Time = " + (System.currentTimeMillis() - lStart));
      //System.out.println("More time goes into the first request because SSL session is not cached yet");
      System.out.println("Response [" + strResponse + "]");
      
      System.out.println("Less time goes into subsequent requests as SSL session is now cached");
      System.out.println("Request [" + strRequest + "]");
      lStart = System.currentTimeMillis();
      strResponse = sslclient.sendRequest("/rpc/RequestAuthorisation", strRequest);
      System.out.println("Time = " + (System.currentTimeMillis() - lStart));
      System.out.println("Response [" + strResponse + "]");
    }
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }

}
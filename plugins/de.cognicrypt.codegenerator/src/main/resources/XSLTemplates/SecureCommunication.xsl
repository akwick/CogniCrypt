<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<xsl:output method="text"/>
<xsl:template match="/">

<xsl:variable name="Rounds"> <xsl:value-of select="//task/algorithm[@type='KeyDerivationAlgorithm']/iterations"/> </xsl:variable>
<xsl:variable name="outputSize"> <xsl:value-of select="//task/algorithm[@type='KeyDerivationAlgorithm']/algorithm[@type='Digest']/outputSize"/> </xsl:variable>

<xsl:choose>
	<xsl:when test="//task/code/server='true'">
		<xsl:result-document href="serverConfig.properties">
			pwd="<xsl:value-of select="//task/code/keystorepassword"/>"
		</xsl:result-document>
	</xsl:when>
	<xsl:otherwise>
		<xsl:result-document href="clientConfig.properties">
			pwd="<xsl:value-of select="//task/code/keystorepassword"/>"
		</xsl:result-document>
	</xsl:otherwise>
</xsl:choose>

<xsl:variable name="filename"><xsl:choose><xsl:when test="//task/code/server='true'">serverConfig.properties</xsl:when><xsl:otherwise>clientConfig.properties</xsl:otherwise></xsl:choose></xsl:variable>


<xsl:if test="//task[@description='SecureCommunication']">
<xsl:choose><xsl:when test="//task/code/server='true'">
<xsl:result-document href="TLSServer.java">
package <xsl:value-of select="//task/Package"/>; 
<xsl:apply-templates select="//Import"/>
/** @author CogniCrypt */
public class TLSServer {	
	private static SSLServerSocket sslServersocket = null;
	private static List&lt;TLSConnection&gt; sslConnections = null;
	private static Properties prop = new Properties();
	private static InputStream input = null;
	private static String pwd = null; 
			
	public TLSServer(int port) {
			System.setProperty("javax.net.ssl.keyStore","<xsl:value-of select="//task/code/keystore"/>");
			try {
				// If you move the generated code in another package (default of CogniCrypt is Crypto),
				// you need to change the parameter (replacing Crypto with the package name).
				input = Object.class.getClass().getResourceAsStream("/Crypto/serverConfig.properties");
				prop.load(input);
				pwd = prop.getProperty("pwd"); 
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
        	System.setProperty("javax.net.ssl.keyStorePassword",pwd);
	        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try {
			sslServersocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(<xsl:choose><xsl:when test="//task/code/port"><xsl:value-of select="//task/code/port"/></xsl:when>
         <xsl:otherwise>port</xsl:otherwise>
		 </xsl:choose>
         );
         
			setCipherSuites();
			setProtocols();
			
			sslConnections = new ArrayList&lt;TLSConnection&gt;();
			startAcceptingConnections();
		} catch (IOException ex) {
			System.out.println("Connection to server could not be established. Please check whether the ip/hostname and port are correct");
			ex.printStackTrace();
		}
	}
	
	private static void startAcceptingConnections() throws IOException {
		while (true) {
			sslConnections.add(new TLSConnection((SSLSocket) sslServersocket.accept()));
		}
	}

	public List&lt;TLSConnection&gt; getCurrentConnections() {
		return sslConnections;
	}
        
    private void setCipherSuites() {
		if (sslServersocket != null) {
		//Insert cipher suites here
		sslServersocket.setEnabledCipherSuites(new String[]{
		<xsl:for-each select="//task/element[@type='SecureCommunication']/Ciphersuites">"<xsl:value-of select="."/>",</xsl:for-each>
		});
		}
	}

	private void setProtocols() {
		if (sslServersocket != null) {
			//Insert TLSxx here
			sslServersocket.setEnabledProtocols( new String[]{
			"TLSv1.1", "TLSv1.2" <!-- <xsl:for-each select="//task/element[@type='SecureCommunication']/TlsVersion">"<xsl:value-of select="."/>",</xsl:for-each>-->
			} );
		}
	}
}
</xsl:result-document>

<xsl:result-document href="TLSConnection.java">
package <xsl:value-of select="//task/Package"/>; 
<xsl:apply-templates select="//Import"/>
/** @author CogniCrypt */
public class TLSConnection {

	private SSLSocket sslSocket = null; 
	private static BufferedWriter bufW = null;
	private static BufferedReader bufR = null;

	public TLSConnection(SSLSocket con) {
		sslSocket = con;
	}
	
	public void closeConnection() {
		try {
			if (!sslSocket.isClosed()) {
				sslSocket.close();
			}
		} catch (IOException ex) {
			System.out.println("Could not close channel.");
			ex.printStackTrace();
		}
	}

	public boolean sendData(String content) {
		try {
			bufW.write(content + "\n");
			bufW.flush();
			return true;
		} catch (IOException ex) {
			System.out.println("Sending data failed.");
			ex.printStackTrace();
			return false;
		}
	}

	public String receiveData() {
		try {
			return bufR.readLine();
		} catch (IOException ex) {
			System.out.println("Receiving data failed.");
			ex.printStackTrace();
			return null;
		}
	}

}
</xsl:result-document>
	
package <xsl:value-of select="//Package"/>; 
<xsl:apply-templates select="//Import"/>	
public class Output {

	public void templateUsage(
		 <xsl:choose>
         <xsl:when test="//task/code/port"></xsl:when>
         <xsl:otherwise>,int port</xsl:otherwise></xsl:choose>) {
         //You need to set the right host (first parameter) and the port name (second parameter). If you wish to pass a IP address, please use overload with InetAdress as second parameter instead of string.
		 TLSServer tls = new TLSServer(<xsl:choose><xsl:when test="//task/code/port"><xsl:value-of select="//task/code/port"/></xsl:when>
         <xsl:otherwise>port</xsl:otherwise></xsl:choose>);
		 
		 tls.getCurrentConnections();
		
	}
}
</xsl:when>
<!-- Server code is finished. Remaining code is client: TLS Client or "simply" connect to an HTTPS connection -->
<!-- TLS client implementation -->
<xsl:when test="//task/code/https='false'">
<xsl:result-document href="TLSClient.java">
package <xsl:value-of select="//task/Package"/>; 
<xsl:apply-templates select="//Import"/>
/** @author CogniCrypt */
public class TLSClient {	
	private static SSLSocket sslsocket = null;
	private static BufferedWriter bufW = null;
	private static BufferedReader bufR = null;
	
		
	public TLSClient(<xsl:choose>
         <xsl:when test="//task/code/host"></xsl:when>
         <xsl:otherwise> String host</xsl:otherwise>
		 </xsl:choose>
		 <xsl:choose>
         <xsl:when test="//task/code/port"></xsl:when>
         <xsl:otherwise>,int port</xsl:otherwise>
		 </xsl:choose>
		 	) {
			Properties prop = new Properties();
			InputStream input = null;
			String pwd = null;
			System.setProperty("javax.net.ssl.trustStore","<xsl:value-of select="//task/code/keystore"/>");
			try {
				// If you move the generated code in another package (default of CogniCrypt is Crypto),
				// you need to change the parameter (replacing Crypto with the package name).
				input = Object.class.getClass().getResourceAsStream("/Crypto/clientConfig.properties");
				prop.load(input);
				pwd = prop.getProperty("pwd"); 
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
          System.setProperty("javax.net.ssl.trustStorePassword",pwd);
	        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		try {
			sslsocket = (SSLSocket) sslsocketfactory.createSocket(<xsl:choose>
         <xsl:when test="//task/code/host">
         "<xsl:value-of select="//task/code/host"/>"</xsl:when>
          <xsl:otherwise> host</xsl:otherwise>
		 </xsl:choose>, 
        <xsl:choose>
         <xsl:when test="//task/code/port"><xsl:value-of select="//task/code/port"/></xsl:when>
         <xsl:otherwise>port</xsl:otherwise>
		 </xsl:choose>
         );
			setCipherSuites();
			setProtocols();
			sslsocket.startHandshake();
	        bufW = new BufferedWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
	        bufR = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
		} catch (IOException ex) {
			System.out.println("Connection to server could not be established. Please check whether the ip/hostname and port are correct");
			ex.printStackTrace();
		}
	        
        }
        
        private void setCipherSuites() {
		if (sslsocket != null) {
			//Insert cipher suites here
			sslsocket.setEnabledCipherSuites(new String[]{
			<xsl:for-each select="//task/element[@type='SecureCommunication']/Ciphersuites">"<xsl:value-of select="."/>",</xsl:for-each>
			});
		}
	}

	private void setProtocols() {
		if (sslsocket != null) {
			//Insert TLSxx here
			sslsocket.setEnabledProtocols( new String[]{
			"TLSv1.1", "TLSv1.2" <!-- <xsl:for-each select="//task/element[@type='SecureCommunication']/TlsVersion">"<xsl:value-of select="."/>",</xsl:for-each>-->
			} );
		}
	}
	
	public void closeConnection() {
		try {
		if (!sslsocket.isClosed()) {
			sslsocket.close();
		}
		} catch (IOException ex) {
			System.out.println("Could not close channel.");
			ex.printStackTrace();
		}
	}
	
	public boolean sendData(String content) {
		try {
			bufW.write(content + "\n");
			bufW.flush();
			return true;
		} catch (IOException ex) {
			System.out.println("Sending data failed.");
			ex.printStackTrace();
			return false;
		}
	}
	
	public String receiveData() {
		try {
			return bufR.readLine();
		} catch (IOException ex) {
			System.out.println("Receiving data failed.");
			ex.printStackTrace();
			return null;
		}
	}
	
}
</xsl:result-document>

package <xsl:value-of select="//Package"/>; 
<xsl:apply-templates select="//Import"/>	
public class Output {

	public void templateUsage(<xsl:choose>
         <xsl:when test="//task/code/host"></xsl:when>
         <xsl:otherwise>String host</xsl:otherwise>
		 </xsl:choose>
		 <xsl:choose>
         <xsl:when test="//task/code/port"></xsl:when>
         <xsl:otherwise>,int port</xsl:otherwise></xsl:choose>) {
         //You need to set the right host (first parameter) and the port name (second parameter). If you wish to pass a IP address, please use overload with InetAdress as second parameter instead of string.
		 TLSClient tls = new TLSClient(<xsl:choose>
         <xsl:when test="//task/code/host"></xsl:when>
         <xsl:otherwise>host</xsl:otherwise>
		 </xsl:choose>
		 <xsl:choose>
         <xsl:when test="//task/code/port"></xsl:when>
         <xsl:otherwise>, port</xsl:otherwise>
		 </xsl:choose>);
		 
		 Boolean sendingSuccessful = tls.sendData("");
		 String data = tls.receiveData();
		
		tls.closeConnection();		
	}
	
	
}
</xsl:when>
<!-- Code template for the remaining option: HTTPS client connection -->
<xsl:otherwise>
<xsl:result-document href="HTTPSConnection.java">
package <xsl:value-of select="//task/Package"/>; 
<xsl:apply-templates select="//Import"/>
/** @author CogniCrypt */
public class HTTPSConnection {	
  private static HttpsURLConnection con = null;
  private static SSLSocket socket = null;
  private static BufferedReader reader = null;
  private static BufferedWriter writer = null;

  public HTTPSConnection(<xsl:choose><xsl:when test="//task/code/host">
            	"<xsl:value-of select="//task/code/host"/>
            </xsl:when><xsl:otherwise>
            	String host
         </xsl:otherwise></xsl:choose>) {
  url = new URL(host)
  /** ToDo Check against documentation because of default implementation of HostnameVerifier and SSLSocketFactory
  con = new HttpsURLConnection(host)  
  socket = con.getSSLSocketFactory()
  reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
  }
  
  	public void closeConnection() {
		try {
		if (!sslsocket.isClosed()) {
			sslsocket.close();
		}
		} catch (IOException ex) {
			System.out.println("Could not close channel.");
			ex.printStackTrace();
		}
	}
	
	public String receiveData() {
		try {
			return reader.readLine();
		} catch (IOException ex) {
			System.out.println("Receiving data failed.");
			ex.printStackTrace();
			return null;
		}
	}

	public boolean sendData(String content) {
		try {
			writer.write(content + "\n");
			writer.flush();
			return true;
		} catch (IOException ex) {
			System.out.println("Sending data failed.");
			ex.printStackTrace();
			return false;
		}
	}
	
}
</xsl:result-document>
</xsl:otherwise>
</xsl:choose>

</xsl:if>


</xsl:template>

<xsl:template match="Import">
import <xsl:value-of select="."/>;
</xsl:template>


</xsl:stylesheet>

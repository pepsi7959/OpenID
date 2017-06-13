package ega.oauth;

import java.net.URLEncoder;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class EGATestApi extends DefaultApi10a{

   private static final String EGA_AUTHORIZE_URL = "http://testopenid.ega.or.th/OAuth.ashx?oauth_token=%s";
   private static final String EGA_OAUTH_URL = "http://testopenid.ega.or.th/OAuth.ashx";
   private static final String EGA_XML_URL = "http://testopenid.ega.or.th/XmlUserInfo.aspx?AccessToken=%s";
   private static String CONSUMERKEY = "sampleconsumer";
   private static String CONSUMERSECRET = "samplesecret";
   
   @Override
   public String getAccessTokenEndpoint()
   {
     return EGA_OAUTH_URL;
   }

   @Override
   public String getRequestTokenEndpoint()
   {
     return EGA_OAUTH_URL;
   }

   @Override
   public String getAuthorizationUrl(Token requestToken)
   {
     return String.format(EGA_AUTHORIZE_URL, URLEncoder.encode(requestToken.getToken()));
   }
   
   public static String getXMLUrl(Token accessToken)
   {
	   return String.format(EGA_XML_URL, URLEncoder.encode(accessToken.getToken()));
   }
   
   public static String getConsumerKey()
   {
	   return CONSUMERKEY;
   }
   
   public static String getConsumerSecret()
   {
	   return CONSUMERSECRET;
   }
}

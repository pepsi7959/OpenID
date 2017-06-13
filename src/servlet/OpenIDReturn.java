package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchResponse;

/**
 * Servlet implementation class OpenIDReturn
 */
@WebServlet("/OpenIDReturn")
public class OpenIDReturn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OpenIDReturn() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		 String email, fullname, citizenId, citizenIdVerifyLv, uuid;
		 ConsumerManager manager = (ConsumerManager) request.getSession().getAttribute("manager");
		 PrintWriter out = response.getWriter();
		 if(manager != null)
		 {
			 VerificationResult vResult = this.verifyResponse(manager, request);
			 //แกะผลการยืนยันข้อมูล
			 Identifier verified = vResult.getVerifiedId();
		     if(verified != null)
		     {
	    	    AuthSuccess authSuccess = (AuthSuccess) vResult.getAuthResponse();

                if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX))
                {
                	try
                	{
	                    FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);
	                    
	                    System.out.println(fetchResp.getAttributes());
	                    // ดึงค่าที่ได้รับคืนมาผ่าน Type URI 
	                    email = fetchResp.getAttributeValueByTypeUri("http://axschema.org/contact/email").trim();
	                    fullname = fetchResp.getAttributeValueByTypeUri("http://axschema.org/namePerson").trim();
	                    citizenId = fetchResp.getAttributeValueByTypeUri("http://www.egov.go.th/2012/identifier/citizenid").trim();
	                    citizenIdVerifyLv = fetchResp.getAttributeValueByTypeUri("http://www.egov.go.th/2012/identifier/citizenidverifiedlevel").trim();
	                    //uuid = fetchResp.getAttributeValueByTypeUri("http://www.egov.go.th/2012/ identifier/uuid").trim();
	                    out.println("Email : " + email);
		   			    out.println("Fullname : " + fullname);
		   			    out.println("Citizen ID : " + citizenId);	
		   			    //out.println("UUID : " + uuid);
		   			    out.println("Test By pepsi");
                	}
                	catch(Exception e)
                	{
                		out.println(e.getMessage());
                	}
                }
			    
		     }
		     else
		     {
		    	 out.println("Local Signature Verification Error : Your are not using UTF-8 as a default CharacterEncoding !!!!");
		     }
		 }
		 else
		 {
			 out.println("No manager Session!!!!!");		 
		 }
		  
		 out.println("<a href=\"" + request.getContextPath() + "\" >back</a>");
		 //out.println("<a href=\""+"https://www.google.co.th/"+ "\" >back</a>");
		
	}
	
	// ทำการอ่านค่าจาก OpenID Response
    public VerificationResult verifyResponse(ConsumerManager manager, HttpServletRequest httpReq)
            throws UnsupportedEncodingException
    {
    	
        try
        {
            // ดึงข้อมูลที่ของ openID endpoint ที่ทำการเก็บไว้ตอนทำ Request
            DiscoveryInformation discovered = (DiscoveryInformation) httpReq.getSession()
                    .getAttribute("openid-disc");

            // สกัดข้อมูลออกมาจาก hrrp request
            StringBuffer receivingURL = httpReq.getRequestURL();

            String queryString = httpReq.getQueryString();
            if (queryString != null && queryString.length() > 0)
                receivingURL.append("?").append(httpReq.getQueryString());

            ParameterList paramList = ParameterList.createFromQueryString(queryString);

            // ทำการยืนยันข้อมูล ที่ได้รับมาจาก OpenID Response กับข้อมูลที่ใช้ทำ Request ว่า
            // ถูกส่งมาจาก OpenID endpoint จริง
            VerificationResult verification = manager.verify(receivingURL.toString(), paramList,
                    discovered);

            return verification;
        }
        catch (OpenIDException e)
        {
            // present error to the user
            e.printStackTrace();
        }
        return null;
    }

}

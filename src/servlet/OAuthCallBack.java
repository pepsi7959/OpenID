package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import ega.oauth.EGATestApi;

/**
 * Servlet implementation class OAuthCallBack
 */
@WebServlet("/OAuthCallBack")
public class OAuthCallBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuthCallBack() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.procressRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void procressRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// สร้าง OAuthConsumer Object
		// Provider 
		// ในกรณี Test Server ใช้ EGATestApi
		// ในกรณี e-Service ภาคประชาชนใช้ EGACitizenApi
		// ในกรณี e-Service เจ้าหน้าที่รัฐใช้ EGAGovernmentAgentApi
		OAuthService service = new ServiceBuilder()
	        .provider(EGATestApi.class)
	        .apiKey(EGATestApi.getConsumerKey())
	        .apiSecret(EGATestApi.getConsumerSecret())
	        .build();
		
		// get ค่า Request Token จาก Session
		Token requestToken = (Token)request.getSession().getAttribute("oauthRequestToken");
		PrintWriter out = response.getWriter();
		
		if(requestToken != null)
		{
			// get ค่า Verifier จาก Querystring
			String ver = request.getParameter("oauth_verifier");
			if(ver != null && ver.length() > 0)
			{
				// ทำการขอ Access Token
				Verifier v = new Verifier(ver);
				Token accessToken = service.getAccessToken(requestToken, v); 
				
				// นำ Access Token ไปแลก xml
				response.sendRedirect(EGATestApi.getXMLUrl(accessToken));
			}
			else
			{
				out.println("Invalid OAuth request");
			}
		}
		else
		{
			out.println("Cannot find Request Token");
		}

	}
}

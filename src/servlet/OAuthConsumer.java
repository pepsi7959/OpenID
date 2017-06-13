package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import ega.oauth.EGATestApi;

/**
 * Servlet implementation class OAuthConsumer
 */
@WebServlet("/OAuthConsumer")
public class OAuthConsumer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String callBackUrl;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuthConsumer() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// กำหนดหน้า callback เพื่อทำขั้นตอน D-G ตามเอกสารอบรม
		callBackUrl = request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath());
		callBackUrl = callBackUrl + "/OAuthCallBack";
		// ทำขั้นตอน A-C ตามเอกสารอบรม
		System.out.println("Callback URL: "+callBackUrl);
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
        .scope("http://tempuri.org/IDataApi/GetName")
        .callback(callBackUrl)
        .build();
		
		// ขอ Request Token
		Token requestToken = service.getRequestToken();
		
		// เก็บ Request Token ใส่ Session
		request.getSession().setAttribute("oauthRequestToken", requestToken);
		
		// ส่งผู้ใช้ไปหน้ายืนยัน
		response.sendRedirect(service.getAuthorizationUrl(requestToken));

	}
}

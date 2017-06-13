package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ax.FetchRequest;

/**
 * Servlet implementation class SSOLogin
 */
@WebServlet("/SSOLogin")
public class SSOLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String EGA_ENDPOINT = "http://openid.egov.go.th";
    private ConsumerManager manager;
    private String returnURL;
    private String realm;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSOLogin() {
        super();
        manager = new ConsumerManager();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		System.out.println("GetContextPath: "+request.getContextPath());
		System.out.println(" GetRequestURL: "+request.getRequestURL().toString());
		System.out.println("           URI: "+request.getRequestURI().substring(0));
		realm = request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath());
		returnURL = realm + "/OpenIDReturn";
        System.out.println("Return URL: "+returnURL);
        
		String result = this.processRequest(EGA_ENDPOINT, request, response);
		if(result.length() > 0)
		{
			PrintWriter rw = response.getWriter();
			rw.println(result);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	 public String processRequest(String userSuppliedString, HttpServletRequest httpReq,
	            HttpServletResponse httpResp) throws IOException
    {
		String result = "";
        try
        {
            // ตั้งค่า Url ที่จะให้ส่งกลับ
            String returnToUrl = returnURL;


            // ตั้งค่า endpoint 
            System.out.println("UserSuppliedString : " + userSuppliedString);
            List discoveries = manager.discover(userSuppliedString);

            // ติดต่อ endpoint เพื่อขอข้อมูลการทำ Authentication
            DiscoveryInformation discovered = manager.associate(discoveries);

            // เก็บข้อมูลลง Session
            httpReq.getSession().setAttribute("openid-disc", discovered);

            // สร้าง OpenID Authentication Request
            AuthRequest authReq = manager.authenticate(discoveries, returnToUrl, realm);

            // ระบุ Attribute AX ที่ต้องการ
            FetchRequest fetch = FetchRequest.createFetchRequest();
            if (userSuppliedString.startsWith(EGA_ENDPOINT))
            {
                fetch.addAttribute("namePerson", "http://axschema.org/namePerson", true);
                fetch.addAttribute("citizenid", "http://www.egov.go.th/2012/identifier/citizenid", false);
                fetch.addAttribute("email", "http://axschema.org/contact/email", false);
               // fetch.addAttribute("uuid", "http://www.egov.go.th/2012/ identifier/uuid", false);
            }

            // เพิ่ม Attribute เข้ากับ Request
            authReq.addExtension(fetch);

            // เก็บข้อมูลการทำ Request ลง Session เพื่อทำการ cross check ภายหลัง
            httpReq.getSession().setAttribute("manager", manager);

            // ส่ง OpenID Authentication Request
            httpResp.sendRedirect(authReq.getDestinationUrl(true));
        }
        catch (OpenIDException e)
        {
        	result = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

}

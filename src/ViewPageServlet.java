import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

/**
 * Display either a welcome page or an account page.
 * 
 * @author Joseph Eichenhofer
 */
public class ViewPageServlet extends HttpServlet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 * 
	 * Serve one of two "pages" showing either a login form (if no session is
	 * registered with the request) or the account page  that are associated
	 * with the session (if one exists).
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		PrintWriter content = res.getWriter();

		HttpSession session = req.getSession(false);

		// set encoding/type
		res.setContentType("text/html; charset=utf-8");
		// set good status code
		res.setStatus(HttpServletResponse.SC_OK);

		if (session == null) {
				
			// no session, show "login" form
			content.println("<!DOCTYPE html>");
                        content.println("<html>");
                        content.println("<head>");
                        content.println("<title>Welcome Page</title>");
                        content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"WelcomePage.css\">");
                        content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"Navbar.css\">");
                        content.println("</head>");

                        content.println("<body>");

                        content.println("<img src=\"./color-flush-reverse-UWlogo-print.png\" width=\"270\" height=\"90\" class=\"d-inline-block align-top\" alt=\"\">");
			content.println("<div id=\"container\">");	
			content.println("<h1>");
			content.println("WisClick");
			content.println("</h1>");

			// write out form for setting username
			content.println("<form action=\"welcome\" method=\"POST\"");
			content.println("accept-charset=\"utf-8\">");
			content.println("USERNAME: <input type=\"text\" name=\"username\">");
			content.println("PASSWORD: <input type=\"password\" name=\"password\">");
			content.println("<input type=\"submit\" value=\"Submit\">");
			content.println("</form>");
			
			content.println("</div>");
			content.println("</body>");
			content.println("</html>");
		} else {
			res.sendRedirect("/account");
		}
	}

	 /*
         * (non-Javadoc)
         * 
         * @see
         * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
         * javax.servlet.http.HttpServletResponse)
         * 
         * Retrieve username and password from HttpServletRequest.
         * Authenticate the username and password
	 * if the username does not exits, create a new account 
         * 
         */
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String username = req.getParameter("username");
                String password = req.getParameter("password");
        
                if (username != null && !username.equals(new String("")) && Database.authenticateUser(username, password)) {
                        req.getSession(true).setAttribute("username", username);
                        req.getSession().setAttribute("clicks", new Integer(0));
                        
                        // redirect to main page
                        res.sendRedirect("/account");
                }
                else {
                        
                        res.sendRedirect("/welcome");
                }	
	}
}

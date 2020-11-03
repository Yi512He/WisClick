import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

/**
 * Java servlet for displaying the ranking data retrieved from database.
 * 
 * @author Emma He
 */
public class RankListPageServlet extends HttpServlet {

	/*
         * (non-Javadoc)
         * 
         * @see
         * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
         * javax.servlet.http.HttpServletResponse)
         * 
         * Retrieve the session from HttpServletRequest.
	 * Retrieve the data from the database.
         * Display the top five ranking on the page. 
         * 
         */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
			HttpSession session = req.getSession(false);

			if(session == null){
				res.sendRedirect("/welcome");
			}
			else {
				// Set up the response content
				PrintWriter content = res.getWriter();
				res.setContentType("text/html; charset=htf-8");
				res.setStatus(HttpServletResponse.SC_OK);

				String username = (String) session.getAttribute("username");

				// Get the data from the database 
				ArrayList<String[]> rankList = Database.getRankList();
			
				// Header of the HTML page, declares the title and css files
				content.println("<!DOCTYPE html>");
				content.println("<html>");
				content.println("<head>");
				content.println("<title>Ranking Page</title>");
				content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"./RankList.css\">");	
				content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"./Navbar.css\">");
				content.println("</head>");

				// Body of the HTML page
				content.println("<body>");
			
				// Logo, course name, and navigation bar
				content.println("<div id=\"headerNav\">");
				content.println("<img src=\"./color-flush-reverse-UWlogo-print.png\" width=\"270\" height=\"90\" class=\"d-inline-block align-top\">");
				content.println("<span class=\"hello\">Logged in as "+ username +"</span>");
                        	content.println("<div id=\"stripe\">");
                        	content.println("<form action=\"logout\" method=\"POST\" class=\"logoutForm\"");
                        	content.println("accept-charset=\"utf-8\">");
                        	content.print("<a class=\"nav\" href=\"account\">My Account</a>");
                        	content.print("<a class=\"nav\" href=\"earn\">Earn Credits</a>");
                        	content.print("<a class=\"nav\" href=\"transfer\">Transfer Credits</a>");
                        	content.print("<a class=\"nav\" href=\"rank\">Ranking</a>");
                        	content.print("<input value=\"Log Out\" type=\"submit\" class=\"logoutInput nav\"></form>");
                        	content.println("</div></div>");

				// Headline and the ranking list
				content.println("<h1> Ranking </h1>");
				content.println("<div id=\"container\">");
				content.println("<ul>");
				for(int i = 0; i < rankList.size(); i++){
					content.println("<li>");
					content.println("<span class=\"rankingNum\">" + (i+1) + "</span>");
					content.println("<a href=\"account?username=" + rankList.get(i)[0] + "\">" + rankList.get(i)[0] + "</a>");
					content.println("<span class=\"rankingCredits\">" + rankList.get(i)[1] + "</span>");
					content.println("</li>");
				}
				content.println("</ul>");
				content.println("</div>");
			
				// Close the tags
				content.println("</body>");
				content.println("</html>");
			}

	}

}

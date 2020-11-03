import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Java servlet for displaying the earn points page and handle the points change.
 *
 * @author Emma He
 */
public class EarnPointsPageServlet extends HttpServlet {
	
	/*
         * (non-Javadoc)
         * 
         * @see
         * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
         * javax.servlet.http.HttpServletResponse)
         * 
         * Retrieve the session from HttpServletRequest.
         * Display the earn points page for users to 
	 * click the button to earn points, 
	 * and add points to their own account. 
         * 
         */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// Get session information
		HttpSession session = req.getSession(false);

		// if no session, go to welcome page; otherwise, write out earn points page
		if(session == null){
			res.sendRedirect("/welcome");
		} else {
			// Get the username from session
                        String username = (String) session.getAttribute("username");
			
			// Set up the response content
			PrintWriter content = res.getWriter();		
			res.setContentType("text/html; charset+utf-8");
			res.setStatus(HttpServletResponse.SC_OK);

			// Header of the HTML page, declares the title and css files
 			content.println("<!DOCTYPE>");
                        content.println("<html>");
                        content.println("<head>");
                        content.println("<title>Earn Credits Page</title>");
                        content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"EarnPoints.css\">");
                        content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"Navbar.css\">");
                        content.println("</head>");

			// Body of the HTML page
                        content.println("<body>");

			// Logo, course name, and navigation bar
			content.println("<div id=\"headerNav\">");
                        content.println("<img src=\"./color-flush-reverse-UWlogo-print.png\" width=\"270\" height=\"90\" class=\"d-inline-block align-top\" alt=\"\">");
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

			// Headline
                        content.println("<h1>");
                        content.println("Welcome, " + username + "!");
                        content.println("</h1>");

			// Display the button-clicking game and allow user to add points earned
 			content.println("<div id=\"container\">");
        		content.println("<div class=\"header\"><h2>Click to Earn</h2></div>");
        		content.println("<div class=\"line\" id=\"message\"></div>");
        		content.println("<div class=\"line point\"><span id=\"click\">" + Database.getPoints(username) + "</span> Credits</div>");
        		content.println("<div class=\"line\"></div>");
        		content.println("<div class=\"line\">");
                	content.println("<button class=\"buttonLike\">CLICK ME</button>");
        		content.println("</div></div>");
			
			// Declare the js file, and then close the tags
			content.println("<script type=\"text/javascript\" src=\"EarnPoints.js\"></script>");
  			content.println("</body>");
			content.println("</html>");
		}
	}

	/*
         * (non-Javadoc)
         * 
         * @see
         * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
         * javax.servlet.http.HttpServletResponse)
         * 
         * Retrieve the session from HttpServletRequest.
         * Retrieve the data from XMLHttpRequest. 
         * Add the data to the database, and send a message with response.
         * 
         */
	@Override
        protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// Get session information
                HttpSession session = req.getSession(false);

		// if no session, go to the welcome page, otherwise, proceeding update data in earn points page
                if (session != null) {
			// Get the username from sessiion	
			String username = (String) session.getAttribute("username");

			// Set up the response content
			res.setContentType("text/html; charset=utf-8");
			res.setStatus(HttpServletResponse.SC_OK);
			res.setHeader("Cache-Control", "no-cache");
			res.setHeader("Pragma", "no-cache");
			PrintWriter out = res.getWriter();

			Boolean result = Database.setPoints(username, new Integer("1"));

			// If data is added successfully, response with success message.
			// Otherwise, response with failure message.
			if(result){	
				out.print("Success!");
			}
			else{
				out.print("Fail!");
			}
                        
                } else {
                        res.sendRedirect("/welcome");
                }
        }



}

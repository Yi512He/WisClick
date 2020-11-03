import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

/**
 * Java servlet for displaying the transfer page, get user input, and call doTransfer servlet.
 *
 * @author Emma He
 */
public class TransferPageServlet extends HttpServlet {

	/*
         * (non-Javadoc)
         * 
         * @see
         * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
         * javax.servlet.http.HttpServletResponse)
         * 
         * Retrieve the session from HttpServletRequest.
         * Display the transfer result if it can be retreived from the response.
         * Display the transfer page to get three parameters for transferring: 
         * to, the user transferred to,
         * points, the amount of points to transfer, 
         * and submit, to call doTransfer servlet to do the transfer. 
         * 
         */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// Get session information
		HttpSession session = req.getSession(false);

		// if no session, go to welcome page; otherwise, write out transfer page
		if(session == null){
			res.sendRedirect("/welcome");
		} else {
			// Get the response message from session
			String result = (String) session.getAttribute("result");
			String username = (String) session.getAttribute("username");
			Integer credits = (Integer) session.getAttribute("credit");
		
			// Set up the response content	
			PrintWriter content = res.getWriter();
			res.setContentType("text/html; charset=utf-8");
			res.setStatus(HttpServletResponse.SC_OK);
	
			// Header of the HTML page, declare the title and CSS files
			content.println("<!DOCTYPE html>");
                	content.println("<html>");
                	content.println("<head>");
			content.println("<title>Transfer Page</title>");
                	content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"./Transfer.css\">");
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
	
			// Headline
			content.println("<h1>Transfer Your Credits</h1>");

			// Form for getting transfer information
    			content.println("<div id=\"container\">");
			
			if(credits != null && credits != -1){
				content.println("<div class=\"header\"><h2> My Credits: " + credits + "</h2></div>");
				session.setAttribute("credit", -1);
			}
			else {
				content.println("<div class=\"header\"><h2> My Credits: " + Database.getPoints(username) + "</h2></div>");
        		}
			content.println("<form action=\"transfer\" method=\"POST\"");
			content.println("accept-charset=\"utf-8\">");

			// if response message is available, display; otherwise, no display
			if(result != null && result != ""){
				content.println("<div class=\"line\">" + result + "</div>");
				session.setAttribute("result", "");

			}
			else {
        			content.println("<div class=\"line\"></div>");
			}
			
			// Getting three parameters: to, points, submit
        		content.println("<div class=\"line\"><span class=\"param\">To:</span> <input type=\"text\" name=\"to\"></div>");
        		content.println("<div class=\"line\"><span class=\"param\">Credits:</span> <input type=\"text\" name=\"pointsToTransfer\"></div>");
        		content.println("<div class=\"line\"><input id=\"submitButton\" type=\"submit\" value=\"Submit\"></div>");
        		content.println("</form></div>");
			
			// Close the tag for the HTML page
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
         * Retrieve the data from the request
         * Connect to the database to transfer the point 
         * If success, send back success message
         * If fail, send back fail message
	 * 
         */
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// Get session information
                HttpSession session = req.getSession(false);

                res.setContentType("text/html; charset=utf-8");
                res.setStatus(HttpServletResponse.SC_OK);

                // Get two parameters: the user to whom credits are trasferred and the number of credits to transfer
                String to = req.getParameter("to");
                String pointsToTransfer = req.getParameter("pointsToTransfer");
                int addPoints = 0;
                if(pointsToTransfer != null){
                        try {
                                addPoints = Integer.parseInt(pointsToTransfer);
                        } catch (Exception ex){
                                addPoints = 0;
                        }
                }
                int deductPoints = 0 - addPoints;

                // If session is not valid, go back to welcome page
                if(session == null){
                        res.sendRedirect("/welcome");
                } else {
                        // Connect to the database, complete the transfer and store the result in session
                        session.setAttribute("result", "Fail to transfer.");
                        String from = (String) session.getAttribute("username");
                        if(addPoints != 0 && to != null && addPoints > 0 && from != null && !from.equals(to)){
                                int result = Database.transferPoints(from, to, addPoints);
                                if(result >= 0){
                                        session.setAttribute("result", "Success!");
                                        session.setAttribute("credit", result);
                                }
                        }
                        res.sendRedirect("/transfer");
                }

	}
}

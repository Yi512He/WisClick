/**
 * Used for earn point page
 *
 * Handle one button clicks and send one POST request 
 *
 * @link   /earn
 * @file   EarnPointsPageServlet.java
 * @author Emma He
 */

// select the submit button and points display
var clickButton = document.querySelectorAll(".buttonLike");
var pointsDisplay = document.getElementById("click");

/*
 * This function is called to send the POST request to the server to
 * increment one credit to the usuer
 * @param reqURL: the URL to send the POST request 
 */
function ajaxSyncRequest(reqURL){
	var xhs;
	if(window.XMLHttpRequest){
		xhs = new XMLHttpRequest();
	} else {
		xhs = new ActiveXObject("Microsoft.XMLHTTP");
	}

	xhs.open("POST", reqURL, true);
	
	xhs.onreadystatechange = function() {
		if(this.readyState === XMLHttpRequest.DONE && this.status === 200){
			document.getElementById("message").innerHTML = xhs.responseText;
			if(xhs.responseText == "Success!"){
				var pointsCount = pointsDisplay.innerHTML;
				pointsCount++;
				pointsDisplay.innerHTML = pointsCount;

			}
		}
		else {
			console.log("Still Waiting!!!");
		}
	};

	xhs.send(null);

}

// if click button is clicked, send one request
clickButton[0].addEventListener("click", function(){
	ajaxSyncRequest("/earn");
	
});

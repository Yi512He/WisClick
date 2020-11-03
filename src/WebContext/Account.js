/**
 * Used for account page
 *
 * Handle two button clicks and send one POST request 
 *
 * @link   /account
 * @file   AccountPageServlet.java
 * @author Emma He
 */

// Select all buttons in the account page, 
// Once it is clicked, show an inputfield
var buttonEdit = document.querySelector("#edit");

/*
 * This function is called when edit/cancel button is clicked
 * It changes the visibility of the input field
 */
buttonEdit.addEventListener("click", function(){
	if(this.innerHTML === "Edit"){
		this.previousElementSibling.previousElementSibling.classList.add("inputDisplay");
		this.previousElementSibling.previousElementSibling.classList.remove("inputField");
		this.innerHTML = "Cancel";
		this.style.marginLeft = "35%";
		this.previousElementSibling.style.display = "inline";
	}
	else {	
		this.previousElementSibling.previousElementSibling.classList.add("inputField");
		this.previousElementSibling.previousElementSibling.classList.remove("inputDisplay");
		this.innerHTML = "Edit";
		this.style.marginLeft = "80%";
		this.previousElementSibling.style.display = "none";
	}	
});

// select the submit button and profile input field
var buttonSubmit = document.querySelector("#submit");
var profileField = document.querySelector("#profile");

/*
 * This function is called when the submit button is cliekd.
 * It will send a POST request with content of profile using Ajax
 */
buttonSubmit.addEventListener("click", function(){
	var x = document.querySelector("textarea").value;
	//console.log(x.replace(new RegExp('\n', 'g'), "<br>"));
	//profileField.innerHTML = x.replace(new RegExp("\n", 'g'), "<br>");
	this.previousElementSibling.classList.add("inputField");
	this.previousElementSibling.classList.remove("inputDisplay");
	this.nextElementSibling.innerHTML = "Edit";
	this.nextElementSibling.style.marginLeft = "80%";
	this.style.display = "none";
	ajaxSyncRequest("/account", x);
});


/*
 * This function is called when submit button is clicked to trigger
 * a POST request.
 * @param reqURL: the URL to send the POST request
 * @param parameter: the content of profile
 */
function ajaxSyncRequest(reqURL, parameter){
	var xhs;
	if(window.XMLHttpRequest){
		xhs = new XMLHttpRequest();
	} else {
		xhs = new ActiveXObject("Microsoft.XMLHTTP");
	}
 
	xhs.open("POST", reqURL, true);
	xhs.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	xhs.onreadystatechange = function() {
		if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
			console.log(xhs.responseText);	
			console.log(parameter);
			document.querySelector("#profile").innerHTML = parameter;
		}
		else
		{
			console.log('Still Waiting!!');
		}


	};
	xhs.send("value=" + parameter);
}

var ok = true;
onload=function() {
	ping();
}

function get(url) {
	xmlHttp=GetXmlHttpObject();
	if (xmlHttp==null) {
  		return;
  	} 
  	url=url+"?sid="+Math.random();
  	try {
		xmlHttp.open("GET",url,true);
		xmlHttp.onreadystatechange=function() {
	  		if (xmlHttp.readyState==4) {
				if (xmlHttp.status!=200) {
					ok = false;
				} else {
					var t=setTimeout("ping()",5000);
				}
			}
		}
		xmlHttp.send(null);
	} catch (e) {}
}
function GetXmlHttpObject() {
	var xmlHttp=null;
	try {
  		// Firefox, Opera 8.0+, Safari
  		xmlHttp=new XMLHttpRequest();
  	} catch (e) {
  	// Internet Explorer
  		try {
    		xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
    	} catch (e)	{
    		xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
	return xmlHttp;
}
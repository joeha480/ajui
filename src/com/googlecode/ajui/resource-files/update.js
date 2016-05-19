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
					var data = xmlHttp.responseXML;
					for (i=0; i<data.getElementsByTagName("update").length; i++) {
						var u = data.getElementsByTagName("update")[i];
						since = u.getAttribute('time');
						var elem = document.getElementById(u.getAttribute('id'));
						if (elem!=null) {
							if (elem.hasChildNodes()) {
							    while (elem.childNodes.length >= 1)
							    {
							    	elem.removeChild(elem.firstChild);       
							    } 
							}
							if (u.hasChildNodes()) {
								try {
									// Gecko- and Webkit-based browsers (Firefox, Chrome), Opera.
									elem.innerHTML = (new XMLSerializer()).serializeToString(u);
								} catch (e) {
								    try {
										elem.innerHTML = u.xml;
									} catch (e) {  
										//Other browsers without XML Serializer
									    elem.innerHTML = 'Error';
									}
								}
								
							}
						}
					}
					var t=setTimeout("ping()",1000);
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
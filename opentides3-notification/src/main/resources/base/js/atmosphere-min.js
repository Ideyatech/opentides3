(function(a,b){if(typeof define==="function"&&define.amd){define(b)
}else{a.atmosphere=b()
}}(this,function(){var c="2.2.2-javascript",a={},d,g=[],f=[],e=0,b=Object.prototype.hasOwnProperty;
a={onError:function(h){},onClose:function(h){},onOpen:function(h){},onReopen:function(h){},onMessage:function(h){},onReconnect:function(i,h){},onMessagePublished:function(h){},onTransportFailure:function(i,h){},onLocalMessage:function(h){},onFailureToReconnect:function(i,h){},onClientTimeout:function(h){},WebsocketApiAdapter:function(i){var h,j;
i.onMessage=function(k){j.onmessage({data:k.responseBody})
};
i.onMessagePublished=function(k){j.onmessage({data:k.responseBody})
};
i.onOpen=function(k){j.onopen(k)
};
j={close:function(){h.close()
},send:function(k){h.push(k)
},onmessage:function(k){},onopen:function(k){},onclose:function(k){},onerror:function(k){}};
h=new a.subscribe(i);
return j
},AtmosphereRequest:function(N){var P={timeout:300000,method:"GET",headers:{},contentType:"",callback:null,url:"",data:"",suspend:true,maxRequest:-1,reconnect:true,maxStreamingLength:10000000,lastIndex:0,logLevel:"info",requestCount:0,fallbackMethod:"GET",fallbackTransport:"streaming",transport:"long-polling",webSocketImpl:null,webSocketBinaryType:null,dispatchUrl:null,webSocketPathDelimiter:"@@",enableXDR:false,rewriteURL:false,attachHeadersAsQueryString:true,executeCallbackBeforeReconnect:false,readyState:0,withCredentials:false,trackMessageLength:false,messageDelimiter:"|",connectTimeout:-1,reconnectInterval:0,dropHeaders:true,uuid:0,async:true,shared:false,readResponsesHeaders:false,maxReconnectOnClose:5,enableProtocol:true,pollingInterval:0,heartbeat:{client:null,server:null},ackInterval:0,onError:function(aB){},onClose:function(aB){},onOpen:function(aB){},onMessage:function(aB){},onReopen:function(aC,aB){},onReconnect:function(aC,aB){},onMessagePublished:function(aB){},onTransportFailure:function(aC,aB){},onLocalMessage:function(aB){},onFailureToReconnect:function(aC,aB){},onClientTimeout:function(aB){}};
var X={status:200,reasonPhrase:"OK",responseBody:"",messages:[],headers:[],state:"messageReceived",transport:"polling",error:null,request:null,partialMessage:"",errorHandled:false,closedByClientTimeout:false,ffTryingReconnect:false};
var aa=null;
var p=null;
var w=null;
var F=null;
var H=null;
var al=true;
var l=0;
var ax=false;
var ab=null;
var ar;
var r=null;
var K=a.util.now();
var L;
var aA;
az(N);
function at(){al=true;
ax=false;
l=0;
aa=null;
p=null;
w=null;
F=null
}function B(){an();
at()
}function M(aC,aB){if(X.partialMessage===""&&(aB.transport==="streaming")&&(aC.responseText.length>aB.maxStreamingLength)){return true
}return false
}function E(){if(P.enableProtocol&&!P.firstMessage){var aD="X-Atmosphere-Transport=close&X-Atmosphere-tracking-id="+P.uuid;
a.util.each(P.headers,function(aF,aH){var aG=a.util.isFunction(aH)?aH.call(this,P,P,X):aH;
if(aG!=null){aD+="&"+encodeURIComponent(aF)+"="+encodeURIComponent(aG)
}});
var aB=P.url.replace(/([?&])_=[^&]*/,aD);
aB=aB+(aB===P.url?(/\?/.test(P.url)?"&":"?")+aD:"");
var aC={connected:false};
var aE=new a.AtmosphereRequest(aC);
aE.attachHeadersAsQueryString=false;
aE.dropHeaders=true;
aE.url=aB;
aE.contentType="text/plain";
aE.transport="polling";
aE.method="GET";
aE.data="";
aE.async=false;
n("",aE)
}}function ao(){if(P.logLevel==="debug"){a.util.debug("Closing")
}ax=true;
if(P.reconnectId){clearTimeout(P.reconnectId);
delete P.reconnectId
}if(P.heartbeatTimer){clearTimeout(P.heartbeatTimer)
}P.reconnect=false;
X.request=P;
X.state="unsubscribe";
X.responseBody="";
X.status=408;
X.partialMessage="";
D();
E();
an()
}function an(){X.partialMessage="";
if(P.id){clearTimeout(P.id)
}if(P.heartbeatTimer){clearTimeout(P.heartbeatTimer)
}if(F!=null){F.close();
F=null
}if(H!=null){H.abort();
H=null
}if(w!=null){w.abort();
w=null
}if(aa!=null){if(aa.canSendMessage){aa.close()
}aa=null
}if(p!=null){p.close();
p=null
}au()
}function au(){if(ar!=null){clearInterval(L);
document.cookie=aA+"=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
ar.signal("close",{reason:"",heir:!ax?K:(ar.get("children")||[])[0]});
ar.close()
}if(r!=null){r.close()
}}function az(aB){B();
P=a.util.extend(P,aB);
P.mrequest=P.reconnect;
if(!P.reconnect){P.reconnect=true
}}function q(){return P.webSocketImpl!=null||window.WebSocket||window.MozWebSocket
}function T(){return window.EventSource
}function u(){if(P.shared){r=ai(P);
if(r!=null){if(P.logLevel==="debug"){a.util.debug("Storage service available. All communication will be local")
}if(r.open(P)){return
}}if(P.logLevel==="debug"){a.util.debug("No Storage service available.")
}r=null
}P.firstMessage=e==0?true:false;
P.isOpen=false;
P.ctime=a.util.now();
if(P.uuid===0){P.uuid=e
}X.closedByClientTimeout=false;
if(P.transport!=="websocket"&&P.transport!=="sse"){t(P)
}else{if(P.transport==="websocket"){if(!q()){R("Websocket is not supported, using request.fallbackTransport ("+P.fallbackTransport+")")
}else{ak(false)
}}else{if(P.transport==="sse"){if(!T()){R("Server Side Events(SSE) is not supported, using request.fallbackTransport ("+P.fallbackTransport+")")
}else{J(false)
}}}}}function ai(aF){var aG,aE,aJ,aB="atmosphere-"+aF.url,aC={storage:function(){function aK(aO){if(aO.key===aB&&aO.newValue){aD(aO.newValue)
}}if(!a.util.storage){return
}var aN=window.localStorage,aL=function(aO){return a.util.parseJSON(aN.getItem(aB+"-"+aO))
},aM=function(aO,aP){aN.setItem(aB+"-"+aO,a.util.stringifyJSON(aP))
};
return{init:function(){aM("children",aL("children").concat([K]));
a.util.on(window,"storage",aK);
return aL("opened")
},signal:function(aO,aP){aN.setItem(aB,a.util.stringifyJSON({target:"p",type:aO,data:aP}))
},close:function(){var aO=aL("children");
a.util.off(window,"storage",aK);
if(aO){if(aH(aO,aF.id)){aM("children",aO)
}}}}
},windowref:function(){var aK=window.open("",aB.replace(/\W/g,""));
if(!aK||aK.closed||!aK.callbacks){return
}return{init:function(){aK.callbacks.push(aD);
aK.children.push(K);
return aK.opened
},signal:function(aL,aM){if(!aK.closed&&aK.fire){aK.fire(a.util.stringifyJSON({target:"p",type:aL,data:aM}))
}},close:function(){if(!aJ){aH(aK.callbacks,aD);
aH(aK.children,K)
}}}
}};
function aH(aN,aM){var aK,aL=aN.length;
for(aK=0;
aK<aL;
aK++){if(aN[aK]===aM){aN.splice(aK,1)
}}return aL!==aN.length
}function aD(aK){var aM=a.util.parseJSON(aK),aL=aM.data;
if(aM.target==="c"){switch(aM.type){case"open":O("opening","local",P);
break;
case"close":if(!aJ){aJ=true;
if(aL.reason==="aborted"){ao()
}else{if(aL.heir===K){u()
}else{setTimeout(function(){u()
},100)
}}}break;
case"message":G(aL,"messageReceived",200,aF.transport);
break;
case"localMessage":ad(aL);
break
}}}function aI(){var aK=new RegExp("(?:^|; )("+encodeURIComponent(aB)+")=([^;]*)").exec(document.cookie);
if(aK){return a.util.parseJSON(decodeURIComponent(aK[2]))
}}aG=aI();
if(!aG||a.util.now()-aG.ts>1000){return
}aE=aC.storage()||aC.windowref();
if(!aE){return
}return{open:function(){var aK;
L=setInterval(function(){var aL=aG;
aG=aI();
if(!aG||aL.ts===aG.ts){aD(a.util.stringifyJSON({target:"c",type:"close",data:{reason:"error",heir:aL.heir}}))
}},1000);
aK=aE.init();
if(aK){setTimeout(function(){O("opening","local",aF)
},50)
}return aK
},send:function(aK){aE.signal("send",aK)
},localSend:function(aK){aE.signal("localSend",a.util.stringifyJSON({id:K,event:aK}))
},close:function(){if(!ax){clearInterval(L);
aE.signal("close");
aE.close()
}}}
}function ae(){var aC,aB="atmosphere-"+P.url,aG={storage:function(){function aH(aJ){if(aJ.key===aB&&aJ.newValue){aD(aJ.newValue)
}}if(!a.util.storage){return
}var aI=window.localStorage;
return{init:function(){a.util.on(window,"storage",aH)
},signal:function(aJ,aK){aI.setItem(aB,a.util.stringifyJSON({target:"c",type:aJ,data:aK}))
},get:function(aJ){return a.util.parseJSON(aI.getItem(aB+"-"+aJ))
},set:function(aJ,aK){aI.setItem(aB+"-"+aJ,a.util.stringifyJSON(aK))
},close:function(){a.util.off(window,"storage",aH);
aI.removeItem(aB);
aI.removeItem(aB+"-opened");
aI.removeItem(aB+"-children")
}}
},windowref:function(){var aI=aB.replace(/\W/g,""),aH=document.getElementById(aI),aJ;
if(!aH){aH=document.createElement("div");
aH.id=aI;
aH.style.display="none";
aH.innerHTML='<iframe name="'+aI+'" />';
document.body.appendChild(aH)
}aJ=aH.firstChild.contentWindow;
return{init:function(){aJ.callbacks=[aD];
aJ.fire=function(aK){var aL;
for(aL=0;
aL<aJ.callbacks.length;
aL++){aJ.callbacks[aL](aK)
}}
},signal:function(aK,aL){if(!aJ.closed&&aJ.fire){aJ.fire(a.util.stringifyJSON({target:"c",type:aK,data:aL}))
}},get:function(aK){return !aJ.closed?aJ[aK]:null
},set:function(aK,aL){if(!aJ.closed){aJ[aK]=aL
}},close:function(){}}
}};
function aD(aH){var aJ=a.util.parseJSON(aH),aI=aJ.data;
if(aJ.target==="p"){switch(aJ.type){case"send":am(aI);
break;
case"localSend":ad(aI);
break;
case"close":ao();
break
}}}ab=function aF(aH){aC.signal("message",aH)
};
function aE(){document.cookie=aA+"="+encodeURIComponent(a.util.stringifyJSON({ts:a.util.now()+1,heir:(aC.get("children")||[])[0]}))+"; path=/"
}aC=aG.storage()||aG.windowref();
aC.init();
if(P.logLevel==="debug"){a.util.debug("Installed StorageService "+aC)
}aC.set("children",[]);
if(aC.get("opened")!=null&&!aC.get("opened")){aC.set("opened",false)
}aA=encodeURIComponent(aB);
aE();
L=setInterval(aE,1000);
ar=aC
}function O(aD,aG,aC){if(P.shared&&aG!=="local"){ae()
}if(ar!=null){ar.set("opened",true)
}aC.close=function(){ao()
};
if(l>0&&aD==="re-connecting"){aC.isReopen=true;
af(X)
}else{if(X.error==null){X.request=aC;
var aE=X.state;
X.state=aD;
var aB=X.transport;
X.transport=aG;
var aF=X.responseBody;
D();
X.responseBody=aF;
X.state=aE;
X.transport=aB
}}}function A(aD){aD.transport="jsonp";
var aC=P,aB;
if((aD!=null)&&(typeof(aD)!=="undefined")){aC=aD
}H={open:function(){var aF="atmosphere"+(++K);
function aE(){var aG=aC.url;
if(aC.dispatchUrl!=null){aG+=aC.dispatchUrl
}var aI=aC.data;
if(aC.attachHeadersAsQueryString){aG=Y(aC);
if(aI!==""){aG+="&X-Atmosphere-Post-Body="+encodeURIComponent(aI)
}aI=""
}var aH=document.head||document.getElementsByTagName("head")[0]||document.documentElement;
aB=document.createElement("script");
aB.src=aG+"&jsonpTransport="+aF;
aB.clean=function(){aB.clean=aB.onerror=aB.onload=aB.onreadystatechange=null;
if(aB.parentNode){aB.parentNode.removeChild(aB)
}};
aB.onload=aB.onreadystatechange=function(){if(!aB.readyState||/loaded|complete/.test(aB.readyState)){aB.clean()
}};
aB.onerror=function(){aB.clean();
aC.lastIndex=0;
if(aC.openId){clearTimeout(aC.openId)
}if(aC.heartbeatTimer){clearTimeout(aC.heartbeatTimer)
}if(aC.reconnect&&l++<aC.maxReconnectOnClose){O("re-connecting",aC.transport,aC);
S(H,aC,aD.reconnectInterval);
aC.openId=setTimeout(function(){ap(aC)
},aC.reconnectInterval+1000)
}else{ag(0,"maxReconnectOnClose reached")
}};
aH.insertBefore(aB,aH.firstChild)
}window[aF]=function(aI){if(aC.reconnect){if(aC.maxRequest===-1||aC.requestCount++<aC.maxRequest){if(!aC.executeCallbackBeforeReconnect){S(H,aC,aC.pollingInterval)
}if(aI!=null&&typeof aI!=="string"){try{aI=aI.message
}catch(aH){}}var aG=y(aI,aC,X);
if(!aG){G(X.responseBody,"messageReceived",200,aC.transport)
}if(aC.executeCallbackBeforeReconnect){S(H,aC,aC.pollingInterval)
}}else{a.util.log(P.logLevel,["JSONP reconnect maximum try reached "+P.requestCount]);
ag(0,"maxRequest reached")
}}};
setTimeout(function(){aE()
},50)
},abort:function(){if(aB&&aB.clean){aB.clean()
}}};
H.open()
}function j(aB){if(P.webSocketImpl!=null){return P.webSocketImpl
}else{if(window.WebSocket){return new WebSocket(aB)
}else{return new MozWebSocket(aB)
}}}function k(){return Y(P,a.util.getAbsoluteURL(P.webSocketUrl||P.url)).replace(/^http/,"ws")
}function ay(){var aB=Y(P);
return aB
}function J(aC){X.transport="sse";
var aB=ay();
if(P.logLevel==="debug"){a.util.debug("Invoking executeSSE");
a.util.debug("Using URL: "+aB)
}if(aC&&!P.reconnect){if(p!=null){an()
}return
}try{p=new EventSource(aB,{withCredentials:P.withCredentials})
}catch(aD){ag(0,aD);
R("SSE failed. Downgrading to fallback transport and resending");
return
}if(P.connectTimeout>0){P.id=setTimeout(function(){if(!aC){an()
}},P.connectTimeout)
}p.onopen=function(aE){z(P);
if(P.logLevel==="debug"){a.util.debug("SSE successfully opened")
}if(!P.enableProtocol){if(!aC){O("opening","sse",P)
}else{O("re-opening","sse",P)
}}else{if(P.isReopen){P.isReopen=false;
O("re-opening",P.transport,P)
}}aC=true;
if(P.method==="POST"){X.state="messageReceived";
p.send(P.data)
}};
p.onmessage=function(aF){z(P);
if(!P.enableXDR&&aF.origin&&aF.origin!==window.location.protocol+"//"+window.location.host){a.util.log(P.logLevel,["Origin was not "+window.location.protocol+"//"+window.location.host]);
return
}X.state="messageReceived";
X.status=200;
aF=aF.data;
var aE=y(aF,P,X);
if(!aE){D();
X.responseBody="";
X.messages=[]
}};
p.onerror=function(aE){clearTimeout(P.id);
if(P.heartbeatTimer){clearTimeout(P.heartbeatTimer)
}if(X.closedByClientTimeout){return
}aj(aC);
an();
if(ax){a.util.log(P.logLevel,["SSE closed normally"])
}else{if(!aC){R("SSE failed. Downgrading to fallback transport and resending")
}else{if(P.reconnect&&(X.transport==="sse")){if(l++<P.maxReconnectOnClose){O("re-connecting",P.transport,P);
if(P.reconnectInterval>0){P.reconnectId=setTimeout(function(){J(true)
},P.reconnectInterval)
}else{J(true)
}X.responseBody="";
X.messages=[]
}else{a.util.log(P.logLevel,["SSE reconnect maximum try reached "+l]);
ag(0,"maxReconnectOnClose reached")
}}}}}
}function ak(aC){X.transport="websocket";
var aB=k(P.url);
if(P.logLevel==="debug"){a.util.debug("Invoking executeWebSocket");
a.util.debug("Using URL: "+aB)
}if(aC&&!P.reconnect){if(aa!=null){an()
}return
}aa=j(aB);
if(P.webSocketBinaryType!=null){aa.binaryType=P.webSocketBinaryType
}if(P.connectTimeout>0){P.id=setTimeout(function(){if(!aC){var aF={code:1002,reason:"",wasClean:false};
aa.onclose(aF);
try{an()
}catch(aG){}return
}},P.connectTimeout)
}aa.onopen=function(aG){z(P);
if(P.logLevel==="debug"){a.util.debug("Websocket successfully opened")
}var aF=aC;
if(aa!=null){aa.canSendMessage=true
}if(!P.enableProtocol){aC=true;
if(aF){O("re-opening","websocket",P)
}else{O("opening","websocket",P)
}}if(aa!=null){if(P.method==="POST"){X.state="messageReceived";
aa.send(P.data)
}}};
aa.onmessage=function(aH){z(P);
if(P.enableProtocol){aC=true
}X.state="messageReceived";
X.status=200;
aH=aH.data;
var aF=typeof(aH)==="string";
if(aF){var aG=y(aH,P,X);
if(!aG){D();
X.responseBody="";
X.messages=[]
}}else{aH=v(P,aH);
if(aH===""){return
}X.responseBody=aH;
D();
X.responseBody=null
}};
aa.onerror=function(aF){clearTimeout(P.id);
if(P.heartbeatTimer){clearTimeout(P.heartbeatTimer)
}};
aa.onclose=function(aF){clearTimeout(P.id);
if(X.state==="closed"){return
}var aG=aF.reason;
if(aG===""){switch(aF.code){case 1000:aG="Normal closure; the connection successfully completed whatever purpose for which it was created.";
break;
case 1001:aG="The endpoint is going away, either because of a server failure or because the browser is navigating away from the page that opened the connection.";
break;
case 1002:aG="The endpoint is terminating the connection due to a protocol error.";
break;
case 1003:aG="The connection is being terminated because the endpoint received data of a type it cannot accept (for example, a text-only endpoint received binary data).";
break;
case 1004:aG="The endpoint is terminating the connection because a data frame was received that is too large.";
break;
case 1005:aG="Unknown: no status code was provided even though one was expected.";
break;
case 1006:aG="Connection was closed abnormally (that is, with no close frame being sent).";
break
}}if(P.logLevel==="warn"){a.util.warn("Websocket closed, reason: "+aG);
a.util.warn("Websocket closed, wasClean: "+aF.wasClean)
}if(X.closedByClientTimeout){return
}aj(aC);
X.state="closed";
if(ax){a.util.log(P.logLevel,["Websocket closed normally"])
}else{if(!aC){R("Websocket failed. Downgrading to Comet and resending")
}else{if(P.reconnect&&X.transport==="websocket"&&aF.code!==1001){an();
if(l++<P.maxReconnectOnClose){O("re-connecting",P.transport,P);
if(P.reconnectInterval>0){P.reconnectId=setTimeout(function(){X.responseBody="";
X.messages=[];
ak(true)
},P.reconnectInterval)
}else{X.responseBody="";
X.messages=[];
ak(true)
}}else{a.util.log(P.logLevel,["Websocket reconnect maximum try reached "+P.requestCount]);
if(P.logLevel==="warn"){a.util.warn("Websocket error, reason: "+aF.reason)
}ag(0,"maxReconnectOnClose reached")
}}}}};
var aD=navigator.userAgent.toLowerCase();
var aE=aD.indexOf("android")>-1;
if(aE&&aa.url===undefined){aa.onclose({reason:"Android 4.1 does not support websockets.",wasClean:false})
}}function v(aC,aJ){var aI=aJ;
if(aC.transport==="polling"){return aI
}if(a.util.trim(aJ).length!==0&&aC.enableProtocol&&aC.firstMessage){var aH=aC.trackMessageLength?1:0;
var aD=aJ.split(aC.messageDelimiter);
if(aD.length<=aH+1){return aI
}aC.firstMessage=false;
aC.uuid=a.util.trim(aD[aH]);
if(aD.length<=aH+2){a.util.log("error",["Protocol data not sent by the server. If you enable protocol on client side, be sure to install JavascriptProtocol interceptor on server side.Also note that atmosphere-runtime 2.2+ should be used."])
}var aB=parseInt(a.util.trim(aD[aH+1]),10);
var aG=aD[aH+2];
if(!isNaN(aB)&&aB>0){var aE=function(){am(aG);
aC.heartbeatTimer=setTimeout(aE,aB)
};
aC.heartbeatTimer=setTimeout(aE,aB)
}if(aC.transport!=="long-polling"){ap(aC)
}e=aC.uuid;
aI="";
aH=aC.trackMessageLength?4:3;
if(aD.length>aH+1){for(var aF=aH;
aF<aD.length;
aF++){aI+=aD[aF];
if(aF+1!==aD.length){aI+=aC.messageDelimiter
}}}if(aC.ackInterval!==0){setTimeout(function(){am("...ACK...")
},aC.ackInterval)
}}else{if(aC.enableProtocol&&aC.firstMessage&&a.util.browser.msie&&+a.util.browser.version.split(".")[0]<10){a.util.log(P.logLevel,["Receiving unexpected data from IE"])
}else{ap(aC)
}}return aI
}function z(aB){clearTimeout(aB.id);
if(aB.timeout>0&&aB.transport!=="polling"){aB.id=setTimeout(function(){s(aB);
E();
an()
},aB.timeout)
}}function s(aB){X.closedByClientTimeout=true;
X.state="closedByClient";
X.responseBody="";
X.status=408;
X.messages=[];
D()
}function ag(aB,aC){an();
clearTimeout(P.id);
X.state="error";
X.reasonPhrase=aC;
X.responseBody="";
X.status=aB;
X.messages=[];
D()
}function y(aF,aE,aB){aF=v(aE,aF);
if(aF.length===0){return true
}aB.responseBody=aF;
if(aE.trackMessageLength){aF=aB.partialMessage+aF;
var aD=[];
var aC=aF.indexOf(aE.messageDelimiter);
while(aC!==-1){var aH=aF.substring(0,aC);
var aG=+aH;
if(isNaN(aG)){throw new Error('message length "'+aH+'" is not a number')
}aC+=aE.messageDelimiter.length;
if(aC+aG>aF.length){aC=-1
}else{aD.push(aF.substring(aC,aC+aG));
aF=aF.substring(aC+aG,aF.length);
aC=aF.indexOf(aE.messageDelimiter)
}}aB.partialMessage=aF;
if(aD.length!==0){aB.responseBody=aD.join(aE.messageDelimiter);
aB.messages=aD;
return false
}else{aB.responseBody="";
aB.messages=[];
return true
}}else{aB.responseBody=aF
}return false
}function R(aB){a.util.log(P.logLevel,[aB]);
if(typeof(P.onTransportFailure)!=="undefined"){P.onTransportFailure(aB,P)
}else{if(typeof(a.util.onTransportFailure)!=="undefined"){a.util.onTransportFailure(aB,P)
}}P.transport=P.fallbackTransport;
var aC=P.connectTimeout===-1?0:P.connectTimeout;
if(P.reconnect&&P.transport!=="none"||P.transport==null){P.method=P.fallbackMethod;
X.transport=P.fallbackTransport;
P.fallbackTransport="none";
if(aC>0){P.reconnectId=setTimeout(function(){u()
},aC)
}else{u()
}}else{ag(500,"Unable to reconnect with fallback transport")
}}function Y(aD,aB){var aC=P;
if((aD!=null)&&(typeof(aD)!=="undefined")){aC=aD
}if(aB==null){aB=aC.url
}if(!aC.attachHeadersAsQueryString){return aB
}if(aB.indexOf("X-Atmosphere-Framework")!==-1){return aB
}aB+=(aB.indexOf("?")!==-1)?"&":"?";
aB+="X-Atmosphere-tracking-id="+aC.uuid;
aB+="&X-Atmosphere-Framework="+c;
aB+="&X-Atmosphere-Transport="+aC.transport;
if(aC.trackMessageLength){aB+="&X-Atmosphere-TrackMessageSize=true"
}if(aC.heartbeat!==null&&aC.heartbeat.server!==null){aB+="&X-Heartbeat-Server="+aC.heartbeat.server
}if(aC.contentType!==""){aB+="&Content-Type="+(aC.transport==="websocket"?aC.contentType:encodeURIComponent(aC.contentType))
}if(aC.enableProtocol){aB+="&X-atmo-protocol=true"
}a.util.each(aC.headers,function(aE,aG){var aF=a.util.isFunction(aG)?aG.call(this,aC,aD,X):aG;
if(aF!=null){aB+="&"+encodeURIComponent(aE)+"="+encodeURIComponent(aF)
}});
return aB
}function ap(aB){if(!aB.isOpen){aB.isOpen=true;
O("opening",aB.transport,aB)
}else{if(aB.isReopen){aB.isReopen=false;
O("re-opening",aB.transport,aB)
}}}function t(aE){var aC=P;
if((aE!=null)||(typeof(aE)!=="undefined")){aC=aE
}aC.lastIndex=0;
aC.readyState=0;
if((aC.transport==="jsonp")||((aC.enableXDR)&&(a.util.checkCORSSupport()))){A(aC);
return
}if(a.util.browser.msie&&+a.util.browser.version.split(".")[0]<10){if((aC.transport==="streaming")){if(aC.enableXDR&&window.XDomainRequest){Q(aC)
}else{aw(aC)
}return
}if((aC.enableXDR)&&(window.XDomainRequest)){Q(aC);
return
}}var aF=function(){aC.lastIndex=0;
if(aC.reconnect&&l++<aC.maxReconnectOnClose){O("re-connecting",aE.transport,aE);
S(aD,aC,aE.reconnectInterval)
}else{ag(0,"maxReconnectOnClose reached")
}};
var aB=function(){X.errorHandled=true;
an();
aF()
};
if(aC.force||(aC.reconnect&&(aC.maxRequest===-1||aC.requestCount++<aC.maxRequest))){aC.force=false;
var aD=a.util.xhr();
aD.hasData=false;
h(aD,aC,true);
if(aC.suspend){w=aD
}if(aC.transport!=="polling"){X.transport=aC.transport;
aD.onabort=function(){aj(true)
};
aD.onerror=function(){X.error=true;
X.ffTryingReconnect=true;
try{X.status=XMLHttpRequest.status
}catch(aH){X.status=500
}if(!X.status){X.status=500
}if(!X.errorHandled){an();
aF()
}}
}aD.onreadystatechange=function(){if(ax){return
}X.error=null;
var aI=false;
var aO=false;
if(aC.transport==="streaming"&&aC.readyState>2&&aD.readyState===4){an();
aF();
return
}aC.readyState=aD.readyState;
if(aC.transport==="streaming"&&aD.readyState>=3){aO=true
}else{if(aC.transport==="long-polling"&&aD.readyState===4){aO=true
}}z(P);
if(aC.transport!=="polling"){var aH=200;
if(aD.readyState===4){aH=aD.status>1000?0:aD.status
}if(aH>=300||aH===0){aB();
return
}if((!aC.enableProtocol||!aE.firstMessage)&&aD.readyState===2){if(a.util.browser.mozilla&&X.ffTryingReconnect){X.ffTryingReconnect=false;
setTimeout(function(){if(!X.ffTryingReconnect){ap(aC)
}},500)
}else{ap(aC)
}}}else{if(aD.readyState===4){aO=true
}}if(aO){var aL=aD.responseText;
X.errorHandled=false;
if(a.util.trim(aL).length===0&&aC.transport==="long-polling"){if(!aD.hasData){aB()
}else{aD.hasData=false
}return
}aD.hasData=true;
ah(aD,P);
if(aC.transport==="streaming"){if(!a.util.browser.opera){var aK=aL.substring(aC.lastIndex,aL.length);
aI=y(aK,aC,X);
aC.lastIndex=aL.length;
if(aI){return
}}else{a.util.iterate(function(){if(X.status!==500&&aD.responseText.length>aC.lastIndex){try{X.status=aD.status;
X.headers=a.util.parseHeaders(aD.getAllResponseHeaders());
ah(aD,P)
}catch(aQ){X.status=404
}z(P);
X.state="messageReceived";
var aP=aD.responseText.substring(aC.lastIndex);
aC.lastIndex=aD.responseText.length;
aI=y(aP,aC,X);
if(!aI){D()
}if(M(aD,aC)){o(aD,aC);
return
}}else{if(X.status>400){aC.lastIndex=aD.responseText.length;
return false
}}},0)
}}else{aI=y(aL,aC,X)
}var aN=M(aD,aC);
try{X.status=aD.status;
X.headers=a.util.parseHeaders(aD.getAllResponseHeaders());
ah(aD,aC)
}catch(aM){X.status=404
}if(aC.suspend){X.state=X.status===0?"closed":"messageReceived"
}else{X.state="messagePublished"
}var aJ=!aN&&aE.transport!=="streaming"&&aE.transport!=="polling";
if(aJ&&!aC.executeCallbackBeforeReconnect){S(aD,aC,aC.pollingInterval)
}if(X.responseBody.length!==0&&!aI){D()
}if(aJ&&aC.executeCallbackBeforeReconnect){S(aD,aC,aC.pollingInterval)
}if(aN){o(aD,aC)
}}};
try{aD.send(aC.data);
al=true
}catch(aG){a.util.log(aC.logLevel,["Unable to connect to "+aC.url]);
ag(0,aG)
}}else{if(aC.logLevel==="debug"){a.util.log(aC.logLevel,["Max re-connection reached."])
}ag(0,"maxRequest reached")
}}function o(aC,aB){ao();
ax=false;
S(aC,aB,500)
}function h(aD,aE,aC){var aB=aE.url;
if(aE.dispatchUrl!=null&&aE.method==="POST"){aB+=aE.dispatchUrl
}aB=Y(aE,aB);
aB=a.util.prepareURL(aB);
if(aC){aD.open(aE.method,aB,aE.async);
if(aE.connectTimeout>0){aE.id=setTimeout(function(){if(aE.requestCount===0){an();
G("Connect timeout","closed",200,aE.transport)
}},aE.connectTimeout)
}}if(P.withCredentials&&P.transport!=="websocket"){if("withCredentials" in aD){aD.withCredentials=true
}}if(!P.dropHeaders){aD.setRequestHeader("X-Atmosphere-Framework",a.util.version);
aD.setRequestHeader("X-Atmosphere-Transport",aE.transport);
if(aD.heartbeat!==null&&aD.heartbeat.server!==null){aD.setRequestHeader("X-Heartbeat-Server",aD.heartbeat.server)
}if(aE.trackMessageLength){aD.setRequestHeader("X-Atmosphere-TrackMessageSize","true")
}aD.setRequestHeader("X-Atmosphere-tracking-id",aE.uuid);
a.util.each(aE.headers,function(aF,aH){var aG=a.util.isFunction(aH)?aH.call(this,aD,aE,aC,X):aH;
if(aG!=null){aD.setRequestHeader(aF,aG)
}})
}if(aE.contentType!==""){aD.setRequestHeader("Content-Type",aE.contentType)
}}function S(aC,aD,aE){if(aD.reconnect||(aD.suspend&&al)){var aB=0;
if(aC&&aC.readyState>1){aB=aC.status>1000?0:aC.status
}X.status=aB===0?204:aB;
X.reason=aB===0?"Server resumed the connection or down.":"OK";
clearTimeout(aD.id);
if(aD.reconnectId){clearTimeout(aD.reconnectId);
delete aD.reconnectId
}if(aE>0){P.reconnectId=setTimeout(function(){t(aD)
},aE)
}else{t(aD)
}}}function af(aB){aB.state="re-connecting";
ac(aB)
}function Q(aB){if(aB.transport!=="polling"){F=W(aB);
F.open()
}else{W(aB).open()
}}function W(aD){var aC=P;
if((aD!=null)&&(typeof(aD)!=="undefined")){aC=aD
}var aI=aC.transport;
var aH=0;
var aB=new window.XDomainRequest();
var aF=function(){if(aC.transport==="long-polling"&&(aC.reconnect&&(aC.maxRequest===-1||aC.requestCount++<aC.maxRequest))){aB.status=200;
Q(aC)
}};
var aG=aC.rewriteURL||function(aK){var aJ=/(?:^|;\s*)(JSESSIONID|PHPSESSID)=([^;]*)/.exec(document.cookie);
switch(aJ&&aJ[1]){case"JSESSIONID":return aK.replace(/;jsessionid=[^\?]*|(\?)|$/,";jsessionid="+aJ[2]+"$1");
case"PHPSESSID":return aK.replace(/\?PHPSESSID=[^&]*&?|\?|$/,"?PHPSESSID="+aJ[2]+"&").replace(/&$/,"")
}return aK
};
aB.onprogress=function(){aE(aB)
};
aB.onerror=function(){if(aC.transport!=="polling"){an();
if(l++<aC.maxReconnectOnClose){if(aC.reconnectInterval>0){aC.reconnectId=setTimeout(function(){O("re-connecting",aD.transport,aD);
Q(aC)
},aC.reconnectInterval)
}else{O("re-connecting",aD.transport,aD);
Q(aC)
}}else{ag(0,"maxReconnectOnClose reached")
}}};
aB.onload=function(){};
var aE=function(aJ){clearTimeout(aC.id);
var aL=aJ.responseText;
aL=aL.substring(aH);
aH+=aL.length;
if(aI!=="polling"){z(aC);
var aK=y(aL,aC,X);
if(aI==="long-polling"&&a.util.trim(aL).length===0){return
}if(aC.executeCallbackBeforeReconnect){aF()
}if(!aK){G(X.responseBody,"messageReceived",200,aI)
}if(!aC.executeCallbackBeforeReconnect){aF()
}}};
return{open:function(){var aJ=aC.url;
if(aC.dispatchUrl!=null){aJ+=aC.dispatchUrl
}aJ=Y(aC,aJ);
aB.open(aC.method,aG(aJ));
if(aC.method==="GET"){aB.send()
}else{aB.send(aC.data)
}if(aC.connectTimeout>0){aC.id=setTimeout(function(){if(aC.requestCount===0){an();
G("Connect timeout","closed",200,aC.transport)
}},aC.connectTimeout)
}},close:function(){aB.abort()
}}
}function aw(aB){F=x(aB);
F.open()
}function x(aE){var aD=P;
if((aE!=null)&&(typeof(aE)!=="undefined")){aD=aE
}var aC;
var aF=new window.ActiveXObject("htmlfile");
aF.open();
aF.close();
var aB=aD.url;
if(aD.dispatchUrl!=null){aB+=aD.dispatchUrl
}if(aD.transport!=="polling"){X.transport=aD.transport
}return{open:function(){var aG=aF.createElement("iframe");
aB=Y(aD);
if(aD.data!==""){aB+="&X-Atmosphere-Post-Body="+encodeURIComponent(aD.data)
}aB=a.util.prepareURL(aB);
aG.src=aB;
aF.body.appendChild(aG);
var aH=aG.contentDocument||aG.contentWindow.document;
aC=a.util.iterate(function(){try{if(!aH.firstChild){return
}var aK=aH.body?aH.body.lastChild:aH;
var aM=function(){var aO=aK.cloneNode(true);
aO.appendChild(aH.createTextNode("."));
var aN=aO.innerText;
aN=aN.substring(0,aN.length-1);
return aN
};
if(!aH.body||!aH.body.firstChild||aH.body.firstChild.nodeName.toLowerCase()!=="pre"){var aJ=aH.head||aH.getElementsByTagName("head")[0]||aH.documentElement||aH;
var aI=aH.createElement("script");
aI.text="document.write('<plaintext>')";
aJ.insertBefore(aI,aJ.firstChild);
aJ.removeChild(aI);
aK=aH.body.lastChild
}if(aD.closed){aD.isReopen=true
}aC=a.util.iterate(function(){var aO=aM();
if(aO.length>aD.lastIndex){z(P);
X.status=200;
X.error=null;
aK.innerText="";
var aN=y(aO,aD,X);
if(aN){return""
}G(X.responseBody,"messageReceived",200,aD.transport)
}aD.lastIndex=0;
if(aH.readyState==="complete"){aj(true);
O("re-connecting",aD.transport,aD);
if(aD.reconnectInterval>0){aD.reconnectId=setTimeout(function(){aw(aD)
},aD.reconnectInterval)
}else{aw(aD)
}return false
}},null);
return false
}catch(aL){X.error=true;
O("re-connecting",aD.transport,aD);
if(l++<aD.maxReconnectOnClose){if(aD.reconnectInterval>0){aD.reconnectId=setTimeout(function(){aw(aD)
},aD.reconnectInterval)
}else{aw(aD)
}}else{ag(0,"maxReconnectOnClose reached")
}aF.execCommand("Stop");
aF.close();
return false
}})
},close:function(){if(aC){aC()
}aF.execCommand("Stop");
aj(true)
}}
}function am(aB){if(r!=null){m(aB)
}else{if(w!=null||p!=null){i(aB)
}else{if(F!=null){Z(aB)
}else{if(H!=null){V(aB)
}else{if(aa!=null){I(aB)
}else{ag(0,"No suspended connection available");
a.util.error("No suspended connection available. Make sure atmosphere.subscribe has been called and request.onOpen invoked before invoking this method")
}}}}}}function n(aC,aB){if(!aB){aB=aq(aC)
}aB.transport="polling";
aB.method="GET";
aB.async=false;
aB.withCredentials=false;
aB.reconnect=false;
aB.force=true;
aB.suspend=false;
aB.timeout=1000;
t(aB)
}function m(aB){r.send(aB)
}function C(aC){if(aC.length===0){return
}try{if(r){r.localSend(aC)
}else{if(ar){ar.signal("localMessage",a.util.stringifyJSON({id:K,event:aC}))
}}}catch(aB){a.util.error(aB)
}}function i(aC){var aB=aq(aC);
t(aB)
}function Z(aC){if(P.enableXDR&&a.util.checkCORSSupport()){var aB=aq(aC);
aB.reconnect=false;
A(aB)
}else{i(aC)
}}function V(aB){i(aB)
}function U(aB){var aC=aB;
if(typeof(aC)==="object"){aC=aB.data
}return aC
}function aq(aC){var aD=U(aC);
var aB={connected:false,timeout:60000,method:"POST",url:P.url,contentType:P.contentType,headers:P.headers,reconnect:true,callback:null,data:aD,suspend:false,maxRequest:-1,logLevel:"info",requestCount:0,withCredentials:P.withCredentials,async:P.async,transport:"polling",isOpen:true,attachHeadersAsQueryString:true,enableXDR:P.enableXDR,uuid:P.uuid,dispatchUrl:P.dispatchUrl,enableProtocol:false,messageDelimiter:"|",trackMessageLength:P.trackMessageLength,maxReconnectOnClose:P.maxReconnectOnClose,heartbeatTimer:P.heartbeatTimer,heartbeat:P.heartbeat};
if(typeof(aC)==="object"){aB=a.util.extend(aB,aC)
}return aB
}function I(aB){var aE=a.util.isBinary(aB)?aB:U(aB);
var aC;
try{if(P.dispatchUrl!=null){aC=P.webSocketPathDelimiter+P.dispatchUrl+P.webSocketPathDelimiter+aE
}else{aC=aE
}if(!aa.canSendMessage){a.util.error("WebSocket not connected.");
return
}aa.send(aC)
}catch(aD){aa.onclose=function(aF){};
an();
R("Websocket failed. Downgrading to Comet and resending "+aB);
i(aB)
}}function ad(aC){var aB=a.util.parseJSON(aC);
if(aB.id!==K){if(typeof(P.onLocalMessage)!=="undefined"){P.onLocalMessage(aB.event)
}else{if(typeof(a.util.onLocalMessage)!=="undefined"){a.util.onLocalMessage(aB.event)
}}}}function G(aE,aB,aC,aD){X.responseBody=aE;
X.transport=aD;
X.status=aC;
X.state=aB;
D()
}function ah(aB,aD){if(!aD.readResponsesHeaders){if(!aD.enableProtocol){aD.uuid=K
}}else{try{var aC=aB.getResponseHeader("X-Atmosphere-tracking-id");
if(aC&&aC!=null){aD.uuid=aC.split(" ").pop()
}}catch(aE){}}}function ac(aB){av(aB,P);
av(aB,a.util)
}function av(aC,aD){switch(aC.state){case"messageReceived":l=0;
if(typeof(aD.onMessage)!=="undefined"){aD.onMessage(aC)
}if(typeof(aD.onmessage)!=="undefined"){aD.onmessage(aC)
}break;
case"error":if(typeof(aD.onError)!=="undefined"){aD.onError(aC)
}if(typeof(aD.onerror)!=="undefined"){aD.onerror(aC)
}break;
case"opening":delete P.closed;
if(typeof(aD.onOpen)!=="undefined"){aD.onOpen(aC)
}if(typeof(aD.onopen)!=="undefined"){aD.onopen(aC)
}break;
case"messagePublished":if(typeof(aD.onMessagePublished)!=="undefined"){aD.onMessagePublished(aC)
}break;
case"re-connecting":if(typeof(aD.onReconnect)!=="undefined"){aD.onReconnect(P,aC)
}break;
case"closedByClient":if(typeof(aD.onClientTimeout)!=="undefined"){aD.onClientTimeout(P)
}break;
case"re-opening":delete P.closed;
if(typeof(aD.onReopen)!=="undefined"){aD.onReopen(P,aC)
}break;
case"fail-to-reconnect":if(typeof(aD.onFailureToReconnect)!=="undefined"){aD.onFailureToReconnect(P,aC)
}break;
case"unsubscribe":case"closed":var aB=typeof(P.closed)!=="undefined"?P.closed:false;
if(!aB){if(typeof(aD.onClose)!=="undefined"){aD.onClose(aC)
}if(typeof(aD.onclose)!=="undefined"){aD.onclose(aC)
}}P.closed=true;
break
}}function aj(aB){if(X.state!=="closed"){X.state="closed";
X.responseBody="";
X.messages=[];
X.status=!aB?501:200;
D()
}}function D(){var aD=function(aG,aH){aH(X)
};
if(r==null&&ab!=null){ab(X.responseBody)
}P.reconnect=P.mrequest;
var aB=typeof(X.responseBody)==="string";
var aE=(aB&&P.trackMessageLength)?(X.messages.length>0?X.messages:[""]):new Array(X.responseBody);
for(var aC=0;
aC<aE.length;
aC++){if(aE.length>1&&aE[aC].length===0){continue
}X.responseBody=(aB)?a.util.trim(aE[aC]):aE[aC];
if(r==null&&ab!=null){ab(X.responseBody)
}if(X.responseBody.length===0&&X.state==="messageReceived"){continue
}ac(X);
if(f.length>0){if(P.logLevel==="debug"){a.util.debug("Invoking "+f.length+" global callbacks: "+X.state)
}try{a.util.each(f,aD)
}catch(aF){a.util.log(P.logLevel,["Callback exception"+aF])
}}if(typeof(P.callback)==="function"){if(P.logLevel==="debug"){a.util.debug("Invoking request callbacks")
}try{P.callback(X)
}catch(aF){a.util.log(P.logLevel,["Callback exception"+aF])
}}}}this.subscribe=function(aB){az(aB);
u()
};
this.execute=function(){u()
};
this.close=function(){ao()
};
this.disconnect=function(){E()
};
this.getUrl=function(){return P.url
};
this.push=function(aD,aC){if(aC!=null){var aB=P.dispatchUrl;
P.dispatchUrl=aC;
am(aD);
P.dispatchUrl=aB
}else{am(aD)
}};
this.getUUID=function(){return P.uuid
};
this.pushLocal=function(aB){C(aB)
};
this.enableProtocol=function(aB){return P.enableProtocol
};
this.request=P;
this.response=X
}};
a.subscribe=function(h,k,j){if(typeof(k)==="function"){a.addCallback(k)
}if(typeof(h)!=="string"){j=h
}else{j.url=h
}e=((typeof(j)!=="undefined")&&typeof(j.uuid)!=="undefined")?j.uuid:0;
var i=new a.AtmosphereRequest(j);
i.execute();
g[g.length]=i;
return i
};
a.unsubscribe=function(){if(g.length>0){var h=[].concat(g);
for(var k=0;
k<h.length;
k++){var j=h[k];
j.close();
clearTimeout(j.response.request.id);
if(j.heartbeatTimer){clearTimeout(j.heartbeatTimer)
}}}g=[];
f=[]
};
a.unsubscribeUrl=function(j){var h=-1;
if(g.length>0){for(var l=0;
l<g.length;
l++){var k=g[l];
if(k.getUrl()===j){k.close();
clearTimeout(k.response.request.id);
if(k.heartbeatTimer){clearTimeout(k.heartbeatTimer)
}h=l;
break
}}}if(h>=0){g.splice(h,1)
}};
a.addCallback=function(h){if(a.util.inArray(h,f)===-1){f.push(h)
}};
a.removeCallback=function(i){var h=a.util.inArray(i,f);
if(h!==-1){f.splice(h,1)
}};
a.util={browser:{},parseHeaders:function(i){var h,k=/^(.*?):[ \t]*([^\r\n]*)\r?$/mg,j={};
while(h=k.exec(i)){j[h[1]]=h[2]
}return j
},now:function(){return new Date().getTime()
},isArray:function(h){return Object.prototype.toString.call(h)==="[object Array]"
},inArray:function(k,l){if(!Array.prototype.indexOf){var h=l.length;
for(var j=0;
j<h;
++j){if(l[j]===k){return j
}}return -1
}return l.indexOf(k)
},isBinary:function(h){return/^\[object\s(?:Blob|ArrayBuffer|.+Array)\]$/.test(Object.prototype.toString.call(h))
},isFunction:function(h){return Object.prototype.toString.call(h)==="[object Function]"
},getAbsoluteURL:function(h){var i=document.createElement("div");
i.innerHTML='<a href="'+h+'"/>';
return encodeURI(decodeURI(i.firstChild.href))
},prepareURL:function(i){var j=a.util.now();
var h=i.replace(/([?&])_=[^&]*/,"$1_="+j);
return h+(h===i?(/\?/.test(i)?"&":"?")+"_="+j:"")
},trim:function(h){if(!String.prototype.trim){return h.toString().replace(/(?:(?:^|\n)\s+|\s+(?:$|\n))/g,"").replace(/\s+/g," ")
}else{return h.toString().trim()
}},param:function(l){var j,h=[];
function k(m,n){n=a.util.isFunction(n)?n():(n==null?"":n);
h.push(encodeURIComponent(m)+"="+encodeURIComponent(n))
}function i(n,o){var m;
if(a.util.isArray(o)){a.util.each(o,function(q,p){if(/\[\]$/.test(n)){k(n,p)
}else{i(n+"["+(typeof p==="object"?q:"")+"]",p)
}})
}else{if(Object.prototype.toString.call(o)==="[object Object]"){for(m in o){i(n+"["+m+"]",o[m])
}}else{k(n,o)
}}}for(j in l){i(j,l[j])
}return h.join("&").replace(/%20/g,"+")
},storage:function(){try{return !!(window.localStorage&&window.StorageEvent)
}catch(h){return false
}},iterate:function(j,i){var k;
i=i||0;
(function h(){k=setTimeout(function(){if(j()===false){return
}h()
},i)
})();
return function(){clearTimeout(k)
}
},each:function(n,o,j){if(!n){return
}var m,k=0,l=n.length,h=a.util.isArray(n);
if(j){if(h){for(;
k<l;
k++){m=o.apply(n[k],j);
if(m===false){break
}}}else{for(k in n){m=o.apply(n[k],j);
if(m===false){break
}}}}else{if(h){for(;
k<l;
k++){m=o.call(n[k],k,n[k]);
if(m===false){break
}}}else{for(k in n){m=o.call(n[k],k,n[k]);
if(m===false){break
}}}}return n
},extend:function(l){var k,j,h;
for(k=1;
k<arguments.length;
k++){if((j=arguments[k])!=null){for(h in j){l[h]=j[h]
}}}return l
},on:function(j,i,h){if(j.addEventListener){j.addEventListener(i,h,false)
}else{if(j.attachEvent){j.attachEvent("on"+i,h)
}}},off:function(j,i,h){if(j.removeEventListener){j.removeEventListener(i,h,false)
}else{if(j.detachEvent){j.detachEvent("on"+i,h)
}}},log:function(j,i){if(window.console){var h=window.console[j];
if(typeof h==="function"){h.apply(window.console,i)
}}},warn:function(){a.util.log("warn",arguments)
},info:function(){a.util.log("info",arguments)
},debug:function(){a.util.log("debug",arguments)
},error:function(){a.util.log("error",arguments)
},xhr:function(){try{return new window.XMLHttpRequest()
}catch(i){try{return new window.ActiveXObject("Microsoft.XMLHTTP")
}catch(h){}}},parseJSON:function(h){return !h?null:window.JSON&&window.JSON.parse?window.JSON.parse(h):new Function("return "+h)()
},stringifyJSON:function(j){var m=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,k={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"};
function h(n){return'"'+n.replace(m,function(o){var p=k[o];
return typeof p==="string"?p:"\\u"+("0000"+o.charCodeAt(0).toString(16)).slice(-4)
})+'"'
}function i(o){return o<10?"0"+o:o
}return window.JSON&&window.JSON.stringify?window.JSON.stringify(j):(function l(s,r){var q,p,n,o,u=r[s],t=typeof u;
if(u&&typeof u==="object"&&typeof u.toJSON==="function"){u=u.toJSON(s);
t=typeof u
}switch(t){case"string":return h(u);
case"number":return isFinite(u)?String(u):"null";
case"boolean":return String(u);
case"object":if(!u){return"null"
}switch(Object.prototype.toString.call(u)){case"[object Date]":return isFinite(u.valueOf())?'"'+u.getUTCFullYear()+"-"+i(u.getUTCMonth()+1)+"-"+i(u.getUTCDate())+"T"+i(u.getUTCHours())+":"+i(u.getUTCMinutes())+":"+i(u.getUTCSeconds())+'Z"':"null";
case"[object Array]":n=u.length;
o=[];
for(q=0;
q<n;
q++){o.push(l(q,u)||"null")
}return"["+o.join(",")+"]";
default:o=[];
for(q in u){if(b.call(u,q)){p=l(q,u);
if(p){o.push(h(q)+":"+p)
}}}return"{"+o.join(",")+"}"
}}})("",{"":j})
},checkCORSSupport:function(){if(a.util.browser.msie&&!window.XDomainRequest&&+a.util.browser.version.split(".")[0]<11){return true
}else{if(a.util.browser.opera&&+a.util.browser.version.split(".")<12){return true
}else{if(a.util.trim(navigator.userAgent).slice(0,16)==="KreaTVWebKit/531"){return true
}else{if(a.util.trim(navigator.userAgent).slice(-7).toLowerCase()==="kreatel"){return true
}}}}var h=navigator.userAgent.toLowerCase();
var i=h.indexOf("android")>-1;
if(i){return true
}return false
}};
d=a.util.now();
(function(){var i=navigator.userAgent.toLowerCase(),h=/(chrome)[ \/]([\w.]+)/.exec(i)||/(webkit)[ \/]([\w.]+)/.exec(i)||/(opera)(?:.*version|)[ \/]([\w.]+)/.exec(i)||/(msie) ([\w.]+)/.exec(i)||/(trident)(?:.*? rv:([\w.]+)|)/.exec(i)||i.indexOf("compatible")<0&&/(mozilla)(?:.*? rv:([\w.]+)|)/.exec(i)||[];
a.util.browser[h[1]||""]=true;
a.util.browser.version=h[2]||"0";
if(a.util.browser.trident){a.util.browser.msie=true
}if(a.util.browser.msie||(a.util.browser.mozilla&&+a.util.browser.version.split(".")[0]===1)){a.util.storage=false
}})();
a.util.on(window,"unload",function(h){a.unsubscribe()
});
a.util.on(window,"keypress",function(h){if(h.charCode===27||h.keyCode===27){if(h.preventDefault){h.preventDefault()
}}});
a.util.on(window,"offline",function(){a.unsubscribe()
});
return a
}));
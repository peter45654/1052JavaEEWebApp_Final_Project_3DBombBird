
//$( "#p1" ).click(alertTest);
//$( "#p2" ).click(alertTest);

$( "#p1" ).click(connect1);
$( "#p2" ).click(connect2);
//$( "#p1" ).css( "border", "9px solid red" );
function serverLog(text){
	//alert(text);
	$("#viewBoard").append('<p>'+text+'</p>');
}
function alertTest(text){
	serverLog(text);
	alert(text);
	}	
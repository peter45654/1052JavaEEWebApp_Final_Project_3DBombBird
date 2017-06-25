<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% String input = (String) request.getAttribute("param1"); %>
<script>var input =<%= input%></script>
<html>
<head>
<title>Net Connection Test</title>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<script src="NetConnection/Connection.js" ></script>
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
</head>
<body>
<button id="p1">player1</button>
<button id="p2">player2</button>
<p>以下server 回傳訊息</p>
<div id="viewBoard">
</div>
</body>
<script type="text/javascript" src="NetConnection/TestControl.js" ></script>
</html>
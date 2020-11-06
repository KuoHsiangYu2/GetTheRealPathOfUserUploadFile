<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index</title>
</head>
<body>
    <h1>index</h1>
    <form action="${pageContext.request.contextPath}/GetFileRealPath" method="post" enctype="multipart/form-data">
        <input id="inputFile" name="inputFile" type="file" required="required" />
        <br />
        <input type="submit" value="上傳檔案" />
    </form>
    <script type="text/javascript">
        'use strict';
        if ('${filename}' !== '') {
        	var filename2 = decodeURIComponent('${filename}');
        	console.log(filename2);
            window.alert('檔案路徑為\n[' + filename2 + ']');
        }
    </script>
    <%
        HttpSession httpSession = request.getSession();
        httpSession.removeAttribute("filename");
    %>
</body>
</html>
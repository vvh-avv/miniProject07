<%@ page contentType="text/html; charset=euc-kr"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>Model2 MVC Shop</title>

<link href="/css/left.css" rel="stylesheet" type="text/css">

</head>

<body topmargin="0" leftmargin="0">

	<table width="100%" height="50" border="0" cellpadding="0"
		cellspacing="0">
		<tr>
			<td height="10"></td>
			<td height="10">&nbsp;</td>
		</tr>
		<tr>
			<td width="800" height="30"><h2>
					<a href="/index.jsp" target="_parent">Model2 MVC Shop</a>
				</h2></td>
		</tr>
		<tr>
			<td height="20" align="right" background="/images/img_bg.gif">
				<table width="300" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="215">
							<!-- 로그인을 했다면 --> <c:if test="${!empty user}">
								<a href="/user/getUser?userId=${user.userId}" target="rightFrame">${user.userId}</a> 님 안녕하세요!
	          	</c:if>

						</td>
						<td width="14">&nbsp;</td>
						<td width="56">
							<!-- 로그인 세션이 존재하지 않으면, 로그인을 안했다면 --> <c:if test="${empty user}">
								<a href="/user/loginView.jsp" target="rightFrame">LOGIN</a>
							</c:if> <c:if test="${!empty user}">
								<a href="/user/logout" target="_parent">LOGOUT</a>
							</c:if>

						</td>
					</tr>
				</table>
			</td>
			<td height="20" background="/images/img_bg.gif">&nbsp;</td>
		</tr>
	</table>

</body>
</html>
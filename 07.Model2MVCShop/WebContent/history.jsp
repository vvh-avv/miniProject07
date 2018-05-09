<%@ page contentType="text/html; charset=EUC-KR"%>

<html>
<head>
<title>열어본 상품 보기</title>
</head>

<body>
당신이 열어본 상품을 알고 있다
<br>
<br>
<%
	request.setCharacterEncoding("euc-kr");
	response.setCharacterEncoding("euc-kr");
	
	String history = null;
	
	Cookie[] cookies = request.getCookies();
	
	if (cookies!=null && cookies.length > 0) {
		//System.out.println("==============================");
		//System.out.println("1 쿠키가 존재합니다");
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			//System.out.println("2 어떤쿠키가 있나요? "+cookie.getName()+"의 "+cookie.getValue()+"값이 있네요!" );
			if (cookie.getName().equals("history")) {
				//System.out.println("3 히스토리 쿠키도 존재하네요! "+cookie.getValue());
				history = cookie.getValue();
			}
		}
		if (history != null) {
			//System.out.println("4 최종 히스토리는 null이 아닙니다");
			String[] h = history.split(",");
			for (int i = 0; i < h.length; i++) {
				if (!h[i].equals("null")) {
					//System.out.println("5 쪼갠 히스토리의 조각 중 null이 아닌게 있습니다. 출력할게요~");
%>
	<a href="/product/getProduct?prodNo=<%=h[i]%>&menu=search" target="rightFrame"><%=h[i]%></a>
	<br>
<%
				}
			}
		}
	}
%>

<!-- ${cookie.history.value} -->
<!-- ${cookie.JSESSIONID.value} -->
</body>
</html>
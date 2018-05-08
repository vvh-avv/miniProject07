<%@ page contentType="text/html; charset=euc-kr"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>회원탈퇴</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
<!--
function fncQuitCheck() {
	var radio = document.detailForm.reason;
	var chkCnt = 0;
	var chkValue ="";
	
	for(var i=0; i<radio.length; i++){
		if(radio[i].checked==true){
			chkCnt++;
			chkValue += radio[i].value;
		}
	}
	
	if( chkCnt==0 || (chkValue=='기타'&&document.detailForm.reasonText.value=="") ){
		alert("사유는 반드시 입력해주시길 바랍니다.");
	}else{
		if(chkValue=='기타') { chkValue = document.detailForm.reasonText.value; }
		
		document.detailForm.action='/user/quitUser?userId=${user.userId}&reason='+chkValue;
	    document.detailForm.submit();
	    
	    top.opener.location="/user/loginView.jsp"
	    window.close();
	    
	}
	
}
-->
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

	<form name="detailForm" method="post">
		<h4>
			정말 탈퇴하시겠습니까?
			</h2>
			탈퇴사유 : <br>
			<input type="radio" name="reason" value="개인정보 때문에"><font size="2">개인정보 때문에</font><br>
			<input type="radio" name="reason" value="마음에 드는 상품이 없어서"><font size="2">마음에 드는 상품이 없어서</font><br>
			<input type="radio" name="reason" value="기타"><font size="2">기타</font>&nbsp;<input type="text" name="reasonText"><br>
			<br>
			<input type="button" value="확인" onclick="javascript:fncQuitCheck();">
			<input type="button" value="취소" onclick="javascript:window.close();">
	</form>

</body>
</html>
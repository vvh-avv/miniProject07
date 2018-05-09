<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
<!--
	// 검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  
	function fncGetList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();
	}
-->
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm" action="/purchase/listPurchase" method="post">

			<table width="100%" height="37" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="15" height="37"><img src="/images/ct_ttl_img01.gif" width="15" height="37"></td>
					<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="93%" class="ct_ttl01">구매 목록조회</td>
							</tr>
						</table>
					</td>
					<td width="12" height="37"><img src="/images/ct_ttl_img03.gif" width="12" height="37"></td>
				</tr>
			</table>

			<!-- 0413 검색조건 추가  -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
				<tr>
					<!-- 서치할 데이터 입력값이 존재하는 경우 -->
					<c:if test="${!empty search.searchCondition}">
						<td align="right">
							<select name="searchCondition" class="ct_input_g" style="width: 80px">
								<option value="0" selected>상품명</option>
							</select>
							<input type="text" name="searchKeyword" onkeypress="javascript:if(event.keyCode==13) fncGetList('1');"
									class="ct_input_g" style="width: 200px; height: 19px" value="${param.searchKeyword}">
						</td>
					</c:if>

					<!-- 서치할 데이터 입력값이 존재하지 않는 경우 -->
					<c:if test="${empty search.searchCondition }">
						<td align="right">
							<select name="searchCondition" class="ct_input_g" style="width: 80px">
								<option value="0">상품명</option>
							</select>
							<input type="text" name="searchKeyword" class="ct_input_g" style="width: 200px; height: 19px"></td>
					</c:if>

					<td align="right" width="70">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="17" height="23"><img src="/images/ct_btnbg01.gif" width="17" height="23"></td>
								<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top: 3px;">
									<a href="javascript:fncGetList('1');">검색</a></td>
								<td width="14" height="23"><img src="/images/ct_btnbg03.gif" width="14" height="23"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<!-- 0413 검색조건 추가 끝 -->


			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
				<tr>
					<td colspan="17">전체 ${resultPage.totalCount} 건수, 현재 ${resultPage.currentPage} 페이지</td>
				</tr>
				<tr>
					<td class="ct_list_b" width="50">No</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="100">주문일자 <!-- 주문일자 기준 소팅기능 추가 -->
						<c:choose>
							<c:when test="${requestScope.sort=='asc'}">
								<a onclick="location.href='/purchase/listPurchase?sort=desc';" style="cursor: pointer"> ↓ </a>
							</c:when>
							<c:otherwise>
								<a onclick="location.href='/purchase/listPurchase?sort=asc';" style="cursor: pointer"> ↑ </a>
							</c:otherwise>
						</c:choose> <!-- 주문일자 기준 소팅기능 끝 -->

					</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">주문번호</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">구매상품</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">받는 분</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">배송지 주소</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">전화번호</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">배송현황</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">정보수정</td>
				</tr>
				<tr>
					<td colspan="17" bgcolor="808285" height="1"></td>
				</tr>

				<c:set var="i" value="0" />
				<c:forEach var="purchase" items="${list}">
					<c:set var="i" value="${i+1}" />

					<tr class="ct_list_pop">
						<td align="center">${i}</td>
						<td></td>

						<!-- 0413 주문일자, 주문번호, 구매정보 노출시키기 -->
						<td align="center">${purchase.orderDate}</td>
						<td></td>

						<td align="center">
							<a href="/purchase/getPurchase?tranNo=${purchase.tranNo}">${purchase.tranNo}</a>
						</td>
						<td></td>

						<td align="center">
							<a href="/product/updateProduct?prodNo=${purchase.purchaseProd.prodNo}&menu=search">${purchase.purchaseProd.prodName}</a>
						</td>
						<td></td>

						<td align="left">${purchase.receiverName}</td>
						<td></td>

						<td align="left">${purchase.divyAddr}</td>
						<td></td>

						<td align="left">${purchase.receiverPhone}</td>
						<td></td>
						<!-- 추가완료 -->

						<td align="left">
							<!-- 주문취소, 반품상태를 복구할 수 있기 때문에 처리 전 현재 tranCode값을 session에 저장함 -->
							<!-- tranCodeTemp 값이 0보다 작은 경우를 추가 한 이유는..세션에 잘못된 값이 들어갔을 때를 대비한 것 -->
							<c:if test="${sessionScope.tranCodeTemp<0 || empty sessionScope.tranCodeTemp}">
								<c:set var="tranCodeTemp" value="${purchase.tranCode}" scope="session" />
							</c:if>
							<c:choose>
								<c:when test="${purchase.tranCode=='1'}">
									현재 구매완료 상태입니다.
									<!-- 0418 취소 기능 추가 -->
									<a href="/purchase/updateTranCode?tranNo=${purchase.tranNo}&tranCode=-1"><font color='red'>주문취소</font></a>
								</c:when>

								<c:when test="${purchase.tranCode=='2'}">
									현재 배송중 상태입니다.
									<a href="/purchase/updateTranCode?tranNo=${purchase.tranNo}&tranCode=3"><font color='blue'>물건도착</font></a>
									<!-- 0418 취소 기능 추가 -->
									<a href="/purchase/updateTranCode?tranNo=${purchase.tranNo}&tranCode=-2"><font color='red'>상품반품</font></a>
								</c:when>

								<c:when test="${purchase.tranCode=='3'}">
									현재 배송완료 상태입니다.
									<!-- 0418 취소 기능 추가 -->
									<a href="/purchase/updateTranCode?tranNo=${purchase.tranNo}&tranCode=-2"><font color='red'>상품반품</font></a>
								</c:when>

								<c:when test="${purchase.tranCode=='-1'}">
									현재 주문취소 상태입니다.
								</c:when>

								<c:when test="${purchase.tranCode=='-2'}">
									현재 반품신청 상태입니다.
								</c:when>

								<c:when test="${purchase.tranCode=='-3'}">
									현재 반품완료 상태입니다.
								</c:when>
							</c:choose>

						</td>
						<td></td>
						<td align="left"></td>
					</tr>
					<tr>
						<td colspan="17" bgcolor="D6D7D6" height="1"></td>
					</tr>
				</c:forEach>
			</table>

			<!-- PageNavigation Start... -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
				<tr>
					<td align="center">
						<input type="hidden" id="currentPage" name="currentPage" value="" />
						<jsp:include page="../common/pageNavigator.jsp" /></td>
				</tr>
			</table>
			<!-- PageNavigation End... -->

		</form>

	</div>
</body>
</html>
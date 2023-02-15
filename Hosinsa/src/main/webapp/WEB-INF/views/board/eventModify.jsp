<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"   uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://code.jquery.com/jquery-3.6.3.min.js" integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU=" crossorigin="anonymous"></script>
<link rel="stylesheet" href="../../../resources/css/hosinsa.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css">

<div class="contentWrap">
	<form class="eventModify" role="form" action="/board/event/modify" enctype="multipart/form-data" method="post">
		<div class="imgWrap">
			<img class="event_img" src="${event.event_img}">
			<input type="file" name="uploadFile" class="event_imgFile hidden">
		</div>
		<input type="text" name="event_img" value="${event.event_img}">
		<h4 class="miniTitle">Event Info <i>이벤트 정보</i></h4>
		<table class="infoTable">
			<tr>
				<th> 번호 </th>
				<td><input type="text" name="event_no" value="${event.event_no}" readonly></td>
			</tr>
			<tr>
				<th> 타이틀 </th>
				<td><input type="text" name="title" value="${event.title}"></td>
			</tr>
			<tr>
				<th> 소제목 </th>
				<td><input type="text" name="subtext" value="${event.subtext}"></td>
			</tr>			
			<tr>
				<th> 시작일 </th>
				<td>
					<input type="date" name="start_date" value="<fmt:formatDate pattern='yyyy-MM-dd' value='${event.start_date}'/>">
				</td>
			</tr>
			<tr>
				<th> 종료일 </th>
				<td>
					<input type="date" name="end_date" value="<fmt:formatDate pattern='yyyy-MM-dd' value='${event.end_date}'/>">
				</td>
			</tr>
		</table>
		<h4 class="miniTitle">상세정보</h4>
		<textarea class="inputDetail" name="content" cols="70" rows="16">${event.content}</textarea>
		<div class="btnWrap right">
			<button class="btn modify">이벤트 수정</button>
			<button class="btn remove">이벤트 삭제</button>
			<button class="btn list">뒤로</button>
		</div>
	</form>
</div>

<script src="../../../resources/js/board.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	if("${register}"==="success"){
		alert("수정 요청이 성공적으로 처리되었습니다.");
	}
	if("${modify}"==="success"){
		alert("수정 요청이 성공적으로 처리되었습니다.");
	}
	if("${remove}"==="success"){
		alert("제품 삭제가 성공적으로 처리되었습니다.");
	}
});
</script>

<%@ include file="../includes/footer.jsp" %>
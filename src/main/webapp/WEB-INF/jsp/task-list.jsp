<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
	<%@ include file="common/global.jsp"%>
	<%@ include file="common/meta.jsp"%>
	<title>待办任务</title>

</head>
<body>
<h1 style="text-align: center">待办任务</h1>
<c:if test="${not empty message}">
	<div>
		<div style="margin-top: 20px; padding: 0 .7em;">
			<p><span style="float: left; margin-right: .3em;"></span>
				<strong>提示：</strong>${message}</p>
		</div>
	</div>
</c:if>
	<table width="100%" border="1" style="border-color: #9fbbb4; border-collapse: collapse;">
		<thead>
		<tr>
			<th>办理人</th>
			<th>任务生成时间</th>
			<th>任务名</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${taskModels }" var="task">
			<tr id="${task.id }">
				<td>${task.assignee }</td>
				<td><fmt:formatDate value="${task.createTime }" type="date" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<a href='${ctx}/diagram-viewer/index.html?processDefinitionId=${task.processDefId}&processInstanceId=${task.processInstanceId}' target="_parent" title="点击查看流程图">${task.name }</a>
				</td>
				<td>
					<c:if test="${empty task.assignee }">
						<a href="${ctx }/task/claim/${task.id}">签收</a>
					</c:if>
					<c:if test="${not empty task.assignee }">
						<a href="${ctx }/task/complete/${task.id}">办理</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
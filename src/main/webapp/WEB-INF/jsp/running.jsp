<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
	<%@ include file="common/global.jsp"%>
	<%@ include file="common/meta.jsp"%>
	<title>运行中流程</title>

</head>
<body>
<h1 style="text-align: center">运行中流程</h1>

	<table width="100%" border="1" style="border-color: #9fbbb4; border-collapse: collapse;">
		<thead>
		<tr>
			<th>申请人</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>当前节点</th>
			<th>任务创建时间</th>
			<th>流程状态</th>
			<th>当前处理人</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${process }" var="proc">
			<tr id="${proc.id }">
				<td>${proc.appPerson }</td>
				<td>${proc.startTime }</td>
				<td>${proc.endTime }</td>
				<td>
					<a href='${ctx}/diagram-viewer/index.html?processDefinitionId=${proc.id}&processInstanceId=${proc.procInstanceId}' target="_parent" title="点击查看流程图">${proc.currentTaskName }</a>
				</td>
				<td>${proc.curretTaskTime }</td>
				<td>${proc.suspended ? "已挂起" : "正常" }；<b title='流程版本号'>V: ${proc.version }</b></td>
				<td>${proc.currentAssign }</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: Aaron
  Date: 2019/8/6
  Time: 0:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="common/global.jsp"%>
    <%@ include file="common/meta.jsp"%>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

    <title>菜单</title>
</head>
<body>
<h1>菜单</h1>
<table width="50%" border="1" style="border-color: #9fbbb4; border-collapse: collapse;">
    <tr>
        <th><a href="${ctx}/model/list" target="_blank">模型列表</a></th>
    </tr>
    <tr>
        <th><a href="${ctx}/process/process-list" target="_blank">流程列表</a></th>
    </tr>
    <tr>
        <th><a href="${ctx}/process/list/running" target="_blank">运行中流程</a></th>
    </tr>
</table>
</body>
</html>

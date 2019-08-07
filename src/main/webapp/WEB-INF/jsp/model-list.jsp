
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="common/global.jsp"%>
    <%@ include file="common/meta.jsp"%>
    <title>模型列表</title>

    <script type="text/javascript">
        $(function() {
            $('#create').button({
                icons: {
                    primary: 'ui-icon-plus'
                }
            }).click(function() {
                $('#createModelTemplate').dialog({
                    modal: true,
                    width: 500,
                    buttons: [{
                        text: '创建',
                        click: function() {
                            if (!$('#name').val()) {
                                alert('请填写名称！');
                                $('#name').focus();
                                return;
                            }
                            setTimeout(function() {
                                location.reload();
                            }, 1000);
                            $('#modelForm').submit();
                        }
                    }]
                });
            });
        });

        //显示灰色 jQuery 遮罩层
        function showBg() {
            var bh = $("body").height();
            var bw = $("body").width();
            $("#fullbg").css({
                height:bh,
                width:bw,
                display:"block"
            });
            $("#dialog").show();
        }
        //关闭灰色 jQuery 遮罩
        function closeBg() {
            $("#fullbg,#dialog").hide();
        }

    </script>

    <style type="text/css">
        body {
            font-family:Arial, Helvetica, sans-serif;
            font-size:12px;
            margin:0;
        }
        #main {
            height:10px;
            padding-top:10px;
            text-align:right;
        }
        #fullbg {
            background-color:gray;
            left:0;
            opacity:0.5;
            position:absolute;
            top:0;
            z-index:3;
            filter:alpha(opacity=50);
            -moz-opacity:0.5;
            -khtml-opacity:0.5;
        }
        #dialog {
            background-color:#fff;
            border:5px solid rgba(0,0,0, 0.4);
            height:400px;
            left:50%;
            margin:-200px 0 0 -200px;
            padding:1px;
            position:fixed !important; /* 浮动对话框 */
            position:absolute;
            top:50%;
            width:400px;
            z-index:5;
            border-radius:5px;
            display:none;
        }
        #dialog p {
            margin:0 0 12px;
            height:24px;
            line-height:24px;
            background:#CCCCCC;
        }
        #dialog p.close {
            text-align:right;
            padding-right:10px;
        }
        #dialog p.close a {
            color:#fff;
            text-decoration:none;
        }

    </style>

</head>
<body>
<h1 style="text-align: center">模型列表</h1>
<c:if test="${not empty message}">
    <div>
        <div style="margin-top: 20px; padding: 0 .7em;">
            <p><span style="float: left; margin-right: .3em;"></span>
                <strong>提示：</strong>${message}</p>
        </div>
    </div>
</c:if>

<div id="main"><a href="javascript:showBg();">创建</a>
    <div id="fullbg"></div>
    <div id="dialog">
        <p class="close"><a href="#" onclick="closeBg();">关闭</a></p>
        <div id="createModelTemplate" title="创建模型" class="template">
            <form id="modelForm" action="${ctx}/model/create" target="_blank" method="post">
                <table >
                    <tr>
                        <td>名称：</td>
                        <td>
                            <input id="name" name="name" type="text" />
                        </td>
                    </tr>
                    <tr>
                        <td>KEY：</td>
                        <td>
                            <input id="key" name="key" type="text" />
                        </td>
                    </tr>
                    <tr>
                        <td>描述：</td>
                        <td>
                            <textarea id="description" name="description" style="width:300px;height: 50px;"></textarea>
                        </td>
                    </tr>
                </table>
                <input type="submit" id="submit" value="创建" />
            </form>
        </div>
    </div>
</div>

<%--<div style="text-align: right"><button id="create">创建</button></div>--%>
<table width="100%" border="1" style="border-color: #9fbbb4; border-collapse: collapse;">
    <thead>
    <tr>
        <th>ID</th>
        <th>KEY</th>
        <th>Name</th>
        <th>Version</th>
        <th>创建时间</th>
        <th>最后更新时间</th>
        <th>元数据</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list }" var="model">
        <tr>
            <td>${model.id }</td>
            <td>${model.key }</td>
            <td>${model.name}</td>
            <td>${model.version}</td>
            <td>${model.createTime}</td>
            <td>${model.lastUpdateTime}</td>
            <td>${model.metaInfo}</td>
            <td>
                <a href="${ctx}/modeler.html?modelId=${model.id}" target="_blank">编辑</a>
                <a href="${ctx}/model/deploy/${model.id}">部署</a>
                导出(<a href="${ctx}/model/export/${model.id}/bpmn" target="_blank">BPMN</a>
                |&nbsp;<a href="${ctx}/model/export/${model.id}/json" target="_blank">JSON</a>)
                <a href="${ctx}/model/delete/${model.id}">删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>

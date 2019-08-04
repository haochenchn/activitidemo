<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="common/global.jsp"%>
    <%@ include file="common/meta.jsp"%>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

    <title>登录</title>
    <style>
        #login
        {
            box-shadow:
                    0 0 2px rgba(0, 0, 0, 0.2),
                    0 1px 1px rgba(0, 0, 0, .2),
                    0 3px 0 #fff,
                    0 4px 0 rgba(0, 0, 0, .2),
                    0 6px 0 #fff,
                    0 7px 0 rgba(0, 0, 0, .2);
        }

        #login
        {
            position: absolute;
            z-index: 0;
        }

        #login:before
        {
            content: '';
            position: absolute;
            z-index: -1;
            border: 1px dashed #ccc;
            top: 5px;
            bottom: 5px;
            left: 5px;
            right: 5px;
            -moz-box-shadow: 0 0 0 1px #fff;
            -webkit-box-shadow: 0 0 0 1px #fff;
            box-shadow: 0 0 0 1px #fff;
        }
        h1
        {
            text-shadow: 0 1px 0 rgba(255, 255, 255, .7), 0px 2px 0 rgba(0, 0, 0, .5);
            text-transform: uppercase;
            text-align: center;
            color: #666;
            margin: 0 0 30px 0;
            letter-spacing: 4px;
            font: normal 26px/1 Verdana, Helvetica;
            position: relative;
        }

        h1:after, h1:before
        {
            background-color: #777;
            content: "";
            height: 1px;
            position: absolute;
            top: 15px;
            width: 120px;
        }

        h1:after
        {
            background-image: -webkit-gradient(linear, left top, right top, from(#777), to(#fff));
            background-image: -webkit-linear-gradient(left, #777, #fff);
            background-image: -moz-linear-gradient(left, #777, #fff);
            background-image: -ms-linear-gradient(left, #777, #fff);
            background-image: -o-linear-gradient(left, #777, #fff);
            background-image: linear-gradient(left, #777, #fff);
            right: 0;
        }

        h1:before
        {
            background-image: -webkit-gradient(linear, right top, left top, from(#777), to(#fff));
            background-image: -webkit-linear-gradient(right, #777, #fff);
            background-image: -moz-linear-gradient(right, #777, #fff);
            background-image: -ms-linear-gradient(right, #777, #fff);
            background-image: -o-linear-gradient(right, #777, #fff);
            background-image: linear-gradient(right, #777, #fff);
            left: 0;
        }
    </style>
</head>
<body>
    <form id="login" action="${ctx}/login/login" method="post">
        <h1>Log In</h1>
        <fieldset id="inputs">
            <input id="id" name="id" type="text" placeholder="Username" autofocus required>
            <input id="pwd" name="pwd" type="password" placeholder="Password" required>
        </fieldset>
        <fieldset id="actions">
            <input type="submit" id="submit" value="登录" />
        </fieldset>
    </form>
</body>


</html>
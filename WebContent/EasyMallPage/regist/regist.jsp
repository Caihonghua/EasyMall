<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>欢迎注册EasyMall</title>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/EasyMallPage/regist/css/regist.css"/>
	</head>
	<body>
		<form action="<%=request.getContextPath()%>/servlet/RegistServlet" method="POST">
		<%
			Random rand= new Random();
			int token=rand.nextInt();
			session.setAttribute("token", token);
		%>
		<input type="hidden" name="token" value="<%=token%>"/>
		<!-- 特别注意，form表单里面的action路径要和servlet类的路径相同 -->
			<h1>欢迎注册EasyMall</h1>
			<table>
				<tr>
					<td class="tds">用户名：</td>
					<td>
						<input type="text" name="username" value="<%=request.getParameter("username")==null?"":request.getParameter("username")%>"/>
					</td>
				</tr>
				<tr>
					<td class="tds">密码：</td>
					<td>
						<input type="password" name="password"/>
					</td>
				</tr>
				<tr>
					<td class="tds">确认密码：</td>
					<td>
						<input type="password" name="password2"/>
					</td>
				</tr>
				<tr>
					<td class="tds">昵称：</td>
					<td>
						<input type="text" name="nickname" value="<%=request.getParameter("nickname")==null?"":request.getParameter("nickname")%>"/>
					</td>
				</tr>
				<tr>
					<td class="tds">邮箱：</td>
					<td>
						<input type="text" name="email" value="a@163.com"/>
					</td>
				</tr>
				<tr>
					<td class="tds">验证码：</td>
					<td>
						<input type="text" name="valistr"/>
						<img onclick="changeImage(this)" src="<%=request.getContextPath()%>/servlet/ValiImageServlet" />
					</td>
					<script>
					function changeImage(thisobj){
						thisobj.src="<%=request.getContextPath()%>/servlet/ValiImageServlet?time="+ new Date().getTime();
					
					}
					</script>
	
				</tr>
				<tr>
					<td class="sub_td" colspan="2" class="tds">
						<input type="submit" value="注册用户"/>
					</td>
				</tr>
				<tr>
					<td class="sub_td" colspan="2" class="tds">
						<%=request.getAttribute("msg")==null? "" : request.getAttribute("msg") %>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>

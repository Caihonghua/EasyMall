<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/EasyMallPage/index/css/index.css"/>
		<title>欢迎光临EasyMall</title>
	</head>
	<body>
		<div id="index">
	
		<%@ include file="/EasyMallPage/head/_head.jsp" %>
		
			<div id="line1">
				<img src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/banner_big.jpg"/>
			</div>
			<div id="line2">
				<img id="line2_1" src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/adv1.jpg"/>
				<img id="line2_2" src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/adv2.jpg"/>
				<img id="line2_3" src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/adv_l1.jpg"/>
			</div>
			<div id="line3">
				<img id="line3_1" src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/adv3.jpg"/>
				<img id="line3_2" src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/adv4.jpg"/>
				<div id="line3_right">
					<img id="line3_3" src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/adv_l2.jpg"/>
					<img id="line3_4" src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/adv_l3.jpg"/>
				</div>
			</div>
			<div id="line4">
				<img src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/217.jpg"/>
			</div>
			<div id="line5">
				<span id="line5_1">
					<img src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/icon_g1.png"/>&nbsp;&nbsp;500强企业 品质保证
				</span>
				<span id="line5_2">
					<img src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/icon_g2.png"/>&nbsp;&nbsp;7天退货 15天换货 
				</span>
				<span id="line5_3">
					<img src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/icon_g3.png"/>&nbsp;&nbsp;100元起免运费 
				</span>
				<span id="line5_4">
					<img src="<%=request.getContextPath()%>/EasyMallPage/index/img/index/icon_g4.png"/>&nbsp;&nbsp;448家维修网点 全国联保 
				</span>
			</div>
			<%@ include file="/EasyMallPage/foot/_foot.jsp" %>
		</div>
	</body>
</html>

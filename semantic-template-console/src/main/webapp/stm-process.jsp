<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>控制台 - Bootstrap后台管理系统模版Ace下载</title>
    <meta name="description" content="站长素材提供Bootstrap模版,Bootstrap教程,Bootstrap中文翻译等相关Bootstrap插件下载">
    <link href="../static/assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../static/assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400,300">
    <link rel="stylesheet" href="../static/assets/css/ace.min.css">
    <link rel="stylesheet" href="../static/assets/css/ace-rtl.min.css">
    <link rel="stylesheet" href="../static/assets/css/ace-skins.min.css">
    <style type="text/css">
        .jqstooltip {
            position: absolute;
            left: 0px;
            top: 0px;
            visibility: hidden;
            background: rgb(0, 0, 0) transparent;
            background-color: rgba(0, 0, 0, 0.6);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);
            -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";
            color: white;
            font: 10px arial, san serif;
            text-align: left;
            white-space: nowrap;
            padding: 5px;
            border: 1px solid white;
            z-index: 10000;
        }
        .label{
            margin:2px;
        }
        .page-content {
            background: #fff;
            margin: 0;
            padding: 3px 20px 24px;
        }
        .table thead>tr>th, .table tbody>tr>th, .table tfoot>tr>th, .table thead>tr>td, .table tbody>tr>td, .table tfoot>tr>td {
            padding: 3px;
            vertical-align: top;
            border-top: 1px solid #ddd;
            font-size: 12px;
        }

        .modifiedSentence{
            color : blue;
            font-weight: bold;
            display: none;
        }
        .hidden{
            display : none;
        }
        .jqsfield {
            color: white;
            font: 10px arial, san serif;
            text-align: left;
        }
    </style>
    <script type="text/javascript">
        //"处理文本展示/隐藏"按钮，点击后会展示/隐藏每段对话的修改语句（蓝色）。
        function slideModifiedSentence(){
            $(".modifiedSentence").slideToggle();
        }

        function showDateTime(){
            $(".dateTime").slideToggle();
        }

        //点击“查询”按钮，根据账号、用户号、起始时间、结束时间来查询出来的对话列表。
        function getProcessData(){
            var accountId = $("#accountId").val();
            var userId = $("#userId").val();
            var fromDateTime = $("#fromDateTime").val();
            var toDateTime = $("#toDateTime").val();
            var url = "../log/getProcessData.do";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify({
                    accountId: accountId,
                    userId: userId,
                    fromDateTime: fromDateTime,
                    toDateTime: toDateTime
                }),
                cache: false,
                ifModified: true,
                contentType:"application/json;charset=utf-8",
                success: function (data) {
                    var jsonData = JSON.parse(data);

                    $("#conversationData").empty();
                    for(i = 0, len = jsonData.length; i < len; i ++){
                        var contextId = jsonData[i].contextId;
                        var dateTime = jsonData[i].dateTime;
                        var id = jsonData[i].id;
                        var sentence = jsonData[i].sentence;
                        var sentenceModified = (jsonData[i].sentenceModified) ? jsonData[i].sentenceModified : '---';
                        var responseMsg = (jsonData[i].responseMsg) ? jsonData[i].responseMsg : '---';
                        var responseType = jsonData[i].responseType;                                                                            //根据responseType决定responseMsg颜色
                        var  responseMsgSpan = "                                                           <div class=\"modifiedSentence\">" + sentenceModified + "</div><div>" + responseMsg + "</div>"  ;
                        if(responseType == 4) {
                            responseMsgSpan = "                                                            <div class=\"modifiedSentence\">" + sentenceModified + "</div><div style=\"color: red;\">" + responseMsg + "</div>"  ;
                        }
                        $("#conversationData").append("<tr>\n" +
                            "                                                    <td width=\"20%\" onclick=\"getOneLogDataProcess(" + id + ")\">" + contextId + "&nbsp;&nbsp;<i id=\"eye_"+ id + "\"\" class=\"icon-eye-open hidden\"/><br/><span class=\"dateTime\">" + dateTime + "</span></td>\n" +
                            "                                                    <td width=\"40%\">\n" +
                            "                                                        <div onclick=\"getOneLogDataProcess(" + id + ")\">\n" +
                            "                                                           " + sentence + "&nbsp;\n" +
                            "                                                        </div>\n" +
                            "                                                    </td>\n" +
                            "                                                    <td width=\"40%\" class=\"hidden-480\">\n" +
                            "                                                        <div onclick=\"getOneLogDataProcess(" + id + ")\">\n" + responseMsgSpan +
                            "                                                        </div>\n" +
                            "                                                    </td>\n" +
                            "                                                </tr>");
                    }
                }
            });
        }


        //点击每一段“对话”，会获取这段对话的LogDataProcess数据，并将其数据图形化展示到左边的表格里面。
        function getOneLogDataProcess(log_data_process_id){
            var url = "../log/getOneLogDataProcess.do?id=" + log_data_process_id;
            $.ajax({
                type:"post",
                url: url,
                data:{
                    "id":log_data_process_id
                },
                cache:false,
                ifModified :true ,
                contentType:"application/json;charset=utf-8",
                success:function(data) {
                    var obj = JSON.parse(data);
                    var params = obj.accountId + "," + obj.userId + "," + obj.contextId + "," + obj.currentTimeMillis;

                    $("#detailContextOwner").empty();
                    $("#responseType").empty();
                    $("#dateTime").empty();
                    $("#sentence").empty();
                    $("#sentenceModified").empty();
                    $("#responseMsg").empty();

                    $("#segmentWeights").empty();
                    $("#state").empty();
                    $("#selectResult").empty();
                    $("#processTrace").empty();

                    $("#lpmRedundantWi").empty();
                    $("#lpmMatchedRew").empty();
                    $("#lpmMatchedRre").empty();
                    $("#lpmLackedRre").empty();

                    $("#cpmRedundantWi").empty();
                    $("#cpmMatchedRew").empty();
                    $("#cpmMatchedRre").empty();
                    $("#cpmLackedRre").empty();

                    $("#fpmRedundantWi").empty();
                    $("#fpmMatchedRew").empty();
                    $("#fpmMatchedRre").empty();
                    $("#fpmLackedRre").empty();

                    $("#configureParams").empty();

                    //1-1.会话所有者
                    $("#detailContextOwner").append("<button id=\"btnLPMData\" class=\"btn btn-xs\" onclick=\"goCalculationLogData(" + params + ")\">" + obj.detailContextOwner + "</button> <span id=\"checkHaveData\" class=\"label label-warning hidden\">没有数据！</span>");
                    //1-2.回答类型
                    if(obj.responseType == '1') $("#responseType").append("完全匹配，运行中意图");
                    if(obj.responseType == '2') $("#responseType").append("完全匹配，非运行中意图");
                    if(obj.responseType == '3') $("#responseType").append("半匹配");
                    if(obj.responseType == '4') $("#responseType").append("没有匹配上");

                    //1-2.上下文状态
                    var state = obj.state;
                    if(state == '-1'){
                        $("#state").append("<div class=\"badge badge-warning\" style=\"position: relative;\">" + state + "</div> <span>缺参上下文状态</span>");
                    }else if(state == '0'){
                        $("#state").append("<div class=\"badge badge-success\" style=\"position: relative;\">" + state + "</div> <span>纯净上下文状态</span>");
                    }else{
                        $("#state").append("<div class=\"badge badge-info\" style=\"position: relative;\">" + state + "</div> <span>运行意图上下文状态</span>");
                    }

                    //1-3.时间
                    var dateTime = obj.dateTime;
                    $("#dateTime").append(dateTime);

                    //2.原句
                    var sentence = obj.sentence;
                    $("#sentence").text(sentence);

                    //3.改写句
                    var sentenceModified = obj.sentenceModified;
                    if(sentenceModified){  $("#sentenceModified").text(sentenceModified); }

                    //4.回答
                    var responseMsg = obj.responseMsg;
                    if(responseMsg){ $("#responseMsg").text(responseMsg);  }

                    //5.对原句的分词和权重
                    var svWords = obj.svWords;
                    var svWeights = obj.svWeights;
                    $("#segmentWeights").append("<span>" + svWords + "</span><br/><span>" + svWeights + "</span>");

                    //6.结果数值
                    var selectResultItems = obj.selectResult.split("|");
                    var matchType = "";
                    switch (selectResultItems[0]){
                        case "1" : { matchType = "LPM"; break; }
                        case "2" : { matchType = "CPM"; break; }
                        case "3" : { matchType = "FPM";break; }
                    }
                    $("#selectResult").append("<span class=\"label label-important\" style=\"position: relative;\">" + matchType + "</span> ")
                    $("#selectResult").append("<span class=\"label label-warning\" style=\"position: relative;\"> 规则 : (" + selectResultItems[1] + ") " + selectResultItems[2] + "</span> ")
                    $("#selectResult").append("<span class=\"label label-warning\" style=\"position: relative;\"> 算法编号 : " + selectResultItems[3] + "</span> ")
                    $("#selectResult").append("<span class=\"label label-important\" style=\"position: relative;\"> 相似度值 : " + selectResultItems[4] + "</span> ")
                    $("#selectResult").append("<span class=\"label label-warning\" style=\"position: relative;\"> 相似度阈值 : " + selectResultItems[5] + "</span> ")

                    //7.会话轨迹
                    var processTraceItems = obj.processTrace.split("--->");
                    processTraceItems.forEach(function(val, index, arr){
                        if(val.indexOf('LPMC') != -1 || val.indexOf('CPMC') != -1 || val.indexOf('FPMC') != -1 ){
                            $("#processTrace").append("<span class=\"label label-danger arrowed-right\">" + val + "</span>");
                        }else{
                            $("#processTrace").append("<span class=\"label label-success arrowed-right\">" + val + "</span>");
                        }
                    });

                    //8.LPM实体
                    if(obj.lpmRedundantWi){
                        var lpmRedundantWiItems = obj.lpmRedundantWi.split("|");
                        for(i = 0, len = lpmRedundantWiItems.length; i < len; i ++) {
                            $("#lpmRedundantWi").append("<span class=\"label\" style=\"position: relative;\">" + lpmRedundantWiItems[i] + "</span> ")
                        }
                    }
                    if(obj.lpmMatchedRew){
                        var lpmMatchedRewItems = obj.lpmMatchedRew.split("|");
                        for(i = 0, len = lpmMatchedRewItems.length; i < len; i ++) {
                            $("#lpmMatchedRew").append("<span class=\"label label-warning\" style=\"position: relative;\">" + lpmMatchedRewItems[i] + "</span> ")
                        }
                    }
                    if(obj.lpmMatchedRre){
                        var lpmMatchedRreItems = obj.lpmMatchedRre.split("|");
                        for(i = 0, len = lpmMatchedRreItems.length; i < len; i ++) {
                            $("#lpmMatchedRre").append("<div class=\"badge badge-important\" style=\"position: relative;\">" + lpmMatchedRreItems[i] + "</div> ")
                        }
                    }
                    if(obj.lpmLackedRre){
                        var lpmLackedRreItems = obj.lpmLackedRre.split("|");
                        for(i = 0, len = lpmLackedRreItems.length; i < len; i ++) {
                            $("#lpmLackedRre").append("<div class=\"badge\" style=\"position: relative;\">" + lpmLackedRreItems[i] + "</div> ")
                        }
                    }

                    //9.CPM实体
                    if(obj.cpmRedundantWi){
                        var cpmRedundantWiItems = obj.cpmRedundantWi.split("|");
                        for(i = 0, len = cpmRedundantWiItems.length; i < len; i ++) {
                            $("#cpmRedundantWi").append("<span class=\"label\" style=\"position: relative;\">" + cpmRedundantWiItems[i] + "</span> ")
                        }
                    }
                    if(obj.cpmMatchedRew){
                        var cpmMatchedRewItems = obj.cpmMatchedRew.split("|");
                        for(i = 0, len = cpmMatchedRewItems.length; i < len; i ++) {
                            $("#cpmMatchedRew").append("<span class=\"label label-warning\" style=\"position: relative;\">" + cpmMatchedRewItems[i] + "</span> ")
                        }
                    }
                    if(obj.cpmMatchedRre){
                        var cpmMatchedRreItems = obj.cpmMatchedRre.split("|");
                        for(i = 0, len = cpmMatchedRreItems.length; i < len; i ++) {
                            $("#cpmMatchedRre").append("<div class=\"badge badge-important\" style=\"position: relative;\">" + cpmMatchedRreItems[i] + "</div> ")
                        }
                    }
                    if(obj.cpmLackedRre){
                        var cpmLackedRreItems = obj.cpmLackedRre.split("|");
                        for(i = 0, len = cpmLackedRreItems.length; i < len; i ++) {
                            $("#cpmLackedRre").append("<div class=\"badge\" style=\"position: relative;\">" + cpmLackedRreItems[i] + "</div> ")
                        }
                    }

                    //10.FPM实体
                    if(obj.fpmRedundantWi){
                        var fpmRedundantWiItems = obj.fpmRedundantWi.split("|");
                        for(i = 0, len = fpmRedundantWiItems.length; i < len; i ++) {
                            $("#fpmRedundantWi").append("<span class=\"label\" style=\"position: relative;\">" + fpmRedundantWiItems[i] + "</span> ")
                        }
                    }
                    if(obj.fpmMatchedRew){
                        var fpmMatchedRewItems = obj.fpmMatchedRew.split("|");
                        for(i = 0, len = fpmMatchedRewItems.length; i < len; i ++) {
                            $("#fpmMatchedRew").append("<span class=\"label label-warning\" style=\"position: relative;\">" + fpmMatchedRewItems[i] + "</span> ")
                        }
                    }
                    if(obj.fpmMatchedRre){
                        var fpmMatchedRreItems = obj.fpmMatchedRre.split("|");
                        for(i = 0, len = fpmMatchedRreItems.length; i < len; i ++) {
                            $("#fpmMatchedRre").append("<div class=\"badge badge-important\" style=\"position: relative;\">" + fpmMatchedRreItems[i] + "</div> ")
                        }
                    }
                    if(obj.fpmLackedRre){
                        var fpmLackedRreItems = obj.fpmLackedRre.split("|");
                        for(i = 0, len = fpmLackedRreItems.length; i < len; i ++) {
                            $("#fpmLackedRre").append("<div class=\"badge\" style=\"position: relative;\">" + fpmLackedRreItems[i] + "</div> ")
                        }
                    }

                    //11.配置参数
                    var json2map = JSON.parse(obj.configureParams);
                    for(var key in json2map){
                        $("#configureParams").append("<span class='green'>&nbsp" + key + ": " + json2map[key] + "&nbsp</span> ");
                    }

                    //12.眼睛标记
                    $(".icon-eye-open").addClass("hidden");
                    $("#eye_" + log_data_process_id).removeClass("hidden");
                }
            });
        }

        //点击"会话编号"的按钮，会去查看详细数据页面，展示详细计算数据（只要详细数据日志开启，有数据才行）
        function goCalculationLogData(accountId, userId, contextId, currentTimeMillis){
            var getParams = "accountId=" + accountId + "&userId=" + userId + "&contextId=" + contextId + "&currentTimeMillis=" + currentTimeMillis;

            var jsonParams = {
                accountId : accountId,
                userId : userId,
                contextId : contextId,
                currentTimeMillis : currentTimeMillis
            }
            var params = JSON.stringify(jsonParams);
            var url = "../log/checkLogDataCalculation.do";
            $.ajax({
                type: "POST",
                url: url,
                data: params,
                cache: false,
                ifModified: true,
                contentType:"application/json;charset=utf-8",
                success: function (data) {
                    if(data == '1'){
                        var url = "../log/getLogDataCalculation.do?" + getParams;
                        window.open(url);
                    }else{
                        $("#checkHaveData").show();
                    }
                }
            });
        }
    </script>
</head>

<body>
<div class="main-container" id="main-container">

    <div class="main-container-inner">
        <a class="menu-toggler" id="menu-toggler" href="#">
            <span class="menu-text"></span>
        </a>

        <%@ include file="/sidebar.jsp" %>

        <div class="main-content" style="margin-left: 100px;">
            <div class="breadcrumbs" id="breadcrumbs">
                <ul class="breadcrumb">
                    <li>
                        <i class="icon-home home-icon"></i>
                        <a href="#">首页</a>
                    </li>
                    <li class="active">控制台</li>
                </ul>
                <div class="nav-search" id="nav-search">
                    <input type="text" placeholder="输入账号" class="nav-search-input" id="accountId" autocomplete="off" style="width:100px;" value="1"/>
                    <input type="text" placeholder="输入用户号" class="nav-search-input" id="userId" autocomplete="off" style="width:100px;" value="2"/>
                    <input type="text" placeholder="输入起始时间" class="nav-search-input" id="fromDateTime" autocomplete="off" style="width:200px;" value="2018-02-01 00:00:00"/>
                    <input type="text" placeholder="输入结束时间" class="nav-search-input" id="toDateTime" autocomplete="off" style="width:200px;" value="2018-03-31 00:00:00"/>
                    <button onclick="getProcessData()" class="btn btn-xs btn-danger"> 查询 </button>
                </div>
            </div>

            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="row">
                            <div class="col-sm-7">
                                <div class="widget-box transparent">
                                    <div class="widget-header widget-header-flat">
                                        <h5 class="lighter">
                                            <i class="icon-star orange"></i>
                                            轨迹数据
                                        </h5>
                                    </div>

                                    <div class="widget-body">
                                        <div class="widget-main no-padding">
                                            <table class="table table-bordered table-striped">
                                                <tbody>
                                                <tr>
                                                    <td width="15%">对话拥有者</td>
                                                    <td width="45%">
                                                        <span id="detailContextOwner"></span>
                                                    </td>
                                                    <td>结果</td>
                                                    <td class="hidden-480">
                                                        <span id="responseType" class="label label-danger arrowed-in"></span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>上下文状态</td>
                                                    <td><span id="state"></span></td>
                                                    <td>时间</td>
                                                    <td class="hidden-480">
                                                        <span id="dateTime"></span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>结果数据</td>
                                                    <td colspan="3"><span id="selectResult"></span></td>
                                                </tr>
                                                <tr>
                                                    <td>原句</td>
                                                    <td colspan="3">
                                                        <span id="sentence"></span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>分词和权重</td>
                                                    <td colspan="3">
                                                        <span id="segmentWeights"></span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>修改句</td>
                                                    <td colspan="3">
                                                        <span id="sentenceModified"></span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>回答</td>
                                                    <td colspan="3">
                                                        <span id="responseMsg"></span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>轨迹</td>
                                                    <td colspan="3">
                                                        <span id="processTrace"></span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        LPM数据
                                                    </td>
                                                    <td colspan="3">
                                                        <div>
                                                            <span id="lpmRedundantWi"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="lpmMatchedRew"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="lpmMatchedRre"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="lpmLackedRre"></span>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        CPM数据
                                                    </td>
                                                    <td colspan="3">
                                                        <div>
                                                            <span id="cpmRedundantWi"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="cpmMatchedRew"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="cpmMatchedRre"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="cpmLackedRre"></span>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        FPM数据
                                                    </td>
                                                    <td colspan="3">
                                                        <div>
                                                            <span id="fpmRedundantWi"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="fpmMatchedRew"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="fpmMatchedRre"></span><br/>
                                                        </div>
                                                        <div>
                                                            <span id="fpmLackedRre"></span>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>标签说明</td>
                                                    <td colspan="3">
                                                        <span class="label" style="position: relative;">多余词语</span>
                                                        <span class="label label-warning" style="position: relative;">匹配上的REWI</span>
                                                        <div class="badge badge-important" style="position: relative;">匹配上的RRE</div>
                                                        <div class="badge" style="position: relative;">没有匹配上的RRE</div>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-5">
                                <div class="widget-box transparent">
                                    <div class="widget-header widget-header-flat">
                                        <h5 class="lighter">
                                            <i class="icon-star orange"></i>
                                            对话历史
                                        </h5>
                                    </div>
                                    <div class="widget-body">
                                        <div class="widget-main no-padding">
                                            <table class="table table-bordered table-striped">
                                                <thead class="thin-border-bottom">
                                                <tr>
                                                    <th width="20%">
                                                        <i class="icon-caret-right blue"></i>
                                                        上下文编号&nbsp;&nbsp;
                                                        <button onclick="showDateTime()" class="bigger-120 icon-calendar" style="padding: 0px;"></button>
                                                    </th>

                                                    <th width="40%">
                                                        <i class="icon-caret-right blue"></i>
                                                        人
                                                    </th>

                                                    <th class="hidden-480" width="40%">
                                                        <i class="icon-caret-right blue"></i>
                                                        机&nbsp;&nbsp;<button onclick="slideModifiedSentence()" class="btn btn-primary" style="padding: 0px;">显示/隐藏修改句子
                                                    </button>
                                                    </th>
                                                </tr>
                                                </thead>
                                            </table>
                                        </div>
                                        <div class="widget-main no-padding" style="height:480px; max-height:480px; overflow-x: hidden;">
                                            <table id="conversationData" class="table table-bordered table-striped">
                                                <tbody>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                                <div class="dataTables_paginate paging_bootstrap">
                                    <ul class="pagination pull-right no-margin">
                                        <li class="prev disabled"><a href="#"> < </a>
                                        </li>
                                        <li class="active"><a href="#">1</a></li>
                                        <li><a href="#">2</a></li>
                                        <li><a href="#">3</a></li>
                                        <li class="next"><a href="#"> > </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="../static/assets/js/jquery-2.0.3.min.js"></script>
<script src="../static/assets/js/ace-elements.min.js"></script>
<script src="../static/assets/js/ace.min.js"></script>

</body>
</html>
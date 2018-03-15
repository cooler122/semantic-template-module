<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    body { line-height: inherit;}
    table { font-size: 10px; border:1px; }
    .table tr{ font-size: 10px; border:1px; }
    .table th, .table td { padding : 1px; }
    .box{border : 0px;}
    .box-content {padding : 0px;}
    .tab-menu.nav-tabs > li { float : left;}
    input[type='text'] { margin-top:10px;}
    .cus_dark { margin-bottom: -56px; margin-right: -50px; position: relative; }
    .p { margin : 0px;}
    .timeslot { height:auto; width : 96%; margin: 5px 5px; }
    .timeslot .task {width : 40%;}
    .modifiedSentence {color: #0B90C4}
    #haveCalculationData { display : none;}

</style>

<script type="text/javascript">
    //"处理文本展示/隐藏"按钮，点击后会展示/隐藏每段对话的修改语句（蓝色）。
    function slideModifiedSentence() {
        $(".modifiedSentence").slideToggle();
    }

    //点击“查询”按钮，根据账号、用户号、起始时间、结束时间来查询出来的对话列表。
    function getProcessData(){
        var accountId = $("#accountId").val();
        var userId = $("#userId").val();
        var fromDateTime = $("#fromDateTime").val();
        var toDateTime = $("#toDateTime").val();
//        debugger
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
//                debugger;
                $("#conversationData").empty();
                for(i = 0, len = jsonData.length; i < len; i ++){
                    var contextId = jsonData[i].contextId;
                    var id = jsonData[i].id;
                    var sentence = jsonData[i].sentence;
                    var sentenceModified = (jsonData[i].sentenceModified) ? jsonData[i].sentenceModified : '---';
                    var responseMsg = (jsonData[i].responseMsg) ? jsonData[i].responseMsg : '---';

                    var responseType = jsonData[i].responseType;                                                                            //根据responseType决定responseMsg颜色
                    var responseMsg = "                                    " + responseMsg + "&nbsp;</span>\n";
                    if(responseType == 4) {
                        responseMsg = "                                    <span style=\"color:red;\">" + responseMsg + "&nbsp;</span>\n";
                    }
                    $("#conversationData").append("<tr><td width='10%'>" + contextId + "</td>" +
                        "                                <td width='45%'>\n" +
                        "                                   <div onclick=\"getOneLogDataProcess(" + id + ")\">\n" +
                        "                                       <span>\n" +
                        "                                           " + sentence + "&nbsp;\n" +
                        "                                       </span>\n" +
                        "                                   </div>\n" +
                        "                               </td>\n" +
                        "                               <td>\n" +
                        "                                <div onclick=\"getOneLogDataProcess(" + id + ")\">\n" +
                        "                                    <span class=\"modifiedSentence\" hidden><i class=\"icon-circle-arrow-right\">&nbsp;" + sentenceModified + "</i></span><br/>\n" + responseMsg +
                        "                                </div>\n" +
                        "                            </td></tr>");
                }
            }
        });
    }

    //点击每一段“对话”，会获取这段对话的LogDataProcess数据，并将其数据图形化展示到左边的表格里面。
    function getOneLogDataProcess(log_data_process_id) {
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
                var params = obj.accountId + "," + obj.userId + "," + obj.contextId + "," + obj.currenttimemillis;

                $("#detailContextOwner").empty();
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
                $("#detailContextOwner").append("<button class=\"btn btn-small\" onclick='goCalculationLogData(" + params + ")'><i class=\"icon-list-alt\"></i>&nbsp;&nbsp;" +  obj.detailContextOwner + "</button> &nbsp;&nbsp; <i id=\"haveCalculationData\" class=\"icon-ban-circle\">NoCalculationData</i>");

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
                        $("#processTrace").append("<div class=\"badge badge-important\" style=\"position: relative;\">" + val + "</div> ");
                    }else{
                        $("#processTrace").append("<div class=\"badge badge-success\" style=\"position: relative;\">" + val + "</div> ");
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
            currentTimeMillis : currentTimeMillis,
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
                if(data == "1"){
                    var url = "../log/getOneLogDataCalculation.do?" + getParams;
                    window.open(url);
                }else{
                    $("#haveCalculationData").show();
                }
            }
        });
    }
</script>

<div id="content" class="span11" style="margin-left:6%;" >

    <div class="span12" style="margin-top: 2px; height: 40px; ">
        &nbsp;&nbsp;&nbsp;&nbsp;
        账号：<input type="text" id="accountId" name="accountId" value="1" />&nbsp;&nbsp;
        用户号：<input type="text" id="userId" name="userId"  value="2"  />&nbsp;&nbsp;
        起始时间：<input type="text" id="fromDateTime" name="fromDateTime"  value="2018-02-27 19:17:00" />&nbsp;&nbsp;
        结束时间：<input type="text" id="toDateTime" name="toDateTime"  value="2018-03-09 19:18:00" />&nbsp;&nbsp;
        <div onclick="getProcessData()" class="btn btn-danger" data-rel="popover" title="">查询</div>
    </div>

    <div class="span7 noMarginLeft" style="margin-top: 2px;">
        <div class="priority high"><span>详细过程数据</span></div>
        <table class="table table-bordered">
            <tbody>
            <tr>
                <td style="width:60px;">会话编号</td>
                <td class="center">
                    <span id="detailContextOwner"></span>
                </td>
                <td style="width:80px;">上下文状态</td>
                <td class="center">
                    <span id="state"></span>
                </td>
                <td style="width:80px;">时间</td>
                <td class="center">
                    <span id="dateTime"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">原句</td>
                <td class="center" colspan="5">
                    <span id="sentence"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">改写句</td>
                <td class="center" colspan="5">
                    <span id="sentenceModified"></span>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">回答</td>
                <td class="center" colspan="5">
                    <span id="responseMsg"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">分词和权重</td>
                <td class="center" colspan="5">
                    <span id="segmentWeights"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">结果</td>
                <td class="center" colspan="5">
                    <span id="selectResult"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">会话轨迹</td>
                <td class="center" colspan="5">
                    <span id="processTrace"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">LPM实体</td>
                <td id="lpm" class="center" colspan="5">
                    <span id="lpmRedundantWi"></span><br/>
                    <span id="lpmMatchedRew"></span><br/>
                    <span id="lpmMatchedRre"></span><br/>
                    <span id="lpmLackedRre"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">CPM实体</td>
                <td id="cpm" class="center" colspan="5">
                    <span id="cpmRedundantWi"></span><br/>
                    <span id="cpmMatchedRew"></span><br/>
                    <span id="cpmMatchedRre"></span><br/>
                    <span id="cpmLackedRre"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">FPM实体</td>
                <td id="fpm" class="center" colspan="5">
                    <span id="fpmRedundantWi"></span><br/>
                    <span id="fpmMatchedRew"></span><br/>
                    <span id="fpmMatchedRre"></span><br/>
                    <span id="fpmLackedRre"></span>
                </td>
            </tr>
            <tr>
                <td style="width:60px;">配置参数</td>
                <td class="center" colspan="5">
                    <span id="configureParams"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="span5 noMarginLeft">
        <div class="cus_dark">
            <div>
                <button class="btn btn-mini btn-danger" style="margin:5px;" onclick="slideModifiedSentence()">处理文本展示/隐藏 </button>
                <div style="height:560px; overflow-x:hidden; overflow-y:auto; margin:5px;">
                    <table id="conversationData" style="border:1px;">

                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


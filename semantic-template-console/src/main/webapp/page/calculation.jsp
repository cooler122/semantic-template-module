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
<div id="content" class="span11" style="margin-left:6%;" onblur="">
    <div class="priority high"><span>基本数据</span></div>
    <table class="table table-bordered">
        <tbody>
            <tr>
                <td style="width:25%;">账号：${accountId}</td>
                <td style="width:25%;">用户号：${userId}</td>
                <td style="width:25%;">上下文编号：${contextId}</td>
                <td style="width:25%;">时间：${dateTime}</td>
            </tr>
            <tr>
                <td class="center" colspan="1">
                    轨迹
                </td>
                <td class="center" colspan="3">
                    <span id="processTrace"> ${processTrace}</span>
                </td>
            </tr>
        </tbody>
    </table>

    <div class="priority high"><span>LPM数据</span></div>
    <div>${calculationLogParam_cpm.sentenceVectors}</div>

    <table class="table table-bordered">

    </table>

    <div class="priority high"><span>CPM数据</span></div>
    <table class="table table-bordered">
    </table>

    <div class="priority high"><span>FPM数据</span></div>
    <table class="table table-bordered">

    </table>
</div>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    table { border:1px; font-size: 10px; }
    .table th, .table td { padding : 1px; }
    .box{border : 0px;}
    .box-content {padding : 0px;}
    .tab-menu.nav-tabs > li { float : left;}

    .cus_dark { padding: 20px 25px 25px 0; top: -26px;margin-bottom: -56px; margin-right: -50px; position: relative; }
    .p { margin : 0px;}
    .timeslot { width : 96%; margin: 5px 5px; }
    .timeslot .task {width : 40%;}
</style>
<script type="text/javascript">
    function closeTab(id){
        $("#calculation_head_" + id).remove();
        $("#calculation_content_" + id).remove();
        $("#welcomeHead").addClass("active");
        $("#welcomeContent").addClass("tab-pane active");
    }


</script>

<div id="content" class="span11" style="margin-left:6%; border: 0px;" >

    <input id="accountCount" type="hidden" value="0"/>
    <input id="processCount" type="hidden" value="0"/>
    <input id="calculationCount" type="hidden" value="0"/>

    <div class="row-fluid" >
        <div class="box span12">
            <div class="box-content">
                <ul class="nav tab-menu nav-tabs" id="myTab" style="margin:0; background: #578EBE">
                    <li class="active" id="welcomeHead">
                        <a href="#welcomeContent">欢迎</a>
                    </li>
                    <li class="" id="processHead1">
                        <a href="#processContent1">过程日志<i class="icon-remove-sign" onclick="closeTab('process1')"></i></a>
                    </li>
                    <li class="" id="calculationHead_1">
                        <a href="#calculationContent_1">计算日志_1<i class="icon-remove-sign" onclick="closeTab('1')"></i></a>
                    </li>
                </ul>

                <div id="myTabContent" class="tab-content" style="overflow: hidden;">
                    <div class="tab-pane active" id="welcomeContent">
                        <%@ include file="../page/welcome.jsp" %>
                    </div>
                    <div class="tab-pane" id="processContent1">
                        <%@ include file="../page/process.jsp" %>
                    </div>
                    <div class="tab-pane" id="calculationContent_1">
                        <%@ include file="../page/calculation.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

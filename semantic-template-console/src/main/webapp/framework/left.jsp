<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    li{
        color : #fff;
    }
    input[type="text"] {
        width : 150px;
        height : 15px;
    }
</style>
<script>
    function showQueryTable(id){
        $("#" + id).slideToggle();
    }

    function buildAccountPage(){

    }

    function buildProcessPage(){
        var url = "../log/buildProcessPage.do";
        $.ajax({
            type: "get",
            url: url,
            cache: false,
            ifModified: true,
            contentType:"text/html;charset=utf-8",
            success: function (data) {
                var processCount = $("#processCount").val();
                var processNum = processCount + 1;
                $("#myTab").append("<li class=\"\" id=\"processHead"+ processNum + "\">\n" +
                    "                        <a href=\"#processContent"+ processNum + ">过程日志<i class=\"icon-remove-sign\" onclick=\"closeTab('process"+ processNum + "')\"></i></a>\n" +
                    "                    </li>");

                $("#myTabContent").append();
                $("#welcomeContent").empty();
                $("#welcomeContent").append(data);

            }
        });
    }

    function addNewContentPage(typeId){

    }
</script>
<div id="sidebar-left" class="span1">
    <div class="nav-collapse sidebar-nav">
        <i class="icon-circle-arrow-left"></i>
        <ul class="nav nav-tabs nav-stacked main-menu">

            <li><a href="../log/goStmConsole.do"><i class="icon-edit"></i><span class="hidden-tablet">欢迎</span></a></li>

            <li><a href="../log/goAccount.do"><i class="icon-edit"></i><span class="hidden-tablet">账户</span></a></li>

            <li><a href="../log/goProcessPage.do"><i class="icon-edit"></i><span class="hidden-tablet">对话</span></a></li>
        </ul>
    </div>
</div>


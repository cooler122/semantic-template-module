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
    .jqsfield {
        color: white;
        font: 10px arial, san serif;
        text-align: left;
    }</style>
</head>

<script src="../static/assets/js/ace-extra.min.js"></script>

<body style="">

<div class="main-container" id="main-container">

    <div class="main-container-inner">
        <a class="menu-toggler" id="menu-toggler" href="#">
            <span class="menu-text"></span>
        </a>

        <%@ include file="/sidebar.jsp" %>

        <div class="main-content" style="margin-left: 100px;">
            <div class="breadcrumbs" id="breadcrumbs">
                <script type="text/javascript">
                    try {
                        ace.settings.check('breadcrumbs', 'fixed')
                    } catch (e) {
                    }
                </script>

                <ul class="breadcrumb">
                    <li>
                        <i class="icon-home home-icon"></i>
                        <a href="#">首页</a>
                    </li>
                    <li class="active">控制台</li>
                </ul><!-- .breadcrumb -->

                <div class="nav-search" id="nav-search">
                    <input type="text" placeholder="输入账号" class="nav-search-input" id="accountId" autocomplete="off" style="width:100px;" value="1">
                    <input type="text" placeholder="输入用户号" class="nav-search-input" id="userId" autocomplete="off" style="width:100px;" value="2">
                    <input type="text" placeholder="输入起始时间" class="nav-search-input" id="fromDateTime" autocomplete="off" style="width:200px;" value="2018-02-01 00:00:00">
                    <input type="text" placeholder="输入结束时间" class="nav-search-input" id="toDateTime" autocomplete="off" style="width:200px;" value="2018-03-31 00:00:00">
                    <button class="btn btn-xs btn-danger"><i class="icon-bolt bigger-110"></i> 查询 </button>
                </div><!-- #nav-search -->
            </div>

            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="row">
                            欢迎页面！
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
<%--<script src="../static/assets/js/cus/stm-process.js"></script>--%>

</body>
</html>
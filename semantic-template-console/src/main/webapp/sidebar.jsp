<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="sidebar" class="sidebar" style="width: 100px; height:100%;">

    <div class="sidebar-shortcuts" id="sidebar-shortcuts">
        <div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
            <button class="btn btn-success">
                <i class="icon-signal"></i>
            </button>

            <button class="btn btn-info">
                <i class="icon-pencil"></i>
            </button>

            <button class="btn btn-warning">
                <i class="icon-group"></i>
            </button>

            <button class="btn btn-danger">
                <i class="icon-cogs"></i>
            </button>
        </div>

        <div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
            <span class="btn btn-success"></span>

            <span class="btn btn-info"></span>

            <span class="btn btn-warning"></span>

            <span class="btn btn-danger"></span>
        </div>
    </div>

    <ul class="nav nav-list">
        <li class="active">
            <a href="../log/goWelcomePage.do">
                <i class="icon-dashboard"></i>
                <span class="menu-text"> 欢迎 </span>
            </a>
        </li>
        <li>
            <a href="../log/goAccountPage.do">
                <i class="icon-text-width"></i>
                <span class="menu-text"> 账户 </span>
            </a>
        </li>
        <li>
            <a href="../log/goProcessPage.do">
                <i class="icon-list-alt"></i>
                <span class="menu-text"> 轨迹 </span>
            </a>
        </li>
        <li>
            <a href="../log/goCalculationPage.do">
                <i class="icon-picture"></i>
                <span class="menu-text"> 计算 </span>
            </a>
        </li>
        <li>
            <a href="../log/goAnalysisPage.do">
                <i class="icon-picture"></i>
                <span class="menu-text"> 统计 </span>
            </a>
        </li>
    </ul>

    <div class="sidebar-collapse" id="sidebar-collapse">
        <i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
    </div>
</div>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>计算日志</title>
    <meta name="keywords" content="计算日志">
    <link rel="stylesheet" href="../static/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="../static/assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400,300">
    <link rel="stylesheet" href="../static/assets/css/ace.min.css">
    <link rel="stylesheet" href="../static/assets/css/ace-rtl.min.css">
    <link rel="stylesheet" href="../static/assets/css/ace-skins.min.css">
    <script src="../static/assets/js/ace-extra.min.js"></script>
    <style>
        .main-content {
            margin-left: 0;
            margin-right: 0;
            margin-top: 0;
            min-height: 100%;
            padding: 0;
        }
        .table thead>tr>th, .table tbody>tr>th, .table tfoot>tr>th, .table thead>tr>td, .table tbody>tr>td, .table tfoot>tr>td {
            padding: 1px;
            text-align: center;
            vertical-align: top;
            border-top: 1px solid #ddd;
        }
        .dataTables_wrapper .row:last-child {
            border-top: 1px solid #DDD;
            padding: 5px;
            background-color: #eff3f8;
            border-bottom: 1px solid #DDD;
        }
        ul, ol {
            margin: 0;
            padding: 0;
        }
        .spaced2>li {
            margin-top: 0px;
            margin-bottom: 0px;
        }
        .pagination {
            display: inline-block;
            padding-left: 0;
            margin: 0;
            border-radius: 4px;
        }
        .label {
            font-size: 12px;
            line-height: 1.15;
            height: 20px;
            margin: 1px;
        }
    </style>

    <script type="text/javascript">
        function scrollNow(id){
            alert("aaa");
            $("#ruleTable").animate({
                    scrollTop:$("#"+id).offsetTop-20
                },1000
            );
        }
    </script>
</head>

<body style="" class="">
<div class="main-container" id="main-container">

    <div class="main-container-inner">

        <div class="main-content">

            <div class="page-content">
                <div class="row">
                    <h3 class="header smaller lighter blue">LPM匹配数据</h3>
                    <div class="col-xs-6">
                        <div class="table-header" style="line-height: 20px;">
                            句子向量数据
                        </div>
                        <div class="table-responsive">
                            <div class="dataTables_wrapper" role="grid">
                                <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody role="alert" aria-live="polite" aria-relevant="all" >
                                            <tr class="odd">
                                                <td class="center  sorting_1  " width="5%" height="1px;"></td>
                                                <td width="19%"></td>
                                                <td width="8%"></td>
                                                <td width="8%"></td>
                                                <td width="20%"></td>
                                                <td width="20%"></td>
                                                <td width="20%"></td>
                                            </tr>

                                            <c:forEach var="sentenceVector" items="${sentenceVectors_fpm}">
                                                <tr class="odd">
                                                    <td id="sv_"${sentenceVector.id}></td>
                                                    <td>${contextId}</td>
                                                    <td colspan="5">
                                                        <c:forEach items="${sentenceVector.words}" var="word">
                                                            <span class="label label-warning">${word}</span>
                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                                <c:forEach begin="0" end="${sentenceVector.words.size() - 1}" varStatus="status">
                                                    <tr class="even">
                                                        <td class="center  sorting_1  "></td>
                                                        <td>${sentenceVector.words[status.index]}</td>
                                                        <td>${sentenceVector.natures[status.index]}</td>
                                                        <td>${sentenceVector.weights[status.index]}</td>
                                                        <td colspan="3">
                                                            <c:forEach items="${sentenceVector.rEntityWordInfosList[status.index]}" var="rewi">
                                                                <span class="badge badge-important">${rewi.entityType}_${rewi.entityId}_${rewi.entityName}</span>
                                                            </c:forEach>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_paginate paging_bootstrap">
                                            <ul class="pagination">
                                                <c:forEach var="sentenceVector" items="${sentenceVectors_fpm}">
                                                    <li><a href="#${sentenceVector.id}">${sentenceVector.id}</a></li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <div class="table-header" style="line-height: 20px;">
                            规则实体统计数据
                        </div>
                        <div class="table-responsive">
                            <div class="dataTables_wrapper" role="grid">
                                <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody id="ruleTable" role="alert" aria-live="polite" aria-relevant="all" >

                                            <tr class="odd">
                                                <td class="center  sorting_1" width="5%"></td>
                                                <td width="15%"></td>
                                                <td width="15%"></td>
                                                <td width="10%"></td>
                                                <td width="15%"></td>
                                                <td width="25%"></td>
                                                <td width="15%"></td>
                                            </tr>

                                            <c:forEach var="hitRRuleEntityMap" items="${hitRRuleEntityMaps}" varStatus="status">
                                                <c:forEach var="hitRRuleEntityMapEntity" items="${hitRRuleEntityMap}">
                                                    <tr class="odd">
                                                        <td></td>
                                                        <td>svId: ${status.index}</td>
                                                        <td>ruleId: ${hitRRuleEntityMapEntity.key}</td>
                                                        <td colspan="4">
                                                            <c:forEach var="rre" items="${hitRRuleEntityMapEntity.value}">
                                                                <span class="badge badge-important">${rre.entityType}_${rre.entityId}_${rre.entityName}_${rre.weight}</span>
                                                            </c:forEach>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_paginate paging_bootstrap">
                                            <ul class="pagination">
                                                <li><a href="#1">1</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <div class="table-header" style="line-height: 20px;">
                            Top5规则
                        </div>
                        <div class="table-responsive">
                            <div class="dataTables_wrapper" role="grid">
                                <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody role="alert" aria-live="polite" aria-relevant="all" >

                                        <c:forEach var="svRuleInfo" items="${svRuleInfosTopFive}" varStatus="status">
                                            <tr class="odd">
                                                <td class="center  sorting_1" width="5%"></td>
                                                <td width="15%"></td>
                                                <td width="15%"></td>
                                                <td width="10%"></td>
                                                <td width="15%"></td>
                                                <td width="25%"></td>
                                                <td width="15%"></td>
                                            </tr>
                                            <tr class="odd">
                                                <td></td>
                                                <td>
                                                    ${svRuleInfo.sentenceVectorId}
                                                </td>
                                                <td>${svRuleInfo.ruleId}</td>
                                                <td>
                                                    <c:if test="${svRuleInfo.isLongConversationRule == false}">短对话</c:if>
                                                    <c:if test="${svRuleInfo.isLongConversationRule == true}">长对话</c:if>
                                                </td>
                                                <td>${svRuleInfo.matchType}</td>
                                                <td>${svRuleInfo.runningAccuracyThreshold}</td>
                                                <td>${svRuleInfo.algorithmType}</td>
                                            </tr>
                                            <tr class="odd">
                                                <td class="center  sorting_1"></td>
                                                <td colspan="3">${svRuleInfo.sentence}</td>
                                                <td colspan="3">${svRuleInfo.sentenceModified}</td>
                                            </tr>
                                            <tr class="odd">
                                                <td class="center  sorting_1"></td>
                                                <td colspan="3">${svRuleInfo.preRuleVolumeRateOccupancy}</td>
                                                <td colspan="3">${svRuleInfo.preRuleWeightOccupancy}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_paginate paging_bootstrap">
                                            <ul class="pagination">
                                                <li><a href="#1">1</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <div class="table-header" style="line-height: 20px;">
                            Top5规则具体实体数据
                        </div>
                        <div class="table-responsive">
                            <div class="dataTables_wrapper" role="grid">
                                <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody role="alert" aria-live="polite" aria-relevant="all" >

                                        <c:forEach var="ruleId_rRuleEntityDataEntity" items="${ruleId_rRuleEntityDataMap}" varStatus="status">
                                            <tr class="odd">
                                                <td class="center  sorting_1" width="5%"></td>
                                                <td width="15%"></td>
                                                <td width="15%"></td>
                                                <td width="10%"></td>
                                                <td width="15%"></td>
                                                <td width="25%"></td>
                                                <td width="15%"></td>
                                            </tr>
                                            <tr class="odd">
                                                <td></td>
                                                <td>ruleId：${ruleId_rRuleEntityDataEntity.key} </td>
                                                <td colspan="5">
                                                    <c:forEach var="rRuleEntity" items="${ruleId_rRuleEntityDataEntity.value.values()}" varStatus="status">
                                                        <span class="badge badge-important">${rRuleEntity.entityType}_${rRuleEntity.entityId}_${rRuleEntity.entityName}_${rRuleEntity.weight}</span><br/>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_paginate paging_bootstrap">
                                            <ul class="pagination">
                                                <li><a href="#1">1</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <div class="table-header" style="line-height: 20px;">
                            Top5规则计算数据
                        </div>
                        <div class="table-responsive">
                            <div class="dataTables_wrapper" role="grid">
                                <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody role="alert" aria-live="polite" aria-relevant="all" >

                                        <tr class="odd">
                                            <td class="center  sorting_1" width="5%"></td>
                                            <td width="15%"></td>
                                            <td width="15%"></td>
                                            <td width="10%"></td>
                                            <td width="15%"></td>
                                            <td width="25%"></td>
                                            <td width="15%"></td>
                                        </tr>

                                        <c:forEach var="ids_rewisMapKey" items="${similarityCalculationData_fpm.ids_rewisMap.keySet()}" varStatus="status">
                                            <tr class="odd">
                                                <td></td>
                                                <td>${ids_rewisMapKey} </td>
                                                <td colspan="3">
                                                    <c:forEach var="rewi" items="${similarityCalculationData_fpm.ids_rewisMap.get(ids_rewisMapKey)}">
                                                        <span class="label label-warning">${rewi.wordId}_${rewi.word}}</span>
                                                        ->
                                                        <span class="badge badge-important">${rewi.entityType}_${rewi.entityId}_${rewi.entityName}</span><br/>
                                                    </c:forEach>
                                                </td>
                                                <td colspan="2">
                                                        ${similarityCalculationData_fpm.ids_scoreMap.get(ids_rewisMapKey)}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_paginate paging_bootstrap">
                                            <ul class="pagination">
                                                <li><a href="#1">1</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <div class="table-header" style="line-height: 20px;">
                            最优规则
                        </div>
                        <div class="table-responsive">
                            <div class="dataTables_wrapper" role="grid">
                                <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody role="alert" aria-live="polite" aria-relevant="all" >

                                            <tr class="odd">
                                                <td class="center  sorting_1" width="5%"></td>
                                                <td width="15%"></td>
                                                <td width="15%"></td>
                                                <td width="10%"></td>
                                                <td width="15%"></td>
                                                <td width="25%"></td>
                                                <td width="15%"></td>
                                            </tr>
                                            <tr class="odd">
                                                <td></td>
                                                <td>${optimalSvRuleInfo_fpm.sentenceVectorId}</td>
                                                <td>${optimalSvRuleInfo_fpm.ruleId}</td>
                                                <td>
                                                    <c:if test="${optimalSvRuleInfo_fpm.isLongConversationRule == false}">短对话</c:if>
                                                    <c:if test="${optimalSvRuleInfo_fpm.isLongConversationRule == true}">长对话</c:if>
                                                </td>
                                                <td>${optimalSvRuleInfo_fpm.matchType}</td>
                                                <td>${optimalSvRuleInfo_fpm.runningAccuracyThreshold}</td>
                                                <td>${optimalSvRuleInfo_fpm.algorithmType}</td>
                                            </tr>
                                            <tr class="odd">
                                                <td class="center  sorting_1"></td>
                                                <td colspan="3">${optimalSvRuleInfo_fpm.sentence}</td>
                                                <td colspan="3">${optimalSvRuleInfo_fpm.sentenceModified}</td>
                                            </tr>
                                            <tr class="odd">
                                                <td class="center  sorting_1  "></td>
                                                <td>多余词语</td>
                                                <td colspan="5">
                                                    <c:forEach items="${optimalSvRuleInfo_fpm.redundantWordInfos}" var="redundantWordInfo">
                                                        <span class="label" style="position: relative;">${redundantWordInfo.wordId}_${redundantWordInfo.word}_${redundantWordInfo.weight}</span>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                            <tr class="odd">
                                                <td class="center  sorting_1  "></td>
                                                <td>匹配的REWIs</td>
                                                <td colspan="5">
                                                    <c:forEach items="${optimalSvRuleInfo_fpm.matchedREntityWordInfos}" var="matchedREntityWordInfo">
                                                        <span class="label label-warning" style="position: relative;">${matchedREntityWordInfo.wordId}_${matchedREntityWordInfo.word} -> ${matchedREntityWordInfo.entityId}_${matchedREntityWordInfo.entityName}</span>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                            <tr class="odd">
                                                <td class="center  sorting_1  "></td>
                                                <td>匹配的RREs</td>
                                                <td colspan="5">
                                                    <c:forEach items="${optimalSvRuleInfo_fpm.matchedRRuleEntities}" var="matchedRRuleEntity">
                                                        <span class="badge badge-important" style="position: relative;">${matchedRRuleEntity.entityType}_${matchedRRuleEntity.entityId}_${matchedRRuleEntity.entityName}</span>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                            <tr class="odd">
                                                <td class="center  sorting_1  "></td>
                                                <td>缺失的RREs</td>
                                                <td colspan="5">
                                                    <c:forEach items="${optimalSvRuleInfo_fpm.lackedRRuleEntities}" var="lackedRRuleEntity">
                                                        <span class="badge" style="position: relative;">${lackedRRuleEntity.entityType}_${lackedRRuleEntity.entityId}_${lackedRRuleEntity.entityName}</span>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_paginate paging_bootstrap">
                                            <ul class="pagination">
                                                <li><a href="javascript:void(0)" onclick="scrollNow('contextId${historyContextId}')">1</a></li>
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
    </div>

</div>

<script src="../static/assets/js/jquery-2.0.3.min.js"></script>
<script src="../static/assets/js/ace-elements.min.js"></script>
<script src="../static/assets/js/ace.min.js"></script>

</body>
</html>
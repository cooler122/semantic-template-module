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

        function showContent(tab){
            if(tab=='LPM'){
                $("#LPM").css("color", "red");
                $("#CPM").css("color", "blue");
                $("#FPM").css("color", "blue");
                $("#ContentLPM").css("display", "block");
                $("#ContentCPM").css("display", "none");
                $("#ContentFPM").css("display", "none");

            }else if(tab == 'CPM'){
                $("#LPM").css("color", "blue");
                $("#CPM").css("color", "red");
                $("#FPM").css("color", "blue");
                $("#ContentLPM").css("display", "none");
                $("#ContentCPM").css("display", "block");
                $("#ContentFPM").css("display", "none");
            }else{
                $("#LPM").css("color", "blue");
                $("#CPM").css("color", "blue");
                $("#FPM").css("color", "red");
                $("#ContentLPM").css("display", "none");
                $("#ContentCPM").css("display", "none");
                $("#ContentFPM").css("display", "block");
            }


        }
    </script>
</head>

<body style="" class="">
<div class="main-container" id="main-container">

    <div class="main-container-inner">
        <a class="menu-toggler" id="menu-toggler" href="#">
            <span class="menu-text"></span>
        </a>

        <div class="main-content">

            <div class="page-content">
                <div class="row">
                    <div id="fpmTab" class="h3 header smaller lighter blue">
                        <span id="LPM" onclick="showContent('LPM')" style="color: red">LPM匹配数据</span>
                        <span id="CPM" onclick="showContent('CPM')">CPM匹配数据</span>
                        <span id="FPM" onclick="showContent('FPM')">FPM匹配数据</span>
                    </div>

                    <%--LPM数据 start--%>
                    <div id="ContentLPM" style="display:block">
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

                                                <c:forEach var="sentenceVector" items="${sentenceVectors_lpm}">
                                                        <tr class="odd">
                                                            <td id="lpm_sv_"${sentenceVector.id}></td>
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
                                                    <c:forEach var="sentenceVector" items="${sentenceVectors_lpm}">
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
                                规则向量数据
                            </div>
                            <div class="table-responsive">
                                <div class="dataTables_wrapper" role="grid">
                                    <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                        <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                            <tbody id="ruleTable" role="alert" aria-live="polite" aria-relevant="all" >

                                                <c:forEach var="historySVRuleInfoEntity" items="${historySVRuleInfoMap_lpm}">
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
                                                            <c:if test="${historySVRuleInfoEntity.value.lackedRRuleEntities.size() > 0}">${historySVRuleInfoEntity.key}</c:if>
                                                            <c:if test="${historySVRuleInfoEntity.value.lackedRRuleEntities.size() <= 0}"><i class="icon-warning-sign bigger-120"></i> <s>${historySVRuleInfoEntity.key}</s></c:if>
                                                        </td>
                                                        <td>${historySVRuleInfoEntity.value.ruleId}</td>
                                                        <td>
                                                            <c:if test="${historySVRuleInfoEntity.value.isLongConversationRule == false}">短对话</c:if>
                                                            <c:if test="${historySVRuleInfoEntity.value.isLongConversationRule == true}">长对话</c:if>
                                                        </td>
                                                        <td>${historySVRuleInfoEntity.value.matchType}</td>
                                                        <td>${historySVRuleInfoEntity.value.runningAccuracyThreshold}</td>
                                                        <td>${historySVRuleInfoEntity.value.algorithmType}</td>
                                                    </tr>
                                                    <tr class="odd">
                                                        <td class="center  sorting_1"></td>
                                                        <td colspan="3">${historySVRuleInfoEntity.value.sentence}</td>
                                                        <td colspan="3">${historySVRuleInfoEntity.value.sentenceModified}</td>
                                                    </tr>
                                                    <tr class="odd">
                                                        <td class="center  sorting_1  "></td>
                                                        <td>多余词语</td>
                                                        <td colspan="5">
                                                            <c:forEach items="${historySVRuleInfoEntity.value.redundantWordInfos}" var="redundantWordInfo">
                                                                <span class="label" style="position: relative;">${redundantWordInfo.wordId}_${redundantWordInfo.word}_${redundantWordInfo.weight}</span>
                                                            </c:forEach>
                                                        </td>
                                                    </tr>
                                                    <tr class="odd">
                                                        <td class="center  sorting_1  "></td>
                                                        <td>匹配的REWIs</td>
                                                        <td colspan="5">
                                                            <c:forEach items="${historySVRuleInfoEntity.value.matchedREntityWordInfos}" var="matchedREntityWordInfo">
                                                                <span class="label label-warning" style="position: relative;">${matchedREntityWordInfo.wordId}_${matchedREntityWordInfo.word} -> ${matchedREntityWordInfo.entityId}_${matchedREntityWordInfo.entityName}</span>
                                                            </c:forEach>
                                                        </td>
                                                    </tr>
                                                    <tr class="odd">
                                                        <td class="center  sorting_1  "></td>
                                                        <td>匹配的RREs</td>
                                                        <td colspan="5">
                                                            <c:forEach items="${historySVRuleInfoEntity.value.matchedRRuleEntities}" var="matchedRRuleEntity">
                                                                <span class="badge badge-important" style="position: relative;">${matchedRRuleEntity.entityType}_${matchedRRuleEntity.entityId}_${matchedRRuleEntity.entityName}</span>
                                                            </c:forEach>
                                                        </td>
                                                    </tr>
                                                    <tr class="odd">
                                                        <td class="center  sorting_1  "></td>
                                                        <td>缺失的RREs</td>
                                                        <td colspan="5">
                                                            <c:if test="${historySVRuleInfoEntity.value.lackedRRuleEntities.size() > 0}">
                                                                <c:forEach items="${historySVRuleInfoEntity.value.lackedRRuleEntities}" var="lackedRRuleEntity">
                                                                    <span class="badge" style="position: relative;">${lackedRRuleEntity.entityType}_${lackedRRuleEntity.entityId}_${lackedRRuleEntity.entityName}</span>
                                                                </c:forEach>
                                                            </c:if>
                                                            <c:if test="${historySVRuleInfoEntity.value.lackedRRuleEntities.size() <= 0}">
                                                                <span class="label label-warning">
                                                                    <i class="icon-warning-sign bigger-120"></i>
                                                                    没有缺失参数，无需补充参数
                                                                </span>
                                                            </c:if>

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
                                                    <c:forEach var="historyContextId" items="${historySVRuleInfoMap_lpm.keySet()}">
                                                        <li><a href="javascript:void(0)" onclick="scrollNow('contextId${historyContextId}')">${historyContextId}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <br/>
                            <div class="alert alert-warning" style="padding: 5px;">
                                <button type="button" class="close" data-dismiss="alert">
                                    <i class="icon-remove"></i>
                                </button>
                                取消匹配的上下文列表：
                                <c:forEach var="canceledLPMContextId" items="${canceledLPMContextIdSet}">
                                     ${canceledLPMContextId}
                                </c:forEach>
                                <br/>
                                实体数量阈值: <span class="label label-success arrowed-in arrowed-in-right">1</span>
                                句子端权重阈值: <span class="label label-success arrowed-in arrowed-in-right">0.2</span>
                                规则端权重阈值： <span class="label label-success arrowed-in arrowed-in-right">0.1</span>
                            </div>

                            <div class="well" style="padding: 5px; text-align: center;">
                                <ul class="pagination">
                                    <c:forEach var="sentenceVector" items="${sentenceVectors_lpm}">
                                        <c:forEach var="historySVRuleInfoEntity" items="${historySVRuleInfoMap_lpm}">
                                            <li><a href="#">${sentenceVector.id} - ${historySVRuleInfoEntity.key}</a></li>
                                        </c:forEach>
                                    </c:forEach>
                                </ul>
                            </div>

                            <div class="table-header" style="line-height: 20px;">
                                初选以及合并过程数据
                            </div>
                            <div class="table-responsive">
                                <div id="sample-table-2_wrapper" class="dataTables_wrapper" role="grid">
                                    <table id="sample-table-2" class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <thead>
                                        <tr role="row">
                                            <th colspan="1" width="4%;">距离</th>
                                            <th colspan="1" width="8%;">会话ID-分词ID</th>
                                            <th colspan="1" width="8%;">历史会话ID</th>
                                            <th colspan="1" width="32%;">合并的实体集</th>
                                            <th colspan="1" width="8%;">句子端权重占比</th>
                                            <th colspan="1" width="8%;">规则端权重占比</th>
                                            <th colspan="1" width="8%;">失忆算法类型</th>
                                            <th colspan="1" width="8%;">失忆底数</th>
                                            <th colspan="1" width="8%;">失忆系数</th>
                                            <th colspan="1" width="8%;">是否跨域门槛</th>
                                        </tr>
                                        </thead>

                                        <tbody role="alert" aria-live="polite" aria-relevant="all">

                                            <c:forEach begin="0" end="${coupleAlterationRateDatas.size()}" varStatus="status">
                                                <c:if test="${coupleAlterationRateDatas[status.index].contextId != null}">
                                                <tr class="odd">
                                                    <td class="center  sorting_1  ">
                                                        <label>
                                                            <input type="checkbox" class="ace">
                                                            <span class="lbl"></span>
                                                        </label>
                                                    </td>
                                                    <td>${contextId} - ${coupleAlterationRateDatas[status.index].sentenceVectorId}</td>
                                                    <td>${coupleAlterationRateDatas[status.index].contextId}</td>
                                                    <td>
                                                        <c:forEach var="combinedREntityWordInfo" items="${coupleAlterationRateDatas[status.index].combinedREntityWordInfos}">
                                                            <span class="label label-warning">${combinedREntityWordInfo.wordId}_${combinedREntityWordInfo.word}</span>
                                                            →
                                                            <span class="badge" style="position: relative;">${combinedREntityWordInfo.entityType}_${combinedREntityWordInfo.entityId}_${combinedREntityWordInfo.entityName}</span>
                                                        </c:forEach>
                                                    </td>
                                                    <td>${coupleAlterationRateDatas[status.index].svWeightOccupyRate}</td>
                                                    <td>${coupleAlterationRateDatas[status.index].ruleWeightOccupyRate}</td>
                                                    <td>${amnesiacDatas[status.index].amnesiacAlgorithmType}</td>
                                                    <td>${amnesiacDatas[status.index].baseNumber}</td>
                                                    <td>${amnesiacDatas[status.index].coefficient}</td>
                                                    <td>${coupleAlterationRateDatas[status.index].isCrossed}</td>
                                                </tr>
                                                </c:if>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <br/>
                        </div>
                        <div class="col-xs-12">
                            <div class="table-header" style="line-height: 20px;">
                                计算数据
                            </div>
                            <div class="table-responsive" style="overflow-x:hidden; max-height:200px; heigh:200px;">

                                <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                    <tbody role="alert" aria-live="polite" aria-relevant="all">
                                    <tr role="row">
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                        <td colspan="1" width="10%;"></td>
                                    </tr>

                                    <c:forEach var="similarityCalculationDataLPM" items="${similarityCalculationDataLPMS}">
                                        <tr class="odd">
                                            <td>基本参数</td>
                                            <td>${similarityCalculationDataLPM.contextId}</td>
                                            <td>${similarityCalculationDataLPM.sentenceVectorId}</td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr class="odd">
                                            <td colspan="1">句子端</td>
                                            <td colspan="4">
                                                <ul class="list-unstyled spaced2" style="text-align:left">
                                                    <li>
                                                        <c:forEach var="rewi" items="${similarityCalculationDataLPM.svInputREWIs}">
                                                            <span class="label label-warning">${rewi.wordId}_${rewi.word} </span> <span class="badge badge-danger">${rewi.entityTypeId}_${rewi.entityName}_${rewi.weights[0]}</span><br/>
                                                        </c:forEach>
                                                    </li>
                                                </ul>
                                            </td>
                                            <td colspan="1">规则端</td>
                                            <td colspan="4">
                                                <ul class="list-unstyled spaced2" style="text-align:left">
                                                    <li>
                                                        <c:forEach var="matchedRRE" items="${similarityCalculationDataLPM.ruleMatchedRREs}">
                                                            <span class="badge badge-success">${matchedRRE.entityTypeId}_${matchedRRE.entityName}_${matchedRRE.weight} </span><br/>
                                                        </c:forEach>
                                                        <c:forEach var="lackedRRE" items="${similarityCalculationDataLPM.ruleLackedRREs}">
                                                            <span class="badge">${lackedRRE.entityTypeId}_${lackedRRE.entityName}_${lackedRRE.weight} </span>
                                                            <span class="label">${lackedRRE.necessaryEntityQuery} </span>
                                                            <br/>
                                                        </c:forEach>
                                                    </li>
                                                </ul>
                                            </td>
                                        </tr>
                                        <tr class="even">
                                            <td colspan="1">交集实体数量占比</td>
                                            <td colspan="4">
                                                <c:forEach var="intersectionEntityVolumnRate" items="${similarityCalculationDataLPM.intersectionEntityVolumnRates}">
                                                    <span class="label label-sm label-primary arrowed arrowed-right">${intersectionEntityVolumnRate}</span>
                                                </c:forEach>
                                            </td>
                                            <td colspan="1">交集实体权重占比</td>
                                            <td colspan="4">
                                                <c:forEach var="intersectionEntityWeightRate" items="${similarityCalculationDataLPM.intersectionEntityWeightRates}">
                                                    <span class="label label-sm label-primary arrowed arrowed-right">${intersectionEntityWeightRate}</span>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                        <tr class="even">
                                            <td colspan="1">相似度数据</td>
                                            <td colspan="9"><span class="label label-danger label-primary arrowed arrowed-right">${similarityCalculationDataLPM.similarityValue}</span></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>
                    <%--LPM数据 end--%>

                    <%--CPM数据 start--%>
                    <div id="ContentCPM" style="display:none">
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

                                            <c:forEach var="sentenceVector" items="${sentenceVectors_cpm}">
                                                <tr class="odd">
                                                    <td id="cpm_sv_"${sentenceVector.id}></td>
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
                                                    <c:forEach var="sentenceVector" items="${sentenceVectors_cpm}">
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
                                规则向量数据
                            </div>
                            <div class="table-responsive">
                                <div class="dataTables_wrapper" role="grid">
                                    <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                        <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                            <tbody role="alert" aria-live="polite" aria-relevant="all" >

                                            <c:forEach var="historySVRuleInfoEntity" items="${historySVRuleInfoMap_cpm}">
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
                                                        <c:if test="${historySVRuleInfoEntity.value.matchedRRuleEntities.size() > 0}">${historySVRuleInfoEntity.key}</c:if>
                                                        <c:if test="${historySVRuleInfoEntity.value.matchedRRuleEntities.size() <= 0}"><i class="icon-warning-sign bigger-120"></i> <s>${historySVRuleInfoEntity.key}</s></c:if>
                                                    </td>
                                                    <td>${historySVRuleInfoEntity.value.ruleId}</td>
                                                    <td>
                                                        <c:if test="${historySVRuleInfoEntity.value.isLongConversationRule == false}">短对话</c:if>
                                                        <c:if test="${historySVRuleInfoEntity.value.isLongConversationRule == true}">长对话</c:if>
                                                    </td>
                                                    <td>${historySVRuleInfoEntity.value.matchType}</td>
                                                    <td>${historySVRuleInfoEntity.value.runningAccuracyThreshold}</td>
                                                    <td>${historySVRuleInfoEntity.value.algorithmType}</td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1"></td>
                                                    <td colspan="3">${historySVRuleInfoEntity.value.sentence}</td>
                                                    <td colspan="3">${historySVRuleInfoEntity.value.sentenceModified}</td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1  "></td>
                                                    <td>多余词语</td>
                                                    <td colspan="5">
                                                        <c:forEach items="${historySVRuleInfoEntity.value.redundantWordInfos}" var="redundantWordInfo">
                                                            <span class="label" style="position: relative;">${redundantWordInfo.wordId}_${redundantWordInfo.word}_${redundantWordInfo.weight}</span>
                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1  "></td>
                                                    <td>匹配的REWIs</td>
                                                    <td colspan="5">
                                                        <c:forEach items="${historySVRuleInfoEntity.value.matchedREntityWordInfos}" var="matchedREntityWordInfo">
                                                            <span class="label label-warning" style="position: relative;">${matchedREntityWordInfo.wordId}_${matchedREntityWordInfo.word} -> ${matchedREntityWordInfo.entityId}_${matchedREntityWordInfo.entityName}</span>
                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1  "></td>
                                                    <td>匹配的RREs</td>
                                                    <td colspan="5">
                                                        <c:forEach items="${historySVRuleInfoEntity.value.matchedRRuleEntities}" var="matchedRRuleEntity">
                                                            <span class="badge badge-important" style="position: relative;">${matchedRRuleEntity.entityType}_${matchedRRuleEntity.entityId}_${matchedRRuleEntity.entityName}</span>
                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1  "></td>
                                                    <td>缺失的RREs</td>
                                                    <td colspan="5">
                                                        <c:if test="${historySVRuleInfoEntity.value.matchedRRuleEntities.size() > 0}">
                                                            <c:forEach items="${historySVRuleInfoEntity.value.lackedRRuleEntities}" var="lackedRRuleEntity">
                                                                <span class="badge" style="position: relative;">${lackedRRuleEntity.entityType}_${lackedRRuleEntity.entityId}_${lackedRRuleEntity.entityName}</span>
                                                            </c:forEach>
                                                        </c:if>
                                                        <c:if test="${historySVRuleInfoEntity.value.matchedRRuleEntities.size() <= 0}">
                                                                <span class="label label-warning">
                                                                    <i class="icon-warning-sign bigger-120"></i>
                                                                    没有匹配上的参数，不能换参数
                                                                </span>
                                                        </c:if>
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
                                                    <c:forEach var="historyContextId" items="${historySVRuleInfoMap_cpm.keySet()}">
                                                        <li><a href="javascript:void(0)" onclick="scrollNow('contextId${historyContextId}')">${historyContextId}</a></li>
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
                                可换实体集
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

                                            <c:forEach var="hitCurrentREWIEntity" items="${hitCurrentREntityWordInfoMap}">
                                                <tr class="odd">
                                                    <td></td>
                                                    <td>${hitCurrentREWIEntity.key}</td>
                                                    <td colspan="5">
                                                        <span class="label label-warning">${hitCurrentREWIEntity.value.wordId}_${hitCurrentREWIEntity.value.word}</span>
                                                        <span class="label label-warning">${hitCurrentREWIEntity.value.entityTypeId}_${hitCurrentREWIEntity.value.entityId}_${hitCurrentREWIEntity.value.entityName}</span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="dataTables_paginate paging_bootstrap">
                                                <%--<ul class="pagination">--%>
                                                    <%--<c:forEach var="sentenceVector" items="${sentenceVectors_cpm}">--%>
                                                        <%--<li><a href="#${sentenceVector.id}">${sentenceVector.id}</a></li>--%>
                                                    <%--</c:forEach>--%>
                                                <%--</ul>--%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="table-header" style="line-height: 20px;">
                                可换实体数据统计
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
                                            <c:forEach var="svIdcontextId_productValueEntity" items="${svIdcontextId_productValueMap}">
                                                <tr class="odd">
                                                    <td class="center  sorting_1"></td>
                                                    <td colspan="3">${svIdcontextId_productValueEntity.key}</td>
                                                    <td colspan="3">${svIdcontextId_productValueEntity.value}</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="dataTables_paginate paging_bootstrap">
                                                <%--<ul class="pagination">--%>
                                                    <%--<c:forEach var="historyContextId" items="${historySVRuleInfoMap_cpm.keySet()}">--%>
                                                        <%--<li><a href="javascript:void(0)" onclick="scrollNow('contextId${historyContextId}')">${historyContextId}</a></li>--%>
                                                    <%--</c:forEach>--%>
                                                <%--</ul>--%>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <div class="table-header" style="line-height: 20px;">
                                初选以及合并过程数据
                            </div>
                            <div class="table-responsive">
                                <div class="dataTables_wrapper" role="grid">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody>
                                        <tr role="row">
                                            <th colspan="1" width="4%;"></th>
                                            <th colspan="1" width="8%;">最佳句子向量ID</th>
                                            <th colspan="1" width="8%;">${maxValueSentenceVectorId}</th>
                                            <th colspan="1" width="32%;">最佳上下文ID</th>
                                            <th colspan="1" width="8%;">${maxValueContextId}</th>
                                            <th colspan="1" width="8%;">最高增量值</th>
                                            <th colspan="1" width="8%;">${maxValue}</th>
                                            <th colspan="1" width="8%;"></th>
                                            <th colspan="1" width="8%;"></th>
                                            <th colspan="1" width="8%;"></th>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <br/>
                        </div>

                        <div class="col-xs-12">
                            <div class="table-header" style="line-height: 20px;">
                                换参结果
                            </div>
                            <div class="table-responsive">
                                <div class="dataTables_wrapper" role="grid">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody>
                                        <tr role="row">
                                            <th colspan="1" width="4%;"></th>
                                            <th colspan="4" width="32%;">
                                                <c:forEach begin="0" end="${oldBestSentenceVector.words.size() - 1}" varStatus="status">
                                                    <span class="label label-warning">${oldBestSentenceVector.words[status.index]}</span>
                                                    <span class="label label-warning">${oldBestSentenceVector.weights[status.index]}</span>
                                                    <c:forEach items="${oldBestSentenceVector.rEntityWordInfosList[status.index]}" var="rewi">
                                                        <span class="badge badge-important">${rewi.entityType}_${rewi.entityId}_${rewi.entityName}</span>
                                                    </c:forEach>
                                                    <br/>
                                                </c:forEach>
                                            </th>
                                            <th colspan="1" width="32%;">
                                                <c:forEach items="${changeParamOptimalSvRuleInfo.matchedREntityWordInfos}" var="matchedREntityWordInfo">
                                                    <c:set var="rewiKey" value="${maxValueSentenceVectorId}_${matchedREntityWordInfo.entityType}_${matchedREntityWordInfo.entityId}"/>
                                                    <c:if test="${hitCurrentREntityWordInfoMap.get(rewiKey) != null}">
                                                        <span class="badge badge-success">${matchedREntityWordInfo.wordId}_${matchedREntityWordInfo.word}</span>
                                                        <span class="label label-success">${matchedREntityWordInfo.weights.get(0)}</span>
                                                        <span class="badge badge-success">${matchedREntityWordInfo.entityType}_${matchedREntityWordInfo.entityId}_${matchedREntityWordInfo.entityName}</span>
                                                    </c:if>
                                                    <c:if test="${hitCurrentREntityWordInfoMap.get(rewiKey) == null}">
                                                        <span class="badge badge-important">${matchedREntityWordInfo.wordId}_${matchedREntityWordInfo.word}</span>
                                                        <span class="label label-warning">${matchedREntityWordInfo.weights.get(0)}</span>
                                                        <span class="badge badge-important">${matchedREntityWordInfo.entityType}_${matchedREntityWordInfo.entityId}_${matchedREntityWordInfo.entityName}</span>
                                                    </c:if>
                                                    <br/>
                                                </c:forEach>
                                                <c:if test="${changeParamOptimalSvRuleInfo.matchedRRuleEntities.size() > 0}">
                                                    <c:forEach items="${changeParamOptimalSvRuleInfo.lackedRRuleEntities}" var="lackedRRuleEntity">
                                                        <span class="badge" style="position: relative;">${lackedRRuleEntity.entityType}_${lackedRRuleEntity.entityId}_${lackedRRuleEntity.entityName}</span><br/>
                                                    </c:forEach>
                                                </c:if>
                                            </th>
                                            <th colspan="4" width="32%;">
                                                <c:forEach items="${oldBestSvRuleInfo.matchedREntityWordInfos}" var="matchedREntityWordInfo">
                                                    <span class="label label-important" style="position: relative;">${matchedREntityWordInfo.wordId}_${matchedREntityWordInfo.word}</span>
                                                    <span class="label label-warning">${matchedREntityWordInfo.weights.get(0)}</span>
                                                    <span class="badge badge-important" style="position: relative;">${matchedREntityWordInfo.entityType}_${matchedREntityWordInfo.entityId}_${matchedREntityWordInfo.entityName}</span>
                                                    <br/>
                                                </c:forEach>
                                                <c:if test="${oldBestSvRuleInfo.matchedRRuleEntities.size() > 0}">
                                                    <c:forEach items="${oldBestSvRuleInfo.lackedRRuleEntities}" var="lackedRRuleEntity">
                                                        <span class="badge" style="position: relative;">${lackedRRuleEntity.entityType}_${lackedRRuleEntity.entityId}_${lackedRRuleEntity.entityName}</span><br/>
                                                    </c:forEach>
                                                </c:if>
                                            </th>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12">
                            <div class="table-header" style="line-height: 20px;">
                                评判数据
                            </div>
                            <div class="table-responsive">
                                <div class="dataTables_wrapper" role="grid">
                                    <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                        <tbody>
                                        <tr role="row">
                                            <th colspan="1" width="4%;"></th>
                                            <th colspan="1" width="8%;">历史规则里的实体数量</th>
                                            <th colspan="1" width="8%;">${historyEntitiesCount}</th>
                                            <th colspan="1" width="32%;">当前句子向量里的实体数量</th>
                                            <th colspan="1" width="8%;">${currentEntitiesCount}</th>
                                            <th colspan="1" width="8%;">当前核心实体数</th>
                                            <th colspan="1" width="8%;">${currentCoreEntitiesCount}</th>
                                            <th colspan="1" width="8%;">可换参数所占的权重在句子向量中权重的占比</th>
                                            <th colspan="1" width="8%;">${currentHitEntityWeightRate}</th>
                                            <th colspan="1" width="8%;"></th>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <br/>
                        </div>

                    </div>
                    <%--CPM数据 end--%>

                    <%--FPM数据 start--%>
                    <div id="ContentFPM" style="display:none">
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
                                                    <td id="fpm_sv_"${sentenceVector.id}></td>
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
                                规则向量数据
                            </div>
                            <div class="table-responsive">
                                <div class="dataTables_wrapper" role="grid">
                                    <div style="overflow-x:hidden; height:180px; max-height:180px;">
                                        <table class="table table-striped table-bordered table-hover dataTable" aria-describedby="sample-table-2_info">
                                            <tbody id="ruleTable" role="alert" aria-live="polite" aria-relevant="all" >

                                            <c:forEach var="historySVRuleInfoEntity" items="${historySVRuleInfoMap_fpm}">
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
                                                        <c:if test="${historySVRuleInfoEntity.value.lackedRRuleEntities.size() > 0}">${historySVRuleInfoEntity.key}</c:if>
                                                        <c:if test="${historySVRuleInfoEntity.value.lackedRRuleEntities.size() <= 0}"><i class="icon-warning-sign bigger-120"></i> <s>${historySVRuleInfoEntity.key}</s></c:if>
                                                    </td>
                                                    <td>${historySVRuleInfoEntity.value.ruleId}</td>
                                                    <td>
                                                        <c:if test="${historySVRuleInfoEntity.value.isLongConversationRule == false}">短对话</c:if>
                                                        <c:if test="${historySVRuleInfoEntity.value.isLongConversationRule == true}">长对话</c:if>
                                                    </td>
                                                    <td>${historySVRuleInfoEntity.value.matchType}</td>
                                                    <td>${historySVRuleInfoEntity.value.runningAccuracyThreshold}</td>
                                                    <td>${historySVRuleInfoEntity.value.algorithmType}</td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1"></td>
                                                    <td colspan="3">${historySVRuleInfoEntity.value.sentence}</td>
                                                    <td colspan="3">${historySVRuleInfoEntity.value.sentenceModified}</td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1  "></td>
                                                    <td>多余词语</td>
                                                    <td colspan="5">
                                                        <c:forEach items="${historySVRuleInfoEntity.value.redundantWordInfos}" var="redundantWordInfo">
                                                            <span class="label" style="position: relative;">${redundantWordInfo.wordId}_${redundantWordInfo.word}_${redundantWordInfo.weight}</span>
                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1  "></td>
                                                    <td>匹配的REWIs</td>
                                                    <td colspan="5">
                                                        <c:forEach items="${historySVRuleInfoEntity.value.matchedREntityWordInfos}" var="matchedREntityWordInfo">
                                                            <span class="label label-warning" style="position: relative;">${matchedREntityWordInfo.wordId}_${matchedREntityWordInfo.word} -> ${matchedREntityWordInfo.entityId}_${matchedREntityWordInfo.entityName}</span>
                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1  "></td>
                                                    <td>匹配的RREs</td>
                                                    <td colspan="5">
                                                        <c:forEach items="${historySVRuleInfoEntity.value.matchedRRuleEntities}" var="matchedRRuleEntity">
                                                            <span class="badge badge-important" style="position: relative;">${matchedRRuleEntity.entityType}_${matchedRRuleEntity.entityId}_${matchedRRuleEntity.entityName}</span>
                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                                <tr class="odd">
                                                    <td class="center  sorting_1  "></td>
                                                    <td>缺失的RREs</td>
                                                    <td colspan="5">
                                                        <c:if test="${historySVRuleInfoEntity.value.lackedRRuleEntities.size() > 0}">
                                                            <c:forEach items="${historySVRuleInfoEntity.value.lackedRRuleEntities}" var="lackedRRuleEntity">
                                                                <span class="badge" style="position: relative;">${lackedRRuleEntity.entityType}_${lackedRRuleEntity.entityId}_${lackedRRuleEntity.entityName}</span>
                                                            </c:forEach>
                                                        </c:if>
                                                        <c:if test="${historySVRuleInfoEntity.value.lackedRRuleEntities.size() <= 0}">
                                                                <span class="label label-warning">
                                                                    <i class="icon-warning-sign bigger-120"></i>
                                                                    没有缺失参数，无需补充参数
                                                                </span>
                                                        </c:if>

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
                                                    <c:forEach var="historyContextId" items="${historySVRuleInfoMap_fpm.keySet()}">
                                                        <li><a href="javascript:void(0)" onclick="scrollNow('contextId${historyContextId}')">${historyContextId}</a></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <%--FPM数据 end--%>
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
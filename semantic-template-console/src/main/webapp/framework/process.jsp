<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cooler.semantic.entity.LogDataProcess"%>
<!-- start: Content -->
<div id="content" class="span10">

    <ul class="breadcrumb">
        <li>
            <i class="icon-home"></i>
            <a href="index.html">Home</a>
            <i class="icon-angle-right"></i>
        </li>
        <li><a href="#">Tasks</a></li>
    </ul>

    <div class="row-fluid">

        <div class="span7">
            <h1>Tasks</h1>

            <div class="priority high"><span>high priority</span></div>

            <div class="task high">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="task high">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div>1 day</div>
                </div>
            </div>
            <div class="task high">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="task high last">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div>1 day</div>
                </div>
            </div>

            <div class="priority medium"><span>medium priority</span></div>

            <div class="task medium">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="task medium last">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>

            <div class="priority low"><span>low priority</span></div>

            <div class="task low">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="task low">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="task low">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="task low">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="task low">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="task low">
                <div class="desc">
                    <div class="title">Lorem Ipsum</div>
                    <div>Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit</div>
                </div>
                <div class="time">
                    <div class="date">Jun 1, 2012</div>
                    <div> 1 day</div>
                </div>
            </div>
            <div class="common-modal modal fade" id="common-Modal1" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-content">
                    <ul class="list-inline item-details">
                        <li><a href="#">Admin templates</a></li>
                        <li><a href="http://themescloud.org">Bootstrap themes</a></li>
                    </ul>
                </div>
            </div>
            <div class="clearfix"></div>

        </div>

        <div class="span5 noMarginLeft">

            <div class="dark">

                <h1>Timeline</h1>

                <div class="timeline">
                    <c:forEach var="logDataProcess" items="${logDataProcesses}">
                        <div class="timeslot">
                            <div class="task">
                            <span>
                                <span class="details">
                                    ${logDataProcess.sentence}
                                </span>
                            </span>
                                <div class="arrow"></div>
                            </div>
                            <div class="icon">
                                <i class="icon-map-marker"></i>
                            </div>
                            <div class="time">
                                    ${logDataProcess.dateTime}
                            </div>
                        </div>

                        <%--<div class="clearfix"></div>--%>

                        <div class="timeslot alt">
                            <div class="task">
                            <span>
                                <span class="details">
                                    ${logDataProcess.responseMsg}
                                </span>
                            </span>
                                <div class="arrow"></div>
                            </div>
                            <div class="icon">
                                <i class="icon-phone"></i>
                            </div>
                            <div class="time">
                                    ${logDataProcess.dateTime}
                            </div>
                        </div>

                    </c:forEach>





                    <div class="timeslot">
                        <div class="task">
                            <span>
                                <span class="details">
                                    Dennis Ji at Bootstrap Metro Dashboard HQ
                                </span>
                            </span>
                            <div class="arrow"></div>
                        </div>
                        <div class="icon">
                            <i class="icon-map-marker"></i>
                        </div>
                        <div class="time">
                            3:43 PM
                        </div>
                    </div>

                    <%--<div class="clearfix"></div>--%>

                    <div class="timeslot alt">
                        <div class="task">
                            <span>
                                <span class="details">
                                    Dennis Ji
                                </span>
                            </span>
                            <div class="arrow"></div>
                        </div>
                        <div class="icon">
                            <i class="icon-phone"></i>
                        </div>
                        <div class="time">
                            3:43 PM
                        </div>
                    </div>





                </div>
            </div>

        </div>

    </div>



</div><!--/.fluid-container-->

<!-- end: Content -->


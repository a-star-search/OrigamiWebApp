<%--
  ~    This file is part of "Origami".
  ~
  ~     Origami is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     (at your option) any later version.
  ~
  ~     Origami is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with Origami.  If not, see <https://www.gnu.org/licenses/>.
  --%>

<jsp:useBean id="figure" scope="request" type="java.lang.String"/>
<jsp:useBean id="visibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="BIRDvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="BIRDflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="BIRDthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="BIRD_BLINTZvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="BIRD_BLINTZflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="BIRD_BLINTZthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="BLINTZvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="BLINTZflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="BLINTZthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="BOATvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="BOATflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="BOATthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="DIAMONDvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="DIAMONDflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="DIAMONDthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="FISHvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="FISHflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="FISHthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="KITEvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="KITEflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="KITEthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="PRELIMINARYvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="PRELIMINARYflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="PRELIMINARYthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="WATERBOMBvisibleEdges" scope="request" type="java.lang.String"/>
<jsp:useBean id="WATERBOMBflat" scope="request" type="java.lang.String"/>
<jsp:useBean id="WATERBOMBthreeD" scope="request" type="java.lang.String"/>
<jsp:useBean id="cameraposition" scope="request" type="java.lang.String"/>
<jsp:useBean id="papersidelength" scope="request" type="java.lang.String"/>
<%@ page session="false" %>
<%@ page contentType="text/html" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Origami Designer</title>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <c:choose>
        <c:when test="${developmentMode}">
            <%--no caching--%>
            <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
            <meta http-equiv="Pragma" content="no-cache" />
            <meta http-equiv="Expires" content="0" />
            <%--un optimized css code--%>
            <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet" title="Default Style">
            <link rel="stylesheet" href="<c:url value="/resources/css/toggle-switch.css" />">
            <link rel="stylesheet" href="<c:url value="/resources/css/toggle-button.css" />">
        </c:when>
        <c:otherwise>
            <base href="${pageContext.request.contextPath}/"/>
            <link rel="stylesheet" type="text/css" href="wro/allcss.css"/>
        </c:otherwise>
    </c:choose>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" />
    <link rel="shortcut icon" href="resources/img/pajarita.png" />
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12" id='headerdiv'>
            <h1 id="header">Orig<img src="resources/img/pajarita.png" alt="a" style="width: 35px;margin-bottom: 10px;">mi
                Designer</h1>
        </div>
    </div>
    <div class="row" id="control-panel">
        <div class="col-md-12">
            <div class="row row-m-t" id="startFromRow">
                <table>
                    <tr>
                        <td class="control-panel-left-col">
                            <span class="label label-default toolbarcontroldescription marginright10">Start From</span>
                        </td>
                        <td>
                            <select class="form-control" id="initialFigureSelect" title="foldType"
                                    style="width: 140px;">
                                <option value="square">Square Paper</option>
                                <option value="bird">Bird Base</option>
                                <option value="birdBlintz">Bird Blintz Base</option>
                                <option value="blintz">Blintz Base</option>
                                <option value="boat">Boat Base</option>
                                <option value="diamond">Diamond Base</option>
                                <option value="fish">Fish Base</option>
                                <option value="kite">Kite Base</option>
                                <option value="preliminary">Preliminary Base</option>
                                <option value="waterbomb">Waterbomb Base</option>
                                <%--
                                <option value="frog">Frog Base</option>
                                --%>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="row row-m-t" id="undoRow">
                <table>
                    <tr>
                        <td class="control-panel-left-col">
                        </td>
                        <td>
                            <button type="button" id='undobutton' class="btn buttontext appbtn">Undo Last
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="row row-m-t" id="resetRow">
                <table>
                    <tr>
                        <td class="control-panel-left-col">
                        </td>
                        <td>
                            <button type="button" id='resetbutton' class="btn buttontext appbtn" data-toggle="modal"
                                    data-target="#resetFigureModal">Reset
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="row row-m-t">
                <table>
                    <tr>
                        <td class="control-panel-left-col">
                            <span class="label label-default toolbarcontroldescription marginright10">View</span>
                        </td>
                        <td>
                            <div class="btn-group" data-toggle="buttons">
                                <label class="btn btn-primary active toggle-button buttontext figure-view-btn">
                                    <input type="radio" name="figureDisplay" id="flatDisplay" autocomplete="off" value="flatDisplay"
                                           checked> Flat
                                </label>
                                <label class="btn btn-primary toggle-button buttontext figure-view-btn">
                                    <input type="radio" name="figureDisplay" id="threeDDisplay" autocomplete="off" value="threeDDisplay"> 3-D
                                </label>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="row row-m-t" id="foldModeRow">
                <table>
                    <tr>
                        <td class="control-panel-left-col">
                            <span class="label label-default toolbarcontroldescription marginright10">Fold mode</span>
                        </td>
                        <td>
                            <span class="label label-default toolbarcontrolhelpdescription">OFF</span>
                            <label class="switch toolbarcontrol">
                                <input type="checkbox" id="fold-mode-checkbox">
                                <span class="slider" id="fold-mode-slider"></span>
                            </label>
                            <span class="label label-default toolbarcontrolhelpdescription">ON</span>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="row row-m-t">
                <table>
                    <tr>
                        <td class="control-panel-left-col">
                            <span class="label label-default toolbarcontroldescription marginright10">Face colors</span>
                        </td>
                        <td><input type="color" id="coloredFace2" name="coloredFace2" class='toolbarcontrol'
                                   title="colored face 2"></td>
                        <td><input type="color" id="coloredFace1" name="coloredFace1" class='toolbarcontrol'
                                   title="colored face 1"></td>
                    </tr>
                </table>
            </div>
            <div class="row row-m-t">
                <table>
                    <tr>
                        <td class="control-panel-left-col" data-toggle="tooltip"
                            data-placement="top" title="Rotates the figure around the Z axis.">
                            <span class="label label-default toolbarcontroldescription marginright10">Rotate</span>
                        </td>
                        <td data-toggle="tooltip" data-placement="top" title="Rotates the figure around the Z axis.">
                            <button type="button" class="btn buttontext halfappbtn" id="rotate-around-left-button"
                                    onClick='guiControllerModule.rotateFigure(-0.2617993878);'>15&deg; L
                            </button>
                        </td>
                        <td data-toggle="tooltip" data-placement="top" title="Rotates the figure around the Z axis.">
                            <button type="button" class="btn buttontext halfappbtn" id="rotate-around-right-button"
                                    onClick='guiControllerModule.rotateFigure(0.2617993878);'>15&deg; R
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="row row-m-t">
                <table>
                    <tr>
                        <td class="control-panel-left-col">
                        </td>
                        <td>
                            <button type="button" id='aboutbutton' class="btn buttontext appbtn"
                                    data-container="body" data-toggle="modal" data-target="#aboutModal">About
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="row row-m-t">
                <table>
                    <tr>
                        <td class="control-panel-left-col">
                        </td>
                        <td>
                            <button type="button" id='tutorialbutton' class="btn buttontext appbtn"
                                    data-container="body"
                                    data-toggle="popover" title="Origami Designer"
                                    data-placement="right"
                                    data-trigger="focus"
                                    data-content="Coming soon...">Tutorial
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div id="fold-settings-panel" class="card" style="border: none;">
        <div class="card-block">
            <div class="row" style="margin-left: 0;">
                <div class="col-md-12">
                    <div class="row row-m-t">
                        <h3 class="card-title">Fold Settings</h3>
                    </div>
                    <div class="row row-m-t">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col">
                                    <span class="label label-default toolbarcontroldescription marginright10">Fold type</span>
                                </td>
                                <td>
                                    <select class="form-control" id="foldTypeSelect" title="foldType"
                                            style="width: 140px;">
                                        <option value="linefold" selected="selected">Line Fold</option>
                                        <option value="crease">Crease</option>
                                        <%--<option value="sinkfold">Sink Fold</option>--%>
                                        <%--<option value="wrap">Wrap Around</option>--%>
                                        <%--<option value="curve">Curve</option>--%>
                                        <%--<option value="pull">Pull Out</option>--%>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col" data-toggle="tooltip" data-placement="top"
                                title="Define a segment by marking the end points or by selecting a pair of edges.">
                                    <span class="label label-default toolbarcontroldescription marginright10">Define with</span>
                                </td>
                                <td>
                                    <select class="form-control" id="segmentDefinitionSelect" title="define"
                                            style="width: 140px;">
                                        <option value="ends" selected="selected">Ends</option>
                                        <option value="edges">Edges</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t" id="foldDirectionRow">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col">
                                    <span class="label label-default toolbarcontroldescription marginright10">Direction</span>
                                </td>
                                <td>
                                    <select class="form-control" id="foldDirectionSelect" title="foldType"
                                            style="width: 140px;">
                                        <option value="valley">Valley</option>
                                        <option value="mountain">Mountain</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t" id="dockToVertexRow">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col">
                                    <span class="label label-default toolbarcontroldescription marginright10">Dock to vertices</span>
                                </td>
                                <td>
                                    <span class="label label-default toolbarcontrolhelpdescription">OFF</span>
                                    <label class="switch toolbarcontrol">
                                        <input type="checkbox" id="dock-to-vertex-checkbox">
                                        <span class="slider" id="dock-to-vertex-slider"></span>
                                    </label>
                                    <span class="label label-default toolbarcontrolhelpdescription">ON</span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t" id="layerFoldRow">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col"  data-toggle="tooltip" data-placement="top"
                                    title="Fold the first -or most shallow- layer and all layers connected to it. / Fold all possible layers that the fold line fully intersects.">
                                    <span class="label label-default toolbarcontroldescription marginright10">Layers</span>
                                </td>
                                <td data-toggle="tooltip" data-placement="top"
                                    title="Fold the first -or most shallow- layer and all layers connected to it. / Fold all possible layers that the fold line fully intersects.">
                                    <select class="form-control" id="layerSelect" title="foldType"
                                            style="width: 140px;">
                                        <option value="firstlayer" selected="selected">First</option>
                                        <option value="alllayers">All</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t" id="foldAngleRow">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col">
                                    <span class="label label-default toolbarcontroldescription marginright10">Angle</span>
                                </td>
                                <td data-toggle="tooltip" data-placement="top"
                                    title="Click or drag to select the fold angle.">
                                    <input class="knob" data-angleOffset=-90 data-angleArc=180 data-step="10"
                                           data-height="70"
                                           data-width="140"
                                           data-min="0"
                                           data-max="180" data-fgColor="#111111"
                                           data-rotation="clockwise" value="180" title="fold knob">
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col">
                                </td>
                                <td>
                                    <button type="button" class="btn buttontext appbtn" id="make-fold-button"
                                            onClick='guiControllerModule.makeFold();' disabled>Make Fold
                                    </button>
                                    <button type="button" class="btn buttontext appbtn" id="make-crease-button"
                                            onClick='guiControllerModule.makeCrease();' style="display: none" disabled>Make Crease
                                    </button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t"></div>
                    <div class="row row-m-t">
                        <h3 class="card-title">Fold UI Settings</h3>
                    </div>
                    <div class="row row-m-t" id="divisionMarksRow">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col">
                                    <span class="label label-default toolbarcontroldescription marginright10">Division marks</span>
                                </td>
                                <td>
                                    <span class="label label-default toolbarcontrolhelpdescription">OFF</span>
                                    <label class="switch toolbarcontrol">
                                        <input type="checkbox" id="display-guide-checkbox">
                                        <span class="slider" id="display-guide-slider"></span>
                                    </label>
                                    <span class="label label-default toolbarcontrolhelpdescription">ON</span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t" id="guideMarksRow">
                        <table>
                            <tr>
                                <td class="fold-settings-panel-left-col">
                                    <span class="label label-default toolbarcontroldescription marginright10">Divisions</span>
                                </td>
                                <td>
                                    <select class="form-control" id="guideMarksSelect" name="guideMarksSelect"
                                            title="guideMarks" style="width: 140px;">
                                        <option value="half" selected="selected">2</option>
                                        <option value="third">3</option>
                                        <option value="quarter">4</option>
                                        <option value="fifth">5</option>
                                        <option value="eigth">8</option>
                                    </select>
                                </td>
                            </tr>
                            <tr style="height: 30px;">
                                <td class="fold-settings-panel-left-col">
                                </td>
                                <td id="sliderTD"  style="width: 140px;">
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="row row-m-t"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id='resetalert' class="alert bottomalert alert-info fade show" role="alert">
    Figure has been reset to its initial form.
    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
        <span aria-hidden="true">&times;</span>
    </button>
</div>
<div id='changecoloralert' class="alert bottomalert alert-info fade show" role="alert">
    Face color has been changed
    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
        <span aria-hidden="true">&times;</span>
    </button>
</div>
<div class="row" style="margin-top: 40px;">
    <div class="col-md-12">
        <canvas id="renderCanvas"></canvas>
    </div>
</div>
</div>
<!-- Modals -->
<div class="modal fade" id="resetFigureModal" tabindex="-1" role="dialog" aria-labelledby="resetFigureModalLabel"
     aria-hidden="true" style="top: 200px;">
    <div class="modal-dialog" role="document" style="width: 320px;">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Reset Figure</h5>
                <button type="button" class="close" id='closeResetModal' data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>You will lose your progress.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary appbtn" id='confirmReset'>
                    Confirm
                </button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="aboutModal" tabindex="-1" role="dialog" aria-labelledby="aboutModal" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLongTitle">About the Virtual Origami application:</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <h4>Introduction</h4>
                <p style="text-align:justify">This is a virtual origami application. That is, an application that lets the user perform different kinds of folds on a square or a folded figure that comes from a square, as origami purists would have. Conveniently, this also slightly simplifies the application.

                <p style="text-align:justify">It aims to (eventually) replicate most of the instructions that can be found in an origami book. Although, in all likelihood, it will never reach the same degree of flexibility that can be communicated in origami instructions which sometimes can also include brief explanatory comments to perform a fold.

                <p style="text-align:justify">It was designed from the beginning with the idea that it would have no restrictions. For example, there is no limit to the number of layers that can be folded together (although it would be a good idea to set an upper limit to prevent what would be unfeasible folds on a real piece of paper of a given size and material). It is also possible to make folds on any plane of the 3D space, so that the resulting figure does not have to be two-dimensional.

                <h4>Representation system</h4>
                <p style="text-align:justify">It is based on a system of layered bundles. Each figure is a set of such bundles, where each bundle occupies a different plane in space. For example, a bidimensional figure -most origami figures are- would consist of a single bundle of faces that have a relative position to each other in the direction of a perpendicular to the plane in which they exist.

                <p style="text-align:justify">The bundle of faces system can represent any figure except those where the faces are deformed to give it a curved or puffed-up shape; in our system, all faces are perfectly straight. This is because folding and rendering layers of deformed faces would be incredibly complicated. In our defense, in practice, a "puffed" or curve deformation is usually only done as a last step.

                <p style="text-align:justify">If the application is not flexible enough to arrive at any arbitrary figure, it is not due to a limitation of its representation system, but because of the lack of flexibility of the GUI to define some folds.

                <h4>Flat and 3D views</h4>
                <p style="text-align:justify">The view of the figure can be toggled between flat and 3D. The flat view is the ideal aspect of the model if we were using an extremely thin piece of paper that is also perfectly rigid and that allows perfectly flat folds. There is nothing close to that sort of material in the real world, so a 3D view is needed to give for a more realistic aspect of the figure.

                <p style="text-align:justify">Note that the 3D view is not just a "more realistic" view. It provides the desired aspect of the figure! At least most of the time. An example would be a figure of an animal with four legs and two ears. On any ordinary origami paper it is not necessary to open up the figure at all for the extremities to be apparent.

                <p style="text-align:justify">The natural looseness of folded paper is a double-edge sword. It must be simultaneously taken advantage of and harnessed.

                <p style="text-align:justify">Furthermore, it is not only the desired look of the figure, but it lets the folder visualize the structure in order to define the next fold.

                <p style="text-align:justify">The corollary is that a good, realistic, 3D look algorithm is paramount to a virtual origami application. This is bad news, because computing such a realistic representation is a complex problem. At the moment, the application uses a series of strategies and heuristics, but it is far from perfect.

                <p style="text-align:justify">Why is then the "flat" view necessary? The flat view puts us back into the ideal world where the folds can be defined with precission in a trivial way (imagine how difficult would
                    it be to define and compute the fold of a bunch of deformed 3D or even straight faces that exist in different planes!). Any diagram of a folding step of an origami model implicitly assumes a perfectly flat model.

                <p style="text-align:justify">As you can see, both representations are complementary, even unavoidable, in a virtual origami application.
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" ></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.3/js/tether.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
<script src="<c:url value="/resources/js/lib/babylon.custom.js" />"></script>
<script src="<c:url value="/resources/js/lib/jquery.knob.min.js" />"></script>
<script src="<c:url value="/resources/js/origami.js" />"></script>
<script src="<c:url value="/resources/js/ajax.js" />"></script>
<script src="<c:url value="/resources/js/ajax_controller.js" />"></script>
<script src="<c:url value="/resources/js/geometry.js" />"></script>
<script src="<c:url value="/resources/js/scene.js" />"></script>
<script src="<c:url value="/resources/js/util.js" />"></script>
<script src="<c:url value="/resources/js/material.js" />"></script>
<script src="<c:url value="/resources/js/drawing.js" />"></script>
<script src="<c:url value="/resources/js/point_docking.js" />"></script>
<script src="<c:url value="/resources/js/edge_docking.js" />"></script>
<script src="<c:url value="/resources/js/gui_controller.js" />"></script>
<script src="<c:url value="/resources/js/ends_folding.js" />"></script>
<script src="<c:url value="/resources/js/edges_folding.js" />"></script>
<script src="<c:url value="/resources/js/guidemarks.js" />"></script>
<script src="<c:url value="/resources/js/knob_hooks.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/AjaxDataStructureConverter.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/Color.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/Face.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/Figure.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/LegacyRibbonFigure.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/Point.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/RibbonFigure.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/RibbonPolygon.js" />"></script>
<script src="<c:url value="/resources/tsc/figureelements/Segment.js" />"></script>
<script>
   var scene;
   var canvas;
   var initialColorOne;
   var initialColorTwo;
   var defaultColorOne = utilModule.html5ColorToRGB("#FFFFFF");
   var defaultColorTwo = utilModule.html5ColorToRGB("#FFFFFF");
   let figure = JSON.parse('${figure}');
   let edges = JSON.parse('${visibleEdges}');
   let birdEdges = JSON.parse('${BIRDvisibleEdges}');
   let birdFlat = JSON.parse('${BIRDflat}');
   let birdThreeD = JSON.parse('${BIRDthreeD}');
   let birdBlintzEdges = JSON.parse('${BIRD_BLINTZvisibleEdges}');
   let birdBlintzFlat = JSON.parse('${BIRD_BLINTZflat}');
   let birdBlintzThreeD = JSON.parse('${BIRD_BLINTZthreeD}');
   let blintzEdges = JSON.parse('${BLINTZvisibleEdges}');
   let blintzFlat = JSON.parse('${BLINTZflat}');
   let blintzThreeD = JSON.parse('${BLINTZthreeD}');
   let boatEdges = JSON.parse('${BOATvisibleEdges}');
   let boatFlat = JSON.parse('${BOATflat}');
   let boatThreeD = JSON.parse('${BOATthreeD}');
   let diamondEdges = JSON.parse('${DIAMONDvisibleEdges}');
   let diamondFlat = JSON.parse('${DIAMONDflat}');
   let diamondThreeD = JSON.parse('${DIAMONDthreeD}');
   let fishEdges = JSON.parse('${FISHvisibleEdges}');
   let fishFlat = JSON.parse('${FISHflat}');
   let fishThreeD = JSON.parse('${FISHthreeD}');
   let kiteEdges = JSON.parse('${KITEvisibleEdges}');
   let kiteFlat = JSON.parse('${KITEflat}');
   let kiteThreeD = JSON.parse('${KITEthreeD}');
   let preliminaryEdges = JSON.parse('${PRELIMINARYvisibleEdges}');
   let preliminaryFlat = JSON.parse('${PRELIMINARYflat}');
   let preliminaryThreeD = JSON.parse('${PRELIMINARYthreeD}');
   let waterbombEdges = JSON.parse('${WATERBOMBvisibleEdges}');
   let waterbombFlat = JSON.parse('${WATERBOMBflat}');
   let waterbombThreeD = JSON.parse('${WATERBOMBthreeD}');
   let cameraPosition = JSON.parse('${cameraposition}');
   let engine;

   let papersidelength =  JSON.parse('${papersidelength}');
   var dockingSphereDiameter = papersidelength/50;
   var dockingCylinderDiameter = papersidelength/50;
   var maxDockableDistance = papersidelength/30;

   function initializeComponentBehavior(){
      window.addEventListener('resize', function () {
         engine.resize();
      });
      $('input[type=radio][name=figureDisplay]').change(function() {
         if (this.value == 'flatDisplay') {
            guiControllerModule.showFlatFigure();
         } else if (this.value == 'threeDDisplay') {
            guiControllerModule.show3DFigure();
         }
      });
      $('#fold-mode-checkbox').change(function () {
         console.log("toggle fold mode ", this.checked);
         if(this.checked){
            guiControllerModule.foldModeOn();
         } else {
            guiControllerModule.foldModeOff();
         }

      });
      $('#foldDirectionSelect').change(function () {
         guiControllerModule.changedFoldDirection($('#foldDirectionSelect option:selected').val());
      });
      $('#confirmReset').click(function () {
         guiControllerModule.showResetAlert();
      });
      $('#undobutton').click(function () {
         guiControllerModule.undo();
      });
      $('#foldTypeSelect').on('change', function () {
         guiControllerModule.foldTypeSelection(this.value);
      });
      $('#segmentDefinitionSelect').on('change', function () {
         guiControllerModule.segmentDefinitionSelect(this.value);
      });
      $('#initialFigureSelect').on('change', function () {
         guiControllerModule.baseSelection(this.value);
      });
      $('#display-guide-checkbox').change(function () {
         if(this.checked){
            guiControllerModule.guidemarksOn();
         } else {
            guiControllerModule.guidemarksOff();
         }
      });
      $('#dock-to-vertex-checkbox').change(function () {
         if(this.checked){
            guiControllerModule.dockToVertexOn();
         } else {
            guiControllerModule.dockToVertexOff();
         }
      });
      $('[data-toggle="popover"]').popover();
      $('.popover-dismiss').popover({
         trigger: 'focus'
      });

      $('[data-toggle="tooltip"]').tooltip();

      document.querySelector("#coloredFace1").addEventListener('change', function (event) {
         guiControllerModule.updateColoredFace(0, event.target.value);
      }, false);
      document.querySelector("#coloredFace2").addEventListener('change', function (event) {
         guiControllerModule.updateColoredFace(1, event.target.value);
      }, false);
      document.querySelector("#confirmReset").addEventListener('click', function () {
         guiControllerModule.reset();
      }, false);

      $('.knob').knob({
         change : function (value) {
            knobHooksModule.change(value);
         },
         release : function (value) {
            knobHooksModule.release(value);
         },
         cancel : function () {
            console.log("cancel : ", this);
         },
         draw : function () {
            knobHooksModule.draw(this);
         }
      });
   }

   function setEventListeners(){
      window.addEventListener('mousedown', function (e) {
         guiControllerModule.mouseDown(e);
      });
      window.addEventListener('mousemove', function (e) {
         guiControllerModule.mouseMove(e);
      });
      window.addEventListener('mouseup', function (e) {
         guiControllerModule.mouseUp(e);
      });
      canvas.addEventListener('mousedown', function (e) {
         guiControllerModule.mouseDown(e);
      });
      canvas.addEventListener('pointerdown', function (e) {
         guiControllerModule.mouseDown(e);
      });
      canvas.addEventListener('mousemove', function (e) {
         guiControllerModule.mouseMove(e);
      });
      canvas.addEventListener('pointermove', function (e) {
         guiControllerModule.mouseMove(e);
      });
      canvas.addEventListener('mouseup', function (e) {
         guiControllerModule.mouseUp(e);
      });
      canvas.addEventListener('pointerup', function (e) {
         guiControllerModule.mouseUp(e);
      });
   }

   function createCanvasAndSceneObjects(){
      canvas = document.getElementById('renderCanvas');
      engine = new BABYLON.Engine(canvas, true);
      scene = sceneModule.createScene(canvas, engine, cameraPosition);
      scene.preventDefaultOnPointerDown = false;
      origamiModule.figure.square = {flat: figure, threed: figure, edges: edges};
      origamiModule.addBase("bird", birdEdges, birdFlat, birdThreeD);
      origamiModule.addBase("birdBlintz", birdBlintzEdges, birdBlintzFlat, birdBlintzThreeD);
      origamiModule.addBase("blintz", blintzEdges, blintzFlat, blintzThreeD);
      origamiModule.addBase("boat", boatEdges, boatFlat, boatThreeD);
      origamiModule.addBase("diamond", diamondEdges, diamondFlat, diamondThreeD);
      origamiModule.addBase("fish", fishEdges, fishFlat, fishThreeD);
      origamiModule.addBase("kite", kiteEdges, kiteFlat, kiteThreeD);
      origamiModule.addBase("preliminary", preliminaryEdges, preliminaryFlat, preliminaryThreeD);
      origamiModule.addBase("waterbomb", waterbombEdges, waterbombFlat, waterbombThreeD);
      engine.runRenderLoop(function () {
         scene.render();
      });
   }
   function initializeFaceColorColorPickers(){
      if(figure.colorOne == null){
         document.querySelector("#coloredFace1").value = utilModule.rgbColorToHex(defaultColorOne);
      } else {
         document.querySelector("#coloredFace1").value = utilModule.rgbColorToHex(figure.colorOne);
      }
      document.querySelector("#coloredFace1").select();
      if(figure.colorTwo == null){
         document.querySelector("#coloredFace2").value = utilModule.rgbColorToHex(defaultColorTwo);
      } else {
         document.querySelector("#coloredFace2").value = utilModule.rgbColorToHex(figure.colorTwo);
      }
      document.querySelector("#coloredFace2").select();
   }
   function initializeComponentState(){
      $('#guideMarksRow').hide();
      $('#fold-mode-checkbox').click();
      initializeFaceColorColorPickers();
   }
   function startup() {
      createCanvasAndSceneObjects();
      initializeComponentBehavior();
      setEventListeners();
      initializeComponentState();
      initialColorOne = figure.colorOne;
      if(figure.colorTwo === null){
         initialColorTwo = figure.colorOne
      } else {
         initialColorTwo = figure.colorTwo;
      }
      geometryModule.recalculateVisibleEdges();
   }
   window.addEventListener("load", startup, false);
</script>
<script>
   $( function() {
      let gmSelect = $( '#guideMarksSelect' );
      let gmSlider = $( '<div id="guideMarksSlider"></div>' ).slider({
         min: 1,
         max: 5,
         step: 1,
         range: "min",
         value: gmSelect[ 0 ].selectedIndex + 1,
         slide: function( event, ui ) {
            gmSelect[ 0 ].selectedIndex = ui.value - 1;
            guiControllerModule.updateGuidemarks();
         }
      });
      $( "#sliderTD" ).append(gmSlider);
      $( "#guideMarksSelect" ).on( "change", function() {
         gmSlider.slider( "value", this.selectedIndex + 1 );
         guiControllerModule.updateGuidemarks();
      });
   } );
</script>
</body>
</html>

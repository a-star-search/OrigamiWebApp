/*
 *    This file is part of "Origami".
 *
 *     Origami is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Origami is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Origami.  If not, see <https://www.gnu.org/licenses/>.
 */

"use strict";
/*jshint esversion: 6 */
/*jshint node: true */
/*jshint unused:false*/
/*globals scene, $:false, endsFoldingModule, edgesFoldingModule, sceneModule, pointDockingModule, edgeDockingModule, guidemarksModule, ajaxModule, origamiModule, utilModule, knobHooksModule, geometryModule */

var guiControllerModule = (function () {

   const DRAGGING_EPSILON = 5;
   let foldMode = false;
   let isDragging = false;
   let lastMouseDownPos = {x: 0, y: 0};
   let foldDefinedButNotMade = false;
   /**
    * This variable is used to store the camera position at the moment that the second point of a fold segment is clicked
    * so that it will be the same value when the fold button is clicked
    */
   let foldDefinedCameraPosition = null;

   let foldModeCheckedBefore3DDisplay = false;

   let isEndsSegmentDefinition = function(){
      return $('#segmentDefinitionSelect  option:selected').val() === "ends";
   };

   let isEdgesSegmentDefinition = function(){
      return $('#segmentDefinitionSelect  option:selected').val() === "visibleEdges";
   };

   let isLineFoldSelected = function(){
      return $('#foldTypeSelect  option:selected').val() === "linefold";
   };

   let isCreaseSelected = function(){
      return $('#foldTypeSelect  option:selected').val() === "crease";
   };

   /**
    * When two points have been defined in the GUI to define a fold segment.
    *
    * Bear in mind that a segment, although it is obviously made of a couple of points (ends)- can be defined in other ways
    * too, from which the point pair will be calculated. Hence the name of the function specifying that it has been
    * defined from a couple of points.
    */
   let pointPairFoldSegmentDefined = function (){
      $('#make-fold-button').prop('disabled', false);
      endsFoldingModule.stopAcceptingPoints = true;
      pointDockingModule.removeDockingData();
      let foldDirection = $('#foldDirectionSelect option:selected').val(); //'mountain' or 'valley'
      endsFoldingModule.drawPointPairFoldSegment(foldDirection);
      endsFoldingModule.storeOppositePointPairFoldLine(foldDirection);
      foldDefinedButNotMade = true;
      foldDefinedCameraPosition = sceneModule.getCameraPosition();
   };
   let edgePairFoldSegmentDefined = function () {
      $('#make-fold-button').prop('disabled', false);
      edgesFoldingModule.stopAcceptingEdges = true;
      edgeDockingModule.removeDockingData();
      edgesFoldingModule.drawEdgePairFoldSegment();
      foldDefinedButNotMade = true;
      foldDefinedCameraPosition = sceneModule.getCameraPosition();
   };

   let pointPairCreaseDefined = function () {
      $('#make-crease-button').prop('disabled', false);
      endsFoldingModule.stopAcceptingPoints = true;
      pointDockingModule.removeDockingData();
      let layerFold = $('#layerSelect option:selected').val();
      endsFoldingModule.drawPointPairCrease(layerFold);
      foldDefinedButNotMade = true;
      foldDefinedCameraPosition = sceneModule.getCameraPosition();
   };

   let edgePairCreaseDefined = function () {
      $('#make-crease-button').prop('disabled', false);
      edgesFoldingModule.stopAcceptingEdges = true;
      edgeDockingModule.removeDockingData();
      edgesFoldingModule.drawEdgePairCrease();
      foldDefinedButNotMade = true;
      foldDefinedCameraPosition = sceneModule.getCameraPosition();
   };

   let click = function () {
      let pickResult = scene.pick(scene.pointerX, scene.pointerY);
      if (pickResult.hit && foldMode) {
         if(isEndsSegmentDefinition()) {
            endsFoldingModule.foldClick(pickResult.pickedPoint);
            if(isLineFoldSelected()) {
               if(endsFoldingModule.foldPoints.length === 2){
                  pointPairFoldSegmentDefined();
               }
            } else if(isCreaseSelected()) {
               if(endsFoldingModule.foldPoints.length === 2){
                  pointPairCreaseDefined();
               }
            }
         }else { //pick visibleEdges
            edgesFoldingModule.foldClick(pickResult.pickedPoint);
            if(isLineFoldSelected()) {
               if(edgesFoldingModule.foldEdges.length === 2){
                  edgePairFoldSegmentDefined();
               }
            } else if(isCreaseSelected()) {
               if(edgesFoldingModule.foldEdges.length === 2){
                  edgePairCreaseDefined();
               }
            }
         }
      }
   };

   let thereIsDragging = function (startingPosition, finalPosition){
      return ((Math.abs(startingPosition.x - finalPosition.x) > DRAGGING_EPSILON) ||
         (Math.abs(startingPosition.y - finalPosition.y) > DRAGGING_EPSILON) );
   };

   let showFoldSettingsPanel = function(){
      $('#fold-settings-panel').show("slide", { direction: "right" }, 1000);
   };

   let resetGUIComponents = function(colorFace1, colorFace2){
      setColorControls(colorFace1, colorFace2);
      $('#undoRow').hide();
      $('#resetRow').hide();
      $('#startFromRow').show();
   };
   let setColorControls = function(colorFace1, colorFace2){
      let hexColor1 = utilModule.rgbColorToHex(colorFace1);
      let hexColor2 = utilModule.rgbColorToHex(colorFace2);
      if(hexColor1 === hexColor2) {
         setColorControlsWithSameColor(colorFace1);
      }
      document.querySelector("#coloredFace1").value = hexColor1;
      document.querySelector("#coloredFace1").select();
      document.querySelector("#coloredFace2").value = hexColor2;
      document.querySelector("#coloredFace2").select();
   };
   let setColorControlsWithSameColor = function(color){
      let hexColor = utilModule.rgbColorToHex(color);
      document.querySelector("#coloredFace1").value = hexColor;
      document.querySelector("#coloredFace1").select();
      document.querySelector("#coloredFace2").value = hexColor;
      document.querySelector("#coloredFace2").select();
   };

   let newFigureIsDisplayed = function () {
      $('#make-fold-button').prop('disabled', true);
      if(origamiModule.thereIsNoHistory()){
         $('#undoRow').hide();
         $('#resetRow').hide();
         $('#startFromRow').show();
      } else {
         $('#undoRow').show();
         $('#resetRow').show();
         $('#startFromRow').hide();
      }
      foldDefinedButNotMade = false;
      foldDefinedCameraPosition = null;
   };

   let displayFoldSettingsControls = function () {
      if(isLineFoldSelected()) {
         displayFoldSettingsControlsForLineFold();
      } else if (isCreaseSelected()) {
         displayFoldSettingsControlsForCrease();
      } else {
         $('#foldDirectionRow').hide();
         $('#foldAngleRow').hide();
         $('#layerFoldRow').hide();
         $('#make-fold-button').show();
         $('#make-crease-button').hide();
      }
   };

   let displayFoldSettingsControlsForLineFold = function () {
      $('#foldDirectionRow').show();
      $('#foldAngleRow').show();
      $('#layerFoldRow').show();
      $('#make-fold-button').show();
      $('#make-crease-button').hide();
      if(isEndsSegmentDefinition()) {
         pointDockingModule.turnOn();
         edgeDockingModule.turnOff();
         endsFoldingModule.stopAcceptingPoints = false;
         edgesFoldingModule.stopAcceptingEdges = true;
         $('#divisionMarksRow').show();
         $('#dockToVertexRow').show();
         let guidemarksOn = $('#display-guide-checkbox')[0].checked;
         if(guidemarksOn){
            guidemarksModule.turnOn();
         } else {
            guidemarksModule.turnOff();
         }
      } else { //segment definition with visibleEdges
         pointDockingModule.turnOff();
         edgeDockingModule.turnOn();
         endsFoldingModule.stopAcceptingPoints = true;
         edgesFoldingModule.stopAcceptingEdges = false;
         guidemarksModule.turnOff();
         $('#divisionMarksRow').hide();
         $('#dockToVertexRow').hide();
      }
   };

   let displayFoldSettingsControlsForCrease = function () {
      $('#foldDirectionRow').hide();
      $('#foldAngleRow').hide();
      $('#layerFoldRow').show();
      $('#make-fold-button').hide();
      $('#make-crease-button').show();
      if(isEndsSegmentDefinition()) {
         pointDockingModule.turnOn();
         edgeDockingModule.turnOff();
         endsFoldingModule.stopAcceptingPoints = false;
         edgesFoldingModule.stopAcceptingEdges = true;
         $('#dockToVertexRow').show();
         $('#divisionMarksRow').show();
         let guidemarksOn = $('#display-guide-checkbox')[0].checked;
         if(guidemarksOn){
            guidemarksModule.turnOn();
         } else {
            guidemarksModule.turnOff();
         }
      } else { //segment definition with visibleEdges
         pointDockingModule.turnOff();
         edgeDockingModule.turnOn();
         endsFoldingModule.stopAcceptingPoints = true;
         edgesFoldingModule.stopAcceptingEdges = false;
         guidemarksModule.turnOff();
         $('#dockToVertexRow').hide();
         $('#divisionMarksRow').hide();
      }
   };

   let resetFoldInformation = function () {
      endsFoldingModule.removeFoldData();
      foldDefinedButNotMade = false;
      foldDefinedCameraPosition = null;
      endsFoldingModule.stopAcceptingPoints = false;
      edgesFoldingModule.stopAcceptingEdges = false;
   };

   let initModule = function () {
      origamiModule.figure.registerListener(function() {
         newFigureIsDisplayed();
      });
   };

   initModule();

   return {
      mouseDown: function (e){
         isDragging = false;
         lastMouseDownPos = {x: e.screenX, y: e.screenY};
      },
      mouseMove: function (e){
         isDragging = thereIsDragging(lastMouseDownPos, {x: e.screenX, y: e.screenY});
         let pickResult = scene.pick(scene.pointerX, scene.pointerY);
         if(pickResult.hit && foldMode  ){
            if(isEndsSegmentDefinition()) {
               if(!endsFoldingModule.stopAcceptingPoints) {
                  pointDockingModule.updateDockedPoint(pickResult.pickedPoint);
               }
            } else {
               if(!edgesFoldingModule.stopAcceptingEdges) {
                  edgeDockingModule.updateDockedEdge(pickResult.pickedPoint);
               }
            }
         }
      },
      mouseUp: function (){
         if (isDragging) {
            let isFlatDisplay = origamiModule.getCurrentDisplay() === origamiModule.DISPLAY_ENUM.FLAT;
            let foldIsNotDefined = !foldDefinedButNotMade;
            if(foldIsNotDefined && isFlatDisplay){
               geometryModule.recalculateVisibleEdges();
            }
         } else {
            click();
         }
      },
      foldModeOn: function (){
         foldMode = true;
         showFoldSettingsPanel();
         displayFoldSettingsControls();
      },
      foldModeOff: function (){
         $('#fold-settings-panel').hide("slide", { direction: "right" }, 1000);
         resetFoldInformation();
         pointDockingModule.turnOff();
         edgeDockingModule.turnOff();
         guidemarksModule.turnOff();
         $('#foldTypeSelect').val('linefold');
         $('#make-fold-button').prop('disabled', true);
      },
      showResetAlert: function (){
         $("#closeResetModal").trigger("click");
         $("#resetalert").fadeTo(2000, 500).slideUp(500, function () {
            $("#resetalert").slideUp(500);
         });
      },
      changeColorAlert: function (){
         $("#changecoloralert").fadeTo(2000, 500).slideUp(500, function () {
            $("#changecoloralert").slideUp(500);
         });
      },
      updateGuidemarks: function () {
         let valueSelected = $('#guideMarksSelect')[0].value;
         if (valueSelected === "half") {
            guidemarksModule.setDivisionMarkCount(guidemarksModule.GUIDE_MARK_DIVISION_ENUM.HALF);
         } else if(valueSelected === "third") {
            guidemarksModule.setDivisionMarkCount(guidemarksModule.GUIDE_MARK_DIVISION_ENUM.THIRD);
         } else if(valueSelected === "quarter") {
            guidemarksModule.setDivisionMarkCount(guidemarksModule.GUIDE_MARK_DIVISION_ENUM.QUARTER);
         } else if(valueSelected === "fifth") {
            guidemarksModule.setDivisionMarkCount(guidemarksModule.GUIDE_MARK_DIVISION_ENUM.FIFTH);
         } else if(valueSelected === "eigth") {
            guidemarksModule.setDivisionMarkCount(guidemarksModule.GUIDE_MARK_DIVISION_ENUM.EIGTH);
         }
      },
      guidemarksOn: function () {
         guidemarksModule.turnOn();
         $('#guideMarksRow').show();
         guiControllerModule.updateGuidemarks();
      },
      guidemarksOff: function () {
         guidemarksModule.turnOff();
         $('#guideMarksRow').hide();
      },
      baseSelection: function(valueSelected) {
         if (valueSelected === "square") {
            origamiModule.loadSquare();
         } else {
            origamiModule.loadBase(valueSelected);
         }
         resetGUIComponents(initialColorOne, initialColorTwo);
      },
      segmentDefinitionSelect: function(valueSelected) {
         displayFoldSettingsControls();
         //if the selection changes, the fold information (fold points) that already existed is invalidated
         resetFoldInformation();
      },
      foldTypeSelection: function(valueSelected){
         displayFoldSettingsControls();
         //if the selection changes, the fold information (fold points) that already existed is invalidated
         resetFoldInformation();
      },
      updateColorSelectors: function(colorOne, colorTwo) {
         setColorControls(colorOne, colorTwo);
      },
      updateColoredFace: function(faceIndex, newColor){
         ajaxModule.sendUpdatedFaceColor(faceIndex, newColor);
      },
      makeFold: function(){
         let foldDirection = $('#foldDirectionSelect option:selected').val();
         let layerFold = $('#layerSelect option:selected').val();
         ajaxModule.sendLineFoldInformation( endsFoldingModule.foldPoints[0].picked, endsFoldingModule.foldPoints[0].docked, endsFoldingModule.foldPoints[0].dockedTo.name,
            endsFoldingModule.foldPoints[1].picked, endsFoldingModule.foldPoints[1].docked, endsFoldingModule.foldPoints[1].dockedTo.name,
            layerFold, knobHooksModule.value, foldDirection, foldDefinedCameraPosition);
      },
      makeCrease: function(){
         //TODO make the crease
         console.log("todo: should make the crease");
      },
      reset: function(){
         ajaxModule.resetFigure();
         origamiModule.reset();
         resetGUIComponents(initialColorOne, initialColorTwo);
      },
      changedFoldDirection: function (foldDirection) {
         if(foldDefinedButNotMade){
            endsFoldingModule.drawExistingFoldLine(foldDirection);
         }
      },
      dockToVertexOn: function () {
         pointDockingModule.dockToVertex(true);
      },
      dockToVertexOff: function () {
         pointDockingModule.dockToVertex(false);
      },
      show3DFigure: function () {
         if ($('#fold-mode-checkbox').is(":checked")){
            $('#fold-mode-checkbox').click();
            foldModeCheckedBefore3DDisplay = true;
         }
         $('#fold-mode-checkbox').attr("disabled", true);
         $('#foldModeRow').hide();
         foldMode = false;
         origamiModule.display3D();
      },
      showFlatFigure: function () {
         $('#foldModeRow').show();
         $("#fold-mode-checkbox").removeAttr("disabled");
         foldMode = foldModeCheckedBefore3DDisplay;
         if ( (!$('#fold-mode-checkbox').is(":checked")) && foldModeCheckedBefore3DDisplay){
            $('#fold-mode-checkbox').click();
         }
         foldModeCheckedBefore3DDisplay = false;
         origamiModule.displayFlat();
      },
      undo: function () {
         ajaxModule.undoLast();
      },
      rotateFigure: function (angle) {
         var lookingAtTheFront = sceneModule.getCameraPosition().z < 0;
         if(lookingAtTheFront) {
            ajaxModule.rotateFigure(angle);
         } else {
            ajaxModule.rotateFigure(-angle);
         }
      }
   };
   
})();

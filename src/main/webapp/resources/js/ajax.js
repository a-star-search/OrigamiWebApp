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
/*globals scene, home, ajaxControllerModule, utilModule, $:false */

var ajaxModule = (function () {
   return {
      baseLoaded: function(baseName){
         $.ajax({
            type : "PUT",
            contentType : "application/json",
            url : "figure/baseLoaded",
            data : JSON.stringify({baseName: baseName}),
            timeout : 100000,
            success : function(data) {
               ajaxControllerModule.baseLoadedSucess(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      squareLoaded: function(){
         $.ajax({
            type : "PUT",
            contentType : "application/json",
            url : "figure/squareLoaded",
            timeout : 100000,
            success : function(data) {
               ajaxControllerModule.squareLoadedSucess(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      makeCrease: function (p1, p2, cameraposition, layerFold) {
         let jsonPoint1 = JSON.stringify({x: p1.x, y: p1.y, z: p1.z});
         let jsonPoint2 = JSON.stringify({x: p2.x, y: p2.y, z: p2.z});
         let jsonCameraPosition = JSON.stringify({x: cameraposition.x, y: cameraposition.y, z: cameraposition.z});
         let jsonLayerFold = JSON.stringify(layerFold);
         $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "crease",
            data: {
               point1: jsonPoint1,
               point2: jsonPoint2,
               cameraposition: jsonCameraPosition,
               jsonLayerFold: jsonLayerFold
            },
            dataType : 'json',
            timeout : 100000,
            success : function(data) {
               ajaxControllerModule.makeCreaseSuccess(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      sendLineFoldInformation: function (pickedFoldPoint1, dockedFoldPoint1, dockedTo1,
                                         pickedFoldPoint2, dockedFoldPoint2, dockedTo2,
                                         layerFold, angle, folddirection, cameraposition) {
         let d = JSON.stringify({pickedFoldPoint1: pickedFoldPoint1, dockedFoldPoint1: dockedFoldPoint1, dockedTo1: dockedTo1,
                                 pickedFoldPoint2: pickedFoldPoint2, dockedFoldPoint2: dockedFoldPoint2, dockedTo2: dockedTo2,
                                 layerfold: layerFold, angle: angle, folddirection: folddirection, cameraposition: cameraposition});
         $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "fold/line",
            data : d,
            dataType : 'json',
            timeout : 100000,
            success : function(data) {
               ajaxControllerModule.sendLineFoldInformationSuccess(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      undoLast: function (){
         $.ajax({
            type : "PUT",
            contentType : "application/json",
            url : "figure/undolast",
            timeout : 100000,
            success : function(data) {
               ajaxControllerModule.undoLastSuccess(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      resetFigure: function () {
         $.ajax({
            type : "PUT",
            contentType : "application/json",
            url : "session/reset",
            timeout : 100000,
            success : function(data) {
               ajaxControllerModule.resetFigureSuccess();
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      thereIsNoHistory: function () {
         $.ajax({
            type : "GET",
            url : "figure/thereisnohistory",
            timeout : 100000,
            success : function(data) {
               return ajaxControllerModule.thereIsNoHistory(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      recalculateVisibleEdges: function (cameraPosition, handleData) {
         let jsonCameraPosition = JSON.stringify({x: cameraPosition.x, y: cameraPosition.y, z: cameraPosition.z});
         $.ajax({
            type : "GET",
            url : "gui/recalculateVisibleEdges",
            data: {
               cameraposition: jsonCameraPosition
            },
            timeout : 100000,
            success : function(data) {
               return ajaxControllerModule.recalculateVisibleEdgesSuccess(handleData, data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
               return null;
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      calculatePointPairCreaseSegment: function (dockedPoint1, dockedPoint2, layerFold, cameraPosition) {
         let jsonPoint1 = JSON.stringify({x: dockedPoint1.x, y: dockedPoint1.y, z: dockedPoint1.z});
         let jsonPoint2 = JSON.stringify({x: dockedPoint2.x, y: dockedPoint2.y, z: dockedPoint2.z});
         let jsonCameraPosition = JSON.stringify({x: cameraPosition.x, y: cameraPosition.y, z: cameraPosition.z});
         let jsonLayerFold = JSON.stringify(layerFold);
         $.ajax({
            type : "GET",
            url : "gui/crease/points",
            data: {
               point1: jsonPoint1,
               point2: jsonPoint2,
               cameraposition: jsonCameraPosition,
               jsonLayerFold: jsonLayerFold
            },
            timeout : 100000,
            success : function(data) {
               return ajaxControllerModule.calculatePointPairCreaseSegmentSuccess(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
               return null;
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      calculatePointPairLineSegmentDashes: function (folddirection, point1, point2, cameraPosition, handleData) {
         let jsonPoint1 = JSON.stringify({x: point1.x, y: point1.y, z: point1.z});
         let jsonPoint2 = JSON.stringify({x: point2.x, y: point2.y, z: point2.z});
         let jsonCameraPosition = JSON.stringify({x: cameraPosition.x, y: cameraPosition.y, z: cameraPosition.z});
         $.ajax({
            type : "GET",
            url : "gui/foldline/points",
            data: {
               folddirection: folddirection,
               point1: jsonPoint1,
               point2: jsonPoint2,
               cameraposition: jsonCameraPosition
            },
            timeout : 100000,
            success : function(data) {
               return ajaxControllerModule.calculateLineFoldDashesSuccess(handleData, data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
               return null;
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      sendUpdatedFaceColor: function (index, color) {
         let rgb = utilModule.html5ColorToRGB(color);
         $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "figure/faceColor",
            data : JSON.stringify({color: rgb, index: index}),
            dataType : 'json',
            timeout : 100000,
            success : function(data) {
               ajaxControllerModule.sendUpdatedFaceColorSuccess(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      },
      rotateFigure: function (angle) {
         $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "figure/rotate",
            data : JSON.stringify({angle: angle}),
            dataType : 'json',
            timeout : 100000,
            success : function(data) {
               ajaxControllerModule.rotateFigureSuccess(data);
            },
            error : function(e) {
               console.log("ERROR: ", e);
            },
            done : function() {
               console.log("DONE");
            }
         });
      }
   };
})();

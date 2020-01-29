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
/*globals BABYLON, scene, ajaxModule, pointDockingModule, sceneModule, drawingModule, origamiModule */

/**
 * This module is about either folding or creasing with ends (that is with points).
 *
 * Some functions in it are specific to creasing while others are to folding.
 */
var endsFoldingModule = (function () {

   let foldLineDataMap = null;

   let newFigureIsDisplayed = function(){
      endsFoldingModule.removeFoldData();
   };

   let initModule = function () {
      foldLineDataMap = new Map();
      foldLineDataMap.set("mountain", null);
      foldLineDataMap.set("valley", null);
      origamiModule.figure.registerListener(function() {
         newFigureIsDisplayed();
      });
   };

   initModule();

   return{
      /* Each fold point is an object like this
       {picked: {x:, y:, z:}, docked: {x:, y:, z:}, dockedTo: (side|vertex)}
       */
      foldPoints: [],
      stopAcceptingPoints: false,
      foldClick: function(pickedPoint){
         let dockedPoint = pointDockingModule.dockedPosition(pickedPoint);
         if(dockedPoint === null){
            return;
         }
         if(endsFoldingModule.stopAcceptingPoints){
            return;
         }
         drawingModule.drawDockFoldPointSphere(dockedPoint.position);
         let newFoldPoint = new Object();
         newFoldPoint.picked = pickedPoint;
         newFoldPoint.docked = dockedPoint.position;
         newFoldPoint.dockedTo = dockedPoint.dockedTo;
         endsFoldingModule.foldPoints.push(newFoldPoint);
      },
      removeFoldData: function(){
         endsFoldingModule.foldPoints = [];
         endsFoldingModule.stopAcceptingPoints = false;
         drawingModule.eraseFoldLine();
         drawingModule.eraseDockingElements();
      },
      /**
       * A fold segment defined from its two ends.
       */
      drawPointPairFoldSegment: function (foldDirection){
         ajaxModule.calculatePointPairLineSegmentDashes(foldDirection,
            endsFoldingModule.foldPoints[0].docked,
            endsFoldingModule.foldPoints[1].docked,
            sceneModule.getCameraPosition(), function(output){
               foldLineDataMap.set(foldDirection, output);
               drawingModule.drawFoldSegment(output.facingsidedashes, output.hiddensidedashes);
            });
      },
      drawPointPairCrease: function (layerFold) {
         ajaxModule.calculatePointPairCreaseSegment(endsFoldingModule.foldPoints[0].docked,
            endsFoldingModule.foldPoints[1].docked,
            layerFold,
            sceneModule.getCameraPosition());
      },
      storeOppositePointPairFoldLine: function (foldDirection) {
         let oppositeDirection = "mountain";
         if(foldDirection === "mountain"){
            oppositeDirection = "valley";
         }
         ajaxModule.calculatePointPairLineSegmentDashes(oppositeDirection,
            endsFoldingModule.foldPoints[0].docked,
            endsFoldingModule.foldPoints[1].docked,
            sceneModule.getCameraPosition(), function(output){
               foldLineDataMap.set(oppositeDirection, output);
            });
      },
      drawExistingFoldLine: function (foldDirection) {
         let foldLine = foldLineDataMap.get(foldDirection);
         if(foldLine === null ){
            return;
         }
         drawingModule.drawFoldSegment(foldLine.facingsidedashes, foldLine.hiddensidedashes);
      }
   };

})();

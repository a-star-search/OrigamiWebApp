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
/*globals BABYLON, scene, ajaxModule, edgeDockingModule, sceneModule, drawingModule, origamiModule */

/**
 * This module is about either folding or creasing with a pair of visibleEdges.
 *
 * Some functions in it are specific to creasing while others are to folding.
 */
var edgesFoldingModule = (function () {

   let newFigureIsDisplayed = function(){
      edgesFoldingModule.removeFoldData();
   };

   let initModule = function () {
      origamiModule.figure.registerListener(function() {
         newFigureIsDisplayed();
      });
   };
   initModule();

   return {
      // An edge is a pair of points
      foldEdges: [],
      stopAcceptingEdges: false,
      foldClick: function(pickedPoint){
         let dockedEdge = edgeDockingModule.dockedEdge(pickedPoint);
         if(dockedEdge === null){
            return;
         }
         if(edgesFoldingModule.stopAcceptingEdges){
            return;
         }
         drawingModule.drawDockFoldEdgeCylinder(dockedEdge);
         edgesFoldingModule.foldEdges.push(dockedEdge);
      },
      removeFoldData: function(){
         edgesFoldingModule.foldEdges = [];
         edgesFoldingModule.stopAcceptingEdges = false;
         drawingModule.eraseFoldLine();
         drawingModule.eraseDockingElements();
      },
      /**
       * A fold segment defined from two visibleEdges.
       */
      drawEdgePairFoldSegment: function (){
         console.log("todo: drawPointPairFoldSegment");
      },
      drawEdgePairCrease: function () {
         console.log("todo: drawPointPairFoldSegment");
      },
   };
})();

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
/*globals scene, home, origamiModule, geometryModule, drawingModule, guiControllerModule, $:false */

var ajaxControllerModule = (function () {
   return {
      baseLoadedSucess: function (data) {
         geometryModule.recalculateVisibleEdges();
      },
      squareLoadedSucess: function (data) {
         geometryModule.recalculateVisibleEdges();
      },
      sendLineFoldInformationSuccess: function (data) {
         if (data instanceof Object) {
            let folded = {flat: data.flat, threed: data.threed, edges: data.visibleEdges};

            let figure;
            if (data.creases == null) {
                figure = Figure.fromAjaxData(data.flat, data.threed, data.visibleEdges);
            } else {
               figure = Figure.fromAjaxData(data.flat, data.threed, data.visibleEdges, data.creases);
            }

            let translationSnapshots = data.translationsnapshots;
            let foldSnapshots = data.foldsnapshots;
            origamiModule.updateFigureWithSnapshots(folded, foldSnapshots, translationSnapshots);
            geometryModule.recalculateVisibleEdges();
         }
      },
      rotateFigureSuccess: function (data) {
         if (data instanceof Object) {
            let folded = {flat: data.flat, threed: data.threed, edges: data.visibleEdges};
            origamiModule.updateFigureNoSnapshots(folded);
            geometryModule.recalculateVisibleEdges();
         }
      },
      undoLastSuccess: function (data) {
         let previousFigure = {flat: data.flat, threed: data.threed, edges: data.visibleEdges};
         origamiModule.updateFigureNoSnapshots(previousFigure);
         //sometimes the previous figure had different colors
         // in other words, the change undone is a color change
         if (data.flat.colorTwo === null) {
            guiControllerModule.updateColorSelectors(data.flat.colorOne, data.flat.colorOne);
         } else {
            guiControllerModule.updateColorSelectors(data.flat.colorTwo, data.flat.colorOne);
         }
         geometryModule.recalculateVisibleEdges();
         let noHistory = data.nohistory == true;
         if (noHistory) {
            guiControllerModule.reset();
         }
      },
      resetFigureSuccess: function (data) {
      },
      recalculateVisibleEdgesSuccess: function (handleDataFunction, data) {
         let allEdges = data.all;
         let darkEdges = data.dark;
         let parsedAllEdges = JSON.parse(allEdges);
         let parsedDarkEdges = JSON.parse(darkEdges);
         handleDataFunction(parsedAllEdges, parsedDarkEdges);
         return null;
      },
      calculateLineFoldDashesSuccess: function (handleDataFunction, data) {
         if (data instanceof Object) {
            handleDataFunction(data);
         } else {
            console.log("ERROR: Line fold dashes data could not be parsed.");
         }
         return null;
      },
      calculatePointPairCreaseSegmentSuccess: function (data) {
         drawingModule.drawCreaseSegment(data);
      },
      makeCreaseSuccess: function (data) {
         console.log("todo: makeCreaseSuccess: to be implemented");
         console.log(data);
      },
      sendUpdatedFaceColorSuccess: function (data) {
         if (data instanceof Object) {
            origamiModule.figure.v = data;
            guiControllerModule.changeColorAlert();
            //after a color change recalculate for dark/clear visibleEdges too
            geometryModule.recalculateVisibleEdges();
         }
      },
      thereIsNoHistory: function (data) {
         let thereIsNoHistory = data.thereisnohistory;
         return thereIsNoHistory == true;
      }
   };
})();
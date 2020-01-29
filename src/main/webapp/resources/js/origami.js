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
/*globals scene, BABYLON, drawingModule, guidemarksModule, endsFoldingModule, ajaxModule, $:false */


var origamiModule = (function () {

   const DISPLAY_ENUM = {
      FLAT: {name: "flat"},
      THREED: {name: "threed"}
   };
   const FIGURE_LAPSE_TIME = 100;
   let baseMap = new Map();

   let squareFigure;
   let currentDisplay = DISPLAY_ENUM.FLAT;

   //async and await not supported yet by JSHint at time of writing, as they are new functionality (check again in the future)
   async function displayFigureWithSnapshots(foldSnapshots, translationSnapshots, FIGURE_LAPSE_TIME) { //jshint ignore:line
      for (let snap of foldSnapshots) {
         drawingModule.drawFigure(snap);
         await utilModule.sleep(FIGURE_LAPSE_TIME); //jshint ignore:line
      }
      for (let snap of translationSnapshots) {
         drawingModule.drawFigure(snap);
         await utilModule.sleep(FIGURE_LAPSE_TIME); //jshint ignore:line
      }
      drawingModule.drawFigure(origamiModule.figure.v.flat);
   }

   return {
      DISPLAY_ENUM: DISPLAY_ENUM,
      //the figure allows the registration of listeners for changes in the figure
      figure: {
         figureInternal: {flat: null, threed: null, edges: null},
         listeners: [],
         set v(newFigure) {
            this.figureInternal = newFigure;
            for(let listener of this.listeners){
               listener(newFigure);
            }
         },
         set square(squareFig) {
            this.figureInternal = squareFig;
            for(let listener of this.listeners){
               listener(squareFig);
            }
            squareFigure = squareFig;
         },
         get v() {
            return this.figureInternal;
         },
         reset: function(fig){
            this.v = fig;
         },
         registerListener: function(listener) {
            this.listeners.push(listener);
         }
      },
      keyInitialFigure: "square",
      addBase: function (key, edges, flat, threeD){
         baseMap.set(key, {flat: flat, threed: threeD, edges: edges});
      },
      loadBase: function (baseKey){
         origamiModule.keyInitialFigure = baseKey;
         origamiModule.figure.v = baseMap.get(baseKey);
         ajaxModule.baseLoaded(baseKey);
      },
      loadSquare: function (){
         origamiModule.keyInitialFigure = "square";
         origamiModule.figure.v = squareFigure;
         ajaxModule.squareLoaded();
      },
      reset: function (){
         if(origamiModule.keyInitialFigure === "square") {
            origamiModule.figure.v = squareFigure;
         } else {
            origamiModule.figure.v = baseMap.get(origamiModule.keyInitialFigure);
         }
      },
      thereIsNoHistory: function () {
         return ajaxModule.thereIsNoHistory();
      },
      //new figure is a map with keys figure, front, back and other info
      updateFigureWithSnapshots: function (folded, foldSnapshots, translationSnapshots){
         origamiModule.figure.v = folded;
         displayFigureWithSnapshots(foldSnapshots, translationSnapshots, FIGURE_LAPSE_TIME);
      },
      updateFigureNoSnapshots: function (folded){
         origamiModule.figure.v = folded;
         let figureToDisplay = origamiModule.getCurrentFigureToDisplay();
         drawingModule.drawFigure(figureToDisplay);
      },
      displayFlat(){
         currentDisplay = DISPLAY_ENUM.FLAT;
         drawingModule.drawFigure(origamiModule.figure.v.flat);
      },
      display3D(){
         currentDisplay = DISPLAY_ENUM.THREED;
         drawingModule.drawFigure(origamiModule.figure.v.threed);
      },
      getCurrentFigureToDisplay(){
         if(currentDisplay === DISPLAY_ENUM.FLAT){
            return origamiModule.figure.v.flat;
         }
         return origamiModule.figure.v.threed;
      },
      getCurrentDisplay(){
         return currentDisplay;
      }
   };

})();

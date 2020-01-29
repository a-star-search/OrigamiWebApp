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
/*globals BABYLON, scene, sceneModule, origamiModule, geometryModule, guidemarksModule, utilModule */
var edgeDockingModule = (function () {
   let dockingModeOn = true;
   var dockingCylinder = null;
   var dockedEdge = null;

   var createDockingCylinder = function (edge){
      edgeDockingModule.removeDockingData();
      dockedEdge = edge;
      dockingCylinder = utilModule.makeCylinder(edge);
      let darkEdges = geometryModule.getVisibleDarkEdges();
      let isADarkEdge = darkEdges.some(function(x) { return JSON.stringify(x) == JSON.stringify(edge); });
      if (isADarkEdge) {
         let ymat = new BABYLON.StandardMaterial("ym", scene);
         ymat.diffuseColor = new BABYLON.Color3(1, 1, 0.5);
         ymat.specularColor = new BABYLON.Color3(0, 0, 0);
         dockingCylinder.material = ymat;
      } else {
         let bmat = new BABYLON.StandardMaterial("bm", scene);
         bmat.diffuseColor = new BABYLON.Color3(0, 0, 0);
         bmat.specularColor = new BABYLON.Color3(0, 0, 0);
         dockingCylinder.material = bmat;
      }
      //sceneModule.excludeFromLights(dockingCylinder);
      dockingCylinder.setEnabled(0);
   };
   return {
      turnOn: function(){
         dockingModeOn = true;
      },
      turnOff: function(){
         edgeDockingModule.removeDockingData();
         dockingModeOn = false;
      },
      removeDockingData: function (){
         if(dockingCylinder !== null){
            dockingCylinder.dispose();
            dockingCylinder = null;
         }
      },
      updateDockedEdge: function (pickedPoint) {
         if (!dockingModeOn) {
            return null;
         }
         let closestEdge = geometryModule.closestEdge(pickedPoint);
         if(closestEdge === null){
            return null;
         }
         let closestPointInEdge = geometryModule.closestPointInEdge(pickedPoint, closestEdge);
         let distance = geometryModule.distanceSquared(pickedPoint, closestPointInEdge);
         if(distance > maxDockableDistance) {
            return null;
         }
         createDockingCylinder(closestEdge);
      },
      dockedEdge: function (pickedPoint) {
         edgeDockingModule.updateDockedEdge(pickedPoint);
         if(dockingCylinder !== null) {
            return dockedEdge;
         }
         return null;
      }
   };
})();
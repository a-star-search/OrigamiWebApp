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
/*globals BABYLON, scene, sceneModule, origamiModule, geometryModule, guidemarksModule */

var pointDockingModule = (function () {
   const DOCK_TO_ENUM = {
      SIDE: {name: "side"},
      VERTEX: {name: "vertex"}
   };

   let dockingModeOn = true;
   let dockToVertex = false;

   let lastDockedTo = DOCK_TO_ENUM.SIDE;

   var dockingSphere = null;

   var createDockingSphere = function (){
      if(dockingSphere === null){
         dockingSphere = BABYLON.Mesh.CreateSphere("dockingSphere", 16, dockingSphereDiameter, scene);

         let blackMat = new BABYLON.StandardMaterial("bm", scene);
         blackMat.diffuseColor = new BABYLON.Color3(0, 0, 0);
         dockingSphere.material = blackMat;
         sceneModule.excludeFromLights(dockingSphere);
         dockingSphere.setEnabled(0);
      }
   };

   return {
      DOCK_TO_ENUM: DOCK_TO_ENUM,
      turnOn: function(){
         dockingModeOn = true;
      },
      turnOff: function(){
         pointDockingModule.removeDockingData();
         dockingModeOn = false;
      },
      removeDockingData: function (){
         if(dockingSphere !== null){
            dockingSphere.dispose();
            dockingSphere = null;
         }
      },
      updateDockedPoint: function (pickedPoint){
         if(!dockingModeOn){
            return null;
         }
         let closestPoint = geometryModule.closestPointInEdges(pickedPoint);
         if(closestPoint === null){
            return null;
         }
         let distance = geometryModule.distanceSquared(pickedPoint, closestPoint);
         createDockingSphere();
         if(distance <= maxDockableDistance){
            guidemarksModule.showGuideMarks(pickedPoint);
            if(dockToVertex){
               let closestVertex = geometryModule.closestVertex(pickedPoint);
               let distanceToVertex = geometryModule.distanceSquared(pickedPoint, closestVertex);
               if(distanceToVertex <= maxDockableDistance) {
                  closestPoint = closestVertex;
                  lastDockedTo = pointDockingModule.DOCK_TO_ENUM.VERTEX;
               }
            } else {
               lastDockedTo = pointDockingModule.DOCK_TO_ENUM.SIDE;
            }
            dockingSphere.position = new BABYLON.Vector3(closestPoint.x, closestPoint.y, closestPoint.z);
            return closestPoint;
         }
         return null;
      },
      dockedPosition: function (pickedPoint){
         pointDockingModule.updateDockedPoint(pickedPoint);
         if(dockingSphere !== null){
            return {position: dockingSphere.position, dockedTo: lastDockedTo};
         }
         return null;
      },
      dockToVertex: function (state) {
         dockToVertex = state;
      }
   };
})();

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
/*globals BABYLON, scene, sceneModule */

var utilModule = (function () {

   /**
    * point parameters should be "BABYLON.Vector3"
    */
   let makeCylinder = function(startPoint, endPoint) {
      /*
      implementation adapted from
      http://www.html5gamedevs.com/topic/9015-cylinder-between-two-points/?page=2
      */
      let distance = BABYLON.Vector3.Distance(startPoint, endPoint );
      let c = BABYLON.Mesh.CreateCylinder("dockingCylinder", distance, dockingCylinderDiameter, dockingCylinderDiameter, 10, scene, true);
      // First of all we have to set the pivot not in the center of the cylinder:
      c.setPivotMatrix(BABYLON.Matrix.Translation(0, -distance / 2, 0), false);
      // Then move the cylinder to the end
      c.position = endPoint;

      // Then find the vector between ends
      let v1 = endPoint.subtract(startPoint);
      v1.normalize();
      let v2 = new BABYLON.Vector3(0, 1, 0);
      // Using cross we will have a vector perpendicular to both vectors
      let axis = BABYLON.Vector3.Cross(v2, v1);
      axis.normalize();
      // Angle between vectors
      let angle = BABYLON.Vector3.Dot(v1, v2);
      angle = Math.acos(angle);
      c.rotationQuaternion = BABYLON.Quaternion.RotationAxis(axis, angle);
      return c;
   };

   let componentToHex = function (c) {
      let hex = c.toString(16);
      return hex.length === 1 ? "0" + hex : hex;
   };

   return {
      sleep: function (ms) {
         return new Promise(resolve => setTimeout(resolve, ms));
      },
      //html color is a string like: #FF0000
      html5ColorToRGB(color) {
         let rPart = color.substr(1, 2);
         let gPart = color.substr(3, 2);
         let bPart = color.substr(5, 2);
         return {"red": parseInt(rPart, 16), "green": parseInt(gPart, 16), "blue": parseInt(bPart, 16)};
      },
      rgbColorToHex: function (color) {
         return "#" + componentToHex(color.red) + componentToHex(color.green) + componentToHex(color.blue);
      },
      makeCylinder: function (edge){
         let end1 = new BABYLON.Vector3(edge[0].x, edge[0].y, edge[0].z);
         let end2 = new BABYLON.Vector3(edge[1].x, edge[1].y, edge[1].z);
         if(edge[1].y < edge[0].y) {
            return makeCylinder(end2, end1);
         }
         return makeCylinder(end1, end2);
      },
      isDarkColor: function (color){
         return (color.red + color.green + color.blue) < 300;
      }
   };
})();


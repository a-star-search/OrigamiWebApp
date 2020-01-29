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
/*globals BABYLON, $:false, figureModule, origamiModule, guidemarksModule */

var sceneModule = (function () {

   let lights = [];
   let camera;

   let setCamera = function (aScene, canvas, cameraPosition){
      camera = new BABYLON.ArcRotateCamera("Camera", 0, 0, 100, BABYLON.Vector3.Zero(), aScene);
      camera.setPosition(new BABYLON.Vector3(cameraPosition.x, cameraPosition.y, cameraPosition.z));
      camera.wheelPrecision = 40;
      camera.attachControl(canvas, false);
      //this is how big do we allow the figure to be seen (how close can we zoom in with the mouse)
      camera.lowerRadiusLimit = 7;
      //this is how small do we allow the figure to be seen (how far can we zoom out with the mouse)
      camera.upperRadiusLimit = 10;
      camera.fov = 0.3;
   };

   return {
      getCamera: function () {
         return camera;
      },
      excludeFromLights: function (object) {
         for(let light of lights){
            light.excludedMeshes.push(object);
         }
      },
      createScene: function (canvas, engine, cameraPosition) {
         let s = new BABYLON.Scene(engine);
         s.clearColor = new BABYLON.Color3(1, 1, 1);
         setCamera(s, canvas, cameraPosition);
         // create a basic light, aiming 0,1,0 - meaning, to the sky
         let light = new BABYLON.HemisphericLight('light', new BABYLON.Vector3(0, 1, 0), s);
         //removes glare from the surface
         light.specular = new BABYLON.Color3(0,0,0);
         //white light
         light.diffuse = new BABYLON.Color3(1, 1, 1);
         //grayish from below
         light.groundColor = new BABYLON.Color3(0.5, 0.5, 0.5);
         lights.push(light);
         s.preventDefaultOnPointerDown = false;
         return s;
      },
      getCameraPosition: function () {
         return {x: camera.position.x, y: camera.position.y, z: camera.position.z};
      }
   };

})();

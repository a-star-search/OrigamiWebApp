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
/*globals BABYLON, scene, materialModule, origamiModule, utilModule */

var drawingModule = (function () {

   const DOCKING_ELEMENT_TAG = "dockingElement";
   const FOLD_LINE_COLOR = {red: 0, green: 0, blue: 0};
   const FOLD_LINE_MESH_TAG = "foldline";

   let meshPathsToRibbonGeometry = function (meshPathPair){
      let path1 = [];
      for(let coordsPath of meshPathPair.path1){
         path1.push(new BABYLON.Vector3(coordsPath.x, coordsPath.y, coordsPath.z));
      }
      let path2 = [];
      for(let coordsPath of meshPathPair.path2){
         path2.push(new BABYLON.Vector3(coordsPath.x, coordsPath.y, coordsPath.z));
      }
      return [path1, path2];
   };

   let displayRibbon = function (meshPaths, color, side, tag, isFigure){
      let initialRibbonGeometry = meshPathsToRibbonGeometry(meshPaths);
      let ribbon = BABYLON.Mesh.CreateRibbon("ribbon", initialRibbonGeometry, false, false, 0, scene, false, side);
      BABYLON.Tags.AddTagsTo(ribbon, tag);
      let babylonColor = new BABYLON.Color3(color.red/255.0, color.green/255.0, color.blue/255.0);
      if(isFigure){
         ribbon.material = materialModule.getFigureMaterial(babylonColor);
         ribbon.enableEdgesRendering(0.95);
         ribbon.edgesWidth = 1.0;
         if(utilModule.isDarkColor(color)){
            ribbon.edgesColor = new BABYLON.Color4(1, 1, 1, 1);
         } else {
            ribbon.edgesColor = new BABYLON.Color4(0, 0, 0, 1);
         }
      }else{
         ribbon.material = materialModule.getElementMaterial(babylonColor);
      }
      return ribbon;
   };

   let removeFigureMeshes = function (){
      for (let mesh of  scene.getMeshesByTags("figure")) {
         mesh.dispose();
      }
   };

   let displayRibbonCollection = function (ribbonCollection, ribbonColor, side, tag, isFigure){
      for (let ribbon of ribbonCollection) {
         displayRibbon(ribbon, ribbonColor, side, tag, isFigure);
      }
   };

   let newFigureIsDisplayed = function(){
      drawingModule.drawFigure(origamiModule.getCurrentFigureToDisplay());
   };

   let initModule = function () {
      origamiModule.figure.registerListener(function() {
         newFigureIsDisplayed();
      });
   };

   initModule();

   return {
      /*
       * A "figure" is the JavaScript version of Java's "RibbonFigure" object.
       */
      drawFigure: function (figure) {
         removeFigureMeshes();
         displayRibbonCollection(figure.facesColorOne, figure.colorOne, BABYLON.Mesh.FRONTSIDE, "figure", true);
         displayRibbonCollection(figure.facesColorTwo, figure.colorTwo, BABYLON.Mesh.FRONTSIDE, "figure", true);
      },
      drawFoldSegment: function (facingSideDashes, hiddenSideDashes) {
         drawingModule.eraseFoldLine();
         displayRibbonCollection(facingSideDashes, FOLD_LINE_COLOR, BABYLON.Mesh.DOUBLESIDE, FOLD_LINE_MESH_TAG, false);
         displayRibbonCollection(hiddenSideDashes, FOLD_LINE_COLOR, BABYLON.Mesh.DOUBLESIDE, FOLD_LINE_MESH_TAG, false);
      },
      drawCreaseSegment: function (data) {
         console.log("TODO: Drawing crease segment:");
         console.log(data);
      },
      drawDockFoldPointSphere: function(point) {
         let dockedFoldPointSphere = BABYLON.Mesh.CreateSphere("dockFoldPointSphere", 16, dockingSphereDiameter, scene);
         let blackMat = new BABYLON.StandardMaterial("bm", scene);
         blackMat.diffuseColor = new BABYLON.Color3(0, 0, 0);
         dockedFoldPointSphere.material = blackMat;
         dockedFoldPointSphere.position = new BABYLON.Vector3(point.x, point.y, point.z);
         BABYLON.Tags.AddTagsTo(dockedFoldPointSphere, DOCKING_ELEMENT_TAG);
      },
      drawDockFoldEdgeCylinder: function(edge) {
         let dockedFoldEdgeCylinder = utilModule.makeCylinder(edge);
         let blackMat = new BABYLON.StandardMaterial("bm", scene);
         blackMat.diffuseColor = new BABYLON.Color3(0, 0, 0);
         dockedFoldEdgeCylinder.material = blackMat;
         BABYLON.Tags.AddTagsTo(dockedFoldEdgeCylinder, DOCKING_ELEMENT_TAG);
      },
      eraseFoldLine: function(){
         for (let foldline of scene.getMeshesByTags(FOLD_LINE_MESH_TAG)){
            foldline.dispose();
         }
      },
      eraseDockingElements: function(){
         for (let elem of scene.getMeshesByTags(DOCKING_ELEMENT_TAG)){
            elem.dispose();
         }
      }
   };

})();

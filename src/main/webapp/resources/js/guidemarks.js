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
/*globals BABYLON, scene, geometryModule, sceneModule, origamiModule */

var guidemarksModule = (function () {
   const GUIDE_MARK_DIVISION_ENUM = {
      HALF: {name: "half", num: 2},
      THIRD: {name: "third", num: 3},
      QUARTER: {name: "quarter", num: 4},
      FIFTH: {name: "fifth", num: 5},
      EIGTH: {name: "eigth", num: 8}
   };
   const GUIDE_MARK_DIAMETER = 0.15;
   const GUIDE_MARK_WIDTH = 0.015;
   const GUIDE_MARK_MESH_TAG = "guidemark";

   let _guideMarksMap = null;
   let _guideMarkDivision = null;
   let guidemarksOn = false;
   let _currentEdge = null;
   let initGuideMarks = function (){
      let map = new Map();
      map.set(GUIDE_MARK_DIVISION_ENUM.HALF, new Map());
      map.set(GUIDE_MARK_DIVISION_ENUM.THIRD, new Map());
      map.set(GUIDE_MARK_DIVISION_ENUM.QUARTER, new Map());
      map.set(GUIDE_MARK_DIVISION_ENUM.FIFTH, new Map());
      map.set(GUIDE_MARK_DIVISION_ENUM.EIGTH, new Map());
      _guideMarksMap = map;
   };
   let alignCylinder = function (cylinder, edge){
      let startPoint = edge[0];
      let endPoint = edge[1];
      let vstart = new BABYLON.Vector3(startPoint.x, startPoint.y, startPoint.z);
      let vend = new BABYLON.Vector3(endPoint.x, endPoint.y, endPoint.z);
      let v1 = vstart.subtract(vend);
      v1.normalize();
      let v2 = new BABYLON.Vector3(0, 1, 0);
      let axis = BABYLON.Vector3.Cross(v2, v1);
      axis.normalize();
      let angle = BABYLON.Vector3.Dot(v1, v2);
      angle = Math.acos(angle);
      cylinder.rotationQuaternion = BABYLON.Quaternion.RotationAxis(axis, angle);
   };
   let removeGuideMarks = function () {
      for (let guideMarks of _guideMarksMap.values()) {
         guideMarks.clear();
      }
      for (let previousMark of scene.getMeshesByTags("guidemark")) {
         previousMark.dispose();
      }
   };
   let setGuideMarksVisibility = function (guideMarkDivision, edge, visible){
      let marks = _guideMarksMap.get(guideMarkDivision).get(edge);
      if(marks == null) {
         return;
      }
      for(let mark of marks){
         if(visible) {
            mark.setEnabled(1);
            scene.meshes.push(mark);
         } else {
            mark.setEnabled(0);
            scene.meshes.pop(mark);
         }
      }
   };
   let createSingleMark = function (point, edge) {
      let mark = BABYLON.MeshBuilder.CreateCylinder("guidemark", {
         diameterTop: GUIDE_MARK_DIAMETER,
         diameterBottom: GUIDE_MARK_DIAMETER,
         tessellation: 24,
         height: GUIDE_MARK_WIDTH }, scene);
      BABYLON.Tags.AddTagsTo(mark, GUIDE_MARK_MESH_TAG);
      alignCylinder(mark, edge);
      mark.position = point;
      let blackMat = new BABYLON.StandardMaterial("bm", scene);
      blackMat.diffuseColor = new BABYLON.Color3(0, 0, 0);
      blackMat.alpha = 0.6;
      mark.material = blackMat;
      //sceneModule.excludeFromLights(mark);
      mark.setEnabled(0);
      scene.meshes.pop(mark);
      return mark;
   };
   let createGuideMarksForEdge = function (edge, num){
      let points = geometryModule.calculateEdgeDivisionPoints(edge, num);
      let marks = [];

      for(let p of points) {
         marks.push(createSingleMark(p, edge));
      }
      return marks;
   };
   let hideGuideMarks = function (){
      for (let m of scene.getMeshesByTags(GUIDE_MARK_MESH_TAG)){
         m.setEnabled(0);
         scene.meshes.pop(m);
      }
   };
   let updateGuideMarks = function (){
      removeGuideMarks();
      for(let edge of origamiModule.figure.v.visibleEdges) {
         for(let strDivision in GUIDE_MARK_DIVISION_ENUM){
            if (GUIDE_MARK_DIVISION_ENUM.hasOwnProperty(strDivision)) {
               let division = GUIDE_MARK_DIVISION_ENUM[strDivision];
               _guideMarksMap.get(division).set(edge, createGuideMarksForEdge(edge, division.num) );
            }
         }
      }
   };
   let newFigureIsDisplayed = function(){
      removeGuideMarks();
   };
   let initModule = function () {
      initGuideMarks();
      origamiModule.figure.registerListener(function() {
         newFigureIsDisplayed();
      });
   };

   initModule();
   return {
      GUIDE_MARK_DIVISION_ENUM: GUIDE_MARK_DIVISION_ENUM,
      turnOn: function(){
         guidemarksOn = true;
      },
      turnOff: function(){
         guidemarksOn = false;
         hideGuideMarks();
      },
      showGuideMarks: function (pickedPoint){
         if(!guidemarksOn) {
            return;
         }
         hideGuideMarks();
         _currentEdge = geometryModule.closestEdge(pickedPoint);
         if(_currentEdge === null){
            return;
         }
         setGuideMarksVisibility(_guideMarkDivision, _currentEdge, true);
      },
      setDivisionMarkCount: function (guideMarkDivision){
         hideGuideMarks();
         _guideMarkDivision = guideMarkDivision;
      },
      makeGuideMarks: function (visibleEdges) {
         removeGuideMarks();
         for(let edge of visibleEdges) {
            for(let strDivision in GUIDE_MARK_DIVISION_ENUM){
               if (GUIDE_MARK_DIVISION_ENUM.hasOwnProperty(strDivision)) {
                  let division = GUIDE_MARK_DIVISION_ENUM[strDivision];
                  _guideMarksMap.get(division).set(edge, createGuideMarksForEdge(edge, division.num) );
               }
            }
         }
      }
   };
})();

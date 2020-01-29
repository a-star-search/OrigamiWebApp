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
/*globals sceneModule, guidemarksModule, ajaxModule */

var geometryModule = (function () {
   let closestPointCalculation = false;
   let visibleEdges = [];
   let visibleDarkEdges = [];
   let closestPointInEdge = function (point, edgeStart, edgeEnd) {
      let px = point.x;
      let py = point.y;
      let pz = point.z;
      let lx1 = edgeStart.x;
      let ly1 = edgeStart.y;
      let lz1 = edgeStart.z;
      let lx2 = edgeEnd.x;
      let ly2 = edgeEnd.y;
      let lz2 = edgeEnd.z;
      let line_dist = geometryModule.distanceSquared(edgeStart, edgeEnd);
      if (line_dist <= 0.00000001) {
         return {x: lx1, y: ly1, z: lz1};
      }
      let t = ((px - lx1) * (lx2 - lx1) + (py - ly1) * (ly2 - ly1) + (pz - lz1) * (lz2 - lz1)) / line_dist;
      if(t > 1) {
         t = 1;
      }
      if(t < 0) {
         t = 0;
      }
      return {x: lx1 + t * (lx2 - lx1), y: ly1 + t * (ly2 - ly1), z: lz1 + t * (lz2 - lz1)};
   };
   return {
      getVisibleEdges: function () {
         return visibleEdges;
      },
      getVisibleDarkEdges: function () {
         return visibleDarkEdges;
      },
      recalculateVisibleEdges: function () {
         let cameraPosition = sceneModule.getCameraPosition();
         closestPointCalculation = false;
         ajaxModule.recalculateVisibleEdges(cameraPosition,
            function(edges, dark){
               closestPointCalculation = true;
               visibleEdges = edges;
               visibleDarkEdges = dark;
               guidemarksModule.makeGuideMarks(visibleEdges);
            });
      },
      calculateEdgeDivisionPoints: function(edge, num){
         let startPoint = edge[0];
         let endPoint = edge[1];
         let result = [];
         for(let i = 1; i < num; i++){
            let point = {
               x: startPoint.x + ((endPoint.x - startPoint.x) * i / num),
               y: startPoint.y + ((endPoint.y - startPoint.y) * i / num),
               z: startPoint.z + ((endPoint.z - startPoint.z) * i / num) };
            result.push(point);
         }
         return result;
      },
      closestVertex: function(point){
         if(!closestPointCalculation){
            return null;
         }
         let closestPoint = visibleEdges[0][0];
         let closestDistance = geometryModule.distanceSquared(point, closestPoint);
         for(let i = 0; i < visibleEdges.length; i++){
            let distanceEnd1 =  geometryModule.distanceSquared(point, visibleEdges[i][0]);
            if(distanceEnd1 < closestDistance){
               closestPoint = visibleEdges[i][0];
               closestDistance = distanceEnd1;
            }
            let distanceEnd2 =  geometryModule.distanceSquared(point, visibleEdges[i][1]);
            if(distanceEnd2 < closestDistance){
               closestPoint = visibleEdges[i][1];
               closestDistance = distanceEnd2;
            }
         }
         return closestPoint;
      },
      closestPointInEdges: function(point){
         if(!closestPointCalculation){
            return null;
         }
         let closestPoint = closestPointInEdge(point, visibleEdges[0][0], visibleEdges[0][1]);
         let closestDistance = geometryModule.distanceSquared(point, closestPoint);
         for(let i = 1; i < visibleEdges.length; i++){
            let pointInEdge = closestPointInEdge(point, visibleEdges[i][0], visibleEdges[i][1]);
            let distance =  geometryModule.distanceSquared(point, pointInEdge);
            if(distance < closestDistance){
               closestPoint = pointInEdge;
               closestDistance = distance;
            }
         }
         return closestPoint;
      },
      closestEdge: function(point){
         if(!closestPointCalculation){
            return null;
         }
         let closestEdge = visibleEdges[0];
         let smallestDistance = geometryModule.distanceSquared(point, closestPointInEdge(point, closestEdge[0], closestEdge[1]));
         for(let i = 1; i < visibleEdges.length; i++){
            let currentEdge = visibleEdges[i];
            let distance =  geometryModule.distanceSquared(point, closestPointInEdge(point, currentEdge[0], currentEdge[1]));
            if(distance < smallestDistance){
               closestEdge = currentEdge;
               smallestDistance = distance;
            }
         }
         return closestEdge;
      },
      closestPointInEdge: function (point, edge) {
         return closestPointInEdge(point, edge[0], edge[1]);
      },
      distance: function(pointA, pointB){
         return Math.sqrt(geometryModule.distanceSquared(pointA, pointB));
      },
      distanceSquared: function(pointA, pointB){
         return geometryModule.sqr(pointA.x - pointB.x) + geometryModule.sqr(pointA.y - pointB.y) + geometryModule.sqr(pointA.z - pointB.z);
      },
      sqr: function(x) {
         return x * x;
      }
   };

})();



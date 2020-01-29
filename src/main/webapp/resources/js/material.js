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
/*globals scene, BABYLON */

var materialModule = (function () {
   let figureMaterialCache = new Map();
   let elementMaterialCache = new Map();

   let createFigureMaterial = function (color) {
      let ribbonmat = new BABYLON.StandardMaterial("someMaterial", scene);
      ribbonmat.diffuseTexture = new BABYLON.Texture("resources/img/whitepapertex.jpg", scene);
      ribbonmat.alpha = 1.0;
      ribbonmat.diffuseColor = color;
      ribbonmat.backFaceCulling = true;
      ribbonmat.wireframe = false;
      return ribbonmat;
   };

   let createElementMaterial = function (color) {
      let ribbonmat = new BABYLON.StandardMaterial("someMaterial", scene);
      ribbonmat.alpha = 1.0;
      ribbonmat.diffuseColor = color;
      ribbonmat.backFaceCulling = true;
      ribbonmat.wireframe = false;
      return ribbonmat;
   };

   let addToMaterialCache = function (color) {
      figureMaterialCache.set(color, createFigureMaterial(color));
   };

   let addToElementMaterialCache = function (color) {
      elementMaterialCache.set(color, createElementMaterial(color));
   };

   //public methods
   return {
      getFigureMaterial: function (color) {
         if (figureMaterialCache.get(color) === undefined){
            addToMaterialCache(color);
         }
         return figureMaterialCache.get(color);
      },
      //elements of the gui, other than the figure
      getElementMaterial: function (color) {
         if (elementMaterialCache.get(color) === undefined){
            addToElementMaterialCache(color);
         }
         return elementMaterialCache.get(color);
      }
   };

})();



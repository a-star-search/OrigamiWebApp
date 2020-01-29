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
/*globals $:false */

var knobHooksModule = (function () {

   return {
      value: 180,
      change: function(value){
         //console.log("change : " + value);
      },
      release: function(value){
         //console.log("release : " + value);
         knobHooksModule.value = value;
         if(value < 10){
            value = 10;
            knobHooksModule.value = 10;
            $('.knob').val(10).trigger('change');
            return false;
         }
      },
      draw: function (knob) {
         // "tron" case
         if (knob.$.data('skin') === 'tron') {

            knob.cursorExt = 0.3;

            let a = knob.arc(knob.cv);  // Arc
            let pa;                   // Previous arc
            let r = 1;

            knob.g.lineWidth = knob.lineWidth;

            if (knob.o.displayPrevious) {
               pa = knob.arc(knob.v);
               knob.g.beginPath();
               knob.g.strokeStyle = knob.pColor;
               knob.g.arc(knob.xy, knob.xy, knob.radius - knob.lineWidth, pa.s, pa.e, pa.d);
               knob.g.stroke();
            }

            knob.g.beginPath();
            knob.g.strokeStyle = r ? knob.o.fgColor : knob.fgColor;
            knob.g.arc(knob.xy, knob.xy, knob.radius - knob.lineWidth, a.s, a.e, a.d);
            knob.g.stroke();

            knob.g.lineWidth = 2;
            knob.g.beginPath();
            knob.g.strokeStyle = knob.o.fgColor;
            knob.g.arc(knob.xy, knob.xy, knob.radius - knob.lineWidth + 1 + knob.lineWidth * 2 / 3, 0, 2 * Math.PI, false);
            knob.g.stroke();

            return false;
         }
      }
   };
})();

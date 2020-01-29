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

package com.moduleforge.origami.webapp.service.folding.guifoldline;

/**
 * The fold type belongs in the front end project. The 'origami' library uses a more geometrical definition (side
 * positive or negative with respect to the polygons normal)
 * to fold polygons
 * but ultimately both definitions are equivalent
 */
public enum LineFoldType {
   MOUNTAIN("mountain") {
      @Override
      public LineFoldType opposite() {
         return VALLEY;
      }
   },
   VALLEY("valley"){
      @Override
      public LineFoldType opposite() {
         return MOUNTAIN;
      }
   };

   private final String name;
   LineFoldType(String name){
      this.name = name;
   }
   public abstract LineFoldType opposite();

   public static LineFoldType fromName(String name) {
      if(MOUNTAIN.name.equals(name))
         return MOUNTAIN;
      else if (VALLEY.name.equals(name))
         return VALLEY;
      return null;
   }
}

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

package com.moduleforge.origami.webapp.guidata.output;

import com.moduleforge.libraries.geometry._3d.Point;

/**
 * This holds the information of the picked edge in the GUI:
 * the picked point and the two ends of the edge.
 *
 * It can be used as output of a fold or of a crease.
 *
 * It can also be a crease that is being picked; it is not necessarily an actual edge of the figure. Note that
 * a fold or a new crease can be defined from an edge or from an existing crease (that is the point of having
 * creases after all!).
 *
 * Naturally the points of this edge are the actual positions of the flat figure, not the usually slightly shifted ones
 * used for rendering.
 *
 * There is a difference with the class that holds a point (in which the point is supposed to be docked to an edge):
 * In that class we are interested in a point and the closest point to it that is contained in any edge.
 * In this case we are trying to pick an edge. The difference is more semantical that the actual information
 * being hold by these classes but it grants separate classes.
 */
public class FoldEdge {
   public enum EdgeType {
      EDGE("edge"),
      CREASE("crease");
      String name;
      EdgeType(String name) {
         this.name = name;
      }
      public static EdgeType byName(String name) {
         for(EdgeType et : EdgeType.values())
            if(et.name.equalsIgnoreCase(name))
               return et;
         throw new RuntimeException("No edge type matches this name.");
      }
   }
   public Point pickedPoint;
   public Point edgeEnd1;
   public Point edgeEnd2;
   /**
    * Edge or crease.
    */
   public EdgeType type;
   public FoldEdge(Point pickedPoint, Point edgeEnd1, Point edgeEnd2, EdgeType type) {
      this.pickedPoint = pickedPoint;
      this.edgeEnd1 = edgeEnd1;
      this.edgeEnd2 = edgeEnd2;
      this.type = type;
   }
}

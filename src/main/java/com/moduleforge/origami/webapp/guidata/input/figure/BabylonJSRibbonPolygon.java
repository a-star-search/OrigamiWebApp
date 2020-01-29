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

package com.moduleforge.origami.webapp.guidata.input.figure;

import com.moduleforge.libraries.geometry._3d.Point;
import com.whitebeluga.origami.figure.component.Face;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * A ribbon to be used by babylonjs.
 */
public class BabylonJSRibbonPolygon {
   public List<Point> perimeter;
   public List<Point> path1;
   public List<Point> path2;

   private BabylonJSRibbonPolygon() {
   }
   public BabylonJSRibbonPolygon(Face polygon) {
      this(polygon.getVertices());
   }
   public BabylonJSRibbonPolygon(List<? extends Point> points) {
      this();
      perimeter = new ArrayList<>(points);
      int indexHalfList = points.size() / 2;
      boolean oddLength = points.size() % 2 == 1;
      if(oddLength) {
         indexHalfList++;
      }
      List<Point> secondHalf = new ArrayList<>(points.subList(indexHalfList, points.size()));
      Collections.reverse(secondHalf);
      if(oddLength) {
         Point lastPoint = secondHalf.get(secondHalf.size() - 1);
         secondHalf.add(lastPoint); // repeat the last point (babylon js needs equal length paths)
      }
      path2 = secondHalf;
      //copy the first part of the list
      path1 = new ArrayList<>();
      List<? extends Point> firstHalf = points.subList(0, indexHalfList);
      path1.addAll(firstHalf);
   }
}

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

import com.moduleforge.libraries.geometry._3d.Point;
import org.javatuples.Pair;

/**
 * A fold segment (line with no width) is transformed into a rectangle
 *
 * Each of the ends of the fold segment become a pair of boundary, that pair of boundary is represented by
 * this class
 */
public class WideSegmentEnd {
   public Pair<Point, Point> points;
   WideSegmentEnd(Point p1, Point p2){
      this.points = new Pair<>(p1, p2);
   }
   WideSegmentEnd(Pair<Point, Point> points){
      this.points = points;
   }
}
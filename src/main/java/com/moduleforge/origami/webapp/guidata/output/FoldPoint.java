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
import com.moduleforge.origami.webapp.service.DockedTo;

/**
 * Contains the point as picked by the user and the docked point, which is the actual point that will be used
 * since folds are made between any combination of point on an edge and vertex.
 *
 * The docked point must be always present.
 */
public class FoldPoint {
   public Point pickedPoint;
   public Point dockedPoint;
   public DockedTo dockedTo;

   public FoldPoint(Point pickedPoint, Point dockedPoint, DockedTo dockedTo) {
      this.pickedPoint = pickedPoint;
      this.dockedPoint = dockedPoint;
      this.dockedTo = dockedTo;
   }
}

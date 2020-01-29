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

package com.moduleforge.origami.webapp.guidata.output.linefold;

import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.origami.webapp.guidata.output.FoldPoint;
import com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;

/**
 * The parameter set for a line fold defined from the two ends of the fold segment
 */
public class PointPairLineFoldParameterSet extends LineFoldParameterSet {
   public FoldPoint foldPoint1;
   public FoldPoint foldPoint2;

   public PointPairLineFoldParameterSet(FoldPoint foldPoint1, FoldPoint foldPoint2, int angle, Point cameraPosition,
                                        LineFoldType foldType, FlapsToFold flapsToFold) {
      super(angle, cameraPosition, foldType, flapsToFold);
      this.foldPoint1 = foldPoint1;
      this.foldPoint2 = foldPoint2;
   }
}

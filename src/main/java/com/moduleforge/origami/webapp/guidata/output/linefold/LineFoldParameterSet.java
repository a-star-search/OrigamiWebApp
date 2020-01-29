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
import com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;

class LineFoldParameterSet {
   public int angle;
   public Point cameraPosition;
   public LineFoldType foldType;
   public FlapsToFold flapsToFold;

   protected LineFoldParameterSet(int angle, Point cameraPosition, LineFoldType foldType, FlapsToFold flapsToFold) {
      this.angle = angle;
      this.cameraPosition = cameraPosition;
      this.foldType = foldType;
      this.flapsToFold = flapsToFold;
   }
}

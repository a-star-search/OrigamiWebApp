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

/*
 * Copyright (c) 2018.  This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */

package com.moduleforge.origami.webapp.guidata.output.crease;

import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.origami.webapp.guidata.output.FoldEdge;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;

/**
 * In this case the crease has been defined in the GUI by using two visibleEdges.
 *
 * The crease to be created should, logically, be the middle line and both visibleEdges should either be parallel
 *
 * or they should share a common vertex (not the same object necessarily, but vertices with the same position.
 *
 * Of course either (or both) of this visibleEdges can actually be creases.
 */
public class EdgePairParameterSet extends CreaseParameterSet {
   public FoldEdge foldEdge1;
   public FoldEdge foldEdge2;

   public EdgePairParameterSet(FoldEdge foldEdge1, FoldEdge foldEdge2, Point cameraPosition, FlapsToFold flapsToFold) {
      super(cameraPosition, flapsToFold);
      this.foldEdge1 = foldEdge1;
      this.foldEdge2 = foldEdge2;
   }
}

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

import java.util.List;

/**
 * It's actually a *set of pairs of points* but passed as a list of lists of points for easiness of processing in the
 * front end.
 *
 * What each list of points is:
 *
 * As explained above, each list of points should be composed of exactly two points. These two points specify a
 * segment that may or may not span the area of *a single face*.
 *
 * It may not for a perfectly flat rendering, but for
 * a layered rendering it makes sense that a single crease segment is split into smaller segments that follow the
 * profile of the layered surface of a bundle (hopefully this explanation is clear enough).
 *
 * As you can imagine, there may be a segment, or sets of segments for each side of the bundle.
 *
 * That's not always the case though. Imagine you intend to make a crease on a flap and that flap is hidden by other
 * layers on one of the sides, then there are no crease segments to display on that side.
 *
 * To be clear, these segments are only for rendering, for display. They are not the actual crease coordinates of the
 * flat figure.
 *
 * Those coordinates also need to be passed to the front end. They are necessary to create other creases or folds, but
 * they do not belong in this class.
 *
 * Notice that all the segments are lumped together into a single list of lists.
 */
public class BabylonJSCreaseSet {
   public List<List<Point>> creases;

   public BabylonJSCreaseSet(List<List<Point>> creases) {
      this.creases = creases;
   }
}

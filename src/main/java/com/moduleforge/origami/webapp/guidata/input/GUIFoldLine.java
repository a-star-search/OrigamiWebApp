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


package com.moduleforge.origami.webapp.guidata.input;

import com.moduleforge.origami.webapp.guidata.input.figure.BabylonJSRibbonPolygon;

import java.util.List;

public class GUIFoldLine {
   public List<BabylonJSRibbonPolygon> visibleSideRibbons;
   public List<BabylonJSRibbonPolygon> hiddenSideRibbons;
   public GUIFoldLine(List<BabylonJSRibbonPolygon> visibleSideRibbons, List<BabylonJSRibbonPolygon> hiddenSideRibbons){
      this.visibleSideRibbons = visibleSideRibbons;
      this.hiddenSideRibbons = hiddenSideRibbons;
   }
}

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

import com.whitebeluga.origami.rendering.JSDisplayableFigure;
import com.whitebeluga.origami.rendering.OneSidedPolygon;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BabylonJSRibbonFigure {
   private final Set<BabylonJSRibbonPolygon> facesColorOne;
   private final Set<BabylonJSRibbonPolygon> facesColorTwo;
   private Color colorOne;
   private Color colorTwo;
   
   private BabylonJSRibbonFigure() {
      facesColorOne = new HashSet<>();
      facesColorTwo = new HashSet<>();
   }
   BabylonJSRibbonFigure(JSDisplayableFigure figure) {
      this();
      this.colorOne = figure.getColorOne();
      this.colorTwo = figure.getColorTwo();
      Set<OneSidedPolygon> ospFacesColorOne = figure.getFacesColorOne();
      Set<OneSidedPolygon> ospFacesColorTwo = figure.getFacesColorTwo();
      for(OneSidedPolygon pol : ospFacesColorOne) 
         facesColorOne.add(new BabylonJSRibbonPolygon(pol.getBoundary()));
      for(OneSidedPolygon pol : ospFacesColorTwo)
         facesColorTwo.add(new BabylonJSRibbonPolygon(pol.getBoundary()));
   }
   public Set<BabylonJSRibbonPolygon> getFacesColorOne() {
      return facesColorOne;
   }
   public Set<BabylonJSRibbonPolygon> getFacesColorTwo() {
      return facesColorTwo;
   }
   public Color getColorOne() {
      return colorOne;
   }
   public Color getColorTwo() {
      return colorTwo;
   }
}

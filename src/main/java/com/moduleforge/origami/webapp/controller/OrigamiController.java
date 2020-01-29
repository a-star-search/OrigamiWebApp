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

package com.moduleforge.origami.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.origami.webapp.controller.implementation.OrigamiControllerCreasingImplementation;
import com.moduleforge.origami.webapp.controller.implementation.OrigamiControllerFoldingImplementation;
import com.moduleforge.origami.webapp.controller.implementation.OrigamiControllerOtherImplementation;
import com.moduleforge.origami.webapp.guidata.input.figure.FigureData;
import com.moduleforge.origami.webapp.guidata.output.FoldEdge;
import com.moduleforge.origami.webapp.guidata.output.FoldPoint;
import com.moduleforge.origami.webapp.service.OrigamiManager;
import com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType;
import com.whitebeluga.origami.figure.Figure;
import com.whitebeluga.origami.figure.OrigamiBase;
import org.javatuples.Pair;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.moduleforge.origami.webapp.controller.OrigamiControllerUtil.*;
import static com.moduleforge.origami.webapp.service.folding.guifoldline.LineFoldType.fromName;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Thin class that is the communication point between front and back end.
 * All it does is read the parameters from the JSON object and defer to the implementation.
 */
@ImportResource(value = {"classpath:spring/beans.xml"})
@Configuration
@Controller
@Scope("session")
public class OrigamiController {
   private static final String INDEX_JSP = "index";
   @Resource(name="squarePaper")
   private Figure squarePaper;
   @Resource(name="figureDataBaseMap")
   private Map<OrigamiBase, FigureData> baseFigureDataMap;
   @Resource(name="baseMap")
   private Map<OrigamiBase, Figure> baseFigureMap;
   @Resource(name="cameraPosition")
   private Point cameraPosition;
   @Resource(name="paperSideLength")
   private Double paperSideLength;
   private OrigamiManager manager = null;
   private OrigamiControllerFoldingImplementation foldingImplementation;
   private OrigamiControllerCreasingImplementation creasingImplementation;
   private OrigamiControllerOtherImplementation otherImplementation;

   @RequestMapping(value = "/", method = GET)
   public ModelAndView index() throws JsonProcessingException {
      if(manager == null) {
         manager = new OrigamiManager(squarePaper, baseFigureMap, paperSideLength);
         foldingImplementation = new OrigamiControllerFoldingImplementation(manager);
         creasingImplementation = new OrigamiControllerCreasingImplementation(manager);
         otherImplementation = new OrigamiControllerOtherImplementation(manager);
      }
      ModelAndView model = new ModelAndView(INDEX_JSP);
      FigureData data = otherImplementation.getFigureData();
      model.addObject("visibleEdges", asString(data.getSegments()));
      model.addObject("figure", asString(data.getFlatFigure()));
      for(Entry<String, String> entry: makeOrigamiBaseMap().entrySet())
         model.addObject(entry.getKey(), entry.getValue());
      model.addObject("cameraposition", asString(cameraPosition));
      model.addObject("papersidelength", asString(paperSideLength));
      return model;
   }
   /**
    * For each origami base there are three map entries, one for the visibleEdges of the flat figure,
    * another for the flat figure itself and a third for the 3d figure.
    */
   private Map<String, String> makeOrigamiBaseMap() throws JsonProcessingException {
      Map<String, String> map = new HashMap<>();
      for(Entry<OrigamiBase, FigureData> mapEntry : baseFigureDataMap.entrySet()){
         String keyPrefix = mapEntry.getKey().toString();
         FigureData data = mapEntry.getValue();
         map.put(keyPrefix + "visibleEdges", asString(data.getSegments()));
         map.put(keyPrefix + "flat", asString(data.getFlatFigure()));
         map.put(keyPrefix + "threeD", asString(data.getThreeDFigure()));
      }
      return map;
   }
   @ResponseBody
   @RequestMapping(value = "/session/reset", method = RequestMethod.PUT)
   public String reset() {
      return otherImplementation.reset();
   }
   @ResponseBody
   @RequestMapping(value = "/figure/undolast", method = RequestMethod.PUT)
   public Map<String, Object> undoLastStep() {
      return otherImplementation.undoLastStep();
   }
   @ResponseBody
   @RequestMapping(value = "/figure/baseLoaded", method = RequestMethod.PUT)
   public String baseLoaded(@RequestBody ObjectNode jsonData ) {
      String baseName = jsonData.get("baseName").textValue();
      return otherImplementation.baseLoaded(baseName);
   }
   @ResponseBody
   @RequestMapping(value = "/figure/squareLoaded", method = RequestMethod.PUT)
   public String squareLoaded() {
      return otherImplementation.squareLoaded();
   }
   @ResponseBody
   @RequestMapping(value = "/figure/thereisnohistory", method = GET)
   public Map<String, Object> thereIsNoHistory() {
      return otherImplementation.thereIsNoHistory();
   }
   @ResponseBody
   @RequestMapping(value = "/figure/faceColor", method = POST)
   public Map<String, Object> updateFaceColor(@RequestBody ObjectNode jsonData) throws JsonProcessingException {
      Color color = extractColor(jsonData);
      int index = jsonData.get("index").intValue();
      return otherImplementation.updateFaceColor(color, index);
   }
   @ResponseBody
   @RequestMapping(value = "/figure/rotate", method = POST)
   public Map<String, Object> rotateFigure(@RequestBody ObjectNode jsonData) throws JsonProcessingException {
      double angle = jsonData.get("angle").doubleValue();
      return otherImplementation.rotateFigure(angle);
   }
   //--- GUI
   /**
    * Returns the visible visibleEdges and visible crease marks.
    *
    * Note that the visibleEdges returned are the ones of the original, perfectly flat figure. Not the pseudo flat or layered
    * one!
    *
    * However the visibleEdges returned should be very close to that of the layered rendered figure.
    *
    * The visibleEdges are passed as a string. Why is the parameter a map of string to string?
    * There should be actually two sets of visibleEdges. One, with the key "all" associated to a value that holds all visibleEdges.
    * Another is the set of visibleEdges that belongs to dark faces!
    * If we need to display any graphical element on an edge, the color of that element should be different when the
    * face is dark than when the face is lightly colored in order to be distinguished.
    */
   @ResponseBody
   @RequestMapping(value = "/gui/recalculateVisibleEdges", method = GET)
   public Map<String, String> recalculateVisibleEdges(@RequestParam("cameraposition") String jsonCameraPosition) throws IOException {
      Point cameraPosition = readPoint(jsonCameraPosition);
      return otherImplementation.recalculateVisibleEdges(cameraPosition);
   }
   //--- Creasing
   @ResponseBody
   @RequestMapping(value = "/crease", method = POST)
   public Map<String, Object> makeCrease(@RequestParam("point1") String jsonPoint1,
                                         @RequestParam("point2") String jsonPoint2,
                                         @RequestParam("cameraposition") String jsonCameraPosition,
                                         @RequestParam("layerfold") String layerFold) throws IOException {
      Point p1 = readPoint(jsonPoint1);
      Point p2 = readPoint(jsonPoint2);
      Point cameraPosition = readPoint(jsonCameraPosition);
      boolean isAMAPFold = isAMAPFold(layerFold);
      return creasingImplementation.makeCrease(p1, p2, cameraPosition, isAMAPFold);
   }
   /**
    * This function is equivalent to the one that makes a fold line to be displayed.
    *
    * It makes a crease to be displayed on the GUI.
    *
    * When making a crease, because the crease is not made yet (confirmed), return the crease information
    * instead of a figure with the new crease.
    *
    * Why is it necessary to return the crease info at all, even if, unlike the fancy "fold" line made of dashes,
    * the simple crease is only a line segment made from a pair of points?
    *
    * For two reasons (given that making a new crease is a two step operation, show it first, then confirm):
    * 1) We might want fancy crease lines in the future, that, naturally, will be calculated in the backend.
    * 2) The segment ends might have to be calculated after all!! Bear in mind that a crease can be made from two
    * end points or it can be made from a vertex, an edge and an angle, or from two visibleEdges.
    *
    * Like folds, with creases there is also the first layer / all layers option.
    *
    * Returns:
    *
    * Because the crease could be seen from both sides and because the rendering is not in a perfect plane and it
    * could also be layered, the lines have to follow the faces in different layers.
    *
    * What that means is that, in the general case, the result returned is a list (a list for simplicity of processing
    * in the front end, it should be a set) of any number of segments
    * (pairs of points) that are very close to each of the creased faces.
    *
    * As with the fold line, just far enough so that the line will be displayed and not clashing with the face.
    *
    */
   @ResponseBody
   @RequestMapping(value = "/gui/crease/points", method = POST)
   public Map<String, Object> makeGUICreaseFromPoints(@RequestParam("point1") String jsonPoint1,
                                                      @RequestParam("point2") String jsonPoint2,
                                                      @RequestParam("cameraposition") String jsonCameraPosition,
                                                      @RequestParam("layerfold") String layerFold) throws IOException {
      Point p1 = readPoint(jsonPoint1);
      Point p2 = readPoint(jsonPoint2);
      Point cameraPosition = readPoint(jsonCameraPosition);
      boolean isAMAPFold = isAMAPFold(layerFold);
      return creasingImplementation.makeGUICreaseFromPoints(p1, p2, cameraPosition, isAMAPFold);
   }
   /**
    * Equivalent to the one that creates a crease from points but in this case the input is a couple of visibleEdges.
    */
   @ResponseBody
   @RequestMapping(value = "/gui/crease/visibleEdges", method = POST)
   public Map<String, Object> makeGUICreaseFromEdges(@RequestParam("point1Edge1") String jsonPoint11,
                                                     @RequestParam("point2Edge1") String jsonPoint12,
                                                     /*
                                                      Either "edge" or "crease".
                                                      A new crease can be defined from two visibleEdges where
                                                      one or both can actually be creases (instead of proper visibleEdges).

                                                      The same applies to the other edge type parameter.
                                                      */
                                                     @RequestParam("typeOfEdge1") String typeOfEdge1,
                                                     @RequestParam("point1Edge2") String jsonPoint21,
                                                     @RequestParam("point2Edge2") String jsonPoint22,
                                                     @RequestParam("typeOfEdge2") String typeOfEdge2,
                                                     @RequestParam("cameraposition") String jsonCameraPosition,
                                                     @RequestParam("layerfold") String layerFold) throws IOException {
      Point p11 = readPoint(jsonPoint11);
      Point p12 = readPoint(jsonPoint12);
      FoldEdge.EdgeType type1 = FoldEdge.EdgeType.byName(typeOfEdge1);
      FoldEdge fe1 = new FoldEdge(null, p11, p12, type1);
      Point p21 = readPoint(jsonPoint21);
      Point p22 = readPoint(jsonPoint22);
      FoldEdge.EdgeType type2 = FoldEdge.EdgeType.byName(typeOfEdge2);
      FoldEdge fe2 = new FoldEdge(null, p21, p22, type2);
      Point cameraPosition = readPoint(jsonCameraPosition);
      boolean isAMAPFold = isAMAPFold(layerFold);
      return creasingImplementation.makeGUICreaseFromEdges(fe1, fe2, cameraPosition, isAMAPFold);
   }
   //--- Folding
   @ResponseBody
   @RequestMapping(value = "/fold/line", method = POST)
   public Map<String, Object> makeFold(@RequestBody ObjectNode jsonData) throws JsonProcessingException {
      Pair<FoldPoint, FoldPoint> foldPoints = extractFoldPoints(jsonData);
      Point cameraPosition = extractPoint(jsonData, "cameraposition");
      String sFoldDirection = jsonData.get("folddirection").textValue();
      LineFoldType foldType = fromName(sFoldDirection);
      int angle = jsonData.get("angle").intValue();
      String layerFold = jsonData.get("layerfold").textValue();
      boolean isAMAPFold = isAMAPFold(layerFold);
      return foldingImplementation.makeFold(foldPoints.getValue0(), foldPoints.getValue1(), cameraPosition, foldType, angle, isAMAPFold);
   }
   /**
    * A pair of fancy straight fold lines made of dashes (one for the valley side, another for the mountain)
    */
   @ResponseBody
   @RequestMapping(value = "/gui/foldline/points", method = GET)
   public Map<String, Object> makeGUIFoldLineFromPoints(@RequestParam("folddirection") String jsonFoldDirection,
                                                        @RequestParam("point1") String jsonPoint1,
                                                        @RequestParam("point2") String jsonPoint2,
                                                        @RequestParam("cameraposition") String jsonCameraPosition) throws IOException {
      Point p1 = readPoint(jsonPoint1);
      Point p2 = readPoint(jsonPoint2);
      Point cameraPosition = readPoint(jsonCameraPosition);
      LineFoldType foldDirection = fromName(jsonFoldDirection);
      return foldingImplementation.makeGUIFoldLinePoints(foldDirection, p1, p2, cameraPosition);
   }
   /**
    * A pair of fancy straight fold lines made of dashes (one for the valley side, another for the mountain)
    */
   @ResponseBody
   @RequestMapping(value = "/gui/foldline/visibleEdges", method = GET)
   public Map<String, Object> makeGUIFoldLineFromEdges(@RequestParam("folddirection") String jsonFoldDirection,
                                                       @RequestParam("point1Edge1") String jsonPoint11,
                                                       @RequestParam("point2Edge1") String jsonPoint12,
                                                     /*
                                                      Either "edge" or "crease".
                                                      A new crease can be defined from two visibleEdges where
                                                      one or both can actually be creases (instead of proper visibleEdges).

                                                      The same applies to the other edge type parameter.
                                                      */
                                                       @RequestParam("typeOfEdge1") String typeOfEdge1,
                                                       @RequestParam("point1Edge2") String jsonPoint21,
                                                       @RequestParam("point2Edge2") String jsonPoint22,
                                                       @RequestParam("typeOfEdge2") String typeOfEdge2,
                                                       @RequestParam("cameraposition") String jsonCameraPosition) throws IOException {
      Point p11 = readPoint(jsonPoint11);
      Point p12 = readPoint(jsonPoint12);
      FoldEdge.EdgeType type1 = FoldEdge.EdgeType.byName(typeOfEdge1);
      FoldEdge fe1 = new FoldEdge(null, p11, p12, type1);
      Point p21 = readPoint(jsonPoint21);
      Point p22 = readPoint(jsonPoint22);
      FoldEdge.EdgeType type2 = FoldEdge.EdgeType.byName(typeOfEdge2);
      FoldEdge fe2 = new FoldEdge(null, p21, p22, type2);
      Point cameraPosition = readPoint(jsonCameraPosition);
      LineFoldType foldDirection = fromName(jsonFoldDirection);
      return foldingImplementation.makeGUIFoldLinePoints(foldDirection, fe1, fe2, cameraPosition);
   }
}

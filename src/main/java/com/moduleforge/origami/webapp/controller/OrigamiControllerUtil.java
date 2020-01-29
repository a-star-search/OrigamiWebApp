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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.origami.webapp.guidata.input.figure.FigureData;
import com.moduleforge.origami.webapp.guidata.output.FoldPoint;
import com.moduleforge.origami.webapp.service.DockedTo;
import com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold;
import org.javatuples.Pair;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold.ALL;
import static com.whitebeluga.origami.figure.folding.linefolding.FlapsToFold.MOST_SHALLOW;

/**
 * Some methods to be used by the origami controller.
 * They all have to do with low level data extraction of the data received from the front end.
 * There is no actual processing of the data, just reading.
 */
public class OrigamiControllerUtil {
   private static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();

   static {
      JSON_OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
   }

   static Color extractColor(ObjectNode jsonData) throws JsonProcessingException {
      return extractColor(jsonData, "color");
   }
   static Color extractColor(ObjectNode jsonData, String key) throws JsonProcessingException {
      JsonNode jsonColor = jsonData.get(key);
      return JSON_OBJECT_MAPPER.treeToValue(jsonColor, Color.class);
   }
   static boolean isAMAPFold(String layerFold) {
      switch (layerFold) {
         case "firstlayer":
            return false;
         case "alllayers":
            return true;
         default:
            throw new RuntimeException("Invalid value for the layers to fold.");
      }
   }
   static Pair<FoldPoint, FoldPoint> extractFoldPoints(ObjectNode jsonData) throws JsonProcessingException {
      Point pickedPoint1 = extractPoint(jsonData, "pickedFoldPoint1");
      Point dockedPoint1 = extractPoint(jsonData, "dockedFoldPoint1");
      String sDockedTo1 = extractObject(jsonData, "dockedTo1", String.class);
      DockedTo dockedTo1 = DockedTo.fromName(sDockedTo1);
      FoldPoint foldPoint1 = new FoldPoint(pickedPoint1, dockedPoint1, dockedTo1);

      Point pickedPoint2 = extractPoint(jsonData, "pickedFoldPoint2");
      Point dockedPoint2 = extractPoint(jsonData, "dockedFoldPoint2");
      String sDockedTo2 = extractObject(jsonData, "dockedTo2", String.class);
      DockedTo dockedTo2 = DockedTo.fromName(sDockedTo2);
      FoldPoint foldPoint2 = new FoldPoint(pickedPoint2, dockedPoint2, dockedTo2);

      return new Pair(foldPoint1, foldPoint2);
   }
   public static String asString(Object o) throws JsonProcessingException {
      return JSON_OBJECT_MAPPER.writeValueAsString(o);
   }
   static <T> T readValue(String content, Class<T> valueType) throws IOException {
      return JSON_OBJECT_MAPPER.readValue(content, valueType);
   }
   static Point readPoint(String content) throws IOException {
      return JSON_OBJECT_MAPPER.readValue(content, Point.class);
   }
   static <T> T extractObject(ObjectNode jsonData, String key, Class<T> tClass) throws JsonProcessingException {
      JsonNode jsonNode = jsonData.get(key);
      return JSON_OBJECT_MAPPER.treeToValue(jsonNode, tClass);
   }
   static Point extractPoint(ObjectNode jsonData, String key) throws JsonProcessingException {
      JsonNode jsonNode = jsonData.get(key);
      return JSON_OBJECT_MAPPER.treeToValue(jsonNode, Point.class);
   }
   public static FlapsToFold flapsToFold(boolean isAMAPFold){
      if(isAMAPFold)
         return ALL;
      return MOST_SHALLOW;
   }
   public static Map<String, Object> makeFigureDataMapWithoutSnapshots(FigureData figureData) {
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("flat", figureData.getFlatFigure());
      responseBody.put("threed", figureData.getThreeDFigure());
      responseBody.put("visibleEdges", figureData.getSegments());
      responseBody.put("creases", figureData.getCreases().creases);
      return responseBody;
   }
   public static Map<String, Object> makeFigureDataMapWithSnapshots(FigureData figureData) {
      Map<String, Object> map = makeFigureDataMapWithoutSnapshots(figureData);
      map.put("foldsnapshots", figureData.getFoldSnapshots());
      map.put("translationsnapshots", figureData.getTranslationSnapshots());
      return map;
   }
}

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
I wonder if there is an automatic way to do this stuff through some framework.

Basically I'm transforming the simplified lists and maps sent from the backend back into objects of classes
like Point, RibbonFigure, etc.

The same goes for sending info back to the server.

It would be VERY nice if I can avoid that hassle and transform from a Java class to my Javascript class.

 */
class AjaxDataStructureConverter {
    private static _instance: AjaxDataStructureConverter;

    private constructor() {
    }

    public static get Instance() {
        // Do you need arguments? Make it a regular method instead.
        return this._instance || (this._instance = new this());
    }

    public convertFigure(flat: any, threed: any, edges: any[][], creases?: any[][]): Figure {
        let flatFigure = this.convertSingleFigure(flat);
        let threeDFigure = this.convertSingleFigure(threed);
        let edgesOfFigure = this.convertSegments(edges);
        let creasesOfFigure: Set<Segment>;
        if(creases) {
          creasesOfFigure = this.convertSegments(creases);
        } else {
          creasesOfFigure = new Set<Segment>();
        }
        return new Figure(flatFigure, threeDFigure, edgesOfFigure, creasesOfFigure);
    }
    public convertSingleFigure(figure: any): RibbonFigure {
        let legacyRibbonFigure = this.convertSingleFigureToLegacyRibbonFigure(figure);
        return this.convertLegacyRibbonFigureToRibbonFigure(legacyRibbonFigure);
    }
    private convertSingleFigureToLegacyRibbonFigure(figure: any): LegacyRibbonFigure {
        let facesColorOne = figure.facesColorOne;
        let structuredFacesColorOne = this.convertFaces(facesColorOne);
        let colorOne = figure.colorOne;
        let structuredColorOne = this.convertColor(colorOne);

        let facesColorTwo = figure.facesColorTwo;
        let structuredFacesColorTwo = this.convertFaces(facesColorTwo);
        let colorTwo = figure.colorTwo;
        let structuredColorTwo = null;
        if(colorTwo !== null) {
            structuredColorTwo = this.convertColor(colorTwo);
        }
        return new LegacyRibbonFigure(structuredFacesColorOne, structuredFacesColorTwo, structuredColorOne, structuredColorTwo);
    }
    private convertSegments(segments: any[][]): Set<Segment> {
        if(segments.length == 0) {
            return new Set();
        }
        let segmentList = [];
        for(let segment of segments) {
            segmentList.push(this.convertSegment(segment));
        }
        return new Set(segmentList);
    }
    private convertSegment(segment: any[]): Segment {
        if(segment.length !== 2) {
            throw new Error("A segment should contain two points.");
        }
        let firstPoint = segment[0];
        let structuredFirstPoint = new Point(firstPoint.x, firstPoint.y, firstPoint.z);
        let secondPoint = segment[1];
        let structuredSecondPoint = new Point(secondPoint.x, secondPoint.y, secondPoint.z);
        return new Segment(structuredFirstPoint, structuredSecondPoint);
    }
    private convertFaces(faces: any[]): Set<RibbonPolygon> {
        let structuredFaceList = [];
        for(let face of faces) {
            let structuredFace = this.convertFace(face);
            structuredFaceList.push(structuredFace);
        }
        return new Set(structuredFaceList);
    }
    private convertColor(color: any): Color {
        return new Color(color.red, color.green, color.blue);
    }
    private convertFace(face: any): RibbonPolygon {
        let path1 = face.path1;
        let structuredPath1 = this.convertPointList(path1);
        let path2 = face.path2;
        let structuredPath2 = this.convertPointList(path2);
        let perimeter = face.perimeter;
        let structuredPerimeter = this.convertPointList(perimeter);
        return new RibbonPolygon(structuredPerimeter, structuredPath1, structuredPath2);
    }
    private convertPointList(points: any[]): Point[] {
        let pointList = [];
        for(let point of points){
            pointList.push(new Point(point.x, point.y, point.z));
        }
        return pointList;
    }
    private convertLegacyRibbonFigureToRibbonFigure(legacyRibbonFigure: LegacyRibbonFigure): RibbonFigure {
        let faces: Face[] = [];
        let facesColorOne = legacyRibbonFigure.facesColorOne;
        for(let faceColorOne of facesColorOne) {
            let face = new Face(legacyRibbonFigure.colorOne, faceColorOne);
            faces.push(face);
        }
        if(legacyRibbonFigure.colorTwo == null)
            return new RibbonFigure(new Set(faces));
        let facesColorTwo = legacyRibbonFigure.facesColorTwo;
        for(let faceColorTwo of facesColorTwo) {
            let face = new Face(legacyRibbonFigure.colorTwo, faceColorTwo);
            faces.push(face);
        }
        return new RibbonFigure(new Set(faces));
    }
}

const dataStructureConverterForAjax = AjaxDataStructureConverter.Instance;
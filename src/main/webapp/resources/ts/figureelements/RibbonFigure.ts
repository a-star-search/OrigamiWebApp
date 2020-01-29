
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

class RibbonFigure {
    private readonly _faces: Set<Face>;

    constructor(faces: Set<Face>) {
        this._faces = faces;
    }

    get faces(): Set<Face> { return this._faces; }

    public static fromAjaxData(figure: any): RibbonFigure {
        return AjaxDataStructureConverter.Instance.convertSingleFigure(figure);
    }
}

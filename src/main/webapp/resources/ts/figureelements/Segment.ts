
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

class Segment {
    private readonly _ends: [Point, Point];

    constructor(p1: Point, p2: Point) {
        this._ends = [p1, p2];
    }
    get ends(): [Point, Point] { return this._ends; }

    public epsilonEquals(other: Segment) : boolean {
        if(other._ends[0].epsilonEquals(this._ends[0]) && other._ends[1].epsilonEquals(this._ends[1]))
            return true;
        return other._ends[1].epsilonEquals(this._ends[0]) && other._ends[0].epsilonEquals(this._ends[1]);
    }
}


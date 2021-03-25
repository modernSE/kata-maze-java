package de.cas.experts.software.maze;

import java.util.Objects;

public final class Location {
	
	private final int row;
	private final int column;
	
	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public int manhattanDistance(Location other) {
		return Math.abs(other.row - this.row) + Math.abs(other.column - this.column);
	}
	
	public Location addOffset(Offset offset) {
		return new Location(this.row + offset.getRowOffset(), this.column + offset.getColumnOffset());
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return column == other.column && row == other.row;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ")";
	}
}

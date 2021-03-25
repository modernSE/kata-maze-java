package de.cas.experts.software.maze;

import java.util.Objects;

class Offset {
	private final int rowOffset;
	private final int columnOffset;

	public Offset(int rowOffset, int columnOffset) {
		this.rowOffset = rowOffset;
		this.columnOffset = columnOffset;
	}

	public int getRowOffset() {
		return rowOffset;
	}

	public int getColumnOffset() {
		return columnOffset;
	}

	@Override
	public int hashCode() {
		return Objects.hash(columnOffset, rowOffset);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Offset other = (Offset) obj;
		return columnOffset == other.columnOffset && rowOffset == other.rowOffset;
	}
}

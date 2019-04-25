package oop.board.square;

public interface Squares {
	public abstract int getSquareID();
	public abstract String getMarker();
	public abstract void setMarker(String marker, boolean isReset);
	public abstract boolean getIsMarked();
}

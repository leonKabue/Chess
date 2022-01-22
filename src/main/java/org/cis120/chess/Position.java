package org.cis120.chess;

public class Position implements Comparable<Position> {
    private int positionX;
    private int positionY;

    public Position(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setPositionX(int posX) {
        this.positionX = posX;
    }

    public void setPositionY(int posY) {
        this.positionY = posY;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    @Override
    public int compareTo(Position o) {
        if (this.positionX < o.positionX) {
            return -1;
        }
        if (this.positionX > o.positionX) {
            return 1;
        }
        return Double.compare(this.positionY, o.positionY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return Double.compare(position.positionX, this.positionX) == 0 &&
                Double.compare(position.positionY, this.positionY) == 0;
    }

}

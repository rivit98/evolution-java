package agh.po.ewolucja;

public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d v){
        this.x = v.x;
        this.y = v.y;
    }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean precedes(Vector2d other){
        return (this.x <= other.x && this.y <= other.y);
    }

    public boolean follows(Vector2d other){
        return (this.x >= other.x && this.y >= other.y);
    }

    public Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other){
        if(this == other){
            return true;
        }

        if(!(other instanceof Vector2d)){
            return false;
        }
        Vector2d that = (Vector2d) other;
        return (this.x == that.x && this.y == that.y);
    }

    @Override
    public int hashCode() {
        int hash = 1337;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }
}

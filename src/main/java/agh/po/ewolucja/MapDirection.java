package agh.po.ewolucja;

import java.util.Random;

enum MapDirection {
    DEG0,
    DEG45,
    DEG90,
    DEG135,
    DEG180,
    DEG225,
    DEG270,
    DEG315;

    private static final MapDirection[] dirArray = MapDirection.values();
    private static final String[] dirString = new String[]{"↑", "↗", "→", "↘", "↓", "↙", "←" , "↖"};
    private static final Random rand = new Random();

    public static MapDirection getRandomDirection(){
        return getDirection(rand.nextInt(dirArray.length));
    }

    public static MapDirection getDirection(Integer x){
        return dirArray[x];
    }

    public MapDirection getNext(){
        return dirArray[(this.ordinal()+1) % dirArray.length];
    }

    public MapDirection getPrevious(){
        return dirArray[(this.ordinal() + values().length - 1) % dirArray.length];
    }

    public Vector2d toUnitVector(){
        switch(this) {
            case DEG0: return new Vector2d(0, 1);
            case DEG45: return new Vector2d(1, 1);
            case DEG90: return new Vector2d(1, 0);
            case DEG135: return new Vector2d(1, -1);
            case DEG180: return new Vector2d(0, -1);
            case DEG225: return new Vector2d(-1, -1);
            case DEG270: return new Vector2d(-1, 0);
            case DEG315: return new Vector2d(-1, 1);
            default:
                throw new IllegalArgumentException("something went wrong, srsly");
        }
    }

    public String toString(){
        return dirString[this.ordinal()];
    }
}
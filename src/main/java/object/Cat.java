package object;

public class Cat {
    private String name;
    private String catColor;
    private int tailLength;
    private int whiskersLength;

    public String getName() {
        return name;
    }

    public String getCatColor() {
        return catColor;
    }

    public int getTailLength() {
        return tailLength;
    }

    public int getWhiskersLength() {
        return whiskersLength;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCatColor(String catColor) {
        this.catColor = catColor;
    }

    public void setTailLength(int tailLength) {
        this.tailLength = tailLength;
    }

    public void setWhiskersLength(int whiskersLength) {
        this.whiskersLength = whiskersLength;
    }

    @Override
    public String toString() {
        return name + ", " +
                catColor + ", " +
                tailLength + ", " +
                whiskersLength;
    }
}
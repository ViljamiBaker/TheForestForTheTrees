package Forest;

public class Tile{
   public int id = 0; // identifier for this
   public double[] changeChance = {0.0}; // how common is the change
   public int[] changeInto = {0}; // this tile changes to theese types
   public int[] changeAdTypes = {0}; // changes theese ids to
   public int[] changeAdInto = {0}; // theese ids
   public double[] changeAdChance = {0.0}; // with this commonality
   public Tile(int id, double[] changeChance, int[] changeInto, int[] changeAdTypes, int[] changeAdInto, double[] changeAdChance){
      this.id = id;
      this.changeChance = changeChance;
      this.changeInto = changeInto;
      this.changeAdTypes = changeAdTypes;
      this.changeAdInto = changeAdInto;
      this.changeAdChance = changeAdChance;
   }
}
package Forest;

import java.util.concurrent.TimeUnit;

public class ForestManager{
   public int[][] forest;
   public int[][] nextForest;
   public int size;
   public Tile[] tiles = {
      new Tile(
         0, 
         new double[] {0.00005}, 
         new int[] {1}, 
         new int[] {}, 
         new int[] {}, 
         new double[]{}
      ), 
      new Tile(
         1, 
         new double[] {0.0005, 0.00001}, 
         new int[] {4,2}, 
         new int[] {0}, 
         new int[] {1}, 
         new double[]{0.005}
      ), 
      new Tile(
         2, 
         new double[] {0.05}, 
         new int[] {5}, 
         new int[] {1,4}, 
         new int[] {2,2}, 
         new double[]{0.5,0.9}
      ),
      new Tile(
         3, 
         new double[] {}, 
         new int[] {}, 
         new int[] {2}, 
         new int[] {0}, 
         new double[]{0.1}
      ),
      new Tile(
         4, 
         new double[] {0.0001,0.0001}, 
         new int[] {0,2}, 
         new int[] {}, 
         new int[] {}, 
         new double[]{}
      ),
      new Tile(
         5, 
         new double[] {0.005}, 
         new int[] {0}, 
         new int[] {}, 
         new int[] {}, 
         new double[]{}
      )
   };
   BobRoss br;
   public ForestManager(int size){
      this.size = size;
      forest = new int[size][];
      nextForest = new int[size][];
      for(int y = 0; y < size; y++){
         forest[y] = new int[size];
         nextForest[y] = new int[size];
         for(int x = 0; x < size; x++){
            forest[y][x] = 0;
            nextForest[y][x] = (int)(Math.random()*3.2);
         }
      }
      br = new BobRoss(forest);
   }
   private void setTile(int x, int y, int to){
      nextForest[clamp(y)][clamp(x)] = to;
   }
   private int clamp(int in){
      if(in>=size){
         return 0;
      }
      if(in<0){
         return size-1;
      }
      return in;
   }
   private void setAdj(int x, int y, int from, int to, double chance){
      if(Math.random()<chance){
         if(forest[clamp(y)][clamp(x+1)] == from) setTile(x+1,y,to);
      }
      if(Math.random()<chance){
         if(forest[clamp(y)][clamp(x-1)] == from) setTile(x-1,y,to);
      }
      if(Math.random()<chance){
         if(forest[clamp(y+1)][clamp(x)] == from) setTile(x,y+1,to);
      }
      if(Math.random()<chance){
         if(forest[clamp(y-1)][clamp(x)] == from) setTile(x,y-1,to);
      }
   }
   private void updateTile(int x, int y){
      Tile tileType = tiles[forest[y][x]];
      for(int i = 0; i<tileType.changeChance.length; i++){
         if(Math.random()<tileType.changeChance[i]){
            setTile(x,y,tileType.changeInto[i]);
            return;
         }
      }
      for(int i = 0; i<tileType.changeAdTypes.length; i++){
         setAdj(x,y,tileType.changeAdTypes[i],tileType.changeAdInto[i],tileType.changeAdChance[i]);
      }
   }
   public void update(){
      int[] counts = new int[tiles.length];
      for(int y = 0; y < size; y++){
         for(int x = 0; x < size; x++){
            updateTile(x,y);
         }
      }
      for(int y = 0; y < size; y++){
         for(int x = 0; x < size; x++){
            forest[y][x] = nextForest[y][x];
            counts[nextForest[y][x]]++;
         }
      }
      String[] info = new String[tiles.length];
      for(int i = 0; i < tiles.length; i++){
         info[i] = String.valueOf(counts[i]);
      }
      br.infoToDraw = info;
      br.paint(br.g);
   }
   
   public static void main(String[] args){
      ForestManager fm = new ForestManager(700);
      while(true){
         long t = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
         fm.update();
         long t2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
         try {Thread.sleep((int)Math.max(16-(t2-t),0));}catch(InterruptedException e){}
      }
   }
}
package Forest;

import java.util.concurrent.TimeUnit;

public class ForestManager{
   public int[][] forest;
   public int[][] nextForest;
   public int size;
   public Tile[] tiles = {
      new Tile(//soil
         0, 
         new double[] {0.0000001, 0.0000005}, 
         new int[] {1,6}, 
         new int[] {}, 
         new int[] {}, 
         new double[]{}
      ), 
      new Tile(// heavy plants
         1, 
         new double[] {0.0005, 0.00001}, 
         new int[] {4,2}, 
         new int[] {0,5,6}, 
         new int[] {1,6,1}, 
         new double[]{0.005,0.01,0.001}
      ), 
      new Tile(// fire
         2, 
         new double[] {0.05}, 
         new int[] {5}, 
         new int[] {1,4,6}, 
         new int[] {2,2,2}, 
         new double[]{0.5,0.9,0.2}
      ),
      new Tile(// water
         3, 
         new double[] {}, 
         new int[] {}, 
         new int[] {2,6}, 
         new int[] {0,1}, 
         new double[]{0.1,0.001}
      ),
      new Tile(// dead foliage
         4, 
         new double[] {0.0001,0.0001}, 
         new int[] {0,2}, 
         new int[] {}, 
         new int[] {}, 
         new double[]{}
      ),
      new Tile(// ash
         5, 
         new double[] {0.005}, 
         new int[] {0}, 
         new int[] {}, 
         new int[] {}, 
         new double[]{}
      ),
      new Tile(// light foliage
         6, 
         new double[] {0.0005, 0.000002, 0.0002}, 
         new int[] {4,2,1}, 
         new int[] {0}, 
         new int[] {6}, 
         new double[]{0.008}
      )
   };

   public int[][] dirs = {
      { 1, 0},
      //{ 1, 1},
      { 0, 1},
      //{-1, 1},
      {-1, 0},
      //{-1,-1},
      { 0,-1},
      //{ 1,-1},
   };
   BobRoss br;

   double bobRossTime = 0.0;
   double lastTime = 0.0;
   double[] fps = new double[10];
   String[] info = new String[tiles.length+3];
   int[] counts = new int[tiles.length];

   public ForestManager(int size, int fancyGraphics){
      this.size = size;
      forest = new int[size][];
      nextForest = new int[size][];
      for(int y = 0; y < size; y++){
         forest[y] = new int[size];
         nextForest[y] = new int[size];
         for(int x = 0; x < size; x++){
            forest[y][x] = 0;
            nextForest[y][x] = (int)(0);
         }
      }
      br = new BobRoss(forest, fancyGraphics);
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
      for (int i = 0; i < dirs.length; i++) {
         if(Math.random()<chance){
            if(forest[clamp(y + dirs[i][1])][clamp(x + dirs[i][0])] == from) setTile(x + dirs[i][0],y + dirs[i][1],to);
         }
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
      double t = System.nanoTime()/1000000000.0;
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
      for(int i = 0; i < tiles.length; i++){
         info[i] = String.valueOf(counts[i]);
      }
      double t2 = System.nanoTime()/1000000000.0;
      info[info.length-3] = String.valueOf((t2-t) * 1000);
      double avgfps = 0.0;
      for (int i = 1; i < fps.length; i++) {
         fps[i-1] = fps[i];
         avgfps+=fps[i-1];
      }
      avgfps += fps[fps.length-1];

      info[info.length-2] = String.valueOf(bobRossTime * 1000);
      info[info.length-1] = String.valueOf(avgfps / fps.length);
      br.infoToDraw = info;
      t = System.nanoTime()/1000000000.0;
      br.paint(br.g);
      t2 = System.nanoTime()/1000000000.0;
      fps[fps.length-1] = 1.0/(t2-lastTime);
      bobRossTime = t2-t;
      lastTime = System.nanoTime()/1000000000.0;
   }
   
   public static void main(String[] args){
      ForestManager fm = new ForestManager(300,1);
      while(true){
         long t = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
         fm.update();
         long t2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
         try {Thread.sleep((int)Math.max(16-(t2-t),0));}catch(InterruptedException e){}
      }
   }
}
package Forest;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.event.KeyEvent;

public class BobRoss extends JFrame{
   int[][] forest;
   public String[] infoToDraw = new String[0];
   Graphics g;
   keyListener kl;
   double xoffset = 0;
   double yoffset = 0;
   double zoom = 1;
   int fancyGraphics = 0;
   public BobRoss(int[][] forest, int fancyGraphics){
      this.forest = forest;
      this.setTitle("painting");
      this.setSize(800, 800);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setResizable(false);
      this.setVisible(true);
      this.fancyGraphics = fancyGraphics;
      g = this.getGraphics();
      kl = new keyListener(this);
   }
   Color[] colors = {new Color(99, 48, 0), new Color(2, 110, 0), Color.ORANGE, Color.CYAN, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.GREEN};
   
   void drawLines(Graphics bg, String[] lines, int x, int y){
      bg.setColor(Color.BLACK);
      for(int i = 0; i<lines.length;i++){
         bg.drawString(lines[i],x,y+i*10);
      }
   }
   int[] pixels = new int[800*800];
   BufferedImage bi = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
   BufferedImage bi2 = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
   
   @Override
   public void paint(Graphics g){
      if(kl == null)
         return;
      if(kl.keyDown(KeyEvent.VK_W)){
         yoffset++;
      }
      if(kl.keyDown(KeyEvent.VK_S)){
         yoffset--;
      }
      if(kl.keyDown(KeyEvent.VK_A)){
         xoffset--;
      }
      if(kl.keyDown(KeyEvent.VK_D)){
         xoffset++;
      }
      if(kl.keyDown(KeyEvent.VK_E)){
         zoom*=1.01;
      }
      if(kl.keyDown(KeyEvent.VK_Q)){
         zoom*=0.99;
      }
      if(kl.keyDown(KeyEvent.VK_1)){
         fancyGraphics = 0;
      }
      if(kl.keyDown(KeyEvent.VK_2)){
         fancyGraphics = 1;
      }
      if(kl.keyDown(KeyEvent.VK_3)){
         fancyGraphics = 2;
      }

      Graphics bg = bi.getGraphics();
      bg.setColor(Color.lightGray);
      bg.fillRect(0,0,800,800);
      bg.setColor(Color.BLACK);
      int[] bandMasks = {0xFF0000, 0x00FF00, 0x0000FF, 0xFF000000};

      for(int y = 0; y < forest.length; y++){
         for(int x = 0; x < forest[y].length; x++){
            bg.setColor(colors[forest[y][x]]);
            bg.fillRect((int)((double)(x-xoffset*forest.length/100.0)*(720.0/forest.length)/zoom)+440,(int)((double)(y+yoffset*forest.length/100.0)*(720.0/forest.length)/zoom)+330,Math.max((int)(720.0/forest.length/zoom),1)+1,Math.max((int)(720.0/forest.length/zoom),1)+1);
         }
      }
      drawLines(bg, infoToDraw, (int)((double)(forest.length+5-xoffset*forest.length/100)*(720.0/forest.length)/zoom)+440,(int)((double)(5+yoffset*forest.length/100)*(720.0/forest.length)/zoom)+330);
      if(fancyGraphics != 0){
         for (int x = 0; x < 800; x++) {
            for (int y = 0; y < 800; y++) {
               double distToCenter = Math.sqrt((x-400)*(x-400)+(y-400)*(y-400));
               double z = 2-distToCenter/566*zoom;
               int x2 = (int)((x-400)/z+400);
               int y2 = (int)((y-400)/z+400);
               int color;
               if(x2<0||x2>=800||y2<0||y2>=800){
                  color = Color.GRAY.getRGB();
               }else if(fancyGraphics == 2){
                  double crt = (Math.sin(y/10.0 + System.nanoTime()/200000000.0)/2.0+0.5);
                  crt *= (Math.sin(x/2.0 + System.nanoTime()/10000000.0)/2.0+0.5);
                  int rgb = bi.getRGB(x2, y2);
                  int r1 = (rgb >> 16) & 0xFF;
                  int g1 = (rgb >> 8) & 0xFF;
                  int b1 = (rgb >> 0) & 0xFF;
                  double l = 0.2126 * r1 + 0.7152 * g1 + 0.0722 * b1;
                  int r2 = (int) (r1 + (l-r1) * crt);
                  int g2 = (int) (g1 + (l-g1) * crt);
                  int b2 = (int) (b1 + (l-b1) * crt);
                  color = (r2<<16) + (g2<<8) + b2;
               }else{
                  color = bi.getRGB(x2, y2);
                  //bg2.setColor(new Color((int)(x2*255.0/800.0),(int)(y2*255.0/800.0),0));
               }
               pixels[y*800 + x] = color;
            }
         }

         DataBufferInt buffer = new DataBufferInt(pixels, pixels.length);

         WritableRaster raster = Raster.createPackedRaster(buffer, 800, 800, 800, bandMasks, null);
         bi2.setData(raster);
         g.drawImage(bi2,0,0,null);
      }else{
         g.drawImage(bi,0,0,null);
      }
   }
}
package Forest;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.awt.event.KeyEvent;

public class BobRoss extends JFrame{
   int[][] forest;
   public String[] infoToDraw = new String[0];
   Graphics g;
   keyListener kl;
   double xoffset = 0;
   double yoffset = 0;
   double zoom = 1;
   boolean fancyGraphics = false;
   public BobRoss(int[][] forest, boolean fancyGraphics){
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
   Color[] colors = {new Color(99, 48, 0), Color.GREEN, Color.ORANGE, Color.CYAN, Color.LIGHT_GRAY, Color.DARK_GRAY};
   
   void drawLines(Graphics bg, String[] lines, int x, int y){
      bg.setColor(Color.BLACK);
      for(int i = 0; i<lines.length;i++){
         bg.drawString(lines[i],x,y+i*10);
      }
   }
   
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
      BufferedImage bi = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
      Graphics bg = bi.getGraphics();
      bg.setColor(Color.WHITE);
      bg.fillRect(0,0,800,800);
      bg.setColor(Color.BLACK);
      for(int y = 0; y < forest.length; y++){
         for(int x = 0; x < forest[y].length; x++){
            bg.setColor(colors[forest[y][x]]);
            bg.fillRect((int)((double)(x-xoffset*forest.length/100.0)*(720.0/forest.length)/zoom)+440,(int)((double)(y+yoffset*forest.length/100.0)*(720.0/forest.length)/zoom)+330,Math.max((int)(720.0/forest.length/zoom),1)+1,Math.max((int)(720.0/forest.length/zoom),1)+1);
         }
      }
      drawLines(bg, infoToDraw, (int)((double)(forest.length+5-xoffset*forest.length/100)*(720.0/forest.length)/zoom)+440,(int)((double)(5+yoffset*forest.length/100)*(720.0/forest.length)/zoom)+330);
      if(fancyGraphics){
         BufferedImage bi2 = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
         Graphics bg2 = bi2.getGraphics();
         for (int x = 0; x < 800; x++) {
            for (int y = 0; y < 800; y++) {
               double distToCenter = Math.sqrt((x-400)*(x-400)+(y-400)*(y-400));
               double z = 2-distToCenter/566*zoom;
               int x2 = Math.min(Math.max((int)((x-400)/z+400),0),799);
               int y2 = Math.min(Math.max((int)((y-400)/z+400),0),799);
               bg2.setColor(new Color(bi.getRGB(x2, y2)));
               bg2.drawLine(x, y, x, y);
            }
         }
         g.drawImage(bi2,0,0,null);
      }else{
         g.drawImage(bi,0,0,null);
      }
   }
}
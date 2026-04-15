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
   int xoffset = 0;
   int yoffset = 0;
   double zoom = 1;
   public BobRoss(int[][] forest){
      this.forest = forest;
      this.setTitle("painting");
      this.setSize(800, 800);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setResizable(false);
      this.setVisible(true);
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
            bg.fillRect((int)((double)(x-xoffset*forest.length/100)*(720.0/forest.length)/zoom)+440,(int)((double)(y+yoffset*forest.length/100)*(720.0/forest.length)/zoom)+330,Math.max((int)(720.0/forest.length/zoom),1)+1,Math.max((int)(720.0/forest.length/zoom),1)+1);
         }
      }
      drawLines(bg, infoToDraw, (int)((double)(forest.length+5-xoffset*forest.length/100)*(720.0/forest.length)/zoom)+440,(int)((double)(5+yoffset*forest.length/100)*(720.0/forest.length)/zoom)+330);
      g.drawImage(bi,0,0,null);
   }
}
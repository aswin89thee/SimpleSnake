package com.snake;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

public class Gui extends Frame implements KeyListener {
  Snake snake;
  public int length = 400;
  public int breadth = 400;
  BufferedImage  bf;        //For double buffering
  
  public Gui(Snake s) { 
    super("Snake");
    snake = s;
    this.setSize(length,breadth);
    this.setResizable(false);    
    this.setVisible(true);
    bf = new BufferedImage( this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
    this.addWindowListener(new WindowCloser(this));
    this.addKeyListener(this);
  }
  
  public void keyPressed(KeyEvent ke){}
  public void keyTyped(KeyEvent ke){}
  
  public void keyReleased(KeyEvent ke)
  {
    if(snake.status == GameStatus.PAUSED)
    {
      if(ke.getKeyCode() == KeyEvent.VK_SPACE)
        snake.status = GameStatus.RUNNING;
    }
    else if(snake.status == GameStatus.RUNNING)
    {
      int dir = ke.getKeyCode();
      if(dir == KeyEvent.VK_UP)
      {
        if(snake.direction != Direction.DOWN)
          snake.direction = Direction.UP;
      }
      else if(dir == KeyEvent.VK_DOWN)
      {
        if(snake.direction != Direction.UP)
          snake.direction = Direction.DOWN;
      }
      else if(dir == KeyEvent.VK_LEFT)
      {
        if(snake.direction != Direction.RIGHT)
          snake.direction = Direction.LEFT;
      }
      else if(dir == KeyEvent.VK_RIGHT)
      {
        if(snake.direction != Direction.LEFT)
          snake.direction = Direction.RIGHT;
      }
      else if(dir == KeyEvent.VK_SPACE)
      {
        snake.status = GameStatus.PAUSED;
      }
      
      snake.addNode();
      snake.checkGameOver();
      snake.deleteLastNode();
      snake.checkFood();      
      //paint(this.getGraphics());
      repaint();
    }
  }     
  
  void checkHighScore(Graphics g)
  {                
      // Object deserialization
      try {
        Statistics object2;
        String filename = StatisticsInitializer.SERIAL;
        File f = new File(filename);
        if(!f.exists())
        {
        	StatisticsInitializer.init(null);
        }
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        object2 = (Statistics)ois.readObject();
        //System.out.println("\nHighest score is "+ object2.highScore);
        if(snake.score >= object2.highScore)
        {
          g.drawString("Congratulations!! You have a high score!",(length/2)-70,(breadth/2)+20);
          ois.close();
          Statistics object1 = new Statistics();
          object1.highScore = snake.score;
          FileOutputStream fos = new FileOutputStream("serial");
          ObjectOutputStream oos = new ObjectOutputStream(fos);
          oos.writeObject(object1);
          oos.flush();
          oos.close();
        }
        else
        {
          g.drawString("The highest score is " + object2.highScore,(length/2)-70,(breadth/2)+20);          
        }        
        ois.close();
      }
      catch(Exception e) {
        System.out.println("Exception during deserialization: " + e);
        e.printStackTrace();
        System.exit(0);
      }
            
  }
  
  void animation(Graphics g)
  {    
    g.setColor(Color.white);
    g.fillRect(0,0,this.getWidth(),this.getHeight());
    g.setColor(Color.black);
    if(snake.status == GameStatus.GAMEOVER)
    {      
      g.drawString("GameOver!! \nYour score is "+snake.score,(length/2)-70,(breadth/2));      
      checkHighScore(g);
    }
    
      SnakeCoordinates temp = snake.head;
      //draw the snake
      while(temp != null)
      {
        int x,y;
        x = temp.x;
        y = temp.y;                  
        g.fillRect(x,y,5,5);
        g.setColor(Color.yellow);
        g.fillRect(x+2,y+2,1,1);
        g.setColor(Color.black);
        
        if(temp.link == null)
        {
          g.setColor(Color.white);
          g.fillRect(x,y,3,3);
          g.setColor(Color.black);
        }        
        temp = temp.link;
      }
      g.setColor(Color.red);
      g.fillRect(snake.xFood,snake.yFood,5,5);
  }
  
  public void paint(Graphics g)
  {
    Graphics imgraph = bf.getGraphics();
    imgraph.clearRect(0,0,this.getWidth(), this.getHeight());
    animation(imgraph);
    g.drawImage(bf,0,0,null);
  }
  
  public void update(Graphics g)
  {
    paint(g);
  }
  
      
}

class WindowCloser extends WindowAdapter
{
  Gui g;
  WindowCloser(Gui g)
  {
    this.g = g;
  }
  
  public void windowClosing(WindowEvent we)
  {
    System.exit(0);
  }
  
}

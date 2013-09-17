package com.snake;


import java.awt.*;
import java.awt.event.*;
import java.util.*;


class Snake implements Runnable
{
  Gui g;
  SnakeCoordinates head, tempNode;
  int direction;
  int xFood,yFood;
  int topEdge = 25;
  Random r;
  int score = 0;
  int status;
    
  Snake()
  {
    g = new Gui(this);
    head = new SnakeCoordinates();
    head.x = g.length/2;
    head.y = g.breadth/2;
    tempNode = new SnakeCoordinates();
    tempNode.x = head.x - 6;
    tempNode.y = head.y;
    head.link = tempNode;
    SnakeCoordinates temp2 = new SnakeCoordinates();
    temp2.x = tempNode.x-6;
    temp2.y = tempNode.y;
    tempNode.link = temp2;
    
    direction = Direction.RIGHT;    
    
    r = new Random ((new Date()).getTime());
    status = GameStatus.RUNNING;
    
    Thread t = new Thread(this);
    t.start();        
    placeFood();
  }
  
  public void placeFood()
  {    
    
    xFood = (r.nextInt()) % g.length;
    yFood = (r.nextInt()) % g.breadth;
    xFood = Math.abs(xFood);
    yFood = Math.abs(yFood);
    if(xFood < 6)
      xFood = 6;
    if(yFood < topEdge+2)
      yFood = topEdge+2;    
    if(xFood > g.length - 11)
      xFood = g.length - 11;
    if(yFood > g.breadth - 11)
      yFood = g.breadth - 11;
    
    //System.out.println("\nxFood: " + xFood);
    //System.out.println("\nyFood: " + yFood);
  }
  
  public void addNode()
  {
    if(direction == Direction.RIGHT)
      {
        SnakeCoordinates temp = new SnakeCoordinates();
        temp.x = (head.x + 6) % g.length;
        if(temp.x > (g.length - 6))
        {
          temp.x = 6;
        }
        if(temp.x <= 5)
        {
          temp.x = 6;
        }
        temp.y = head.y;
        temp.link = head;
        head = temp;        
      }
      else if(direction == Direction.LEFT)
      {
        SnakeCoordinates temp = new SnakeCoordinates();
        temp.x = (head.x - 6);
        temp.y = head.y;
        if(temp.x <= 0)
        {
          temp.x = g.length - 10;
        }
        if(temp.x > (g.length - 10))
        {
          temp.x = 6;
        }
        if(temp.x <= 5)
        {
          temp.x = 6;
        }
        temp.link = head;
        head = temp;        
      }
      else if(direction == Direction.UP)
      {
        SnakeCoordinates temp = new SnakeCoordinates();
        temp.x = head.x;
        temp.y = (head.y - 6);
        if(temp.y < topEdge)
        {
          temp.y = g.breadth - 10;
        }
        /*if(temp.y > (g.breadth - 10))
        {
          temp.y = topEdge + 1; 
        }*/
        temp.link = head;
        head = temp;        
      }
      else if(direction == Direction.DOWN)
      {
        SnakeCoordinates temp = new SnakeCoordinates();
        temp.x = head.x;
        temp.y = (head.y + 6) % g.breadth;
        /*if(temp.y < topEdge)
        {
          temp.y = g.breadth - 10;
        }*/
        if(temp.y > (g.breadth - 6))
        {
          temp.y = topEdge + 1; 
        }
        temp.link = head;
        head = temp;        
      }
      checkGameOver();     
  }
 
  public void checkFood()
  {
    Boolean xTouch = false;
      xTouch = ((head.x >= xFood) && (head.x <= xFood + 5)) || ((head.x <= xFood) && (head.x >= xFood - 5));
      Boolean yTouch = false;
      yTouch = ((head.y >= yFood) && (head.y < yFood +5)) || ((head.y <= yFood) && (head.y > yFood - 5));
      if(xTouch && yTouch)
      {
        placeFood();
        addNode();
        addNode();
        addNode();
        score += 100;
      }
  }
  
  public void checkGameOver()
  {
    SnakeCoordinates temp = new SnakeCoordinates();    
    temp = head;
    while(temp.link != null)
    {
      if(temp != head && (temp.x == head.x) && (temp.y == head.y))
      {
        status = GameStatus.GAMEOVER;
      }
      temp = temp.link;      
    }
    
  }
  
  public void run()
  {
    while(true)
    {
      try{
      Thread.sleep(50);
      }catch(Exception e){}
      
      if(status == GameStatus.GAMEOVER)
      {
        
      }
      else if(status == GameStatus.RUNNING)
      {
        //move in the specific direction
        addNode();
        deleteLastNode();            
        //check if snake reaches food
        checkFood();
        //check for gameover
        checkGameOver();
        
      }
      g.repaint();
      //g.paint(g.getGraphics());
    }
  }
  
  public void deleteLastNode()
  {
    SnakeCoordinates temp = new SnakeCoordinates();
    SnakeCoordinates temp1 = new SnakeCoordinates();
    temp = head;
    while((temp.link).link != null)
    {
      temp = temp.link;      
    }
    temp1 = (temp.link).link;    
    temp.link = null;
    temp1 = null;
  }
  
}
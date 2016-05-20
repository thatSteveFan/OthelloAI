/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import othello.AIHelper;
import othello.AIPlayer;
import othello.Location;

/**
 * This was a relatively simple AI. I looked at how MinMax and the Monte Carlo Tree Search work, 
 * but did not have time to implement either. This AI simply looks for corners and sides not adjacent to corners
 * 
 * The development was relatively simple.
 * 
 * One request I wanna ask is a way to play many games in succesion for the same 
 * pair of players, as sitting and mashing the mouse feels like a bad solution. 
 * I tried to implement it myself, but I have no clue how swing works
 * 
 * 
 * 
 * @author pramukh
 */
public class MonteCarloAIPlayer extends AIPlayer
{

    public MonteCarloAIPlayer(int id) throws IOException
    {

        super("Link_pic.png", "thatSteveFan2", id);
    }

    @Override
    public Location chooseMove(final int[][] idArray)
    {
        List<Location> possibleMoves = AIHelper.getValidMoves(idArray, getID(), getOpponentID());
        
        if(disksOnBoard(idArray) < 10)
        {
            Optional<Location> corner = possibleMoves.stream().filter((Location t) ->
        {
            return (t.getCol() == 0 || t.getCol() == idArray[0].length - 1) && (t.getRow() == 0 ||t.getRow() == idArray.length - 1);
        }).findAny();
        
        if(corner.isPresent()) return corner.get();
        
        
        List<Location> side = possibleMoves.stream().filter((Location t) ->
        {
            return (t.getCol() == 0 || t.getCol() == idArray[0].length - 1) || (t.getRow() == 0 ||t.getRow() == idArray.length - 1);
        }).collect(Collectors.toList());
        
        for(Location sideLocation : side)
        {
            if(!nearCorner(idArray, sideLocation))
            return sideLocation;
        }

        possibleMoves.removeAll(side);

        return possibleMoves.isEmpty() ? getRandom(AIHelper.getValidMoves(idArray, getID(), getOpponentID())) : getRandom(possibleMoves);
        }

        
        Timer t = new Timer();
        
        BooleanProperty b = new SimpleBooleanProperty(true);
        
        t.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }, 230);
        
        
        Node<OthelloStats> root = new Node<>(new OthelloStats(idArray));
        while(b.getValue())
        {
            
        }
        
        try
        {
        return root.getChildren().stream().max((Node<OthelloStats> e1, Node<OthelloStats> e2) -> 
        {
            return 100 * (int)(e1.get().winPercent() - e2.get().winPercent());
        }).get().get();
        }
        catch(NoSuchElementException nsee)
                {
                    return null;
                }
        
    }
    
    private Location diff(int[][] old, int[][] new)
    {
        
    }
    
    private <T> T getRandom(List<? extends T> list)
    {
        return list.get((int)(Math.random() * list.size()));
    }
    
    private boolean nearCorner(int[][] idArray, Location spot)
    {
        return ((spot.getRow() == 0 || spot.getRow() == idArray.length - 1) && (spot.getCol() != 1 && spot.getCol() != idArray[0].length))
                    || ((spot.getCol() == 0 || spot.getCol() == idArray.length) &&(spot.getRow() != 1 && spot.getRow() != idArray.length) );
    }

    private int disksOnBoard(int[][] idArray)
    {
        int counter = 0;
        for(int[] arr : idArray)for(int i : arr)if(i != 0) counter++;
        return counter;
    }
    
    
}

class Node<T>
{
    private T data;
    private List<Node<T>> children;
    public Node(T data)
    {
        this.data = data;
    }
    
    @SafeVarargs
    public Node(T data, Node<T>... children)
    {
        this.data = data;
        this.children = new ArrayList<>();
        this.children.addAll(Arrays.asList(children));
    }
    
    public Node(T data, Collection<Node<T>> children)
    {
        this.data = data;
        this.children = new ArrayList<>(children);
    }
    
    @Override
    public boolean equals(Object other)
    {
        return (other instanceof Node)? data.equals(((Node)other).get()): false;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.data);
        return hash;
    }
    
    
    public T get()
    {
        return data;
    }
    
    
    public List<Node<T>> getChildren()
    {
        return (children);
    }
    
    public void addChild(Node<T> child)
    {
        children.add(child);
    }
    
    public void addChild(T child)
    {
        addChild(new Node<>(child));
    }
}

class OthelloStats
{
    private final int[][] board;
    private int seen;
    private int won;
    
    public OthelloStats(int[][] arr)
    {
        board = arr;
    }
    
    public void loss()
    {
        seen++;
    }
    public void won()
    {
        seen++;
        won++;
    }
    
    public int[][] getBoard()
    {
        return board;
    }
    
    @Override
    public boolean equals(Object other)
    {
       return other instanceof OthelloStats? Arrays.deepEquals(board, ((OthelloStats)other).getBoard()) : false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + Arrays.deepHashCode(this.board);
        return hash;
    }
    
    public double winPercent()
    {
        return ((double)won)/seen;
    }
}

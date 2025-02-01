package com.mycompany.eightboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.Serializable;
import javax.swing.JButton;

public class EightController extends JLabel implements VetoableChangeListener, ActionListener, Serializable {
    private int holePosition; // Store the position of the hole
    
    /**
     * constructor used to initialize the EightController variables
     */
    public EightController() {
        super("START");
        holePosition = 0; 
    }
    
    /**
     * method used when you restart the game to set up the EightController
     * 
     * @param labels array containing the new labels when you restart the game.
     */
    public void restart(int[] labels) {
        setText("START");
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] == 9) {
                holePosition = i+1; 
            }
        }
    }
    
    /**
    * vetoable change listener that checks if the the action is legal
    * 
    * @param evt event
    * @throws java.beans.PropertyVetoException
    */
    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        String propertyName = evt.getPropertyName();
        
        if ("label".equals(propertyName)) {
            EightTile tile = (EightTile) evt.getSource();
            int tilePosition = tile.getPosition();

            // Check if the tile is the hole or not adjacent to the hole
            if (tilePosition == holePosition || !isAdjacent(tilePosition, holePosition)) {
                setText("KO");
                throw new PropertyVetoException("Illegal move", evt);
            } else {
                setText("OK");
                this.holePosition = tilePosition;
            }
        }
    }

     /**
     * method to see if two tiles are adjacent
     * 
     * @param position1 position of the first tile.
     * @param position2 position of the second tile.
     * @return true if the tiles are adjacent, false otherwhise.
     */
    private boolean isAdjacent(int position1, int position2) {
        int row1 = (position1 - 1) / 3;
        int column1 = (position1 - 1) % 3;
        int row2 = (position2 - 1) / 3;
        int column2 = (position2 - 1) % 3;
        return Math.abs(row1 - row2) + Math.abs(column1 - column2) == 1;
    }

    
    /**
     * method to perform an action when restart button is clicked
     * 
     * @param ae avent 
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // retrieve the Array from the the restartButton's properties
        JButton button = (JButton) ae.getSource();
        if(button.getActionCommand().equals("restart")){
            int[] retrievedArray = (int[]) button.getClientProperty("labels");
            this.restart(retrievedArray);
        }
    }
    
    public int getHolePosition(){
        return this.holePosition;
    }
}
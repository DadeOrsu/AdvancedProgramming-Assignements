package com.mycompany.eightboard;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.io.Serializable;
import javax.swing.SwingUtilities;

public class EightTile extends JButton implements ActionListener, Serializable {
    private int label;  
    private int position;    
    private VetoableChangeSupport vetos = new VetoableChangeSupport(this);
    
    public EightTile() {}
    
    /**
     * costructor that initializes the tile
     * 
     * @param position position of the tile
     */
    public EightTile(int position) {
        super(String.valueOf(0)); 
        this.label = 0;        
        this.position = position;       
        this.updateBackgroundColor();
    }

    /**
     * method to get the position of the tile
     * 
     * @return the position of the tile
     */
    public int getPosition() {
        return position;
    }

    /**
     * method to get the label of the tile
     * 
     * @return the label of the tile 
     */
    @Override
    public String getLabel(){
        return Integer.toString(label);
    }
    
    /**
     * method to set the label of the tile
     * 
     * @param label
     * @throws InterruptedException 
     */
    
    public void setLabel(int label) throws InterruptedException {
        int oldLabel = this.label;
        try{
            vetos.fireVetoableChange("label",oldLabel,label);
            this.label = label;
            if(this.label==9)
                setText(" ");
            else
                setText(String.valueOf(this.label));
            this.updateBackgroundColor();
        }
        catch(PropertyVetoException e){
            Color oldBackground = getBackground();
            setBackground(Color.RED);

            // Use swingutilities to make it blink if the veto is negative.
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {  }
                setBackground(oldBackground);
            });
            throw new InterruptedException();
        }
    }
    
    
    /**
     * method to add a vetoable change listener to the tile
     * 
     * @param l 
     */
    public void addVetoableChangeListener(VetoableChangeListener l){
        vetos.addVetoableChangeListener(l);
    }
    
    /**
     * method to remove a vetoable change listener to the tile
     * 
     * @param l 
     */
    public void removeVetoableChangeListener(VetoableChangeListener l){
        vetos.removeVetoableChangeListener(l);
    }
    
    
    /**
     * method to update the backgorund color of the tile
     * 
     */
    public void updateBackgroundColor() {
        if (label == 9) {
            setBackground(Color.GRAY);  
        } else if (label == position) {
            setBackground(Color.GREEN); 
        } else {
            setBackground(Color.YELLOW); 
        }
    }
    
    
    /**
     * method used when the game restarted
     * 
     * @param labels a permutation of the labels
     */
    public void restart(int[] labels){
        this.label = labels[this.position - 1];
        if(label == 9)
            setText(String.valueOf(" "));
        else
            setText(String.valueOf(label));
        this.updateBackgroundColor();
    }

    /**
     * action perfordmed to manage the pression of the restart button 
     * 
     * @param ae event 
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
}
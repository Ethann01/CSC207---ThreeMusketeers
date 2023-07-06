package assignment2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class BoardPanel extends GridPane implements EventHandler<ActionEvent> {

    private final View view;
    private final Board board;
    public Cell fromCell;
    public Cell toCell;
    

    /**
     * Constructs a new GridPane that contains a Cell for each position in the board
     *
     * Contains default alignment and styles which can be modified
     * @param view
     * @param board
     */
    public BoardPanel(View view, Board board) {
        this.view = view;
        this.board = board;

        // Can modify styling
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #181a1b;");
        int size = 550;
        this.setPrefSize(size, size);
        this.setMinSize(size, size);
        this.setMaxSize(size, size);
        this.fromCell = null;
        this.toCell = null;
        setupBoard();
        updateCells();
    }


    /**
     * Setup the BoardPanel with Cells
     */
    private void setupBoard(){ // TODO
    	for(Cell tile: this.board.getAllCells()) {
    		this.add(tile,tile.getCoordinate().row, tile.getCoordinate().col);
    		tile.setOnAction(this);
    	}
    }

    /**
     * Updates the BoardPanel to represent the board with the latest information
     *
     * If it's a computer move: disable all cells and disable all game controls in view
     *
     * If it's a human player turn and they are picking a piece to move:
     *      - disable all cells
     *      - enable cells containing valid pieces that the player can move
     * If it's a human player turn and they have picked a piece to move:
     *      - disable all cells
     *      - enable cells containing other valid pieces the player can move
     *      - enable cells containing the possible destinations for the currently selected piece
     *
     * If the game is over:
     *      - update view.messageLabel with the winner ('MUSKETEER' or 'GUARD')
     *      - disable all cells
     */
    protected void updateCells(){ 
    	
    	if(!this.view.model.isHumanTurn() && (this.view.gameMode.getGameModeLabel() == "Human vs Computer(Random)") || 
    			this.view.gameMode.getGameModeLabel() == "Human vs Computer(Greedy)") {
    		for(Cell c: this.board.getAllCells()) {
    			c.setDisable(true);
    		}
    		if(this.view.undoButton != null && this.view.saveButton != null &&
    				this.view.restartButton != null) {
    			this.view.saveButton.setDisable(true);
    			this.view.undoButton.setDisable(true);
    			this.view.restartButton.setDisable(true);
    		}
    		
    	}
    	
    	if(this.view.gameMode.getGameModeLabel() == "Human vs Human") {
        	if(this.fromCell == null) {
           		for(Cell c: this.board.getAllCells()) {
        			c.setDisable(true);
        		}
           		for(Cell cell: this.board.getPossibleCells()) {
           			cell.setDisable(false);
           		}
        	
           		if(this.view.undoButton != null && this.view.saveButton != null &&
        				this.view.restartButton != null) {
        			this.view.saveButton.setDisable(false);
        			this.view.setUndoButton();
        			this.view.restartButton.setDisable(false);
        		}
        	}
        	
        	else if(this.fromCell != null && this.toCell == null) {
           		for(Cell c: this.board.getAllCells()) {
        			c.setDisable(true);
        		}
           		for(Cell c: this.board.getPossibleCells()) {
        			c.setDisable(false);
        		}

        		
           		for(Cell c: this.board.getPossibleDestinations(this.fromCell)) {
        			c.setDisable(false);
        		}

        		
           		
        		if(this.view.undoButton != null && this.view.saveButton != null &&
        				this.view.restartButton != null) {
        			this.view.saveButton.setDisable(false);
        			this.view.setUndoButton();
        			this.view.restartButton.setDisable(false);
        		}
        	}
    	}

    	
    	else if(this.view.model.isHumanTurn() && this.fromCell == null) {
       		for(Cell c: this.board.getAllCells()) {
    			c.setDisable(true);
    		}
       		for(Cell cell: this.board.getPossibleCells()) {
       			cell.setDisable(false);
       		}
       		
    		if(this.view.undoButton != null && this.view.saveButton != null &&
    				this.view.restartButton != null) {
    			this.view.saveButton.setDisable(false);
    			this.view.setUndoButton();
    			this.view.restartButton.setDisable(false);
    		}
    	}
    	else if(this.view.model.isHumanTurn() && this.fromCell != null) {
       		for(Cell c: this.board.getAllCells()) {
    			c.setDisable(true);
    		}
       		for(Cell c: this.board.getPossibleCells()) {
    			c.setDisable(false);
    		}
       		for(Cell c: this.board.getPossibleDestinations(this.fromCell)) {
    			c.setDisable(false);
    		}
       		
    		if(this.view.undoButton != null && this.view.saveButton != null &&
    				this.view.restartButton != null) {
    			this.view.saveButton.setDisable(false);
    			this.view.setUndoButton();
    			this.view.restartButton.setDisable(false);
    		}
    	}

    		
       	if(this.board.isGameOver()) {
       		for(Cell c: this.board.getAllCells()) {
    			c.setDisable(true);
    		}
    		if(this.view.undoButton != null && this.view.saveButton != null &&
    				this.view.restartButton != null) {
    			this.view.saveButton.setDisable(true);
    			this.view.undoButton.setDisable(true);
    			this.view.restartButton.setDisable(true);
    		}
    		
       	}
    	
    }

    /**
     * Handles Cell clicks and updates the board accordingly
     * When a Cell gets clicked the following must be handled:
     *  - If it's a valid piece that the player can move, select the piece and update the board
     *  - If it's a destination for a selected piece to move, perform the move and update the board
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) { // TODO
    	Cell c = (Cell)actionEvent.getSource();
    	
    	if(this.fromCell == null) {
    		for(Cell cell: this.board.getPossibleCells()) {
    			if(cell == c) {
    				this.fromCell = c;
    				this.updateCells();
    			}
    		}
    	}
    	else if(this.fromCell != null && this.toCell == null) {
    		for(Cell cell:this.board.getPossibleDestinations(this.fromCell)) {
    			if(cell == c) {
    				this.toCell = c;
    				this.updateCells();
    			}
    		}
    	}
    	if(this.fromCell != null && this.toCell != null){
    		this.view.runMove();
    		this.updateCells();
    		this.fromCell = null;
    		this.toCell = null;
    		this.view.runMove();
    		this.updateCells();
    		
    	}
    	
    }
}

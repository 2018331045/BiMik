package Gui;

import javafx.scene.input.KeyCode;
import logic.ClearRow;
import logic.DownData;
import logic.SimpleBoard;
import logic.ViewData;
import logic.events.EventSource;
import logic.FileDeo;

import logic.events.InputEventListener;
import logic.events.MoveEvent;



public class GameController implements InputEventListener {
    
    
    private SimpleBoard board = new SimpleBoard(25, 10);
   
    
    private final GuiController viewController;
  
    
    public GameController(GuiController c) {
       // brick =new JBrick();
		this.viewController = c;
		this.viewController.setEventLister(this);
		board.createNewBrick();
		this.viewController.initGameView(board.getBoardMatrix(),board.getViewData());
		this.viewController.bindScore(board.getScore().scoreProperty());
                int x=board.gethScore();
               
                this.viewController.bindScore2(x);
              // int s= this.viewController.bindScore(board.getScore().scoreProperty());
	}
    


    @Override
    public DownData onDownEvent(MoveEvent event) {
       boolean canMove = board.moveBrickDown();
		ClearRow clearRow = null;
		if(!canMove) {
			board.mergeBrickToBackground();
			clearRow = board.clearRows();
			if(clearRow.getLinesRemoved() > 0) {
				board.getScore().add(clearRow.getScoreBonus());
			}
			if(board.createNewBrick()) {
				viewController.gameOver();
			}
			
			
		}else {
			if(event.getEventSource() == EventSource.USER) {
				board.getScore().add(1);
			}
		}
		
		viewController.refreshGameBackground(board.getBoardMatrix());
		
		return new DownData(clearRow, board.getViewData());
    }    

    //@Override
    

    @Override
	public ViewData onLeftEvent() {
		board.moveBrickLeft();
		board.getScore().add(1);
		return board.getViewData();
	}

	@Override
	public ViewData onRightEvent() {
		board.moveBrickRight();
		board.getScore().add(1);
		return board.getViewData();
	}

	@Override
	public ViewData onRotateEvent() {
		board.rotateBrickLeft();
		
		return board.getViewData();
	}
    
}

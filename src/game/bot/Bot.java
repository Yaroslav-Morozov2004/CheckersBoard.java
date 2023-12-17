package game.bot;

import game.data.GameDataManeger;
import game.logic.Move;
import game.logic.MoveOptionsService;
import game.logic.MoveType;
import game.model.Cell;
import game.model.Chescer;
import game.model.Player;
import game.move.MoveService;

import java.util.*;

public class Bot {
    private Player player;

    public Bot(Player player) {
        this.player = player;
    }

    public Player doMove(GameDataManeger gameDataManeger, MoveService moveService){
        Map<Cell, Move> move_ = getMove(gameDataManeger);
        for (Cell from: move_.keySet()) {
            Move move = move_.get(from);
            Cell to = move.getTo();
            if(move.getMoveType() == MoveType.KILL){
                to = move.getTo_();
            }
            return moveService.doMove(gameDataManeger, player, from, to);

        }
        return MoveOptionsService.getEnemyPlayer(gameDataManeger, player);
    }
    public Map<Cell, Move> getMove(GameDataManeger gameDataManeger){
        Map<Cell, List<Move>> opt = getMovesOptions(gameDataManeger);

        List<Cell> cells = new ArrayList<>(opt.keySet());

        Cell cell = cells.get(new Random().nextInt(cells.size()));

        List<Move> moves = opt.get(cell);

        Move move = moves.get(
                new Random().nextInt(
                        moves.size()
                )
        );
        return Map.of(cell, move);
    }
    public Map<Cell, List<Move>> getMovesOptions(GameDataManeger gameDataManeger){
        List<Cell> cells = gameDataManeger.getPlayerCells(player);
        Map<Cell, List<Move>> optDefult = new HashMap<>();
        Map<Cell, List<Move>> optKill = new HashMap<>();
        for (Cell c: cells) {
            MoveOptionsService service = gameDataManeger.getMoveService(c);
            List<Move> defult = service.getDefultMoves(gameDataManeger, player, c);
            if(defult != null && !defult.isEmpty()){
                optDefult.put(c, defult);
            }

            List<Move> kill = service.getKillMoves(gameDataManeger, player, c);
            if(kill != null && !kill.isEmpty()){
                optKill.put(c, kill);
            }
        }

        if(!optKill.isEmpty()) {
            return optKill;
        }
        if(!optDefult.isEmpty()) {
            return optDefult;
        }

        return new HashMap<>();
    }

    public Player getPlayer() {
        return player;
    }
}

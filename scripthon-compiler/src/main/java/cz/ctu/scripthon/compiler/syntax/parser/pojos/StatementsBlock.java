package cz.ctu.scripthon.compiler.syntax.parser.pojos;

import java.util.Map;

public class StatementsBlock {

    private Map<Integer, Object> statementsOrBlocks;

    public StatementsBlock(Map<Integer, Object> statementsOrBlocks) {
        this.statementsOrBlocks = statementsOrBlocks;
    }

    public Map<Integer, Object> getStatementsOrBlocks() {
        return statementsOrBlocks;
    }

    public void setStatementsOrBlocks(Map<Integer, Object> statementsOrBlocks) {
        this.statementsOrBlocks = statementsOrBlocks;
    }
}

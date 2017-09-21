package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model;

import java.io.Serializable;

public class BlockInfo extends BaseJavaClassModelInfo implements Block, Serializable {

    int statementsNumber = 0;

    @Override
    public int getStatementsNumber() {
        return this.statementsNumber;
    }

    public void setStatementsNumber(int statementsNumber) {
        this.statementsNumber = statementsNumber;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");
        buffer.append("Block with statements number: ").append(statementsNumber);
        return buffer.toString();
    }
}
package cz.ctu.scripthon.compiler.syntax;

import cz.ctu.scripthon.compiler.syntax.expressions.properties.Definition;
import cz.ctu.scripthon.compiler.syntax.statements.Program;
import cz.ctu.scripthon.compiler.syntax.statements.Structure;

import java.util.List;

public class ProgramContainer {

    private static volatile ProgramContainer programContainer;
    private Program program = null;
    private String errorMessage;

    private ProgramContainer() {
    }

    public static ProgramContainer getInstance() {
        if (programContainer == null) {
            synchronized (ProgramContainer.class) {
                if (programContainer == null) {
                    programContainer = new ProgramContainer();
                }
            }
        }
        return programContainer;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * if this returns true, the previous program was less accurate, and therefore,
     * the previous corresponding result can be taken
     * otherwise, all the new sources must be compared again
     *
     * @param previousProgram
     * @return
     */
    public boolean moreAccurateChange(Program previousProgram) {
        if (previousProgram != null && program != null) {
            if (compareStatements(program.getStatements(), previousProgram.statements)) return true;
        }
        return false;
    }

    private boolean compareStatements(List<Statement> originalStatements, List<Statement> previousStatements) {
        if (originalStatements.size() != previousStatements.size()) {
            return false;
        }
        for (Statement originalStatement : originalStatements) {
            for (Statement previousStatement : previousStatements) {
                if (!compareStatementsAccuracy(originalStatement, previousStatement)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean compareStatementsAccuracy(Statement originalStatement, Statement previousStatement) {
        if (originalStatement instanceof Structure && previousStatement instanceof Structure) {
            if (!(((Structure) originalStatement).type == ((Structure) previousStatement).type)) {
                return false;
            }

            final Definition originalDefinition = ((Structure) originalStatement).definition;
            final Definition previousDefinition = ((Structure) previousStatement).definition;
            if (originalDefinition == null && previousDefinition != null) {
                return false;
            }

            if (originalDefinition != null && previousDefinition != null) {
                if (originalDefinition.numberOfDefinedProperties() <= previousDefinition.numberOfDefinedProperties()) {
                    return true;
                }
            }

            //TODO compare the definitions accuracy
            return true;
        }
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

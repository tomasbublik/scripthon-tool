package cz.ctu.scripthon.compiler.syntax.parser;

import cz.ctu.scripthon.compiler.pojo.Token;
import cz.ctu.scripthon.compiler.syntax.Statement;
import cz.ctu.scripthon.compiler.syntax.SupportedStatements;
import cz.ctu.scripthon.compiler.syntax.SupportedStructure;
import cz.ctu.scripthon.compiler.syntax.expressions.CompExp;
import cz.ctu.scripthon.compiler.syntax.expressions.Identifier;
import cz.ctu.scripthon.compiler.syntax.expressions.properties.*;
import cz.ctu.scripthon.compiler.syntax.parser.pojos.StatementsBlock;
import cz.ctu.scripthon.compiler.syntax.parser.pojos.TokenStatement;
import cz.ctu.scripthon.compiler.syntax.statements.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StatementsRecognizer {

    static final Logger LOG = LoggerFactory.getLogger(StatementsRecognizer.class);

    public Program recognizeProgram(StatementsBlock statementBlock) {
        return recognize(statementBlock, new Program());
    }

    public Program recognize(StatementsBlock statementBlock, Statement statement) {
        //recursive passing and recognize the statements
        Map<Integer, Object> statementsOrBlock = statementBlock.getStatementsOrBlocks();
        if (hasElements(statementsOrBlock)) {
            Set<Map.Entry<Integer, Object>> entrySet = statementsOrBlock.entrySet();
            Statement detectedStatement = null;
            IfStatement ifStatement = null;
            Block block = null;
            List<Statement> statementsOrBlockBuffer = new ArrayList<Statement>();
            for (Map.Entry<Integer, Object> entry : entrySet) {
                if (entry.getValue() instanceof List) {
                    for (TokenStatement tokenStatement : (List<TokenStatement>) entry.getValue()) {
                        detectedStatement = detectStatement(tokenStatement);
                        if (isIfStatement(detectedStatement)) {
                            ifStatement = (IfStatement) detectedStatement;
                        }
                        if (isElseStatementAfterIf(detectedStatement, ifStatement)) {
                            ifStatement.addElseStatement((ElseStatement) detectedStatement);
                            continue;
                        }
                        statementsOrBlockBuffer.add(detectedStatement);
                    }
                }
                if (isStatementBlock(entry)) {
                    block = new Block();
                    recognize((StatementsBlock) entry.getValue(), block);

                    if (!statementsOrBlockBuffer.isEmpty()) {
                        //if tokenStatement is first and the parent is Meth -> create block artificially
                        if (detectedStatementIsStructure(detectedStatement)) {
                            if (isMethod(detectedStatement)) {
                                Vector<Statement> innerStatements = block.statements;
                                if (hasStatementsWithBlockFirst(innerStatements)) {
                                    //adding a new block into the detected statement
                                    insertNewBlockIntoStatements(block, innerStatements);
                                    LOG.debug("Inserting a new block into the statement: " + getStructureName((Structure) detectedStatement));
                                }
                            }
                            //Removing unnecessary block from the Class statement
                            if (isClass(detectedStatement)) {
                                Vector<Statement> innerStatements = block.statements;
                                //1 because there can be only Block() statement, nothing else matters
                                if (containsOnlyBlock(innerStatements)) {
                                    //TODO - don't forget to propagate properties from removed Block() to the upper (Class()) level!
                                    block = ((Structure) innerStatements.get(0)).block;
                                    LOG.debug("Removing unnecessary block from the Class statement: " + getStructureName((Structure) detectedStatement));
                                }
                            }
                        }
                        detectedStatement.putElement(block);
                    } else {
                        statement.putElement(block);
                    }
                }
            }
            if (!statementsOrBlockBuffer.isEmpty()) {
                for (Statement statementFromBuffer : statementsOrBlockBuffer) {
                    statement.putElement(statementFromBuffer);
                }
            }
        }
        if (isProgram(statement)) {
            return (Program) statement;
        }
        return null;
    }

    private boolean isProgram(Statement statement) {
        return statement instanceof Program;
    }

    private boolean isMethod(Statement detectedStatement) {
        return isGivenStructure((Structure) detectedStatement, SupportedStructure.METHOD);
    }

    private boolean isClass(Statement detectedStatement) {
        return isGivenStructure((Structure) detectedStatement, SupportedStructure.CLASS);
    }

    private boolean isGivenStructure(Structure detectedStatement, SupportedStructure method) {
        return detectedStatement.type.equals(method);
    }

    private String getStructureName(Structure detectedStatement) {
        return detectedStatement.type.getName();
    }

    private void insertNewBlockIntoStatements(Block block, Vector<Statement> innerStatements) {
        Statement newBlockStatement = new Structure(SupportedStructure.BLOCK);
        Vector<Statement> newStatements = new Vector<>();
        Block newBlock = new Block();
        newBlock.statements = innerStatements;
        newBlockStatement.putElement(newBlock);
        newStatements.add(newBlockStatement);
        block.statements = newStatements;
    }

    private boolean isStatementBlock(Map.Entry<Integer, Object> entry) {
        return entry.getValue() instanceof StatementsBlock;
    }

    private boolean isElseStatementAfterIf(Statement detectedStatement, IfStatement ifStatement) {
        return detectedStatement instanceof ElseStatement && ifStatement != null;
    }

    private boolean isIfStatement(Statement detectedStatement) {
        return detectedStatement instanceof IfStatement;
    }

    private boolean hasStatementsWithBlockFirst(Vector<Statement> innerStatements) {
        return hasElements(innerStatements) && !firstStatementIsBlock(innerStatements);
    }

    private boolean containsOnlyBlock(Vector<Statement> innerStatements) {
        return isOneStatementOnly(innerStatements) && firstStatementIsBlock(innerStatements) && hasBlock(innerStatements);
    }

    private boolean hasBlock(Vector<Statement> innerStatements) {
        return ((Structure) innerStatements.get(0)).block != null;
    }

    private boolean isOneStatementOnly(Vector<Statement> innerStatements) {
        return innerStatements != null && innerStatements.size() == 1;
    }

    private boolean detectedStatementIsStructure(Statement detectedStatement) {
        return detectedStatement != null && detectedStatement instanceof Structure;
    }

    private boolean firstStatementIsBlock(Vector<Statement> innerStatements) {
        return isGivenStructure((Structure) innerStatements.get(0), SupportedStructure.BLOCK);
    }

    private boolean hasElements(Map<Integer, Object> collection) {
        return collection != null && !collection.isEmpty();
    }

    private boolean hasElements(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    private Statement detectStatement(TokenStatement tokenStatement) {
        Iterator iterator = tokenStatement.getTokenSequence().getTokenList().iterator();

        Token firstToken = (Token) iterator.next();
        List<Token> recordedTokens = recordProperties(iterator);

        Identifier identifier = isInitVariable(recordedTokens);
        if (firstToken.getValue().equals(SupportedStructure.CLASS.getName())) {
            ClassDefinition definition = DefinitionCreator.createClassDefinitions(tokenStatement.getTokenSequence().getTokenList());
            if (identifier != null) {
                return new InitVariable(identifier, SupportedStructure.CLASS, definition);
            }
            return new Structure(SupportedStructure.CLASS, definition);
        }
        if (firstToken.getValue().equals(SupportedStructure.BLOCK.getName())) {
            return new Structure(SupportedStructure.BLOCK);
        }

        if (firstToken.getValue().equals(SupportedStructure.METHOD.getName())) {
            MethDefinition definition = DefinitionCreator.createMethDefinitions(recordedTokens);
            if (identifier != null) {
                return new InitVariable(identifier, SupportedStructure.METHOD, definition);
            }
            return new Structure(SupportedStructure.METHOD, definition);
        }

        if (firstToken.getValue().equals(SupportedStructure.METHOD_CALL.getName())) {
            MethCallDefinition definition = DefinitionCreator.createMethCallDefinitions(recordedTokens);
            return new Structure(SupportedStructure.METHOD_CALL, definition);
        }

        if (firstToken.getValue().equals(SupportedStructure.INIT.getName())) {
            InitDefinition definition = DefinitionCreator.createInitDefinitions(recordedTokens);
            return new Structure(SupportedStructure.INIT, definition);
        }

        if (firstToken.getValue().equals(SupportedStructure.ANY.getName())) {
            return new Structure(SupportedStructure.ANY);
        }

        if (firstToken.getValue().equals(SupportedStatements.IF.getName())) {
            CompExp compExp = DefinitionCreator.createCompExpression(recordedTokens);
            return new IfStatement(compExp);
        }

        if (firstToken.getValue().equals(SupportedStatements.ELSE.getName())) {
            return new ElseStatement();
        }
        return null;
    }

    private Identifier isInitVariable(List<Token> tokenList) {
        if (tokenList != null && !tokenList.isEmpty()) {
            int lastIndex = tokenList.size() - 1;
            Token token = tokenList.get(lastIndex);
            if (token != null && token.getType().equals("word")) {
                return new Identifier((String) token.getValue());
            }
        }
        return null;
    }

    private List<Token> recordProperties(Iterator iterator) {
        boolean startRecording = false;
        List<Token> recordedTokens = new ArrayList<>();
        while (iterator.hasNext()) {
            Token token = (Token) iterator.next();
            if (startRecording && !(token.getValue().equals(")"))) {
                recordedTokens.add(token);
            }
            if (token.getValue().equals("(")) {
                startRecording = true;
            }
        }
        return recordedTokens;
    }

}

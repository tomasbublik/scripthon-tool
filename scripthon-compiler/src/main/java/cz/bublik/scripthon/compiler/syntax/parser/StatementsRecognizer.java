package cz.bublik.scripthon.compiler.syntax.parser;

import cz.bublik.scripthon.compiler.pojo.Token;
import cz.bublik.scripthon.compiler.syntax.Statement;
import cz.bublik.scripthon.compiler.syntax.SupportedStatements;
import cz.bublik.scripthon.compiler.syntax.SupportedStructures;
import cz.bublik.scripthon.compiler.syntax.expressions.CompExp;
import cz.bublik.scripthon.compiler.syntax.expressions.Identifier;
import cz.bublik.scripthon.compiler.syntax.expressions.properties.*;
import cz.bublik.scripthon.compiler.syntax.parser.pojos.StatementsBlock;
import cz.bublik.scripthon.compiler.syntax.parser.pojos.TokenStatement;
import cz.bublik.scripthon.compiler.syntax.statements.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StatementsRecognizer {

    static final Logger LOG = LoggerFactory.getLogger(StatementsRecognizer.class);

    public Program recognize(StatementsBlock StatementBlock, Statement statement) {
        //recursive passing and recognize the statements
        Map<Integer, Object> statementsOrBlock = StatementBlock.getStatementsOrBlocks();
        if (statementsOrBlock != null && !statementsOrBlock.isEmpty()) {
            Set<Map.Entry<Integer, Object>> entrySet = statementsOrBlock.entrySet();
            Statement detectedStatement = null;
            IfStatement ifStatement = null;
            Block block = null;
            List<Statement> statementsOrBlockBuffer = new ArrayList<Statement>();
            for (Map.Entry<Integer, Object> entry : entrySet) {
                if (entry.getValue() instanceof List) {
                    for (TokenStatement tokenStatement : (List<TokenStatement>) entry.getValue()) {
                        detectedStatement = statementDetect(tokenStatement);
                        if (detectedStatement instanceof IfStatement) {
                            ifStatement = (IfStatement) detectedStatement;
                        }
                        if (detectedStatement instanceof ElseStatement) {
                            if (ifStatement != null) {
                                ifStatement.addElseStatement((ElseStatement) detectedStatement);
                                continue;
                            }
                        }
                        statementsOrBlockBuffer.add(detectedStatement);
                    }
                }
                if (entry.getValue() instanceof StatementsBlock) {
                    block = new Block();
                    recognize((StatementsBlock) entry.getValue(), block);

                    if (!statementsOrBlockBuffer.isEmpty()) {
                        //if tokenStatement is first and the parent is Meth -> create block artificially
                        if (detectedStatement != null) {
                            if (detectedStatement instanceof Structure) {
                                if (((Structure) detectedStatement).type.equals(SupportedStructures.METHOD)) {
                                    Vector<Statement> innerStatements = block.statements;
                                    if (innerStatements != null && !innerStatements.isEmpty()) {
                                        if (!((Structure) innerStatements.get(0)).type.equals(SupportedStructures.BLOCK)) {
                                            //adding a new block into the detected statement
                                            Statement newBlockStatement = new Structure(SupportedStructures.BLOCK);
                                            Vector<Statement> newStatements = new Vector<>();
                                            Block newBlock = new Block();
                                            newBlock.statements = innerStatements;
                                            newBlockStatement.putElement(newBlock);
                                            newStatements.add(newBlockStatement);
                                            block.statements = newStatements;
                                            LOG.debug("Inserting a new block into the statement: " + ((Structure) detectedStatement).type.getName());
                                        }
                                    }
                                }
                                //Removing unnecessary block from the Class statement
                                if (((Structure) detectedStatement).type.equals(SupportedStructures.CLASS)) {
                                    Vector<Statement> innerStatements = block.statements;
                                    //1 because there can be only Block() statement, nothing else matters
                                    if (innerStatements != null && innerStatements.size() == 1) {
                                        if (((Structure) innerStatements.get(0)).type.equals(SupportedStructures.BLOCK)) {
                                            if (((Structure) innerStatements.get(0)).block != null) {
                                                //TODO - don't forget to propagate properties from removed Block() to the upper (Class()) level!
                                                block = ((Structure) innerStatements.get(0)).block;
                                                LOG.debug("Removing unnecessary block from the Class statement: " + ((Structure) detectedStatement).type.getName());
                                            }
                                        }
                                    }
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
        if (statement instanceof Program) {
            return (Program) statement;
        }
        return null;
    }

    private Statement statementDetect(TokenStatement tokenStatement) {
        Iterator iterator = tokenStatement.getTokenSequence().getTokenList().iterator();

        Token firstToken = (Token) iterator.next();
        List<Token> recordedTokens = recordProperties(iterator);

        Identifier identifier = isInitVariable(recordedTokens);
        if (firstToken.getValue().equals(SupportedStructures.CLASS.getName())) {
            ClassDefinition definition = DefinitionCreator.createClassDefinitions(tokenStatement.getTokenSequence().getTokenList());
            if (identifier != null) {
                return new InitVariable(identifier, SupportedStructures.CLASS, definition);
            }
            return new Structure(SupportedStructures.CLASS, definition);
        }
        if (firstToken.getValue().equals(SupportedStructures.BLOCK.getName())) {
            return new Structure(SupportedStructures.BLOCK);
        }

        if (firstToken.getValue().equals(SupportedStructures.METHOD.getName())) {
            MethDefinition definition = DefinitionCreator.createMethDefinitions(recordedTokens);
            if (identifier != null) {
                return new InitVariable(identifier, SupportedStructures.METHOD, definition);
            }
            return new Structure(SupportedStructures.METHOD, definition);
        }

        if (firstToken.getValue().equals(SupportedStructures.METHOD_CALL.getName())) {
            MethCallDefinition definition = DefinitionCreator.createMethCallDefinitions(recordedTokens);
            return new Structure(SupportedStructures.METHOD_CALL, definition);
        }

        if (firstToken.getValue().equals(SupportedStructures.INIT.getName())) {
            InitDefinition definition = DefinitionCreator.createInitDefinitions(recordedTokens);
            return new Structure(SupportedStructures.INIT, definition);
        }

        if (firstToken.getValue().equals(SupportedStructures.ANY.getName())) {
            return new Structure(SupportedStructures.ANY);
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

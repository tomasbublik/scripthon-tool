package cz.ctu.scripthon.compiler.syntax.expressions.properties;

import cz.ctu.scripthon.compiler.pojo.Token;
import cz.ctu.scripthon.compiler.syntax.expressions.*;
import cz.ctu.scripthon.compiler.syntax.Exp;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefinitionCreator {

    private static final Pattern NUMBER_PATTERN = Pattern.compile(".*[^0-9].*");

    public static ClassDefinition createClassDefinitions(List<Token> tokenList) {
        ClassDefinition classDefinition = null;
        if (tokenList != null && !tokenList.isEmpty()) {
            classDefinition = new ClassDefinition();
            for (int i = 0; i < tokenList.size(); i++) {
                Token token = tokenList.get(i);
                if (((String) token.getValue()).equals(";")) {
                    continue;
                }
                if (!((String) token.getValue()).equals(")")) {
                    switch ((String) token.getValue()) {
                        case "Name":
                            classDefinition.name = (String) tokenList.get(i + 3).getValue();
                            i = i + 5;
                            break;
                        case "Rest":
                            classDefinition.rest = Rest.getEnum((String) tokenList.get(i + 2).getValue());
                            i = i + 3;
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return classDefinition;
    }

    public static MethDefinition createMethDefinitions(List<Token> tokenList) {
        MethDefinition methDefinition = null;
        if (tokenList != null && !tokenList.isEmpty()) {
            methDefinition = new MethDefinition();
            for (int i = 0; i < tokenList.size(); i++) {
                Token token = tokenList.get(i);
                if (((String) token.getValue()).equals(";")) {
                    continue;
                }
                if (!((String) token.getValue()).equals(")")) {
                    switch ((String) token.getValue()) {
                        case "Name":
                            methDefinition.name = (String) tokenList.get(i + 3).getValue();
                            i = i + 5;
                            break;
                        case "Rest":
                            methDefinition.rest = Rest.getEnum((String) tokenList.get(i + 2).getValue());
                            i = i + 3;
                            break;
                        case "ParamsNum":
                            methDefinition.paramsNum = Integer.valueOf((String) tokenList.get(i + 2).getValue());
                            i = i + 3;
                            break;
                        case "Ret":
                            methDefinition.ret = (String) tokenList.get(i + 3).getValue();
                            i = i + 5;
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return methDefinition;
    }

    public static MethCallDefinition createMethCallDefinitions(List<Token> tokenList) {
        MethCallDefinition methCallDefinition = null;
        if (tokenList != null && !tokenList.isEmpty()) {
            methCallDefinition = new MethCallDefinition();
            for (int i = 0; i < tokenList.size(); i++) {
                Token token = tokenList.get(i);
                if (((String) token.getValue()).equals(";")) {
                    continue;
                }
                if (!((String) token.getValue()).equals(")")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    switch ((String) token.getValue()) {
                        case "Name":
                            //can contain "."
                            int j = i + 3;
                            while (!getValueOfIndex(j, tokenList).equals("\"")) {
                                stringBuilder.append((String) tokenList.get(j).getValue());
                                j++;
                            }
                            methCallDefinition.name = stringBuilder.toString();
                            i = j + 1;
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return methCallDefinition;
    }


    public static InitDefinition createInitDefinitions(List<Token> tokenList) {
        InitDefinition initDefinition = null;
        if (tokenList != null && !tokenList.isEmpty()) {
            initDefinition = new InitDefinition();
            for (int i = 0; i < tokenList.size(); i++) {
                Token token = tokenList.get(i);
                if (((String) token.getValue()).equals(";")) {
                    continue;
                }
                if (!((String) token.getValue()).equals(")")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    switch ((String) token.getValue()) {
                        case "Name":
                            initDefinition.name = (String) tokenList.get(i + 3).getValue();
                            i = i + 5;
                            break;
                        case "Type":
                            //initDefinition.type = (String) tokenList.get(i + 3).getValue();
                            //can contain .
                            int j = i + 3;
                            while (!getValueOfIndex(j, tokenList).equals("\"")) {
                                stringBuilder.append((String) tokenList.get(j).getValue());
                                j++;
                            }
                            initDefinition.name = stringBuilder.toString();
                            i = j + 1;
                            break;
                        case "Value":
                            initDefinition.value = (String) tokenList.get(i + 2).getValue();
                            i = i + 3;
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return initDefinition;
    }

    public static CompExp createCompExpression(List<Token> tokenList) {
        CompExp compExp = null;
        CompType compType = null;
        Exp firstExpression = null;
        Exp secondExpression = null;
        if (tokenList != null && !tokenList.isEmpty()) {
            for (int i = 0; i < tokenList.size(); i++) {
                Token token = tokenList.get(i);
                if (!((String) token.getValue()).equals(")")) {
                    final CompType compTypeTemp = CompType.getByLiteral(String.valueOf(token.getValue()));
                    if (compTypeTemp != null) {
                        compType = compTypeTemp;
                        continue;
                    }
                    switch ((String) token.getValue()) {
                        case "\"":
                            StringBuilder stringBuilder = new StringBuilder();
                            int j = i + 1;
                            while (!getValueOfIndex(j, tokenList).equals("\"")) {
                                stringBuilder.append((String) tokenList.get(j).getValue());
                                j++;
                            }
                            i = j + 1;
                            final StringLiteral stringLiteral = new StringLiteral(stringBuilder.toString());
                            if (compType == null) {
                                firstExpression = stringLiteral;
                            } else {
                                secondExpression = stringLiteral;
                            }
                            break;
                        default:
                            //could be IntegerLiteral or IdentifierAttributeCall
                            if (((String) tokenList.get(i + 1).getValue()).equals(".")) {
                                //it is a call
                                final IdentifierAttributeCall identifierAttributeCall = new IdentifierAttributeCall(new Identifier((String) tokenList.get(i).getValue()), ((String) tokenList.get(i + 2).getValue()));
                                if (compType == null) {
                                    firstExpression = identifierAttributeCall;
                                } else {
                                    secondExpression = identifierAttributeCall;
                                }
                                i = i + 2;
                                break;
                            }
                            Matcher matcher = NUMBER_PATTERN.matcher((String) token.getValue());
                            if (matcher.find()) {
                                final IntegerLiteral integerLiteral = new IntegerLiteral((String) token.getValue());
                                if (compType == null) {
                                    firstExpression = integerLiteral;
                                } else {
                                    secondExpression = integerLiteral;
                                }
                                break;
                            }
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        if (firstExpression != null && secondExpression != null && compType != null) {
            compExp = new CompExp(firstExpression, secondExpression, compType);
        }
        return compExp;
    }

    private static int indexOfTokenValue(String value, List<Token> tokens) {
        if (tokens != null && !tokens.isEmpty()) {
            for (int i = 0; i < tokens.size(); i++) {
                Token token = tokens.get(i);
                if (((String) token.getValue()).equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static String getValueOfIndex(int index, List<Token> tokens) {
        if (tokens != null && !tokens.isEmpty()) {
            Token token = tokens.get(index);
            return ((String) token.getValue());
        }
        return "";
    }
}

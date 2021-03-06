package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【语义分析】一元表达式
 *
 * @author bajdcc
 */
public class ExpSinop implements IExp {

	/**
	 * 操作符
	 */
	private Token token = null;

	/**
	 * 操作数
	 */
	private IExp operand = null;

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public IExp getOperand() {
		return operand;
	}

	public void setOperand(IExp operand) {
		this.operand = operand;
	}

	@Override
	public boolean isConstant() {
		return operand.isConstant();
	}
	
	@Override
	public boolean isEnumerable() {
		return operand.isEnumerable();
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		if (!isConstant()) {
			return this;
		}
		if (operand instanceof ExpValue) {
			if (token.kToken == TokenType.OPERATOR) {
				if (TokenTools.sinop(recorder, this)) {
					return operand;
				}
			}
		}
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		OperatorType type = (OperatorType) token.object;
		if (type == OperatorType.PLUS_PLUS || type == OperatorType.MINUS_MINUS) {
			if (!(operand instanceof ExpValue)) {
				recorder.add(SemanticError.INVALID_OPERATOR, token);
			}
		} else {
			operand.analysis(recorder);
		}
	}

	@Override
	public void genCode(ICodegen codegen) {
		operand.genCode(codegen);
		codegen.genCode(TokenTools.op2ins(token));
		OperatorType type = (OperatorType) token.object;
		if (type == OperatorType.PLUS_PLUS || type == OperatorType.MINUS_MINUS) {
			ExpValue value = (ExpValue) operand;
			codegen.genCode(RuntimeInst.ipush,
					codegen.genDataRef(value.getToken().object));
			codegen.genCode(RuntimeInst.istore);
		}
	}

	@Override
	public String toString() {
		return "( " + token.toRealString() + " " + operand.toString() + " )";
	}

	@Override
	public String print(StringBuilder prefix) {
		return toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		operand.addClosure(scope);
	}

	@Override
	public void setYield() {
		operand.setYield();
	}
}

package priv.bajdcc.semantic.lexer;

import java.util.ArrayList;

import priv.bajdcc.lexer.Lexer;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.lexer.token.Token;

/**
 * 词法分析器
 * 
 * @author bajdcc
 */
public class TokenFactory extends Lexer {
	
	/**
	 * 保存当前分析的单词流
	 */
	private ArrayList<Token> m_arrTokens = new ArrayList<Token>();

	public TokenFactory(String context) throws RegexException {
		super(context);
	}
	
	@Override
	public IRegexStringIterator copy() {
		TokenFactory o = null;
		try {
			o = (TokenFactory) super.clone();			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public ArrayList<Token> tokenList() {
		return m_arrTokens;
	}

	@Override
	public void saveToken() {
		m_arrTokens.add(m_Token);
	}	
}

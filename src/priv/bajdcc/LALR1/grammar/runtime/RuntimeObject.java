package priv.bajdcc.LALR1.grammar.runtime;

import java.math.BigDecimal;
import java.math.BigInteger;

import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【运行时】运行时对象
 *
 * @author bajdcc
 */
public class RuntimeObject implements Cloneable {

	private Object obj = null;
	private RuntimeObjectType type = RuntimeObjectType.kNull;
	private boolean readonly = false;
	private boolean copyable = true;

	public RuntimeObject(Object obj) {
		this.obj = obj;
		calcTypeFromObject();
	}

	public RuntimeObject(Object obj, RuntimeObjectType type) {
		this.obj = obj;
		this.type = type;
	}

	public RuntimeObject(Object obj, boolean readonly) {
		this.obj = obj;
		this.readonly = readonly;
		calcTypeFromObject();
	}

	public RuntimeObject(Object obj, boolean readonly, boolean copyable) {
		this.obj = obj;
		this.readonly = readonly;
		this.copyable = copyable;
		calcTypeFromObject();
	}
	
	public RuntimeObject(Object obj, RuntimeObjectType type, boolean readonly, boolean copyable) {
		this.obj = obj;
		this.type = type;
		this.readonly = readonly;
		this.copyable = copyable;
	}

	public RuntimeObject(RuntimeObject obj) {
		copyFrom(obj);
	}

	public void copyFrom(RuntimeObject obj) {
		if (obj != null) {
			this.obj = obj.obj;
			this.readonly = obj.readonly;
			this.copyable = obj.copyable;
			this.type = obj.type;
		} else {
			calcTypeFromObject();
		}
	}

	public static RuntimeObject createObject(RuntimeObject obj) {
		return new RuntimeObject(obj);
	}

	public void calcTypeFromObject() {
		type = fromObject(obj);
	}

	public Object getObj() {
		return obj;
	}

	public String getTypeName() {
		return fromObject(obj).getName();
	}

	public String getTypeString() {
		return fromObject(obj).toString();
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public RuntimeObjectType getType() {
		return type;
	}

	public void setType(RuntimeObjectType type) {
		this.type = type;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		switch (this.type) {
			case kArray:
			case kMap:
				return;
		}
		this.readonly = readonly;
	}

	public boolean isCopyable() {
		return copyable;
	}

	public void setCopyable(boolean copyable) {
		this.copyable = copyable;
	}

	public static RuntimeObjectType fromObject(Object obj) {
		if (obj == null) {
			return RuntimeObjectType.kNull;
		}
		if (obj instanceof String) {
			return RuntimeObjectType.kString;
		}
		if (obj instanceof Character) {
			return RuntimeObjectType.kChar;
		}
		if (obj instanceof BigInteger) {
			return RuntimeObjectType.kInt;
		}
		if (obj instanceof BigDecimal) {
			return RuntimeObjectType.kReal;
		}
		if (obj instanceof Boolean) {
			return RuntimeObjectType.kBool;
		}
		if (obj instanceof Integer) {
			return RuntimeObjectType.kPtr;
		}
		if (obj instanceof RuntimeFuncObject) {
			return RuntimeObjectType.kFunc;
		}
		if (obj instanceof RuntimeArray) {
			return RuntimeObjectType.kArray;
		}
		if (obj instanceof RuntimeMap) {
			return RuntimeObjectType.kMap;
		}
		return RuntimeObjectType.kNull;
	}
	
	public static TokenType toTokenType(RuntimeObjectType obj) {
		switch (obj) {
		case kBool:
			return TokenType.BOOL;
		case kChar:
			return TokenType.CHARACTER;
		case kInt:
			return TokenType.INTEGER;
		case kReal:
			return TokenType.DECIMAL;
		case kString:
			return TokenType.STRING;
		default:
			return TokenType.ERROR;
		}
	}

	public RuntimeObject clone() {
		try {
			return (RuntimeObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new RuntimeObject(null);
	}

	@Override
	public String toString() {
		return type.getName() +
				(obj == null ? "(null)" : "(" + obj.toString() + ")") +
				(readonly ? 'R' : 'r') +
				(copyable ? 'C' : 'c');
	}
}

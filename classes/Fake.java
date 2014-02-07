import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import org.telosys.tools.generator.context.JavaBeanClass;
import org.telosys.tools.generator.context.JavaBeanClassAttribute;
import org.telosys.tools.generator.context.JavaBeanClassForeignKey;
import org.telosys.tools.generator.context.JavaBeanClassForeignKeyColumn;
import org.telosys.tools.generator.context.JavaBeanClassLink;

public class Fake {
	
	//private short currentCounter = 0 ;
	
	public String getFakeValue (JavaBeanClassAttribute attribute, String counterVariableName) {
		
		String fullType = attribute.getFullType() ;
		String simpleType = attribute.getWrapperType() ;
		if ( "Boolean".equals(simpleType) ) {
			return "true" ;
		}
		else if ( "Byte".equals(simpleType) ) {
			return "(byte) (" + counterVariableName + " < Byte.MAX_VALUE  ? " + counterVariableName + " : Byte.MAX_VALUE )" ;
		}
		else if ( "Short".equals(simpleType) ) {
			return "(short) (" + counterVariableName + " < Short.MAX_VALUE  ? " + counterVariableName + " : Short.MAX_VALUE )" ;
		}
		else if ( "Integer".equals(simpleType) ) {
			return "" + counterVariableName + " < Integer.MAX_VALUE  ? " + counterVariableName + " : Integer.MAX_VALUE " ;
		}
		else if ( "Long".equals(simpleType) ) {
			return counterVariableName ;
		}
		else if ( "Float".equals(simpleType) ) {
			//return counter < Short.MAX_VALUE  ? counter+".45F" : Short.MAX_VALUE+".45F" ;
			return "(float) " + counterVariableName ;
		}
		else if ( "Double".equals(simpleType) ) {
			//return counter < Short.MAX_VALUE  ? counter+".75" : Short.MAX_VALUE+".75" ;
			return "(double) " + counterVariableName ;
		}
		else if ( "BigInteger".equals(simpleType) ) {
			return "new BigInteger( \"\" + " + counterVariableName + " )" ;
		}
		else if ( "BigDecimal".equals(simpleType) ) {
			return "new BigDecimal( \"\" + " + counterVariableName + " )" ;
		}
		else if ( "String".equals(simpleType) ) {
			return buildString( attribute, counterVariableName) ;
		}

		//--- Full Type
		else if ( "java.util.Date".equals(fullType) ) {
			return "new java.util.Date()" ;
		}
		else if ( "java.sql.Date".equals(fullType) ) {
			return "new java.sql.Date( (new java.util.Date()).getTime() )" ;
		}
		else if ( "java.sql.Time".equals(fullType) ) {
			return "new java.sql.Time( (new java.util.Date()).getTime() )" ;
		}
		else if ( "java.sql.Timestamp".equals(fullType) ) {
			return "new java.sql.Timestamp( (new java.util.Date()).getTime() )" ;
		}
		else {
			return "null" ;
		}
	}
	
	private char currentChar = 'A'-1 ;
	private char nextChar() {
		currentChar++ ;
		if ( currentChar > 'Z') {
			currentChar = 'A' ;
		}
		return currentChar ;
	}
	
	private String buildDynamicShortString(int n) {
		StringBuilder sb = new StringBuilder();
		char c = 'A' ;
		short modulo = 26 ;
		for ( int i = 0 ; i < n ; i++ ) {
			if ( i > 0 ) {
				sb.append(" + " );
			}
			sb.append("(char)('" + c +"' + (i%" + modulo + ") )" );
			c++ ;
			modulo--;
		}
		return sb.toString();
	}

	private String buildString(JavaBeanClassAttribute attribute, String counterVariableName) {
//		int min = 0;
//		try {
//			min = Integer.parseInt(attribute.getMinLength()) ;
//		} catch (NumberFormatException e) {
//			min = 0 ;
//		}
		int max = 0;
		try {
			max = Integer.parseInt(attribute.getMaxLength()) ;
		} catch (NumberFormatException e) {
			max = 20 ;
		}
//		if ( min > max ) {
//			max = min ;
//		}

		
		String attributeName = attribute.getName() ;
		final int counterSize = 5 ;
		StringBuffer sb = new StringBuffer();
		
		if ( ( attributeName.length() + counterSize ) <= max ) {
			//--- Attribute name + counter ( e.g. : "firstName" + i )
			sb.append('"');
			sb.append( attributeName ) ;
			sb.append('"');
			sb.append('+');
			sb.append(counterVariableName);
		}
		else {
			//--- "A" + counter ( e.g. : "A" + i )
			if ( counterSize + 1 <= max) {
				sb.append('"');
				sb.append('A');
				sb.append('"');
				sb.append('+');
				sb.append(counterVariableName);
			}
//			else if ( counterSize <= max) {
//				sb.append('"');
//				sb.append('"');
//				sb.append('+');
//				sb.append(counterVariableName);
//			}
			else {
				// "" + (char)('A' + (i%26) ) + (char)('B' + (i%25) ) + etc
				sb.append('"');
				sb.append('"');
				sb.append('+');
				sb.append(buildDynamicShortString(max));
			}
		}
		
		return sb.toString();
	}
}

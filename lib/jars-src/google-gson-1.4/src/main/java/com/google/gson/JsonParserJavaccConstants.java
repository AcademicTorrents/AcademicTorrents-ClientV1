/* Generated By:JavaCC: Do not edit this line. JsonParserJavaccConstants.java */
package com.google.gson;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
interface JsonParserJavaccConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int EXPONENT = 5;
  /** RegularExpression Id. */
  int DIGITS = 6;
  /** RegularExpression Id. */
  int NULL = 7;
  /** RegularExpression Id. */
  int NAN = 8;
  /** RegularExpression Id. */
  int INFINITY = 9;
  /** RegularExpression Id. */
  int BOOLEAN = 10;
  /** RegularExpression Id. */
  int IDENTIFIER_SANS_EXPONENT = 11;
  /** RegularExpression Id. */
  int IDENTIFIER_STARTS_WITH_EXPONENT = 12;
  /** RegularExpression Id. */
  int HEX_CHAR = 13;
  /** RegularExpression Id. */
  int UNICODE_CHAR = 14;
  /** RegularExpression Id. */
  int ESCAPE_CHAR = 15;
  /** RegularExpression Id. */
  int SINGLE_QUOTE_LITERAL = 16;
  /** RegularExpression Id. */
  int DOUBLE_QUOTE_LITERAL = 17;
  /** RegularExpression Id. */
  int QUOTE = 18;
  /** RegularExpression Id. */
  int ENDQUOTE = 20;
  /** RegularExpression Id. */
  int CHAR = 21;
  /** RegularExpression Id. */
  int CNTRL_ESC = 22;
  /** RegularExpression Id. */
  int HEX = 24;
  /** RegularExpression Id. */
  int HEX_ESC = 25;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int STRING_STATE = 1;
  /** Lexical state. */
  int ESC_STATE = 2;
  /** Lexical state. */
  int HEX_STATE = 3;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<EXPONENT>",
    "<DIGITS>",
    "\"null\"",
    "\"NaN\"",
    "\"Infinity\"",
    "<BOOLEAN>",
    "<IDENTIFIER_SANS_EXPONENT>",
    "<IDENTIFIER_STARTS_WITH_EXPONENT>",
    "<HEX_CHAR>",
    "<UNICODE_CHAR>",
    "<ESCAPE_CHAR>",
    "<SINGLE_QUOTE_LITERAL>",
    "<DOUBLE_QUOTE_LITERAL>",
    "\"\\\"\"",
    "\"\\\\\"",
    "<ENDQUOTE>",
    "<CHAR>",
    "<CNTRL_ESC>",
    "\"u\"",
    "<HEX>",
    "<HEX_ESC>",
    "\")]}\\\'\\n\"",
    "\"{\"",
    "\"}\"",
    "\",\"",
    "\":\"",
    "\"[\"",
    "\"]\"",
    "\"-\"",
    "\".\"",
  };

}

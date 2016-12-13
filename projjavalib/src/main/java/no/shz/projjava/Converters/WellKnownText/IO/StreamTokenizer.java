package no.shz.projjava.converters.wellKnownText.io;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import no.shz.projjava.converters.wellKnownText.TokenType;
import no.shz.utilities.TextReader;

/**
 * Copyright 2005 - 2009 - Morten Nielsen (www.sharpgis.net)
 * This file is part of ProjNet.
 * ProjNet is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ProjNet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ProjNet; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * SOURCECODE IS MODIFIED FROM ANOTHER WORK AND IS ORIGINALLY BASED ON GeoTools.NET:
 *
 *  Copyright (C) 2002 Urban Science Applications, Inc. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

/**
 * ProjJava - ported by imshz (https://github.com/imshz/projjava)
 *
 * ProjJava is a android compatible point-to-point coordinate conversions java library.
 *
 * This library is a port of Proj.NET - ProjNet is free software published under
 * the terms of GNU Lesser General Public License, as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ProjJava is published under the same license and will allways follow the same
 * restrictions as the original project.
 */

public class StreamTokenizer {
    private final NumberFormat _nfi = NumberFormat.getNumberInstance(Locale.US);

    private TokenType _currentTokenType = TokenType.values()[0];
    private TextReader _reader;
    private String _currentToken;

    private int _lineNumber = 1;
    private int _colNumber = 1;
    private boolean _ignoreWhitespace;

    /**
     * Initializes a new instance of the StreamTokenizer class.
     *
     * @param reader           A TextReader with some text to read.
     * @param ignoreWhitespace Flag indicating whether whitespace should be ignored.
     */
    public StreamTokenizer(TextReader reader, boolean ignoreWhitespace) {
        if (reader == null) {
            throw new IllegalArgumentException("reader");
        }

        _reader = reader;
        _ignoreWhitespace = ignoreWhitespace;
    }

    /**
     * Determines a characters type (e.g. number, symbols, character).
     *
     * @param character The character to determine.
     * @return The TokenType the character is.
     */
    private static TokenType getType(char character) {
        if (Character.isDigit(character)) {
            return TokenType.Number;
        }
        if (Character.isLetter(character)) {
            return TokenType.Word;
        }
        if (character == '\n') {
            return TokenType.Eol;
        }
        if (Character.isWhitespace(character) || Character.isISOControl(character)) {
            return TokenType.Whitespace;
        }
        return TokenType.Symbol;
    }

    /**
     * The current line number of the stream being read.
     */
    protected final int getLineNumber() {
        return _lineNumber;
    }

    /**
     * The current column number of the stream being read.
     */
    protected final int getColumn() {
        return _colNumber;
    }

    private boolean getIgnoreWhitespace() {
        return _ignoreWhitespace;
    }

    /**
     * If the current token is a number, this field contains the value of that number. The current token is a number when the value of the ttype field is TT_NUMBER.
     *
     * @throws IllegalArgumentException Current token is not a number in a valid format.
     */
    public final double getNumericValue() {
        String number = getStringValue();

        try {
            if (getTokenType() == TokenType.Number) {
                return _nfi.parse(number).doubleValue();
            }
        } catch (java.text.ParseException ignored) {
        }

        String s = String.format(Locale.US, "The token '%1$s' is not a number at line %2$s column %3$s.", number, getLineNumber(), getColumn());
        throw new IllegalArgumentException(s);
    }

    /**
     * If the current token is a word token, this field contains a string giving the characters of the word token.
     */
    public final String getStringValue() {
        return _currentToken;
    }

    /**
     * Gets the token type of the current token.
     *
     * @return TokenType
     */
    private TokenType getTokenType() {
        return _currentTokenType;
    }

    /**
     * Returns the next token.
     *
     * @param ignoreWhitespace Determines is whitespace is ignored. True if whitespace is to be ignored.
     * @return The TokenType of the next token.
     */
    protected final TokenType nextToken(boolean ignoreWhitespace) throws IOException {
        return ignoreWhitespace ? nextNonWhitespaceToken() : nextTokenAny();
    }

    /**
     * Returns the next token.
     *
     * @return The TokenType of the next token.
     */
    public final TokenType nextToken() throws IOException {
        return nextToken(getIgnoreWhitespace());
    }

    private TokenType nextTokenAny() throws IOException {
        _currentToken = "";
        _currentTokenType = TokenType.Eof;
        boolean finished = _reader.eof();

        boolean isNumber = false;
        boolean isWord = false;

        while (!finished) {
            char currentCharacter = _reader.getAnyChar();
            char nextCharacter = _reader.peek();
            _currentTokenType = getType(currentCharacter);
            TokenType nextTokenType = getType(nextCharacter);

            if (isWord && currentCharacter == '_') {
                _currentTokenType = TokenType.Word;
            }

            if (isWord && _currentTokenType == TokenType.Number) {
                _currentTokenType = TokenType.Word;
            }

            if (!isNumber) {
                if (_currentTokenType == TokenType.Word && nextCharacter == '_') {
                    nextTokenType = TokenType.Word;
                    isWord = true;
                }
                if (_currentTokenType == TokenType.Word && nextTokenType == TokenType.Number) {
                    nextTokenType = TokenType.Word;
                    isWord = true;
                }
            }

            if (currentCharacter == '-' && nextTokenType == TokenType.Number && !isNumber) {
                _currentTokenType = TokenType.Number;
                nextTokenType = TokenType.Number;
            }

            if (isNumber && nextTokenType == TokenType.Number && currentCharacter == '.') {
                _currentTokenType = TokenType.Number;
            }
            if (_currentTokenType == TokenType.Number && nextCharacter == '.' && !isNumber) {
                nextTokenType = TokenType.Number;
                isNumber = true;
            }

            if (isNumber) {
                if (_currentTokenType == TokenType.Number && nextCharacter == 'E') {
                    nextTokenType = TokenType.Number;
                }
                if (currentCharacter == 'E' && (nextCharacter == '-' || nextCharacter == '+')) {
                    _currentTokenType = TokenType.Number;
                    nextTokenType = TokenType.Number;
                }
                if ((currentCharacter == 'E' || currentCharacter == '-' || currentCharacter == '+') && nextTokenType == TokenType.Number) {
                    _currentTokenType = TokenType.Number;
                }
            }


            _colNumber++;
            if (_currentTokenType == TokenType.Eol) {
                _lineNumber++;
                _colNumber = 1;
            }

            _currentToken = _currentToken + currentCharacter;

            finished = _currentTokenType != nextTokenType || _currentTokenType == TokenType.Symbol && currentCharacter != '-' || _reader.eof();
        }
        return _currentTokenType;
    }

    /**
     * Returns next token that is not whitespace.
     *
     * @return TokenType
     */
    private TokenType nextNonWhitespaceToken() throws IOException {

        TokenType tokentype = nextTokenAny();
        while (tokentype == TokenType.Whitespace || tokentype == TokenType.Eol) {
            tokentype = nextTokenAny();
        }
        return tokentype;
    }
}
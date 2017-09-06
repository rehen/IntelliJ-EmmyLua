/*
 * Copyright (c) 2017. tangzx(love.tangzx@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tang.intellij.lua.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.tang.intellij.lua.comment.psi.LuaDocTokenType;
import com.tang.intellij.lua.comment.psi.LuaDocTypes;
import com.tang.intellij.lua.psi.LuaStringTypes;
import com.tang.intellij.lua.psi.LuaTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangzx
 * Date : 2015/11/15.
 */
public class LuaSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TokenSet KEYWORD_TOKENS = TokenSet.create(
            LuaTypes.AND,
            LuaTypes.BREAK,
            LuaTypes.DO,
            LuaTypes.ELSE,
            LuaTypes.ELSEIF,
            LuaTypes.END,
            LuaTypes.FOR,
            LuaTypes.FUNCTION,
            LuaTypes.IF,
            LuaTypes.IN,
            LuaTypes.LOCAL,
            LuaTypes.NOT,
            LuaTypes.OR,
            LuaTypes.REPEAT,
            LuaTypes.RETURN,
            LuaTypes.THEN,
            LuaTypes.UNTIL,
            LuaTypes.WHILE,

            //lua5.3
            LuaTypes.DOUBLE_COLON,
            LuaTypes.GOTO
    );
    public static final TokenSet PRIMITIVE_TYPE_SET = TokenSet.create(
            LuaTypes.FALSE,
            LuaTypes.NIL,
            LuaTypes.TRUE
    );
    public static final TokenSet DOC_KEYWORD_TOKENS = TokenSet.create(
            LuaDocTypes.TAG_PARAM,
            LuaDocTypes.TAG_RETURN,
            LuaDocTypes.CLASS,
            LuaDocTypes.MODULE,
            LuaDocTypes.TYPE,
            LuaDocTypes.FIELD,
            LuaDocTypes.LANGUAGE,
            LuaDocTypes.OVERLOAD
    );

    private static final Map<IElementType, TextAttributesKey> ourMap1;
    private static final Map<IElementType, TextAttributesKey> ourMap2;

    static {
        ourMap1 = new HashMap<>();
        ourMap2 = new HashMap<>();

        //key words
        fillMap(ourMap1, KEYWORD_TOKENS, LuaHighlightingData.KEYWORD);
        fillMap(ourMap1, LuaHighlightingData.SEMICOLON, LuaTypes.SEMI);
        fillMap(ourMap1, LuaHighlightingData.COMMA, LuaTypes.COMMA);
        fillMap(ourMap1, LuaHighlightingData.OPERATORS, LuaTypes.BINARY_OP, LuaTypes.UNARY_OP);
        fillMap(ourMap1, LuaHighlightingData.BRACKETS, LuaTypes.LBRACK, LuaTypes.RBRACK);
        fillMap(ourMap1, LuaHighlightingData.BRACES, LuaTypes.LCURLY, LuaTypes.RCURLY);
        fillMap(ourMap1, LuaHighlightingData.PARENTHESES, LuaTypes.LPAREN, LuaTypes.RPAREN);
        //comment
        fillMap(ourMap1, LuaHighlightingData.LINE_COMMENT, LuaTypes.SHEBANG);
        fillMap(ourMap1, LuaHighlightingData.DOC_COMMENT, LuaTypes.SHEBANG_CONTENT);

        fillMap(ourMap1, LuaHighlightingData.LINE_COMMENT, LuaTypes.SHORT_COMMENT, LuaTypes.BLOCK_COMMENT);
        fillMap(ourMap1, LuaHighlightingData.DOC_COMMENT, LuaTypes.REGION, LuaTypes.ENDREGION);
        fillMap(ourMap1, DOC_KEYWORD_TOKENS, LuaHighlightingData.DOC_COMMENT_TAG);
        fillMap(ourMap1, LuaHighlightingData.DOC_COMMENT_TAG, LuaDocTypes.TAG_NAME);
        //primitive types
        fillMap(ourMap1, LuaHighlightingData.NUMBER, LuaTypes.NUMBER);
        fillMap(ourMap1, LuaHighlightingData.STRING, LuaTypes.STRING);
        fillMap(ourMap1, PRIMITIVE_TYPE_SET, LuaHighlightingData.PRIMITIVE_TYPE);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new LuaFileLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType type) {
        if (ourMap1.containsKey(type))
            return pack(ourMap1.get(type), ourMap2.get(type));
        //comment default
        else if (type instanceof LuaDocTokenType)
            return pack(LuaHighlightingData.DOC_COMMENT);
        //for string
        else if (type == LuaStringTypes.NEXT_LINE)
            return pack(DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
        else if (type == StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN)
            return pack(DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
        else if (type == StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN)
            return pack(DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
        else if (type == LuaStringTypes.INVALID_NEXT_LINE)
            return pack(DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
        return pack(null);
    }
}

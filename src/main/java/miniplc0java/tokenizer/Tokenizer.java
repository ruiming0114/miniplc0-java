package miniplc0java.tokenizer;

import miniplc0java.error.TokenizeError;
import miniplc0java.error.ErrorCode;
import miniplc0java.util.Pos;

public class Tokenizer {

    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    // 这里本来是想实现 Iterator<Token> 的，但是 Iterator 不允许抛异常，于是就这样了
    /**
     * 获取下一个 Token
     * 
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexUInt();
        } else if (Character.isAlphabetic(peek)) {
            return lexIdentOrKeyword();
        } else {
            return lexOperatorOrUnknown();
        }
    }

    private Token lexUInt() throws TokenizeError {
        // 请填空：
        // 直到查看下一个字符不是数字为止:
        // -- 前进一个字符，并存储这个字符
        StringBuilder str = new StringBuilder();
        Pos startPos = it.currentPos();
        Pos endPos = null;
        while (Character.isDigit(it.peekChar()) && !it.isEOF()){
            str.append(it.nextChar());
            endPos = it.currentPos();
        }
        // 解析存储的字符串为无符号整数
        // 解析成功则返回无符号整数类型的token，否则返回编译错误
        try{
            int res = Integer.parseInt(str.toString());
            return new Token(TokenType.Uint,res,startPos,endPos);
        }catch (Exception e){
            // Token 的 Value 应填写数字的值
            throw new TokenizeError(ErrorCode.IntegerOverflow,startPos);
        }
    }

    private Token lexIdentOrKeyword() throws TokenizeError {
        // 请填空：
        // 直到查看下一个字符不是数字或字母为止:
        // -- 前进一个字符，并存储这个字符
        StringBuilder str = new StringBuilder();
        Pos startPos = it.currentPos();
        Pos endPos = null;
        while ((Character.isAlphabetic(it.peekChar()) || Character.isDigit(it.peekChar())) && !it.isEOF()) {
            str.append(it.nextChar());
            endPos = it.currentPos();
        }
        // 尝试将存储的字符串解释为关键字
        // -- 如果是关键字，则返回关键字类型的 token
        // -- 否则，返回标识符
        switch (str.toString()) {
            case "begin":
                return new Token(TokenType.Begin, str.toString(), startPos, endPos);
            case "end":
                return new Token(TokenType.End, str.toString(), startPos, endPos);
            case "const":
                return new Token(TokenType.Const, str.toString(), startPos, endPos);
            case "var":
                return new Token(TokenType.Var, str.toString(), startPos, endPos);
            case "print":
                return new Token(TokenType.Print, str.toString(), startPos, endPos);
            default:
                return new Token(TokenType.Ident, str.toString(), startPos, endPos);
        }
        // Token 的 Value 应填写标识符或关键字的字符串
        // throw new Error("Not implemented");
    }

    private Token lexOperatorOrUnknown() throws TokenizeError {
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.Plus, '+', it.previousPos(), it.currentPos());
            case '-':
                // 填入返回语句
                return new Token(TokenType.Minus, '-', it.previousPos(), it.currentPos());
            case '*':
                // 填入返回语句
                return new Token(TokenType.Mult, '*', it.previousPos(), it.currentPos());
            case '/':
                // 填入返回语句
                return new Token(TokenType.Div, '/', it.previousPos(), it.currentPos());
            case '=':
                return new Token(TokenType.Equal, '=', it.previousPos(), it.currentPos());
            case ';':
                return new Token(TokenType.Semicolon, ';', it.previousPos(), it.currentPos());
            case '(':
                return new Token(TokenType.LParen, '(', it.previousPos(), it.currentPos());
            case ')':
                return new Token(TokenType.RParen, ')', it.previousPos(), it.currentPos());
            default:
                // 不认识这个输入，摸了
                throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}

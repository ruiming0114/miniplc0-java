package miniplc0java;

import miniplc0java.error.TokenizeError;
import miniplc0java.tokenizer.StringIter;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;
import miniplc0java.tokenizer.Tokenizer;
import miniplc0java.util.Pos;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class TokenizerTest {
    //因为原Pos类没有重写equals方法，为了便于测试Token，使用PosTest代替Pos类
    public static class PosTest extends Pos {
        public PosTest(int row, int col) {
            super(row, col);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null )
                return false;
            Pos pos = (Pos) o;
            return row == pos.row && col == pos.col;
        }
    }

    @Test
    public void lexUIntTest() throws FileNotFoundException, TokenizeError {
        Scanner scanner = new Scanner(new File("src/test/java/miniplc0java/input","UInt.txt"));
        StringIter it = new StringIter(scanner);
        Tokenizer tokenizer = new Tokenizer(it);
        assertEquals(new Token(TokenType.Uint,123, new PosTest(0, 0), new PosTest(0, 3)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Uint,0, new PosTest(1, 0), new PosTest(1, 1)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Uint,12345, new PosTest(2, 0), new PosTest(2, 5)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Uint,54321, new PosTest(2, 7), new PosTest(2, 12)),tokenizer.nextToken());
    }

    @Test
    public void lexIdentOrKeywordTest() throws FileNotFoundException, TokenizeError {
        Scanner scanner = new Scanner(new File("src/test/java/miniplc0java/input","IdentOrKeyword.txt"));
        StringIter it = new StringIter(scanner);
        Tokenizer tokenizer = new Tokenizer(it);
        assertEquals(new Token(TokenType.Begin,"begin", new PosTest(0, 0), new PosTest(0, 5)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.End,"end", new PosTest(0, 6), new PosTest(0, 9)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Const,"const", new PosTest(1, 0), new PosTest(1, 5)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Var,"var", new PosTest(2, 0), new PosTest(2, 3)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Print,"print", new PosTest(3, 0), new PosTest(3, 5)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Ident,"a1", new PosTest(4, 0), new PosTest(4, 2)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Ident,"aaa", new PosTest(5, 0), new PosTest(5, 3)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Uint,1, new PosTest(6, 0), new PosTest(6, 1)),tokenizer.nextToken());
        assertEquals(new Token(TokenType.Ident,"a", new PosTest(6, 1), new PosTest(6, 2)),tokenizer.nextToken());
    }
}

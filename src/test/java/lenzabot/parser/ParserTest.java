package lenzabot.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ParserTest {
    @Test
    void parse_commandWithoutArgument_returnsEmptyArgument() {
        Parser.ParsedInput parsedInput = Parser.parse("list");

        assertEquals("list", parsedInput.getCommand());
        assertEquals("", parsedInput.getArgument());
    }

    @Test
    void parse_commandWithArgument_splitsAtFirstSpace() {
        Parser.ParsedInput parsedInput = Parser.parse("todo read book");

        assertEquals("todo", parsedInput.getCommand());
        assertEquals("read book", parsedInput.getArgument());
    }

    @Test
    void parse_argumentWithExtraSpaces_trimsArgument() {
        Parser.ParsedInput parsedInput = Parser.parse("todo    read book   ");

        assertEquals("todo", parsedInput.getCommand());
        assertEquals("read book", parsedInput.getArgument());
    }
}

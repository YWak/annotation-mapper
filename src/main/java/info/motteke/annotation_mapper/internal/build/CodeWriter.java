package info.motteke.annotation_mapper.internal.build;

import java.io.Closeable;
import java.io.Flushable;
import java.io.PrintWriter;
import java.io.Writer;

public class CodeWriter implements Closeable, Flushable {

    private final PrintWriter writer;

    private final String indent;

    private int level;

    private boolean newLine;

    public CodeWriter(Writer writer, String indent) {
        this.writer = new PrintWriter(writer);
        this.indent = indent;
        newLine = true;
    }

    public void print(String value) {
        ensureIndent();
        writer.print(value);
    }

    public void print(int value) {
        ensureIndent();
        writer.print(value);
    }

    public void println(String value) {
        print(value);
        println();
    }

    public void println() {
        writer.println();
        newLine = true;
    }

    public void printlnAndIndent() {
        println();
        level++;
    }

    public void outdentAndPrintln() {
        level--;
        println();
    }

    private void ensureIndent() {
        if (!newLine) {
            return;
        }

        newLine = false;

        for (int i = 0; i < level; i++) {
            writer.print(indent);
        }
    }

    @Override
    public void flush() {
        writer.flush();
    }

    @Override
    public void close() {
        writer.close();
    }
}

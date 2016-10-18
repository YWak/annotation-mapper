package info.motteke.annotation_mapper.internal.desc.jsr269;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.seasar.aptina.unit.AptinaTestCase;
import org.seasar.aptina.unit.SourceNotGeneratedException;

public abstract class AbstractCompositeProcessorTestCase extends AptinaTestCase {

    private String expectedSourcesDir;

    protected void setExpectedSourcesDir(String dir) {
        this.expectedSourcesDir = dir;
    }

    protected void assertGeneratedSource(String className) throws SourceNotGeneratedException, IllegalStateException, IOException {
        File resource = new File(expectedSourcesDir, className.replaceAll("\\.", "/") + ".java");
        String expected = read(resource);

        assertThat(getGeneratedSource(className), isSameAs(expected));
    }

    private Matcher<String> isSameAs(final String expected) {
        return new TypeSafeDiagnosingMatcher<String>() {

            @Override
            public void describeTo(Description desc) {

            }

            @Override
            protected boolean matchesSafely(String item, Description desc) {
                String[] actuals = item.split("\r\n|\r|\n");
                String[] expects = expected.split("\r\n|\r|\n");

                int i;
                int length = Math.min(actuals.length, expects.length);

                for (i = 0; i < length; i++) {
                    if (!actuals[i].equals(expects[i])) {
                        desc.appendText("line " + (i + 1) + " was ");
                        desc.appendValue(actuals[i]);
                        return false;
                    }
                }

                if (actuals.length > expects.length) {
                    desc.appendText("expected lines is ");
                    desc.appendValue(expects.length);
                    desc.appendText(" but actual lines was ");
                    desc.appendValue(actuals.length);
                    return false;
                }
                if (actuals.length < expects.length) {
                    desc.appendText("line " + (i + 1) + " is ");
                    desc.appendValue(expects[i]);
                    desc.appendText(" but actual was EOF.");
                    return false;
                }

                return true;
            }
        };
    }

    protected DiagnosticMatcher hasKind(Kind kind) {
        DiagnosticMatcher matcher = new DiagnosticMatcher();
        matcher.kind = kind;

        return matcher;
    }

    private String read(File file) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // nop
                }
            }
        }

        return new String(out.toByteArray());
    }

    protected static class DiagnosticMatcher extends TypeSafeDiagnosingMatcher<Diagnostic<? extends JavaFileObject>>{

        private Long line;

        private Long column;

        private Kind kind;

        private String message;

        public DiagnosticMatcher line(long line) {
            this.line = line;
            return this;
        }

        public DiagnosticMatcher column(long column) {
            this.column = column;
            return this;
        }

        public DiagnosticMatcher meessage(String message) {
            this.message = message;
            return this;
        }

        @Override
        public void describeTo(Description desc) {
            desc.appendText("[");
            desc.appendValue(kind.toString());

            if (line != null) {
                desc.appendText(", line = ");
                desc.appendText(String.valueOf(line));
            }
            if (column != null) {
                desc.appendText(", column = ");
                desc.appendText(String.valueOf(column));
            }
            if (message != null) {
                desc.appendText(", message = '");
                desc.appendText(message);
                desc.appendText("'");
            }
            desc.appendText("]");
        }

        @Override
        protected boolean matchesSafely(Diagnostic<? extends JavaFileObject> actual, Description desc) {
            if (!actual.getKind().equals(kind)) {
                desc.appendText("kind was ");
                desc.appendValue(actual.getKind());
                return false;
            }

            if (line != null && actual.getLineNumber() != line.longValue()) {
                desc.appendText("line was ");
                desc.appendValue(actual.getLineNumber());
                return false;
            }

            if (column != null && actual.getColumnNumber() != column.longValue()) {
                desc.appendText("column was ");
                desc.appendValue(actual.getColumnNumber());
                return false;
            }

            if (message != null && !actual.getMessage(null).equals(message)) {
                desc.appendText("message was ");
                desc.appendValue(message);
                return false;
            }

            return true;
        }

        private DiagnosticMatcher() {

        }
    }
}

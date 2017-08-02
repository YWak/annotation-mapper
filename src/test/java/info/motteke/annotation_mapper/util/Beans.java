package info.motteke.annotation_mapper.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.rules.TestWatcher;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class Beans extends TestWatcher {

    private final Yaml yaml;

    private Class<?> testClass;

    public Beans() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);

        yaml = new Yaml(options);
    }

    @Override
    protected void starting(org.junit.runner.Description description) {
        testClass = description.getTestClass();
    }

    /**
     * 指定したyamlファイルをもとに、オブジェクトを生成して返します。
     *
     * @param fileName yamlファイル。テストクラスと同一パッケージにある。
     * @param clazz yamlから生成されるオブジェクトのクラス
     * @return 生成されたリスト
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> loadAll(String fileName, Class<T> clazz) {
        try (InputStream in = testClass.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new FileNotFoundException(fileName);
            }

            return yaml.loadAs(in, List.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * オブジェクトを検証します。
     *
     * @param actual 実際のオブジェクト
     * @param fileName 想定しているオブジェクトを表現したYAMLファイル
     * @throws AssertionError オブジェクトに差異があった場合
     */
    public <T> void verify(Iterable<T> actual, String fileName) throws AssertionError {
        try (InputStream in = testClass.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new FileNotFoundException(fileName);
            }

            List<String> actualYaml = divideToLines(yaml.dump(actual));
            List<String> expectedYaml = divideToLines(yaml.dump(yaml.load(in)));

            Patch<String> patch = DiffUtils.diff(expectedYaml, actualYaml);
            List<Delta<String>> deltas = patch.getDeltas();

            if (!deltas.isEmpty()) {
                throw new AssertionError(describe(expectedYaml, deltas));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<String> divideToLines(String string) {
        return Arrays.asList(string.split("\r\n|\r|\n"));
    }

    private String describe(List<String> lines, List<Delta<String>> deltas) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("not matched. see yaml");

        final int maxLeft = lines.stream().mapToInt(String::length).max().getAsInt();
        int last = 0;

        for (Delta<String> delta: deltas) {
            Chunk<String> original = delta.getOriginal();
            Chunk<String> revised = delta.getRevised();

            // 差分がない部分を書き出す
            for (int pos = original.getPosition(); last < pos; last++) {
                pw.println(sameLine(lines.get(last), maxLeft));
            }

            // 差分を書き出す
            List<String> oLines = original.getLines();
            List<String> rLines = revised.getLines();
            int i = 0;
            int length = Math.max(oLines.size(), rLines.size());

            while (i < length) {
                pw.println(diffLine(oLines, rLines, i, maxLeft));
                i++;
            }

            // 差分で書き出した分を進める
            last += oLines.size();
        }

        // 残りを書き出す
        lines.stream().skip(last).forEach(s -> pw.println(sameLine(s, maxLeft)));

        return sw.toString();
    }

    /**
     * 値が同じときの表示内容を作成します。
     *
     * @param line 行の内容
     * @param padding 左側の最大文字数
     * @return 値が同じときの表示内容
     */
    private String sameLine(String line, int padding) {
        return pad(line, ' ', "|", line, padding);
    }

    /**
     * 値が異なるときの表示内容を作成します。
     *
     * @param left 左にくる行
     * @param right 右にくる行
     * @param i {@code left}や{@code right}のうち、表示する行のインデックス
     * @param padding 左側の最大文字数
     * @return 値が異なるときの表示内容
     */
    private String diffLine(List<String> left, List<String> right, int i, int padding) {
        String l = getLine(left, i, padding);
        String r = getLine(right, i, padding);

        return pad(l, ' ', ">", r, padding);
    }

    private String getLine(List<String> lines, int i, int fill) {
        if (i <= lines.size()) {
            return lines.get(i);
        } else {
            return repeat('X', fill);
        }
    }

    private String pad(String left, char leftPad, String rightPad, String right, int padding) {
        StringBuilder buf = new StringBuilder();

        buf.append(left);
        buf.append(' ');
        buf.append(repeat(leftPad, padding - left.length()));
        buf.append(rightPad);
        buf.append(right);

        return buf.toString();
    }

    private static String repeat(char c, int n) {
        StringBuilder buf = new StringBuilder(n);
        IntStream.range(0, n).forEach(i -> buf.append(c));

        return buf.toString();
    }
}

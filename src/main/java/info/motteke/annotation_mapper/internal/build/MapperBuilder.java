package info.motteke.annotation_mapper.internal.build;

import info.motteke.annotation_mapper.Mapper;
import info.motteke.annotation_mapper.internal.desc.IAssociation;
import info.motteke.annotation_mapper.internal.desc.IProperty;
import info.motteke.annotation_mapper.internal.desc.IPropertyReader;
import info.motteke.annotation_mapper.internal.desc.IPropertyWriter;
import info.motteke.annotation_mapper.internal.desc.IType;
import info.motteke.annotation_mapper.internal.utils.jsr269.ElementUtils;

import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

public class MapperBuilder {

    private static final String GENERATOR_NAME = Mapper.class.getCanonicalName();

    private final ProcessingEnvironment env;

    private final Map<IAssociation, Integer> mappings;

    private IAssociation association;

    private Element element;

    private CodeWriter writer;

    private String baseClassName;

    private String mapperFullyQualifiedName;

    private String mapperPackageName;

    private String mapperClassName;

    private String mainClassName;

    public MapperBuilder(ProcessingEnvironment env) {
        this.env = env;
        this.mappings = new HashMap<IAssociation, Integer>();
    }

    public void setAssociation(IAssociation association) {
        this.association = association;

        handleAssociation(new AssociationHandler(writer, mappings) {
            private int n = 1;

            @Override
            public void handle(IAssociation association) {
                mappings.put(association, n++);
            }
        });
    }

    public void setElement(Element element) {
        this.baseClassName = ElementUtils.toTypeElement(element).getQualifiedName().toString();
        this.element = element;
    }

    public void build() {

        mainClassName = association.getType().getName();
        mapperFullyQualifiedName = baseClassName + "Mapper";
        mapperPackageName = getPackage(mapperFullyQualifiedName);
        mapperClassName = getClassName(mapperFullyQualifiedName);

        try {
            JavaFileObject filer = env.getFiler().createSourceFile(mapperFullyQualifiedName, element);
            writer = new CodeWriter(filer.openWriter(), "    ");

            writePackage();
            writeClass();

            writer.flush();
        } catch (IOException e) {
            env.getMessager().printMessage(Kind.ERROR, "コード生成に失敗しました。 : " + e.getMessage(), element);
        } finally {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        }
    }

    private String getPackage(String fqn) {
        int index = fqn.lastIndexOf('.');

        if (index == -1) {
            return "";
        } else {
            return fqn.substring(0, index);
        }
    }

    private String getClassName(String fqn) {
        int index = fqn.lastIndexOf('.');

        if (index == -1) {
            return fqn;
        } else {
            return fqn.substring(index + 1);
        }
    }

    private void writePackage() {
        if (!mapperPackageName.isEmpty()) {
            writer.print("package ");
            writer.print(mapperPackageName);
            writer.print(";");
            writer.println();
        }

        writer.println();
    }

    private void writeClass() {
        writer.print("@javax.annotation.Generated(\"");
        writer.print(GENERATOR_NAME);
        writer.print("\")");
        writer.println();

        writer.print("public final class ");
        writer.print(mapperClassName);
        writer.print(" {");
        writer.printlnAndIndent();
        writer.println();

        writeMapping();
        writeEquals();
        writeNullSafeEquals();
        writeConstructor();

        writer.printlnAndOutdent();
        writer.print("}");
        writer.println();
    }

    private void writeMapping() {
        writer.print("public static java.util.Collection<");
        writer.print(mainClassName);
        writer.print("> map(");
        writer.print("java.util.Collection<? extends ");
        writer.print(baseClassName);
        writer.print("> values) {");
        writer.printlnAndIndent();
        writer.println();

        writer.print("java.util.List<");
        writer.print(mainClassName);
        writer.print("> mappedValues = new java.util.ArrayList<");
        writer.print(mainClassName);
        writer.print(">();");
        writer.println();
        writer.println();

        // 変数定義
        writeVars();

        writer.print(baseClassName);
        writer.print(" prev = null;");
        writer.println();
        writer.println();

        writer.print("for (");
        writer.print(baseClassName);
        writer.print(" curr : values) {");
        writer.printlnAndIndent();
        writer.println();

        // キーが変わったか調べて、変わっていたらインスタンスを作りなおす
        writeComparisons();

        writer.print("prev = curr;");
        writer.printlnAndOutdent();
        writer.print("}");
        writer.println();
        writer.println();

        writer.print("return mappedValues;");
        writer.printlnAndOutdent();

        writer.print("}");
        writer.println();
        writer.println();
    }

    private void writeVars() {
        writer.println("// declare variables");

        handleAssociation(new AssociationHandler(writer, mappings) {
            @Override
            public void handle(IAssociation association) {
                int n = mappings.get(association);
                IType type = association.getType();
                IType objectType;

                if (type.isCollection()) {
                    writer.print(type.getName());
                    writer.print("<");
                    writer.print(type.getSubType().getName());
                    writer.print(">");
                    writer.print(" c");
                    writer.print(n);
                    writer.print(" = null;");
                    writer.println();
                }

                if (type.getSubType() == null) {
                    objectType = type;
                } else {
                    objectType = type.getSubType();
                }

                writer.print(objectType.getName());
                writer.print(" o");
                writer.print(n);

                if (!objectType.isPrimitive()) {
                    writer.print(" = null");
                }

                writer.println(";");
            }
        });

        writer.println();
    }

    private void writeComparisons() {

        writer.println("// check if changes");
        handleAssociation(new AssociationHandler(writer, mappings) {
            @Override
            public void handle(IAssociation association) {
                int n = mappings.get(association);
                writer.print("boolean _equals");
                writer.print(n);
                writer.print(" = equals");
                writer.print(n);
                writer.print("(prev, curr);");
                writer.println();
            }
        });

        writer.println();
        writer.println("// create new Object if changes");
        handleAssociation(new AssociationHandler(writer, mappings) {
            @Override
            public void handle(IAssociation association) {
                writer.print("if (");

                String delim = "";

                for (IAssociation a = association; a != null; a = a.getParent()) {
                    int n = mappings.get(a);

                    writer.print(delim);
                    writer.print("_equals");
                    writer.print(n);

                    delim = " || ";
                }

                writer.print(") {");
                writer.printlnAndIndent();

                // インスタンス更新
                // 自身のインスタンスを作りなおす
                // 親要素に登録する
                // 子要素にコレクションがあれば作りなおす

                int n = mappings.get(association);
                String instance = "o" + n;
                IType type = association.getType();
                IType objectType;

                if (type.getSubType() == null) {
                    objectType = type;
                } else {
                    objectType = type.getSubType();
                }

                writer.print(instance);

                if (!objectType.isPrimitive()) {
                    writer.print(" = new ");
                    writer.print(objectType.getName());
                    writer.print("()");
                }

                writer.println(";");

                if (association.getParent() == null) {
                    writer.print("mappedValues.add(");
                    writer.print(instance);
                    writer.print(");");
                    writer.println();
                } else if (type.isCollection()) {
                    String collection = "c" + mappings.get(association);

                    writer.print(collection);
                    writer.print(".add(");
                    writer.print(instance);
                    writer.print(");");
                    writer.println();
                } else {
                    String object = "o" + mappings.get(association.getParent());

                    writer.print(writer(object, getProperty(association.getParent().getType(), association.getName()), instance));
                    writer.println(";");
                }

                for (IAssociation a : association.getAssociations()) {
                    if (a.getType().isCollection()) {
                        String collection = "c" + mappings.get(a);
                        writer.print(collection);
                        writer.print(" = new ");

                        if (type.isSet()) {
                            writer.print("java.util.LinkedHashSet");
                        } else {
                            writer.print("java.util.ArrayList");
                        }

                        writer.print("<");
                        writer.print(a.getType().getSubType().getName());
                        writer.print(">();");
                        writer.println();

                        writer.print(writer(instance, association.getType().getProperty(a.getName()), collection));
                        writer.print(";");
                        writer.println();
                    }
                }

                writer.printlnAndOutdent();
                writer.println("}");
                writer.println();
            }
        });

        writer.println();
        writer.println("// copy values");
        handleAssociation(new AssociationHandler(writer, mappings) {
            @Override
            public void handle(IAssociation association) {
                int n = mappings.get(association);
                String instance = "o" + n;

                for (Entry<IProperty, List<IProperty>> entry : association.getProperties().entrySet()) {
                    String value = reader("curr", entry.getKey());

                    for (IProperty target : entry.getValue()) {
                        writer.print(writer(instance, target, value));
                        writer.println(";");
                    }
                }
            }
        });
    }

    private void writeEquals() {
        handleAssociation(new AssociationHandler(writer, mappings) {
            @Override
            public void handle(IAssociation association) {
                int n = mappings.get(association);

                // declare method
                writer.print("private static boolean equals");
                writer.print(n);
                writer.print("(");
                writer.print(baseClassName);
                writer.print(" o1, ");
                writer.print(baseClassName);
                writer.print(" o2) {");
                writer.printlnAndIndent();

                // compare if not equals
                for (IProperty property : association.getKeys().keySet()) {
                    writer.print("if (");

                    if (property.getType().isPrimitive()) {
                        writer.print(reader("o1", property));
                        writer.print(" != ");
                        writer.print(reader("o2", property));
                    } else {
                        writer.print("!_equals(");
                        writer.print(reader("o1", property));
                        writer.print(", ");
                        writer.print(reader("o2", property));
                        writer.print(")");
                    }

                    writer.print(") {");
                    writer.printlnAndIndent();
                    writer.print("return false;");
                    writer.printlnAndOutdent();
                    writer.print("}");
                    writer.println();
                    writer.println();
                }

                writer.print("return true;");

                // end method
                writer.printlnAndOutdent();
                writer.print("}");
                writer.println();
                writer.println();
            }
        });
    }

    private void writeNullSafeEquals() {
        writer.println("// null safe object comparison");
        writer.print("private static boolean _equals(Object o1, Object o2) {");
        writer.printlnAndIndent();
        writer.print("return ");
        writer.print("(o1 == null || o2 == null) ?");
        writer.print(" (o1 == null && o2 == null) :");
        writer.print(" o1.equals(o2);");
        writer.printlnAndOutdent();
        writer.print("}");
        writer.println();
        writer.println();
    }

    private void writeConstructor() {
        writer.print("private ");
        writer.print(mapperClassName);
        writer.print("() { }");
        writer.println();
        writer.println();
    }

    private String reader(String instance, IProperty property) {
        IPropertyReader reader = property.getReader();

        StringBuilder buf = new StringBuilder();
        buf.append(instance);
        buf.append(".");
        buf.append(reader.getName());

        switch (reader.getAccessor()) {
        case METHOD:
            buf.append("()");
            break;

        case FIELD:
            break;

        default:
            throw new IllegalArgumentException();
        }

        return buf.toString();
    }

    private String writer(String instance, IProperty property, String value) {
        IPropertyWriter writer = property.getWriter();

        StringBuilder buf = new StringBuilder();
        buf.append(instance);
        buf.append(".");
        buf.append(writer.getName());

        switch (writer.getAccessor()) {
        case METHOD:
            buf.append("(");
            buf.append(value);
            buf.append(")");
            break;

        case FIELD:
            buf.append(" = ");
            buf.append(value);
            break;

        default:
            throw new IllegalArgumentException();
        }

        return buf.toString();
    }

    private IProperty getProperty(IType type, String name) {
        if (type.getSubType() == null) {
            return type.getProperty(name);
        } else {
            return type.getSubType().getProperty(name);
        }
    }

    private void handleAssociation(AssociationHandler handler) {
        Deque<IAssociation> queue = new LinkedList<IAssociation>();
        queue.add(association);

        while (!queue.isEmpty()) {
            IAssociation target = queue.poll();

            handler.handle(target);
            for (IAssociation a : target.getAssociations()) {
                queue.add(a);
            }
        }
    }
}

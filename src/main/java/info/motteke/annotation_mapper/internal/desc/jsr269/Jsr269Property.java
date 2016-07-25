package info.motteke.annotation_mapper.internal.desc.jsr269;

import info.motteke.annotation_mapper.internal.desc.IProperty;
import info.motteke.annotation_mapper.internal.desc.IPropertyReader;
import info.motteke.annotation_mapper.internal.desc.IPropertyWriter;
import info.motteke.annotation_mapper.internal.desc.IType;
import info.motteke.annotation_mapper.internal.desc.impl.PropertyReader;
import info.motteke.annotation_mapper.internal.desc.impl.PropertyWriter;

public class Jsr269Property implements IProperty {

    private final IType type;

    private final String name;

    private PropertyReader reader;

    private PropertyWriter writer;

    public Jsr269Property(IType type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public IType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IPropertyReader getReader() {
        return reader;
    }

    @Override
    public IPropertyWriter getWriter() {
        return writer;
    }

    public void updateReader(PropertyReader reader) {
        if (this.reader == null || this.reader.compareTo(reader) < 0) {
            this.reader = reader;
        }
    }

    public void updateWriter(PropertyWriter writer) {
        if (this.writer == null || this.writer.compareTo(writer) < 0) {
            this.writer = writer;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s %s (reader = %s, writer = %s)",
                             getClass().getSimpleName(),
                             type,
                             name,
                             reader,
                             writer);
    }
}

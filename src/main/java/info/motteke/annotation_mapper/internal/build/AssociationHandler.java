package info.motteke.annotation_mapper.internal.build;

import info.motteke.annotation_mapper.internal.desc.IAssociation;

import java.util.Map;

public abstract class AssociationHandler {

    protected final CodeWriter writer;

    protected final Map<IAssociation, Integer> mappings;

    protected AssociationHandler(CodeWriter writer, Map<IAssociation, Integer> mappings) {
        this.writer = writer;
        this.mappings = mappings;
    }

    public abstract void handle(IAssociation association);
}
package info.motteke.annotation_mapper.internal.desc.jsr269;

import info.motteke.annotation_mapper.internal.build.MapperBuilder;
import info.motteke.annotation_mapper.internal.desc.IAssociation;
import info.motteke.annotation_mapper.internal.desc.IMapper;
import info.motteke.annotation_mapper.internal.desc.impl.Association;
import info.motteke.annotation_mapper.internal.utils.jsr269.BeanUtils;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("info.motteke.annotation_mapper.Mapper")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CompositeProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        BeanUtils.init(processingEnv);

        boolean ok = true;

        for (TypeElement typeElement : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                IMapper mapper = new Jsr269Mapper(processingEnv, element);
                IAssociation association = new Association(mapper);

                if (association.hasError()) {
                    continue;
                }

                MapperBuilder builder = new MapperBuilder(processingEnv);
                builder.setElement(element);
                builder.setAssociation(association);
                builder.build();
            }
        }

        return ok;
    }
}

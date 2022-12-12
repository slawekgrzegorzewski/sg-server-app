package pl.sg;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;

public final class SGGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        return super.getJavaPackageName(definition, mode).replace(".public", "");
    }
}
package com.oroplatform.idea.oroplatform.schema;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiReferenceProvider;
import com.oroplatform.idea.oroplatform.intellij.codeAssist.CompletionProviders;
import com.oroplatform.idea.oroplatform.intellij.codeAssist.ReferenceProviders;
import com.oroplatform.idea.oroplatform.schema.requirements.Requirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Scalar implements Element {

    private final Collection<Requirement> requirements = new LinkedList<>();
    private final DefaultValueDescriptor defaultValueDescriptor;

    Scalar(Collection<? extends Requirement> requirements, @Nullable DefaultValueDescriptor defaultValueDescriptor) {
        this.requirements.addAll(requirements);
        this.defaultValueDescriptor = defaultValueDescriptor;
    }

    Scalar(Collection<? extends Requirement> requirements) {
        this(requirements, null);
    }

    Scalar() {
        this(Collections.emptyList());
    }

    @NotNull
    public Collection<Requirement> getRequirements() {
        return Collections.unmodifiableCollection(requirements);
    }

    @Nullable
    public CompletionProvider<CompletionParameters> getProvider(CompletionProviders providers, InsertHandler<LookupElement> insertHandler) {
        return null;
    }

    public DefaultValueDescriptor getDefaultValueDescriptor() {
        return defaultValueDescriptor;
    }

    @Nullable
    public PsiReferenceProvider getProvider(ReferenceProviders providers, InsertHandler<LookupElement> insertHandler) {
        return null;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitScalar(this);
    }
}

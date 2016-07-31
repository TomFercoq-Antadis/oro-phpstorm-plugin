package com.oroplatform.idea.oroplatform.intellij.codeAssist.yml.referenceProvider;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.oroplatform.idea.oroplatform.intellij.codeAssist.PhpClassReference;
import com.oroplatform.idea.oroplatform.intellij.codeAssist.PhpMethodReference;
import com.oroplatform.idea.oroplatform.schema.PhpClass;
import com.oroplatform.idea.oroplatform.schema.PhpMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.*;

import java.util.Collections;

public class PhpCallableReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        if(element instanceof YAMLScalar && element.getParent() instanceof YAMLSequenceItem) {
            final YAMLScalar scalarElement = (YAMLScalar) element;
            final YAMLSequenceItem parent = (YAMLSequenceItem) element.getParent();
            final YAMLSequence sequence = (YAMLSequence) parent.getParent();
            final int index = sequence.getItems().indexOf(parent);

            if(index == 0) {
                return new PsiReference[]{ new PhpClassReference(element, PhpClass.any(), scalarElement.getTextValue(), null, Collections.<String>emptySet()) };
            } else if(index == 1) {
                final YAMLSequenceItem classSequenceItem = sequence.getItems().get(0);
                if(classSequenceItem.getValue() instanceof YAMLScalar) {
                    final YAMLScalar classScalar = (YAMLScalar) classSequenceItem.getValue();
                    return new PsiReference[]{ new PhpMethodReference(element, new PhpMethod(new PhpMethod.PhpMethodStaticMatcher()), classScalar.getTextValue(), scalarElement.getTextValue()) };
                }
            }
        }

        return new PsiReference[0];
    }
}

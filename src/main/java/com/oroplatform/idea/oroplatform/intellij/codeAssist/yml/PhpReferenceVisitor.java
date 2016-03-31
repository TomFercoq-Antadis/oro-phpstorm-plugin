package com.oroplatform.idea.oroplatform.intellij.codeAssist.yml;

import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceRegistrar;
import com.oroplatform.idea.oroplatform.schema.Container;
import com.oroplatform.idea.oroplatform.schema.Scalar;
import com.oroplatform.idea.oroplatform.schema.Visitor;
import org.jetbrains.yaml.psi.YAMLQuotedText;

import static com.intellij.patterns.PlatformPatterns.psiElement;

class PhpReferenceVisitor extends YmlVisitor {

    private final PsiReferenceRegistrar registrar;

    PhpReferenceVisitor(PsiReferenceRegistrar registrar, ElementPattern<? extends PsiElement> capture) {
        super(capture);
        this.registrar = registrar;
    }

    @Override
    protected Visitor nextVisitor(ElementPattern<? extends PsiElement> capture) {
        return new PhpReferenceVisitor(registrar, capture);
    }

    @Override
    protected void handleContainer(Container container, ElementPattern<? extends PsiElement> capture) {
    }

    @Override
    public void visitLiteralPhpClassValue(Scalar.PhpClass phpClass) {
        registrar.registerReferenceProvider(
            psiElement().andOr(
                capture,
                psiElement(YAMLQuotedText.class).withParent(capture)
            ),
            new PhpClassReferenceProvider(phpClass)
        );
    }

    @Override
    public void visitLiteralPhpMethodValue(Scalar.PhpMethod phpMethod) {
        registrar.registerReferenceProvider(capture, new PhpMethodReferenceProvider(phpMethod));
    }
}

package com.oroplatform.idea.oroplatform.intellij.codeAssist.yml;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiReferenceProvider;
import com.oroplatform.idea.oroplatform.intellij.codeAssist.ReferenceProviders;
import com.oroplatform.idea.oroplatform.intellij.codeAssist.referenceProvider.FilePathReferenceProvider;
import com.oroplatform.idea.oroplatform.intellij.codeAssist.yml.referenceProvider.*;
import com.oroplatform.idea.oroplatform.schema.PhpClass;
import com.oroplatform.idea.oroplatform.schema.PhpMethod;
import com.oroplatform.idea.oroplatform.schema.PropertyPath;

class YamlReferenceProviders implements ReferenceProviders {
    @Override
    public PsiReferenceProvider filePath(InsertHandler<LookupElement> insertHandler) {
        return new FilePathReferenceProvider();
    }

    @Override
    public PsiReferenceProvider phpCallback(InsertHandler<LookupElement> insertHandler) {
        return new PhpCallbackReferenceProvider();
    }

    @Override
    public PsiReferenceProvider phpCallable(InsertHandler<LookupElement> insertHandler) {
        return new PhpCallableReferenceProvider();
    }

    @Override
    public PsiReferenceProvider phpClass(PhpClass phpClass, InsertHandler<LookupElement> insertHandler) {
        return new PhpClassReferenceProvider(phpClass, insertHandler);
    }

    @Override
    public PsiReferenceProvider phpField(PropertyPath classPropertyPath, InsertHandler<LookupElement> insertHandler) {
        return new PhpFieldReferenceProvider(classPropertyPath);
    }

    @Override
    public PsiReferenceProvider phpMethod(String pattern, InsertHandler<LookupElement> insertHandler) {
        return new PhpMethodReferenceProvider(new PhpMethod(pattern));
    }

    @Override
    public PsiReferenceProvider twigTemplate(InsertHandler<LookupElement> insertHandler) {
        return new TwigTemplateReferenceProvider();
    }
}

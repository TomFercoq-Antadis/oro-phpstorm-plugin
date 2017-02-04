package com.oroplatform.idea.oroplatform.intellij.indexes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;

import java.util.Collection;

public class TranslationIndex {
    private final Project project;

    private TranslationIndex(Project project) {
        this.project = project;
    }


    public static TranslationIndex instance(Project project) {
        return new TranslationIndex(project);
    }

    public Collection<String> findTranslations() {
        return FileBasedIndex.getInstance().getAllKeys(TranslationFileBasedIndex.KEY, project);
    }

    public Collection<VirtualFile> findFilesContaining(String translation) {
        return FileBasedIndex.getInstance().getContainingFiles(TranslationFileBasedIndex.KEY, translation, GlobalSearchScope.allScope(project));
    }

    public Collection<String> findDomains() {
        return FileBasedIndex.getInstance().getAllKeys(TranslationDomainFileBasedIndex.KEY, project);
    }

}

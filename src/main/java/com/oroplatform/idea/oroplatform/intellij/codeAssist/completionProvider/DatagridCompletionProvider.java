package com.oroplatform.idea.oroplatform.intellij.codeAssist.completionProvider;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.util.ProcessingContext;
import com.oroplatform.idea.oroplatform.intellij.indexes.ConfigurationIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DatagridCompletionProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        final Project project = parameters.getOriginalFile().getProject();

        final Collection<String> datagrids = ConfigurationIndex.instance(project).getDatagrids();

        for (String datagrid : datagrids) {
            result.addElement(LookupElementBuilder.create(datagrid));
        }
    }
}

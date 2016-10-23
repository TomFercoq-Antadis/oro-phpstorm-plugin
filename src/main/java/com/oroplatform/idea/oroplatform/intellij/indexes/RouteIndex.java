package com.oroplatform.idea.oroplatform.intellij.indexes;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.oroplatform.idea.oroplatform.symfony.Route;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RouteIndex {
    private final Project project;
    private final GlobalSearchScope searchScope;

    private RouteIndex(Project project) {
        this.project = project;
        //TODO: restrict to OroPlatformSettings.getAppDir() directory?
        this.searchScope = GlobalSearchScope.allScope(project);
    }

    public static RouteIndex instance(Project project) {
        return new RouteIndex(project);
    }

    public Collection<String> findRouteNames() {
        return FileBasedIndex.getInstance().getAllKeys(RouteFileBasedIndex.KEY, project);
    }

    public Optional<Route> findRoute(@NotNull String name) {
        final List<Route> values = FileBasedIndex.getInstance().getValues(RouteFileBasedIndex.KEY, name, searchScope);

        return values.stream().findFirst();
    }
}

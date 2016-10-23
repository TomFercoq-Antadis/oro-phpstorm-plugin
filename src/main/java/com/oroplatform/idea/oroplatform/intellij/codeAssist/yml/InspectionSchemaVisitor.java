package com.oroplatform.idea.oroplatform.intellij.codeAssist.yml;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.oroplatform.idea.oroplatform.OroPlatformBundle;
import com.oroplatform.idea.oroplatform.schema.*;
import com.oroplatform.idea.oroplatform.schema.requirements.Requirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.*;

import static com.oroplatform.idea.oroplatform.intellij.codeAssist.yml.YamlPsiElements.*;

class InspectionSchemaVisitor implements Visitor {
    private final ProblemsHolder problems;
    private final List<PsiElement> elements = new LinkedList<>();

    InspectionSchemaVisitor(ProblemsHolder problems, List<? extends PsiElement> elements) {
        this.problems = problems;
        this.elements.addAll(elements);
    }

    @Override
    public void visitSequence(Sequence sequence) {
        Visitor visitor = new InspectionSchemaVisitor(problems, getSequenceItems(elements));
        sequence.getType().accept(visitor);
    }

    @Override
    public void visitContainer(Container container) {
        for(YAMLMapping element : filterMappings(elements)) {
            Collection<YAMLKeyValue> keyValues = getKeyValuesFrom(element);

            for(Property property : container.getProperties()) {
                boolean found = false;
                for(YAMLKeyValue keyValue : keyValues) {
                    if(property.nameMatches(keyValue.getName())) {
                        found = true;
                        Visitor visitor = new InspectionSchemaVisitor(problems, Collections.<PsiElement>singletonList(keyValue.getValue()));
                        property.getValueElement().accept(visitor);
                    }
                }

                if(!found && property.isRequired()) {
                    problems.registerProblem(element, OroPlatformBundle.message("inspection.schema.required", property.getName()));
                }
            }

            for(YAMLKeyValue keyValue : keyValues) {
                if(!container.areExtraPropertiesAllowed() && !existsPropertyMatchingTo(container.getProperties(), keyValue.getKeyText())) {
                    problems.registerProblem(keyValue, OroPlatformBundle.message("inspection.schema.notAllowedProperty", keyValue.getKeyText()));
                }
            }
        }
    }

    private boolean existsPropertyMatchingTo(List<Property> properties, String name) {
        for(Property property : properties) {
            if(property.nameMatches(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void visitOneOf(OneOf oneOf) {
        final List<ProblemsHolder> problemsHolders = new LinkedList<>();
        for(Element element : oneOf.getElements()) {
            ProblemsHolder newProblems = new ProblemsHolder(problems.getManager(), problems.getFile(), problems.isOnTheFly());
            Visitor visitor = new InspectionSchemaVisitor(newProblems, elements);
            element.accept(visitor);
            problemsHolders.add(newProblems);
        }

        final NavigableSet<ProblemsHolder> sortedProblemsHolders = getSortedProblemHolders(problemsHolders);

        if(sortedProblemsHolders.size() > 0) {
            ProblemsHolder theShortestProblemsHolder = sortedProblemsHolders.first();
            theShortestProblemsHolder.getResults().forEach(problems::registerProblem);
        }
    }

    @NotNull
    private NavigableSet<ProblemsHolder> getSortedProblemHolders(List<ProblemsHolder> problemsHolders) {
        NavigableSet<ProblemsHolder> sortedProblemsHolders = new TreeSet<>(new ProblemsHolderComparator());
        sortedProblemsHolders.addAll(problemsHolders);
        return sortedProblemsHolders;
    }

    @Override
    public void visitRepeatAtAnyLevel(Repeated repeated) {
        //right now it is not used for inspections, so skip implementation
    }

    @Override
    public void visitScalar(Scalar scalar) {
        for (Requirement requirement : scalar.getRequirements()) {
            for(YAMLScalar element : filterScalars(elements)) {
                for (String error : requirement.getErrors(element.getTextValue())) {
                    problems.registerProblem(element, error);
                }
            }
        }
    }

    private static class ProblemsHolderComparator implements Comparator<ProblemsHolder> {

        private final String pattern = OroPlatformBundle.message("inspection.schema.notAllowedPropertyValue", "PLACEHOLDER", "PLACEHOLDER").replace(".", "\\.").replace("PLACEHOLDER", ".*");

        @Override
        public int compare(ProblemsHolder o1, ProblemsHolder o2) {
            boolean choiceInO1 = containsNotAllowedPropertyValueProblem(o1);
            boolean choiceInO2 = containsNotAllowedPropertyValueProblem(o2);

            if(choiceInO2 == choiceInO1) {
                return o1.getResultCount() - o2.getResultCount();
            } else {
                // prefer problems that does not match to not allowed property value - because that
                // kind problem could define the type of element inside "OneOf" element and is not natural to show problems
                // for schema, that has correct value in "choice" element type
                return choiceInO1 ? 1 : -1;
            }
        }

        private boolean containsNotAllowedPropertyValueProblem(ProblemsHolder o1) {
            for(ProblemDescriptor descriptor : o1.getResults()) {
                if(descriptor.getDescriptionTemplate().matches(pattern)) {
                    return true;
                }
            }
            return false;
        }
    }
}

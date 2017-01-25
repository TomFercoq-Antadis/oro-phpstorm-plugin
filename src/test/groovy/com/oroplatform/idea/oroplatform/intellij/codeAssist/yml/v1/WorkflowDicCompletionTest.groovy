package com.oroplatform.idea.oroplatform.intellij.codeAssist.yml.v1

import com.oroplatform.idea.oroplatform.intellij.codeAssist.CompletionTest
import com.oroplatform.idea.oroplatform.intellij.codeAssist.RandomIdentifiers
import com.oroplatform.idea.oroplatform.schema.SchemasV1

class WorkflowDicCompletionTest extends CompletionTest implements RandomIdentifiers {
    @Override
    String fileName() {
        return SchemasV1.FilePathPatterns.WORKFLOW
    }

    def condition1 = randomIdentifier("condition1")
    def condition2a = randomIdentifier("condition2a")
    def condition2b = randomIdentifier("condition2b")
    def condition3 = randomIdentifier("condition3")
    def unknown = randomIdentifier("unknown")
    def action1 = randomIdentifier("action1")
    def action2a = randomIdentifier("action2a")
    def action2b = randomIdentifier("action2b")
    def action3 = randomIdentifier("action3")

    @Override
    protected void setUp() throws Exception {
        super.setUp()

        configureByText("Resources/config/services.xml",
            """
            |<container>
            |  <services>
            |    <service id="condition1_id">
            |      <tag name="oro_workflow.condition" alias="$condition1"/>
            |    </service>
            |    <service id="condition2_id">
            |      <tag name="oro_workflow.condition" alias="$condition2a|$condition2b"/>
            |    </service>
            |    <service id="condition4_id">
            |      <tag name="oro_workflow.condition" alias="condition4"/>
            |    </service>
            |    <service id="some_service">
            |      <tag name="xxx" alias="$unknown"/>
            |    </service>
            |    <service id="action1_id">
            |      <tag name="oro_workflow.action" alias="$action1"/>
            |    </service>
            |    <service id="action2_id">
            |      <tag name="oro_workflow.action" alias="$action2a|$action2b"/>
            |    </service>
            |  </services>
            |</container>
          """.stripMargin()
        )

        configureByText("Resources/config/services.yml",
            """
            |services:
            |  condition3_id:
            |    tags:
            |      - { name: oro_workflow.condition, alias: $condition3 }
            |  action3_id:
            |    tags:
            |      - { name: oro_workflow.action, alias: $action3 }
            """.stripMargin()
        )
    }

    def void "test: suggest conditions defined in xml file"() {
        suggestions(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          <caret>
            """.stripMargin(),
            ["@$condition1", "@$condition2a", "@$condition2b"],
            ["@$unknown"]
        )
    }

    def void "test: suggest conditions defined in yml file"() {
        suggestions(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          <caret>
            """.stripMargin(),
            ["@$condition3"]
        )
    }

    def void "test: does not suggest conditions as property value"() {
        suggestions(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          @not: <caret>
            """.stripMargin(),
            [],
            ["@$condition1", "@$condition2a", "@$condition2b"]
        )
    }

    def void "test: suggest actions after new line defined in xml file"() {
        suggestions(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        init_actions:
            |          -
            |            <caret>
            """.stripMargin(),
            ["@$action1", "@$action2a", "@$action2b"],
            ["@$unknown"]
        )
    }

    def void "test: suggest actions in the same line defined in xml file"() {
        suggestions(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        init_actions:
            |          - <caret>
            """.stripMargin(),
            ["@$action1", "@$action2a", "@$action2b"],
            ["@$unknown"]
        )
    }

    def void "test: suggest actions defined in yml file"() {
        suggestions(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        init_actions:
            |          -
            |            <caret>
            """.stripMargin(),
            ["@$action3"]
        )
    }

    def void "test: complete conditions as keys"() {
        completion(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          ondition4<caret>
            """.stripMargin(),
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          '@condition4': <caret>
            """.stripMargin(),
        )
    }

    def void "test: complete quoted conditions as keys"() {
        completion(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          'ondition4<caret>'
            """.stripMargin(),
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          '@condition4': <caret>
            """.stripMargin(),
        )
    }

    def void "test: complete quoted at beginning conditions as keys"() {
        completion(
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          'ondition4<caret>
            """.stripMargin(),
            """
            |workflows:
            |  some:
            |    transition_definitions:
            |      some_transition:
            |        conditions:
            |          '@condition4': <caret>
            """.stripMargin(),
        )
    }
}

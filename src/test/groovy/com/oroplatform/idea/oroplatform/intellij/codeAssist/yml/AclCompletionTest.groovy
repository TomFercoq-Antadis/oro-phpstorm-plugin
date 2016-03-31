package com.oroplatform.idea.oroplatform.intellij.codeAssist.yml

import com.oroplatform.idea.oroplatform.intellij.codeAssist.CompletionTest

public class AclCompletionTest extends CompletionTest {

    def void "test: suggest key in new line"() {
        suggestions(
            """
            |some_id:
            |  <caret>
            """.stripMargin(),

            ["type", "label"]
        )
    }

    def void "test: suggest key in new line when the value is defined"() {
        suggestions(
            """
            |some_id:
            |  <caret>: entity
            """.stripMargin(),

            ["type", "label"]
        )
    }

    def void "test: suggest key in new line without value after some property"() {
        suggestions(
            """
            |some_id:
            |  label: abc
            |  <caret>
            """.stripMargin(),

            ["type", "permission"]
        )
    }

    def void "test: suggest key in new line after some property when the value is defined"() {
        suggestions(
            """
            |some_id:
            |  label: abc
            |  <caret>: entity
            """.stripMargin(),

            ["type", "permission"]
        )
    }

    def void "test: does not suggest keys as property values"() {
        suggestions(
            """
            |some_id:
            |  label: <caret>
            """.stripMargin(),

            [],
            ["type"]
        )
    }

    //TODO: for more sophisticated cases
    def void "ignored test: does not suggest keys as property values at top level"() {
        suggestions(
            """
            |some_id: <caret>
            """.stripMargin(),

            [],
            ["type"]
        )
    }

    def void "test: suggest key in hash object"() {
        suggestions(
            """
            |some_id: { <caret> }
            """.stripMargin(),

            ["type", "label"]
        )
    }

    def void "test: does not suggest keys as values in hash object"() {
        suggestions(
            """
            |some_id: { label: <caret> }
            """.stripMargin(),

            [],
            ["type"]
        )
    }

    def void "test: suggest key in hash object as the second key"() {
        suggestions(
            """
            |some_id: { label: value, <caret> }
            """.stripMargin(),

            ["type", "permission"]
        )
    }

    def void "test: suggest key in has object when the value is defined"() {
        suggestions(
            """
            |some_id: { <caret>: entity }
            """.stripMargin(),

            ["type", "label"]
        )
    }

    def void "test: suggest choice scalar values"() {
        suggestions(
            """
            |some_id:
            |  permission: <caret>
            """.stripMargin(),

            ["VIEW", "EDIT"],
            ["type", "label"]
        )
    }

    def void "test: suggest choice scalar values inside hash object"() {
        suggestions(
            """
            |some_id: { type: <caret> }
            """.stripMargin(),

            ["entity", "action"],

        )
    }

    def void "test: suggest key inside sequence"() {
        suggestions(
            """
            |some_id:
            |  bindings:
            |    - { <caret> }
            """.stripMargin(),

            ["class", "method"],
            ["type", "label"]
        )
    }

    def void "test: suggest key inside sequence when value is defined"() {
        suggestions(
            """
            |some_id:
            |  bindings:
            |    - { <caret>: someClass }
            """.stripMargin(),

            ["class", "method"],
            ["type", "label"]
        )
    }

    def void "test: does not suggest keys of object one level up for sequence notation"() {
        suggestions(
            """
            |some_id:
            |  - { <caret> }
            """.stripMargin(),

            [],
            ["type"]
        )
    }

}
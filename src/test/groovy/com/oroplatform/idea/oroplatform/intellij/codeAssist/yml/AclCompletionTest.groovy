package com.oroplatform.idea.oroplatform.intellij.codeAssist.yml

import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.junit.Ignore;

public class AclCompletionTest extends LightPlatformCodeInsightFixtureTestCase {

    def void testSuggestCompoundValueKey() {
        completion(
            """
            |some_id:
            |  t<caret>
            """.stripMargin(),
            """
            |some_id:
            |  type<caret>
            """.stripMargin()
        )
    }

    def void testSuggestCompoundValueKey_withValue() {
        completion(
            """
            |some_id:
            |  t<caret>: entity
            """.stripMargin(),
            """
            |some_id:
            |  type: entity
            """.stripMargin()
        )
    }

    def void testSuggestCompoundValueKey_afterOtherKey() {
        completion(
            """
            |some_id:
            |  label: abc
            |  t<caret>
            """.stripMargin(),
            """
            |some_id:
            |  label: abc
            |  type
            """.stripMargin()
        )
    }

    def void testSuggestCompoundValueKey_withValue_andAfterOtherKey() {
        completion(
            """
            |some_id:
            |  label: abc
            |  t<caret>: entity
            """.stripMargin(),
            """
            |some_id:
            |  label: abc
            |  type: entity
            """.stripMargin()
        )
    }

    def void testSuggestCompoundValue_doesNotSuggestKeyAsValue() {
        completion(
            """
            |some_id:
            |  label: t<caret>
            """.stripMargin(),
            """
            |some_id:
            |  label: t
            """.stripMargin()
        )
    }

    //TODO: for more sophisticated cases
    def void ignoreTestSuggestKeyValueOnTopLevel_doesNotSuggestKeyAsValue() {
        completion(
            """
            |some_id: t<caret>
            """.stripMargin(),
            """
            |some_id: t
            """.stripMargin()
        )
    }

    def void testSuggestCompoundValueInHash() {
        completion(
            """
            |some_id: { t<caret> }
            """.stripMargin(),
            """
            |some_id: { type }
            """.stripMargin()
        )
    }

    def void testSuggestCompoundValueInHash_doesNotSuggestKeyAsValue() {
        completion(
            """
            |some_id: { label: t<caret> }
            """.stripMargin(),
            """
            |some_id: { label: t }
            """.stripMargin()
        )
    }

    def void testSuggestCompoundValueInHash_suggestNextKey() {
        completion(
            """
            |some_id: { label: value, t<caret> }
            """.stripMargin(),
            """
            |some_id: { label: value, type }
            """.stripMargin()
        )
    }

    def void testSuggestCompoundValueInHash_withValue() {
        completion(
            """
            |some_id: { t<caret>: entity }
            """.stripMargin(),
            """
            |some_id: { type: entity }
            """.stripMargin()
        )
    }

    private def completion(String contents, String expected) {
        myFixture.configureByText("acl.yml", contents)
        def elements = myFixture.completeBasic()

        if(elements != null && elements.length == 1) {
            myFixture.finishLookup(Lookup.NORMAL_SELECT_CHAR)
        }

        myFixture.checkResult(expected.replace("\r", ""))
    }

}
package org.sonar.plugins.findbugs.scala;

import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.plugins.findbugs.rules.FakeRuleScalaFinder;
import org.sonar.plugins.findbugs.rules.FbContribScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindbugsScalaRulesDefinition;
import org.sonar.plugins.scala.Scala;

public abstract class FindbugsTests {
    protected RulesProfile createRulesProfileWithActiveRulesScala(boolean findbugs, boolean fbContrib, boolean findsecbug
    ) {
        RulesProfile profile = RulesProfile.create();
        profile.setName("FindBugs Scala");
        profile.setLanguage(Scala.KEY);
        RuleFinder ruleFinder = FakeRuleScalaFinder.createWithAllRules();
        if (findbugs) {
            for (Rule rule : ruleFinder.findAll(RuleQuery.create().withRepositoryKey(FindbugsScalaRulesDefinition.REPOSITORY_KEY))) {
                profile.activateRule(rule, null);
            }
        }
        if (fbContrib) {
            for (Rule rule : ruleFinder.findAll(RuleQuery.create().withRepositoryKey(FbContribScalaRulesDefinition.REPOSITORY_KEY))) {
                profile.activateRule(rule, null);
            }
        }
        if (findsecbug) {
            for (Rule rule : ruleFinder.findAll(RuleQuery.create().withRepositoryKey(FindSecurityBugsScalaRulesDefinition.REPOSITORY_KEY))) {
                rule.setRepositoryKey(FindSecurityBugsRulesDefinition.REPOSITORY_KEY);
                profile.activateRule(rule, null);
            }
        }

        return profile;
    }
}

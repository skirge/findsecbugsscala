/*
 * SonarQube Findbugs Plugin
 * Copyright (C) 2012 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.findbugs.scala;

import org.sonar.plugins.findbugs.FindbugsCategory;
import org.sonar.plugins.findbugs.FindbugsConstants;
import org.sonar.plugins.findbugs.FindbugsLevelUtils;
import org.sonar.plugins.findbugs.rules.FbContribScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindSecurityBugsScalaRulesDefinition;
import org.sonar.plugins.findbugs.rules.FindbugsScalaRulesDefinition;
import org.sonar.plugins.scala.Scala;
import com.google.common.collect.Iterables;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.profiles.ProfileImporter;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.findbugs.language.Jsp;
import org.sonar.plugins.findbugs.rules.*;
import org.sonar.plugins.findbugs.xml.FindBugsFilter;
import org.sonar.plugins.java.Java;

import java.io.Reader;
import java.util.Map;

public class FindbugsScalaProfileImporter extends ProfileImporter {

    private final RuleFinder ruleFinder;
    private static final Logger LOG = LoggerFactory.getLogger(FindbugsScalaProfileImporter.class);

    public FindbugsScalaProfileImporter(RuleFinder ruleFinder) {
        super(FindbugsScalaRulesDefinition.REPOSITORY_KEY, FindbugsConstants.PLUGIN_NAME);
        setSupportedLanguages(Scala.KEY);
        this.ruleFinder = ruleFinder;
    }

    @Override
    public RulesProfile importProfile(Reader findbugsConf, ValidationMessages messages) {
        RulesProfile profile = RulesProfile.create();
        try {
            XStream xStream = FindBugsFilter.createXStream();
            FindBugsFilter filter = (FindBugsFilter) xStream.fromXML(findbugsConf);

            activateRulesByCategory(profile, filter, messages);
            activateRulesByCode(profile, filter, messages);
            activateRulesByPattern(profile, filter, messages);

            return profile;
        } catch (Exception e) {
            String errorMessage = "The Findbugs configuration file is not valid";
            messages.addErrorText(errorMessage + " : " + e.getMessage());
            LOG.error(errorMessage, e);
            return profile;
        }
    }

    private void activateRulesByPattern(RulesProfile profile, FindBugsFilter filter, ValidationMessages messages) {
        for (Map.Entry<String, String> patternLevel : filter.getPatternLevels(new FindbugsLevelUtils()).entrySet()) {
            Rule rule = ruleFinder.findByKey(FindbugsScalaRulesDefinition.REPOSITORY_KEY, patternLevel.getKey());
            if (rule == null) {
                rule = ruleFinder.findByKey(FbContribScalaRulesDefinition.REPOSITORY_KEY, patternLevel.getKey());
                if (rule == null) {
                    rule = ruleFinder.findByKey(FindSecurityBugsScalaRulesDefinition.REPOSITORY_KEY, patternLevel.getKey());

                }
            }
            if (rule != null) {
                profile.activateRule(rule, getPriorityFromSeverity(patternLevel.getValue()));
            } else {
                messages.addWarningText("Unable to activate unknown rule : '" + patternLevel.getKey() + "'");
            }
        }
    }

    private void activateRulesByCode(RulesProfile profile, FindBugsFilter filter, ValidationMessages messages) {
        for (Map.Entry<String, String> codeLevel : filter.getCodeLevels(new FindbugsLevelUtils()).entrySet()) {
            boolean someRulesHaveBeenActivated = false;
            for (Rule rule : rules()) {
                if (rule.getKey().equals(codeLevel.getKey()) || StringUtils.startsWith(rule.getKey(), codeLevel.getKey() + "_")) {
                    someRulesHaveBeenActivated = true;
                    profile.activateRule(rule, getPriorityFromSeverity(codeLevel.getValue()));
                }
            }
            if (!someRulesHaveBeenActivated) {
                messages.addWarningText("Unable to find any rules associated to code  : '" + codeLevel.getKey() + "'");
            }
        }
    }

    private void activateRulesByCategory(RulesProfile profile, FindBugsFilter filter, ValidationMessages messages) {
        for (Map.Entry<String, String> categoryLevel : filter.getCategoryLevels(new FindbugsLevelUtils()).entrySet()) {
            boolean someRulesHaveBeenActivated = false;
            String sonarCateg = FindbugsCategory.findbugsToSonar(categoryLevel.getKey());
            for (Rule rule : rules()) {
                if (sonarCateg != null && rule.getName().startsWith(sonarCateg)) {
                    someRulesHaveBeenActivated = true;
                    profile.activateRule(rule, getPriorityFromSeverity(categoryLevel.getValue()));
                }
            }
            if (!someRulesHaveBeenActivated) {
                messages.addWarningText("Unable to find any rules associated to category  : '" + categoryLevel.getKey() + "'");
            }
        }
    }

    private static RulePriority getPriorityFromSeverity(String severity) {
        if (Severity.INFO.equals(severity)) {
            return RulePriority.INFO;
        } else if (Severity.MAJOR.equals(severity)) {
            return RulePriority.MAJOR;
        } else if (Severity.BLOCKER.equals(severity)) {
            return RulePriority.BLOCKER;
        }
        return null;
    }

    private Iterable<Rule> rules() {
        return Iterables.concat(
                ruleFinder.findAll(RuleQuery.create().withRepositoryKey(FindbugsScalaRulesDefinition.REPOSITORY_KEY)),
                ruleFinder.findAll(RuleQuery.create().withRepositoryKey(FbContribScalaRulesDefinition.REPOSITORY_KEY)),
                ruleFinder.findAll(RuleQuery.create().withRepositoryKey(FindSecurityBugsScalaRulesDefinition.REPOSITORY_KEY))
        );
    }

}
